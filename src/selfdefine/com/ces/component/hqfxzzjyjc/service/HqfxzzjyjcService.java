package com.ces.component.hqfxzzjyjc.service;

import com.ces.component.hqfxzzjyjc.dao.HqfxzzjyjcDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
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
public class HqfxzzjyjcService extends TraceShowModuleDefineDaoService<StringIDEntity, HqfxzzjyjcDao> {

    @PersistenceContext
    private EntityManager entityManager;

    public Map<String, Object> serachjson(String kssj,String jssj,String rqlx) {
        Map<String,Object> result = new HashMap<String, Object>();
        String sql = "";
        if(rqlx.equalsIgnoreCase("1")){
            sql = "select nvl(a.qymc, 'unknown') as qymc, b.* from (select sum(jyzl) as jyzl , qybm, jysj from T_SDZYC_YLJYXX where jysj between '"+kssj+"' and '"+jssj+"' group by jysj, qybm) b left join t_sdzyc_qyda a on a.qybm = b.qybm and a.dwlx = 'ZZQY' order by jysj asc";
        }else{
            kssj = kssj.substring(0,7);
            jssj = jssj.substring(0,7);
            sql = "select nvl(a.qymc, 'unknown') as qymc, b.* from (select sum(jyzl) as jyzl, qybm, substr(jysj, 0, 7) as jysj from T_SDZYC_YLJYXX where substr(jysj, 0, 7) between '"+kssj+"' and '"+jssj+"' group by substr(jysj, 0, 7), qybm) b left join t_sdzyc_qyda a on a.qybm = b.qybm and a.dwlx = 'ZZQY' order by jysj asc";
        }

//        String sql ="select * from T_SDZYC_YLJYXX where jysj between '"+ kssj +"' and '"+ newjssj(jssj,rqlx) +"'";
        List<Map<String, String>> list = (List<Map<String, String>>) entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        List<String> dateList = new ArrayList<String>();
        List<String> qyList = new ArrayList<String>();
        List<Map<String,Object>> series = new ArrayList<Map<String, Object>>();
        for (Map<String,String> map : list) {
            String date = map.get("JYSJ");
            String qy = map.get("QYMC");
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
            List<String> zlList = new ArrayList<String>();
            for (String date : dateList) {
                boolean exsit = false;
                for (Map<String,String> map : list) {
                    if (map.get("QYMC").equalsIgnoreCase(qy) && map.get("JYSJ").equalsIgnoreCase(date)) {
                        zlList.add(map.get("JYZL"));
                        exsit = true;
                    }
                }
                if (!exsit) zlList.add("0");
            }
            seriesMap.put("data",zlList);
            series.add(seriesMap);
        }
        result.put("series",series);
        return result;
    }

    public Map<String, Object> getResultData(List<Map<String, String>> data) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", data);
        return result;
    }

}
