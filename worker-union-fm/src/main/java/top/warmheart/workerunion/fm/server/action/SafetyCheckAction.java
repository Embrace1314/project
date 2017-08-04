package top.warmheart.workerunion.fm.server.action;

import java.math.BigInteger;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;

import top.warmheart.server.util.dto.Page;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.fm.server.action.ex.ActionJson;
import top.warmheart.workerunion.fm.server.constant.SessionKey;
import top.warmheart.workerunion.fm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.model.SafetyCheckItem;
import top.warmheart.workerunion.server.model.SafetyCheckItemAttachment;
import top.warmheart.workerunion.server.model.Staff;
import top.warmheart.workerunion.server.service.ProjectService;
import top.warmheart.workerunion.server.service.SafetyCheckItemService;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("serial")
@Scope("prototype")
public class SafetyCheckAction extends ActionJson {
	private Project project;
	private Page<Void> page;
	private SafetyCheckItem safetyCheckItem;
	private SafetyCheckItemService safetyCheckItemService;
	private SafetyCheckItemAttachment safetyCheckItemAttachment;
	private ProjectService projectService;

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Page<Void> getPage() {
		return page;
	}

	public void setPage(Page<Void> page) {
		this.page = page;
	}

	public SafetyCheckItem getSafetyCheckItem() {
		return safetyCheckItem;
	}

	public void setSafetyCheckItem(SafetyCheckItem safetyCheckItem) {
		this.safetyCheckItem = safetyCheckItem;
	}

	public SafetyCheckItemService getSafetyCheckItemService() {
		return safetyCheckItemService;
	}

	public SafetyCheckItemAttachment getSafetyCheckItemAttachment() {
		return safetyCheckItemAttachment;
	}

	public void setSafetyCheckItemAttachment(SafetyCheckItemAttachment safetyCheckItemAttachment) {
		this.safetyCheckItemAttachment = safetyCheckItemAttachment;
	}

	@Resource(name = "safetyCheckItemService")
	public void setSafetyCheckItemService(SafetyCheckItemService safetyCheckItemService) {
		this.safetyCheckItemService = safetyCheckItemService;
	}

	public ProjectService getProjectService() {
		return projectService;
	}

	@Resource(name = "projectService")
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	/**
	 * 分页获取项目安全检查项列表
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

		Page<SafetyCheckItem> pageCheck = safetyCheckItemService.pageByProjectId(projectEx.getId(), page);

		JSONObject json = getSuccessJsonTemplate();
		json.put("page", pageCheck);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取安全检查记录详情
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getItemDetail() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(safetyCheckItem);
		WhNull.check(safetyCheckItem.getId());

		/*
		 * 校验项目
		 */
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

		/*
		 * 校验安全记录
		 */
		SafetyCheckItem safetyCheckItemCheck = safetyCheckItemService.getItemById(safetyCheckItem.getId());
		WhOwnerShipUtil.checkProject(safetyCheckItemCheck, projectEx);

		// 获取所属附件列表
		List<SafetyCheckItemAttachment> attachments = safetyCheckItemService.listAttachmentByItemId(safetyCheckItem
				.getId());

		JSONObject json = getSuccessJsonTemplate();
		json.put("safetyCheckItem", safetyCheckItemCheck);
		json.put("attachments", attachments);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 删除安全检查附件
	 * 
	 * @return
	 * @throws Exception
	 */
	public String removeAttachmentById() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(safetyCheckItemAttachment);
		WhNull.check(safetyCheckItemAttachment.getId());

