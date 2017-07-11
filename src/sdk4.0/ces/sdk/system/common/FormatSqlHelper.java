package ces.sdk.system.common;


public class FormatSqlHelper {
	
	public static final String formatSql(String sql, Object...params) {
		if(sql.endsWith("?")){ //修复最后一个字符是?号, 分割之后只有一个元素的数组的问题
			sql += " ";
		}
		String[] sqlArray = sql.split("\\?");
		StringBuilder formatSql = new StringBuilder();
		for (int i = 0; i < sqlArray.length; i++) {
			if( i == sqlArray.length - 1){
				formatSql.append(sqlArray[i]);
			} else {
				formatSql.append(sqlArray[i]).append("'").append(params[i]).append("'"); 
			}
		}
		return formatSql.toString();
	}
}
