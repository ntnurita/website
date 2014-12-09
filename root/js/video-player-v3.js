/**
 * Uses three animated divs to create a video player carousel
 */

var switchVideo;

// list all videos here
var simVideos = [
  { title: 'Balancing Act', link: 'simulation/balancing-act' },
  { title: 'Color Vision', link: 'simulation/color-vision' },
  { title: 'Energy Skate Park Basics', link: 'simulation/energy-skate-park-basics' },
  { title: 'Faraday\'s Law', link: 'simulation/faradays-law' },
  { title: 'Friction', link: 'simulation/friction' },
  { title: 'John Travoltage', link: 'simulation/travoltage' },
  { title: 'Wave on a String', link: 'simulation/wave-on-a-string' }
];

// force safari to reload page instead of saving the old state when users press 'back'
// this prevents the video from coming up as blank until the carousel shifts
window.onpageshow = function( event ) {
  if ( event.persisted ) {
    window.location.reload()
  }
};

document.addEventListener( 'DOMContentLoaded', function() {
  var ANIMATE_MILLISECONDS = 1000;
  var INTERVAL_LENGTH = 5000;
  var sourceIndex = 0; // index into simVideos

  var installerPath = 'index.html';
  var isInstallerBuild = window.location.pathname.indexOf( installerPath ) > -1;
  var locale;
  if ( isInstallerBuild ) {
    var splitPath = window.location.pathname.split( '/' );
    var localeRegex = /^.{2}$|^.{2}_.{2}$/;
    locale = 'file://' + window.location.pathname.replace( installerPath, localeRegex.test( splitPath[splitPath.length - 2] ) ?
                                                                          splitPath[splitPath.length - 2] + '/' : 'en/' );
  }
  else {
    locale = ( window.location.pathname === '/' ) ? '/en/' : window.location.pathname;
  }

  // set the first video to be in focus
  document.getElementById( 'box0' ).setAttribute( 'style', 'left: 0%' );

  var videos = document.getElementsByClassName( 'sim-video' );
  var container = document.getElementById( 'video-container' );
  var simName = document.getElementById( 'sim-name' );

  var timer; // setInterval for changing videos
  var switchable = true; // whether or not the switch video buttons are enabled (they get disabled while animating)

  /**
   * Switch the video in the carousel by animating the div to slide over and changing the video source
   * @param direction the direction that the carousel should slide
   */
  switchVideo = function( direction ) {
    if ( !switchable ) {
      return; // don't allow the video to be switched during animation
    }
    switchable = false;

    var boxes = $( '.box' );

    // get the current video div
    var currentBoxId = boxes.filter( function() {
      return $( this ).offset().left === $( '#video-container' ).offset().left
    } )[0].id;
    var currentBox = $( '#' + currentBoxId );

    if ( direction === 'left' ) {
      if ( currentBox.next().size() > 0 ) {
        currentBox.next().css( 'left', '100%' );
      }
      else {
        boxes.first().css( 'left', '100%' );
      }

      currentBox.animate( {
        left: '-100%'
      }, ANIMATE_MILLISECONDS );

      if ( currentBox.next().size() > 0 ) {
        currentBox.next().animate( {
          left: '0%'
        }, ANIMATE_MILLISECONDS );
      }
      else {
        boxes.first().animate( {
          left: '0%'
        }, ANIMATE_MILLISECONDS );
      }
    }
    else { // direction === 'right'

      if ( currentBox.prev().size() > 0 ) {
        currentBox.prev().css( 'left', '-100%' );
      }
      else {
        boxes.last().css( 'left', '-100%' );
      }

      currentBox.animate( {
        left: '100%'
      }, ANIMATE_MILLISECONDS );

      if ( currentBox.prev().size() > 0 ) {
        currentBox.prev().animate( {
          left: '0%'
        }, ANIMATE_MILLISECONDS );
      }
      else {
        boxes.last().animate( {
          left: '0%'
        }, ANIMATE_MILLISECONDS );
      }
    }

    // setup and start the next video
    sourceIndex = ( direction === 'left' ) ? ( sourceIndex + 1 ) % simVideos.length : ( sourceIndex - 1 );
    if ( sourceIndex < 0 ) {
      sourceIndex += simVideos.length;
    }

    videos[sourceIndex].play();
    simName.innerHTML = simVideos[sourceIndex].title;
    var href = locale + simVideos[sourceIndex].link + ( isInstallerBuild ? '.html' : '' );
    simName.setAttribute( 'href', href );
    document.getElementById( 'video-container' ).onclick = function() { window.location.href = href; };

    // restart the timer
    clearInterval( timer );
    timer = setInterval( function() {switchVideo( 'left' );}, INTERVAL_LENGTH );

    // set a timeout to ensure the button can't be pressed while animating
    setTimeout( function() {switchable = true;}, ANIMATE_MILLISECONDS );
  };

  videos[sourceIndex].play();
  simName.innerHTML = simVideos[sourceIndex].title;
  var href = locale + simVideos[sourceIndex].link + ( isInstallerBuild ? '.html' : '' );
  simName.setAttribute( 'href', href );
  document.getElementById( 'video-container' ).onclick = function() { window.location.href = href; };

  // switch videos every 5 seconds
  timer = setInterval( function() {switchVideo( 'left' );}, INTERVAL_LENGTH );

}, false );
