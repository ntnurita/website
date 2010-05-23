package edu.colorado.phet.website.tests;

import org.junit.Test;

import edu.colorado.phet.website.data.Changelog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChangelogTests {
    @Test
    /**
     * Tests parsing of the correct changelog format
     */
    public void testCorrectFormat() {
        String logString = "# 3.22.00 (38666) Feb 16, 2010\n" +
                           "2/16/10 Deployment to production server.\n" +
                           "# 3.21.01 (38614) Feb 12, 2010\n" +
                           "2/12/10 > Fixed an issue where U-238 was shown decaying into Pb-207 instead of Pb-206.\n" +
                           "# 3.21.00 (38193) Jan 22, 2010\n" +
                           "1/22/10 Deployment to production server.";

        Changelog devLog = new Changelog( logString );
        System.out.println( "*** dev log: \n" + devLog + "\n\n" );

        Changelog log = devLog.getNonDevChangelog();
        System.out.println( "*** non-dev log: \n" + log + "\n\n" );

        /*---------------------------------------------------------------------------*
        * development log tests
        *----------------------------------------------------------------------------*/

        assertTrue( devLog.getEntries().size() == 3 );
        Changelog.Entry dev1 = devLog.getEntries().get( 0 );
        assertTrue( dev1.getMajorVersion() == 3 );
        assertTrue( dev1.getDevVersion() == 0 );
        assertTrue( dev1.getRevision() == 38666 );
        assertTrue( dev1.getDate() != null );
        assertTrue( dev1.getLines().size() == 1 );
        assertTrue( !dev1.getLines().get( 0 ).isVisible() );
        assertTrue( dev1.getLines().get( 0 ).getMessage().equals( "Deployment to production server." ) );
        Changelog.Entry dev2 = devLog.getEntries().get( 1 );
        assertTrue( dev2.getLines().size() == 1 );
        Changelog.Line devE = dev2.getLines().get( 0 );
        assertTrue( devE.isVisible() );
        assertTrue( devE.getDate() != null );
        assertTrue( devE.getMessage().equals( "Fixed an issue where U-238 was shown decaying into Pb-207 instead of Pb-206." ) );

        /*---------------------------------------------------------------------------*
        * visible log tests
        *----------------------------------------------------------------------------*/

        assertTrue( log.getEntries().size() == 2 );
        Changelog.Entry e1 = devLog.getEntries().get( 0 );
        assertTrue( e1.getMajorVersion() == 3 );
        assertTrue( e1.getDevVersion() == 0 );
        assertTrue( e1.getRevision() == 38666 );
        assertTrue( e1.getDate() != null );
        assertTrue( e1.getLines().size() == 1 );
        for ( Changelog.Entry entry : log.getEntries() ) {
            assertTrue( entry.getDevVersion() == 0 );
            for ( Changelog.Line line : entry.getLines() ) {
                assertTrue( line.isVisible() );
            }
        }

    }

    @Test
    /**
     * Tests parsing of weird changelog formats
     */
    public void testUglyFormat() {
        String logString = "# 2.04.00 (40105) Jan 10, 2010\n" +
                           "1/10/10 > Something visible\n" +
                           "1/10/10 >Something visible\n" +
                           "1/10/10 Something invisible\n" +
                           "> Another visible thing\n" +
                           ">Yet another visible thing\n" +
                           "Something invisible\n" +
                           "\n" +
                           "\n" +
                           "# 2.02.03 (28282) 02-09-2009\n" +
                           "# 1.01.11 12-31-2008\n" +
                           "# 1.01.05\n" +
                           "#### I AM A COMMENT BECAUSE I HAVE TOO MANY #s\n" +
                           "I AM A GARBAGE LINE";

        Changelog devLog = new Changelog( logString );
        System.out.println( "*** dev log: \n" + devLog + "\n\n" );

        Changelog log = devLog.getNonDevChangelog();
        System.out.println( "*** non-dev log: \n" + log + "\n\n" );

        assertEquals( devLog.getEntries().size(), 4 );
        Changelog.Entry firstEntry = devLog.getEntries().get( 0 );
        assertEquals( firstEntry.getLines().size(), 6 );

        assertTrue( firstEntry.getLines().get( 0 ).getDate() != null );
        assertTrue( firstEntry.getLines().get( 0 ).isVisible() );
        assertTrue( firstEntry.getLines().get( 0 ).getMessage().equals( "Something visible" ) );

        assertTrue( firstEntry.getLines().get( 1 ).getDate() != null );
        assertTrue( firstEntry.getLines().get( 1 ).isVisible() );
        assertTrue( firstEntry.getLines().get( 1 ).getMessage().equals( "Something visible" ) );

        assertTrue( firstEntry.getLines().get( 2 ).getDate() != null );
        assertTrue( !firstEntry.getLines().get( 2 ).isVisible() );
        assertTrue( firstEntry.getLines().get( 2 ).getMessage().equals( "Something invisible" ) );

        assertTrue( firstEntry.getLines().get( 3 ).getDate() == null );
        assertTrue( firstEntry.getLines().get( 3 ).isVisible() );
        assertTrue( firstEntry.getLines().get( 3 ).getMessage().equals( "Another visible thing" ) );

        assertTrue( firstEntry.getLines().get( 4 ).getDate() == null );
        assertTrue( firstEntry.getLines().get( 4 ).isVisible() );
        assertTrue( firstEntry.getLines().get( 4 ).getMessage().equals( "Yet another visible thing" ) );

        assertTrue( firstEntry.getLines().get( 5 ).getDate() == null );
        assertTrue( !firstEntry.getLines().get( 5 ).isVisible() );
        assertTrue( firstEntry.getLines().get( 5 ).getMessage().equals( "Something invisible" ) );

        assertTrue( devLog.getEntries().get( 1 ).getDate() != null );
        assertTrue( devLog.getEntries().get( 2 ).getDate() != null );
    }
}