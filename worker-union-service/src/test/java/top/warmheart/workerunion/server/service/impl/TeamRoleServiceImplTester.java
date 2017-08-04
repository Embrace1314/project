package top.warmheart.workerunion.server.service.impl;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import top.warmheart.workerunion.server.service.TeamRoleService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
@Transactional
public class TeamRoleServiceImplTester {
	private TeamRoleService teamRoleService;

	public TeamRoleService getTeamRoleService() {
		return teamRoleService;
	}

	@Autowired
	public void setTeamRoleService(TeamRoleService teamRoleService) {
		this.teamRoleService = teamRoleService;
	}

	@Test
	public void testGetById() {
		assertNotNull(teamRoleService.getById(BigInteger.valueOf(1L)));
	}

}
