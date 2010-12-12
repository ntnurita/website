/*
 Handles sorting of columns in the contribution lists available on the "Browse Contribution" and "Simulation" pages
 */

var phet = new Object(); // it's like our namespace. TODO: when we include multiple JS files (if we do), do this first

phet.trace = function( mess ) {
    $( "#phet-page-debug" )[0].innerHTML += "<br/>" + mess;
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
        return (timestampA - timestampB ) * (reverse ? -1 : 1);
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
    phet.sortContributionsWithFunction( phet.compareClassText( 'ct-title', false ) );
    return false;
};

phet.sortContributionsByAuthors = function() {
    phet.sortContributionsWithFunction( phet.compareClassText( 'ct-authors', false ) );
    return false;
};

phet.sortContributionsByUpdated = function() {
    phet.sortContributionsWithFunction( phet.compareClassRel( 'ct-updated', false ) );
    return false;
};

$( document ).ready( function() {

} );
