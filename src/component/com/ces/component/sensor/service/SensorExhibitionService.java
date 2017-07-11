package com.ces.component.sensor.service;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.database.Database;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Synge on 2015/10/26.
 */
@Component
public class SensorExhibitionService {

    /**
     * 工作量
     *
     * @return
     */
    public Map<String, Object> getWorkLoad() {
        String czsjwhereCondition = getDateCondition("GZLTJZQ", "czsj");
        String bzsjwhereCondition = getDateCondition("GZLTJZQ", "bzsj");
        String cssjwhereCondition = getDateCondition("GZLTJZQ", "cssj");
        String bzsql = "select count(t.id) as bzcs from t_zz_scbz t where " + czsjwhereCondition;
        String ggsql = "select count(t.id) as ggcs from t_zz_scgg t where " + czsjwhereCondition;
        String sfsql = "select count(t.id) as sfcs from t_zz_scsf t where " + czsjwhereCondition;
        String yysql = "select count(t.id) as yycs from t_zz_scyy t where " + czsjwhereCondition;
        String ccsql = "select count(t.id) as cccs from t_zz_sccc t where " + czsjwhereCondition;
        String cssql = "select count(t.id) as cscs from t_zz_sccs t where " + czsjwhereCondition;
        String dyzssql = "select sum(dyzs) as dyzs from t_zz_csnzwxq where pid in (select t.id from t_zz_csgl t where " + cssjwhereCondition + ")";
        String bzslsql = "select sum(cpsl) as cpsl from t_zz_bzgl where " + bzsjwhereCondition;
        Map<String, Object> bz = DatabaseHandlerDao.getInstance().queryForMap(bzsql);
        Map<String, Object> gg = DatabaseHandlerDao.getInstance().queryForMap(ggsql);
        Map<String, Object> sf = DatabaseHandlerDao.getInstance().queryForMap(sfsql);
        Map<String, Object> yy = DatabaseHandlerDao.getInstance().queryForMap(yysql);
        Map<String, Object> cc = DatabaseHandlerDao.getInstance().queryForMap(ccsql);
        Map<String, Object> cs = DatabaseHandlerDao.getInstance().queryForMap(cssql);
        Map<String, Object> dy = DatabaseHandlerDao.getInstance().queryForMap(dyzssql);
        Map<String, Object> bzsl = DatabaseHandlerDao.getInstance().queryForMap(bzslsql);
        Map<String, Object> result = new HashMap<String, Object>();
        result.putAll(bz);
        result.putAll(gg);
        result.putAll(sf);
        result.putAll(yy);
        result.putAll(cc);
        result.putAll(cs);
        result.putAll(dy);
        result.putAll(bzsl);
        return result;
    }

    /**
     * 销售量
     *
     * @return
     */
    public List<Map<String, Object>> getSalesVolume() {
        String ccsjwhereCondition = getDateCondition("XSLTJZQ", "ccsj");
        Calendar calendar = Calendar.getInstance();
        String sql = "select * from (select * from (select t.pzbh, max(t.pz) as pz, sum(t.cczl) as cczl\n" +
                "  from t_zz_ccshxx t\n" +
                " where t.pid in (select id from t_zz_ccgl where " + ccsjwhereCondition + ")\n" +
                " group by pzbh) where rownum < 11) order by cczl desc";
        List<Map<String, Object>> mapList = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return mapList;
    }

    /**
     * 满意度调查
     *
     * @return
     */
    public List<Map<String, Object>> getSatisfaction() {
        String lysjjwhereConditon = getDateCondition("MYDTJZQ", "lysj");
        String sql = "select cpmyd,count(*) as cs from t_zz_ly where " + lysjjwhereConditon + " group by cpmyd order by cpmyd asc";
        List<Map<String,Object>> mapList = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return mapList;
    }

    /**
     * 安全性
     *
     * @return
     */
    public List<Map<String, Object>> getSafety() {
        Calendar calendar = Calendar.getInstance();
        String sql = "select round(t1.fine_weight / decode(t2.total_weight,0,1,t2.total_weight), 4) * 100 || '%' as fine_percentage,t1.mon\n" +
                "  from (select sum(zl) as fine_weight, s.mon\n" +
                "          from t_zz_csnzwxq t,\n" +
                "               (select id, substr(cssj, 0, 7) as mon\n" +
                "                  from t_zz_csgl\n" +
                "                 where cssj like '" + calendar.get(Calendar.YEAR) + "%') s\n" +
                "         where t.wtbj = '0'\n" +
                "           and t.pid = s.id\n" +
                "         group by s.mon) t1,\n" +
                "       (select sum(zl) as total_weight, s.mon\n" +
                "          from t_zz_csnzwxq t,\n" +
                "               (select id, substr(cssj, 0, 7) as mon\n" +
                "                  from t_zz_csgl\n" +
                "                 where cssj like '" + calendar.get(Calendar.YEAR) + "%') s\n" +
                "         where t.pid = s.id group by s.mon) t2 where t1.mon = t2.mon order by t1.mon asc";
        List<Map<String, Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return maps;
    }

