/*
 Handles sorting of columns in the contribution lists available on the "Browse Contribution" and "Simulation" pages
 */

var phet = new Object(); // it's like our namespace. TODO: when we include multiple JS files (if we do), do this first

phet.trace = function( mess ) {
    $( "#phet-page-debug" )[0].innerHTML += "<br/>" + mess;
};

phet.sortByChildClassText = function( arr, className, reverse ) {
    arr.sort( function( a, b ) {
        var titleA = $( a ).find( '.' + className ).text().toLowerCase();
        var titleB = $( b ).find( '.' + className ).text().toLowerCase();
        if ( titleA == titleB ) {
            return 0;
        }
        var larger = titleA > titleB;
        if ( reverse ) {
            larger = !larger;
        }
        if ( larger ) {
            return 1;
        }
        else {
            return -1;
        }
    } );
};

phet.sortContributionsByTitle = function() {
    var arr = [];
    var items = $( ".ct-contribution" );
    var table = $( "#ct-table" )[0];
    phet.trace( "table: " + table );
    items.each( function( index ) {
        arr.push( items[index] );
        $( items[index] ).remove();
    } );
    phet.sortByChildClassText( arr, 'ct-title', false );
    $.each( arr, function( idx, val ) {
        table.appendChild( val );
    } );
    return false;
};

phet.sortContributionsByAuthors = function() {
    phet.trace( "This is a test" );
    return false;
};

$( document ).ready( function() {

} );
