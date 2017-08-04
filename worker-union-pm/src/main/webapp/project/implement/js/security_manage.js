var PAGE_SIZE = 12;

/**
 * 页面启动调用
 */
$(function() {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
    // 显示项目名称
    showProject();
});

/**
 * 显示项目名称
 */
function showProject() {
    ajaxGetProjectDetailById(getUrlParam("project.id"), function(data) {
    	showProjectTitle(data.project);
        //显示安全管理信息
        showSecurityInfo();
    }, function(data) {
    }, true, waiting, run, true);
}

/**
 * 显示安全管理信息
 */
function showSecurityInfo() {
    // 分页参数
    var pagination = $("#pagination").html();
    // 清空页码
    $("#pagination").html("");
    $("#totalPage").html("");

    ajaxPageSafetyCheckItem(getUrlParam("project.id"), {
        "pagination": pagination,
        "size": PAGE_SIZE
    }, function(data) {
        // 设置 页码
        $("#pagination").html(data.page.pagination);
        $("#totalPage").html(data.page.totalPage);

        clearTableTr("#tableSecurity");
        for (var i = 0; i < PAGE_SIZE; i++) {
            if (i < data.page.list.length) {
                var j = i+1;
                setTableRowContent("#tableSecurity", i+1, data.page.list[i].id,
                    '<a href="#" onclick="btnGoToDetail(this)">'+ j +'</a>',
                    data.page.list[i].inspectDate.substring(0, 10),
                    data.page.list[i].staffName, RECTIFY_STATUS[data.page.list[i].rectifyStatus],
                    data.page.list[i].rectifyOpinion, data.page.list[i].evaluateDate.substring(0, 10));
            } else {
                setTableRowContent("#tableSecurity", i+1, "", "", "", "", "", "", "", "");
            }
        }
    }, function(data) {
    }, true, waiting, run, true);
}

/**
 *安全记录详情
 */
function btnGoToDetail(obj) {
    var attachmentId = $(obj).parent().parent().children("td").first().html();
    gotoLocalHtml('/project/implement/security_record_modify.html?project.id='
        + getUrlParam('project.id')+'&safetyCheckItem.id='+attachmentId);
}

/**
 * 点击上一页
 */
function btnPrevPage() {
    $("#pagination").html(Math.max(parseInt($("#pagination").html()) - 1, 1));
    showSecurityInfo();
}

/**
 * 点击下一页
 */
function btnNextPage() {
    $("#pagination").html(Math.max(parseInt($("#pagination").html()) + 1, 1));
    showSecurityInfo();
}
