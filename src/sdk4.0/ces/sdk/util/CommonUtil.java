package ces.sdk.util;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 通用方法
 * 
 * @author wanglei 2010-12-02
 * 
 */
public class CommonUtil {
	protected static Log log = LogFactory.getLog(StringUtil.class);
	/**
	 * 将数组填充到列表中
	 * 
	 * @param lis
	 * @param objs
	 * @return
	 */
	public static void arrayInList(List lis, Object[] objs) {
		if (objs == null)
			return;
		List lisObj = Arrays.asList(objs);
		lis.addAll(lisObj);
	}

	/**
	 * 判断当前值是否匹配值范围
	 * 
	 * @param optionValues
	 * @param value
	 * @return
	 */
	public static boolean isInOptionValues(String[] optionValues, String value) {
		Map valueMap = new HashMap(optionValues.length);
		for (int i = 0; i < optionValues.length; i++) {
			valueMap.put(optionValues[i], "");
		}
		return valueMap.get(value) != null;
	}

	/**
	 * 将字符串转为整数返回，转换异常返回0
	 * 
	 * @param s
	 * @return
	 * 
	 */
	public static Integer toInteger(String s) {
		try {
			if (s == null)
				return null;
			return new Integer(s);
		} catch (Throwable e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			return new Integer(0);
		}
	}
	/**
	 * 日期格式化 标准格式
	 */
	public static String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
//	private static SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh24:mm:ss");

	/**
	 * 将字符串转为日期型返回，转换异常返回null
	 * 
	 * @param s
	 * @return
	 */
	public static Date toDate(String s) {
		try {
			if (s == null)
				return null;
			return DateUtils.parseDate(s,new String []{DATE_FORMAT_PATTERN});
//			return sf.parse(s);
		} catch (Throwable e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 将Date类型的对象转化为String
	 * 
	 * @param date
	 * @return
	 */
	public static String dateTostr(Date date) {
		if (isNull(date))
			return "";
		return DateFormatUtils.format(date, DATE_FORMAT_PATTERN);
//		return sf.format(date);
	}

	/**
	 * 判断对象是否为空
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNull(Object obj) {
		return obj == null || "".equals(obj);
	}

	public static boolean isNull(List list) {
		return list.isEmpty();
	}

	/**
	 * 判断对象是否为非空
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNotNull(Object obj) {
		return !isNull(obj);
	}

	public static boolean isNotNull(List list) {
		return !isNull(list);
	}

	/**
	 * 首字母大写
	 * @param s
	 * @return
	 * @auth liuliang 2008-9-25
	 */
	public static String firstLetterUpperCase(String s) {
		if (s == null || s.length() < 1)
			return s;
		return s.toUpperCase().charAt(0) + s.substring(1);
	}

	/**
	 * 首字母小写
	 * @param s
	 * @return
	 */
	public static String firstLetterLowerCase(String s) {
		if (s == null || s.length() < 1)
			return s;
		return s.toLowerCase().charAt(0) + s.substring(1);
	}

	/**
	 * 转换数据类型
	 * @param obj
	 * @param clazz
	 * @return
	 */
	public static Object changeDataType(Object obj, Class clazz) {
		if (obj == null || clazz == null)
			return null;
		Class old = obj.getClass();
		if (old.equals(clazz))
			return obj;
		try {
			if (clazz.equals(String.class))
				return convertToString(obj);
			if (obj instanceof String) {
				String value = (String) obj;
				if (clazz.equals(Long.class))
					return new Long(value);
				if (clazz.equals(Integer.class))
					return new Integer(value);
				if (clazz.equals(Short.class))
					return new Short(value);
				if (clazz.equals(Float.class))
					return new Float(value);
				if (clazz.equals(Double.class))
					return new Double(value);
				if (clazz.equals(Date.class)) {
					Date date = null;
					date = DateFormat.getDateTimeInstance().parse(value);
					try {
						date = DateFormat.getDateTimeInstance().parse(value);
					} catch (ParseException e) {
						try {
							date = DateFormat.getDateInstance().parse(value);
						} catch (ParseException e1) {
							try {
								date = DateFormat.getTimeInstance()
										.parse(value);
							} catch (ParseException e2) {
								e2.printStackTrace();
								log.error(e2.getMessage(), e2);
							}
						}
					}
					return date;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		return obj;
	}

	/**
	 * 转换字符串
	 * @param obj
	 * @return
	 */
	public static String convertToString(Object obj) {
		if (obj == null)
			return "";
		else if (obj instanceof Number) {
			NumberFormat nf = NumberFormat.getInstance();
			nf.setGroupingUsed(false);
			nf.setMaximumFractionDigits(2);
			return nf.format(obj);
		} else if (obj instanceof Date) {
//			return sf.format(obj);
			return DateFormatUtils.format((Date)obj,DATE_FORMAT_PATTERN);
		}
		return obj.toString();
	}
	
	/**
	   * 字符串分割成字符数组
	   * @param s
	   * @param splitFlag
	   * @return
	   */
	  public static String[] stringParse(String s, String splitFlag) {
	    StringTokenizer stk = new StringTokenizer(s,
	        splitFlag);
	    List splitList = new ArrayList();
	    while (stk.hasMoreTokens()) {
	      splitList.add(stk.nextToken());
	    }
	    String[] splitArray = new String[splitList.size()];
	    for (int i = 0; i < splitArray.length; i++) {
	      splitArray[i] = (String) splitList.get(i);
	    }
	    return splitArray;
	  }

	  /**
	   * 判别一个字符串是否全为0
	   * @param s 待辨识的字符串
	   * @return boolean
	   */
	  public static boolean isZeroString(String s) {
	      if (s.equals("") || s == "" || s.length() == 0 || s == null) {
	          return false;
	      }
	      boolean tmp = false;
	      try {
	          if (new Integer(s).intValue() == 0) {
	              tmp = true;
	          }
	          else {
	              tmp = false;
	          }
	      } catch (NumberFormatException nfe) {
	          nfe.printStackTrace();
	          log.error(nfe.getMessage(), nfe);
	      }
	      return tmp;
	  }
	
}
