package top.warmheart.workerunion.pm.server.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;

import top.warmheart.server.util.dto.Page;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.pm.server.action.ex.ActionJson;
import top.warmheart.workerunion.pm.server.constant.SessionKey;
import top.warmheart.workerunion.pm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.exception.WhAttachmentAuditedException;
import top.warmheart.workerunion.server.exception.WhInvalidAttachmentException;
import top.warmheart.workerunion.server.exception.WhSafetyCheckItemModifyDisabledException;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.model.SafetyCheckItem;
import top.warmheart.workerunion.server.model.SafetyCheckItemAttachment;
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
		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);

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
		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);

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
	 * 申请审批安全检查记录
	 * 
	 * @return
	 * @throws Exception
	 */
	public String applyItemVerify() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(safetyCheckItem);
		WhNull.check(safetyCheckItem.getId());
		WhNull.check(safetyCheckItem.getProjectFeedback());

		/*
		 * 校验项目
		 */
		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);
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

		// 修改
		safetyCheckItemCheck.setProjectFeedback(safetyCheckItem.getProjectFeedback());
		safetyCheckItemCheck.setRectifyStatus(SafetyCheckItem.RECTIFY_STATUS_VERIFY);
		safetyCheckItemService.modifyItemById(safetyCheckItemCheck);

		JSONObject json = getSuccessJsonTemplate();
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
		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);
		
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectService.getById(projectEx.getId()));

		/*
		 * 校验附件
		 */
		SafetyCheckItemAttachment safetyCheckItemAttachmentCheck = safetyCheckItemService.getAttachmentById(safetyCheckItemAttachment.getId());
		WhOwnerShipUtil.checkProject(safetyCheckItemAttachmentCheck, projectEx);
		if (!SafetyCheckItemAttachment.TYPE_CALLBACK.equalsIgnoreCase(safetyCheckItemAttachmentCheck.getType())) {
			throw new WhInvalidAttachmentException();
		}
		
		/*
		 * 校验记录
		 */
		SafetyCheckItem safetyCheckItemCheck = safetyCheckItemService.getItemById(safetyCheckItemAttachmentCheck.getSafetyCheckItemId());
		WhOwnerShipUtil.checkProject(safetyCheckItemCheck, projectEx);
		if (!SafetyCheckItem.RECTIFY_STATUS_RECTIFY.equalsIgnoreCase(safetyCheckItemCheck.getRectifyStatus())){
			throw new WhAttachmentAuditedException();
		}
		
		// 删除附件
		safetyCheckItemService.removeAttachmentById(safetyCheckItemAttachment.getId());

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}
}
