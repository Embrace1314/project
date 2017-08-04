
/**
 * 页面初始调用
 **/
$(function () {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
    // 初始化select的成本项类型
    initCostDeepenItemType();
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
    //显示成本深化表
    showCostDeepenFile();
}

/**
 * 显示成本深化表
 */
function showCostDeepenFile() {
    ajaxGetCostAnalysisDeepenUniqueAttachment(getUrlParam("project.id"), function (data) {
        if(data.hasAttachment){
            //显示删除按钮
            $("#btnDeleteFile")[0].style.display = 'block';
            $("#btnUpload")[0].style.display = 'none';
            //显示成本深化表
            $("#txtAttachmentId").val(data.attachment.id);
            $("#txtFileName").html(data.attachment.name);
            $("#txtCreatorName").html(data.attachment.staffName);
            $("#txtCreateTime").html(data.attachment.cstCreate);
        }else {
            //显示上传按钮
            $("#btnDeleteFile")[0].style.display = 'none';
            $("#btnUpload")[0].style.display = 'block';
            //显示成本深化表
            $("#txtAttachmentId").val("");
            $("#txtFileName").html("");
            $("#txtCreatorName").html("");
            $("#txtCreateTime").html("");
        }
        //显示成本深化详情
        showCostDeepenDetail();
    }, function (data) {
    }, true, waiting, run, true);
}

/**
 * 显示成本深化详情
 **/
function showCostDeepenDetail() {
    ajaxAcquireCostAnalysisDeepenDetail(getUrlParam("project.id"), function(data) {
        $("#txtDirectCostSum").html(data.directCostSum);
        $("#txtCostSum").html(data.costSum);
        $("#txtPretaxSum").html(data.pretaxSum);
        $("#txtProfitSum").html(data.profitSum);
        $("#txtSubcontractCostSum").html(data.subcontractCostSum);
        $("#txtIndirectCostSum").html(data.indirectCostSum);
        showCostDeepenItems(data.costAnalysisItemDeepens);
    }, function() {
    }, true, waiting, run, true);
}

/**
 * 显示成本深化项列表
 */
function showCostDeepenItems(costAnalysisItemDeepens) {
    removeTableTr("#tableCost");
    for (var i = 0; i < costAnalysisItemDeepens.length; i++) {
        $("#tableCost").append(
            '<tr class="list_mid_row" onclick="btnSelectTableTr(this)"><td hidden="true">'
            + costAnalysisItemDeepens[i].id + '</td><td>'
            + COST_ANALYSIS_ITEM_TYPE[costAnalysisItemDeepens[i].type]
            + '</td><td>' + costAnalysisItemDeepens[i].name + '</td><td>'
            + costAnalysisItemDeepens[i].price + '</td><td>'
            + costAnalysisItemDeepens[i].amount + '</td><td>'
            + costAnalysisItemDeepens[i].unit + '</td><td>'
            + costAnalysisItemDeepens[i].cost
            + '</td><td class="list_last_col">'
            + costAnalysisItemDeepens[i].memo + '</td></tr>');
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
            ajaxAcquireCostDeepenUpToken(getUrlParam("project.id"), function (data) {
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
                    //刷新页面
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
 *下载成本深化表
 */
function btnDownloadFile() {
    // 获取授权信息
    ajaxAcquireCostDeepenDownToken(getUrlParam("project.id"), $("#txtAttachmentId").val(),
        function(data) {
			window.location = data.url;
        }, function() {
        }, true, waiting, run, true);
}

/**
 *删除成本深化表
 */
function btnDeleteFile() {
    if (confirm("确定删除该附件？")) {
        ajaxRemoveCostAnalysisDeepenUniqueAttachment(getUrlParam("project.id"), $("#txtAttachmentId").val(),
            function (data) {
            // 刷新页面
            showCostDeepenFile();
        }, function (data) {
        }, true, waiting, run, true);
    }
}


/**
 * 初始化select的成本项类型
 */
function initCostDeepenItemType() {
    for ( var key in COST_ANALYSIS_ITEM_TYPE) {
        $("#txtTypeModify").append(
            "<option value='" + key + "'>" + COST_ANALYSIS_ITEM_TYPE[key]
            + "</option>");
        $("#txtTypeAdd").append(
            "<option value='" + key + "'>" + COST_ANALYSIS_ITEM_TYPE[key]
            + "</option>");
    }
}

/**
 * 添加按钮
 * 显示成本项添加弹窗
 */
function btnToAddDialog() {
    //显示成本项添加弹窗
    $("#dialogAdd").show();
}

/**
 * 修改按钮
 * 显示成本项修改弹窗
 */
function btnToModifyDialog() {
    var costDeepenItemId = getSelectedTableTrValue("#tableCost");
    if (!isEmpty(costDeepenItemId)){
        ajaxGetCostAnalysisItemDeepenById(getUrlParam("project.id"), costDeepenItemId, function(data){
            //显示成本项修改弹窗
            $("#dialogModify").show();
            // 保存当前选择的成本分析项ID
            $("#txtCostDeepenItemId").html(costDeepenItemId);
            $("#txtTypeModify").val(data.costAnalysisItemDeepen.type);
            $("#txtNameModify").val(data.costAnalysisItemDeepen.name);
            $("#txtPriceModify").val(data.costAnalysisItemDeepen.price);
            $("#txtAmountModify").val(data.costAnalysisItemDeepen.amount);
            $("#txtUnitModify").val(data.costAnalysisItemDeepen.unit);
            $("#txtCostModify").html(data.costAnalysisItemDeepen.cost);
            $("#txtMemoModify").val(data.costAnalysisItemDeepen.memo);
        }, function(data){}, true, waiting, run, true);
    }
}

/**
 * 清空弹窗成本项编辑框
 */
function clearCostDeepenItem(){
    $("#txtTypeAdd").val("");
    $("#txtNameAdd").val("");
    $("#txtPriceAdd").val("");
    $("#txtAmountAdd").val("");
    $("#txtUnitAdd").val("");
    $("#txtMemoAdd").val("");
}

/**
 * 关闭弹窗
 */
function closeDialog() {
    //清空弹窗编辑框
    clearCostDeepenItem();
    $("#dialogAdd").hide();
    $("#dialogModify").hide();
}

/**
 * 添加弹窗
 * 保存
 */
function btnSaveCostItem() {
	if(!preventRapidClick()){
		return;
	}
    var projectId = getUrlParam("project.id");
    var type = $("#txtTypeAdd").val();
    var name = $("#txtNameAdd").val();
    var price = $("#txtPriceAdd").val();
    var amount = $("#txtAmountAdd").val();
    var unit = $("#txtUnitAdd").val();
    var memo = $("#txtMemoAdd").val();
    if (isEmpty(projectId)){
        alert("无效的项目");
        return;
    }
    if (!verifyNotEmpty(type,"成本类型",true)){
        return;
    }
    if (!verifyStringNotEmpty(name,"成本项",32,true)){
        return;
    }
    if (!verifyMoney(price,"单价",true)){
        return;
    }
    if (!verifyAmount(amount,"数量",true)){
        return;
    }
    if (!verifyStringNotEmpty(unit,"计量单位",32,true)){
        return;
    }
    if (!verifyString(memo,"备注",1024,true)){
        return;
    }

    if(!verifyMoney(multiply(price,amount),"合计值",true)){
        return;
    }
    var costDeepenItem = {
        projectId : projectId,
        type : type,
        name : name,
        memo : memo,
        price : price,
        amount : amount,
        unit : unit
    };
    ajaxAddCostAnalysisItemDeepen(costDeepenItem, function(data){
        closeDialog();
        // 刷新成本深化信息
        showCostDeepenDetail();
    }, function(data){}, true, waiting, run, true);
}

/**
 * 修改弹窗
 * 保存
 */
function btnModifyCostItem() {
    var projectId = getUrlParam("project.id");
    var id = $("#txtCostDeepenItemId").html();
    var type = $("#txtTypeModify").val();
    var name = $("#txtNameModify").val();
    var price = $("#txtPriceModify").val();
    var amount = $("#txtAmountModify").val();
    var unit = $("#txtUnitModify").val();
    var memo = $("#txtMemoModify").val();

    if (isEmpty(projectId)){
        alert("无效的项目");
        return;
    }
    if (!verifyNotEmpty(type,"成本类型",true)){
        return;
    }
    if (!verifyStringNotEmpty(name,"成本项",32,true)){
        return;
    }
    if (!verifyMoney(price,"单价",true)){
        return;
    }
    if (!verifyAmount(amount,"数量",true)){
        return;
    }
    if (!verifyStringNotEmpty(unit,"计量单位",32,true)){
        return;
    }
    if (!verifyString(memo,"备注",1024,true)){
        return;
    }
    if(!verifyMoney(multiply(price, amount),"合计值",true)){
        return;
    }
    var costDeepenItem = {
        projectId : projectId,
        id : id,
        type : type,
        name : name,
        memo : memo,
        price : price,
        amount : amount,
        unit : unit
    };
    ajaxModifyCostAnalysisItemDeepenById(costDeepenItem, function(data){
        closeDialog();
        // 刷新成本深化信息
        showCostDeepenDetail();
    }, function(data){}, true, waiting, run, true);
}

/**
 * 删除按钮
 * 删除选中的成本深化项
 */
function btnDeleteCostDeepenItem() {
    var costDeepenItemId = getSelectedTableTrValue("#tableCost");
    if (!isEmpty(costDeepenItemId)){
        if(confirm("确定删除该成本深化项？")){
            ajaxRemoveCostAnalysisItemDeepenById(getUrlParam("project.id"), costDeepenItemId, function(data){
                // 刷新成本深化信息
                showCostDeepenDetail();
            }, function(data){}, true, waiting, run, true);
        }
    }
}


