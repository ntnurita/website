var phet = phet || {};

phet.checkboxToggle = function( simName, checkboxDiv, thumbnailUrl ) {
  var imageId = 'sim-thumbnail-' + simName;
  var checkbox = checkboxDiv.children[0];
  var thumbnailDiv = document.getElementById( 'sim-thumbnails' );
  var image = document.getElementById( imageId );

  if ( !image && checkbox.checked ) {
    image = document.createElement( 'img' );
    image.src = thumbnailUrl;
    image.className = 'sim-thumbnail';
    image.id = imageId;
    thumbnailDiv.appendChild( image );
  }
  else if ( image ) {
    thumbnailDiv.removeChild( image );
  }
  return true;
};
