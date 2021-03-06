/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.translation;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableMultiLineLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.colorado.phet.website.authentication.PhetSession;
import edu.colorado.phet.website.components.InvisibleComponent;
import edu.colorado.phet.website.components.LocalizedLabel;
import edu.colorado.phet.website.components.RawLabel;
import edu.colorado.phet.website.constants.WebsiteConstants;
import edu.colorado.phet.website.data.TranslatedString;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.test.TestTranslateString;
import edu.colorado.phet.website.translation.entities.TranslationEntity;
import edu.colorado.phet.website.util.attributes.ClassAppender;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.StringUtils;
import edu.colorado.phet.website.util.StringUtils.StringStatus;
import edu.colorado.phet.website.util.hibernate.HibernateTask;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;

public class TranslateEntityPanel extends PhetPanel {

    private TranslationEntity entity;
    private int translationId;
    private PreviewHolder panel;
    private Map<String, IModel> stringModelMap = new HashMap<String, IModel>();
    private TranslationEditPage page;

    private static final Logger logger = Logger.getLogger( TranslateEntityPanel.class.getName() );

    public TranslateEntityPanel( String id, final PageContext context, final TranslationEditPage page, final TranslationEntity entity, final int translationId, final Locale testLocale ) {
        super( id, context );
        this.entity = entity;
        this.translationId = translationId;
        this.page = page;

        // TODO: really start caching stuff or something. throwing TONS of queries at postgresql

        final PageContext externalContext = new PageContext( "/translation/" + String.valueOf( translationId ) + "/", "", testLocale );

        setOutputMarkupId( true );

        panel = new PreviewHolder( "panel", externalContext, entity );
        panel.setOutputMarkupId( true );
        add( panel );

        if ( entity.getMinDisplaySize() <= 225 ) {
            add( new ClassAppender( "preview-small" ) );
        }
        else if ( entity.getMinDisplaySize() <= 525 ) {
            add( new ClassAppender( "preview-medium" ) );
        }

        if ( entity.getDescription() != null ) {
            add( new RawLabel( "entity-description", entity.getDescription() ) );
        }
        else {
            add( new InvisibleComponent( "entity-description" ) );
        }

        ListView stringList = new ListView<TranslationEntityString>( "translation-string-list", entity.getStrings() ) {
            protected void populateItem( final ListItem<TranslationEntityString> item ) {
                final TranslationEntityString tString = item.getModelObject();

                item.setOutputMarkupId( true );
                String markupId = "id_string_" + tString.getKey().replaceAll( "\\.", "_" );
                item.setMarkupId( markupId );

                String initString = getLocalizer().getString( tString.getKey(), TranslateEntityPanel.this );
                final Model<String> model = new Model<String>( StringUtils.mapStringForEditing( initString ) );
                stringModelMap.put( tString.getKey(), model );

                if ( tString.getNotes() == null ) {
                    item.add( new InvisibleComponent( "translation-string-notes" ) );
                }
                else {
                    item.add( new Label( "translation-string-notes", tString.getNotes() ) {{
                        // allow customization of CSS for each string note
                        add( new ClassAppender( "string-note-" + tString.getKey().replace( '.', '-' ) ) );
                    }} );
                }

                item.add( new Label( "translation-string-key", tString.getKey() ) );

                if ( testLocale.equals( WebsiteConstants.ENGLISH ) ) {
                    item.add( new InvisibleComponent( "translation-string-english" ) );
                }
                else {
                    item.add( new LocalizedLabel( "translation-string-english", WebsiteConstants.ENGLISH, new ResourceModel( tString.getKey() ) ) );
                }

                final AjaxEditableMultiLineLabel<String> editableLabel = new AjaxEditableMultiLineLabel<String>( "translation-string-value", model ) {
                    @Override
                    protected void onSubmit( AjaxRequestTarget target ) {
                        super.onSubmit( target );
                        StringStatus status = StringUtils.stringStatus( getHibernateSession(), tString.getKey(), translationId );
                        String value = model.getObject();
                        if ( value == null ) {
                            value = ""; // check in case they put in a blank string
                        }
                        if ( allowStringChange( tString.getKey(), value ) ) {
                            logger.info( "user " + PhetSession.get().getUser().getEmail() + " setting string " + tString.getKey() + " on translation #" + translationId );
                            StringUtils.setString( getHibernateSession(), tString.getKey(), value, translationId );
                            if ( status == StringStatus.OUT_OF_DATE ) {
                                Map<Integer, Integer> map = entity.getOutOfDateMap();
                                Integer old = map.get( translationId );
                                map.put( translationId, old - 1 );
                            }
                            else if ( status == StringStatus.UNTRANSLATED ) {
                                Map<Integer, Integer> map = entity.getUntranslatedMap();
                                Integer old = map.get( translationId );
                                map.put( translationId, old - 1 );
                            }
                            add( new AttributeModifier( "class", new Model<String>( "string-value" ) ) );
                        }
                        else {
                            // mostly preventative measure to prevent attacks
                            String oldValue = StringUtils.getStringDirect( getHibernateSession(), tString.getKey(), translationId );
                            if ( oldValue == null ) {
                                oldValue = StringUtils.getEnglishStringDirect( getHibernateSession(), tString.getKey() );
                            }
                            model.setObject( StringUtils.mapStringForEditing( oldValue ) );
                        }
                        target.addComponent( panel );
                        target.addComponent( item );
                        target.addComponent( page.getEntityListPanel() );
                    }
                };
                editableLabel.setCols( 80 );
                // TODO: to increase performance, we could batch-request ALL the strings for the translation, then process
                if ( !isStringSet( tString.getKey() ) ) {
                    editableLabel.add( new ClassAppender( "not-translated" ) );
                    item.setVisible( page.isShowUntranslated() );
                }
                else if ( !isStringUpToDate( tString.getKey() ) ) {
                    editableLabel.add( new ClassAppender( "string-out-of-date" ) );
                    item.setVisible( page.isShowOutOfDate() );
                }
                else {
                    // up to date
                    item.setVisible( page.isShowUpToDate() );
                }
                item.add( editableLabel );

                if ( ( (PhetSession) getSession() ).getUser().isTeamMember() ) {
                    //item.add( new InvisibleComponent( "translate-auto" ) );

                    item.add( new AjaxLink( "translate-auto" ) {
                        public void onClick( AjaxRequestTarget target ) {
                            String value = TestTranslateString.translate( model.getObject(), "en", testLocale.getLanguage() );
                            StringStatus status = StringUtils.stringStatus( getHibernateSession(), tString.getKey(), translationId );
                            if ( value != null ) {
                                StringUtils.setString( getHibernateSession(), tString.getKey(), value, translationId );
                            }
                            target.addComponent( panel );
                            target.addComponent( item );
                            // TODO: consolidate with above functions
                            //page.getEntityListPanel().updateEntity( entity );
                            if ( status == StringStatus.OUT_OF_DATE ) {
                                Map<Integer, Integer> map = entity.getOutOfDateMap();
                                Integer old = map.get( translationId );
                                map.put( translationId, old - 1 );
                            }
                            else if ( status == StringStatus.UNTRANSLATED ) {
                                Map<Integer, Integer> map = entity.getUntranslatedMap();
                                Integer old = map.get( translationId );
                                map.put( translationId, old - 1 );
                            }
                            target.addComponent( page.getEntityListPanel() );
                            editableLabel.add( new AttributeModifier( "class", new Model<String>( "string-value" ) ) );
                            model.setObject( value );
                        }
                    } );

                }
                else {
                    item.add( new InvisibleComponent( "translate-auto" ) );
                }
            }
        };
        add( stringList );
    }

