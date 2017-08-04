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
import top.warmheart.workerunion.server.dto.CertificateDto;
import top.warmheart.workerunion.server.model.Certificate;
import top.warmheart.workerunion.server.model.CertificateAttachment;
import top.warmheart.workerunion.server.service.CertificateService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
@Transactional
public class CertificateServiceImplTester {
	private CertificateService certificateService;

	public CertificateService getCertificateService() {
		return certificateService;
	}

	@Autowired
	public void setCertificateService(CertificateService certificateService) {
		this.certificateService = certificateService;
	}

	@Test
	public void testGetTypeById() {
		assertNotNull(certificateService.getTypeById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testListSerie() {
		assertEquals(1, certificateService.listSerie().size());
	}

	@Test
	public void testListTypeBySerieId() {
		assertEquals(2, certificateService.listTypeBySerieId(BigInteger.valueOf(1L)).size());
	}

	@Test
	public void testAdd() {
		Certificate certificate = certificateService.getById(BigInteger.valueOf(1L));
		assertTrue(certificateService.add(certificate).compareTo(BigInteger.valueOf(0))>0);
	}

	@Test
	public void testGetById() {
		assertNotNull(certificateService.getById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testModifyById() {
		Certificate certificate = certificateService.getById(BigInteger.valueOf(1L));
		assertEquals(1, certificateService.modifyById(certificate));
	}

	@Test
	public void testRemoveById() {
		assertEquals(1, certificateService.removeById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testGetDetailById() {
		assertNotNull(certificateService.getDetailById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testListByStaffId() {
		assertEquals(2, certificateService.listByStaffId(BigInteger.valueOf(1L)).size());
	}

	@Test
	public void testPageByFuzzy() {
		CertificateDto certificateDto = new CertificateDto();
		certificateDto.setCompanyId(BigInteger.valueOf(1L));
		assertEquals(2, certificateService.pageByFuzzy(certificateDto, new Page<Void>(12, 3)).getList().size());
	}

	@Test
	public void testGetAttachmentById() {
		assertNotNull(certificateService.getAttachmentById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testRemoveAttachmentById() {
		assertEquals(1, certificateService.removeAttachmentById(BigInteger.valueOf(1L)));
	}
	
	@Test
	public void testlistAttachmentByCertificateId() {
		assertEquals(1, certificateService.listAttachmentByCertificateId(BigInteger.valueOf(1L)).size());
	}
	
	@Test
	public void testAddAttachmentById() {
		CertificateAttachment certificateAttachment = certificateService.getAttachmentById(BigInteger.valueOf(1L));
		certificateAttachment.setId(BigInteger.valueOf(2L));
		assertNotNull(certificateService.addAttachment(certificateAttachment));
	}
}
