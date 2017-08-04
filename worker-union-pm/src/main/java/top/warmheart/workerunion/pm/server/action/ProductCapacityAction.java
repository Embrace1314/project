package top.warmheart.workerunion.pm.server.action;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;

import top.warmheart.server.util.dto.Page;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.pm.server.action.ex.ActionJson;
import top.warmheart.workerunion.pm.server.constant.SessionKey;
import top.warmheart.workerunion.pm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.pm.server.util.WhYearMonthUtil;
import top.warmheart.workerunion.server.exception.WhInvalidAttachmentException;
import top.warmheart.workerunion.server.exception.WhProductCapacityItemCheckedException;
import top.warmheart.workerunion.server.exception.WhProductCapacityItemExistException;
import top.warmheart.workerunion.server.model.Attachment;
import top.warmheart.workerunion.server.model.ProductCapacityItem;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.model.Staff;
import top.warmheart.workerunion.server.model.Target;
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
		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);

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
		if (!Attachment.TYPE_PRODUCT_CAPACITY_FILE.equalsIgnoreCase(attachmentCheck.getType())) {
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

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);

		Page<ProductCapacityItem> pageCheck = productCapacityItemService.pageByProjectId(projectEx.getId(), page);

		JSONObject json = getSuccessJsonTemplate();
		json.put("page", pageCheck);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 添加某月生产产值
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addItem() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(productCapacityItem);
		WhNull.check(productCapacityItem.getYear());
		WhNull.check(productCapacityItem.getMonth());
		WhNull.check(productCapacityItem.getExpectedState());
		WhNull.check(productCapacityItem.getActualState());
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
		WhYearMonthUtil
				.check(productCapacityItem.getYear(), productCapacityItem.getMonth(), targetCheck.getStartDate());

		ProductCapacityItem productCapacityItemExist = productCapacityItemService.getByProjectYearMonth(
				projectEx.getId(), productCapacityItem.getYear(), productCapacityItem.getMonth());
		if (null != productCapacityItemExist) {
			throw new WhProductCapacityItemExistException();
		}
		
		/*
		 * 增加生产产值项
		 */
		productCapacityItem.setCompanyId(projectEx.getCompanyId());
		productCapacityItem.setProjectId(projectEx.getId());
		productCapacityItem.setChecked(false);
		productCapacityItem.setCheckedState("");
		productCapacityItem.setStaffId(staffEx.getId());
		productCapacityItem.setStaffName(staffEx.getName());
		productCapacityItemService.add(productCapacityItem);

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 修改某月生产产值
	 * 
	 * @return
	 * @throws Exception
	 */
	public String modifyItem() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(productCapacityItem);
		WhNull.check(productCapacityItem.getId());
		WhNull.check(productCapacityItem.getActualState());

		/*
		 * 校验项目
		 */
		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectService.getById(projectEx.getId()));

		/*
		 * 校验生产产值项
		 */
		ProductCapacityItem productCapacityItemCheck = productCapacityItemService.getById(productCapacityItem.getId());
		WhOwnerShipUtil.checkProductCapacityItemProject(productCapacityItemCheck, projectEx);

		// 修改
		productCapacityItemCheck.setActualState(productCapacityItem.getActualState());
		productCapacityItemService.modifyById(productCapacityItemCheck);

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 删除某月生产产值
	 * 
	 * @return
	 * @throws Exception
	 */
	public String removeItem() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(productCapacityItem);
		WhNull.check(productCapacityItem.getId());

		/*
		 * 校验项目
		 */
		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectService.getById(projectEx.getId()));

		/*
		 * 校验生产产值项
		 */
		ProductCapacityItem productCapacityItemCheck = productCapacityItemService.getById(productCapacityItem.getId());
		WhOwnerShipUtil.checkProductCapacityItemProject(productCapacityItemCheck, projectEx);
		if (productCapacityItemCheck.getChecked()) {
			throw new WhProductCapacityItemCheckedException();
		}

		productCapacityItemService.removeById(productCapacityItem.getId());

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}
}
