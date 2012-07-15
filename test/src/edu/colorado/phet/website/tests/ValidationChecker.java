/*
 * Copyright 2010, University of Colorado
 */

package edu.colorado.phet.website.tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Checks w3c validation of pages
 */
public class ValidationChecker {

    public static boolean isValid( String uri ) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL( "http://validator.w3.org/check?uri=" + URLEncoder.encode( uri, "UTF-8" ) );
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod( "GET" );
            conn.setReadTimeout( 30000 );
            conn.setInstanceFollowRedirects( false );
            conn.setDoInput( true );
            conn.connect();

            BufferedReader reader = new BufferedReader( new InputStreamReader( conn.getInputStream() ) );
            try {
                String line;
                while ( ( line = reader.readLine() ) != null ) {
                    if ( line.trim().equals( "[Valid]" ) ) {
                        return true;
                    }
                    if ( line.trim().equals( "[Invalid]" ) ) {
                        return false;
                    }
                }
            }
            finally {
                reader.close();
            }
        }
        catch( MalformedURLException e ) {
            e.printStackTrace();
        }
        catch( IOException e ) {
            e.printStackTrace();
            System.out.println( "Throw in checking url: " + uri );
        }
        finally {
            if ( conn != null ) {
                conn.disconnect();
            }
        }
        return false;
    }

    public static void check( String uri ) {
        boolean valid = isValid( uri );
        if ( valid ) {
            //System.out.println( "OK\t" + uri );
        }
        else {
            System.out.println( "BAD!\t" + uri );
        }
    }

    public static void main( String[] args ) {
        check( "http://phet.colorado.edu" );
        check( "http://phet.colorado.edu/ar_SA/" );
        check( "http://phet.colorado.edu/ar_SA/about" );
        check( "http://phet.colorado.edu/ar_SA/about/contact" );
        check( "http://phet.colorado.edu/ar_SA/about/licensing" );
        check( "http://phet.colorado.edu/ar_SA/about/news" );
        check( "http://phet.colorado.edu/ar_SA/about/source-code" );
        check( "http://phet.colorado.edu/ar_SA/about/sponsors" );
        check( "http://phet.colorado.edu/ar_SA/contributions/view/2708" );
        check( "http://phet.colorado.edu/ar_SA/donate" );
        check( "http://phet.colorado.edu/ar_SA/for-teachers" );
        check( "http://phet.colorado.edu/ar_SA/for-teachers/browse-activities" );
        check( "http://phet.colorado.edu/ar_SA/for-teachers/legend" );
        check( "http://phet.colorado.edu/ar_SA/for-teachers/submit-activity" );
        check( "http://phet.colorado.edu/ar_SA/for-teachers/workshops" );
        check( "http://phet.colorado.edu/ar_SA/for-translators" );
        check( "http://phet.colorado.edu/ar_SA/for-translators/translation-utility" );
        check( "http://phet.colorado.edu/ar_SA/get-phet" );
        check( "http://phet.colorado.edu/ar_SA/get-phet/full-install" );
        check( "http://phet.colorado.edu/ar_SA/get-phet/one-at-a-time" );
        check( "http://phet.colorado.edu/ar_SA/research" );
        check( "http://phet.colorado.edu/ar_SA/sign-in?dest=%2F" );
        check( "http://phet.colorado.edu/ar_SA/simulation/aphid-maze" );
        check( "http://phet.colorado.edu/ar_SA/simulation/circuit-construction-kit-ac" );
        check( "http://phet.colorado.edu/ar_SA/simulation/curve-fitting" );
        check( "http://phet.colorado.edu/ar_SA/simulation/discharge-lamps" );
        check( "http://phet.colorado.edu/ar_SA/simulation/electric-hockey" );
        check( "http://phet.colorado.edu/ar_SA/simulation/forces-1d" );
        check( "http://phet.colorado.edu/ar_SA/simulation/magnets-and-electromagnets" );
        check( "http://phet.colorado.edu/ar_SA/simulation/mass-spring-lab" );
        check( "http://phet.colorado.edu/ar_SA/simulation/maze-game" );
        check( "http://phet.colorado.edu/ar_SA/simulation/my-solar-system" );
        check( "http://phet.colorado.edu/ar_SA/simulation/rotation" );
        check( "http://phet.colorado.edu/ar_SA/simulations/category/by-level" );
        check( "http://phet.colorado.edu/ar_SA/simulations/category/by-level/university" );
        check( "http://phet.colorado.edu/ar_SA/simulations/category/chemistry" );
        check( "http://phet.colorado.edu/ar_SA/simulations/category/physics/electricity-magnets-and-circuits" );
        check( "http://phet.colorado.edu/ar_SA/simulation/signal-circuit" );
        check( "http://phet.colorado.edu/ar_SA/simulations/index" );
        check( "http://phet.colorado.edu/ar_SA/simulations/keyword/conservationOfEnergy" );
        check( "http://phet.colorado.edu/ar_SA/simulations/keyword/force" );
        check( "http://phet.colorado.edu/ar_SA/simulations/keyword/springs" );
        check( "http://phet.colorado.edu/ar_SA/simulations/keyword/thermalEnergy" );
        check( "http://phet.colorado.edu/ar_SA/simulations/translated" );
        check( "http://phet.colorado.edu/ar_SA/simulations/translated/ar" );
        check( "http://phet.colorado.edu/ar_SA/simulations/translated/fr" );
        check( "http://phet.colorado.edu/ar_SA/simulations/translated/hr" );
        check( "http://phet.colorado.edu/ar_SA/simulations/translated/vi" );
        check( "http://phet.colorado.edu/ar_SA/simulations/translated/zh_CN" );
        check( "http://phet.colorado.edu/ar_SA/simulations/translated/zh_TW" );
        check( "http://phet.colorado.edu/ar_SA/simulation/travoltage" );
        check( "http://phet.colorado.edu/ar_SA/troubleshooting" );
        check( "http://phet.colorado.edu/ar_SA/?wicket:bookmarkablePage=:edu.colorado.phet.website.authentication.SignInPage" );
        check( "http://phet.colorado.edu/en/about" );
        check( "http://phet.colorado.edu/en/about/contact" );
        check( "http://phet.colorado.edu/en/about/licensing" );
        check( "http://phet.colorado.edu/en/about/news" );
        check( "http://phet.colorado.edu/en/about/source-code" );
        check( "http://phet.colorado.edu/en/about/sponsors" );
        check( "http://phet.colorado.edu/en/contributions/view/2708" );
        check( "http://phet.colorado.edu/en/donate" );
        check( "http://phet.colorado.edu/en/for-teachers" );
        check( "http://phet.colorado.edu/en/for-teachers/activity-guide" );
        check( "http://phet.colorado.edu/en/for-teachers/browse-activities" );
        check( "http://phet.colorado.edu/en/for-teachers/legend" );
        check( "http://phet.colorado.edu/en/for-teachers/manage-activities" );
        check( "http://phet.colorado.edu/en/for-teachers/submit-activity" );
        check( "http://phet.colorado.edu/en/for-teachers/workshops" );
        check( "http://phet.colorado.edu/en/for-translators" );
        check( "http://phet.colorado.edu/en/for-translators/translation-utility" );
        check( "http://phet.colorado.edu/en/for-translators/website" );
        check( "http://phet.colorado.edu/en/get-phet" );
        check( "http://phet.colorado.edu/en/get-phet/full-install" );
        check( "http://phet.colorado.edu/en/get-phet/one-at-a-time" );
        check( "http://phet.colorado.edu/en/register?dest=/" );
        check( "http://phet.colorado.edu/en/research" );
        check( "http://phet.colorado.edu/en/search?q=Energy" );
        check( "http://phet.colorado.edu/en/search?q=Energy+Skate+Park" );
        check( "http://phet.colorado.edu/en/search?q=Equations" );
        check( "http://phet.colorado.edu/en/search?q=Equilibrium" );
        check( "http://phet.colorado.edu/en/search?q=Estimation" );
        check( "http://phet.colorado.edu/en/sign-in?dest=%2F" );
        check( "http://phet.colorado.edu/en/simulation/alpha-decay" );
        check( "http://phet.colorado.edu/en/simulation/aphid-maze" );
        check( "http://phet.colorado.edu/en/simulation/aphid-maze/changelog" );
        check( "http://phet.colorado.edu/en/simulation/arithmetic" );
        check( "http://phet.colorado.edu/en/simulation/atomic-interactions" );
        check( "http://phet.colorado.edu/en/simulation/balloons" );
        check( "http://phet.colorado.edu/en/simulation/balloons-and-buoyancy" );
        check( "http://phet.colorado.edu/en/simulation/balloons-and-buoyancy/changelog" );
        check( "http://phet.colorado.edu/en/simulation/band-structure" );
        check( "http://phet.colorado.edu/en/simulation/battery-resistor-circuit" );
        check( "http://phet.colorado.edu/en/simulation/battery-voltage" );
        check( "http://phet.colorado.edu/en/simulation/beta-decay" );
        check( "http://phet.colorado.edu/en/simulation/blackbody-spectrum" );
        check( "http://phet.colorado.edu/en/simulation/bound-states" );
        check( "http://phet.colorado.edu/en/simulation/calculus-grapher" );
        check( "http://phet.colorado.edu/en/simulation/charges-and-fields" );
        check( "http://phet.colorado.edu/en/simulation/circuit-construction-kit-ac" );
        check( "http://phet.colorado.edu/en/simulation/circuit-construction-kit-ac/changelog" );
        check( "http://phet.colorado.edu/en/simulation/circuit-construction-kit-dc" );
        check( "http://phet.colorado.edu/en/simulation/color-vision" );
        check( "http://phet.colorado.edu/en/simulation/conductivity" );
        check( "http://phet.colorado.edu/en/simulation/covalent-bonds" );
        check( "http://phet.colorado.edu/en/simulation/curve-fitting" );
        check( "http://phet.colorado.edu/en/simulation/curve-fitting/changelog" );
        check( "http://phet.colorado.edu/en/simulation/davisson-germer" );
        check( "http://phet.colorado.edu/en/simulation/discharge-lamps" );
        check( "http://phet.colorado.edu/en/simulation/discharge-lamps/changelog" );
        check( "http://phet.colorado.edu/en/simulation/eating-and-exercise" );
        check( "http://phet.colorado.edu/en/simulation/efield" );
        check( "http://phet.colorado.edu/en/simulation/electric-hockey" );
        check( "http://phet.colorado.edu/en/simulation/electric-hockey/changelog" );
        check( "http://phet.colorado.edu/en/simulation/energy-skate-park" );
        check( "http://phet.colorado.edu/en/simulation/equation-grapher" );
        check( "http://phet.colorado.edu/en/simulation/estimation" );
        check( "http://phet.colorado.edu/en/simulation/faraday" );
        check( "http://phet.colorado.edu/en/simulation/faradays-law" );
        check( "http://phet.colorado.edu/en/simulation/forces-1d" );
        check( "http://phet.colorado.edu/en/simulation/forces-1d/changelog" );
        check( "http://phet.colorado.edu/en/simulation/fourier" );
        check( "http://phet.colorado.edu/en/simulation/friction" );
        check( "http://phet.colorado.edu/en/simulation/gas-properties" );
        check( "http://phet.colorado.edu/en/simulation/gene-machine-lac-operon" );
        check( "http://phet.colorado.edu/en/simulation/generator" );
        check( "http://phet.colorado.edu/en/simulation/geometric-optics" );
        check( "http://phet.colorado.edu/en/simulation/glaciers" );
        check( "http://phet.colorado.edu/en/simulation/gravity-force-lab" );
        check( "http://phet.colorado.edu/en/simulation/greenhouse" );
        check( "http://phet.colorado.edu/en/simulation/hydrogen-atom" );
        check( "http://phet.colorado.edu/en/simulation/ladybug-motion-2d" );
        check( "http://phet.colorado.edu/en/simulation/lasers" );
        check( "http://phet.colorado.edu/en/simulation/lunar-lander" );
        check( "http://phet.colorado.edu/en/simulation/magnet-and-compass" );
        check( "http://phet.colorado.edu/en/simulation/magnets-and-electromagnets" );
        check( "http://phet.colorado.edu/en/simulation/magnets-and-electromagnets/changelog" );
        check( "http://phet.colorado.edu/en/simulation/mass-spring-lab" );
        check( "http://phet.colorado.edu/en/simulation/mass-spring-lab/changelog" );
        check( "http://phet.colorado.edu/en/simulation/maze-game" );
        check( "http://phet.colorado.edu/en/simulation/maze-game/changelog" );
        check( "http://phet.colorado.edu/en/simulation/microwaves" );
        check( "http://phet.colorado.edu/en/simulation/molecular-motors" );
        check( "http://phet.colorado.edu/en/simulation/motion-2d" );
        check( "http://phet.colorado.edu/en/simulation/moving-man" );
        check( "http://phet.colorado.edu/en/simulation/mri" );
        check( "http://phet.colorado.edu/en/simulation/my-solar-system" );
        check( "http://phet.colorado.edu/en/simulation/my-solar-system/changelog" );
        check( "http://phet.colorado.edu/en/simulation/natural-selection" );
        check( "http://phet.colorado.edu/en/simulation/nuclear-fission" );
        check( "http://phet.colorado.edu/en/simulation/ohms-law" );
        check( "http://phet.colorado.edu/en/simulation/optical-quantum-control" );
        check( "http://phet.colorado.edu/en/simulation/optical-tweezers" );
        check( "http://phet.colorado.edu/en/simulation/pendulum-lab" );
        check( "http://phet.colorado.edu/en/simulation/photoelectric" );
        check( "http://phet.colorado.edu/en/simulation/ph-scale" );
        check( "http://phet.colorado.edu/en/simulation/plinko-probability" );
        check( "http://phet.colorado.edu/en/simulation/projectile-motion" );
        check( "http://phet.colorado.edu/en/simulation/quantum-tunneling" );
        check( "http://phet.colorado.edu/en/simulation/quantum-wave-interference" );
        check( "http://phet.colorado.edu/en/simulation/radioactive-dating-game" );
        check( "http://phet.colorado.edu/en/simulation/radio-waves" );
        check( "http://phet.colorado.edu/en/simulation/reactants-products-and-leftovers" );
        check( "http://phet.colorado.edu/en/simulation/reactions-and-rates" );
        check( "http://phet.colorado.edu/en/simulation/resistance-in-a-wire" );
        check( "http://phet.colorado.edu/en/simulation/reversible-reactions" );
        check( "http://phet.colorado.edu/en/simulation/rotation" );
        check( "http://phet.colorado.edu/en/simulation/rotation/changelog" );
        check( "http://phet.colorado.edu/en/simulation/rutherford-scattering" );
        check( "http://phet.colorado.edu/en/simulations/category/biology" );
        check( "http://phet.colorado.edu/en/simulations/category/by-level" );
        check( "http://phet.colorado.edu/en/simulations/category/by-level/elementary-school" );
        check( "http://phet.colorado.edu/en/simulations/category/by-level/elementary-school/index" );
        check( "http://phet.colorado.edu/en/simulations/category/by-level/high-school" );
        check( "http://phet.colorado.edu/en/simulations/category/by-level/middle-school" );
        check( "http://phet.colorado.edu/en/simulations/category/by-level/university" );
        check( "http://phet.colorado.edu/en/simulations/category/by-level/university/index" );
        check( "http://phet.colorado.edu/en/simulations/category/chemistry" );
        check( "http://phet.colorado.edu/en/simulations/category/chemistry/general" );
        check( "http://phet.colorado.edu/en/simulations/category/chemistry/quantum" );
        check( "http://phet.colorado.edu/en/simulations/category/cutting-edge-research" );
        check( "http://phet.colorado.edu/en/simulations/category/earth-science" );
        check( "http://phet.colorado.edu/en/simulations/category/math" );
        check( "http://phet.colorado.edu/en/simulations/category/math/applications" );
        check( "http://phet.colorado.edu/en/simulations/category/math/applications/index" );
        check( "http://phet.colorado.edu/en/simulations/category/math/tools" );
        check( "http://phet.colorado.edu/en/simulations/category/new" );
        check( "http://phet.colorado.edu/en/simulations/category/new/index" );
        check( "http://phet.colorado.edu/en/simulations/category/physics" );
        check( "http://phet.colorado.edu/en/simulations/category/physics/electricity-magnets-and-circuits" );
        check( "http://phet.colorado.edu/en/simulations/category/physics/electricity-magnets-and-circuits/index" );
        check( "http://phet.colorado.edu/en/simulations/category/physics/heat-and-thermodynamics" );
        check( "http://phet.colorado.edu/en/simulations/category/physics/heat-and-thermodynamics/index" );
        check( "http://phet.colorado.edu/en/simulations/category/physics/light-and-radiation" );
        check( "http://phet.colorado.edu/en/simulations/category/physics/motion" );
        check( "http://phet.colorado.edu/en/simulations/category/physics/motion/index" );
        check( "http://phet.colorado.edu/en/simulations/category/physics/quantum-phenomena" );
        check( "http://phet.colorado.edu/en/simulations/category/physics/sound-and-waves" );
        check( "http://phet.colorado.edu/en/simulations/category/physics/sound-and-waves/index" );
        check( "http://phet.colorado.edu/en/simulations/category/physics/work-energy-and-power" );
        check( "http://phet.colorado.edu/en/simulations/category/physics/work-energy-and-power/index" );
        check( "http://phet.colorado.edu/en/simulation/self-driven-particle-model" );
        check( "http://phet.colorado.edu/en/simulation/semiconductor" );
        check( "http://phet.colorado.edu/en/simulation/signal-circuit" );
        check( "http://phet.colorado.edu/en/simulation/signal-circuit/changelog" );
        check( "http://phet.colorado.edu/en/simulations/index" );
        check( "http://phet.colorado.edu/en/simulations/keyword/1d" );
        check( "http://phet.colorado.edu/en/simulations/keyword/acceleration" );
        check( "http://phet.colorado.edu/en/simulations/keyword/acCircuits" );
        check( "http://phet.colorado.edu/en/simulations/keyword/alternatingCurrent" );
        check( "http://phet.colorado.edu/en/simulations/keyword/ammeter" );
        check( "http://phet.colorado.edu/en/simulations/keyword/angularPosition" );
        check( "http://phet.colorado.edu/en/simulations/keyword/angularVelocity" );
        check( "http://phet.colorado.edu/en/simulations/keyword/astronomy" );
        check( "http://phet.colorado.edu/en/simulation/soluble-salts" );
        check( "http://phet.colorado.edu/en/simulation/sound" );
        check( "http://phet.colorado.edu/en/simulation/states-of-matter" );
        check( "http://phet.colorado.edu/en/simulation/stern-gerlach" );
        check( "http://phet.colorado.edu/en/simulations/translated" );
        check( "http://phet.colorado.edu/en/simulations/translated/ar" );
        check( "http://phet.colorado.edu/en/simulations/translated/cs" );
        check( "http://phet.colorado.edu/en/simulations/translated/da" );
        check( "http://phet.colorado.edu/en/simulations/translated/de" );
        check( "http://phet.colorado.edu/en/simulations/translated/el" );
        check( "http://phet.colorado.edu/en/simulations/translated/es" );
        check( "http://phet.colorado.edu/en/simulations/translated/es_CO" );
        check( "http://phet.colorado.edu/en/simulations/translated/et" );
        check( "http://phet.colorado.edu/en/simulations/translated/eu" );
        check( "http://phet.colorado.edu/en/simulations/translated/fa" );
        check( "http://phet.colorado.edu/en/simulations/translated/fi" );
        check( "http://phet.colorado.edu/en/simulations/translated/fr" );
        check( "http://phet.colorado.edu/en/simulations/translated/ga" );
        check( "http://phet.colorado.edu/en/simulations/translated/hr" );
        check( "http://phet.colorado.edu/en/simulations/translated/hu" );
        check( "http://phet.colorado.edu/en/simulations/translated/in" );
        check( "http://phet.colorado.edu/en/simulations/translated/it" );
        check( "http://phet.colorado.edu/en/simulations/translated/iw" );
        check( "http://phet.colorado.edu/en/simulations/translated/ja" );
        check( "http://phet.colorado.edu/en/simulations/translated/ka" );
        check( "http://phet.colorado.edu/en/simulations/translated/km" );
        check( "http://phet.colorado.edu/en/simulations/translated/ko" );
        check( "http://phet.colorado.edu/en/simulations/translated/lt" );
        check( "http://phet.colorado.edu/en/simulations/translated/mk" );
        check( "http://phet.colorado.edu/en/simulations/translated/mn" );
        check( "http://phet.colorado.edu/en/simulations/translated/nb" );
        check( "http://phet.colorado.edu/en/simulations/translated/nl" );
        check( "http://phet.colorado.edu/en/simulations/translated/pl" );
        check( "http://phet.colorado.edu/en/simulations/translated/pt" );
        check( "http://phet.colorado.edu/en/simulations/translated/pt_BR" );
        check( "http://phet.colorado.edu/en/simulations/translated/ro" );
        check( "http://phet.colorado.edu/en/simulations/translated/ru" );
        check( "http://phet.colorado.edu/en/simulations/translated/sk" );
        check( "http://phet.colorado.edu/en/simulations/translated/sl" );
        check( "http://phet.colorado.edu/en/simulations/translated/sr" );
        check( "http://phet.colorado.edu/en/simulations/translated/tn" );
        check( "http://phet.colorado.edu/en/simulations/translated/tr" );
        check( "http://phet.colorado.edu/en/simulations/translated/uk" );
        check( "http://phet.colorado.edu/en/simulations/translated/vi" );
        check( "http://phet.colorado.edu/en/simulations/translated/zh_CN" );
        check( "http://phet.colorado.edu/en/simulations/translated/zh_TW" );
        check( "http://phet.colorado.edu/en/simulation/stretching-dna" );
        check( "http://phet.colorado.edu/en/simulation/the-ramp" );
        check( "http://phet.colorado.edu/en/simulation/torque" );
        check( "http://phet.colorado.edu/en/simulation/travoltage" );
        check( "http://phet.colorado.edu/en/simulation/travoltage/changelog" );
        check( "http://phet.colorado.edu/en/simulation/vector-addition" );
        check( "http://phet.colorado.edu/en/simulation/wave-interference" );
        check( "http://phet.colorado.edu/en/simulation/wave-on-a-string" );
        check( "http://phet.colorado.edu/en/troubleshooting" );
        check( "http://phet.colorado.edu/en/troubleshooting/flash" );
        check( "http://phet.colorado.edu/en/troubleshooting/java" );
    }
}
