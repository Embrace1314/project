package top.warmheart.workerunion.server.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import top.warmheart.server.util.dto.Page;
import top.warmheart.workerunion.server.model.SettlementItem;
import top.warmheart.workerunion.server.service.SettlementItemService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
@Transactional
public class SettlementItemServiceImplTester {
	private SettlementItemService settlementItemService;

	public SettlementItemService getSettlementItemService() {
		return settlementItemService;
	}

	@Autowired
	public void setSettlementItemService(SettlementItemService settlementItemService) {
		this.settlementItemService = settlementItemService;
	}

	@Test
	public void testpageByProjectId() {
		assertEquals(2, settlementItemService.pageByProjectId(BigInteger.valueOf(1L), new Page<Void>(12, 2)).getList()
				.size());
	}

	@Test
	public void testGetById() {
		assertNotNull(settlementItemService.getById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testGetDetailById() {
		assertNotNull(settlementItemService.getDetailById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testAdd() {
		SettlementItem settlementItem = settlementItemService.getById(BigInteger.valueOf(1L));
		settlementItem.setMonth(12);
		assertEquals(1, settlementItemService.add(settlementItem));
	}

	@Test
	public void testRemoveById() {
		assertEquals(1, settlementItemService.removeById(BigInteger.valueOf(1L)));
		assertEquals(1, settlementItemService.removeById(BigInteger.valueOf(2L)));
	}

	@Test
	public void testModifyById() {
		SettlementItem settlementItem = settlementItemService.getById(BigInteger.valueOf(1L));
		settlementItem.setMoney(BigDecimal.valueOf(1200));
		assertEquals(1, settlementItemService.modifyById(settlementItem));
	}

	@Test
	public void testListByResourceImplementItemId() {
		assertEquals(2, settlementItemService.listByResourceImplementItemId(BigInteger.valueOf(1L)).size());
	}
}
