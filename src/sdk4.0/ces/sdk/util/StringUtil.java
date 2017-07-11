package ces.sdk.util;


/**
 * sdk 字符串工具类
 * 
 * <p>描述:</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
 * @date 2015年6月9日 下午9:10:26
 * @version 1.0.2015.0601
 */
public class StringUtil {

	public static final String RETURN_AND_NEWLINE = "RETURN_AND_NEWLINE";
	
	public static boolean isNotBlank(String srcStr) {
		return srcStr != null && srcStr.trim().length() > 0;
	}

	public static boolean isBlank(String srcStr) {
		return srcStr == null || srcStr.trim().length() == 0;
	}
	
	
	public static String replaceAll(String source, String finder,
			String replacement) {
		if (source == null || source.equals("")) {
			return source;
		}
		if (finder == null || finder.equals("")) {
			finder = " ";
		}
		String str0 = source;
		String target = "";
		try {
			if (finder == StringUtil.RETURN_AND_NEWLINE) {
				while (str0.indexOf("\r") != -1) {
					target += str0.substring(0, str0.indexOf("\r"))
							+ replacement;
					str0 = str0.substring(str0.indexOf("\r") + 2);
				}
				/*
				 * while (str0.indexOf("\n") != -1) { target +=
				 * str0.substring(0, str0.indexOf("\n") - 1) + replacement; str0 =
				 * str0.substring(str0.indexOf("\n") + 1); }
				 */
			} else {
				while (str0.indexOf(finder) != -1) {
					target += str0.substring(0, str0.indexOf(finder))
							+ replacement;
					// replacement = replacement.trim();
					str0 = str0.substring(str0.indexOf(finder)
							+ finder.length());
				}
			}
			target += str0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (target.equals("")) {
			target = str0;
		}
		return target;
	}
}
