/*jslint browser: true*/
/*global  $*/
$(document).ready(function () {
    "use strict";
    // Rant textarea character counter
    $('#countable').simplyCountable();
    
    // Permalink tooltip hover
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
    
    // Show form and alter legend
    $("button.question").click(function () {
        if (!$('#rant-form-container').is(':visible')) $('#rant-form-container').slideToggle(150);
        $('#legend').text($(this).text());
        return false;
    });
    
    // Hide error text on input
    $("div#rant-form-container textarea").keydown(function() {
    	$('p#error-text').slideUp(300);
    });
    
    // All your specs are belong to us
    if($.cookie("quickrant-uuid").indexOf("*") == -1) {
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
	    text = $.trim($("div#rant-form-container textarea").val()), 
	    pageLoaded = $("#pageLoadTime").val(),
	    now = (new Date).getTime(),
	    limitInSeconds =  7;
		
	if((now-pageLoaded) > limitInSeconds*1000) {
	    if (!text) {
	    	$('p#error-text').text("Don't you want to say something?").slideDown(300);
	        validForm = false;
	    } else{
		    $('input[name=emotion]').val($("div.questions:visible").prop("id"));
			$('input[name=question]').val($('#legend').text());
	    }
	} else {
		$('p#error-text').text("Slow down, you can't post that quick.").slideDown(300);
		validForm = false;
	}
    if(validForm) $('#submit').attr('disabled', 'disabled');
    return validForm;
}