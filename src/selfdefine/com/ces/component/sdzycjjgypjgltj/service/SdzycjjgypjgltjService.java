package com.ces.component.sdzycjjgypjgltj.service;

import com.ces.component.sdzycjjgypjgltj.dao.SdzycjjgypjgltjDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SdzycjjgypjgltjService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzycjjgypjgltjDao> {


    public Map<String, Object> serachjson(String kssj,String jssj,String rqlx,String cpmc) {
        Map<String,Object> result = new HashMap<String, Object>();
        String sql = "";
        if(rqlx.equalsIgnoreCase("1")){
            if(StringUtil.isNotEmpty(cpmc)){
                sql ="select scrq,sum(jgzzl) jgzzl,qybm,qymc from (select nvl(sum(jgzzl),0) jgzzl , substr(scrq, 0, 11) scrq,q.qybm,q.qymc from t_Sdzyc_Jjg_Ypscxx y ,t_sdzyc_qyda q where  scrq between '"+kssj+"' and '"+jssj+"' and y.qybm=q.qybm and q.dwlx='JJGQY' and ypmc like '%"+cpmc+"%' and y.qybm=?  group by scrq,q.qybm,q.qymc order by scrq) aa group by scrq,qybm,qymc order by scrq";
            }else{
                sql ="select scrq,sum(jgzzl) jgzzl ,qybm,qymc from (select nvl(sum(jgzzl),0) jgzzl , substr(scrq, 0, 11) scrq,q.qybm,q.qymc from t_Sdzyc_Jjg_Ypscxx y ,t_sdzyc_qyda q where  scrq between '"+kssj+"' and '"+jssj+"' and y.qybm=q.qybm and q.dwlx='JJGQY' and y.qybm=?  group by scrq,q.qybm,q.qymc order by scrq) aa group by scrq,qybm,qymc order by scrq";
            }

        }else{
            kssj = kssj.substring(0,7);
            jssj = jssj.substring(0,7);
            if(StringUtil.isNotEmpty(cpmc)){
                sql = "select nvl(sum(jgzzl),0) jgzzl , substr(scrq, 0, 8) scrq,q.qybm,q.qymc from t_Sdzyc_Jjg_Ypscxx y ,t_sdzyc_qyda q where  substr(scrq, 0, 8) between '" + kssj + "' and '" + jssj + "' and ypmc like '%"+cpmc+"%'  and y.qybm=q.qybm and q.dwlx='JJGQY' and y.qybm=?  group by substr(scrq, 0, 8),q.qybm,q.qymc order by scrq";
            }else {
                sql = "select nvl(sum(jgzzl),0) jgzzl , substr(scrq, 0, 8) scrq,q.qybm,q.qymc from t_Sdzyc_Jjg_Ypscxx y ,t_sdzyc_qyda q where  substr(scrq, 0, 8) between '" + kssj + "' and '" + jssj + "' and y.qybm=q.qybm and q.dwlx='JJGQY' and y.qybm=?  group by substr(scrq, 0, 8),q.qybm,q.qymc order by scrq";
            }
        }
        List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql,new String[]{SerialNumberUtil.getInstance().getCompanyCode()});
        List<String> dateList = new ArrayList<String>();
        List<String> qyList = new ArrayList<String>();
        List<Map<String,Object>> series = new ArrayList<Map<String, Object>>();
        for (Map<String,Object> map : list) {
            String date = map.get("SCRQ").toString();
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
                    if (map.get("QYMC").equals(qy)&& map.get("SCRQ").equals(date)) {
                        zlList.add(Float.parseFloat(map.get("JGZZL").toString()));
                        exsit = true;
                    }
                }
                if (!exsit) zlList.add((float)0.0);
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