package top.warmheart.workerunion.server.service.impl;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import top.warmheart.workerunion.server.model.ProjectApplication;
import top.warmheart.workerunion.server.service.ProjectApplicationService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
@Transactional
public class ProjectApplicationServiceImplTester {
	private ProjectApplicationService projectApplicationService;

	public ProjectApplicationService getProjectApplicationService() {
		return projectApplicationService;
	}

	@Autowired
	public void setProjectApplicationService(ProjectApplicationService projectApplicationService) {
		this.projectApplicationService = projectApplicationService;
	}

	@Test
	public void testListStaffByProjectId() {
		assertEquals(1, projectApplicationService.listStaffByProjectId(BigInteger.valueOf(1L)).size());
	}

	@Test
	public void testGetByProjectStaff() {
		assertNotNull(projectApplicationService.getByProjectStaff(BigInteger.valueOf(1L), BigInteger.valueOf(1L)));
	}

	@Test
	public void testDeleteByProjectStaff() {
		assertEquals(1, projectApplicationService.deleteByProjectStaff(BigInteger.valueOf(1L),BigInteger.valueOf(1L)));
	}

	@Test
	public void testGetValidByStaff() {
		assertEquals(1, projectApplicationService.getValidByStaff(BigInteger.valueOf(1L)).size());
	}

	@Test
	public void testGetByProjectRole() {
		assertNotNull(projectApplicationService.getByProjectRole(BigInteger.valueOf(1L), ProjectApplication.TYPE_PROJECT_MANAGER));
		assertNull(projectApplicationService.getByProjectRole(BigInteger.valueOf(1L), ProjectApplication.TYPE_CONSTRUCTION_WORKER));
	}

	@Test
	public void testAdd() {
		assertEquals(1, projectApplicationService.add(BigInteger.valueOf(1L), BigInteger.valueOf(2L), ProjectApplication.TYPE_CONSTRUCTION_WORKER));
		assertEquals(0, projectApplicationService.add(BigInteger.valueOf(1L), BigInteger.valueOf(1L), ProjectApplication.TYPE_CONSTRUCTION_WORKER));
	}
}
