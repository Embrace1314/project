package top.warmheart.workerunion.server.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import top.warmheart.server.util.dto.Page;
import top.warmheart.workerunion.server.model.ProductCapacityItem;
import top.warmheart.workerunion.server.service.ProductCapacityItemService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
@Transactional
public class ProductCapacityItemServiceImplTester {
	private ProductCapacityItemService productCapacityItemService;

	public ProductCapacityItemService getProductCapacityItemService() {
		return productCapacityItemService;
	}

	@Autowired
	public void setProductCapacityItemService(ProductCapacityItemService productCapacityItemService) {
		this.productCapacityItemService = productCapacityItemService;
	}

	@Test
	public void testPageByProjectId() {
		assertEquals(5, productCapacityItemService.pageByProjectId(BigInteger.valueOf(1L), new Page<Void>(12, 2)).getList().size());
		System.out.println(productCapacityItemService.pageByProjectId(BigInteger.valueOf(1L), new Page<Void>(12, 2)));
	}

	@Test
	public void testGetByProjectYearMonth() {
		assertNotNull(productCapacityItemService.getByProjectYearMonth(BigInteger.valueOf(1L), 2016, 1));
	}

	@Test
	public void testAdd() {
		ProductCapacityItem productCapacityItem = productCapacityItemService.getById(BigInteger.valueOf(1L));
		productCapacityItem.setMonth(12);
		assertEquals(1, productCapacityItemService.add(productCapacityItem));
	}

	@Test
	public void testGetById() {
		assertNotNull(productCapacityItemService.getById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testModifyById() {
		ProductCapacityItem productCapacityItem = productCapacityItemService.getById(BigInteger.valueOf(1L));
		productCapacityItem.setActualState("OK");
		assertEquals(1, productCapacityItemService.modifyById(productCapacityItem));
	}

	@Test
	public void testRemoveById() {
		assertEquals(1, productCapacityItemService.removeById(BigInteger.valueOf(3L)));
		assertEquals(0, productCapacityItemService.removeById(BigInteger.valueOf(1L)));
	}

}
