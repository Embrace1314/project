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
import top.warmheart.workerunion.server.exception.WhCostAnalysisItemDeepenLinkedException;
import top.warmheart.workerunion.server.exception.WhInvalidAttachmentException;
import top.warmheart.workerunion.server.model.Attachment;
import top.warmheart.workerunion.server.model.CostAnalysisItemDeepen;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.service.AttachmentService;
import top.warmheart.workerunion.server.service.CostAnalysisItemDeepenService;
import top.warmheart.workerunion.server.service.FundPlanItemService;
import top.warmheart.workerunion.server.service.ProjectService;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("serial")
@Scope("prototype")
public class CostAnalysisDeepenAction extends ActionJson {
	private Project project;
	private Attachment attachment;
	private CostAnalysisItemDeepen costAnalysisItemDeepen;
	private AttachmentService attachmentService;
	private CostAnalysisItemDeepenService costAnalysisItemDeepenService;
	private FundPlanItemService fundPlanItemService;
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

	public CostAnalysisItemDeepen getCostAnalysisItemDeepen() {
		return costAnalysisItemDeepen;
	}

	public void setCostAnalysisItemDeepen(CostAnalysisItemDeepen costAnalysisItemDeepen) {
		this.costAnalysisItemDeepen = costAnalysisItemDeepen;
	}

	public AttachmentService getAttachmentService() {
		return attachmentService;
	}

	@Resource(name = "attachmentService")
	public void setAttachmentService(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}

	public CostAnalysisItemDeepenService getCostAnalysisItemDeepenService() {
		return costAnalysisItemDeepenService;
	}

	@Resource(name = "costAnalysisItemDeepenService")
	public void setCostAnalysisItemDeepenService(CostAnalysisItemDeepenService costAnalysisItemDeepenService) {
		this.costAnalysisItemDeepenService = costAnalysisItemDeepenService;
	}

	public FundPlanItemService getFundPlanItemService() {
		return fundPlanItemService;
	}

	@Resource(name = "fundPlanItemService")
	public void setFundPlanItemService(FundPlanItemService fundPlanItemService) {
		this.fundPlanItemService = fundPlanItemService;
	}

	public ProjectService getProjectService() {
		return projectService;
	}

	@Resource(name = "projectService")
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	/**
	 * 获取成本深化项
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getItemById() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(costAnalysisItemDeepen);
		WhNull.check(costAnalysisItemDeepen.getId());

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);

		/*
		 * 查找并校验
		 */
		CostAnalysisItemDeepen costAnalysisItemDeepenCheck = costAnalysisItemDeepenService
				.getById(costAnalysisItemDeepen.getId());
		WhOwnerShipUtil.checkProject(costAnalysisItemDeepenCheck, projectEx);

