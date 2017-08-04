package top.warmheart.workerunion.pm.server.action;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;

import top.warmheart.server.util.dto.Page;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.pm.server.action.ex.ActionJson;
import top.warmheart.workerunion.pm.server.constant.SessionKey;
import top.warmheart.workerunion.pm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.dto.FinalSettlementItemDto;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.service.FinalSettlementItemService;
import top.warmheart.workerunion.server.service.ProjectService;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("serial")
@Scope("prototype")
public class FinalSettlementAction extends ActionJson {
	private Project project;
	private Page<Void> page;
	private FinalSettlementItemService finalSettlementItemService;
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

	public FinalSettlementItemService getFinalSettlementItemService() {
		return finalSettlementItemService;
	}

	@Resource(name = "finalSettlementItemService")
	public void setFinalSettlementItemService(FinalSettlementItemService finalSettlementItemService) {
		this.finalSettlementItemService = finalSettlementItemService;
	}
	public ProjectService getProjectService() {
		return projectService;
	}

	@Resource(name = "projectService")
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}
	/**
	 * 获取竣工决算条目列表
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

		// 合计送审金额（单位：元）
		BigDecimal sumSubmitMoney = new BigDecimal(0);
		// 合计核定金额（单位：元）
		BigDecimal sumCheckedMoney = new BigDecimal(0);
		// 合计核减金额（单位：元）
		BigDecimal sumIncreasedMoney = new BigDecimal(0);
		// 合计核增金额（单位：元）
		BigDecimal sumDecreasedMoney = new BigDecimal(0);
		// 获取列表
		List<FinalSettlementItemDto> list = finalSettlementItemService.listItemByProjectId(project.getId());
		for (FinalSettlementItemDto finalSettlementItemDto : list) {
			sumSubmitMoney = sumSubmitMoney.add(finalSettlementItemDto.getSubmitMoney());
			sumCheckedMoney = sumCheckedMoney.add(finalSettlementItemDto.getCheckedMoney());
			sumIncreasedMoney = sumIncreasedMoney.add(finalSettlementItemDto.getIncreasedMoney());
			sumDecreasedMoney = sumDecreasedMoney.add(finalSettlementItemDto.getDecreasedMoney());
		}
		
		JSONObject json = getSuccessJsonTemplate();
		json.put("finalSettlementItems", list);
		json.put("sumSubmitMoney", sumSubmitMoney);
		json.put("sumCheckedMoney", sumCheckedMoney);
		json.put("sumIncreasedMoney", sumIncreasedMoney);
		json.put("sumDecreasedMoney", sumDecreasedMoney);
		writeStream(json);
		return SUCCESS;
	}
}
