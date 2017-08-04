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
    //显示产值详情
    showCapacityDetail();
}

/**
 * 显示附件
 **/
function showAttachment(){
    ajaxGetLatestBusinessCapacityAttachment(getUrlParam("project.id"), function (data) {
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
            ajaxAcquireBusinessCapacityFileUpToken(getUrlParam("project.id"), function (data) {
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
 *下载经营管理产值文件
 */
function btnDownloadFile() {
    // 获取授权信息
    ajaxAcquireBusinessCapacityFileDownToken(getUrlParam("project.id"), $("#txtAttachmentId").val(),
        function(data) {
			window.location = data.url;
        }, function() {
        }, true, waiting, run, true);
}

/**
 * 显示产值详情
 **/
function showCapacityDetail(){
    // 分页参数
    var pagination = $("#pagination").html();
    // 清空页码
    $("#pagination").html("");
    $("#totalPage").html("");

    ajaxPageBusinessCapacityItem(getUrlParam("project.id"),{
        "pagination": pagination,
        "size": PAGE_SIZE
    }, function (data) {
        // 设置 页码
        $("#pagination").html(data.page.pagination);
        $("#totalPage").html(data.page.totalPage);

        clearTableTr("#tableCapacity");
        for (var i = 0; i < PAGE_SIZE; i++) {
            if (i < data.page.list.length) {
                setTableRowContent("#tableCapacity", i+1, data.page.list[i].id,
                    data.page.list[i].year, data.page.list[i].month,
                    data.page.list[i].productValue, data.page.list[i].checkedValue,
                    data.page.list[i].cstCreate.substring(0, 10), data.page.list[i].staffName,
                    data.page.list[i].sumValue);
                setRowOnClickSelected("#tableCapacity", i+1, true);
            } else {
                setTableRowContent("#tableCapacity", i+1, "", "", "", "", "", "", "",
                    "");
                setRowOnClickSelected("#tableCapacity", i+1, false);
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
 * 显示当月产值添加弹窗
 */
function btnToAddDialog() {
    //显示当月产值添加弹窗
    $("#dialogAdd").show();
}

/**
 * 修改按钮
 * 显示当月产值修改弹窗
 */
function btnToModifyDialog() {
    var capacityItemId = getSelectedTableTrValue("#tableCapacity");
    if (!isEmpty(capacityItemId)) {
        //显示当月产值修改弹窗
        $("#dialogModify").show();
        // 保存当前选择的经营产值项ID
        $("#txtCapacityItemId").val(capacityItemId);
        $("#txtYearModify").html($("#tableCapacity").find("tr.list_tr_selected").first().children("td").eq(1).html());
        $("#txtMonthModify").html($("#tableCapacity").find("tr.list_tr_selected").first().children("td").eq(2).html());
        $("#txtCapacityModify").val($("#tableCapacity").find("tr.list_tr_selected").first().children("td").eq(3).html());
    }
}

/**
 * 清空弹窗经营产值项编辑框
 */
function clearCapacityItem(){
    $("#selectYear").val("");
    $("#selectMonth").val("");
    $("#txtCapacityAdd").val("");
}

/**
 * 关闭弹窗
 */
function closeDialog() {
    //清空弹窗编辑框
    clearCapacityItem();
    $("#dialogAdd").hide();
    $("#dialogModify").hide();
}

/**
 * 添加弹窗
 * 保存
 */
function btnSaveCapacityItem() {
	if(!preventRapidClick()){
		return;
	}
    var projectId = getUrlParam("project.id");
    var year = $("#selectYear").val();
    var month = $("#selectMonth").val();
    var capacity = $("#txtCapacityAdd").val();

    if (isEmpty(projectId)){
        alert("无效的项目");
        return;
    }
    if (!verifyNotEmpty(year,"产值填报年份",true)){
        return;
    }
    if (!verifyNotEmpty(month,"产值填报月份",true)){
        return;
    }
    if (!verifyMoney(capacity,"当月产值",true)){
        return;
    }
    var capacityItem = {
        projectId : projectId,
        year : year,
        month : month,
        capacity : capacity
    };
    ajaxAddBusinessCapacityItem(capacityItem, function(data){
        closeDialog();
        // 刷新产值详情信息
        showCapacityDetail();
    }, function(data){}, true, waiting, run, true);
}

/**
 * 修改弹窗
 * 保存
 */
function btnModifyCapacityItem() {
    var projectId = getUrlParam("project.id");
    var id = $("#txtCapacityItemId").val();
    var capacity = $("#txtCapacityModify").val();

    if (isEmpty(projectId)){
        alert("无效的项目");
        return;
    }
    if (!verifyMoney(capacity,"当月产值",true)){
        return;
    }
    var capacityItem = {
        projectId : projectId,
        id : id,
        capacity : capacity
    };
    ajaxModifyBusinessCapacityItem(capacityItem, function(data){
        closeDialog();
        // 刷新产值详情信息
        showCapacityDetail();
    }, function(data){}, true, waiting, run, true);
}

/**
 * 删除按钮
 * 删除选中的经营产值项
 */
function btnDeleteCapacityItem() {
    var capacityItemId = getSelectedTableTrValue("#tableCapacity");
    if (!isEmpty(capacityItemId)){
        if(confirm("确定删除该经营管理产值项？")){
            ajaxRemoveBusinessCapacityItem(getUrlParam("project.id"), capacityItemId, function(data){
                // 刷新产值详情信息
                showCapacityDetail();
            }, function(data){}, true, waiting, run, true);
        }
    }
}

/**
 * 点击上一页
 */
function btnPrevPage() {
    $("#pagination").html(Math.max(parseInt($("#pagination").html()) - 1, 1));
    showCapacityDetail();
}

/**
 * 点击下一页
 */
function btnNextPage() {
    $("#pagination").html(Math.max(parseInt($("#pagination").html()) + 1, 1));
    showCapacityDetail();
}