package top.warmheart.workerunion.server.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import top.warmheart.server.util.dto.Page;
import top.warmheart.workerunion.server.model.QualityCheckItem;
import top.warmheart.workerunion.server.service.QualityCheckItemService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
@Transactional
public class QualityCheckItemServiceImplTester {
	private QualityCheckItemService qualityCheckItemService;

	public QualityCheckItemService getQualityCheckItemService() {
		return qualityCheckItemService;
	}

	@Autowired
	public void setQualityCheckItemService(QualityCheckItemService qualityCheckItemService) {
		this.qualityCheckItemService = qualityCheckItemService;
	}

	@Test
	public void testPageByProjectId() {
		assertEquals(1, qualityCheckItemService.pageByProjectId(BigInteger.valueOf(1L), new Page<Void>(12, 2))
				.getList().size());
	}

	@Test
	public void testGetById() {
		assertNotNull(qualityCheckItemService.getItemById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testGetAttachmentById() {
		assertNotNull(qualityCheckItemService.getAttachmentById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testListAttachmentByItemId() {
		assertEquals(1, qualityCheckItemService.listAttachmentByItemId(BigInteger.valueOf(1L)).size());
	}

	@Test
	public void testModifyItemById() {
		QualityCheckItem qualityCheckItem = qualityCheckItemService.getItemById(BigInteger.valueOf(1L));
		qualityCheckItem.setProjectFeedback("Great");
		assertEquals(1, qualityCheckItemService.modifyItemById(qualityCheckItem));
	}

	@Test
	public void testRemoveAttachmentById() {
		assertEquals(1, qualityCheckItemService.removeAttachmentById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testAddAttachment() {
		assertEquals(1, qualityCheckItemService.addAttachment(qualityCheckItemService.getAttachmentById(BigInteger
				.valueOf(1L))));
	}

	@Test
	public void testRemoveItemById() {
		assertEquals(1, qualityCheckItemService.removeItemById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testAddItem() {
		assertTrue(qualityCheckItemService.addItem(qualityCheckItemService.getItemById(BigInteger.valueOf(1L)))
				.compareTo(BigInteger.valueOf(0)) > 0);
	}
	
	@Test
	public void testListByCompanyAndStatus() {
		assertEquals(1, qualityCheckItemService.listByCompanyAndStatus(BigInteger.valueOf(1L), QualityCheckItem.RECTIFY_STATUS_PASS).size());
	}
}
