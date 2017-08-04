/**
 * Copyright © WarmHeart Intelligence Science&Technology(NanJing) Company, Limited.
 * All Rights Reserved
 */
package top.warmheart.workerunion.fm.server.action;

import java.math.BigInteger;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Scope;

import top.warmheart.server.util.WhStringUtil;
import top.warmheart.server.util.dto.Page;
import top.warmheart.server.util.exception.WhNull;
import top.warmheart.workerunion.fm.server.action.ex.ActionJson;
import top.warmheart.workerunion.fm.server.constant.SessionKey;
import top.warmheart.workerunion.fm.server.util.WhOwnerShipUtil;
import top.warmheart.workerunion.server.dto.CertificateDto;
import top.warmheart.workerunion.server.exception.WhInvalidStaffException;
import top.warmheart.workerunion.server.model.Certificate;
import top.warmheart.workerunion.server.model.CertificateAttachment;
import top.warmheart.workerunion.server.model.CertificateSerie;
import top.warmheart.workerunion.server.model.CertificateType;
import top.warmheart.workerunion.server.model.Staff;
import top.warmheart.workerunion.server.service.CertificateService;
import top.warmheart.workerunion.server.service.StaffService;

import com.alibaba.fastjson.JSONObject;

/**
 * 企业员工
 * 
 * @author seulad
 *
 */
@SuppressWarnings("serial")
@Scope("prototype")
public class CertificateAction extends ActionJson {
	private Page<Void> page;
	private Certificate certificate;
	private CertificateSerie certificateSerie;
	private CertificateDto certificateDto;
	private Staff staff;
	private StaffService staffService;
	private CertificateService certificateService;
	private CertificateAttachment certificateAttachment;

	public CertificateAttachment getCertificateAttachment() {
		return certificateAttachment;
	}

	public void setCertificateAttachment(CertificateAttachment certificateAttachment) {
		this.certificateAttachment = certificateAttachment;
	}

	public Page<Void> getPage() {
		return page;
	}

	public void setPage(Page<Void> page) {
		this.page = page;
	}

	public Certificate getCertificate() {
		return certificate;
	}

	public void setCertificate(Certificate certificate) {
		this.certificate = certificate;
	}

	public CertificateSerie getCertificateSerie() {
		return certificateSerie;
	}

