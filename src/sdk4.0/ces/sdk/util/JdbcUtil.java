package ces.sdk.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/**
 * jdbc 实体工具类
 * 
 * <p>描述: jdbc 实体工具类</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
 * @date 2015-6-9 21:01:20
 * @version 1.0.2015.0609
 */
public class JdbcUtil {
	
	private static Log log = LogFactory.getLog(JdbcUtil.class);

	/**
	 * 关闭结果集
	 * @param resultSet 结果集
	 */
	public static void closeResultSet(ResultSet resultSet) {
		try {
			if (resultSet != null) {
				resultSet.close();
			}
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	/**
	 * 关闭preparedStatement
	 * @param preparedStatement preparedStatement
	 */
	public static void closePreparedStatement(
			PreparedStatement preparedStatement) {
		try {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}

	/**
	 * 关闭数据库连接
	 * @param connection 数据库连接
	 */
	public static void closeConnection(Connection connection) {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭所有连接
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月10日 上午10:21:46
	 * @param con
	 * @param preparedStatement
	 * @param resultSet
	 */
	public static void close(Connection con,PreparedStatement preparedStatement,ResultSet resultSet){
		JdbcUtil.closeResultSet(resultSet);
		JdbcUtil.closePreparedStatement(preparedStatement);
		JdbcUtil.closeConnection(con);
	}
	
	
	/**
	 * 根据jdbc查询结果集构建 bean对象
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @param <T>
	 * @comments:<p> 根据jdbc查询结果集构建 组织类型对象</p>
	 * @date 2015年6月2日 上午10:58:20
	 * @param bean 对象
	 * @param resultSet
	 * @return 实体对象
	 * @throws Exception
	 */
	public static <T> T setBeanByResultSet(Class<T> clazz, ResultSet resultSet) throws Exception{
		ResultSetMetaData metaData =  resultSet.getMetaData();
		int colsSize = metaData.getColumnCount();
		Method[] methods = clazz.getMethods();
		T bean = clazz.newInstance();
		for (Method method : methods) {
			if(!method.getName().startsWith("set")){
				continue;
			}
			for (int i = 0; i < colsSize; i++) {
				String columnName = metaData.getColumnName(i+1);

				String setMethodName = "set"+columnName;
				if(setMethodName.equalsIgnoreCase(method.getName())){
					Class<?> fieldClass = method.getParameterTypes()[0];
					if(fieldClass.equals(String.class)){
						method.invoke(bean, resultSet.getString(columnName));
					}
					else if(fieldClass.equals(int.class) || fieldClass.equals(Integer.class)){
						method.invoke(bean, resultSet.getInt(columnName));
					}
					else if(fieldClass.equals(long.class) || fieldClass.equals(Long.class)){
						method.invoke(bean, resultSet.getLong(columnName));
					}
					else if(fieldClass.equals(double.class) || fieldClass.equals(Double.class)){
						method.invoke(bean, resultSet.getDouble(columnName));
					}
					break;
				}
			}
		}
		return bean;
	}
	
	/**
	 * 根据jdbc查询结果集构建 bean集合对象
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> 根据jdbc查询结果集构建 组织类型对象</p>
	 * @date 2015年6月2日 上午10:58:20
	 * @param bean 对象
	 * @param resultSet
	 * @return 实体对象集合
	 * @throws Exception
	 */
	public static <T> List<T> setBeanListByResultSet(Class<T> clazz, ResultSet resultSet) throws Exception{
		ResultSetMetaData metaData =  resultSet.getMetaData();
		int colsSize = metaData.getColumnCount();
		List<T> list = new ArrayList<T>(colsSize);
		Method[] methods = clazz.getMethods();
		while (resultSet.next()) {
			T bean = clazz.newInstance();
			for (Method method : methods) {
				if(!method.getName().startsWith("set")){
					continue;
				}
				for (int i = 0; i < colsSize; i++) {
					String columnName = metaData.getColumnName(i+1);
					
					String setMethodName = "set"+columnName;
					if(setMethodName.equalsIgnoreCase(method.getName())){
						Class<?> fieldClass = method.getParameterTypes()[0];
						if(fieldClass.equals(String.class)){
							method.invoke(bean, resultSet.getString(columnName));
						}
						else if(fieldClass.equals(int.class) || fieldClass.equals(Integer.class)){
							method.invoke(bean, resultSet.getInt(columnName));
						}
						else if(fieldClass.equals(long.class) || fieldClass.equals(Long.class)){
							method.invoke(bean, resultSet.getLong(columnName));
						}
						else if(fieldClass.equals(double.class) || fieldClass.equals(Double.class)){
							method.invoke(bean, resultSet.getDouble(columnName));
						}
						break;
					}
				}
			}
			list.add(bean);
		}
		return list;
	}
	
	/**
	 * 根据jdbc查询结果集构建 bean对象
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @param <T>
	 * @comments:<p> 根据jdbc查询结果集构建 组织类型对象</p>
	 * @date 2015年6月2日 上午10:58:20
	 * @param bean 对象
	 * @param resultSet
	 * @return 实体对象
	 * @throws Exception
	 */
	public static Object setBeanByResultSet(Object bean, ResultSet resultSet) throws Exception{
		ResultSetMetaData metaData =  resultSet.getMetaData();
		int colsSize = metaData.getColumnCount();
		Method[] methods = bean.getClass().getMethods();
		for (Method method : methods) {
			if(!method.getName().startsWith("set")){
				continue;
			}
			for (int i = 0; i < colsSize; i++) {
				String columnName = metaData.getColumnName(i+1);

				String setMethodName = "set"+columnName;
				if(setMethodName.equalsIgnoreCase(method.getName())){
					Class<?> fieldClass = method.getParameterTypes()[0];
					if(fieldClass.equals(String.class)){
						method.invoke(bean, resultSet.getString(columnName));
					}
					else if(fieldClass.equals(int.class) || fieldClass.equals(Integer.class)){
						method.invoke(bean, resultSet.getInt(columnName));
					}
					else if(fieldClass.equals(long.class) || fieldClass.equals(Long.class)){
						method.invoke(bean, resultSet.getLong(columnName));
					}
					else if(fieldClass.equals(double.class) || fieldClass.equals(Double.class)){
						method.invoke(bean, resultSet.getDouble(columnName));
					}
					break;
				}
			}
		}
		return bean;
	}
	
	/**
	 * 通过bean为preparedStatement设置参数, 支持新增和修改 (注意保持bean的字段顺序和要设置的参数顺序相同, bean的第一个字段必须是id)
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月9日 下午9:55:56
	 * @param preparedStatement
	 * @param bean
	 * @param isAdd
	 * @return
	 * @throws SQLException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static PreparedStatement setPreparedStatementByBean(PreparedStatement preparedStatement,Object bean,boolean isAdd ) throws SQLException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Field[] fieldsArray = bean.getClass().getDeclaredFields();
		List<Field> fields = new ArrayList<Field>(Arrays.asList(fieldsArray));
		//如果是修改,则将fields第一个字段(id)移除, 然后放到list的最后面
		if(!isAdd){
			Field idColumn = fields.remove(0);
			fields.add(idColumn);
		}
		int i = 1;
		for (Field field : fields) {
			Class<?> fieldType = field.getType();
			String getMethodName = "get" + field.getName().substring(0,1).toUpperCase() + field.getName().substring(1);
			Method getMethod = bean.getClass().getMethod(getMethodName);
			Object value = getMethod.invoke(bean, new Object[]{});
			if(fieldType.equals(String.class)){
				preparedStatement.setString(i, value == null ? null : value.toString());
			}
			else if(fieldType.equals(int.class) || fieldType.equals(Integer.class)){
				preparedStatement.setInt(i, value == null ? null : (Integer)value);
			}
			else if(fieldType.equals(long.class) || fieldType.equals(Long.class)){
				preparedStatement.setLong(i, value == null ? null : (Long)value);
			}
			else if(fieldType.equals(double.class) || fieldType.equals(Double.class)){
				preparedStatement.setDouble(i, value == null ? null : (Double)value);
			}
			i++;
		}
		return preparedStatement;
	}
	
	/**
	 * 根据给定的数据字段, 得到对应bean的get值
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月22日 下午4:36:58
	 * @param columns
	 * @param bean
	 * @return
	 */
	public static Object[] getColumnsValueByBean(String columns,Object bean,boolean isAdd){
		String[] columnsArray = columns.split(",");
		List<Object> columnsValue = new LinkedList<Object>();
		try {
			for (String column : columnsArray) {
				//将下划线的名称转为驼峰格式,并拼接get方法名 
				String getMethodName = "get"+CamelCaseUtils.toCapitalizeCamelCase(column);
				Method method = bean.getClass().getMethod(getMethodName);
				Object value = method.invoke(bean, new Object[]{});
				columnsValue.add(value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//如果是新增则直接返回, 如果是更新则将第一位的id的值放到最后
		if(isAdd){
			return columnsValue.toArray();
		} else {
			Object idValue = columnsValue.remove(0);
			columnsValue.add(idValue);
			return columnsValue.toArray();
		}
	}
	
	public static void setAutoCommit(Connection conn,boolean isAutoCommit){
		try {
			conn.setAutoCommit(isAutoCommit);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void rollback(Connection conn){
		try {
			conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 生成in语句后的占位符
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月5日 下午12:37:01
	 * @param sql
	 * @param idsArray
	 * @return
	 */
	public static  StringBuilder generatorPlaceHolder(Object[] paramArray){
		StringBuilder condition = new StringBuilder();
		int len = paramArray.length;
		if(len == 1){
			condition.append("(?)");
		} else {
			for (int i = 0; i < len; i++) {
				if(i == 0){
					condition.append("(?,");
				} 
				else if(i > 0 && i < len -1){
					condition.append("?,");
				} 
				else if(i == len -1){
					condition.append("?)");
				}
			}
		}
		return condition;
	} 
	
	/**
	 * 根据字段名称拼接出select语句和update语句
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月23日 下午5:14:48
	 * @param columnsNameArray 字段名数组
	 * @return 拼接好的select语句和update语句, 封装为数组.第一个元素是select语句, 第二个元素是update语句
	 * 
	 */
	public static String[] getSelectParamAndUpdateParam(String[] columnsNameArray){
		
		StringBuilder selectParam = new StringBuilder();
		StringBuilder updateParam = new StringBuilder();
		for (int i = 0; i < columnsNameArray.length; i++) {
			selectParam.append(columnsNameArray[i] + " as "+CamelCaseUtils.toCamelCase(columnsNameArray[i])+",");
			if(i == columnsNameArray.length -1){
				selectParam.deleteCharAt(selectParam.length()-1);
				updateParam.deleteCharAt(updateParam.length()-1);
			} else {
				updateParam.append(columnsNameArray[i+1] + " = ?,");
			}
		}
		String[] params = new String[2];
		params[0] = selectParam.toString();
		params[1] = updateParam.toString();
		
		return params;
	}
}
