package ces.sdk.system.dbfacade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ces.sdk.sdk.db.SelfMaintainIdGenerator;
import ces.sdk.system.bean.OpLogInfo;
import ces.sdk.system.common.CommonConst;
import ces.sdk.util.JdbcUtil;
import ces.sdk.system.exception.SystemFacadeException;
import ces.sdk.system.facade.OpLogInfoFacade;
import ces.sdk.system.factory.EntityFactory;
import ces.sdk.util.ConnectionUtil;
import ces.sdk.util.CreateChartUtil;
import ces.sdk.util.PropertyUtil;
import ces.sdk.util.StringUtil;
import ces.sdk.util.Util;

public class DBOpLogInfoFacade extends BaseFacade implements OpLogInfoFacade {
	/** * 日志 */
	private final Log log = LogFactory.getLog(DBOpLogInfoFacade.class);

	@Override
	public int addOpLogInfo(OpLogInfo opLogInfo) throws SystemFacadeException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int addSysLogInfo(OpLogInfo opLogInfo) throws SystemFacadeException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<OpLogInfo> queryOpLogInfo(Map<String, String> map,
			int currentPage, int pageSize) throws SystemFacadeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OpLogInfo> querySysLogInfo(Map<String, String> map,
			int currentPage, int pageSize) throws SystemFacadeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int exportOpLogs(HttpServletResponse response, String log_date_end)
			throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int exportSysLogs(HttpServletResponse response, String log_date_end)
			throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Integer getTotalNum4QueryOpLogInfo(Map<String, String> map)
			throws SystemFacadeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getTotalNum4QuerySysLogInfo(Map<String, String> map)
			throws SystemFacadeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteSysLogInfoByIdBatch(String sysLogId)
			throws SystemFacadeException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteOpLogInfoByIdBatch(String opLogId)
			throws SystemFacadeException {
		// TODO Auto-generated method stub
		
	}
	

}