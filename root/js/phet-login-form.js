var phet = phet || {};

$( document ).ready( function() {
  var openLoginDialog = function() {
    $( '#login-form' ).lightbox_me( {
      centered: true,
      onLoad: function() {
        $( '#login-form' ).find( 'input:first' ).focus()
      }
    } );
  };

  phet.ensureLogin = function( url ) {
    $.getJSON( "/services/check-login", function( data ) {
      console.log( url );
      if ( data['loggedIn'] === false ) {
        openLoginDialog();
        $( '#destinationURL' ).val( url );
      }
      else {
        window.location.href = url;
      }
    } );
  };

  $( '#login-button' ).click( function( e ) {
    $( '#sourceURL' ).val( window.location.pathname );
    openLoginDialog();
    e.preventDefault();
  } );

  if ( window.location.search.indexOf( 'login-failed' ) > -1 ) {
    openLoginDialog();
    $( '#login-validation-message' ).css( 'display', 'inherit' );
  }
} );