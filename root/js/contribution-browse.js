/*
 Handles sorting of columns in the contribution lists available on the "Browse Contribution" and "Simulation" pages
 */

var ContributionSectionStyle = "background-color: white; font-weight: bold; font-size: 120%;";

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

phet.getTable = function() {
    return $( "#ct-table" )[0];
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

phet.compareIconPresent = function( className, reverse ) {
  return function( a, b ) {
    var iconPresentA = $( a ).find( '.' + className + ' img' ).length;
    var iconPresentB = $( b ).find( '.' + className + ' img' ).length;
    return ( reverse ) ? iconPresentA > iconPresentB : iconPresentA < iconPresentB;
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
    var table = phet.getTable();

    phet.wipeContributions();

    arr.sort( compare );

    $.each( arr, function( idx, tr ) {
        table.appendChild( tr );
    } );
};

/**
 * Lengthen the height of the element with the given id.
 * This is used on the sim page to adjust the height of the expandable text area when the table
 * size changes dynamically.
 * @param id
 */
phet.lengthenExpandableText = function( id ) {
  var element = document.getElementById( id );
  element.style.height = element.scrollHeight + 'px';
};

/**
 * Shorten the height of the element with the given id.
 * This is used on the sim page to adjust the height of the expandable text area when the table
 * size changes dynamically.
 * @param id
 */
phet.shortenExpandableText = function( id ) {
  var element = document.getElementById( id );
  element.style.height = phet.originalHeights[ id ];
};

phet.sortContributionsByTitle = function() {
    phet.beforeSort( 'ct-title' );
    phet.sortContributionsWithFunction( phet.compareClassText( 'ct-title', phet.currentSort.reverse ) );
  if ( window.location.href.indexOf( 'simulation' ) > -1 ) {
    phet.shortenExpandableText( 'for-teachers' );
  }
    return false;
};

phet.sortContributionsByGoldStar = function() {
  phet.beforeSort( 'ct-gold-star' );
  phet.sortContributionsWithFunction( phet.compareIconPresent( 'ct-gold-star', phet.currentSort.reverse ) );
  if ( window.location.href.indexOf( 'simulation' ) > -1 ) {
    phet.shortenExpandableText( 'for-teachers' );
  }
  return false;
};

phet.sortContributionsByPhet = function() {
  phet.beforeSort( 'ct-phet' );
  phet.sortContributionsWithFunction( phet.compareIconPresent( 'ct-phet', phet.currentSort.reverse ) );
  if ( window.location.href.indexOf( 'simulation' ) > -1 ) {
    phet.shortenExpandableText( 'for-teachers' );
  }
  return false;
};

phet.sortContributionsByAuthors = function() {
    phet.beforeSort( 'ct-authors' );
    phet.sortContributionsWithFunction( phet.compareClassText( 'ct-authors', phet.currentSort.reverse ) );
  if ( window.location.href.indexOf( 'simulation' ) > -1 ) {
    phet.shortenExpandableText( 'for-teachers' );
  }
    return false;
};

phet.sortContributionsByUpdated = function() {
    phet.beforeSort( 'ct-updated' );
    phet.sortContributionsWithFunction( phet.compareClassRel( 'ct-updated', phet.currentSort.reverse ) );
  if ( window.location.href.indexOf( 'simulation' ) > -1 ) {
    phet.shortenExpandableText( 'for-teachers' );
  }
    return false;
};

// sorts by level and type.
phet.sortContributionsByTag = function( className, tags ) {
    phet.beforeSort( className );
    var items = phet.getContributions();
    phet.trace( "items.length: " + items.length );
    var table = phet.getTable();

    items.sort( phet.compareClassRel( 'ct-updated', false ) );

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
                table.appendChild( newNode );
            } );
        }
    } );
  if ( window.location.href.indexOf( 'simulation' ) > -1 ) {
    phet.shortenExpandableText( 'for-teachers' );
  }
};

phet.sortContributionsByLevel = function() {
    phet.sortContributionsByTag( "ct-level", phet.getAllLevels() );
  if ( window.location.href.indexOf( 'simulation' ) > -1 ) {
    phet.lengthenExpandableText( 'for-teachers' );
  }
    return false;
};

phet.sortContributionsByType = function() {
    phet.sortContributionsByTag( "ct-type", phet.getAllTypes() );
  if ( window.location.href.indexOf( 'simulation' ) > -1 ) {
    phet.lengthenExpandableText( 'for-teachers' );
  }
    return false;
};

phet.sortContributionsBySimulations = function() {
    phet.beforeSort( "ct-sim" );
    var items = phet.getContributions();
    var table = phet.getTable();

    items.sort( phet.compareClassRel( 'ct-updated', false ) );

    var directory = {};
    phet.wipeContributions();
    var simNames = [];

    $.each( items, function( index, tr ) {
        $( tr ).find( '.ct-sim' ).each( function( idx, sim ) {
            var simName = $( sim ).text();
            if ( typeof directory[simName] == "undefined" ) {
                directory[simName] = [];
                simNames.push( simName );
            }
            directory[simName].push( tr );
        } );
    } );

    simNames.sort();

    if ( phet.currentSort.reverse ) {
        simNames.reverse();
    }

    $.each( simNames, function( idx, simName ) {
        phet.addContributionSeparator( simName );
        $.each( directory[simName], function( idx, tr ) {
            var newNode = tr.cloneNode( true );
            table.appendChild( newNode );
        } );
    } );

  if ( window.location.href.indexOf( 'simulation' ) > -1 ) {
    phet.shortenExpandableText( 'for-teachers' );
  }
    return false;
};

// initial setup and handling
$( document ).ready( function() {
    // only do this on the simulation pages
    if ( window.location.href.indexOf( 'simulation' ) > -1 ) {
      var forTeachers = 'for-teachers';
      phet.originalHeights = {};
      phet.originalHeights[ forTeachers ] = document.getElementById( forTeachers ).scrollHeight + 'px';
    }
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
    phet.getTable().appendChild( tr );
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


