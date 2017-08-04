package top.warmheart.workerunion.server.service.impl;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import top.warmheart.workerunion.server.service.InternalSettlementService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
@Transactional
public class InternalSettlementServiceImplTester {
	private InternalSettlementService internalSettlementService;

	public InternalSettlementService getInternalSettlementService() {
		return internalSettlementService;
	}

	@Autowired
	public void setInternalSettlementService(InternalSettlementService internalSettlementService) {
		this.internalSettlementService = internalSettlementService;
	}

	@Test
	public void testGetByProjectId() {
		assertNotNull(internalSettlementService.getByProjectId(BigInteger.valueOf(1L)));
	}

	@Test
	public void testReplace() {
		assertTrue(internalSettlementService.replace(internalSettlementService.getByProjectId(BigInteger.valueOf(1L))) > 0);
	}

}
