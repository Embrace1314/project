var PAGE_SIZE = 12;
var PROJECT_NAME_LEN_MAX = 32;

/**
 * 获取授权信息
 */
function getPageProjects() {
	var status = $("#status").val();
	// 年份
	var year = $("#selectYear option:selected").val();
	// 分页参数
	var pagination = $("#pagination").html();

	// 重置状态
	resetButtons(status);
	if (isEmpty(status) && isEmpty(year)) {
		// 发起AJAX请求-获取所有
		ajaxGetPageAllProjects({
			"pagination" : pagination,
			"size" : PAGE_SIZE
		}, success, function() {
		}, true, waiting, run, true);
	} else if (isEmpty(status) && !isEmpty(year)) {
		// 根据年份获取
		ajaxGetPageProjectsByYear(year + "-01-01", {
			"pagination" : pagination,
			"size" : PAGE_SIZE
		}, success, function() {
		}, true, waiting, run, true);
	} else if (!isEmpty(status) && isEmpty(year)) {
		// 根据状态获取
		ajaxGetPageProjectsByStatus(status, {
			"pagination" : pagination,
			"size" : PAGE_SIZE
		}, success, function() {
		}, true, waiting, run, true);
	} else if (!isEmpty(status) && !isEmpty(year)) {
		// 根据状态和年份获取
		ajaxGetPageProjectsByStatusAndYear(status, year + "-01-01", {
			"pagination" : pagination,
			"size" : PAGE_SIZE
		}, success, function() {
		}, true, waiting, run, true);
	}

}

/**
 * 成功获取分页项目列表信息
 * 
 * @param data
 */
function success(data) {
	$("#projectList").empty();
	var list = data.page.list;
	for (i = 0; i < list.length; i++) {
		var projectName = retainStringFixLenEng(list[i].name, PROJECT_NAME_LEN_MAX);
		$("#projectList")
				.append(
						"<button class='btn_project' onclick='gotoLocalHtml(\"/project/establish/bid_info.html?project.id="
								+ list[i].id
								+ "\")'>"
								+ projectName
								+ "</button>");
	}
	// 设置页码
	$("#pagination").html(data.page.pagination);
	$("#totalPage").html(data.page.totalPage);
}

/**
 * 重置项目状态按钮效果
 */
function resetButtons(status) {
	// 清空列表及页码
	$("#projectList").empty();
	$("#pagination").html("");
	$("#totalPage").html("");

	$("#btnGoingProject").attr("class", "btn_ongoing_project");
	$("#btnArchivedProject").attr("class", "btn_archived_project");
	$("#btnAllProject").attr("class", "btn_all_project");
	switch (status) {
	case PROJECT_TYPE_GOING:
		$("#btnGoingProject").attr("class", "btn_ongoing_project_selected");
		break;
	case PROJECT_TYPE_ARCHIVED:
		$("#btnArchivedProject").attr("class", "btn_archived_project_selected");
		break;
	case PROJECT_TYPE_ALL:
		$("#btnAllProject").attr("class", "btn_all_project_selected");
		break;
	default:
		$("#btnAllProject").attr("class", "btn_all_project_selected");
		break;
	}
}

/**
 * 页面启动调用
 */
$(function() {
	$("#txtUserName").html($.cookie(COOKIE_STAFF_NAME_KEY));
	//初始化年份选择下拉框
	initDate();

	switch (getUrlParam("status")) {
	case PROJECT_TYPE_GOING:
		$("#status").val(PROJECT_TYPE_GOING);
		break;
	case PROJECT_TYPE_ARCHIVED:
		$("#status").val(PROJECT_TYPE_ARCHIVED);
		break;
	case PROJECT_TYPE_ALL:
		$("#status").val(PROJECT_TYPE_ALL);
		break;
	default:
		// 默认显示进行中项目
		$("#status").val(PROJECT_TYPE_GOING);
		break;
	}
	// 获取项目列表
	getPageProjects();

});

/**
 * 初始化年份选择下拉框
 */
function initDate() {
	//设置年份的选择
	var myDate = new Date();
	var startYear = myDate.getFullYear() - 20;//起始年份
	var endYear = myDate.getFullYear();//结束年份
	var obj = document.getElementById('selectYear');
	obj.options.add(new Option("请选择立项年份", ""));
	for (var i = endYear; i >= startYear; i--) {
		obj.options.add(new Option(i, i));
	}
	obj.options[0].selected = 1;
}

/**
 * 点击按钮-所有项目
 */
function btnAllProject() {
	$("#status").val(PROJECT_TYPE_ALL);
	getPageProjects();
}

/**
 * 点击按钮-已归档项目
 */
function btnArchivedProject() {
	$("#status").val(PROJECT_TYPE_ARCHIVED);
	getPageProjects();
}

/**
 * 点击按钮-进行中项目
 */
function btnGoingProject() {
	$("#status").val(PROJECT_TYPE_GOING);
	getPageProjects();
}

/**
 * 点击下一页
 */
function nextPage() {
	$("#pagination").html(Math.max($("#pagination").html() + 1, 1));
	getPageProjects();
}

/**
 * 点击上一页
 */
function prePage() {
	$("#pagination").html(Math.max($("#pagination").html() - 1, 1));
	getPageProjects();
}