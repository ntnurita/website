package edu.colorado.phet.website.tests;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

public class SeleniumUtils {
    public static final String WEB_SERVER = "192.168.1.64";
    public static final int PORT = 4444;

    public static Selenium createDefaultSelenium() {
        return new DefaultSelenium( "localhost", SeleniumUtils.PORT, "*firefox", "http://" + SeleniumUtils.WEB_SERVER + "/" );
    }
}
