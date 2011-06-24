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

        overwriteString( session, "get-phet.full-install.creatingInstallationCD.step1", "Download the <a {0}>PhET CD-ROM Distribution archive</a> onto your computer ({1} MB).", "Download the PhET Distribution archive onto your computer:<ul><li><a {0}>CD without teaching activities</a> ({1} MB)</li><li><a {2}>DVD with English teaching activities</a> ({3} MB)</li></ul>" );
        overwriteString( session, "get-phet.full-install.creatingInstallationCD.step1", "Download the PhET Distribution archive onto your computer:<ul><li><a {0}>CD without teaching activities</a> ({1} MB)</li><li><a {2}>DVD with English teaching activities</a> ({3} MB)</li></ul>", "Download the PhET Distribution archive onto your computer:<ul><li><a {0}>CD without teaching activities</a> ({1} MB)</li><li><a {2}>DVD with English teaching activities (in English only)</a> ({3} MB)</li></ul>" );
        overwriteString( session, "get-phet.full-install.contactForCD", "If you have an Internet connection that is too slow, we can send you a CD with the full installation package. Click <a {0}>here to contact us</a>.", "If you have an Internet connection that is too slow, we can send you a CD-ROM or DVD-ROM with the full installation package. The CD version contains installers that exclude the teaching activities in order to reduce the size, and the DVD version contains installers that include English-language teaching activities.  Be sure to specify which one you would prefer when <a {0}>contacting us</a>." );
        overwriteString( session, "get-phet.full-install.contactForCD", "If you have an Internet connection that is too slow, we can send you a CD-ROM or DVD-ROM with the full installation package. The CD version contains installers that exclude the teaching activities in order to reduce the size, and the DVD version contains installers that include English-language teaching activities.  Be sure to specify which one you would prefer when <a {0}>contacting us</a>.", "If you have an Internet connection that is too slow, we can send you a CD-ROM or DVD-ROM with the full installation package. The installers on the DVD-ROM contain English-language teaching activities, whereas the installers on the CD-ROM exclude these activities in order to reduce the size.  Be sure to specify which one you would prefer when <a {0}>contacting us</a>." );
        overwriteString( session, "get-phet.full-install.creatingInstallationCD", "Creating an Installation CD-ROM", "Creating an Installation CD or DVD" );
        overwriteString( session, "get-phet.full-install.creatingInstallationCD.intro", "You can make a CD-ROM for doing the full PhET installation on computers without Internet access. You will need a computer equipped with a CD writer, a blank CD, and an Internet connection. Please <a {0}>contact PhET</a> help with any questions.", "You can create your own CD or DVD for doing the full PhET installation on computers without Internet access. You will need a computer equipped with a CD/DVD writer, a blank disk, and an Internet connection. Please <a {0}>contact PhET</a> help with any questions." );
        overwriteString( session, "get-phet.full-install.creatingInstallationCD.step3", "Copy all the files to the CD-ROM.", "Copy all the files to the CD-ROM or DVD-ROM." );

        addString( session, "about.source-code.prerequisites.header", "Software Prerequisites" );
        addString( session, "about.source-code.prerequisites.requirements", "The following software is required to build PhET simulations from source code:" );
        addString( session, "about.source-code.prerequisites.java", "Java simulations: Java JDK 5+" );
        addString( session, "about.source-code.prerequisites.flash", "Flash simulations: Java JDK 5+ and Flash CS4+" );
        addString( session, "about.source-code.prerequisites.flex", "Flex simulations: Java JDK 5+ and Flex 3 (3.4 or later)" );

        addString( session, "faq.mobileDevices.title", "Can PhET simulations be used on my tablet/iPad/Android devices?" );
        addString( session, "faq.mobileDevices.answer", "<p>At this time tablet/iPad/Android devices platforms do not fully support Java or Flash, which is required to run PhET simulations.</p><ul><li>iPads lack full support of Java and Flash so our sims currently cannot run on the interface. Our sims also aren't developed for touch devices and the mobile devices usually perform very poorly when running our simulations due to lack of processing power.</li><li>Android devices cannot run full Java programs like PhET sims, just Android Java programs. Flash sims have poor performance and user input response.</li></ul><p>In addition to the lack of support for required platforms, porting PhET sims to iPad/ tablet devices is not simply a matter of rewriting the code in another language. PhET sims are designed for devices with a mouse and keyboard. There are certain things that are awkward with a touch screen (like typing, especially when the virtual keyboard covers what you want to look at), right clicking...and some things that are impossible, such as hovering over objects.</p><p>If your school district is considering the purchase of iPads or tablet devices, please understand that PhET simulations will either not function or perform poorly. We recommend laptops or netbooks for using our simulations using our <a {0}>system requirements</a>.</p>" );

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
