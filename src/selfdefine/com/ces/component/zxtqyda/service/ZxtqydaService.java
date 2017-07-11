package com.ces.component.zxtqyda.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.*;
import java.util.Map.Entry;

import cesgroup.rear.lib.client.enterprise.RearClientBuilder;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ces.config.utils.StringUtil;
import com.ces.config.utils.UUIDGenerator;
import enterprise.endpoints.EnterpriseService;
import enterprise.entity.CompanyEntity;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.TableNameUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.menu.Menu;
import com.ces.config.dhtmlx.service.menu.MenuService;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.utils.FileUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import sun.misc.BASE64Encoder;

@Service
public class ZxtqydaService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao>{
	@PersistenceContext
	private EntityManager entitymanager;

	@Autowired
	@Override
	public void setDao(TraceShowModuleDao dao){
		super.setDao(dao);
	}
	/**
	 * 根据菜单名称获取企业档案
	 * @param menuId
	 * @return
	 */
	public Map<String,Object> getQydaByMenuId(String menuId){
		String menuName = getRootMenuNameRecursion(menuId);
		String qybm = SerialNumberUtil.getInstance().getCompanyCode();
		return getQydaByMenunameAndQyam(menuName,qybm);
	}
	/**
	 * 根据菜单名称获取企业档案
	 * @param menuId
	 * @return
	 */
	public Map<String,Object> getQydaByMenu(String menuId, String dwlx){
		String menuName = getRootMenuNameRecursion(menuId);
		String qybm = SerialNumberUtil.getInstance().getCompanyCode();
		return getQydaByMenunameAndQybm(menuName,qybm,dwlx);
	}
	/**
	 * 获取根菜单名称
	 * @param menuId
	 * @return
	 */
	private String getRootMenuNameRecursion(String menuId){
		Menu menu = getService(MenuService.class).getByID(menuId);
		String parentId = menu.getParentId();
		if("-1".equals(parentId)){
			return menu.getName();
		}
		else{
			return getRootMenuNameRecursion(parentId);
		}
	}

	/**
	 * 获取企业档案
	 * @param menuName
	 * @param qybm
	 * @return
	 */
	private Map<String,Object> getQydaByMenunameAndQyam(String menuName,String qybm){
		String tableName = "";
		String sysName = "";
		String sql = "";
		String sql1 = "";
		if ("批发市场".equals(menuName)) {
			sysName = "PC";
		} else if("蔬菜种植".equals(menuName)){
			sysName = "ZZ";
		} else if("零售市场".equals(menuName)){
			sysName = "LS";
		} else if("活畜屠宰".equals(menuName)){
			sysName = "TZ";
		} else if("超市".equals(menuName)){
			sysName = "CS";
		} else if("猪肉批发".equals(menuName)){
			sysName = "PR";
		} else if("活畜养殖".equals(menuName)){
			sysName = "YZ";
		} else if("团体".equals(menuName)){
			sysName = "TT";
		} else if("餐饮".equals(menuName)){
			sysName = "CY";
		} else if("加工".equals(menuName)){
			sysName = "JG";
		}else if ("山东中药材".equals(menuName)){
			sysName = "SDZYC";
		}
		tableName = TableNameUtil.getQydaTableName(sysName);
		sql = "select * from "+tableName+" T where T.QYBM = '"+qybm+"'";
		sql1 = "select t.tpmc,t.tplj from t_common_qytp t where t.qybm = '" + qybm + "' order by t.tpmc asc";
		String logoSql = "select t.tpmc,t.tplj from t_common_qylogo t where t.qybm = '" + qybm + "'";
		Map<String,Object> resultMap = DatabaseHandlerDao.getInstance().queryForMap(sql);
		List<Map<String,Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql1);
		List<Map<String,Object>> logoMap = DatabaseHandlerDao.getInstance().queryForMaps(logoSql);

