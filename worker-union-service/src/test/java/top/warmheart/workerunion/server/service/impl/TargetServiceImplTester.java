package top.warmheart.workerunion.server.service.impl;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import top.warmheart.workerunion.server.model.Target;
import top.warmheart.workerunion.server.service.TargetService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
@Transactional
public class TargetServiceImplTester {
	private TargetService targetService;

	public TargetService getTargetService() {
		return targetService;
	}

	@Autowired
	public void setTargetService(TargetService targetService) {
		this.targetService = targetService;
	}

	@Test
	public void testGetByProjectId() {
		assertNotNull(targetService.getByProjectId(BigInteger.valueOf(1L)));
	}

	@Test
	public void testReplace() {
		Target target = targetService.getByProjectId(BigInteger.valueOf(1L));
		assertTrue(targetService.replace(target) > 0);
	}
}