		JSONObject json = getSuccessJsonTemplate();
		json.put("costAnalysisItemDeepen", costAnalysisItemDeepenCheck);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取唯一的成本深化表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getUniqueAttachment() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);

		// 查找附件信息
		Attachment attachmentCheck = attachmentService.getLatestByType(project.getId(),
				Attachment.TYPE_COST_ANALYSIS_DEEPEN);

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
	 * 获取成本深化详情
	 * 
	 * @return
	 * @throws Exception
	 */
	public String acquireDetail() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);

		// 获取成本深化项列表
		List<CostAnalysisItemDeepen> costAnalysisItemDeepens = costAnalysisItemDeepenService.listByProjectId(projectEx
				.getId());

		/*
		 * 根据具体成本深化项 计算合计金额
		 */
		// 直接成本总额
		BigDecimal directCostSum = BigDecimal.valueOf(0);
		// 间接成本总额
		BigDecimal indirectCostSum = BigDecimal.valueOf(0);
		// 指定分包成本总额
		BigDecimal subcontractCostSum = BigDecimal.valueOf(0);
		// 税额总计
		BigDecimal taxCostSum = BigDecimal.valueOf(0);
		for (CostAnalysisItemDeepen costAnalysisItemDeepen : costAnalysisItemDeepens) {
			switch (costAnalysisItemDeepen.getType()) {
			case CostAnalysisItemDeepen.TYPE_DIRECT_COST:
				directCostSum = directCostSum.add(costAnalysisItemDeepen.getCost());
				break;
			case CostAnalysisItemDeepen.TYPE_INDIRECT_COST:
				indirectCostSum = indirectCostSum.add(costAnalysisItemDeepen.getCost());
				break;
			case CostAnalysisItemDeepen.TYPE_SUBCONTRACT_COST:
				subcontractCostSum = subcontractCostSum.add(costAnalysisItemDeepen.getCost());
				break;
			case CostAnalysisItemDeepen.TYPE_TAX_COST:
				taxCostSum = taxCostSum.add(costAnalysisItemDeepen.getCost());
				break;
			default:
				break;
			}
		}
		// 税前总额
		BigDecimal pretaxSum = directCostSum.add(indirectCostSum).add(subcontractCostSum);
		// 成本总额
		BigDecimal costSum = pretaxSum.add(taxCostSum);
		// 预计利润
		BigDecimal profitSum = projectEx.getBidPrice().subtract(costSum);

		JSONObject json = getSuccessJsonTemplate();
		json.put("directCostSum", directCostSum);
		json.put("indirectCostSum", indirectCostSum);
		json.put("subcontractCostSum", subcontractCostSum);
		json.put("taxCostSum", taxCostSum);
		json.put("pretaxSum", pretaxSum);
		json.put("costSum", costSum);
		json.put("profitSum", profitSum);

		json.put("costAnalysisItemDeepens", costAnalysisItemDeepens);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取成本深化项列表
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

		// 获取成本深化项列表
		List<CostAnalysisItemDeepen> costAnalysisItemDeepens = costAnalysisItemDeepenService.listByProjectId(projectEx
				.getId());

		JSONObject json = getSuccessJsonTemplate();
		json.put("costAnalysisItemDeepens", costAnalysisItemDeepens);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 删除成本深化表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String removeUniqueAttachment() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(attachment);
		WhNull.check(attachment.getId());

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectService.getById(projectEx.getId()));

		/*
		 * 校验附件信息
		 */
		Attachment attachmentCheck = attachmentService.getById(attachment.getId());
		WhOwnerShipUtil.checkProject(attachmentCheck, projectEx);

		if (!attachmentCheck.getType().equalsIgnoreCase(Attachment.TYPE_COST_ANALYSIS_DEEPEN)) {
			throw new WhInvalidAttachmentException();
		}

		attachmentService.removeById(attachmentCheck.getId());

		writeStream(getSuccessJsonTemplate());
		return SUCCESS;
	}

	/**
	 * 增加新的成本分析深化项
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addItem() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(costAnalysisItemDeepen);
		WhNull.checkTrimEmpty(costAnalysisItemDeepen.getType());
		WhNull.checkTrimEmpty(costAnalysisItemDeepen.getName());
		WhNull.check(costAnalysisItemDeepen.getMemo());
		WhNull.check(costAnalysisItemDeepen.getPrice());
		WhNull.check(costAnalysisItemDeepen.getAmount());
		WhNull.checkTrimEmpty(costAnalysisItemDeepen.getUnit());
		costAnalysisItemDeepen.setPrice(costAnalysisItemDeepen.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP));
		costAnalysisItemDeepen.setAmount(costAnalysisItemDeepen.getAmount().setScale(5, BigDecimal.ROUND_HALF_UP));
		if (costAnalysisItemDeepen.getPrice().compareTo(BigDecimal.valueOf(0)) < 0) {
			throw new IllegalArgumentException();
		}
		if (costAnalysisItemDeepen.getAmount().compareTo(BigDecimal.valueOf(0)) < 0) {
			throw new IllegalArgumentException();
		}

		/*
		 * 校验TYPE合法性
		 */
		if (!CostAnalysisItemDeepen.TYPE_LIST.contains(costAnalysisItemDeepen.getType())) {
			throw new IllegalArgumentException();
		}
		/*
		 * 校验项目合法性
		 */
		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectService.getById(projectEx.getId()));

		costAnalysisItemDeepen.setProjectId(projectEx.getId());
		costAnalysisItemDeepen.setCost(costAnalysisItemDeepen.getPrice().multiply(costAnalysisItemDeepen.getAmount()).setScale(2, BigDecimal.ROUND_HALF_UP));
		costAnalysisItemDeepen.setCompanyId(projectEx.getCompanyId());
		costAnalysisItemDeepenService.add(costAnalysisItemDeepen);

		writeStream(getSuccessJsonTemplate());
		return SUCCESS;
	}

	/**
	 * 修改成本分析项
	 * 
	 * @return
	 * @throws Exception
	 */
	public String modifyItemById() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(costAnalysisItemDeepen);
		WhNull.check(costAnalysisItemDeepen.getId());
		WhNull.checkTrimEmpty(costAnalysisItemDeepen.getType());
		WhNull.checkTrimEmpty(costAnalysisItemDeepen.getName());
		WhNull.check(costAnalysisItemDeepen.getMemo());
		WhNull.check(costAnalysisItemDeepen.getPrice());
		WhNull.check(costAnalysisItemDeepen.getAmount());
		WhNull.checkTrimEmpty(costAnalysisItemDeepen.getUnit());
		if (costAnalysisItemDeepen.getPrice().compareTo(BigDecimal.valueOf(0)) < 0) {
			throw new IllegalArgumentException();
		}
		if (costAnalysisItemDeepen.getAmount().compareTo(BigDecimal.valueOf(0)) < 0) {
			throw new IllegalArgumentException();
		}
		costAnalysisItemDeepen.setType(WhStringUtil.trimAll(costAnalysisItemDeepen.getType()));
		costAnalysisItemDeepen.setName(WhStringUtil.trimAll(costAnalysisItemDeepen.getName()));
		costAnalysisItemDeepen.setUnit(WhStringUtil.trimAll(costAnalysisItemDeepen.getUnit()));
		/*
		 * 校验TYPE合法性
		 */
		if (!CostAnalysisItemDeepen.TYPE_LIST.contains(costAnalysisItemDeepen.getType())) {
			throw new IllegalArgumentException();
		}

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectService.getById(projectEx.getId()));

		/*
		 * 校验成本分析项的合法性
		 */
		CostAnalysisItemDeepen costAnalysisItemDeepenCheck = costAnalysisItemDeepenService
				.getById(costAnalysisItemDeepen.getId());
		WhOwnerShipUtil.checkProject(costAnalysisItemDeepenCheck, projectEx);

		// 整理数据
		costAnalysisItemDeepenCheck.setType(costAnalysisItemDeepen.getType());
		costAnalysisItemDeepenCheck.setName(costAnalysisItemDeepen.getName());
		costAnalysisItemDeepenCheck.setMemo(costAnalysisItemDeepen.getMemo());
		costAnalysisItemDeepenCheck.setPrice(costAnalysisItemDeepen.getPrice());
		costAnalysisItemDeepenCheck.setAmount(costAnalysisItemDeepen.getAmount());
		costAnalysisItemDeepenCheck.setUnit(costAnalysisItemDeepen.getUnit());
		costAnalysisItemDeepenCheck.setCost(costAnalysisItemDeepen.getPrice().multiply(
				costAnalysisItemDeepen.getAmount()));

		costAnalysisItemDeepenService.modifyById(costAnalysisItemDeepenCheck);

		writeStream(getSuccessJsonTemplate());
		return SUCCESS;
	}

	/**
	 * 删除成本分析项
	 * 
	 * @return
	 * @throws Exception
	 */
	public String removeItemById() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		WhNull.check(costAnalysisItemDeepen);
		WhNull.check(costAnalysisItemDeepen.getId());

		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);
		// 归档校验
		WhOwnerShipUtil.checkGoingProject(projectService.getById(projectEx.getId()));

		/*
		 * 校验成本项合法性
		 */
		CostAnalysisItemDeepen costAnalysisItemDeepenCheck = costAnalysisItemDeepenService
				.getById(costAnalysisItemDeepen.getId());
		WhOwnerShipUtil.checkProject(costAnalysisItemDeepenCheck, projectEx);

		/*
		 * 检查是否存在关联成本深化项的资金计划
		 */
		if (!fundPlanItemService.listByCostAnalysisItemId(costAnalysisItemDeepen.getId()).isEmpty()) {
			throw new WhCostAnalysisItemDeepenLinkedException();
		}

		costAnalysisItemDeepenService.removeById(costAnalysisItemDeepen.getId());

		writeStream(getSuccessJsonTemplate());
		return SUCCESS;
	}
}
