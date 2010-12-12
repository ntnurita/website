/*
 Handles sorting of columns in the contribution lists available on the "Browse Contribution" and "Simulation" pages
 */

function trace( mess ) {
    $( "#phet-page-debug" )[0].innerHTML += "<br/>" + mess;
}

function sortByChildClassText( arr, className, reverse ) {
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
            return 1
        }
        else {
            return -1;
        }
    } );
}

function sortContributionsByTitle() {
    var arr = [];
    var items = $( ".ct-contribution" );
    var table = $( "#ct-table" )[0];
    trace( "table: " + table );
    items.each( function( index ) {
        arr.push( items[index] );
        $( items[index] ).remove();
    } );
    sortByChildClassText( arr, 'ct-title', false );
    $.each( arr, function( idx, val ) {
        table.appendChild( val );
    } );
    return false;
}

function sortContributionsByAuthors() {
    trace( "This is a test" );
    return false;
}

$( document ).ready( function() {

} );
