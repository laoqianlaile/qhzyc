package ces.sdk.system.facade;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import ces.sdk.system.bean.OpLogInfo;
import ces.sdk.system.exception.SystemFacadeException;

/**
 * 日志操作代理接口.<br>
 * @author Administrator
 *
 */
public interface OpLogInfoFacade {
	/***************************************************************************
	 * 增加操作日志信息
	 * 
	 * @param opLogInfo 日志操作信息
	 * @param sfContext 系统访问上下文
	 * @return 0 增加成功；１ 增加失败
	 * @throws SystemFacadeException
	 */
	public int addOpLogInfo(OpLogInfo opLogInfo) throws SystemFacadeException;
	
	/***************************************************************************
	 * 增加系统日志信息
	 * 
	 * @param opLogInfo 系统日志信息
	 * @param sfContext 系统访问上下文
	 * @return 0 增加成功；１ 增加失败
	 * @throws SystemFacadeException
	 */
	public int addSysLogInfo(OpLogInfo opLogInfo) throws SystemFacadeException;

	/***************************************************************************
	 * 获得系统日志列表
	 * @param Map<String, String> map 需要封装的key: user_name、operate、log_date_start、log_date_end
	 * @param int currentPage 当前页(用于分页)
	 * @param int pageSize 每页显示的条数 
	 * <br>该接口用于分页查询日志信息,如果参数Map没有传,则分页检索所有操作日志,如果传了参数map,则按条件分页检索
	 * @return 检索的日志列表信息
	 * @throws SystemFacadeException
	 */
	public List<OpLogInfo> queryOpLogInfo(Map<String, String> map,int currentPage,int pageSize) throws SystemFacadeException;

	/***************************************************************************
	 * 获得系统日志列表
	 * @param Map<String, String> map 需要封装的key: user_name、operate、log_date_start、log_date_end
	 * @param int currentPage 当前页(用于分页)
	 * @param int pageSize 每页显示的条数 
	 * <br>该接口用于分页查询日志信息,如果参数Map没有传,则分页检索所有操作日志,如果传了参数map,则按条件分页检索
	 * @return 检索的日志列表信息
	 * @throws SystemFacadeException
	 */
	public List<OpLogInfo> querySysLogInfo(Map<String, String> map,int currentPage,int pageSize)throws SystemFacadeException;
	
	/***************************************************************************
	 * 导出指定日期的系统日志
	 * @param log_date_end 截至日期
	 * @param response 响应对象
	 * @return 0 导出成功 1 导出失败
	 * @throws SystemFacadeException
	 */
	public int exportOpLogs(HttpServletResponse response,String log_date_end) throws Exception ;
	
	/***************************************************************************
	 * 导出指定日期的登录日志
	 * @param log_date_end 截至日期
	 * @param response 响应对象
	 * @return 0 导出成功 1 导出失败
	 * @throws SystemFacadeException
	 */
	public int exportSysLogs(HttpServletResponse response,String log_date_end) throws Exception ;
	
	/**
	 * @param Map<String, String> map 需要封装的key: user_name、operate、log_date_start、log_date_end
	 * @return 返回查询的总条目数
	 * @throws SystemFacadeException
	 */
	public Integer getTotalNum4QueryOpLogInfo(Map<String, String> map)throws SystemFacadeException;
	
	/**
	 * @param Map<String, String> map 需要封装的key: user_name、operate、log_date_start、log_date_end
	 * @return 返回查询的总条目数
	 * @throws SystemFacadeException
	 */
	public Integer getTotalNum4QuerySysLogInfo(Map<String, String> map)throws SystemFacadeException;
	
	/**
	 * 根据id删除系统日志
	 * @throws SystemFacadeException
	 */
	public void deleteSysLogInfoByIdBatch(String sysLogId)throws SystemFacadeException;	
	
	/**
	 * 根据id删除系统日志
	 * @throws SystemFacadeException
	 */
	public void deleteOpLogInfoByIdBatch(String opLogId)throws SystemFacadeException;
}