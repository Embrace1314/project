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
		$("#txtNum").html(data.project.num);
		$("#txtName").html(data.project.name);
		$("#txtConstructerName").html(data.project.constructerName);
		$("#txtDesignerName").html(data.project.designerName);
		$("#txtSuperviserName").html(data.project.superviserName);
		$("#txtAddress").html(data.project.address);
		$("#txtType").html(data.project.type);
		$("#txtScale").html(data.project.scale);
		$("#txtBidPrice").html(data.project.bidPrice);
		$("#txtBidDuration").html(data.project.bidDuration);
		if (!isEmpty(data.attachment.id)){
			$("#txtAttachmentId").val(data.attachment.id);
		}
	}, function(data) {
	}, true, waiting, run, true);
}

/**
 * 下载中标通知书
 */
function btnDownload() {
	// 获取授权信息
	ajaxAcquireLetterOfAcceptanceDownToken(getUrlParam("project.id"), $("#txtAttachmentId").val(),
			function(data) {
				window.location = data.url;
				$("#imgDownloadMark").attr("class","img_download_mark_success");
			}, function() {
			}, true, waiting, run, true);
}
