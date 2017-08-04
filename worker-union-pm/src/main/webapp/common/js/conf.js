var URL_IP = "";

var HTML_HOME = "/worker-union-pm/home.html";
var HTML_LOGIN = "/worker-union-pm/login.html";
var FM_LOGIN = window.location.protocol + "//"  + window.location.host + "/worker-union-fm/login.html";

/** 成本项类型 */
var COST_ANALYSIS_ITEM_TYPE = {
	"DIRECT_COST" : "直接成本",
	"INDIRECT_COST" : "间接成本",
	"SUBCONTRACT_COST" : "指定分包成本",
	"TAX_COST" : "税额"
};

/** 岗位角色 */
var JOB_ITEM_TYPE = {
	"PROJECT_MANAGER" : "项目经理",
	"TECHNICAL_DIRECTOR" : "技术负责人",
	"CONSTRUCTION_WORKER" : "施工员",
	"QUALITY_WORKER" : "质量员",
	"SAFETY_WORKER" : "安全员",
	"BUDGETER" : "预算员",
	"MATERIALMAN" : "材料员",
	"MACHINIST" : "机械员",
	"LABOR_WORKER" : "劳务员",
	"DATA_PROCESSOR" : "资料员"
};

/** 审核状态 */
var AUDIT_STATUS = {
	"UNDETERMINED" : "未审核",
	"PASS" : "通过",
	"FAIL" : "驳回"
};

/** 材料操作类型 */
var MATERIAL_ORDER_TYPE = {
	"IMPORT" : "入库",
	"EXPORT" : "出库"
};

/** 整改状态 */
var RECTIFY_STATUS = {
	"PASS" : "整改通过",
	"RECTIFY" : "重新整改",
	"VERIFY" : "正在审批"
};

/** 性别 */
var SEX = {
	"MALE" : "男",
	"FEMALE" : "女"
};

/** 企业ID */
var COMPANY_ID = "1";

/** 项目状态-进行中 */
var PROJECT_TYPE_GOING = "GOING";
/** 项目状态-已归档 */
var PROJECT_TYPE_ARCHIVED = "ARCHIVED";
/** 所有项目状态 */
var PROJECT_TYPE_ALL = "";

/** COOKIE STAFF NAME*/
var COOKIE_STAFF_NAME_KEY = "STAFF_NAME";

/** OSS-URL */
var URL_OSS = "http://oss-cn-hangzhou.aliyuncs.com";

/** 分页 */
function Page(size, pagination) {
	this.pagination = pagination;
	this.size = size;
}

// STAFF
/** URL-登录 */
var URL_LOGIN = URL_IP + "/worker-union-pm/staff/login.action?staff.companyId=" + COMPANY_ID;
/** URL-获取已登录用户信息 */
var URL_GET_LOGINED_STAFF_INFO = URL_IP + "/worker-union-pm/staff/getLoginedStaffInfo.action";
/** URL-根据模糊条件查询员工信息，分页 */
var URL_PAGE_STAFFS_BY_FUZZY = URL_IP + "/worker-union-pm/staff/pageStaffByFuzzy.action";
/** URL-修改密码 */
var URL_CHANGE_PASSWORD = URL_IP + "/worker-union-pm/staff/changePassword.action";
/** URL-退出系统 */
var URL_LOGOUT = URL_IP+"/worker-union-pm/staff/logout.action";

