package com.ces.component.qyptmdgl.service;

import com.ces.component.qyptmdgl.dao.QyptmdglDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/10.
 */

@Component
public class QyptmdglService extends TraceShowModuleDefineDaoService<StringIDEntity, QyptmdglDao> {
    private EntityManager entityManager;

    public List<Map<String,Object>> getQyxx(){
        String sql=null;
        List<Map<String,Object>> dataList=new ArrayList<Map<String, Object>>();
        List<Map<String,Object>> newdataList=new ArrayList<Map<String, Object>>();
        Map<String,Object> dataMap =null;
        sql="select t.qymc ,t.auth_parent_id,t.auth_id  from T_QYPT_ZHGL t where t.auth_parent_id='-1'";
        dataList= DatabaseHandlerDao.getInstance().queryForMaps(sql);
        for(Map<String,Object> data:dataList){
            dataMap = new HashMap<String, Object>();
            dataMap.put("id",data.get("AUTH_ID"));
            dataMap.put("name",data.get("QYMC"));
            newdataList.add(dataMap);
        }
        return newdataList;
    }
    public  Map<String,Object> getMdxx(String auth_parent_id){
        String sql=null;
        List<Map<String,Object>> dataList=null;
        if(null!=auth_parent_id&&""!=auth_parent_id) {
            sql = "select t.zhbh,t.qymc,t.auth_parent_id,t.auth_id from T_QYPT_ZHGL t where t.auth_parent_id='" + auth_parent_id+"'";
            dataList= DatabaseHandlerDao.getInstance().queryForMaps(sql);
            Map<String,Object> dataMap = new HashMap<String, Object>();
            dataMap.put("data",dataList);
            return dataMap;
        }
      return null;
    }
    //YYZZ,LXR,YX,SJ,ZJ,CZ,SXSJ,DZ,JD,WD,CSXX,CSMC,XTLX,auth_parent_id,auth_id

    public int saveQyht(String YYZZ,String LXR,String YX,String SJ,String ZJ,String CZ,String SXSJ,String DZ ,String JD,String WD,String CSXX,String CSMC,String XTLX, String auth_parent_id,String auth_id){
        String sql=null;

        sql="update T_QYPT_ZHGL t set t.YYZZ='"+YYZZ+"',T.LXR='"+LXR+"',T.YX='"+YX+"',T.SJ='"+SJ+"',T.ZJ='"+ZJ+"',T.CZ='"+CZ+"',T.SXSJ='"+SXSJ+"',T.DZ='"+DZ+"', t.CSXX='"+ CSXX+"',t.JD='"+JD+"',t.WD='"+WD+"',t.CSMC='"+CSMC+"' ,t.XTLX='"+XTLX+"'  where t.auth_parent_id='"+auth_parent_id+"' and t.auth_id='"+auth_id+"'";
        return  DatabaseHandlerDao.getInstance().executeSql(sql);

    }
    public Map<String,Object> initForm(String auth_parent_id,String auth_id){
        String sql=null;
        //String sql2=null;
        if((null!=auth_parent_id&&""!=auth_parent_id)&&(null!=auth_id&&""!=auth_id)){
           sql="select ZHBH,QYMC,YYZZ,LXR,YX,SJ,ZJ,CZ,SXSJ,DZ,JD,WD,CSXX,XTLX from T_QYPT_ZHGL t where t.auth_parent_id='" + auth_parent_id +"' and auth_id='"+ auth_id+"'";
            //sql="select ZHBH,QYMC,JD,WD,CSXX,XTLX from T_QYPT_ZHGL t where t.auth_parent_id='" + auth_parent_id +"' and auth_id='"+ auth_id+"'";
            //sql2="select YYZZ,LXR,YX,SJ,ZJ,CZ,SXSJ,DZ,JD,WD,CSXX,XTLX from T_QYPT_ZHGL t where t.auth_parent_id='" + -1 +"' and auth_id='"+ auth_parent_id+"'";
            Map<String,Object> dataMap=DatabaseHandlerDao.getInstance().queryForMap(sql);
            //Map<String,Object> dataMap2=DatabaseHandlerDao.getInstance().queryForMap(sql2);
            //dataMap1.putAll(dataMap2);
            return dataMap;
        }
        return null;
    }

    public List<Map<String,Object>> getXtlxGrid(String sysName){
        String sql="select Z.NAME, Z.VALUE from T_XTPZ_CODE Z where Z.code_type_code='CSPTXTLX'";
        if("SDZYC".equals(sysName)){
            sql = "select Z.NAME, Z.VALUE from T_XTPZ_CODE Z where Z.code_type_code='SDZYCQYLX'";
        }
        List<Map<String,Object>> dataList=new ArrayList<Map<String, Object>>();
        dataList= DatabaseHandlerDao.getInstance().queryForMaps(sql);
        List<Map<String,Object>> newData = new ArrayList<Map<String, Object>>();
        if(dataList!=null && !dataList.isEmpty()){
            for (Map<String ,Object> map:dataList){
                Map<String,Object> dataMap=new HashMap<String, Object>();
                dataMap.put("value",map.get("VALUE"));
                dataMap.put("text",map.get("NAME"));
                newData.add(dataMap);
           }
        }
        return newData;
    }
}
