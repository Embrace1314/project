package top.warmheart.workerunion.server.service.impl;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import top.warmheart.server.util.dto.Page;
import top.warmheart.workerunion.server.exception.WhAttachmentExistException;
import top.warmheart.workerunion.server.model.Attachment;
import top.warmheart.workerunion.server.model.AttachmentAudit;
import top.warmheart.workerunion.server.service.AttachmentService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
@Transactional
public class AttachmentServiceImplTester {
	private AttachmentService attachmentService;

	public AttachmentService getAttachmentService() {
		return attachmentService;
	}

	@Autowired
	public void setAttachmentService(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}

	@Test
	public void testGetById() {
		assertNotNull(attachmentService.getById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testAuditById() {
		assertNotNull(attachmentService.getAuditById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testRemoveById() {
		assertEquals(1, attachmentService.removeById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testAdd() {
		Attachment attachment = new Attachment();
		attachment.setCompanyId(BigInteger.valueOf(2L));
		attachment.setProjectId(BigInteger.valueOf(2L));
		attachment.setName("test.mp4");
		attachment.setContentType("");
		attachment.setSize(BigInteger.valueOf(1200L));
		attachment.setPath("");
		attachment.setType("");
		attachment.setStaffId(BigInteger.valueOf(1L));
		attachment.setStaffName("");
		attachment.setCstCreate(new Date());
		attachment.setCstModified(attachment.getCstCreate());
		attachment.setDel(false);
		assertEquals(1, attachmentService.add(attachment));
	}

	@Test(expected = WhAttachmentExistException.class)
	public void testAddUniqueType() throws WhAttachmentExistException {
		Attachment attachment = new Attachment();
		attachment.setCompanyId(BigInteger.valueOf(1L));
		attachment.setProjectId(BigInteger.valueOf(1L));
		attachment.setName("test.mp4");
		attachment.setContentType("");
		attachment.setSize(BigInteger.valueOf(1200L));
		attachment.setPath("");
		attachment.setType(Attachment.TYPE_LETTER_OF_ACCEPTANCE);
		attachment.setStaffId(BigInteger.valueOf(1L));
		attachment.setStaffName("");
		attachment.setCstCreate(new Date());
		attachment.setCstModified(attachment.getCstCreate());
		attachment.setDel(false);
		attachmentService.addUniqueType(attachment);
	}

	@Test
	public void testGetLatestByType() {
		assertNotNull(attachmentService.getLatestByType(BigInteger.valueOf(1L), Attachment.TYPE_LETTER_OF_ACCEPTANCE));
	}

	@Test
	public void testListAuditAttachment() {
		assertTrue(!attachmentService.listAuditAttachment(BigInteger.valueOf(1L), Attachment.TYPE_LETTER_OF_ACCEPTANCE)
				.isEmpty());
	}

	@Test
	public void testPageByProjectType() {
		assertEquals(
				2,
				attachmentService
						.pageByProjectType(BigInteger.valueOf(1L), Attachment.TYPE_LETTER_OF_ACCEPTANCE,
								new Page<Void>(12, 4)).getList().size());
	}

	@Test
	public void testPageAuditByProjectType() {
		assertEquals(
				2,
				attachmentService
						.pageAuditByProjectType(BigInteger.valueOf(1L), Attachment.TYPE_LETTER_OF_ACCEPTANCE,
								new Page<Void>(12, 4)).getList().size());
	}

	@Test
	public void testReplaceAttachmentAudit() {
		AttachmentAudit attachmentAudit = new AttachmentAudit();
		attachmentAudit.setAttachmentId(BigInteger.valueOf(1L));
		attachmentAudit.setCompanyId(BigInteger.valueOf(1L));
		attachmentAudit.setProjectId(BigInteger.valueOf(1L));
		attachmentAudit.setStatus(AttachmentAudit.STATUS_PASS);
		attachmentAudit.setOpinion("TEST");
		assertTrue(attachmentService.replaceAttachmentAudit(attachmentAudit) > 0);
		attachmentAudit.setAttachmentId(BigInteger.valueOf(2L));
		assertTrue(attachmentService.replaceAttachmentAudit(attachmentAudit) > 0);
	}

	@Test
	public void testListUndeterminedByTypeInCompany() {
		assertEquals(
				2,
				attachmentService.listUndeterminedByTypeInCompany(BigInteger.valueOf(1L),
						Attachment.TYPE_LETTER_OF_ACCEPTANCE).size());
	}

	@Test
	public void testListByProjectType() {
		assertEquals(2,
				attachmentService.listByProjectType(BigInteger.valueOf(1L), Attachment.TYPE_LETTER_OF_ACCEPTANCE)
						.size());
	}
}
