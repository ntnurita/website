/*
 * Copyright 2011, University of Colorado
 */

package edu.colorado.phet.website.tests;

import org.junit.Test;

import edu.colorado.phet.website.PhetWicketApplication;
import edu.colorado.phet.website.util.DataTrie;

import static org.junit.Assert.assertEquals;

public class DataTrieTest {
    @Test
    public void testStringTrie1() {
        DataTrie<TestData> trie = new DataTrie<TestData>( PhetWicketApplication.getDefaultLocale() ) {
            @Override
            protected String toString( TestData data ) {
                return data.getStr();
            }
        };
        System.out.println( "---\n" + trie + "\n---" );
        for ( String str : new String[]{
                "This is a test",
                "focus",
                "this is not a test",
                "this is a test", // duplicate
                "This was a test",
                "this is",
                "thIs",
                "th",
                "than"
        } ) {
            System.out.println( "adding " + str );
            trie.add( new TestData( str ) );
            System.out.println( "---\n" + trie + "\n---" );
        }

        System.out.flush();

        for ( TestData ob : trie.getStartingWith( "this" ) ) {
            System.out.println( "::" + ob.getStr() );
        }

        assertEquals( trie.getStartingWith( "this" ).size(), 5 );
        assertEquals( trie.getStartingWith( "this is" ).size(), 3 );
        assertEquals( trie.getStartingWith( "bark" ).size(), 0 );
        assertEquals( trie.getStartingWith( "this is a" ).size(), 1 );
        assertEquals( trie.getStartingWith( "fo" ).size(), 1 );
        assertEquals( trie.getStartingWith( "focus" ).size(), 1 );
        assertEquals( trie.getStartingWith( "focused" ).size(), 0 );
        assertEquals( trie.getStartingWith( "folly" ).size(), 0 );
        assertEquals( trie.getStartingWith( "" ).size(), 8 );
        assertEquals( trie.getStartingWith( "th" ).size(), 7 );
    }
}

class TestData {
    private String str;

    TestData( String str ) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }
}
