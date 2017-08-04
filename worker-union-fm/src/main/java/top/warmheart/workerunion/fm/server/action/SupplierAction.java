/**
 * Copyright © WarmHeart Intelligence Science&Technology(NanJing) Company, Limited.
 * All Rights Reserved
 */
package top.warmheart.workerunion.fm.server.action;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;

import top.warmheart.server.util.dto.Page;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.fm.server.action.ex.ActionJson;
import top.warmheart.workerunion.fm.server.constant.SessionKey;
import top.warmheart.workerunion.fm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.dto.SupplierDto;
import top.warmheart.workerunion.server.exception.WhSupplierExistException;
import top.warmheart.workerunion.server.model.Staff;
import top.warmheart.workerunion.server.model.Supplier;
import top.warmheart.workerunion.server.model.SupplierAttachment;
import top.warmheart.workerunion.server.model.SupplierGrade;
import top.warmheart.workerunion.server.model.SupplierQualification;
import top.warmheart.workerunion.server.service.SupplierService;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("serial")
@Scope("prototype")
public class SupplierAction extends ActionJson {
	private Page<Void> page;
	private Supplier supplier;
	private SupplierDto supplierDto;
	private SupplierAttachment supplierAttachment;
	private SupplierService supplierService;
	private List<SupplierQualification> supplierQualifications;

	public SupplierDto getSupplierDto() {
		return supplierDto;
	}

	public void setSupplierDto(SupplierDto supplierDto) {
		this.supplierDto = supplierDto;
	}

	public Page<Void> getPage() {
		return page;
	}

