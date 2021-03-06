/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.translation.entities;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Session;

import edu.colorado.phet.website.data.Keyword;
import edu.colorado.phet.website.util.hibernate.HibernateTask;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;

public class KeywordsEntity extends TranslationEntity {

    public KeywordsEntity() {
        final List<Keyword> allKeywords = new LinkedList<Keyword>();
        HibernateUtils.wrapSession( new HibernateTask() {
            public boolean run( Session session ) {
                List keywords = session.createQuery( "select k from Keyword as k" ).list();
                for ( Object keyword : keywords ) {
                    allKeywords.add( (Keyword) keyword );
                }
                return true;
            }
        } );

        Collections.sort( allKeywords, new Comparator<Keyword>() {
            public int compare( Keyword a, Keyword b ) {
                return a.getKey().compareTo( b.getKey() );
            }
        } );

        for ( Keyword keyword : allKeywords ) {
            addString( keyword.getKey() );
        }
    }

    public String getDisplayName() {
        return "Keywords";
    }
}
