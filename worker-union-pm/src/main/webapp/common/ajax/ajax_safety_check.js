/**
 * AJAX-分页获取项目安全检查项列表
 *
 * @param ProjectId
 *           项目ID
 * @param waiting
 *            缓冲
 * @param success
 *            处理成功获取业务数据
 * @param fail
 * @param page
 *            分页参数
 * @param run
 *            正常运行
 * @param isExplicit
 *            是否显式处理
 * @param isLoadingExplicit
 *            缓冲是否显示处理
 */
function ajaxPageSafetyCheckItem(ProjectId, page, success, fail, isExplicit, waiting, run,
                                isLoadingExplicit) {
    // 显示缓冲
    if (isLoadingExplicit) {
        waiting();
    }
    $.ajax({
        url : URL_PAGE_SAFETY_CHECK_ITEM,
        type : TYPE_POST,
        data : {
            "project.id" : ProjectId,
            "page.pagination" : page.pagination,
            "page.size" : page.size
        },
        dataType : DATATYPE_JSON,
        async : true,
        // 将XHR对象的withCredentials设为true
        xhrFields:{
            withCredentials:true
        },
        success : function(data) {
            // 关闭缓冲
            if (isLoadingExplicit) {
                run();
            }

            // 业务返回数据处理
            if (RETURNCODE_SUCCESS == data.returnCode) {
                success(data);
            } else if (RETURNCODE_FAIL == data.returnCode) {
                failDefault(data, isExplicit);
                fail(data);
            }
        },
        error : function() {
            // 关闭缓冲
            if (isLoadingExplicit) {
                run();
            }
            // 网络故障
            networkFault(isExplicit);
        }
    });
}

/**
 * AJAX-获取安全检查记录详情
 *
 * @param ProjectId
 *           项目ID
 * @param safetyCheckItemId
 * @param waiting
 *            缓冲
 * @param success
 *            处理成功获取业务数据
 * @param fail
 * @param run
 *            正常运行
 * @param isExplicit
 *            是否显式处理
 * @param isLoadingExplicit
 *            缓冲是否显示处理
 */
function ajaxGetSafetyCheckItemDetail(ProjectId, safetyCheckItemId, success, fail, isExplicit, waiting, run,
                                 isLoadingExplicit) {
    // 显示缓冲
    if (isLoadingExplicit) {
        waiting();
    }
    $.ajax({
        url : URL_GET_SAFETY_CHECK_ITEM_DETAIL,
        type : TYPE_POST,
        data : {
            "project.id" : ProjectId,
            "safetyCheckItem.id" : safetyCheckItemId
        },
        dataType : DATATYPE_JSON,
        async : true,
        // 将XHR对象的withCredentials设为true
        xhrFields:{
            withCredentials:true
        },
        success : function(data) {
            // 关闭缓冲
            if (isLoadingExplicit) {
                run();
            }

            // 业务返回数据处理
            if (RETURNCODE_SUCCESS == data.returnCode) {
                success(data);
            } else if (RETURNCODE_FAIL == data.returnCode) {
                failDefault(data, isExplicit);
                fail(data);
            }
        },
        error : function() {
            // 关闭缓冲
            if (isLoadingExplicit) {
                run();
            }
            // 网络故障
            networkFault(isExplicit);
        }
    });
}

/**
 * AJAX-删除安全检查记录下的附件
 *
 * @param ProjectId
 *           项目ID
 * @param attachmentId
 * @param waiting
 *            缓冲
 * @param success
 *            处理成功获取业务数据
 * @param fail
 * @param run
 *            正常运行
 * @param isExplicit
 *            是否显式处理
 * @param isLoadingExplicit
 *            缓冲是否显示处理
 */
function ajaxRemoveSafetyCheckItemAttachmentById(ProjectId, attachmentId, success, fail, isExplicit, waiting, run,
                                      isLoadingExplicit) {
    // 显示缓冲
    if (isLoadingExplicit) {
        waiting();
    }
    $.ajax({
        url : URL_REMOVE_SAFETY_CHECK_ITEM_ATTACHMENT_BY_ID,
        type : TYPE_POST,
        data : {
            "project.id" : ProjectId,
            "safetyCheckItemAttachment.id" : attachmentId
        },
        dataType : DATATYPE_JSON,
        async : true,
        // 将XHR对象的withCredentials设为true
        xhrFields:{
            withCredentials:true
        },
        success : function(data) {
            // 关闭缓冲
            if (isLoadingExplicit) {
                run();
            }

            // 业务返回数据处理
            if (RETURNCODE_SUCCESS == data.returnCode) {
                success(data);
            } else if (RETURNCODE_FAIL == data.returnCode) {
                failDefault(data, isExplicit);
                fail(data);
            }
        },
        error : function() {
            // 关闭缓冲
            if (isLoadingExplicit) {
                run();
            }
            // 网络故障
            networkFault(isExplicit);
        }
    });
}

/**
 * AJAX-申请审批安全检查记录
 *
 * @param ProjectId
 *           项目ID
 * @param safetyCheckItemId
 * @param ProjectFeedback
 * @param waiting
 *            缓冲
 * @param success
 *            处理成功获取业务数据
 * @param fail
 * @param run
 *            正常运行
 * @param isExplicit
 *            是否显式处理
 * @param isLoadingExplicit
 *            缓冲是否显示处理
 */
function ajaxApplySafetyCheckItemVerify(ProjectId, safetyCheckItemId, ProjectFeedback, success, fail, isExplicit, waiting, run,
                                                 isLoadingExplicit) {
    // 显示缓冲
    if (isLoadingExplicit) {
        waiting();
    }
    $.ajax({
        url : URL_APPLY_SAFETY_CHECK_ITEM_VERIFY,
        type : TYPE_POST,
        data : {
            "project.id" : ProjectId,
            "safetyCheckItem.id" : safetyCheckItemId,
            "safetyCheckItem.projectFeedback" : ProjectFeedback
        },
        dataType : DATATYPE_JSON,
        async : true,
        // 将XHR对象的withCredentials设为true
        xhrFields:{
            withCredentials:true
        },
        success : function(data) {
            // 关闭缓冲
            if (isLoadingExplicit) {
                run();
            }

            // 业务返回数据处理
            if (RETURNCODE_SUCCESS == data.returnCode) {
                success(data);
            } else if (RETURNCODE_FAIL == data.returnCode) {
                failDefault(data, isExplicit);
                fail(data);
            }
        },
        error : function() {
            // 关闭缓冲
            if (isLoadingExplicit) {
                run();
            }
            // 网络故障
            networkFault(isExplicit);
        }
    });
}
