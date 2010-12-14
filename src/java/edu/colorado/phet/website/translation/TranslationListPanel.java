package edu.colorado.phet.website.translation;

import java.util.*;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.hibernate.Session;

import edu.colorado.phet.common.phetcommon.util.LocaleUtils;
import edu.colorado.phet.common.phetcommon.util.PhetLocales;
import edu.colorado.phet.website.PhetWicketApplication;
import edu.colorado.phet.website.authentication.PhetSession;
import edu.colorado.phet.website.components.InvisibleComponent;
import edu.colorado.phet.website.content.IndexPage;
import edu.colorado.phet.website.data.PhetUser;
import edu.colorado.phet.website.data.Translation;
import edu.colorado.phet.website.notification.NotificationHandler;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.PhetRequestCycle;
import edu.colorado.phet.website.util.WicketUtils;
import edu.colorado.phet.website.util.hibernate.HibernateTask;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;

public class TranslationListPanel extends PhetPanel {

    private PhetLocales phetLocales;

    private static final Logger logger = Logger.getLogger( TranslationListPanel.class.getName() );
    private List<Translation> translations;

    public TranslationListPanel( String id, PageContext context, final List<Translation> translations ) {
        super( id, context );
        this.translations = translations;

        final Map<Translation, Integer> sizes = new HashMap<Translation, Integer>();

        phetLocales = ( (PhetWicketApplication) getApplication() ).getSupportedLocales();

        HibernateUtils.wrapTransaction( getHibernateSession(), new HibernateTask() {
            public boolean run( Session session ) {
                for ( Translation translation : translations ) {
                    sizes.put( translation, ( (Long) session.createQuery( "select count(*) from TranslatedString as ts where ts.translation = :translation" )
                            .setEntity( "translation", translation ).iterate().next() ).intValue() );
                }
                return true;
            }
        } );

        if ( translations.isEmpty() ) {
            setVisible( false );
        }

        add( new TranslationListView( "translation-list", sizes ) );

    }

    public List<Translation> getTranslations() {
        return translations;
    }

    private class TranslationListView extends ListView<Translation> {
        private final Map<Translation, Integer> sizes;

        public TranslationListView( String id, Map<Translation, Integer> sizes ) {
            super( id, translations );
            this.sizes = sizes;
        }

        protected void populateItem( ListItem<Translation> item ) {
            final Translation translation = item.getModelObject();
            final PhetUser user = PhetSession.get().getUser();

            // whether this is the main English translation
            boolean isDefaultEnglish = translation.isVisible() && translation.getLocale().equals( PhetWicketApplication.getDefaultLocale() );

            boolean visibleToggleShown = translation.allowToggleVisibility( user );
            boolean editShown = translation.allowEdit( user );
            boolean submitShown = translation.allowSubmit( user );
            boolean deleteShown = translation.allowDelete( user );
            boolean requestShown = translation.allowRequestToCollaborate( user );

            item.add( new Label( "id", String.valueOf( translation.getId() ) ) );
            item.add( new Label( "locale", phetLocales.getName( translation.getLocale() ) + " (" + LocaleUtils.localeToString( translation.getLocale() ) + ")" ) );
            item.add( new Label( "num-strings", String.valueOf( sizes.get( translation ) ) ) );
            Label visibleLabel = new Label( "visible-label", String.valueOf( translation.isVisible() ? "visible" : "hidden" ) );
            if ( translation.isVisible() ) {
                visibleLabel.add( new AttributeAppender( "class", true, new Model<String>( "translation-visible" ), " " ) );
            }
            item.add( visibleLabel );

            // TODO: add visibility switcher component
            if ( visibleToggleShown ) {
                item.add( new Link( "visible-toggle" ) {
                    public void onClick() {
                        toggleVisibility( translation );
                    }
                } );
            }
            else {
                item.add( new InvisibleComponent( "visible-toggle" ) );
            }

            PageContext newContext;
            String type;
            if ( translation.isVisible() ) {
                type = "view";
                newContext = new PageContext( PageContext.getStandardPrefix( translation.getLocale() ), "", translation.getLocale() );
            }
            else {
                type = "preview";
                newContext = new PageContext( PageContext.getTranslationPrefix( translation.getId() ), "", translation.getLocale() );
            }

            // make a popuplink class
            Link popupLink = IndexPage.getLinker().getLink( "preview", newContext, getPhetCycle() );
            popupLink.setPopupSettings( new PopupSettings( PopupSettings.LOCATION_BAR | PopupSettings.MENU_BAR | PopupSettings.RESIZABLE
                                                           | PopupSettings.SCROLLBARS | PopupSettings.STATUS_BAR | PopupSettings.TOOL_BAR ) );
            popupLink.add( new Label( "type", type ) );
            item.add( popupLink );

            if ( editShown ) {
                item.add( new Link( "edit" ) {
                    public void onClick() {
                        PageParameters params = new PageParameters();
                        params.put( TranslationEditPage.TRANSLATION_ID, translation.getId() );
                        params.put( TranslationEditPage.TRANSLATION_LOCALE, LocaleUtils.localeToString( translation.getLocale() ) );
                        setResponsePage( TranslationEditPage.class, params );
                    }
                } );
            }
            else {
                item.add( new InvisibleComponent( "edit" ) );
            }

            if ( user.isTeamMember() || translation.isUserAuthorized( user ) ) {
                Label lockLabel = new Label( "locked-label", String.valueOf( translation.isLocked() ? "locked" : "editable" ) );
                if ( translation.isLocked() ) {
                    lockLabel.add( new AttributeAppender( "class", true, new Model<String>( "translation-locked" ), " " ) );
                }
                item.add( lockLabel );
            }
            else {
                item.add( new InvisibleComponent( "locked-label" ) );
            }

            if ( translation.allowToggleLocking( user ) ) {
                item.add( new Link( "lock-toggle" ) {
                    @Override
                    public void onClick() {
                        toggleLock( translation );
                    }
                } );
            }
            else {
                item.add( new InvisibleComponent( "lock-toggle" ) );
            }

            if ( !submitShown ) {
                item.add( new InvisibleComponent( "submit-link" ) );
            }
            else {
                item.add( new Link( "submit-link" ) {
                    @Override
                    public void onClick() {
                        boolean success = HibernateUtils.wrapTransaction( getHibernateSession(), new HibernateTask() {
                            public boolean run( Session session ) {
                                Translation t = (Translation) session.load( Translation.class, translation.getId() );

                                List<PhetUser> users = new LinkedList<PhetUser>();
                                for ( Object u : t.getAuthorizedUsers() ) {
                                    users.add( (PhetUser) u );
                                }

                                return NotificationHandler.sendTranslationSubmittedNotification( translation.getId(), t.getLocale(), users );
                            }
                        } );

                        if ( success ) {
                            setResponsePage( TranslationSubmittedPage.class );
                        }
                    }
                } );
            }

            if ( deleteShown ) {
                item.add( new Link( "delete" ) {
                    public void onClick() {
                        delete( translation );
                    }
                } );
            }
            else {
                item.add( new InvisibleComponent( "delete" ) );
            }

            if ( requestShown ) {
                item.add( new Link( "request-to-collaborate" ) {
                    @Override
                    public void onClick() {
                        boolean success = HibernateUtils.wrapTransaction( getHibernateSession(), new HibernateTask() {
                            public boolean run( Session session ) {
                                Translation t = (Translation) session.load( Translation.class, translation.getId() );

                                List<PhetUser> users = new LinkedList<PhetUser>();
                                for ( Object u : t.getAuthorizedUsers() ) {
                                    users.add( (PhetUser) u );
                                }

                                PhetUser currentUser = PhetSession.get().getUser();

                                return NotificationHandler.sendTranslationRequestForCollaboration( translation.getId(), t.getLocale(), users, currentUser );
                            }
                        } );

                        if ( success ) {
                            setResponsePage( CollaborationRequestSubmittedPage.class );
                        }
                    }
                } );
            }
            else {
                item.add( new InvisibleComponent( "request-to-collaborate" ) );
            }

            WicketUtils.highlightListItem( item );
        }

