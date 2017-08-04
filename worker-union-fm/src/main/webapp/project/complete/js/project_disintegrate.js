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
        
        //显示项目报建人员信息
        showBuildReportInfo();
        if(COLLAPSED_TYPE_COLLAPSED == data.project.collapseStatus){
            $("#txtHint").html("项目部已解体！");
            $("#btnConfirm").attr({class:'btn_confirm_disable',disabled:'disabled'});
            $("#btnCancel").attr({class:'btn_cancel_disable',disabled:'disabled'});
        }
    }, function(data) {
    }, true, waiting, run, true);
}

/**
 * 显示项目名称
 */
function showBuildReportInfo() {
    ajaxListProjectApplicationStaff(getUrlParam("project.id"), function(data) {
        for (var i = 0; i < data.staff.length; i++) {
            if (data.staff[i].type=='PROJECT_MANAGER') {
                setTableRowContent("#tableStaff", 0, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age, data.staff[i].phone, '项目经理');
            }
            if (data.staff[i].type=='TECHNICAL_DIRECTOR') {
                setTableRowContent("#tableStaff", 1, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,data.staff[i].phone, "技术负责人");
            }
            if (data.staff[i].type=='CONSTRUCTION_WORKER') {
                setTableRowContent("#tableStaff", 2, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,data.staff[i].phone,"施工员");
            }
            if (data.staff[i].type=='QUALITY_WORKER') {
                setTableRowContent("#tableStaff", 3, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,data.staff[i].phone, "质量员");
            }
            if (data.staff[i].type=='SAFETY_WORKER') {
                setTableRowContent("#tableStaff", 4, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,data.staff[i].phone, "安全员");
            }
            if (data.staff[i].type=='BUDGETER') {
                setTableRowContent("#tableStaff", 5, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,data.staff[i].phone, "预算员");
            }
            if (data.staff[i].type=='MATERIALMAN') {
                setTableRowContent("#tableStaff", 6, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,data.staff[i].phone, "材料员");
            }
            if (data.staff[i].type=='MACHINIST') {
                setTableRowContent("#tableStaff", 7, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,data.staff[i].phone, "机械员");
            }
            if (data.staff[i].type=='LABOR_WORKER') {
                setTableRowContent("#tableStaff", 8, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,data.staff[i].phone, "劳务员");
            }
            if (data.staff[i].type=='DATA_PROCESSOR') {
                setTableRowContent("#tableStaff", 9, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,data.staff[i].phone, "资料员");
            }
        }
    }, function(data) {
    }, true, waiting, run, true);
}


/**
 * 确定按钮
 * 项目部解体
 */
function btnConfirm() {
    if (confirm("确定解体该项目部？")) {
        ajaxCollapseProjectById(getUrlParam("project.id"), function(data) {
            //刷新页面
            reload();
        }, function(data) {
        }, true, waiting, run, true);
    }

}
