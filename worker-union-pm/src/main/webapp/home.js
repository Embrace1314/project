
/*每页展示数量*/
var PAGE_SIZE = 12;
var PROJECT_NAME_LEN_MAX = 36;

/*
 * 页面初始调用
 * */
$(function () {
    $("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
    switch (getUrlParam("status")) {
        case PROJECT_TYPE_GOING:
            $("#status").val(PROJECT_TYPE_GOING);
            break;
        case PROJECT_TYPE_ARCHIVED:
            $("#status").val(PROJECT_TYPE_ARCHIVED);
            break;
        case PROJECT_TYPE_ALL:
            $("#status").val(PROJECT_TYPE_ALL);
            break;
        default:
            //默认显示进行中项目
            $("#status").val(PROJECT_TYPE_GOING);
            break;
    }
    getPageProjects();
});

function getPageProjects() {
    var year = $("#selectYear option:selected").val();
    var status = $("#status").val();
    var pagination = $("#pagination").html();

    resetButtons(status);

    if (isEmpty(status) && isEmpty(year)) {
        //获取所有项目
        ajaxGetPageAllProjects({
            "pagination": pagination,
            "size": PAGE_SIZE
        }, success, function () {
        }, true, waiting, run, true);
    } else if (!isEmpty(status) && isEmpty(year)){
        //根据状态获取
        ajaxGetPageProjectsByStatus(status,{
            "pagination": pagination,
            "size": PAGE_SIZE
        },success,function () {
        },true,waiting,run,true);
    } else if(isEmpty(status) && !isEmpty(year)){
        //根据年份获取
        ajaxGetPageProjectsByYear(year + "-01-01",{
            "pagination": pagination,
            "size": PAGE_SIZE
        },success,function () {
        },true,waiting,run,true);
    }else if(!isEmpty(status) && !isEmpty(year)){
        //根据状态和年份获取
        ajaxGetPageProjectsByStatusAndYear(status,year+"-01-01",{
            "pagination": pagination,
            "size": PAGE_SIZE
        },success,function () {
        },true,waiting,run,true);
    }

}

/*
 * 成功获取分页项目列表信息
 * */
function success(data) {
    $("#projectList").empty();
    for(var i=0;i<data.page.list.length;i++){
        var projectName = retainStringFixLenEng(data.page.list[i].name, PROJECT_NAME_LEN_MAX);
        $("#projectList")
            .append(
                "<button class='btn_project' onclick='btnProjectItem("
                + data.page.list[i].id
                + ")'>"
                + projectName
                + "</button>");

    }
    //设置页码
    $("#pagination").html(data.page.pagination);
    $("#totalPage").html(data.page.totalPage);
}

/*
* 项目按钮
* 项目登录校验成功跳转
* */
function btnProjectItem(projectId) {
    ajaxProjectLogin(projectId, function(data) {
        //跳转到项目中标信息页面
        gotoLocalHtml("/project/establish/bid_info.html?project.id=" + projectId);
    }, function(data) {
    }, true, waiting, run, true);
}

/*
 * 重置项目状态按钮效果
 * */
function resetButtons(status) {
    //清空项目列表及页码
    $("#projectList").empty();
    $("#pagination").html("");
    $("#totalPage").html("");

    $("#btnGoingProject").attr("class","btn_ongoing_project");
    $("#btnArchivedProject").attr("class","btn_archived_project");
    $("#btnAllProject").attr("class","btn_all_project");
    switch(status){
        case PROJECT_TYPE_GOING:
            $("#btnGoingProject").attr("class","btn_ongoing_project_selected");
            break;
        case PROJECT_TYPE_ARCHIVED:
            $("#btnArchivedProject").attr("class","btn_archived_project_selected");
            break;
        case PROJECT_TYPE_ALL:
            $("#btnAllProject").attr("class","btn_all_project_selected");
            break;
        default:
            $("#btnAllProject").attr("class", "btn_all_project_selected");
            break;
    }
}

/**
 * 点击按钮-所有项目
 */
function btnAllProject() {
    $("#status").val(PROJECT_TYPE_ALL);
    getPageProjects();
}

/**
 * 点击按钮-已归档项目
 */
function btnArchivedProject() {
    $("#status").val(PROJECT_TYPE_ARCHIVED);
    getPageProjects();
}

/**
 * 点击按钮-进行中项目
 */
function btnGoingProject() {
    $("#status").val(PROJECT_TYPE_GOING);
    getPageProjects();
}

/**
 * 点击下一页
 */
function nextPage() {
    $("#pagination").html(Math.max($("#pagination").html() + 1, 1));
    getPageProjects();
}

/**
 * 点击上一页
 */
function prePage() {
    $("#pagination").html(Math.max($("#pagination").html() - 1, 1));
    getPageProjects();
}

/**
 * 修改密码
 * 显示修改密码弹窗
 */
function btnToModifyPasswordDialog() {
    $("#dialog").show();
}

/**
 * 保存密码
 */
function btnSavePassword() {
    var original = $("#txtOriginal").val();
    var newCode = $("#txtNew").val();
    var confirm = $("#txtConfirm").val();
    if (!verifyNotEmpty(original, "原始密码",true)){
        return;
    }
    if (!verifyNotEmpty(newCode, "新密码",true)){
        return;
    }
    if (!verifyNotEmpty(confirm, "确认新密码",true)){
        return;
    }
    ajaxChangePassword(original, newCode, confirm, function(data){
        closeDialog();
        // 重新登录
        gotoHtml(HTML_LOGIN);
    }, function(data){}, true, waiting, run, true);
}

/**
 * 关闭弹窗
 */
function closeDialog() {
    $("#txtOriginal").val("");
    $("#txtNew").val("");
    $("#txtConfirm").val("");
    $("#dialog").hide();
}