        public void toggleLock( final Translation translation ) {
            final boolean[] ret = new boolean[1];
            boolean success = HibernateUtils.wrapTransaction( getHibernateSession(), new HibernateTask() {
                public boolean run( Session session ) {
                    Translation tr = (Translation) session.load( Translation.class, translation.getId() );

                    tr.setLocked( !tr.isLocked() );
                    ret[0] = tr.isLocked();
                    session.update( tr );

                    return true;
                }
            } );

            if ( success ) {
                translation.setLocked( ret[0] );
            }
        }

        public void toggleVisibility( final Translation translation ) {
            final boolean[] ret = new boolean[1];
            boolean success = HibernateUtils.wrapTransaction( getHibernateSession(), new HibernateTask() {
                public boolean run( Session session ) {
                    Translation tr = (Translation) session.load( Translation.class, translation.getId() );
                    List otherTranslations = session.createQuery( "select t from Translation as t where t.visible = true and t.locale = :locale" ).setLocale( "locale", translation.getLocale() ).list();

                    if ( !tr.isVisible() && !otherTranslations.isEmpty() ) {
                        throw new RuntimeException( "There is already a visible translation of that locale" );
                    }

                    tr.setVisible( !tr.isVisible() );
                    ret[0] = tr.isVisible();
                    session.update( tr );

                    return true;
                }
            } );
            if ( success ) {
                translation.setVisible( ret[0] );
                if ( translation.isVisible() ) {
                    ( (PhetWicketApplication) getApplication() ).addTranslation( translation );
                }
                else {
                    ( (PhetWicketApplication) getApplication() ).removeTranslation( translation );
                }
            }
        }

        public void delete( final Translation translation ) {
            final List<PhetUser> users = new LinkedList<PhetUser>();
            final Locale locale = translation.getLocale();
            final int id = translation.getId();
            final boolean success = HibernateUtils.wrapTransaction( getHibernateSession(), new HibernateTask() {
                public boolean run( Session session ) {
                    session.load( translation, translation.getId() );

                    logger.info( "attempting to delete translation #" + translation.getId() + " from user " + PhetSession.get().getUser().getEmail() + " and IP " + PhetRequestCycle.get().getHttpServletRequest().getRemoteHost() );

                    translations.remove( translation );
                    for ( Object o : translation.getTranslatedStrings() ) {
                        session.delete( o );
                    }
                    for ( Object o : translation.getAuthorizedUsers() ) {
                        PhetUser user = (PhetUser) o;
                        users.add( user );
                        user.getTranslations().remove( translation );
                        session.update( user );
                    }
                    session.delete( translation );
                    return true;
                }
            } );
            if ( success ) {
                NotificationHandler.sendTranslationDeletedNotification( id, locale, users );
            }
        }
    }

}
