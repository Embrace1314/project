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
import top.warmheart.workerunion.server.model.CostAnalysisItemDeepen;
import top.warmheart.workerunion.server.service.CostAnalysisItemDeepenService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
@Transactional
public class CostAnalysisItemDeepenServiceImplTester {
	private CostAnalysisItemDeepenService costAnalysisItemDeepenService;

	public CostAnalysisItemDeepenService getCostAnalysisItemDeepenService() {
		return costAnalysisItemDeepenService;
	}

	@Autowired
	public void setCostAnalysisItemDeepenService(CostAnalysisItemDeepenService costAnalysisItemDeepenService) {
		this.costAnalysisItemDeepenService = costAnalysisItemDeepenService;
	}

	@Test
	public void testListByProjectId() {
		assertEquals(2, costAnalysisItemDeepenService.listByProjectId(BigInteger.valueOf(1L))
				.size());
	}

	@Test
	public void testGetById() {
		assertNotNull(costAnalysisItemDeepenService.getById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testAdd() {
		CostAnalysisItemDeepen costAnalysisItemDeepen = new CostAnalysisItemDeepen();
		costAnalysisItemDeepen.setId(BigInteger.valueOf(2L));
		costAnalysisItemDeepen.setCompanyId(BigInteger.valueOf(1L));
		costAnalysisItemDeepen.setProjectId(BigInteger.valueOf(1L));
		costAnalysisItemDeepen.setType(CostAnalysisItem.TYPE_DIRECT_COST);
		costAnalysisItemDeepen.setName("");
		costAnalysisItemDeepen.setPrice(BigDecimal.valueOf(11.5));
		costAnalysisItemDeepen.setAmount(BigDecimal.valueOf(2.2));
		costAnalysisItemDeepen.setUnit("元/斤");
		costAnalysisItemDeepen.setCost(costAnalysisItemDeepen.getPrice().multiply(costAnalysisItemDeepen.getAmount()));
		costAnalysisItemDeepen.setMemo("");
		costAnalysisItemDeepen.setCstCreate(new Date());
		costAnalysisItemDeepen.setCstModified(costAnalysisItemDeepen.getCstCreate());
		assertEquals(1, costAnalysisItemDeepenService.add(costAnalysisItemDeepen));
	}

	@Test
	public void testRemoveById() {
		assertEquals(0, costAnalysisItemDeepenService.removeById(BigInteger.valueOf(1L)));
		assertEquals(1, costAnalysisItemDeepenService.removeById(BigInteger.valueOf(2L)));
	}

	@Test
	public void testModifyById() {
		CostAnalysisItemDeepen costAnalysisItemDeepen = new CostAnalysisItemDeepen();
		costAnalysisItemDeepen.setId(BigInteger.valueOf(1L));
		costAnalysisItemDeepen.setCompanyId(BigInteger.valueOf(1L));
		costAnalysisItemDeepen.setProjectId(BigInteger.valueOf(1L));
		costAnalysisItemDeepen.setType(CostAnalysisItem.TYPE_DIRECT_COST);
		costAnalysisItemDeepen.setName("");
		costAnalysisItemDeepen.setPrice(BigDecimal.valueOf(13.5));
		costAnalysisItemDeepen.setAmount(BigDecimal.valueOf(2.2));
		costAnalysisItemDeepen.setUnit("元/斤");
		costAnalysisItemDeepen.setCost(costAnalysisItemDeepen.getPrice().multiply(costAnalysisItemDeepen.getAmount()));
		costAnalysisItemDeepen.setMemo("");
		costAnalysisItemDeepen.setCstCreate(new Date());
		costAnalysisItemDeepen.setCstModified(costAnalysisItemDeepen.getCstCreate());
		assertEquals(1, costAnalysisItemDeepenService.modifyById(costAnalysisItemDeepen));
	}

}
