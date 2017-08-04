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
                    data.page.list[i].cstCreate.substring(0,10), data.page.list[i].staffName,
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
 * 保存当月产值
 */
function btnSave() {
    var capacityItemId = getSelectedTableTrValue("#tableCapacity");
    var checkedValue = $("#txtProduction").val();
    if(isEmpty(capacityItemId)){
        alert("请先选择一条经营管理信息");
        return;
    }
    if (!verifyMoney(checkedValue, "当月核定产值",true)){
        return;
    }
    var businessCapacityItem = {
        id : capacityItemId,
        checkedValue : checkedValue
    };
    ajaxCheckBusinessCapacityItem(getUrlParam("project.id"), businessCapacityItem, function(data){
        $("#txtProduction").val("");
        // 刷新产值详情信息
        showCapacityDetail();
    }, function(data){}, true, waiting, run, true);
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