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
import top.warmheart.workerunion.server.model.MaterialType;
import top.warmheart.workerunion.server.model.MaterialTypeAttachment;
import top.warmheart.workerunion.server.service.MaterialTypeService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
@Transactional
public class MaterialTypeServiceImplTester {
	private MaterialTypeService materialTypeService;

	public MaterialTypeService getMaterialTypeService() {
		return materialTypeService;
	}

	@Autowired
	public void setMaterialTypeService(MaterialTypeService materialTypeService) {
		this.materialTypeService = materialTypeService;
	}

	@Test
	public void testGetByNum() {
		assertNotNull(materialTypeService.getByNum(BigInteger.valueOf(1L), "001"));
	}

	@Test
	public void testGetById() {
		assertNotNull(materialTypeService.getById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testListByTypeId() {
		assertNotNull(materialTypeService.listByTypeId(BigInteger.valueOf(1L)));
	}

	@Test
	public void testAdd() {
		MaterialType materialType = materialTypeService.getById(BigInteger.valueOf(1L));
		materialType.setNum("TEST000001");
		assertTrue(materialTypeService.add(materialType).compareTo(BigInteger.valueOf(0L)) > 0);
	}

	@Test
	public void testGetAttachmentById() {
		assertNotNull(materialTypeService.getAttachmentById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testRemoveAttachmentById() {
		assertNotNull(materialTypeService.removeAttachmentById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testRemoveById() {
		assertNotNull(materialTypeService.removeById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testAddAttachment() {
		MaterialTypeAttachment attachment = materialTypeService.getAttachmentById(BigInteger.valueOf(1L));
		assertEquals(1, materialTypeService.addAttachment(attachment));
	}
	
	@Test
	public void testPageByFuzzy() {
		MaterialType materialType = new MaterialType();
		materialType.setCompanyId(BigInteger.valueOf(1L));
		assertEquals(1, materialTypeService.pageByFuzzy(materialType, new Page<Void>(12, 2)).getList().size());
	}
}
