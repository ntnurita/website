/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website;

import java.io.File;

import javax.servlet.ServletContext;

public class WebsiteProperties {
    private ServletContext servletContext;

    public static final String PHET_DOCUMENT_ROOT = "phet-document-root";
    public static final String PHET_DOWNLOAD_ROOT = "phet-download-root";
    public static final String PHET_DOWNLOAD_LOCATION = "phet-download-location";
    public static final String BUILD_LOCAL_PROPERTIES = "build-local-properties";
    public static final String PATH_TO_JAR_JDK = "path-to-jar-jdk";
    public static final String SIM_STAGING_AREA = "sim-staging-area";

    public static final String MAIL_HOST = "mail-host";
    public static final String MAIL_USER = "mail-user";
    public static final String MAIL_PASSWORD = "mail-password";

    public static final String NEWSLETTER_FILE = "newsletter-file";

    public static final String HTTPS_AVAILABLE = "https-available";

    public static final String WEB_HOSTNAME = "web-hostname";
    public static final String SIMULATION_METADATA = "simulation-metadata";

    public static final String GPG_PUBLIC_KEY_LOCATION = "gpg-public-key-location";
    public static final String GPG_PUBLIC_KEY_FILE = "gpg-public-key-file";
    public static final String GPG_PRIVATE_KEY_FILE = "gpg-private-key-file";
    public static final String GPG_PASSPHRASE = "gpg-passphrase";

    public static final String LEARNING_REGISTRY_NODE_HOST = "learning-registry-node-host";
    public static final String LEARNING_REGISTRY_USER = "learning-registry-user";
    public static final String LEARNING_REGISTRY_PASSWORD = "learning-registry-password";
    public static final String LEARNING_REGISTRY_PROTOCOL = "learning-registry-protocol";

    public WebsiteProperties( ServletContext servletContext ) {
        this.servletContext = servletContext;
    }

    public File getPhetDocumentRoot() {
        return getFileFromLocation( getParameter( PHET_DOCUMENT_ROOT ) );
    }

    public File getPhetDownloadRoot() {
        return getFileFromLocation( getParameter( PHET_DOWNLOAD_ROOT ) );
    }

    public File getBuildLocalPropertiesFile() {
        return new File( getParameter( BUILD_LOCAL_PROPERTIES ) );
    }

    public File getNewsletterFile() {
        return new File( getParameter( NEWSLETTER_FILE ) );
    }

    public File getGPGPublicKeyFile() {
        return new File( getParameter( GPG_PUBLIC_KEY_FILE ) );
    }

    public File getGPGPrivateKeyFile() {
        return new File( getParameter( GPG_PRIVATE_KEY_FILE ) );
    }

    public String getGPGPublicKeyLocation() {
        return getParameter( GPG_PUBLIC_KEY_LOCATION );
    }

    public String getGPGPassphrase() {
        return getParameter( GPG_PASSPHRASE );
    }

    public String getLearningRegistryNodeHost() {
        return getParameter( LEARNING_REGISTRY_NODE_HOST );
    }

    public String getLearningRegistryUser() {
        return getParameter( LEARNING_REGISTRY_USER );
    }

    public String getLearningRegistryPassword() {
        return getParameter( LEARNING_REGISTRY_PASSWORD );
    }

    public boolean hasLearningRegistryCredentials() {
        return getParameter( LEARNING_REGISTRY_USER ) != null && getParameter( LEARNING_REGISTRY_PASSWORD ) != null;
    }

    public String getLearningRegistryProtocol() {
        return getParameter( LEARNING_REGISTRY_PROTOCOL );
    }

    public String getPhetDownloadLocation() {
        return getParameter( PHET_DOWNLOAD_LOCATION );
    }

    public String getPathToJarJdk() {
        return getParameter( PATH_TO_JAR_JDK );
    }

    public boolean hasMailParameters() {
        return hasParameter( MAIL_HOST ) && hasParameter( MAIL_USER ) && hasParameter( MAIL_PASSWORD );
    }

    public String getMailHost() {
        return getParameter( MAIL_HOST );
    }

    public String getMailUser() {
        return getParameter( MAIL_USER );
    }

    public String getMailPassword() {
        return getParameter( MAIL_PASSWORD );
    }

    public boolean isHttpsAvailable() {
        String https = getParameter( HTTPS_AVAILABLE );
        return https != null && https.equals( "yes" );
    }

    public String getWebHostname() {
        return getParameter( WEB_HOSTNAME );
    }

    public File getSimulationMetadataDir() {
        return new File( getParameter( SIMULATION_METADATA ) );
    }

    /**
     * @return Returns the directory where simulations being deployed should be located. By convention, it should contain
     *         subdirectories named after their respective projects
     */
    public File getSimStagingArea() {
        return new File( getParameter( SIM_STAGING_AREA ) );
    }

    private String getParameter( String paramName ) {
        return servletContext.getInitParameter( paramName );
    }

    private boolean hasParameter( String paramName ) {
        return getParameter( paramName ) != null;
    }

    private static File getFileFromLocation( String location ) {
        if ( location == null ) {
            return null;
        }
        File file = new File( location );
        if ( !file.exists() ) {
            return null;
        }
        return file;
    }
}
