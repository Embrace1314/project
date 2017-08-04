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

import top.warmheart.workerunion.server.model.ResourceImplementItem;
import top.warmheart.workerunion.server.service.ResourceImplementItemService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
@Transactional
public class ResourceImplementItemServiceImplTester {
	private ResourceImplementItemService resourceImplementItemService;

	public ResourceImplementItemService getResourceImplementItemService() {
		return resourceImplementItemService;
	}

	@Autowired
	public void setResourceImplementItemService(ResourceImplementItemService resourceImplementItemService) {
		this.resourceImplementItemService = resourceImplementItemService;
	}

	@Test
	public void testListByProjectId() {
		assertEquals(2, resourceImplementItemService.listByProjectId(BigInteger.valueOf(1L)).size());
	}

	@Test
	public void testGetById() {
		assertNotNull(resourceImplementItemService.getById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testAdd() {
		ResourceImplementItem resourceImplementItem= new ResourceImplementItem();
		resourceImplementItem.setCompanyId(BigInteger.valueOf(1L));
		resourceImplementItem.setProjectId(BigInteger.valueOf(1L));
		resourceImplementItem.setPurchaseItemId(BigInteger.valueOf(1L));
		resourceImplementItem.setPurchaseItemName("");
		resourceImplementItem.setFundPlanItemId(BigInteger.valueOf(1L));
		resourceImplementItem.setSubcontractorId(BigInteger.valueOf(1L));
		resourceImplementItem.setSubcontractorName("");
		resourceImplementItem.setContractId(BigInteger.valueOf(1L));
		resourceImplementItem.setContractName("");
		resourceImplementItem.setContractNum("");
		resourceImplementItem.setName("testsss");
		resourceImplementItem.setUnit("");
		resourceImplementItem.setPrice(BigDecimal.valueOf(12));
		resourceImplementItem.setAmount(BigDecimal.valueOf(1));
		resourceImplementItem.setMoney(resourceImplementItem.getPrice().multiply(resourceImplementItem.getAmount()));
		
		assertEquals(1, resourceImplementItemService.add(resourceImplementItem));
	}

	@Test
	public void testRemoveById() {
		assertEquals(0, resourceImplementItemService.removeById(BigInteger.valueOf(1L)));
		assertEquals(1, resourceImplementItemService.removeById(BigInteger.valueOf(2L)));
	}

	@Test
	public void testModifyById() {
		ResourceImplementItem resourceImplementItem= resourceImplementItemService.getById(BigInteger.valueOf(1L));
		resourceImplementItem.setName("testsss2222");
		assertEquals(1, resourceImplementItemService.modifyById(resourceImplementItem));
	}
	
	@Test
	public void testListByFundPlanItemId() {
		assertEquals(2, resourceImplementItemService.listByFundPlanItemId(BigInteger.valueOf(1L)).size());
	}
	
	@Test
	public void testListSimpleByProjectId() {
		assertEquals(2, resourceImplementItemService.listSimpleByProjectId(BigInteger.valueOf(1L)).size());
	}
}
