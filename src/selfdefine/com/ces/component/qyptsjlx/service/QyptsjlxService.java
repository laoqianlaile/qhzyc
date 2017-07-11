package com.ces.component.qyptsjlx.service;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class QyptsjlxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

	
    /**
     * 分类编码验重
     */
    public Object getIsRepeatByFlbm(String flbm,String id){
        String sql = "";
        sql = "select * from t_common_sjlx t where t.lxbm = '" + flbm + "'";
        if(!"".equals(id) && id != null){
            sql += " and t.id !='" + id + "'";
        }
        List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return list;
    }

    /**
     * 类型编码验重
     */
    public Object getIsRepeatByLxbm(String lxbm,String id){
        String sql = "";
        sql = "select * from t_common_sjlx t where t.lxbm = '" + lxbm + "'";
        if(!"".equals(id) && id != null){
            sql += " and t.id !='" + id + "'";
        }
        List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return list;
    }

    /**
     * 数据类型验重
     */
    public Object getIsRepeatBySjbm(String lxbm,String sjbm,String id){
        String sql = "";
        sql = "select * from t_common_sjlx_code t where t.sjbm = '" + sjbm + "' and t.lxbm = '" + lxbm + "'";
        if(!"".equals(id) && id != null){
            sql += " and t.id !='" + id + "'";
        }
        List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return list;
    }

    /**
     * 顺序级别验重
     */
    public Object getIsRepeatBySxjb(String lxbm,String sxjb,String id){
        String sql = "";
        sql = "select * from t_common_sjlx_code t where t.sxjb = '" + sxjb + "' and t.lxbm = '" + lxbm + "'";
        if(!"".equals(id) && id != null){
            sql += " and t.id !='" + id + "'";
        }
        List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return list;
    }
    
    /**
     * 所属系统编码验重
     */
    public Object getIsRepeatBySsxtbm(String ssxtbm,String ssxtmc){
        String sql = "";
        sql = "select * from t_common_sjlx t where t.ssxtbm = '" + ssxtbm + "'";
        if(!"".equals(ssxtmc) && ssxtmc != null){
            sql += " and t.ssxtmc !='" + ssxtmc + "'";
        }
        List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return list;
    }

    /**
     * 获取数据字典类型名称
     * @return
     */
    public Object getSjzdmc(){
        String sql = "";
        sql = "select T.SSXTMC,T.SSXTBM FROM T_COMMON_SJLX T WHERE T.IS_DELETE = '0' and T.FLAG='2'  ";
        List<Map<String,Object>> xtlist = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        List xtDataList = new ArrayList();
        for(Map xtMap:xtlist){
        	Map xtSeMap = new HashMap();
            //数据字典分类名称
            sql = "select  T.FLMC,T.FLBM FROM T_COMMON_SJLX T WHERE T.IS_DELETE = '0' and T.FLAG='1' and T.SSXTBM=?  order by T.FLBM ";
            List<Map<String,Object>> li = DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{String.valueOf(xtMap.get("SSXTBM"))});
            //List<Map<String,Object>> newdataList = new ArrayList<Map<String, Object>>();
            //Map<String,Object> dataMap = null;
            List dataList = new ArrayList();        
            //转换tree格式
    		for (Map m : li) {
    			//Object a = m.get("LXBM");
    			Map map = new HashMap<String, String>();
    			//sql = "SELECT T.SJBM,T.SJMC FROM t_common_sjlx_code T WHERE T.is_delete = '0' and T.LXBM= ?  ORDER BY T.LXMC";
                sql = "select t.lxbm ,t.lxmc  from t_common_sjlx t where t.is_delete = '0' and t.FLBM= ? and t.flag='0' order by t.LXBM ";
    			List<Map<String, Object>> List = DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{String.valueOf(m.get("FLBM"))});
    			List children = new ArrayList();
    			for (Map lxMap : List) {
    				Map seMap = new HashMap();
    				seMap.put("id", lxMap.get("LXBM"));
    				seMap.put("name", lxMap.get("LXMC"));
    				//数据字典子节点
    				seMap.put("diczjd", "diczjd");
    				children.add(seMap);
    			}
                map.put("id",m.get("FLBM").toString());
                map.put("name",m.get("FLMC").toString());
                //数据字典节点
    			map.put("dicjd", "dicjd");
    			map.put("children", children);
    			dataList.add(map);
            }
    		xtSeMap.put("id",xtMap.get("SSXTBM").toString());
    		xtSeMap.put("name",xtMap.get("SSXTMC").toString());
            //数据字典节点
    		xtSeMap.put("dicxtjd", "dicxtjd");
    		xtSeMap.put("children", dataList);
			xtDataList.add(xtSeMap);
        	
        }
        return xtDataList;
    }


    /**
     * 根据数据字典类型编码获取相关数据
     */
    public Object getSjzdByLxbm(String lxbm){
        String sql = "";
        sql = "select SJBM,SJMC,ID,SXJB from t_common_sjlx_code t where t.lxbm = '" + lxbm + "' order by SXJB";
        List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data",list);
        return dataMap;
    }
    /**
     * 保存
     */
    @Override
    public String save(String tableName, Map<String, String> dataMap, Map<String, Object> paramMap) {
        return super.save(tableName, dataMap, paramMap);
    }

    /**
     * 根据ID删除
     */
    @Transactional
    public boolean deleteById(String ids){
        try {
            String sql = "";
            int rs = 0;
            String id[] = ids.split("___");
            for(int i = 0;i<id.length;i++){
                sql = "delete from t_common_sjlx_code t where t.id = '" + id[i] + "'";
                rs = DatabaseHandlerDao.getInstance().executeSql(sql);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public void delete(String tableId, String dTableIds, String ids, boolean isLogicalDelete, Map<String, Object> paramMap) {
        String id[] = ids.split(",");
        String sql = "";
        Map<String,Object> map = new HashMap<String, Object>();
        for(int i = 0;i<id.length;i++){
            sql = "select * from t_common_sjlx t where id = '" + id[i] + "'";
            map = DatabaseHandlerDao.getInstance().queryForMap(sql);
            sql = "delete from t_common_sjlx_code t where lxbm = '" + map.get("LXBM").toString() + "'";
            DatabaseHandlerDao.getInstance().executeSql(sql);
        }
        super.delete(tableId, dTableIds, ids, isLogicalDelete, paramMap);
    }

    /**
     * 返回最大顺序级别并+1
     */
    public Object getMaxSxjbByLxbm(String lxbm){
        String sql = "";
        sql = "select * from t_common_sjlx_code t where t.lxbm = '" + lxbm + "' and t.sxjb is not null order by t.sxjb desc";
        List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        Map<String,Object> map = list.size()==0?new HashMap<String, Object>():list.get(0);
        map.put("SXJB",(Integer.parseInt((map.get("SXJB")==null?0:map.get("SXJB")).toString())+1));
        return map;
    }
    
    /**
     * 通过flag区分分类和类型
     * @return
     */
    public Object getGridData(){
    	String sql = "select t.ID, t.LXBM,t.LXMC,t.FLMC,t.FLBM from t_common_sjlx t where t.flag ='0' order by t.FLBM";
        List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);    	
    	return list;
    }
    

}