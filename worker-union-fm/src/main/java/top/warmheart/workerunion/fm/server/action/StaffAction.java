/**
 * Copyright © WarmHeart Intelligence Science&Technology(NanJing) Company, Limited.
 * All Rights Reserved
 */
package top.warmheart.workerunion.fm.server.action;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.context.annotation.Scope;

import top.warmheart.server.util.WhEncoder;
import top.warmheart.server.util.WhIdCardUtil;
import top.warmheart.server.util.WhStringUtil;
import top.warmheart.server.util.dto.Page;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.server.util.exception.WhUtilException;
import top.warmheart.workerunion.fm.server.action.ex.ActionJson;
import top.warmheart.workerunion.fm.server.constant.SessionKey;
import top.warmheart.workerunion.fm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.dto.StaffDto;
import top.warmheart.workerunion.server.exception.WhIncorrectPasswordConfirmException;
import top.warmheart.workerunion.server.exception.WhInvalidIDCardException;
import top.warmheart.workerunion.server.exception.WhStaffCannotDeleteException;
import top.warmheart.workerunion.server.exception.WhStaffJobNoExistException;
import top.warmheart.workerunion.server.model.Certificate;
import top.warmheart.workerunion.server.model.Staff;
import top.warmheart.workerunion.server.service.CertificateService;
import top.warmheart.workerunion.server.service.RegionalCodeService;
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
public class StaffAction extends ActionJson {
	/** 企业用户信息 */
	private Staff staff;
	private String passwordNew;
	private String passwordConfirm;
	private StaffDto staffDto;
	private Page<Void> page;
	private StaffService staffService;
	private RegionalCodeService regionalCodeService;
	private CertificateService certificateService;
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

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public StaffDto getStaffDto() {
		return staffDto;
	}

	public void setStaffDto(StaffDto staffDto) {
		this.staffDto = staffDto;
	}

	public Page<Void> getPage() {
		return page;
	}

	public void setPage(Page<Void> page) {
		this.page = page;
	}

	public StaffService getStaffService() {
		return staffService;
	}

	@Resource(name = "staffService")
	public void setStaffService(StaffService staffService) {
		this.staffService = staffService;
	}

	public RegionalCodeService getRegionalCodeService() {
		return regionalCodeService;
	}

	@Resource(name = "regionalCodeService")
	public void setRegionalCodeService(RegionalCodeService regionalCodeService) {
		this.regionalCodeService = regionalCodeService;
	}

	public CertificateService getCertificateService() {
		return certificateService;
	}

	@Resource(name = "certificateService")
	public void setCertificateService(CertificateService certificateService) {
		this.certificateService = certificateService;
	}