    /**
     * 获取日期条件sql语句
     *
     * @param tjlx 统计类型：GZLTJZQ,XSLTJZQ,MYDTJZQ
     * @param zdmc 字段名称
     * @return
     */
    private String getDateCondition(String tjlx, String zdmc) {
        String zqSql = "select sjmc from t_common_sjlx_code where lxbm='" + tjlx + "' and sjbm='TJZQ'";
        Map<String, Object> zqMap = DatabaseHandlerDao.getInstance().queryForMap(zqSql);
        Date d = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        Calendar cl = Calendar.getInstance();
        cl.add(Calendar.DAY_OF_MONTH, -Integer.parseInt(zqMap.get("SJMC").toString()));
        String now = df.format(d);
        String begin = df.format(cl.getTime());
        String whereCondition = " to_date(substr(" + zdmc + ",0,10),'yyyy-mm-dd')<=to_date('" + now + "','yyyy-mm-dd') and to_date(substr(" + zdmc + ",0,10),'yyyy-mm-dd')>to_date('" + begin + "','yyyy-mm-dd') ";
        return whereCondition;
    }

    /**
     * 根据地块编号获取物联网数据
     * @param dkbh
     * @return
     */
    public Map<String, Object> getIotByDkbh(String dkbh) {
        String sbbhsql = "select t.cgqz from t_zz_dkxx t where t.dkbh = ?";
        Map<String,Object> map = DatabaseHandlerDao.getInstance().queryForMap(sbbhsql,new Object[]{dkbh});
        if (null==map||map.isEmpty()) {
            return null;
        }
        String[] cgqzs = map.get("CGQZ").toString().split(",");
        String condition = "('!'";
        for (String cgqz : cgqzs) {
            condition += ",'"+cgqz+"'";
        }
        condition += ")";
        String sql = "select max(jqsj) as jqsj,\n" +
                "       decode(avg(dqsd), '0', '0.000', to_char(avg(dqsd), 'fm999999999999999.000')) as dqsd,\n" +
                "       decode(avg(dqwd), '0', '0.000', to_char(avg(dqwd), 'fm999999999999999.000')) as dqwd,\n" +
                "       decode(avg(trsd), '0', '0.000', to_char(avg(trsd), 'fm999999999999999.000')) as trsd,\n" +
                "       decode(avg(trwd), '0', '0.000', to_char(avg(trwd), 'fm999999999999999.000')) as trwd,\n" +
                "       decode(avg(gzqd), '0', '0.000', to_char(avg(gzqd), 'fm999999999999999.000')) as gzqd,\n" +
                "       decode(avg(eyhtnd), '0', '0.000', to_char(avg(eyhtnd), 'fm999999999999999.000')) as eyhtnd\n" +
                "  from (select s.sbsbh,\n" +
                "               s.jqsj,\n" +
                "               s.dqsd,\n" +
                "               s.dqwd,\n" +
                "               s.trsd,\n" +
                "               s.trwd,\n" +
                "               s.gzqd,\n" +
                "               s.eyhtnd\n" +
                "          from t_zz_wlwcgqsj s,\n" +
                "               (select sbsbh, max(jqsj) as jqsj\n" +
                "                  from t_zz_wlwcgqsj t\n" +
                "                 where sbsbh in "+condition+"\n" +
                "                 group by sbsbh) t1\n" +
                "         where s.sbsbh = t1.sbsbh\n" +
                "           and s.jqsj = t1.jqsj)\n";
        Map<String,Object> result = DatabaseHandlerDao.getInstance().queryForMap(sql);
        result.put("CGQZ",map.get("CGQZ").toString());
        return result;
    }

    /**
     * 视频账号
     * @param dkbh
     * @return
     */
    public Map<String, Object> getVideoAccount(String dkbh) {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("IP","192.10.33.249");
        map.put("account","admin");
        map.put("password","Admin123");
        return map;
    }

}
