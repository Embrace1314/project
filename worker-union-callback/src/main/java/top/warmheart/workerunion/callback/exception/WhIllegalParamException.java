/**
 * Copyright © WarmHeart Intelligence Science&Technology(NanJing) Company, Limited.
 * All Rights Reserved
 */
package top.warmheart.workerunion.callback.exception;

@SuppressWarnings("serial")
public class WhIllegalParamException extends Exception {

	public WhIllegalParamException() {
		super();
	}

	public WhIllegalParamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public WhIllegalParamException(String message, Throwable cause) {
		super(message, cause);
	}

	public WhIllegalParamException(String message) {
		super(message);
	}

	public WhIllegalParamException(Throwable cause) {
		super(cause);
	}


}
