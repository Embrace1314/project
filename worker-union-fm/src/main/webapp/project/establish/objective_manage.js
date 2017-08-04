
/**
 * 页面启动调用
 */
$(function() {
    $("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
    //初始化日期
    initDate();
    // 显示项目信息
    showProject();
});

/**
 * 初始化日期
 */
function initDate() {
    initdatepicker_cn();
    $("#txtStartDate").datepicker({
        dateFormat : "yy-mm-dd"
    });
    $("#txtEndDate").datepicker({
        dateFormat : "yy-mm-dd"
    });
}

/**
 * 显示项目信息
 */
function showProject() {
    ajaxGetProjectDetailById(getUrlParam("project.id"), function(data) {
        showProjectTitle(data.project);
        // 显示目标协议书表格
        showAttachment();
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
            showBtnDeleteFile();
            $("#txtAttachmentId").val(data.attachment.id);
            $("#txtFileName").html(data.attachment.name);
            $("#txtCreatorName").html(data.attachment.staffName);
            $("#txtCreateTime").html(data.attachment.cstCreate);
        } else {
            showBtnUpload();
            $("#txtAttachmentId").val("");
            $("#txtFileName").html("");
            $("#txtCreatorName").html("");
            $("#txtCreateTime").html("");
        }
        //显示目标管理信息详情
        showTargetDetail();
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
            $("#btnChooseManager")[0].style.display="none";
            $("#btnDeleteManager")[0].style.display="block";
            $("#btnDeleteManager").attr({name:data.projectManager.id});
        } else {
            $("#btnChooseManager")[0].style.display="block";
            $("#btnDeleteManager")[0].style.display="none";
        }
        if(data.hasTarget){
            $("#txtDurationObjective").val(data.target.duration);
            $("#txtStartDate").val(data.target.startDate.substring(0,10));
            $("#txtEndDate").val(data.target.endDate.substring(0,10));
            $("#txtQualityObjective").val(data.target.quality);
            $("#txtSafeObjective").val(data.target.safety);
            $("#txtCostObjective").val(data.target.cost);
        }
    }, function(data) {
    }, true, waiting, run, true);
}

/**
 * 选择项目经理
 */
function btnToChooseManager() {
    gotoLocalHtml('/project/establish/choose_project_manager.html?project.id='
        + getUrlParam('project.id'));
}

/**
 * 删除项目经理
 */
function btnDeleteManager() {
    ajaxRemoveProjectManager(getUrlParam("project.id"), $("#btnDeleteManager").attr("name"), function(data) {
        $("#txtProjectManager").html("");
        $("#btnChooseManager")[0].style.display="block";
        $("#btnDeleteManager")[0].style.display="none";
        $("#btnDeleteManager").attr({name:""});
    }, function(data) {
    }, true, waiting, run, true);
}

/**
 * 保存目标管理信息
 */
function btnSave() {
	if(!preventRapidClick()){
		return;
	}
    var duration = $("#txtDurationObjective").val();
    var startDate = $("#txtStartDate").val();
    var endDate = $("#txtEndDate").val();
    var quality = $("#txtQualityObjective").val();
    var safety = $("#txtSafeObjective").val();
    var cost = $("#txtCostObjective").val();

    if (!verifyNotEmpty(duration, "工期目标",true)){
        return;
    }
    if (!verifyDate(startDate, "开工日期",true)){
        return;
    }
    if (!verifyDate(endDate, "完工日期",true)){
        return;
    }
    if (!verifyStringNotEmpty(quality, "质量目标",1024,true)){
        return;
    }
    if (!verifyStringNotEmpty(safety, "安全与文明施工目标",1024,true)){
        return;
    }
    if (!verifyMoney(cost, "成本目标",true)){
        return;
    }

    var target = {
        duration : duration,
        startDate : startDate,
        endDate : endDate,
        quality : quality,
        safety : safety,
        cost : cost
    };
    ajaxSaveTarget(getUrlParam("project.id"), target, function(data) {
        alert("保存成功");
    }, function(data) {
    }, true, waiting, run, true);
}


// 上传工具的参数
var uploaderParams = {
    runtimes : 'html5,flash,silverlight,html4',
    browse_button : 'btnUpload',
    multi_selection : false,
    multipart : true,
    url : URL_OSS,
    flash_swf_url : getContextRoot() + '/common/plupload/Moxie.swf',
    silverlight_xap_url : getContextRoot() + '/common/plupload/Moxie.xap',
    init : {

        // 上传绑定事件-添加文件
        FilesAdded : function(uploader, files) {
            // 开始上传
            ajaxAcquireAgreementOfTargetUpToken(getUrlParam("project.id"), function(
                data) {
                uploader.bind('BeforeUpload', function(uploader, file) {
                    // 进行参数配置
                    new_multipart_params = {
                        'key' : data.dir + "/" + data.uuid + "_" + file.name,
                        'OSSAccessKeyId' : data.OSSAccessKeyId,
                        'policy' : data.policy,
                        'success_action_status' : "200",
                        'Signature' : data.Signature,
                        'x-oss-security-token' : data.securityToken,
                        'callback' : data.callback,
						'Content-Disposition':"attachment; filename="+file.name
                    };
                    uploader.settings.url=data.ossUrl;
                    uploader.setOption({
                        'multipart_params' : new_multipart_params
                    });
                    waiting();
                });
                uploader.start();
            }, function(data) {
            }, true, waiting, run, true);
        },
        // 上传进度显示
        UploadProgress : function(uploader, file) {
            waiting(file.percent + "%");
        },
        FileUploaded : function(uploader, file, info) {
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
        Error : function(uploader, errObject) {
            run();
            alert(errObject.message);
        }
    }
};

// 上传工具
var uploader = new plupload.Uploader(uploaderParams);

/**
 * 显示上传按钮
 */
function showBtnUpload() {
    $("#btnUpload").show();
    $("#btnDeleteFile").hide();
    if (uploader == null) {
        uploader = new plupload.Uploader(uploaderParams);
    }
    uploader.init();
}

/**
 * 显示删除文件按钮
 */
function showBtnDeleteFile() {
    $("#btnUpload").hide();
    $("#btnDeleteFile").show();
    if (uploader != null) {
        uploader.destroy();
        uploader = null;
    }
}

/**
 * 删除附件
 */
function btnDeleteFile() {
    if (confirm("确定删除该附件？")) {
        ajaxRemoveTargetUniqueAttachment(getUrlParam("project.id"), $("#txtAttachmentId").val(),
            function(data) {
                // 刷新附件信息
                showAttachment();
            }, function(data) {
            }, true, waiting, run, true);
    }
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


