/**
 * 控制侧边导航栏的跳转
 */
NAV_BAR_SELECT_CLASS = "nav_bar_submenu_select";

$(function() {
	disposeNavBar($("#navBidInfo"), "/project/establish/bid_info.html");
	disposeNavBar($("#navCostAnalysis"), "/project/establish/cost_analysis.html");
	disposeNavBar($("#navObjectiveManage"), "/project/establish/objective_manage.html");
	disposeNavBar($("#navBuildReport"), "/project/establish/build_report.html");
	
	disposeNavBar($("#navTeamBuild"), "/project/prepare/team_build.html");
	disposeNavBar($("#navPlanDeepen"), "/project/prepare/plan_deepen.html");
	disposeNavBar($("#navCostDeepen"), "/project/prepare/cost_deepen.html");
	disposeNavBar($("#navCapitalPlan"), "/project/prepare/capital_plan.html");
	
	disposeNavBar($("#navOperationManage"), "/project/implement/operation_manage.html");
	disposeNavBar($("#navProductionManage"), "/project/implement/production_manage.html");
	disposeNavBar($("#navTechnicalManage"), "/project/implement/technical_manage.html");
	disposeNavBar($("#navSecurityManage"), "/project/implement/security_manage.html");
	disposeNavBar($("#navQualityManage"), "/project/implement/quality_manage.html");
	disposeNavBar($("#navResourceImplement"), "/project/implement/resource_implement.html");
	disposeNavBar($("#navSettlementManage"), "/project/implement/settlement_manage.html");
	disposeNavBar($("#navLaborManage"), "/project/implement/labor_manage.html");
	disposeNavBar($("#navStoreManage"), "/project/implement/store_manage.html");
	
	disposeNavBar($("#navCompleteCheck"), "/project/complete/complete_check.html");
	disposeNavBar($("#navProjectDisintegrate"), "/project/complete/project_disintegrate.html");
	disposeNavBar($("#navProjectSummary"), "/project/complete/project_summary.html");
	disposeNavBar($("#navInternalSettlement"), "/project/complete/internal_settlement.html");
	disposeNavBar($("#navCompleteSettlement"), "/project/complete/complete_settlement.html");
});


/**
 * 处理导航栏
 */
function disposeNavBar(obj, htmlPrefix) {
	if (obj.hasClass(NAV_BAR_SELECT_CLASS)) {
		obj.attr("href", getJsReload());
	} else {
		obj.attr("href", getJsToHref(htmlPrefix));
	}
}

/**
 * 返回跳转页面的JS代码
 */
function getJsToHref(htmlPrefix) {
	return "javascript:gotoLocalHtml('" + htmlPrefix
			+ "?project.id='+getUrlParam('project.id'))";
}

/**
 * 返回刷新页面的JS代码
 */
function getJsReload() {
	return "javascript:reload()";
}
