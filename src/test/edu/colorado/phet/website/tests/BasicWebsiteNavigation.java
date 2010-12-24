package edu.colorado.phet.website.tests;

import javax.swing.*;

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
        selenium.select( "optionsc", "label=" + SeleniumUtils.getString( "contribution.level.graduate" ) );
        selenium.click( "//select[@id='optionsc']/option[2]" );
        selenium.click( "buttond" );
        selenium.click( "submit" );
        loadWithoutError();
        verifyTrue( selenium.isTextPresent( SeleniumUtils.getString( "contribution.edit.validation.mustHaveFiles" ) ) );

        selenium.focus( "upload3" );

        JOptionPane.showMessageDialog( null, "Please select a file, then click here" );

        selenium.click( "submit" );
        loadWithoutError();
        Thread.sleep( 1000 );
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


    @After
    public void tearDown() throws Exception {
        selenium.stop();
    }
}
