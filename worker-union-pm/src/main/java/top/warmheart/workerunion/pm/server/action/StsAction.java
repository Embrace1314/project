package top.warmheart.workerunion.pm.server.action;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;

import top.warmheart.server.util.WhStringUtil;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.pm.server.action.ex.ActionJson;
import top.warmheart.workerunion.pm.server.constant.Config;
import top.warmheart.workerunion.pm.server.constant.SessionKey;
import top.warmheart.workerunion.pm.server.service.StsService;
import top.warmheart.workerunion.pm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.exception.WhAttachmentExistException;
import top.warmheart.workerunion.server.exception.WhInvalidAttachmentException;
import top.warmheart.workerunion.server.exception.WhQualityCheckItemModifyDisabledException;
import top.warmheart.workerunion.server.exception.WhSafetyCheckItemModifyDisabledException;
import top.warmheart.workerunion.server.model.Attachment;
import top.warmheart.workerunion.server.model.FinalSettlementItem;
import top.warmheart.workerunion.server.model.FinalSettlementItemAttachment;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.model.QualityCheckItem;
import top.warmheart.workerunion.server.model.QualityCheckItemAttachment;
import top.warmheart.workerunion.server.model.SafetyCheckItem;
import top.warmheart.workerunion.server.model.SafetyCheckItemAttachment;
import top.warmheart.workerunion.server.model.Staff;
import top.warmheart.workerunion.server.service.AttachmentService;
import top.warmheart.workerunion.server.service.FinalSettlementItemService;
import top.warmheart.workerunion.server.service.ProjectService;
import top.warmheart.workerunion.server.service.QualityCheckItemService;
import top.warmheart.workerunion.server.service.SafetyCheckItemService;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse.Credentials;

@SuppressWarnings("serial")
@Scope("prototype")
public class StsAction extends ActionJson {
	/** 上传附件URL约束 */
	private static final String POLICY_TEMPLATE = "{\"expiration\":\"EXPIRATION_VALUE\",\"conditions\":[{\"bucket\":\"BUCKET_VALUE\"},{\"callback\":\"CALLBACK_VALUE\"},[\"starts-with\", \"$key\", \"KEY_STARTS_WITH_VALUE\"]]}";
	/** 上传附件URL请求内容基本信息 */
	private static final String CALLBACK_BODY_BASE = "bucket=${bucket}&object=${object}&etag=${etag}&size=${size}&mimeType=${mimeType}";
	/** 上传唯一性附件URL请求地址 */
	private static final String CALLBACK_URL_UPLOAD_UNIQUE_ATTACHMENT = Config.OSS_CALLBACK_URL_BASE
			+ "/UploadUniqueAttachmentServlet";
	/** 上传唯一性附件URL请求内容 */
	private static final String CALLBACK_BODY_UPLOAD_UNIQUE_ATTACHMENT = CALLBACK_BODY_BASE
			+ "&companyId=COMPANY_ID&projectId=PROJECT_ID&type=TYPE&staffId=STAFF_ID&staffName=STAFF_NAME";
	/** 上传非唯一性附件URL请求地址 */
	private static final String CALLBACK_URL_UPLOAD_ATTACHMENT = Config.OSS_CALLBACK_URL_BASE
			+ "/UploadAttachmentServlet";
	/** 上传非唯一性附件URL请求内容 */
	private static final String CALLBACK_BODY_UPLOAD_ATTACHMENT = CALLBACK_BODY_BASE
			+ "&companyId=COMPANY_ID&projectId=PROJECT_ID&type=TYPE&staffId=STAFF_ID&staffName=STAFF_NAME";
	/** 上传安全记录附件URL请求地址 */
	private static final String CALLBACK_URL_UPLOAD_SAFETY_CHECK_ITEM_ATTACHMENT = Config.OSS_CALLBACK_URL_BASE
			+ "/UploadSafetyCheckItemAttachmentServlet";
	/** 上传安全记录附件URL请求内容 */
	private static final String CALLBACK_BODY_UPLOAD_SAFETY_CHECK_ITEM_ATTACHMENT = CALLBACK_BODY_BASE
			+ "&companyId=COMPANY_ID&projectId=PROJECT_ID&safetyCheckItemId=SAFETY_CHECK_ITEM_ID&type=TYPE&staffId=STAFF_ID&staffName=STAFF_NAME";
	/** 上传质量记录附件URL请求地址 */
	private static final String CALLBACK_URL_UPLOAD_QUALITY_CHECK_ITEM_ATTACHMENT = Config.OSS_CALLBACK_URL_BASE
			+ "/UploadQualityCheckItemAttachmentServlet";
	/** 上传质量记录附件URL请求内容 */
	private static final String CALLBACK_BODY_UPLOAD_QUALITY_CHECK_ITEM_ATTACHMENT = CALLBACK_BODY_BASE
			+ "&companyId=COMPANY_ID&projectId=PROJECT_ID&qualityCheckItemId=QUALITY_CHECK_ITEM_ID&type=TYPE&staffId=STAFF_ID&staffName=STAFF_NAME";

