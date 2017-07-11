package com.ces.component.hqfxzzyccsl.service;

import com.ces.component.hqfxzzyccsl.dao.HqfxzzyccslDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.xarch.core.entity.StringIDEntity;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class HqfxzzyccslService extends TraceShowModuleDefineDaoService<StringIDEntity, HqfxzzyccslDao> {


    public Map<String, Object> serachjson(String kssj,String jssj,String rqlx,String ycmc,String ckqy) {
        Map<String,Object> result = new HashMap<String, Object>();
        String sql = "";
        String ycmccode = "";
        String qybmcode = "";
        if(!ycmc.equals("1")){//非全部药材选项
            ycmccode = "and b.zsspm =" + "'"+ycmc+"'";
        }
        if(!ckqy.equals("1")){//非全部企业选项
            qybmcode = "b.qybm =" + "'"+ckqy+"' and";
        }
        if(rqlx.equalsIgnoreCase("2")){
            kssj = kssj.substring(0,4);
            jssj = jssj.substring(0,4);
            sql = "select nvl(c.qymc, 'unknown') as qymc,b.ycmname, a.cszl， a.jssj from (select sum(cszl) as cszl,substr(jssj,0,4) as jssj,ycdm from T_SDZYC_CSGLXX group by substr(jssj,0,4),ycdm) a join T_SDZYC_ZYCSPBM b on a.ycdm = b.zsspm and b.qylx = 'ZZ' join t_sdzyc_qyda c on c.qybm = b.qybm and c.dwlx='ZZQY' "+ycmccode+" where "+qybmcode +" (substr(a.jssj,0,4) between '"+kssj+"' and '"+jssj+"') order by a.jssj";
        }else{
            kssj = kssj.substring(0,7);
            jssj = jssj.substring(0,7);
            sql = "select nvl(c.qymc, 'unknown') as qymc,b.ycmname, a.cszl， a.jssj from (select sum(cszl) as cszl,substr(jssj,0,7) as jssj,ycdm from T_SDZYC_CSGLXX group by substr(jssj,0,7),ycdm) a join T_SDZYC_ZYCSPBM b on a.ycdm = b.zsspm and b.qylx = 'ZZ' join t_sdzyc_qyda c on c.qybm = b.qybm and c.dwlx='ZZQY' "+ycmccode+" where "+qybmcode +"(substr(a.jssj,0,7) between '"+kssj+"' and '"+jssj+"') order by a.jssj";
        }

        String ycm = null;
        List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        List<String> dateList = new ArrayList<String>();
        List<String> qyList = new ArrayList<String>();
        List<Map<String,Object>> series = new ArrayList<Map<String, Object>>();
        for (Map<String,Object> map : list) {
            if(!ycmc.equals("1")){
                ycm = map.get("YCMNAME").toString() +"  月(年)累计采收量";
            }else{
                ycm = "全部药材  月(年)累计采收量";
            }
            String date = null;
            if(rqlx.equals("2")){
                date = map.get("JSSJ").toString().substring(0,4);
            }else{
                date = map.get("JSSJ").toString().substring(0,7);
            }
            String qy = map.get("QYMC").toString();
            if (dateList.indexOf(date) == -1) {
                dateList.add(date);
            }
            if (qyList.indexOf(qy) == -1) {
                qyList.add(qy);
            }
        }
        result.put("categories",dateList);
        for(String qy : qyList){
            Map<String,Object> seriesMap = new HashMap<String, Object>();
            seriesMap.put("name",qy);
            List<Float> zlList = new ArrayList<Float>();
            for (String date : dateList) {
                boolean exsit = false;
                for (Map<String,Object> map : list) {
                    String jssjdate = null;
                    if(rqlx.equals("2")){
                        jssjdate = map.get("JSSJ").toString().substring(0,4);
                    }else {
                        jssjdate = map.get("JSSJ").toString().substring(0,7);
                    }
                    if (map.get("QYMC").equals(qy) && jssjdate.equals(date)) {
                        zlList.add(Float.parseFloat(map.get("CSZL").toString()));
                        exsit = true;
                    }
                }
                if (!exsit) zlList.add((float)0.0);
            }
            seriesMap.put("data",zlList);
            series.add(seriesMap);
        }
        result.put("series",series);
        result.put("ycm",ycm);
        return result;
    }

    public Map<String, Object> getResultData(List<Map<String, String>> data) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", data);
        return result;
    }

    public List<Map<String, Object>> serachyc(){
        String sql = "select ycmname,zsspm from T_SDZYC_ZYCSPBM where qylx = 'ZZ'";
        List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        Map<String,Object> resultmap = null;
        List<Map<String, Object>> ypmclist = new ArrayList<Map<String, Object>>();
        for(Map<String, Object> map:list){
            resultmap = new HashMap<String, Object>();
            resultmap.put("text",map.get("YCMNAME"));
            resultmap.put("value",map.get("ZSSPM"));
            ypmclist.add(resultmap);
        }
        Map<String,Object> defultmap = new HashMap<String, Object>();
        defultmap.put("text","全部药材");
        defultmap.put("value","1");
        defultmap.put("selected","true");
        ypmclist.add(defultmap);
        return ypmclist;
    }

    public List<Map<String, Object>> serachqy(){
        String sql = "select a.qybm,b.qymc from T_SDZYC_ZYCSPBM a join t_sdzyc_qyda b on a.qybm = b.qybm where a.qylx = 'ZZ' and b.dwlx = 'ZZQY'";
        List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        Map<String,Object> resultmap = null;
        List<Map<String, Object>> qymclist = new ArrayList<Map<String, Object>>();
        for(Map<String, Object> map:list){
            resultmap = new HashMap<String, Object>();
            resultmap.put("text",map.get("QYMC"));
            resultmap.put("value",map.get("QYBM"));
            qymclist.add(resultmap);
        }
        Map<String,Object> defultmap = new HashMap<String, Object>();
        defultmap.put("text","全部企业");
        defultmap.put("value","1");
        defultmap.put("selected","true");
        qymclist.add(defultmap);
        return qymclist;
    }

}