// PROJECT
/** URL-获取分页的项目列表信息 */
var URL_GET_PAGE_ALL_PROJECTS = URL_IP + "/worker-union-pm/project/getPageAllProjects.action";
/** URL-根据项目状态获取分页的项目列表信息 */
var URL_GET_PAGE_PROJECTS_BY_STATUS = URL_IP + "/worker-union-pm/project/getPageProjectsByStatus.action";
/** URL-根据立项年份获取分页的项目列表信息 */
var URL_GET_PAGE_PROJECTS_BY_YEAR = URL_IP + "/worker-union-pm/project/getPageProjectsByYear.action";
/** URL-根据项目状态及立项年份获取分页的项目列表信息 */
var URL_GET_PAGE_PROJECTS_BY_STATUS_AND_YEAR = URL_IP + "/worker-union-pm/project/getPageProjectsByStatusAndYear.action";
/** URL-项目登录 */
var URL_PROJECT_LOGIN = URL_IP + "/worker-union-pm/project/loginProject.action";
/** URL-根据项目ID获取项目信息 */
var URL_GET_PROJECT_DETAIL_BY_ID = URL_IP + "/worker-union-pm/project/getProjectDetailById.action";
/** URL-获取企业下未归档项目简要信息 */
var URL_LIST_UNARCHIVED_PROJECT = URL_IP + "/worker-union-pm/project/listUnarchivedProject.action";

