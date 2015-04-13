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
    td = document.createElement( 'td' );
    td.id = thumbnailId;

    var container = document.createElement( 'span' );
    container.className = 'sim-thumbnail';

    var image = document.createElement( 'img' );
    image.src = thumbnailUrl;

    var badge = document.createElement( 'span' );
    badge.className = 'sim-display-badge sim-badge-' + simType;

    container.appendChild( image );
    container.appendChild( badge );
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

  // if the checkbox has been unchecked, delete the thumbnail and the containing table row if it
  // is empty
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
  }

  return true;
};