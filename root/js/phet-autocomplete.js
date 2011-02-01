function selectItem( li ) {
    window.location = li.data[1];
}
function htmlEncode( value ) {
    return $( '<div/>' ).text( value ).html();
}
function render( value, data ) {
    if ( data[0] == 'sim' ) {
        return " <span style=\"color: #666;\">" + phetSimulationString.replace( /\{0\}/g, "<span style='color: #000;'>" + htmlEncode( value ) + "</span>" ) + "</span>";
    }
    return htmlEncode( value );
}

$( document ).ready( function() {
    $( "#search-text-id" ).autocomplete( {
                                             url:"/autocomplete",
                                             extraParams:{
                                                 l:phetLocale
                                             },
                                             delay:250,
                                             onItemSelect:selectItem,
                                             showResult:render
                                         } );
} );
