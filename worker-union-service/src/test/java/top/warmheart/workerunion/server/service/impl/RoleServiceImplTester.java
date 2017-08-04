package top.warmheart.workerunion.server.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import top.warmheart.workerunion.server.service.RoleService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
@Transactional
public class RoleServiceImplTester {
	private RoleService roleService;
	
	public RoleService getRoleService() {
		return roleService;
	}

	@Autowired
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	@Test
	public void testListByCompanyId() {
		assertEquals(2, roleService.listByCompanyId(BigInteger.valueOf(1L)).size());
	}

	@Test
	public void testListByStaffId() {
		assertEquals(1, roleService.listByStaffId(BigInteger.valueOf(1L),BigInteger.valueOf(1L)).size());
	}

	@Test
	public void testGetById() {
		assertNotNull(roleService.getById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testReplace() {
		roleService.replace(BigInteger.valueOf(1L), roleService.listByCompanyId(BigInteger.valueOf(1L)));
	}

}
