
var PAGE_SIZE = 12;

/**
 * 页面初始调用
 **/
$(function () {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
    //显示产值文件列表
    showCapacityFile();
});

/**
 * 显示产值文件列表
 **/
function showCapacityFile(){
    // 分页参数
    var pagination = $("#pagination").html();
    // 清空页码
    $("#pagination").html("");
    $("#totalPage").html("");

    ajaxPageBusinessCapacityAttachment(getUrlParam("project.id"),{
        "pagination": pagination,
        "size": PAGE_SIZE
    }, function (data) {
        // 设置 页码
        $("#pagination").html(data.page.pagination);
        $("#totalPage").html(data.page.totalPage);

        clearTableTr("#tableCapacityFile");
        for (var i = 0; i < PAGE_SIZE; i++) {
            if (i < data.page.list.length) {
                setTableRowContent("#tableCapacityFile", i+1, data.page.list[i].id,
                    '<a href="#" onclick="btnDownloadFile(this)">'+data.page.list[i].name+'</a>', data.page.list[i].staffName,
                    data.page.list[i].cstCreate, '<button class="btn_delete" type="button" onclick="btnDeleteFile(this)">删除</button>');
            } else {
                setTableRowContent("#tableCapacityFile", i+1, "", "", "", "", "");
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
    ajaxAcquireBusinessCapacityFileDownToken(getUrlParam("project.id"), attachmentId,
        function(data) {
			window.location = data.url;
        }, function() {
        }, true, waiting, run, true);
}

/**
 * 删除产值文件
 **/
function btnDeleteFile(obj) {
    var attachmentId = $(obj).parent().parent().children("td").first().html();
    if (!isEmpty(attachmentId)){
        if(confirm("确定删除该经营管理产值文件项？")){
            ajaxRemoveBusinessCapacityAttachment(getUrlParam("project.id"), attachmentId, function(data){
                // 刷新产值文件列表
                showCapacityFile();
            }, function(data){}, true, waiting, run, true);
        }
    }
}


/**
 * 点击上一页
 */
function btnPrevPage() {
    $("#pagination").html(Math.max(parseInt($("#pagination").html()) - 1, 1));
    showCapacityFile();
}

/**
 * 点击下一页
 */
function btnNextPage() {
    $("#pagination").html(Math.max(parseInt($("#pagination").html()) + 1, 1));
    showCapacityFile();
}