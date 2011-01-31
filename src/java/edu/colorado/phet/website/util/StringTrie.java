/*
 * Copyright 2011, University of Colorado
 */

package edu.colorado.phet.website.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Unsorted String Trie. This means that given a string 'str', it should be able to efficiently add it to the trie, or
 * find all contained strings that start with 'str'
 */
public class StringTrie {
    private StringTrieNode root = new StringTrieNode( 0 );

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
            StringTrieNode next = node.getChild( character );
            if ( next == null ) {
                if ( node.isLeaf() && node.string.startsWith( start ) ) {
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
}

class StringTrieNode {
    public String string;
    public HashMap<Character, StringTrieNode> children;
    public int depth;

    StringTrieNode( int depth ) {
        this.depth = depth;
    }

    public boolean isEmpty() {
        return string == null && ( children == null );
    }

    public boolean isLeaf() {
        return children == null;
    }

    public StringTrieNode getChild( char character ) {
        if ( children == null ) {
            return null;
        }
        return children.get( character );
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
        children = new HashMap<Character, StringTrieNode>();
    }

    public void addString( String str ) {
        if ( isEmpty() ) {
            // we must be the root
            string = str;
            return;
        }

        // if the string to add is a duplicate of our existing string, bail
        if ( str.equals( string ) ) {
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
                StringTrieNode possibleChild = getChild( str.charAt( depth ) );
                if ( possibleChild != null ) {
                    // follow the path
                    possibleChild.addString( str );
                }
                else {
                    // create a new node
                    StringTrieNode child = new StringTrieNode( depth + 1 );
                    child.string = str;
                    children.put( str.charAt( depth ), child );
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
            for ( Character character : children.keySet() ) {
                ret += padding + character + "\n";
                ret += children.get( character ).toString();
            }
            return ret;
        }
    }
}
