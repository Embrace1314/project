var TEAM_NUMBER = 10;

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
                setTableRowContent("#tableStaff", 1, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,
                    data.staff[i].phone,'<input class="btn_delete_disable" type="button" value="删除" disabled="disabled">',
                    '项目经理');
            }
            if (data.staff[i].type=='TECHNICAL_DIRECTOR') {
                $("#tableStaff").find("tr").eq(2).find(".txt_staff_info").show();
                $("#tableStaff").find("tr").eq(2).find(".btn_staff_info").hide();
                setTableRowContent("#tableStaff", 2, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,
                    data.staff[i].phone,'<input class="btn_delete" type="button" value="删除" onclick="btnDelete(this)" name="'+
                    data.staff[i].id+'">', "技术负责人");
            }
            if (data.staff[i].type=='CONSTRUCTION_WORKER') {
                $("#tableStaff").find("tr").eq(3).find(".txt_staff_info").show();
                $("#tableStaff").find("tr").eq(3).find(".btn_staff_info").hide();
                setTableRowContent("#tableStaff", 3, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,
                    data.staff[i].phone,'<input class="btn_delete" type="button" value="删除" onclick="btnDelete(this)" name="'+
                    data.staff[i].id+'">', "施工员");
            }
            if (data.staff[i].type=='QUALITY_WORKER') {
                $("#tableStaff").find("tr").eq(4).find(".txt_staff_info").show();
                $("#tableStaff").find("tr").eq(4).find(".btn_staff_info").hide();
                setTableRowContent("#tableStaff", 4, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,
                    data.staff[i].phone,'<input class="btn_delete" type="button" value="删除" onclick="btnDelete(this)" name="'+
                    data.staff[i].id+'">', "质量员");
            }
            if (data.staff[i].type=='SAFETY_WORKER') {
                $("#tableStaff").find("tr").eq(5).find(".txt_staff_info").show();
                $("#tableStaff").find("tr").eq(5).find(".btn_staff_info").hide();
                setTableRowContent("#tableStaff", 5, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,
                    data.staff[i].phone,'<input class="btn_delete" type="button" value="删除" onclick="btnDelete(this)" name="'+
                    data.staff[i].id+'">', "安全员");
            }
            if (data.staff[i].type=='BUDGETER') {
                $("#tableStaff").find("tr").eq(6).find(".txt_staff_info").show();
                $("#tableStaff").find("tr").eq(6).find(".btn_staff_info").hide();
                setTableRowContent("#tableStaff", 6, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,
                    data.staff[i].phone,'<input class="btn_delete" type="button" value="删除" onclick="btnDelete(this)" name="'+
                    data.staff[i].id+'">', "预算员");
            }
            if (data.staff[i].type=='MATERIALMAN') {
                $("#tableStaff").find("tr").eq(7).find(".txt_staff_info").show();
                $("#tableStaff").find("tr").eq(7).find(".btn_staff_info").hide();
                setTableRowContent("#tableStaff", 7, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,
                    data.staff[i].phone,'<input class="btn_delete" type="button" value="删除" onclick="btnDelete(this)" name="'+
                    data.staff[i].id+'">', "材料员");
            }
            if (data.staff[i].type=='MACHINIST') {
                $("#tableStaff").find("tr").eq(8).find(".txt_staff_info").show();
                $("#tableStaff").find("tr").eq(8).find(".btn_staff_info").hide();
                setTableRowContent("#tableStaff", 8, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,
                    data.staff[i].phone,'<input class="btn_delete" type="button" value="删除" onclick="btnDelete(this)" name="'+
                    data.staff[i].id+'">', "机械员");
            }
            if (data.staff[i].type=='LABOR_WORKER') {
                $("#tableStaff").find("tr").eq(9).find(".txt_staff_info").show();
                $("#tableStaff").find("tr").eq(9).find(".btn_staff_info").hide();
                setTableRowContent("#tableStaff", 9, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,
                    data.staff[i].phone,'<input class="btn_delete" type="button" value="删除" onclick="btnDelete(this)" name="'+
                    data.staff[i].id+'">', "劳务员");
            }
            if (data.staff[i].type=='DATA_PROCESSOR') {
                $("#tableStaff").find("tr").eq(10).find(".txt_staff_info").show();
                $("#tableStaff").find("tr").eq(10).find(".btn_staff_info").hide();
                setTableRowContent("#tableStaff", 10, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,
                    data.staff[i].phone,'<input class="btn_delete" type="button" value="删除" onclick="btnDelete(this)" name="'+
                    data.staff[i].id+'">', "资料员");
            }
        }
    }, function(data) {
    }, true, waiting, run, true);
}

/**
 * 选择团队成员按钮
 */
function btnChooseTeam(obj) {
    gotoLocalHtml("/project/prepare/choose_team_number.html?project.id="
        + getUrlParam("project.id")+"&type=" + $(obj).attr("name"));
}

/**
 * 删除按钮
 */
function btnDelete(obj) {
    if (!isEmpty($(obj).attr("name"))) {
        if (confirm("确定删除该人员信息？")) {
            ajaxRemoveStaffFromProjectTeam(getUrlParam("project.id"), $(obj).attr("name"), function(data) {
                //刷新列表
                reload();
                }, function(data) {
                }, true, waiting, run, true);
        }
    }
}

/**
 * 跳转到上传附件页面
 */
function btnToUploadAttachment() {
    gotoLocalHtml('/project/prepare/team_build_attachment.html?project.id='
        + getUrlParam('project.id'));
}

