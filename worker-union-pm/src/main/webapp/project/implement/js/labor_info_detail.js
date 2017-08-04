$(function() {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
    $('label').click(function(){
        var radioId = $(this).attr('name');
        $('label').removeAttr('class') && $(this).attr('class', 'checked');
        $('input[type="radio"]').removeAttr('checked') && $('#' + radioId).attr('checked', 'checked');
    });
});

