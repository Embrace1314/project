package top.warmheart.workerunion.server.service.impl;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import top.warmheart.workerunion.server.service.PurchaseItemService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
@Transactional
public class PurchaseItemServiceImplTester {
	private PurchaseItemService purchaseItemService;

	public PurchaseItemService getPurchaseItemService() {
		return purchaseItemService;
	}

	@Autowired
	public void setPurchaseItemService(PurchaseItemService purchaseItemService) {
		this.purchaseItemService = purchaseItemService;
	}

	@Test
	public void testListByCompanyId() {
		assertEquals(1, purchaseItemService.list().size());
	}

	@Test
	public void testGetById() {
		assertNotNull(purchaseItemService.getById(BigInteger.valueOf(1L)));
	}

}
