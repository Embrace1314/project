
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
            } else {
                setTableRowContent("#tableSettlement", i+1, "", "", "", "", "", "", "", "", "");
            }
        }
    }, function (data) {
    }, true, waiting, run, true);
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