// STS
/** URL-获取下载中标通知书授权信息 */
var URL_ACQUIRE_LETTER_OF_ACCEPTANCE_DOWN_TOKEN = URL_IP + "/worker-union-pm/sts/acquireLetterOfAcceptanceDownToken.action";
/** URL-获取下载成本分析授权信息 */
var URL_ACQUIRE_COST_ANALYSIS_DOWN_TOKEN = URL_IP + "/worker-union-pm/sts/acquireCostAnalysisDownToken.action";
/** URL-获取下载合同备案表凭证 */
var URL_ACQUIRE_CONTRACT_RECORD_DOWN_TOKEN = URL_IP + "/worker-union-pm/sts/acquireContractRecordDownToken.action";
/** URL-获取下载质监通知书凭证 */
var URL_ACQUIRE_QUALITY_INSPECT_NOTICE_DOWN_TOKEN = URL_IP + "/worker-union-pm/sts/acquireQualityInspectNoticeDownToken.action";
/** URL-获取下载安监登记表凭证 */
var URL_ACQUIRE_SAFETY_SUPERVISION_FORM_DOWN_TOKEN = URL_IP + "/worker-union-pm/sts/acquireSafetySupervisionFormDownToken.action";
/** URL-获取下载施工许可证凭证 */
var URL_ACQUIRE_CONSTRUCTION_PERMIT_DOWN_TOKEN = URL_IP + "/worker-union-pm/sts/acquireConstructionPermitDownToken.action";
/** URL-获取下载目标协议书凭证 */
var URL_ACQUIRE_AGREEMENT_OF_TARGET_DOWN_TOKEN = URL_IP + "/worker-union-pm/sts/acquireAgreementOfTargetDownToken.action";
/** URL-获取上传方案深化表凭证 */
var URL_ACQUIRE_SCHEME_DEEPEN_UP_TOKEN = URL_IP + "/worker-union-pm/sts/acquireSchemeDeepenUpToken.action";
/** URL-获取下载方案深化表凭证 */
var URL_ACQUIRE_SCHEME_DEEPEN_DOWN_TOKEN = URL_IP + "/worker-union-pm/sts/acquireSchemeDeepenDownToken.action";
/** URL-获取上传成本深化表凭证 */
var URL_ACQUIRE_COST_DEEPEN_UP_TOKEN = URL_IP + "/worker-union-pm/sts/acquireCostDeepenUpToken.action";
/** URL-获取下载成本深化表凭证 */
var URL_ACQUIRE_COST_DEEPEN_DOWN_TOKEN = URL_IP + "/worker-union-pm/sts/acquireCostDeepenDownToken.action";
/** URL-获取上传经营管理产值文件凭证 */
var URL_ACQUIRE_BUSINESS_CAPACITY_FILE_UP_TOKEN = URL_IP + "/worker-union-pm/sts/acquireBusinessCapacityFileUpToken.action";
/** URL-获取下载经营管理产值文件凭证 */
var URL_ACQUIRE_BUSINESS_CAPACITY_FILE_DOWN_TOKEN = URL_IP + "/worker-union-pm/sts/acquireBusinessCapacityFileDownToken.action";
/** URL-获取上传生产产值文件凭证 */
var URL_ACQUIRE_PRODUCT_CAPACITY_FILE_UP_TOKEN = URL_IP + "/worker-union-pm/sts/acquireProductCapacityFileUpToken.action";
/** URL-获取下载生产产值文件凭证 */
var URL_ACQUIRE_PRODUCT_CAPACITY_FILE_DOWN_TOKEN = URL_IP + "/worker-union-pm/sts/acquireProductCapacityFileDownToken.action";
/** URL-获取上传技术方案凭证 */
var URL_ACQUIRE_TECHNICAL_PROPOSAL_UP_TOKEN = URL_IP + "/worker-union-pm/sts/acquireTechnicalProposalUpToken.action";
/** URL-获取下载技术方案凭证 */
var URL_ACQUIRE_TECHNICAL_PROPOSAL_DOWN_TOKEN = URL_IP + "/worker-union-pm/sts/acquireTechnicalProposalDownToken.action";
/** URL-获取上传结算文件凭证 */
var URL_ACQUIRE_SETTLEMENT_UP_TOKEN = URL_IP + "/worker-union-pm/sts/acquireSettlementUpToken.action";
/** URL-获取下载结算文件凭证 */
var URL_ACQUIRE_SETTLEMENT_DOWN_TOKEN = URL_IP + "/worker-union-pm/sts/acquireSettlementDownToken.action";
/** URL-获取上传竣工资料凭证 */
var URL_ACQUIRE_COMPLETION_DATA_UP_TOKEN = URL_IP + "/worker-union-pm/sts/acquireCompletionDataUpToken.action";
/** URL-获取下载竣工资料凭证 */
var URL_ACQUIRE_COMPLETION_DATA_DOWN_TOKEN = URL_IP + "/worker-union-pm/sts/acquireCompletionDataDownToken.action";
/** URL-获取上传总结报告凭证 */
var URL_ACQUIRE_FINAL_REPORT_UP_TOKEN = URL_IP + "/worker-union-pm/sts/acquireFinalReportUpToken.action";
/** URL-获取下载总结报告凭证 */
var URL_ACQUIRE_FINAL_REPORT_DOWN_TOKEN = URL_IP + "/worker-union-pm/sts/acquireFinalReportDownToken.action";
/** URL-获取内部结算文件下载凭证 */
var URL_ACQUIRE_INTERNAL_SETTLEMENT_DOWN_TOKEN = URL_IP + "/worker-union-pm/sts/acquireInternalSettlementDownToken.action";
/** URL-获取决算条目附件下载凭证 */
var URL_ACQUIRE_FINAL_SETTLEMENT_ATTACHMENT_DOWN_TOKEN = URL_IP + "/worker-union-pm/sts/acquireFinalSettlementAttachmentDownToken.action";
/** URL-获取上传安全记录附件凭证 */
var URL_ACQUIRE_SAFETY_CHECK_ITEM_ATTACHMENT_UP_TOKEN = URL_IP + "/worker-union-pm/sts/acquireSafetyCheckItemAttachmentUpToken.action";
/** URL-获取下载安全记录附件凭证 */
var URL_ACQUIRE_SAFETY_CHECK_ITEM_ATTACHMENT_DOWN_TOKEN = URL_IP + "/worker-union-pm/sts/acquireSafetyCheckItemAttachmentDownToken.action";
/** URL-获取上传质量记录附件凭证 */
var URL_ACQUIRE_QUALITY_CHECK_ITEM_ATTACHMENT_UP_TOKEN = URL_IP + "/worker-union-pm/sts/acquireQualityCheckItemAttachmentUpToken.action";
/** URL-获取下载质量记录附件凭证 */
var URL_ACQUIRE_QUALITY_CHECK_ITEM_ATTACHMENT_DOWN_TOKEN = URL_IP + "/worker-union-pm/sts/acquireQualityCheckItemAttachmentDownToken.action";
/** URL-获取上传团队搭建附件凭证 */
var URL_ACQUIRE_PROJECT_ATTACHMENT_UP_TOKEN = URL_IP + "/worker-union-pm/sts/acquireProjectTeamAttachmentUpToken.action";
/** URL-获取下载团队搭建附件凭证 */
var URL_ACQUIRE_PROJECT_TEAM_ATTACHMENT_DOWN_TOKEN = URL_IP + "/worker-union-pm/sts/acquireProjectTeamAttachmentDownToken.action";


