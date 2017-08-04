var STAFF_NUMBER = 8;

/**
 * 页面启动调用
 */
$(function() {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
    // 显示项目报建信息
    showProject();
});

/**
 * 显示项目报建信息
 */
function showProject() {
    ajaxGetProjectDetailById(getUrlParam("project.id"), function(data) {
    	showProjectTitle(data.project);
        // 显示人员信息列表
        showPersonList();
        //显示附件信息
        showAttachment();
    }, function(data) {
    }, true, waiting, run, true);
}

/**
 * 显示人员信息列表
 */
function showPersonList() {
    ajaxListProjectApplicationStaff(getUrlParam("project.id"), function(
        data) {
        for (var i = 0; i < data.staff.length; i++) {
            if (data.staff[i].type=='PROJECT_MANAGER') {
                setTableRowContent("#tableStaff", 1, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age, data.staff[i].idCardNo,
                    data.staff[i].qualification,'项目经理');
            }
            if (data.staff[i].type=='TECHNICAL_DIRECTOR') {
                setTableRowContent("#tableStaff", 2, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,data.staff[i].idCardNo,
                    data.staff[i].qualification, "技术负责人");
            }
            if (data.staff[i].type=='CONSTRUCTION_WORKER') {
                setTableRowContent("#tableStaff", 3, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,data.staff[i].idCardNo,
                    data.staff[i].qualification, "施工员");
            }
            if (data.staff[i].type=='QUALITY_WORKER') {
                setTableRowContent("#tableStaff", 4, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,data.staff[i].idCardNo,
                    data.staff[i].qualification,"质量员");
            }
            if (data.staff[i].type=='SAFETY_WORKER') {
                setTableRowContent("#tableStaff", 5, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,data.staff[i].idCardNo,
                    data.staff[i].qualification, "安全员");
            }
            if (data.staff[i].type=='BUDGETER') {
                setTableRowContent("#tableStaff", 6, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,data.staff[i].idCardNo,
                    data.staff[i].qualification, "预算员");
            }
            if (data.staff[i].type=='MATERIALMAN') {
                setTableRowContent("#tableStaff", 7, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,data.staff[i].idCardNo,
                    data.staff[i].qualification, "材料员");
            }
            if (data.staff[i].type=='MACHINIST') {
                setTableRowContent("#tableStaff", 8, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,data.staff[i].idCardNo,
                    data.staff[i].qualification, "机械员");
            }
            if (data.staff[i].type=='LABOR_WORKER') {
                setTableRowContent("#tableStaff", 9, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,data.staff[i].idCardNo,
                    data.staff[i].qualification, "劳务员");
            }
            if (data.staff[i].type=='DATA_PROCESSOR') {
                setTableRowContent("#tableStaff", 10, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,data.staff[i].idCardNo,
                    data.staff[i].qualification, "资料员");
            }
        }
    }, function(data) {
    }, true, waiting, run, true);
}

/**
 * 显示附件信息
 */
function showAttachment() {
    ajaxListProjectApplicationAttachment(getUrlParam("project.id"), function(
        data) {
        //下载合同备案表按钮
        if(data.hasAttachmentContractRecord){
            $("#btnContract").attr("name",data.attachmentContractRecord.id);
        }else{
            $("#btnContract").attr({class:"btn_download_disable",disabled:"disabled"});
        }
        //下载质监通知书按钮
        if(data.hasAttachmentQualityInspectNotice){
            $("#btnQuality").attr("name",data.attachmentQualityInspectNotice.id);
        }else{
            $("#btnQuality").attr({class:"btn_download_disable",disabled:"disabled"});
        }
        //下载安监登记表按钮
        if(data.hasAttachmentSafetySupervisionForm){
            $("#btnSafety").attr("name",data.attachmentSafetySupervisionForm.id);
        }else{
            $("#btnSafety").attr({class:"btn_download_disable",disabled:"disabled"});
        }
        //下载施工许可证按钮
        if(data.hasAttachmentConstructionPermit){
            $("#btnConstrcution").attr("name",data.attachmentConstructionPermit.id);
        }else{
            $("#btnConstrcution").attr({class:"btn_download_disable",disabled:"disabled"});
        }
    }, function(data) {
    }, true, waiting, run, true);
}

/**
 * 下载合同备案表
 */
function btnDownloadContract() {
    // 获取授权信息
    ajaxAcquireContractRecordDownToken(getUrlParam("project.id"), $("#btnContract").attr("name"),
        function(data) {
			window.location = data.url;
        }, function() {
        }, true, waiting, run, true);
}

/**
 * 下载质监通知书
 */
function btnDownloadQuality() {
    // 获取授权信息
    ajaxAcquireQualityInspectNoticeDownToken(getUrlParam("project.id"), $("#btnQuality").attr("name"),
        function(data) {
			window.location = data.url;
        }, function() {
        }, true, waiting, run, true);
}

/**
 * 下载安监登记表
 */
function btnDownloadSafety() {
    // 获取授权信息
    ajaxAcquireSafetySupervisionFormDownToken(getUrlParam("project.id"), $("#btnSafety").attr("name"),
        function(data) {
			window.location = data.url;
        }, function() {
        }, true, waiting, run, true);
}

/**
 * 下载施工许可证
 */
function btnDownloadConstrcution() {
    // 获取授权信息
    ajaxAcquireConstructionPermitDownToken(getUrlParam("project.id"), $("#btnConstrcution").attr("name"),
        function(data) {
    		window.location = data.url;
        }, function() {
        }, true, waiting, run, true);
}