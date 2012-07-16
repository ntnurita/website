/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.admin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.value.ValueMap;
import org.hibernate.Session;

import edu.colorado.phet.buildtools.BuildLocalProperties;
import edu.colorado.phet.buildtools.util.PhetJarSigner;
import edu.colorado.phet.common.phetcommon.util.FileUtils;
import edu.colorado.phet.website.PhetWicketApplication;
import edu.colorado.phet.website.components.RawLabel;
import edu.colorado.phet.website.components.StringTextField;
import edu.colorado.phet.website.constants.WebsiteConstants;
import edu.colorado.phet.website.data.PhetUser;
import edu.colorado.phet.website.data.Simulation;
import edu.colorado.phet.website.data.TranslatedString;
import edu.colorado.phet.website.metadata.LearningRegistryUtils;
import edu.colorado.phet.website.metadata.MetadataUtils;
import edu.colorado.phet.website.metadata.SimulationRecord;
import edu.colorado.phet.website.metadata.formats.NSDLDCConverter;
import edu.colorado.phet.website.newsletter.NewsletterSender;
import edu.colorado.phet.website.panels.faq.FAQPanel;
import edu.colorado.phet.website.translation.PhetLocalizer;
import edu.colorado.phet.website.util.EmailUtils;
import edu.colorado.phet.website.util.ImageUtils;
import edu.colorado.phet.website.util.PDFUtils;
import edu.colorado.phet.website.util.StringUtils;
import edu.colorado.phet.website.util.hibernate.HibernateTask;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;
import edu.colorado.phet.website.util.hibernate.VoidTask;

public class AdminMainPage extends AdminPage {

    private TextField keyText;
    private TextField valueText;

    private String logTest = "";

    private static final Logger logger = Logger.getLogger( AdminMainPage.class.getName() );