    public static boolean allowStringChange( String key, String value ) {
        if ( stringHasXSS( value ) ) {
            return false;
        }
        if ( key.equals( "language.dir" ) && !value.equals( "ltr" ) && !value.equals( "rtl" ) ) {
            return false;
        }
        return true;
    }

    public boolean isStringUpToDate( final String key ) {
        boolean success = HibernateUtils.wrapTransaction( getHibernateSession(), new HibernateTask() {
            public boolean run( Session session ) {
                TranslatedString standard = (TranslatedString) session.createQuery( "select ts from TranslatedString as ts, Translation as t where ts.translation = t and t.visible = true and t.locale = :locale and ts.key = :key" )
                        .setLocale( "locale", WebsiteConstants.ENGLISH )
                        .setString( "key", key )
                        .uniqueResult();
                TranslatedString current = (TranslatedString) session.createQuery( "select ts from TranslatedString as ts, Translation as t where ts.translation = t and t.id = :id and ts.key = :key" )
                        .setInteger( "id", translationId )
                        .setString( "key", key )
                        .uniqueResult();
                return current.getUpdatedAt().compareTo( standard.getUpdatedAt() ) >= 0;
            }
        } );
        return success;
    }

    private static final String[] allowedTags = new String[] { "p", "strong", "em", "br", "ul", "ol", "li", "a", "span", "table", "tbody", "thead", "tr", "td" };
    private static final String[] blacklistedStrings = new String[] { "<scr" + "ipt", "<SC" + "RIPT", "<fo" + "rm", "<FO" + "RM", "expres" + "sion(", "docu" + "ment.coo" + "kie" };

    public static boolean stringHasXSS( String str ) {
        for ( String blacklistedString : blacklistedStrings ) {
            if ( str.contains( blacklistedString ) ) {
                return true;
            }
        }
        return false;
    }

    public boolean isStringSet( String key ) {
        Session session = getHibernateSession();
        Transaction tx = null;
        List results = null;
        try {
            tx = session.beginTransaction();

            Query query = session.createQuery( "select ts from TranslatedString as ts, Translation as t where (t.id = :id AND ts.translation = t AND ts.key = :key)" );
            query.setInteger( "id", translationId );
            query.setString( "key", key );
            results = query.list();

            tx.commit();
        }
        catch ( RuntimeException e ) {
            logger.warn( "Exception: " + e );
            if ( tx != null && tx.isActive() ) {
                try {
                    tx.rollback();
                }
                catch ( HibernateException e1 ) {
                    logger.error( "ERROR: Error rolling back transaction", e1 );
                }
                throw e;
            }
        }

        if ( results == null || results.isEmpty() ) {
            return false;
        }
        return true;
    }

    @Override
    public String getVariation() {
        return Integer.toString( translationId );
    }

    private Long renderStart;

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        renderStart = System.currentTimeMillis();
    }

    @Override
    protected void onAfterRender() {
        super.onAfterRender();
        logger.debug( "TranslateEntityPanel Render: " + ( System.currentTimeMillis() - renderStart ) + " ms" );
    }

}
