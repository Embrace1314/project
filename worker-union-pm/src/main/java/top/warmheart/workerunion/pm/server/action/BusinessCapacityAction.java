package top.warmheart.workerunion.pm.server.action;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;

import top.warmheart.server.util.dto.Page;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.pm.server.action.ex.ActionJson;
import top.warmheart.workerunion.pm.server.constant.SessionKey;
import top.warmheart.workerunion.pm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.pm.server.util.WhYearMonthUtil;
import top.warmheart.workerunion.server.dto.BusinessCapacityItemDto;
import top.warmheart.workerunion.server.exception.WhBusinessCapacityItemCheckedException;
import top.warmheart.workerunion.server.exception.WhBusinessCapacityItemExistException;
import top.warmheart.workerunion.server.exception.WhInvalidAttachmentException;
import top.warmheart.workerunion.server.model.Attachment;
import top.warmheart.workerunion.server.model.BusinessCapacityItem;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.model.Staff;
import top.warmheart.workerunion.server.model.Target;
import top.warmheart.workerunion.server.service.AttachmentService;
import top.warmheart.workerunion.server.service.BusinessCapacityItemService;
import top.warmheart.workerunion.server.service.ProjectService;
import top.warmheart.workerunion.server.service.TargetService;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("serial")
@Scope("prototype")
public class BusinessCapacityAction extends ActionJson {
	private Project project;
	private Attachment attachment;
	private BusinessCapacityItem businessCapacityItem;
	private Page<Void> page;
	private AttachmentService attachmentService;
	private TargetService targetService;
	private BusinessCapacityItemService businessCapacityItemService;
	private ProjectService projectService;

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Attachment getAttachment() {
		return attachment;
	}

	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}

	public Page<Void> getPage() {
		return page;
	}

	public void setPage(Page<Void> page) {
		this.page = page;
	}

	public BusinessCapacityItem getBusinessCapacityItem() {
		return businessCapacityItem;
	}

	public void setBusinessCapacityItem(BusinessCapacityItem businessCapacityItem) {
		this.businessCapacityItem = businessCapacityItem;
	}

	public BusinessCapacityItemService getBusinessCapacityItemService() {
		return businessCapacityItemService;
	}

	@Resource(name = "businessCapacityItemService")
	public void setBusinessCapacityItemService(BusinessCapacityItemService businessCapacityItemService) {
		this.businessCapacityItemService = businessCapacityItemService;
	}

	public AttachmentService getAttachmentService() {
		return attachmentService;
	}

	@Resource(name = "attachmentService")
	public void setAttachmentService(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}

	public TargetService getTargetService() {
		return targetService;
	}

	@Resource(name = "targetService")
	public void setTargetService(TargetService targetService) {
		this.targetService = targetService;
	}

	public ProjectService getProjectService() {
		return projectService;
	}

	@Resource(name = "projectService")
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	/**
	 * 获取最新的产值确认文件信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getLatestAttachment() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);

		// 获取最新的附件信息
		Attachment attachmentCheck = attachmentService.getLatestByType(projectEx.getId(),
				Attachment.TYPE_BUSINESS_CAPACITY_FILE);

		JSONObject json = getSuccessJsonTemplate();
		if (null != attachmentCheck) {
			json.put("hasAttachment", true);
			json.put("attachment", attachmentCheck);
		} else {
			json.put("hasAttachment", false);
		}
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 删除产值确认文件
	 * 
	 * @return
	 * @throws Exception
	 */
	public String removeAttachment() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(attachment);
		WhNull.check(attachment.getId());

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectService.getById(projectEx.getId()));

		Attachment attachmentCheck = attachmentService.getById(attachment.getId());
		WhOwnerShipUtil.checkProject(attachmentCheck, projectEx);
		if (!Attachment.TYPE_BUSINESS_CAPACITY_FILE.equalsIgnoreCase(attachmentCheck.getType())) {
			throw new WhInvalidAttachmentException();
		}

		attachmentService.removeById(attachment.getId());

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 分页获取产值确认文件列表信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String pageAttachment() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.checkPage(page);
		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);

		Page<Attachment> pageCheck = attachmentService.pageByProjectType(projectEx.getId(),
				Attachment.TYPE_BUSINESS_CAPACITY_FILE, page);

		JSONObject json = getSuccessJsonTemplate();
		json.put("page", pageCheck);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 分页获取经营产值项列表信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String pageItem() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.checkPage(page);

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);

		Page<BusinessCapacityItemDto> pageCheck = businessCapacityItemService.pageByProjectId(projectEx.getId(), page);

		JSONObject json = getSuccessJsonTemplate();
		json.put("page", pageCheck);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 添加某月经营产值
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addItem() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(businessCapacityItem);
		WhNull.check(businessCapacityItem.getYear());
		WhNull.check(businessCapacityItem.getMonth());
		WhNull.check(businessCapacityItem.getProductValue());
		businessCapacityItem.setProductValue(businessCapacityItem.getProductValue().setScale(2, BigDecimal.ROUND_HALF_UP));
		if (businessCapacityItem.getProductValue().compareTo(BigDecimal.valueOf(0)) < 0) {
			throw new IllegalArgumentException();
		}

		/*
		 * 校验项目
		 */
		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectService.getById(projectEx.getId()));

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		Target targetCheck = targetService.getByProjectId(projectEx.getId());
		WhOwnerShipUtil.checkTarget(targetCheck);

		// 校验月份
		WhYearMonthUtil.check(businessCapacityItem.getYear(), businessCapacityItem.getMonth(),
				targetCheck.getStartDate());

		BusinessCapacityItem businessCapacityItemExist = businessCapacityItemService.getByProjectYearMonth(
				projectEx.getId(), businessCapacityItem.getYear(), businessCapacityItem.getMonth());
		if (null != businessCapacityItemExist) {
			throw new WhBusinessCapacityItemExistException();
		}

		/*
		 * 增加经营产值项
		 */
		businessCapacityItem.setCompanyId(projectEx.getCompanyId());
		businessCapacityItem.setProjectId(projectEx.getId());
		businessCapacityItem.setChecked(false);
		businessCapacityItem.setCheckedValue(BigDecimal.valueOf(0));
		businessCapacityItem.setStaffId(staffEx.getId());
		businessCapacityItem.setStaffName(staffEx.getName());
		businessCapacityItemService.add(businessCapacityItem);

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 修改某月经营产值
	 * 
	 * @return
	 * @throws Exception
	 */
	public String modifyItem() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(businessCapacityItem);
		WhNull.check(businessCapacityItem.getId());
		WhNull.check(businessCapacityItem.getProductValue());
		businessCapacityItem.setProductValue(businessCapacityItem.getProductValue().setScale(2, BigDecimal.ROUND_HALF_UP));
		if (businessCapacityItem.getProductValue().compareTo(BigDecimal.valueOf(0)) < 0) {
			throw new IllegalArgumentException();
		}

		/*
		 * 校验项目
		 */
		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectService.getById(projectEx.getId()));

		/*
		 * 校验经营产值项
		 */
		BusinessCapacityItem businessCapacityItemCheck = businessCapacityItemService.getById(businessCapacityItem
				.getId());
		WhOwnerShipUtil.checkBusinessCapacityItemProject(businessCapacityItemCheck, projectEx);

		// 修改
		businessCapacityItemCheck.setProductValue(businessCapacityItem.getProductValue());
		businessCapacityItemService.modifyById(businessCapacityItemCheck);

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 删除某月经营产值
	 * 
	 * @return
	 * @throws Exception
	 */
	public String removeItem() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(businessCapacityItem);
		WhNull.check(businessCapacityItem.getId());

		/*
		 * 校验项目
		 */
		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectService.getById(projectEx.getId()));

		/*
		 * 校验经营产值项
		 */
		BusinessCapacityItem businessCapacityItemCheck = businessCapacityItemService.getById(businessCapacityItem
				.getId());
		WhOwnerShipUtil.checkBusinessCapacityItemProject(businessCapacityItemCheck, projectEx);
		if (businessCapacityItemCheck.getChecked()) {
			throw new WhBusinessCapacityItemCheckedException();
		}

		businessCapacityItemService.removeById(businessCapacityItem.getId());

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}
}
