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
        // 显示附件
        showAttachment();
        //显示报建人员信息
        showTeamInfo();
    }, function(data) {
    }, true, waiting, run, true);
}

/**
 * 显示附件
 */
function showAttachment() {
    ajaxListProjectApplicationAttachment(getUrlParam("project.id"), function(data) {
        //合同备案表
        showBtnContract(data);
        //质量通知书
        showBtnQuality(data);
        //安监登记表
        showBtnSafety(data);
        //施工许可证
        showBtnConstruction(data);
    }, function(data) {
    }, true, waiting, run, true);
}
/**
 * 合同备案表按钮显示
 */
function showBtnContract(data) {
    if (data.hasAttachmentContractRecord) {
        $("#btnUploadContract").hide();
        $("#btnDownloadContract").show();
        $("#btnDeleteContract").attr("name",data.attachmentContractRecord.id);
        if (uploaderContract != null) {
            uploaderContract.destroy();
            uploaderContract = null;
        }
    } else {
        $("#btnUploadContract").show();
        $("#btnDownloadContract").hide();
        if (uploaderContract == null) {
            uploaderContract = new plupload.Uploader(uploaderParamsContract);
        }
        uploaderContract.init();
    }
}
/**
 * 质监通知书按钮显示
 */
function showBtnQuality(data) {
    if (data.hasAttachmentQualityInspectNotice) {
        $("#btnUploadQuality").hide();
        $("#btnDownloadQuality").show();
        $("#btnDeleteQuality").attr("name",data.attachmentQualityInspectNotice.id);
        if (uploaderQuality != null) {
            uploaderQuality.destroy();
            uploaderQuality = null;
        }
    } else {
        $("#btnUploadQuality").show();
        $("#btnDownloadQuality").hide();
        if (uploaderQuality == null) {
            uploaderQuality = new plupload.Uploader(uploaderParamsQuality);
        }
        uploaderQuality.init();
    }
}
/**
 * 安监登记表按钮显示
 */
function showBtnSafety(data) {
    if (data.hasAttachmentSafetySupervisionForm) {
        $("#btnUploadSafety").hide();
        $("#btnDownloadSafety").show();
        $("#btnDeleteSafety").attr("name",data.attachmentSafetySupervisionForm.id);
        if (uploaderSafety != null) {
            uploaderSafety.destroy();
            uploaderSafety = null;
        }
    } else {
        $("#btnUploadSafety").show();
        $("#btnDownloadSafety").hide();
        if (uploaderSafety == null) {
            uploaderSafety = new plupload.Uploader(uploaderParamsSafety);
        }
        uploaderSafety.init();
    }
}
/**
 * 施工许可证按钮显示
 */
function showBtnConstruction(data) {
    if (data.hasAttachmentConstructionPermit) {
        $("#btnUploadConstruction").hide();
        $("#btnDownloadConstruction").show();
        $("#btnDeleteConstruction").attr("name",data.attachmentConstructionPermit.id);
        if (uploaderConstruction != null) {
            uploaderConstruction.destroy();
            uploaderConstruction = null;
        }
    } else {
        $("#btnUploadConstruction").show();
        $("#btnDownloadConstruction").hide();
        if (uploaderConstruction == null) {
            uploaderConstruction = new plupload.Uploader(uploaderParamsConstruction);
        }
        uploaderConstruction.init();
    }
}