	public void setPage(Page<Void> page) {
		this.page = page;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public SupplierAttachment getSupplierAttachment() {
		return supplierAttachment;
	}

	public void setSupplierAttachment(SupplierAttachment supplierAttachment) {
		this.supplierAttachment = supplierAttachment;
	}

	public List<SupplierQualification> getSupplierQualifications() {
		return supplierQualifications;
	}

	public void setSupplierQualifications(List<SupplierQualification> supplierQualifications) {
		this.supplierQualifications = supplierQualifications;
	}

	public SupplierService getSupplierService() {
		return supplierService;
	}

	@Resource(name = "supplierService")
	public void setSupplierService(SupplierService supplierService) {
		this.supplierService = supplierService;
	}

	/**
	 * 模糊查询获取供应商信息列表，分页
	 * 
	 * @return
	 * @throws Exception
	 */
	public String pageByFuzzy() throws Exception {
		WhNull.check(supplierDto);
		WhNull.check(supplierDto.getName());
		WhNull.check(supplierDto.getNum());
		WhNull.checkPage(page);
		
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);
		supplierDto.setCompanyId(staffEx.getCompanyId());
		
		Page<SupplierDto>pageCheck = supplierService.pageByFuzzy(supplierDto, page);
		
		JSONObject json = getSuccessJsonTemplate();
		json.put("page", pageCheck);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 添加供应商信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String add() throws Exception {
		WhNull.check(supplier);
		WhNull.checkTrimEmpty(supplier.getName());
		WhNull.checkTrimEmpty(supplier.getNum());
		WhNull.check(supplier.getSupplierGradeId());
		WhNull.check(supplier.getValidDate());
		WhNull.check(supplier.getContactPerson());
		WhNull.check(supplier.getContactPhone());
		if (null == supplierQualifications){
			supplierQualifications = new ArrayList<SupplierQualification>();
		}
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		/*
		 * 校验供应商级别
		 */
		SupplierGrade supplierGradeCheck = supplierService.getGradeById(supplier.getSupplierGradeId());
		WhOwnerShipUtil.checkSupplierGrade(supplierGradeCheck);

		/*
		 * 校验资质
		 */
		for (SupplierQualification supplierQualification : supplierQualifications) {
			SupplierQualification supplierQualificationCheck = supplierService
					.getQualificationById(supplierQualification.getId());
			WhOwnerShipUtil.checkSupplierQualification(supplierQualificationCheck);
		}

		/*
		 * 校验编号
		 */
		Supplier supplierExist = supplierService.getByCompanyAndNum(staffEx.getCompanyId(), supplier.getNum());
		if (null != supplierExist) {
			throw new WhSupplierExistException();
		}
		supplier.setCompanyId(staffEx.getCompanyId());
		supplier.setSupplierGradeName(supplierGradeCheck.getName());

		// 增加供应商信息
		BigInteger id = supplierService.add(supplier, supplierQualifications);

		JSONObject json = getSuccessJsonTemplate();
		json.put("id", id);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 删除供应商信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String removeById() throws Exception {
		WhNull.check(supplier);
		WhNull.check(supplier.getId());

		/*
		 * 校验供应商
		 */
		Supplier supplierCheck = supplierService.getById(supplier.getId());
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaffCompany(supplierCheck, staffEx);

		// 删除供应商信息
		supplierService.removeById(supplier.getId());

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 修改供应商信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String modifyById() throws Exception {
		WhNull.check(supplier);
		WhNull.check(supplier.getId());
		WhNull.checkTrimEmpty(supplier.getName());
		WhNull.check(supplier.getSupplierGradeId());
		WhNull.check(supplier.getValidDate());
		WhNull.check(supplier.getContactPerson());
		WhNull.check(supplier.getContactPhone());
		if (null == supplierQualifications){
			supplierQualifications = new ArrayList<SupplierQualification>();
		}
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);
		
		/*
		 * 校验供应商
		 */
		Supplier supplierCheck = supplierService.getById(supplier.getId());
		WhOwnerShipUtil.checkStaffCompany(supplierCheck, staffEx);

		/*
		 * 校验供应商级别
		 */
		SupplierGrade supplierGradeCheck = supplierService.getGradeById(supplier.getSupplierGradeId());
		WhOwnerShipUtil.checkSupplierGrade(supplierGradeCheck);

		/*
		 * 校验资质
		 */
		for (SupplierQualification supplierQualification : supplierQualifications) {
			SupplierQualification supplierQualificationCheck = supplierService
					.getQualificationById(supplierQualification.getId());
			WhOwnerShipUtil.checkSupplierQualification(supplierQualificationCheck);
		}

		supplierCheck.setName(supplier.getName());
		supplierCheck.setSupplierGradeId(supplier.getSupplierGradeId());
		supplierCheck.setSupplierGradeName(supplierGradeCheck.getName());
		supplierCheck.setValidDate(supplier.getValidDate());
		supplierCheck.setContactPerson(supplier.getContactPerson());
		supplierCheck.setContactPhone(supplier.getContactPhone());

		// 增加供应商信息
		supplierService.modifyById(supplierCheck, supplierQualifications);

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取供应商信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getDetailById() throws Exception {
		WhNull.check(supplier);
		WhNull.check(supplier.getId());

		/*
		 * 获取供应商
		 */
		Supplier supplierCheck = supplierService.getById(supplier.getId());
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaffCompany(supplierCheck, staffEx);
		
		/*
		 * 获取供应商资质列表
		 */
		List<SupplierQualification> supplierQualifications = supplierService.listQualificationBySupplierId(supplier.getId());
		
		/*
		 * 获取供应商信息附件
		 */
		List<SupplierAttachment> supplierAttachments = supplierService.listAttachmentBySupplierId(supplier.getId());
		
		JSONObject json = getSuccessJsonTemplate();
		json.put("supplier", supplierCheck);
		json.put("supplierQualifications", supplierQualifications);
		json.put("supplierAttachments", supplierAttachments);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 删除供应商信息附件文件
	 * 
	 * @return
	 * @throws Exception
	 */
	public String removeAttachmentById() throws Exception {
		WhNull.check(supplierAttachment);
		WhNull.check(supplierAttachment.getId());
		/*
		 * 获取供应商附件信息
		 */
		SupplierAttachment supplierAttachmentCheck = supplierService.getAttachmentById(supplierAttachment.getId());
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaffCompany(supplierAttachmentCheck, staffEx);
		
		supplierService.removeAttachmentById(supplierAttachment.getId());
		
		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取企业下的供应商级别列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String listGrade() throws Exception {
		List<SupplierGrade> supplierGrades = supplierService.listGrade();
		
		JSONObject json = getSuccessJsonTemplate();
		json.put("supplierGrades", supplierGrades);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取企业下的供应商资质列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String listQualification() throws Exception {
		List<SupplierQualification> supplierQualifications = supplierService.listQualification();
		
		JSONObject json = getSuccessJsonTemplate();
		json.put("supplierQualifications", supplierQualifications);
		writeStream(json);
		return SUCCESS;
	}

}
