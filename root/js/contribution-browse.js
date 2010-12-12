/*
 Handles sorting of columns in the contribution lists available on the "Browse Contribution" and "Simulation" pages
 */

var ContributionSectionStyle = "background-color: white; border-top: 1px solid #666; font-weight: bold; font-size: 120%;";

// TODO: promote to phet.contribution
var phet = new Object(); // it's like our namespace. TODO: when we include multiple JS files (if we do), do this first

phet.separators = [];

phet.trace = function( mess ) {
    var debugs = $( "#phet-page-debug" );
    if ( debugs.length > 0 ) {
        $( "#phet-page-debug" )[0].innerHTML += "<br/>" + mess;
    }
};

// get an array of contributions (tr elements)
phet.getContributions = function() {
    var items = $( ".ct-contribution" );
    var arr = [];
    var spot = {};
    phet.trace( 'items.length: ' + items.length );
    items.each( function( idx, tr ) {
        var rel = $( tr ).attr( 'rel' ); // unique string
        if ( !spot[rel] ) {
            // not added
            arr.push( tr );
            spot[rel] = true;
        }
    } );
    phet.trace( 'contributions.length: ' + arr.length );
    return arr;
};

phet.wipeContributions = function() {
    $( ".ct-contribution" ).each( function( idx, tr ) {
        $( tr ).remove();
    } );
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
    var arr = phet.getContributions();
    var table = $( "#ct-table" )[0];

    phet.wipeContributions();

    arr.sort( compare );

    $.each( arr, function( idx, tr ) {
        // give every other row a background color
        if ( idx % 2 == 0 ) {
            $( tr ).addClass( 'list-highlight-background' );
        }
        else {
            $( tr ).removeClass( 'list-highlight-background' );
        }
        table.appendChild( tr );
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

// sorts by level and type.
phet.sortContributionsByTag = function( className, tags ) {
    phet.beforeSort( className );
    var items = phet.getContributions();
    phet.trace( "items.length: " + items.length );
    var table = $( "#ct-table" )[0];

    // initialize directory (maps tags => array of contributions)
    var directory = {};
    $.each( tags, function( idx, val ) {
        directory[val] = [];
    } );

    phet.wipeContributions();

    // fill directory
    $.each( items, function( index, tr ) {
        $.each( $( tr ).find( '.' + className ).attr( 'rel' ).split( ',' ), function( idx, tagString ) {
            directory[tagString].push( tr );
        } );
    } );

    // add everything back in
    $.each( tags, function( idx, tagString ) {
        if ( directory[tagString].length > 0 ) {
            phet.addContributionSeparator( tagString );
            $.each( directory[tagString], function( idx, tr ) {
                var newNode = tr.cloneNode( true );
                if ( idx % 2 == 0 ) {
                    $( tr ).addClass( 'list-highlight-background' );
                }
                else {
                    $( tr ).removeClass( 'list-highlight-background' );
                }
                table.appendChild( newNode );
            } );
        }
    } );
};

phet.sortContributionsByLevel = function() {
    phet.sortContributionsByTag( "ct-level", phet.getAllLevels() );
    return false;
};

phet.sortContributionsByType = function() {
    phet.sortContributionsByTag( "ct-type", phet.getAllTypes() );
    return false;
};

// initial setup and handling
$( document ).ready( function() {

} );

phet.currentSort = {className: 'BOGUS_CLASS_NAME', reverse: false}; // set up initial conditions

phet.beforeSort = function( className ) {
    $.each( phet.separators, function( idx, separator ) {
        $( separator ).remove();
    } );
    phet.separators = [];

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

phet.addContributionSeparator = function( text ) {
    var tr = document.createElement( 'tr' );
    var td = document.createElement( 'td' );
    td.innerHTML = "<br/>" + text;
    td.setAttribute( 'style', ContributionSectionStyle );
    td.setAttribute( 'colspan', '' + phet.colspan() ); // our number of columns can change, so we need to set it correctly here
    tr.appendChild( td );
    $( "#ct-table" )[0].appendChild( tr );
    phet.separators.push( tr );
};

phet.colspan = function() {
    return $( "#ct-table-header" ).children().length;
};

// return array of level strings
phet.getAllLevels = function() {
    return $( '#ct-table' ).attr( 'rel' ).split( '|' )[0].split( ',' );
};

// return array of type strings
phet.getAllTypes = function() {
    return $( '#ct-table' ).attr( 'rel' ).split( '|' )[1].split( ',' );
};


