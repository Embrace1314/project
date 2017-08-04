package top.warmheart.workerunion.server.service.impl;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import top.warmheart.server.util.dto.Page;
import top.warmheart.workerunion.server.model.SafetyCheckItem;
import top.warmheart.workerunion.server.service.SafetyCheckItemService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
@Transactional
public class SafetyCheckItemServiceImplTester {
	private SafetyCheckItemService safetyCheckItemService;

	public SafetyCheckItemService getSafetyCheckItemService() {
		return safetyCheckItemService;
	}

	@Autowired
	public void setSafetyCheckItemService(SafetyCheckItemService safetyCheckItemService) {
		this.safetyCheckItemService = safetyCheckItemService;
	}

	@Test
	public void testPageByProjectId() {
		assertEquals(1, safetyCheckItemService.pageByProjectId(BigInteger.valueOf(1L), new Page<Void>(12, 2)).getList()
				.size());
	}

	@Test
	public void testGetById() {
		assertNotNull(safetyCheckItemService.getItemById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testGetAttachmentById() {
		assertNotNull(safetyCheckItemService.getAttachmentById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testListAttachmentByItemId() {
		assertEquals(1, safetyCheckItemService.listAttachmentByItemId(BigInteger.valueOf(1L)).size());
	}
	
	@Test
	public void testListByCompanyAndStatus() {
		assertEquals(1, safetyCheckItemService.listByCompanyAndStatus(BigInteger.valueOf(1L), SafetyCheckItem.RECTIFY_STATUS_PASS).size());
	}

	@Test
	public void testModifyItemById() {
		SafetyCheckItem safetyCheckItem = safetyCheckItemService.getItemById(BigInteger.valueOf(1L));
		safetyCheckItem.setProjectFeedback("Great");
		assertEquals(1, safetyCheckItemService.modifyItemById(safetyCheckItem));
	}

	@Test
	public void testRemoveAttachmentById() {
		assertEquals(1, safetyCheckItemService.removeAttachmentById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testAddAttachment() {
		assertEquals(1,
				safetyCheckItemService.addAttachment(safetyCheckItemService.getAttachmentById(BigInteger.valueOf(1L))));
	}

	@Test
	public void testAddItem() {
		assertTrue(safetyCheckItemService.addItem(safetyCheckItemService.getItemById(BigInteger.valueOf(1L)))
				.compareTo(BigInteger.valueOf(0)) > 0);
	}

	@Test
	public void testRemoveItemById() {
		assertEquals(1, safetyCheckItemService.removeItemById(BigInteger.valueOf(1L)));
	}
}
