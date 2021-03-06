/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.admin.deploy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import edu.colorado.phet.buildtools.BuildLocalProperties;
import edu.colorado.phet.buildtools.JARGenerator;
import edu.colorado.phet.buildtools.resource.ResourceDeployUtils;
import edu.colorado.phet.buildtools.util.PhetJarSigner;
import edu.colorado.phet.common.phetcommon.util.FileUtils;
import edu.colorado.phet.common.phetcommon.util.IProguardKeepClass;
import edu.colorado.phet.common.phetcommon.util.StreamReaderThread;
import edu.colorado.phet.website.PhetWicketApplication;

/**
 * Builds all files necessary to test and run simulations with the new resource before they are published (copied into
 * the live sim directories)
 * <p/>
 * See WebsiteResourceDeployClient for the main documentation.
 * <p/>
 * NOTE: run on the server, so do not rename / move this without changing the other references under
 * edu.colorado.phet.buildtools.resource
 */
public class WebsiteResourceDeployServer implements IProguardKeepClass {

    private String jarCommand;
    private File resourceDir;

    private boolean ready = false;

    private File resourceFile;
    private String resourceDestination;
    private String[] projectNames;
    private File backupDir;
    private String mode;
    private boolean onlyAllJARs;
    private boolean generateJARs;
    private boolean copySWFs;
    private boolean copyJNLPs;
    private File testDir;
    private List<String> existingProjects;
    private File liveSimsDir;

    private static final boolean localBackup = false;

    private static final Logger logger = Logger.getLogger( WebsiteResourceDeployServer.class.getName() );

