package edu.colorado.phet.website.tests;

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
        selenium.waitForPageToLoad( "30000" );
        selenium.type( "username", "" );
        selenium.type( "username", "test@phet.colorado.edu" );
        selenium.type( "password", "test-password" );
        selenium.click( "submit" );
        selenium.waitForPageToLoad( "30000" );
        selenium.click( "home-submit-activity-link" );
        selenium.waitForPageToLoad( "30000" );
        selenium.click( "//a[@id='nav-location-nav-teacherIdeas-submit']/span" );
        selenium.waitForPageToLoad( "30000" );
        selenium.click( "submit" );
        selenium.waitForPageToLoad( "30000" );
        verifyTrue( selenium.isTextPresent( "Please enter a title for the contribution" ) );
        selenium.type( "cep-title", "Sample Title" );
        selenium.click( "submit" );
        selenium.waitForPageToLoad( "30000" );
        verifyTrue( selenium.isTextPresent( "Please enter keywords for the contribution" ) );
        selenium.type( "cep-keywords", "key1,key2" );
        selenium.click( "submit" );
        selenium.waitForPageToLoad( "30000" );
        verifyTrue( selenium.isTextPresent( "The activity must reference at least one simulation" ) );
        selenium.select( "options6", "label=Acid-Base Solutions" );
        selenium.click( "//option[@value='100']" );
        selenium.click( "button7" );
        selenium.click( "submit" );
        selenium.waitForPageToLoad( "30000" );
        verifyTrue( selenium.isTextPresent( "The activity must specify at least one type" ) );
        selenium.select( "options9", "label=Concept Questions" );
        selenium.click( "//select[@id='options9']/option[2]" );
        selenium.click( "buttona" );
        selenium.select( "optionsc", "label=Graduate" );
        selenium.click( "//select[@id='optionsc']/option[2]" );
        selenium.click( "buttond" );
        selenium.click( "submit" );
        selenium.waitForPageToLoad( "30000" );
        verifyTrue( selenium.isTextPresent( "The activity must contain at least one file." ) );
        selenium.type( "upload3", "/home/jon/tmp/upload.pdf" );
        selenium.click( "submit" );
        selenium.waitForPageToLoad( "30000" );
        verifyTrue( selenium.isTextPresent( "Update Success" ) );
        selenium.click( "link=Continue to the activity >>" );
        selenium.waitForPageToLoad( "30000" );
        verifyTrue( selenium.isTextPresent( "Sample Title" ) );
        selenium.click( "//a[@id='nav-location-nav-teacherIdeas-manage']/span" );
        selenium.waitForPageToLoad( "30000" );
        selenium.click( "link=delete" );
        selenium.waitForPageToLoad( "30000" );
    }

    @After
    public void tearDown() throws Exception {
        selenium.stop();
    }
}
