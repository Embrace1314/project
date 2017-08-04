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
import top.warmheart.workerunion.server.service.MaterialService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
@Transactional
public class MaterialServiceImplTester {
	private MaterialService materialService;

	public MaterialService getMaterialService() {
		return materialService;
	}

	@Autowired
	public void setMaterialService(MaterialService materialService) {
		this.materialService = materialService;
	}

	@Test
	public void testPageByFuzzy() {
		assertEquals(1, materialService.pageByFuzzy(BigInteger.valueOf(1L), "00", "水", new Page<Void>(12, 2)).getList()
				.size());
	}

	@Test
	public void testGetByNum() {
		assertNotNull(materialService.getByNum(BigInteger.valueOf(1L), "00001"));
	}
}
