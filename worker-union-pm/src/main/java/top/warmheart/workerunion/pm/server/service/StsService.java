package top.warmheart.workerunion.pm.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import top.warmheart.workerunion.server.model.Staff;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;

/**
 * 阿里云STS服务
 * 
 * @author seulad
 *
 */
public class StsService {
	private static final Logger LOG = LoggerFactory.getLogger(StsService.class);
	/** 密钥ID */
	private String accessKeyId;
	/** 密钥密文 */
	private String accessKeySecret;
	/** 角色的全局资源描述符 */
	private String roleArn;
	/** 可用区ID */
	private String regionId;
	/** STS版本号 */
	private String version;
	/** OSS-Bucket名称 */
	private String ossBucketName;

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getAccessKeySecret() {
		return accessKeySecret;
	}

	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}

	public String getRoleArn() {
		return roleArn;
	}

	public void setRoleArn(String roleArn) {
		this.roleArn = roleArn;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getOssBucketName() {
		return ossBucketName;
	}

	public void setOssBucketName(String ossBucketName) {
		this.ossBucketName = ossBucketName;
	}

	/**
	 * 获取上传权限
	 * 
	 * @param staff
	 * @return
	 * @throws ClientException
	 */
	public AssumeRoleResponse uploadRequest(Staff staff, String[] paths) throws ClientException {
		/*
		 * 组织policy的JSON格式
		 */
		JSONObject json = new JSONObject();
		json.put("Version", "1");
		JSONArray jsonStatements = new JSONArray();
		JSONObject jsonStatement = new JSONObject();
		jsonStatement.put("Action", new String[]{"oss:PostObject", "oss:PutObject"});
		jsonStatement.put("Effect", "Allow");
		JSONArray jsonResource = new JSONArray();
		for (String path : paths) {
			jsonResource.add("acs:oss:*:*:" + ossBucketName + path);
		}
		jsonStatement.put("Resource", jsonResource);
		jsonStatements.add(jsonStatement);
		json.put("Statement", jsonStatements);
		LOG.debug(json.toString());
		return assumeRole(staff.getCompanyId() + "_" + staff.getId(), json.toString());
	}
	
	/**
	 * 获取上传权限
	 * 
	 * @param staff
	 * @return
	 * @throws ClientException
	 */
	public AssumeRoleResponse downloadRequest(Staff staff, String[] paths) throws ClientException {
		/*
		 * 组织policy的JSON格式
		 */
		JSONObject json = new JSONObject();
		json.put("Version", "1");
		JSONArray jsonStatements = new JSONArray();
		JSONObject jsonStatement = new JSONObject();
		jsonStatement.put("Action", "oss:GetObject");
		jsonStatement.put("Effect", "Allow");
		JSONArray jsonResource = new JSONArray();
		for (String path : paths) {
			jsonResource.add("acs:oss:*:*:" + ossBucketName + path);
		}
		jsonStatement.put("Resource", jsonResource);
		jsonStatements.add(jsonStatement);
		json.put("Statement", jsonStatements);
		return assumeRole(staff.getCompanyId() + "_" + staff.getId(), json.toString());
	}

	/**
	 * 扮演角色
	 * 
	 * @param roleSessionName
	 * @param policy
	 * @return
	 * @throws ClientException
	 */
	private AssumeRoleResponse assumeRole(String roleSessionName, String policy) throws ClientException {
		// 创建一个 Aliyun Acs Client, 用于发起 OpenAPI 请求
		IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
		DefaultAcsClient client = new DefaultAcsClient(profile);

		// 创建一个 AssumeRoleRequest 并设置请求参数
		final AssumeRoleRequest request = new AssumeRoleRequest();
		request.setVersion(version);
		request.setMethod(MethodType.POST);
		request.setProtocol(ProtocolType.HTTPS);
		request.setRoleArn(roleArn);
		request.setRoleSessionName(roleSessionName);
		request.setPolicy(policy);

		// 发起请求，并得到response
		final AssumeRoleResponse response = client.getAcsResponse(request);
		return response;
	}
}
