var phet = phet || {};

phet.checkboxToggle = function( simName, checkboxDiv, thumbnailUrl, simType ) {
  var thumbnailId = 'sim-thumbnail-' + simName + '-' + simType;
  var checkbox = checkboxDiv.children[ 0 ];
  var thumbnailTable = document.getElementById( 'sim-thumbnails' );
  var td = document.getElementById( thumbnailId );

  var currentRow;

  // if the thumbnail doesn't exist already and the checkbox has just been checked,
  // create a new td (and a new tr if necessary) to hold the thumbnail image and sim badge
  if ( !td && checkbox.checked ) {
    //td = document.createElement( 'td' );
    //td.id = thumbnailId;

    var container = document.createElement( 'div' );
    container.className = 'sim-thumbnail';

    var image = document.createElement( 'img' );
    image.src = thumbnailUrl;

    var badge = document.createElement( 'div' );
    badge.className = 'sim-thumbnail-badge sim-badge-' + simType;

    var x = document.createElement( 'div' );
    x.className = 'sim-thumbnail-x';
    x.onclick = function() { phet.checkboxToggle( simName, checkboxDiv, thumbnailUrl, simType ) };

    container.appendChild( image );
    container.appendChild( badge );
    container.appendChild( x );
    td.appendChild( container );

    if ( thumbnailTable.children.length ) {
      currentRow = thumbnailTable.children[ thumbnailTable.children.length - 1 ];
    }
    else {
      currentRow = document.createElement( 'tr' );
      thumbnailTable.appendChild( currentRow );
    }

    if ( currentRow.children.length >= 4 ) {
      var newTr = document.createElement( 'tr' );
      thumbnailTable.appendChild( newTr );
      newTr.appendChild( td );
    }
    else {
      currentRow.appendChild( td );
    }
  }

  // if the checkbox has been unchecked, delete the thumbnail and the containing table row if it is empty
  else if ( td ) {
    currentRow = td.parentNode;
    currentRow.removeChild( td );
    if ( currentRow.children.length === 0 ) {
      thumbnailTable.removeChild( currentRow );
    }
    else {
      var lastRow = thumbnailTable.children[ thumbnailTable.children.length - 1 ];
      var lastTd = lastRow.children[ lastRow.children.length - 1 ];
      lastRow.removeChild( lastTd );
      currentRow.appendChild( lastTd );
    }

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

phet.markUnvalidatedComponents = function() {
  var borderStyle = '2px solid red';
  var markTableColumn = function ( index ) {
    $( $( '#cep-checkbox-table th' )[ index ] ).css( {
      'border-top': borderStyle,
      'border-left': borderStyle,
      'border-right': borderStyle
    } );
    $( $( '#cep-checkbox-table td' )[ index ] ).css( {
      'border-bottom': borderStyle,
      'border-left': borderStyle,
      'border-right': borderStyle
    } );
  };

  $( 'span.feedbackPanelERROR' ).each( function( index, item ) {
    if ( item.textContent.indexOf( 'title' ) > -1 ) {
      $( '#cep-title' ).addClass( 'validation-error' );
    }
    if ( item.textContent.indexOf( 'keywords' ) > -1 ) {
      $( '#cep-keywords' ).addClass( 'validation-error' );
    }
    if ( item.textContent.indexOf( 'file' ) > -1 ) {
      $( '#cep-upload' ).addClass( 'validation-error' );
    }
    if ( item.textContent.indexOf( 'simulation' ) > -1 ) {
      $( '#simulations' ).addClass( 'validation-error' );
    }
    if ( item.textContent.indexOf( 'type' ) > -1 ) {
      markTableColumn( 0 );
    }
    if ( item.textContent.indexOf( 'level' ) > -1 ) {
      markTableColumn( 1 );
    }
    if ( item.textContent.indexOf( 'subject' ) > -1 ) {
      markTableColumn( 2 );
    }
  } );
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
        // this is a hack to get the thumbnail images to trigger for pre-checked boxes
        // the thumbnails appear onclick, so they won't appear when the page first loads
        checkbox.parentNode.onclick();
        checkbox.parentNode.onclick();
        checkbox.parentNode.onclick();
      }
    } );
  }

  if ( $('#contribution-feedback').length ) {
    phet.markUnvalidatedComponents();
  }
} );