    public WebsiteResourceDeployServer( File resourceDir ) {
        this.resourceDir = resourceDir;

        File propertiesFile = ResourceDeployUtils.getResourceProperties( resourceDir );
        Properties properties = new Properties();

        try {
            FileInputStream inputStream = new FileInputStream( propertiesFile );
            try {
                properties.load( inputStream );
                String resourceFilename = properties.getProperty( "resourceFile" );
                resourceFile = new File( ResourceDeployUtils.getResourceSubDir( resourceDir ), resourceFilename );

                if ( !resourceFile.exists() ) {
                    logger.info( "Cannot locate resource file, aborting" );
                    return;
                }

                resourceDestination = properties.getProperty( "resourceDestination" );
                if ( resourceDestination.startsWith( "/" ) ) {
                    resourceDestination = resourceDestination.substring( 1 );
                }

                mode = properties.getProperty( "mode" );
                if ( mode.equals( "java" ) ) {
                    onlyAllJARs = true;
                    generateJARs = true;
                    copySWFs = false;
                    copyJNLPs = true;
                }
                else if ( mode.equals( "flash" ) ) {
                    onlyAllJARs = false;
                    generateJARs = false;
                    copySWFs = true;
                    copyJNLPs = false;
                }

                String simsString = properties.getProperty( "sims" );
                projectNames = simsString.split( "," );

                // prune projectNames list so it only includes projectNames deployed to the server
                existingProjects = new LinkedList<String>();
                for ( String projectName : projectNames ) {
                    File projectDir = new File( getLiveSimsDir(), projectName );
                    if ( projectDir.exists() ) {
                        existingProjects.add( projectName );
                    }
                    else {
                        logger.warn( "project " + projectName + " was not found on the production server" );
                    }
                }
                projectNames = existingProjects.toArray( new String[existingProjects.size()] );

                ready = true;
            }
            finally {
                inputStream.close();
            }
        }
        catch ( FileNotFoundException e ) {
            e.printStackTrace();
        }
        catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public boolean process( File liveSimsDir ) {
        this.liveSimsDir = liveSimsDir;

        if ( !ready ) {
            logger.error( "did not initialize correctly, cannot process" );
            return false;
        }
        try {
            if ( localBackup ) {
                backupDir = new File( resourceDir, "backup" );
                backupDir.mkdir();
            }

            if ( localBackup ) {
                logger.info( "Creating backup JARs" );
                createBackupJARs();
            }
            else {
                logger.info( "Skipping backup JARs" );
            }

            logger.info( "Copying test JARs" );
            copyTestJARs();

            logger.info( "Poking JARs" );
            pokeJARs();

            logger.info( "Signing JARs" );
            signAndPackJARs();

            if ( generateJARs ) {
                logger.info( "Generating offline JARs" );
                generateOfflineJARs();
            }
            else {
                logger.info( "Skipping generating offline JARs" );
            }

            if ( localBackup ) {
                logger.info( "Backing up extra files" );
                backupExtras();
            }
            else {
                logger.info( "Skipping backup of extra files" );
            }

            logger.info( "Copying extra files" );
            copyExtras();

            if ( copySWFs ) {
                logger.info( "Copying Flash SWFs" );
                copyFlashSWFs();
            }

            if ( copyJNLPs ) {
                logger.info( "Copying Java JNLPs" );
                copyJavaJNLPs();
            }
        }
        catch ( IOException e ) {
            e.printStackTrace();
            return false;
        }
        catch ( InterruptedException e ) {
            e.printStackTrace();
            return false;
        }

        logger.info( "All successful!" );

        return true;

    }

    public List<String> getExistingProjects() {
        return existingProjects;
    }

    public static Logger getLogger() {
        return logger;
    }

    private void copyJavaJNLPs() throws IOException {
        testDir = ResourceDeployUtils.getTestDir( resourceDir );

        for ( String projectName : projectNames ) {
            File backupSimDir;
            if ( localBackup ) {
                backupSimDir = new File( backupDir, projectName );
            }
            File testSimDir = new File( testDir, projectName );
            File simDir = new File( getLiveSimsDir(), projectName );

            File[] jarFiles = simDir.listFiles( new FilenameFilter() {
                public boolean accept( File file, String s ) {
                    return s.endsWith( ".jar" ) && !s.endsWith( "_all.jar" );
                }
            } );

            for ( File jarFile : jarFiles ) {
                String jnlpName = jarFile.getName().replace( ".jar", ".jnlp" );
                File baseJnlpFile = new File( simDir, jnlpName );
                if ( !baseJnlpFile.exists() ) {
                    try {
                        logger.warn( "JNLP does not exist: " + baseJnlpFile.getCanonicalPath() );
                    }
                    catch ( IOException e ) {
                        e.printStackTrace();
                    }
                    finally {
                        continue;
                    }
                }
                if ( localBackup ) {
                    FileUtils.copyToDir( baseJnlpFile, backupSimDir );
                }
                FileUtils.copyToDir( baseJnlpFile, testSimDir );

                File testJnlp = new File( testSimDir, jnlpName );
                modifyJnlp( testJnlp );
            }

        }
    }

    private void modifyJnlp( File jnlpFile ) throws IOException {
        String str = FileUtils.loadFileAsString( jnlpFile, "utf-8" );
        str = str.replace( "http://phet.colorado.edu/sims", "http://phet.colorado.edu/staging/resources/" + resourceDir.getName() + "/test" );
        FileUtils.writeString( jnlpFile, str, "utf-8" );
    }

    private void createBackupJARs() throws IOException {
        for ( String sim : projectNames ) {
            File simDir = new File( getLiveSimsDir(), sim );

            if ( !simDir.exists() ) {
                logger.warn( "skipping sim dir " + simDir.getCanonicalPath() + ", does not exist" );
                continue;
            }

            File[] jarFiles = simDir.listFiles( new FilenameFilter() {
                public boolean accept( File file, String name ) {
                    return name.endsWith( ".jar" );
                }
            } );

            if ( jarFiles.length == 0 ) {
                logger.warn( "no JARs found in sim dir: " + simDir.getCanonicalPath() );
                continue;
            }

            File simBackupDir = new File( backupDir, sim );
            simBackupDir.mkdir();

            for ( File jarFile : jarFiles ) {
                FileUtils.copyToDir( jarFile, simBackupDir );
            }
        }
    }

    private void copyTestJARs() throws IOException, InterruptedException {
        testDir = ResourceDeployUtils.getTestDir( resourceDir );
        logger.info( "Creating test directory at: " + testDir.getAbsolutePath() );
        testDir.mkdir();


        for ( String projectName : projectNames ) {
            logger.info( "  processing " + projectName );

            File projectDir = new File( getLiveSimsDir(), projectName );
            File testSimDir = new File( testDir, projectName );
            logger.info( "  Simulation source directory: " + projectDir.getAbsolutePath() );
            logger.info( "  Creating simulation test destination directory: " + testSimDir.getAbsolutePath() );
            testSimDir.mkdir();

            File[] jarFiles = projectDir.listFiles();

            for ( File jarFile : jarFiles ) {
                if ( onlyAllJARs && !jarFile.getName().endsWith( "_all.jar" ) ) {
                    continue;
                }

                FileUtils.copyToDir( jarFile, testSimDir );
                logger.info( "      copying " + jarFile.getAbsolutePath() + " to directory " + testSimDir.getAbsolutePath() );
            }
        }
    }

    private void pokeJARs() throws IOException, InterruptedException {
        File tmpDir = new File( resourceDir, ".tmp" );
        tmpDir.mkdir();

        File holderDir = new File( tmpDir, resourceDestination );
        holderDir.mkdirs();

        FileUtils.copyToDir( resourceFile, holderDir );

        for ( String projectName : projectNames ) {
            logger.info( "  processing " + projectName );

            File testSimDir = new File( testDir, projectName );

            File[] jarFiles = testSimDir.listFiles( new FilenameFilter() {
                public boolean accept( File file, String s ) {
                    return s.endsWith( ".jar" );
                }
            } );

            for ( File jarFile : jarFiles ) {
                String command = PhetWicketApplication.get().getWebsiteProperties().getPathToJarJdk() + "/bin/jar" + " uf " + jarFile.getAbsolutePath() + " -C " + tmpDir.getAbsolutePath() + " " + resourceDestination + resourceFile.getName();
                runStringCommand( command );
            }
        }
    }

    private void signAndPackJARs() {
        for ( String projectName : projectNames ) {
            logger.info( "  processing " + projectName );

            File testSimDir = new File( testDir, projectName );
            File[] jarFiles = testSimDir.listFiles( new FilenameFilter() {
                public boolean accept( File file, String s ) {
                    return s.endsWith( ".jar" );
                }
            } );

            for ( File jarFile : jarFiles ) {
                if ( jarFile.getName().endsWith( "_all.jar" ) ) {
                    // only pack our _all.jars
                    packAndSignJAR( jarFile );
                }
                else {
                    signJAR( jarFile );
                }
            }
        }
    }

    private void signJAR( File jarFile ) {
        PhetJarSigner phetJarSigner = new PhetJarSigner( BuildLocalProperties.getInstance() );
        phetJarSigner.signJar( PhetWicketApplication.get().getWebsiteProperties().getPathToJarJdk(), jarFile );
    }

    private void packAndSignJAR( File jarFile ) {
        PhetJarSigner phetJarSigner = new PhetJarSigner( BuildLocalProperties.getInstance() );
        phetJarSigner.createExtraJars( PhetWicketApplication.get().getWebsiteProperties().getPathToJarJdk(), jarFile );
    }

    private void generateOfflineJARs() throws IOException, InterruptedException {
        JARGenerator generator = new JARGenerator();
        for ( String projectName : projectNames ) {
            logger.info( "  processing " + projectName );

            File testSimDir = new File( testDir, projectName );
            File[] jarFiles = testSimDir.listFiles( new FilenameFilter() {
                public boolean accept( File dir, String name ) {
                    // only accept JARs!
                    return name.endsWith( ".jar" );
                }
            } );

            for ( File jarFile : jarFiles ) {
                logger.info( "generating offline JARs for jar: " + jarFile.getAbsolutePath() );
                generator.generateOfflineJARs( jarFile, BuildLocalProperties.getInstance(), PhetWicketApplication.get().getWebsiteProperties().getPathToJarJdk() );
            }
        }
    }

    private void backupExtras() throws IOException {
        File liveDir = getLiveSimsDir();
        File extrasDir = ResourceDeployUtils.getExtrasDir( resourceDir );
        if ( !extrasDir.exists() ) {
            return;
        }

        File[] simExtraDirs = extrasDir.listFiles();

        for ( File simExtraDir : simExtraDirs ) {
            String sim = simExtraDir.getName();
            logger.info( "  processing " + sim );

            File[] extraFiles = simExtraDir.listFiles();

            for ( File extraFile : extraFiles ) {
                File liveExtraFile = new File( liveDir, sim + "/" + extraFile.getName() );
                if ( liveExtraFile.exists() ) {
                    File backupExtraDir = new File( backupDir, sim );
                    backupExtraDir.mkdirs();

                    logger.info( "Copying live extra file " + liveExtraFile.getCanonicalPath() + " to " + backupExtraDir.getCanonicalPath() );
                    FileUtils.copyToDir( liveExtraFile, backupExtraDir );
                }
                else {
                    logger.warn( "Live extra file does not exist: " + liveExtraFile.getCanonicalPath() );
                }
            }
        }
    }

    private void copyExtras() throws IOException {
        logger.info( "copyExtras()" );
        File extrasDir = ResourceDeployUtils.getExtrasDir( resourceDir );
        if ( !extrasDir.exists() ) {
            return;
        }

        File[] simExtraDirs = extrasDir.listFiles();

        for ( File simExtraDir : simExtraDirs ) {
            String projectName = simExtraDir.getName();
            logger.info( "  processing " + projectName );

            File[] extraFiles = simExtraDir.listFiles();

            for ( File extraFile : extraFiles ) {
                File testExtraDir = new File( testDir, projectName );
                testExtraDir.mkdirs();

                FileUtils.copyToDir( extraFile, testExtraDir );
            }
        }
    }

    private void copyFlashSWFs() throws IOException {
        File[] testSimDirs = testDir.listFiles();

        for ( File testSimDir : testSimDirs ) {
            String projectName = testSimDir.getName();
            logger.info( "  processing " + projectName );

            File liveSimDir = new File( liveSimsDir, projectName );

            File[] swfFiles = liveSimDir.listFiles( new FilenameFilter() {
                public boolean accept( File dir, String name ) {
                    return name.endsWith( ".swf" );
                }
            } );
            for ( File swfFile : swfFiles ) {
                if ( !swfFile.exists() ) {
                    logger.warn( "sim SWF doesn't exist: " + projectName + "/" + swfFile.getName() + " at expected location " + swfFile.getAbsolutePath() );
                    continue;
                }

                // copy the SWF to the test dir so that we can test the new generated HTMLs
                FileUtils.copyToDir( swfFile, testSimDir );

                if ( localBackup ) {
                    // copy the SWF to the backup sim dir so that we will have them for posterity (and a warning won't be seen
                    // if the user reverts)
                    File backupSimDir = new File( backupDir, projectName );
                    backupSimDir.mkdirs();
                    FileUtils.copyToDir( swfFile, backupSimDir );
                }
            }
        }
    }

    public File getLiveSimsDir() {
        return new File( resourceDir, "../../../sims" );
    }

    private void runStringCommand( String command ) throws IOException, InterruptedException {
        logger.info( "Running command: " + command );
        Process p = Runtime.getRuntime().exec( command );
        new StreamReaderThread( p.getErrorStream(), "err>" ).start();
        new StreamReaderThread( p.getInputStream(), "" ).start();
        p.waitFor();
    }

}