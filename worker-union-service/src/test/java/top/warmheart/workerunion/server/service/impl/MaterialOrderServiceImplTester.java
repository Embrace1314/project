package top.warmheart.workerunion.server.service.impl;

import static org.junit.Assert.*;

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
import top.warmheart.workerunion.server.model.MaterialOrder;
import top.warmheart.workerunion.server.model.MaterialOrderDetail;
import top.warmheart.workerunion.server.service.MaterialOrderService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
@Transactional
public class MaterialOrderServiceImplTester {
	private MaterialOrderService materialOrderService;

	public MaterialOrderService getMaterialOrderService() {
		return materialOrderService;
	}

	@Autowired
	public void setMaterialOrderService(MaterialOrderService materialOrderService) {
		this.materialOrderService = materialOrderService;
	}

	@Test
	public void testPageByFuzzy() throws ParseException {
		assertEquals(
				2,
				materialOrderService
						.pageByFuzzy(BigInteger.valueOf(1L), "TEST", WhDateUtil.parseTimestamp("20170330000000"), "00",
								new Page<Void>(12, 2)).getList().size());
	}

	@Test
	public void testPageDetailByFuzzy() throws ParseException {
		assertEquals(
				2,
				materialOrderService
						.pageDetailByFuzzy(BigInteger.valueOf(1L), "TEST", WhDateUtil.parseTimestamp("20170330000000"), "00",
								new Page<Void>(12, 2)).getList().size());
	}
	
	@Test
	public void testAmountOrderDetailByType() {
		assertEquals(
				0,
				BigDecimal.valueOf(1200.00).compareTo(
						materialOrderService.amountOrderDetailByType(BigInteger.valueOf(1L), "001",
								MaterialOrder.TYPE_IMPORT)));
	}

	@Test
	public void testPageOrderDetailByNum() {
		assertEquals(2, materialOrderService.pageOrderDetailByNum(BigInteger.valueOf(1L), "001", new Page<Void>(12, 2))
				.getList().size());
	}

	@Test
	public void testGetByProjectBatchNo() {
		assertNotNull(materialOrderService.getByProjectBatchNo(BigInteger.valueOf(1L), "000001"));
	}

	@Test
	public void testImportMaterial() {
		MaterialOrder materialOrder = materialOrderService.getByProjectBatchNo(BigInteger.valueOf(1L), "000001");
		materialOrder.setBatchNo("0000000002");
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
		materialOrderService.importMaterial(materialOrder);
	}
	
	@Test
	public void testExportMaterial() {
		MaterialOrder materialOrder = materialOrderService.getByProjectBatchNo(BigInteger.valueOf(1L), "000001");
		materialOrder.setBatchNo("0000000003");
		materialOrder.setMoney(BigDecimal.valueOf(0));
		materialOrder.setType(MaterialOrder.TYPE_EXPORT);
		List<MaterialOrderDetail> materialOrderDetails = new ArrayList<MaterialOrderDetail>();
		MaterialOrderDetail materialOrderDetail = new MaterialOrderDetail();
		materialOrderDetail.setName("TEST");
		materialOrderDetail.setNum("00001");
		materialOrderDetail.setModel("");
		materialOrderDetail.setUnit("");
		materialOrderDetails.add(materialOrderDetail);
		materialOrderDetail.setAmount(BigDecimal.valueOf(1));
		materialOrderDetail.setPrice(BigDecimal.valueOf(0));
		materialOrderDetail.setMoney(BigDecimal.valueOf(0));
		materialOrder.setMaterialOrderDetails(materialOrderDetails);
		materialOrderService.exportMaterial(materialOrder);
	}
}
