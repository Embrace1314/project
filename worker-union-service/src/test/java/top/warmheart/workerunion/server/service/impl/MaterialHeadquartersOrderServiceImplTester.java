package top.warmheart.workerunion.server.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import top.warmheart.server.util.WhDateUtil;
import top.warmheart.server.util.dto.Page;
import top.warmheart.workerunion.server.model.MaterialHeadquartersOrder;
import top.warmheart.workerunion.server.model.MaterialHeadquartersOrderDetail;
import top.warmheart.workerunion.server.model.MaterialOrder;
import top.warmheart.workerunion.server.model.MaterialOrderDetail;
import top.warmheart.workerunion.server.service.MaterialHeadquartersOrderService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
@Transactional
public class MaterialHeadquartersOrderServiceImplTester {
	private MaterialHeadquartersOrderService materialHeadquartersOrderService;

	public MaterialHeadquartersOrderService getMaterialHeadquartersOrderService() {
		return materialHeadquartersOrderService;
	}

	@Autowired
	public void setMaterialHeadquartersOrderService(MaterialHeadquartersOrderService materialHeadquartersOrderService) {
		this.materialHeadquartersOrderService = materialHeadquartersOrderService;
	}

	@Test
	public void testPageByFuzzy() throws ParseException {
		assertEquals(
				2,
				materialHeadquartersOrderService
						.pageByFuzzy(BigInteger.valueOf(1L), "TEST", WhDateUtil.parseTimestamp("20170330000000"), "00",
								new Page<Void>(12, 2)).getList().size());
	}

	@Test
	public void testPageDetailByFuzzy() throws ParseException {
		assertEquals(
				2,
				materialHeadquartersOrderService
						.pageDetailByFuzzy(BigInteger.valueOf(1L), "TEST", WhDateUtil.parseTimestamp("20170330000000"),
								"00", new Page<Void>(12, 2)).getList().size());
	}

	@Test
	public void testAmountOrderDetailByType() {
		assertEquals(
				0,
				BigDecimal.valueOf(1200.00).compareTo(
						materialHeadquartersOrderService.amountOrderDetailByType(BigInteger.valueOf(1L), "001",
								MaterialHeadquartersOrder.TYPE_IMPORT)));
	}

	@Test
	public void testPageOrderDetailByNum() {
		assertEquals(
				2,
				materialHeadquartersOrderService
						.pageOrderDetailByNum(BigInteger.valueOf(1L), "001", new Page<Void>(12, 2)).getList().size());
	}

	@Test
	public void testGetByCompanyBatchNo() {
		assertNotNull(materialHeadquartersOrderService.getByCompanyBatchNo(BigInteger.valueOf(1L), "000001"));
	}

	@Test
	public void testImportMaterialHeadquarters() {
		MaterialHeadquartersOrder materialHeadquartersOrder = materialHeadquartersOrderService.getByCompanyBatchNo(
				BigInteger.valueOf(1L), "000001");
		materialHeadquartersOrder.setBatchNo("0000000002");
		materialHeadquartersOrder.setType(MaterialHeadquartersOrder.TYPE_IMPORT);
		List<MaterialHeadquartersOrderDetail> materialHeadquartersOrderDetails = new ArrayList<MaterialHeadquartersOrderDetail>();
		MaterialHeadquartersOrderDetail materialHeadquartersOrderDetail = new MaterialHeadquartersOrderDetail();
		materialHeadquartersOrderDetail.setName("TEST");
		materialHeadquartersOrderDetail.setNum("001");
		materialHeadquartersOrderDetail.setModel("");
		materialHeadquartersOrderDetail.setUnit("");
		materialHeadquartersOrderDetails.add(materialHeadquartersOrderDetail);
		materialHeadquartersOrderDetail.setAmount(BigDecimal.valueOf(1200));
		materialHeadquartersOrderDetail.setPrice(BigDecimal.valueOf(1));
		materialHeadquartersOrderDetail.setMoney(BigDecimal.valueOf(1200));
		materialHeadquartersOrder.setMaterialHeadquartersOrderDetails(materialHeadquartersOrderDetails);
		materialHeadquartersOrderService.importMaterialHeadquarters(materialHeadquartersOrder);
	}

	@Test
	public void testExportMaterialHeadquarters() {
		MaterialHeadquartersOrder materialHeadquartersOrder = materialHeadquartersOrderService.getByCompanyBatchNo(
				BigInteger.valueOf(1L), "000001");
		materialHeadquartersOrder.setBatchNo("0000000003");
		materialHeadquartersOrder.setTargetProjectId(BigInteger.valueOf(1L));
		materialHeadquartersOrder.setMoney(BigDecimal.valueOf(0));
		materialHeadquartersOrder.setType(MaterialHeadquartersOrder.TYPE_EXPORT);
		List<MaterialHeadquartersOrderDetail> materialHeadquartersOrderDetails = new ArrayList<MaterialHeadquartersOrderDetail>();
		MaterialHeadquartersOrderDetail materialHeadquartersOrderDetail = new MaterialHeadquartersOrderDetail();
		materialHeadquartersOrderDetail.setName("TEST");
		materialHeadquartersOrderDetail.setNum("00001");
		materialHeadquartersOrderDetail.setModel("");
		materialHeadquartersOrderDetail.setUnit("");
		materialHeadquartersOrderDetails.add(materialHeadquartersOrderDetail);
		materialHeadquartersOrderDetail.setAmount(BigDecimal.valueOf(1));
		materialHeadquartersOrderDetail.setPrice(BigDecimal.valueOf(0));
		materialHeadquartersOrderDetail.setMoney(BigDecimal.valueOf(0));
		materialHeadquartersOrder.setMaterialHeadquartersOrderDetails(materialHeadquartersOrderDetails);

		MaterialOrder materialOrder = new MaterialOrder();
		materialOrder.setCompanyId(BigInteger.valueOf(1L));
		materialOrder.setProjectId(BigInteger.valueOf(1L));
		materialOrder.setStaffId(BigInteger.valueOf(1L));
		materialOrder.setStaffName("TESTER");
		materialOrder.setBatchNo("0000000003");
		materialOrder.setMoney(BigDecimal.valueOf(1200));
		materialOrder.setType(MaterialOrder.TYPE_IMPORT);
		List<MaterialOrderDetail> materialOrderDetails = new ArrayList<MaterialOrderDetail>();
		MaterialOrderDetail materialOrderDetail = new MaterialOrderDetail();
		materialOrderDetail.setName("TEST");
		materialOrderDetail.setNum("001");
		materialOrderDetail.setModel("");
		materialOrderDetail.setUnit("");
		materialOrderDetails.add(materialOrderDetail);
		materialOrderDetail.setAmount(BigDecimal.valueOf(1200));
		materialOrderDetail.setPrice(BigDecimal.valueOf(1));
		materialOrderDetail.setMoney(BigDecimal.valueOf(1200));
		materialOrder.setMaterialOrderDetails(materialOrderDetails);
		materialHeadquartersOrderService.exportMaterialHeadquartersToProject(materialHeadquartersOrder, materialOrder);
	}
}