// COST_ANALYSIS
/** URL-获取成本分析表信息 */
var URL_GET_COST_ANALYSIS_UNIQUE_ATTACHMENT = URL_IP + "/worker-union-pm/cost_analysis/getCostAnalysisUniqueAttachment.action";
/** URL-获取成本分析详情 */
var URL_ACQUIRE_COST_ANALYSIS_DETAIL = URL_IP + "/worker-union-pm/cost_analysis/acquireCostAnalysisDetail.action";

// PROJECT_APPLICATION
/** URL-获取项目报建人员信息列表*/
var URL_LIST_PROJECT_APPLICATION_STAFF = URL_IP + "/worker-union-pm/project_application/listProjectApplicationStaff.action";
/** URL-获取项目报建附件信息列表*/
var URL_LIST_PROJECT_APPLICATION_ATTACHMENT = URL_IP + "/worker-union-pm/project_application/listProjectApplicationAttachment.action";

// TARGET
/** URL-获取目标协议书附件信息*/
var URL_GET_TARGET_UNIQUE_ATTACHMENT = URL_IP + "/worker-union-pm/target/getTargetUniqueAttachment.action";
/** URL-获取目标管理详情*/
var URL_GET_TARGET_DETAIL = URL_IP + "/worker-union-pm/target/getTargetDetail.action";

// PROJECT_TEAM
/** URL-获取项目组成员信息*/
var URL_LIST_PROJECT_TEAM_STAFF = URL_IP + "/worker-union-pm/project_team/listProjectTeamStaff.action";
/** URL-增加项目组成员*/
var URL_ADD_STAFF_TO_PROJECT_TEAM = URL_IP + "/worker-union-pm/project_team/addStaffToProjectTeam.action";
/** URL-删除项目组成员*/
var URL_REMOVE_STAFF_FROM_PROJECT_TEAM = URL_IP + "/worker-union-pm/project_team/removeStaffFromProjectTeam.action";
/** URL-获取团队搭建附件列表信息*/
var URL_LIST_PROJECT_TEAM_ATTACHMENT = URL_IP + "/worker-union-pm/project_team/listProjectTeamAttachment.action";
/** URL- 删除团队搭建附件*/
var URL_REMOVE_PROJECT_TEAM_ATTACHMENT = URL_IP + "/worker-union-pm/project_team/removeProjectTeamAttachment.action";

// SCHEME_DEEPEN
/** URL-获取方案深化审核信息列表*/
var URL_LIST_SCHEME_DEEPEN_ATTACHMENT = URL_IP + "/worker-union-pm/scheme_deepen/listSchemeDeepenAttachment.action";
/** URL-删除方案深化表*/
var URL_DELETE_SCHEME_DEEPEN_ATTACHMENT_BY_ID = URL_IP + "/worker-union-pm/scheme_deepen/removeSchemeDeepenAttachmentById.action";