	private StsService stsService;
	private AttachmentService attachmentService;
	private ProjectService projectService;
	private Attachment attachment;
	private Project project;
	private SafetyCheckItem safetyCheckItem;
	private SafetyCheckItemAttachment safetyCheckItemAttachment;
	private SafetyCheckItemService safetyCheckItemService;
	private QualityCheckItem qualityCheckItem;
	private QualityCheckItemAttachment qualityCheckItemAttachment;
	private QualityCheckItemService qualityCheckItemService;
	private FinalSettlementItem finalSettlementItem;
	private FinalSettlementItemService finalSettlementItemService;
	
	public Attachment getAttachment() {
		return attachment;
	}

	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public FinalSettlementItem getFinalSettlementItem() {
		return finalSettlementItem;
	}

	public void setFinalSettlementItem(FinalSettlementItem finalSettlementItem) {
		this.finalSettlementItem = finalSettlementItem;
	}

	public SafetyCheckItem getSafetyCheckItem() {
		return safetyCheckItem;
	}

	public void setSafetyCheckItem(SafetyCheckItem safetyCheckItem) {
		this.safetyCheckItem = safetyCheckItem;
	}

	public StsService getStsService() {
		return stsService;
	}

	public SafetyCheckItemAttachment getSafetyCheckItemAttachment() {
		return safetyCheckItemAttachment;
	}

	public void setSafetyCheckItemAttachment(SafetyCheckItemAttachment safetyCheckItemAttachment) {
		this.safetyCheckItemAttachment = safetyCheckItemAttachment;
	}

	public QualityCheckItem getQualityCheckItem() {
		return qualityCheckItem;
	}

	public void setQualityCheckItem(QualityCheckItem qualityCheckItem) {
		this.qualityCheckItem = qualityCheckItem;
	}

	public QualityCheckItemAttachment getQualityCheckItemAttachment() {
		return qualityCheckItemAttachment;
	}

	public void setQualityCheckItemAttachment(QualityCheckItemAttachment qualityCheckItemAttachment) {
		this.qualityCheckItemAttachment = qualityCheckItemAttachment;
	}

	@Resource(name = "stsService")
	public void setStsService(StsService stsService) {
		this.stsService = stsService;
	}

	public AttachmentService getAttachmentService() {
		return attachmentService;
	}

	@Resource(name = "attachmentService")
	public void setAttachmentService(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}

	public ProjectService getProjectService() {
		return projectService;
	}

	@Resource(name = "projectService")
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	public SafetyCheckItemService getSafetyCheckItemService() {
		return safetyCheckItemService;
	}

	@Resource(name = "safetyCheckItemService")
	public void setSafetyCheckItemService(SafetyCheckItemService safetyCheckItemService) {
		this.safetyCheckItemService = safetyCheckItemService;
	}

	public QualityCheckItemService getQualityCheckItemService() {
		return qualityCheckItemService;
	}

