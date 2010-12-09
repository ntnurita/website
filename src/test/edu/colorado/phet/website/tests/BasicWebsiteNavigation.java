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

    @After
    public void tearDown() throws Exception {
        selenium.stop();
    }
}