// COST_ANALYSIS_DEEPEN
/** URL-获取成本深化表*/
var URL_GET_COST_ANALYSIS_DEEPEN_UNIQUE_ATTACHMENT = URL_IP + "/worker-union-pm/cost_analysis_deepen/getCostAnalysisDeepenUniqueAttachment.action";
/** URL-删除成本深化表*/
var URL_REMOVE_COST_ANALYSIS_DEEPEN_UNIQUE_ATTACHMENT = URL_IP + "/worker-union-pm/cost_analysis_deepen/removeCostAnalysisDeepenUniqueAttachment.action";
/** URL-获取成本深化详情*/
var URL_ACQUIRE_COST_ANALYSIS_DEEPEN_DETAIL = URL_IP + "/worker-union-pm/cost_analysis_deepen/acquireCostAnalysisDeepenDetail.action";
/** URL-增加成本深化项*/
var URL_ADD_COST_ANALYSIS_ITEM_DEEPEN = URL_IP + "/worker-union-pm/cost_analysis_deepen/addCostAnalysisItemDeepen.action";
/** URL-获取成本深化项*/
var URL_GET_COST_ANALYSIS_ITEM_DEEPEN_BY_ID = URL_IP + "/worker-union-pm/cost_analysis_deepen/getCostAnalysisItemDeepenById.action";
/** URL-修改成本深化项*/
var URL_MODIFY_COST_ANALYSIS_ITEM_DEEPEN_BY_ID = URL_IP + "/worker-union-pm/cost_analysis_deepen/modifyCostAnalysisItemDeepenById.action";
/** URL-删除成本深化项*/
var URL_REMOVE_COST_ANALYSIS_ITEM_DEEPEN_BY_ID = URL_IP + "/worker-union-pm/cost_analysis_deepen/removeCostAnalysisItemDeepenById.action";
/** URL-获取成本深化项列表*/
var URL_LIST_COST_ANALYSIS_ITEM_DEEPEN = URL_IP + "/worker-union-pm/cost_analysis_deepen/listCostAnalysisItemDeepen.action";

// FUND_PLAN
/** URL-获取资金计划项列表*/
var URL_LIST_FUND_PLAN_ITEM = URL_IP + "/worker-union-pm/fund_plan/listFundPlanItem.action";
/** URL-获取资金计划项*/
var URL_GET_FUND_PLAN_ITEM_BY_ID = URL_IP + "/worker-union-pm/fund_plan/getFundPlanItemById.action";
/** URL-增加资金计划项*/
var URL_ADD_FUND_PLAN_ITEM = URL_IP + "/worker-union-pm/fund_plan/addFundPlanItem.action";
/** URL-修改资金计划项*/
var URL_MODIFY_FUND_PLAN_ITEM_BY_ID = URL_IP + "/worker-union-pm/fund_plan/modifyFundPlanItemById.action";
/** URL-删除资金计划项*/
var URL_REMOVE_FUND_PLAN_ITEM_BY_ID = URL_IP + "/worker-union-pm/fund_plan/removeFundPlanItemById.action";
/** URL-获取简要资金计划项列表*/
var URL_LIST_SIMPLE_FUND_PLAN_ITEM = URL_IP + "/worker-union-pm/fund_plan/listSimpleFundPlanItem.action";

// PURCHASE_ITEM
/** URL-获取采购条目列表*/
var URL_LIST_PURCHASE_ITEM = URL_IP + "/worker-union-pm/purchase_item/listPurchaseItem.action";

// BUSINESS_CAPACITY
/** URL-获取最新的经营产值文件信息*/
var URL_GET_LATEST_BUSINESS_CAPACITY_ATTACHMENT = URL_IP + "/worker-union-pm/business_capacity/getLatestBusinessCapacityAttachment.action";
/** URL-分页获取经营产值项列表信息*/
var URL_PAGE_BUSINESS_CAPACITY_ITEM = URL_IP + "/worker-union-pm/business_capacity/pageBusinessCapacityItem.action";
/** URL-添加某月经营产值*/
var URL_ADD_BUSINESS_CAPACITY_ITEM = URL_IP + "/worker-union-pm/business_capacity/addBusinessCapacityItem.action";
/** URL-修改某月经营产值*/
var URL_MODIFY_BUSINESS_CAPACITY_ITEM = URL_IP + "/worker-union-pm/business_capacity/modifyBusinessCapacityItem.action";
/** URL-删除某月经营产值*/
var URL_REMOVE_BUSINESS_CAPACITY_ITEM = URL_IP + "/worker-union-pm/business_capacity/removeBusinessCapacityItem.action";
/** URL-分页获取经营产值文件列表信息*/
var URL_PAGE_BUSINESS_CAPACITY_ATTACHMENT = URL_IP + "/worker-union-pm/business_capacity/pageBusinessCapacityAttachment.action";
/** URL-删除经营管理产值文件*/
var URL_REMOVE_BUSINESS_CAPACITY_ATTACHMENT = URL_IP + "/worker-union-pm/business_capacity/removeBusinessCapacityAttachment.action";

