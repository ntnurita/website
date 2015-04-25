/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import edu.colorado.phet.website.authentication.PhetSession;
import edu.colorado.phet.website.data.util.IntId;

/**
 * User account for the PhET website.
 */
public class PhetUser implements Serializable, IntId {

    private int id;
    private String email;
    private String hashedPassword;
    private boolean teamMember = false;
    private boolean trustedTranslator = false;
    private boolean newsletterOnlyAccount;
    private boolean confirmed = false;
    private Set translations = new HashSet();

    private String name;
    private String organization;

    private String description;
    private String jobTitle;

    private String address1;
    private String address2;
    private String city;
    private String state;
    private String country;
    private String zipcode;

    private String phone1;
    private String phone2;
    private String fax;

    private String confirmationKey; // key that is used for subscribing or unsubscribing to email. will be reset on each subscribe/unsubscribe operation

    private boolean receiveEmail = true; // for receiving newsletters
    private boolean receiveSimulationNotifications = false; // for receiving notifications about new / modified simulations (for everyone)
    private boolean receiveWebsiteNotifications = false; // for receiving internal (team-member) only notifications

    // new fields added in April 2015, see https://github.com/phetsims/website/issues/94

    // roles
    private boolean teacher;
    private boolean student;
    private boolean researcher;
    private boolean translator;
    private boolean otherRole;
    private String otherRoleText;

    // subjects
    private boolean generalScience;
    private boolean earthScience;
    private boolean biology;
    private boolean physics;
    private boolean chemistry;
    private boolean astronomy;
    private boolean math;
    private boolean otherSubject;
    private String otherSubjectText;

    // grades
    private boolean elementary;
    private boolean middle;
    private boolean high;
    private boolean university;
    private boolean gradeK;
    private boolean grade1;
    private boolean grade2;
    private boolean grade3;
    private boolean grade4;
    private boolean grade5;
    private boolean grade6;
    private boolean grade7;
    private boolean grade8;
    private boolean grade9;
    private boolean grade10;
    private boolean grade11;
    private boolean grade12;
    private boolean year1;
    private boolean year2plus;
    private boolean graduate;
    private boolean otherGrade;

    // years teaching
    private String yearsTeaching;

    private static Random random = new Random(); // for computing things like the confirmation keys
    private static Logger logger = Logger.getLogger( PhetUser.class );

    /**
     * @return An array of possible options for the 'description' field. Older descriptions may exist from legacy data.
     */
    public static List<String> getDescriptionOptions() {
        return Arrays.asList(
                "I am a teacher who uses PhET in my classes",
                "I am a teacher interested in using PhET in the future",
                "I am a student who uses PhET",
                "I am a student interested in using PhET in the future",
                "I am just interested in physics",
                "Other"
        );
    }

    /**
     * Lookup user from key, otherwise return null
     * <p/>
     * Assumes that it is within a transaction
     *
     * @param session
     * @param confirmationKey
     * @return
     */
    public static PhetUser getUserFromConfirmationKey( Session session, String confirmationKey ) {
        List list = session.createQuery( "select u from PhetUser as u where u.confirmationKey = :key" ).setString( "key", confirmationKey ).list();
        if ( list.size() == 0 ) {
            return null;
        }
        else if ( list.size() == 1 ) {
            return (PhetUser) list.get( 0 );
        }
        else {
            throw new RuntimeException( "Multiple users with same newsletter key" );
        }
    }

    /**
     * @return A new confirmation key that is suitable for setting a user's confirmationKey
     */
    public static synchronized String generateConfirmationKey() {
        return Long.toHexString( random.nextLong() ) + "-" + Long.toHexString( System.currentTimeMillis() );
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        return o != null && o instanceof PhetUser && ( (PhetUser) o ).getId() == getId();
    }

    @Override
    public int hashCode() {
        return ( id * 475165 ) % 2567;
    }

    public static boolean isValidEmail( String email ) {
        return Pattern.matches( "^.+@.+\\.[a-z]+$", email );
    }

    public static String validateEmail( String email ) {
        if ( !isValidEmail( email ) ) {
            return "validation.email";
        }
        return null;
    }

    public void setPassword( String password, String email ) {
        setHashedPassword( PhetSession.saltedPassword( email, password ) );
    }

