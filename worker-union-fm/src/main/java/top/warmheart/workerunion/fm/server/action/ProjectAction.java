package top.warmheart.workerunion.fm.server.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.context.annotation.Scope;

import top.warmheart.server.util.dto.Page;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.fm.server.action.ex.ActionJson;
import top.warmheart.workerunion.fm.server.constant.SessionKey;
import top.warmheart.workerunion.fm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.dto.Attachment2Dto;
import top.warmheart.workerunion.server.dto.QualityCheckItemDto;
import top.warmheart.workerunion.server.dto.SafetyCheckItemDto;
import top.warmheart.workerunion.server.exception.WhProjectArchiveReleaseException;
import top.warmheart.workerunion.server.exception.WhProjectArchivedException;
import top.warmheart.workerunion.server.exception.WhProjectCollapsedException;
import top.warmheart.workerunion.server.model.Attachment;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.model.QualityCheckItem;
import top.warmheart.workerunion.server.model.SafetyCheckItem;
import top.warmheart.workerunion.server.model.Staff;
import top.warmheart.workerunion.server.service.AttachmentService;
import top.warmheart.workerunion.server.service.ProjectService;
import top.warmheart.workerunion.server.service.QualityCheckItemService;
import top.warmheart.workerunion.server.service.SafetyCheckItemService;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("serial")
@Scope("prototype")
public class ProjectAction extends ActionJson {
	/** 分页参数 */
	private Page<Void> page;
	/** 项目状态 */
	private String status;
	/** 项目立项年份 */
	private Date year;
	/** 项目信息 */
	private Project project;
	private ProjectService projectService;
	private AttachmentService attachmentService;
	private SafetyCheckItemService safetyCheckItemService;
	private QualityCheckItemService qualityCheckItemService;

	public Page<Void> getPage() {
		return page;
	}

