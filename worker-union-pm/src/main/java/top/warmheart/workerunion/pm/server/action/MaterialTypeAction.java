package top.warmheart.workerunion.pm.server.action;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;

import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.pm.server.action.ex.ActionJson;
import top.warmheart.workerunion.pm.server.constant.SessionKey;
import top.warmheart.workerunion.pm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.model.MaterialType;
import top.warmheart.workerunion.server.model.Staff;
import top.warmheart.workerunion.server.service.MaterialTypeService;
import top.warmheart.workerunion.server.service.ProjectService;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("serial")
@Scope("prototype")
public class MaterialTypeAction extends ActionJson {
	private MaterialType materialType;
	private MaterialTypeService materialTypeService;
	private ProjectService projectService;
	public MaterialType getMaterialType() {
		return materialType;
	}

	public void setMaterialType(MaterialType materialType) {
		this.materialType = materialType;
	}

	public MaterialTypeService getMaterialTypeService() {
		return materialTypeService;
	}

	@Resource(name = "materialTypeService")
	public void setMaterialTypeService(MaterialTypeService materialTypeService) {
		this.materialTypeService = materialTypeService;
	}
	public ProjectService getProjectService() {
		return projectService;
	}

	@Resource(name = "projectService")
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}
	/**
	 * 根据企业材料编号获取材料类型信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getImportByNum() throws Exception {
		// 校验参数
		WhNull.check(materialType);
		WhNull.checkTrimEmpty(materialType.getNum());
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		// 获取模糊查询分页结果
		MaterialType materialTypeCheck = materialTypeService.getByNum(staffEx.getCompanyId(), materialType.getNum());
		WhOwnerShipUtil.checkMaterialType(materialTypeCheck);

		JSONObject json = getSuccessJsonTemplate();
		json.put("materialType", materialTypeCheck);
		writeStream(json);
		return SUCCESS;
	}

}
