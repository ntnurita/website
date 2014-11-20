/**
 * Uses three animated divs to create a video player carousel
 */

var switchVideo;

// list all videos here
var simVideos = [
  { title: 'Balancing Act', path: 'balancing-act' },
  { title: 'Color Vision', path: 'color-vision' },
  { title: 'Energy Skate Park Basics', path: 'energy-skate-park-basics' },
  { title: 'Faraday\'s Law', path: 'faradays-law' },
  { title: 'Friction', path: 'friction' },
  { title: 'John Travoltage', path: 'john-travoltage' },
  { title: 'Wave on a String', path: 'wave-on-a-string' }
];

document.addEventListener( 'DOMContentLoaded', function() {
  var sourceIndex = 0; // index into simVideos
  var locale = ( window.location.pathname === '/' ) ? '/en/' : window.location.pathname;
  var sourceFiles = [];
  var i;

  // create an object for each video with links to the videos, title, link, and fallback image
  for ( i = 0; i < simVideos.length; i++ ) {
    var prefix = '/files/rotator/' + simVideos[i].path;
    sourceFiles.push( {
      sources: [prefix + '.mp4', prefix + '.ogv'],
      title: simVideos[i].title,
      link: 'simulation/' + simVideos[i].path,
      fallback: prefix + '.jpg' } );
  }

  var videos = [];
  var sources = [];
  var fallbackImages = [];

  var container = document.getElementById( 'video-container' );
  var simName = document.getElementById( 'sim-name' );

  var timer; // setInterval for changing videos
  var switchable = true; // whether or not the switch video buttons are enabled (they get disabled while animating)
  var currentIndex = 0; // index of the current video player

  // add the video elements in divs
  for ( i = 0; i < 3; i++ ) {
    var div = document.createElement( 'div' );
    var widthValue = (i === 0) ? 0 : (i === 1) ? 100 : -100;
    div.setAttribute( 'class', 'box' );
    div.setAttribute( 'style', 'margin-top: 0; ' +
                               'position: absolute; ' +
                               'width: 100%; ' +
                               'height: 100%; ' +
                               'left: ' + widthValue + '%;' );

    videos.push( document.createElement( 'video' ) );
    // using width and height 100% doesn't work in FF, so its hardcoded here
    videos[i].setAttribute( 'width', '325px' );
    videos[i].setAttribute( 'height', '213px' );
    videos[i].setAttribute( 'muted', '' );

    sources.push( document.createElement( 'source' ) );
    sources.push( document.createElement( 'source' ) );
    sources[2 * i].setAttribute( 'type', 'video/mp4' );
    sources[2 * i + 1].setAttribute( 'type', 'video/ogg' );

    fallbackImages.push( document.createElement( 'img' ) );
    fallbackImages[i].setAttribute( 'width', '325px' );
    fallbackImages[i].setAttribute( 'height', '213px' );

    videos[i].appendChild( sources[2 * i] );
    videos[i].appendChild( sources[2 * i + 1] );
    videos[i].appendChild( fallbackImages[i] );

    div.appendChild( videos[i] );
    container.appendChild( div );
  }

  /**
   * Switch the video in the carousel by animating the div to slide over and changing the video source
   * @param direction the direction that the carousel should slide
   */
  switchVideo = function( direction ) {
    if ( !switchable ) {
      return; // don't allow the video to be switched during animation
    }
    switchable = false;

    // currentIndex is the index of the div to be animated. Only three divs are needed to provide a smooth animation
    currentIndex = ( direction === 'left' ) ? ( currentIndex + 1 ) % 3 : ( currentIndex - 1 );
    if ( currentIndex < 0 ) {
      currentIndex += 3;
    }

    if ( direction === 'left' ) {
      $( '.box' ).each( function() {
        if ( $( this ).offset().left < $( '#video-container' ).offset().left ) {
          $( this ).css( 'left', '100%' );
        }
        else if ( $( this ).offset().left >= $( '#video-container' ).offset().left + $( '#video-container' ).width() ) {
          $( this ).animate( {
            left: '0%'
          }, 1000 );
        }
        else {
          $( this ).animate( {
            left: '-100%'
          }, 1000 );
        }
      } );
    }
    else { // direction === right
      $( '.box' ).each( function() {
        if ( $( this ).offset().left < $( '#video-container' ).offset().left ) {
          $( this ).animate( {
            left: '0%'
          }, 1000 );
        }
        else if ( $( this ).offset().left >= $( '#video-container' ).offset().left + $( '#video-container' ).width() ) {
          $( this ).css( 'left', '-100%' );
        }
        else {
          $( this ).animate( {
            left: '100%'
          }, 1000 );
        }
      } );
    }

    // setup and start the next video
    sourceIndex = ( direction === 'left' ) ? ( sourceIndex + 1 ) % sourceFiles.length : ( sourceIndex - 1 );
    if ( sourceIndex < 0 ) {
      sourceIndex += sourceFiles.length;
    }

    sources[2 * currentIndex].setAttribute( 'src', sourceFiles[sourceIndex].sources[0] );
    sources[2 * currentIndex + 1].setAttribute( 'src', sourceFiles[sourceIndex].sources[1] );
    fallbackImages[currentIndex].setAttribute( 'src', sourceFiles[sourceIndex].fallback );
    videos[currentIndex].setAttribute( 'poster', sourceFiles[sourceIndex].fallback );
    videos[currentIndex].load();
    videos[currentIndex].play();
    simName.innerHTML = sourceFiles[sourceIndex].title;
    var href = locale + sourceFiles[sourceIndex].link;
    simName.setAttribute( 'href', href );
    document.getElementById( 'video-container' ).onclick = function() { window.location.href = href; };

    // restart the timer
    clearInterval( timer );
    timer = setInterval( function() {switchVideo( 'left' );}, 5000 );

    // set a timeout to ensure the button can't be pressed while animating
    setTimeout( function() {switchable = true;}, 1000 );
  };

  // start the first video
  sources[2 * currentIndex].setAttribute( 'src', sourceFiles[sourceIndex].sources[0] );
  sources[2 * currentIndex + 1].setAttribute( 'src', sourceFiles[sourceIndex].sources[1] );
  fallbackImages[currentIndex].setAttribute( 'src', sourceFiles[sourceIndex].fallback );
  videos[currentIndex].setAttribute( 'poster', sourceFiles[sourceIndex].fallback );
  videos[currentIndex].play();
  simName.innerHTML = sourceFiles[sourceIndex].title;
  var href = locale + sourceFiles[sourceIndex].link;
  simName.setAttribute( 'href', href );
  document.getElementById( 'video-container' ).onclick = function() { window.location.href = href; };

  // switch videos every 5 seconds
  timer = setInterval( function() {switchVideo( 'left' );}, 5000 );

}, false );