// PRODUCT_CAPACITY
/** URL-获取最新的生产产值文件信息*/
var URL_GET_LATEST_PRODUCT_CAPACITY_ATTACHMENT = URL_IP + "/worker-union-pm/product_capacity/getLatestProductCapacityAttachment.action";
/** URL-分页获取生产产值项列表信息*/
var URL_PAGE_PRODUCT_CAPACITY_ITEM = URL_IP + "/worker-union-pm/product_capacity/pageProductCapacityItem.action";
/** URL-添加某月生产产值*/
var URL_ADD_PRODUCT_CAPACITY_ITEM = URL_IP + "/worker-union-pm/product_capacity/addProductCapacityItem.action";
/** URL-修改某月生产产值*/
var URL_MODIFY_PRODUCT_CAPACITY_ITEM = URL_IP + "/worker-union-pm/product_capacity/modifyProductCapacityItem.action";
/** URL-删除某月生产产值*/
var URL_REMOVE_PRODUCT_CAPACITY_ITEM = URL_IP + "/worker-union-pm/product_capacity/removeProductCapacityItem.action";
/** URL-分页获取生产产值文件列表信息*/
var URL_PAGE_PRODUCT_CAPACITY_ATTACHMENT = URL_IP + "/worker-union-pm/product_capacity/pageProductCapacityAttachment.action";
/** URL-删除生产管理产值文件*/
var URL_REMOVE_PRODUCT_CAPACITY_ATTACHMENT = URL_IP + "/worker-union-pm/product_capacity/removeProductCapacityAttachment.action";

// TECHNICAL_PROPOSAL
/** URL-获取技术方案审核信息列表*/
var URL_LIST_TECHNICAL_PROPOSAL_ATTACHMENT = URL_IP + "/worker-union-pm/technical_proposal/listTechnicalProposalAttachment.action";
/** URL-删除技术方案*/
var URL_REMOVE_TECHNICAL_PROPOSAL_ATTACHMENT_BY_ID = URL_IP + "/worker-union-pm/technical_proposal/removeTechnicalProposalAttachmentById.action";

// RESOURCE_IMPLEMENT
/** URL-获取资源落实项列表*/
var URL_LIST_RESOURCE_IMPLEMENT_ITEM = URL_IP + "/worker-union-pm/resource_implement/listResourceImplementItem.action";
/** URL-删除资源落实项*/
var URL_REMOVE_RESOURCE_IMPLEMENT_ITEM_BY_ID = URL_IP + "/worker-union-pm/resource_implement/removeResourceImplementItemById.action";
/** URL-增加资源落实项*/
var URL_ADD_RESOURCE_IMPLEMENT_ITEM = URL_IP + "/worker-union-pm/resource_implement/addResourceImplementItem.action";
/** URL-获取资源落实项*/
var URL_GET_RESOURCE_IMPLEMENT_ITEM_BY_ID = URL_IP + "/worker-union-pm/resource_implement/getResourceImplementItemById.action";
/** URL-修改资源落实项*/
var URL_MODIFY_RESOURCE_IMPLEMENT_ITEM_BY_ID = URL_IP + "/worker-union-pm/resource_implement/modifyResourceImplementItemById.action";
/** URL-简要获取资源落实项列表*/
var URL_LIST_SIMPLE_RESOURCE_IMPLEMENT_ITEM = URL_IP + "/worker-union-pm/resource_implement/listSimpleResourceImplementItem.action";

