var PAGE_SIZE = 12;

/**
 * 页面启动加载
 */
$(function() {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
    //初始化入职起止时间
    initdatepicker_cn();
    $("#txtStartDate").datepicker({
        dateFormat : "yy-mm-dd"
    });
    $("#txtEndDate").datepicker({
        dateFormat : "yy-mm-dd"
    });
});

/**
 * 查询员工
 */
function btnSearch() {
    ajaxPageStaffsByFuzzy(new Page(PAGE_SIZE, 1), $("#txtName").val(),
        $("#txtJobNo").val(), $("#txtIdCardNo").val(), $("#txtRoleName").val(),
        $("#txtProjectName").val(), $("#txtStartDate").val(), $(
            "#txtEndDate").val(),
        function(data) {
            showSearchResult(data);
        }, function(data) {
        }, true, waiting, run, true);
}

/**
 * 显示搜索结果
 *
 * @param data
 */
function showSearchResult(data) {
    clearTableTr("#tableStaffs");
    for (var i = 0; i < PAGE_SIZE; i++) {
        if (i < data.page.list.length) {
            setTableRowContent("#tableStaffs", i+1, data.page.list[i].id,
                data.page.list[i].jobNo, data.page.list[i].name,
                data.page.list[i].idCardNo, data.page.list[i].phone,
                data.page.list[i].roleName, data.page.list[i].entryDate.substring(0, 10),
                data.page.list[i].projectName);
            setRowOnClickSelected("#tableStaffs", i+1, true);
        } else {
            setTableRowContent("#tableStaffs", i+1, "", "", "", "", "", "", "",
                "");
            setRowOnClickSelected("#tableStaffs", i+1, false);
        }
    }

    // 设置 页码
    $("#txtPagination").html(data.page.pagination);
    $("#txtTotalPage").html(data.page.totalPage);
}

/**
 * 上一页
 */
function btnPrevPage() {
    ajaxPageStaffsByFuzzy(new Page(PAGE_SIZE, Math.max(parseInt($("#txtPagination").html()) - 1, 1)), $("#txtName").val(),
        $("#txtJobNo").val(), $("#txtIdCardNo").val(), $("#txtRoleName").val(),
        $("#txtProjectName").val(), $("#txtStartDate").val(), $("#txtEndDate").val(),
        function(data) {
            showSearchResult(data);
        }, function(data) {
        }, true, waiting, run, true);
}

/**
 * 下一页
 */
function btnNextPage() {
    ajaxPageStaffsByFuzzy($(new Page(PAGE_SIZE, parseInt($("#txtPagination").html()) + 1,1), "#txtName").val(),
        $("#txtJobNo").val(), $("#txtIdCardNo").val(), $("#txtRoleName").val(),
        $("#txtProjectName").val(), $("#txtStartDate").val(), $("#txtEndDate").val(),
        function(data) {
            showSearchResult(data);
        }, function(data) {
        }, true, waiting, run, true);
}

/**
 * 确定提交选中人员
 */
function btnConfirm() {
    if (!isEmpty(getSelectedTableTrValue("#tableStaffs"))) {
        ajaxAddStaffToProjectTeam(getUrlParam("project.id") ,getSelectedTableTrValue("#tableStaffs"), getUrlParam("type"), function (data) {
            alert("提交成功！");
            //gotoBack();
            gotoLocalHtml('/project/prepare/team_build.html?project.id='
                + getUrlParam('project.id'));
        }, function (data) {
        }, true, waiting, run, true);
    }
}

