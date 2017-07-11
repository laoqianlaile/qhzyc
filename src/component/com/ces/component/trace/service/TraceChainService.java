package com.ces.component.trace.service;

import ces.coral.lang.StringUtil;
import com.ces.component.cspt.entity.TCsptZsxxEntity;
import com.ces.component.trace.dao.TraceChainDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.database.Database;
import com.ces.config.utils.UUIDGenerator;
import com.ces.xarch.core.service.StringIDDefineDaoService;
import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 追溯链条Service
 * Created by bdz on 2015/7/9.
 */
@Component
public class TraceChainService extends StringIDDefineDaoService<TCsptZsxxEntity, TraceChainDao> {


    /**
     * 跟据追溯码查询产品信息
     *
     * @param zsm       追溯码
     * @param startNode 起始节点
     * @return Map
     */
    public Map queryByZsm(Map startNode, String zsm) {
        String xtlx = startNode.get("XTLX").toString();
        Map spxx = new HashMap();
        if ("10".equals(xtlx)) {//加工
            //获取加工原料追溯信息
            spxx = queryJgcxx(zsm);
            //加工历程信息
            spxx.put("LCXX", this.queryJglcxx(zsm));
            //加工工艺信息
            spxx.put("JGXX", this.queryScjgJgxx(zsm));
            //加工包装信息
            spxx.put("BZXX", this.queryScjgBzxx(zsm));
            //检测检验信息
            spxx.put("JCJYXX", this.queryJgjcxx(zsm));
        } else {
            spxx = this.queryBaseInfoByZsm(startNode);
            Map nbmMap = new HashMap();
            List li = queryListByJhpch(startNode.get("JHPCH").toString());
            //产品历程信息
            spxx.put("CPLC", getFxList(zsm, li, nbmMap));
            //检测检验信息
            spxx.put("JCJYXX", queryJcjyxxList(nbmMap));
        }
        //图片加载
        if ("1".equals(xtlx) || "2".equals(xtlx) || "10".equals(xtlx)) {
            //生产基地图片
            spxx.put("SCJDTP", this.queryScjdtp(startNode.get("QYBM").toString()));
            //生产企业图片
            spxx.put("SCQYTP", this.queryScqytp(startNode.get("QYBM").toString()));
            //企业证书
            spxx.put("QYZS", this.queryQyzs(startNode.get("QYBM").toString()));
            if ("1".equals(xtlx)) {
                spxx.put("SCZTP", this.queryZzGzrytp(startNode.get("QYBM").toString(), startNode.get("ZZYZPCH").toString(), startNode.get("ZSM").toString()));
                spxx.put("PMTP", this.queryScsptp(spxx.get("SPBM").toString()));
            }
            if ("2".equals(xtlx)) {
                Map map = this.queryLastNode(zsm);//获取最终节点的系统类型与refId
                if (map != null && !map.isEmpty()) {//获取肉类商品名称
                    Map spMap = this.queryRlSpmc(map);
                    if (spMap != null && !spMap.isEmpty()) {
                        spxx.put("SPMC", spMap.get("SPMC"));
                        spxx.put("PMTP", this.queryRlSpTp(spMap.get("SPBM").toString()));
                    }
                }
                spxx.put("SCZTP", this.queryYzGzrytp(startNode.get("QYBM").toString(), startNode.get("ZZYZPCH").toString(), startNode.get("ZSM").toString()));
            }
            if ("10".equals(xtlx)) {
                spxx.put("PMTP", this.queryCpsptp(spxx.get("CPBH").toString(), spxx.get("QYBM").toString()));
                spxx.put("SCZTP", this.queryGgGzrytp(zsm));

            }
        }
        spxx.put("XTLX", xtlx);
        return spxx;
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

    /**
     * 根据追溯码获取追溯码对应节点
     *
     * @param zsm
     * @return
     */
    public Map queryNode(String zsm) {
        String sql = "SELECT T.* FROM T_CSPT_JYXX WHERE T.ZSM = ?";
        return DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{zsm});
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
     * 根据追溯码查询产品基本信息及生产加工信息
     *
     * @param startNode
     * @return
     */
    public Map queryBaseInfoByZsm(Map<String, String> startNode) {
        String xtlx = startNode.get("XTLX");
        String zsm = startNode.get("ZSM");
        Map spxx = new HashMap();
        if ("1".equals(xtlx)) {
            spxx = this.queryZzxxByZsm(zsm);
            spxx.put("SYXX", this.getZzSyxxList(zsm));
            spxx.put("SFXX", this.getZzSfxxList(zsm));
        } else if ("2".equals(xtlx)) {
            spxx = this.queryYzxxByZsm(zsm);
            spxx.put("YYXX", this.getYzYyxxList(zsm));
            spxx.put("SLXX", this.getYzSlxxList(zsm));
        } else if ("3".equals(xtlx)) {
            spxx = this.queryPcxxByZsm(zsm);
        } else if ("4".equals(xtlx)) {
            spxx = this.queryTzxxByZsm(zsm);
        } else if ("5".equals(xtlx)) {
            spxx = this.queryPrxxByZsm(zsm);
        }
        return spxx;
    }

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
     * 查询生产企业图片（FJLX=0）
     *
     * @param qybm
     * @return
     */
    public List queryScqytp(String qybm) {
        String sql = "SELECT T.TPBCMC " +
                "  FROM T_QYPT_ZHGLFJ T, T_QYPT_ZHGL Z " +
                " WHERE T.ZBID = Z.ID AND T.FJLX = '0'" +
                "   AND Z.AUTH_ID = (SELECT S.AUTH_PARENT_ID " +
                "                      FROM T_QYPT_ZHGL S " +
                "                     WHERE S.ZHBH = ?) ORDER BY T.SCSJ DESC";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{qybm});
    }