	public void setPage(Page<Void> page) {
		this.page = page;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getYear() {
		return year;
	}

	public void setYear(Date year) {
		this.year = year;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public ProjectService getProjectService() {
		return projectService;
	}

	@Resource(name = "projectService")
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	public AttachmentService getAttachmentService() {
		return attachmentService;
	}

	@Resource(name = "attachmentService")
	public void setAttachmentService(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
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

	/**
	 * 获取分页项目列表信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String pageAll() throws Exception {
		// 校验参数
		WhNull.checkPage(page);
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		// 进行查找
		Page<Project> pageCheck = projectService.pageAll(staffEx.getCompanyId(), page);

		JSONObject json = getSuccessJsonTemplate();
		json.put("page", pageCheck);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取企业下项目简要信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String listSimple() throws Exception {
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		List<Project> projects = projectService.listSimpleByCompanyId(staffEx.getCompanyId());

		JSONObject json = getSuccessJsonTemplate();
		json.put("projects", projects);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取未归档的项目列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String listUnarchivedProject() throws Exception {
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		// 进行查找
		List<Project> projects = projectService.listSimpleByFileStatus(staffEx.getCompanyId(),
				Project.FILE_STATUS_GOING);
		JSONObject json = getSuccessJsonTemplate();
		json.put("projects", projects);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 
	 * 根据项目状态获取分页项目列表信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String pageByStatus() throws Exception {
		// 校验参数
		WhNull.checkPage(page);
		WhNull.checkTrimEmpty(status);
		if (!Project.STATUS_LIST.contains(status)) {
			throw new IllegalArgumentException("status参数非法");
		}
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);
		// 进行查找
		Page<Project> pageCheck = projectService.pageByStatus(staffEx.getCompanyId(), status, page);

		JSONObject json = getSuccessJsonTemplate();
		json.put("page", pageCheck);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 
	 * 根据项目立项年份获取分页项目列表信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String pageByYear() throws Exception {
		// 校验参数
		WhNull.checkPage(page);
		WhNull.check(year);
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);
		// 进行查找
		Page<Project> pageCheck = projectService.pageByYear(staffEx.getCompanyId(), year, page);

		JSONObject json = getSuccessJsonTemplate();
		json.put("page", pageCheck);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 
	 * 根据项目状态及立项年份获取分页项目列表信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String pageByStatusAndYear() throws Exception {
		// 校验参数
		WhNull.checkPage(page);
		WhNull.check(year);
		WhNull.checkTrimEmpty(status);
		if (!Project.STATUS_LIST.contains(status)) {
			throw new IllegalArgumentException("status参数非法");
		}
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);
		// 进行查找
		Page<Project> pageCheck = projectService.pageByStatusAndYear(staffEx.getCompanyId(), status, year, page);

		JSONObject json = getSuccessJsonTemplate();
		json.put("page", pageCheck);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 
	 * 根据项目ID获取项目信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getDetailById() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

		/*
		 * 查找中标通知书附件信息
		 */
		Attachment attachmentCheck = attachmentService.getLatestByType(projectEx.getId(),
				Attachment.TYPE_LETTER_OF_ACCEPTANCE);

		JSONObject json = getSuccessJsonTemplate();
		json.put("project", projectEx);
		json.put("attachment", attachmentCheck);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 
	 * 根据项目ID获取项目信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getById() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

		JSONObject json = getSuccessJsonTemplate();
		json.put("project", projectEx);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 
	 * 项目部解体
	 * 
	 * @return
	 * @throws Exception
	 */
	public String collapseById() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

		if (Project.COLLAPSE_STATUS_COLLAPSED.equalsIgnoreCase(projectEx.getCollapseStatus())) {
			throw new WhProjectCollapsedException();
		}

		projectEx.setCollapseStatus(Project.COLLAPSE_STATUS_COLLAPSED);
		projectService.modifyById(projectEx);

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 
	 * 项目归档
	 * 
	 * @return
	 * @throws Exception
	 */
	public String archiveById() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

		if (Project.FILE_STATUS_ARCHIVED.equalsIgnoreCase(projectEx.getFileStatus())) {
			throw new WhProjectArchivedException();
		}

		projectEx.setFileStatus(Project.FILE_STATUS_ARCHIVED);
		projectService.modifyById(projectEx);

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 
	 * 项目解除归档
	 * 
	 * @return
	 * @throws Exception
	 */
	public String releaseArchiveById() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

		if (Project.FILE_STATUS_GOING.equalsIgnoreCase(projectEx.getFileStatus())) {
			throw new WhProjectArchiveReleaseException();
		}

		projectEx.setFileStatus(Project.FILE_STATUS_GOING);
		projectService.modifyById(projectEx);

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取待办事项
	 * 
	 * @return
	 * @throws Exception
	 */
	public String listSchedule() throws Exception {
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		Subject subject = SecurityUtils.getSubject();
		List<Attachment2Dto> attachments = new ArrayList<Attachment2Dto>();
		/*
		 * 方案深化
		 */
		if (subject.isPermitted(SessionKey.PERMISSION_SCHEME_DEEPEN_UPDATE)) {
			List<Attachment2Dto> attachment2Dtos = attachmentService.listUndeterminedByTypeInCompany(
					staffEx.getCompanyId(), Attachment.TYPE_SCHEME_DEEPEN);
			attachments.addAll(attachment2Dtos);
		}
		/*
		 * 技术方案
		 */
		if (subject.isPermitted(SessionKey.PERMISSION_TECHNICAL_PROPOSAL_UPDATE)) {
			List<Attachment2Dto> attachment2Dtos = attachmentService.listUndeterminedByTypeInCompany(
					staffEx.getCompanyId(), Attachment.TYPE_TECHNICAL_PROPOSAL);
			attachments.addAll(attachment2Dtos);
		}
		/*
		 * 结算文件
		 */
		if (subject.isPermitted(SessionKey.PERMISSION_SETTLEMENT_UPDATE)) {
			List<Attachment2Dto> attachment2Dtos = attachmentService.listUndeterminedByTypeInCompany(
					staffEx.getCompanyId(), Attachment.TYPE_SETTLEMENT_FILE);
			attachments.addAll(attachment2Dtos);
		}
		/*
		 * 竣工验收
		 */
		if (subject.isPermitted(SessionKey.PERMISSION_COMPLETION_DATA)) {
			List<Attachment2Dto> attachment2Dtos = attachmentService.listUndeterminedByTypeInCompany(
					staffEx.getCompanyId(), Attachment.TYPE_COMPLETION_DATA);
			attachments.addAll(attachment2Dtos);
		}
		/*
		 * 项目总结
		 */
		if (subject.isPermitted(SessionKey.PERMISSION_FINAL_REPORT)) {
			List<Attachment2Dto> attachment2Dtos = attachmentService.listUndeterminedByTypeInCompany(
					staffEx.getCompanyId(), Attachment.TYPE_FINAL_REPORT);
			attachments.addAll(attachment2Dtos);
		}

		/*
		 * 安全管理审批
		 */
		List<SafetyCheckItemDto> safetyCheckItems = new ArrayList<SafetyCheckItemDto>();
		if (subject.isPermitted(SessionKey.PERMISSION_SAFETY_CHECK)) {
			safetyCheckItems = safetyCheckItemService.listByCompanyAndStatus(staffEx.getCompanyId(),
					SafetyCheckItem.RECTIFY_STATUS_VERIFY);
		}
		
		/*
		 * 质量管理审批
		 */
		List<QualityCheckItemDto> qualityCheckItems = new ArrayList<QualityCheckItemDto>();
		if (subject.isPermitted(SessionKey.PERMISSION_QUALITY_CHECK)) {
			qualityCheckItems = qualityCheckItemService.listByCompanyAndStatus(staffEx.getCompanyId(),
					QualityCheckItem.RECTIFY_STATUS_VERIFY);
		}

		JSONObject json = getSuccessJsonTemplate();
		json.put("attachments", attachments);
		json.put("safetyCheckItems", safetyCheckItems);
		json.put("qualityCheckItems", qualityCheckItems);
		writeStream(json);
		return SUCCESS;
	}
}
