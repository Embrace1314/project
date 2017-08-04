package top.warmheart.workerunion.fm.server.action;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
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
import top.warmheart.workerunion.fm.server.action.ex.ActionJson;
import top.warmheart.workerunion.fm.server.constant.Config;
import top.warmheart.workerunion.fm.server.constant.SessionKey;
import top.warmheart.workerunion.fm.server.service.StsService;
import top.warmheart.workerunion.fm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.exception.WhAttachmentExistException;
import top.warmheart.workerunion.server.exception.WhFinalSettlementItemAttachmentExistException;
import top.warmheart.workerunion.server.exception.WhInvalidAttachmentException;
import top.warmheart.workerunion.server.exception.WhProjectExistException;
import top.warmheart.workerunion.server.model.Attachment;
import top.warmheart.workerunion.server.model.Certificate;
import top.warmheart.workerunion.server.model.CertificateAttachment;
import top.warmheart.workerunion.server.model.Contract;
import top.warmheart.workerunion.server.model.ContractAttachment;
import top.warmheart.workerunion.server.model.FinalSettlementItem;
import top.warmheart.workerunion.server.model.FinalSettlementItemAttachment;
import top.warmheart.workerunion.server.model.MaterialType;
import top.warmheart.workerunion.server.model.MaterialTypeAttachment;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.model.QualityCheckItem;
import top.warmheart.workerunion.server.model.QualityCheckItemAttachment;
import top.warmheart.workerunion.server.model.SafetyCheckItem;
import top.warmheart.workerunion.server.model.SafetyCheckItemAttachment;
import top.warmheart.workerunion.server.model.Staff;
import top.warmheart.workerunion.server.model.Subcontractor;
import top.warmheart.workerunion.server.model.SubcontractorAttachment;
import top.warmheart.workerunion.server.model.Supplier;
import top.warmheart.workerunion.server.model.SupplierAttachment;
import top.warmheart.workerunion.server.service.AttachmentService;
import top.warmheart.workerunion.server.service.CertificateService;
import top.warmheart.workerunion.server.service.ContractService;
import top.warmheart.workerunion.server.service.FinalSettlementItemService;
import top.warmheart.workerunion.server.service.MaterialTypeService;
import top.warmheart.workerunion.server.service.ProjectService;
import top.warmheart.workerunion.server.service.QualityCheckItemService;
import top.warmheart.workerunion.server.service.SafetyCheckItemService;
import top.warmheart.workerunion.server.service.SubcontractorService;
import top.warmheart.workerunion.server.service.SupplierService;

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

	/** 上传中标通知书URL请求地址 */
	private static final String CALLBACK_URL_CREATE_PROJECT = Config.OSS_CALLBACK_URL_BASE + "/CreateProjectServlet";
	/** 上传中标通知书URL请求内容 */
	private static final String CALLBACK_BODY_CREATE_PROJECT = CALLBACK_BODY_BASE
			+ "&companyId=COMPANY_ID&num=NUM&name=NAME&constructerName=CONSTRUCTER_NAME&designerName=DESIGNER_NAME&superviserName=SUPERVISER_NAME&address=ADDRESS&type=TYPE&scale=SCALE&bidPrice=BID_PREICE&bidDuration=BID_DURATION&staffId=STAFF_ID&staffName=STAFF_NAME";

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

	/** 上传供应商附件URL请求地址 */
	private static final String CALLBACK_URL_UPLOAD_SUPPLIER_ATTACHMENT = Config.OSS_CALLBACK_URL_BASE
			+ "/UploadSupplierAttachmentServlet";
	/** 上传供应商附件URL请求内容 */
	private static final String CALLBACK_BODY_UPLOAD_SUPPLIER_ATTACHMENT = CALLBACK_BODY_BASE
			+ "&companyId=COMPANY_ID&supplierId=SUPPLIER_ID&type=TYPE&staffId=STAFF_ID&staffName=STAFF_NAME";
	/** 上传分包商附件URL请求地址 */
	private static final String CALLBACK_URL_UPLOAD_SUBCONTRACTOR_ATTACHMENT = Config.OSS_CALLBACK_URL_BASE
			+ "/UploadSubcontractorAttachmentServlet";
	/** 上传分包商附件URL请求内容 */
	private static final String CALLBACK_BODY_UPLOAD_SUBCONTRACTOR_ATTACHMENT = CALLBACK_BODY_BASE
			+ "&companyId=COMPANY_ID&subcontractorId=SUBCONTRACTOR_ID&type=TYPE&staffId=STAFF_ID&staffName=STAFF_NAME";

	/** 上传合同附件URL请求地址 */
	private static final String CALLBACK_URL_UPLOAD_CONTRACT_ATTACHMENT = Config.OSS_CALLBACK_URL_BASE
			+ "/UploadContractAttachmentServlet";
	/** 上传合同附件URL请求内容 */
	private static final String CALLBACK_BODY_UPLOAD_CONTRACT_ATTACHMENT = CALLBACK_BODY_BASE
			+ "&companyId=COMPANY_ID&contractId=CONTRACT_ID&type=TYPE&staffId=STAFF_ID&staffName=STAFF_NAME";
	/** 上传材料类型附件URL请求地址 */
	private static final String CALLBACK_URL_UPLOAD_MATERIALTYPE_ATTACHMENT = Config.OSS_CALLBACK_URL_BASE
			+ "/UploadMaterialTypeAttachmentServlet";
	/** 上传材料类型附件URL请求内容 */
	private static final String CALLBACK_BODY_UPLOAD_MATERIALTYPE_ATTACHMENT = CALLBACK_BODY_BASE
			+ "&companyId=COMPANY_ID&materialTypeId=MATERIALTYPE_ID&type=TYPE&staffId=STAFF_ID&staffName=STAFF_NAME";
	/** 上传竣工决算附件URL请求地址 */
	private static final String CALLBACK_URL_UPLOAD_FINAL_SETTLEMENT_ITEM_ATTACHMENT = Config.OSS_CALLBACK_URL_BASE
			+ "/UploadFinalSettlementItemAttachmentServlet";
	/** 上传竣工决算附件URL请求内容 */
	private static final String CALLBACK_BODY_UPLOAD_FINAL_SETTLEMENT_ITEM_ATTACHMENT = CALLBACK_BODY_BASE
			+ "&companyId=COMPANY_ID&projectId=PROJECT_ID&finalSettlementItemId=FINAL_SETTLEMENT_ITEM_ID&type=TYPE&staffId=STAFF_ID&staffName=STAFF_NAME";
	/** 上传证书附件URL请求地址 */
	private static final String CALLBACK_URL_UPLOAD_CERTIFICATE_ATTACHMENT = Config.OSS_CALLBACK_URL_BASE
			+ "/UploadCertificateAttachmentServlet";
	/** 上传证书附件URL请求内容 */
	private static final String CALLBACK_BODY_UPLOAD_CERTIFICATE_ATTACHMENT = CALLBACK_BODY_BASE
			+ "&companyId=COMPANY_ID&certificateId=CERTIFICATE_ID&type=TYPE&staffId=STAFF_ID&staffName=STAFF_NAME";
	private MaterialType materialType;
	private MaterialTypeAttachment materialTypeAttachment;
	private MaterialTypeService materialTypeService;

	private Contract contract;
	private ContractAttachment contractAttachment;
	private ContractService contractService;

	private Subcontractor subcontractor;
	private SubcontractorAttachment subcontractorAttachment;
	private SubcontractorService subcontractorService;

	private Certificate certificate;
	private CertificateAttachment certificateAttachment;
	private CertificateService certificateService;
	private Supplier supplier;
	private SupplierAttachment supplierAttachment;
	private SupplierService supplierService;
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

	public Certificate getCertificate() {
		return certificate;
	}

	public void setCertificate(Certificate certificate) {
		this.certificate = certificate;
	}

	public CertificateAttachment getCertificateAttachment() {
		return certificateAttachment;
	}

	public void setCertificateAttachment(CertificateAttachment certificateAttachment) {
		this.certificateAttachment = certificateAttachment;
	}

	public MaterialType getMaterialType() {
		return materialType;
	}

	public void setMaterialType(MaterialType materialType) {
		this.materialType = materialType;
	}

	public MaterialTypeAttachment getMaterialTypeAttachment() {
		return materialTypeAttachment;
	}

	public void setMaterialTypeAttachment(MaterialTypeAttachment materialTypeAttachment) {
		this.materialTypeAttachment = materialTypeAttachment;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public ContractAttachment getContractAttachment() {
		return contractAttachment;
	}

	public void setContractAttachment(ContractAttachment contractAttachment) {
		this.contractAttachment = contractAttachment;
	}

	public Subcontractor getSubcontractor() {
		return subcontractor;
	}

	public void setSubcontractor(Subcontractor subcontractor) {
		this.subcontractor = subcontractor;
	}

	public SubcontractorAttachment getSubcontractorAttachment() {
		return subcontractorAttachment;
	}

	public void setSubcontractorAttachment(SubcontractorAttachment subcontractorAttachment) {
		this.subcontractorAttachment = subcontractorAttachment;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public SupplierAttachment getSupplierAttachment() {
		return supplierAttachment;
	}

	public void setSupplierAttachment(SupplierAttachment supplierAttachment) {
		this.supplierAttachment = supplierAttachment;
	}

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
	public void setFinalSettlementItemService(FinalSettlementItemService finalSettlementItemService) {
		this.finalSettlementItemService = finalSettlementItemService;
	}

	public SupplierService getSupplierService() {
		return supplierService;
	}

	@Resource(name = "supplierService")
	public void setSupplierService(SupplierService supplierService) {
		this.supplierService = supplierService;
	}

	public MaterialTypeService getMaterialTypeService() {
		return materialTypeService;
	}

	@Resource(name = "materialTypeService")
	public void setMaterialTypeService(MaterialTypeService materialTypeService) {
		this.materialTypeService = materialTypeService;
	}

	public ContractService getContractService() {
		return contractService;
	}

	@Resource(name = "contractService")
	public void setContractService(ContractService contractService) {
		this.contractService = contractService;
	}

	public SubcontractorService getSubcontractorService() {
		return subcontractorService;
	}

	@Resource(name = "subcontractorService")
	public void setSubcontractorService(SubcontractorService subcontractorService) {
		this.subcontractorService = subcontractorService;
	}

	public CertificateService getCertificateService() {
		return certificateService;
	}

	@Resource(name = "certificateService")
	public void setCertificateService(CertificateService certificateService) {
		this.certificateService = certificateService;
	}

	/**
	 * 获取中标通知书上传凭证
	 * 
	 * @return
	 * @throws Excpetion
	 */
	public String acquireLetterOfAcceptanceUpToken() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.checkTrimEmpty(project.getNum());
		WhNull.checkTrimEmpty(project.getName());
		WhNull.checkTrimEmpty(project.getConstructerName());
		WhNull.checkTrimEmpty(project.getDesignerName());
		WhNull.checkTrimEmpty(project.getSuperviserName());
		WhNull.checkTrimEmpty(project.getAddress());
		WhNull.checkTrimEmpty(project.getType());
		WhNull.checkTrimEmpty(project.getScale());
		WhNull.check(project.getBidPrice());
		project.setBidPrice(project.getBidPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
		if (project.getBidPrice().compareTo(BigDecimal.valueOf(0)) < 0) {
			throw new IllegalArgumentException("中标价不能小于0！");
		}
		WhNull.check(project.getBidDuration());
		if (project.getBidDuration() <= 0) {
			throw new IllegalArgumentException("中标工期必须大于0！");
		}

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);

		/*
		 * 校验是否存在重复的项目编号
		 */
		Project projectEx = projectService.getByNum(staffEx.getCompanyId(), WhStringUtil.trimAll(project.getNum()));
		if (null != projectEx) {
			throw new WhProjectExistException("重复的项目编号！");
		}

		// 整理数据
		project.setNum(WhStringUtil.trimAll(project.getNum()));
		project.setName(WhStringUtil.trimAll(project.getName()));
		project.setConstructerName(WhStringUtil.trimAll(project.getConstructerName()));
		project.setDesignerName(WhStringUtil.trimAll(project.getDesignerName()));
		project.setSuperviserName(WhStringUtil.trimAll(project.getSuperviserName()));
		project.setAddress(WhStringUtil.trimAll(project.getAddress()));
		project.setType(WhStringUtil.trimAll(project.getType()));

		String dir = String.valueOf(staffEx.getCompanyId());
		Credentials credentials = stsService.uploadRequest(staffEx, new String[] { "/" + dir + "/*" }).getCredentials();
		credentials.setExpiration(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date(new Date().getTime()
				+ Config.OSS_VALID_DUARATION)));

		JSONObject json = getSuccessJsonTemplate();
		// 文件目录
		String uuid = WhStringUtil.uuid();
		json.put("uuid", uuid);
		json.put("dir", dir);
		// 密钥ID
		json.put("OSSAccessKeyId", credentials.getAccessKeyId());
		// 创建callback约束
		JSONObject jsonCallback = new JSONObject();
		jsonCallback.put("callbackUrl", CALLBACK_URL_CREATE_PROJECT);
		jsonCallback.put(
				"callbackBody",
				CALLBACK_BODY_CREATE_PROJECT.replace("COMPANY_ID", staffEx.getCompanyId().toString())
						.replace("STAFF_ID", staffEx.getId().toString()).replace("STAFF_NAME", staffEx.getName())
						.replace("NUM", project.getNum()).replace("CONSTRUCTER_NAME", project.getConstructerName())
						.replace("DESIGNER_NAME", project.getDesignerName())
						.replace("SUPERVISER_NAME", project.getSuperviserName()).replace("NAME", project.getName())
						.replace("ADDRESS", project.getAddress()).replace("TYPE", project.getType())
						.replace("SCALE", project.getScale()).replace("BID_PREICE", project.getBidPrice().toString())
						.replace("BID_DURATION", String.valueOf(project.getBidDuration())));

		// 权限策略
		String encodePolicy = new String(Base64.encodeBase64(POLICY_TEMPLATE
				.replace("EXPIRATION_VALUE", credentials.getExpiration())
				.replace("BUCKET_VALUE", Config.OSS_BUCKET_NAME).replace("KEY_STARTS_WITH_VALUE", dir)
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
	 * 获取成本分析表上传凭证
	 * 
	 * @return
	 * @throws Excpetion
	 */
	public String acquireCostAnalysisUpToken() throws Exception {
		return acquireUniqueAttachmentUpToken(Attachment.TYPE_COST_ANALYSIS);
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
	 * 获取目标协议书上传凭证
	 * 
	 * @return
	 * @throws Excpetion
	 */
	public String acquireAgreementOfTargetUpToken() throws Exception {
		return acquireUniqueAttachmentUpToken(Attachment.TYPE_AGREEMENT_OF_TARGET);
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
	 * 获取上传合同备案表凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireContractRecordUpToken() throws Exception {
		return acquireUniqueAttachmentUpToken(Attachment.TYPE_CONTRACT_RECORD);
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
	 * 获取上传质检通知书凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireQualityInspectNoticeUpToken() throws Exception {
		return acquireUniqueAttachmentUpToken(Attachment.TYPE_QUALITY_INSPECT_NOTICE);
	}

	/**
	 * 获取上传安监登记表凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireSafetySupervisionFormUpToken() throws Exception {
		return acquireUniqueAttachmentUpToken(Attachment.TYPE_SAFETY_SUPERVISION_FORM);
	}

	/**
	 * 获取上传施工许可证凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireConstructionPermitUpToken() throws Exception {
		return acquireUniqueAttachmentUpToken(Attachment.TYPE_CONSTRUCTION_PERMIT);
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
	 * 获取上传内部结算文件凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireInternalSettlementUpToken() throws Exception {
		return acquireAttachmentUpToken(Attachment.TYPE_INTERNAL_SETTLEMENT_FILE);
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

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

		// 获取附件信息
		SafetyCheckItemAttachment safetyCheckItemAttachmentCheck = safetyCheckItemService
				.getAttachmentById(safetyCheckItemAttachment.getId());
		WhOwnerShipUtil.checkProject(safetyCheckItemAttachmentCheck, projectEx);

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

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);
		WhOwnerShipUtil.checkGoingProject(projectEx);

		/*
		 * 校验安全记录
		 */
		SafetyCheckItem safetyCheckItemCheck = safetyCheckItemService.getItemById(safetyCheckItem.getId());
		WhOwnerShipUtil.checkProject(safetyCheckItemCheck, projectEx);

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
						.replace("TYPE", SafetyCheckItemAttachment.TYPE_RECORD)
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
	 * 获取上传竣工决算项附件凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireFinalSettlementItemAttachmentUpToken() throws Exception {
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(finalSettlementItem);
		WhNull.check(finalSettlementItem.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);
		WhOwnerShipUtil.checkGoingProject(projectEx);

		/*
		 * 校验竣工决算记录
		 */
		FinalSettlementItem finalSettlementItemCheck = finalSettlementItemService.getItemById(finalSettlementItem
				.getId());
		WhOwnerShipUtil.checkProject(finalSettlementItemCheck, projectEx);

		FinalSettlementItemAttachment finalSettlementItemAttachmentExist = finalSettlementItemService
				.getAttachmentByItemId(finalSettlementItem.getId());
		if (null != finalSettlementItemAttachmentExist) {
			throw new WhFinalSettlementItemAttachmentExistException();
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
		jsonCallback.put("callbackUrl", CALLBACK_URL_UPLOAD_FINAL_SETTLEMENT_ITEM_ATTACHMENT);
		jsonCallback.put(
				"callbackBody",
				CALLBACK_BODY_UPLOAD_FINAL_SETTLEMENT_ITEM_ATTACHMENT
						.replace("COMPANY_ID", staffEx.getCompanyId().toString())
						.replace("PROJECT_ID", projectEx.getId().toString())
						.replace("FINAL_SETTLEMENT_ITEM_ID", finalSettlementItem.getId().toString())
						.replace("TYPE", FinalSettlementItemAttachment.TYPE_INFORMATION)
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

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

		// 获取附件信息
		QualityCheckItemAttachment qualityCheckItemAttachmentCheck = qualityCheckItemService
				.getAttachmentById(qualityCheckItemAttachment.getId());
		WhOwnerShipUtil.checkProject(qualityCheckItemAttachmentCheck, projectEx);

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

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

		// 获取附件信息
		FinalSettlementItemAttachment finalSettlementItemAttachmentCheck = finalSettlementItemService
				.getAttachmentByItemId(finalSettlementItem.getId());
		WhOwnerShipUtil.checkProject(finalSettlementItemAttachmentCheck, projectEx);

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

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);
		WhOwnerShipUtil.checkGoingProject(projectEx);

		/*
		 * 校验安全记录
		 */
		QualityCheckItem qualityCheckItemCheck = qualityCheckItemService.getItemById(qualityCheckItem.getId());
		WhOwnerShipUtil.checkProject(qualityCheckItemCheck, projectEx);

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
						.replace("TYPE", QualityCheckItemAttachment.TYPE_RECORD)
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
	 * 获取下载供应商附件凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireSupplierAttachmentDownToken() throws Exception {
		WhNull.check(supplierAttachment);
		WhNull.check(supplierAttachment.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);

		// 获取附件信息
		SupplierAttachment supplierAttachmentCheck = supplierService.getAttachmentById(supplierAttachment.getId());
		WhOwnerShipUtil.checkStaffCompany(supplierAttachmentCheck, staffEx);

		// 获取授权密钥
		Credentials credentials = stsService.downloadRequest(staffEx,
				new String[] { "/" + supplierAttachmentCheck.getPath() }).getCredentials();
		credentials.setExpiration(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date(new Date().getTime()
				+ Config.OSS_VALID_DUARATION)));

		// 设置URL过期时间
		Date expiration = new Date(new Date().getTime() + Config.OSS_VALID_DUARATION);
		OSSClient client = new OSSClient(Config.OSS_URL_END_POINT, credentials.getAccessKeyId(),
				credentials.getAccessKeySecret(), credentials.getSecurityToken());

		// 生成URL
		URL url = client.generatePresignedUrl(Config.OSS_BUCKET_NAME, supplierAttachmentCheck.getPath(), expiration);

		JSONObject json = getSuccessJsonTemplate();
		json.put("url", url);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取上传供应商附件凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireSupplierAttachmentUpToken() throws Exception {
		WhNull.check(supplier);
		WhNull.check(supplier.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);

		/*
		 * 校验供应商
		 */
		Supplier supplierCheck = supplierService.getById(supplier.getId());
		WhOwnerShipUtil.checkStaffCompany(supplierCheck, staffEx);

		JSONObject json = getSuccessJsonTemplate();

		String dir = staffEx.getCompanyId().toString();
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
		jsonCallback.put("callbackUrl", CALLBACK_URL_UPLOAD_SUPPLIER_ATTACHMENT);
		jsonCallback.put(
				"callbackBody",
				CALLBACK_BODY_UPLOAD_SUPPLIER_ATTACHMENT.replace("COMPANY_ID", staffEx.getCompanyId().toString())
						.replace("SUPPLIER_ID", supplier.getId().toString())
						.replace("TYPE", SupplierAttachment.TYPE_INFORMATION)
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
	 * 获取下载分包商附件凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireSubcontractorAttachmentDownToken() throws Exception {
		WhNull.check(subcontractorAttachment);
		WhNull.check(subcontractorAttachment.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);

		// 获取附件信息
		SubcontractorAttachment subcontractorAttachmentCheck = subcontractorService
				.getAttachmentById(subcontractorAttachment.getId());
		WhOwnerShipUtil.checkStaffCompany(subcontractorAttachmentCheck, staffEx);

		// 获取授权密钥
		Credentials credentials = stsService.downloadRequest(staffEx,
				new String[] { "/" + subcontractorAttachmentCheck.getPath() }).getCredentials();
		credentials.setExpiration(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date(new Date().getTime()
				+ Config.OSS_VALID_DUARATION)));

		// 设置URL过期时间
		Date expiration = new Date(new Date().getTime() + Config.OSS_VALID_DUARATION);
		OSSClient client = new OSSClient(Config.OSS_URL_END_POINT, credentials.getAccessKeyId(),
				credentials.getAccessKeySecret(), credentials.getSecurityToken());

		// 生成URL
		URL url = client.generatePresignedUrl(Config.OSS_BUCKET_NAME, subcontractorAttachmentCheck.getPath(),
				expiration);

		JSONObject json = getSuccessJsonTemplate();
		json.put("url", url);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取上传分包商附件凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireSubcontractorAttachmentUpToken() throws Exception {
		WhNull.check(subcontractor);
		WhNull.check(subcontractor.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);

		/*
		 * 校验分包商
		 */
		Subcontractor subcontractorCheck = subcontractorService.getById(subcontractor.getId());
		WhOwnerShipUtil.checkStaffCompany(subcontractorCheck, staffEx);

		JSONObject json = getSuccessJsonTemplate();

		String dir = staffEx.getCompanyId().toString();
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
		jsonCallback.put("callbackUrl", CALLBACK_URL_UPLOAD_SUBCONTRACTOR_ATTACHMENT);
		jsonCallback.put(
				"callbackBody",
				CALLBACK_BODY_UPLOAD_SUBCONTRACTOR_ATTACHMENT.replace("COMPANY_ID", staffEx.getCompanyId().toString())
						.replace("SUBCONTRACTOR_ID", subcontractor.getId().toString())
						.replace("TYPE", SubcontractorAttachment.TYPE_INFORMATION)
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
	 * 获取下载合同附件凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireContractAttachmentDownToken() throws Exception {
		WhNull.check(contractAttachment);
		WhNull.check(contractAttachment.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);

		// 获取附件信息
		ContractAttachment contractAttachmentCheck = contractService.getAttachmentById(contractAttachment.getId());
		WhOwnerShipUtil.checkStaffCompany(contractAttachmentCheck, staffEx);

		// 获取授权密钥
		Credentials credentials = stsService.downloadRequest(staffEx,
				new String[] { "/" + contractAttachmentCheck.getPath() }).getCredentials();
		credentials.setExpiration(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date(new Date().getTime()
				+ Config.OSS_VALID_DUARATION)));

		// 设置URL过期时间
		Date expiration = new Date(new Date().getTime() + Config.OSS_VALID_DUARATION);
		OSSClient client = new OSSClient(Config.OSS_URL_END_POINT, credentials.getAccessKeyId(),
				credentials.getAccessKeySecret(), credentials.getSecurityToken());

		// 生成URL
		URL url = client.generatePresignedUrl(Config.OSS_BUCKET_NAME, contractAttachmentCheck.getPath(), expiration);

		JSONObject json = getSuccessJsonTemplate();
		json.put("url", url);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取上传合同附件凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireContractAttachmentUpToken() throws Exception {
		WhNull.check(contract);
		WhNull.check(contract.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);

		/*
		 * 校验合同
		 */
		Contract contractCheck = contractService.getById(contract.getId());
		WhOwnerShipUtil.checkStaffCompany(contractCheck, staffEx);

		JSONObject json = getSuccessJsonTemplate();

		String dir = staffEx.getCompanyId().toString();
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
		jsonCallback.put("callbackUrl", CALLBACK_URL_UPLOAD_CONTRACT_ATTACHMENT);
		jsonCallback.put(
				"callbackBody",
				CALLBACK_BODY_UPLOAD_CONTRACT_ATTACHMENT.replace("COMPANY_ID", staffEx.getCompanyId().toString())
						.replace("CONTRACT_ID", contract.getId().toString())
						.replace("TYPE", ContractAttachment.TYPE_INFORMATION)
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
	 * 获取下载证书附件凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireCertificateAttachmentDownToken() throws Exception {
		WhNull.check(certificateAttachment);
		WhNull.check(certificateAttachment.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);

		// 获取附件信息
		CertificateAttachment certificateAttachmentCheck = certificateService.getAttachmentById(certificateAttachment.getId());
		WhOwnerShipUtil.checkStaffCompany(certificateAttachmentCheck, staffEx);

		// 获取授权密钥
		Credentials credentials = stsService.downloadRequest(staffEx,
				new String[] { "/" + certificateAttachmentCheck.getPath() }).getCredentials();
		credentials.setExpiration(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date(new Date().getTime()
				+ Config.OSS_VALID_DUARATION)));

		// 设置URL过期时间
		Date expiration = new Date(new Date().getTime() + Config.OSS_VALID_DUARATION);
		OSSClient client = new OSSClient(Config.OSS_URL_END_POINT, credentials.getAccessKeyId(),
				credentials.getAccessKeySecret(), credentials.getSecurityToken());

		// 生成URL
		URL url = client.generatePresignedUrl(Config.OSS_BUCKET_NAME, certificateAttachmentCheck.getPath(), expiration);

		JSONObject json = getSuccessJsonTemplate();
		json.put("url", url);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取上传证书附件凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireCertificateAttachmentUpToken() throws Exception {
		WhNull.check(certificate);
		WhNull.check(certificate.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);

		/*
		 * 校验合同
		 */
		Certificate certificateCheck = certificateService.getById(certificate.getId());
		WhOwnerShipUtil.checkStaffCompany(certificateCheck, staffEx);

		JSONObject json = getSuccessJsonTemplate();

		String dir = staffEx.getCompanyId().toString();
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
		jsonCallback.put("callbackUrl", CALLBACK_URL_UPLOAD_CERTIFICATE_ATTACHMENT);
		jsonCallback.put(
				"callbackBody",
				CALLBACK_BODY_UPLOAD_CERTIFICATE_ATTACHMENT.replace("COMPANY_ID", staffEx.getCompanyId().toString())
						.replace("CERTIFICATE_ID", certificate.getId().toString())
						.replace("TYPE", CertificateAttachment.TYPE_INFORMATION)
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
	 * 获取下载材料类型附件凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireMaterialTypeAttachmentDownToken() throws Exception {
		WhNull.check(materialTypeAttachment);
		WhNull.check(materialTypeAttachment.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);

		// 获取附件信息
		MaterialTypeAttachment materialTypeAttachmentCheck = materialTypeService
				.getAttachmentById(materialTypeAttachment.getId());
		WhOwnerShipUtil.checkStaffCompany(materialTypeAttachmentCheck, staffEx);

		// 获取授权密钥
		Credentials credentials = stsService.downloadRequest(staffEx,
				new String[] { "/" + materialTypeAttachmentCheck.getPath() }).getCredentials();
		credentials.setExpiration(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date(new Date().getTime()
				+ Config.OSS_VALID_DUARATION)));

		// 设置URL过期时间
		Date expiration = new Date(new Date().getTime() + Config.OSS_VALID_DUARATION);
		OSSClient client = new OSSClient(Config.OSS_URL_END_POINT, credentials.getAccessKeyId(),
				credentials.getAccessKeySecret(), credentials.getSecurityToken());

		// 生成URL
		URL url = client
				.generatePresignedUrl(Config.OSS_BUCKET_NAME, materialTypeAttachmentCheck.getPath(), expiration);

		JSONObject json = getSuccessJsonTemplate();
		json.put("url", url);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取上传材料类型附件凭证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireMaterialTypeAttachmentUpToken() throws Exception {
		WhNull.check(materialType);
		WhNull.check(materialType.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);

		/*
		 * 校验材料类型
		 */
		MaterialType materialTypeCheck = materialTypeService.getById(materialType.getId());
		WhOwnerShipUtil.checkStaffCompany(materialTypeCheck, staffEx);

		JSONObject json = getSuccessJsonTemplate();

		String dir = staffEx.getCompanyId().toString();
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
		jsonCallback.put("callbackUrl", CALLBACK_URL_UPLOAD_MATERIALTYPE_ATTACHMENT);
		jsonCallback.put(
				"callbackBody",
				CALLBACK_BODY_UPLOAD_MATERIALTYPE_ATTACHMENT.replace("COMPANY_ID", staffEx.getCompanyId().toString())
						.replace("MATERIALTYPE_ID", materialType.getId().toString())
						.replace("TYPE", MaterialTypeAttachment.TYPE_INFORMATION)
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

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

		// 获取附件信息
		Attachment attachmentCheck = attachmentService.getById(attachment.getId());
		WhOwnerShipUtil.checkProject(attachmentCheck, projectEx);

		if (!attachmentCheck.getType().equalsIgnoreCase(attachmentType)) {
			throw new WhInvalidAttachmentException();
		}

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

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);
		WhOwnerShipUtil.checkGoingProject(projectEx);

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

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);
		WhOwnerShipUtil.checkGoingProject(projectEx);

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
