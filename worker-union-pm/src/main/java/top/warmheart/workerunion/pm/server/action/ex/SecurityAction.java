/**
 * Copyright © WarmHeart Intelligence Science&Technology(NanJing) Company, Limited.
 * All Rights Reserved
 */
package top.warmheart.workerunion.pm.server.action.ex;

import org.springframework.context.annotation.Scope;

import top.warmheart.workerunion.server.constant.WebErrorEnum;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("serial")
@Scope("prototype")
public class SecurityAction extends ActionJson {

	/**
	 * 未登录，返回
	 * @return
	 * @throws Exception
	 */
	public String notLogin() throws Exception {
		JSONObject json = getErrorJsonTemplate();
		json.put(WebErrorEnum.CODE, WebErrorEnum.NOT_LOGIN.errorCode);
		json.put(WebErrorEnum.CODE_DESC, WebErrorEnum.NOT_LOGIN.errorCodeDesc);
		writeStream(json);
		return SUCCESS;
	}

	/**
	 * 成功登录，返回
	 * @return
	 * @throws Exception
	 */
	public String success() throws Exception {
		writeStream(getSuccessJsonTemplate());
		return SUCCESS;
	}

	/**
	 * 未授权，返回
	 * @return
	 * @throws Exception
	 */
	public String unauthorized() throws Exception {
		JSONObject json = getErrorJsonTemplate();
		json.put(WebErrorEnum.CODE, WebErrorEnum.UNAUTHORIZED.errorCode);
		json.put(WebErrorEnum.CODE_DESC, WebErrorEnum.UNAUTHORIZED.errorCodeDesc);
		writeStream(json);
		return SUCCESS;
	}
}
