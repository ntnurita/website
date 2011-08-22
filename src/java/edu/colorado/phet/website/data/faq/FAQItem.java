// Copyright 2002-2011, University of Colorado
package edu.colorado.phet.website.data.faq;

import java.io.Serializable;

import edu.colorado.phet.website.data.util.IntId;

/**
 * Either a FAQ (Frequently Asked Question) or a category header
 */
public class FAQItem implements Serializable, IntId {

    // unique ID
    private int id;

    // whether it is a question or a header
    private boolean question;

    // the partial translation key
    private String key;

    // the list that this item is in
    private FAQList list;

    public String getBaseKey() {
        return "faq." + list.getName() + "." + key;
    }

    public String getHeaderKey() {
        return getBaseKey() + ".header";
    }

    public String getQuestionKey() {
        return getBaseKey() + ".question";
    }

    public String getAnswerKey() {
        return getBaseKey() + ".answer";
    }

    public FAQItem() {
    }

    public int getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }

    public boolean isQuestion() {
        return question;
    }

    public void setQuestion( boolean question ) {
        this.question = question;
    }

    public String getKey() {
        return key;
    }

    public void setKey( String key ) {
        this.key = key;
    }

    public FAQList getList() {
        return list;
    }

    public void setList( FAQList list ) {
        this.list = list;
    }
}