	@Resource(name = "qualityCheckItemService")
	public void setQualityCheckItemService(QualityCheckItemService qualityCheckItemService) {
		this.qualityCheckItemService = qualityCheckItemService;
	}

	public FinalSettlementItemService getFinalSettlementItemService() {
		return finalSettlementItemService;
	}

	@Resource(name = "finalSettlementItemService")
	public void setFinalSettlementItemService(
			FinalSettlementItemService finalSettlementItemService) {
		this.finalSettlementItemService = finalSettlementItemService;
	}

	/**
	 * 获取中标通知书下载凭证
	 * 
	 * @return
	 * @throws Excpetion
	 */
	public String acquireLetterOfAcceptanceDownToken() throws Exception {
		return acquireAttachmentDownToken(Attachment.TYPE_LETTER_OF_ACCEPTANCE);
	}

	/**
	 * 获取目标协议书下载凭证
	 * 
	 * @return
	 * @throws Excpetion
	 */
	public String acquireAgreementOfTargetDownToken() throws Exception {
		return acquireAttachmentDownToken(Attachment.TYPE_AGREEMENT_OF_TARGET);
	}

	/**
	 * 获取成本分析表下载凭证
	 * 
	 * @return
	 * @throws Excpetion
	 */
	public String acquireCostAnalysisDownToken() throws Exception {
		return acquireAttachmentDownToken(Attachment.TYPE_COST_ANALYSIS);
	}

