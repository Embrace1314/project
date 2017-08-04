
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
            $("#txtDurationAssess").val(data.internalSettlement.duration);
            $("#txtQualityAssess").val(data.internalSettlement.quality);
            $("#txtSafetyAssess").val(data.internalSettlement.safety);
            $("#txtCostAssess").val(data.internalSettlement.cost);
            $("#txtInternalMoney").val(data.internalSettlement.internalCost);
            $("#txtExternalMoney").val(data.internalSettlement.extraAward);
        }
    }, function (data) {
    }, true, waiting, run, true);
}

/**
 * 保存按钮
 **/
function btnSave(){
    var duration = $("#txtDurationAssess").val();
    var quality = $("#txtQualityAssess").val();
    var safety = $("#txtSafetyAssess").val();
    var cost = $("#txtCostAssess").val();
    var internalMoney = $("#txtInternalMoney").val();
    var externalMoney = $("#txtExternalMoney").val();

    if (!verifyNotEmpty(duration, "工期目标完成情况",true)){
        return;
    }
    if (!verifyStringNotEmpty(quality, "质量目标完成情况",1024,true)){
        return;
    }
    if (!verifyStringNotEmpty(safety, "安全与施工目标完成情况",1024,true)){
        return;
    }
    if (!verifyMoney(cost, "成本目标完成情况",true)){
        return;
    }
    if (!verifyMoney(internalMoney, "内部结算金额",true)){
        return;
    }
    if (!verifyMoney(externalMoney, "额外奖励金额",true)){
        return;
    }
    var internalSettlement = {
        duration : duration,
        quality : quality,
        safety : safety,
        cost : cost,
        internalCost : internalMoney,
        extraAward : externalMoney
    };
    ajaxSaveInternalSettlementAssessment(getUrlParam("project.id"), internalSettlement,
        function (data) {
            alert("保存成功");
        }, function (data) {
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
            ajaxAcquireInternalSettlementUpToken(getUrlParam("project.id"), function (data) {
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
uploader.init();

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