/**
 * Copyright © WarmHeart Intelligence Science&Technology(NanJing) Company, Limited.
 * All Rights Reserved
 */
package top.warmheart.workerunion.fm.server.security;

import java.math.BigInteger;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import top.warmheart.server.util.WhEncoder;
import top.warmheart.workerunion.fm.server.constant.SessionKey;
import top.warmheart.workerunion.server.model.Permission;
import top.warmheart.workerunion.server.model.Role;
import top.warmheart.workerunion.server.model.Staff;
import top.warmheart.workerunion.server.service.StaffService;

@Controller("basicRealm")
@Scope("singleton")
public class BasicRealm extends AuthorizingRealm {
	private StaffService staffService;

	public StaffService getStaffService() {
		return staffService;
	}

	@Resource(name = "staffService")
	public void setStaffService(StaffService staffService) {
		this.staffService = staffService;
	}

	/**
	 * 认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
			throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

		BigInteger companyId = new BigInteger(token.getUsername().split(":")[0]);
		String jobNo = token.getUsername().split(":")[1];
		String password = new String(token.getPassword());

		// 校验用户密码
		Staff staffEx = staffService.getByJobNo(companyId, jobNo);
		if (null == staffEx || staffEx.getDel()) {
			throw new UnknownAccountException("账号不存在");
		}
		if (!WhEncoder.md5((password + staffEx.getSalt())).equalsIgnoreCase(staffEx.getPassword())) {
			throw new IncorrectCredentialsException("密码错误");
		}

		// 保存当前用户信息到Session
		Subject subject = SecurityUtils.getSubject();
		subject.getSession().setAttribute(SessionKey.KEY_STAFF, staffEx);
		
		return new SimpleAuthenticationInfo(staffEx, password, getName());
	}

	/**
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		Staff staff = (Staff) principalCollection.getPrimaryPrincipal();

		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		// 获取员工的权限记录
		Staff staffEx = staffService.getAuthenticationStaff(staff.getId());
		if (null != staffEx) {
			for (Role role : staffEx.getRoles()) {
				info.addRole(role.getName());
				for (Permission permission : role.getPermissions()) {
					info.addStringPermission(permission.getValue());
				}
			}
		}
		return info;
	}

}
