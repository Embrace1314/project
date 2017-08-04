package top.warmheart.workerunion.fm.server.action;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;

import top.warmheart.server.util.dto.Page;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.fm.server.action.ex.ActionJson;
import top.warmheart.workerunion.fm.server.constant.SessionKey;
import top.warmheart.workerunion.fm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.model.Attachment;
import top.warmheart.workerunion.server.model.ProductCapacityItem;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.model.Staff;
import top.warmheart.workerunion.server.service.AttachmentService;
import top.warmheart.workerunion.server.service.ProductCapacityItemService;
import top.warmheart.workerunion.server.service.ProjectService;
import top.warmheart.workerunion.server.service.TargetService;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("serial")
@Scope("prototype")
public class ProductCapacityAction extends ActionJson {
	private Project project;
	private Attachment attachment;
	private ProductCapacityItem productCapacityItem;
	private Page<Void> page;
	private AttachmentService attachmentService;
	private TargetService targetService;
	private ProductCapacityItemService productCapacityItemService;
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

	public ProductCapacityItem getProductCapacityItem() {
		return productCapacityItem;
	}

	public void setProductCapacityItem(ProductCapacityItem productCapacityItem) {
		this.productCapacityItem = productCapacityItem;
	}

	public ProductCapacityItemService getProductCapacityItemService() {
		return productCapacityItemService;
	}

	@Resource(name = "productCapacityItemService")
	public void setProductCapacityItemService(ProductCapacityItemService productCapacityItemService) {
		this.productCapacityItemService = productCapacityItemService;
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
				Attachment.TYPE_PRODUCT_CAPACITY_FILE);

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
				Attachment.TYPE_PRODUCT_CAPACITY_FILE, page);

		JSONObject json = getSuccessJsonTemplate();
		json.put("page", pageCheck);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 分页获取生产产值项列表信息
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

		Page<ProductCapacityItem> pageCheck = productCapacityItemService.pageByProjectId(projectEx.getId(), page);

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
		WhNull.check(productCapacityItem);
		WhNull.check(productCapacityItem.getId());
		WhNull.check(productCapacityItem.getCheckedState());
		
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectEx);
		
		/*
		 * 校验产值项
		 */
		ProductCapacityItem productCapacityItemCheck = productCapacityItemService.getById(productCapacityItem.getId());
		WhOwnerShipUtil.checkProductCapacityItemProject(productCapacityItemCheck, projectEx);
		
		productCapacityItemCheck.setCheckedState(productCapacityItem.getCheckedState());
		productCapacityItemCheck.setChecked(true);
		productCapacityItemService.modifyById(productCapacityItemCheck);
		
		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}
}
