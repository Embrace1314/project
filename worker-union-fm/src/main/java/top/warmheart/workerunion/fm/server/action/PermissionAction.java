/**
 * Copyright © WarmHeart Intelligence Science&Technology(NanJing) Company, Limited.
 * All Rights Reserved
 */
package top.warmheart.workerunion.fm.server.action;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;

import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.fm.server.action.ex.ActionJson;
import top.warmheart.workerunion.fm.server.constant.SessionKey;
import top.warmheart.workerunion.fm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.model.Role;
import top.warmheart.workerunion.server.model.Staff;
import top.warmheart.workerunion.server.service.RoleService;
import top.warmheart.workerunion.server.service.StaffService;

import com.alibaba.fastjson.JSONObject;

/**
 * 企业员工
 * 
 * @author seulad
 *
 */
@SuppressWarnings("serial")
@Scope("prototype")
public class PermissionAction extends ActionJson {
	private Staff staff;
	private List<Role> roles;
	private StaffService staffService;
	private RoleService roleService;

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public StaffService getStaffService() {
		return staffService;
	}

	@Resource(name = "staffService")
	public void setStaffService(StaffService staffService) {
		this.staffService = staffService;
	}

	public RoleService getRoleService() {
		return roleService;
	}

	@Resource(name = "roleService")
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	/**
	 * 获取企业角色列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String listRoleByCompany() throws Exception {
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		List<Role> roles = roleService.listByCompanyId(staffEx.getCompanyId());

		JSONObject json = getSuccessJsonTemplate();
		json.put("roles", roles);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取用户拥有的角色
	 * 
	 * @return
	 * @throws Exception
	 */
	public String listRoleByStaff() throws Exception {
		WhNull.check(staff);
		WhNull.check(staff.getId());
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		Staff staffCheck = staffService.getById(staff.getId());
		WhOwnerShipUtil.checkStaffCompany(staffCheck, staffEx);

		List<Role> roles = roleService.listByStaffId(staffCheck.getId(), staffEx.getCompanyId());

		JSONObject json = getSuccessJsonTemplate();
		json.put("roles", roles);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 配置用户的角色
	 * 
	 * @return
	 * @throws Exception
	 */
	public String saveStaffRole() throws Exception {
		WhNull.check(staff);
		WhNull.check(staff.getId());
		if (null == roles){
			roles = new ArrayList<Role>();
		}
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		Staff staffCheck = staffService.getById(staff.getId());
		WhOwnerShipUtil.checkStaffCompany(staffCheck, staffEx);

		// 校验角色信息
		for (Role role : roles) {
			Role roleCheck = roleService.getById(role.getId());
			WhOwnerShipUtil.checkStaffCompany(roleCheck, staffEx);
		}

		roleService.replace(staff.getId(), roles);

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}

}
