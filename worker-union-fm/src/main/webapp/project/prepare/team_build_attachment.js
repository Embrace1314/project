/**
 * 页面启动加载
 */
$(function() {
    $("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
    refreshTeamAttachments();
});

/**
 * 刷新附件显示
 */
function refreshTeamAttachments() {
    ajaxListProjectTeamAttachment(getUrlParam("project.id"), function(
        data) {
        // 显示附件信息
        showTeamAttachments(data.attachments);
    }, function(data) {
    }, true, waiting, run, true);
}

/**
 * 显示附件列表
 *
 * @param TeamAttachments
 */
function showTeamAttachments(teamAttachments) {
    $("#divAttachment").empty();
    for (var i = 0; i < teamAttachments.length; i++) {
        $("#divAttachment")
            .append(
                '<div class="img_picture_display"><attachmentId hidden="true">'
                + teamAttachments[i].id
                + '</attachmentId><a class="txt_file_name" onclick="btnDownloadAttachment(this)">'
                + teamAttachments[i].name
                + '</a></div>');
    }
}

/**
 * 确定按钮
 */
function btnSave() {
    gotoBack();
}

/**
 * 下载附件
 *
 * @param obj
 */
function btnDownloadAttachment(obj) {
    var attachmentId = $(obj).siblings("attachmentId").first().html();
    if (!isEmpty(attachmentId)) {
        // 获取授权信息
        ajaxAcquireProjectTeamAttachmentDownToken(getUrlParam("project.id"), attachmentId,
            function(data) {
                window.location = data.url;
            }, function(data) {
            }, true, waiting, run, true);
    }
}
