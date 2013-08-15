// this causes all "external" rel-tagged links to open in a new tab / window
$( document ).ready( function() {
    $( 'a[rel*=external]' ).each( function() {
        $( this ).attr( 'target', '_blank' );
    } );
    $( 'a[class*=external]' ).each( function() {
        $( this ).attr( 'target', '_blank' );
    } );
    $( '.autocompleteOff' ).each( function() {
        $( this ).attr( 'autocomplete', 'off' );
    } );
  if( !Modernizr.svg ) {
//  if( Modernizr.svg && window.devicePixelRatio > 1 ) {
    // replace the main logo with the SVG version if we are using a high resolution, and have SVG support
//    $('img[src*="phet-logo-117.png"]').attr('src', function() {
//      return $(this).attr('src').replace('.png', '.svg');
//    });

    $('img[src*="svg"]').attr('src', function() {
      return $(this).attr('src').replace('.svg', '.png');
    });
  }
} );
