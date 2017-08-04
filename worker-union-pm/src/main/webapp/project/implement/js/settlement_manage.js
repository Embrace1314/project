
var PAGE_SIZE = 12;

/**
 * 页面初始调用
 **/
$(function () {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
    //显示项目名称
    showProject();
});

/**
 * 显示项目名称
 **/
function showProject(){
    ajaxGetProjectDetailById(getUrlParam("project.id"), function (data) {
    	showProjectTitle(data.project);
    }, function (data) {
    }, true, waiting, run, true);
    //显示附件
    showAttachment();
    //显示结算管理详情
    showSettlementDetail();
}

/**
 * 显示附件
 **/
function showAttachment(){
    ajaxGetLatestSettlementAttachment(getUrlParam("project.id"), function (data) {
        if (data.hasAttachment) {
            $("#txtAttachmentId").val(data.attachment.id);
            $("#txtFileName").html(data.attachment.name);
            $("#txtCreatorName").html(data.attachment.staffName);
            $("#txtCreateTime").html(data.attachment.cstCreate);
        }
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
            ajaxAcquireSettlementUpToken(getUrlParam("project.id"), function (data) {
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
uploader.init();

/**
 *下载结算管理文件
 */
function btnDownloadFile() {
    // 获取授权信息
    ajaxAcquireSettlementDownToken(getUrlParam("project.id"), $("#txtAttachmentId").val(),
        function(data) {
			window.location = data.url;
        }, function() {
        }, true, waiting, run, true);
}

/**
 * 显示结算管理详情
 **/
function showSettlementDetail(){
    // 分页参数
    var pagination = $("#pagination").html();
    // 清空页码
    $("#pagination").html("");
    $("#totalPage").html("");

    ajaxPageSettlementItem(getUrlParam("project.id"),{
        "pagination": pagination,
        "size": PAGE_SIZE
    }, function (data) {
        // 设置 页码
        $("#pagination").html(data.page.pagination);
        $("#totalPage").html(data.page.totalPage);

        clearTableTr("#tableSettlement");
        for (var i = 0; i < PAGE_SIZE; i++) {
            if (i < data.page.list.length) {
                setTableRowContent("#tableSettlement", i+1, data.page.list[i].id,
                    data.page.list[i].year, data.page.list[i].month,
                    data.page.list[i].purchaseItemName, data.page.list[i].resourceImplementItemName,
                    data.page.list[i].resourceImplementItemMoney, data.page.list[i].subcontractorName,
                    data.page.list[i].money, data.page.list[i].sumMoney);
                setRowOnClickSelected("#tableSettlement", i+1, true);
            } else {
                setTableRowContent("#tableSettlement", i+1, "", "", "", "", "", "", "",
                    "", "");
                setRowOnClickSelected("#tableSettlement", i+1, false);
            }
        }
    }, function (data) {
    }, true, waiting, run, true);
}

/**
 * 添加按钮
 */
function btnToAddSettlement() {
    gotoLocalHtml('/project/implement/settlement_manage_add.html?project.id='
        + getUrlParam('project.id'));
}

/**
 * 修改按钮
 */
function btnToModifySettlement() {
    var settlementItemId = getSelectedTableTrValue("#tableSettlement");
    if(!isEmpty(settlementItemId)){
        gotoLocalHtml('/project/implement/settlement_manage_modify.html?project.id='
            + getUrlParam('project.id') + '&settlementItem.id=' + settlementItemId);
    }else {
        alert("请先选择一条结算管理信息");
    }
}

/**
 * 删除按钮
 */
function btnDeleteSettlementItem() {
    var settlementItemId = getSelectedTableTrValue("#tableSettlement");
    if (!isEmpty(settlementItemId)){
        if(confirm("确定删除该结算管理项？")){
            ajaxRemoveSettlementItemById(getUrlParam("project.id"), settlementItemId, function(data){
                // 刷新结算管理详情
                showSettlementDetail();
            }, function(data){}, true, waiting, run, true);
        }
    }
}

/**
 * 点击上一页
 */
function btnPrevPage() {
    $("#pagination").html(Math.max(parseInt($("#pagination").html()) - 1, 1));
    showSettlementDetail();
}

/**
 * 点击下一页
 */
function btnNextPage() {
    $("#pagination").html(Math.max(parseInt($("#pagination").html()) + 1, 1));
    showSettlementDetail();
}