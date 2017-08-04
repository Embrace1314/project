package top.warmheart.workerunion.fm.server.util;

import java.util.Date;
import java.util.Random;

import top.warmheart.server.util.WhDateUtil;
import top.warmheart.workerunion.fm.server.constant.Config;

public class WhBatchNoUtil {
	public static String headquartersGen() {
		return (Config.MATERIAL_HEADQUARTERS_ORDER_AUTO_BATCH_NO_PREFIX + gen()).toUpperCase();
	}

	public static String projectGen() {
		return (Config.MATERIAL_ORDER_AUTO_BATCH_NO_PREFIX + gen()).toUpperCase();
	}

	/**
	 * 产生批次号
	 * @return
	 */
	private static String gen() {
		return (WhDateUtil.toTimestampStr(new Date()) + random(10)).toUpperCase();
	}
	
	/**
	 * 随机数，数字
	 * @param length
	 * @return
	 */
	private static String random(int length) {
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; ++i) {
			int number = random.nextInt(10);
			sb.append(number);
		}
		return sb.toString();
	}
}