// CONTRACT
/** URL-获取指定项目简要合同列表*/
var URL_LIST_SIMPLE_CONTRACT_ITEM_BY_PROJECT_ID = URL_IP + "/worker-union-pm/contract/listSimpleContractItemByProjectId.action";

// SUBCONTRACTOR
/** URL-获取简要分包商列表*/
var URL_LIST_SIMPLE_SUBCONTRACTOR_ITEM = URL_IP + "/worker-union-pm/subcontractor/listSimpleSubcontractorItem.action";

// SETTLEMENT
/** URL-获取最新的结算文件信息*/
var URL_GET_LATEST_SETTLEMENT_ATTACHMENT = URL_IP + "/worker-union-pm/settlement/getLatestSettlementAttachment.action";
/** URL-分页获取结算项列表*/
var URL_PAGE_SETTLEMENT_ITEM = URL_IP + "/worker-union-pm/settlement/pageSettlementItem.action";
/** URL-删除结算项*/
var URL_REMOVE_SETTLEMENT_ITEM_BY_ID = URL_IP + "/worker-union-pm/settlement/removeSettlementItemById.action";
/** URL-增加结算项*/
var URL_ADD_SETTLEMENT_ITEM = URL_IP + "/worker-union-pm/settlement/addSettlementItem.action";
/** URL-获取结算项*/
var URL_GET_SETTLEMENT_ITEM_BY_ID = URL_IP + "/worker-union-pm/settlement/getSettlementItemById.action";
/** URL-修改结算项*/
var URL_MODIFY_SETTLEMENT_ITEM_BY_ID = URL_IP + "/worker-union-pm/settlement/modifySettlementItemById.action";
/** URL-分页获取结算文件审核信息列表*/
var URL_PAGE_SETTLEMENT_ATTACHMENT = URL_IP + "/worker-union-pm/settlement/pageSettlementAttachment.action";
/** URL-删除结算文件*/
var URL_REMOVE_SETTLEMENT_ATTACHMENT_BY_ID = URL_IP + "/worker-union-pm/settlement/removeSettlementAttachmentById.action";

// MATERIAL
/** URL-分页获取仓库材料列表*/
var URL_PAGE_MATERIAL_BY_FUZZY = URL_IP + "/worker-union-pm/material/pageMaterialByFuzzy.action";
/** URL-分页获取仓库材料出入库批次列表*/
var URL_PAGE_MATERIAL_ORDER_BY_FUZZY = URL_IP + "/worker-union-pm/material/pageMaterialOrderByFuzzy.action";
/** URL-材料入库*/
var URL_IMPORT_MATERIAL = URL_IP + "/worker-union-pm/material/importMaterial.action";
/** URL-材料出库*/
var URL_EXPORT_MATERIAL = URL_IP + "/worker-union-pm/material/exportMaterial.action";
/** URL-转库申请*/
var URL_APPLY_TRANSFER_MATERIAL = URL_IP + "/worker-union-pm/material/applyTransferMaterial.action";
/** URL-获取企业材料编号对应的材料信息-出库*/
var URL_GET_EXPORT_MATERIAL_BY_NUM = URL_IP + "/worker-union-pm/material/getExportMaterialByNum.action";
/** URL-分页获取指定材料出入库历史记录*/
var URL_PAGE_MATERIAL_ORDER_BY_MATERIAL_NUM = URL_IP + "/worker-union-pm/material/pageMaterialOrderByMaterialNum.action";

