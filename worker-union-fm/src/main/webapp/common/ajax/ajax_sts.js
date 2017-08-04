/**
 * 获取中标通知书上传权限
 * 
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireLetterOfAcceptanceUpToken(project, success, fail,
		isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_LETTER_OF_ACCEPTANCE_UP_TOKEN,
		data : {
			"project.num" : project.num,
			"project.name" : project.name,
			"project.constructerName" : project.constructerName,
			"project.designerName" : project.designerName,
			"project.superviserName" : project.superviserName,
			"project.address" : project.address,
			"project.type" : project.type,
			"project.scale" : project.scale,
			"project.bidDuration" : project.bidDuration,
			"project.bidPrice" : project.bidPrice
		},
		type : TYPE_POST,
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
 * 获取成本分析上传权限
 * 
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireCostAnalysisUpToken(projectId, success, fail, isExplicit,
		waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_COST_ANALYSIS_UP_TOKEN,
		data : {
			"project.id" : projectId
		},
		type : TYPE_POST,
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
 * 获取中标通知书下载权限
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
function ajaxAcquireLetterOfAcceptanceDownToken(projectId, attachmentId, success, fail,
		isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_LETTER_OF_ACCEPTANCE_DOWN_TOKEN,
		type : TYPE_POST,
		data : {
			'project.id' : projectId,
			'attachment.id' : attachmentId
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
 * 获取成本分析表下载权限
 *
 * @param projectId
 * @param attachmentId
 *          附件id
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireCostAnalysisDownToken(projectId, attachmentId, success, fail,
		isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_COST_ANALYSIS_DOWN_TOKEN,
		type : TYPE_POST,
		data : {
			'project.id' : projectId,
			'attachment.id' : attachmentId
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
 * 获取下载合同备案表凭证
 *
 * @param projectId
 *          项目id
 * @param attachmentId
 *          附件id
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireContractRecordDownToken(projectId, attachmentId, success, fail,
										  isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_CONTRACT_RECORD_DOWN_TOKEN,
		type : TYPE_POST,
		data : {
			"project.id" : projectId,
			"attachment.id" : attachmentId
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
 * 获取下载质监通知书凭证
 *
 * @param projectId
 *          项目id
 * @param attachmentId
 *          附件id
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireQualityInspectNoticeDownToken(projectId, attachmentId, success, fail,
											isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_QUALITY_INSPECT_NOTICE_DOWN_TOKEN,
		type : TYPE_POST,
		data : {
			"project.id" : projectId,
			"attachment.id" : attachmentId
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
 * 获取下载安监登记表凭证
 *
 * @param projectId
 *          项目id
 * @param attachmentId
 *          附件id
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireSafetySupervisionFormDownToken(projectId, attachmentId, success, fail,
												  isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_SAFETY_SUPERVISION_FORM_DOWN_TOKEN,
		type : TYPE_POST,
		data : {
			"project.id" : projectId,
			"attachment.id" : attachmentId
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
 * 获取下载施工许可证凭证
 *
 * @param projectId
 *          项目id
 * @param attachmentId
 *          附件id
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireConstructionPermitDownToken(projectId, attachmentId, success, fail,
												   isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_CONSTRUCTION_PERMIT_DOWN_TOKEN,
		type : TYPE_POST,
		data : {
			"project.id" : projectId,
			"attachment.id" : attachmentId
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
 * 获取下载目标协议书凭证
 *
 * @param projectId
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireAgreementOfTargetUpToken(projectId, success, fail,
											   isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_AGREEMENT_OF_TARGET_UP_TOKEN,
		type : TYPE_POST,
		data : {
			'project.id' : projectId
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
 * 获取下载目标协议书凭证
 *
 * @param projectId
 * @param attachmentId
 *          附件id
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireAgreementOfTargetDownToken(projectId, attachmentId, success, fail,
										  isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_AGREEMENT_OF_TARGET_DOWN_TOKEN,
		type : TYPE_POST,
		data : {
			'project.id' : projectId,
			'attachment.id' : attachmentId
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
 * 获取上传方案深化表凭证
 *
 * @param projectId
 *          项目id
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireSchemeDeepenUpToken(projectId, success, fail,
											isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_SCHEME_DEEPEN_UP_TOKEN,
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
 * 获取下载方案深化表凭证
 *
 * @param projectId
 * @param attachmentId
 *          附件id
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireSchemeDeepenDownToken(projectId, attachmentId, success, fail,
										isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_SCHEME_DEEPEN_DOWN_TOKEN,
		type : TYPE_POST,
		data : {
			'project.id' : projectId,
			'attachment.id' : attachmentId
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
 * 获取上传成本深化表凭证
 *
 * @param projectId
 *          项目id
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireCostDeepenUpToken(projectId, success, fail,
										isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_COST_DEEPEN_UP_TOKEN,
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
 * 获取下载成本深化表凭证
 *
 * @param projectId
 * @param attachmentId
 *          附件id
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireCostDeepenDownToken(projectId, attachmentId, success, fail,
											   isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_COST_DEEPEN_DOWN_TOKEN,
		type : TYPE_POST,
		data : {
			'project.id' : projectId,
			'attachment.id' : attachmentId
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
 * 获取上传经营管理产值文件凭证
 *
 * @param projectId
 *          项目id
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireBusinessCapacityFileUpToken(projectId, success, fail,
									  isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_BUSINESS_CAPACITY_FILE_UP_TOKEN,
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
 * 获取下载经营管理产值文件凭证
 *
 * @param projectId
 * @param attachmentId
 *          附件id
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireBusinessCapacityFileDownToken(projectId, attachmentId, success, fail,
										isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_BUSINESS_CAPACITY_FILE_DOWN_TOKEN,
		type : TYPE_POST,
		data : {
			'project.id' : projectId,
			'attachment.id' : attachmentId
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
 * 获取上传生产产值文件凭证
 *
 * @param projectId
 *          项目id
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireProductCapacityFileUpToken(projectId, success, fail,
												isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_PRODUCT_CAPACITY_FILE_UP_TOKEN,
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
 * 获取下载生产产值文件凭证
 *
 * @param projectId
 * @param attachmentId
 *          附件id
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireProductCapacityFileDownToken(projectId, attachmentId, success, fail,
												  isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_PRODUCT_CAPACITY_FILE_DOWN_TOKEN,
		type : TYPE_POST,
		data : {
			'project.id' : projectId,
			'attachment.id' : attachmentId
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
 * 获取上传技术方案凭证
 *
 * @param projectId
 *          项目id
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireTechnicalProposalUpToken(projectId, success, fail,
											   isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_TECHNICAL_PROPOSAL_UP_TOKEN,
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
 * 获取下载技术方案凭证
 *
 * @param projectId
 * @param attachmentId
 *          附件id
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireTechnicalProposalDownToken(projectId, attachmentId, success, fail,
												 isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_TECHNICAL_PROPOSAL_DOWN_TOKEN,
		type : TYPE_POST,
		data : {
			'project.id' : projectId,
			'attachment.id' : attachmentId
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
 * 获取上传结算文件凭证
 *
 * @param projectId
 *          项目id
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireSettlementUpToken(projectId, success, fail,
									  isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_SETTLEMENT_UP_TOKEN,
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
 * 获取下载结算文件凭证
 *
 * @param projectId
 * @param attachmentId
 *          附件id
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireSettlementDownToken(projectId, attachmentId, success, fail,
											   isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_SETTLEMENT_DOWN_TOKEN,
		type : TYPE_POST,
		data : {
			'project.id' : projectId,
			'attachment.id' : attachmentId
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
 * 获取上传竣工资料凭证
 *
 * @param projectId
 *          项目id
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireCompletionDataUpToken(projectId, success, fail,
									  isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_COMPLETION_DATA_UP_TOKEN,
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
 * 获取下载竣工资料凭证
 *
 * @param projectId
 * @param attachmentId
 *          附件id
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireCompletionDataDownToken(projectId, attachmentId, success, fail,
										isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_COMPLETION_DATA_DOWN_TOKEN,
		type : TYPE_POST,
		data : {
			'project.id' : projectId,
			'attachment.id' : attachmentId
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
 * 获取上传总结报告凭证
 *
 * @param projectId
 *          项目id
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireFinalReportUpToken(projectId, success, fail,
										  isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_FINAL_REPORT_UP_TOKEN,
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
 * 获取下载总结资料凭证
 *
 * @param projectId
 * @param attachmentId
 *          附件id
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireFinalReportDownToken(projectId, attachmentId, success, fail,
											isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_FINAL_REPORT_DOWN_TOKEN,
		type : TYPE_POST,
		data : {
			'project.id' : projectId,
			'attachment.id' : attachmentId
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
 * 获取上传内部结算附件凭证
 *
 * @param projectId
 *          项目id
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireInternalSettlementUpToken(projectId, success, fail,
									   isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_INTERNAL_SETTLEMENT_UP_TOKEN,
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
 * 获取内部结算文件下载凭证
 *
 * @param projectId
 * @param attachmentId
 *          附件id
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireInternalSettlementDownToken(projectId, attachmentId, success, fail,
													   isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_INTERNAL_SETTLEMENT_DOWN_TOKEN,
		type : TYPE_POST,
		data : {
			'project.id' : projectId,
			'attachment.id' : attachmentId
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
 * 获取决算条目附件上传凭证
 *
 * @param projectId
 * @param attachmentId
 *          附件id
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireFinalSettlementItemAttachmentUpToken(projectId, attachmentId, success, fail,
													   isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_FINAL_SETTLEMENT_ATTACHMENT_UP_TOKEN,
		type : TYPE_POST,
		data : {
			'project.id' : projectId,
			'finalSettlementItem.id' : attachmentId
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
 * 获取决算条目附件下载凭证
 *
 * @param projectId
 * @param attachmentId
 *          附件id
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireFinalSettlementAttachmentDownToken(projectId, attachmentId, success, fail,
										 isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_FINAL_SETTLEMENT_ATTACHMENT_DOWN_TOKEN,
		type : TYPE_POST,
		data : {
			'project.id' : projectId,
			'finalSettlementItem.id' : attachmentId
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
 * 获取上传安全记录附件凭证
 *
 * @param projectId
 *          项目id
 * @param safetyCheckItemId
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireSafetyCheckItemAttachmentUpToken(projectId, safetyCheckItemId, success, fail,
									   isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_SAFETY_CHECK_ITEM_ATTACHMENT_UP_TOKEN,
		type : TYPE_POST,
		data : {
			"project.id" : projectId,
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
 * 获取下载安全记录附件凭证
 *
 * @param projectId
 * @param attachmentId
 *          附件id
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireSafetyCheckItemAttachmentDownToken(projectId, attachmentId, success, fail,
										 isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_SAFETY_CHECK_ITEM_ATTACHMENT_DOWN_TOKEN,
		type : TYPE_POST,
		data : {
			'project.id' : projectId,
			'safetyCheckItemAttachment.id' : attachmentId
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
 * 获取上传质量记录附件凭证
 *
 * @param projectId
 *          项目id
 * @param qualityCheckItemId
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireQualityCheckItemAttachmentUpToken(projectId, qualityCheckItemId, success, fail,
													 isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_QUALITY_CHECK_ITEM_ATTACHMENT_UP_TOKEN,
		type : TYPE_POST,
		data : {
			"project.id" : projectId,
			"qualityCheckItem.id" : qualityCheckItemId

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
 * 获取下载质量记录附件凭证
 *
 * @param projectId
 * @param attachmentId
 *          附件id
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireQualityCheckItemAttachmentDownToken(projectId, attachmentId, success, fail,
													   isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_QUALITY_CHECK_ITEM_ATTACHMENT_DOWN_TOKEN,
		type : TYPE_POST,
		data : {
			'project.id' : projectId,
			'qualityCheckItemAttachment.id' : attachmentId
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
 * 获取上传供应商附件凭证
 *
 * @param supplierId
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireSupplierAttachmentUpToken(supplierId, success, fail,
									   isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_SUPPLIER_ATTACHMENT_UP_TOKEN,
		type : TYPE_POST,
		data : {
			"supplier.id" : supplierId

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
 * 获取下载供应商附件凭证
 *
 * @param attachmentId
 *          附件id
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireSupplierAttachmentDownToken(attachmentId, success, fail,
										 isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_SUPPLIER_ATTACHMENT_DOWN_TOKEN,
		type : TYPE_POST,
		data : {
			'supplierAttachment.id' : attachmentId
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
 * 获取上传分包商附件凭证
 *
 * @param subcontractorId
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireSubcontractorAttachmentUpToken(subcontractorId, success, fail,
									   isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_SUBCONTRACTOR_ATTACHMENT_UP_TOKEN,
		type : TYPE_POST,
		data : {
			"subcontractor.id" : subcontractorId

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
 * 获取下载分包商附件凭证
 *
 * @param attachmentId
 *          附件id
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireSubcontractorAttachmentDownToken(attachmentId, success, fail,
										 isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_SUBCONTRACTOR_ATTACHMENT_DOWN_TOKEN,
		type : TYPE_POST,
		data : {
			'subcontractorAttachment.id' : attachmentId
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
 * 获取上传材料类型附件凭证
 *
 * @param materialTypeId
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireMaterialTypeAttachmentUpToken(materialTypeId, success, fail,
									   isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_MATERIAL_TYPE_ATTACHMENT_UP_TOKEN,
		type : TYPE_POST,
		data : {
			"materialType.id" : materialTypeId

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
 * 获取下载材料类型附件凭证
 *
 * @param attachmentId
 *          附件id
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireMaterialTypeAttachmentDownToken(attachmentId, success, fail,
										 isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_MATERIAL_TYPE_ATTACHMENT_DOWN_TOKEN,
		type : TYPE_POST,
		data : {
			'materialTypeAttachment.id' : attachmentId
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
 * 获取上传合同附件凭证
 *
 * @param contractId
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireContractAttachmentUpToken(contractId, success, fail,
									   isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_CONTRACT_ATTACHMENT_UP_TOKEN,
		type : TYPE_POST,
		data : {
			"contract.id" : contractId

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
 * 获取下载合同附件凭证
 *
 * @param attachmentId
 *          附件id
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireContractAttachmentDownToken(attachmentId, success, fail,
										 isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_CONTRACT_ATTACHMENT_DOWN_TOKEN,
		type : TYPE_POST,
		data : {
			'contractAttachment.id' : attachmentId
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
 * 获取上传合同备案表凭证
 *
 * @param projectId
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireContractRecordUpToken(projectId, success, fail,
											  isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_CONTRACT_RECORD_UP_TOKEN,
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
 * 获取上传质检通知书凭证
 *
 * @param projectId
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireQualityInspectNoticeUpToken(projectId, success, fail,
										  isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_QUALITY_INSPECT_NOTICE_UP_TOKEN,
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
 * 获取上传安监登记表凭证
 *
 * @param projectId
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireSafetySupervisionFormUpToken(projectId, success, fail,
												isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_SAFETY_SUPERVISION_FORM_UP_TOKEN,
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
 * 获取上传施工许可证凭证
 *
 * @param projectId
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireConstructionPermitUpToken(projectId, success, fail,
												 isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_CONSTRUCTION_PERMIT_UP_TOKEN,
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
 * 获取下载团队搭建附件凭证
 *
 * @param projectId
 * @param attachmentId
 *          附件id
 * @param success
 * @param fail
 * @param isExplicit
 * @param waiting
 * @param run
 * @param isLoadingExplicit
 */
function ajaxAcquireProjectTeamAttachmentDownToken(projectId, attachmentId, success, fail,
												   isExplicit, waiting, run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ACQUIRE_PROJECT_TEAM_ATTACHMENT_DOWN_TOKEN,
		type : TYPE_POST,
		data : {
			'project.id' : projectId,
			'attachment.id' : attachmentId
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

