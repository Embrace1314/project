
var PAGE_SIZE = 12;

$(function() {
    $("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
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
    var staffDto = {
        name : $("#txtName").val(),
        jobNo : $("#txtJobNo").val(),
        idCardNo : $("#txtIdCardNo").val(),
        roleName : $("#txtRoleName").val(),
        projectName : $("#txtProjectName").val(),
        startDate : $("#txtStartDate").val(),
        endDate : $("#txtEndDate").val()
    };
    ajaxPageStaffByFuzzy(staffDto, new Page(PAGE_SIZE, 1),
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
 * 提交选定项目经理信息
 */
function btnConfirm() {
    var staffId = getSelectedTableTrValue("#tableStaffs");
    if (!isEmpty(staffId)) {
        if (confirm("确定选择该项目经理？")) {
            ajaxAddProjectManager(getUrlParam("project.id"), staffId, function(data){
                gotoLocalHtml("/project/establish/objective_manage.html?project.id="
                    + getUrlParam("project.id"));
            }, function(data){}, true, waiting, run, true);
        }
    }
}

/**
 * 上一页
 */
function btnPrevPage() {
    var staffDto = {
        name : $("#txtName").val(),
        jobNo : $("#txtJobNo").val(),
        idCardNo : $("#txtIdCardNo").val(),
        roleName : $("#txtRoleName").val(),
        projectName : $("#txtProjectName").val(),
        startDate : $("#txtStartDate").val(),
        endDate : $("#txtEndDate").val()
    };
    ajaxPageStaffByFuzzy(staffDto, new Page(PAGE_SIZE, Math.max(
            parseInt($("#txtPagination").html()) - 1, 1)), function(
            data) {
            showSearchResult(data);
        }, function(data) {
        }, true, waiting, run, true);
}

/**
 * 下一页
 */
function btnNextPage() {
    var staffDto = {
        name : $("#txtName").val(),
        jobNo : $("#txtJobNo").val(),
        idCardNo : $("#txtIdCardNo").val(),
        roleName : $("#txtRoleName").val(),
        projectName : $("#txtProjectName").val(),
        startDate : $("#txtStartDate").val(),
        endDate : $("#txtEndDate").val()
    };
    ajaxPageStaffByFuzzy(staffDto, new Page(PAGE_SIZE, parseInt($(
                "#txtPagination").html()) + 1, 1), function(data) {
            showSearchResult(data);
        }, function(data) {
        }, true, waiting, run, true);
}