		resultMap.put("sysName", sysName);
		resultMap.put("tp", maps);
		resultMap.put("logo", logoMap);
		return resultMap;
	}
	/**
	 * 获取企业档案
	 * @param menuName
	 * @param qybm
	 * @return
	 */
	private Map<String,Object> getQydaByMenunameAndQybm(String menuName,String qybm,String dwlx){
		String tableName = "";
		String sysName = "";
		String sql = "";
		String sql1 = "";
		if ("批发市场".equals(menuName)) {
			sysName = "PC";
		} else if("蔬菜种植".equals(menuName)){
			sysName = "ZZ";
		} else if("零售市场".equals(menuName)){
			sysName = "LS";
		} else if("活畜屠宰".equals(menuName)){
			sysName = "TZ";
		} else if("超市".equals(menuName)){
			sysName = "CS";
		} else if("猪肉批发".equals(menuName)){
			sysName = "PR";
		} else if("活畜养殖".equals(menuName)){
			sysName = "YZ";
		} else if("团体".equals(menuName)){
			sysName = "TT";
		} else if("餐饮".equals(menuName)){
			sysName = "CY";
		} else if("加工".equals(menuName)){
			sysName = "JG";
		}else if ("山东中药材".equals(menuName)){
			sysName = "SDZYC";
		}else if ("中医院".equals(menuName)){
			sysName = "SDZYC";
		}
		tableName = TableNameUtil.getQydaTableName(sysName);
		sql = "select * from "+tableName+" T where T.QYBM = '"+qybm+"' and t.dwlx=?";
		sql1 = "select t.tpmc,t.tplj from t_common_qytp t where t.qybm = '" + qybm + "' and t.pid=? order by t.tpmc asc";
		String logoSql = "select t.tpmc,t.tplj from t_common_qylogo t where t.qybm = '" + qybm + "' and t.pid=?";
		Map<String,Object> resultMap = DatabaseHandlerDao.getInstance().queryForMap(sql,new Object[]{dwlx});
		List<Map<String,Object>> maps = null;
		List<Map<String,Object>> logoMap = null;
		if(null!=resultMap){
			maps = DatabaseHandlerDao.getInstance().queryForMaps(sql1,new String[]{String.valueOf(resultMap.get("ID"))});
			logoMap = DatabaseHandlerDao.getInstance().queryForMaps(logoSql,new String[]{String.valueOf(resultMap.get("ID"))});
		}
		resultMap.put("sysName", sysName);
		resultMap.put("tp", maps);
		resultMap.put("logo", logoMap);
		return resultMap;
	}

	private void sendModifyCompInfoService(Map<String,Object> map, Map<String,String> logoMap){
		EnterpriseService service = RearClientBuilder.build(EnterpriseService.class);
		CompanyEntity ce = new CompanyEntity();
		ce.setComp_code((String)(map.get("qybm")));
		ce.setComp_name((String)(map.get("qymc")));
		ce.setAddress((String)(map.get("zcdz")));
		ce.setTel((String)(map.get("lxdh")));
		ce.setLicense((String)(map.get("gszcdjzh")));
		ce.setImage(logoMap.get("imageName"));
		ce.setBase64(logoMap.get("logoImage"));
		ce.setComp_type(logoMap.get("type"));
		ce.setContact((String)map.get("linkman"));
		ce.setArea_code((String)map.get("lsxzqhdm"));
		if(!StringUtil.isEmpty(map.get("linkman"))){
			ce.setLegal_rep((String) map.get("linkman"));
		}
		if (!StringUtil.isEmpty(map.get("qyjj"))){
			ce.setDescription((String) map.get("qyjj"));
		}
		if (!StringUtil.isEmpty(map.get("wz"))){
			ce.setWebsite((String) map.get("wz"));
		}
		if (!StringUtil.isEmpty(map.get("cz"))){
			ce.setFax((String) map.get("cz"));
		}
		service.modifyComp((String)(map.get("qybm")),ce);
	}
	/**
	 * 保存企业档案
	 * @param map
	 * @param imageUpload
	 * @param imageUploadFileName
	 * @param logoImageUpload
	 * @param logoImageUploadFileName
	 * @return
	 */
	public List<String> saveQyda(Map<String,Object> map,List<File> imageUpload,List<String> imageUploadFileName,File logoImageUpload,String logoImageUploadFileName){
		String qybm = SerialNumberUtil.getInstance().getCompanyCode();
		String sysName = map.get("sysName").toString();
		String tableName = TableNameUtil.getQydaTableName(sysName);
		map.remove("sysName");
		map.remove("zzjddz");
		map.remove("zzjdmj");
		map.remove("zzjdmc");
		map.remove("cdmc");
		map.remove("cdbm");
		String qyjj =(String) map.get("cdms");
		map.remove("cdms");
		map.put("qyjj", qyjj);
		String id = (String)map.get("ID");
		String dwlx = (String)map.get("dwlx");
		String realpath = ServletActionContext.getServletContext().getRealPath("/spzstpfj");
		List<String> fileNamesWithStamp = new ArrayList<String>();
		/****保存logo图片start****/
		Map<String, String> logoMap = new HashMap<String, String>();
		if(StringUtil.isEmpty(id)){
			map.put("qybm", qybm);
			id = saveOne("T_SDZYC_QYDA",mapToString(map));
			map.put("ID", id);
		}
		map.put("qybm", qybm);
		if(logoImageUpload!=null){
			Map<String, String> imageDataMap = new HashMap<String, String>();
			String sql = "select * from T_COMMON_QYLOGO t where t.QYBM = ? and t.pid=?";
			Map<String, Object> tpMap = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{qybm,id});
			if (tpMap != null && !tpMap.isEmpty()) {//原来有数据，修改
				imageDataMap.put("ID", String.valueOf(tpMap.get("ID")));
				//删除原来的图片
				FileUtils.deleteQuietly(new File(realpath + "/" + tpMap.get("TPBCMC")));
			}
			String[] args = logoImageUploadFileName.split("\\.");
			String fileNameWithStamp = System.currentTimeMillis()+ UUIDGenerator.uuid()+"."+args[1];
			fileNamesWithStamp.add(fileNameWithStamp);
			File destFile = new File(realpath + "/" + fileNameWithStamp);
			try {
				FileUtils.copyFile(logoImageUpload, destFile);
//				newImageName = fileNameWithStamp;
			} catch (IOException e) {
				e.printStackTrace();
			}
			Date now = new Date();
			DateFormat df = DateFormat.getDateTimeInstance();
			String date = df.format(now);
			imageDataMap.put("TPMC",logoImageUploadFileName);
			imageDataMap.put("TPLJ",fileNameWithStamp);
			imageDataMap.put("QYBM",qybm);
			imageDataMap.put("UPLOADTIME",date);
			imageDataMap.put("PID",id);
			saveOne("T_COMMON_QYLOGO",imageDataMap);
			try {
				logoMap.put("logoImage", encodeBase64File(realpath + "/" + fileNameWithStamp));
				logoMap.put("imageName", logoImageUploadFileName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		/*********结束**********/
		/****保存图片start*****/
		if(imageUpload!=null&&imageUpload.size()!=0&&!imageUpload.isEmpty()){
			for(int i = 0;i<imageUpload.size();i++) {
				Map<String,String> dataMap = new HashMap<String, String>();
				String fileNameWithStamp = System.currentTimeMillis() + "_" + imageUploadFileName.get(i);
				fileNamesWithStamp.add(fileNameWithStamp);
				File destFile = new File(realpath + "/" + fileNameWithStamp);
				try {
					FileUtils.copyFile(imageUpload.get(i), destFile);
//				newImageName = fileNameWithStamp;
				} catch (IOException e) {
					e.printStackTrace();
				}
				Date now = new Date();
				DateFormat df = DateFormat.getDateTimeInstance();
				String date = df.format(now);
				dataMap.put("TPMC",imageUploadFileName.get(i));
				dataMap.put("TPLJ",fileNameWithStamp);
				dataMap.put("QYBM",qybm);
				dataMap.put("UPLOADTIME",date);
				dataMap.put("PID",id);
				saveOne("T_COMMON_QYTP",dataMap);
//			String sql = "update " + tableName + " T set T.QYTP='"
//					+ imageUploadFileName + "',T.QYTP2='"
//					+ fileNameWithStamp + "' where T.QYBM='" + qybm + "'";
//			DatabaseHandlerDao.getInstance().executeSql(sql);
				//删除旧图片
//			FileUtil.deleteFile(realpath + "/" + oldImageFile);
			}
		}
		/****保存图片end*****/
		/****保存数据start******/
		String sqlStr ="";
		Set<Entry<String, Object>> allSet = map.entrySet();
		Iterator<Entry<String,Object>> iter = allSet.iterator();
		while(iter.hasNext()){
			Entry<String,Object> entry = iter.next();
			if(!"ZZJDDZ".equalsIgnoreCase(entry.getKey()) && !"CDMS".equalsIgnoreCase(entry.getKey()) && !"CDBM".equalsIgnoreCase(entry.getKey()) && !"CDMC".equalsIgnoreCase(entry.getKey())  && !"ZZJDMJ".equalsIgnoreCase(entry.getKey()) && !"ZZJDMC".equalsIgnoreCase(entry.getKey())) {//ZZJDMJ
				sqlStr += entry.getKey() + "= '" + entry.getValue() + "',";
			}
		}
		sqlStr = sqlStr.substring(0, sqlStr.length()-1);
		String sql2 = "update " +tableName+ " set " +sqlStr+ " where QYBM='"+qybm+"' and id =? and dwlx=?";
		DatabaseHandlerDao.getInstance().executeSql(sql2,new String[]{id,dwlx});
		if (dwlx.equals("ZZQY")){
			logoMap.put("type", "1");
		}else if(dwlx.equals("CJGQY")){
			logoMap.put("type", "2");
		}else if(dwlx.equals("JJGQY")){
			logoMap.put("type", "3");
		}else {
			logoMap.put("type", "4");
		}
		sendModifyCompInfoService(map, logoMap);

		/****保存数据end******/
		String sycSql = "update t_qypt_zhgl set ";
		String fieldSql = "";
		/*****同步数据start*****/
		if("ZZ".equals(sysName)){
			/*****种植start****/
			fieldSql += "frdb='"+map.get("fddb").toString()+"',";//法人代表
			fieldSql += "dz='"+map.get("jydz").toString()+"',";//经营地址
			fieldSql += "sj='"+map.get("lxdh").toString()+"',";//联系电话
			fieldSql += "cz='"+map.get("cz").toString()+"',";//传真
			fieldSql += "gszch='"+map.get("gszcdjzh").toString()+"',";//工商注册号
			fieldSql += "yb='"+map.get("yb").toString()+"',";//邮编
			fieldSql += "wz='"+map.get("wz").toString()+"'";//网址
			sycSql += fieldSql;
			sycSql += " where zhbh='"+qybm+"'";
			DatabaseHandlerDao.getInstance().executeSql(sycSql);
			/*****种植end*****/
		}else if("SDZYC".equals(sysName)){

		} else{
			/*****not种植start****/
			String mdField = getMdfiled(sysName,"md");
			String dzField = getMdfiled(sysName,"dz");
			String czField = getMdfiled(sysName,"cz");
			String frdbField = getMdfiled(sysName,"frdb");
			if (!"none".equals(mdField)) {
				fieldSql += "qymc='"+map.get(mdField).toString()+"',";//门店名称
			}
			if (!"none".equals(czField)) {
				fieldSql += "cz='"+map.get(czField).toString()+"',";//传真
			}
			if (!"none".equals(frdbField)) {
				fieldSql += "frdb='"+map.get("frdb").toString()+"',";//法人代表
			}
			fieldSql += "dz='"+map.get(dzField).toString()+"',";//地址
			fieldSql += "sj='"+map.get("lxdh").toString()+"',";//联系电话
			fieldSql += "gszch='"+map.get("gszcdjzh").toString()+"' ";//工商注册号
			sycSql += fieldSql;
			sycSql += " where zhbh='"+qybm+"'";
			DatabaseHandlerDao.getInstance().executeSql(sycSql);
			/*****not种植end*****/
		}

		/*****同步数据end*****/
		return fileNamesWithStamp;
	}
	/**
	 * 将文件转成base64 字符串
	 * @param path
	 * @return  *
	 * @throws Exception
	 */

	public static String encodeBase64File(String path) throws Exception {
		InputStream in;
		byte[] data = null;
		//读取图片字节数组
		try
		{
			in = new FileInputStream(path);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		//对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);//返回Base64编码过的字节数组字符串

	}
	public Map<String, String> mapToString(Map<String, Object> map){
		Map<String, String> dataMap = new HashMap<String, String>();
		Set<String> keySet = map.keySet();
		for(String key: keySet){
			dataMap.put(key,String.valueOf(map.get(key)));
		}
		return dataMap;
	}

	private String getMdfiled(String sysName,String lx){
		String md = "";
		String dz = "";
		String cz = "";
		String frdb = "";
		if("YZ".equals(sysName)){
			md = "yzcmc";
			dz = "yzcdz";
			cz = "none";
			frdb = "none";
		} else if ("PC".equals(sysName)) {
			md = "pfscmc";
			dz = "jydz";
			cz = "cz";
			frdb = "frdb";
		} else if ("PR".equals(sysName)) {
			md = "pfscmc";
			dz = "jydz";
			cz = "cz";
			frdb = "frdb";
		} else if ("TZ".equals(sysName)) {
			md = "tzcmc";
			dz = "jydz";
			cz = "cz";
			frdb = "frdb";
		} else if ("CS".equals(sysName)) {
			md = "csmc";
			dz = "jydz";
			cz = "cz";
			frdb = "frdb";
		} else if ("TT".equals(sysName)) {
			md = "none";
			dz = "jydz";
			cz = "cz";
			frdb = "frdb";
		} else if ("JG".equals(sysName)) {
			md = "jgcmc";
			dz = "jgcdz";
			cz = "none";
			frdb = "none";
		} else if ("LS".equals(sysName)) {
			md = "lsscmc";
			dz = "jydz";
			cz = "cz";
			frdb = "frdb";
		} else if ("CY".equals(sysName)) {
			md = "none";
			dz = "jydz";
			cz = "cz";
			frdb = "frdb";
		}
		if("md".equals(lx)){
			return md;
		} else if("dz".equals(lx)){
			return dz;
		} else if("cz".equals(lx)){
			return cz;
		} else {
			return frdb;
		}
	}

	public Object deleteImage(String tplj,String type) {
		String qybm = SerialNumberUtil.getInstance().getCompanyCode();
		String tableName;
		if("qytp".equals(type)){
			tableName = "T_COMMON_QYTP";
		}else{
			tableName="T_COMMON_QYLOGO";
		}
		String sql = "delete from "+tableName+" t where t.tplj = '"+tplj+"' and t.qybm = '"+qybm+"'";
		int result = DatabaseHandlerDao.getInstance().executeSql(sql);
		String realpath = ServletActionContext.getServletContext().getRealPath("/spzstpfj");
		if(1==result){
			FileUtil.deleteFile(realpath + "/" + tplj);
		}
		return result;
	}
}