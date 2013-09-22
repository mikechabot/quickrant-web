/*jslint browser: true*/
/*global  $*/
$(document).ready(function () {
    "use strict";
    $('#countable').simplyCountable();   
    $("a.emotion").click(function () {
        var id = $(this).prop("id"),
            visible = $("div.questions:visible").prop("id");
        if ($('#rant').is(':visible')) {
            $('#rant').slideToggle(150);
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
        if ($('#rant').is(':visible')) {
            $('#legend').text($(this).text());
        } else {
            $('#legend').text($(this).text());
            $('#rant').slideToggle(150);
        }
        return false;
    });
    $("div#rant textarea").keydown(function() {
    	$('p#error-text').slideUp(300);
    });
});
function validate() {
	var validForm = true;
	    text = $.trim($("div#rant textarea").val());
    if (!text) {
    	$('p#error-text').text("Don't you want to say something?").slideDown(300);
        validForm = false;
    }
    $('input[name=emotion]').val($("div.questions:visible").prop("id"));
	$('input[name=context]').val($('#legend').text()); 
    
    if(validForm) $('#submit').attr('disabled', 'disabled');
    
    return validForm;
}