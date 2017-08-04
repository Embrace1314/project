package top.warmheart.workerunion.fm.server.action;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;

import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.fm.server.action.ex.ActionJson;
import top.warmheart.workerunion.fm.server.constant.SessionKey;
import top.warmheart.workerunion.fm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.model.Attachment;
import top.warmheart.workerunion.server.model.CostAnalysisItemDeepen;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.model.Staff;
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

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

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

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

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

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

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

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = projectService.getById(project.getId());
		WhOwnerShipUtil.checkStaffCompany(projectEx, staffEx);

		// 获取成本深化项列表
		List<CostAnalysisItemDeepen> costAnalysisItemDeepens = costAnalysisItemDeepenService.listByProjectId(projectEx
				.getId());

		JSONObject json = getSuccessJsonTemplate();
		json.put("costAnalysisItemDeepens", costAnalysisItemDeepens);
		writeStream(json);
		return SUCCESS;
	}

}