    public AdminMainPage( PageParameters parameters ) {
        super( parameters );

        add( new SetStringForm( "set-string-form" ) );

        add( new Link( "debug-email" ) {
            public void onClick() {
                try {
                    logger.info( "debugging email" );
                    EmailUtils.GeneralEmailBuilder message = new EmailUtils.GeneralEmailBuilder( "Test Email \u8FD9\u4E2A\u7537\u5B69\u5B50\u6CA1\u6709\u53EA\u72D7", WebsiteConstants.PHET_NO_REPLY_EMAIL_ADDRESS );
                    message.setBody( "This is the body. Here is some unicode: \u4E00\u4E2A\u82F9\u679C\u7EA2\u8272\u7684" );
                    message.addRecipient( "olsonsjc@gmail.com" );
                    message.addReplyTo( "phethelp@colorado.edu" );
                    EmailUtils.sendMessage( message );
                }
                catch( MessagingException e ) {
                    e.printStackTrace();
                }
            }
        } );

        add( new Link( "debug-error" ) {
            public void onClick() {
                throw new RuntimeException( "test" );
            }
        } );

        add( new Link( "debug-sign" ) {
            public void onClick() {
                File tmpDir = new File( System.getProperty( "java.io.tmpdir" ) );
                try {
                    File source = new File( PhetWicketApplication.get().getSimulationsRoot(), "test-project/sim2_zh_TW.jar" );
                    FileUtils.copyToDir( source, tmpDir );
                    File jarFile = new File( tmpDir, "sim2_zh_TW.jar" );
                    ( new PhetJarSigner( BuildLocalProperties.getInstance() ) ).signJar( jarFile );
                }
                catch( IOException e ) {
                    e.printStackTrace();
                }
            }
        } );

        add( new Link( "debug-pack" ) {
            public void onClick() {
                File tmpDir = new File( System.getProperty( "java.io.tmpdir" ) );
                try {
                    File source = new File( PhetWicketApplication.get().getSimulationsRoot(), "test-project/sim2_zh_TW.jar" );
                    FileUtils.copyToDir( source, tmpDir );
                    File jarFile = new File( tmpDir, "sim2_zh_TW.jar" );
                    ( new PhetJarSigner( BuildLocalProperties.getInstance() ) ).packAndSignJar( jarFile );
                }
                catch( IOException e ) {
                    e.printStackTrace();
                }
            }
        } );

        add( new Link( "debug-ticks" ) {
            public void onClick() {
                HibernateUtils.wrapTransaction( getHibernateSession(), new HibernateTask() {
                    public boolean run( Session session ) {
                        List strings = session.createQuery( "select ts from TranslatedString as ts" ).list();
                        for ( Object o : strings ) {
                            TranslatedString string = (TranslatedString) o;
                            if ( string.getValue().contains( "''" ) ) {
                                String newVal = string.getValue().replace( "''", "'" );
                                logger.info( "String " + string.getKey() + " in translation " + string.getTranslation().getId() + " removing double single-quotes from \"" + string.getValue() + "\" to \"" + newVal + "\"" );
                                string.setValue( newVal );
                                session.update( string );
                                PhetLocalizer.get().updateCachedString( string.getTranslation(), string.getKey(), string.getValue() );
                            }
                        }
                        return true;
                    }
                } );
            }
        } );

        add( new Link( "debug-logging" ) {
            @Override
            public void onClick() {
                StringWriter writer = new StringWriter();
                WriterAppender appender = new WriterAppender( new PatternLayout( "%d{DATE} %5p %25c{1} - %m%n" ), writer );
                Logger log4jLogger = Logger.getLogger( AdminMainPage.class );
                log4jLogger.addAppender( appender );

                java.util.logging.Logger julLogger = java.util.logging.Logger.getLogger( AdminMainPage.class.getName() );
                logger.info( "log4j info" );
                julLogger.info( "jul info" );
                logger.debug( "log4j debug" );
                julLogger.fine( "jul fine" );

                log4jLogger.removeAppender( appender );

                System.out.println( "Logged:\n" + writer.toString() );
            }
        } );

        add( new Link( "debug-newsletter" ) {
            @Override
            public void onClick() {
                final List<PhetUser> users = new LinkedList<PhetUser>();
                HibernateUtils.wrapCatchTransaction( getHibernateSession(), new VoidTask() {
                    public void run( Session session ) {

                        List list = session.createQuery( "select u from PhetUser as u" ).list();
                        for ( Object o : list ) {
                            PhetUser user = (PhetUser) o;
                            if ( user.isConfirmed() && user.isReceiveEmail() ) {
                                users.add( user );
                            }
                        }

                        for ( PhetUser user : users ) {
                            // if user doesn't have a good confirmation key for unsubscribing, generate one
                            user.ensureHasConfirmationKey( session );
                        }
                    }
                } );
                new NewsletterSender().sendNewsletters( users );
            }
        } );

        add( new Link( "debug-newsletter2" ) {
            // this is a temporary hack to send the newsletter to all the people with null names.
            @Override
            public void onClick() {
                final List<PhetUser> users = new LinkedList<PhetUser>();
                HibernateUtils.wrapCatchTransaction( getHibernateSession(), new VoidTask() {
                    public void run( Session session ) {
                        List list = session.createQuery( "select u from PhetUser as u" ).list();
                        for ( Object o : list ) {
                            PhetUser user = (PhetUser) o;
                            if ( user.isConfirmed() && user.isReceiveEmail() ) {
                                if ( user.getName() == null ) {
                                    users.add( user );
                                }
                            }
                        }

                        for ( PhetUser user : users ) {
                            // if user doesn't have a good confirmation key for unsubscribing, generate one
                            user.ensureHasConfirmationKey( session );
                        }
                    }
                } );
                new NewsletterSender().sendNewsletters( users );
            }
        } );

        add( new Link( "debug-newsletter3" ) {
            @Override
            public void onClick() {
                final List<PhetUser> users = new LinkedList<PhetUser>();
                HibernateUtils.wrapCatchTransaction( getHibernateSession(), new VoidTask() {
                    public void run( Session session ) {
                        String[] emails = new String[]{
                                "olsonsjc@gmail.com"
                        };
                        for ( String email : emails ) {
                            PhetUser user = (PhetUser) session.createQuery( "select u from PhetUser as u where u.email = :email" ).setString( "email", email ).uniqueResult();
                            if ( user.isConfirmed() && user.isReceiveEmail() ) {
                                users.add( user );
                            }
                        }

                        for ( PhetUser user : users ) {
                            // if user doesn't have a good confirmation key for unsubscribing, generate one
                            user.ensureHasConfirmationKey( session );
                        }
                    }
                } );
                new NewsletterSender().sendNewsletters( users );
            }
        } );


        add( new Link( "debug-imagesize" ) {
            @Override
            public void onClick() {
                HashMap<String, String> map = new HashMap<String, String>();
                for ( int i = 0; i < 200; i++ ) {
                    map.put( String.valueOf( new Integer( i ).hashCode() ), String.valueOf( i ) + " value" );
                }
                logger.info( "X" );
                for ( int i = 0; i < 200; i++ ) {
                    map.get( String.valueOf( new Integer( i ).hashCode() ) );
                }
                logger.info( "A" );
                for ( int i = 0; i < 200; i++ ) {
                    File imageFile = new File( PhetWicketApplication.get().getWebsiteProperties().getPhetDocumentRoot(), "sims/build-an-atom/build-an-atom-screenshot.png" );
                    ImageUtils.getImageFileDimension( imageFile );
                }
                logger.info( "B" );
                for ( int i = 0; i < 200; i++ ) {
                    String downArrow = "/images/arrow-down.gif";
                    InputStream stream = getServletContext().getResourceAsStream( downArrow );
                    ImageUtils.getImageStreamDimension( stream, downArrow );
                }
                logger.info( "C" );

            }
        } );

        add( new Link( "debug-writemetadata" ) {
            @Override
            public void onClick() {
                MetadataUtils.writeSimulations();
            }
        } );

        add( new Link( "debug-gradelevelcategories" ) {
            @Override
            public void onClick() {
                HibernateUtils.wrapTransaction( getHibernateSession(), new VoidTask() {
                    public void run( Session session ) {
                        List sims = session.createQuery( "select s from Simulation as s" ).list();
                        for ( Object o : sims ) {
                            Simulation sim = (Simulation) o;
                            sim.setLowGradeLevel( sim.getMinGradeLevel() );
                            sim.setHighGradeLevel( sim.getMaxGradeLevel() );
                            session.update( sim );
                        }
                    }
                } );
            }
        } );

        add( new Link( "debug-pdfout" ) {
            @Override
            public void onClick() {
                try {
                    File outputFile = new File( PhetWicketApplication.get().getWebsiteProperties().getPhetDownloadRoot(), "faq/test.pdf" );
                    outputFile.getParentFile().mkdir();
                    PDFUtils.writeHTMLToPDF( PDFUtils.constructPreferredHTML( "<div class=\"simFaqItem\">\n" +
                                                                              "    <div class=\"simFaqQuestion\">How can I use this in my class?</div>\n" +
                                                                              "\n" +
                                                                              "    <div class=\"simFaqAnswer\">Here's some ideas:<br/><br/>You can use the Build a Molecule sim:<br/><br/><strong>- In class</strong> by\n" +
                                                                              "        having your students work individually or in pairs at computers. Check out the sample activities for this simulation available\n" +
                                                                              "        in the Teacher Resources section (below where you download the sim).<br/><br/><strong>- As a demo</strong> by projecting the\n" +
                                                                              "        simulation onto a screen. You can manipulate the simulation, or you can select students to come up and build molecules for the\n" +
                                                                              "        class.<br/><br/><strong>- As homework</strong> by asking students to complete a set number of collection boxes in preparation\n" +
                                                                              "        for a class, or as practice after an introduction to molecules.<br/><br/><strong>- With clicker questions</strong> by asking\n" +
                                                                              "        students the name or geometry of a molecule with a clicker questions, and then build and visualize it to see if they were\n" +
                                                                              "        correct.\n" +
                                                                              "    </div>\n" +
                                                                              "</div>\n" +
                                                                              "\n" +
                                                                              "<div class=\"simFaqItem\">\n" +
                                                                              "    <div class=\"simFaqQuestion\">What can students learn from playing with this sim?</div>\n" +
                                                                              "    <div class=\"simFaqAnswer\">A lot! Students can learn:<br/>\n" +
                                                                              "        <ul>\n" +
                                                                              "            <li>Molecules are made from atoms,</li>\n" +
                                                                              "            <li>What are molecular formulas,</li>\n" +
                                                                              "            <li>What are subscripts and coefficients,</li>\n" +
                                                                              "            <li>The same molecule can be represented in many different ways (with a name, formula, in 2D and 3D, in space filling and\n" +
                                                                              "                ball and stick view),\n" +
                                                                              "            </li>\n" +
                                                                              "        </ul>\n" +
                                                                              "        Students can also:\n" +
                                                                              "        <ul>\n" +
                                                                              "            <li>Compare names for similar looking molecules to start learning about naming,</li>\n" +
                                                                              "            <li>Learn that molecules have a specific structure (a certain way the atoms go together) and begin asking questions about\n" +
                                                                              "                why do atoms go together in particular ways.\n" +
                                                                              "            </li>\n" +
                                                                              "            <li>And more...!</li>\n" +
                                                                              "        </ul>\n" +
                                                                              "    </div>\n" +
                                                                              "</div>",
                                                                              FAQPanel.getFAQCSS() ), outputFile );
                }
                catch( Exception e ) {
                    logger.warn( e );
                }
            }
        } );

        add( new Link( "debug-safe-1" ) {
            @Override
            public void onClick() {
                HibernateUtils.wrapTransaction( getHibernateSession(), new VoidTask() {
                    public void run( Session session ) {
                        Simulation simulation = (Simulation) session.createQuery("select s from Simulation as s where s.name = 'density'").uniqueResult();

                        LearningRegistryUtils.submitEnvelope( new SimulationRecord( MetadataUtils.simulationToMasterFormat( simulation ) ), MetadataUtils.ieeeLomConverter() );
                    }
                } );
            }
        } );

        add( new RawLabel( "logTest", new Model<String>( logTest ) ) );
    }

    public final class SetStringForm extends Form {
        private static final long serialVersionUID = 1L;

        private final ValueMap properties = new ValueMap();

        public SetStringForm( final String id ) {
            super( id );

            add( keyText = new StringTextField( "key", new PropertyModel<String>( properties, "key" ) ) );
            add( valueText = new StringTextField( "value", new PropertyModel<String>( properties, "value" ) ) );

            // don't turn <'s and other characters into HTML/XML entities!!!
            valueText.setEscapeModelStrings( false );
        }

        public final void onSubmit() {
            // TODO: important before deploy: add authorization or remove (for specific translation)
            String key = keyText.getModelObject().toString();
            String value = valueText.getModelObject().toString();
            logger.info( "Submitted new string: " + key + " = " + value );
            StringUtils.setEnglishString( getHibernateSession(), key, value );
        }
    }

}
