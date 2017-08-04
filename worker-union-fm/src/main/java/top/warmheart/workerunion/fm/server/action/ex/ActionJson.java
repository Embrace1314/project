/**
 * Copyright © WarmHeart Intelligence Science&Technology(NanJing) Company, Limited.
 * All Rights Reserved
 */
package top.warmheart.workerunion.fm.server.action.ex;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import top.warmheart.server.util.WhFastJsonUtil;
import top.warmheart.workerunion.server.constant.WebErrorEnum;

import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Action返回JSON格式，其中包含字段returnCode、errorCode、errorCodeDesc
 * returnCode:{SUCCESS,FAIL}
 * errorCode:错误代号
 * errorCodeDesc:错误代号描述
 * @author seulad
 *
 */
@SuppressWarnings("serial")
public class ActionJson extends ActionSupport {
	private InputStream inputStream;

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	/**
	 * 正确返回的JSON格式模板
	 * 
	 * @return
	 */
	protected JSONObject getSuccessJsonTemplate() {
		JSONObject json = new JSONObject();
		json.put("returnCode", "SUCCESS");
		return json;
	}

	/**
	 * 异常返回的JSON格式模板
	 * 
	 * @return
	 */
	protected JSONObject getErrorJsonTemplate() {
		JSONObject json = new JSONObject();
		json.put("returnCode", "FAIL");
		json.put(WebErrorEnum.CODE, WebErrorEnum.UNKNOWN_ERROR.errorCode);
		json.put(WebErrorEnum.CODE_DESC, WebErrorEnum.UNKNOWN_ERROR.errorCodeDesc);
		return json;
	}

	/**
	 * 将数据写入inputStream流中，返回客户端
	 * 
	 * @param content
	 *            输入内容
	 */
	protected void writeStream(String content) {
		try {
			inputStream = new ByteArrayInputStream(content.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// 返回错误代码
			LOG.error("Invoke method getBytes(String charsetName), contains unsupported parameter charsetName.");
			inputStream = new ByteArrayInputStream(getErrorJsonTemplate().toString().getBytes());
		}
	}

	/**
	 * 将数据写入inputStream流中，返回客户端
	 * 
	 * @param content
	 *            输入内容
	 */
	protected void writeStream(JSONObject json) {
		try {
			inputStream = new ByteArrayInputStream(JSONObject.toJSONString(json, WhFastJsonUtil.getConfigDatetime())
					.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// 返回错误代码
			LOG.error("Invoke method getBytes(String charsetName), contains unsupported parameter charsetName.");
			inputStream = new ByteArrayInputStream(getErrorJsonTemplate().toString().getBytes());
		}
	}
	

}


