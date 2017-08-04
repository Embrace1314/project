/**
 * Copyright © WarmHeart Intelligence Science&Technology(NanJing) Company, Limited.
 * All Rights Reserved
 */
package top.warmheart.workerunion.callback.exception;

@SuppressWarnings("serial")
public class WhInvalidStaffException extends Exception {

	public WhInvalidStaffException() {
		super();
	}

	public WhInvalidStaffException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public WhInvalidStaffException(String message, Throwable cause) {
		super(message, cause);
	}

	public WhInvalidStaffException(String message) {
		super(message);
	}

	public WhInvalidStaffException(Throwable cause) {
		super(cause);
	}


}
