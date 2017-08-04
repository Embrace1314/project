var TITLE_LEN_MAX = 46;

/**
 * 将项目名称显示在标题中
 * @param project
 */
function showProjectTitle(project){
    $("#txtTitle").html(retainStringFixLenEng("("+ project.num+")" + project.name, TITLE_LEN_MAX-getLenEng($("#txtTitle").html())-1) + "-"+ $("#txtTitle").html());
}

/**
 * 退出系统
 */
function btnLogOut() {
    if (confirm("确定退出系统？")) {
        ajaxLogout(function(data) {
            // 跳转到登录页面
            gotoHtml(HTML_LOGIN);
        }, function(data) {
        }, true, waiting, run, true);
    }
}