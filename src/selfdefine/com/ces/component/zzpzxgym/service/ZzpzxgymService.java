package com.ces.component.zzpzxgym.service;

import com.ces.component.trace.utils.CompanyInfoUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.TableNameUtil;
import com.ces.component.zznyda.dao.ZznydaDao;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by bdz on 2015/8/13.
 */
@Component
public class ZzpzxgymService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

	/**
	 * 保存品种信息及附件
	 *
	 * @param map
	 * @param imageUpload
	 * @param imageUploadFileName
	 * @return
	 */
	@Transactional
	public String savePzxx(Map map, File imageUpload, String imageUploadFileName) {
		String qybm = SerialNumberUtil.getInstance().getCompanyCode();
		String qymc = CompanyInfoUtil.getInstance().getCompanyName("ZZ", qybm);
		map.put("QYBM", qybm);
		map.put("QYMC", qymc);
		String realpath = ServletActionContext.getServletContext().getRealPath("/spzstpfj");
//		String newImageName = "";
//		String oldImageFile = (String) DatabaseHandlerDao
//				.getInstance()
//				.queryForObject("select QYTP2 from "+tableName+" where QYBM='"+qybm+"'");
//		newImageName = oldImageFile;
		/****保存主表数据start******/
//		String sqlStr ="";
//		Set<Map.Entry<String, Object>> allSet = map.entrySet();
//		Iterator<Map.Entry<String,Object>> iter = allSet.iterator();
//		while(iter.hasNext()){
//			Map.Entry<String,Object> entry = iter.next();
//			sqlStr += entry.getKey() + "= '" +entry.getValue() + "',";
//		}
//		sqlStr = sqlStr.substring(0, sqlStr.length()-1);
//		String sql2 = "update " +tableName+ " set " +sqlStr+ " where QYBM='"+qybm+"'";
//		DatabaseHandlerDao.getInstance().executeSql(sql2);
		String masterId = save("T_ZZ_XPZXX", map, null);
		/****保存主表数据end******/
		/****保存从表图片数据start*****/
		if (imageUpload != null) {
			Map<String, String> imageDataMap = new HashMap<String, String>();
			String sql = "select * from T_ZZ_PLTP t where t.zbid = ?";
			Map<String, Object> tpMap = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{masterId});
			if (tpMap != null && !tpMap.isEmpty()) {//原来有数据，修改
				imageDataMap.put("ID", String.valueOf(tpMap.get("ID")));
				//删除原来的图片
				FileUtils.deleteQuietly(new File(realpath + "/" + tpMap.get("TPBCMC")));
			}
			String fileNameWithStamp = System.currentTimeMillis() + "_" + imageUploadFileName;
			File destFile = new File(realpath + "/" + fileNameWithStamp);
			try {
				FileUtils.copyFile(imageUpload, destFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = df.format(new Date());
			imageDataMap.put("ZBID", masterId);
			imageDataMap.put("SCTPMC", imageUploadFileName);
			imageDataMap.put("SCTPDX", String.valueOf(imageUpload.length()));
			imageDataMap.put("SCTPGS", imageUploadFileName.substring(imageUploadFileName.lastIndexOf(".") + 1));
			imageDataMap.put("TPBCMC", fileNameWithStamp);
			imageDataMap.put("SCSJ", date);
			saveOne("T_ZZ_PLTP", imageDataMap);
		}
		return masterId;
		/****保存从表图片数据end*****/
	}

	/**
	 * 根据编号查询品种
	 * @param treeNodeId
	 * @return
	 */
	public Map<String, Object> getPzById(String treeNodeId) {
		String sql = "select t.*, s.tpbcmc from T_ZZ_XPZXX t, T_ZZ_PLTP s where t.id = s.zbid(+) and t.id = ?";
		return DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{treeNodeId});
	}
}