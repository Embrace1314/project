
/**
 * AJAX-获取技术方案审核信息列表
 *
 * @param projectId
 *            项目ID
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
function ajaxListTechnicalProposalAttachment(projectId, success, fail, isExplicit, waiting, run,
                                         isLoadingExplicit) {
    // 显示缓冲
    if (isLoadingExplicit) {
        waiting();
    }
    $.ajax({
        url : URL_LIST_TECHNICAL_PROPOSAL_ATTACHMENT,
        type : TYPE_POST,
        data : {
            "project.id" : projectId
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
 * 通过技术方案附件审核
 * 
 * @param projectId
 * @param attachmentAudit
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxPassTechnicalProposalAttachment(projectId, attachmentAudit, success,
		fail, isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_PASS_TECHNICAL_PROPOSAL_ATTACHMENT,
		type : TYPE_POST,
		data : {
			"project.id" : projectId,
			"attachmentAudit.attachmentId" : attachmentAudit.attachmentId,
			"attachmentAudit.opinion" : attachmentAudit.opinion
		},
		dataType : DATATYPE_JSON,
		async : true,
		// 将XHR对象的withCredentials设为true
		xhrFields : {
			withCredentials : true
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
 * 驳回技术方案附件审核
 * @param projectId
 * @param attachmentAudit
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxFailTechnicalProposalAttachment(projectId, attachmentAudit, success,
		fail, isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_FAIL_TECHNICAL_PROPOSAL_ATTACHMENT,
		type : TYPE_POST,
		data : {
			"project.id" : projectId,
			"attachmentAudit.attachmentId" : attachmentAudit.attachmentId,
			"attachmentAudit.opinion" : attachmentAudit.opinion
		},
		dataType : DATATYPE_JSON,
		async : true,
		// 将XHR对象的withCredentials设为true
		xhrFields : {
			withCredentials : true
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