    // if the user has no confirmation (unsubscription) key, create one for them. needs to be run from within a transaction
    public void ensureHasConfirmationKey( Session session ) {
        if ( getConfirmationKey() == null ) {
            setConfirmationKey( PhetUser.generateConfirmationKey() );
            session.update( this );
        }
    }

    // TODO: don't allow users with the same email address!

    public PhetUser() {

    }

    public PhetUser( String email, boolean newsletterOnly ) {
        setEmail( email );
        setConfirmationKey( generateConfirmationKey() );
        setNewsletterOnlyAccount( newsletterOnly );
    }

    public int getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    /**
     * This returns a hashed copy of the password.
     *
     * @return
     */
    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword( String hashedPassword ) {
        this.hashedPassword = hashedPassword;
    }

    public boolean isTeamMember() {
        return teamMember;
    }

    public void setTeamMember( boolean teamMember ) {
        this.teamMember = teamMember;
    }

    public boolean isTrustedTranslator() {
        return trustedTranslator;
    }

    public void setTrustedTranslator( boolean trustedTranslator ) {
        this.trustedTranslator = trustedTranslator;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed( boolean confirmed ) {
        this.confirmed = confirmed;
    }

    public boolean isNewsletterOnlyAccount() {
        return newsletterOnlyAccount;
    }

    public void setNewsletterOnlyAccount( boolean newsletterOnlyAccount ) {
        this.newsletterOnlyAccount = newsletterOnlyAccount;
    }

    public Set getTranslations() {
        return translations;
    }

    public void setTranslations( Set translations ) {
        this.translations = translations;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization( String organization ) {
        this.organization = organization;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle( String jobTitle ) {
        this.jobTitle = jobTitle;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1( String address1 ) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2( String address2 ) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity( String city ) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState( String state ) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry( String country ) {
        this.country = country;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode( String zipcode ) {
        this.zipcode = zipcode;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1( String phone1 ) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2( String phone2 ) {
        this.phone2 = phone2;
    }

    public String getFax() {
        return fax;
    }

    public void setFax( String fax ) {
        this.fax = fax;
    }

    public String getConfirmationKey() {
        return confirmationKey;
    }

    public void setConfirmationKey( String confirmationKey ) {
        this.confirmationKey = confirmationKey;
    }

    public boolean isReceiveEmail() {
        return receiveEmail;
    }

    public void setReceiveEmail( boolean receiveEmail ) {
        this.receiveEmail = receiveEmail;
    }

    public boolean isReceiveWebsiteNotifications() {
        return receiveWebsiteNotifications;
    }

    public void setReceiveWebsiteNotifications( boolean receiveWebsiteNotifications ) {
        this.receiveWebsiteNotifications = receiveWebsiteNotifications;
    }

    public boolean isReceiveSimulationNotifications() {
        return receiveSimulationNotifications;
    }

    public void setReceiveSimulationNotifications( boolean receiveSimulationNotifications ) {
        this.receiveSimulationNotifications = receiveSimulationNotifications;
    }

    public boolean isTeacher() {
        return teacher;
    }

    public void setTeacher( boolean teacher ) {
        this.teacher = teacher;
    }

    public boolean isStudent() {
        return student;
    }

    public void setStudent( boolean student ) {
        this.student = student;
    }

    public boolean isResearcher() {
        return researcher;
    }

    public void setResearcher( boolean researcher ) {
        this.researcher = researcher;
    }

    public boolean isTranslator() {
        return translator;
    }

    public void setTranslator( boolean translator ) {
        this.translator = translator;
    }

    public boolean isOtherRole() {
        return otherRole;
    }

    public void setOtherRole( boolean otherRole ) {
        this.otherRole = otherRole;
    }

    public String getOtherRoleText() {
        return otherRoleText;
    }

    public void setOtherRoleText( String otherRoleText ) {
        this.otherRoleText = otherRoleText;
    }

    public boolean isGeneralScience() {
        return generalScience;
    }

    public void setGeneralScience( boolean generalScience ) {
        this.generalScience = generalScience;
    }

    public boolean isEarthScience() {
        return earthScience;
    }

    public void setEarthScience( boolean earthScience ) {
        this.earthScience = earthScience;
    }

    public boolean isBiology() {
        return biology;
    }

    public void setBiology( boolean biology ) {
        this.biology = biology;
    }

    public boolean isPhysics() {
        return physics;
    }

    public void setPhysics( boolean physics ) {
        this.physics = physics;
    }

    public boolean isChemistry() {
        return chemistry;
    }

    public void setChemistry( boolean chemistry ) {
        this.chemistry = chemistry;
    }

    public boolean isAstronomy() {
        return astronomy;
    }

    public void setAstronomy( boolean astronomy ) {
        this.astronomy = astronomy;
    }

    public boolean isMath() {
        return math;
    }

    public void setMath( boolean math ) {
        this.math = math;
    }

    public boolean isOtherSubject() {
        return otherSubject;
    }

    public void setOtherSubject( boolean otherSubject ) {
        this.otherSubject = otherSubject;
    }

    public String getOtherSubjectText() {
        return otherSubjectText;
    }

    public void setOtherSubjectText( String otherSubjectText ) {
        this.otherSubjectText = otherSubjectText;
    }

    public boolean isElementary() {
        return elementary;
    }

    public void setElementary( boolean elementary ) {
        this.elementary = elementary;
    }

    public boolean isMiddle() {
        return middle;
    }

    public void setMiddle( boolean middle ) {
        this.middle = middle;
    }

    public boolean isHigh() {
        return high;
    }

    public void setHigh( boolean high ) {
        this.high = high;
    }

    public boolean isUniversity() {
        return university;
    }

    public void setUniversity( boolean university ) {
        this.university = university;
    }

    public boolean isGradeK() {
        return gradeK;
    }

    public void setGradeK( boolean gradeK ) {
        this.gradeK = gradeK;
    }

    public boolean isGrade1() {
        return grade1;
    }

    public void setGrade1( boolean grade1 ) {
        this.grade1 = grade1;
    }

    public boolean isGrade2() {
        return grade2;
    }

    public void setGrade2( boolean grade2 ) {
        this.grade2 = grade2;
    }

    public boolean isGrade3() {
        return grade3;
    }

    public void setGrade3( boolean grade3 ) {
        this.grade3 = grade3;
    }

    public boolean isGrade4() {
        return grade4;
    }

    public void setGrade4( boolean grade4 ) {
        this.grade4 = grade4;
    }

    public boolean isGrade5() {
        return grade5;
    }

    public void setGrade5( boolean grade5 ) {
        this.grade5 = grade5;
    }

    public boolean isGrade6() {
        return grade6;
    }

    public void setGrade6( boolean grade6 ) {
        this.grade6 = grade6;
    }

    public boolean isGrade7() {
        return grade7;
    }

    public void setGrade7( boolean grade7 ) {
        this.grade7 = grade7;
    }

    public boolean isGrade8() {
        return grade8;
    }

    public void setGrade8( boolean grade8 ) {
        this.grade8 = grade8;
    }

    public boolean isGrade9() {
        return grade9;
    }

    public void setGrade9( boolean grade9 ) {
        this.grade9 = grade9;
    }

    public boolean isGrade10() {
        return grade10;
    }

    public void setGrade10( boolean grade10 ) {
        this.grade10 = grade10;
    }

    public boolean isGrade11() {
        return grade11;
    }

    public void setGrade11( boolean grade11 ) {
        this.grade11 = grade11;
    }

    public boolean isGrade12() {
        return grade12;
    }

    public void setGrade12( boolean grade12 ) {
        this.grade12 = grade12;
    }

    public boolean isYear1() {
        return year1;
    }

    public void setYear1( boolean year1 ) {
        this.year1 = year1;
    }

    public boolean isYear2plus() {
        return year2plus;
    }

    public void setYear2plus( boolean year2plus ) {
        this.year2plus = year2plus;
    }

    public boolean isGraduate() {
        return graduate;
    }

    public void setGraduate( boolean graduate ) {
        this.graduate = graduate;
    }

    public boolean isOtherGrade() {
        return otherGrade;
    }

    public void setOtherGrade( boolean otherGrade ) {
        this.otherGrade = otherGrade;
    }

    public String getYearsTeaching() {
        return yearsTeaching;
    }

    public void setYearsTeaching( String yearsTeaching ) {
        this.yearsTeaching = yearsTeaching;
    }

    /*_/|
    =0-0=
    \'I'|
    |<|,,\_
    |[>,,/,\
    |[|,\_,,)
    ((J(=_*/

}
