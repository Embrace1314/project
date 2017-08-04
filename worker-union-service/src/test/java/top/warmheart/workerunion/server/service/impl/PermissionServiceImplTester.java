package top.warmheart.workerunion.server.service.impl;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import top.warmheart.workerunion.server.service.PermissionService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
@Transactional
public class PermissionServiceImplTester {
	private PermissionService permissionService;

	public PermissionService getPermissionService() {
		return permissionService;
	}

	@Autowired
	public void setPermissionService(PermissionService permissionService) {
		this.permissionService = permissionService;
	}

	@Test
	public void testListByTeamRole() {
		assertEquals(1, permissionService.listByTeamRole(BigInteger.valueOf(1L)).size());
	}

}
