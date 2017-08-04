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
        // 显示附件信息
        showAttachment();
        //显示结算评估项
        showSettlementInfo();
    }, function(data) {
    }, true, waiting, run, true);
}

/**
 * 显示附件
 **/
function showAttachment(){
    ajaxGetLatestInternalSettlementAttachment(getUrlParam("project.id"), function (data) {
        if (data.hasAttachment) {
            $("#txtAttachmentId").val(data.attachment.id);
            $("#txtFileName").html(data.attachment.name);
            $("#txtCreatorName").html(data.attachment.staffName);
            $("#txtCreateTime").html(data.attachment.cstCreate);
        }
    }, function (data) {
    }, true, waiting, run, true);
}

/**
 * 显示结算评估项
 **/
function showSettlementInfo(){
    ajaxGetInternalSettlementDetail(getUrlParam("project.id"), function (data) {
        if(data.hasTarget){
            $("#txtDuration").html(data.target.duration);
            $("#txtQuality").html(data.target.quality);
            $("#txtSafety").html(data.target.safety);
            $("#txtCost").html(data.target.cost);
        }
        if(data.hasInternalSettlement){
            $("#txtDurationAssess").html(data.internalSettlement.duration);
            $("#txtQualityAssess").html(data.internalSettlement.quality);
            $("#txtSafetyAssess").html(data.internalSettlement.safety);
            $("#txtCostAssess").html(data.internalSettlement.cost);
            $("#txtInternalMoney").html(data.internalSettlement.internalCost);
            $("#txtExternalMoney").html(data.internalSettlement.extraAward);
        }
    }, function (data) {
    }, true, waiting, run, true);
}

/**
 *下载附件
 */
function btnDownloadFile() {
    // 获取授权信息
    ajaxAcquireInternalSettlementDownToken(getUrlParam("project.id"), $("#txtAttachmentId").val(),
        function(data) {
			window.location = data.url;
        }, function() {
        }, true, waiting, run, true);
}
