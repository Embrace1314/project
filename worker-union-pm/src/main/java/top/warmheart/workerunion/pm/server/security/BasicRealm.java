/**
 * Copyright © WarmHeart Intelligence Science&Technology(NanJing) Company, Limited.
 * All Rights Reserved
 */
package top.warmheart.workerunion.pm.server.security;

import java.math.BigInteger;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import top.warmheart.server.util.WhEncoder;
import top.warmheart.workerunion.pm.server.constant.SessionKey;
import top.warmheart.workerunion.server.model.Permission;
import top.warmheart.workerunion.server.model.Project;
import top.warmheart.workerunion.server.model.ProjectTeam;
import top.warmheart.workerunion.server.model.Staff;
import top.warmheart.workerunion.server.model.TeamRole;
import top.warmheart.workerunion.server.service.PermissionService;
import top.warmheart.workerunion.server.service.ProjectService;
import top.warmheart.workerunion.server.service.ProjectTeamService;
import top.warmheart.workerunion.server.service.StaffService;
import top.warmheart.workerunion.server.service.TeamRoleService;

@Controller("basicRealm")
@Scope("singleton")
public class BasicRealm extends AuthorizingRealm {
	/** 登录项目 */
	public static final String LOGIN_PROJECT = "LOGIN_PROJECT";
	/** 登录用户 */
	public static final String LOGIN_STAFF = "LOGIN_STAFF";
	private StaffService staffService;
	private ProjectService projectService;
	private ProjectTeamService projectTeamService;
	private TeamRoleService teamRoleService;
	private PermissionService permissionService;
	
	public StaffService getStaffService() {
		return staffService;
	}

	@Resource(name = "staffService")
	public void setStaffService(StaffService staffService) {
		this.staffService = staffService;
	}

	public ProjectService getProjectService() {
		return projectService;
	}

	@Resource(name = "projectService")
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	public ProjectTeamService getProjectTeamService() {
		return projectTeamService;
	}

	@Resource(name = "projectTeamService")
	public void setProjectTeamService(ProjectTeamService projectTeamService) {
		this.projectTeamService = projectTeamService;
	}

	public TeamRoleService getTeamRoleService() {
		return teamRoleService;
	}

	@Resource(name = "teamRoleService")
	public void setTeamRoleService(TeamRoleService teamRoleService) {
		this.teamRoleService = teamRoleService;
	}

	
	public PermissionService getPermissionService() {
		return permissionService;
	}

	@Resource(name = "permissionService")
	public void setPermissionService(PermissionService permissionService) {
		this.permissionService = permissionService;
	}

	/**
	 * 认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
			throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
		switch (token.getUsername().split(":")[0]) {
		case LOGIN_STAFF:
			// 用户登录
			verifyStaff(token);
			return new SimpleAuthenticationInfo(LOGIN_STAFF, token.getPassword(), getName());
		case LOGIN_PROJECT:
			// 项目登录
			verifyProject(token);
			return new SimpleAuthenticationInfo(LOGIN_PROJECT, token.getPassword(), getName());
		default:
			return new SimpleAuthenticationInfo("", token.getPassword(), getName());
		}

	}

	/**
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		switch ((String) principalCollection.getPrimaryPrincipal()) {
		case LOGIN_STAFF:
			// 用户登录,不进行权限分配
			return new SimpleAuthorizationInfo();
		case LOGIN_PROJECT:
			// 项目登录，进行权限分配
			return genProjectAuthorizationInfo();
		default:
			return new SimpleAuthorizationInfo();
		}
	}
	
	/**
	 * 生成员工在该项目下的权限信息
	 * @return
	 */
	private AuthorizationInfo genProjectAuthorizationInfo(){
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Project projectEx = (Project) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_PROJECT);
		if (null == staffEx || null == projectEx){
			return new SimpleAuthorizationInfo();
		}
		
		/*
		 * 获取角色名称
		 */
		ProjectTeam projectTeamCheck = projectTeamService.getByProjectStaff(projectEx.getId(), staffEx.getId());
		if (null == projectTeamCheck){
			return new SimpleAuthorizationInfo();
		}
		TeamRole teamRoleCheck = teamRoleService.getById(projectTeamCheck.getTeamRoleId());
		if (null == teamRoleCheck){
			return new SimpleAuthorizationInfo();
		}
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.addRole(teamRoleCheck.getType());
		
		/*
		 * 获取权限
		 */
		List<Permission> permissions = permissionService.listByTeamRole(teamRoleCheck.getId());
		for(Permission permission : permissions){
			info.addStringPermission(permission.getValue());
		}
		return info;
	}
	

	/**
	 * 校验用户登录
	 * 
	 * @param token
	 */
	private void verifyStaff(UsernamePasswordToken token) {
		BigInteger companyId = new BigInteger(token.getUsername().split(":")[1]);
		String jobNo = token.getUsername().split(":")[2];
		String password = new String(token.getPassword());

		// 校验用户
		Staff staffCheck = staffService.getByJobNo(companyId, jobNo);
		if (null == staffCheck || staffCheck.getDel()) {
			throw new UnknownAccountException("账号不存在");
		}
		if (staffCheck.getDel()) {
			throw new DisabledAccountException("账户已注销");
		}

		// 校验密码
		if (!WhEncoder.md5((password + staffCheck.getSalt())).equalsIgnoreCase(staffCheck.getPassword())) {
			throw new IncorrectCredentialsException("密码错误");
		}

		// 保存当前用户信息到Session
		SecurityUtils.getSubject().getSession().setAttribute(SessionKey.KEY_STAFF, staffCheck);
	}

	/**
	 * 校验登录项目
	 * 
	 * @param token
	 */
	private void verifyProject(UsernamePasswordToken token) {
		BigInteger projectId = new BigInteger(token.getUsername().split(":")[1]);

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		if (null == staffEx) {
			throw new ExpiredCredentialsException("无效的登录信息");
		}
		
		// 校验项目信息
		Project projectCheck = projectService.getById(projectId);
		if (null == projectCheck || projectCheck.getDel()
				|| !staffEx.getCompanyId().equals(projectCheck.getCompanyId())) {
			throw new UnsupportedTokenException("无效的项目信息");
		}
		
		// 校验成员的项目组归属
		if (!projectTeamService.contains(projectCheck.getId(), staffEx.getId())) {
			throw new UnsupportedTokenException("非法的项目组成员");
		}

		// 保存当前用户信息到Session
		SecurityUtils.getSubject().getSession().setAttribute(SessionKey.KEY_PROJECT, projectCheck);
	}

}
