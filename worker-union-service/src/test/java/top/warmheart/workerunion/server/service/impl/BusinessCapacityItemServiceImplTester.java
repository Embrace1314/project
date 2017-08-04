package top.warmheart.workerunion.server.service.impl;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import top.warmheart.server.util.dto.Page;
import top.warmheart.workerunion.server.model.BusinessCapacityItem;
import top.warmheart.workerunion.server.service.BusinessCapacityItemService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
@Transactional
public class BusinessCapacityItemServiceImplTester {
	private BusinessCapacityItemService businessCapacityItemService;

	public BusinessCapacityItemService getBusinessCapacityItemService() {
		return businessCapacityItemService;
	}

	@Autowired
	public void setBusinessCapacityItemService(BusinessCapacityItemService businessCapacityItemService) {
		this.businessCapacityItemService = businessCapacityItemService;
	}

	@Test
	public void testPageByProjectId() {
		assertEquals(5, businessCapacityItemService.pageByProjectId(BigInteger.valueOf(1L), new Page<Void>(12, 2)).getList().size());
		System.out.println(businessCapacityItemService.pageByProjectId(BigInteger.valueOf(1L), new Page<Void>(12, 2)));
	}

	@Test
	public void testGetByProjectYearMonth() {
		assertNotNull(businessCapacityItemService.getByProjectYearMonth(BigInteger.valueOf(1L), 2016, 1));
	}

	@Test
	public void testAdd() {
		BusinessCapacityItem businessCapacityItem = businessCapacityItemService.getById(BigInteger.valueOf(1L));
		businessCapacityItem.setMonth(12);
		assertEquals(1, businessCapacityItemService.add(businessCapacityItem));
	}

	@Test
	public void testGetById() {
		assertNotNull(businessCapacityItemService.getById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testModifyById() {
		BusinessCapacityItem businessCapacityItem = businessCapacityItemService.getById(BigInteger.valueOf(1L));
		businessCapacityItem.setProductValue(BigDecimal.valueOf(1200));
		assertEquals(1, businessCapacityItemService.modifyById(businessCapacityItem));
	}

	@Test
	public void testRemoveById() {
		assertEquals(1, businessCapacityItemService.removeById(BigInteger.valueOf(3L)));
		assertEquals(0, businessCapacityItemService.removeById(BigInteger.valueOf(1L)));
	}

}
