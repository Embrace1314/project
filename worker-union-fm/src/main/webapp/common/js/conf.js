var URL_IP = "";


var HTML_HOME = "/worker-union-fm/home.html";
var HTML_LOGIN = "/worker-union-fm/login.html";
var PM_LOGIN = window.location.protocol + "//" + window.location.host + "/worker-union-pm/login.html";

/** 成本项类型 */
var COST_ANALYSIS_ITEM_TYPE = {
	"DIRECT_COST" : "直接成本",
	"INDIRECT_COST" : "间接成本",
	"SUBCONTRACT_COST" : "指定分包成本",
	"TAX_COST" : "税额"
};

/** 性别 */
var SEX = {
	"MALE" : "男",
	"FEMALE" : "女"
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

var AUDIT_STATUS_UNDETERMINED = "UNDETERMINED";
/** 转库类型 */
var MATERIAL_TRANSFER_APPLICATION_TYPE = {
	"TO_PROJECT" : "项目组",
	"TO_HEADQUARTERS" : "总部仓库"
};
var MATERIAL_TRANSFER_APPLICATION_TYPE_TO_HEADQUARTERS = "TO_HEADQUARTERS";
/** 转库状态 */
var MATERIAL_TRANSFER_APPLICATION_STATUS = {
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
/** 待办事项类型 */
var SCHEDULE_TYPE = {
	"SCHEME_DEEPEN" : "方案深化审核",
	"TECHNICAL_PROPOSAL" : "技术方案审核",
	"SETTLEMENT_FILE" : "结算文件审核",
	"COMPLETION_DATA" : "竣工资料审核",
	"FINAL_REPORT" : "总结报告审核",
	"SAFETY_CHECK_ITEM" : "安全检查审批",
	"QUALITY_CHECK_ITEM" : "质量检查审批"
};
/** 待办事项类型 */
var SCHEDULE_URL = {
	"SCHEME_DEEPEN" : "/worker-union-fm/project/prepare/plan_deepen.html",
	"TECHNICAL_PROPOSAL" : "/worker-union-fm/project/implement/technical_manage.html",
	"SETTLEMENT_FILE" : "/worker-union-fm/project/implement/settlement_manage_inquiry.html",
	"COMPLETION_DATA" : "/worker-union-fm/project/complete/complete_check.html",
	"FINAL_REPORT" : "/worker-union-fm/project/complete/project_summary.html",
	"SAFETY_CHECK_ITEM" : "/worker-union-fm/project/implement/security_record_modify.html",
	"QUALITY_CHECK_ITEM" : "/worker-union-fm/project/implement/quality_record_modify.html"
};
/** 企业ID */
var CID = "1";

/** 项目状态-进行中 */
var PROJECT_TYPE_GOING = "GOING";
/** 项目状态-已归档 */
var PROJECT_TYPE_ARCHIVED = "ARCHIVED";
/** 所有项目状态 */
var PROJECT_TYPE_ALL = "";

/** 解体状态-运行中 */
var COLLAPSED_TYPE_GOING = "GOING";
/** 解体状态-已解体 */
var COLLAPSED_TYPE_COLLAPSED = "COLLAPSED";

/** COOKIE STAFF NAME */
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
var URL_LOGIN = URL_IP+"/worker-union-fm/staff/login.action?staff.companyId=" + CID;
/** URL-获取授权信息 */
var URL_CHECK_AUTHORIZATION = URL_IP+"/worker-union-fm/staff/checkAuthorization.action";
/** URL-新增员工信息 */
var URL_ADD_STAFF = URL_IP+"/worker-union-fm/staff/addStaff.action";
/** URL-修改员工信息 */
var URL_MODIFY_STAFF_BY_ID = URL_IP+"/worker-union-fm/staff/modifyStaffById.action";
/** URL-删除员工信息 */
var URL_DELETE_STAFF_BY_ID = URL_IP+"/worker-union-fm/staff/removeStaffById.action";
/** URL-获取员工信息详情 */
var URL_GET_STAFF_DETAIL_BY_ID = URL_IP+"/worker-union-fm/staff/getStaffDetailById.action";
/** URL-根据模糊条件查询员工信息，分页 */
var URL_GET_PAGE_STAFF_BY_FUZZY = URL_IP+"/worker-union-fm/staff/pageStaffByFuzzy.action";
/** URL-修改密码 */
var URL_CHANGE_PASSWORD = URL_IP+"/worker-union-fm/staff/changePassword.action";
/** URL-退出系统 */
var URL_LOGOUT = URL_IP+"/worker-union-fm/staff/logout.action";

// PROJECT
/** URL-获取分页的项目列表信息 */
var URL_GET_PAGE_ALL_PROJECTS = URL_IP+"/worker-union-fm/project/getPageAllProjects.action";
/** URL-根据项目状态获取分页的项目列表信息 */
var URL_GET_PAGE_PROJECTS_BY_STATUS = URL_IP+"/worker-union-fm/project/getPageProjectsByStatus.action";
/** URL-根据立项年份获取分页的项目列表信息 */
var URL_GET_PAGE_PROJECTS_BY_YEAR = URL_IP+"/worker-union-fm/project/getPageProjectsByYear.action";
/** URL-根据项目状态及立项年份获取分页的项目列表信息 */
var URL_GET_PAGE_PROJECTS_BY_STATUS_AND_YEAR = URL_IP+"/worker-union-fm/project/getPageProjectsByStatusAndYear.action";
/** URL-根据项目ID获取项目信息 */
var URL_GET_PROJECT_DETAIL_BY_ID = URL_IP+"/worker-union-fm/project/getProjectDetailById.action";
/** URL-获取企业下项目简要信息 */
var URL_LIST_SIMPLE_PROJECT = URL_IP+"/worker-union-fm/project/listSimpleProject.action";
/** URL-获取项目信息 */
var URL_GET_PROJECT_BY_ID = URL_IP+"/worker-union-fm/project/getProjectById.action";
/** URL-项目部解体 */
var URL_COLLAPSE_PROJECT_BY_ID = URL_IP+"/worker-union-fm/project/collapseProjectById.action";
/** URL-项目归档 */
var URL_ARCHIVE_PROJECT_BY_ID = URL_IP+"/worker-union-fm/project/archiveProjectById.action";
/** URL-项目解除归档 */
var URL_RELEASE_ARCHIVE_PROJECT_BY_ID = URL_IP+"/worker-union-fm/project/releaseArchiveProjectById.action";
/** URL-获取未归档企业项目列表 */
var URL_LIST_UNARCHIVED_PROJECT = URL_IP+"/worker-union-fm/project/listUnarchivedProject.action";
/** URL-获取项目代办事项 */
var URL_LIST_SCHEDULE = URL_IP + "/worker-union-fm/project/listSchedule.action";


// PERMISSION
/** URL-获取企业角色列表 */
var URL_LIST_ROLE_BY_COMPANY = URL_IP+"/worker-union-fm/permission/listRoleByCompany.action";
/** URL-获取用户拥有的角色 */
var URL_LIST_ROLE_BY_STAFF = URL_IP+"/worker-union-fm/permission/listRoleByStaff.action";
/** URL-配置用户的角色 */
var URL_SAVE_STAFF_ROLE = URL_IP+"/worker-union-fm/permission/saveStaffRole.action";


// STS
/** URL-获取上传中标通知书授权信息 */
var URL_ACQUIRE_LETTER_OF_ACCEPTANCE_UP_TOKEN = URL_IP+"/worker-union-fm/sts/acquireLetterOfAcceptanceUpToken.action";
/** URL-获取下载中标通知书授权信息 */
var URL_ACQUIRE_LETTER_OF_ACCEPTANCE_DOWN_TOKEN = URL_IP+"/worker-union-fm/sts/acquireLetterOfAcceptanceDownToken.action";
/** URL-获取上传成本分析授权信息 */
var URL_ACQUIRE_COST_ANALYSIS_UP_TOKEN = URL_IP+"/worker-union-fm/sts/acquireCostAnalysisUpToken.action";
/** URL-获取下载成本分析授权信息 */
var URL_ACQUIRE_COST_ANALYSIS_DOWN_TOKEN = URL_IP+"/worker-union-fm/sts/acquireCostAnalysisDownToken.action";
/** URL-获取下载合同备案表凭证 */
var URL_ACQUIRE_CONTRACT_RECORD_DOWN_TOKEN = URL_IP + "/worker-union-fm/sts/acquireContractRecordDownToken.action";
/** URL-获取下载质监通知书凭证 */
var URL_ACQUIRE_QUALITY_INSPECT_NOTICE_DOWN_TOKEN = URL_IP + "/worker-union-fm/sts/acquireQualityInspectNoticeDownToken.action";
/** URL-获取下载安监登记表凭证 */
var URL_ACQUIRE_SAFETY_SUPERVISION_FORM_DOWN_TOKEN = URL_IP + "/worker-union-fm/sts/acquireSafetySupervisionFormDownToken.action";
/** URL-获取下载施工许可证凭证 */
var URL_ACQUIRE_CONSTRUCTION_PERMIT_DOWN_TOKEN = URL_IP + "/worker-union-fm/sts/acquireConstructionPermitDownToken.action";
/** URL-获取上传目标协议书凭证 */
var URL_ACQUIRE_AGREEMENT_OF_TARGET_UP_TOKEN = URL_IP + "/worker-union-fm/sts/acquireAgreementOfTargetUpToken.action";
/** URL-获取下载目标协议书凭证 */
var URL_ACQUIRE_AGREEMENT_OF_TARGET_DOWN_TOKEN = URL_IP + "/worker-union-fm/sts/acquireAgreementOfTargetDownToken.action";
/** URL-获取上传方案深化表凭证 */
var URL_ACQUIRE_SCHEME_DEEPEN_UP_TOKEN = URL_IP + "/worker-union-fm/sts/acquireSchemeDeepenUpToken.action";
/** URL-获取下载方案深化表凭证 */
var URL_ACQUIRE_SCHEME_DEEPEN_DOWN_TOKEN = URL_IP + "/worker-union-fm/sts/acquireSchemeDeepenDownToken.action";
/** URL-获取上传成本深化表凭证 */
var URL_ACQUIRE_COST_DEEPEN_UP_TOKEN = URL_IP + "/worker-union-fm/sts/acquireCostDeepenUpToken.action";
/** URL-获取下载成本深化表凭证 */
var URL_ACQUIRE_COST_DEEPEN_DOWN_TOKEN = URL_IP + "/worker-union-fm/sts/acquireCostDeepenDownToken.action";
/** URL-获取上传经营管理产值文件凭证 */
var URL_ACQUIRE_BUSINESS_CAPACITY_FILE_UP_TOKEN = URL_IP + "/worker-union-fm/sts/acquireBusinessCapacityFileUpToken.action";
/** URL-获取下载经营管理产值文件凭证 */
var URL_ACQUIRE_BUSINESS_CAPACITY_FILE_DOWN_TOKEN = URL_IP + "/worker-union-fm/sts/acquireBusinessCapacityFileDownToken.action";
/** URL-获取上传生产产值文件凭证 */
var URL_ACQUIRE_PRODUCT_CAPACITY_FILE_UP_TOKEN = URL_IP + "/worker-union-fm/sts/acquireProductCapacityFileUpToken.action";
/** URL-获取下载生产产值文件凭证 */
var URL_ACQUIRE_PRODUCT_CAPACITY_FILE_DOWN_TOKEN = URL_IP + "/worker-union-fm/sts/acquireProductCapacityFileDownToken.action";
/** URL-获取上传技术方案凭证 */
var URL_ACQUIRE_TECHNICAL_PROPOSAL_UP_TOKEN = URL_IP + "/worker-union-fm/sts/acquireTechnicalProposalUpToken.action";
/** URL-获取下载技术方案凭证 */
var URL_ACQUIRE_TECHNICAL_PROPOSAL_DOWN_TOKEN = URL_IP + "/worker-union-fm/sts/acquireTechnicalProposalDownToken.action";
/** URL-获取上传结算文件凭证 */
var URL_ACQUIRE_SETTLEMENT_UP_TOKEN = URL_IP + "/worker-union-fm/sts/acquireSettlementUpToken.action";
/** URL-获取下载结算文件凭证 */
var URL_ACQUIRE_SETTLEMENT_DOWN_TOKEN = URL_IP + "/worker-union-fm/sts/acquireSettlementDownToken.action";
/** URL-获取上传竣工资料凭证 */
var URL_ACQUIRE_COMPLETION_DATA_UP_TOKEN = URL_IP + "/worker-union-fm/sts/acquireCompletionDataUpToken.action";
/** URL-获取下载竣工资料凭证 */
var URL_ACQUIRE_COMPLETION_DATA_DOWN_TOKEN = URL_IP + "/worker-union-fm/sts/acquireCompletionDataDownToken.action";
/** URL-获取上传总结报告凭证 */
var URL_ACQUIRE_FINAL_REPORT_UP_TOKEN = URL_IP + "/worker-union-fm/sts/acquireFinalReportUpToken.action";
/** URL-获取下载总结报告凭证 */
var URL_ACQUIRE_FINAL_REPORT_DOWN_TOKEN = URL_IP + "/worker-union-fm/sts/acquireFinalReportDownToken.action";
/** URL-获取内部结算文件上传凭证 */
var URL_ACQUIRE_INTERNAL_SETTLEMENT_UP_TOKEN = URL_IP + "/worker-union-fm/sts/acquireInternalSettlementUpToken.action";
/** URL-获取内部结算文件下载凭证 */
var URL_ACQUIRE_INTERNAL_SETTLEMENT_DOWN_TOKEN = URL_IP + "/worker-union-fm/sts/acquireInternalSettlementDownToken.action";
/** URL-获取决算条目附件上传凭证 */
var URL_ACQUIRE_FINAL_SETTLEMENT_ATTACHMENT_UP_TOKEN = URL_IP + "/worker-union-fm/sts/acquireFinalSettlementItemAttachmentUpToken.action";
/** URL-获取决算条目附件下载凭证 */
var URL_ACQUIRE_FINAL_SETTLEMENT_ATTACHMENT_DOWN_TOKEN = URL_IP + "/worker-union-fm/sts/acquireFinalSettlementAttachmentDownToken.action";
/** URL-获取上传安全记录附件凭证 */
var URL_ACQUIRE_SAFETY_CHECK_ITEM_ATTACHMENT_UP_TOKEN = URL_IP + "/worker-union-fm/sts/acquireSafetyCheckItemAttachmentUpToken.action";
/** URL-获取下载安全记录附件凭证 */
var URL_ACQUIRE_SAFETY_CHECK_ITEM_ATTACHMENT_DOWN_TOKEN = URL_IP + "/worker-union-fm/sts/acquireSafetyCheckItemAttachmentDownToken.action";
/** URL-获取上传质量记录附件凭证 */
var URL_ACQUIRE_QUALITY_CHECK_ITEM_ATTACHMENT_UP_TOKEN = URL_IP + "/worker-union-fm/sts/acquireQualityCheckItemAttachmentUpToken.action";
/** URL-获取下载质量记录附件凭证 */
var URL_ACQUIRE_QUALITY_CHECK_ITEM_ATTACHMENT_DOWN_TOKEN = URL_IP + "/worker-union-fm/sts/acquireQualityCheckItemAttachmentDownToken.action";
/** URL-获取上传供应商附件凭证 */
var URL_ACQUIRE_SUPPLIER_ATTACHMENT_UP_TOKEN = URL_IP + "/worker-union-fm/sts/acquireSupplierAttachmentUpToken.action";
/** URL-获取下载供应商附件凭证 */
var URL_ACQUIRE_SUPPLIER_ATTACHMENT_DOWN_TOKEN = URL_IP + "/worker-union-fm/sts/acquireSupplierAttachmentDownToken.action";
/** URL-获取上传分包商附件凭证 */
var URL_ACQUIRE_SUBCONTRACTOR_ATTACHMENT_UP_TOKEN = URL_IP + "/worker-union-fm/sts/acquireSubcontractorAttachmentUpToken.action";
/** URL-获取下载分包商附件凭证 */
var URL_ACQUIRE_SUBCONTRACTOR_ATTACHMENT_DOWN_TOKEN = URL_IP + "/worker-union-fm/sts/acquireSubcontractorAttachmentDownToken.action";
/** URL-获取上传合同附件凭证 */
var URL_ACQUIRE_CONTRACT_ATTACHMENT_UP_TOKEN = URL_IP + "/worker-union-fm/sts/acquireContractAttachmentUpToken.action";
/** URL-获取下载合同附件凭证 */
var URL_ACQUIRE_CONTRACT_ATTACHMENT_DOWN_TOKEN = URL_IP + "/worker-union-fm/sts/acquireContractAttachmentDownToken.action";
/** URL-获取上传材料类型附件凭证 */
var URL_ACQUIRE_MATERIAL_TYPE_ATTACHMENT_UP_TOKEN = URL_IP + "/worker-union-fm/sts/acquireMaterialTypeAttachmentUpToken.action";
/** URL-获取下载材料类型附件凭证 */
var URL_ACQUIRE_MATERIAL_TYPE_ATTACHMENT_DOWN_TOKEN = URL_IP + "/worker-union-fm/sts/acquireMaterialTypeAttachmentDownToken.action";
/** URL-获取上传合同备案表凭证 */
var URL_ACQUIRE_CONTRACT_RECORD_UP_TOKEN = URL_IP + "/worker-union-fm/sts/acquireContractRecordUpToken.action";
/** URL-获取上传质检通知书凭证 */
var URL_ACQUIRE_QUALITY_INSPECT_NOTICE_UP_TOKEN = URL_IP + "/worker-union-fm/sts/acquireQualityInspectNoticeUpToken.action";
/** URL-获取上传安监登记表凭证 */
var URL_ACQUIRE_SAFETY_SUPERVISION_FORM_UP_TOKEN = URL_IP + "/worker-union-fm/sts/acquireSafetySupervisionFormUpToken.action";
/** URL-获取上传施工许可证凭证 */
var URL_ACQUIRE_CONSTRUCTION_PERMIT_UP_TOKEN = URL_IP + "/worker-union-fm/sts/acquireConstructionPermitUpToken.action";
/** URL-获取下载证书附件凭证 */
var URL_ACQUIRE_CERTIFICATE_ATTACHMENT_DOWN_TOKEN = URL_IP + "/worker-union-fm/sts/acquireCertificateAttachmentDownToken.action";
/** URL-获取上传证书附件凭证 */
var URL_ACQUIRE_CERTIFICATE_ATTACHMENT_UP_TOKEN = URL_IP + "/worker-union-fm/sts/acquireCertificateAttachmentUpToken.action";
/** URL-获取下载团队成员附件凭证 */
var URL_ACQUIRE_PROJECT_TEAM_ATTACHMENT_DOWN_TOKEN = URL_IP + "/worker-union-fm/sts/acquireProjectTeamAttachmentDownToken.action";

// COST_ANALYSIS
/** URL-获取成本分析表信息 */
var URL_GET_COST_ANALYSIS_UNIQUE_ATTACHMENT = URL_IP+"/worker-union-fm/cost_analysis/getCostAnalysisUniqueAttachment.action";
/** URL-删除成本分析表 */
var URL_DELETE_COST_ANALYSIS_UNIQUE_ATTACHMENT = URL_IP+"/worker-union-fm/cost_analysis/removeCostAnalysisUniqueAttachment.action";
/** URL-获取成本分析详情 */
var URL_ACQUIRE_COST_ANALYSIS_DETAIL = URL_IP+"/worker-union-fm/cost_analysis/acquireCostAnalysisDetail.action";
/** URL-新增成本分析项 */
var URL_ADD_COST_ANALYSIS_ITEM = URL_IP+"/worker-union-fm/cost_analysis/addCostAnalysisItem.action";
/** URL-修改成本分析项 */
var URL_MODIFY_COST_ANALYSIS_ITEM = URL_IP+"/worker-union-fm/cost_analysis/modifyCostAnalysisItem.action";
/** URL-删除成本分析项 */
var URL_REMOVE_COST_ANALYSIS_ITEM = URL_IP+"/worker-union-fm/cost_analysis/removeCostAnalysisItem.action";
/** URL-获取成本分析项 */
var URL_GET_COST_ANALYSIS_ITEM_BY_ID = URL_IP+"/worker-union-fm/cost_analysis/getCostAnalysisItemById.action";

// PROJECT_APPLICATION
/** URL-获取项目报建人员信息列表 */
var URL_LIST_PROJECT_APPLICATION_STAFF = URL_IP + "/worker-union-fm/project_application/listProjectApplicationStaff.action";
/** URL-增加项目报建成员 */
var URL_ADD_STAFF_TO_PROJECT_APPLICATION = URL_IP + "/worker-union-fm/project_application/addStaffToProjectApplication.action";
/** URL-删除项目报建成员 */
var URL_REMOVE_STAFF_FORM_PROJECT_APPLICATION = URL_IP + "/worker-union-fm/project_application/removeStaffFromProjectApplication.action";
/** URL-获取项目报建附件信息列表 */
var URL_LIST_PROJECT_APPLICATION_ATTACHMENT = URL_IP + "/worker-union-fm/project_application/listProjectApplicationAttachment.action";
/** URL-删除合同备案表凭证 */
var URL_REMOVE_CONTRACT_RECORD_ATTACHMENT = URL_IP + "/worker-union-fm/project_application/removeContractRecordAttachment.action";
/** URL-删除质检通知书凭证 */
var URL_REMOVE_QUALITY_INSPECT_NOTICE_ATTACHMENT = URL_IP + "/worker-union-fm/project_application/removeQualityInspectNoticeAttachmentn.action";
/** URL-删除安监登记表凭证 */
var URL_REMOVE_SAFETY_SUPERVISION_FORM_ATTACHMENT = URL_IP + "/worker-union-fm/project_application/removeSafetySupervisionFormAttachment.action";
/** URL-删除施工许可证凭证 */
var URL_REMOVE_CONSTRUCTION_PEIMIT_ATTACHMENT = URL_IP + "/worker-union-fm/project_application/removeConstructionPermitAttachment.action";


// TARGET
/** URL-获取目标协议书附件信息 */
var URL_GET_TARGET_UNIQUE_ATTACHMENT = URL_IP + "/worker-union-fm/target/getTargetUniqueAttachment.action";
/** URL-获取目标管理详情 */
var URL_GET_TARGET_DETAIL = URL_IP + "/worker-union-fm/target/getTargetDetail.action";
/** URL-删除目标协议书 */
var URL_REMOVE_TARGET_UNIQUE_ATTACHMENT = URL_IP + "/worker-union-fm/target/removeTargetUniqueAttachment.action";
/** URL-新增项目经理 */
var URL_ADD_PROJECT_MANAGER = URL_IP + "/worker-union-fm/target/addProjectManager.action";
/** URL-删除项目经理 */
var URL_REMOVE_PROJECT_MANAGER = URL_IP + "/worker-union-fm/target/removeProjectManager.action";
/** URL-保存目标信息 */
var URL_SAVE_TARGET = URL_IP + "/worker-union-fm/target/saveTarget.action";

// PROJECT_TEAM
/** URL-获取项目组成员信息 */
var URL_LIST_PROJECT_TEAM_STAFF = URL_IP + "/worker-union-fm/project_team/listProjectTeamStaff.action";
/** URL-获取团队成员附件列表信息 */
var URL_LIST_PROJECT_TEAM_ATTACHMENT = URL_IP + "/worker-union-fm/project_team/listProjectTeamAttachment.action";

// SCHEME_DEEPEN
/** URL-获取方案深化审核信息列表 */
var URL_LIST_SCHEME_DEEPEN_ATTACHMENT = URL_IP + "/worker-union-fm/scheme_deepen/listSchemeDeepenAttachment.action";
/** URL-通过方案深化附件审核 */
var URL_PASS_SCHEME_DEEPEN_ATTACHMENT = URL_IP + "/worker-union-fm/scheme_deepen/passSchemeDeepenAttachment.action";
/** URL-驳回方案深化附件审核 */
var URL_FAIL_SCHEME_DEEPEN_ATTACHMENT = URL_IP + "/worker-union-fm/scheme_deepen/failSchemeDeepenAttachment.action";



// COST_ANALYSIS_DEEPEN
/** URL-获取成本深化表 */
var URL_GET_COST_ANALYSIS_DEEPEN_UNIQUE_ATTACHMENT = URL_IP + "/worker-union-fm/cost_analysis_deepen/getCostAnalysisDeepenUniqueAttachment.action";
/** URL-获取成本深化详情 */
var URL_ACQUIRE_COST_ANALYSIS_DEEPEN_DETAIL = URL_IP + "/worker-union-fm/cost_analysis_deepen/acquireCostAnalysisDeepenDetail.action";
/** URL-获取成本深化项 */
var URL_GET_COST_ANALYSIS_ITEM_DEEPEN_BY_ID = URL_IP + "/worker-union-fm/cost_analysis_deepen/getCostAnalysisItemDeepenById.action";
/** URL-获取成本深化项列表 */
var URL_LIST_COST_ANALYSIS_ITEM_DEEPEN = URL_IP + "/worker-union-fm/cost_analysis_deepen/listCostAnalysisItemDeepen.action";

// FUND_PLAN
/** URL-获取资金计划项列表 */
var URL_LIST_FUND_PLAN_ITEM = URL_IP + "/worker-union-fm/fund_plan/listFundPlanItem.action";
/** URL-获取资金计划项 */
var URL_GET_FUND_PLAN_ITEM_BY_ID = URL_IP + "/worker-union-fm/fund_plan/getFundPlanItemById.action";
/** URL-获取简要资金计划项列表 */
var URL_LIST_SIMPLE_FUND_PLAN_ITEM = URL_IP + "/worker-union-fm/fund_plan/listSimpleFundPlanItem.action";

// PURCHASE_ITEM
/** URL-获取采购条目列表 */
var URL_LIST_PURCHASE_ITEM = URL_IP + "/worker-union-fm/purchase_item/listPurchaseItem.action";

// BUSINESS_CAPACITY
/** URL-获取最新的经营产值文件信息 */
var URL_GET_LATEST_BUSINESS_CAPACITY_ATTACHMENT = URL_IP + "/worker-union-fm/business_capacity/getLatestBusinessCapacityAttachment.action";
/** URL-分页获取经营产值项列表信息 */
var URL_PAGE_BUSINESS_CAPACITY_ITEM = URL_IP + "/worker-union-fm/business_capacity/pageBusinessCapacityItem.action";
/** URL-分页获取经营产值文件列表信息 */
var URL_PAGE_BUSINESS_CAPACITY_ATTACHMENT = URL_IP + "/worker-union-fm/business_capacity/pageBusinessCapacityAttachment.action";
/** URL-核定经营产值 */
var URL_CHECK_BUSINESS_CAPACITY_ITEM = URL_IP + "/worker-union-fm/business_capacity/checkBusinessCapacityItem.action";

// PRODUCT_CAPACITY
/** URL-获取最新的生产产值文件信息 */
var URL_GET_LATEST_PRODUCT_CAPACITY_ATTACHMENT = URL_IP + "/worker-union-fm/product_capacity/getLatestProductCapacityAttachment.action";
/** URL-分页获取生产产值项列表信息 */
var URL_PAGE_PRODUCT_CAPACITY_ITEM = URL_IP + "/worker-union-fm/product_capacity/pageProductCapacityItem.action";
/** URL-分页获取生产产值文件列表信息 */
var URL_PAGE_PRODUCT_CAPACITY_ATTACHMENT = URL_IP + "/worker-union-fm/product_capacity/pageProductCapacityAttachment.action";
/** URL-核定生产产值 */
var URL_CHECK_PRODUCT_CAPACITY_ITEM = URL_IP + "/worker-union-fm/product_capacity/checkProductCapacityItem.action";



// TECHNICAL_PROPOSAL
/** URL-获取技术方案审核信息列表 */
var URL_LIST_TECHNICAL_PROPOSAL_ATTACHMENT = URL_IP + "/worker-union-fm/technical_proposal/listTechnicalProposalAttachment.action";
/** URL-通过技术管理文件附件审核 */
var URL_PASS_TECHNICAL_PROPOSAL_ATTACHMENT = URL_IP + "/worker-union-fm/technical_proposal/passTechnicalProposalAttachment.action";
/** URL-驳回技术管理文件附件审核 */
var URL_FAIL_TECHNICAL_PROPOSAL_ATTACHMENT = URL_IP + "/worker-union-fm/technical_proposal/failTechnicalProposalAttachment.action";




// RESOURCE_IMPLEMENT
/** URL-获取资源落实项列表 */
var URL_LIST_RESOURCE_IMPLEMENT_ITEM = URL_IP + "/worker-union-fm/resource_implement/listResourceImplementItem.action";
/** URL-获取资源落实项 */
var URL_GET_RESOURCE_IMPLEMENT_ITEM_BY_ID = URL_IP + "/worker-union-fm/resource_implement/getResourceImplementItemById.action";
/** URL-简要获取资源落实项列表 */
var URL_LIST_SIMPLE_RESOURCE_IMPLEMENT_ITEM = URL_IP + "/worker-union-fm/resource_implement/listSimpleResourceImplementItem.action";



// SETTLEMENT
/** URL-获取最新的结算文件信息 */
var URL_GET_LATEST_SETTLEMENT_ATTACHMENT = URL_IP + "/worker-union-fm/settlement/getLatestSettlementAttachment.action";
/** URL-分页获取结算项列表 */
var URL_PAGE_SETTLEMENT_ITEM = URL_IP + "/worker-union-fm/settlement/pageSettlementItem.action";
/** URL-获取结算项 */
var URL_GET_SETTLEMENT_ITEM_BY_ID = URL_IP + "/worker-union-fm/settlement/getSettlementItemById.action";
/** URL-分页获取结算文件审核信息列表 */
var URL_PAGE_SETTLEMENT_ATTACHMENT = URL_IP + "/worker-union-fm/settlement/pageSettlementAttachment.action";
/** URL-获取结算项 */
var URL_GET_SETTLEMENT_ITEM_DETAIL_BY_ID = URL_IP + "/worker-union-fm/settlement/getSettlementItemDetailById.action";
/** URL-通过结算管理文件附件审核 */
var URL_PASS_SETTLEMENT_ATTACHMENT = URL_IP + "/worker-union-fm/settlement/passSettlementAttachment.action";
/** URL-驳回结算管理文件附件审核 */
var URL_FAIL_SETTLEMENT_ATTACHMENT = URL_IP + "/worker-union-fm/settlement/failSettlementAttachment.action";


// MATERIAL
/** URL-分页获取仓库材料列表 */
var URL_PAGE_MATERIAL_BY_FUZZY = URL_IP + "/worker-union-fm/material/pageMaterialByFuzzy.action";
/** URL-分页获取仓库材料出入库批次列表 */
var URL_PAGE_MATERIAL_ORDER_BY_FUZZY = URL_IP + "/worker-union-fm/material/pageMaterialOrderByFuzzy.action";
/** URL-分页获取指定材料出入库历史记录 */
var URL_PAGE_MATERIAL_ORDER_BY_MATERIAL_NUM = URL_IP + "/worker-union-fm/material/pageMaterialOrderByMaterialNum.action";

// MATERIAL_TYPE
/** URL-获取企业材料编号对应的材料类型信息-入库 */
var URL_GET_IMPORT_MATERIAL_TYPE_BY_NUM = URL_IP + "/worker-union-fm/material_type/getImportMaterialTypeByNum.action";
/** URL-获取企业材料类型详情 */
var URL_GET_MATERIAL_TYPE_DETAIL_BY_ID = URL_IP + "/worker-union-fm/material_type/getMaterialTypeDetailById.action";
/** URL-添加企业材料类型 */
var URL_ADD_MATERIAL_TYPE = URL_IP + "/worker-union-fm/material_type/addMaterialType.action";
/** URL-删除企业材料类型附件 */
var URL_REMOVE_MATERIAL_TYPE_ATTACHMENT_BY_ID = URL_IP + "/worker-union-fm/material_type/removeMaterialTypeAttachmentById.action";
/** URL-删除企业材料类型 */
var URL_REMOVE_MATERIAL_TYPE_BY_ID = URL_IP + "/worker-union-fm/material_type/removeMaterialTypeById.action";
/** URL-模糊查询企业材料类型 */
var URL_PAGE_MATERIAL_TYPE_BY_FUZZY = URL_IP + "/worker-union-fm/material_type/pageMaterialTypeByFuzzy.action";


// COMPLETION_DATA
/** URL-获取竣工资料审核信息列表 */
var URL_LIST_COMPLETION_DATA_ATTACHMENT = URL_IP + "/worker-union-fm/completion_data/listCompletionDataAttachment.action";
/** URL-通过项目竣工文件附件审核 */
var URL_PASS_COMPLETION_DATA_ATTACHMENT = URL_IP + "/worker-union-fm/completion_data/passCompletionDataAttachment.action";
/** URL-驳回项目竣工文件附件审核 */
var URL_FAIL_COMPLETION_DATA_ATTACHMENT = URL_IP + "/worker-union-fm/completion_data/failCompletionDataAttachment.action";

// FINAL_REPORT
/** URL-获取总结报告审核信息列表 */
var URL_LIST_FINAL_REPORT_ATTACHMENT = URL_IP + "/worker-union-fm/final_report/listFinalReportAttachment.action";
/** URL-通过项目总结文件附件审核 */
var URL_PASS_FINAL_REPORT_ATTACHMENT = URL_IP + "/worker-union-fm/final_report/passFinalReportAttachment.action";
/** URL-驳回项目总结文件附件审核 */
var URL_FAIL_FINAL_REPORT_ATTACHMENT = URL_IP + "/worker-union-fm/final_report/failFinalReportAttachment.action";

// INTERNAL_SETTLEMENT
/** URL-获取最新的内部结算文件信息 */
var URL_GET_LATEST_INTERNAL_SETTLEMENT_ATTACHMENT = URL_IP + "/worker-union-fm/internal_settlement/getLatestInternalSettlementAttachment.action";
/** URL-获取项目完成情况信息 */
var URL_GET_INTERNAL_SETTLEMENT_DETAIL = URL_IP + "/worker-union-fm/internal_settlement/getInternalSettlementDetail.action";
/** URL-获取分页内部结算文件列表 */
var URL_PAGE_INTERNAL_SETTLEMENT_ATTACHMENT = URL_IP + "/worker-union-fm/internal_settlement/pageInternalSettlementAttachment.action";
/** URL-删除内部结算文件 */
var URL_REMOVE_INTERNAL_SETTLEMENT_ATTACHMENT_BY_ID = URL_IP + "/worker-union-fm/internal_settlement/removeInternalSettlementAttachmentById.action";
/** URL-保存结算评估结果 */
var URL_SAVE_INTERNAL_SETTLEMENT_ASSESSMENT = URL_IP + "/worker-union-fm/internal_settlement/saveInternalSettlementAssessment.action";



// FINAL_SETTLEMENT
/** URL-获取竣工决算条目列表 */
var URL_LIST_FINAL_SETTLEMENT_ITEM = URL_IP + "/worker-union-fm/final_settlement/listFinalSettlementItem.action";
/** URL-增加新的竣工决算项 */
var URL_ADD_FINAL_SETTLEMENT_ITEM = URL_IP + "/worker-union-fm/final_settlement/addFinalSettlementItem.action";
/** URL-修改竣工决算项 */
var URL_MODIFY_FINAL_SETTLEMENT_ITEM_BY_ID = URL_IP + "/worker-union-fm/final_settlement/modifyFinalSettlementItemById.action";
/** URL-获取竣工决算项 */
var URL_GET_FINAL_SETTLEMENT_ITEM_BY_ID = URL_IP + "/worker-union-fm/final_settlement/getFinalSettlementItemById.action";
/** URL-删除竣工决算项 */
var URL_REMOVE_FINAL_SETTLEMENT_ITEM_BY_ID = URL_IP + "/worker-union-fm/final_settlement/removeFinalSettlementItemById.action";
/** URL-删除竣工决算项附件 */
var URL_REMOVE_FINAL_SETTLEMENT_ITEM_ATTACHMENT_BY_ID = URL_IP + "/worker-union-fm/final_settlement/removeFinalSettlementItemAttachmentById.action";

// SAFETY_CHECK
/** URL-分页获取项目安全检查项列表 */
var URL_PAGE_SAFETY_CHECK_ITEM = URL_IP + "/worker-union-fm/safety_check/pageSafetyCheckItem.action";
/** URL-获取安全检查记录详情 */
var URL_GET_SAFETY_CHECK_ITEM_DETAIL = URL_IP + "/worker-union-fm/safety_check/getSafetyCheckItemDetail.action";
/** URL-删除安全检查记录下的附件 */
var URL_REMOVE_SAFETY_CHECK_ITEM_ATTACHMENT_BY_ID = URL_IP + "/worker-union-fm/safety_check/removeSafetyCheckItemAttachmentById.action";
/** URL-新增安全检查记录 */
var URL_ADD_SAFETY_CHECK_ITEM = URL_IP + "/worker-union-fm/safety_check/addSafetyCheckItem.action";
/** URL-删除安全检查记录 */
var URL_REMOVE_SAFETY_CHECK_ITEM_BY_ID = URL_IP + "/worker-union-fm/safety_check/removeSafetyCheckItemById.action";
/** URL-修改安全检查记录 */
var URL_MODIFY_SAFETY_CHECK_ITEM_BY_ID = URL_IP + "/worker-union-fm/safety_check/modifySafetyCheckItemById.action";

// QUALITY_CHECK
/** URL-分页获取项目质量检查项列表 */
var URL_PAGE_QUALITY_CHECK_ITEM = URL_IP + "/worker-union-fm/quality_check/pageQualityCheckItem.action";
/** URL-获取质量检查记录详情 */
var URL_GET_QUALITY_CHECK_ITEM_DETAIL = URL_IP + "/worker-union-fm/quality_check/getQualityCheckItemDetail.action";
/** URL-删除质量检查记录下的附件 */
var URL_REMOVE_QUALITY_CHECK_ITEM_ATTACHMENT_BY_ID = URL_IP + "/worker-union-fm/quality_check/removeQualityCheckItemAttachmentById.action";
/** URL-新增质量检查记录 */
var URL_ADD_QUALITY_CHECK_ITEM = URL_IP + "/worker-union-fm/quality_check/addQualityCheckItem.action";
/** URL-删除质量检查记录 */
var URL_REMOVE_QUALITY_CHECK_ITEM_BY_ID = URL_IP + "/worker-union-fm/quality_check/removeQualityCheckItemById.action";
/** URL-修改质量检查记录 */
var URL_MODIFY_QUALITY_CHECK_ITEM_BY_ID = URL_IP + "/worker-union-fm/quality_check/modifyQualityCheckItemById.action";



// CONTRACT
/** URL-获取指定项目简要合同列表 */
var URL_LIST_SIMPLE_CONTRACT_ITEM_BY_PROJECT_ID = URL_IP + "/worker-union-fm/contract/listSimpleContractItemByProjectId.action";
/** URL-添加合同 */
var URL_ADD_CONTRACT = URL_IP + "/worker-union-fm/contract/addContract.action";
/** URL-删除合同 */
var URL_REMOVE_CONTRACT_BY_ID = URL_IP + "/worker-union-fm/contract/removeContractById.action";
/** URL-获取合同详情 */
var URL_GET_CONTRACT_DETAIL_BY_ID = URL_IP + "/worker-union-fm/contract/getContractDetailById.action";
/** URL-删除合同附件 */
var URL_REMOVE_CONTRACT_ATTACHMENT_BY_ID = URL_IP + "/worker-union-fm/contract/removeContractAttachmentById.action";
/** URL-修改合同 */
var URL_MODIFY_CONTRACT_BY_ID = URL_IP + "/worker-union-fm/contract/modifyContractById.action";
/** URL-模糊查询合同列表信息，分页 */
var URL_PAGE_CONTRACT_BY_FUZZY = URL_IP + "/worker-union-fm/contract/pageContractByFuzzy.action";



// CERTIFICATE
/** URL-根据模糊条件查询证书信息，分页 */
var URL_PAGE_CERTIFICATE_By_FUZZY = URL_IP+"/worker-union-fm/certificate/pageCertificateByFuzzy.action";
/** URL-添加证书信息 */
var URL_ADD_CERTIFICATE = URL_IP+"/worker-union-fm/certificate/addCertificate.action";
/** URL-删除证书信息 */
var URL_REMOVE_CERTIFICATE_By_ID = URL_IP+"/worker-union-fm/certificate/removeCertificateById.action";
/** URL-查询证书详细信息 */
var URL_GET_CERTIFICATE_DETAIL_BY_ID = URL_IP+"/worker-union-fm/certificate/getCertificateDetailById.action";
/** URL-修改证书信息 */
var URL_MODIFY_CERTIFICATE_BY_ID = URL_IP+"/worker-union-fm/certificate/modifyCertificateById.action";
/** URL-获取所有的证书系列列表 */
var URL_LIST_CERTIFICATE_SERIE = URL_IP+"/worker-union-fm/certificate/listCertificateSerie.action";
/** URL-获取证书系列下的证书类型列表 */
var URL_LIST_CERTIFICATE_TYPE_BY_SERIE_ID = URL_IP+"/worker-union-fm/certificate/listCertificateTypeBySerieId.action";
/** URL-删除证书信息附件 */
var URL_REMOVE_CERTIFICATE_ATTACHMENT_BY_ID = URL_IP+"/worker-union-fm/certificate/removeCertificateAttachmentById.action";


// SUPPLIER
/** URL-根据模糊条件查询供应商信息，分页 */
var URL_PAGE_SUPPLIER_BY_FUZZY = URL_IP+"/worker-union-fm/supplier/pageSupplierByFuzzy.action";
/** URL-添加供应商信息 */
var URL_ADD_SUPPLIER = URL_IP+"/worker-union-fm/supplier/addSupplier.action";
/** URL-删除供应商信息 */
var URL_REMOVE_SUPPLIER_BY_ID = URL_IP+"/worker-union-fm/supplier/removeSupplierById.action";
/** URL-查询供应商详细信息 */
var URL_GET_SUPPLIER_DETAIL_BY_ID = URL_IP+"/worker-union-fm/supplier/getSupplierDetailById.action";
/** URL-修改供应商信息 */
var URL_MODIFY_SUPPLIER_BY_ID = URL_IP+"/worker-union-fm/supplier/modifySupplierById.action";
/** URL-删除供应商信息附件文件 */
var URL_REMOVE_SUPPLIER_ATTACHMENT_BY_ID = URL_IP+"/worker-union-fm/supplier/removeSupplierAttachmentById.action";
/** URL-获取企业下的供应商级别列表 */
var URL_LIST_SUPPLIER_GRADE = URL_IP+"/worker-union-fm/supplier/listSupplierGrade.action";
/** URL-获取企业下的供应商资质列表 */
var URL_LIST_SUPPLIER_QUALIFICATION = URL_IP+"/worker-union-fm/supplier/listSupplierQualification.action";


// SUBCONTRACTOR
/** URL-根据模糊条件查询分包商信息，分页 */
var URL_PAGE_SUBCONTRACTOR_BY_FUZZY = URL_IP+"/worker-union-fm/subcontractor/pageSubcontractorByFuzzy.action";
/** URL-添加分包商信息 */
var URL_ADD_SUBCONTRACTOR = URL_IP+"/worker-union-fm/subcontractor/addSubcontractor.action";
/** URL-删除分包商信息 */
var URL_REMOVE_SUBCONTRACTOR_BY_ID = URL_IP+"/worker-union-fm/subcontractor/removeSubcontractorById.action";
/** URL-查询分包商详细信息 */
var URL_GET_SUBCONTRACTOR_DETAIL_BY_ID = URL_IP+"/worker-union-fm/subcontractor/getSubcontractorDetailById.action";
/** URL-修改分包商信息 */
var URL_MODIFY_SUBCONTRACTOR_BY_ID = URL_IP+"/worker-union-fm/subcontractor/modifySubcontractorById.action";
/** URL-删除分包商信息附件文件 */
var URL_REMOVE_SUBCONTRACTOR_ATTACHMENT_BY_ID = URL_IP+"/worker-union-fm/subcontractor/removeSubcontractorAttachmentById.action";
/** URL-获取企业下的分包商级别列表 */
var URL_LIST_SUBCONTRACTOR_GRADE = URL_IP+"/worker-union-fm/subcontractor/listSubcontractorGrade.action";
/** URL-获取企业下的分包商资质列表 */
var URL_LIST_SUBCONTRACTOR_QUALIFICATION = URL_IP+"/worker-union-fm/subcontractor/listSubcontractorQualification.action";
/** URL-获取简要分包商列表 */
var URL_LIST_SIMPLE_SUBCONTRACTOR_ITEM = URL_IP + "/worker-union-fm/subcontractor/listSimpleSubcontractorItem.action";


// MATERIAL_HEADQUARTERS
/** URL-分页获取总部仓库材料列表 */
var URL_PAGE_MATERIAL_HEADQUARTERS_BY_FUZZY = URL_IP + "/worker-union-fm/material_headquarters/pageMaterialHeadquartersByFuzzy.action";
/** URL-分页获取总部仓库材料出入库批次列表 */
var URL_PAGE_MATERIAL_HEADQUARTERS_ORDER_BY_FUZZY = URL_IP + "/worker-union-fm/material_headquarters/pageMaterialHeadquartersOrderByFuzzy.action";
/** URL-分页获取指定材料出入库历史记录 */
var URL_PAGE_MATERIAL_HEADQUARTERS_ORDER_BY_MATERIAL_HEADQUARTERS_NUM = URL_IP + "/worker-union-fm/material_headquarters/pageMaterialHeadquartersOrderByMaterialHeadquartersNum.action";
/** URL-材料入库 */
var URL_IMPORT_MATERIAL_HEADQUARTERS = URL_IP + "/worker-union-fm/material_headquarters/importMaterialHeadquarters.action";
/** URL-材料领用 */
var URL_EXPORT_MATERIAL_HEADQUARTERS = URL_IP + "/worker-union-fm/material_headquarters/exportMaterialHeadquarters.action";
/** URL-获取企业材料编号对应的材料信息-出库用 */
var URL_GET_EXPORT_MATERIAL_HEADQUARTERS_BY_NUM = URL_IP + "/worker-union-fm/material_headquarters/getExportMaterialHeadquartersByNum.action";
/** URL-获取未审核转库申请列表 */
var URL_LIST_UNDETERMINED_MATERIAL_TRANSFER_APPLICATION = URL_IP + "/worker-union-fm/material_headquarters/listUndeterminedMaterialTransferApplication.action";
/** URL-模糊查询转库申请历史记录，分页 */
var URL_PAGE_MATERIAL_TRANSFER_APPLICATION_BY_FUZZY = URL_IP + "/worker-union-fm/material_headquarters/pageMaterialTransferApplicationByFuzzy.action";
/** URL-获取转库申请详情 */
var URL_GET_MATERIAL_TRANSFER_APPLICATION_DETAIL_BY_ID = URL_IP + "/worker-union-fm/material_headquarters/getMaterialTransferApplicationDetailById.action";
/** URL-通过转库审核 */
var URL_PASS_MATERIAL_TRANSFER_APPLICATION_BY_ID = URL_IP + "/worker-union-fm/material_headquarters/passMaterialTransferApplicationById.action";
/** URL-驳回转库审核 */
var URL_FAIL_MATERIAL_TRANSFER_APPLICATION_BY_ID = URL_IP + "/worker-union-fm/material_headquarters/failMaterialTransferApplicationById.action";

