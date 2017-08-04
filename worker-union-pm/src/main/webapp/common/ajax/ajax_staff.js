/**
 * AJAX-模糊查询员工信息
 *
 * @param page
 * @param name
 * @param jobNo
 * @param idCardNo
 * @param roleName
 * @param projectName
 * @param startDate
 * @param endDate
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
function ajaxPageStaffsByFuzzy(page, name, jobNo, idCardNo, roleName, projectName, startDate, endDate, success, fail, isExplicit, waiting, run,
		isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_PAGE_STAFFS_BY_FUZZY,
		type : TYPE_POST,
		data : {
			"page.pagination" : page.pagination,
			"page.size" : page.size,
			"staffDto.name" : name,
			"staffDto.jobNo" : jobNo,
			"staffDto.idCardNo" : idCardNo,
			"staffDto.roleName" : roleName,
			"staffDto.projectName" : projectName,
			"staffDto.startDate" : startDate,
			"staffDto.endDate" : endDate

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
 * AJAX-获取已登录用户信息
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
function ajaxGetLoginedStaffInfo(success, fail, isExplicit, waiting, run,
								isLoadingExplicit) {
	// 显示缓冲
	if (isLoadingExplicit) {
		waiting();
	}
	$.ajax({
		url : URL_GET_LOGINED_STAFF_INFO,
		type : TYPE_POST,
		data : {},
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