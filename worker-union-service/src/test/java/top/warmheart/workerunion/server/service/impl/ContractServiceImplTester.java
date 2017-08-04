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
import top.warmheart.workerunion.server.dto.ContractDto;
import top.warmheart.workerunion.server.model.Contract;
import top.warmheart.workerunion.server.model.ContractAttachment;
import top.warmheart.workerunion.server.service.ContractService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
@Transactional
public class ContractServiceImplTester {
	private ContractService contractService;

	public ContractService getContractService() {
		return contractService;
	}

	@Autowired
	public void setContractService(ContractService contractService) {
		this.contractService = contractService;
	}

	@Test
	public void testGetById() {
		assertNotNull(contractService.getById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testListSimpleItemByProjectId() {
		assertEquals(1, contractService.listSimpleItemByProjectId(BigInteger.valueOf(1L)).size());
	}

	@Test
	public void testGetByCompanyAndNum() {
		assertNotNull(contractService.getByCompanyAndNum(BigInteger.valueOf(1L), "1"));
	}

	@Test
	public void testAdd() {
		Contract contract = contractService.getById(BigInteger.valueOf(1L));
		contract.setNum("TEST001");
		assertTrue(contractService.add(contract).compareTo(BigInteger.valueOf(0L)) > 0);
	}

	@Test
	public void testRemoveById() {
		assertEquals(1, contractService.removeById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testGetDetailById() {
		assertNotNull(contractService.getDetailById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testGetAttachmentById() {
		assertNotNull(contractService.getAttachmentById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testRemoveAttachmentById() {
		assertEquals(1, contractService.removeAttachmentById(BigInteger.valueOf(1L)));
	}

	@Test
	public void testModifyById() {
		Contract contract = contractService.getById(BigInteger.valueOf(1L));
		contract.setContentAbstract("");
		assertEquals(1, contractService.modifyById(contract));
	}

	@Test
	public void testPageByFuzzy() {
		ContractDto contractDto = new ContractDto();
		contractDto.setCompanyId(BigInteger.valueOf(1L));
		assertEquals(1, contractService.pageByFuzzy(contractDto, new Page<Void>(12, 2)).getList().size());
	}

	@Test
	public void testAddAttachment() {
		ContractAttachment attachment = contractService.getAttachmentById(BigInteger.valueOf(1L));
		assertEquals(1, contractService.addAttachment(attachment));
	}
	
	@Test
	public void testListAttachmentByContractId() {
		assertEquals(1, contractService.listAttachmentByContractId(BigInteger.valueOf(1L)).size());
	}
}