		/*
		 * 校验项目
		 */
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectEx);

		/*
		 * 校验附件
		 */
		SafetyCheckItemAttachment safetyCheckItemAttachmentCheck = safetyCheckItemService
				.getAttachmentById(safetyCheckItemAttachment.getId());
		WhOwnerShipUtil.checkProject(safetyCheckItemAttachmentCheck, projectEx);

		/*
		 * 校验记录
		 */
		SafetyCheckItem safetyCheckItemCheck = safetyCheckItemService.getItemById(safetyCheckItemAttachmentCheck
				.getSafetyCheckItemId());
		WhOwnerShipUtil.checkProject(safetyCheckItemCheck, projectEx);

		// 删除附件
		safetyCheckItemService.removeAttachmentById(safetyCheckItemAttachment.getId());

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 新增安全检查记录
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addItem() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(safetyCheckItem);
		WhNull.check(safetyCheckItem.getInspectDate());
		WhNull.checkEmpty(safetyCheckItem.getInspectRecord());
		WhNull.check(safetyCheckItem.getRectifyOpinion());
		WhNull.check(safetyCheckItem.getEvaluateDate());
		WhNull.check(safetyCheckItem.getEvaluate());
		WhNull.checkTrimEmpty(safetyCheckItem.getRectifyStatus());
		if (!SafetyCheckItem.RECTIFY_STATUS_LIST.contains(safetyCheckItem.getRectifyStatus())) {
			throw new IllegalArgumentException();
		}

		/*
		 * 校验项目
		 */
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectEx);

		// 整理数据，新增记录
		safetyCheckItem.setCompanyId(staffEx.getCompanyId());
		safetyCheckItem.setProjectId(project.getId());
		safetyCheckItem.setProjectFeedback("");
		safetyCheckItem.setStaffId(staffEx.getId());
		safetyCheckItem.setStaffName(staffEx.getName());
		BigInteger id = safetyCheckItemService.addItem(safetyCheckItem);

		JSONObject json = getSuccessJsonTemplate();
		json.put("id", id);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 删除安全检查记录
	 * 
	 * @return
	 * @throws Exception
	 */
	public String removeItemById() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(safetyCheckItem);
		WhNull.check(safetyCheckItem.getId());

		/*
		 * 校验项目
		 */
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectEx);

		/*
		 * 校验记录
		 */
		SafetyCheckItem safetyCheckItemCheck = safetyCheckItemService.getItemById(safetyCheckItem.getId());
		WhOwnerShipUtil.checkProject(safetyCheckItemCheck, projectEx);

		// 删除附件
		safetyCheckItemService.removeItemById(safetyCheckItem.getId());

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 修改安全检查记录
	 * 
	 * @return
	 * @throws Exception
	 */
	public String modifyItemById() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(safetyCheckItem);
		WhNull.check(safetyCheckItem.getId());
		WhNull.check(safetyCheckItem.getInspectDate());
		WhNull.checkEmpty(safetyCheckItem.getInspectRecord());
		WhNull.check(safetyCheckItem.getRectifyOpinion());
		WhNull.check(safetyCheckItem.getEvaluateDate());
		WhNull.check(safetyCheckItem.getEvaluate());
		WhNull.checkTrimEmpty(safetyCheckItem.getRectifyStatus());
		if (!SafetyCheckItem.RECTIFY_STATUS_LIST.contains(safetyCheckItem.getRectifyStatus())) {
			throw new IllegalArgumentException();
		}

		/*
		 * 校验项目
		 */
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectEx);
		/*
		 * 校验记录
		 */
		SafetyCheckItem safetyCheckItemCheck = safetyCheckItemService.getItemById(safetyCheckItem.getId());
		WhOwnerShipUtil.checkProject(safetyCheckItemCheck, projectEx);

		// 整理数据，新增记录
		safetyCheckItemCheck.setInspectDate(safetyCheckItem.getInspectDate());
		safetyCheckItemCheck.setInspectRecord(safetyCheckItem.getInspectRecord());
		safetyCheckItemCheck.setRectifyOpinion(safetyCheckItem.getRectifyOpinion());
		safetyCheckItemCheck.setEvaluateDate(safetyCheckItem.getEvaluateDate());
		safetyCheckItemCheck.setEvaluate(safetyCheckItem.getEvaluate());
		safetyCheckItemCheck.setRectifyStatus(safetyCheckItem.getRectifyStatus());

		safetyCheckItemService.modifyItemById(safetyCheckItemCheck);

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}
}
