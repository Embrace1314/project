package top.warmheart.workerunion.server.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import top.warmheart.server.util.dto.Page;
import top.warmheart.workerunion.server.dto.SubcontractorDto;
import top.warmheart.workerunion.server.model.Subcontractor;
import top.warmheart.workerunion.server.model.SubcontractorAttachment;
import top.warmheart.workerunion.server.model.SubcontractorQualification;
import top.warmheart.workerunion.server.service.SubcontractorService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
@Transactional
public class SubcontractorServiceImplTester {
	private SubcontractorService subcontractorService;

	public SubcontractorService getSubcontractorService() {
		return subcontractorService;
	}

	@Autowired
	public void setSubcontractorService(SubcontractorService subcontractorService) {
		this.subcontractorService = subcontractorService;
	}

	@Test
	public void testGetById() {
		assertNotNull(subcontractorService.getById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testListSimpleItemByCompanyId() {
		assertEquals(1, subcontractorService.listSimpleItemByCompanyId(BigInteger.valueOf(1L)).size());
	}

	@Test
	public void testGetGradeById() {
		assertNotNull(subcontractorService.getGradeById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testGetQualificationById() {
		assertNotNull(subcontractorService.getQualificationById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testGetByCompanyAndNum() {
		assertNotNull(subcontractorService.getByCompanyAndNum(BigInteger.valueOf(1L), "1"));
	}

	@Test
	public void testAdd() {
		Subcontractor subcontractor = subcontractorService.getById(BigInteger.valueOf(1L));
		subcontractor.setNum("TEST00002");
		assertTrue(subcontractorService.add(subcontractor, new ArrayList<SubcontractorQualification>()).compareTo(BigInteger.valueOf(0L)) > 0);
	}

	@Test
	public void testRemoveById() {
		assertEquals(1, subcontractorService.removeById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testModifyById() {
		Subcontractor subcontractor = subcontractorService.getById(BigInteger.valueOf(1L));
		subcontractor.setContactPhone("");
		subcontractorService.modifyById(subcontractor, new ArrayList<SubcontractorQualification>());
	}

	@Test
	public void testListQualificationBySubcontractorId() {
		assertEquals(2, subcontractorService.listQualificationBySubcontractorId(BigInteger.valueOf(1L)).size());
	}

	@Test
	public void testListAttachmentBySubcontractorId() {
		assertEquals(1, subcontractorService.listAttachmentBySubcontractorId(BigInteger.valueOf(1L)).size());
	}

	@Test
	public void testGetAttachmentById() {
		assertNotNull(subcontractorService.getAttachmentById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testRemoveAttachmentById() {
		assertEquals(1, subcontractorService.removeAttachmentById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testListGradeByCompanyId() {
		assertEquals(2, subcontractorService.listGrade().size());
	}

	@Test
	public void testListQualificationByCompanyId() {
		assertEquals(2, subcontractorService.listQualification().size());
	}

	@Test
	public void testPageByFuzzy() {
		SubcontractorDto subcontractorDto = new SubcontractorDto();
		subcontractorDto.setCompanyId(BigInteger.valueOf(1L));
		assertEquals(1, subcontractorService.pageByFuzzy(subcontractorDto, new Page<Void>(12, 2)).getList().size());
	}

	@Test
	public void testAddAttachment() {
		SubcontractorAttachment attachment = subcontractorService.getAttachmentById(BigInteger.valueOf(1L));
		assertEquals(1, subcontractorService.addAttachment(attachment));
	}
}
