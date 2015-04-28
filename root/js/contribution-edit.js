var phet = phet || {};

phet.checkboxToggle = function( simName, checkbox, thumbnailUrl, simType ) {
  var thumbnailId = 'sim-thumbnail-' + simName + '-' + simType;
  var thumbnailTable = document.getElementById( 'sim-thumbnails' );
  var container = document.getElementById( thumbnailId );

  // if the thumbnail doesn't exist already and the checkbox has just been checked,
  // create a new div to hold the thumbnail image and sim badge
  if ( !container && checkbox.checked ) {

    container = document.createElement( 'div' );
    container.id = thumbnailId;
    container.className = 'sim-thumbnail';

    var image = document.createElement( 'img' );
    image.src = thumbnailUrl;

    var badge = document.createElement( 'div' );
    badge.className = 'sim-thumbnail-badge sim-badge-' + simType;

    var x = document.createElement( 'div' );
    x.className = 'sim-thumbnail-x';
    x.onclick = function() { phet.checkboxToggle( simName, checkbox, thumbnailUrl, simType ) };

    container.appendChild( image );
    container.appendChild( badge );
    container.appendChild( x );
    thumbnailTable.appendChild( container );
  }

  // if the checkbox has been unchecked, delete the thumbnail and the containing table row if it is empty
  else if ( container ) {
    thumbnailTable.removeChild( container );

    if ( checkbox.checked ) {
      checkbox.checked = false;
    }
  }

  return true;
};

var fileAdded = function() {
  console.log( 'called' );
  var files = $( '.wicket-mfu-container' )[ 0 ].childNodes;
  var i;
  console.log( files );

  for ( i = 0; i < files.length; i++ ) {
    var file = files[ i ];
    if ( file.nodeType === 3 ) {
      continue;
    }

    console.log( file );
    file.className = 'file-div';

    var input = file.childNodes[ 1 ];
    console.log( input );
    input.value = '';

    var text = file.childNodes[ 0 ].textContent;
    file.childNodes[ 0 ].textContent = text.replace( 'C:\\fakepath\\', '' );
  }

  var inputs = $( 'input[type=file]' );
  for ( i = 0; i < inputs.length; i++ ) {
    inputs[ i ].style.position = 'absolute';
    inputs[ i ].style.left = '-1000px';
    inputs[ i ].removeEventListener( 'change', fileAdded, false );
    inputs[ i ].addEventListener( 'change', fileAdded, false );
  }
};

$( 'document' ).ready( function() {
  var fileInput = $( '.wicket-mfu-field' )[ 0 ];
  if ( fileInput ) {
    $( '#fake-file' ).click( function() {
      $( 'input[type=file]' )[ 0 ].click();
    } );

    fileInput.style.position = 'absolute';
    fileInput.style.left = '-1000px';
    fileInput.addEventListener( 'click', fileAdded, false );
  }

  var simCheckboxes = $( '.sim-checkbox-div input' );
  if ( simCheckboxes.length ) {
    simCheckboxes.each( function( index, checkbox ) {
      if ( checkbox.getAttribute( 'checked' ) ) {
        checkbox.onclick();
      }
    } );
  }
} );
