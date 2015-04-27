// Copyright 2002-2015, University of Colorado

(function() {
  function initializeCheckboxListeners() {
    var elementaryCheckbox = $( '#elementary-checkbox' );
    var gradeKCheckbox = $( '#grade-k-checkbox' );
    var grade1Checkbox = $( '#grade1-checkbox' );
    var grade2Checkbox = $( '#grade2-checkbox' );
    var grade3Checkbox = $( '#grade3-checkbox' );
    var grade4Checkbox = $( '#grade4-checkbox' );
    var grade5Checkbox = $( '#grade5-checkbox' );

    var middleCheckbox = $( '#middle-checkbox' );
    var grade6Checkbox = $( '#grade6-checkbox' );
    var grade7Checkbox = $( '#grade7-checkbox' );
    var grade8Checkbox = $( '#grade8-checkbox' );

    var highCheckbox = $( '#high-checkbox' );
    var grade9Checkbox = $( '#grade9-checkbox' );
    var grade10Checkbox = $( '#grade10-checkbox' );
    var grade11Checkbox = $( '#grade11-checkbox' );
    var grade12Checkbox = $( '#grade12-checkbox' );

    var universityCheckbox = $( '#university-checkbox' );
    var year1Checkbox = $( '#year1-checkbox' );
    var year2PlusCheckbox = $( '#year2plus-checkbox' );
    var graduateCheckbox = $( '#graduate-checkbox' );

    elementaryCheckbox.change( function() {
      if ( elementaryCheckbox.attr( 'checked' ) ) {
        gradeKCheckbox.attr( 'checked', true );
        grade1Checkbox.attr( 'checked', true );
        grade2Checkbox.attr( 'checked', true );
        grade3Checkbox.attr( 'checked', true );
        grade4Checkbox.attr( 'checked', true );
        grade5Checkbox.attr( 'checked', true );
      }
    } );

    middleCheckbox.change( function() {
      if ( middleCheckbox.attr( 'checked' ) ) {
        grade6Checkbox.attr( 'checked', true );
        grade7Checkbox.attr( 'checked', true );
        grade8Checkbox.attr( 'checked', true );
      }
    } );

    highCheckbox.change( function() {
      if ( highCheckbox.attr( 'checked' ) ) {
        grade9Checkbox.attr( 'checked', true );
        grade10Checkbox.attr( 'checked', true );
        grade11Checkbox.attr( 'checked', true );
        grade12Checkbox.attr( 'checked', true );
      }
    } );

    universityCheckbox.change( function() {
      if ( universityCheckbox.attr( 'checked' ) ) {
        year1Checkbox.attr( 'checked', true );
        year2PlusCheckbox.attr( 'checked', true );
        graduateCheckbox.attr( 'checked', true );
      }
    } );

    gradeKCheckbox.change( function() {
      if ( gradeKCheckbox.attr( 'checked' ) ) {
        elementaryCheckbox.attr( 'checked', true );
      }
    } );

    grade1Checkbox.change( function() {
      if ( grade1Checkbox.attr( 'checked' ) ) {
        elementaryCheckbox.attr( 'checked', true );
      }
    } );

    grade2Checkbox.change( function() {
      if ( grade2Checkbox.attr( 'checked' ) ) {
        elementaryCheckbox.attr( 'checked', true );
      }
    } );

    grade3Checkbox.change( function() {
      if ( grade3Checkbox.attr( 'checked' ) ) {
        elementaryCheckbox.attr( 'checked', true );
      }
    } );

    grade4Checkbox.change( function() {
      if ( grade4Checkbox.attr( 'checked' ) ) {
        elementaryCheckbox.attr( 'checked', true );
      }
    } );

    grade5Checkbox.change( function() {
      if ( grade5Checkbox.attr( 'checked' ) ) {
        elementaryCheckbox.attr( 'checked', true );
      }
    } );

    grade6Checkbox.change( function() {
      if ( grade6Checkbox.attr( 'checked' ) ) {
        middleCheckbox.attr( 'checked', true );
      }
    } );

    grade7Checkbox.change( function() {
      if ( grade7Checkbox.attr( 'checked' ) ) {
        middleCheckbox.attr( 'checked', true );
      }
    } );

    grade8Checkbox.change( function() {
      if ( grade8Checkbox.attr( 'checked' ) ) {
        middleCheckbox.attr( 'checked', true );
      }
    } );

    grade9Checkbox.change( function() {
      if ( grade9Checkbox.attr( 'checked' ) ) {
        highCheckbox.attr( 'checked', true );
      }
    } );

    grade10Checkbox.change( function() {
      if ( grade10Checkbox.attr( 'checked' ) ) {
        highCheckbox.attr( 'checked', true );
      }
    } );

    grade11Checkbox.change( function() {
      if ( grade11Checkbox.attr( 'checked' ) ) {
        highCheckbox.attr( 'checked', true );
      }
    } );

    grade12Checkbox.change( function() {
      if ( grade12Checkbox.attr( 'checked' ) ) {
        highCheckbox.attr( 'checked', true );
      }
    } );

    year1Checkbox.change( function() {
      if ( year1Checkbox.attr( 'checked' ) ) {
        universityCheckbox.attr( 'checked', true );
      }
    } );

    year2PlusCheckbox.change( function() {
      if ( year2PlusCheckbox.attr( 'checked' ) ) {
        universityCheckbox.attr( 'checked', true );
      }
    } );

    graduateCheckbox.change( function() {
      if ( graduateCheckbox.attr( 'checked' ) ) {
        universityCheckbox.attr( 'checked', true );
      }
    } );
  }

  var interval = setInterval( function() {
    if ( typeof $ !== 'undefined' ) {
      $( document ).ready( function() {
        initializeCheckboxListeners();
      } );
      clearInterval( interval );
    }
  }, 100 );
})();