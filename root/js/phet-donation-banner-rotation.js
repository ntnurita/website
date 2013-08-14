// Copyright 2002-2011, University of Colorado

$( document ).ready( function () {
  var width = 192;
  var height = 144;
  var quantity = 4;

  /**
   * The index into the preview array that represents the preview currently in focus. The animation will slide until
   * this preview takes up the entire rotator.
   */
  var idx = 0;

  /**
   * The index into the preview array for the preview that has the focus at load time
   */
  var startIdx = 0;

  /**
   * The current offset (in pixels) of the desired slide position from the current slide position.
   */
  var offset = 0;

  var framesBetweenSwitch = 5 * 30; // * 30 since 30 frames a second. needs to be an integral number

  /**
   * Sliding-model velocity so we can do smooth animation
   */
  var velocity = 0;

  var totalWidth = width * quantity;

  function nextIdx( i ) {
    return (i + 1) < quantity ? i + 1 : 0;
  }

  var counter = 0;
  setInterval( function () {
    counter++;

    if ( counter % framesBetweenSwitch == 0 ) {
      // switch to the next image
      idx = nextIdx( idx );
      offset += width;
    }

    if ( offset == 0 ) {
      return;
    }

    // do some math that is mostly an over-damped oscillator but near the target is weighted with something else
    // copied somewhat from the ActionScript version in Rotator.as

    var c0 = 0.2;
    var c1 = 0.015;
    var a = -c0 * velocity - c1 * offset;
    var bounce = velocity + 0.5 * a;
    velocity += a;

    var minv = 5;
    var slide = 50;
    if ( Math.abs( offset ) < slide ) {
      // how much weight to give to the stabilizer
      var frac = Math.abs( offset ) / slide;
      if ( frac < 0 ) { frac = 0; }
      bounce *= 2 - frac;
    }
    if ( Math.abs( offset ) < minv ) {
      bounce = -offset;
    }

    offset += bounce;

    for ( var i = 0; i < quantity; i++ ) {
      var preview = document.getElementById( 'donation' + i );

      if ( preview ) {
        var x = ((i - idx) * width + offset) % totalWidth;
        if ( x < 0 ) { x += totalWidth; }
        if ( totalWidth - x < width ) {
          x -= totalWidth;
        }
        if ( x < width ) {
          preview.style.left = x + "px";
          preview.style.display = "block"; // make it visible
        }
        else {
          // hide it
          preview.style.left = "5000px";
          preview.style.display = "none";
        }
      }
    }
  }, 33 )
} );