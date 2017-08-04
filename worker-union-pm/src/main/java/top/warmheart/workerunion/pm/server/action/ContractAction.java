package top.warmheart.workerunion.pm.server.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;

import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.pm.server.action.ex.ActionJson;
import top.warmheart.workerunion.pm.server.constant.SessionKey;
import top.warmheart.workerunion.pm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.model.Contract;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.service.ContractService;
import top.warmheart.workerunion.server.service.ProjectService;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("serial")
@Scope("prototype")
public class ContractAction extends ActionJson {
	private Project project;
	private ContractService contractService;
	private ProjectService projectService;
	
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public ContractService getContractService() {
		return contractService;
	}

	@Resource(name = "contractService")
	public void setContractService(ContractService contractService) {
		this.contractService = contractService;
	}
	public ProjectService getProjectService() {
		return projectService;
	}

	@Resource(name = "projectService")
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}
	/**
	 * 获取指定项目简要合同列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String listSimpleItemByProjectId() throws Exception {
		// 校验参数
		WhNull.check(project);
		WhNull.check(project.getId());
		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		WhOwnerShipUtil.checkProject(project, projectEx);

		// 获取指定项目简要合同列表
		List<Contract> contracts = contractService.listSimpleItemByProjectId(project.getId());

		JSONObject json = getSuccessJsonTemplate();
		json.put("contracts", contracts);
		writeStream(json);
		return SUCCESS;
	}
}
