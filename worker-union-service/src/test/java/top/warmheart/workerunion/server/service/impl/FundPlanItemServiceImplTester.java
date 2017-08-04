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

import top.warmheart.workerunion.server.model.FundPlanItem;
import top.warmheart.workerunion.server.service.FundPlanItemService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
@Transactional
public class FundPlanItemServiceImplTester {
	private FundPlanItemService fundPlanItemService;

	public FundPlanItemService getFundPlanItemService() {
		return fundPlanItemService;
	}

	@Autowired
	public void setFundPlanItemService(FundPlanItemService fundPlanItemService) {
		this.fundPlanItemService = fundPlanItemService;
	}

	@Test
	public void testListByProjectId() {
		assertEquals(2, fundPlanItemService.listByProjectId(BigInteger.valueOf(1L)).size());
	}

	@Test
	public void testGetById() {
		assertNotNull(fundPlanItemService.getById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testAdd() {
		FundPlanItem fundPlanItem= new FundPlanItem();
		fundPlanItem.setCompanyId(BigInteger.valueOf(1L));
		fundPlanItem.setProjectId(BigInteger.valueOf(1L));
		fundPlanItem.setPurchaseItemId(BigInteger.valueOf(1L));
		fundPlanItem.setPurchaseItemName("test");
		fundPlanItem.setCostAnalysisItemDeepenId(BigInteger.valueOf(1L));
		fundPlanItem.setRequirement("Test");
		fundPlanItem.setMoney(BigDecimal.valueOf(12));
		fundPlanItem.setExpectedPurchaseDate(new Date());
		fundPlanItem.setExpectedServiceDate(new Date());
		assertEquals(1, fundPlanItemService.add(fundPlanItem));
	}

	@Test
	public void testRemoveById() {
		assertEquals(0, fundPlanItemService.removeById(BigInteger.valueOf(1L)));
		assertEquals(1, fundPlanItemService.removeById(BigInteger.valueOf(2L)));
	}

	@Test
	public void testModifyById() {
		FundPlanItem fundPlanItem= fundPlanItemService.getById(BigInteger.valueOf(1L));
		fundPlanItem.setRequirement("Test");
		fundPlanItem.setMoney(BigDecimal.valueOf(12));
		fundPlanItem.setExpectedPurchaseDate(new Date());
		fundPlanItem.setExpectedServiceDate(new Date());
		assertEquals(1, fundPlanItemService.modifyById(fundPlanItem));
	}
	
	@Test
	public void testListByCostAnalysisItemId() {
		assertEquals(2, fundPlanItemService.listByCostAnalysisItemId(BigInteger.valueOf(1L)).size());
	}
	@Test
	public void testListSimpleByProjectId() {
		assertEquals(2, fundPlanItemService.listSimpleByProjectId(BigInteger.valueOf(1L)).size());
	}
}
