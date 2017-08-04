/**
 * 获取成本深化表
 *
 * @param projectId
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxGetCostAnalysisDeepenUniqueAttachment(projectId, success, fail, isExplicit,
                                       waiting, run, isLoadingExplicit) {
    // 显示缓冲
    if (isLoadingExplicit) {
        waiting();
    }
    $.ajax({
        url : URL_GET_COST_ANALYSIS_DEEPEN_UNIQUE_ATTACHMENT,
        data : {
            "project.id" : projectId
        },
        type : TYPE_POST,
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
 * 获取成本深化详情
 *
 * @param projectId
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireCostAnalysisDeepenDetail(projectId, success, fail, isExplicit,
                                                   waiting, run, isLoadingExplicit) {
    // 显示缓冲
    if (isLoadingExplicit) {
        waiting();
    }
    $.ajax({
        url : URL_ACQUIRE_COST_ANALYSIS_DEEPEN_DETAIL,
        data : {
            "project.id" : projectId
        },
        type : TYPE_POST,
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
 * 获取成本深化项
 *
 * @param projectId
 * @param costAnalysisItemDeepenId
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxGetCostAnalysisItemDeepenById(projectId, costAnalysisItemDeepenId, success, fail, isExplicit,
                                             waiting, run, isLoadingExplicit) {
    // 显示缓冲
    if (isLoadingExplicit) {
        waiting();
    }
    $.ajax({
        url : URL_GET_COST_ANALYSIS_ITEM_DEEPEN_BY_ID,
        data : {
            "project.id" : projectId,
            "costAnalysisItemDeepen.id" : costAnalysisItemDeepenId
        },
        type : TYPE_POST,
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
 * 获取成本深化项列表
 *
 * @param projectId
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxListCostAnalysisItemDeepen(projectId, success, fail, isExplicit,
                                             waiting, run, isLoadingExplicit) {
    // 显示缓冲
    if (isLoadingExplicit) {
        waiting();
    }
    $.ajax({
        url : URL_LIST_COST_ANALYSIS_ITEM_DEEPEN,
        data : {
            "project.id" : projectId
        },
        type : TYPE_POST,
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


