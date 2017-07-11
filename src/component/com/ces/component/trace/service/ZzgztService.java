package com.ces.component.trace.service;

import com.ces.component.trace.dao.ZzgztDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.CommonUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by harper on 2015/9/15.
 */
@Component
public class ZzgztService extends TraceShowModuleDefineDaoService<StringIDEntity,ZzgztDao> {

    public Map<String,Object> searchTrp(){
        String sql = "select * from (select t.*, rownum rn from t_zz_trpcggl t where to_date(t.dqr, 'yyyy-MM-dd') <= (to_date(to_char(sysdate, 'yyyy-MM-dd'), 'yyyy-MM-dd') + 5) "+defaultCode()+" ) where rn < 5 order by dqr asc";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }


    /**
     * 查询订单信息：待出货状态
     * @return
     */
    public Map<String,Object> searchDdxx(){
        String sql ="select * from (select t.*,rownum rn from t_zz_xsddxx t where t.ddzt=1 "+defaultCode()+") where rn<6 order by xdrq desc";
        return  getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }

    /**
     * 根据负责人编号查询此负责人需要负责的区域信息以及地块信息
     *
     * @return
     */
    public Map<String,Object> searchQyxx(){
        String sql = "select qybh,qymc from t_zz_qyxx where 1=1" +defaultCode() +" ORDER BY QYBH ASC";
        List<Map<String,Object>> qyxx = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        Map<String,Object> dataMap = new HashMap<String, Object>();
        if(qyxx.size() == 0) return null;
        dataMap.put("qyxx",qyxx);
        sql = "SELECT DKBH,DKMC,CGQZ FROM T_ZZ_DKXX WHERE 1=1 AND QYBH = ?"+defaultCode() + "ORDER BY DKBH ASC";
        List<Map<String,Object>> dkxx = DatabaseHandlerDao.getInstance().queryForMaps(sql, new String[]{String.valueOf(qyxx.get(0).get("QYBH"))});
        if(dkxx.size() == 0) return dataMap;
        dataMap.put("dkxx",dkxx);
//        String cgqzs[] =  String.valueOf(dkxx.get(0).get("CGQZ")).split(",");
//        sql = "select avg(t.sbsbh) as sbsbh,avg(t.dqsd) as dqsd,max(t.jqsj) as jqsj,avg(t.dqwd) dqwd,avg(t.trsd) trsd,avg(t.trwd) trwd,avg(t.gzqd) gzqd,avg(t.eyhtnd) eyhtnd from t_zz_wlwcgqsj t where t.sbsbh in ('!'";
//        for(int i = 0; i < cgqzs.length; i++){
//            sql += ",'" + cgqzs[i] + "'";
//        }
//        sql += ")";
//        Map<String,Object> cgqxx = DatabaseHandlerDao.getInstance().queryForMap(sql);
//        if(cgqxx.size() == 0) return dataMap;
        dataMap.put("cgqxx",((HashMap<String,Object>)searchWlwxx(dkxx.get(0).get("DKBH").toString())).get("cgqxx"));
        return dataMap;
    }
    /**
     * 根据区域编号查询地块信息并且是非休耕状态的种植单元中的地块
     * @return
     */
    public Map<String,Object> searchDkxx(String qybh){
        String sql = "select DKBH,DKMC,CGQZ from t_zz_dkxx where 1=1 and qybh = ?"+defaultCode() +" order by dkbh asc";
        List<Map<String,Object>> dkxx = DatabaseHandlerDao.getInstance().queryForMaps(sql,new String[]{qybh});
        Map<String,Object> dataMap = new HashMap<String, Object>();
        if(dkxx.size() == 0) return dataMap;
        dataMap.put("dkxx",dkxx);
//        sql = "select * from t_zz_wlwcgqsj t where t.sbsbh = '" + String.valueOf(dkxx.get(0).get("CGQZ")) + "'";
//        Map<String,Object> cgqxx = DatabaseHandlerDao.getInstance().queryForMap(sql);
//        if(cgqxx == null) return dataMap;
//        dataMap.put("cgqxx",cgqxx);
        return dataMap;
    }

    /**
     * 工作台：根据地块编号查询物联网作息
     */
    public Object searchWlwxx(String dkbh){

        String sql = "select DKBH,DKMC,CGQZ from t_zz_dkxx where 1=1 and dkbh = ?"+defaultCode() + "order by dkbh asc";
        Map<String,Object> dkxx = DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{dkbh});
        String cgqzs[] =  String.valueOf(dkxx.get("CGQZ")).split(",");

