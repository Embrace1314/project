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
        //显示质量检查记录详情
        showQualityDetail();
    }, function(data) {
    }, true, waiting, run, true);
}

/**
 * 显示质量检查记录详情
 */
function showQualityDetail() {
    ajaxGetQualityCheckItemDetail(getUrlParam("project.id"), getUrlParam("qualityCheckItem.id"), function(data) {
        //显示质量检查记录信息
        $("#txtInspectDate").html(data.qualityCheckItem.inspectDate.substring(0, 10));
        $("#txtInspector").html(data.qualityCheckItem.staffName);
        $("#txtInspectRecord").html(data.qualityCheckItem.inspectRecord);
        $("#txtOpinion").html(data.qualityCheckItem.rectifyOpinion);
        $("#txtEvaluate").html(data.qualityCheckItem.evaluate);
        $("#txtEvaluateDate").html(data.qualityCheckItem.evaluateDate.substring(0, 10));
        $("#txtFeedback").val(data.qualityCheckItem.projectFeedback);

        //如果审批状态是正在审批或通过，申请审批按钮灰显；如果是重新整改，申请审批按钮可点
        if(data.qualityCheckItem.rectifyStatus=="VERIFY"){
            $("#btnApply").attr({class:"btn_apply_disable",disabled:"disabled"});
        }
        if(data.qualityCheckItem.rectifyStatus=="PASS"){
            $("#btnApply").attr({class:"btn_apply_disable",disabled:"disabled"});
            $("#rdbtnPass").attr("checked","checked");
        }
        if(data.qualityCheckItem.rectifyStatus=="RECTIFY"){
            $("#btnApply").attr("class","btn_apply");
            $("#rdbtnRectify").attr("checked","checked");
        }
        //显示附件
        showAttachment()
    }, function(data) {
    }, true, waiting, run, true);
}

/**
 * 显示附件
 */
function showAttachment() {
    $("#btnUpload").siblings().remove();
    ajaxGetQualityCheckItemDetail(getUrlParam("project.id"), getUrlParam("qualityCheckItem.id"), function(data) {
        //判断附件type如果是CALLBACK，显示删除按钮；如果是RECORD，不显示删除按钮
        for(var i=0;i<data.attachments.length;i++){
            if(data.attachments[i].type=="RECORD"){
                $("#btnUpload").before(
                    '<div class="img_picture_display">'
                    +'<input class="txt_attachment" hidden="hidden" name="'+ data.attachments[i].id +'">'
                    +'<a class="txt_file_name" onclick="btnDownAttachment(this)">'+data.attachments[i].name+'</a></div>');
            }
            if(data.attachments[i].type=="CALLBACK"){
                $("#btnUpload").before(
                    '<div class="img_picture_display">'
                    +'<input class="btn_picture_delete" type="button" onclick="btnDeleteAttachment(this)" name="'
                    + data.attachments[i].id +'">'
                    +'<a class="txt_file_name" onclick="btnDownAttachment(this)">'+data.attachments[i].name+'</a></div>');
            }
        }
        if (uploader != null) {
            uploader.destroy();
        }
        uploader = new plupload.Uploader(uploaderParams);
        uploader.init();
    }, function(data) {
    }, true, waiting, run, true);
}


/**
 * 申请审批
 */
function btnToApply() {
    var txtFeedback = $("#txtFeedback").val();
    if(!verifyStringNotEmpty(txtFeedback,"项目部反馈",1024,true)){
        return;
    }
    ajaxApplyQualityCheckItemVerify(getUrlParam("project.id"), getUrlParam("qualityCheckItem.id"), txtFeedback, function(data) {
        alert("成功提交审批申请！");
        gotoLocalHtml('/project/implement/quality_manage.html?project.id='
            + getUrlParam('project.id'));
    }, function(data) {
    }, true, waiting, run, true);
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
            ajaxAcquireQualityCheckItemAttachmentUpToken(getUrlParam("project.id"), getUrlParam("qualityCheckItem.id"), function (data) {
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
                    //刷新附件信息
                    showAttachment();
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

/**
 *下载附件
 */
function btnDownAttachment(obj) {
    // 获取授权信息
    var attachmentId = $(obj).parent().children("input").first().attr('name');
    ajaxAcquireQualityCheckItemAttachmentDownToken(getUrlParam("project.id"), attachmentId,
        function(data) {
			window.location = data.url;
        }, function() {
        }, true, waiting, run, true);
}

/**
 * 删除附件
 */
function btnDeleteAttachment(obj) {
    if(confirm("确定删除该附件？")){
        ajaxRemoveQualityCheckItemAttachmentById(getUrlParam("project.id"), $(obj).attr("name"), function(data) {
            //刷新附件信息
            showAttachment();
        }, function(data) {
        }, true, waiting, run, true);
    }
}






