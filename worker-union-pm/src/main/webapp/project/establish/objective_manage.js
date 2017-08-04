
/**
 * 页面启动调用
 */
$(function() {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
    // 显示项目信息
    showProject();
});

/**
 * 显示项目信息
 */
function showProject() {
    ajaxGetProjectDetailById(getUrlParam("project.id"), function(data) {
    	showProjectTitle(data.project);
        // 显示目标协议书表格
        showAttachment();
        //显示目标管理信息详情
        showTargetDetail();
    }, function(data) {
    }, true, waiting, run, true);
}

/**
 * 显示目标协议书表格信息
 */
function showAttachment() {
    ajaxGetTargetUniqueAttachment(getUrlParam("project.id"), function(
        data) {
        if (data.hasAttachment) {
            $("#btnDownloadTarget").show();
            $("#btnDownloadTargetDisable").hide();
            $("#txtAttachmentId").val(data.attachment.id);
            $("#txtFileName").html(data.attachment.name);
            $("#txtCreatorName").html(data.attachment.staffName);
            $("#txtCreateTime").html(data.attachment.cstCreate);
        }else {
            $("#btnDownloadTarget").hide();
            $("#btnDownloadTargetDisable").show();
        }
    }, function(data) {
    }, true, waiting, run, true);
}

/**
 * 显示目标管理信息详情
 */
function showTargetDetail() {
    ajaxGetTargetDetail(getUrlParam("project.id"), function(data) {
        if(data.hasProjectManager){
            $("#txtProjectManager").html(data.projectManager.name);
        }
        if(data.hasTarget){
            $("#txtDurationObjective").html(data.target.duration);
            $("#txtStartDate").html(data.target.startDate.substring(0, 10));
            $("#txtEndDate").html(data.target.endDate.substring(0, 10));
            $("#txtQualityObjective").html(data.target.quality);
            $("#txtSafeObjective").html(data.target.safety);
            $("#txtCostObjective").html(data.target.cost);
        }
    }, function(data) {
    }, true, waiting, run, true);
}

/**
 * 下载目标协议书
 */
function btnDownloadFile() {
    // 获取授权信息
    ajaxAcquireAgreementOfTargetDownToken(getUrlParam("project.id"), $("#txtAttachmentId").val(),
        function(data) {
    		window.location = data.url;
        }, function() {
        }, true, waiting, run, true);
}

