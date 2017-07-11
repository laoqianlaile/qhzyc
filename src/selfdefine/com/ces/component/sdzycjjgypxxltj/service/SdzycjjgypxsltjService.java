package com.ces.component.sdzycjjgypxxltj.service;

import com.ces.component.sdzycjjgypxxltj.dao.SdzycjjgypxsltjDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.StringUtil;
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
public class SdzycjjgypxsltjService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzycjjgypxsltjDao> {


    public Map<String, Object> serachjson(String kssj,String jssj,String rqlx,String cpmc) {
        Map<String,Object> result = new HashMap<String, Object>();
        String dataType ="  substr(a.jysj, 0, 11)  ";
        if(rqlx.equalsIgnoreCase("1")){

        }else{
            kssj = kssj.substring(0,7);
            jssj = jssj.substring(0,7);
            dataType  = "  substr(a.jysj, 0, 8)  ";
        }
        String sql = "select d.qymc, c.*\n" +
                "  from (select a.qybm,\n" +dataType+" as jysj,\n" +
                "       sum(b.jyzl) as jyzl\n" +
                "  from t_Sdzyc_Jjg_cpjyxx a\n" +
                "  join t_Sdzyc_Jjg_Cpjyxxxx b\n" +
                "    on a.xsddh = b.xsddh\n" +
                " group by a.qybm ,"+dataType+") c\n" +
                "  join t_sdzyc_qyda d\n" +
                "    on c.qybm = d.qybm and d.qybm=?\n" ;
        if(StringUtil.isNotEmpty(cpmc)){
            sql += " and ypmc like '%"+cpmc+"%' ";
        }
       sql +=   " where d.dwlx = 'JJGQY'\n" +
                "   and c.jysj between '"+kssj+"' and '"+jssj+"'\n" +
                " order by c.jysj\n";
        List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql,new String[]{SerialNumberUtil.getInstance().getCompanyCode()});
        List<String> dateList = new ArrayList<String>();
        List<String> qyList = new ArrayList<String>();
        List<Map<String,Object>> series = new ArrayList<Map<String, Object>>();
        for (Map<String,Object> map : list) {
            String date = map.get("JYSJ").toString();
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
                    if (map.get("QYMC").equals(qy)&& map.get("JYSJ").equals(date)) {
                        zlList.add(Float.parseFloat(map.get("JYZL").toString()));
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