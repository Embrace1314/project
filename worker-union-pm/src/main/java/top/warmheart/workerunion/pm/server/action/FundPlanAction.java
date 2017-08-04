package top.warmheart.workerunion.pm.server.action;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;

import top.warmheart.server.util.WhStringUtil;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.pm.server.action.ex.ActionJson;
import top.warmheart.workerunion.pm.server.constant.SessionKey;
import top.warmheart.workerunion.pm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.dto.FundPlanItemDto;
import top.warmheart.workerunion.server.exception.WhFundPlanItemLinkedException;
import top.warmheart.workerunion.server.model.CostAnalysisItemDeepen;
import top.warmheart.workerunion.server.model.FundPlanItem;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.model.PurchaseItem;
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

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);

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

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);

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

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);

		// 获取资金计划项列表
		List<FundPlanItem> fundPlanItems = fundPlanItemService.listSimpleByProjectId(
				projectEx.getId());

		JSONObject json = getSuccessJsonTemplate();
		json.put("fundPlanItems", fundPlanItems);
		writeStream(json);
		return SUCCESS;
	}
	/**
	 * 增加新的资金计划项
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addItem() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(fundPlanItem);
		WhNull.check(fundPlanItem.getPurchaseItemId());
		WhNull.checkTrimEmpty(fundPlanItem.getRequirement());
		WhNull.check(fundPlanItem.getMoney());
		WhNull.check(fundPlanItem.getExpectedPurchaseDate());
		WhNull.check(fundPlanItem.getExpectedServiceDate());
		WhNull.check(fundPlanItem.getCostAnalysisItemDeepenId());
		fundPlanItem.setMoney(fundPlanItem.getMoney().setScale(2, BigDecimal.ROUND_HALF_UP));
		if (fundPlanItem.getMoney().compareTo(BigDecimal.valueOf(0)) < 0) {
			throw new IllegalArgumentException();
		}
		
		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectService.getById(projectEx.getId()));

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);
				
		/*
		 * 校验采购条目
		 */
		PurchaseItem purchaseItemCheck = purchaseItemService.getById(fundPlanItem.getPurchaseItemId());
		WhOwnerShipUtil.checkPurchaseItem(purchaseItemCheck);
		
		/*
		 * 校验成本深化项
		 */
		CostAnalysisItemDeepen costAnalysisItemDeepenCheck = costAnalysisItemDeepenService.getById(fundPlanItem.getCostAnalysisItemDeepenId());
		WhOwnerShipUtil.checkProject(costAnalysisItemDeepenCheck, projectEx);
		
		fundPlanItem.setCompanyId(projectEx.getCompanyId());
		fundPlanItem.setProjectId(projectEx.getId());
		fundPlanItem.setPurchaseItemName(purchaseItemCheck.getName());
		
		fundPlanItemService.add(fundPlanItem);

		writeStream(getSuccessJsonTemplate());
		return SUCCESS;
	}

	/**
	 * 修改资金计划项
	 * 
	 * @return
	 * @throws Exception
	 */
	public String modifyItemById() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(fundPlanItem);
		WhNull.check(fundPlanItem.getId());
		WhNull.checkTrimEmpty(fundPlanItem.getRequirement());
		WhNull.check(fundPlanItem.getMoney());
		WhNull.check(fundPlanItem.getExpectedPurchaseDate());
		WhNull.check(fundPlanItem.getExpectedServiceDate());
		WhNull.check(fundPlanItem.getCostAnalysisItemDeepenId());
		fundPlanItem.setMoney(fundPlanItem.getMoney().setScale(2, BigDecimal.ROUND_HALF_UP));
		if (fundPlanItem.getMoney().compareTo(BigDecimal.valueOf(0)) < 0) {
			throw new IllegalArgumentException();
		}
		fundPlanItem.setRequirement(WhStringUtil.trimAll(fundPlanItem.getRequirement()));
		
		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectService.getById(projectEx.getId()));

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);
		
		/*
		 * 校验资金计划项
		 */
		FundPlanItem fundPlanItemCheck = fundPlanItemService.getById(fundPlanItem.getId());
		WhOwnerShipUtil.checkProject(fundPlanItemCheck, projectEx);
		
		/*
		 * 校验成本深化项
		 */
		CostAnalysisItemDeepen costAnalysisItemDeepenCheck = costAnalysisItemDeepenService.getById(fundPlanItem.getCostAnalysisItemDeepenId());
		WhOwnerShipUtil.checkProject(costAnalysisItemDeepenCheck, projectEx);
		
		
		fundPlanItemCheck.setRequirement(fundPlanItem.getRequirement());
		fundPlanItemCheck.setMoney(fundPlanItem.getMoney());
		fundPlanItemCheck.setExpectedPurchaseDate(fundPlanItem.getExpectedPurchaseDate());
		fundPlanItemCheck.setExpectedServiceDate(fundPlanItem.getExpectedServiceDate());
		fundPlanItemCheck.setCostAnalysisItemDeepenId(fundPlanItem.getCostAnalysisItemDeepenId());

		fundPlanItemService.modifyById(fundPlanItemCheck);

		writeStream(getSuccessJsonTemplate());
		return SUCCESS;
	}

	/**
	 * 删除资金计划项
	 * 
	 * @return
	 * @throws Exception
	 */
	public String removeItemById() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(fundPlanItem);
		WhNull.check(fundPlanItem.getId());

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectService.getById(projectEx.getId()));

		/*
		 * 校验成本项合法性
		 */
		FundPlanItem fundPlanItemCheck = fundPlanItemService.getById(fundPlanItem.getId());
		WhOwnerShipUtil.checkProject(fundPlanItemCheck, projectEx);

		/*
		 * 检查关联的资源落实项
		 */
		if(!resourceImplementItemService.listByFundPlanItemId(fundPlanItem.getId()).isEmpty()){
			throw new WhFundPlanItemLinkedException();
		}
		
		fundPlanItemService.removeById(fundPlanItem.getId());

		writeStream(getSuccessJsonTemplate());
		return SUCCESS;
	}
}
