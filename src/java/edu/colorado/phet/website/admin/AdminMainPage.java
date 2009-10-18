package edu.colorado.phet.website.admin;

import java.text.MessageFormat;
import java.util.*;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.value.ValueMap;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.colorado.phet.common.phetcommon.util.LocaleUtils;
import edu.colorado.phet.website.data.LocalizedSimulation;
import edu.colorado.phet.website.data.Simulation;
import edu.colorado.phet.website.data.TranslatedString;
import edu.colorado.phet.website.data.Translation;
import edu.colorado.phet.website.translation.TranslationEntityString;
import edu.colorado.phet.website.translation.entities.TranslationEntity;
import edu.colorado.phet.website.util.HibernateTask;
import edu.colorado.phet.website.util.HibernateUtils;
import edu.colorado.phet.website.util.StringUtils;

// TODO: move the simulation list panel to a separate page, so if something goes bad, this page is still accessible

// TODO: add strings checks to make sure all translatable strings are contained in entities
public class AdminMainPage extends AdminPage {

    private TextField keyText;
    private TextField valueText;

    public AdminMainPage( PageParameters parameters ) {
        super( parameters );

        Session session = getHibernateSession();
        List<Simulation> simulations = new LinkedList();
        final Map<Simulation, LocalizedSimulation> englishSims = new HashMap<Simulation, LocalizedSimulation>();

        Locale english = LocaleUtils.stringToLocale( "en" );

        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            List sims = session.createQuery( "select s from Simulation as s" ).list();

            for ( Object sim : sims ) {
                Simulation simulation = (Simulation) sim;
                simulations.add( simulation );
                for ( Object o : simulation.getLocalizedSimulations() ) {
                    LocalizedSimulation lsim = (LocalizedSimulation) o;
                    if ( lsim.getLocale().equals( english ) ) {
                        englishSims.put( simulation, lsim );
                        break;
                    }
                }
            }

            tx.commit();
        }
        catch( RuntimeException e ) {
            if ( tx != null && tx.isActive() ) {
                try {
                    tx.rollback();
                }
                catch( HibernateException e1 ) {
                    System.out.println( "ERROR: Error rolling back transaction" );
                }
                throw e;
            }
        }

        Collections.sort( simulations, new Comparator<Simulation>() {
            public int compare( Simulation a, Simulation b ) {
                return ( (Simulation) a ).getName().compareTo( ( (Simulation) b ).getName() );
            }
        } );

        ListView simulationList = new ListView( "simulation-list", simulations ) {
            protected void populateItem( ListItem item ) {
                final Simulation simulation = (Simulation) item.getModel().getObject();
                LocalizedSimulation lsim = englishSims.get( simulation );
                Link simLink = new Link( "simulation-link" ) {
                    public void onClick() {
                        PageParameters params = new PageParameters();
                        params.put( "simulationId", simulation.getId() );
                        setResponsePage( AdminSimPage.class, params );
                    }
                };
                simLink.add( new Label( "simulation-name", simulation.getName() ) );
                item.add( simLink );
                Link projectLink = new Link( "project-link" ) {
                    public void onClick() {
                        PageParameters params = new PageParameters();
                        params.put( "projectId", simulation.getProject().getId() );
                        setResponsePage( AdminProjectPage.class, params );
                    }
                };
                projectLink.add( new Label( "project-name", simulation.getProject().getName() ) );
                item.add( projectLink );
                if ( lsim == null ) {
                    item.add( new Label( "simulation-title", "?" ) );
                }
                else {
                    item.add( new Label( "simulation-title", lsim.getTitle() ) );
                }
            }
        };

        add( simulationList );

        add( new SetStringForm( "set-string-form" ) );

        add( new Link( "debug-action" ) {
            public void onClick() {

//                final List<TranslationEntity> entities = TranslationEntity.getTranslationEntities();
//
//                Long a = System.currentTimeMillis();
//
//                HibernateUtils.wrapTransaction( getHibernateSession(), new HibernateTask() {
//                    public boolean run( Session session ) {
//                        Long c = System.currentTimeMillis();
//                        Translation english = (Translation) session.load( Translation.class, 1 );
//                        Translation arabic = (Translation) session.load( Translation.class, 4 );
//
//                        Set englishStrings = english.getTranslatedStrings();
//                        Set arabicStrings = arabic.getTranslatedStrings();
//
//                        Map<String, TranslatedString> englishMap = new HashMap<String, TranslatedString>();
//                        Map<String, TranslatedString> arabicMap = new HashMap<String, TranslatedString>();
//
//                        for ( Object string : englishStrings ) {
//                            englishMap.put( ( (TranslatedString) string ).getKey(), ( (TranslatedString) string ) );
//                        }
//
//                        for ( Object string : arabicStrings ) {
//                            arabicMap.put( ( (TranslatedString) string ).getKey(), ( (TranslatedString) string ) );
//                        }
//
//                        for ( TranslationEntity entity : entities ) {
//                            int total = 0;
//                            int untranslated = 0;
//                            int outOfDate = 0;
//                            for ( TranslationEntityString string : entity.getStrings() ) {
//                                total++;
//                                TranslatedString a = arabicMap.get( string.getKey() );
//                                if ( a == null ) {
//                                    untranslated++;
//                                    continue;
//                                }
//                                TranslatedString e = englishMap.get( string.getKey() );
//                                if ( a.getUpdatedAt().compareTo( e.getUpdatedAt() ) < 0 ) {
//                                    outOfDate++;
//                                }
//                            }
//                            System.out.println( entity.getDisplayName() + ": " + MessageFormat.format( "{0} ({1}, {2})", total, untranslated, outOfDate ) );
//                            break;
//                        }
//                        Long d = System.currentTimeMillis();
//                        System.out.println( "Y: " + ( d - c ) );
//                        return true;
//                    }
//                } );
//
//                Long b = System.currentTimeMillis();
//
//                System.out.println( "XYZ: " + ( b - a ) );

            }
        } );
    }

    public final class SetStringForm extends Form {
        private static final long serialVersionUID = 1L;

        private final ValueMap properties = new ValueMap();

        public SetStringForm( final String id ) {
            super( id );

            add( keyText = new TextField( "key", new PropertyModel( properties, "key" ) ) );
            add( valueText = new TextField( "value", new PropertyModel( properties, "value" ) ) );

            // don't turn <'s and other characters into HTML/XML entities!!!
            valueText.setEscapeModelStrings( false );
        }

        public final void onSubmit() {
            String key = keyText.getModelObjectAsString();
            String value = valueText.getModelObjectAsString();
            System.out.println( "Submitted: " + key + " = " + value );
            StringUtils.setEnglishString( getHibernateSession(), key, value );
        }
    }

}
