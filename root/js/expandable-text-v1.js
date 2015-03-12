// Copyright 2002-2014, University of Colorado

/**
 * Toggle expandable text
 * @param {string} id - the id of the div that will be expanded and retracted on click
 * @param {boolean} jumpToLocation - true if we should set the url hash to the location of the expanding text
 * @returns {boolean}
 */
function toggleMe( id, jumpToLocation ) {
  var e = document.getElementById( id );
  var thisElement = document.getElementById( id + '-header' );
  var newClass;
  if ( !e || window['answer_' + id] ) {
    return true;
  }
  if ( !e.style.height || e.style.height === "0px" ) {
    if ( jumpToLocation ) {
      window.location.hash = '';
      window.location.hash = '#' + id;
    }
    window['answer_' + id] = true;
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
        window['answer_' + id] = false;
      }
      e.style.height = h + "px";
    }, 8 );
  }
  else {
    window['answer_' + id] = true;
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
        window['answer_' + id] = false;
      }
      e.style.height = h + "px";
    }, 8 );
  }
  return false;
}

(function() {
  var interval = setInterval( function() {
    if ( typeof $ !== 'undefined' ) {
      $( document ).ready( function() {
        toggleMe( window.location.hash.replace( '#', '' ).replace( '-header', '' ), true );
      } );
      clearInterval( interval );
    }
  }, 100 );
})();