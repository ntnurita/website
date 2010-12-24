package edu.colorado.phet.website.tests;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.colorado.phet.website.content.IndexPanel;
import edu.colorado.phet.website.panels.LogInOutPanel;

import com.thoughtworks.selenium.SeleneseTestCase;

public class BasicWebsiteNavigation extends SeleneseTestCase {
    @Before
    public void setUp() throws Exception {
        selenium = SeleniumUtils.createDefaultSelenium();
        selenium.start();
    }

    public void loadWithoutError() {
        pageLoad();
        assertNoError();
    }

    public void pageLoad() {
        selenium.waitForPageToLoad( "30000" );
    }

    public void assertNoError() {
        assertNotEquals( selenium.getTitle(), "error.internalError" );
        assert ( !selenium.getBodyText().contains( "Unexpected RuntimeException" ) );
    }

    @Test
    public void testClickOnLogo() throws Exception {
        selenium.open( "/" );
        selenium.click( "//img[@alt='PhET Logo']" );
        loadWithoutError();
    }

    @Test
    public void testClickOnPlay() throws Exception {
        selenium.open( "/" );
        selenium.click( IndexPanel.PLAY_SIMS_ID );
        loadWithoutError();
    }

    @Test
    public void testSignIn() throws Exception {
        // checks validation

        String signInFailed = SeleniumUtils.getString( "signIn.validation.failed" );
        String signInTitle = SeleniumUtils.getString( "signIn.title" );

        selenium.open( "/" );
        selenium.click( LogInOutPanel.SIGN_IN_ID );
        loadWithoutError();
        selenium.type( "username", "bogus-email" );
        selenium.type( "password", "bogus-password" );
        selenium.click( "submit" );
        loadWithoutError();
        assert ( selenium.getBodyText().contains( signInFailed ) );
        assertEquals( selenium.getTitle(), signInTitle );
        selenium.type( "username", "test@phet.colorado.edu" );
        selenium.click( "submit" );
        loadWithoutError();
        assert ( selenium.getBodyText().contains( signInFailed ) );
        assertEquals( selenium.getTitle(), signInTitle );
        selenium.type( "password", "test-password" );
        selenium.click( "submit" );
        loadWithoutError();
        assert ( !selenium.getBodyText().contains( signInFailed ) );
        assertNotEquals( selenium.getTitle(), signInTitle );
    }

    @Test
    public void testSearch() throws Exception {
        selenium.open( "/" );
        selenium.type( "q", "circuit" );
        selenium.click( "search-submit-id" );
        loadWithoutError();
        assert ( selenium.getBodyText().contains( "Circuit Construction Kit" ) );
    }