// 合同备案表上传工具的参数
var uploaderParamsContract = {
    runtimes : 'html5,flash,silverlight,html4',
    browse_button : 'btnUploadContract',
    multi_selection : false,
    multipart : true,
    url : URL_OSS,
    flash_swf_url : getContextRoot() + '/common/plupload/Moxie.swf',
    silverlight_xap_url : getContextRoot() + '/common/plupload/Moxie.xap',
    init : {

        // 上传绑定事件-添加文件
        FilesAdded : function(uploader, files) {
            // 开始上传
            ajaxAcquireContractRecordUpToken(getUrlParam("project.id"), function(
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
                    reload();
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
var uploaderContract = new plupload.Uploader(uploaderParamsContract);

// 质监通知书上传工具的参数
var uploaderParamsQuality = {
    runtimes : 'html5,flash,silverlight,html4',
    browse_button : 'btnUploadQuality',
    multi_selection : false,
    multipart : true,
    url : URL_OSS,
    flash_swf_url : getContextRoot() + '/common/plupload/Moxie.swf',
    silverlight_xap_url : getContextRoot() + '/common/plupload/Moxie.xap',
    init : {

        // 上传绑定事件-添加文件
        FilesAdded : function(uploader, files) {
            // 开始上传
            ajaxAcquireQualityInspectNoticeUpToken(getUrlParam("project.id"), function(
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
                    reload();
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
var uploaderQuality = new plupload.Uploader(uploaderParamsQuality);

// 安监登记表上传工具的参数
var uploaderParamsSafety = {
    runtimes : 'html5,flash,silverlight,html4',
    browse_button : 'btnUploadSafety',
    multi_selection : false,
    multipart : true,
    url : URL_OSS,
    flash_swf_url : getContextRoot() + '/common/plupload/Moxie.swf',
    silverlight_xap_url : getContextRoot() + '/common/plupload/Moxie.xap',
    init : {

        // 上传绑定事件-添加文件
        FilesAdded : function(uploader, files) {
            // 开始上传
            ajaxAcquireSafetySupervisionFormUpToken(getUrlParam("project.id"), function(
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
                    reload();
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
var uploaderSafety = new plupload.Uploader(uploaderParamsSafety);

// 施工许可证上传工具的参数
var uploaderParamsConstruction = {
    runtimes : 'html5,flash,silverlight,html4',
    browse_button : 'btnUploadConstruction',
    multi_selection : false,
    multipart : true,
    url : URL_OSS,
    flash_swf_url : getContextRoot() + '/common/plupload/Moxie.swf',
    silverlight_xap_url : getContextRoot() + '/common/plupload/Moxie.xap',
    init : {

        // 上传绑定事件-添加文件
        FilesAdded : function(uploader, files) {
            // 开始上传
            ajaxAcquireConstructionPermitUpToken(getUrlParam("project.id"), function(
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
                    reload();
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
var uploaderConstruction = new plupload.Uploader(uploaderParamsConstruction);

/**
 * 下载合同备案表
 */
function btnDownloadContract() {
    // 获取授权信息
    ajaxAcquireContractRecordDownToken(getUrlParam("project.id"), $("#btnDeleteContract").attr("name"),
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
    ajaxAcquireQualityInspectNoticeDownToken(getUrlParam("project.id"), $("#btnDeleteQuality").attr("name"),
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
    ajaxAcquireSafetySupervisionFormDownToken(getUrlParam("project.id"), $("#btnDeleteSafety").attr("name"),
        function(data) {
    		window.location = data.url;
        }, function() {
        }, true, waiting, run, true);
}
/**
 * 下载施工许可证
 */
function btnDownloadConstruction() {
    // 获取授权信息
    ajaxAcquireConstructionPermitDownToken(getUrlParam("project.id"), $("#btnDeleteConstruction").attr("name"),
        function(data) {
    		window.location = data.url;
        }, function() {
        }, true, waiting, run, true);
}

/**
 * 删除合同备案表
 */
function btnDeleteContract() {
    var attachmentId = $("#btnDeleteContract").attr("name");
    if (!isEmpty(attachmentId)) {
        if (confirm("确定删除该合同备案表？")) {
            ajaxRemoveContractRecordAttachment(getUrlParam("project.id"), attachmentId,
                function(data) {
                    //刷新附件信息
                    reload();
                }, function() {
                }, true, waiting, run, true);
        }
    }
}
/**
 * 删除质监通知书
 */
function btnDeleteQuality() {
    var attachmentId = $("#btnDeleteQuality").attr("name");
    if (!isEmpty(attachmentId)) {
        if (confirm("确定删除该质监通知书？")) {
            ajaxRemoveQualityInspectNoticeAttachment(getUrlParam("project.id"), attachmentId,
                function(data) {
                    //刷新附件信息
                    reload();
                }, function() {
                }, true, waiting, run, true);
        }
    }
}
/**
 * 删除安监登记表
 */
function btnDeleteSafety() {
    var attachmentId = $("#btnDeleteSafety").attr("name");
    if (!isEmpty(attachmentId)) {
        if (confirm("确定删除该安监登记表？")) {
            ajaxRemoveSafetySupervisionFormAttachment(getUrlParam("project.id"), attachmentId,
                function(data) {
                    //刷新附件信息
                    reload();
                }, function() {
                }, true, waiting, run, true);
        }
    }
}
/**
 * 删除施工许可证
 */
function btnDeleteConstruction() {
    var attachmentId = $("#btnDeleteConstruction").attr("name");
    if (!isEmpty(attachmentId)) {
        if (confirm("确定删除该施工许可证？")) {
            ajaxRemoveConstructionPermitAttachment(getUrlParam("project.id"), attachmentId,
                function(data) {
                    //刷新附件信息
                    reload();
                }, function() {
                }, true, waiting, run, true);
        }
    }
}


/**
 * 显示报建人员信息
 */
function showTeamInfo() {
    ajaxListProjectApplicationStaff(getUrlParam("project.id"), function(data) {
        for (var i = 0; i < data.staff.length; i++) {
            if (data.staff[i].type=='PROJECT_MANAGER') {
                $("#tableStaff").find("tr").eq(1).find(".txt_staff_info").show();
                $("#tableStaff").find("tr").eq(1).find(".btn_staff_info").hide();
                setTableRowContent("#tableStaff", 1, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age, data.staff[i].idCardNo,
                    data.staff[i].qualification,'<input class="btn_delete" type="button" value="删除" onclick="btnDelete(this)" name="'+
                    data.staff[i].id+'">', '项目经理');
            }
            if (data.staff[i].type=='TECHNICAL_DIRECTOR') {
                $("#tableStaff").find("tr").eq(2).find(".txt_staff_info").show();
                $("#tableStaff").find("tr").eq(2).find(".btn_staff_info").hide();
                setTableRowContent("#tableStaff", 2, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,data.staff[i].idCardNo,
                    data.staff[i].qualification,'<input class="btn_delete" type="button" value="删除" onclick="btnDelete(this)" name="'+
                    data.staff[i].id+'">', "技术负责人");
            }
            if (data.staff[i].type=='CONSTRUCTION_WORKER') {
                $("#tableStaff").find("tr").eq(3).find(".txt_staff_info").show();
                $("#tableStaff").find("tr").eq(3).find(".btn_staff_info").hide();
                setTableRowContent("#tableStaff", 3, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,data.staff[i].idCardNo,
                    data.staff[i].qualification,'<input class="btn_delete" type="button" value="删除" onclick="btnDelete(this)" name="'+
                    data.staff[i].id+'">', "施工员");
            }
            if (data.staff[i].type=='QUALITY_WORKER') {
                $("#tableStaff").find("tr").eq(4).find(".txt_staff_info").show();
                $("#tableStaff").find("tr").eq(4).find(".btn_staff_info").hide();
                setTableRowContent("#tableStaff", 4, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,data.staff[i].idCardNo,
                    data.staff[i].qualification,'<input class="btn_delete" type="button" value="删除" onclick="btnDelete(this)" name="'+
                    data.staff[i].id+'">', "质量员");
            }
            if (data.staff[i].type=='SAFETY_WORKER') {
                $("#tableStaff").find("tr").eq(5).find(".txt_staff_info").show();
                $("#tableStaff").find("tr").eq(5).find(".btn_staff_info").hide();
                setTableRowContent("#tableStaff", 5, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,data.staff[i].idCardNo,
                    data.staff[i].qualification,'<input class="btn_delete" type="button" value="删除" onclick="btnDelete(this)" name="'+
                    data.staff[i].id+'">', "安全员");
            }
            if (data.staff[i].type=='BUDGETER') {
                $("#tableStaff").find("tr").eq(6).find(".txt_staff_info").show();
                $("#tableStaff").find("tr").eq(6).find(".btn_staff_info").hide();
                setTableRowContent("#tableStaff", 6, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,data.staff[i].idCardNo,
                    data.staff[i].qualification,'<input class="btn_delete" type="button" value="删除" onclick="btnDelete(this)" name="'+
                    data.staff[i].id+'">', "预算员");
            }
            if (data.staff[i].type=='MATERIALMAN') {
                $("#tableStaff").find("tr").eq(7).find(".txt_staff_info").show();
                $("#tableStaff").find("tr").eq(7).find(".btn_staff_info").hide();
                setTableRowContent("#tableStaff", 7, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,data.staff[i].idCardNo,
                    data.staff[i].qualification,'<input class="btn_delete" type="button" value="删除" onclick="btnDelete(this)" name="'+
                    data.staff[i].id+'">', "材料员");
            }
            if (data.staff[i].type=='MACHINIST') {
                $("#tableStaff").find("tr").eq(8).find(".txt_staff_info").show();
                $("#tableStaff").find("tr").eq(8).find(".btn_staff_info").hide();
                setTableRowContent("#tableStaff", 8, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,data.staff[i].idCardNo,
                    data.staff[i].qualification,'<input class="btn_delete" type="button" value="删除" onclick="btnDelete(this)" name="'+
                    data.staff[i].id+'">', "机械员");
            }
            if (data.staff[i].type=='LABOR_WORKER') {
                $("#tableStaff").find("tr").eq(9).find(".txt_staff_info").show();
                $("#tableStaff").find("tr").eq(9).find(".btn_staff_info").hide();
                setTableRowContent("#tableStaff", 9, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,data.staff[i].idCardNo,
                    data.staff[i].qualification,'<input class="btn_delete" type="button" value="删除" onclick="btnDelete(this)" name="'+
                    data.staff[i].id+'">', "劳务员");
            }
            if (data.staff[i].type=='DATA_PROCESSOR') {
                $("#tableStaff").find("tr").eq(10).find(".txt_staff_info").show();
                $("#tableStaff").find("tr").eq(10).find(".btn_staff_info").hide();
                setTableRowContent("#tableStaff", 10, data.staff[i].jobNo,
                    data.staff[i].name,  data.staff[i].age,data.staff[i].idCardNo,
                    data.staff[i].qualification,'<input class="btn_delete" type="button" value="删除" onclick="btnDelete(this)" name="'+
                    data.staff[i].id+'">', "资料员");
            }
        }
    }, function(data) {
    }, true, waiting, run, true);
}

/**
 * 选择报建人员按钮
 */
function btnChooseTeam(obj) {
    gotoLocalHtml("/project/establish/choose_person.html?project.id="
        + getUrlParam("project.id")+"&type=" + $(obj).attr("name"));
}

/**
 * 删除按钮
 */
function btnDelete(obj) {
    if (!isEmpty($(obj).attr("name"))) {
        if (confirm("确定删除该人员信息？")) {
            ajaxRemoveStaffFromProjectApplication(getUrlParam("project.id"), $(obj).attr("name"), function(data) {
                //刷新列表
                reload();
            }, function(data) {
            }, true, waiting, run, true);
        }
    }
}




