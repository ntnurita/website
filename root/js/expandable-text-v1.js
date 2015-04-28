// Copyright 2002-2014, University of Colorado

var phet = phet || {};
phet.textIds = {};

/**
 * Toggle expandable text
 * @param {string} id - id of the div that will be expanded and retracted on click
 * @param {boolean} jumpToLocation - true if the page should scroll to the expandable text header as well as expanding
 * @returns {boolean}
 */
phet.toggleExpandableText = function( id, jumpToLocation ) {
  var sessionStorageSupported = window.sessionStorage;
  var e = document.getElementById( id );
  var thisElement = document.getElementById( id + '-header' );
  var newClass;
  if ( !e || phet.textIds[ 'text_' + id ] ) {
    return true;
  }
  if ( !e.style.height || e.style.height === "0px" ) {
    if ( sessionStorageSupported ) {
      sessionStorage.setItem( id, true );
    }
    var hash = '#' + id + '-header';
    if ( jumpToLocation ) {
      window.location.hash = '';
      window.location.hash = hash;
    }
    phet.textIds[ 'text_' + id ] = true;
    newClass = thisElement.className.replace( 'right', 'down' );
    thisElement.className = newClass;
    var expandInterval = setInterval( function() {
      var h = e.offsetHeight;
      var sh = e.scrollHeight;
      if ( h < sh ) {
        h += 20;
      }
      else {
        clearInterval( expandInterval );
        phet.textIds[ 'text_' + id ] = false;
      }
      e.style.height = h + "px";
    }, 8 );
  }
  else {
    if ( sessionStorageSupported ) {
      sessionStorage.setItem( id, false );
    }
    phet.textIds[ 'text_' + id ] = true;
    newClass = thisElement.className.replace( 'down', 'right' );
    thisElement.className = newClass;
    var retractInterval = setInterval( function() {
      var h = e.offsetHeight;
      if ( h > 0 ) {
        h -= 20;
      }
      else {
        e.style.height = "0px";
        clearInterval( retractInterval );
        phet.textIds[ 'text_' + id ] = false;
      }
      e.style.height = h + "px";
    }, 8 );
  }
  return false;
};

$( document ).ready( function() {
  phet.toggleExpandableText( window.location.hash.replace( '#', '' ).replace( '-header', '' ), true );

  // save the state of expandable texts for when the back button is used
  if ( window.sessionStorage ) {
    for ( var i = 0; i < sessionStorage.length; i++ ) {
      var key = sessionStorage.key( i );
      var val = sessionStorage.getItem( key );
      if ( val === 'true' ) {
        phet.toggleExpandableText( key, false );
      }
    }
  }
} );