    @Test
    public void testContributionCreateDelete() throws Exception {
        selenium.open( "/" );
        selenium.click( "sign-in-link" );
        loadWithoutError();
        selenium.type( "username", "" );
        selenium.type( "username", "test@phet.colorado.edu" );
        selenium.type( "password", "test-password" );
        selenium.click( "submit" );
        loadWithoutError();
        selenium.click( "home-submit-activity-link" );
        loadWithoutError();
        selenium.click( "//a[@id='nav-location-nav-teacherIdeas-submit']/span" );
        loadWithoutError();
        selenium.click( "submit" );
        loadWithoutError();
        verifyTrue( selenium.isTextPresent( SeleniumUtils.getString( "contribution.edit.title.Required" ) ) );
        selenium.type( "cep-title", "Sample Title" );
        selenium.click( "submit" );
        loadWithoutError();
        verifyTrue( selenium.isTextPresent( SeleniumUtils.getString( "contribution.edit.keywords.Required" ) ) );
        selenium.type( "cep-keywords", "key1,key2" );
        selenium.click( "submit" );
        loadWithoutError();
        verifyTrue( selenium.isTextPresent( SeleniumUtils.getString( "contribution.edit.validation.mustHaveSims" ) ) );
        selenium.select( "options6", "label=Acid-Base Solutions" );
        selenium.click( "//option[@value='100']" );
        selenium.click( "button7" );
        selenium.click( "submit" );
        loadWithoutError();
        verifyTrue( selenium.isTextPresent( SeleniumUtils.getString( "contribution.edit.validation.mustHaveTypes" ) ) );
        selenium.select( "options9", "label=" + SeleniumUtils.getString( "contribution.type.conceptQuestions" ) );
        selenium.click( "//select[@id='options9']/option[2]" );
        selenium.click( "buttona" );
        Thread.sleep( 5000 ); // allow ajax to load
        selenium.select( "optionsc", "label=" + SeleniumUtils.getString( "contribution.level.graduate" ) );
        selenium.click( "//select[@id='optionsc']/option[2]" );
        selenium.click( "buttond" );
        selenium.click( "submit" );
        loadWithoutError();
        verifyTrue( selenium.isTextPresent( SeleniumUtils.getString( "contribution.edit.validation.mustHaveFiles" ) ) );
        Thread.sleep( 5000 );
        selenium.attachFile( "upload3", new File( "/home/jon/tmp/upload.pdf" ).toURL().toString() );
        selenium.click( "submit" );
        loadWithoutError();
        Thread.sleep( 5000 );
        verifyTrue( selenium.isTextPresent( SeleniumUtils.getString( "contribution.edit.successHeader" ) ) );
        selenium.click( "continue-to-the-activity" );
        loadWithoutError();
        verifyTrue( selenium.isTextPresent( "Sample Title" ) );
        selenium.click( "//a[@id='nav-location-nav-teacherIdeas-manage']/span" );
        loadWithoutError();
        selenium.click( "link=delete" );
        loadWithoutError();
    }

    @Test
    public void testBasicNavigation() throws Exception {
        selenium.open( "/" );
        selenium.click( "play-sims" );
        loadWithoutError();
        selenium.click( "nav-location-nav-physics" );
        loadWithoutError();
        selenium.click( "//a[@id='nav-location-nav-electricity-magnets-and-circuits']/span" );
        loadWithoutError();
        selenium.click( "simulation-display-thumbnail-capacitor-lab" );
        loadWithoutError();
        verifyTrue( selenium.isTextPresent( "Capacitor Lab" ) );
        selenium.click( "link=change log" );
        loadWithoutError();
        verifyTrue( selenium.isTextPresent( "1.00 (Dec 7, 2010)" ) );
        selenium.click( "//a[@id='nav-location-nav-teacherIdeas']/span" );
        loadWithoutError();
        verifyTrue( selenium.isTextPresent( SeleniumUtils.getString( "teacherIdeas.adviceSection" ) ) );
        selenium.click( "//a[@id='nav-location-nav-teacherIdeas-browse']/span" );
        loadWithoutError();
        selenium.click( "//a[@id='nav-location-nav-workshops']/span" );
        loadWithoutError();
        selenium.click( "//a[@id='nav-location-nav-teacherIdeas-submit']/span" );
        loadWithoutError();
        selenium.click( "//a[@id='nav-location-nav-teacherIdeas-manage']/span" );
        loadWithoutError();
        selenium.click( "//a[@id='nav-location-nav-about-legend']/span" );
        loadWithoutError();
        selenium.click( "//a[@id='nav-location-nav-stayConnected']/span" );
        loadWithoutError();
        selenium.click( "//a[@id='nav-location-nav-get-phet']/span" );
        loadWithoutError();
        selenium.click( "//a[@id='nav-location-nav-get-phet-full-install']/span" );
        loadWithoutError();
        selenium.click( "//a[@id='nav-location-nav-get-phet-one-at-a-time']/span" );
        loadWithoutError();
        selenium.click( "//a[@id='nav-location-nav-troubleshooting-main']/span" );
        loadWithoutError();
        selenium.click( "//a[@id='nav-location-nav-troubleshooting-java']/span" );
        loadWithoutError();
        selenium.click( "//a[@id='nav-location-nav-troubleshooting-flash']/span" );
        loadWithoutError();
        selenium.click( "//a[@id='nav-location-nav-troubleshooting-javascript']/span" );
        loadWithoutError();
        selenium.click( "//a[@id='nav-location-nav-forTranslators']/span" );
        loadWithoutError();
        selenium.click( "//a[@id='nav-location-nav-forTranslators-translationUtility']/span" );
        loadWithoutError();
        selenium.click( "//a[@id='nav-location-nav-donate']/span" );
        loadWithoutError();
        selenium.click( "//a[@id='nav-location-nav-research']/span" );
        loadWithoutError();
        selenium.click( "//a[@id='nav-location-nav-about']/span" );
        loadWithoutError();
        selenium.click( "//a[@id='nav-location-nav-about-source-code']/span" );
        loadWithoutError();
        selenium.click( "//a[@id='nav-location-nav-about-news']/span" );
        loadWithoutError();
        selenium.click( "//a[@id='nav-location-nav-about-licensing']/span" );
        loadWithoutError();
        selenium.click( "//a[@id='nav-location-nav-about-contact']/span" );
        loadWithoutError();
        selenium.click( "//a[@id='nav-location-nav-sponsors']/span" );
        loadWithoutError();
    }

