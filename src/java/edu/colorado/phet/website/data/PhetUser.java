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
    private Boolean teacher;
    private Boolean student;
    private Boolean researcher;
    private Boolean translator;
    private Boolean teacherEducator;
    private Boolean otherRole;
    private String otherRoleText;

    // subjects
    private Boolean generalScience;
    private Boolean earthScience;
    private Boolean biology;
    private Boolean physics;
    private Boolean chemistry;
    private Boolean astronomy;
    private Boolean math;
    private Boolean otherSubject;
    private String otherSubjectText;

    // grades
    private Boolean elementary;
    private Boolean middle;
    private Boolean high;
    private Boolean university;
    private Boolean gradeK;
    private Boolean grade1;
    private Boolean grade2;
    private Boolean grade3;
    private Boolean grade4;
    private Boolean grade5;
    private Boolean grade6;
    private Boolean grade7;
    private Boolean grade8;
    private Boolean grade9;
    private Boolean grade10;
    private Boolean grade11;
    private Boolean grade12;
    private Boolean year1;
    private Boolean year2plus;
    private Boolean graduate;
    private Boolean adultEducation;
    private Boolean otherGrade;

    // years teaching
    private String yearsTeaching;

    private static Random random = new Random(); // for computing things like the confirmation keys
    private static Logger logger = Logger.getLogger( PhetUser.class );

    /**
     * @return An array of possible options for the 'description' field. Older descriptions may exist from legacy data.
     * @deprecated
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

    public static List<String> getCountries() {
        return Arrays.asList(
                "Afghanistan",
                "Åland Islands",
                "Albania",
                "Algeria",
                "American Samoa",
                "Andorra",
                "Angola",
                "Anguilla",
                "Antarctica",
                "Antigua And Barbuda",
                "Argentina",
                "Armenia",
                "Aruba",
                "Australia",
                "Austria",
                "Azerbaijan",
                "Bahamas",
                "Bahrain",
                "Bangladesh",
                "Barbados",
                "Belarus",
                "Belgium",
                "Belize",
                "Benin",
                "Bermuda",
                "Bhutan",
                "Bolivia",
                "Bosnia And Herzegovina",
                "Botswana",
                "Bouvet Island",
                "Brazil",
                "British Indian Ocean Territory",
                "Brunei Darussalam",
                "Bulgaria",
                "Burkina Faso",
                "Burundi",
                "Cambodia",
                "Cameroon",
                "Canada",
                "Cape Verde",
                "Cayman Islands",
                "Central African Republic",
                "Chad",
                "Chile",
                "China",
                "Christmas Island",
                "Cocos (Keeling) Islands",
                "Colombia",
                "Comoros",
                "Congo",
                "Congo, The Democratic Republic Of The",
                "Cook Islands",
                "Costa Rica",
                "Cote D'ivoire",
                "Croatia",
                "Cuba",
                "Cyprus",
                "Czech Republic",
                "Denmark",
                "Djibouti",
                "Dominica",
                "Dominican Republic",
                "Ecuador",
                "Egypt",
                "El Salvador",
                "Equatorial Guinea",
                "Eritrea",
                "Estonia",
                "Ethiopia",
                "Falkland Islands (Malvinas)",
                "Faroe Islands",
                "Fiji",
                "Finland",
                "France",
                "French Guiana",
                "French Polynesia",
                "French Southern Territories",
                "Gabon",
                "Gambia",
                "Georgia",
                "Germany",
                "Ghana",
                "Gibraltar",
                "Greece",
                "Greenland",
                "Grenada",
                "Guadeloupe",
                "Guam",
                "Guatemala",
                "Guernsey",
                "Guinea",
                "Guinea-bissau",
                "Guyana",
                "Haiti",
                "Heard Island And Mcdonald Islands",
                "Holy See (Vatican City State)",
                "Honduras",
                "Hong Kong",
                "Hungary",
                "Iceland",
                "India",
                "Indonesia",
                "Iran, Islamic Republic Of",
                "Iraq",
                "Ireland",
                "Isle Of Man",
                "Israel",
                "Italy",
                "Jamaica",
                "Japan",
                "Jersey",
                "Jordan",
                "Kazakhstan",
                "Kenya",
                "Kiribati",
                "Korea, Democratic People's Republic Of",
                "Korea, Republic Of",
                "Kuwait",
                "Kyrgyzstan",
                "Lao People's Democratic Republic",
                "Latvia",
                "Lebanon",
                "Lesotho",
                "Liberia",
                "Libyan Arab Jamahiriya",
                "Liechtenstein",
                "Lithuania",
                "Luxembourg",
                "Macao",
                "Macedonia, The Former Yugoslav Republic Of",
                "Madagascar",
                "Malawi",
                "Malaysia",
                "Maldives",
                "Mali",
                "Malta",
                "Marshall Islands",
                "Martinique",
                "Mauritania",
                "Mauritius",
                "Mayotte",
                "Mexico",
                "Micronesia, Federated States Of",
                "Moldova, Republic Of",
                "Monaco",
                "Mongolia",
                "Montenegro",
                "Montserrat",
                "Morocco",
                "Mozambique",
                "Myanmar",
                "Namibia",
                "Nauru",
                "Nepal",
                "Netherlands",
                "Netherlands Antilles",
                "New Caledonia",
                "New Zealand",
                "Nicaragua",
                "Niger",
                "Nigeria",
                "Niue",
                "Norfolk Island",
                "Northern Mariana Islands",
                "Norway",
                "Oman",
                "Pakistan",
                "Palau",
                "Palestinian Territory, Occupied",
                "Panama",
                "Papua New Guinea",
                "Paraguay",
                "Peru",
                "Philippines",
                "Pitcairn",
                "Poland",
                "Portugal",
                "Puerto Rico",
                "Qatar",
                "Reunion",
                "Romania",
                "Russian Federation",
                "Rwanda",
                "Saint Helena",
                "Saint Kitts And Nevis",
                "Saint Lucia",
                "Saint Pierre And Miquelon",
                "Saint Vincent And The Grenadines",
                "Samoa",
                "San Marino",
                "Sao Tome And Principe",
                "Saudi Arabia",
                "Senegal",
                "Serbia",
                "Seychelles",
                "Sierra Leone",
                "Singapore",
                "Slovakia",
                "Slovenia",
                "Solomon Islands",
                "Somalia",
                "South Africa",
                "South Georgia And The South Sandwich Islands",
                "Spain",
                "Sri Lanka",
                "Sudan",
                "Suriname",
                "Svalbard And Jan Mayen",
                "Swaziland",
                "Sweden",
                "Switzerland",
                "Syrian Arab Republic",
                "Taiwan, Province Of China",
                "Tajikistan",
                "Tanzania, United Republic Of",
                "Thailand",
                "Timor-leste",
                "Togo",
                "Tokelau",
                "Tonga",
                "Trinidad And Tobago",
                "Tunisia",
                "Turkey",
                "Turkmenistan",
                "Turks And Caicos Islands",
                "Tuvalu",
                "Uganda",
                "Ukraine",
                "United Arab Emirates",
                "United Kingdom",
                "United States",
                "United States Minor Outlying Islands",
                "Uruguay",
                "Uzbekistan",
                "Vanuatu",
                "Venezuela",
                "Viet Nam",
                "Virgin Islands, British",
                "Virgin Islands, U.S.",
                "Wallis And Futuna",
                "Western Sahara",
                "Yemen",
                "Zambia",
                "Zimbabwe" );
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

    public Boolean isTeacher() {
        return teacher;
    }

    public void setTeacher( Boolean teacher ) {
        this.teacher = teacher;
    }

    public Boolean isStudent() {
        return student;
    }

    public void setStudent( Boolean student ) {
        this.student = student;
    }

    public Boolean isResearcher() {
        return researcher;
    }

    public void setResearcher( Boolean researcher ) {
        this.researcher = researcher;
    }

    public Boolean isTranslator() {
        return translator;
    }

    public void setTranslator( Boolean translator ) {
        this.translator = translator;
    }

    public Boolean isOtherRole() {
        return otherRole;
    }

    public void setOtherRole( Boolean otherRole ) {
        this.otherRole = otherRole;
    }

    public String getOtherRoleText() {
        return otherRoleText;
    }

    public void setOtherRoleText( String otherRoleText ) {
        this.otherRoleText = otherRoleText;
    }

    public Boolean isGeneralScience() {
        return generalScience;
    }

    public void setGeneralScience( Boolean generalScience ) {
        this.generalScience = generalScience;
    }

    public Boolean isEarthScience() {
        return earthScience;
    }

    public void setEarthScience( Boolean earthScience ) {
        this.earthScience = earthScience;
    }

    public Boolean isBiology() {
        return biology;
    }

    public void setBiology( Boolean biology ) {
        this.biology = biology;
    }

    public Boolean isPhysics() {
        return physics;
    }

    public void setPhysics( Boolean physics ) {
        this.physics = physics;
    }

    public Boolean isChemistry() {
        return chemistry;
    }

    public void setChemistry( Boolean chemistry ) {
        this.chemistry = chemistry;
    }

    public Boolean isAstronomy() {
        return astronomy;
    }

    public void setAstronomy( Boolean astronomy ) {
        this.astronomy = astronomy;
    }

    public Boolean isMath() {
        return math;
    }

    public void setMath( Boolean math ) {
        this.math = math;
    }

    public Boolean isOtherSubject() {
        return otherSubject;
    }

    public void setOtherSubject( Boolean otherSubject ) {
        this.otherSubject = otherSubject;
    }

    public String getOtherSubjectText() {
        return otherSubjectText;
    }

    public void setOtherSubjectText( String otherSubjectText ) {
        this.otherSubjectText = otherSubjectText;
    }

    public Boolean isElementary() {
        return elementary;
    }

    public void setElementary( Boolean elementary ) {
        this.elementary = elementary;
    }

    public Boolean isMiddle() {
        return middle;
    }

    public void setMiddle( Boolean middle ) {
        this.middle = middle;
    }

    public Boolean isHigh() {
        return high;
    }

    public void setHigh( Boolean high ) {
        this.high = high;
    }

    public Boolean isUniversity() {
        return university;
    }

    public void setUniversity( Boolean university ) {
        this.university = university;
    }

    public Boolean isGradeK() {
        return gradeK;
    }

    public void setGradeK( Boolean gradeK ) {
        this.gradeK = gradeK;
    }

    public Boolean isGrade1() {
        return grade1;
    }

    public void setGrade1( Boolean grade1 ) {
        this.grade1 = grade1;
    }

    public Boolean isGrade2() {
        return grade2;
    }

    public void setGrade2( Boolean grade2 ) {
        this.grade2 = grade2;
    }

    public Boolean isGrade3() {
        return grade3;
    }

    public void setGrade3( Boolean grade3 ) {
        this.grade3 = grade3;
    }

    public Boolean isGrade4() {
        return grade4;
    }

    public void setGrade4( Boolean grade4 ) {
        this.grade4 = grade4;
    }

    public Boolean isGrade5() {
        return grade5;
    }

    public void setGrade5( Boolean grade5 ) {
        this.grade5 = grade5;
    }

    public Boolean isGrade6() {
        return grade6;
    }

    public void setGrade6( Boolean grade6 ) {
        this.grade6 = grade6;
    }

    public Boolean isGrade7() {
        return grade7;
    }

    public void setGrade7( Boolean grade7 ) {
        this.grade7 = grade7;
    }

    public Boolean isGrade8() {
        return grade8;
    }

    public void setGrade8( Boolean grade8 ) {
        this.grade8 = grade8;
    }

    public Boolean isGrade9() {
        return grade9;
    }

    public void setGrade9( Boolean grade9 ) {
        this.grade9 = grade9;
    }

    public Boolean isGrade10() {
        return grade10;
    }

    public void setGrade10( Boolean grade10 ) {
        this.grade10 = grade10;
    }

    public Boolean isGrade11() {
        return grade11;
    }

    public void setGrade11( Boolean grade11 ) {
        this.grade11 = grade11;
    }

    public Boolean isGrade12() {
        return grade12;
    }

    public void setGrade12( Boolean grade12 ) {
        this.grade12 = grade12;
    }

    public Boolean isYear1() {
        return year1;
    }

    public void setYear1( Boolean year1 ) {
        this.year1 = year1;
    }

    public Boolean isYear2plus() {
        return year2plus;
    }

    public void setYear2plus( Boolean year2plus ) {
        this.year2plus = year2plus;
    }

    public Boolean isGraduate() {
        return graduate;
    }

    public void setGraduate( Boolean graduate ) {
        this.graduate = graduate;
    }

    public Boolean isOtherGrade() {
        return otherGrade;
    }

    public void setOtherGrade( Boolean otherGrade ) {
        this.otherGrade = otherGrade;
    }

    public String getYearsTeaching() {
        return yearsTeaching;
    }

    public void setYearsTeaching( String yearsTeaching ) {
        this.yearsTeaching = yearsTeaching;
    }

    public Boolean getAdultEducation() {
        return adultEducation;
    }

    public void setAdultEducation( Boolean adultEducation ) {
        this.adultEducation = adultEducation;
    }

    public Boolean getTeacherEducator() {
        return teacherEducator;
    }

    public void setTeacherEducator( Boolean teacherEducator ) {
        this.teacherEducator = teacherEducator;
    }

    /*_/|
    =0-0=
    \'I'|
    |<|,,\_
    |[>,,/,\
    |[|,\_,,)
    ((J(=_*/

}
