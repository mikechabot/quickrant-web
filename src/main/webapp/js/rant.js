/*jslint browser: true*/
/*global  $*/
$(document).ready(function () {
  "use strict";
  // Rant textarea character counter
  $('#form-container-textarea').simplyCountable();

  // Permalink tooltip hover
  $(".permalink").tooltip({ title: "permalink", placement: "top", trigger: "hover"});

  // Set the page load time
  $('input[name=pageLoadTime]').val((new Date()).getTime());

  $('.scrollspy').jscroll({
	loadingHtml: '<img src="/img/ajax-loader.gif" alt="Loading" />',
	nextSelector: 'a.next-selector:last',
  });  
  
  // Controls display for questionnaire UI
  $("a.emotion").click(function () {
    $(".retract").each(function () {
      $(this).slideUp(150);
    });
    var id = $(this).prop("id"),
      visible = $("div.question-buttons:visible").prop("id");
    if (id !== visible) {
      $("div.question-buttons:visible").slideToggle(150);
      $("div#" + id).slideToggle(150);
    } else {
      $("div#" + id).slideToggle(150);
    }
    return false;
  });

  // Show form and alter legend
  $("button.question-button").click(function () {
    if (!$('#form-container').is(':visible')) { $('#form-container').slideToggle(150); }
    
    var panel = undefined;
    if ($(this).hasClass('btn-success')) {
    	panel = 'panel-success';
    } else if ($(this).hasClass('btn-danger')){
    	panel = 'panel-danger';
    } else if ($(this).hasClass('btn-primary')){
    	panel = 'panel-info';
    } else {
    	panel = 'panel-default';
    }    

    $('#form-container div.panel').removeClass().addClass('panel').addClass(panel);
    $('#legend').text($(this).text());
    return false;
  });

  // Hide error text on input
  $("div#form-container textarea").keydown(function () {
    $('p#error-text').slideUp(300);
  });

  if ($.cookie("quickrant-uuid").indexOf("*") === -1) {
    $.ajax({
      context: this,
      url: "/ajax/phonehome",
      type: "POST",
      data: { "ajax": true, "screen_height": window.screen.availHeight, "screen_width": window.screen.availWidth, "screen_color": window.screen.colorDepth }
    });
  }
});

function validate() {
  var validForm = true,
    text = $.trim($("div#form-container textarea").val()),
    pageLoaded = $("#pageLoadTime").val(),
    now = (new Date()).getTime(),
    limitInSeconds =  7;

  if ((now - pageLoaded) > limitInSeconds * 1000) {
    if (!text) {
      $('p#error-text').text("Don't you want to say something?").slideDown(300);
      validForm = false;
    } else {
      $('input[name=emotion]').val($("div.question-buttons:visible").prop("id"));
      $('input[name=question]').val($('#legend').text());
    }
  } else {
    $('p#error-text').text("Slow down, you can't post that quick.").slideDown(300);
    validForm = false;
  }
  if (validForm) { $('#submit').attr('disabled', 'disabled'); }
  return validForm;
}