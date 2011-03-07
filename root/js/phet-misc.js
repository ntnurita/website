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
} );
