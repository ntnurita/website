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

    @Test
    public void testClickOnLogo() throws Exception {
        selenium.open( "/" );
        selenium.click( "//img[@alt='PhET Logo']" );
        selenium.waitForPageToLoad( "30000" );
    }

    @Test
    public void testClickOnPlay() throws Exception {
        selenium.open( "/" );
        selenium.click( IndexPanel.PLAY_SIMS_ID );
        selenium.waitForPageToLoad( "30000" );
    }

    @Test
    public void testSignIn() throws Exception {
        // checks validation

        String signInFailed = SeleniumUtils.getString( "signIn.validation.failed" );
        String signInTitle = SeleniumUtils.getString( "signIn.title" );

        selenium.open( "/" );
        selenium.click( LogInOutPanel.SIGN_IN_ID );
        selenium.waitForPageToLoad( "30000" );
        selenium.type( "username", "bogus-email" );
        selenium.type( "password", "bogus-password" );
        selenium.click( "submit" );
        selenium.waitForPageToLoad( "30000" );
        assert ( selenium.getBodyText().contains( signInFailed ) );
        assertEquals( selenium.getTitle(), signInTitle );
        selenium.type( "username", "test@phet.colorado.edu" );
        selenium.click( "submit" );
        selenium.waitForPageToLoad( "30000" );
        assert ( selenium.getBodyText().contains( signInFailed ) );
        assertEquals( selenium.getTitle(), signInTitle );
        selenium.type( "password", "test-password" );
        selenium.click( "submit" );
        selenium.waitForPageToLoad( "30000" );
        assert ( !selenium.getBodyText().contains( signInFailed ) );
        assertNotEquals( selenium.getTitle(), signInTitle );
    }

    @After
    public void tearDown() throws Exception {
        selenium.stop();
    }
}