	/**
	 * 获取下载合同备案表凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireContractRecordDownToken() throws Exception {
		return acquireAttachmentDownToken(Attachment.TYPE_CONTRACT_RECORD);
	}

	/**
	 * 获取下载质检通知书凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireQualityInspectNoticeDownToken() throws Exception {
		return acquireAttachmentDownToken(Attachment.TYPE_QUALITY_INSPECT_NOTICE);
	}

	/**
	 * 获取下载安监登记表凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireSafetySupervisionFormDownToken() throws Exception {
		return acquireAttachmentDownToken(Attachment.TYPE_SAFETY_SUPERVISION_FORM);
	}

	/**
	 * 获取下载施工许可证凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireConstructionPermitDownToken() throws Exception {
		return acquireAttachmentDownToken(Attachment.TYPE_CONSTRUCTION_PERMIT);
	}

	/**
	 * 获取下载方案深化表凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireSchemeDeepenDownToken() throws Exception {
		return acquireAttachmentDownToken(Attachment.TYPE_SCHEME_DEEPEN);
	}

	/**
	 * 获取上传方案深化表凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireSchemeDeepenUpToken() throws Exception {
		return acquireAttachmentUpToken(Attachment.TYPE_SCHEME_DEEPEN);
	}

	/**
	 * 获取下载成本深化表凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireCostDeepenDownToken() throws Exception {
		return acquireAttachmentDownToken(Attachment.TYPE_COST_ANALYSIS_DEEPEN);
	}

	/**
	 * 获取上传成本深化表凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireCostDeepenUpToken() throws Exception {
		return acquireUniqueAttachmentUpToken(Attachment.TYPE_COST_ANALYSIS_DEEPEN);
	}

	/**
	 * 获取下载经营管理产值文件凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireBusinessCapacityFileDownToken() throws Exception {
		return acquireAttachmentDownToken(Attachment.TYPE_BUSINESS_CAPACITY_FILE);
	}

	/**
	 * 获取上传经营管理产值文件凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireBusinessCapacityFileUpToken() throws Exception {
		return acquireAttachmentUpToken(Attachment.TYPE_BUSINESS_CAPACITY_FILE);
	}

	/**
	 * 获取下载生产产值文件凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireProductCapacityFileDownToken() throws Exception {
		return acquireAttachmentDownToken(Attachment.TYPE_PRODUCT_CAPACITY_FILE);
	}

	/**
	 * 获取上传生产产值文件凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireProductCapacityFileUpToken() throws Exception {
		return acquireAttachmentUpToken(Attachment.TYPE_PRODUCT_CAPACITY_FILE);
	}

	/**
	 * 获取下载技术方案凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireTechnicalProposalDownToken() throws Exception {
		return acquireAttachmentDownToken(Attachment.TYPE_TECHNICAL_PROPOSAL);
	}

	/**
	 * 获取上传技术方案凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireTechnicalProposalUpToken() throws Exception {
		return acquireAttachmentUpToken(Attachment.TYPE_TECHNICAL_PROPOSAL);
	}

	/**
	 * 获取下载结算文件凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireSettlementDownToken() throws Exception {
		return acquireAttachmentDownToken(Attachment.TYPE_SETTLEMENT_FILE);
	}

	/**
	 * 获取上传结算文件凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireSettlementUpToken() throws Exception {
		return acquireAttachmentUpToken(Attachment.TYPE_SETTLEMENT_FILE);
	}

	/**
	 * 获取下载竣工资料凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireCompletionDataDownToken() throws Exception {
		return acquireAttachmentDownToken(Attachment.TYPE_COMPLETION_DATA);
	}

	/**
	 * 获取上传竣工资料凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireCompletionDataUpToken() throws Exception {
		return acquireAttachmentUpToken(Attachment.TYPE_COMPLETION_DATA);
	}

	/**
	 * 获取下载总结报告凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireFinalReportDownToken() throws Exception {
		return acquireAttachmentDownToken(Attachment.TYPE_FINAL_REPORT);
	}

	/**
	 * 获取上传总结报告凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireFinalReportUpToken() throws Exception {
		return acquireAttachmentUpToken(Attachment.TYPE_FINAL_REPORT);
	}
	
	/**
	 * 获取下载内部结算文件凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireInternalSettlementDownToken() throws Exception {
		return acquireAttachmentDownToken(Attachment.TYPE_INTERNAL_SETTLEMENT_FILE);
	}

	/**
	 * 获取下载团队搭建附件
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireProjectTeamAttachmentDownToken() throws Exception {
		return acquireAttachmentDownToken(Attachment.TYPE_PROJECT_TEAM);
	}

	/**
	 * 获取上传团队搭建附件
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireProjectTeamAttachmentUpToken() throws Exception {
		return acquireAttachmentUpToken(Attachment.TYPE_PROJECT_TEAM);
	}
	
	/**
	 * 获取下载安全记录附件凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireSafetyCheckItemAttachmentDownToken() throws Exception {
		WhNull.check(safetyCheckItemAttachment);
		WhNull.check(safetyCheckItemAttachment.getId());
		WhNull.check(project);
		WhNull.check(project.getId());

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);

		// 获取附件信息
		SafetyCheckItemAttachment safetyCheckItemAttachmentCheck = safetyCheckItemService
				.getAttachmentById(safetyCheckItemAttachment.getId());
		WhOwnerShipUtil.checkProject(safetyCheckItemAttachmentCheck, projectEx);

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		// 获取授权密钥
		Credentials credentials = stsService.downloadRequest(staffEx,
				new String[] { "/" + safetyCheckItemAttachmentCheck.getPath() }).getCredentials();
		credentials.setExpiration(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date(new Date().getTime()
				+ Config.OSS_VALID_DUARATION)));

		// 设置URL过期时间
		Date expiration = new Date(new Date().getTime() + Config.OSS_VALID_DUARATION);
		OSSClient client = new OSSClient(Config.OSS_URL_END_POINT, credentials.getAccessKeyId(),
				credentials.getAccessKeySecret(), credentials.getSecurityToken());

		// 生成URL
		URL url = client.generatePresignedUrl(Config.OSS_BUCKET_NAME, safetyCheckItemAttachmentCheck.getPath(),
				expiration);

		JSONObject json = getSuccessJsonTemplate();
		json.put("url", url);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取上传安全记录附件凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireSafetyCheckItemAttachmentUpToken() throws Exception {
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(safetyCheckItem);
		WhNull.check(safetyCheckItem.getId());

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(projectEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectService.getById(projectEx.getId()));

		/*
		 * 校验安全记录
		 */
		SafetyCheckItem safetyCheckItemCheck = safetyCheckItemService.getItemById(safetyCheckItem.getId());
		WhOwnerShipUtil.checkProject(safetyCheckItemCheck, projectEx);
		if (!SafetyCheckItem.RECTIFY_STATUS_RECTIFY.equalsIgnoreCase(safetyCheckItemCheck.getRectifyStatus())) {
			throw new WhSafetyCheckItemModifyDisabledException();
		}

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		JSONObject json = getSuccessJsonTemplate();