	/**
	 * 企业用户新增
	 * 
	 * @return
	 * @throws Exception
	 */
	public String add() throws Exception {
		WhNull.check(staff);
		WhNull.checkTrimEmpty(staff.getJobNo());
		WhNull.checkTrimEmpty(staff.getName());
		WhNull.check(staff.getPhone());
		WhNull.checkTrimEmpty(staff.getIdCardNo());
		WhNull.check(staff.getAddress());
		WhNull.check(staff.getEntryDate());
		WhNull.check(staff.getCareer());
		WhNull.check(staff.getMemo());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		// 整理姓名、工号、手机号码、身份证编号数据、企业ID
		staff.setName(WhStringUtil.trimAll(staff.getName()));
		staff.setJobNo(WhStringUtil.trimAll(staff.getJobNo()));
		staff.setPhone(WhStringUtil.trimAll(staff.getPhone()));
		staff.setIdCardNo(WhStringUtil.trimAll(staff.getIdCardNo()));
		staff.setCompanyId(staffEx.getCompanyId());

		// 校验身份证信息合法性
		if (!WhIdCardUtil.isIdCard(staff.getIdCardNo())) {
			throw new WhInvalidIDCardException();
		}

		// 判断用户是否存在
		if (null != staffService.getByJobNo(staff.getCompanyId(), staff.getJobNo())) {
			throw new WhStaffJobNoExistException();
		}

		// 设置出生日期
		staff.setBirthday(WhIdCardUtil.getBirthday(staff.getIdCardNo()));
		// 设置性别
		staff.setSex(WhIdCardUtil.isMale(staff.getIdCardNo()) ? Staff.SEX_MALE : Staff.SEX_FEMALE);
		// 设置籍贯
		staff.setNativePlace(regionalCodeService.getNameByCode(WhIdCardUtil.getRegionalCode(staff.getIdCardNo())));
		// 设置盐
		staff.setSalt(WhStringUtil.uuid());
		// 设置初始密码：身份证后六位
		staff.setPassword(WhEncoder.md5(staff.getIdCardNo().substring(staff.getIdCardNo().length() - 6)
				+ staff.getSalt()));

		// 新增用户信息
		staffService.add(staff);

		writeStream(getSuccessJsonTemplate());
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
	 * 员工信息修改
	 * 
	 * @return
	 * @throws Exception
	 */
	public String modifyById() throws Exception {
		WhNull.check(staff);
		WhNull.check(staff.getId());
		WhNull.checkTrimEmpty(staff.getName());
		WhNull.checkTrimEmpty(staff.getIdCardNo());
		WhNull.check(staff.getPhone());
		WhNull.check(staff.getAddress());
		WhNull.check(staff.getEntryDate());
		WhNull.check(staff.getCareer());
		WhNull.check(staff.getMemo());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		/*
		 * 校验员工信息
		 */
		Staff staffCheck = staffService.getById(staff.getId());
		WhOwnerShipUtil.checkStaffCompany(staffCheck, staffEx);

		// 整理姓名、手机号码、身份证编号数据
		staffCheck.setName(WhStringUtil.trimAll(staff.getName()));
		staffCheck.setPhone(WhStringUtil.trimAll(staff.getPhone()));
		staffCheck.setAddress(staff.getAddress());
		staffCheck.setEntryDate(staff.getEntryDate());
		staffCheck.setCareer(staff.getCareer());
		staffCheck.setMemo(staff.getMemo());

		// 身份证信息有改变
		if (!staffCheck.getIdCardNo().equalsIgnoreCase(WhStringUtil.trimAll(staff.getIdCardNo()))) {
			staffCheck.setIdCardNo(WhStringUtil.trimAll(staff.getIdCardNo()));

			// 校验身份证信息合法性
			if (!WhIdCardUtil.isIdCard(staffCheck.getIdCardNo())) {
				throw new WhInvalidIDCardException();
			}

			// 设置出生日期
			staffCheck.setBirthday(WhIdCardUtil.getBirthday(staffCheck.getIdCardNo()));
			// 设置性别
			staffCheck.setSex(WhIdCardUtil.isMale(staffCheck.getIdCardNo()) ? Staff.SEX_MALE : Staff.SEX_FEMALE);
			// 设置籍贯
			staffCheck.setNativePlace(regionalCodeService.getNameByCode(WhIdCardUtil.getRegionalCode(staffCheck
					.getIdCardNo())));
		}
		staffService.modifyById(staffCheck);

		writeStream(getSuccessJsonTemplate());
		return SUCCESS;
	}
	
	/**
	 * 退出登录
	 * 
	 * @return
	 * @throws Exception
	 */
	public String logout()throws Exception{
		Subject subject = SecurityUtils.getSubject();
		// 清空Session
		subject.getSession().removeAttribute(SessionKey.KEY_STAFF);
		// 退出登录
		subject.logout();
		
		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
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
		UsernamePasswordToken token = new UsernamePasswordToken(staff.getCompanyId() + ":" + staff.getJobNo(),
				staff.getPassword());

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
	 * 检查员工授权信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String checkAuthorization() throws Exception {
		Subject subject = SecurityUtils.getSubject();

		JSONObject json = getSuccessJsonTemplate();
		json.put("project", subject.isPermitted(SessionKey.PERMISSION_PROJECT_ENTRY));
		json.put("personnel", subject.isPermitted(SessionKey.PERMISSION_STAFF_ENTRY));
		json.put("certificate", subject.isPermitted(SessionKey.PERMISSION_CERTIFICATE_ENTRY));
		json.put("contract", subject.isPermitted(SessionKey.PERMISSION_CONTRACT_ENTRY));
		json.put("supplier", subject.isPermitted(SessionKey.PERMISSION_SUPPLIER_ENTRY));
		json.put("subcontractor", subject.isPermitted(SessionKey.PERMISSION_SUBCONTRACTOR_ENTRY));
		json.put("material", subject.isPermitted(SessionKey.PERMISSION_MATERIAL_TYPE_ENTRY));
		json.put("storage", subject.isPermitted(SessionKey.PERMISSION_MATERIAL_HEADQUARTERS_ENTRY));
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

	/**
	 * 删除员工信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String removeById() throws Exception {
		WhNull.check(staff);
		WhNull.check(staff.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		if (staff.getId().equals(staffEx.getId())) {
			throw new WhStaffCannotDeleteException();
		}

		/*
		 * 校验员工信息
		 */
		StaffDto staffDtoCheck = staffService.getDetailById(staff.getId());
		WhOwnerShipUtil.checkStaffCompany(staffDtoCheck, staffEx);

		// 从数据库删除员工信息
		staffService.removeById(staff.getId());

		writeStream(getSuccessJsonTemplate());
		return SUCCESS;
	}

	/**
	 * 获取员工信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getDetailById() throws Exception {
		WhNull.check(staff);
		WhNull.check(staff.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		/*
		 * 获取员工信息，并校验
		 */
		StaffDto staffDto = staffService.getDetailById(staff.getId());
		WhOwnerShipUtil.checkStaffCompany(staffDto, staffEx);
		staffDto.setAge(getAge(staffDto.getBirthday()));
		
		/*
		 * 获取证件信息
		 */
		List<Certificate> certificates = certificateService.listByStaffId(staff.getId());
		
		JSONObject json = getSuccessJsonTemplate();
		json.put("staffDto", staffDto);
		json.put("certificates", certificates);
		writeStream(json);
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
	 * 由出生日期计算年龄
	 * 
	 * @param birthday
	 * @return
	 * @throws Exception
	 */
	private int getAge(Date birthday) {
		WhNull.check(birthday);
		Calendar cal = Calendar.getInstance();

		if (cal.before(birthday)) {
			throw new WhUtilException("非法的出生日期");
		}
		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH);
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(birthday);

		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				if (dayOfMonthNow < dayOfMonthBirth)
					age--;
			} else {
				age--;
			}
		}
		return age;
	}
}
