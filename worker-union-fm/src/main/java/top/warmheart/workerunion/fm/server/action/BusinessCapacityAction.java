package top.warmheart.workerunion.fm.server.action;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;

import top.warmheart.server.util.dto.Page;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.fm.server.action.ex.ActionJson;
import top.warmheart.workerunion.fm.server.constant.SessionKey;
import top.warmheart.workerunion.fm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.dto.BusinessCapacityItemDto;
import top.warmheart.workerunion.server.model.Attachment;
import top.warmheart.workerunion.server.model.BusinessCapacityItem;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.model.Staff;
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
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

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

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

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

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

		Page<BusinessCapacityItemDto> pageCheck = businessCapacityItemService.pageByProjectId(projectEx.getId(), page);

		JSONObject json = getSuccessJsonTemplate();
		json.put("page", pageCheck);
		writeStream(json);
		return SUCCESS;
	}
	/**
	 * 核定产值
	 * 
	 * @return
	 * @throws Exception
	 */
	public String checkItem() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(businessCapacityItem);
		WhNull.check(businessCapacityItem.getId());
		WhNull.check(businessCapacityItem.getCheckedValue());
		businessCapacityItem.setCheckedValue(businessCapacityItem.getCheckedValue().setScale(2, BigDecimal.ROUND_HALF_UP));
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectEx);
		
		/*
		 * 校验产值项
		 */
		BusinessCapacityItem businessCapacityItemCheck = businessCapacityItemService.getById(businessCapacityItem.getId());
		WhOwnerShipUtil.checkBusinessCapacityItemProject(businessCapacityItemCheck, projectEx);
		
		// 设置
		businessCapacityItemCheck.setCheckedValue(businessCapacityItem.getCheckedValue());
		businessCapacityItemCheck.setChecked(true);
		businessCapacityItemService.modifyById(businessCapacityItemCheck);
		
		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}

}