    @Test
    public void testSignInOutProfile() throws Exception {
        selenium.open( "/" );
        selenium.click( "sign-in-link" );
        loadWithoutError();
        selenium.type( "username", "test@phet.colorado.edu" );
        selenium.type( "password", "test-password" );
        selenium.click( "submit" );
        loadWithoutError();
        selenium.click( "link=Edit profile" );
        loadWithoutError();
        selenium.type( "jobTitle", "New Job Title" );
        selenium.click( "submit" );
        loadWithoutError();
        selenium.click( "link=Edit profile" );
        loadWithoutError();
        verifyEquals( "New Job Title", selenium.getValue( "jobTitle" ) );
        selenium.click( "//img[@alt='PhET Logo']" );
        loadWithoutError();
        selenium.click( "sign-out-link" );
        loadWithoutError();
        verifyTrue( selenium.isTextPresent( SeleniumUtils.getString( "session.loginRegister" ) ) );
    }

    @Test
    public void testKeywordPage() throws Exception {
        selenium.open( "/en/simulations/keyword/electricity" );
        verifyTrue( selenium.isTextPresent( "Balloons and Static Electricity" ) );
        verifyTrue( selenium.isTextPresent( "Charges and Fields" ) );
    }

    @Test
    public void testBrowseContributions() throws Exception {
        selenium.open( "/en/for-teachers/browse-activities" );
        selenium.removeSelection( "contrib-search-sims", "label=All Simulations" );
        selenium.addSelection( "contrib-search-sims", "label=Acid-Base Solutions" );
        selenium.addSelection( "contrib-search-sims", "label=Alpha Decay" );
        selenium.addSelection( "contrib-search-sims", "label=Arithmetic" );
        selenium.addSelection( "contrib-search-sims", "label=Atomic Interactions" );
        selenium.addSelection( "contrib-search-sims", "label=Balloons & Buoyancy" );
        selenium.addSelection( "contrib-search-sims", "label=Balloons and Static Electricity" );
        selenium.addSelection( "contrib-search-sims", "label=Band Structure" );
        selenium.addSelection( "contrib-search-sims", "label=Battery Voltage" );
        selenium.addSelection( "contrib-search-sims", "label=Battery-Resistor Circuit" );
        selenium.addSelection( "contrib-search-sims", "label=Beta Decay" );
        selenium.click( "//input[@value='browse']" );
        loadWithoutError();
        verifyTrue( selenium.isTextPresent( "Inquiry Based Modeling Static Electricity" ) );
    }

    @Test
    public void testContributionView() throws Exception {
        selenium.open( "/en/contributions/view/2847" );
        verifyTrue( selenium.isTextPresent( "Inquiry Based Modeling Static Electricity" ) );
    }


    @After
    public void tearDown() throws Exception {
        selenium.stop();
    }
}
