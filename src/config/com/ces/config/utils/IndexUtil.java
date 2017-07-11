/**
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * qiucs 2015-2-10 下午5:18:09
 * <p>描述: TODO(用一句话描述该文件做什么)</p>
 */
package com.ces.config.utils;

/**
 * @author qiucs
 *
 */
public class IndexUtil {
	
	private static boolean exists(String tableId, String columnName) {
		
		return false;
	}
	
	public static String getIdColumn(String tableId) {
		return ConstantVar.ColumnName.ID;
	}

	public static String getCreateTimeColumn(String tableId) {
		return ConstantVar.ColumnName.CREATE_TIME;
	}

	public static String getUpdateTimeColumn(String tableId) {
		return ConstantVar.ColumnName.UPDATE_TIME;
	}

	public static String getDeleteTimeColumn(String tableId) {
		return ConstantVar.ColumnName.DELETE_TIME;
	}

	public static String getIsDeleteColumn(String tableId) {
		return ConstantVar.ColumnName.IS_DELETE;
	}

	public static String getDeleteLogTable() {
		return "T_XTPZ_INDEX_DELETE_LOG";
	}
}
