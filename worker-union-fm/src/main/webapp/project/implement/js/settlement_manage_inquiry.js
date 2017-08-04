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
                    '<a href="#" onclick="btnDownloadFile(this)">'+data.page.list[i].name+'</a>',data.page.list[i].staffName,
                    data.page.list[i].cstCreate, AUDIT_STATUS[data.page.list[i].auditStatus],
                    '<div class="div_btn_verify"><button class="btn_pass" type="button" onclick="btnPass(this)">通过</button>' +
                    '<div class="div_line"></div><button class="btn_rebut" type="button" onclick="btnRebut(this)">驳回</button></div>',
                    '<input contenteditable="true" value="'+ data.page.list[i].auditOpinion +'">');
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
 *通过按钮
 */
function btnPass(obj) {
    var attachmentId = $(obj).parents("tr").first().children("td").first().html();
    var opinion =  $(obj).parent().parent().next().first().children("input").val();
    var attachmentAudit = {
        attachmentId : attachmentId,
        opinion : opinion
    };
    if (!verifyString(opinion, "审核意见", 1024, true)) {
		return;
	}
    if(confirm("确定通过该附件的审核？")){
        ajaxPassSettlementAttachment(getUrlParam("project.id"), attachmentAudit,
            function(data) {
                //刷新列表
                showSettlementFile();
            }, function() {
            }, true, waiting, run, true);
    }
}

/**
 *驳回按钮
 */
function btnRebut(obj) {
    var attachmentId = $(obj).parent().parent().parent().children("td").first().html();
    var opinion =  $(obj).parent().parent().next().first().children("input").val();
    var attachmentAudit = {
        attachmentId : attachmentId,
        opinion : opinion
    };
    if (!verifyString(opinion, "审核意见", 1024, true)) {
		return;
	}
    if(confirm("确定驳回该附件的审核？")){
        ajaxFailSettlementAttachment(getUrlParam("project.id"), attachmentAudit,
            function(data) {
                //刷新列表
                showSettlementFile();
            }, function() {
            }, true, waiting, run, true);
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