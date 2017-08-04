/**
 * 项目、职能单选框
 */
$("input[type='radio']").click(function(){
	$("input[type='radio'][name='"+$(this).attr('name')+"']").parent().removeClass("checked");
	$(this).parent().addClass("checked");

	//判断若是项目主线，跳转至项目登录页面
	var channel = $('input:radio:checked').val();
	if(channel=='PM'){
		gotoHtml(PM_LOGIN);
	}
});


/**
 * 登录按钮响应
 */
function btnlogin() {
	// 参数校验
	var userName = $("#userName").val().trim();
	var password = $("#password").val();
	if (isEmpty(userName)) {
		alert("工号不能为空！");
		return;
	}
	if (isEmpty(password)) {
		alert("密码不能为空！");
		return;
	}
	// 发起AJAX请求
	ajaxLogin(userName, password, function(data) {
		$.cookie(COOKIE_STAFF_NAME_KEY, data.name, {
			expires : 7,
			path : '/'
		});
		// 跳转到主页
		gotoHtml(HTML_HOME);
	}, function(data) {
		$("#password").val("")
	}, true, waiting, run, true);
}

/**
 * 用户按键响应
 */
document.onkeydown = function(e) {
	if ((e || event).keyCode == 13)
		btnlogin();
};
