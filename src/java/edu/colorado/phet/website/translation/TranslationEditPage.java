/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.translation;

import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.authorization.AuthorizationException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.html.pages.AccessDeniedPage;
import org.apache.wicket.model.Model;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.colorado.phet.common.phetcommon.util.LocaleUtils;
import edu.colorado.phet.website.content.IndexPage;
import edu.colorado.phet.website.data.Translation;
import edu.colorado.phet.website.panels.PanelHolder;
import edu.colorado.phet.website.translation.entities.TranslationEntity;
import edu.colorado.phet.website.util.PageContext;

public class TranslationEditPage extends TranslationPage {
    private int translationId;
    private PanelHolder panelHolder;
    private TranslateEntityPanel subPanel;
    private TranslationEntityListPanel entityListPanel;
    private Locale testLocale;
    private String selectedEntityName = null;
    private TranslationEntity selectedEntity;

    private boolean showUntranslated = true;
    private boolean showOutOfDate = true;
    private boolean showUpToDate = true;

    public static final String TRANSLATION_ID = "translationId";
    public static final String TRANSLATION_LOCALE = "translationLocale";
    private static final Logger logger = Logger.getLogger( TranslationEditPage.class.getName() );

    public TranslationEditPage( PageParameters parameters ) {
        super( parameters );

        testLocale = LocaleUtils.stringToLocale( parameters.getString( TRANSLATION_LOCALE ) );
        translationId = parameters.getInt( TRANSLATION_ID );

        Session session = getHibernateSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Translation translation = (Translation) session.load( Translation.class, translationId );

            add( new Label( "translation", translation.toString() ) );

            if ( !translation.allowEdit( getUser() ) ) {
                throw new AuthorizationException( "You are not authorized to edit this translation" ) {
                };
            }

            tx.commit();
        }
        catch( AuthorizationException e ) {
            setResponsePage( AccessDeniedPage.class );
        }
        catch( RuntimeException e ) {
            logger.warn( e );
            if ( tx != null && tx.isActive() ) {
                try {
                    tx.rollback();
                }
                catch( HibernateException e1 ) {
                    logger.error( "ERROR: Error rolling back transaction", e1 );
                }
                throw e;
            }
            throw e;
        }

        final PageContext externalContext = new PageContext( "/translation/" + String.valueOf( translationId ) + "/", "", testLocale );
        Link popupLink = IndexPage.getLinker().getLink( "translation-popup", externalContext, getPhetCycle() );
        popupLink.setPopupSettings( new PopupSettings( PopupSettings.LOCATION_BAR | PopupSettings.MENU_BAR | PopupSettings.RESIZABLE
                                                       | PopupSettings.SCROLLBARS | PopupSettings.STATUS_BAR | PopupSettings.TOOL_BAR ) );
        add( popupLink );

        panelHolder = new PanelHolder( "translation-panel", getPageContext() );
        add( panelHolder );
        setSelectedEntity( TranslationEntity.getTranslationEntities().get( 0 ) );
        updateEntityPanel();

        entityListPanel = new TranslationEntityListPanel( "entity-list-panel", getPageContext(), this );
        add( entityListPanel );

        add( new TranslationUserPanel( "user-panel", getPageContext(), translationId ) );

        add( new StringFilterForm( "string-filter-form" ) );
    }

    /**
     * Attaches a new entity panel to the panel holder using the current data.
     */
    public void updateEntityPanel() {
        if ( subPanel != null ) {
            panelHolder.remove( subPanel );
        }
        subPanel = new TranslateEntityPanel( panelHolder.getWicketId(), getPageContext(), this, selectedEntity, translationId, testLocale );
        panelHolder.add( subPanel );
    }

    /**
     * Updates the entity panel with a new instance, and adds it to the current ajax request target
     *
     * @param target Ajax request target
     */
    public void updateEntityPanel( AjaxRequestTarget target ) {
        updateEntityPanel();
        target.addComponent( panelHolder );
    }

    public int getTranslationId() {
        return translationId;
    }

    public PanelHolder getPanelHolder() {
        return panelHolder;
    }

    public TranslateEntityPanel getSubPanel() {
        return subPanel;
    }

    public Locale getTestLocale() {
        return testLocale;
    }

    public String getSelectedEntityName() {
        return selectedEntityName;
    }

    public void setSelectedEntity( TranslationEntity entity ) {
        this.selectedEntityName = entity.getDisplayName();
        this.selectedEntity = entity;
    }

    public boolean isShowUntranslated() {
        return showUntranslated;
    }

    public boolean isShowOutOfDate() {
        return showOutOfDate;
    }

    public boolean isShowUpToDate() {
        return showUpToDate;
    }

    public TranslationEntityListPanel getEntityListPanel() {
        return entityListPanel;
    }

    public class StringFilterForm extends Form {

        private AjaxCheckBox untranslatedBox;
        private AjaxCheckBox outOfDateBox;
        private AjaxCheckBox upToDateBox;

        public StringFilterForm( String id ) {
            super( id );

            add( untranslatedBox = new AjaxCheckBox( "untranslated-check", new Model<Boolean>( showUntranslated ) ) {
                @Override
                protected void onUpdate( AjaxRequestTarget target ) {
                    showUntranslated = untranslatedBox.getModelObject();
                    target.addComponent( panelHolder );
                }
            } );
            add( outOfDateBox = new AjaxCheckBox( "out-of-date-check", new Model<Boolean>( showOutOfDate ) ) {
                @Override
                protected void onUpdate( AjaxRequestTarget target ) {
                    showOutOfDate = outOfDateBox.getModelObject();
                    target.addComponent( panelHolder );
                }
            } );
            add( upToDateBox = new AjaxCheckBox( "up-to-date-check", new Model<Boolean>( showUpToDate ) ) {
                @Override
                protected void onUpdate( AjaxRequestTarget target ) {
                    showUpToDate = upToDateBox.getModelObject();
                    target.addComponent( panelHolder );
                }
            } );
        }
    }
}
