// Copyright 2002-2014, University of Colorado

/**
 * Toggle expandable text
 * @param id of the div that will be expanded and retracted on click
 * @returns {boolean}
 */
function toggleMe( id ) {
  var e = document.getElementById( id );
  var thisElement = document.getElementById( id + '-header' );
  if ( !e || window['answer_' + id] ) {
    return true;
  }
  if ( !e.style.height || e.style.height === "0px" ) {
    window['answer_' + id] = true;
    thisElement.className.replace( 'right', 'down' );
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
    thisElement.className.replace( 'down', 'right' );
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
