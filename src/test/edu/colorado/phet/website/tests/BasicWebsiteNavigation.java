package edu.colorado.phet.website.tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.colorado.phet.website.content.IndexPanel;

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

    @After
    public void tearDown() throws Exception {
        selenium.stop();
    }
}
