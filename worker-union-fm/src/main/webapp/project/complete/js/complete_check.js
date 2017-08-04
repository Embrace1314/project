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
        // 显示竣工验收文件列表
        showFileList();
    }, function(data) {
    }, true, waiting, run, true);
}

/**
 * 显示竣工验收文件列表
 */
function showFileList() {
    ajaxListCompletionDataAttachment(getUrlParam("project.id"), function(data) {
        removeTableTr("#TableComplete");
        for (var i = 0; i < data.attachments.length; i++) {
            $("#TableComplete").append(
                '<tr class="list_mid_row"><td hidden="true">'+ data.attachments[i].id
                +'</td><td><a href="#" onclick="btnDownloadFile(this)">'+ data.attachments[i].name
                +'</a></td><td>' + data.attachments[i].cstCreate
                +'</td><td>' + data.attachments[i].staffName
                +'</td><td class="audit_status">'+ AUDIT_STATUS[data.attachments[i].auditStatus]
                +'</td><td><input contenteditable="true" value="'+ data.attachments[i].auditOpinion
                +'"></td><td class="list_last_col_row"><div class="div_audit_btn">'
                +'<button id="btnPass" class="btn_pass" type="button" onclick="btnPass(this)">通过</button><div class="div_line"></div>'
                +'<button id="btnRebut" class="btn_rebut" type="button" onclick="btnRebut(this)">驳回</button></div></td></tr>');
        }
    }, function(data) {
    }, true, waiting, run, true);
}

/**
 * 通过按钮
 */
function btnPass(obj) {
	var attachmentId = $(obj).parents("tr").first().children("td").first()
			.html();
	var opinion = $(obj).parent().parent().prev().children("input").val();
	var attachmentAudit = {
		attachmentId : attachmentId,
		opinion : opinion
	};
	if (!verifyString(opinion, "审核意见", 1024, true)) {
		return;
	}
	if(confirm("确定通过该附件的审核？")){
	ajaxPassCompletionDataAttachment(getUrlParam("project.id"),
			attachmentAudit, function(data) {
				// 刷新列表
				showFileList();
			}, function() {
			}, true, waiting, run, true);
	}
}

/**
 * 驳回按钮
 */
function btnRebut(obj) {
    var attachmentId = $(obj).parent().parent().parent().children("td").first().html();
    var opinion =  $(obj).parent().parent().prev().children("input").val();
    var attachmentAudit = {
        attachmentId : attachmentId,
        opinion : opinion
    };
    if (!verifyString(opinion, "审核意见", 1024, true)) {
		return;
	}
    if(confirm("确定驳回该附件的审核？")){
        ajaxFailCompletionDataAttachment(getUrlParam("project.id"), attachmentAudit,
            function(data) {
                //刷新列表
                showFileList();
            }, function() {
            }, true, waiting, run, true);
    }
}

/**
 *下载
 */
function btnDownloadFile(obj) {
    // 获取授权信息
    var attachmentId = $(obj).parent().parent().children("td").first().html();
    ajaxAcquireCompletionDataDownToken(getUrlParam("project.id"), attachmentId,
        function(data) {
			window.location = data.url;
        }, function() {
        }, true, waiting, run, true);
}



