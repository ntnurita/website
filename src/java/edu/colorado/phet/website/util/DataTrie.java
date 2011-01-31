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

public abstract class DataTrie<T> {
    private DataTrieNode<T> root;
    private Collator collator;

    protected abstract String toString( T data );

    public DataTrie( Locale locale ) {
        this( locale, Collator.PRIMARY );
    }

    public DataTrie( Locale locale, int collatorStrength ) {
        this.root = new DataTrieNode<T>( 0, this );
        this.collator = Collator.getInstance( locale );
        this.collator.setStrength( collatorStrength );
    }

    /**
     * Returns all strings that start with the substring start
     *
     * @param start Starting substring
     * @return All strings that start with the substring start
     */
    public List<T> getStartingWith( String start ) {
        List<T> ret = new LinkedList<T>();
        DataTrieNode<T> node = root;
        for ( char character : start.toCharArray() ) {
            CollationKey key = collator.getCollationKey( String.valueOf( character ) );
            DataTrieNode<T> next = node.getChild( key );
            if ( next == null ) {
                if ( node.isLeaf() && startsWith( start, toString( node.data ) ) ) {
                    ret.add( node.data );
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
     * Adds an object to the trie
     */
    public void add( T object ) {
        root.add( object, toString( object ) );
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

class DataTrieNode<T> {
    public T data;
    public HashMap<CollationKey, DataTrieNode<T>> children;
    public int depth;
    public DataTrie<T> trie;

    DataTrieNode( int depth, DataTrie<T> trie ) {
        this.depth = depth;
        this.trie = trie;
    }

    public boolean isEmpty() {
        return data == null && ( children == null );
    }

    public boolean isLeaf() {
        return children == null;
    }

    public DataTrieNode<T> getChild( CollationKey key ) {
        if ( children == null ) {
            return null;
        }
        return children.get( key );
    }

    public void addChildStringsToList( List<T> strings ) {
        if ( data != null ) {
            strings.add( data );
        }
        if ( children != null ) {
            for ( DataTrieNode<T> node : children.values() ) {
                node.addChildStringsToList( strings );
            }
        }
    }

    private void convertToBranch() {
        data = null;
        children = new HashMap<CollationKey, DataTrieNode<T>>();
    }

    public void add( T newData, String newString ) {
        if ( isEmpty() ) {
            // we must be the root
            data = newData;
            return;
        }

        String string = data == null ? null : trie.toString( data );

        // if the string to add is a duplicate of our existing string, bail
        if ( string != null && string.length() == newString.length() && trie.equivalent( string, newString ) ) {
            return;
        }

        if ( isLeaf() ) {
            // we are a leaf. convert to a branch
            T other = data;
            convertToBranch();

            // add the two strings in
            add( other, trie.toString( other ) );
            add( newData, newString );
        }
        else {
            // not a leaf node
            if ( newString.length() == depth ) {
                // we are adding a shorter version of existing strings
                data = newData;
            }
            else {
                CollationKey key = trie.stringToKey( newString.substring( depth, depth + 1 ) );
                DataTrieNode<T> possibleChild = getChild( key );
                if ( possibleChild != null ) {
                    // follow the path
                    possibleChild.add( newData, newString );
                }
                else {
                    // create a new node
                    DataTrieNode<T> child = new DataTrieNode<T>( depth + 1, trie );
                    child.data = newData;
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
            return padding + trie.toString( data ) + "\n";
        }
        else {
            String ret = "";
            if ( data != null ) {
                ret += padding + "* (" + trie.toString( data ) + ")\n";
            }
            for ( CollationKey key : children.keySet() ) {
                ret += padding + key.getSourceString() + "\n";
                ret += children.get( key ).toString();
            }
            return ret;
        }
    }
}
