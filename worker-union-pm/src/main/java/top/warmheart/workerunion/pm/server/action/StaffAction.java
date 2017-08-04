/**
 * Copyright © WarmHeart Intelligence Science&Technology(NanJing) Company, Limited.
 * All Rights Reserved
 */
package top.warmheart.workerunion.pm.server.action;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.context.annotation.Scope;

import com.alibaba.fastjson.JSONObject;

import top.warmheart.server.util.WhEncoder;
import top.warmheart.server.util.WhStringUtil;
import top.warmheart.server.util.dto.Page;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.pm.server.action.ex.ActionJson;
import top.warmheart.workerunion.pm.server.constant.SessionKey;
import top.warmheart.workerunion.pm.server.security.BasicRealm;
import top.warmheart.workerunion.pm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.dto.StaffDto;
import top.warmheart.workerunion.server.exception.WhIncorrectPasswordConfirmException;
import top.warmheart.workerunion.server.model.Staff;
import top.warmheart.workerunion.server.service.StaffService;

/**
 * 企业员工
 * 
 * @author seulad
 *
 */
@SuppressWarnings("serial")
@Scope("prototype")
public class StaffAction extends ActionJson {
	/** 用户信息 */
	private Staff staff;
	private String passwordNew;
	private String passwordConfirm;
	private StaffService staffService;
	private StaffDto staffDto;
	private Page<Void> page;

	public String getPasswordNew() {
		return passwordNew;
	}

	public void setPasswordNew(String passwordNew) {
		this.passwordNew = passwordNew;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public Page<Void> getPage() {
		return page;
	}

	public void setPage(Page<Void> page) {
		this.page = page;
	}

	public StaffDto getStaffDto() {
		return staffDto;
	}

	public void setStaffDto(StaffDto staffDto) {
		this.staffDto = staffDto;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public StaffService getStaffService() {
		return staffService;
	}

	@Resource(name = "staffService")
	public void setStaffService(StaffService staffService) {
		this.staffService = staffService;
	}

	/**
	 * 登录
	 * 
	 * @return
	 * @throws Exception
	 */
	public String login() throws Exception {
		WhNull.check(staff);
		WhNull.check(staff.getCompanyId());
		WhNull.checkTrimEmpty(staff.getJobNo());
		WhNull.checkTrimEmpty(staff.getPassword());

		Subject currentStaff = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(BasicRealm.LOGIN_STAFF + ":" + staff.getCompanyId()
				+ ":" + staff.getJobNo(), staff.getPassword());

		// 校验用户
		currentStaff.login(token);

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		// 校验成功
		JSONObject json = getSuccessJsonTemplate();
		json.put("id", staffEx.getId());
		json.put("companyId", staffEx.getCompanyId());
		json.put("sex", staffEx.getSex());
		json.put("name", staffEx.getName());
		json.put("jobNo", staffEx.getJobNo());
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 退出登录
	 * 
	 * @return
	 * @throws Exception
	 */
	public String logout() throws Exception {
		Subject subject = SecurityUtils.getSubject();
		// 清空Session
		subject.getSession().removeAttribute(SessionKey.KEY_STAFF);
		subject.getSession().removeAttribute(SessionKey.KEY_PROJECT);
		// 退出登录
		subject.logout();

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 修改密码
	 * 
	 * @return
	 * @throws Exception
	 */
	public String changePassword() throws Exception {
		WhNull.check(staff);
		WhNull.checkTrimEmpty(staff.getPassword());
		WhNull.checkTrimEmpty(passwordConfirm);
		WhNull.checkTrimEmpty(passwordNew);

		if (!passwordConfirm.equals(passwordNew)) {
			throw new WhIncorrectPasswordConfirmException();
		}

		/*
		 * 校验密码
		 */
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);
		Staff staffCheck = staffService.getById(staffEx.getId());
		if (null == staffCheck) {
			throw new UnknownAccountException("账号不存在");
		}
		if (!WhEncoder.md5((staff.getPassword() + staffEx.getSalt())).equalsIgnoreCase(staffEx.getPassword())) {
			throw new IncorrectCredentialsException("密码错误");
		}
		// 设置盐
		staffCheck.setSalt(WhStringUtil.uuid());
		// 设置密码
		staffCheck.setPassword(WhEncoder.md5(passwordNew + staffCheck.getSalt()));
		staffService.modifyById(staffCheck);
		writeStream(getSuccessJsonTemplate());
		return SUCCESS;
	}

	/**
	 * 获取已登录用户信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getLoginedInfo() throws Exception {
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		JSONObject json = getSuccessJsonTemplate();
		json.put("id", staffEx.getId());
		json.put("companyId", staffEx.getCompanyId());
		json.put("sex", staffEx.getSex());
		json.put("name", staffEx.getName());
		json.put("jobNo", staffEx.getJobNo());
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 根据模糊条件查询员工信息，分页
	 * 
	 * @return
	 * @throws Exception
	 */
	public String pageByFuzzy() throws Exception {
		WhNull.check(staffDto);
		WhNull.check(staffDto.getName());
		WhNull.check(staffDto.getJobNo());
		WhNull.check(staffDto.getIdCardNo());
		WhNull.check(staffDto.getRoleName());
		WhNull.check(staffDto.getProjectName());

		WhNull.checkPage(page);

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		// 整理参数
		staffDto.setCompanyId(staffEx.getCompanyId());
		staffDto.setName(WhStringUtil.trimAll(staffDto.getName()));
		staffDto.setJobNo(WhStringUtil.trimAll(staffDto.getJobNo()));
		staffDto.setIdCardNo(WhStringUtil.trimAll(staffDto.getIdCardNo()));
		staffDto.setRoleName(WhStringUtil.trimAll(staffDto.getRoleName()));
		staffDto.setProjectName(WhStringUtil.trimAll(staffDto.getProjectName()));

		JSONObject json = getSuccessJsonTemplate();
		json.put("page", staffService.pageByFuzzy(staffDto, page));
		writeStream(json);
		return SUCCESS;
	}
}
