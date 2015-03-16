function selectItem( li ) {
    // temporary measure to make non english searches default to legacy pages
    if ( phetLocale !== "en" && li.data[1].indexOf('simulation') > -1 ) {
        window.location = li.data[1].replace( 'simulation', 'simulation/legacy' );
    }
    else {
        window.location = li.data[1];
    }
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

// if there is no phetLocale, we won't run this part. there must not be a search box, like in the offline installer
if ( typeof phetLocale != 'undefined' ) {
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
}