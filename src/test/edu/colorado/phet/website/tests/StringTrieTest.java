/*
 * Copyright 2011, University of Colorado
 */

package edu.colorado.phet.website.tests;

import org.junit.Test;

import edu.colorado.phet.website.util.StringTrie;

import static org.junit.Assert.assertEquals;

public class StringTrieTest {
    @Test
    public void testStringTrie1() {
        StringTrie trie = new StringTrie();
        System.out.println( "---\n" + trie );
        trie.add( "this is a test" );
        System.out.println( "---\n" + trie );
        trie.add( "focus" );
        System.out.println( "---\n" + trie );
        trie.add( "this is not a test" );
        System.out.println( "---\n" + trie );
        trie.add( "this is a test" );
        System.out.println( "---\n" + trie );
        trie.add( "this was a test" );
        System.out.println( "---\n" + trie );
        trie.add( "partial 1" );
        System.out.println( "---\n" + trie );
        trie.add( "partial" );
        System.out.println( "---\n" + trie );
        trie.add( "partial 2" );
        System.out.println( "---\n" + trie );

        assertEquals( trie.getStartingWith( "this" ).size(), 3 );
        assertEquals( trie.getStartingWith( "this is" ).size(), 2 );
        assertEquals( trie.getStartingWith( "bark" ).size(), 0 );
        assertEquals( trie.getStartingWith( "this is a" ).size(), 1 );
        assertEquals( trie.getStartingWith( "fo" ).size(), 1 );
        assertEquals( trie.getStartingWith( "focus" ).size(), 1 );
        assertEquals( trie.getStartingWith( "focused" ).size(), 0 );
        assertEquals( trie.getStartingWith( "folly" ).size(), 0 );
        assertEquals( trie.getStartingWith( "" ).size(), 7 );
    }
}