// MATERIAL_TYPE
/** URL-获取企业材料编号对应的材料类型信息-入库*/
var URL_GET_IMPORT_MATERIAL_TYPE_BY_NUM = URL_IP + "/worker-union-pm/material_type/getImportMaterialTypeByNum.action";

// COMPLETION_DATA
/** URL-获取竣工资料审核信息列表*/
var URL_LIST_COMPLETION_DATA_ATTACHMENT = URL_IP + "/worker-union-pm/completion_data/listCompletionDataAttachment.action";
/** URL-删除竣工资料*/
var URL_REMOVE_COMPLETION_DATA_ATTACHMENT_BY_ID = URL_IP + "/worker-union-pm/completion_data/removeCompletionDataAttachmentById.action";

// FINAL_REPORT
/** URL-获取总结报告审核信息列表*/
var URL_LIST_FINAL_REPORT_ATTACHMENT = URL_IP + "/worker-union-pm/final_report/listFinalReportAttachment.action";
/** URL-删除总结报告*/
var URL_REMOVE_FINAL_REPORT_ATTACHMENT_BY_ID = URL_IP + "/worker-union-pm/final_report/removeFinalReportAttachmentById.action";

// INTERNAL_SETTLEMENT
/** URL-获取最新的内部结算文件信息*/
var URL_GET_LATEST_INTERNAL_SETTLEMENT_ATTACHMENT = URL_IP + "/worker-union-pm/internal_settlement/getLatestInternalSettlementAttachment.action";
/** URL-获取项目完成情况信息*/
var URL_GET_INTERNAL_SETTLEMENT_DETAIL = URL_IP + "/worker-union-pm/internal_settlement/getInternalSettlementDetail.action";
/** URL-获取分页内部结算文件列表*/
var URL_PAGE_INTERNAL_SETTLEMENT_ATTACHMENT = URL_IP + "/worker-union-pm/internal_settlement/pageInternalSettlementAttachment.action";

// FINAL_SETTLEMENT
/** URL-获取竣工决算条目列表*/
var URL_LIST_FINAL_SETTLEMENT_ITEM = URL_IP + "/worker-union-pm/final_settlement/listFinalSettlementItem.action";

// SAFETY_CHECK
/** URL-分页获取项目安全检查项列表*/
var URL_PAGE_SAFETY_CHECK_ITEM = URL_IP + "/worker-union-pm/safety_check/pageSafetyCheckItem.action";
/** URL-获取安全检查记录详情*/
var URL_GET_SAFETY_CHECK_ITEM_DETAIL = URL_IP + "/worker-union-pm/safety_check/getSafetyCheckItemDetail.action";
/** URL-删除安全检查记录下的附件*/
var URL_REMOVE_SAFETY_CHECK_ITEM_ATTACHMENT_BY_ID = URL_IP + "/worker-union-pm/safety_check/removeSafetyCheckItemAttachmentById.action";
/** URL-申请审批安全检查记录*/
var URL_APPLY_SAFETY_CHECK_ITEM_VERIFY = URL_IP + "/worker-union-pm/safety_check/applySafetyCheckItemVerify.action";

// QUALITY_CHECK
/** URL-分页获取项目质量检查项列表*/
var URL_PAGE_QUALITY_CHECK_ITEM = URL_IP + "/worker-union-pm/quality_check/pageQualityCheckItem.action";
/** URL-获取质量检查记录详情*/
var URL_GET_QUALITY_CHECK_ITEM_DETAIL = URL_IP + "/worker-union-pm/quality_check/getQualityCheckItemDetail.action";
/** URL-申请审批质量检查记录*/
var URL_APPLY_QUALITY_CHECK_ITEM_VERIFY = URL_IP + "/worker-union-pm/quality_check/applyQualityCheckItemVerify.action";
/** URL-删除质量检查记录下的附件*/
var URL_REMOVE_QUALITY_CHECK_ITEM_ATTACHMENT_BY_ID = URL_IP + "/worker-union-pm/quality_check/removeQualityCheckItemAttachmentById.action";

