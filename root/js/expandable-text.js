// Copyright 2002-2014, University of Colorado

/**
 * Created by aarondavis on 9/19/14.
 */
function expand( element ) {
  var target = document.getElementById( element );
  var h = target.offsetHeight;
  var sh = target.scrollHeight;
  var loopTimer = setTimeout( 'expand(\'' + element + '\')', 8 );
  if ( h < sh ) {
    h += 5;
  }
  else {
    clearTimeout( loopTimer );
  }
  target.style.height = h + "px";
}

function retract( element ) {
  var target = document.getElementById( element );
  var h = target.offsetHeight;
  var loopTimer = setTimeout( 'retract(\'' + element + '\')', 8 );
  if ( h > 0 ) {
    h -= 5;
  }
  else {
    target.style.height = "0px";
    clearTimeout( loopTimer );
  }
  target.style.height = h + "px";
}

function toggleMe( id ) {
  var e = document.getElementById( id );
  if ( !e ) {
    return true;
  }
  if ( !e.style.height || e.style.height === "0px" ) {
    expand( id );
  }
  else {
    retract( id );
  }
  return false;
}
