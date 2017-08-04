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
                +'</a></td></td><td>' + data.attachments[i].cstCreate
                +'</td><td>' + data.attachments[i].staffName
                +'</td><td class="audit_status">'+ AUDIT_STATUS[data.attachments[i].auditStatus]
                +'</td><td>'+ data.attachments[i].auditOpinion
                +'</td><td class="list_last_col"><button class="btn_delete" type="button" onclick="btnDeleteFile(this)">删除</button></td></tr>');
        }
        }, function(data) {
    }, true, waiting, run, true);
}

/**
 * 删除竣工验收文件
 */
function btnDeleteFile(obj) {
    if (confirm("确定删除该文件？")) {
        var attachmentId = $(obj).parent().parent().children("td").first().html();
        ajaxRemoveCompletionDataAttachmentById(getUrlParam("project.id"), attachmentId, function (data) {
            //刷新列表
            showFileList();
        }, function (data) {
        }, true, waiting, run, true);
    }
}

//上传工具的参数
var uploaderParams = {
    runtimes: 'html5,flash,silverlight,html4',
    browse_button: 'btnUpload',
    multi_selection: false,
    multipart: true,
    url: URL_OSS,
    flash_swf_url: getContextRoot() + '/common/plupload/Moxie.swf',
    silverlight_xap_url: getContextRoot()
    + '/common/plupload/Moxie.xap',
    init: {
        // 上传绑定事件-添加文件
        FilesAdded: function (uploader, files) {
            // 开始上传
            ajaxAcquireCompletionDataUpToken(getUrlParam("project.id"), function (data) {
                uploader.bind('BeforeUpload', function (uploader, file) {
                    // 进行参数配置
                    new_multipart_params = {
                        'key': data.dir + "/" + data.uuid + "_" + file.name,
                        'OSSAccessKeyId': data.OSSAccessKeyId,
                        'policy': data.policy,
                        'success_action_status': "200",
                        'Signature': data.Signature,
                        'x-oss-security-token': data.securityToken,
                        'callback': data.callback,
						'Content-Disposition':"attachment; filename="+file.name
                    };
                    uploader.settings.url=data.ossUrl;
					uploader.setOption({
                        'multipart_params': new_multipart_params
                    });
                    waiting();
                });
                uploader.start();
            }, function (data) {
            }, true, waiting, run, true);
        },
        // 上传进度显示
        UploadProgress: function (uploader, file) {
            waiting(file.percent + "%");
        },
        FileUploaded: function (uploader, file, info) {
            run();
            if (info.status == 200) {
                if (RETURNCODE_SUCCESS == JSON.parse(info.response).returnCode) {
                    alert("上传成功！");
                    //刷新列表
                    showFileList();
                } else if (RETURNCODE_FAIL == JSON.parse(info.response).returnCode) {
                    alert(JSON.parse(info.response).errorCodeDesc);
                }
            } else if (info.status == 203) {
                alert(info.response);
            } else {
                alert(info.response);
            }
        },
        Error: function (uploader, errObject) {
            run();
            alert(errObject.message);
        }
    }
};
//上传工具
var uploader = new plupload.Uploader(uploaderParams);
uploader.init();

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