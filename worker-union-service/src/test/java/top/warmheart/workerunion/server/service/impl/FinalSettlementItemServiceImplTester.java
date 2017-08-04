package top.warmheart.workerunion.server.service.impl;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import top.warmheart.workerunion.server.model.FinalSettlementItemAttachment;
import top.warmheart.workerunion.server.service.FinalSettlementItemService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
@Transactional
public class FinalSettlementItemServiceImplTester {
	private FinalSettlementItemService finalSettlementItemService;

	public FinalSettlementItemService getFinalSettlementItemService() {
		return finalSettlementItemService;
	}

	@Autowired
	public void setFinalSettlementItemService(FinalSettlementItemService finalSettlementItemService) {
		this.finalSettlementItemService = finalSettlementItemService;
	}

	@Test
	public void testListByProjectId() {
		assertEquals(1, finalSettlementItemService.listItemByProjectId(BigInteger.valueOf(1L)).size());
	}

	@Test
	public void testGetByItemId() {
		assertNotNull(finalSettlementItemService.getAttachmentByItemId(BigInteger.valueOf(1L)));
	}

	@Test
	public void testAddItem() {
		assertEquals(1,
				finalSettlementItemService.addItem(finalSettlementItemService.getItemById(BigInteger.valueOf(1L))));
	}

	@Test
	public void testGetItemById() {
		assertNotNull(finalSettlementItemService.getItemById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testModifyItemById() {
		assertEquals(1, finalSettlementItemService.modifyItemById(finalSettlementItemService.getItemById(BigInteger
				.valueOf(1L))));
	}

	@Test
	public void testRemoveItemById() {
		assertEquals(1, finalSettlementItemService.removeItemById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testRemoveAttachmentByItemId() {
		assertEquals(1, finalSettlementItemService.removeAttachmentByItemId(BigInteger.valueOf(1L)));
	}

	@Test
	public void testAddAttachment() {
		FinalSettlementItemAttachment finalSettlementItemAttachment = finalSettlementItemService
				.getAttachmentByItemId(BigInteger.valueOf(1L));
		finalSettlementItemAttachment.setFinalSettlementItemId(BigInteger.valueOf(5L));
		assertEquals(1, finalSettlementItemService.addAttachment(finalSettlementItemAttachment));
	}
}
