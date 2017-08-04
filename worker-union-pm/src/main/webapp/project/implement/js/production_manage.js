
var PAGE_SIZE = 12;
var JANUARY = 1;
var DECEMBER = 12;

/**
 * 页面初始调用
 **/
$(function () {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
    //显示项目名称
    showProject();
    //初始化select下拉框
    initSelect();
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
    //显示生产进度详情
    showProductionDetail();
}

/**
 * 显示附件
 **/
function showAttachment(){
    ajaxGetLatestProductCapacityAttachment(getUrlParam("project.id"), function (data) {
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
            ajaxAcquireProductCapacityFileUpToken(getUrlParam("project.id"), function (data) {
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
 *下载生产管理产值文件
 */
function btnDownloadFile() {
    // 获取授权信息
    ajaxAcquireProductCapacityFileDownToken(getUrlParam("project.id"), $("#txtAttachmentId").val(),
        function(data) {
			window.location = data.url;
        }, function() {
        }, true, waiting, run, true);
}

/**
 * 显示生产进度详情
 **/
function showProductionDetail(){
    // 分页参数
    var pagination = $("#pagination").html();
    // 清空页码
    $("#pagination").html("");
    $("#totalPage").html("");

    ajaxPageProductCapacityItem(getUrlParam("project.id"),{
        "pagination": pagination,
        "size": PAGE_SIZE
    }, function (data) {
        // 设置 页码
        $("#pagination").html(data.page.pagination);
        $("#totalPage").html(data.page.totalPage);

        clearTableTr("#tableProduction");
        for (var i = 0; i < PAGE_SIZE; i++) {
            if (i < data.page.list.length) {
                setTableRowContent("#tableProduction", i+1, data.page.list[i].id,
                    data.page.list[i].year, data.page.list[i].month,
                    data.page.list[i].expectedState, data.page.list[i].actualState,
                    data.page.list[i].cstCreate.substring(0, 10), data.page.list[i].staffName,
                    data.page.list[i].checkedState);
                setRowOnClickSelected("#tableProduction", i+1, true);
            } else {
                setTableRowContent("#tableProduction", i+1, "", "", "", "", "", "", "",
                    "");
                setRowOnClickSelected("#tableProduction", i+1, false);
            }
        }
    }, function (data) {
    }, true, waiting, run, true);
}

/**
 * 初始化select下拉框
 */
function initSelect() {
    //设置年份的选择
    var myDate = new Date();
    var startYear = myDate.getFullYear() - 20;//起始年份
    var endYear = myDate.getFullYear();//结束年份
    var year = document.getElementById('selectYear');
    for (var i = endYear; i >= startYear; i--) {
        year.options.add(new Option(i, i));
    }
    year.options[0].selected = 1;

    //设置月份的选择
    var month = document.getElementById('selectMonth');
    for (var j = JANUARY; j <= DECEMBER; j++) {
        month.options.add(new Option(j, j));
    }
    month.options[0].selected = 1;
}

/**
 * 添加按钮
 * 显示实际进度添加弹窗
 */
function btnToAddDialog() {
    //显示实际进度添加弹窗
    $("#dialogAdd").show();
}

/**
 * 修改按钮
 * 显示实际进度修改弹窗
 */
function btnToModifyDialog() {
    var productionItemId = getSelectedTableTrValue("#tableProduction");
    if (!isEmpty(productionItemId)) {
        //显示实际进度修改弹窗
        $("#dialogModify").show();
        // 保存当前选择的生产产值项ID
        $("#txtProductionItemId").val(productionItemId);
        $("#txtYearModify").html($("#tableProduction").find("tr.list_tr_selected").first().children("td").eq(1).html());
        $("#txtMonthModify").html($("#tableProduction").find("tr.list_tr_selected").first().children("td").eq(2).html());
        $("#txtExpectedStateModify").html($("#tableProduction").find("tr.list_tr_selected").first().children("td").eq(3).html());
        $("#txtScheduleModify").val($("#tableProduction").find("tr.list_tr_selected").first().children("td").eq(4).html());
    }
}

/**
 * 清空弹窗生产产值项编辑框
 */
function clearProductionItem(){
    $("#selectYear").val("");
    $("#selectMonth").val("");
    $("#txtExpectedState").val("");
    $("#txtScheduleAdd").val("");

    $("#txtYearModify").html("");
    $("#txtMonthModify").html("");
    $("#txtExpectedStateModify").html("");
    $("#txtScheduleModify").val("");
}

/**
 * 关闭弹窗
 */
function closeDialog() {
    //清空弹窗编辑框
    clearProductionItem();
    $("#dialogAdd").hide();
    $("#dialogModify").hide();
}

/**
 * 添加弹窗
 * 保存
 */
function btnSaveProductionItem() {
	if(!preventRapidClick()){
		return;
	}
    var projectId = getUrlParam("project.id");
    var year = $("#selectYear").val();
    var month = $("#selectMonth").val();
    var ExpectedState = $("#txtExpectedState").val();
    var schedule = $("#txtScheduleAdd").val();

    if (isEmpty(projectId)){
        alert("无效的项目");
        return;
    }
    if (!verifyNotEmpty(year,"进度填报年份",true)){
        return;
    }
    if (!verifyNotEmpty(month,"进度填报月份",true)){
        return;
    }
    if (!verifyStringNotEmpty(ExpectedState,"预计进度",1024,true)){
        return;
    }
    if (!verifyStringNotEmpty(schedule,"实际进度",1024,true)){
        return;
    }
    var productionItem = {
        projectId : projectId,
        year : year,
        month : month,
        expectedState : ExpectedState,
        schedule : schedule
    };
    ajaxAddProductCapacityItem(productionItem, function(data){
        closeDialog();
        // 刷新生产进度详情
        showProductionDetail();
    }, function(data){}, true, waiting, run, true);
}

/**
 * 修改弹窗
 * 保存
 */
function btnModifyProductionItem() {
    var projectId = getUrlParam("project.id");
    var id = $("#txtProductionItemId").val();
    var schedule = $("#txtScheduleModify").val();

    if (isEmpty(projectId)){
        alert("无效的项目");
        return;
    }
    if (!verifyStringNotEmpty(schedule,"实际进度",1024,true)){
        return;
    }
    var productionItem = {
        projectId : projectId,
        id : id,
        schedule : schedule
    };
    ajaxModifyProductCapacityItem(productionItem, function(data){
        closeDialog();
        // 刷新生产进度详情
        showProductionDetail();
    }, function(data){}, true, waiting, run, true);
}

/**
 * 删除按钮
 * 删除选中的生产进度项
 */
function btnDeleteProductionItem() {
    var productionItemId = getSelectedTableTrValue("#tableProduction");
    if (!isEmpty(productionItemId)){
        if(confirm("确定删除该生产进度项？")){
            ajaxRemoveProductCapacityItem(getUrlParam("project.id"), productionItemId, function(data){
                // 刷新生产进度详情
                showProductionDetail();
            }, function(data){}, true, waiting, run, true);
        }
    }
}

/**
 * 点击上一页
 */
function btnPrevPage() {
    $("#pagination").html(Math.max(parseInt($("#pagination").html()) - 1, 1));
    showProductionDetail();
}

/**
 * 点击下一页
 */
function btnNextPage() {
    $("#pagination").html(Math.max(parseInt($("#pagination").html()) + 1, 1));
    showProductionDetail();
}