    /**
     * 查询生产基地图片
     *
     * @param qybm
     * @return
     */
    public List queryScjdtp(String qybm) {
        String sql = "SELECT T.TPLJ AS TPBCMC FROM T_COMMON_QYTP T WHERE T.QYBM = ? AND ROWNUM<=6 ORDER BY T.UPLOADTIME DESC";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{qybm});
    }

    /**
     * 获取企业证书图片（FJLX=1）
     *
     * @param qybm
     * @return
     */
    public List queryQyzs(String qybm) {
        String sql = "SELECT T.TPBCMC " +
                "  FROM T_QYPT_ZHGLFJ T, T_QYPT_ZHGL Z " +
                " WHERE T.ZBID = Z.ID AND T.FJLX = '1'" +
                "   AND Z.AUTH_ID = (SELECT S.AUTH_PARENT_ID " +
                "                      FROM T_QYPT_ZHGL S " +
                "                     WHERE S.ZHBH = ?) AND ROWNUM<=4 ORDER BY T.SCSJ DESC";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{qybm});
    }

    /**
     * 查询养殖工作人员档案
     *
     * @param qybm
     * @param yzpch
     * @param zsm
     * @return
     */
    public List queryYzGzrytp(String qybm, String yzpch, String zsm) {
        String sql = "SELECT G.TPBCMC " +
                "  FROM T_YZ_GZRYFJ G, T_YZ_GZRYDA D " +
                " WHERE G.ZBID = D.ID " +
                "   AND D.QYBM = ? " +
                "   AND D.GZRYBH IN (SELECT T.FZRBH " +
                "                      FROM T_YZ_CLXX T " +
                "                     WHERE T.ZSM = ? " +
                "                    UNION ALL " +
                "                    SELECT Y.FZRBH " +
                "                      FROM T_YZ_YYXX Y " +
                "                     WHERE Y.YZPCH = ? " +
                "                       AND Y.QYBM = ? " +
                "                    UNION ALL " +
                "                    SELECT S.FZRBH " +
                "                      FROM T_YZ_SYXX S " +
                "                     WHERE S.YZPCH = ? " +
                "                       AND S.QYBM = ?" +
                "                    UNION ALL " +
                "                    SELECT J.FZRBH FROM T_YZ_JLXX J " +
                "                    WHERE J.YZPCH = ? AND J.QYBM = ?) AND ROWNUM<=6 ORDER BY G.SCSJ DESC";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{qybm, zsm, yzpch, qybm, yzpch, qybm, yzpch, qybm});
    }

    /**
     * 查询种植工作人员图片
     *
     * @param qybm
     * @param yzpch
     * @param zsm
     * @return
     */
    public List queryZzGzrytp(String qybm, String yzpch, String zsm) {
        String sql = "SELECT G.TPBCMC " +
                "  FROM T_ZZ_GZRYDAFJ G, T_ZZ_GZRYDA D " +
                " WHERE G.ZBID = D.ID " +
                "   AND D.QYBM = ? " +
                "   AND D.GZRYBH IN (SELECT T.FZRBH " +
                "                      FROM T_ZZ_CCXX T " +
                "                     WHERE T.ZSM = ? " +
                "                    UNION ALL " +
                "                    SELECT Y.FZRBH " +
                "                      FROM T_ZZ_SYXX Y " +
                "                     WHERE Y.ZZPCH = ? " +
                "                       AND Y.QYBM = ? " +
                "                    UNION ALL " +
                "                    SELECT S.FZRBH " +
                "                      FROM T_ZZ_SFXX S " +
                "                     WHERE S.ZZPCH = ? " +
                "                       AND S.QYBM = ? " +
                "                    UNION ALL " +
                "                    SELECT Z.FZRBH " +
                "                      FROM T_ZZ_ZPXX Z " +
                "                     WHERE Z.ZZPCH = ? " +
                "                       AND Z.QYBM = ? ) AND ROWNUM<=6 ORDER BY G.SCSJ";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{qybm, zsm, yzpch, qybm, yzpch, qybm, yzpch, qybm});
    }

    /**
     *
     * @param qybm
     * @param zsm
     * @return
     */
    public List queryGgGzrytp(String zsm) {
        String sql = "select TPBCMC from (SELECT f.tpbcmc\n" +
                "  FROM T_JG_JGMXXX T\n" +
                "  join t_jg_bzxx b\n" +
                "    on t.qybm = b.qybm\n" +
                "   and t.ddbh = b.ddbh\n" +
                "  join t_jg_gzryda g\n" +
                "    on g.qybm = b.qybm\n" +
                "   and g.gzrybh = t.fzrbh\n" +
                "  join t_jg_gzrydafj f\n" +
                "    on f.zbid = g.id\n" +
                " WHERE b.zsm = ?) union\n" +
                " (SELECT f.tpbcmc\n" +
                "  FROM T_JG_JGBZMX T\n" +
                "  join T_JG_BZXX b\n" +
                "    on T.DDBH = b.DDBH\n" +
                "   AND T.QYBM = b.QYBM\n" +
                "  join t_jg_gzryda g\n" +
                "    on g.qybm = b.qybm\n" +
                "   and g.gzrybh = t.fzrbh\n" +
                "  join t_jg_gzrydafj f\n" +
                "    on f.zbid = g.id\n" +
                " WHERE b.ZSM = ?) union\n" +
                " (SELECT f.tpbcmc\n" +
                "  FROM T_JG_JYXX T\n" +
                "  JOIN T_JG_BZXX B\n" +
                "    ON T.QYBM = B.QYBM\n" +
                "   AND T.DDBH = B.DDBH\n" +
                "  join t_jg_gzryda g\n" +
                "    on g.qybm = b.qybm\n" +
                "   and g.gzrybh = t.jyrbh\n" +
                "  join t_jg_gzrydafj f\n" +
                "    on f.zbid = g.id\n" +
                " WHERE B.ZSM = ?)";
        List result = DatabaseHandlerDao.getInstance().queryForMaps(sql,new Object[]{zsm,zsm,zsm});
        return result;
    }

    /**
     * 查询蔬菜商品图片
     *
     * @param spbm
     * @return
     */
    public List queryScsptp(String spbm) {
        String sql = "SELECT T.TPBCMC FROM T_QYPT_MRSPTP T ,T_COMMON_SCSPXX S " +
                " WHERE T.ZBID = S.ID AND S.SPBM = ? AND ROWNUM<=6 ORDER BY T.SCSJ DESC";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{spbm});
    }

    /**
     * 查询加工成品图片
     *
     * @param cpbm 成品编码
     * @param qybm 企业编码
     * @return
     */
    public List queryCpsptp(String cpbm, String qybm) {
        String sql = "SELECT T.TPBCMC FROM T_JG_CPZLTP T,T_JG_CPZL C WHERE T.ZBID = C.ID AND C.CPBH=? AND C.QYBM=?";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{cpbm, qybm});
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
     * 根据追溯码获取种植场商品信息
     *
     * @param zsm
     * @return
     */
    public Map<String, Object> queryZzxxByZsm(String zsm) {
        String sql = "SELECT D.SPMC,D.SPBM,C.CDMC,C.QYMC,C.QYBM,C.ZZJDMC AS JDMC,Z.FZR,C.CDZSH,Z.ZPRQ," +
                " T.CSRQ,T.ZZPCH,C.QYTP2" +
                " FROM T_ZZ_CCXX T, T_ZZ_CDDA C, T_ZZ_CZDA D,T_ZZ_ZPXX Z " +
                " WHERE T.ZSM = ? AND T.QYBM = C.QYBM AND T.QYBM = D.QYBM AND T.QYBM = Z.QYBM " +
                " AND T.ZZPCH =Z.ZZPCH AND Z.SYCZBH = D.CZBH";
        return DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{zsm});
    }

    /**
     * 根据追溯码获取养殖商品信息
     *
     * @param zsm 追溯码
     * @return
     */
    public Map<String, Object> queryYzxxByZsm(String zsm) {
        String sql = "SELECT T.QYMC,T.QYBM,T.YZCMC AS JDMC,T.QYTP2,T.CDMC,C.FZR,J.PZTYM,T.CDZSH,C.CLRQ,J.JLRQ,C.YZPCH" +
                " FROM T_YZ_CDDA T,T_YZ_CLXX C,T_YZ_JLXX J " +
                " WHERE C.ZSM = ? AND C.QYBM = T.QYBM AND C.YZPCH = J.YZPCH AND T.QYBM = J.QYBM";
        return DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{zsm});
    }

    /**
     * 获取最终节点的系统类型和关联ID
     *
     * @param zsm
     * @return
     */
    public Map<String, Object> queryLastNode(String zsm) {
        String sql = "SELECT T.XTLX,T.REFID FROM T_CSPT_JYXX T WHERE T.JYPZH = ? AND T.ZSM IS NULL";
        return DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{zsm});
    }

    /**
     * 获取肉类商品名称
     *
     * @param map
     * @return
     */
    public Map queryRlSpmc(Map<String, Object> map) {
        String xtlx = map.get("XTLX").toString();
        String refId = map.get("REFID").toString();
        String tableName = "";
        String columns = "T.SPMC,T.SPBM";
        if ("5".equals(xtlx)) {//猪肉批发市场
            tableName = "T_PR_RPJCXX";
        } else if ("6".equals(xtlx)) {//团体采购
            tableName = "T_TT_RPJCXX";
        } else if ("7".equals(xtlx)) {//超市
            tableName = "T_CS_RPJCXX";
        } else if ("8".equals(xtlx)) {//零售市场
            tableName = "T_LS_RPJCXX";
        } else if ("9".equals(xtlx)) {//餐饮
            tableName = "T_CY_RPJCXX";
        } else if ("10".equals(xtlx)) {//加工
            tableName = "T_JG_YLHWXX";
            columns = "T.SPBM,T.YLSPMC AS SPMC";
        } else {//其他市场
            return null;
        }
        String sql = "SELECT " + columns + " FROM " + tableName + " T WHERE T.ID = ?";
        Map spMap = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{refId});
        if (spMap != null && !spMap.isEmpty())
            return spMap;
        else
            return null;
    }

    /**
     * 肉类默认商品图片
     *
     * @param spbm
     * @return
     */
    public List queryRlSpTp(String spbm) {
        String sql = "select * from(SELECT T.TPBCMC, SCSJ\n" +
                "  FROM T_QYPT_MRRPSPTP T, T_COMMON_RPSPXX S\n" +
                " WHERE T.ZBID = S.ID\n" +
                "   AND S.SPBM = ?\n" +
                " ORDER BY T.SCSJ DESC)\n" +
                " where ROWNUM <= 6\n";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{spbm});
    }

    /**
     * 根据追溯码获取批菜商品信息
     *
     * @param zsm 追溯码
     * @return Map
     */
    public Map<String, Object> queryPcxxByZsm(String zsm) {
        String sql = "SELECT H.SCJD AS JDMC, L.SPMC,J.PFSCMC AS QYMC,H.CDMC" +
                "  FROM T_PC_JYMXXX T, T_PC_JYXX J, T_PC_JCLHXX H, T_PC_JCLHMXXX L" +
                " WHERE T.T_PC_JYXX_ID = J.ID" +
                "   AND J.JCLHBH = H.JCLHBH" +
                "   AND T.ZSM=?" +
                "   AND H.ID = L.PID";
        return DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{zsm});
    }

    /**
     * 根据追溯码获取批肉商品信息
     *
     * @param zsm 追溯码
     * @return
     */
    public Map<String, Object> queryPrxxByZsm(String zsm) {
        String sql = "SELECT J.SPMC, J.CDMC,T.PFSCMC AS QYMC" +
                "  FROM T_PR_JYXX T, T_PR_RPJCXX J" +
                " WHERE T.ZSPZH = J.ZSPZH" +
                "   AND T.ZSM = ?";
        return DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{zsm});
    }

    /**
     * 根据追溯码获取屠宰商品信息
     *
     * @param zsm 追溯码
     * @return
     */
    public Map<String, Object> queryTzxxByZsm(String zsm) {
        String sql = "SELECT T.SPMC, J.CDMC,T.TZCMC AS QYMC" +
                "  FROM T_TZ_JYXX T, T_TZ_SZJCXX J" +
                " WHERE T.SZCDJYZH = J.SZCDJYZH" +
                "   AND T.ZSM = ?";
        return DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{zsm});
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

    /**
     * 种植施药信息（不分页）
     *
     * @param zsm
     * @return
     */
    public List getZzSyxxList(String zsm) {
        String sql = "SELECT T.ID,T.SYNY,T.SYSJ,T.FZR " +
                " FROM T_ZZ_SYXX T, T_ZZ_CCXX C" +
                " WHERE T.ZZPCH = C.ZZPCH" +
                "   AND T.QYBM = C.QYBM" +
                "   AND C.ZSM = ?";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{zsm});
    }

    /**
     * 获取种植施肥信息List
     *
     * @param zsm
     * @return
     */
    public List getZzSfxxList(String zsm) {
        String sql = "SELECT T.ID, T.SFSJ, T.SYFL, T.FZR" +
                "  FROM T_ZZ_SFXX T, T_ZZ_CCXX C" +
                " WHERE T.ZZPCH = C.ZZPCH" +
                "   AND T.QYBM = C.QYBM" +
                "   AND C.ZSM = ?";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{zsm});
    }

    /**
     * 获取养殖饲养信息（不分页）
     *
     * @param zsm
     * @return
     */
    public List getYzSlxxList(String zsm) {
        String sql = "SELECT T.ID, T.SYSL, T.WSSJ, T.FZR" +
                "  FROM T_YZ_SYXX T, T_YZ_CLXX C" +
                " WHERE T.YZPCH = C.YZPCH" +
                "   AND T.QYBM = C.QYBM" +
                "   AND C.ZSM = ?";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{zsm});
    }

    /**
     * 获取养殖用药信息（不分页）
     *
     * @param zsm
     * @return
     */
    public List getYzYyxxList(String zsm) {
        String sql = "SELECT T.ID, T.SYSY, T.YYSJ, T.FZR" +
                "  FROM T_YZ_YYXX T, T_YZ_CLXX C" +
                " WHERE T.YZPCH = C.YZPCH" +
                "   AND T.QYBM = C.QYBM" +
                "   AND C.ZSM = ?";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{zsm});
    }

    /**
     * 获取加工厂基本信息
     *
     * @param zsm 追溯码
     * @return
     */
    public Map queryJgcxx(String zsm) {
        String sql = "SELECT T.CDMC,T.QYMC,T.QYTP2,T.JGCMC AS JDMC,B.CPMC AS SPMC,B.CPBH,B.QYBM FROM T_JG_JGCDA T ,T_JG_BZXX B" +
                " WHERE T.QYBM = B.QYBM AND B.ZSM =? ";
        return DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{zsm});
    }

    /**
     * 获取加工产品原材料信息
     *
     * @param zsm
     * @return
     */
    public List<Map<String, Object>> queryJgzsxx(String zsm) {
        String sql = "SELECT H.ZSPZH,T.BZBH,T.CPMC,T.DDBH,Y.YLBH,Y.YLMC,Y.YLPC,T.QYBM,'10' AS XTLX " +
                " FROM T_JG_BZXX T,T_JG_XLMXXX Y,T_JG_YLHWXX H WHERE T.DDBH=Y.DDBH AND T.QYBM=H.QYBM AND T.QYBM=Y.QYBM" +
                " AND Y.YLPC=H.YLPCH AND T.ZSM=? ";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{zsm});
    }

    /**
     * 加工历程信息
     *
     * @param zsm 追溯码
     * @return Map
     */
    public Map queryJglcxx(String zsm) {
        //加工信息SQL
        String jgSql = "SELECT T.HJKSSJ, T.JGHJMC" +
                "  FROM T_JG_JGMXXX T, T_JG_BZXX B" +
                " WHERE T.QYBM = B.QYBM" +
                "   AND T.DDBH = B.DDBH" +
                "   AND B.ZSM = ?";
        List jgMap = DatabaseHandlerDao.getInstance().queryForMaps(jgSql, new Object[]{zsm});
        //包装信息SQL
        String bzSql = "SELECT T.BZKSSJ,T.BZGY " +
                "  FROM T_JG_JGBZMX T , T_JG_BZXX B" +
                " WHERE T.QYBM = B.QYBM" +
                "   AND T.DDBH = B.DDBH" +
                "   AND B.ZSM = ?";
        List bzMap = DatabaseHandlerDao.getInstance().queryForMaps(bzSql, new Object[]{zsm});
        //出场信息SQL
        String ccSql = "SELECT T.CCSJ " +
                "   FROM T_JG_CPCCXX T, T_JG_BZXX B" +
                " WHERE T.QYBM = B.QYBM" +
                "   AND T.DDBH = B.DDBH" +
                "   AND B.ZSM = ?";
        Map ccMap = DatabaseHandlerDao.getInstance().queryForMap(ccSql, new Object[]{zsm});
        //原料进厂信息SLQ
        String ylSql = "SELECT T.YLMC, T.JCSJ" +
                "  FROM T_JG_YLHWXX T, T_JG_XLMXXX X, T_JG_BZXX B" +
                " WHERE T.YLPCH = X.YLPC" +
                "   AND T.QYBM = X.QYBM" +
                "   AND T.QYBM = B.QYBM" +
                "   AND X.DDBH = B.DDBH" +
                "   AND B.ZSM = ?";
        List yljcList = DatabaseHandlerDao.getInstance().queryForMaps(ylSql, new Object[]{zsm});
        Map<String, Object> lcMap = new HashMap();
        lcMap.put("JGXX", jgMap);
        lcMap.put("BZXX", bzMap);
        lcMap.put("CCXX", ccMap);
        lcMap.put("YLXX", yljcList);
        return lcMap;
    }

    /**
     * 查询生产加工加工环节信息List
     *
     * @param zsm
     * @return {List}
     */
    public List queryScjgJgxx(String zsm) {
        String sql = "SELECT T.HJKSSJ, T.HJJSSJ, T.JGHJMC, J.SYTJJMC, T.FZR" +
                "  FROM T_JG_JGMXXX T, T_JG_JGHJTJJMXXX J,t_jg_bzxx b" +
                " WHERE T.DDBH = J.DDBH" +
                "   AND J.JGHJBH = T.JGHJBH" +
                "   AND T.QYBM = J.QYBM" +
                "   and t.qybm = b.qybm" +
                "   and t.ddbh = b.ddbh" +
                "   and b.zsm=?";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{zsm});
    }

    /**
     * 查询生产加工包装信息List
     *
     * @param zsm 追溯码
     * @return {List}
     */
    public List queryScjgBzxx(String zsm) {
        String sql = "SELECT T.BZKSSJ, T.BZJSSJ, B.BZCLZL, T.FZR" +
                "  FROM T_JG_JGBZMX T, T_JG_BZCLMX B,T_JG_BZXX Z" +
                " WHERE T.DDBH = B.DDBH" +
                "   AND T.BZGYBH = B.PID" +
                "   AND T.QYBM = B.QYBM" +
                "   AND T.DDBH = Z.DDBH" +
                "   AND T.QYBM = Z.QYBM" +
                "   AND Z.ZSM =?";
        sql += " ORDER BY T.BZKSSJ";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{zsm});
    }

    /**
     * 查询加工检验信息List
     *
     * @param zsm 追溯码
     * @return {List}
     */
    public List queryJgjcxx(String zsm) {
        String sql = "SELECT T.JYXM AS JCXM, T.JYSJ AS JCSJ, C.NAME AS JCJG, T.JYR AS JCRY, J.JGCMC AS JCDD,G.TPBCMC AS TPBCMC" +
                "   FROM T_JG_JYXX T JOIN T_XTPZ_CODE C ON T.JYJG = C.VALUE" +
                "  JOIN T_JG_BZXX B ON T.QYBM = B.QYBM AND T.DDBH = B.DDBH" +
                "  JOIN T_JG_JGCDA J ON T.QYBM = J.QYBM" +
                "  LEFT JOIN T_JG_JCBG G ON T.ID = G.ZBID" +
                " WHERE C.CODE_TYPE_CODE = 'JGJYJG'" +
                "   AND B.ZSM = ?";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{zsm});
    }

    /**
     * 获取正向节点树
     *
     * @param zzyzpch 企业编码和种植养殖批次号的合成编码
     * @return
     */
    public Map getTree(String zzyzpch) {
        //企业编码
        String qybm = zzyzpch.substring(0, 9);
        //种植养殖批次号
        zzyzpch = zzyzpch.substring(9, zzyzpch.length());
        //系统类型
        String xtlx = getZzYzxtlx(zzyzpch, qybm);
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
     * 获取对应系统类型
     *
     * @param zzyzpch
     * @param qybm
     * @return
     */
    public String getZzYzxtlx(String zzyzpch, String qybm) {
        String sql = "SELECT XTLX FROM T_CSPT_JYXX T WHERE T.ZZYZPCH =? AND T.QYBM =? GROUP BY T.XTLX";
        Map map = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{zzyzpch, qybm});
        return (String) map.get("XTLX");
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
     * 根据种植养殖批次号获取进货批次号
     *
     * @param zzyzpch
     * @param qybm
     * @param xtlx
     * @return
     */
    public List<String> queryJhpchByZzyzpch(String zzyzpch, String qybm, String xtlx) {
        String sql = "";
        if ("1".equals(xtlx)) {
            sql += "SELECT T.PCH FROM T_ZZ_CCXX T WHERE T.ZZPCH=? AND T.QYBM = ?";
        } else if ("2".equals(xtlx)) {
            sql += "SELECT T.PCH FROM T_YZ_CLXX T WHERE T.YZPCH=? AND T.QYBM = ?";
        } else if (StringUtil.isBlank(xtlx)) {
            return new ArrayList<String>();
        }
        return DatabaseHandlerDao.getInstance().queryForList(sql, new Object[]{zzyzpch, qybm});
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
     * 根据经度、纬度获取一定半径范围内的市场信息
     *
     * @param mlong  经度
     * @param mlat   纬度
     * @param radius 方圆
     * @return
     */
    public List<Map<String, Object>> getNearMarkets(String mlong, String mlat, String radius) {
        String sql = "select * from t_qypt_zhgl where sqrt(power((jd - ?)*102834.74258026089786013677476285,2) + power((wd - ?)*111712.69150641055729984301412873,2)) < ? and xtlx in ('3','5','7','8')";
        System.out.println(sql);
        List<Map<String, Object>> datas = DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{mlong, mlat, radius});
        return datas;
    }

    /**
     * 计算两个市场的GPS坐标的距离
     * n表示经度
     * e表示维度
     *
     * @param n1
     * @param e1
     * @param n2
     * @param e2
     * @return
     */
    public String distance(String n1, String e1, String n2, String e2) {
        if (StringUtil.isBlank(n1) || StringUtil.isBlank(e1) || StringUtil.isBlank(n2) || StringUtil.isBlank(e2)) {
            return null;
        }
        double d_n1 = Double.valueOf(n1);
        double d_e1 = Double.valueOf(e1);
        double d_n2 = Double.valueOf(n2);
        double d_e2 = Double.valueOf(e2);
        double jl_jd = 102834.74258026089786013677476285;
        double jl_wd = 111712.69150641055729984301412873;
        double b = Math.abs((d_e1 - d_e2) * jl_wd);
        double a = Math.abs((d_n1 - d_n2) * jl_jd);
        double dis = Math.sqrt((a * a + b * b));
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        return nf.format(dis);
    }

    /**
     * 获取企业详细信息
     *
     * @param qybm
     * @return
     */
    public Map<String, Object> getQyxxDetail(String qybm) {
        String sql = "select * from t_qypt_zhgl t where t.zhbh = ?";
        Map<String, Object> data = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{qybm});
        return data;
    }

    public void addComment(String mdbm, String pjdj, String pjnr) {
        String id = UUIDGenerator.uuid();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String create_time = df.format(new Date());
        String sql = "insert into t_qypt_pjgl (id,create_time,mdbm,pjdj,pjnr,pjsj) values (?,?,?,?,?,?)";
        DatabaseHandlerDao.getInstance().executeSql(sql, new Object[]{id, create_time, mdbm, pjdj, pjnr, create_time});
    }

    public Object getComment(String mdbm) {
        String sql = "select pjdj,pjnr,mdbm,pjsj from t_qypt_pjgl where 1=1";
        if (null != mdbm && !"".equals(mdbm)) {
            sql += " and mdbm = '" + mdbm + "'";
        }
        List<Map<String, Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return maps;
    }
}
