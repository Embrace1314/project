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
import top.warmheart.workerunion.server.model.QualityCheckItem;
import top.warmheart.workerunion.server.model.QualityCheckItemAttachment;
import top.warmheart.workerunion.server.model.Staff;
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

		Page<QualityCheckItem> pageCheck = qualityCheckItemService.pageByProjectId(projectEx.getId(), page);

		JSONObject json = getSuccessJsonTemplate();
		json.put("page", pageCheck);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取质量检查记录详情
	 * 
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
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

		/*
		 * 校验安全记录
		 */
		QualityCheckItem qualityCheckItemCheck = qualityCheckItemService.getItemById(qualityCheckItem.getId());
		WhOwnerShipUtil.checkProject(qualityCheckItemCheck, projectEx);

		// 获取所属附件列表
		List<QualityCheckItemAttachment> attachments = qualityCheckItemService.listAttachmentByItemId(qualityCheckItem
				.getId());

		JSONObject json = getSuccessJsonTemplate();
		json.put("qualityCheckItem", qualityCheckItemCheck);
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
		WhNull.check(qualityCheckItemAttachment);
		WhNull.check(qualityCheckItemAttachment.getId());

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
		QualityCheckItemAttachment qualityCheckItemAttachmentCheck = qualityCheckItemService
				.getAttachmentById(qualityCheckItemAttachment.getId());
		WhOwnerShipUtil.checkProject(qualityCheckItemAttachmentCheck, projectEx);

		/*
		 * 校验记录
		 */
		QualityCheckItem qualityCheckItemCheck = qualityCheckItemService.getItemById(qualityCheckItemAttachmentCheck
				.getQualityCheckItemId());
		WhOwnerShipUtil.checkProject(qualityCheckItemCheck, projectEx);

		// 删除附件
		qualityCheckItemService.removeAttachmentById(qualityCheckItemAttachment.getId());

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}
	
	/**
	 * 新增质量检查记录
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addItem() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(qualityCheckItem);
		WhNull.check(qualityCheckItem.getInspectDate());
		WhNull.check(qualityCheckItem.getInspectRecord());
		WhNull.check(qualityCheckItem.getRectifyOpinion());
		WhNull.check(qualityCheckItem.getEvaluateDate());
		WhNull.check(qualityCheckItem.getEvaluate());
		WhNull.checkTrimEmpty(qualityCheckItem.getRectifyStatus());
		if (!QualityCheckItem.RECTIFY_STATUS_LIST.contains(qualityCheckItem.getRectifyStatus())) {
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
		qualityCheckItem.setCompanyId(staffEx.getCompanyId());
		qualityCheckItem.setProjectId(project.getId());
		qualityCheckItem.setProjectFeedback("");
		qualityCheckItem.setStaffId(staffEx.getId());
		qualityCheckItem.setStaffName(staffEx.getName());
		BigInteger id = qualityCheckItemService.addItem(qualityCheckItem);

		JSONObject json = getSuccessJsonTemplate();
		json.put("id", id);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 删除质量检查记录
	 * 
	 * @return
	 * @throws Exception
	 */
	public String removeItemById() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(qualityCheckItem);
		WhNull.check(qualityCheckItem.getId());

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
		QualityCheckItem qualityCheckItemCheck = qualityCheckItemService.getItemById(qualityCheckItem.getId());
		WhOwnerShipUtil.checkProject(qualityCheckItemCheck, projectEx);

		// 删除附件
		qualityCheckItemService.removeItemById(qualityCheckItem.getId());

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 修改质量检查记录
	 * 
	 * @return
	 * @throws Exception
	 */
	public String modifyItemById() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(qualityCheckItem);
		WhNull.check(qualityCheckItem.getId());
		WhNull.check(qualityCheckItem.getInspectDate());
		WhNull.check(qualityCheckItem.getInspectRecord());
		WhNull.check(qualityCheckItem.getRectifyOpinion());
		WhNull.check(qualityCheckItem.getEvaluateDate());
		WhNull.check(qualityCheckItem.getEvaluate());
		WhNull.checkTrimEmpty(qualityCheckItem.getRectifyStatus());
		if (!QualityCheckItem.RECTIFY_STATUS_LIST.contains(qualityCheckItem.getRectifyStatus())) {
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
		QualityCheckItem qualityCheckItemCheck = qualityCheckItemService.getItemById(qualityCheckItem.getId());
		WhOwnerShipUtil.checkProject(qualityCheckItemCheck, projectEx);

		// 整理数据，新增记录
		qualityCheckItemCheck.setInspectDate(qualityCheckItem.getInspectDate());
		qualityCheckItemCheck.setInspectRecord(qualityCheckItem.getInspectRecord());
		qualityCheckItemCheck.setRectifyOpinion(qualityCheckItem.getRectifyOpinion());
		qualityCheckItemCheck.setEvaluateDate(qualityCheckItem.getEvaluateDate());
		qualityCheckItemCheck.setEvaluate(qualityCheckItem.getEvaluate());
		qualityCheckItemCheck.setRectifyStatus(qualityCheckItem.getRectifyStatus());

		qualityCheckItemService.modifyItemById(qualityCheckItemCheck);

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}

}
