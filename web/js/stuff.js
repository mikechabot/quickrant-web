/*jslint browser: true*/
/*global  $*/
$(document).ready(function () {
  $("a.emotion").click(function () {
    var id = $(this).prop("id");
    var visible = $("div.questions:visible").prop("id");
    if ($('#texting').is(':visible')) {
    	 $('#texting').slideToggle(150);
    }
    if (id !== visible) {
      $("div.questions:visible").slideToggle(150);
      $("div#" + id).slideToggle(150);
    } else {
      $("div#" + id).slideToggle(150);
    }
    return false;
  });
  $("button.question").click(function () {
    if ($('#texting').is(':visible')) {
      $('#texting textarea').val($(this).text());
    } else {
      $('#texting textarea').val($(this).text());
      $('#texting').slideToggle(150);
    }
    return false;
  });
});