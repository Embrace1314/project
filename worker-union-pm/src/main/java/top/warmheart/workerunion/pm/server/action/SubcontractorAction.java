package top.warmheart.workerunion.pm.server.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;

import top.warmheart.workerunion.pm.server.action.ex.ActionJson;
import top.warmheart.workerunion.pm.server.constant.SessionKey;
import top.warmheart.workerunion.pm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.model.Staff;
import top.warmheart.workerunion.server.model.Subcontractor;
import top.warmheart.workerunion.server.service.ProjectService;
import top.warmheart.workerunion.server.service.SubcontractorService;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("serial")
@Scope("prototype")
public class SubcontractorAction extends ActionJson {
	private Project project;
	private SubcontractorService subcontractorService;
	private ProjectService projectService;
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public SubcontractorService getSubcontractorService() {
		return subcontractorService;
	}

	@Resource(name = "subcontractorService")
	public void setSubcontractorService(SubcontractorService subcontractorService) {
		this.subcontractorService = subcontractorService;
	}
	public ProjectService getProjectService() {
		return projectService;
	}

	@Resource(name = "projectService")
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}
	/**
	 * 获取简要分包商列表
	 * @return
	 * @throws Exception
	 */
	public String listSimpleItem() throws Exception {
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);
		List<Subcontractor> subcontractors = subcontractorService.listSimpleItemByCompanyId(staffEx.getCompanyId());
		
		JSONObject json = getSuccessJsonTemplate();
		json.put("subcontractors", subcontractors);
		writeStream(json);
		return SUCCESS;
	}
}
