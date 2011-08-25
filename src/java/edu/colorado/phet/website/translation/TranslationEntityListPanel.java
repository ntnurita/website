/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.translation;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.hibernate.Session;

import edu.colorado.phet.website.components.InvisibleComponent;
import edu.colorado.phet.website.constants.WebsiteConstants;
import edu.colorado.phet.website.data.TranslatedString;
import edu.colorado.phet.website.data.Translation;
import edu.colorado.phet.website.panels.PhetPanel;
import edu.colorado.phet.website.translation.entities.TranslationEntity;
import edu.colorado.phet.website.util.ClassAppender;
import edu.colorado.phet.website.util.PageContext;
import edu.colorado.phet.website.util.hibernate.HibernateTask;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;

public class TranslationEntityListPanel extends PhetPanel {

    private int translationId;

    private TranslationEditPage page;

    private static final Logger logger = Logger.getLogger( TranslationEntityListPanel.class.getName() );

    public TranslationEntityListPanel( String id, PageContext context, final TranslationEditPage page ) {
        super( id, context );

        this.page = page;

        translationId = page.getTranslationId();

        initializeEntities();

        setDefaultModel( new StringMapModel( page.getTranslationId() ) );

        setOutputMarkupId( true );

        List<TranslationEntity> translationEntities = TranslationEntity.getTranslationEntitiesForLocale( page.getTestLocale() );
        ListView entities = new ListView<TranslationEntity>( "translation-entities", translationEntities ) {
            private AttributeAppender appender = new ClassAppender( "selected" );
            private ListItem selected = null;

            protected void populateItem( final ListItem<TranslationEntity> item ) {
                final TranslationEntity entity = item.getModelObject();

                AjaxLink link = new AjaxLink( "translation-entity-link" ) {
                    public void onClick( AjaxRequestTarget target ) {
                        page.setSelectedEntity( entity );
                        page.updateEntityPanel( target ); // adds necessary component to target
                        target.addComponent( TranslationEntityListPanel.this );
                    }
                };
                if ( page.getSelectedEntityName().equals( entity.getDisplayName() ) ) {
                    if ( selected != null ) {
                        selected.remove( appender );
                        selected = null;
                    }
                    item.add( appender );
                    selected = item;
                }
                link.add( new Label( "translation-entity-display-name", entity.getDisplayName() ) );

                int numOutOfDate = entity.getOutOfDateMap().get( translationId );
                int numUntranslated = entity.getUntranslatedMap().get( translationId );

                if ( numOutOfDate == 0 ) {
                    link.add( new InvisibleComponent( "out-of-date" ) );
                }
                else {
                    link.add( new Label( "out-of-date", "(" + String.valueOf( numOutOfDate ) + ")" ) );
                }
                if ( numUntranslated == 0 ) {
                    link.add( new InvisibleComponent( "untranslated" ) );
                }
                else {
                    link.add( new Label( "untranslated", "(" + String.valueOf( numUntranslated ) + ")" ) );
                }
                item.add( link );
            }
        };
        add( entities );
    }

    public void updateEntity( final TranslationEntity entity ) {
        HibernateUtils.wrapTransaction( getHibernateSession(), new HibernateTask() {
            public boolean run( Session session ) {
                Translation english = (Translation) session.createQuery( "select t from Translation as t where t.visible = true and t.locale = :locale" )
                        .setLocale( "locale", WebsiteConstants.ENGLISH ).uniqueResult();
                Translation other = (Translation) session.load( Translation.class, translationId );

                int untranslated = 0;
                int outOfDate = 0;
                for ( TranslationEntityString string : entity.getStrings() ) {
                    List results = session.createQuery( "select ts from TranslatedString as ts where ts.translation.id = :tid and ts.key = :key" )
                            .setInteger( "tid", other.getId() ).setString( "key", string.getKey() ).list();
                    if ( results.isEmpty() ) {
                        untranslated++;
                        continue;
                    }
                    TranslatedString a = (TranslatedString) results.get( 0 );
                    results = session.createQuery( "select ts from TranslatedString as ts where ts.translation.id = :tid and ts.key = :key" )
                            .setInteger( "tid", english.getId() ).setString( "key", string.getKey() ).list();
                    if ( results.isEmpty() ) {
                        continue;
                    }
                    TranslatedString e = (TranslatedString) results.get( 0 );
                    if ( a.getUpdatedAt().compareTo( e.getUpdatedAt() ) < 0 ) {
                        outOfDate++;
                    }
                }

                entity.getUntranslatedMap().put( translationId, untranslated );
                entity.getOutOfDateMap().put( translationId, outOfDate );

                return true;
            }
        } );
    }

