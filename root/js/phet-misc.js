// this causes all "external" rel-tagged links to open in a new tab / window
$( document ).ready( function () {
  $( 'a[rel*=external]' ).each( function () {
    $( this ).attr( 'target', '_blank' );
  } );
  $( 'a[class*=external]' ).each( function () {
    $( this ).attr( 'target', '_blank' );
  } );
  $( '.autocompleteOff' ).each( function () {
    $( this ).attr( 'autocomplete', 'off' );
  } );
  if ( !Modernizr.svg ) {
//  if( Modernizr.svg && window.devicePixelRatio > 1 ) {
    // replace the main logo with the SVG version if we are using a high resolution, and have SVG support
//    $('img[src*="phet-logo-117.png"]').attr('src', function() {
//      return $(this).attr('src').replace('.png', '.svg');
//    });

    $( 'img[src*="svg"]' ).attr( 'src', function () {
      return $( this ).attr( 'src' ).replace( '.svg', '.png' );
    } );
  }

  // feature detection if we are on the HTML5 page
  if ( document.getElementById( "html5-features-missing" ) ) {
    var criticalFeaturesMissing = [];
    var modernizrFeatures = [
      'hsla',
      'opacity',
      'rgba',
      'csstransforms',
      'canvas',
      'canvastext',
      'audio',
      'inlinesvg',
      'svg',
      'svgclippaths',
      'json'
    ];
    for ( var i = 0; i < modernizrFeatures.length; i++ ) {
      if ( !Modernizr[modernizrFeatures[i]] ) {
        criticalFeaturesMissing.push( modernizrFeatures[i] );
      }
    }
    if ( !( Array.prototype &&
            Array.prototype.every &&
            Array.prototype.filter &&
            Array.prototype.forEach &&
            Array.prototype.indexOf &&
            Array.prototype.lastIndexOf &&
            Array.prototype.map &&
            Array.prototype.some &&
            Array.prototype.reduce &&
            Array.prototype.reduceRight &&
            Array.isArray ) ) {
      criticalFeaturesMissing.push( 'es5array' );
    }
    if ( !( Object.keys &&
            Object.create &&
            Object.getPrototypeOf &&
            Object.getOwnPropertyNames &&
            Object.isSealed &&
            Object.isFrozen &&
            Object.isExtensible &&
            Object.getOwnPropertyDescriptor &&
            Object.defineProperty &&
            Object.defineProperties &&
            Object.seal &&
            Object.freeze &&
            Object.preventExtensions ) ) {
      criticalFeaturesMissing.push( 'es5object' );
    }
    if ( criticalFeaturesMissing.length ) {
      document.getElementById( "html5-features-missing" ).style.display = 'block';
      window.criticalFeaturesMissing = criticalFeaturesMissing;
    }
  }
} );
