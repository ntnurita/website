/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import edu.colorado.phet.website.data.TranslatedString;
import edu.colorado.phet.website.util.StringUtils;
import edu.colorado.phet.website.util.hibernate.HibernateTask;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;

/**
 * Contains strings that have been addedor modified since the last production deployment. If strings by those key names
 * don't exist they will be created.
 */
public class StringChanges {

    private static Logger logger = Logger.getLogger( StringChanges.class );

    public static void checkNewStrings() {
        Session session = HibernateUtils.getInstance().openSession();

        deleteString( session, "about.source-code.location" );
        deleteString( session, "about.source-code.flash-simulations" );

        addString( session, "about.source-code.browseOnline.header", "To browse PhET source code online" );
        addString( session, "about.source-code.browseOnline.visitUrlStep", "Visit the URL {0}" );
        addString( session, "about.source-code.browseOnline.loginStep", "Enter username={0} and password={1}" );
        addString( session, "about.source-code.navigating.header", "Navigating our source code" );
        addString( session, "about.source-code.navigating.structureReadme", "README.txt describes the main folder structure. Simulations can be found under these folders:" );
        addString( session, "about.source-code.navigating.javaSims", "Java simulations: simulations-java/simulations" );
        addString( session, "about.source-code.navigating.flashSims", "Flash simulations: simulations-flash/simulations" );
        addString( session, "about.source-code.navigating.flexSims", "Flex simulations: simulations-flex/simulations" );
        addString( session, "about.source-code.navigating.projectOrganization", "PhET simulations are organized into projects, each of which can contain one or more related simulations. For example, the \"nuclear-physics\" project contains 4 simulations, including \"Alpha Decay\" and \"Radioactive Dating Game\"." );
        addString( session, "about.source-code.checkout.header", "To checkout PhET's source code repository using <a {0}>Subversion</a> on the command-line" );
        addString( session, "about.source-code.checkout.useTheFollowing", "Use the following command:" );
        addString( session, "about.source-code.checkout.result", "This will put all files in a folder named \"trunk\" -- it will take a while, so please be patient." );
        addString( session, "about.source-code.buildAndRun.header", "To build and run PhET simulations using the <em>PhET Build GUI</em>" );
        addString( session, "about.source-code.buildAndRun.instructions", "Check out our source code with the instructions above, and then run one of these two scripts depending on your operating system:" );
        addString( session, "about.source-code.buildAndRun.windows", "Windows:" );
        addString( session, "about.source-code.buildAndRun.macLinux", "Mac, Linux:" );
        addString( session, "about.source-code.usingPBG.header", "Using the <em>PhET Build GUI</em>" );
        addString( session, "about.source-code.usingPBG.flashFlex", "For building Flash/Flex simulations, please download {0} to trunk/build-tools/build-local.properties, and replace relevant paths with your own." );
        addString( session, "about.source-code.usingPBG.browseProjects", "Browse through and select the project (listed on the left) that contains the simulation you are looking for." );
        addString( session, "about.source-code.usingPBG.selectSimulationLocale", "Select the simulation and/or locale" );
        addString( session, "about.source-code.usingPBG.pressTest", "Press \"Test\". Be patient, since it can take several minutes to compile and launch the simulation." );

        session.close();
    }

    /*---------------------------------------------------------------------------*
    * deprecated strings:
    * newsletter-instructions
    *----------------------------------------------------------------------------*/

    public static void addString( Session session, String key, String value ) {
        String result = StringUtils.getStringDirect( session, key, PhetWicketApplication.getDefaultLocale() );
        if ( result == null ) {
            logger.warn( "Auto-setting English string with key=" + key + " value=" + value );
            StringUtils.setEnglishString( session, key, value );
        }
    }

    private static void overwriteString( Session session, String key, String oldValue, String newValue ) {
        String result = StringUtils.getStringDirect( session, key, PhetWicketApplication.getDefaultLocale() );
        if ( result == null ) {
            logger.warn( "Auto-setting English string with key=" + key + " value=" + newValue );
            StringUtils.setEnglishString( session, key, newValue );
        }
        else {
            if ( result.equals( oldValue ) ) {
                logger.warn( "Auto-setting English string with key=" + key + " value=" + newValue + " over old value " + oldValue );
                StringUtils.setEnglishString( session, key, newValue );
            }
        }
    }

    private static void deleteString( Session session, final String key ) {
        String result = StringUtils.getStringDirect( session, key, PhetWicketApplication.getDefaultLocale() );
        if ( result != null ) {
            logger.warn( "Deleting English string with key=" + key + " value=" + result );
            HibernateUtils.wrapTransaction( session, new HibernateTask() {
                public boolean run( Session session ) {
                    TranslatedString translatedString = (TranslatedString) session.createQuery( "select ts from TranslatedString as ts, Translation as t where (ts.translation = t and t.visible = true and t.locale = :locale and ts.key = :key)" )
                            .setLocale( "locale", PhetWicketApplication.getDefaultLocale() ).setString( "key", key ).uniqueResult();
                    if ( translatedString != null ) {
                        translatedString.getTranslation().removeString( translatedString );
                        session.delete( translatedString );
                    }
                    return true;
                }
            } );
        }
    }
}
