$( document ).ready( function() {
  $( '#login-button' ).click( function( e ) {
    $( '#login-form' ).lightbox_me( {
      centered: true,
      onLoad: function() {
        $( '#login-form' ).find( 'input:first' ).focus()
      }
    } );
    e.preventDefault();
  } );
} );