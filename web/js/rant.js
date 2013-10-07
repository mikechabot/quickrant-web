/*jslint browser: true*/
/*global  $*/
$(document).ready(function () {
    "use strict";
    // Rant textarea character counter
    $('#countable').simplyCountable();
    
    $(".permalink").tooltip({ title: "permalink", placement: "top", trigger: "hover"});
    
    // Controls display for questionnaire UI
    $("a.emoticon").click(function () {
    	$(".retract").each(function() {    		
    		$(this).slideUp(150);
    	});
        var id = $(this).prop("id"),
            visible = $("div.questions:visible").prop("id");
        if (id !== visible) {
            $("div.questions:visible").slideToggle(150);
            $("div#" + id).slideToggle(150);
        } else {
            $("div#" + id).slideToggle(150);
        }
        return false;
    });
    $("button.question").click(function () {
        if ($('#rant-form-container').is(':visible')) {
            $('#legend').text($(this).text());
        } else {
            $('#legend').text($(this).text());
            $('#rant-form-container').slideToggle(150);
        }
        return false;
    });
    $("div#rant-form-container textarea").keydown(function() {
    	$('p#error-text').slideUp(300);
    });
    var height = window.screen.availHeight;
    var width = window.screen.availWidth;
    var color = window.screen.colorDepth;
    if($.cookie("quickrant-uid").indexOf("COMPLETE") == -1) {
	    $.ajax({
	        context: this,
	        url: "/rant/ajax",
	        type: "POST",
	        data: { "ajax": true, "height": height, "width": width, "color": color }
	    });
    }
});
function validate() {
	var validForm = true;
	    text = $.trim($("div#rant-form-container textarea").val());
    if (!text) {
    	$('p#error-text').text("Don't you want to say something?").slideDown(300);
        validForm = false;
    }
    $('input[name=emotion]').val($("div.questions:visible").prop("id"));
	$('input[name=question]').val($('#legend').text()); 
    
    if(validForm) $('#submit').attr('disabled', 'disabled');
    
    return validForm;
}