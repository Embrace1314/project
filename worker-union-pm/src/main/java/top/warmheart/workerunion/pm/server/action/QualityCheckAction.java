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
import top.warmheart.workerunion.server.exception.WhQualityCheckItemModifyDisabledException;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.model.QualityCheckItem;
import top.warmheart.workerunion.server.model.QualityCheckItemAttachment;
import top.warmheart.workerunion.server.model.SafetyCheckItem;
import top.warmheart.workerunion.server.service.ProjectService;
import top.warmheart.workerunion.server.service.QualityCheckItemService;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("serial")
@Scope("prototype")
public class QualityCheckAction extends ActionJson {
	private Project project;
	private Page<Void> page;
	private QualityCheckItem qualityCheckItem;
	private QualityCheckItemService qualityCheckItemService;
	private ProjectService projectService;
	private QualityCheckItemAttachment qualityCheckItemAttachment;
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

	public QualityCheckItemService getQualityCheckItemService() {
		return qualityCheckItemService;
	}

	@Resource(name = "qualityCheckItemService")
	public void setQualityCheckItemService(QualityCheckItemService qualityCheckItemService) {
		this.qualityCheckItemService = qualityCheckItemService;
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

		Page<QualityCheckItem> pageCheck = qualityCheckItemService.pageByProjectId(projectEx.getId(), page);
		
		JSONObject json = getSuccessJsonTemplate();
		json.put("page", pageCheck);
		writeStream(json);
		return SUCCESS;
	}
	
	/**
	 * 获取质量检查记录详情
	 * @return
	 * @throws Exception
	 */
	public String getItemDetail() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(qualityCheckItem);
		WhNull.check(qualityCheckItem.getId());
		
		/*
		 * 校验项目
		 */
		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);

		/*
		 * 校验安全记录
		 */
		QualityCheckItem qualityCheckItemCheck = qualityCheckItemService.getItemById(qualityCheckItem.getId());
		WhOwnerShipUtil.checkProject(qualityCheckItemCheck, projectEx);
		
		// 获取所属附件列表
		List<QualityCheckItemAttachment> attachments = qualityCheckItemService.listAttachmentByItemId(qualityCheckItem.getId());
		
		JSONObject json = getSuccessJsonTemplate();
		json.put("qualityCheckItem", qualityCheckItemCheck);
		json.put("attachments", attachments);
		writeStream(json);
		return SUCCESS;
	}
	
	/**
	 * 申请审批质量检查记录
	 * @return
	 * @throws Exception
	 */
	public String applyItemVerify() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(qualityCheckItem);
		WhNull.check(qualityCheckItem.getId());
		WhNull.check(qualityCheckItem.getProjectFeedback());
		
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
		QualityCheckItem qualityCheckItemCheck = qualityCheckItemService.getItemById(qualityCheckItem.getId());
		WhOwnerShipUtil.checkProject(qualityCheckItemCheck, projectEx);
		if (!QualityCheckItem.RECTIFY_STATUS_RECTIFY.equalsIgnoreCase(qualityCheckItemCheck.getRectifyStatus())){
			throw new WhQualityCheckItemModifyDisabledException();
		}
		
		// 修改
		qualityCheckItemCheck.setProjectFeedback(qualityCheckItem.getProjectFeedback());
		qualityCheckItemCheck.setRectifyStatus(QualityCheckItem.RECTIFY_STATUS_VERIFY);
		qualityCheckItemService.modifyItemById(qualityCheckItemCheck);
		
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
		WhNull.check(qualityCheckItemAttachment);
		WhNull.check(qualityCheckItemAttachment.getId());

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
		QualityCheckItemAttachment qualityCheckItemAttachmentCheck = qualityCheckItemService.getAttachmentById(qualityCheckItemAttachment.getId());
		WhOwnerShipUtil.checkProject(qualityCheckItemAttachmentCheck, projectEx);
		if (!QualityCheckItemAttachment.TYPE_CALLBACK.equalsIgnoreCase(qualityCheckItemAttachmentCheck.getType())) {
			throw new WhInvalidAttachmentException();
		}
		
		/*
		 * 校验记录
		 */
		QualityCheckItem qualityCheckItemCheck = qualityCheckItemService.getItemById(qualityCheckItemAttachmentCheck.getQualityCheckItemId());
		WhOwnerShipUtil.checkProject(qualityCheckItemCheck, projectEx);
		if (!SafetyCheckItem.RECTIFY_STATUS_RECTIFY.equalsIgnoreCase(qualityCheckItemCheck.getRectifyStatus())){
			throw new WhAttachmentAuditedException();
		}
		
		// 删除附件
		qualityCheckItemService.removeAttachmentById(qualityCheckItemAttachment.getId());

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}
}
