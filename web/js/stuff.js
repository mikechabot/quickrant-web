/*jslint browser: true*/
/*global  $*/
$(document).ready(function () {
  $("a.emotion").click(function () {
    var id = $(this).prop("id");
    var visible = $("div.questions:visible").prop("id");
    if ($('#texting').is(':visible')) {
    	 $('#texting').slideToggle(250);
    }
    if (id !== visible) {
      $("div.questions:visible").slideToggle(250);
      $("div#" + id).slideToggle(250);
    } else {
      $("div#" + id).slideToggle(250);
    }
    return false;
  });
  $("button.question").click(function () {
    if ($('#texting').is(':visible')) {
      $('#texting label').text($(this).text());
    } else {
      $('#texting label').text($(this).text());
      $('#texting').slideToggle(500);
    }
    return false;
  });
});