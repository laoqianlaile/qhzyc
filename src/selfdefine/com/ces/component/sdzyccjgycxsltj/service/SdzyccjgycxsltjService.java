package com.ces.component.sdzyccjgycxsltj.service;

import com.ces.component.sdzyccjgycxsltj.dao.SdzyccjgycxsltjDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.component.trace.utils.SerialNumberUtil;
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
public class SdzyccjgycxsltjService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzyccjgycxsltjDao> {

    @PersistenceContext
    private EntityManager entityManager;

    public Map<String, Object> serachjson(String kssj,String jssj,String rqlx,String cpmc) {
        Map<String,Object> result = new HashMap<String, Object>();
        String sql = "";
        if(rqlx.equalsIgnoreCase("1")){
            if(cpmc!=null && cpmc!="")
            {
                sql = "  select d.qymc, c.*\n" +
                        "    from (select a.qybm,substr(a.jysj, 0, 11) as jysj,sum(b.jyzl) as jyzl\n" +
                        "    from t_Sdzyc_Cjg_Ycjyxx a\n" +
                        "    join t_Sdzyc_Cjg_Ycjyxxxx b\n" +
                        "      on a.xsddh = b.xsddh  and b.ycmc like '%" + cpmc + "%'\n" +
                        "   group by a.qybm,substr(a.jysj, 0, 11)) c\n" +
                        "    join t_sdzyc_qyda d\n" +
                        "      on c.qybm = d.qybm  and d.qybm =?\n" +
                        "   where d.dwlx = 'JJGQY'\n" +
                        "     and c.jysj between '" + kssj + "' and '" + jssj + "'\n" +
                        "   order by c.jysj";
            }else {
                sql = "  select d.qymc, c.*\n" +
                        "    from (select a.qybm,substr(a.jysj, 0, 11) as jysj,sum(b.jyzl) as jyzl\n" +
                        "    from t_Sdzyc_Cjg_Ycjyxx a\n" +
                        "    join t_Sdzyc_Cjg_Ycjyxxxx b\n" +
                        "      on a.xsddh = b.xsddh\n" +
                        "   group by a.qybm,substr(a.jysj, 0, 11)) c\n" +
                        "    join t_sdzyc_qyda d\n" +
                        "      on c.qybm = d.qybm  and d.qybm =?\n" +
                        "   where d.dwlx = 'JJGQY'\n" +
                        "     and c.jysj between '" + kssj + "' and '" + jssj + "'\n" +
                        "   order by c.jysj";
            }
        }else{
            kssj = kssj.substring(0,7);
            jssj = jssj.substring(0,7);
            if(cpmc!=null && cpmc!="") {
                sql = "  select d.qymc, c.*\n" +
                        "    from (select a.qybm,substr(a.jysj, 0, 8) as jysj,sum(b.jyzl) as jyzl\n" +
                        "    from t_Sdzyc_Cjg_Ycjyxx a\n" +
                        "    join t_Sdzyc_Cjg_Ycjyxxxx b\n" +
                        "     on a.xsddh = b.xsddh\n" +
                        "     and b.ycmc like '%" + cpmc + "%'\n" +
                        "   group by a.qybm,substr(a.jysj, 0, 8)) c\n" +
                        "    join t_sdzyc_qyda d\n" +
                        "      on c.qybm = d.qybm  and d.qybm =?\n" +
                        "   where d.dwlx = 'JJGQY'\n" +
                        "     and c.jysj between '" + kssj + "' and '" + jssj + "'\n" +
                        "   order by c.jysj";
            }else{
                sql = "  select d.qymc, c.*\n" +
                        "    from (select a.qybm,substr(a.jysj, 0, 8) as jysj,sum(b.jyzl) as jyzl\n" +
                        "    from t_Sdzyc_Cjg_Ycjyxx a\n" +
                        "    join t_Sdzyc_Cjg_Ycjyxxxx b\n" +
                        "      on a.xsddh = b.xsddh\n" +
                        "   group by a.qybm,substr(a.jysj, 0, 8)) c\n" +
                        "    join t_sdzyc_qyda d\n" +
                        "      on c.qybm = d.qybm  and d.qybm =?\n" +
                        "   where d.dwlx = 'JJGQY'\n" +
                        "     and c.jysj between '" + kssj + "' and '" + jssj + "'\n" +
                        "   order by c.jysj";
            }
        }

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