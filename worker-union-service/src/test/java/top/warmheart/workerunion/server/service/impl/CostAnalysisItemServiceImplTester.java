package top.warmheart.workerunion.server.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import top.warmheart.workerunion.server.model.CostAnalysisItem;
import top.warmheart.workerunion.server.service.CostAnalysisItemService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
@Transactional
public class CostAnalysisItemServiceImplTester {
	private CostAnalysisItemService costAnalysisItemService;

	public CostAnalysisItemService getCostAnalysisItemService() {
		return costAnalysisItemService;
	}

	@Autowired
	public void setCostAnalysisItemService(CostAnalysisItemService costAnalysisItemService) {
		this.costAnalysisItemService = costAnalysisItemService;
	}

	@Test
	public void testListByProjectId() {
		assertEquals(1, costAnalysisItemService.listByProjectId(BigInteger.valueOf(1L)).size());
	}

	@Test
	public void testGetById() {
		assertNotNull(costAnalysisItemService.getById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testAdd() {
		CostAnalysisItem costAnalysisItem = new CostAnalysisItem();
		costAnalysisItem.setId(BigInteger.valueOf(2L));
		costAnalysisItem.setCompanyId(BigInteger.valueOf(1L));
		costAnalysisItem.setProjectId(BigInteger.valueOf(1L));
		costAnalysisItem.setType(CostAnalysisItem.TYPE_DIRECT_COST);
		costAnalysisItem.setName("");
		costAnalysisItem.setCost(BigDecimal.valueOf(1200L));
		costAnalysisItem.setMemo("");
		costAnalysisItem.setCstCreate(new Date());
		costAnalysisItem.setCstModified(costAnalysisItem.getCstCreate());
		assertEquals(1, costAnalysisItemService.add(costAnalysisItem));
	}

	@Test
	public void testRemoveById() {
		assertEquals(1, costAnalysisItemService.removeById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testModifyById() {
		CostAnalysisItem costAnalysisItem = new CostAnalysisItem();
		costAnalysisItem.setId(BigInteger.valueOf(1L));
		costAnalysisItem.setCompanyId(BigInteger.valueOf(1L));
		costAnalysisItem.setProjectId(BigInteger.valueOf(1L));
		costAnalysisItem.setType(CostAnalysisItem.TYPE_DIRECT_COST);
		costAnalysisItem.setName("");
		costAnalysisItem.setCost(BigDecimal.valueOf(1200L));
		costAnalysisItem.setMemo("");
		costAnalysisItem.setCstCreate(new Date());
		costAnalysisItem.setCstModified(costAnalysisItem.getCstCreate());
		assertEquals(1, costAnalysisItemService.modifyById(costAnalysisItem));
	}

}
