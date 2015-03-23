var _gaq = _gaq || [];
_gaq.push( ['_setAccount', 'UA-5033201-1'] );
_gaq.push( ['_setDomainName', 'phet.colorado.edu'] );
_gaq.push( ['_trackPageview'] );
_gaq.push( ['_trackPageLoadTime'] );

var benchmarkTracker = null;

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
    };
    var s = document.getElementsByTagName( 'script' )[0];
    s.parentNode.insertBefore( ga, s );
})();

(function() {
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
    (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
    m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','hewlettOERTracker');
  hewlettOERTracker('create', 'UA-5033010-1', 'phet.colorado.edu');
  hewlettOERTracker('send', 'pageview');
})();

(function() {
    var phetJS = document.createElement( 'script' );
    phetJS.type = 'text/javascript';
    phetJS.async = true;
    phetJS.src = '/js/phet-v15-min.js';
    var s = document.getElementsByTagName( 'script' )[0];
    s.parentNode.insertBefore( phetJS, s );
})();
