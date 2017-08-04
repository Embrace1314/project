

var PAGE_SIZE = 12;

/**
 * 页面初始调用
 **/
$(function () {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
    //显示结算文件列表
    showSettlementFile();
});

/**
 * 显示结算文件列表
 **/
function showSettlementFile(){
    // 分页参数
    var pagination = $("#pagination").html();
    // 清空页码
    $("#pagination").html("");
    $("#totalPage").html("");

    ajaxPageSettlementAttachment(getUrlParam("project.id"),{
        "pagination": pagination,
        "size": PAGE_SIZE
    }, function (data) {
        // 设置 页码
        $("#pagination").html(data.page.pagination);
        $("#totalPage").html(data.page.totalPage);

        clearTableTr("#tableSettlementFile");
        for (var i = 0; i < PAGE_SIZE; i++) {
            if (i < data.page.list.length) {
                setTableRowContent("#tableSettlementFile", i+1, data.page.list[i].id,
                    '<a href="#" onclick="btnDownloadFile(this)">'+data.page.list[i].name+'</a>', data.page.list[i].staffName,
                    data.page.list[i].cstCreate, AUDIT_STATUS[data.page.list[i].auditStatus],
                    data.page.list[i].auditOpinion, '<button class="btn_delete" type="button" onclick="btnDeleteFile(this)">删除</button>');
                $("#tableSettlementFile"+" tr").eq(i+1).children("td").eq(4).attr("class","audit_status");
            } else {
                setTableRowContent("#tableSettlementFile", i+1, "", "", "", "","", "", "");

            }
        }
    }, function (data) {
    }, true, waiting, run, true);
}

/**
 * 下载结算文件
 **/
function btnDownloadFile(obj) {
    // 获取授权信息
    var attachmentId = $(obj).parent().parent().children("td").first().html();
    ajaxAcquireSettlementDownToken(getUrlParam("project.id"), attachmentId,
        function(data) {
			window.location = data.url;
        }, function() {
        }, true, waiting, run, true);
}

/**
 * 删除结算文件
 **/
function btnDeleteFile(obj) {
    var attachmentId = $(obj).parent().parent().children("td").first().html();
    if (!isEmpty(attachmentId)){
        if(confirm("确定删除该结算文件？")){
            ajaxRemoveSettlementAttachmentById(getUrlParam("project.id"), attachmentId, function(data){
                // 刷新页面
                reload();
            }, function(data){}, true, waiting, run, true);
        }
    }
}


/**
 * 点击上一页
 */
function btnPrevPage() {
    $("#pagination").html(Math.max(parseInt($("#pagination").html()) - 1, 1));
    showSettlementFile();
}

/**
 * 点击下一页
 */
function btnNextPage() {
    $("#pagination").html(Math.max(parseInt($("#pagination").html()) + 1, 1));
    showSettlementFile();
}