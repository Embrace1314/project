/**
 * 项目、职能单选框
 */
$("input[type='radio']").click(function(){
    $("input[type='radio'][name='"+$(this).attr('name')+"']").parent().removeClass("checked");
    $(this).parent().addClass("checked");

    //判断若是职能主线，跳转至职能登录页面
    var channel = $('input:radio:checked').val();
    if(channel=='FM'){
        gotoHtml(FM_LOGIN);
    }
});

/**
 * 登录按钮响应
 */
function btnlogin() {
    var userName = $("#userName").val().trim();
    var password = $("#password").val();
    if (isEmpty(userName)) {
        alert("用户名不能为空");
        return;
    }
    if (isEmpty(password)) {
        alert("密码不能为空");
        return;
    }

    ajaxLogin(userName, password, function (data) {
    	$.cookie(COOKIE_STAFF_NAME_KEY, data.name, {
			expires : 7,
			path : '/'
		});
            //跳转到主页
            gotoHtml(HTML_HOME)
        }, function (data) {
            $("#password").val("")
        }, true, waiting, run,
        true)
}

/**
 * 用户按键响应
 * 按Enter键登录
 */
document.onkeydown = function (e) {
    if ((e || event).keyCode == 13)
        btnlogin();
};

