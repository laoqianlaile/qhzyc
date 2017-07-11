package com.ces.component.trace.utils;

import java.util.ArrayList;
import java.util.List;

import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.config.utils.CodeUtil;

/**
 * 获取相应的表名
 * Created by 黄翔宇 on 15/5/14.
 */
public class TableNameUtil {

	/**
	 * 根据系统缩写返回企业档案表名
	 * @return
	 */
	public static String getQydaTableName(String sysName) {
		if ("HQFX".equalsIgnoreCase(sysName) || "ZL".equalsIgnoreCase(sysName) || "QYPTHT".equalsIgnoreCase(sysName) || "QYPTQT".equalsIgnoreCase(sysName) || "CSGL".equalsIgnoreCase(sysName)) {
			return null;
		}
		if (sysName.equalsIgnoreCase("YZ")) {
			return "T_YZ_CDDA";
		} else if (sysName.equalsIgnoreCase("ZZ")) {
			return "T_ZZ_CDDA";
		} else if (sysName.equalsIgnoreCase("JG")) {
			return "T_JG_JGCDA";
		} else {
			return "T_" + sysName + "_QYDA";
		}
	}

	/**
	 * 根据系统缩写返回流水号编码表名
	 * @param sysName
	 * @return
	 */
	public static String getLshbmTableName(String sysName) {
		return "T_" + sysName + "_LSHBM";
	}

	/**
	 * 根据系统缩写返回流通卡管理表名
	 * @param sysName
	 * @return
	 */
	public static String getLtkglTableName(String sysName) {
		return "T_" + sysName + "_LTKGL";
	}

	/**
	 * 获取企业档案中门店名称的列名
	 * @param sysName
	 * @return
	 */
	public static String getMdmcColumnName(String sysName) {
		if ("PC".equalsIgnoreCase(sysName)) {
			return "PFSCMC";
		} else if ("TZ".equalsIgnoreCase(sysName)) {
			return "TZCMC";
		} else if ("PR".equalsIgnoreCase(sysName)) {
			return "PFSCMC";
		} else if ("LS".equalsIgnoreCase(sysName)) {
			return "LSSCMC";
		} else if ("ZZ".equalsIgnoreCase(sysName)) {
			return "QYMC";
		} else if ("YZ".equalsIgnoreCase(sysName)) {
			return "YZCMC";
		} else if ("JG".equalsIgnoreCase(sysName)) {
			return "JGCMC";
		} else if ("CS".equalsIgnoreCase(sysName)) {
			return "CSMC";
		}
		return "QYMC";
	}
	
	/**
	 * 获取所有系统企业档案的表名
	 * @return
	 */
	public static List<String> getAllSystemQydaTableName() {
		List<Code> codes = CodeUtil.getInstance().getCodeList("CSPTXTLX");
		List<String> tables = new ArrayList<String>();
		for (Code code : codes) {
			tables.add(getQydaTableName(code.getRemark()));
		}
		return tables;
	}

	/**
	 * 获取隶属行政区划代码字段名
	 * @return
	 */
	public static String getLsxzqhdmColumnName(String sysName) {
		return "LSXZQHDM";
	}

	/**
	 * 获取隶属行政区字段名
	 * @return
	 */
	public static String getLsxzqColumnName(String sysName) {
		return "LSXZQ";
	}

}
