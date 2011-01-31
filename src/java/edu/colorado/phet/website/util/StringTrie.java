/*
 * Copyright 2011, University of Colorado
 */

package edu.colorado.phet.website.util;

import java.text.CollationKey;
import java.text.Collator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Unsorted String Trie. This means that given a string 'str', it should be able to efficiently add it to the trie, or
 * find all contained strings that start with 'str'
 */
public class StringTrie {
    private Locale locale;
    private StringTrieNode root;
    private Collator collator;

    public StringTrie( Locale locale ) {
        this( locale, Collator.PRIMARY );
    }

    public StringTrie( Locale locale, int collatorStrength ) {
        this.locale = locale;
        this.root = new StringTrieNode( 0, this );
        this.collator = Collator.getInstance( locale );
        this.collator.setStrength( collatorStrength );
    }

    /**
     * Returns all strings that start with the substring start
     *
     * @param start Starting substring
     * @return All strings that start with the substring start
     */
    public List<String> getStartingWith( String start ) {
        List<String> ret = new LinkedList<String>();
        StringTrieNode node = root;
        for ( char character : start.toCharArray() ) {
            CollationKey key = collator.getCollationKey( String.valueOf( character ) );
            StringTrieNode next = node.getChild( key );
            if ( next == null ) {
                if ( node.isLeaf() && startsWith( start, node.string ) ) {
                    ret.add( node.string );
                }
                node = next;
                break;
            }
            node = next;
        }
        if ( node == null ) {
            return ret;
        }
        node.addChildStringsToList( ret );
        return ret;
    }

    /**
     * Adds a string to the trie
     *
     * @param str String to add
     */
    public void add( String str ) {
        root.addString( str );
    }

    @Override
    public String toString() {
        return root.toString();
    }

    protected boolean equivalent( String a, String b ) {
        return collator.compare( a, b ) == 0;
    }

    protected boolean equivalent( char a, char b ) {
        return collator.compare( String.valueOf( a ), String.valueOf( b ) ) == 0;
    }

    protected CollationKey stringToKey( String str ) {
        return collator.getCollationKey( str );
    }

    /**
     * @return Whether str starts with a i18nized case-insensitive prefix
     */
    protected boolean startsWith( String prefix, String str ) {
        if ( str.length() < prefix.length() ) {
            return false;
        }
        return equivalent( prefix, str.substring( 0, prefix.length() ) );
    }
}

class StringTrieNode {
    public String string;
    public HashMap<CollationKey, StringTrieNode> children;
    public int depth;
    public StringTrie trie;

    StringTrieNode( int depth, StringTrie trie ) {
        this.depth = depth;
        this.trie = trie;
    }

    public boolean isEmpty() {
        return string == null && ( children == null );
    }

    public boolean isLeaf() {
        return children == null;
    }

    public StringTrieNode getChild( CollationKey key ) {
        if ( children == null ) {
            return null;
        }
        return children.get( key );
    }

    public void addChildStringsToList( List<String> strings ) {
        if ( string != null ) {
            strings.add( string );
        }
        if ( children != null ) {
            for ( StringTrieNode node : children.values() ) {
                node.addChildStringsToList( strings );
            }
        }
    }

    private void convertToBranch() {
        string = null;
        children = new HashMap<CollationKey, StringTrieNode>();
    }

    public void addString( String str ) {
        if ( isEmpty() ) {
            // we must be the root
            string = str;
            return;
        }

        // if the string to add is a duplicate of our existing string, bail
        if ( string != null && string.length() == str.length() && trie.equivalent( str, string ) ) {
            return;
        }

        if ( isLeaf() ) {
            // we are a leaf. convert to a branch
            String other = string;
            convertToBranch();

            // add the two strings in
            addString( other );
            addString( str );
        }
        else {
            // not a leaf node
            if ( str.length() == depth ) {
                // we are adding a shorter version of existing strings
                string = str;
            }
            else {
                CollationKey key = trie.stringToKey( str.substring( depth, depth + 1 ) );
                StringTrieNode possibleChild = getChild( key );
                if ( possibleChild != null ) {
                    // follow the path
                    possibleChild.addString( str );
                }
                else {
                    // create a new node
                    StringTrieNode child = new StringTrieNode( depth + 1, trie );
                    child.string = str;
                    children.put( key, child );
                }
            }
        }
    }

    @Override
    public String toString() {
        // for verification purposes

        if ( isEmpty() ) {
            return "empty";
        }
        String padding = "";
        for ( int i = 0; i < depth; i++ ) {
            padding += ".   ";
        }
        if ( isLeaf() ) {
            return padding + string + "\n";
        }
        else {
            String ret = "";
            if ( string != null ) {
                ret += padding + "* (" + string + ")\n";
            }
            for ( CollationKey key : children.keySet() ) {
                ret += padding + key.getSourceString() + "\n";
                ret += children.get( key ).toString();
            }
            return ret;
        }
    }
}
