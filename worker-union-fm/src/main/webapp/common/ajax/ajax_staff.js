/**
 * AJAX-新增员工信息
 * 
 * @param staff
 *            员工信息
 * @param waiting
 *            缓冲
 * @param success
 *            处理成功获取业务数据
 * @param run
 *            正常运行
 * @param show
 *            是否显示调用
 * @param isExplicit
 *            是否显式处理
 * @param isLoadingExplicit
 *            缓冲是否显示处理
 */
function ajaxAddStaff(staff, success, fail, isExplicit, waiting, run,
		isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_ADD_STAFF,
		type : TYPE_POST,
		data : {
			"staff.jobNo" : staff.jobNo,
			"staff.name" : staff.name,
			"staff.phone" : staff.phone,
			"staff.idCardNo" : staff.idCardNo,
			"staff.address" : staff.address,
			"staff.entryDate" : staff.entryDate,
			"staff.career" : staff.career,
			"staff.memo" : staff.memo
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
 * AJAX-修改员工信息
 * 
 * @param staff
 *            员工信息
 * @param waiting
 *            缓冲
 * @param success
 *            处理成功获取业务数据
 * @param run
 *            正常运行
 * @param show
 *            是否显示调用
 * @param isExplicit
 *            是否显式处理
 * @param isLoadingExplicit
 *            缓冲是否显示处理
 */
function ajaxModifyStaffById(staff, success, fail, isExplicit, waiting, run,
		isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_MODIFY_STAFF_BY_ID,
		type : TYPE_POST,
		data : {
			"staff.id" : staff.id,
			"staff.name" : staff.name,
			"staff.phone" : staff.phone,
			"staff.address" : staff.address,
			"staff.entryDate" : staff.entryDate,
			"staff.career" : staff.career,
			"staff.memo" : staff.memo,
			"staff.idCardNo" : staff.idCardNo
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
 * AJAX-删除员工信息
 * 
 * @param staffId
 *            员工ID
 * @param waiting
 *            缓冲
 * @param success
 *            处理成功获取业务数据
 * @param run
 *            正常运行
 * @param show
 *            是否显示调用
 * @param isExplicit
 *            是否显式处理
 * @param isLoadingExplicit
 *            缓冲是否显示处理
 */
function ajaxRemoveStaffById(staffId, success, fail, isExplicit, waiting, run,
		isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_DELETE_STAFF_BY_ID,
		type : TYPE_POST,
		data : {
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
 * AJAX-获取员工信息详情
 * 
 * @param staffId
 *            员工ID
 * @param waiting
 *            缓冲
 * @param success
 *            处理成功获取业务数据
 * @param run
 *            正常运行
 * @param show
 *            是否显示调用
 * @param isExplicit
 *            是否显式处理
 * @param isLoadingExplicit
 *            缓冲是否显示处理
 */
function ajaxGetStaffDetailById(staffId, success, fail, isExplicit, waiting,
		run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_GET_STAFF_DETAIL_BY_ID,
		type : TYPE_POST,
		data : {
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
 * AJAX-模糊查询员工信息
 * 
 * @param waiting
 *            缓冲
 * @param success
 *            处理成功获取业务数据
 * @param run
 *            正常运行
 * @param show
 *            是否显示调用
 * @param isExplicit
 *            是否显式处理
 * @param isLoadingExplicit
 *            缓冲是否显示处理
 */
function ajaxPageStaffByFuzzy(staffDto, page, success, fail, isExplicit, waiting, run,
		isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_GET_PAGE_STAFF_BY_FUZZY,
		type : TYPE_POST,
		data : {
			"page.pagination" : page.pagination,
			"page.size" : page.size,
			"staffDto.name" : staffDto.name,
			"staffDto.jobNo" : staffDto.jobNo,
			"staffDto.idCardNo" : staffDto.idCardNo,
			"staffDto.roleName" : staffDto.roleName,
			"staffDto.projectName" : staffDto.projectName,
			"staffDto.startDate" : staffDto.startDate,
			"staffDto.endDate" : staffDto.endDate
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
 * AJAX-获取授权信息
 * 
 * @param waiting
 *            缓冲
 * @param success
 *            处理成功获取业务数据
 * @param run
 *            正常运行
 * @param show
 *            是否显示调用
 * @param isExplicit
 *            是否显式处理
 * @param isLoadingExplicit
 *            缓冲是否显示处理
 */
function ajaxCheckAuthorization(success, fail, isExplicit, waiting, run,
		isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_CHECK_AUTHORIZATION,
		type : TYPE_POST,
		data : {},
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
 * AJAX-修改密码
 *
 * @param password
 * @param passwordNew
 * @param passwordConfirm
 * @param waiting
 *            缓冲
 * @param success
 *            处理成功获取业务数据
 * @param run
 *            正常运行
 * @param show
 *            是否显示调用
 * @param isExplicit
 *            是否显式处理
 * @param isLoadingExplicit
 *            缓冲是否显示处理
 */
function ajaxChangePassword(password, passwordNew, passwordConfirm, success, fail, isExplicit, waiting,
								run, isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_CHANGE_PASSWORD,
		type : TYPE_POST,
		data : {
			"staff.password" : password,
			"passwordNew" : passwordNew,
			"passwordConfirm" : passwordConfirm
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
 * AJAX-退出系统
 *
 * @param waiting
 *            缓冲
 * @param success
 *            处理成功获取业务数据
 * @param run
 *            正常运行
 * @param show
 *            是否显示调用
 * @param isExplicit
 *            是否显式处理
 * @param isLoadingExplicit
 *            缓冲是否显示处理
 */
function ajaxLogout(success, fail, isExplicit, waiting, run,
				   isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_LOGOUT,
		type : TYPE_GET,
		data : {},
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
