package top.warmheart.workerunion.fm.server.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;

import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.fm.server.action.ex.ActionJson;
import top.warmheart.workerunion.fm.server.constant.SessionKey;
import top.warmheart.workerunion.fm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.dto.FundPlanItemDto;
import top.warmheart.workerunion.server.model.FundPlanItem;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.model.Staff;
import top.warmheart.workerunion.server.service.CostAnalysisItemDeepenService;
import top.warmheart.workerunion.server.service.FundPlanItemService;
import top.warmheart.workerunion.server.service.ProjectService;
import top.warmheart.workerunion.server.service.PurchaseItemService;
import top.warmheart.workerunion.server.service.ResourceImplementItemService;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("serial")
@Scope("prototype")
public class FundPlanAction extends ActionJson {
	private Project project;
	private FundPlanItem fundPlanItem;
	private FundPlanItemService fundPlanItemService;
	private PurchaseItemService purchaseItemService;
	private CostAnalysisItemDeepenService costAnalysisItemDeepenService;
	private ResourceImplementItemService resourceImplementItemService;
	private ProjectService projectService;
	
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public FundPlanItem getFundPlanItem() {
		return fundPlanItem;
	}

	public void setFundPlanItem(FundPlanItem fundPlanItem) {
		this.fundPlanItem = fundPlanItem;
	}

	public FundPlanItemService getFundPlanItemService() {
		return fundPlanItemService;
	}

	@Resource(name = "fundPlanItemService")
	public void setFundPlanItemService(FundPlanItemService fundPlanItemService) {
		this.fundPlanItemService = fundPlanItemService;
	}

	public PurchaseItemService getPurchaseItemService() {
		return purchaseItemService;
	}

	@Resource(name = "purchaseItemService")
	public void setPurchaseItemService(PurchaseItemService purchaseItemService) {
		this.purchaseItemService = purchaseItemService;
	}

	public CostAnalysisItemDeepenService getCostAnalysisItemDeepenService() {
		return costAnalysisItemDeepenService;
	}

	@Resource(name = "costAnalysisItemDeepenService")
	public void setCostAnalysisItemDeepenService(CostAnalysisItemDeepenService costAnalysisItemDeepenService) {
		this.costAnalysisItemDeepenService = costAnalysisItemDeepenService;
	}

	public ResourceImplementItemService getResourceImplementItemService() {
		return resourceImplementItemService;
	}

	@Resource(name = "resourceImplementItemService")
	public void setResourceImplementItemService(ResourceImplementItemService resourceImplementItemService) {
		this.resourceImplementItemService = resourceImplementItemService;
	}
	public ProjectService getProjectService() {
		return projectService;
	}

	@Resource(name = "projectService")
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}
	/**
	 * 获取资金计划项
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getItemById() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(fundPlanItem);
		WhNull.check(fundPlanItem.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

		/*
		 * 查找并校验
		 */
		FundPlanItem fundPlanItemCheck = fundPlanItemService.getById(fundPlanItem.getId());
		WhOwnerShipUtil.checkProject(fundPlanItemCheck, projectEx);

		JSONObject json = getSuccessJsonTemplate();
		json.put("fundPlanItem", fundPlanItemCheck);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取资金计划项列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String listItem() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

		// 获取资金计划项列表
		List<FundPlanItemDto> fundPlanItems = fundPlanItemService.listByProjectId(
				projectEx.getId());

		JSONObject json = getSuccessJsonTemplate();
		json.put("fundPlanItems", fundPlanItems);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取简要的资金计划项列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String listSimpleItem() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

		// 获取资金计划项列表
		List<FundPlanItem> fundPlanItems = fundPlanItemService.listSimpleByProjectId(
				projectEx.getId());

		JSONObject json = getSuccessJsonTemplate();
		json.put("fundPlanItems", fundPlanItems);
		writeStream(json);
		return SUCCESS;
	}
	
}
