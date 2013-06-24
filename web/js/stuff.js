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
        }
        if (id !== visible) {
            $("div.questions:visible").slideToggle(150);
            $("div#" + id).slideToggle(150);
        } else {
            $("div#" + id).slideToggle(150);
        }
        return false;
    });
    $('input.optional').focusin(function() {
        if ($.trim($(this).val()) === "Name (optional)" || $.trim($(this).val()) === "Location (optional)") {
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
    if (!$.trim($("div#comment textarea").val())) {
        alert("cheers");
    }
}