
/**
 * 页面启动调用
 */
$(function() {
    $("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
    // 显示项目名称
    showProject();
});

/**
 * 项目归档
 */
function btnArchive() {
    ajaxArchiveProjectById(getUrlParam("project.id"), function(data) {
        alert("归档成功！");
        $("#btnArchive").hide();
        $("#btnRemoveArchive").show();
    }, function(data) {
    }, true, waiting, run, true);
}

/**
 * 项目解除归档
 */
function btnRemoveArchive() {
    ajaxReleaseArchiveProjectById(getUrlParam("project.id"), function(data) {
        alert("解除归档成功！");
        $("#btnArchive").show();
        $("#btnRemoveArchive").hide();
    }, function(data) {
    }, true, waiting, run, true);
}

/**
 * 显示项目名称
 */
function showProject() {
    ajaxGetProjectDetailById(getUrlParam("project.id"), function(data) {
        showProjectTitle(data.project);
        $("#txtProjectName").html(data.project.num + data.project.name);
        if(PROJECT_TYPE_GOING == data.project.fileStatus){
            $("#btnArchive").show();
            $("#btnRemoveArchive").hide();
        }
        if(PROJECT_TYPE_ARCHIVED == data.project.fileStatus){
            $("#btnArchive").hide();
            $("#btnRemoveArchive").show();
        }
        //显示竣工结算信息
        showCompleteInfo();
    }, function(data) {
    }, true, waiting, run, true);
}

/**
 * 显示竣工结算信息
 */
function showCompleteInfo() {
    ajaxListFinalSettlementItem(getUrlParam("project.id"), function(data) {
        removeTableTr("#tableComplete");
        for (var i = 0; i < data.finalSettlementItems.length; i++) {
            var settlementItemId = data.finalSettlementItems[i].id;
            var attachment = data.finalSettlementItems[i].hasAttachment?'<div class="div_verify_btn"><button class="btn_pass" type="button" onclick="btnDownloadFile(this)">下载</button><div class="div_line"></div>'
            +'<button class="btn_rebut" type="button" onclick="btnDeleteFile(this)">删除</button></div>':'<a href="#" id="attachment'+ settlementItemId +'">上传附件</a>';
            var j = i+1;
            $("#tableComplete").append(
                '<tr class="list_mid_row" onclick="btnSelectTableTr(this)"><td hidden="true">'+ settlementItemId
                +'</td><td>'+ j
                +'</td><td>' + data.finalSettlementItems[i].subprojectName
                +'</td><td>' + data.finalSettlementItems[i].submitMoney
                +'</td><td>' + data.finalSettlementItems[i].checkedMoney
                +'</td><td>' + data.finalSettlementItems[i].decreasedMoney
                +'</td><td>' + data.finalSettlementItems[i].increasedMoney
                +'</td><td class="list_last_col">'+ attachment
                +'</td></tr>');
            btnUploadFile(settlementItemId);
        }
        $("#tableComplete").append(
            '<tr class="list_last_row" onclick="clearTableSelectStatus(this)"><td>'+ "合计"
            +'</td><td>'+ ""
            +'</td><td>' + data.sumSubmitMoney
            +'</td><td>' + data.sumCheckedMoney
            +'</td><td class="list_col_color_yellow">' + data.sumDecreasedMoney
            +'</td><td  class="list_col_color_yellow">' + data.sumIncreasedMoney
            +'</td><td class="list_last_col"></td></tr>');
    }, function(data) {
    }, true, waiting, run, true);
}

/**
 * 上传附件
 */
function btnUploadFile(obj) {
// 上传工具的参数
    var uploaderParams = {
        runtimes : 'html5,flash,silverlight,html4',
        browse_button : "attachment"+obj,
        multi_selection : false,
        multipart : true,
        url : URL_OSS,
        flash_swf_url : getContextRoot() + '/common/plupload/Moxie.swf',
        silverlight_xap_url : getContextRoot() + '/common/plupload/Moxie.xap',
        init : {

            // 上传绑定事件-添加文件
            FilesAdded : function(uploader, files) {
                // 开始上传
                ajaxAcquireFinalSettlementItemAttachmentUpToken(getUrlParam("project.id"),obj, function(
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
                        //刷新列表
                        showCompleteInfo();
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
    var uploader = new plupload.Uploader(uploaderParams);
    uploader.init();
}

/**
 *下载附件
 */
function btnDownloadFile(obj) {
    // 获取授权信息
    var attachmentId = $(obj).parent().parent().parent().children("td").first().html();
    ajaxAcquireFinalSettlementAttachmentDownToken(getUrlParam("project.id"), attachmentId,
        function(data) {
    		window.location = data.url;
        }, function() {
        }, true, waiting, run, true);
}

/**
 * 删除附件
 */
function btnDeleteFile(obj) {
    var attachmentId = $(obj).parent().parent().parent().children("td").first().html();
    if (!isEmpty(attachmentId)) {
        if (confirm("确定删除该附件？")) {
            ajaxRemoveFinalSettlementItemAttachmentById(getUrlParam("project.id"), attachmentId,
                function(data) {
                    //刷新列表
                    showCompleteInfo();
                }, function(data) {
                }, true, waiting, run, true);
        }
    }
}



/**
 * 添加按钮
 * 显示结算详细添加弹窗
 */
function btnToAddDialog() {
    //显示结算详细添加弹窗
    $("#dialogAdd").show();
}

/**
 * 修改按钮
 * 显示结算详细修改弹窗
 */
function btnToModifyDialog() {
    var settlementItemId = getSelectedTableTrValue("#tableComplete");
    if (!isEmpty(settlementItemId)){
        ajaxGetFinalSettlementItemById(getUrlParam("project.id"), settlementItemId, function(data){
            //显示结算详细修改弹窗
            $("#dialogModify").show();
            // 保存当前选择的结算详细项ID
            $("#txtSettlementItemId").html(settlementItemId);
            $("#nameModify").val(data.finalSettlementItem.subprojectName);
            $("#submitMoneyModify").val(data.finalSettlementItem.submitMoney);
            $("#checkedMoneyModify").val(data.finalSettlementItem.checkedMoney);
            $("#increasedMoneyModify").val(data.finalSettlementItem.increasedMoney);
            $("#decreasedMoneyModify").val(data.finalSettlementItem.decreasedMoney);
        }, function(data){}, true, waiting, run, true);
    }
}

/**
 * 清空弹窗结算详细项编辑框
 */
function clearSettlementItem(){
    $("#nameAdd").val("");
    $("#submitMoneyAdd").val("");
    $("#checkedMoneyAdd").val("");
    $("#increasedMoneyAdd").val("");
    $("#decreasedMoneyAdd").val("");
}

/**
 * 关闭弹窗
 */
function closeDialog() {
    //清空弹窗编辑框
    clearSettlementItem();
    $("#dialogAdd").hide();
    $("#dialogModify").hide();
}

/**
 * 添加弹窗
 * 保存
 */
function btnSaveItem() {
	if(!preventRapidClick()){
		return;
	}
    var projectId = getUrlParam("project.id");
    var name = $("#nameAdd").val();
    var submitMoney = $("#submitMoneyAdd").val();
    var checkedMoney = $("#checkedMoneyAdd").val();
    var increasedMoney = $("#increasedMoneyAdd").val();
    var decreasedMoney = $("#decreasedMoneyAdd").val();

    if (isEmpty(projectId)){
        alert("无效的项目");
        return;
    }
    if (!verifyStringNotEmpty(name, "单位工程名称",512,true)){
        return;
    }
    if (!verifyMoney(submitMoney, "送审金额",true)){
        return;
    }
    if (!verifyMoney(checkedMoney, "核定金额",true)){
        return;
    }
    if (!verifyMoney(increasedMoney, "核增金额",true)){
        return;
    }
    if (!verifyMoney(decreasedMoney, "核减金额",true)){
        return;
    }
    var settlementItem = {
        subprojectName : name,
        submitMoney : submitMoney,
        checkedMoney : checkedMoney,
        increasedMoney : increasedMoney,
        decreasedMoney : decreasedMoney
    };
    ajaxAddFinalSettlementItem(projectId,settlementItem, function(data){
        closeDialog();
        // 刷新结算详细信息
        showCompleteInfo();
    }, function(data){}, true, waiting, run, true);
}

/**
 * 修改弹窗
 * 保存
 */
function btnModifyItem() {
    var projectId = getUrlParam("project.id");

    var id = $("#txtSettlementItemId").html();
    var name = $("#nameModify").val();
    var submitMoney = $("#submitMoneyModify").val();
    var checkedMoney = $("#checkedMoneyModify").val();
    var increasedMoney = $("#increasedMoneyModify").val();
    var decreasedMoney = $("#decreasedMoneyModify").val();

    if (isEmpty(projectId)){
        alert("无效的项目");
        return;
    }
    if (!verifyStringNotEmpty(name, "单位工程名称",512,true)){
        return;
    }
    if (!verifyMoney(submitMoney, "送审金额",true)){
        return;
    }
    if (!verifyMoney(checkedMoney, "核定金额",true)){
        return;
    }
    if (!verifyMoney(increasedMoney, "核增金额",true)){
        return;
    }
    if (!verifyMoney(decreasedMoney, "核减金额",true)){
        return;
    }
    var settlementItem = {
        id : id,
        subprojectName : name,
        submitMoney : submitMoney,
        checkedMoney : checkedMoney,
        increasedMoney : increasedMoney,
        decreasedMoney : decreasedMoney
    };
    ajaxModifyFinalSettlementItemById(projectId,settlementItem, function(data){
        closeDialog();
        // 刷新结算详细信息
        showCompleteInfo();
    }, function(data){}, true, waiting, run, true);
}

/**
 * 删除按钮
 * 删除选中的结算详细项
 */
function btnDeleteItem() {
    var settlementItemId = getSelectedTableTrValue("#tableComplete");
    if (!isEmpty(settlementItemId)){
        if(confirm("确定删除该结算详细项？")){
            ajaxRemoveFinalSettlementItemById(getUrlParam("project.id"), settlementItemId, function(data){
                // 刷新结算详细信息
                showCompleteInfo();
            }, function(data){}, true, waiting, run, true);
        }
    }
}
