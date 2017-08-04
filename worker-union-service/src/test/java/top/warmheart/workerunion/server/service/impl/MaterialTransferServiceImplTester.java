package top.warmheart.workerunion.server.service.impl;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import top.warmheart.server.util.WhDateUtil;
import top.warmheart.server.util.dto.Page;
import top.warmheart.workerunion.server.dto.MaterialTransferApplicationDto;
import top.warmheart.workerunion.server.model.MaterialTransferApplication;
import top.warmheart.workerunion.server.model.MaterialTransferApplicationDetail;
import top.warmheart.workerunion.server.model.Staff;
import top.warmheart.workerunion.server.service.MaterialTransferService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
@Transactional
public class MaterialTransferServiceImplTester {
	private MaterialTransferService materialTransferService;

	public MaterialTransferService getMaterialTransferService() {
		return materialTransferService;
	}

	@Autowired
	public void setMaterialTransferService(MaterialTransferService materialTransferService) {
		this.materialTransferService = materialTransferService;
	}

	@Test
	public void testApplyTransferMaterial() {
		MaterialTransferApplication materialTransferApplication = materialTransferService.getById(BigInteger
				.valueOf(1L));
		List<MaterialTransferApplicationDetail> materialTransferApplicationDetails = materialTransferService
				.listApplicationDetailByApplicationId(materialTransferApplication.getId());
		materialTransferService.applyTransferMaterial(materialTransferApplication, materialTransferApplicationDetails);
	}

	@Test
	public void testListByCompanyId() {
		assertEquals(
				1,
				materialTransferService.listByCompanyId(BigInteger.valueOf(1L),
						MaterialTransferApplication.STATUS_UNDETERMINED).size());
	}

	@Test
	public void testGetDetailById() {
		assertNotNull(materialTransferService.getDetailById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testGetById() {
		assertNotNull(materialTransferService.getById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testListApplicationDetailByApplicationId() {
		assertEquals(1, materialTransferService.listApplicationDetailByApplicationId(BigInteger.valueOf(1L)).size());
	}

	@Test
	public void testFailById() {
		materialTransferService.failById(BigInteger.valueOf(1L));
	}

	@Test
	public void testPassById() {
		Staff staff = new Staff();
		staff.setId(BigInteger.valueOf(1L));
		staff.setName("TESTER");
		materialTransferService.passById(BigInteger.valueOf(1L), "BATCHNOTEST", staff);
	}

	@Test
	public void testPageByFuzzy() throws ParseException {
		MaterialTransferApplicationDto materialTransferApplicationDto = new MaterialTransferApplicationDto();
		materialTransferApplicationDto.setCompanyId(BigInteger.valueOf(1L));
		materialTransferApplicationDto.setExportProjectName("WH");
		materialTransferApplicationDto.setImportProjectId(null);
		materialTransferApplicationDto.setId(BigInteger.valueOf(1L));
		materialTransferApplicationDto.setType(MaterialTransferApplication.TYPE_TO_HEADQUARTERS);
		materialTransferApplicationDto.setStatus(MaterialTransferApplication.STATUS_UNDETERMINED);
		materialTransferApplicationDto.setStaffName("TEST");
		materialTransferApplicationDto.setCstCreate(WhDateUtil.parseDateTime("2017-04-07 00:00:00"));
		assertEquals(1, materialTransferService.pageByFuzzy(materialTransferApplicationDto, new Page<Void>(12, 2))
				.getList().size());
		MaterialTransferApplicationDto materialTransferApplicationDto2 = new MaterialTransferApplicationDto();
		materialTransferApplicationDto2.setCompanyId(BigInteger.valueOf(1L));
		assertEquals(1, materialTransferService.pageByFuzzy(materialTransferApplicationDto2, new Page<Void>(12, 2))
				.getList().size());
	}

}
