package com.ces.component.trace.service;

import ces.coral.lang.StringUtil;
import com.ces.component.trace.dao.TraceInfoDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.component.trace.utils.DataDictionaryUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.UUIDGenerator;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.service.StringIDDefineDaoService;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Synge on 2015/9/29.
 */
@Component
public class TraceInfoService extends StringIDDefineDaoService<StringIDEntity, TraceInfoDao> {
    /**
     * 根据追溯码获得对应的商品信息：产品或散货信息
     *
     * @param zsm
     * @return
     */
    public Map<String, Object> getProductInfo(String zsm) {
        Map ltMap = queryProInfoByLtzsm(zsm);
        if (ltMap != null && !ltMap.isEmpty()) {//流通追溯码查询
            return ltMap;
        } else {//产品追溯码查询
            Map cpMap = queryProInfoByCpzsm(zsm);
            if (cpMap != null && !cpMap.isEmpty()) {
                return cpMap;
            }
        }
        return null;
    }

    /**
     * 查询预包装信息
     *
     * @param zsm
     * @return
     */
    public Map queryYbzcpxx(String zsm) {
        String bzcpSql = " select distinct t.CPMC,t.CPBH,t.BZLSH,c.TPBCMC,t.QYBM " +
                " from t_zz_bzgl t,T_ZZ_CPXXGL c,t_zz_bzglplxx p " +
                " where p.bzpczsm = ? and t.id = p.pid and t.cpbh = c.cpbh and t.qybm = c.qybm";
        Map<String, Object> bzcpMap = DatabaseHandlerDao.getInstance().queryForMap(bzcpSql, new String[]{zsm});
        String sql = "select distinct s.pzbh, p.tpbcmc, p.zbid, b.cspch, s.pz CPMC\n" +
                "  from t_zz_xpzxx s, t_zz_pltp p, T_ZZ_BZGLPLXX b\n" +
                "  where s.id = p.zbid\n" +
                "  and b.pzbh=s.pzbh and b.qybm = s.qybm" +
                " and b.bzpczsm=?";
        List<Map<String, Object>> datamap = DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{zsm});
        if (datamap.size() > 0 && datamap != null) {
            bzcpMap.put("childs", datamap);
        }
        return bzcpMap;
    }

    /**
     * 根据追溯码获取起始节点
     *
     * @param zsm
     * @return
     */
    public Map queryStartNode(String zsm) {
        String sql = "SELECT S.* FROM (SELECT T.* FROM T_CSPT_JYXX T START WITH T.ZSM=? " +
                " CONNECT BY   PRIOR T.JYPZH  =  T.ZSM) S WHERE S.JYPZH IS NULL";
        return DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{zsm});
    }

    /**
     * 根据产品追溯码查询产品信息
     *
     * @param zsm 产品追溯码
     * @return
     */
    public Map queryProInfoByCpzsm(String zsm) {
        String bzcpSql = "select distinct t.CPMC, t.CPBH, t.BZLSH, c.TPBCMC, t.QYBM\n" +
                "  from t_zz_bzgl t\n" +
                "  left join T_ZZ_CPXXGL c\n" +
                "    on t.cpbh = c.cpbh\n" +
                "   and t.qybm = c.qybm\n" +
                " where t.cpzsm = ?";
        Map<String, Object> bzcpMap = DatabaseHandlerDao.getInstance().queryForMap(bzcpSql, new String[]{zsm});
        //判断是否存在大礼包信息
        if (bzcpMap != null && !bzcpMap.isEmpty()) {
            //如果是大礼包，则需要查询相关的礼包中品种的信息
            String bzlsh = String.valueOf(bzcpMap.get("BZLSH"));
            String qybm = String.valueOf(bzcpMap.get("QYBM"));
            //根据大礼包ID获得配料信息
            String bzplSql = "select distinct s.pzbh, p.tpbcmc, p.zbid, m.cspch, s.pz CPMC\n" +
                    "  from\n" +
                    "       (select t.pzbh, t.pz, t.cspch, bz.qybm\n" +
                    "          from T_ZZ_BZGLPLXX t, T_ZZ_BZGL bz\n" +
                    "         where t.pid = bz.id\n" +
                    "           and bz.BZLSH = '" + bzlsh + "'\n" +
                    "           and bz.qybm = '" + qybm + "') m left join t_zz_xpzxx s on m.pzbh = s.pzbh\n" +
                    "   and m.qybm = s.qybm left join t_zz_pltp p on s.id = p.zbid";
            //String bzplSql = "select b.pzbh,b.pz CPMC,b.CSPCH,p.TPBCMC from T_ZZ_BZGLPLXX b,t_zz_xpzxx s,t_zz_pltp p where b.BZLSH=? and b.pzbh= s.pzbh and b.qybm=s.qybm and s.id =p.zbid";
            List<Map<String, Object>> datamap = DatabaseHandlerDao.getInstance().queryForMaps(bzplSql);
            bzcpMap.put("childs", datamap);
            return bzcpMap;
        }
        //查询预包装信息
        Map map = queryYbzcpxx(zsm);
        if (map != null && !map.isEmpty()) {
            return map;
        }
        String shSql = " \n" +
                "select distinct t.pz CPMC, t.pzbh, t.cpzsm, t.pch cspch, p.TPBCMC\n" +
                "  from t_zz_ccshxx t\n" +
                "  left join t_zz_xpzxx s\n" +
                "    on s.pzbh = t.pzbh\n" +
                "   and t.qybm = s.qybm\n" +
                "  left join t_zz_pltp p\n" +
                "    on s.id = p.zbid\n" +
                " where t.cpzsm = ?";
        Map<String, Object> shMap = DatabaseHandlerDao.getInstance().queryForMap(shSql, new String[]{zsm});
        //判读是否存在散货数据
        if (shMap != null && !shMap.isEmpty()) {
            return shMap;
        }
        return null;
    }

    /**
     * 根据流通追溯码查询产品信息
     *
     * @param zsm 流通追溯码
     * @return
     */
    public Map queryProInfoByLtzsm(String zsm) {
        Map startNode = queryStartNode(zsm);
        if (startNode != null && !startNode.isEmpty()) {
            //包装产品获取产品追溯码
            String sql = "SELECT T.CPZSM,T.ZSM FROM T_ZZ_CCBZCPXX T WHERE T.ZSM = ?";
            Map map = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{startNode.get("ZSM").toString()});
            String cpzsm;
            if (map != null && !map.isEmpty()) {
                cpzsm = map.get("CPZSM").toString();
            } else {//若不为包装产品则判断为散货
                cpzsm = startNode.get("ZSM").toString();
            }
            return queryProInfoByCpzsm(cpzsm);
        } else {
            return null;
        }
    }

    /**
     * 根据企业编码获得对应的企业相关的信息
     *
     * @param cspch
     * @return
     */
    public Map<String, Object> searchCompInfo(String cspch) {
        String sql = "select t.qybm,\n" +
                "       t.qymc,\n" +
                "       t.lsxzq,\n" +
                "       t.zcdz,\n" +
                "       t.jydz,\n" +
                "       t.lxdh,\n" +
                "       t.fddb,\n" +
                "       z.zldj,\n" +
                "       c.cssj,\n" +
                "       t.cdms,\n" +
                "       s.tplj,\n" +
                "       t.rzqk as qyrz,\n" +
                "       p.rzlx as pzrz\n" +
                "  from t_zz_CDDA t\n" +
                "  left join t_common_qytp s\n" +
                "    on t.qybm = s.qybm\n" +
                "  left join T_ZZ_CSNZWXQ z\n" +
                "    on t.qybm = z.qybm\n" +
                "  left join T_ZZ_CSgl c\n" +
                "    on c.id = z.pid\n" +
                "  left join t_zz_xpzxx p\n" +
                "    on p.pzbh = c.pzbh and p.qybm = c.qybm\n" +
                " where z.pch = '" + cspch + "' and rownum = 1\n";
        List<Map<String, Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        if (dataList != null && !dataList.isEmpty())
            return dataList.get(0);
        return null;
    }

    /**
     * 根据企业编码获得相关认证信息
     *
     * @param qybm
     * @return
     */
    public List<Map<String, Object>> searchRzxx(String qybm) {
        String sql = "select RZMC,TP from T_ZZ_RZXX t where t.qybm =? and t.is_delete <> '1'";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new String[]{qybm});
    }


    /**
     * 根据采收批次号和追溯码查询相关的作业信息
     *
     * @param cspch
     * @return
     */
    public Map<String, Object> searchZyxx(String cspch, String zsm) {
        //作业信息--------------------------------------------------------------------
        List<Code> clist = DataDictionaryUtil.getInstance().getDictionaryData("CZLX");
        Map<String, Object> dataMap = new HashMap<String, Object>();
        for (Code code : clist) {
            String value = code.getValue();
            if (value.equalsIgnoreCase("SCCS")) continue;//过滤采收信息
            String sql = "select i.czr, i.jssj czsj, j.trptym, j.yl, j.nszyxmc, j.aqq\n" +
                    "  from T_ZZ_CZJL i,\n" +
                    "       (select n.id, m.cssj, k.trptym, k.yl, n.nszyxmc, tj.aqq\n" +
                    "          from (select s.id, c.CSSJ, t.qybm\n" +
                    "                  from T_ZZ_CSNZWXQ t, T_ZZ_CSGL c, T_ZZ_SCDA s\n" +
                    "                 where t.pch = '" + cspch + "'\n" +
                    "                   and t.pid = c.id\n" +
                    "                   and s.scdabh = c.scdabh) m\n" +
                    "          left join T_ZZ_" + value + " n\n" +
                    "            on n.pid = m.id\n" +
                    "          left join t_zz_" + value + "trp k\n" +
                    "            on n.id = k.pid\n" +
                    "          left join t_zz_trpjbxx tj\n" +
                    "            on tj.trpbh = k.trpmc and m.qybm = tj.qybm\n" +
                    "        ) j\n" +
                    " where i.pid = j.id\n" +
                    " order by i.jssj desc\n";
            List<Map<String, Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(sql);
            if (dataList != null && !dataList.isEmpty()) {
                dataMap.put(value, dataList);
            } else {
                dataMap.put(value, "");
            }
        }
        //获取分拣信息-----------------------------------------------------
        String fjSql = "select cssj as czsj, '采收' as nszyxmc\n" +
                "  from t_zz_csgl\n" +
                " where id = (select pid from t_zz_csnzwxq where pch = ?)\n";
        List<Map<String, Object>> fjData = DatabaseHandlerDao.getInstance().queryForMaps(fjSql, new Object[]{cspch});
        if (fjData != null && !fjData.isEmpty()) {
            dataMap.put("sccs", fjData);
        } else {
            dataMap.put("sccs", "");
        }
        //若为流通追溯码，获取其作业追溯码 及包装的出场追溯码----------------
        String cpzsm = "";
        String bzcczsm = "";
        Map startNode = queryStartNode(zsm);
        if (startNode != null && !startNode.isEmpty()) { //用追溯码查询 有起始节点 则为已经出场（散货或包装）
            //包装产品获取产品追溯码
            String sql = "SELECT T.CPZSM,T.ZSM FROM T_ZZ_CCBZCPXX T WHERE T.ZSM = ?";
            Map map = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{startNode.get("ZSM").toString()});
            if (map != null && !map.isEmpty()) {
                cpzsm = map.get("CPZSM").toString();
                bzcczsm = map.get("ZSM").toString();
            } else {//若不为包装产品则判断为散货
                cpzsm = startNode.get("ZSM").toString();
            }
        } else { //用追溯码查询 无起始节点 则只能为包装产品
            cpzsm = zsm;
        }
        //加工包装信息-----------------------------------------------------
        String jgbzSQL = "select bzlsh,bzsj from t_zz_bzgl where cpzsm = ? and rownum = 1";
        Map<String, Object> jgbzList = DatabaseHandlerDao.getInstance().queryForMap(jgbzSQL, new Object[]{cpzsm});
        dataMap.put("jgbz", jgbzList.size() == 0 ? "" : jgbzList);
        //配送信息
        //根据追溯码信息查询是散货的配送还是产品的配送信息
