
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
    //显示生产进度详情
    showProductionDetail();
}

/**
 * 显示附件
 **/
function showAttachment(){
    ajaxGetLatestProductCapacityAttachment(getUrlParam("project.id"), function (data) {
        if (data.hasAttachment) {
            //显示下载按钮
            $("#btnDownload").show();
            $("#btnDownloadDisable").hide();
            //显示附件表格
            $("#txtAttachmentId").val(data.attachment.id);
            $("#txtFileName").html(data.attachment.name);
            $("#txtCreatorName").html(data.attachment.staffName);
            $("#txtCreateTime").html(data.attachment.cstCreate);
        }else {
            //显示下载按钮不可点击状态
            $("#btnDownload").hide();
            $("#btnDownloadDisable").show();
            //显示附件表格
            $("#txtAttachmentId").val("");
            $("#txtFileName").html("");
            $("#txtCreatorName").html("");
            $("#txtCreateTime").html("");
        }
    }, function (data) {
    }, true, waiting, run, true);

}

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
                    data.page.list[i].cstCreate.substring(0,10), data.page.list[i].staffName,
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
 * 保存核定进度
 */
function btnSave() {
    var productionItemId = getSelectedTableTrValue("#tableProduction");
    var checkedValue = $("#txtSchedule").val();
    if(isEmpty(productionItemId)){
        alert("请先选择一条生产管理信息");
        return;
    }
    if (!verifyStringNotEmpty(checkedValue, "核定进度",1024,true)){
        return;
    }
    var productCapacityItem = {
        id : productionItemId,
        checkedValue : checkedValue
    };
    ajaxCheckProductCapacityItem(getUrlParam("project.id"), productCapacityItem, function(data){
        $("#txtSchedule").val("");
        // 刷新产值详情信息
        showProductionDetail();
    }, function(data){}, true, waiting, run, true);
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