    private void initializeEntities() {
        logger.debug( "initializing entries for translation id " + translationId );
        final List<TranslationEntity> entities = TranslationEntity.getTranslationEntitiesForLocale( page.getTestLocale() );

        HibernateUtils.wrapTransaction( getHibernateSession(), new HibernateTask() {
            public boolean run( Session session ) {
                Translation english = (Translation) session.createQuery( "select t from Translation as t where t.visible = true and t.locale = :locale" )
                        .setLocale( "locale", WebsiteConstants.ENGLISH ).uniqueResult();
                Translation other = (Translation) session.load( Translation.class, translationId );

                Set englishStrings = english.getTranslatedStrings();
                Set otherStrings = other.getTranslatedStrings();

                Map<String, TranslatedString> englishMap = new HashMap<String, TranslatedString>();
                Map<String, TranslatedString> otherMap = new HashMap<String, TranslatedString>();

                for ( Object string : englishStrings ) {
                    englishMap.put( ( (TranslatedString) string ).getKey(), ( (TranslatedString) string ) );
                }

                for ( Object string : otherStrings ) {
                    otherMap.put( ( (TranslatedString) string ).getKey(), ( (TranslatedString) string ) );
                }

                for ( TranslationEntity entity : entities ) {
                    int untranslated = 0;
                    int outOfDate = 0;
                    for ( TranslationEntityString string : entity.getStrings() ) {
                        TranslatedString a = otherMap.get( string.getKey() );
                        if ( a == null ) {
                            untranslated++;
                            continue;
                        }
                        TranslatedString e = englishMap.get( string.getKey() );
                        if ( e == null ) {
                            continue;
                        }
                        if ( a.getUpdatedAt().compareTo( e.getUpdatedAt() ) < 0 ) {
                            outOfDate++;
                        }
                    }
                    entity.getUntranslatedMap().put( translationId, untranslated );
                    entity.getOutOfDateMap().put( translationId, outOfDate );
                    logger.debug( entity.getDisplayName() + " untranslated: " + untranslated );
                    logger.debug( entity.getDisplayName() + " outOfDate: " + outOfDate );
                }
                return true;
            }
        } );
    }

    private class StringMapModel extends LoadableDetachableModel {
        private int translationId;

        private StringMapModel( int translationId ) {
            this.translationId = translationId;
        }

        protected Object load() {
            final Map<String, StringDat> map = new HashMap<String, StringDat>();
            HibernateUtils.wrapTransaction( getHibernateSession(), new HibernateTask() {
                public boolean run( Session session ) {
                    Translation localTranslation = (Translation) session.load( Translation.class, translationId );
                    Translation englishTranslation = (Translation) session.createQuery( "select t from Translation as t where t.visible = true and t.locale = :locale" ).setLocale( "locale", WebsiteConstants.ENGLISH ).uniqueResult();
                    for ( Object o : localTranslation.getTranslatedStrings() ) {
                        TranslatedString string = (TranslatedString) o;
                        map.put( string.getKey(), new StringDat( string.getKey(), string.getUpdatedAt() ) );
                    }
                    for ( Object o : englishTranslation.getTranslatedStrings() ) {
                        TranslatedString string = (TranslatedString) o;
                        StringDat dat = map.get( string.getKey() );
                        if ( dat != null ) {
                            dat.setEnglishDate( string.getUpdatedAt() );
                        }
                    }
                    return true;
                }
            } );
            return map;
        }
    }

    private static class StringDat implements Serializable {
        private String key;
        private Date englishDate;
        private Date localDate;

        private StringDat( String key, Date localDate ) {
            this.key = key;
            this.localDate = localDate;
        }

        public String getKey() {
            return key;
        }

        public void setKey( String key ) {
            this.key = key;
        }

        public Date getEnglishDate() {
            return englishDate;
        }

        public void setEnglishDate( Date englishDate ) {
            this.englishDate = englishDate;
        }

        public Date getLocalDate() {
            return localDate;
        }

        public void setLocalDate( Date localDate ) {
            this.localDate = localDate;
        }
    }
}
