/**
 * AJAX-获取目标协议书附件信息
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
function ajaxGetTargetUniqueAttachment(projectId, success, fail, isExplicit,
		waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_GET_TARGET_UNIQUE_ATTACHMENT,
		type : TYPE_POST,
		data : {
			"project.id" : projectId
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
 * AJAX-获取目标管理详情
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
function ajaxGetTargetDetail(projectId, success, fail, isExplicit, waiting,
		run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_GET_TARGET_DETAIL,
		type : TYPE_POST,
		data : {
			"project.id" : projectId
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
 * 删除目标协议书
 * 
 * @param projectId
 * @param attachmentId
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxRemoveTargetUniqueAttachment(projectId, attachmentId, success,
		fail, isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_REMOVE_TARGET_UNIQUE_ATTACHMENT,
		type : TYPE_POST,
		data : {
			"project.id" : projectId,
			"attachment.id" : attachmentId,
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
 * 新增项目经理
 * 
 * @param projectId
 * @param staffId
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAddProjectManager(projectId, staffId, success, fail, isExplicit,
		waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ADD_PROJECT_MANAGER,
		type : TYPE_POST,
		data : {
			"project.id" : projectId,
			"staff.id" : staffId
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
 * 删除项目经理
 * 
 * @param projectId
 * @param staffId
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxRemoveProjectManager(projectId, staffId, success, fail,
		isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_REMOVE_PROJECT_MANAGER,
		type : TYPE_POST,
		data : {
			"project.id" : projectId,
			"staff.id" : staffId
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
 * 保存目标详情
 * @param projectId
 * @param target
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxSaveTarget(projectId, target, success, fail, isExplicit, waiting,
		run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_SAVE_TARGET,
		type : TYPE_POST,
		data : {
			"project.id" : projectId,
			"target.duration" : target.duration,
			"target.startDate" : target.startDate,
			"target.endDate" : target.endDate,
			"target.quality" : target.quality,
			"target.safety" : target.safety,
			"target.cost" : target.cost
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
