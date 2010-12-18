package edu.colorado.phet.website.data;

import java.io.Serializable;
import java.util.*;

import org.hibernate.Session;

import edu.colorado.phet.common.phetcommon.util.LocaleUtils;
import edu.colorado.phet.website.PhetWicketApplication;
import edu.colorado.phet.website.data.util.IntId;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;
import edu.colorado.phet.website.util.hibernate.Result;
import edu.colorado.phet.website.util.hibernate.Task;

/**
 * A translation, which has a certain number of strings and authorized users.
 */
public class Translation implements Serializable, IntId {

    private int id;

    private Locale locale;
    private Set translatedStrings = new HashSet();
    private Set authorizedUsers = new HashSet();

    public static final Locale[] DIRECTLY_EDITED_LOCALES = new Locale[] {
            PhetWicketApplication.getDefaultLocale(),
            LocaleUtils.stringToLocale( "ar_SA" )
    };

    /**
     * Whether this translation is globally visible (and shown in the links of translations at the bottom of the page).
     * There can be multiple translations with the same locale, but only ONE of these can be visible.
     */
    private boolean visible;

    /**
     * Whether this translation is blocked from edits by non-phet-team-members. Generally, visible translations should
     * be locked for security reasons
     */
    private boolean locked;

    private Translation parent;

    public void addString( TranslatedString str ) {
        translatedStrings.add( str );
        str.setTranslation( this );
    }

    public void removeString( TranslatedString str ) {
        translatedStrings.remove( str );
    }

    public void addUser( PhetUser user ) {
        authorizedUsers.add( user );
        user.getTranslations().add( this );
    }

    public void removeUser( PhetUser user ) {
        authorizedUsers.remove( user );
        user.getTranslations().remove( this );
    }

    /**
     * @return Whether this translation is the default translation (IE English)
     */
    public boolean isDefault() {
        return isVisible() && getLocale().equals( PhetWicketApplication.getDefaultLocale() );
    }

    public boolean isPublished( Session session ) {
        for ( Locale locale : DIRECTLY_EDITED_LOCALES ) {
            if ( getLocale().equals( locale ) ) {
                return isVisible();
            }
        }
        Result<Boolean> isParentOfVisibleResult = HibernateUtils.resultCatchTransaction( session, new Task<Boolean>() {
            public Boolean run( Session session ) {
                Translation t = (Translation) session.createQuery( "select t from Translation as t where t.visible = true and t.locale = :locale" )
                        .setLocale( "locale", getLocale() ).uniqueResult();
                return t.getParent().getId() == getId();
            }
        } );
        return ( isParentOfVisibleResult.success && isParentOfVisibleResult.value );
    }

    /**
     * NOTE: needs to be in a transaction!
     */
    public List<Translation> getChildren( Session session ) {
        final List<Translation> ret = new LinkedList<Translation>();
        Translation thisTranslation = (Translation) session.load( Translation.class, getId() );
        List list = session.createQuery( "select t from Translation as t where t.parent = :translation" )
                .setEntity( "translation", thisTranslation ).list();
        for ( Object o : list ) {
            ret.add( (Translation) o );
        }
        return ret;
    }

    /*---------------------------------------------------------------------------*
    * authorization / security access
    *----------------------------------------------------------------------------*/

    public boolean isUserAuthorized( PhetUser user ) {
        if ( user.isTeamMember() && !isDefault() ) {
            return true;
        }
        for ( Object authorizedUser : authorizedUsers ) {
            if ( ( (PhetUser) authorizedUser ).getId() == user.getId() ) {
                return true;
            }
        }
        return false;
    }

    public boolean allowToggleVisibility( PhetUser user ) {
        return user.isTeamMember() && !isDefault();
    }

    public boolean allowToggleLocking( PhetUser user ) {
        return user.isTeamMember() && !isDefault();
    }

    public boolean allowEdit( PhetUser user ) {
        return isUserAuthorized( user ) && ( user.isTeamMember() || !isLocked() );
    }

    public boolean allowSubmit( PhetUser user ) {
        return isUserAuthorized( user ) && !isVisible() && !isLocked();
    }

    public boolean allowDelete( PhetUser user ) {
        return !isVisible() && isUserAuthorized( user ) && ( ( user.isTeamMember() && !isDefault() ) || ( !isDefault() && !isLocked() ) );
    }

    public boolean allowRequestToCollaborate( PhetUser user ) {
        // TODO: add opt-out for collaboration
        return !isUserAuthorized( user );
    }

    /*---------------------------------------------------------------------------*
    * object method implementations
    *----------------------------------------------------------------------------*/

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        return o != null && o instanceof Translation && ( (Translation) o ).getId() == getId();
    }

    @Override
    public int hashCode() {
        return ( id * 475165 ) % 2567;
    }

    @Override
    public String toString() {
        // used in drop down choices in translation area
        return PhetWicketApplication.get().getSupportedLocales().getName( locale ) + " (" + LocaleUtils.localeToString( getLocale() ) + ", #" + id + ")";
    }

    /*---------------------------------------------------------------------------*
    * constructor
    *----------------------------------------------------------------------------*/

    public Translation() {
    }

    /*---------------------------------------------------------------------------*
    * getters and setters
    *----------------------------------------------------------------------------*/

    public int getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale( Locale locale ) {
        this.locale = locale;
    }

    public Set getTranslatedStrings() {
        return translatedStrings;
    }

    public void setTranslatedStrings( Set translatedStrings ) {
        this.translatedStrings = translatedStrings;
    }

    public Set getAuthorizedUsers() {
        return authorizedUsers;
    }

    public void setAuthorizedUsers( Set authorizedUsers ) {
        this.authorizedUsers = authorizedUsers;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible( boolean visible ) {
        this.visible = visible;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked( boolean locked ) {
        this.locked = locked;
    }

    public Translation getParent() {
        return parent;
    }

    public void setParent( Translation parent ) {
        this.parent = parent;
    }
}
