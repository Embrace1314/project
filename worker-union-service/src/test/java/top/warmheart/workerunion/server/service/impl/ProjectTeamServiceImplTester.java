package top.warmheart.workerunion.server.service.impl;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import top.warmheart.workerunion.server.model.TeamRole;
import top.warmheart.workerunion.server.service.ProjectTeamService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
@Transactional
public class ProjectTeamServiceImplTester {
	private ProjectTeamService projectTeamService;

	public ProjectTeamService getProjectTeamService() {
		return projectTeamService;
	}

	@Autowired
	public void setProjectTeamService(ProjectTeamService projectTeamService) {
		this.projectTeamService = projectTeamService;
	}

	@Test
	public void testContains() {
		assertTrue(projectTeamService.contains(BigInteger.valueOf(1L), BigInteger.valueOf(1L)));
		assertFalse(projectTeamService.contains(BigInteger.valueOf(1L), BigInteger.valueOf(2L)));
	}

	@Test
	public void testAdd() {
		assertEquals(1, projectTeamService.add(BigInteger.valueOf(2L), BigInteger.valueOf(2L), BigInteger.valueOf(2L)));
	}

	@Test
	public void testRoleContains() {
		assertTrue(projectTeamService.roleContains(BigInteger.valueOf(1L), BigInteger.valueOf(1L)));
	}

	@Test
	public void testGetByProjectStaff() {
		assertNotNull(projectTeamService.getByProjectStaff(BigInteger.valueOf(1L), BigInteger.valueOf(1L)));
	}

	@Test
	public void testDeleteByProjectStaff() {
		assertEquals(1, projectTeamService.deleteByProjectStaff(BigInteger.valueOf(1L), BigInteger.valueOf(1L)));
	}

	@Test
	public void testListStaffByProjectId() {
		assertEquals(1, projectTeamService.listStaffByProjectId(BigInteger.valueOf(1L)).size());
	}

	@Test
	public void testGetByProjectTeamType() {
		assertNotNull(projectTeamService.getByProjectTeamType(BigInteger.valueOf(1L), TeamRole.TYPE_PROJECT_MANAGER));
	}
}
