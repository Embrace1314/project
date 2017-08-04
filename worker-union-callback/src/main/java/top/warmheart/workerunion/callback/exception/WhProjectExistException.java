/**
 * Copyright © WarmHeart Intelligence Science&Technology(NanJing) Company, Limited.
 * All Rights Reserved
 */
package top.warmheart.workerunion.callback.exception;

@SuppressWarnings("serial")
public class WhProjectExistException extends Exception {

	public WhProjectExistException() {
		super();
	}

	public WhProjectExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public WhProjectExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public WhProjectExistException(String message) {
		super(message);
	}

	public WhProjectExistException(Throwable cause) {
		super(cause);
	}


}