//        String psxxSQL =  " select c.pszrr,c.ccsj,c.sfdh " +
//                "  from t_zz_ccbzcpxx t, T_ZZ_CCGL c " +
//                " where t.cpzsm = '"+cpzsm+"' " +
//                "   and t.pid = c.id order by c.ccsj desc";
//        List<Map<String, Object>> psxxList = DatabaseHandlerDao.getInstance().queryForMaps(psxxSQL);
//        //当前追溯信息是否为包装产品追溯信息
//        if(psxxList == null || psxxList.isEmpty()) {
//            //当前追溯信息为散货
//            psxxSQL =  " select c.pszrr,c.ccsj,c.sfdh " +
//                    "  from t_zz_ccshxx t, T_ZZ_CCGL c " +
//                    " where t.cpzsm = '"+cpzsm+"' " +
//                    "   and t.pid = c.id order by c.ccsj desc";
//            psxxList = DatabaseHandlerDao.getInstance().queryForMaps(psxxSQL);
//        }
//        if(psxxList != null && !psxxList.isEmpty()) {
//            dataMap.put("psxx", psxxList);
//        } else {
//            dataMap.put("psxx", "");
//        }

//        List<String> ccpchs = new ArrayList<String>();
//        for (Map<String,Object> psxx : psxxList) {
//            ccpchs.add(psxx.get("SFDH") == null ? "" : psxx.get("SFDH").toString());
//        }
        /*******************获取产品历程信息***********************/
        Map nbmMap = new HashMap();
        Map lcMap = new HashMap();
        if (startNode != null && !startNode.isEmpty()) {
            List li = queryListByJhpch(startNode.get("JHPCH").toString());
            lcMap = getFxList(zsm, li, nbmMap);
        }
        /********************************************************/
        //查询种植检测信息
        List jcList = searchJcxx(cspch);
        dataMap.put("lcxx", lcMap.size() == 0 ? "" : lcMap);
        jcList.addAll(queryJcjyxxList(nbmMap));
        dataMap.put("jcxx", jcList.size() == 0 ? "" : jcList);//检测信息
        return dataMap;
    }

    /**
     * 根据jhpch获取某一批次的流通信息
     *
     * @param jhpch
     * @return list
     */
    public List queryListByJhpch(String jhpch) {
        String sql1 = "SELECT T.* FROM T_CSPT_JYXX T WHERE T.JHPCH = ?";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql1, new Object[]{jhpch});
    }

    private List<Map<String, Object>> searchCsrk(List<String> ccpchs) {
        String condition = "('!'";
        for (String ccpch : ccpchs) {
            condition += ",'" + ccpch + "'";
        }
        condition += ")";
        String sql = "select t.csmc ,t.jcrq from  t_cs_scjcxx t where jhpch in " + condition + " order by jcrq asc";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql);
    }

    /**
     * 根据采收批次号查询相关物联网信息
     *
     * @param cspch
     * @return
     */
    public Map<String, Object> getWlwxx(String cspch) {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//可以方便地修改日期格式
        String formatNow = dateFormat.format(now);
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.add(c.DATE, -1);
        Date early = c.getTime();
        String formatEarly = dateFormat.format(early);
        //获取地块编号
        String dkSql = "SELECT t.dkbh FROM T_ZZ_CSGL t, T_ZZ_CSNZWXQ s where t.id = s.pid and s.pch = ?";
        Map<String, Object> dkMap = DatabaseHandlerDao.getInstance().queryForMap(dkSql, new Object[]{cspch});
        if (null == dkMap || null == dkMap.get("DKBH") || dkMap.get("DKBH").equals("")) {
            return null;
        }
        String dkbh = dkMap.get("DKBH").toString();
        //获取传感器组
        String sbbhsql = "select t.cgqz from t_zz_dkxx t where t.dkbh = ?";
        Map<String, Object> map = DatabaseHandlerDao.getInstance().queryForMap(sbbhsql, new Object[]{dkbh});
        if (null == map || map.isEmpty()) {
            return null;
        }
        String[] cgqzs = {};
        if (map.get("CGQZ") != null) {
            cgqzs = map.get("CGQZ").toString().split(",");
        }
        String condition = "('!'";
        for (String cgqz : cgqzs) {
            condition += ",'" + cgqz + "'";
        }
        condition += ")";

//        String sql = "select decode(avg(t.dqsd),\n" +
//                "              '0',\n" +
//                "              '0.000',\n" +
//                "              to_char(avg(t.dqsd), 'fm999999999999999.000')) as dqsd,\n" +
//                "       decode(avg(t.dqwd),\n" +
//                "              '0',\n" +
//                "              '0.000',\n" +
//                "              to_char(avg(t.dqwd), 'fm999999999999999.000')) as dqwd,\n" +
//                "       decode(avg(t.trsd),\n" +
//                "              '0',\n" +
//                "              '0.000',\n" +
//                "              to_char(avg(t.trsd), 'fm999999999999999.000')) as trsd,\n" +
//                "       decode(avg(t.trwd),\n" +
//                "              '0',\n" +
//                "              '0.000',\n" +
//                "              to_char(avg(t.trwd), 'fm999999999999999.000')) as trwd,\n" +
//                "       decode(avg(t.gzqd),\n" +
//                "              '0',\n" +
//                "              '0.000',\n" +
//                "              to_char(avg(t.gzqd), 'fm999999999999999.000')) as gzqd,\n" +
//                "       decode(avg(t.eyhtnd),\n" +
//                "              '0',\n" +
//                "              '0.000',\n" +
//                "              to_char(avg(t.eyhtnd), 'fm999999999999999.000')) as eyhtnd\n" +
//                "  from t_zz_wlwcgqsj t\n" +
//                " where t.sbsbh in " + condition + "\n";
////                "   and t.jqsj > '"+formatEarly+"'\n";
        String sql = "select to_char(avg(t.dqsd), 'fm999999999999999') as dqsd,\n" +
                "       \n" +
                "       to_char(avg(t.dqwd), 'fm999999999999999') as dqwd,\n" +
                "       \n" +
                "       to_char(avg(t.trsd), 'fm999999999999999') as trsd,\n" +
                "       \n" +
                "       to_char(avg(t.trwd), 'fm999999999999999') as trwd,\n" +
                "       \n" +
                "       to_char(avg(t.gzqd), 'fm999999999999999') as gzqd,\n" +
                "       \n" +
                "       to_char(avg(t.eyhtnd), 'fm999999999999999') as eyhtnd\n" +
                "  from t_zz_wlwcgqsj t\n" +
                " where t.sbsbh in " + condition;
        Map<String, Object> allDayMap = DatabaseHandlerDao.getInstance().queryForMap(sql);
        //折线图数据
        allDayMap.put("lineChart", getWlwsjByDkbh(condition));
        return allDayMap;
    }

    /**
     * @param condition 传感器组
     * @return
     */
    private List<Map<String, Object>> getWlwsjByDkbh(String condition) {
        String sql = "select decode(avg(t.dqsd),\n" +
                "              '0',\n" +
                "              '0.000',\n" +
                "              to_char(avg(t.dqsd), 'fm999999999999999.000')) as dqsd,\n" +
                "       decode(avg(t.dqwd),\n" +
                "              '0',\n" +
                "              '0.000',\n" +
                "              to_char(avg(t.dqwd), 'fm999999999999999.000')) as dqwd,\n" +
                "       decode(avg(t.trsd),\n" +
                "              '0',\n" +
                "              '0.000',\n" +
                "              to_char(avg(t.trsd), 'fm999999999999999.000')) as trsd,\n" +
                "       decode(avg(t.trwd),\n" +
                "              '0',\n" +
                "              '0.000',\n" +
                "              to_char(avg(t.trwd), 'fm999999999999999.000')) as trwd,\n" +
                "       decode(avg(t.gzqd),\n" +
                "              '0',\n" +
                "              '0.000',\n" +
                "              to_char(avg(t.gzqd), 'fm999999999999999.000')) as gzqd,\n" +
                "       decode(avg(t.eyhtnd),\n" +
                "              '0',\n" +
                "              '0.000',\n" +
                "              to_char(avg(t.eyhtnd), 'fm999999999999999.000')) as eyhtnd,\n" +
                "              substr(t.jqsj,0,10) as jqsj\n" +
                "  from t_zz_wlwcgqsj t\n" +
                " where t.sbsbh in " + condition + "\n" +
                " group by substr(t.jqsj,0,10)\n" +
                " order by substr(t.jqsj,0,10) asc";
        List<Map<String, Object>> result = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return result;
    }

    /**
     * Map<String, Object> 强制转换为 Map<String, String>
     *
     * @param dataMap Map<String,String>
     * @return
     */
    public Map<String, String> convertMap(Map<String, Object> dataMap) {
        Set<String> keys = dataMap.keySet();
        Map<String, String> map = new HashMap<String, String>();
        for (String key : keys) {
            map.put(key, String.valueOf(dataMap.get(key)));
        }
        return map;
    }

    /**
     * 根据采收批次查询销售的门店信息
     *
     * @param cspch
     * @return
     */
    public List<Map<String, Object>> searchXsmdxx(String cspch) {
        String sql = "select MDMC,TP from T_ZZ_KHMDXX t where t.id in( select z.JRMD from T_ZZ_PCJDXX z where z.cspch = ? )";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new String[]{cspch});
    }


    /**
     * 根据采收批次号获得对应检测信息
     *
     * @param cspch
     * @return
     */
    public List<Map<String, Object>> searchJcxx(String cspch) {
        String sql = "select tb.tpbcmc, ta.*\n" +
                "  from (select s.id, s.JCXM, s.JCSJ, s.JCDW AS JCDD, s.JCJG, s.JCR AS JCRY\n" +
                "          from t_zz_scjcjl s\n" +
                "         where s.cslsh in (select c.cslsh\n" +
                "                             from t_zz_csnzwxq t, t_zz_csgl c\n" +
                "                            where t.pch = '" + cspch + "'\n" +
                "                              and c.id = t.pid)\n" +
                "         order by s.JCSJ desc) ta\n" +
                "  left join t_zz_scjcjl tb\n" +
                "    on ta.id = tb.zbid\n";
        List<Map<String, Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return dataList;
    }

    /**
     * 保存留言
     *
     * @param lxfs
     * @param yjhjy
     * @param cpmyd
     */
    //todo to be deleted
    public void saveSuggestion(String lxfs, String yjhjy, String cpmyd) {
        String id = UUIDGenerator.uuid();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sql = "insert into t_zz_ly (id,lxfs,yjhjy,cpmyd,lysj) values (?,?,?,?,?)";
        DatabaseHandlerDao.getInstance().executeSql(sql, new Object[]{id, lxfs, yjhjy, cpmyd, df.format(new Date())});
    }


    /**
     * @param zzyzpch
     * @return
     */
    public List<Map<String, Object>> getEndNode(String zzyzpch) {
        //企业编码
        String qybm = zzyzpch.substring(0, 9);
        //系统类型
        String xtlx = getZzYzxtlx(qybm);
        if (xtlx.equals("")) return new ArrayList<Map<String, Object>>();
        //种植养殖批次号
        if (xtlx.equals("2")) zzyzpch = zzyzpch.substring(9, zzyzpch.length());
        List<String> jhpchList = new ArrayList();
        List li = new ArrayList();
        jhpchList = queryJhpchByZzyzpch(zzyzpch, qybm, xtlx);
        String condition = "('!'";
        for (String jhpch : jhpchList) {
            condition += ",'" + jhpch + "'";
        }
        condition += ")";
        String sql = "select distinct t.zhbh as qybm, t.qymc, t.dz, t.csxx, t.sj\n" +
                "  from t_qypt_zhgl t\n" +
                " where t.zhbh in (select qybm\n" +
                "                    from T_CSPT_JYXX a,\n" +
                "                         \n" +
                "                         (select jypzh\n" +
                "                            from (select count(id) as ct, jypzh\n" +
                "                                    from T_CSPT_JYXX\n" +
                "                                   where jhpch in " + condition + "\n" +
                "                                   group by jypzh)\n" +
                "                           where jypzh is not null\n" +
                "                             and ct = 1) b\n" +
                "                   where zsm is null\n" +
                "                     and a.jypzh = b.jypzh)\n";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql);
    }

    /**
     * @param zzyzpch
     * @return
     */
    public Map getTree(String zzyzpch) {
        //企业编码
        String qybm = zzyzpch.substring(0, 9);
        //系统类型
        String xtlx = getZzYzxtlx(qybm);
        if (xtlx.equals("")) return new HashMap();
        //种植养殖批次号
        if (xtlx.equals("2")) zzyzpch = zzyzpch.substring(9, zzyzpch.length());
        List<String> jhpchList = new ArrayList();
        List li = new ArrayList();
        Map startNode = new HashMap();
        jhpchList = queryJhpchByZzyzpch(zzyzpch, qybm, xtlx);
        if (jhpchList.size() > 0) {
            for (String jhpch : jhpchList) {
                List list = queryListByJhpch(jhpch);
                startNode = getStart(jhpch, list);
                Map child = getChild(startNode, list);
                if (child != null && !child.isEmpty()) {
                    li.add(child);
                }
            }
            startNode.put("children", li);
        }
        return startNode;
    }

    /**
     * 递归查询子节点
     *
     * @param jyMap 父节点
     * @param li    剩余节点list
     * @return map父节点
     */
    public Map getChild(Map jyMap, List<Map> li) {
        Map jcMap = new HashMap();//进厂记录 唯一
        List<Map> outList = new ArrayList();//交易记录
        Iterator it = li.iterator();
        while (it.hasNext()) {//获取所有子记录（包括进场出厂记录）
            Map m = (Map) it.next();
            if (jyMap.get("ZSM").equals(m.get("JYPZH"))) {
                if (m.get("ZSM") == null) {//进厂记录
                    jcMap = m;
                } else {//交易记录
                    outList.add(m);
                }
            }
        }
        li.remove(jcMap);
        li.removeAll(outList);
        List childList = new ArrayList();
        if ("10".equals(jcMap.get("XTLX"))) {//判断是否为加工厂
            List<Map> jgCcxx = this.queryJgccPch(jcMap.get("JYPZH").toString());
            for (Map m : jgCcxx) {//记录子节点
                //获取出场批次
                List pcList = queryListByJhpch(m.get("PCH").toString());
                //获取加工初始节点
                Map startNode = getJgStartNode(m.get("PCH").toString(), m.get("ZSM").toString());
                Map childMap = getChild(startNode, pcList);
                if (!childMap.isEmpty())
                    childList.add(childMap);
            }
            jcMap.put("children", childList);
        } else {
            for (Map map1 : outList) {
                if (map1.get("JYPZH").equals(jcMap.get("JYPZH"))) {//获取进厂记录对应交易记录
                    Map map = getChild(map1, li);
                    if (map != null && !map.isEmpty())
                        childList.add(map);
                }
                jcMap.put("children", childList);
            }
        }
        return jcMap;
    }

    /**
     * 获取加工厂交易对应开始节点
     *
     * @param jhpch 进货批次号
     * @param zsm   追溯码
     * @return
     */
    public Map getJgStartNode(String jhpch, String zsm) {
        String sql = "SELECT T.* FROM T_CSPT_JYXX T WHERE T.JHPCH=? AND T.ZSM=?";
        return DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{jhpch, zsm});
    }

    /**
     * 正向追溯到加工厂，查询加工厂出场时生成批次号
     *
     * @param zsm 追溯码
     * @return
     */
    public List queryJgccPch(String zsm) {
        String sql = "SELECT T.PCH,T.ZSM FROM T_JG_CPCCXX T,T_JG_XLMXXX X,T_JG_YLHWXX Y WHERE T.DDBH=X.DDBH" +
                " AND Y.YLPCH = X.YLPC AND T.QYBM = X.QYBM AND T.QYBM = Y.QYBM AND Y.ZSPZH=? ";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{zsm});
    }

    /**
     * 获取起点
     *
     * @param jhpch
     * @param li
     * @return
     */
    public Map getStart(String jhpch, List<Map> li) {
        Map start = new HashMap();
        for (Map m : li) {
            if (m.get("JYPZH") == null && m.get("ZSM") != null) {
                start = m;
            }
        }
        return start;
    }

    /**
     * 获取对应系统类型
     *
     * @param qybm
     * @return
     */
    private String getZzYzxtlx(String qybm) {
        String sql = "SELECT XTLX FROM T_CSPT_JYXX T WHERE T.QYBM =? and rownum=1";
        Map map = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{qybm});
        return (String) map.get("XTLX");
    }

    public List<String> queryJhpchByZzyzpch(String zzyzpch, String qybm, String xtlx) {
        String sql = "";
        if ("1".equals(xtlx)) {//种植系统追溯批次号有企业编码前缀
            sql += "select *\n" +
                    "  from (select t.zspch from t_zz_ccshxx t where t.pch = ?)\n" +
                    "union (select t.zspch from t_zz_ccbzcpxx t where cpzsm in (select cpzsm\n" +
                    "         from t_zz_bzgl\n" +
                    "        where id in (select t.pid\n" +
                    "                       from t_zz_bzglplxx t\n" +
                    "                      where t.cspch = ?)))";
            return DatabaseHandlerDao.getInstance().queryForList(sql, new Object[]{zzyzpch, zzyzpch});
        } else if ("2".equals(xtlx)) {
            sql += "SELECT T.PCH FROM T_YZ_CLXX T WHERE T.YZPCH=? AND T.QYBM = ?";
            return DatabaseHandlerDao.getInstance().queryForList(sql, new Object[]{zzyzpch, qybm});
        } else return new ArrayList<String>();
    }

    /****************************
     * unity全景展示begin
     ****************************/
    public Map<String, Object> getUnitityTraceInfo(String cspch, String zsm) {
        String sql = "select t.qybm,\n" +
                "       t.scdabh,\n" +
                "       j.id as scdaid,\n" +
                "       t.dkbh,\n" +
                "       substr(t.cssj,0,10) as cssj,\n" +
                "       s.dkfzr,\n" +
                "       s.dkfzrbh,\n" +
                "       s.dkmc,\n" +
                "       s.mj,\n" +
                "       q.tplj as gzrytp\n" +
                "  from t_zz_csnzwxq p\n" +
                "  left join t_zz_csgl t\n" +
                "    on p.pid = t.id\n" +
                "  left join t_zz_dkxx s\n" +
                "    on t.dkbh = s.dkbh\n" +
                "  left join t_zz_gzryda q\n" +
                "    on s.dkfzrbh = q.gzrybh\n" +
                "  left join t_zz_scda j\n" +
                "    on t.scdabh = j.scdabh  \n" +
                " where p.pch = ?\n" +
                "   and t.qybm = q.qybm\n" +
                "   and t.qybm = s.qybm";
        Map<String, Object> basicInfo = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{cspch});
        if (basicInfo.isEmpty() || null == basicInfo) {
            return null;
        }
        //获取企业图片
        String sql1 = "select max(t.tplj) as qytp from T_COMMON_QYTP t where qybm=?";
        Map<String, Object> qytpInfo = DatabaseHandlerDao.getInstance().queryForMap(sql1, new Object[]{basicInfo.get("QYBM") == null ? "" : basicInfo.get("QYBM").toString()});
        basicInfo.put("QYTP", qytpInfo.get("QYTP"));
        //获取农事项操作信息
        String scdaid = basicInfo.get("SCDAID") == null ? "" : basicInfo.get("SCDAID").toString();
        basicInfo.put("bzxx", getNsxCzxx("bz", scdaid));
        basicInfo.put("ggxx", getNsxCzxx("gg", scdaid));
        basicInfo.put("sfxx", getNsxCzxx("sf", scdaid));
        basicInfo.put("yyxx", getNsxCzxx("yy", scdaid));
        basicInfo.put("ccxx", getNsxCzxx("cc", scdaid));
        basicInfo.put("csxx", getNsxCzxx("cs", scdaid));
        //获取最近的出场信息
        String cpzsm = zsm;
        Map startNode = queryStartNode(zsm);
        if (startNode != null && !startNode.isEmpty()) { //用追溯码查询 有起始节点 则为已经出场（散货或包装）
            //包装产品获取产品追溯码
            String cpSql = "SELECT T.CPZSM,T.ZSM FROM T_ZZ_CCBZCPXX T WHERE T.ZSM = ?";
            Map map = DatabaseHandlerDao.getInstance().queryForMap(cpSql, new Object[]{startNode.get("ZSM").toString()});
            if (map != null && !map.isEmpty()) {
                cpzsm = map.get("CPZSM").toString();
            } else {//若不为包装产品则判断为散货
                cpzsm = startNode.get("ZSM").toString();
            }
        } else { //用追溯码查询 无起始节点 则只能为包装产品
            cpzsm = zsm;
        }
        String ccSql = "select *\n" +
                "  from (select substr(s.ccsj,0,10) as ccsj, s.pszrr\n" +
                "          from t_zz_ccgl s\n" +
                "         where s.id in ((select t.pid\n" +
                "                           from t_zz_ccbzcpxx t\n" +
                "                          where t.cpzsm = ?) union\n" +
                "                        (select p.pid\n" +
                "                           from t_zz_ccshxx p\n" +
                "                          where p.cpzsm = ?))\n" +
                "         order by s.ccsj desc) t1\n" +
                " where rownum = 1\n";
        Map<String, Object> ccMap = DatabaseHandlerDao.getInstance().queryForMap(ccSql, new Object[]{cpzsm, cpzsm});
        ccMap.put("TYPE", "sell");
        basicInfo.put("cc", ccMap);

        return basicInfo;
    }

    //获取农事项信息
    private Map<String, Object> getNsxCzxx(String nsxlx, String scdaid) {
        String sql = "select *\n" +
                "  from (select o.trptym, o.yl, t1.*\n" +
                "          from (select s.id, s.nszyxmc,  substr(s.czsj,0,10) as czsj, k.czr, k.czrbh,g.tplj\n" +
                "                  from t_zz_sc" + nsxlx + " s\n" +
                "                  left join t_zz_czjl k\n" +
                "                    on s.id = k.pid\n" +
                "                  left join t_zz_gzryda g\n" +
                "                    on g.gzrybh = k.czrbh  \n" +
                "                 where s.czsj = (select max(t.czsj) as czsj\n" +
                "                                   from t_zz_sc" + nsxlx + " t\n" +
                "                                  where t.pid=?\n" +
                "                                    and t.czsj is not null) " +
                "                 and g.qybm = (select qybm from t_zz_scda where id = ?)) t1\n" +
                "          left join t_zz_sc" + nsxlx + "trp o\n" +
                "            on o.pid = t1.id\n" +
                "         order by o.yl desc)\n" +
                " where rownum = 1\n";
        Map<String, Object> result = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{scdaid, scdaid});
        result.put("TYPE", nsxlx);
        return result;
    }

    /**************************unity全景展示end**************************/


    /**
     * 递归反向追溯流通节点
     *
     * @param zsm    追溯码
     * @param list   进货批次号对应所有记录
     * @param nbmMap 流程中所有内部流通字段拼接（用于查询检疫检测记录）
     * @return
     */
    public Map getFxList(String zsm, List<Map> list, Map<String, String> nbmMap) {
        Map m = new HashMap();
        List li = new ArrayList();
        String nextZsm = "";
        for (Map map : list) {
            if (map.get("ZSM") != null && zsm.equals(map.get("ZSM"))) {//获取该追溯码对应交易记录
                /**
                 * 获取有检疫的子系统内部流通编码
                 */
                if ("4".equals(map.get("XTLX"))) {//屠宰信息
                    map.put("TZRQ", queryTzrqByZsm(map.get("ZSM").toString())); //获取屠宰日期
                    String szcdjyzh = nbmMap.get("SZCDJYZH");
                    szcdjyzh += "," + querySzcdjyzhByZsm(map.get("ZSM").toString());//拼接生猪产地检疫证号（内部流通码）
                    nbmMap.put("SZCDJYZH", szcdjyzh);
                } else if ("3".equals(map.get("XTLX"))) {//批菜信息
                    String jclhbh = nbmMap.get("JCLHBH");
                    jclhbh += "," + queryJclhbhByZsm(map.get("ZSM").toString());//拼接进场理货编号（内部流通码）
                    nbmMap.put("JCLHBH", jclhbh);
                } else if ("5".equals(map.get("XTLX"))) {//批肉
                    String zspzh = nbmMap.get("ZSPZH");
                    zspzh += "," + queryZspzhByZsm(map.get("ZSM").toString());//拼接追溯凭证号（内部流通码）
                    nbmMap.put("ZSPZH", zspzh);
                }
                /*********结束**********/

                m.put("JIAOYI", map);//记录交易记录
                li.add(map);
                if (map.get("JYPZH") != null) {
                    nextZsm = map.get("JYPZH").toString();
                }
            } else if (zsm.equals(map.get("JYPZH"))) {//获取该追溯码对应进厂记录
                m.put("JINCHANG", map);
                li.add(map);
            }
        }
        if (m.get("JIAOYI") == null) {//判断是否末端节点
            Map map = (Map) m.get("JINCHANG");
            nextZsm = (String) map.get("JYPZH");
        }
        list.removeAll(li);
        if (StringUtil.isNotBlank(nextZsm)) {
            m.put("PARENT", getFxList(nextZsm, list, nbmMap));
        }
        return m;
    }

    /**
     * 获取屠宰日期
     *
     * @param zsm
     * @return
     */
    public String queryTzrqByZsm(String zsm) {
        String sql = "SELECT T.CJRQ FROM T_TZ_SZJYXX T,T_TZ_JYXX J WHERE T.SZCDJYZH = J.SZCDJYZH AND J.ZSM=?";
        Map map = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{zsm});
        try {
            if (map != null && !map.isEmpty()) {
                String tzrq = map.get("CJRQ").toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = null;
                date = sdf.parse(tzrq);
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                calendar.add(calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动
                date = calendar.getTime();   //这个时间就是日期往后推一天的结果
                return sdf.format(date);
            } else {
                return null;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据追溯码获取生猪产地检疫证号
     *
     * @param zsm
     * @return
     */
    public String querySzcdjyzhByZsm(String zsm) {
        String sql = "SELECT T.SZCDJYZH FROM T_TZ_JYXX T WHERE T.ZSM=?";
        Map map = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{zsm});
        if (map != null && !map.isEmpty()) {
            return map.get("SZCDJYZH").toString();
        }
        return null;
    }

    /**
     * 根据追溯码获取进场理货编号
     *
     * @param zsm
     * @return
     */
    public String queryJclhbhByZsm(String zsm) {
        String sql = "SELECT  J.JCLHBH FROM T_PC_JYMXXX T ,T_PC_JYXX J WHERE T.T_PC_JYXX_ID = J.ID AND T.ZSM =?";
        Map map = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{zsm});
        if (map != null && !map.isEmpty()) {
            return map.get("JCLHBH").toString();
        }
        return null;
    }

    /**
     * 根据追溯码获取追溯凭证号
     *
     * @param zsm
     * @return
     */
    public String queryZspzhByZsm(String zsm) {
        String sql = "SELECT T.ZSPZH FROM T_PR_JYXX T WHERE T.ZSM = ?";
        Map map = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{zsm});
        if (map != null && !map.isEmpty()) {
            return map.get("ZSPZH").toString();
        }
        return null;
    }

    /**
     * 获取检测检疫信息（不分页）
     *
     * @param nbmMap 系统内部流通码
     * @return
     */
    public List queryJcjyxxList(Map<String, String> nbmMap) {
        StringBuffer sql = new StringBuffer();
        String szcdjyzh = nbmMap.get("SZCDJYZH");
        String jclhbh = nbmMap.get("JCLHBH");
        String zspzh = nbmMap.get("ZSPZH");
        if (StringUtil.isNotBlank(szcdjyzh)) {//生猪屠宰检疫信息
            String str[] = szcdjyzh.split(",");
            for (int i = 0; i < str.length; ++i) {
                sql.append("SELECT S.JYY AS JCRY,S.CJRQ AS JCSJ,S.TZCMC AS JCDD,'合格' AS JCJG,T.TPBCMC AS TPBCMC FROM T_TZ_SZJYBG T RIGHT JOIN T_TZ_SZJYXX S " +
                        " ON T.ZBID = S.ID WHERE S.SZCDJYZH='")
                        .append(str[i]).append("' ");
                if (i < str.length - 1) {
                    sql.append(" UNION ALL ");
                }
            }
        }
        if (StringUtil.isNotBlank(jclhbh)) {//蔬菜检测信息
            if (sql.length() > 0) {
                sql.append(" UNION ALL ");
            }
            String str[] = jclhbh.split(",");
            for (int i = 0; i < str.length; ++i) {
                sql.append("SELECT T.JCY AS JCRY,T.JCRQ AS JCSJ,T.PFSCMC AS JCDD,C.NAME AS JCJG,J.TPBCMC AS TPBCMC  FROM T_PC_JCXX T JOIN T_XTPZ_CODE C " +
                        " ON T.JCJG = C.VALUE LEFT JOIN T_PC_JCBG J ON T.ID = J.ZBID" +
                        " WHERE C.CODE_TYPE_CODE = 'JCJG' AND T.JCLHBH='")
                        .append(str[i]).append("' ");
                if (i < str.length - 1) {
                    sql.append(" UNION ALL ");
                }
            }
        }
        if (StringUtil.isNotBlank(zspzh)) {//批肉检测信息
            String str[] = zspzh.split(",");
            if (sql.length() > 0) {
                sql.append(" UNION ALL ");
            }
            for (int i = 0; i < str.length; ++i) {
                sql.append("SELECT T.JCY AS JCRY,T.JCRQ AS JCSJ,T.PFSCMC AS JCDD,D.NAME AS JCJG,J.TPBCMC AS TPBCMC  FROM T_PR_JCXX2 T JOIN T_XTPZ_CODE D" +
                        " ON T.JCJG = D.VALUE LEFT JOIN T_PR_JCBG J ON T.ID = J.ZBID" +
                        "  WHERE D.CODE_TYPE_CODE = 'JCJG' AND T.ZSPZH='")
                        .append(str[i]).append("' ");
                if (i < str.length - 1) {
                    sql.append(" UNION ALL ");
                }
            }
        }
        if (StringUtil.isNotBlank(sql.toString())) {
            return DatabaseHandlerDao.getInstance().queryForMaps(sql.toString());
        } else {
            return new ArrayList();
        }

    }

}
