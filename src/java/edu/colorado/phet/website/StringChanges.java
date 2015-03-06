/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import edu.colorado.phet.website.util.StringUtils;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;

/**
 * Contains strings that have been added or modified since the last production deployment. If strings by those key names
 * don't exist they will be created.
 */
public class StringChanges {

    private static Logger logger = Logger.getLogger( StringChanges.class );

    public static void checkNewStrings() {
        Session session = HibernateUtils.getInstance().openSession();

        StringUtils.addString( session, "webglDisabled.windows", "Windows" );
        StringUtils.addString( session, "webglDisabled.mac", "Mac" );
        StringUtils.addString( session, "webglDisabled.mobile", "Mobile" );
        StringUtils.addString( session, "nav.forTeachers.facilitatingActivities", "Facilitating Activities for K12" );
        StringUtils.addString( session, "forTeachers.facilitatingActivities.title", "Facilitating Activities for K12" );

        StringUtils.addString( session, "htmlLicensing.header", "PhET SOFTWARE AGREEMENT<br/>" +
                "Terms of Use and Privacy Policy for PhET's collection of HTML Simulations<br/>" +
                "(Version 1)<br/>" +
                "University of Colorado Boulder<br/>" );
        StringUtils.addString( session, "htmlLicensing.preamble", "In this document:" );
        StringUtils.addString( session, "htmlLicensing.agreementI", "Overview" );
        StringUtils.addString( session, "htmlLicensing.agreementII", "Software License Information" );
        StringUtils.addString( session, "htmlLicensing.agreementIIA", "'PhET Regular HTML Simulation File': Creative " +
                "Commons Attribution 4.0" );
        StringUtils.addString( session, "htmlLicensing.agreementIIB", "'PhET HTML Simulation Source Code': GNU " +
                "General Public License Version 3" );
        StringUtils.addString( session, "htmlLicensing.agreementIIC", "'PhET HTML Common Source Code': MIT License" );
        StringUtils.addString( session, "htmlLicensing.agreementIID", "Donations" );
        StringUtils.addString( session, "htmlLicensing.agreementIIE", "Third Party Software Credits" );
        StringUtils.addString( session, "htmlLicensing.agreementIIF", "Alternative Licensing Options" );
        StringUtils.addString( session, "htmlLicensing.agreementIII", "PhET Name and Logo Trademark" );
        StringUtils.addString( session, "htmlLicensing.agreementIV", "PhET Name and Logo Trademark" );
        StringUtils.addString( session, "htmlLicensing.agreementIVA", "Commitment to Individual Privacy" );
        StringUtils.addString( session, "htmlLicensing.agreementIVB", "Information Collected" );
        StringUtils.addString( session, "htmlLicensing.agreementIVC", "Information Security" );
        StringUtils.addString( session, "htmlLicensing.agreementIVD", "Information Sharing" );
        StringUtils.addString( session, "htmlLicensing.agreementIVE", "Colorado Open Records Act" );
        StringUtils.addString( session, "htmlLicensing.agreementIVF", "Additional privacy terms apply when PhET is " +
                "used with the Microsoft Office Mix Service" );
        StringUtils.addString( session, "htmlLicensing.agreementV", "Disclaimer" );
        StringUtils.addString( session, "htmlLicensing.agreementVI", "Contact Us" );
        StringUtils.addString( session, "htmlLicensing.agreementI.header", "I. OVERVIEW" );
        StringUtils.addString( session, "htmlLicensing.agreementI.p1", "This PhET Software Agreement " +
                "(\"<b>Agreement</b>\") pertains to the collection of HTML simulations developed by the PhET " +
                "Interactive Simulations Project at the University of Colorado Boulder (\"<b>PhET</b>\"). PhET " +
                "currently offers three different software licensing options, which are detailed in Section II(A), " +
                "II(B) and II(C) below, to the users of its HTML simulations." );
        StringUtils.addString( session, "htmlLicensing.agreementI.p2", "This Agreement and the licenses specified " +
                "below do <i>not</i> permit the licensing of PhET's Java or Flash simulations. Information " +
                "regarding PhET's Java or Flash simulations is available at <a href=\"http://phet.colorado.edu\">" +
                "http://phet.colorado.edu</a>." );
        StringUtils.addString( session, "htmlLicensing.agreementII.header", "II. SOFTWARE LICENSE OPTIONS" );
        StringUtils.addString( session, "htmlLicensing.agreementIIA.header", "A. PhET Regular HTML Simulation File: " +
                "Creative Commons Attribution 4.0" );
        StringUtils.addString( session, "htmlLicensing.agreementIIA.p1", " <b>\"PhET Regular HTML Simulation File\" " +
                "</b> means an individual minified HTML simulation file where PhET HTML Simulation Source Code " +
                "(PhET HTML Simulation Source Code is defined below) assets and PhET HTML Common Source Code (PhET " +
                "HTML Common Source Code is " +
                "defined below) assets are combined into a single, minified HTML file that can be run in a browser, " +
                "or downloaded and launched, " +
                "without any other requirements." );
        StringUtils.addString( session, "htmlLicensing.agreementIIA.p2", "PhET licenses PhET Regular HTML Simulation " +
                "Files individually under a <a href=\"http://creativecommons.org/licenses/by/4.0/\" " +
                "target=\"_blank\">Creative Commons Attribution 4.0 License</a> (\"<b>CC BY 4.0</b>\"). The full " +
                "text of the Creative Commons Attribution 4.0 license is available here: <a " +
                "href=\"http://creativecommons.org/licenses/by/4.0/legalcode\" target=\"_blank\">" +
                "http://creativecommons.org/licenses/by/4.0/legalcode</a>." );
        StringUtils.addString( session, "htmlLicensing.agreementIIA.p3", "The collection of PhET Regular HTML " +
                "Simulation Files are available to run or download from " +
                "<a href=\"http://phet.colorado.edu\">http://phet.colorado.edu</a>." );
        StringUtils.addString( session, "htmlLicensing.agreementIIA.p4", "What does the CC BY 4.0 License mean?" );
        StringUtils.addString( session, "htmlLicensing.agreementIIA.p5", "PhET Regular HTML Simulation Files may be " +
                "freely used and/or redistributed by third parties (e.g. students, educators, school " +
                "districts, museums, publishers, vendors, etc.) for non-commercial or commercial purposes." );
        StringUtils.addString( session, "htmlLicensing.agreementIIA.p6", "Any use of a PhET Regular HTML Simulation " +
                "File under the CC BY 4.0 requires the following attribution: \"PhET Interactive " +
                "Simulations, University of Colorado Boulder, " +
                "<a href=\"http://phet.colorado.edu\">http://phet.colorado.edu</a>.\"" );
        StringUtils.addString( session, "htmlLicensing.agreementIIA.p7", "The CC-BY-4.0 license does <i>not</i> " +
                "apply to the PhET name and PhET logo, which are trademarks of The Regents of the University " +
                "of Colorado, a body corporate. See Section III below for additional information regarding how to " +
                "use legally the PhET name and logo." );
        StringUtils.addString( session, "htmlLicensing.agreementIIB.header", "B. 'PhET HTML Simulation Source Code': " +
                "GNU General Public License Version 3" );
        StringUtils.addString( session, "htmlLicensing.agreementIIB.p1", "<b>\"PhET HTML Simulation Source Code\" " +
                "</b> is the collection of original simulation specific assets used by a PhET simulation, " +
                "including but not limited to source code (JavaScript/HTML/CSS), images, audio and text." );
        StringUtils.addString( session, "htmlLicensing.agreementIIB.p2", "PhET licenses PhET HTML Simulation Source " +
                "Code under the <a href=\"https://www.gnu.org/licenses/gpl.html\" target=\"_blank\">GNU General " +
                "Public License Version 3</a> (\"<b>GPLv3</b>\"). The full text of the GPLv3 is available here: " +
                "<a href=\"https://www.gnu.org/licenses/gpl.html\" target=\"_blank\">" +
                "https://www.gnu.org/licenses/gpl.html</a>" );
        StringUtils.addString( session, "htmlLicensing.agreementIIB.p3", " The simulation-specific PhET HTML " +
                "Simulation Source Code repositories can be accessed at " +
                "<a href=\"http://github.com/phetsims\" target=\"_blank\">http://github.com/phetsims</a>. " +
                "The license file within the specific PhET HTML Simulation Source Code repository specifies whether " +
                "it is available under the GPLv3." );
        StringUtils.addString( session, "htmlLicensing.agreementIIB.p4", "What does the GPLv3 mean?" );
        StringUtils.addString( session, "htmlLicensing.agreementIIB.p5", "The simulation-specific source code for " +
                "each PhET HTML Simulation Source Code is available for use and/or modification. Anyone may " +
                "access to the source code and make modifications. If a user makes any changes whatsoever to the " +
                "source code of the software, then " +
                "the changes must be made publicly available by the party that makes the changes." );
        StringUtils.addString( session, "htmlLicensing.agreementIIC.header", "C. 'PhET HTML Common Source Code': MIT " +
                "License" );
        StringUtils.addString( session, "htmlLicensing.agreementIIC.p1", "<b>\"PhET HTML Simulation Source Code\" " +
                "</b>is the collection of original simulation specific assets used by a PhET simulation, " +
                "including but not limited to source code (JavaScript/HTML/CSS), images, audio and text." );
        StringUtils.addString( session, "htmlLicensing.agreementIIC.p2", "PhET licenses PhET HTML Simulation Source " +
                "Code under the <a href=\"http://opensource.org/licenses/MIT\" target=\"_blank\">MIT License</a>. " +
                "The full " +
                "text of the MIT License is available here: <a href=\"http://opensource.org/licenses/MIT\" " +
                "target=\"_blank\">http://opensource.org/licenses/MIT</a>." );
        StringUtils.addString( session, "htmlLicensing.agreementIIC.p3", "PhET HTML Common Source Code can be " +
                "accessed at <a href=\"http://github.com/phetsims\" target=\"_blank\">" +
                "http://github.com/phetsims</a>. The license file within the specific PhET HTML Common Source Code " +
                "repository specifies whether it is available under the MIT License." );
        StringUtils.addString( session, "htmlLicensing.agreementIIC.p4", "What does using the MIT License mean?" );
        StringUtils.addString( session, "htmlLicensing.agreementIIC.p5", "The common source code libraries for PhET " +
                "HTML Common Source Code are available for use and/or modification. For directions about " +
                "how to access the source code, go to: " +
                "<a href=\"http://phet.colorado.edu/about/source-code.php\">http://phet.colorado" +
                ".edu/about/source-code.php</a>" + ". Anyone can have access to the " +
                "source code and make modifications. The copyright and " +
                "permission notice must be included in all copies or copies of substantial portions of the Software." );
        StringUtils.addString( session, "htmlLicensing.agreementIID.header", "D. Donations" );
        StringUtils.addString( session, "htmlLicensing.agreementIID.p1", "PhET requires ongoing donations, " +
                "sponsorships and grant funding in order to support the project, continue the development of new " +
                "simulations, and maintain existing simulations." );
        StringUtils.addString( session, "htmlLicensing.agreementIID.p2", "Commercial users are highly encouraged to " +
                "support the project and its mission with a tax-deductible donation to PhET. For more " +
                "information, email <a href=\"mailto:phethelp@colorado.edu\" target=\"_blank\">phethelp@colorado.edu</a> or visit <a " +
                "href=\"http://phet.colorado.edu/en/donate\">http://phet.colorado.edu/en/donate</a>." );
        StringUtils.addString( session, "htmlLicensing.agreementIIE.header", "E. Third Party Software Credits" );
        StringUtils.addString( session, "htmlLicensing.agreementIIE.p1", "PhET simulations use third-party software. " +
                "A complete list of third-party libraries used, the developers, and the associated " +
                "licenses is listed at the top of each \"PhET Regular HTML Simulation File\", using the browser's " +
                "view source tool." );
        StringUtils.addString( session, "htmlLicensing.agreementIIF.header", "F. Alternative License Options" );
        StringUtils.addString( session, "htmlLicensing.agreementIIF.p1", "Permissions beyond the scope of these " +
                "licenses, including licensing for PhET Enhanced HTML Simulation Files or source code assets " +
                "not identified as openly-licensed in their license files, may be available upon request. Please " +
                "contact us at <a href=\"mailto:phethelp@colorado.edu\" target=\"_blank\">phethelp@colorado.edu</a>." );
        StringUtils.addString( session, "htmlLicensing.agreementIII.header", "III. PHET NAME AND LOGO TRADEMARK" );
        StringUtils.addString( session, "htmlLicensing.agreementIII.p1", "The PhET name and PhET logo are trademarks " +
                "of The Regents of the University of Colorado, a body corporate. Permission is granted to " +
                "use the PhET name and PhET logo only for attribution purposes. Use of the PhET name and/or PhET " +
                "logo for promotional, marketing, or advertising purposes requires a separate license agreement from " +
                "the University of Colorado. Contact <a href=\"mailto:phethelp@colorado.edu\" target=\"_blank\">" +
                "phethelp@colorado.edu</a> to discuss trademark licensing options." );
        StringUtils.addString( session, "htmlLicensing.agreementIV.header", "IV. PRIVACY POLICY" );
        StringUtils.addString( session, "htmlLicensing.agreementIVA.header", "A. Commitment to Individual Privacy" );
        StringUtils.addString( session, "htmlLicensing.agreementIVA.p1", "The University of Colorado and the PhET " +
                "Interactive Simulations Project support the protection of individual privacy, and ensuring " +
                "the confidentiality of information provided by its employees, students, visitors, and resource users." );
        StringUtils.addString( session, "htmlLicensing.agreementIVB.header", "B. Information Collected" );
        StringUtils.addString( session, "htmlLicensing.agreementIVB.p1", "It is the policy and practice of the " +
                "University to collect the least amount of personally identifiable information necessary to " +
                "fulfill its required duties and responsibilities, complete a particular transaction, deliver " +
                "services, or as required by law. This applies to the collection of all personally identifiable " +
                "information regardless of source or medium." );
        StringUtils.addString( session, "htmlLicensing.agreementIVB.p2", "Users may choose whether or not to provide " +
                "personally identifiable information to the University and PhET via the PhET website." );
        StringUtils.addString( session, "htmlLicensing.agreementIVB.p3", "In order to document the amount of use of " +
                "PhET simulations and to provide the best services to our users, PhET collects " +
                "non-personally identifying information through the use of Google Analytics in each PhET Regular " +
                "HTML Simulation File. When the " +
                "simulation is opened, information is collected via Google Analytics' standard web statistics " +
                "services. As of July 8, 2014 Google Analytics Terms of Service are located here " +
                "<a href=\"http://www.google.com/analytics/terms/us.html\"  + " +
                "target=\"_blank\">http://www.google.com/analytics/terms/us.html</a>." );
        StringUtils.addString( session, "htmlLicensing.agreementIVB.p4", "In addition, PhET uses \"cookies,\" which " +
                "are small text files stored on the user's device, to help operate its website and collect " +
                "information about online activity." );
        StringUtils.addString( session, "htmlLicensing.agreementIVC.header", "C. Information Security" );
        StringUtils.addString( session, "htmlLicensing.agreementIVC.p1", "The PhET has in place appropriate security " +
                "measures to protect against the unauthorized use or access of its data. Such measures " +
                "include internal review of data collection, storage, and processing practices and security " +
                "measures, as well as physical security " +
                "measures to guard against unauthorized access to systems where data is stored." );
        StringUtils.addString( session, "htmlLicensing.agreementIVD.header", "D. Information Sharing" );
        StringUtils.addString( session, "htmlLicensing.agreementIVD.p1", "Access to information collected through " +
                "the use of PhET simulations is limited to those employees, contractors, and agents who need " +
                "to know that information in order to operate, develop, or improve our services. PhET may report a " +
                "summary of this information (in " +
                "aggregate) to other organizations and individuals for grant reporting purposes, or in published " +
                "articles to document PhET's " +
                "wide-spread use. No personally-identifying information is distributed. PhET requires third parties " +
                "to whom it discloses " +
                "information to protect the information in accordance with this policy and applicable law. In " +
                "addition, PhET may disclose information to third parties when such disclosure is required or " +
                "permitted by law." );
        StringUtils.addString( session, "htmlLicensing.agreementIVE.header", "E. Colorado Open Records Act" );
        StringUtils.addString( session, "htmlLicensing.agreementIVE.p1", "The University of Colorado and the PhET " +
                "project are subject to the Colorado Open Records Act, C.R.S § 24-72-101 <i>et seq</i>. The " +
                "Colorado Open Records Act requires that all records maintained by the University and PhET be " +
                "available for public inspection except as otherwise provided by law. Personal identifying " +
                "information collected by PhET may be subject to public inspection and " +
                "copying unless protected by state or federal law." );
        StringUtils.addString( session, "htmlLicensing.agreementIVF.header", "F. Additional Privacy Terms Apply When " +
                "PhET is Used with the Microsoft Office Service" );
        StringUtils.addString( session, "htmlLicensing.agreementIVF.p1", "The app for Office Mix will share " +
                "information with Microsoft about how the users interact with it, such as whether the app loaded " +
                "successfully, any content that is displayed in the app, and any information entered in the app. " +
                "Microsoft may combine this information with other information to provide the Office Mix service in " +
                "accordance with the statement on <a href=\"http://aka.ms/privacy-onlinelearning\" " +
                "target=\"_blank\">Privacy &amp; Cookies</a> for Office Mix. By using this app, the user consents " +
                "to the app sharing information with Microsoft for these purposes." );
        StringUtils.addString( session, "htmlLicensing.agreementV.header", "V. DISCLAIMER" );
        StringUtils.addString( session, "htmlLicensing.agreementV.p1", "This software and the information contained " +
                "therein is provided as a public service, with the understanding that neither the " +
                "University of Colorado nor PhET makes any warranties, either express or implied, concerning the " +
                "accuracy, completeness, reliability, or suitability of this software and information." );
        StringUtils.addString( session, "htmlLicensing.agreementV.p2", "By using this software, you assume all risks " +
                "associated with such use, including but not limited to the risk of any damage to your " +
                "computer, software, or data. In no event shall the University or PhET be liable for any direct, " +
                "indirect, punitive, special, incidental, or consequential damages (including, without limitation, " +
                "lost revenues or profits or lost or damaged data) arising from the user's use of this software." );
        StringUtils.addString( session, "htmlLicensing.agreementV.p3", "Neither the University nor PhET are a law " +
                "firm and do not provide legal services or legal advice. Using the simulations under the " +
                "licensing options in this agreement does not create a lawyer-client or other relationship. Any " +
                "information provided to the user regarding the licensing options is intended to provide general " +
                "guidance to the user. If the user requires legal assistance, the user should contact a law firm or " +
                "a lawyer." );
        StringUtils.addString( session, "htmlLicensing.agreementVI.header", "VI. CONTACT US" );
        StringUtils.addString( session, "htmlLicensing.agreementVI.p1", "Please send comments, questions, or " +
                "concerns regarding this software agreement to " +
                "<a href=\"mailto:phethelp@colorado.edu\" target=\"_blank\">phethelp@colorado.edu</a>. " +
                "Please do not send attachments with the message." );

        StringUtils.addString( session, "htmlLicensing.disclaimer", "Disclaimer: The text of the licensing information below is only legally valid for the English version. Any translations of the licensing information are not legally binding, but instead are provided for the convenience of non-English users." );


        session.close();
    }

    /*---------------------------------------------------------------------------*
    * deprecated strings:
    * newsletter-instructions
    * about.who-we-are.title
    *----------------------------------------------------------------------------*/

}