	public void setCertificateSerie(CertificateSerie certificateSerie) {
		this.certificateSerie = certificateSerie;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public CertificateDto getCertificateDto() {
		return certificateDto;
	}

	public void setCertificateDto(CertificateDto certificateDto) {
		this.certificateDto = certificateDto;
	}

	public StaffService getStaffService() {
		return staffService;
	}

	@Resource(name = "staffService")
	public void setStaffService(StaffService staffService) {
		this.staffService = staffService;
	}

	public CertificateService getCertificateService() {
		return certificateService;
	}

	@Resource(name = "certificateService")
	public void setCertificateService(CertificateService certificateService) {
		this.certificateService = certificateService;
	}

	/**
	 * 增加证书信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String add() throws Exception {
		WhNull.check(certificate);
		WhNull.check(staff);
		WhNull.checkTrimEmpty(staff.getJobNo());
		WhNull.checkTrimEmpty(staff.getIdCardNo());
		WhNull.check(certificate.getCertificateTypeId());
		WhNull.checkTrimEmpty(certificate.getNum());
		WhNull.check(certificate.getValidDate());
		staff.setJobNo(WhStringUtil.trimAll(staff.getJobNo()));
		staff.setIdCardNo(WhStringUtil.trimAll(staff.getIdCardNo()));
		certificate.setNum(WhStringUtil.trimAll(certificate.getNum()));

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		/*
		 * 校验用户
		 */
		Staff staffCheck = staffService.getByJobNo(staffEx.getCompanyId(), staff.getJobNo());
		WhOwnerShipUtil.checkStaffCompany(staffCheck, staffEx);
		if (!staffCheck.getIdCardNo().equalsIgnoreCase(WhStringUtil.trimAll(staff.getIdCardNo()))) {
			throw new WhInvalidStaffException();
		}

		/*
		 * 校验证书类型
		 */
		CertificateType certificateTypeCheck = certificateService.getTypeById(certificate.getCertificateTypeId());
		WhOwnerShipUtil.checkCertificate(certificateTypeCheck);

		certificate.setCompanyId(staffEx.getCompanyId());
		certificate.setStaffId(staffCheck.getId());
		certificate.setCertificateTypeName(certificateTypeCheck.getName());
		certificate.setCertificateSerieId(certificateTypeCheck.getCertificateSerieId());
		certificate.setCertificateSerieName(certificateTypeCheck.getCertificateSerieName());

		BigInteger id = certificateService.add(certificate);

		JSONObject json = getSuccessJsonTemplate();
		json.put("id", id);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 修改证书信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String modifyById() throws Exception {
		WhNull.check(certificate);
		WhNull.check(certificate.getId());
		WhNull.checkTrimEmpty(certificate.getNum());
		WhNull.check(certificate.getValidDate());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Certificate certificateCheck = certificateService.getById(certificate.getId());
		WhOwnerShipUtil.checkStaffCompany(certificateCheck, staffEx);

		// 整理数据
		certificateCheck.setNum(certificate.getNum());
		certificateCheck.setValidDate(certificate.getValidDate());

		// 修改证书信息
		certificateService.modifyById(certificateCheck);

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 根据模糊条件查询证书信息，分页
	 * 
	 * @return
	 * @throws Exception
	 */
	public String pageByFuzzy() throws Exception {
		WhNull.check(certificateDto);
		WhNull.check(certificateDto.getStaffIdCardNo());
		WhNull.check(certificateDto.getStaffName());
		WhNull.check(certificateDto.getNum());
		WhNull.check(certificateDto.getCertificateTypeName());
		WhNull.checkPage(page);

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		WhOwnerShipUtil.checkStaff(staffEx);

		certificateDto.setCompanyId(staffEx.getCompanyId());
		Page<CertificateDto> pageCheck = certificateService.pageByFuzzy(certificateDto, page);

		JSONObject json = getSuccessJsonTemplate();
		json.put("page", pageCheck);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 删除证书信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String removeById() throws Exception {
		WhNull.check(certificate);
		WhNull.check(certificate.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		Certificate certificateCheck = certificateService.getById(certificate.getId());
		WhOwnerShipUtil.checkStaffCompany(certificateCheck, staffEx);

		// 删除证书信息
		certificateService.removeById(certificate.getId());
		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取证书信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getDetailById() throws Exception {
		WhNull.check(certificate);
		WhNull.check(certificate.getId());

		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		CertificateDto certificateCheck = certificateService.getDetailById(certificate.getId());
		WhOwnerShipUtil.checkStaffCompany(certificateCheck, staffEx);

		List<CertificateAttachment> certificateAttachments = certificateService
				.listAttachmentByCertificateId(certificate.getId());

		JSONObject json = getSuccessJsonTemplate();
		json.put("certificate", certificateCheck);
		json.put("certificateAttachments", certificateAttachments);

		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取所有的证书系列列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String listSerie() throws Exception {
		List<CertificateSerie> certificateSeries = certificateService.listSerie();

		JSONObject json = getSuccessJsonTemplate();
		json.put("certificateSeries", certificateSeries);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 获取证书系列下的证书类型列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String listTypeBySerieId() throws Exception {
		WhNull.check(certificateSerie);
		WhNull.check(certificateSerie.getId());

		List<CertificateType> certificateTypes = certificateService.listTypeBySerieId(certificateSerie.getId());

		JSONObject json = getSuccessJsonTemplate();
		json.put("certificateTypes", certificateTypes);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 删除证书附件
	 * 
	 * @return
	 * @throws Exception
	 */
	public String removeAttachmentById() throws Exception {
		WhNull.check(certificateAttachment);
		WhNull.check(certificateAttachment.getId());

		/*
		 * 校验合同附件信息
		 */
		Staff staffEx = (Staff) SecurityUtils.getSubject().getSession().getAttribute(SessionKey.KEY_STAFF);
		CertificateAttachment certificateAttachmentCheck = certificateService.getAttachmentById(certificateAttachment
				.getId());
		WhOwnerShipUtil.checkStaffCompany(certificateAttachmentCheck, staffEx);

		// 删除合同附件
		certificateService.removeAttachmentById(certificateAttachment.getId());

		JSONObject json = getSuccessJsonTemplate();
		writeStream(json);
		return SUCCESS;
	}
}
