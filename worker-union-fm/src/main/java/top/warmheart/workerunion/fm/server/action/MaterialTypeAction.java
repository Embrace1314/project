package top.warmheart.workerunion.fm.server.action;

import java.math.BigInteger;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;

import top.warmheart.server.util.WhStringUtil;
import top.warmheart.server.util.dto.Page;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.fm.server.action.ex.ActionJson;
import top.warmheart.workerunion.fm.server.constant.SessionKey;
import top.warmheart.workerunion.fm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.exception.WhMaterialTypeExistException;
import top.warmheart.workerunion.server.model.MaterialType;
import top.warmheart.workerunion.server.model.MaterialTypeAttachment;
import top.warmheart.workerunion.server.model.Staff;
import top.warmheart.workerunion.server.service.MaterialTypeService;
import top.warmheart.workerunion.server.service.ProjectService;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("serial")
@Scope("prototype")
public class MaterialTypeAction extends ActionJson {
	private MaterialType materialType;
	private Page<Void> page;
	private MaterialTypeAttachment materialTypeAttachment;
	private MaterialTypeService materialTypeService;
	private ProjectService projectService;

	public Page<Void> getPage() {
		return page;
	}

	public void setPage(Page<Void> page) {
		this.page = page;
	}

	public MaterialType getMaterialType() {
		return materialType;
	}

	public void setMaterialType(MaterialType materialType) {
		this.materialType = materialType;
	}

	public MaterialTypeAttachment getMaterialTypeAttachment() {
		return materialTypeAttachment;
	}

	public void setMaterialTypeAttachment(MaterialTypeAttachment materialTypeAttachment) {
		this.materialTypeAttachment = materialTypeAttachment;
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

		// 获取企业材料类型
		MaterialType materialTypeCheck = materialTypeService.getByNum(staffEx.getCompanyId(), materialType.getNum());
		WhOwnerShipUtil.checkMaterialType(materialTypeCheck);

		JSONObject json = getSuccessJsonTemplate();
		json.put("materialType", materialTypeCheck);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取企业材料类型详情
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getDetailById() throws Exception {
		// 校验参数
		WhNull.check(materialType);
		WhNull.check(materialType.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		// 获取信息详情
		MaterialType materialTypeCheck = materialTypeService.getById(materialType.getId());
		WhOwnerShipUtil.checkStaffCompany(materialTypeCheck, staffEx);

		List<MaterialTypeAttachment> materialTypeAttachments = materialTypeService.listByTypeId(materialType.getId());

		JSONObject json = getSuccessJsonTemplate();
		json.put("materialType", materialTypeCheck);
		json.put("materialTypeAttachments", materialTypeAttachments);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 添加企业材料类型
	 * 
	 * @return
	 * @throws Exception
	 */
	public String add() throws Exception {
		// 校验参数
		WhNull.check(materialType);
		WhNull.checkTrimEmpty(materialType.getNum());
		WhNull.checkTrimEmpty(materialType.getName());
		WhNull.checkTrimEmpty(materialType.getModel());
		WhNull.checkTrimEmpty(materialType.getUnit());
		materialType.setNum(WhStringUtil.trimAll(materialType.getNum()));
		materialType.setName(WhStringUtil.trimAll(materialType.getName()));
		materialType.setModel(WhStringUtil.trimAll(materialType.getModel()));
		materialType.setUnit(WhStringUtil.trimAll(materialType.getUnit()));

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		// 校验编号
		MaterialType materialTypeExist = materialTypeService.getByNum(staffEx.getCompanyId(), materialType.getNum());
		if (null != materialTypeExist) {
			throw new WhMaterialTypeExistException();
		}

		materialType.setCompanyId(staffEx.getCompanyId());
		BigInteger id = materialTypeService.add(materialType);

		JSONObject json = getSuccessJsonTemplate();
		json.put("id", id);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 删除企业材料类型附件
	 * 
	 * @return
	 * @throws Exception
	 */
	public String removeAttachmentById() throws Exception {
		// 校验参数
		WhNull.check(materialTypeAttachment);
		WhNull.check(materialTypeAttachment.getId());
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		// 获取材料类型附件信息
		MaterialTypeAttachment materialTypeAttachmentCheck = materialTypeService
				.getAttachmentById(materialTypeAttachment.getId());
		WhOwnerShipUtil.checkStaffCompany(materialTypeAttachmentCheck, staffEx);

		materialTypeService.removeAttachmentById(materialTypeAttachment.getId());

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 删除企业材料类型
	 * 
	 * @return
	 * @throws Exception
	 */
	public String removeById() throws Exception {
		// 校验参数
		WhNull.check(materialType);
		WhNull.check(materialType.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		// 获取信息详情
		MaterialType materialTypeCheck = materialTypeService.getById(materialType.getId());
		WhOwnerShipUtil.checkStaffCompany(materialTypeCheck, staffEx);

		materialTypeService.removeById(materialType.getId());
		
		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}
	
	/**
	 * 模糊查询获取企业材料类型信息列表，分页
	 * 
	 * @return
	 * @throws Exception
	 */
	public String pageByFuzzy() throws Exception {
		WhNull.check(materialType);
		WhNull.check(materialType.getName());
		WhNull.check(materialType.getNum());
		WhNull.checkPage(page);
		
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);
		materialType.setCompanyId(staffEx.getCompanyId());
		
		Page<MaterialType>pageCheck = materialTypeService.pageByFuzzy(materialType, page);
		
		JSONObject json = getSuccessJsonTemplate();
		json.put("page", pageCheck);
		writeStream(json);
		return SUCCESS;
	}
}
