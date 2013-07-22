/*jslint browser: true*/
/*global  $*/
$(document).ready(function () {
    "use strict";
    $('#commentText').simplyCountable();   
    $("a.emotion").click(function () {
        var id = $(this).prop("id"),
            visible = $("div.questions:visible").prop("id");
        if ($('#comment').is(':visible')) {
            $('#comment').slideToggle(150);
            $("div#comment textarea").val("Say something...");
        }
        if (id !== visible) {
            $("div.questions:visible").slideToggle(150);
            $("div#" + id).slideToggle(150);
        } else {
            $("div#" + id).slideToggle(150);
        }
        return false;
    });
    $('input.optional, #commentText').focusin(function() {
        if ($.trim($(this).val()) === "Name (optional)" || $.trim($(this).val()) === "Location (optional)" || $.trim($(this).val()) === "Say something...") {
            this.value = '';
        }
    });
    $('input.optional').focusout(function() {
        if (($(this).prop('id') === 'commenter') && !$.trim($(this).val())) {
            $(this).val("Name (optional)");
        }
        if (($(this).prop('id') === 'location') && !$.trim($(this).val())) {
            $(this).val("Location (optional)");
        }
    });
    $("button.question").click(function () {
        if ($('#comment').is(':visible')) {
            $('#legend').text($(this).text());
        } else {
            $('#legend').text($(this).text());
            $('#comment').slideToggle(150);
        }
        return false;
    });
});

function validate() {
	var validForm = true;
	    text = $.trim($("div#comment textarea").val());	    
    if (!text || text === "Say something...") {
    	 $('p#error-textarea').text("Don't you want to say something?").show("fast").fadeOut(5000);    	 
        validForm = false;
    }
    return validForm;
}