        //求不同设备识别号最近时间显示指数的平均值。
        sql =  "select round(avg(dqsd),1) as dqsd,max(jqsj) as jqsj,round(avg(dqwd),1) as dqwd,round(avg(trsd),1) as trsd,round(avg(trwd),1) as trwd,round(avg(gzqd),1) as gzqd,round(avg(eyhtnd),1) as eyhtnd from t_zz_wlwcgqsj a,(select sbsbh,max(jqsj) as maxjqsj from t_zz_wlwcgqsj where sbsbh in (";
        for(int i = 0; i < cgqzs.length; i++){
            sql += "'" + cgqzs[i] + "'";
            if(cgqzs.length>1 && i<cgqzs.length-1){
                sql +=",";
            }
        }
        sql += ") group by sbsbh) b where a.sbsbh = b.sbsbh and a.jqsj = b.maxjqsj";
        Map<String,Object> cgqxx = DatabaseHandlerDao.getInstance().queryForMap(sql);
        cgqxx.put("SBSBH",dkxx.get("CGQZ"));
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("cgqxx",cgqxx);
        return dataMap;
    }

    /**
     * 查看操作规范
     * @return
     */
    public Map<String,Object> searchCzgf(){
        String sql = "select *\n" +
                "  from (select t.*\n" +
                "          from t_zz_czgfgl t\n" +
                "         where 1 = 1\n" + defaultCode() +
                "         order by scsj desc)\n" +
                " where rownum < 6\n";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }

    public String check(){

        return null;
    }


    public Map<String,Object> getGridTypeData(List<Map<String,Object>> list){
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data",list);
        return dataMap;
    }

    public String defaultCode(){
        String code = SerialNumberUtil.getInstance().getCompanyCode();
        return AppDefineUtil.RELATION_AND+" qybm = '"+code+"' "+ AppDefineUtil.RELATION_AND +" is_delete <> '1'";
    }

    public Object serchFarmingInfo(){
        StringBuilder sql ;
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        sql = new StringBuilder("select count(1) as ct,'1' as lx,max(zt) as zt from v_zz_nsxgz t where t.NSXLX = '1' and t.qybm = '" + qybm + "' and zt = '2'" +
                " union select count(1) as ct,'1' as lx,max(zt) as zt from v_zz_nsxgz t where t.NSXLX = '1' and t.qybm = '" + qybm + "' and zt = '3'" +
                " union select count(1) as ct,'2' as lx,max(zt) as zt from v_zz_nsxgz t where t.NSXLX = '2' and t.qybm = '" + qybm + "' and zt = '2'" +
                " union select count(1) as ct,'2' as lx,max(zt) as zt from v_zz_nsxgz t where t.NSXLX = '2' and t.qybm = '" + qybm + "' and zt = '3'" +
                " union select count(1) as ct,'3' as lx,max(zt) as zt from v_zz_nsxgz t where t.NSXLX = '3' and t.qybm = '" + qybm + "' and zt = '2'" +
                " union select count(1) as ct,'3' as lx,max(zt) as zt from v_zz_nsxgz t where t.NSXLX = '3' and t.qybm = '" + qybm + "' and zt = '3'" +
                " union select count(1) as ct,'4' as lx,max(zt) as zt from v_zz_nsxgz t where t.NSXLX = '4' and t.qybm = '" + qybm + "' and zt = '2'" +
                " union select count(1) as ct,'4' as lx,max(zt) as zt from v_zz_nsxgz t where t.NSXLX = '4' and t.qybm = '" + qybm + "' and zt = '3'" +
                " union select count(1) as ct,'5' as lx,max(zt) as zt from v_zz_nsxgz t where t.NSXLX = '5' and t.qybm = '" + qybm + "' and zt = '2'" +
                " union select count(1) as ct,'5' as lx,max(zt) as zt from v_zz_nsxgz t where t.NSXLX = '5' and t.qybm = '" + qybm + "' and zt = '3'" +
                " union select count(1) as ct,'6' as lx,max(zt) as zt from v_zz_nsxgz t where t.NSXLX = '6' and t.qybm = '" + qybm + "' and zt = '2'" +
                " union select count(1) as ct,'6' as lx,max(zt) as zt from v_zz_nsxgz t where t.NSXLX = '6' and t.qybm = '" + qybm + "' and zt = '3'" +
                " union select count(1) as ct,'7' as lx,max(zt) as zt from v_zz_nsxgz t where t.NSXLX = '7' and t.qybm = '" + qybm + "' and zt = '2'" +
                " union select count(1) as ct,'7' as lx,max(zt) as zt from v_zz_nsxgz t where t.NSXLX = '7' and t.qybm = '" + qybm + "' and zt = '3'" );
        List<Map<String, Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString());
        return dataList;
    }
}