		String dir = staffEx.getCompanyId() + "/" + projectEx.getNum();
		Credentials credentials = stsService.uploadRequest(staffEx, new String[] { "/" + dir + "/*" }).getCredentials();
		credentials.setExpiration(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date(new Date().getTime()
				+ Config.OSS_VALID_DUARATION)));

		// 文件目录
		String uuid = WhStringUtil.uuid();
		json.put("uuid", uuid);
		json.put("dir", dir);
		// 密钥ID
		json.put("OSSAccessKeyId", credentials.getAccessKeyId());
		// 创建callback约束
		JSONObject jsonCallback = new JSONObject();
		jsonCallback.put("callbackUrl", CALLBACK_URL_UPLOAD_SAFETY_CHECK_ITEM_ATTACHMENT);
		jsonCallback.put(
				"callbackBody",
				CALLBACK_BODY_UPLOAD_SAFETY_CHECK_ITEM_ATTACHMENT
						.replace("COMPANY_ID", staffEx.getCompanyId().toString())
						.replace("PROJECT_ID", projectEx.getId().toString())
						.replace("SAFETY_CHECK_ITEM_ID", safetyCheckItem.getId().toString())
						.replace("TYPE", SafetyCheckItemAttachment.TYPE_CALLBACK)
						.replace("STAFF_ID", staffEx.getId().toString()).replace("STAFF_NAME", staffEx.getName()));

		// 权限策略
		String encodePolicy = new String(Base64.encodeBase64(POLICY_TEMPLATE
				.replace("EXPIRATION_VALUE", credentials.getExpiration())
				.replace("BUCKET_VALUE", Config.OSS_BUCKET_NAME).replace("KEY_STARTS_WITH_VALUE", dir + "/" + uuid)
				.replace("CALLBACK_VALUE", new String(Base64.encodeBase64(jsonCallback.toString().getBytes("UTF-8"))))
				.getBytes("UTF-8")));
		json.put("callback", new String(Base64.encodeBase64(jsonCallback.toString().getBytes("UTF-8"))));
		json.put("policy", encodePolicy);
		json.put("Signature", computeSignature(credentials.getAccessKeySecret(), encodePolicy));
		json.put("securityToken", credentials.getSecurityToken());
		json.put("ossUrl", Config.OSS_URL);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取下载质量记录附件凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireQualityCheckItemAttachmentDownToken() throws Exception {
		WhNull.check(qualityCheckItemAttachment);
		WhNull.check(qualityCheckItemAttachment.getId());
		WhNull.check(project);
		WhNull.check(project.getId());

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);

		// 获取附件信息
		QualityCheckItemAttachment qualityCheckItemAttachmentCheck = qualityCheckItemService
				.getAttachmentById(qualityCheckItemAttachment.getId());
		WhOwnerShipUtil.checkProject(qualityCheckItemAttachmentCheck, projectEx);

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		// 获取授权密钥
		Credentials credentials = stsService.downloadRequest(staffEx,
				new String[] { "/" + qualityCheckItemAttachmentCheck.getPath() }).getCredentials();
		credentials.setExpiration(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date(new Date().getTime()
				+ Config.OSS_VALID_DUARATION)));

		// 设置URL过期时间
		Date expiration = new Date(new Date().getTime() + Config.OSS_VALID_DUARATION);
		OSSClient client = new OSSClient(Config.OSS_URL_END_POINT, credentials.getAccessKeyId(),
				credentials.getAccessKeySecret(), credentials.getSecurityToken());

		// 生成URL
		URL url = client.generatePresignedUrl(Config.OSS_BUCKET_NAME, qualityCheckItemAttachmentCheck.getPath(),
				expiration);

		JSONObject json = getSuccessJsonTemplate();
		json.put("url", url);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取决算条目附件下载凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireFinalSettlementAttachmentDownToken() throws Exception {
		WhNull.check(finalSettlementItem);
		WhNull.check(finalSettlementItem.getId());
		WhNull.check(project);
		WhNull.check(project.getId());

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);

		// 获取附件信息
		FinalSettlementItemAttachment finalSettlementItemAttachmentCheck = finalSettlementItemService.getAttachmentByItemId(finalSettlementItem.getId());
		WhOwnerShipUtil.checkProject(finalSettlementItemAttachmentCheck, projectEx);

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		// 获取授权密钥
		Credentials credentials = stsService.downloadRequest(staffEx,
				new String[] { "/" + finalSettlementItemAttachmentCheck.getPath() }).getCredentials();
		credentials.setExpiration(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date(new Date().getTime()
				+ Config.OSS_VALID_DUARATION)));

		// 设置URL过期时间
		Date expiration = new Date(new Date().getTime() + Config.OSS_VALID_DUARATION);
		OSSClient client = new OSSClient(Config.OSS_URL_END_POINT, credentials.getAccessKeyId(),
				credentials.getAccessKeySecret(), credentials.getSecurityToken());

		// 生成URL
		URL url = client.generatePresignedUrl(Config.OSS_BUCKET_NAME, finalSettlementItemAttachmentCheck.getPath(),
				expiration);

		JSONObject json = getSuccessJsonTemplate();
		json.put("url", url);
		writeStream(json);
		return SUCCESS;
	}
	
	/**
	 * 获取上传质量记录附件凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireQualityCheckItemAttachmentUpToken() throws Exception {
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(qualityCheckItem);
		WhNull.check(qualityCheckItem.getId());

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(projectEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectService.getById(projectEx.getId()));

		/*
		 * 校验安全记录
		 */
		QualityCheckItem qualityCheckItemCheck = qualityCheckItemService.getItemById(qualityCheckItem.getId());
		WhOwnerShipUtil.checkProject(qualityCheckItemCheck, projectEx);
		if (!QualityCheckItem.RECTIFY_STATUS_RECTIFY.equalsIgnoreCase(qualityCheckItemCheck.getRectifyStatus())) {
			throw new WhQualityCheckItemModifyDisabledException();
		}

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		JSONObject json = getSuccessJsonTemplate();

		String dir = staffEx.getCompanyId() + "/" + projectEx.getNum();
		Credentials credentials = stsService.uploadRequest(staffEx, new String[] { "/" + dir + "/*" }).getCredentials();
		credentials.setExpiration(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date(new Date().getTime()
				+ Config.OSS_VALID_DUARATION)));

		// 文件目录
		String uuid = WhStringUtil.uuid();
		json.put("uuid", uuid);
		json.put("dir", dir);
		// 密钥ID
		json.put("OSSAccessKeyId", credentials.getAccessKeyId());
		// 创建callback约束
		JSONObject jsonCallback = new JSONObject();
		jsonCallback.put("callbackUrl", CALLBACK_URL_UPLOAD_QUALITY_CHECK_ITEM_ATTACHMENT);
		jsonCallback.put(
				"callbackBody",
				CALLBACK_BODY_UPLOAD_QUALITY_CHECK_ITEM_ATTACHMENT
						.replace("COMPANY_ID", staffEx.getCompanyId().toString())
						.replace("PROJECT_ID", projectEx.getId().toString())
						.replace("QUALITY_CHECK_ITEM_ID", qualityCheckItem.getId().toString())
						.replace("TYPE", QualityCheckItemAttachment.TYPE_CALLBACK)
						.replace("STAFF_ID", staffEx.getId().toString()).replace("STAFF_NAME", staffEx.getName()));

		// 权限策略
		String encodePolicy = new String(Base64.encodeBase64(POLICY_TEMPLATE
				.replace("EXPIRATION_VALUE", credentials.getExpiration())
				.replace("BUCKET_VALUE", Config.OSS_BUCKET_NAME).replace("KEY_STARTS_WITH_VALUE", dir + "/" + uuid)
				.replace("CALLBACK_VALUE", new String(Base64.encodeBase64(jsonCallback.toString().getBytes("UTF-8"))))
				.getBytes("UTF-8")));
		json.put("callback", new String(Base64.encodeBase64(jsonCallback.toString().getBytes("UTF-8"))));
		json.put("policy", encodePolicy);
		json.put("Signature", computeSignature(credentials.getAccessKeySecret(), encodePolicy));
		json.put("securityToken", credentials.getSecurityToken());
		json.put("ossUrl", Config.OSS_URL);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取附件下载凭证
	 * 
	 * @param attachmentType
	 * @return
	 * @throws Exception
	 */
	private String acquireAttachmentDownToken(String attachmentType) throws Exception {
		WhNull.check(attachment);
		WhNull.check(attachment.getId());
		WhNull.check(project);
		WhNull.check(project.getId());

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);

		// 获取附件信息
		Attachment attachmentCheck = attachmentService.getById(attachment.getId());
		WhOwnerShipUtil.checkProject(attachmentCheck, projectEx);

		if (!attachmentCheck.getType().equalsIgnoreCase(attachmentType)) {
			throw new WhInvalidAttachmentException();
		}

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);
		// 获取授权密钥
		Credentials credentials = stsService.downloadRequest(staffEx, new String[] { "/" + attachmentCheck.getPath() })
				.getCredentials();
		credentials.setExpiration(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date(new Date().getTime()
				+ Config.OSS_VALID_DUARATION)));

		// 设置URL过期时间
		Date expiration = new Date(new Date().getTime() + Config.OSS_VALID_DUARATION);
		OSSClient client = new OSSClient(Config.OSS_URL_END_POINT, credentials.getAccessKeyId(),
				credentials.getAccessKeySecret(), credentials.getSecurityToken());

		// 生成URL
		URL url = client.generatePresignedUrl(Config.OSS_BUCKET_NAME, attachmentCheck.getPath(), expiration);

		JSONObject json = getSuccessJsonTemplate();
		json.put("url", url);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取唯一性附件上传凭证
	 * 
	 * @return
	 * @throws Excpetion
	 */
	private String acquireUniqueAttachmentUpToken(String attachmentType) throws Exception {
		WhNull.check(project);
		WhNull.check(project.getId());

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(projectEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectService.getById(projectEx.getId()));

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);

		/*
		 * 检查唯一附件是否存在
		 */
		Attachment attachmentExist = attachmentService.getLatestByType(project.getId(), attachmentType);
		if (null != attachmentExist) {
			throw new WhAttachmentExistException();
		}

		JSONObject json = getSuccessJsonTemplate();

		String dir = staffEx.getCompanyId() + "/" + projectEx.getNum();
		Credentials credentials = stsService.uploadRequest(staffEx, new String[] { "/" + dir + "/*" }).getCredentials();
		credentials.setExpiration(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date(new Date().getTime()
				+ Config.OSS_VALID_DUARATION)));

		// 文件目录
		String uuid = WhStringUtil.uuid();
		json.put("uuid", uuid);
		json.put("dir", dir);
		// 密钥ID
		json.put("OSSAccessKeyId", credentials.getAccessKeyId());
		// 创建callback约束
		JSONObject jsonCallback = new JSONObject();
		jsonCallback.put("callbackUrl", CALLBACK_URL_UPLOAD_UNIQUE_ATTACHMENT);
		jsonCallback.put(
				"callbackBody",
				CALLBACK_BODY_UPLOAD_UNIQUE_ATTACHMENT.replace("COMPANY_ID", staffEx.getCompanyId().toString())
						.replace("PROJECT_ID", projectEx.getId().toString()).replace("TYPE", attachmentType)
						.replace("STAFF_ID", staffEx.getId().toString()).replace("STAFF_NAME", staffEx.getName()));

		// 权限策略
		String encodePolicy = new String(Base64.encodeBase64(POLICY_TEMPLATE
				.replace("EXPIRATION_VALUE", credentials.getExpiration())
				.replace("BUCKET_VALUE", Config.OSS_BUCKET_NAME).replace("KEY_STARTS_WITH_VALUE", dir + "/" + uuid)
				.replace("CALLBACK_VALUE", new String(Base64.encodeBase64(jsonCallback.toString().getBytes("UTF-8"))))
				.getBytes("UTF-8")));
		json.put("callback", new String(Base64.encodeBase64(jsonCallback.toString().getBytes("UTF-8"))));
		json.put("policy", encodePolicy);
		json.put("Signature", computeSignature(credentials.getAccessKeySecret(), encodePolicy));
		json.put("securityToken", credentials.getSecurityToken());
		json.put("ossUrl", Config.OSS_URL);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取非唯一性附件上传凭证
	 * 
	 * @return
	 * @throws Excpetion
	 */
	private String acquireAttachmentUpToken(String attachmentType) throws Exception {
		WhNull.check(project);
		WhNull.check(project.getId());

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(projectEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectService.getById(projectEx.getId()));

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);

		JSONObject json = getSuccessJsonTemplate();

		String dir = staffEx.getCompanyId() + "/" + projectEx.getNum();
		Credentials credentials = stsService.uploadRequest(staffEx, new String[] { "/" + dir + "/*" }).getCredentials();
		credentials.setExpiration(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date(new Date().getTime()
				+ Config.OSS_VALID_DUARATION)));

		// 文件目录
		String uuid = WhStringUtil.uuid();
		json.put("uuid", uuid);
		json.put("dir", dir);
		// 密钥ID
		json.put("OSSAccessKeyId", credentials.getAccessKeyId());
		// 创建callback约束
		JSONObject jsonCallback = new JSONObject();
		jsonCallback.put("callbackUrl", CALLBACK_URL_UPLOAD_ATTACHMENT);
		jsonCallback.put(
				"callbackBody",
				CALLBACK_BODY_UPLOAD_ATTACHMENT.replace("COMPANY_ID", staffEx.getCompanyId().toString())
						.replace("PROJECT_ID", projectEx.getId().toString()).replace("TYPE", attachmentType)
						.replace("STAFF_ID", staffEx.getId().toString()).replace("STAFF_NAME", staffEx.getName()));

		// 权限策略
		String encodePolicy = new String(Base64.encodeBase64(POLICY_TEMPLATE
				.replace("EXPIRATION_VALUE", credentials.getExpiration())
				.replace("BUCKET_VALUE", Config.OSS_BUCKET_NAME).replace("KEY_STARTS_WITH_VALUE", dir + "/" + uuid)
				.replace("CALLBACK_VALUE", new String(Base64.encodeBase64(jsonCallback.toString().getBytes("UTF-8"))))
				.getBytes("UTF-8")));
		json.put("callback", new String(Base64.encodeBase64(jsonCallback.toString().getBytes("UTF-8"))));
		json.put("policy", encodePolicy);
		json.put("Signature", computeSignature(credentials.getAccessKeySecret(), encodePolicy));
		json.put("securityToken", credentials.getSecurityToken());
		json.put("ossUrl", Config.OSS_URL);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 计算签名
	 * 
	 * @param accessKeySecret
	 * @param encodePolicy
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	private static String computeSignature(String accessKeySecret, String encodePolicy)
			throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
		// convert to UTF-8
		byte[] key = accessKeySecret.getBytes("UTF-8");
		byte[] data = encodePolicy.getBytes("UTF-8");

		// hmac-sha1
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(new SecretKeySpec(key, "HmacSHA1"));
		byte[] sha = mac.doFinal(data);

		// base64
		return new String(Base64.encodeBase64(sha));
	}
}
