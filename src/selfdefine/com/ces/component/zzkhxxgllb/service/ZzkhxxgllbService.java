package com.ces.component.zzkhxxgllb.service;

import ces.sdk.system.bean.RoleInfo;
import ces.sdk.system.bean.SystemInfo;
import ces.sdk.system.bean.UserInfo;
import ces.sdk.system.factory.SystemFacadeFactory;
import ces.sdk.util.MD5;
import com.ces.component.trace.utils.CompanyInfoUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.management.StringValueExp;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
public class ZzkhxxgllbService extends
		TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

	private static final String tableName = "T_ZZ_KHXX";
	private static final String childtableName = "T_ZZ_KHMDXX";

	/**
	 * 默认权限过滤
	 * 
	 * @return
	 */
	public String defaultCode() {
		String code = SerialNumberUtil.getInstance().getCompanyCode();
		String defaultCode = " ";
		if (code != null && !"".equals(code))
			defaultCode = AppDefineUtil.RELATION_AND + " QYBM = '" + code
					+ "' and is_delete <> '1'";
		return defaultCode;
	}

	@Override
	protected String buildCustomerFilter(String tableId,
			String componentVersionId, String moduleId, String menuId,
			Map<String, Object> paramMap) {
		// 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
		return defaultCode();
	}

	/**
	 * 查重
	 */
	public Object validYhm(String yhm) {
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("loginName", yhm);
		List<UserInfo> dataList = CommonUtil.getUserInfoFacade()
				.findByCondition(dataMap);
		for (Iterator iterator = dataList.iterator(); iterator.hasNext();) {
			UserInfo user = (UserInfo) iterator.next();
			if ("1".equals(user.getStatus())) {
				iterator.remove();
			}
		}
		return dataList;
	}

	/**
	 * 保存客户信息
	 * 
	 * @param map
	 * @param tpFile
	 * @param tpFileFileName
	 * @param logoFile
	 * @param logoFileFileName
	 * @return
	 */
	@Transactional
	public Object saveKhxx(Map<String, String> map, String dentityJson,
			File tpFile, String tpFileFileName, File logoFile,
			String logoFileFileName) {
		map.put("QYBM", SerialNumberUtil.getInstance().getCompanyCode());
		String sql = "";

		/************** 添加用户 begin *****************/


		/************** 添加用户 end *****************/

		// 默认上传路径为spzstpfj
		String realpath = ServletActionContext.getServletContext().getRealPath(
				"/spzstpfj");
		// 文件重命名 图片上传的文件名称
		String tpfileNameWithStamp = System.currentTimeMillis() + "_"
				+ tpFileFileName;
		// 重命名 logo图片上传的文件名称
		String logofileNameWithStamp = System.currentTimeMillis() + "_"
				+ logoFileFileName;
		String id = map.get("ID") + "";
		Map<String, Object> objMap = new HashMap<String, Object>();
		boolean isNew = true;
		if (id != null && !"".equals(id) && (id.indexOf("tem") == -1)) {// 不是新增操作
			objMap = getObjKhxx(id);
			isNew = false;
		} else {
			map.put("ID", "");
		}
		boolean tpfileisExit = false;
		if (tpFile != null && tpFile.isFile()) {
			// 保存修改后的文件到数据库中
			map.put("TP", tpfileNameWithStamp);
			tpfileisExit = true;
		}
		boolean logofileisExit = false;
		if (logoFile != null && logoFile.isFile()) {
			// 保存修改后的文件到数据库中
			map.put("LOGOTP", logofileNameWithStamp);
			logofileisExit = true;
		}
		if (tpfileisExit) {// 图片进行替换操作
			// 进行图片的上传
			File desFile = new File(realpath + "/" + tpfileNameWithStamp);
			// 数据库中保存的文件名称
			String delFilePath = realpath + "/" + objMap.get("TP");
			File delFile = new File(delFilePath);
			// 执行文件上传和删除操作
			copyFile(tpFile, desFile, delFile);
		}

		if (logofileisExit) {// logo图片进行替换
			// 进行图片的上传
			File desFile = new File(realpath + "/" + logofileNameWithStamp);
			// 数据库中保存的文件名称
			String delFilePath = realpath + "/" + objMap.get("LOGOTP");
			File delFile = new File(delFilePath);
			// 执行文件上传和删除操作
			copyFile(logoFile, desFile, delFile);
		}
		String newid = saveOne(tableName, map);
		// if (isNew) {//是新增操作更新掉临时的ID
		// //更新关联字表信息
		// updateMdxxPid(id, newid);
		// }else{
		// 处理列表数据

		return getObjKhxx(newid);
	}

	/**
	 * 进行门店信息的客户关联字段更新
	 * 
	 * @param oldPid
	 * @param newPid
	 */
	public void updateMdxxPid(String oldPid, String newPid) {
		String sql = "update t_zz_khmdxx set pid='" + newPid
				+ "'   where pid ='" + oldPid + "'";
		DatabaseHandlerDao.getInstance().executeSql(sql);
		sql = "select * from t_zz_khmdxx where pid = '" + newPid + "'"
				+ defaultCode();
		List<Map<String, Object>> dataMap = DatabaseHandlerDao.getInstance()
				.queryForMaps(sql);
		for (Map<String, Object> map : dataMap) {
			Map<String, String> mdyhMap = new HashMap<String, String>();
			String yhm = String.valueOf(map.get("YHM"));
			if (isExistYhm(yhm) == 0) {
				mdyhMap.put("YHM", yhm);
				mdyhMap.put("MM",
						(new MD5()).getMD5ofStr(String.valueOf(map.get("MM")))
								.toLowerCase());
				mdyhMap.put("QYBM", String.valueOf(map.get("QYBM")));
				mdyhMap.put("SSKH", newPid);
				saveOne("t_zz_khyhxx", mdyhMap);
			}
		}
	}

	/**
	 * 文件上传的操作
	 * 
	 * @param oldFile
	 * @param newFile
	 * @param delFile
	 */
	public void copyFile(File oldFile, File newFile, File delFile) {
		try {
			if (delFile != null && delFile.isFile() && delFile.exists()) {// 执行删除文件的操作
				FileUtils.deleteQuietly(delFile);
			}
			// 进行文件复制
			FileUtils.copyFile(oldFile, newFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获得指定的客户信息
	 * 
	 * @param id
	 * @return
	 */
	public Map<String, Object> getObjKhxx(String id) {
		String sql = "select * from " + tableName + " where id ='" + id + "'";
		return DatabaseHandlerDao.getInstance().queryForMap(sql);

	}

	/**
	 * 获得指定的门店信息
	 * 
	 * @param id
	 * @return
	 */
	public Map<String, Object> getObjKhMdxx(String id) {
		String sql = "select id,tp from " + childtableName + " where id ='"
				+ id + "'";
		return DatabaseHandlerDao.getInstance().queryForMap(sql);

	}

	/**
	 * 获得指定客户的门店相关信息
	 * 
	 * @param pid
	 * @return
	 */
	public Object searchKhmdxx(String pid) {
		String sql = "select t.* from " + childtableName + " t where t.pid ='"
				+ pid + "' and is_delete <> '1'";
		List<Map<String, Object>> listMap = DatabaseHandlerDao.getInstance()
				.queryForMaps(sql);
		Map<String, Object> mapData = new HashMap<String, Object>();
		mapData.put("data", listMap);
		mapData.put("pageNumber", 1);
		mapData.put("pageSize", listMap.size());
		mapData.put("total", 1);
		mapData.put("totalPages", 1);
		return mapData;
	}

	/**
	 * 保存门店信息
	 * 
	 * @param entityJson
	 * @param mdtpFile
	 * @param mdtpFileFileName
	 * @return
	 */
	@Transactional
	public Map<String, String> saveMdxx(String entityJson, File mdtpFile,
			String mdtpFileFileName) {
		// 默认上传路径为spzstpfj
		String realpath = ServletActionContext.getServletContext().getRealPath(
				"/spzstpfj");
		String tableName = childtableName;
		JsonNode entityNode = JsonUtil.json2node(entityJson);
		Map<String, String> dataMap = node2map(entityNode);
		dataMap.remove("CZ");// 去掉操作列数据
		dataMap.put("QYBM", SerialNumberUtil.getInstance().getCompanyCode());
		boolean isExitFile = false;
		String id = dataMap.get("ID") + "";
		Map<String, Object> objMap = new HashMap<String, Object>();
		// 判断是否为新增操作
		boolean isNew = true;
		if (id != null && !"".equals(id)) {// 不是新增进行修改操作
			objMap = getObjKhMdxx(id);
			isNew = false;
		}
		// 文件重命名
		String mdtpfileNameWithStamp = System.currentTimeMillis() + "_"
				+ mdtpFileFileName;
		if (mdtpFile != null && mdtpFile.isFile()) {
			// 保存修改后的文件到数据库中
			dataMap.put("TP", mdtpfileNameWithStamp);
			isExitFile = true;
		}
		// 执行操作
		id = save(tableName, dataMap, null);
		if (isExitFile) {
			// 进行图片的上传
			File desFile = new File(realpath + "/" + mdtpfileNameWithStamp);
			// 数据库中保存的文件名称
			String delFilePath = realpath + "/" + objMap.get("TP");
			File delFile = new File(delFilePath);
			// 执行文件上传和删除操作
			copyFile(mdtpFile, desFile, delFile);
		}

		dataMap.put(AppDefineUtil.C_ID, id);
		return dataMap;
	}

	/**
	 * 保存门店图片信息
	 * 
	 * @param dataMap
	 * @param mdtpFile
	 * @param mdtpFileFileName
	 * @return
	 */
	@Transactional
	public Map<String, String> saveMdtpxx(Map<String, String> dataMap,
			File mdtpFile, String mdtpFileFileName) {
		// 默认上传路径为spzstpfj
		String realpath = ServletActionContext.getServletContext().getRealPath(
				"/spzstpfj");
		String tableName = childtableName;
		dataMap.put("QYBM", SerialNumberUtil.getInstance().getCompanyCode());
		boolean isExitFile = false;
		String id = dataMap.get("ID") + "";
		Map<String, Object> objMap = null;
		// 判断是否为新增操作
		if (!StringUtil.isEmpty(id)) {// 不是新增进行修改操作
			objMap = getObjKhMdxx(id);
			// isExitFile = true;
		}
		// 文件重命名
		String mdtpfileNameWithStamp = System.currentTimeMillis() + "_"
				+ mdtpFileFileName;
		if (mdtpFile != null && mdtpFile.isFile()) {
			// 保存修改后的文件到数据库中
			dataMap.put("TP", mdtpfileNameWithStamp);
			isExitFile = true;
		}
		// 执行操作
		id = save(tableName, dataMap, null);
		if (isExitFile) {
			// 进行图片的上传
			File desFile = new File(realpath + "/" + mdtpfileNameWithStamp);
			// 数据库中保存的文件名称
			String delFilePath = realpath + "/" + dataMap.get("TP");
			File delFile = new File(delFilePath);
			// 执行文件上传和删除操作
			copyFile(mdtpFile, desFile, delFile);
		}
		dataMap.put(AppDefineUtil.C_ID, id);
		return dataMap;
	}

	/**
	 * 进行客户门店信息的标志位删除
	 * 
	 * @param id
	 */
	public void deleteKhmdxx(String id) {
		String sql = "update t_zz_khmdxx set is_delete = 1 where id = '" + id
				+ "'";

		DatabaseHandlerDao.getInstance().executeSql(sql);
	}

	@Override
	@Transactional
	public void delete(String tableId, String dTableIds, String ids,
			boolean isLogicalDelete, Map<String, Object> paramMap) {
		String id[] = ids.split(",");
		for (int i = 0; i < id.length; i++) {
			String sql = "select * from T_ZZ_KHXX t where t.id = '" + id[i]
					+ "'";
			Map<String, Object> dataMap = DatabaseHandlerDao.getInstance()
					.queryForMap(sql);
			UserInfo userInfo;
			userInfo = CommonUtil.getUserInfoFacade().findByLoginName(
					String.valueOf(dataMap.get("YHM")));
			if (userInfo != null) {
				CommonUtil.getUserInfoFacade().delete(userInfo.getId());
			}
		}
		super.delete(tableId, dTableIds, ids, isLogicalDelete, paramMap);
	}

	public Object getMdssdqByKhssdq(String ssdq) {
		if (!"".equals(String.valueOf(ssdq))) {
			for (int i = (ssdq.length() - 1); i >= 0; i--) {
				if (!ssdq.substring(i, i + 1).equals("0")) {
					ssdq = ssdq.substring(0, i + 1);
					break;
				}
			}
		}
		String sql = "select * from t_common_cdxx t where t.cdbm like '"
				+ ssdq + "%'";
		List<Map<String, Object>> dataList = DatabaseHandlerDao.getInstance()
				.queryForMaps(sql);
		for (Map<String, Object> map : dataList) {
			map.put("value", map.get("CDBM"));
			map.put("text", map.get("CDMC"));
		}
		return dataList;
	}

	/**
	 * 判断用户名是否存在
	 * 
	 * @param yhm
	 * @return
	 */
	public int isExistYhm(String yhm) {
		String sql = "select * from t_zz_khyhxx where yhm='" + yhm + "'";
		List<Map<String, Object>> dataList = DatabaseHandlerDao.getInstance()
				.queryForMaps(sql);
		if (dataList != null && !dataList.isEmpty()) {
			if (dataList.size() > 1) {
				return dataList.size();
			}
		}
		return 0;
	}

	/**
	 * 根据用户名获得对应用户的信息
	 * 
	 * @param yhm
	 * @return
	 */
	public Map<String, Object> searchMdYhxx(String yhm) {
		String sql = "select * from t_zz_khyhxx where yhm='" + yhm + "' "
				+ defaultCode();
		return DatabaseHandlerDao.getInstance().queryForMap(sql);
	}

	public Object getKhbh(){
//		String qybm = SerialNumberUtil.getInstance().getCompanyCode();
//		int lshLength = 6;
//		int khbhLength = qybm.length() + lshLength;
//		StringBuilder sql = new StringBuilder("SELECT lPAD(NVL2(MAX(T.KHBH),MAX(T.KHBH),RPAD('"+qybm+"'," + khbhLength + ",'0')) + 1," + khbhLength + ",'0') AS KHBH FROM T_ZZ_KHXX T WHERE T.QYBM = '" + qybm + "'and t.is_delete ='0'");
//		Map<String, Object> dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql.toString());
		return null;
	}
}