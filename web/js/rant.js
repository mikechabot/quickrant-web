/*jslint browser: true*/
/*global  $*/
$(document).ready(function () {
    "use strict";
    $('#rantText').simplyCountable();   
    $("a.emotion").click(function () {
        var id = $(this).prop("id"),
            visible = $("div.questions:visible").prop("id");
        if ($('#rant').is(':visible')) {
            $('#rant').slideToggle(150);
            $("div#rant textarea").val("Say something...");
        }
        if (id !== visible) {
            $("div.questions:visible").slideToggle(150);
            $("div#" + id).slideToggle(150);
        } else {
            $("div#" + id).slideToggle(150);
        }
        return false;
    });
    $('input.optional, #rantText').focusin(function() {
        if ($.trim($(this).val()) === "Name (optional)" || $.trim($(this).val()) === "Location (optional)" || $.trim($(this).val()) === "Say something...") {
            this.value = '';
        }
    });
    $('input.optional').focusout(function() {
        if (($(this).prop('id') === 'ranter') && !$.trim($(this).val())) {
            $(this).val("Name (optional)");
        }
        if (($(this).prop('id') === 'location') && !$.trim($(this).val())) {
            $(this).val("Location (optional)");
        }
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
    $(document).scroll(function(e){
        var scrollAmount = $(window).scrollTop();
        var documentHeight = $(document).height();        

        var scrollPercent = (scrollAmount / documentHeight) * 100;
        if(scrollPercent > 50) {
        	//do something doSomething();
        }
     
        function doSomething() { 
           alert("doc height: " + documentHeight + "   %: " + scrollPercent);
        }
    });
});

function validate() {
	var validForm = true;
	    text = $.trim($("div#rant textarea").val());	    
    if (!text || text === "Say something...") {
    	 $('p#error-textarea').text("Don't you want to say something?").show("fast").fadeOut(5000);    	 
        validForm = false;
    }
    setEmotion();
    function setEmotion() {
        $('input[name=emotion]').val($("div.questions:visible").prop("id"));
    	$('input[name=context]').val($('#legend').text());
    }
    return validForm;
}