
var PAGE_SIZE = 12;

/**
 * 页面初始调用
 **/
$(function () {
    $("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
    //显示产值文件列表
    showProductFile();
});

/**
 * 显示产值文件列表
 **/
function showProductFile(){
    // 分页参数
    var pagination = $("#pagination").html();
    // 清空页码
    $("#pagination").html("");
    $("#totalPage").html("");

    ajaxPageProductCapacityAttachment(getUrlParam("project.id"),{
        "pagination": pagination,
        "size": PAGE_SIZE
    }, function (data) {
        // 设置 页码
        $("#pagination").html(data.page.pagination);
        $("#totalPage").html(data.page.totalPage);

        clearTableTr("#tableProductFile");
        for (var i = 0; i < PAGE_SIZE; i++) {
            if (i < data.page.list.length) {
                setTableRowContent("#tableProductFile", i+1, data.page.list[i].id,
                    '<a href="#" onclick="btnDownloadFile(this)">'+data.page.list[i].name+'</a>', data.page.list[i].staffName,
                    data.page.list[i].cstCreate, '<button class="btn_delete_disable" type="button" disabled="disabled">删除</button>');
            } else {
                setTableRowContent("#tableProductFile", i+1, "", "", "", "", "");
            }
        }
    }, function (data) {
    }, true, waiting, run, true);
}

/**
 * 下载产值文件
 **/
function btnDownloadFile(obj) {
    // 获取授权信息
    var attachmentId = $(obj).parent().parent().children("td").first().html();
    ajaxAcquireProductCapacityFileDownToken(getUrlParam("project.id"), attachmentId,
        function(data) {
    		window.location = data.url;
        }, function() {
        }, true, waiting, run, true);
}

/**
 * 点击上一页
 */
function btnPrevPage() {
    $("#pagination").html(Math.max(parseInt($("#pagination").html()) - 1, 1));
    showProductFile();
}

/**
 * 点击下一页
 */
function btnNextPage() {
    $("#pagination").html(Math.max(parseInt($("#pagination").html()) + 1, 1));
    showProductFile();
}