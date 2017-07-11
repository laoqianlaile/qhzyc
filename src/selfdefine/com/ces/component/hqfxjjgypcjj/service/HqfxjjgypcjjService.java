package com.ces.component.hqfxjjgypcjj.service;

import com.ces.component.hqfxjjgypcjj.dao.HqfxjjgypcjjDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.xarch.core.entity.StringIDEntity;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class HqfxjjgypcjjService extends TraceShowModuleDefineDaoService<StringIDEntity, HqfxjjgypcjjDao> {

    @PersistenceContext
    private EntityManager entityManager;

    public Map<String, Object> serachjson(String kssj,String jssj,String ypmc,String rqlx,List<String> dateAxis) {
        Map<String,Object> resultmap = new HashMap<String, Object>();
        resultmap.put("ypmc",ypmc);
        String sql = "";
        if(rqlx.equalsIgnoreCase("1")){
            sql = "select c.qybm, substr(c.jysj, 0, 10) as jysj,c.ypmc,c.jydj,b.qymc from (select a.qybm, a.jysj, a.ypmc, avg(a.jydj) jydj from v_sdzyc_jjg_ypjyxx a where a.ypmc = '"+ypmc+"' and a.is_delete = '0' and a.sfjy = '1' group by a.qybm, a.jysj, a.ypmc ) c inner join (select qybm, qymc from t_sdzyc_qyda where dwlx='JJGQY') b on c.qybm = b.qybm where substr(jysj,0,10) between '"+kssj+"' and '"+jssj+"' order by jysj";
        }else if(rqlx.equalsIgnoreCase("2")){
            kssj = kssj.substring(0,7);
            jssj = jssj.substring(0,7);
            sql = "select c.qybm, substr(c.jysj, 0, 7) as jysj,c.ypmc,c.jydj,b.qymc from (select a.qybm, a.jysj, a.ypmc, avg(a.jydj) jydj from v_sdzyc_jjg_ypjyxx a where a.ypmc = '"+ypmc+"' and a.is_delete = '0' and a.sfjy = '1' group by a.qybm, a.jysj, a.ypmc ) c inner join (select qybm, qymc from t_sdzyc_qyda where dwlx='JJGQY') b on c.qybm = b.qybm where substr(jysj,0,7) between '"+kssj+"' and '"+jssj+"' order by jysj";
        }else{
            kssj = kssj.substring(0,4);
            jssj = jssj.substring(0,4);
            sql = "select c.qybm, substr(c.jysj, 0, 4) as jysj,c.ypmc,c.jydj,b.qymc from (select a.qybm, a.jysj, a.ypmc, avg(a.jydj) jydj from v_sdzyc_jjg_ypjyxx a where a.ypmc = '"+ypmc+"' and a.is_delete = '0' and a.sfjy = '1' group by a.qybm, a.jysj, a.ypmc ) c inner join (select qybm, qymc from t_sdzyc_qyda where dwlx='JJGQY') b on c.qybm = b.qybm where substr(jysj,0,4) between '"+kssj+"' and '"+jssj+"' order by jysj";
        }

        List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        List<Map<String,Object>> seriesList = new ArrayList<Map<String, Object>>();
        List<String> qyList = new ArrayList<String>();
        for(Map<String, Object> map: list){
            String qy = map.get("QYMC").toString();
            if(qyList.indexOf(qy) == -1){
                qyList.add(qy);
            }
        }
        List<Float> jydjList = null;


        for(String qy:qyList){
            Map<String,Object> seriesmap = new HashMap<String, Object>();
            seriesmap.put("name",qy);
            jydjList = new ArrayList<Float>();
            for (String date:dateAxis) {
                float jydj = (float) 0.0;
                for (Map<String,Object> map:list) {
                    Map<String,Object> mapa = map;
                    if(qy.equals(mapa.get("QYMC")) && date.equals(mapa.get("JYSJ"))) {
                        jydj = Float.parseFloat(mapa.get("JYDJ").toString());

                    }

                }
                jydjList.add(jydj);
            }
            seriesmap.put("data",jydjList);
            seriesList.add(seriesmap);
        }

        resultmap.put("series",seriesList);
        resultmap.put("categories",dateAxis);
        return resultmap;
    }


    public List<Map<String, String>> serachyp(){
        String sql = "select distinct(ypmc) from V_SDZYC_JJG_YPJYXX";
        List<Map<String, String>> list = (List<Map<String, String>>) entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        Map<String,String> resultmap = null;
        List<Map<String, String>> ypmclist = new ArrayList<Map<String, String>>();
        for(Map<String, String> map:list){
            resultmap = new HashMap<String, String>();
            resultmap.put("text",map.get("YPMC"));
            resultmap.put("value",map.get("YPMC"));
            ypmclist.add(resultmap);
        }
        return ypmclist;
    }


    public static List<String> getContinuousDate(String rqlx, String kssj, String jssj) {
        List<String> data = new ArrayList<String>();
        int type = Calendar.YEAR;
        try {
            if ("1".equals(rqlx)) {
                rqlx = "yyyy-MM-dd";
                type = Calendar.DATE;
            }
            if ("2".equals(rqlx)) {
                rqlx = "yyyy-MM";
                type = Calendar.MONTH;
            }
            if ("3".equals(rqlx)){
                rqlx = "yyyy";
                type = Calendar.YEAR;
            }
            Date kssjD = new SimpleDateFormat(rqlx).parse(kssj);//定义起始日期
            Date jssjD = new SimpleDateFormat(rqlx).parse(jssj);//定义结束日期
            Calendar dd = Calendar.getInstance();//定义日期实例
            dd.setTime(kssjD);//设置日期起始时间
            while (jssjD.getTime() >= dd.getTime().getTime()) {//判断是否到结束日期
                SimpleDateFormat sdf = new SimpleDateFormat(rqlx);
                String str = sdf.format(dd.getTime());//日期格式化
                data.add(str);//日期结果保存
                dd.add(type, 1);//进行当前日期月份加1
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

}
