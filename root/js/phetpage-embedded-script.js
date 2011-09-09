var _gaq = _gaq || [];
_gaq.push( ['_setAccount', 'UA-5033201-1'] );
_gaq.push( ['_setDomainName', 'phet.colorado.edu'] );
_gaq.push( ['_trackPageview'] );
_gaq.push( ['_trackPageLoadTime'] );
_gaq.push( ['b._setAccount', 'UA-5033010-1'] );
_gaq.push( ['b._setDomainName', 'phet.colorado.edu'] );
_gaq.push( ['b._trackPageview'] );

var benchmarkTracker = null;
var overallTracker = null;

(function() {
    var ga = document.createElement( 'script' );
    ga.type = 'text/javascript';
    ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    ga.onload = ga.onreadystatechange = function() {
        var rs = this.readyState;
        if ( rs && rs != 'complete' && rs != 'loaded' ) {
            return;
        }
        benchmarkTracker = _gat._getTracker( "UA-5033201-1" );
        benchmarkTracker._setDomainName( 'phet.colorado.edu' );
        overallTracker = _gat._getTracker( "UA-5033010-1" );
        overallTracker._setDomainName( 'phet.colorado.edu' );
    };
    var s = document.getElementsByTagName( 'script' )[0];
    s.parentNode.insertBefore( ga, s );
})();

(function() {
    var phetJS = document.createElement( 'script' );
    phetJS.type = 'text/javascript';
    phetJS.async = true;
    phetJS.src = '/js/phet-v9-min.js';
    var s = document.getElementsByTagName( 'script' )[0];
    s.parentNode.insertBefore( phetJS, s );
})();

// when compressed: