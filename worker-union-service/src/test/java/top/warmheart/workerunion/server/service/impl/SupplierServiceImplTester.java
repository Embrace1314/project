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
import top.warmheart.workerunion.server.dto.SupplierDto;
import top.warmheart.workerunion.server.model.Supplier;
import top.warmheart.workerunion.server.model.SupplierAttachment;
import top.warmheart.workerunion.server.model.SupplierQualification;
import top.warmheart.workerunion.server.service.SupplierService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
@Transactional
public class SupplierServiceImplTester {
	private SupplierService supplierService;

	public SupplierService getSupplierService() {
		return supplierService;
	}

	@Autowired
	public void setSupplierService(SupplierService supplierService) {
		this.supplierService = supplierService;
	}

	@Test
	public void testGetGradeById() {
		assertNotNull(supplierService.getGradeById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testGetQualificationById() {
		assertNotNull(supplierService.getQualificationById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testGetByCompanyAndNum() {
		assertNotNull(supplierService.getByCompanyAndNum(BigInteger.valueOf(1L), "1"));
	}

	@Test
	public void testAdd() {
		Supplier supplier = supplierService.getById(BigInteger.valueOf(1L));
		supplier.setNum("TEST00002");
		assertTrue(supplierService.add(supplier, new ArrayList<SupplierQualification>()).compareTo(
				BigInteger.valueOf(0L)) > 0);

	}

	@Test
	public void testGetById() {
		assertNotNull(supplierService.getById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testRemoveById() {
		assertEquals(1, supplierService.removeById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testModifyById() {
		Supplier supplier = supplierService.getById(BigInteger.valueOf(1L));
		supplier.setContactPhone("");
		supplierService.modifyById(supplier, new ArrayList<SupplierQualification>());
	}

	@Test
	public void testListQualificationBySupplierId() {
		assertEquals(2, supplierService.listQualificationBySupplierId(BigInteger.valueOf(1L)).size());
	}

	@Test
	public void testListAttachmentBySupplierId() {
		assertEquals(1, supplierService.listAttachmentBySupplierId(BigInteger.valueOf(1L)).size());
	}

	@Test
	public void testGetAttachmentById() {
		assertNotNull(supplierService.getAttachmentById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testRemoveAttachmentById() {
		assertEquals(1, supplierService.removeAttachmentById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testListGradeByCompanyId() {
		assertEquals(2, supplierService.listGrade().size());
	}

	@Test
	public void testListQualificationByCompanyId() {
		assertEquals(2, supplierService.listQualification().size());
	}

	@Test
	public void testPageByFuzzy() {
		SupplierDto supplierDto = new SupplierDto();
		supplierDto.setCompanyId(BigInteger.valueOf(1L));
		assertEquals(1, supplierService.pageByFuzzy(supplierDto, new Page<Void>(12, 2)).getList().size());
	}

	@Test
	public void testAddAttachment() {
		SupplierAttachment attachment = supplierService.getAttachmentById(BigInteger.valueOf(1L));
		assertEquals(1, supplierService.addAttachment(attachment));
	}
}
