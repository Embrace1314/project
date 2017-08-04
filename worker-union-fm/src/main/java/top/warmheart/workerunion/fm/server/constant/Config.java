package top.warmheart.workerunion.fm.server.constant;

public class Config {
	/**项目组材料批次号自动生成前缀*/
	public static final String MATERIAL_ORDER_AUTO_BATCH_NO_PREFIX = "B";
	/**总材料批次号自动生成前缀*/
	public static final String MATERIAL_HEADQUARTERS_ORDER_AUTO_BATCH_NO_PREFIX = "HQB";
	/** OSS Bucket的域名后缀 */
	public static String OSS_URL_END_POINT;
	/** OSS Bucket名称 */
	public static String OSS_BUCKET_NAME;
	/** OSS 临时授权有效时间 */
	public static long OSS_VALID_DUARATION;
	/** OSS 上传反馈地址 */
	public static String OSS_CALLBACK_URL_BASE;
	/** OSS Bucket的域名 */
	public static String OSS_URL;
	
	public static String getOSS_URL_END_POINT() {
		return OSS_URL_END_POINT;
	}

	public static void setOSS_URL_END_POINT(String oSS_URL_END_POINT) {
		OSS_URL_END_POINT = oSS_URL_END_POINT;
	}

	public static String getOSS_BUCKET_NAME() {
		return OSS_BUCKET_NAME;
	}

	public static void setOSS_BUCKET_NAME(String oSS_BUCKET_NAME) {
		OSS_BUCKET_NAME = oSS_BUCKET_NAME;
	}

	public static long getOSS_VALID_DUARATION() {
		return OSS_VALID_DUARATION;
	}

	public static void setOSS_VALID_DUARATION(long oSS_VALID_DUARATION) {
		OSS_VALID_DUARATION = oSS_VALID_DUARATION;
	}

	public static String getOSS_CALLBACK_URL_BASE() {
		return OSS_CALLBACK_URL_BASE;
	}

	public static void setOSS_CALLBACK_URL_BASE(String oSS_CALLBACK_URL_BASE) {
		OSS_CALLBACK_URL_BASE = oSS_CALLBACK_URL_BASE;
	}

	public static String getOSS_URL() {
		return OSS_URL;
	}

	public static void setOSS_URL(String oSS_URL) {
		OSS_URL = oSS_URL;
	}

}
