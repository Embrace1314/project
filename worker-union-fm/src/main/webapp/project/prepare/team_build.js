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
        // 显示团队人员信息列表
        showTeamInfo();
    }, function(data) {
    }, true, waiting, run, true);
}

/**
 * 显示团队人员信息
 */
function showTeamInfo() {
    ajaxListProjectTeamStaff(getUrlParam("project.id"), function(data) {
        for (var i = 0; i < data.staff.length; i++) {
            if (data.staff[i].type=='PROJECT_MANAGER') {
                setTableRowContent("#tableStaff", 0, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,
                    data.staff[i].phone, "项目经理");
            }
            if (data.staff[i].type=='TECHNICAL_DIRECTOR') {
                setTableRowContent("#tableStaff", 1, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,
                    data.staff[i].phone, "技术负责人");
            }
            if (data.staff[i].type=='CONSTRUCTION_WORKER') {
                setTableRowContent("#tableStaff", 2, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,
                    data.staff[i].phone, "施工员");
            }
            if (data.staff[i].type=='QUALITY_WORKER') {
                setTableRowContent("#tableStaff", 3, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,
                    data.staff[i].phone, "质量员");
            }
            if (data.staff[i].type=='SAFETY_WORKER') {
                setTableRowContent("#tableStaff", 4, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,
                    data.staff[i].phone, "安全员");
            }
            if (data.staff[i].type=='BUDGETER') {
                setTableRowContent("#tableStaff", 5, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,
                    data.staff[i].phone, "预算员");
            }
            if (data.staff[i].type=='MATERIALMAN') {
                setTableRowContent("#tableStaff", 6, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,
                    data.staff[i].phone, "材料员");
            }
            if (data.staff[i].type=='MACHINIST') {
                setTableRowContent("#tableStaff", 7, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,
                    data.staff[i].phone, "机械员");
            }
            if (data.staff[i].type=='LABOR_WORKER') {
                setTableRowContent("#tableStaff", 8, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,
                    data.staff[i].phone, "劳务员");
            }
            if (data.staff[i].type=='DATA_PROCESSOR') {
                setTableRowContent("#tableStaff", 9, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,
                    data.staff[i].phone, "资料员");
            }
        }
    }, function(data) {
    }, true, waiting, run, true);
}

/**
 * 跳转到查看附件页面
 */
function btnToAttachmentDetail() {
    gotoLocalHtml('/project/prepare/team_build_attachment.html?project.id='
        + getUrlParam('project.id'));
}