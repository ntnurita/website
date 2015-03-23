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

  if ( window.location.search.indexOf( 'login-failed' ) > -1 ) {
    $( '#login-form' ).lightbox_me( {
      centered: true,
      onLoad: function() {
        $( '#login-form' ).find( 'input:first' ).focus()
      }
    } );

    $( '#login-validation-message' ).css( 'display', 'inherit' );
  }
} );