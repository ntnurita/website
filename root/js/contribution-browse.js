/*
 Handles sorting of columns in the contribution lists available on the "Browse Contribution" and "Simulation" pages
 */

// TODO: remember to detect correct colspan
var ContributionSectionStyle = "background-color: white; border-top: 1px solid #666; font-weight: bold;";

var phet = new Object(); // it's like our namespace. TODO: when we include multiple JS files (if we do), do this first

phet.trace = function( mess ) {
    var debugs = $( "#phet-page-debug" );
    if ( debugs.length > 0 ) {
        $( "#phet-page-debug" )[0].innerHTML += "<br/>" + mess;
    }
};

phet.compareClassText = function( className, reverse ) {
    return function( a, b ) {
        var titleA = $( a ).find( '.' + className ).text().toLowerCase();
        var titleB = $( b ).find( '.' + className ).text().toLowerCase();
        if ( titleA == titleB ) {
            return 0;
        }
        var larger = titleA > titleB;
        if ( reverse ) {
            larger = !larger;
        }
        return larger ? 1 : -1;
    };
};

phet.compareClassRel = function( className, reverse ) {
    return function( a, b ) {
        // that's right. we are storing the timestamps in the 'rel' attribute so this will validate
        var timestampA = parseInt( $( a ).find( '.' + className ).attr( 'rel' ) );
        var timestampB = parseInt( $( b ).find( '.' + className ).attr( 'rel' ) );
        return (timestampA - timestampB ) * (reverse ? 1 : -1);
    };
};

phet.sortContributionsWithFunction = function( compare ) {
    var arr = []; // we store all the rows here
    var items = $( ".ct-contribution" );
    var table = $( "#ct-table" )[0];

    // store rows and remove them
    items.each( function( index ) {
        arr.push( items[index] );
        $( items[index] ).remove();
    } );

    arr.sort( compare );

    $.each( arr, function( idx, val ) {
        // give every other row a background color
        if ( idx % 2 == 0 ) {
            $( val ).addClass( 'list-highlight-background' );
        }
        else {
            $( val ).removeClass( 'list-highlight-background' );
        }
        table.appendChild( val );
    } );
};

phet.sortContributionsByTitle = function() {
    phet.beforeSort( 'ct-title' );
    phet.sortContributionsWithFunction( phet.compareClassText( 'ct-title', phet.currentSort.reverse ) );
    return false;
};

phet.sortContributionsByAuthors = function() {
    phet.beforeSort( 'ct-authors' );
    phet.sortContributionsWithFunction( phet.compareClassText( 'ct-authors', phet.currentSort.reverse ) );
    return false;
};

phet.sortContributionsByUpdated = function() {
    phet.beforeSort( 'ct-updated' );
    phet.sortContributionsWithFunction( phet.compareClassRel( 'ct-updated', phet.currentSort.reverse ) );
    return false;
};

phet.sortContributionsByLevel = function() {
    var items = $( ".ct-contribution" );
    var table = $( "#ct-table" )[0];

    items.each( function( index ) {
        var tr = items[index];
        var rel = $( tr ).find( '.ct-level' ).attr( 'rel' );
        phet.trace( "rel: " + rel );
    } );

    phet.trace( "colspan: " + $( "#ct-table-header" ).children().length );

    phet.trace( "table.rel: " + $( table ).attr( 'rel' ) );

    return false;
};

phet.sortContributionsByType = function() {
    return false;
};

// initial setup and handling
$( document ).ready( function() {

} );

phet.currentSort = {className: 'BOGUS_CLASS_NAME', reverse: false}; // set up initial conditions

phet.beforeSort = function( className ) {
    if ( className == phet.currentSort.className ) {
        // if already sorted on the desired column, reverse it. we can ignore the reverse flag if we want
        phet.currentSort.reverse = !phet.currentSort.reverse;
    }
    else {
        // change column sorted row, and set to initially not reverse
        phet.currentSort.className = className;
        phet.currentSort.reverse = false;
    }
};