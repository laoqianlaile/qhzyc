package com.ces.component.trace.service;

import com.ces.component.trace.dao.FarmOperationDao;
import com.ces.component.trace.utils.ResponseUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.database.Database;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.service.StringIDDefineDaoService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 食用农产品生产作业信息管理系统Service
 * Created by bdz on 2015/8/25.
 */
@Component
public class FarmOpertionService extends StringIDDefineDaoService<StringIDEntity, FarmOperationDao> {
    /**
     * 查询企业基地信息
     *
     * @param qybm 企业编码
     * @return
     */
    public List queryBaseInfo(String qybm) {
        String sql = "SELECT T.* FROM T_ZZ_JDXX T WHERE T.QYBM = ? AND T.IS_DELETE='0'";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{qybm});
    }

    /**
     * 查询基地下区域信息
     *
     * @param qybm 企业编码
     * @param jdbh 基地编号
     * @return
     */
    public List queryAreaInfo(String qybm, String jdbh) {
        String sql = "SELECT T.* FROM T_ZZ_QYXX T WHERE T.IS_DELETE='0' AND T.QYBM = ? AND T.JDBH=?";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{qybm, jdbh});
    }

    /**
     * 查询区域下地块信息
     *
     * @param qybm 企业编码
     * @param jdbh 基地编号
     * @param qybh 区域编号
     * @return
     */
    public List queryPlotInfo(String qybm, String jdbh, String qybh) {
        String sql = "SELECT T.* FROM T_ZZ_DKXX T WHERE T.IS_DELETE='0' AND T.QYBM = ? AND T.JDBH=? AND T.QYBH=?";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{qybm, jdbh, qybh});
    }

    /**
     * 查询地块下单元信息
     *
     * @param qybm 企业编码
     * @param jdbh 基地编号
     * @param qybh 区域编号
     * @param dkbh 地块编号
     * @return
     */
    public List queryUnitInfo(String qybm, String jdbh, String qybh, String dkbh) {
        String sql = "SELECT T.* FROM T_ZZ_DY T WHERE T.IS_DELETE='0' AND T.QYBM = ? AND T.JDBH=? AND T.QYBH=? AND T.DKBH=?";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{qybm, jdbh, qybh, dkbh});
    }

    /**
     * 获取具体操作page
     *
     * @param pageRequest
     * @param qybm
     * @param qybh
     * @param dkbh
     * @param dybh
     * @param czlx
     * @return
     */
    public Page queryCzPage(PageRequest pageRequest, String qybm, String qybh, String dkbh, String dybh, String czlx) {
        String str = dybh.replace(",", "','");
        String sql = "SELECT T.*  FROM T_ZZ_NSJL T WHERE T.CZLX='" + czlx + "'" +
                " AND T.QYBH = '" + qybh + "' AND T.QYBM='" + qybm + "' " +
                "AND DKBH='" + dkbh + "' AND T.DYBH IN ('" + str + "')";
        return queryPage(pageRequest, sql);
    }

    /**
     * 保存操作记录
     *
     * @param pId
     * @param czrbh
     * @param czr
     */
    public void saveCzjl(String pId, String czrbh, String czr) {
        String id = getCzjlId(pId, czrbh);
        //判断是否当天已刷过卡
        if (StringUtil.isNotEmpty(id)) {
            updateCzjl(pId, czrbh, czr, id);
        } else {
            insertCzjl(pId, czrbh, czr);
        }
    }

    /**
     * 插入操作记录
     *
     * @param pId   父表ID
     * @param czrbh 操作人编号
     * @param czr   操作人
     */
    public void insertCzjl(String pId, String czrbh, String czr) {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sql = "INSERT INTO T_ZZ_CZJL (ID,KSSJ,CZR,CZRBH,PID) VALUES(SYS_GUID(),?,?,?,?)";
        DatabaseHandlerDao.getInstance().executeSql(sql, new Object[]{df.format(date), czr, czrbh, pId});
    }

    /**
     * 更新操作记录
     *
     * @param pId   父表ID
     * @param czrbh 操作人编号
     * @param czr   操作人
     * @param id    记录ID
     */
    public void updateCzjl(String pId, String czrbh, String czr, String id) {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sql = "UPDATE T_ZZ_CZJL SET JSSJ=? WHERE ID=?";
        DatabaseHandlerDao.getInstance().executeSql(sql, new Object[]{df.format(date), id});
    }

    /**
     * 通过pId获取操作记录page
     *
     * @param pId         父id
     * @param pageRequest 分页参数
     * @return
     */
    public Page queryCzjlByPId(String pId, PageRequest pageRequest) {
        String sql = "SELECT T.* FROM T_ZZ_CZJL T WHERE T.PID = '" + pId + "' ORDER BY T.KSSJ DESC,T.JSSJ DESC";
        return queryPage(pageRequest, sql);
    }

    /**
     * 判断当天是否已经刷过卡
     *
     * @param pId   父表ID
     * @param czrbh 操作人编号
     * @return ID 返回操作记录ID
     */
    public String getCzjlId(String pId, String czrbh) {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "SELECT T.ID FROM T_ZZ_CZJL T WHERE T.PID = ? AND T.CZRBH = ? AND T.KSSJ LIKE ?";
        Map map = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{pId, czrbh, "%" + df.format(date) + "%"});
        if (!map.isEmpty())
            return map.get("ID").toString();
        else
            return null;
    }

    /**
     * 查询生产档案
     *
     * @param pageRequest
     * @param qybm
     * @param qybh
     * @param dkbh
     * @param dybh
     * @param queryItems
     * @param suffix
     * @return
     */
    public Page queryAllPage(PageRequest pageRequest, String qybm, String qybh, String dkbh, String dybh, String[] queryItems, String suffix) {
        String str = dybh.replace(",", "','");
        String tableName = "T_ZZ_SC" + suffix;
        String items = "";
        for (int i = 0; i < queryItems.length; ++i) {
            if (i < queryItems.length) {
                items += ",";
            }
            String s = queryItems[i];
            items += s;
        }
        String sql = "SELECT B.ID, T.ZZDYMC " + items +
                "  FROM T_ZZ_SCDA T, " + tableName + " B " +
                " WHERE T.ID = B.PID " +
                "   AND T.QYBM = '" + qybm + "' " +
                "   AND T.SSQYBH = '" + qybh + "' " +
                "   AND T.DKBH = '" + dkbh + "' " +
                "   AND T.ZZDYBH IN ('" + str + "') " +
                "   AND T.CREATE_TIME = (SELECT MAX(D.CREATE_TIME) " +
                "                         FROM T_ZZ_SCDA D " +
                "                         WHERE d.QYBM = '" + qybm + "' " +
                "                         AND d.SSQYBH = '" + qybh + "' " +
                "                         AND d.DKBH = '" + dkbh + "' " +
                "                         AND d.ZZDYBH IN ('" + str + "')) ORDER BY B.CREATE_TIME DESC";
        return queryPage(pageRequest, sql);
    }

    /**
     * 获取所有操作ID
     *
     * @param qybm
     * @param qybh
     * @param dkbh
     * @param dybh
     * @param suffix
     * @return
     */
    public List queryIds(String qybm, String qybh, String dkbh, String dybh, String suffix) {
        String str = dybh.replace(",", "','");
        String tableName = "T_ZZ_SC" + suffix;
        String sql = "SELECT B.ID" +
                "  FROM T_ZZ_SCDA T, " + tableName + " B " +
                " WHERE T.ID = B.PID " +
                "   AND T.QYBM = '" + qybm + "' " +
                "   AND T.SSQYBH = '" + qybh + "' " +
                "   AND T.DKBH = '" + dkbh + "' " +
                "   AND T.ZZDYBH IN ('" + str + "') " +
                "   AND T.CREATE_TIME = (SELECT MAX(D.CREATE_TIME) " +
                "                         FROM T_ZZ_SCDA D " +
                "                         WHERE d.QYBM = '" + qybm + "' " +
                "                         AND d.SSQYBH = '" + qybh + "' " +
                "                         AND d.DKBH = '" + dkbh + "' " +
                "                         AND d.ZZDYBH IN ('" + str + "')) ORDER BY B.CREATE_TIME DESC";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql);
    }

    /**
     * 再做一次
     *
     * @param id
     * @param items
     * @param suffix
     */
    public void doAgain(String id, String[] items, String suffix) {
        String tableName = "T_ZZ_SC" + suffix;
        String insertItems = "";
        for (int i = 0; i < items.length; ++i) {
            if (i < items.length) {
                insertItems += ",";
            }
            String s = items[i];
            insertItems += s;
        }
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sql = "INSERT INTO " + tableName +
                "  (ID" + insertItems + ", PID, CREATE_TIME)" +
                "  SELECT SYS_GUID() AS ID " + insertItems + ",T.PID, '" + df.format(date) + "'" +
                "    FROM " + tableName + " T" +
                "   WHERE T.ID = ?";
        DatabaseHandlerDao.getInstance().executeSql(sql, new Object[]{id});
    }

    /**
     * 评价
     *
     * @param id
     */
    public void updateEvaluate(String pj, String id) {
        String sql = "UPDATE T_ZZ_CZJL SET PJ = ? WHERE ID=?";
        DatabaseHandlerDao.getInstance().executeSql(sql, new Object[]{pj, id});
    }

    /**
     * 获取单元编号
     *
     * @param icNum
     * @param qybm
     * @return
     */
    public List getDybh(String icNum, String qybm) {
        String sql = "SELECT T.ZZDYBH FROM V_ZZ_CMPDYXX T WHERE T.QYICK = ? AND T.DKICK = ? AND T.QYBM = ?";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{icNum, icNum, qybm});
    }

    /**
     * 分页查询
     *
     * @param pageRequest
     * @param sql
     * @return Object
     */
    public Page queryPage(PageRequest pageRequest, String sql) {
        if (StringUtil.isEmpty(sql)) {
            return null;
        }
        //查总数
        String count = "select count(*) as count from (" + sql + ")";
        Map map = DatabaseHandlerDao.getInstance().queryForMap(count);
        //总数
        long total = Long.parseLong(map.get("COUNT").toString());
        int begin = pageRequest.getOffset();
        int end = begin + pageRequest.getPageSize();
        if (begin > total) {
            int remainder = (int) (total % pageRequest.getPageSize());
            end = (int) total;
            begin = (int) (total - (remainder == 0 ? pageRequest.getPageSize() : remainder));
        }

        List<Map<String, Object>> content = DatabaseHandlerDao.getInstance().pageMaps(sql, begin, end);
        if (content == null) {
            return null;
        } else {
            return new PageImpl<Map<String, Object>>(content, pageRequest, total);
        }
    }

    public Object queryCardInfo(String card, String qybm) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        String sql = "SELECT * FROM V_ZZ_CMPDYXX T WHERE (T.QYICK = '" + card + "' or T.DKICK = '" + card + "') AND T.QYBM = '" + qybm + "'";
        List<Map<String, Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        sql = "select * from t_zz_dkxx t where t.ickbh = '" + card + "' and t.is_delete <> '1'";
        List<Map<String, Object>> wlwList = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        if (wlwList.size() == 1) {
            sql = "select * from t_zz_wlwcgqsj t where t.sbsbh = '" + String.valueOf(wlwList.get(0).get("CGQZ")) + "'";
            wlwList = DatabaseHandlerDao.getInstance().queryForMaps(sql);
            if (wlwList.size() != 0) {
                dataMap.put("wlwxx", wlwList.get(0));
            }else {
                dataMap.put("wlwxx", "");
            }
        }
        dataMap.put("data", dataList);
        return dataMap;
    }

    public Object queryNsx(String lx, String qybm, String qybh, String dkbh) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sDate = sdf.format(date);
        if (!"".equals(String.valueOf(lx))) {
            String sql = String.valueOf(getQueryNsxSql("t_zz_sc" + String.valueOf(lx), qybm, sDate, qybh, dkbh));
            List<Map<String, Object>> listData = DatabaseHandlerDao.getInstance().queryForMaps(sql);
            return listData;
        }
        return null;
    }

    public Object getQueryNsxSql(String tableName, String qybm, String sDate, String qybh, String dkbh) {
        StringBuffer sql = new StringBuffer();
        sql.append("select a.id as id,b.id as bid,a.pz,a.dkmc,a.zzdymc,b.nszyxmc from t_zz_scda a," + tableName + " b where a.qybm = '" + qybm + "' and a.id = b.pid and b.is_delete <> '1' and to_date(b.ygsj1,'yyyy-mm-dd') <= to_date('" + sDate + "','yyyy-mm-dd') and to_date(b.ygsj2,'yyyy-mm-dd') >= to_date('" + sDate + "','yyyy-mm-dd')");
        if (!"".equals(qybh)) {
            sql.append(" and a.ssqybh = '" + qybh + "'");
        } else if (!"".equals(dkbh)) {
            sql.append(" and a.dkbh = '" + dkbh + "'");
        }
        sql.append(" order by b.nszyxbh asc");
        return sql;
    }

    @Transactional
    public Object operation(String card, String qybm, String ids, String lx) {
        Map<String, String> msgMap = new HashMap<String, String>();
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String czsj = sdf.format(date);
            String tableName = "t_zz_sc" + lx;
            String id[] = ids.split(",");
            String sql = "select * from t_zz_gzryda t where t.qybm = '" + qybm + "' and t.ickbh = '" + card + "'";
            Map<String, Object> gzryMap = DatabaseHandlerDao.getInstance().queryForMap(sql);
            if (gzryMap.size() == 0) {
                msgMap.put("result", "error");
                return msgMap;
            }
            for (int i = 0; i < id.length; i++) {
                sql = "select a.id as aid, b.id as bid, b.qsnsx, b.czsj  from t_zz_scda a," + tableName + " b where a.id = b.pid and a.qybm = '" + qybm + "' and b.id = '" + id[i] + "' and b.is_delete <> '1'";
                Map<String, Object> nsxMap = DatabaseHandlerDao.getInstance().queryForMap(sql);
                if ("1".equals(String.valueOf(nsxMap.get("QSNSX"))) && ("".equals(String.valueOf(nsxMap.get("CZSJ"))) || "null".equals(String.valueOf(nsxMap.get("CZSJ"))))) {
                    //若是开始操作起始农事项：堆算农事项时间
                    //堆算农事项时间
                    String tables[] = {"t_zz_scbz", "t_zz_scgg", "t_zz_scsf", "t_zz_scyy", "t_zz_scjc", "t_zz_sccs", "t_zz_sccc", "t_zz_scqt"};
                    for (int m = 0; m < tables.length; m++) {
                        sql = "select * from " + tables[m] + " t where t.pid = '" + String.valueOf(nsxMap.get("AID")) + "' and t.is_delete <> '1' and ygsj1 is null and ygsj2 is null";
                        List<Map<String, Object>> nsxList = DatabaseHandlerDao.getInstance().queryForMaps(sql);
                        for (Map<String, Object> map : nsxList) {
                            if (!"".equals(String.valueOf(map.get("NSXJGSJ"))) && !"".equals(String.valueOf(map.get("CZFDSJ")))) {
                                sdf = new SimpleDateFormat("yyyy-MM-dd");
                                czsj = sdf.format(date);
                                Date czsjDate = sdf.parse(czsj);
                                String ygsj1 = sdf.format(new Date(czsjDate.getTime() + Long.parseLong(String.valueOf(map.get("NSXJGSJ"))) * 24 * 60 * 60 * 1000 - Long.parseLong(String.valueOf(map.get("CZFDSJ"))) * 24 * 60 * 60 * 1000));
                                String ygsj2 = sdf.format(new Date(czsjDate.getTime() + Long.parseLong(String.valueOf(map.get("NSXJGSJ"))) * 24 * 60 * 60 * 1000 + Long.parseLong(String.valueOf(map.get("CZFDSJ"))) * 24 * 60 * 60 * 1000));
                                sql = "update " + tables[m] + " t set t.ygsj1 = '" + ygsj1 + "',t.ygsj2 = '" + ygsj2 + "' where t.id = '" + String.valueOf(map.get("ID")) + "'";
                                DatabaseHandlerDao.getInstance().executeSql(sql);
                            }
                        }
                    }
                }
                if (("".equals(String.valueOf(nsxMap.get("CZSJ"))) || "null".equals(String.valueOf(nsxMap.get("CZSJ"))))) {
                    //写入起始农事项操作时间
                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    czsj = sdf.format(date);
                    sql = "update " + tableName + " t set t.czsj = '" + czsj + "' where id = '" + id[i] + "'";
                    DatabaseHandlerDao.getInstance().executeSql(sql);
                }
                //录入操作记录
                saveCzjl(id[i], String.valueOf(gzryMap.get("GZRYBH")), String.valueOf(gzryMap.get("XM")));
            }
            msgMap.put("result", "success");
        } catch (ParseException e) {
            msgMap.put("result", "error");
            e.printStackTrace();
        }
        return msgMap;
    }

    public Object queryCzjl(String ids,String lx) {
        String id[] = ids.split(",");
        StringBuffer sql = new StringBuffer();
        sql.append("select a.nszyxmc,t.* from t_zz_sc" + lx + " a,t_zz_czjl t where a.id = t.pid and t.pid in ('!'");
        for (int i = 0; i < id.length; i++) {
            sql.append(",'" + id[i] + "'");
        }
        sql.append(") order by t.kssj asc");
        List<Map<String, Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(String.valueOf(sql));
        return dataList;
    }

    /**
     * 再做一次
     *
     * @param ids 再做一次的农事项ID
     * @param lx  再做一次的农事项类型
     */
    @Transactional
    public Object doOneAgain(String ids, String lx) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            String id = ids.split(",")[0];
            String tableName = "t_zz_sc" + lx;
            tableName = tableName.toUpperCase();
            StringBuffer sql;
            //查询新一条农事项的农事项编号、ID以及创建时间
            sql = new StringBuffer();
            sql.append("SELECT (MAX(NSZYXBH)+1) AS NSZYXBH FROM " + tableName + " T WHERE PID = (SELECT PID FROM " + tableName + " A WHERE A.ID = '" + id + "')  AND IS_DELETE <> '1'");
            Map<String, Object> newNsx = DatabaseHandlerDao.getInstance().queryForMap(sql.toString());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String nDate = sdf.format(date);
            //插入一条新的农事项
            sql = new StringBuffer();
            sql.append("SELECT T.COLUMN_NAME  FROM USER_TAB_COLS T WHERE TABLE_NAME = '" + tableName + "'");
            List<Map<String, Object>> nameList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString());
            sql = new StringBuffer();
            sql.append("INSERT INTO " + tableName + " (");
            for (Map<String, Object> map : nameList) {
                sql.append("" + String.valueOf(map.get("COLUMN_NAME")) + ",");
            }
            sql.append("--");
            sql = new StringBuffer(sql.toString().replaceAll(",--", "") + ") select");
            for (Map<String, Object> map : nameList) {
                if ("ID".equals(String.valueOf(map.get("COLUMN_NAME")))) {
                    sql.append(" SYS_GUID() AS ID,");
                } else if ("NSZYXBH".equals(String.valueOf(map.get("COLUMN_NAME")))) {
                    sql.append(" '" + String.valueOf(newNsx.get("NSZYXBH")) + "' AS NSZYXBH,");
                } else if ("CREATE_TIME".equals(String.valueOf(map.get("COLUMN_NAME")))) {
                    sql.append(" '" + nDate + "' AS CREATE_TIME,");
                } else if ("CZSJ".equals(String.valueOf(map.get("COLUMN_NAME")))) {
                    sql.append(" '' AS CZSJ,");
                } else if ("QSNSX".equals(String.valueOf(map.get("COLUMN_NAME")))) {
                    sql.append(" '0' AS QSNSX,");
                } else {
                    sql.append(" " + String.valueOf(map.get("COLUMN_NAME")) + ",");
                }
            }
            sql.append("--");
            sql = new StringBuffer(sql.toString().replaceAll(",--", "") + " FROM " + tableName + " T WHERE T.ID = '" + id + "'");
            DatabaseHandlerDao.getInstance().executeSql(sql.toString());

            //插入农事项投入品
            sql = new StringBuffer();
            sql.append("SELECT NSZYXBH AS NSZYXBH,ID FROM " + tableName + " T WHERE PID = (SELECT PID FROM " + tableName + " A WHERE A.ID = '" + id + "') AND NSZYXBH = '" + String.valueOf(newNsx.get("NSZYXBH")) + "' AND IS_DELETE <> '1'");
            newNsx = DatabaseHandlerDao.getInstance().queryForMap(sql.toString());
            sql = new StringBuffer();
            sql.append("SELECT COLUMN_NAME FROM USER_TAB_COLS WHERE TABLE_NAME = '" + tableName + "TRP'");
            nameList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString());
            sql = new StringBuffer();
            sql.append("SELECT * FROM " + tableName + "TRP T WHERE T.PID = '" + id + "'");
            List<Map<String, Object>> trpList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString());

            sql = new StringBuffer();
            sql.append("INSERT INTO " + tableName + "TRP (");
            for (Map<String, Object> map : nameList) {
                sql.append("" + map.get("COLUMN_NAME") + ",");
            }
            sql.append("--");
            sql = new StringBuffer(sql.toString().replaceAll(",--", "") + ") select ");
            for (Map<String, Object> map : nameList) {
                if ("ID".equals(String.valueOf(map.get("COLUMN_NAME")))) {
                    sql.append(" SYS_GUID() AS ID,");
                } else if ("NSZYXBH".equals(String.valueOf(map.get("COLUMN_NAME")))) {
                    sql.append(" '" + String.valueOf(newNsx.get("NSZYXBH")) + "' AS NSZYXBH,");
                } else if ("CREATE_TIME".equals(String.valueOf(map.get("COLUMN_NAME")))) {
                    sql.append(" '" + nDate + "' AS CREATE_TIME,");
                } else if ("PID".equals(String.valueOf(map.get("COLUMN_NAME")))) {
                    sql.append(" '" + String.valueOf(newNsx.get("ID")) + "' AS PID,");
                } else {
                    sql.append(String.valueOf(map.get("COLUMN_NAME")) + ",");
                }
            }
            sql.append("--");
            sql = new StringBuffer(sql.toString().replaceAll(",--", "") + " FROM " + tableName + "TRP T WHERE T.PID = '" + id + "' AND T.IS_DELETE <> '1'");
            DatabaseHandlerDao.getInstance().executeSql(sql.toString());
            if(resultMap.size()==0){
                resultMap.put("result", "SUCCESS");
            }
        } catch (Exception e) {
            resultMap.put("result", "ERROR");
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     * qiucs 2015-2-28 上午11:23:29
     * <p>描述: 将JsonNode转换为键值对 </p>
     *
     * @return Map<String,String>
     */
    protected Map<String, String> node2map(JsonNode node) {
        Map<String, String> dMap = new HashMap<String, String>();
        Iterator<String> it = node.fieldNames();
        while (it.hasNext()) {
            String col = (String) it.next();
            dMap.put(col, node.get(col).asText());
        }
        return dMap;
    }

    @Transactional
    public Object commentNsx(String cz, String card, String qybm) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            JsonNode entityNode = JsonUtil.json2node(cz);
            Map<String, String> czMap = node2map(entityNode);
            czMap.remove(czMap.get("NULL"));
            StringBuffer sql;
            sql = new StringBuffer();
            sql.append("SELECT * FROM T_ZZ_GZRYDA T WHERE T.IS_DELETE <> '1' AND QYBM = '" + qybm + "' AND T.ICKBH = '" + card + "'");
            Map<String, Object> gzryMap = DatabaseHandlerDao.getInstance().queryForMap(sql.toString());
            if (gzryMap.size() == 0) {
                resultMap.put("result", "ERROR");
                resultMap.put("message", "IC卡信息错误！");
                return resultMap;
            }
            if (czMap.size() == 0) {
                resultMap.put("result", "ERROR");
                resultMap.put("message", "请先评论再进行刷卡！");
                return resultMap;
            }
            Iterator<Map.Entry<String, String>> entries = czMap.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, String> entry = entries.next();
                sql = new StringBuffer();
                sql.append("UPDATE T_ZZ_CZJl T SET PJ = '" + String.valueOf(entry.getValue()) + "',PJR = '" + String.valueOf(gzryMap.get("XM")) + "',PJRBH = '" + String.valueOf(gzryMap.get("GZRYBH")) + "' WHERE T.ID = '" + String.valueOf(entry.getKey()) + "'");
                DatabaseHandlerDao.getInstance().executeSql(sql.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("result", "ERROR");
            resultMap.put("message", "操作失败！");
        }
        resultMap.put("result", "SUCCESS");
        resultMap.put("message", "操作成功！");
        return resultMap;
    }

    public Object getQybm(String ickbh){
        String sql = "select * from t_zz_gzryda where ickbh = '" + ickbh + "' and is_delete <> '1'";
        Map<String,Object> dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql);
        if(dataMap.size() == 0){
            sql = "select * from t_zz_dkxx where ickbh = '" + ickbh + "' and is_delete <> '1'";
            dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql);
        }
        return dataMap;
    }



    /***********************************************touchV2.0 begin*************************************************************/
    @Transactional
    public Object touchIn(String card){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("RESULT","ERROR");
        resultMap.put("MESSAGE","IC卡号错误或者不是农事负责人卡号，请检查IC卡！");
        String sql = "SELECT * FROM V_TOUCH_IN1 T WHERE T.ICKBH = '" + card + "'";
        List<Map<String, Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        if(dataList.size() == 0){
            return resultMap;
        }
        resultMap.put("RESULT","SUCCESS");
        resultMap.put("MESSAGE","操作成功！");
        resultMap.put("DATA",dataList.get(0));
        return resultMap;
    }

    /**查询区域信息
     * @param operatorIcCode  操作人Ic号
     * @return
     */
    @Transactional
    public Object queryArea(String operatorIcCode){
//        根据区域或者地块负责人查询所属区域
//        String sql = "SELECT A.QYBH,A.QYMC FROM T_ZZ_QYXX A,T_ZZ_DKXX B WHERE A.QYBH = B.QYBH AND A.IS_DELETE <> '1' AND B.IS_DELETE <> '1' AND (A.FZRBH IN (SELECT MAX(C.GZRYBH) FROM T_ZZ_GZRYDA C WHERE C.ICKBH = '" + operatorIcCode + "' ) OR B.DKFZRBH IN (SELECT MAX(C.GZRYBH) FROM T_ZZ_GZRYDA C WHERE C.ICKBH = '" + operatorIcCode + "' )) AND A.QYBM IN (SELECT MAX(C.QYBM) FROM T_ZZ_GZRYDA C WHERE C.ICKBH = '" + operatorIcCode + "') GROUP BY A.QYBH,A.QYMC";
        String sql = "SELECT A.QYBH,A.QYMC FROM (SELECT V.QYBH,V.QYMC,V.ICKBH FROM V_TOUCH_IN1 V GROUP BY V.QYBH,V.QYMC,V.ICKBH) A WHERE A.ICKBH = ?";
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        Map<String,Object> shareMap = new HashMap<String, Object>();
        shareMap.put("QYBH", "all");
        shareMap.put("QYMC", "区域（全部）");
        dataList.add(shareMap);
        dataList.addAll(DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{operatorIcCode}));
        return dataList;
    }

    /**查询地块信息
     * @param areaCode  区域编码
     * @param operatorIcCode  操作人编号
     * @return
     */
    @Transactional
    public Object queryLand(String areaCode, String operatorIcCode){
//        根据区域或者地块负责人查询所属区域
//        String sql = "SELECT A.QYBH,A.QYMC，B.DKBH,B.DKMC FROM T_ZZ_QYXX A,T_ZZ_DKXX B WHERE A.QYBH = B.QYBH AND A.IS_DELETE <> '1' AND B.IS_DELETE <> '1' AND (A.FZRBH IN (SELECT MAX(C.GZRYBH) FROM T_ZZ_GZRYDA C WHERE C.ICKBH = '" + operatorIcCode + "' ) OR B.DKFZRBH IN (SELECT MAX(C.GZRYBH) FROM T_ZZ_GZRYDA C WHERE C.ICKBH = '" + operatorIcCode + "' )) AND A.QYBM IN (SELECT MAX(C.QYBM) FROM T_ZZ_GZRYDA C WHERE C.ICKBH = '" + operatorIcCode + "') GROUP BY A.QYBH,A.QYMC,B.DKBH,B.DKMC";
        String sql = "SELECT A.QYBH,A.QYMC，A.DKBH,A.DKMC FROM (SELECT V.QYBH,V.QYMC,V.DKBH,V.DKMC,V.ICKBH FROM V_TOUCH_IN1 V GROUP BY V.QYBH,V.QYMC,V.DKBH,V.DKMC,V.ICKBH) A WHERE A.ICKBH = ?";
        if(!"all".equals(areaCode)){
            sql += " AND A.QYBH = '" + areaCode + "'";
        }
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        Map<String,Object> shareMap = new HashMap<String, Object>();
        shareMap.put("DKBH", "all");
        shareMap.put("DKMC", "地块（全部）");
        dataList.add(shareMap);
        dataList.addAll(DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{operatorIcCode}));
        return dataList;
    }


    /**查询农事项
     * @param area
     * @param land
     * @param farmingType
     * @param company
     * @return
     */
    @Transactional
    public Object queryFarmingItem(String area, String land, String farmingType, String company, String is_end, String operatorIcCode) {
        //计算当前时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String nowTime = sdf.format(date);
        //查询符合条件的不同状态的农事项数量
        StringBuilder countSql ;
        //查询符合条件的农事项
        StringBuilder sql = new StringBuilder("select * from" +
                " (select c.*,max(d.id) as gid,max(d.kssj) as beginTime,max(d.jssj) as endTime,max(d.czr) as operator from (select a.id as id,b.id as sid,a.qybm,a.ssqymc,a.ssqybh,a.dkmc,a.dkbh,'bz' as type,b.nszyxmc,nvl(b.qsnsx,0) as qsnsx,b.ygsj1,b.ygsj2,b.is_increase from t_zz_scda a right join t_zz_scbz b on a.id = b.pid  where b.is_delete <> '1' and a.is_delete <> '1') c left join t_zz_czjl d on c.sid = d.pid and d.is_delete <> '1' group by c.id,c.sid,c.qybm,c.ssqymc,c.ssqybh,c.dkmc,c.dkbh,c.type,c.nszyxmc,c.qsnsx,c.ygsj1,c.ygsj2,c.is_increase" +
                " union" +
                " select c.*,max(d.id) as gid,max(d.kssj) as beginTime,max(d.jssj) as endTime,max(d.czr) as operator from (select a.id as id,b.id as sid,a.qybm,a.ssqymc,a.ssqybh,a.dkmc,a.dkbh,'gg' as type,b.nszyxmc,nvl(b.qsnsx,0) as qsnsx,b.ygsj1,b.ygsj2,b.is_increase from t_zz_scda a right join t_zz_scgg b on a.id = b.pid  where b.is_delete <> '1' and a.is_delete <> '1') c left join t_zz_czjl d on c.sid = d.pid and d.is_delete <> '1' group by c.id,c.sid,c.qybm,c.ssqymc,c.ssqybh,c.dkmc,c.dkbh,c.type,c.nszyxmc,c.qsnsx,c.ygsj1,c.ygsj2,c.is_increase" +
                " union" +
                " select c.*,max(d.id) as gid,max(d.kssj) as beginTime,max(d.jssj) as endTime,max(d.czr) as operator from (select a.id as id,b.id as sid,a.qybm,a.ssqymc,a.ssqybh,a.dkmc,a.dkbh,'sf' as type,b.nszyxmc,nvl(b.qsnsx,0) as qsnsx,b.ygsj1,b.ygsj2,b.is_increase from t_zz_scda a right join t_zz_scsf b on a.id = b.pid  where b.is_delete <> '1' and a.is_delete <> '1') c left join t_zz_czjl d on c.sid = d.pid and d.is_delete <> '1' group by c.id,c.sid,c.qybm,c.ssqymc,c.ssqybh,c.dkmc,c.dkbh,c.type,c.nszyxmc,c.qsnsx,c.ygsj1,c.ygsj2,c.is_increase" +
                " union" +
                " select c.*,max(d.id) as gid,max(d.kssj) as beginTime,max(d.jssj) as endTime,max(d.czr) as operator from (select a.id as id,b.id as sid,a.qybm,a.ssqymc,a.ssqybh,a.dkmc,a.dkbh,'yy' as type,b.nszyxmc,nvl(b.qsnsx,0) as qsnsx,b.ygsj1,b.ygsj2,b.is_increase from t_zz_scda a right join t_zz_scyy b on a.id = b.pid  where b.is_delete <> '1' and a.is_delete <> '1') c left join t_zz_czjl d on c.sid = d.pid and d.is_delete <> '1' group by c.id,c.sid,c.qybm,c.ssqymc,c.ssqybh,c.dkmc,c.dkbh,c.type,c.nszyxmc,c.qsnsx,c.ygsj1,c.ygsj2,c.is_increase" +
                " union" +
                " select c.*,max(d.id) as gid,max(d.kssj) as beginTime,max(d.jssj) as endTime,max(d.czr) as operator from (select a.id as id,b.id as sid,a.qybm,a.ssqymc,a.ssqybh,a.dkmc,a.dkbh,'cs' as type,b.nszyxmc,nvl(b.qsnsx,0) as qsnsx,b.ygsj1,b.ygsj2,b.is_increase from t_zz_scda a right join t_zz_sccs b on a.id = b.pid  where b.is_delete <> '1' and a.is_delete <> '1') c left join t_zz_czjl d on c.sid = d.pid and d.is_delete <> '1' group by c.id,c.sid,c.qybm,c.ssqymc,c.ssqybh,c.dkmc,c.dkbh,c.type,c.nszyxmc,c.qsnsx,c.ygsj1,c.ygsj2,c.is_increase" +
                " union" +
                " select c.*,max(d.id) as gid,max(d.kssj) as beginTime,max(d.jssj) as endTime,max(d.czr) as operator from (select a.id as id,b.id as sid,a.qybm,a.ssqymc,a.ssqybh,a.dkmc,a.dkbh,'cc' as type,b.nszyxmc,nvl(b.qsnsx,0) as qsnsx,b.ygsj1,b.ygsj2,b.is_increase from t_zz_scda a right join t_zz_sccc b on a.id = b.pid  where b.is_delete <> '1' and a.is_delete <> '1') c left join t_zz_czjl d on c.sid = d.pid and d.is_delete <> '1' group by c.id,c.sid,c.qybm,c.ssqymc,c.ssqybh,c.dkmc,c.dkbh,c.type,c.nszyxmc,c.qsnsx,c.ygsj1,c.ygsj2,c.is_increase" +
                " union" +
                " select c.*,max(d.id) as gid,max (d.kssj) as beginTime,max(d.jssj) as endTime,max(d.czr) as operator from (select a.id as id,b.id as sid,a.qybm,a.ssqymc,a.ssqybh,a.dkmc,a.dkbh,'qt' as type,b.nszyxmc,nvl(b.qsnsx,0) as qsnsx,b.ygsj1,b.ygsj2,b.is_increase from t_zz_scda a right join t_zz_scqt b on a.id = b.pid  where b.is_delete <> '1' and a.is_delete <> '1') c left join t_zz_czjl d on c.sid = d.pid and d.is_delete <> '1' group by c.id,c.sid,c.qybm,c.ssqymc,c.ssqybh,c.dkmc,c.dkbh,c.type,c.nszyxmc,c.qsnsx,c.ygsj1,c.ygsj2,c.is_increase) x" +
                " where 1 = 1");
        if(!"".equals(area) && !"null".equals(area) && !"all".equals(area)){
            sql.append(" and x.ssqybh = '" + area + "'");
        }else{
            sql.append(" and x.ssqybh in ");
            sql.append(" (SELECT A.QYBH FROM (SELECT V.QYBH,V.QYMC,V.ICKBH FROM V_TOUCH_IN1 V GROUP BY V.QYBH,V.QYMC,V.ICKBH) A WHERE A.ICKBH = '" + operatorIcCode + "')");
        }
        if(!"".equals(land) && !"null".equals(land) && !"all".equals(land)){
            sql.append(" and x.dkbh = '" + land + "'");
        }else{
            sql.append(" and x.dkbh in ");
            sql.append(" (SELECT A.DKBH FROM (SELECT V.QYBH,V.QYMC,V.DKBH,V.DKMC,V.ICKBH FROM V_TOUCH_IN1 V GROUP BY V.QYBH,V.QYMC,V.DKBH,V.DKMC,V.ICKBH) A WHERE A.ICKBH = '" + operatorIcCode + "')");
        }
        if(!"".equals(farmingType) && !"null".equals(farmingType) && !"all".equals(farmingType)){
            sql.append(" and x.type = '" + farmingType + "'");
        }
        countSql = new StringBuilder(sql.toString());
        if("1".equals(is_end)){
            sql.append(" and x.endtime is not null");
            countSql.append(" and x.endtime is null and to_date(x.ygsj1,'yyyy-mm-dd') <= to_date('" + nowTime + "','yyyy-mm-dd') and to_date(x.ygsj2, 'yyyy-mm-dd') >= to_date('" + nowTime + "', 'yyyy-mm-dd')");
            countSql.append(" and qybm = '" + company + "'  order by x.endTime asc,x.ygsj1 asc");
            sql.append(" and qybm = '" + company + "'  order by x.endTime desc,x.ygsj1 asc");
        }else if("0".equals(is_end)){
            sql.append(" and x.endtime is null and to_date(x.ygsj1,'yyyy-mm-dd') <= to_date('" + nowTime + "','yyyy-mm-dd') and to_date(x.ygsj2, 'yyyy-mm-dd') >= to_date('" + nowTime + "', 'yyyy-mm-dd')");
            countSql.append(" and x.endtime is not null");
            countSql.append(" and qybm = '" + company + "'  order by x.qsnsx desc,x.beginTime asc,x.ygsj1 asc");
            sql.append(" and qybm = '" + company + "'  order by x.qsnsx desc,x.beginTime asc,x.ygsj1 asc");
        }
        List<Map<String,Object>> resultList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString());
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("DATA",resultList);
        resultMap.put("COUNT",(DatabaseHandlerDao.getInstance().queryForMaps(countSql.toString())).size());
        return resultMap;
    }

    /**操作农事项
     * @param sid
     * @param ickbh
     * @param farmingType
     * @return
     */
    @Transactional
    public Object operationFarmingItem(String sid, String ickbh, String farmingType) {
        Map<String,Object> shareMap ;
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            //查询操作人
            StringBuilder sql = new StringBuilder("SELECT T.XM,T.GZRYBH FROM T_ZZ_GZRYDA T WHERE T.ICKBH = ?");
            List<Map<String, Object>> operatorList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString(), new Object[]{ickbh});
            //计算当前时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String dateStr = sdf.format(date);
            //判断是否是开始操作
            String sids[] = sid.split(",");
            String farmingTypes[] = farmingType.split(",");
            if(sids.length == farmingTypes.length){
                for(int i = 0; i < sids.length; i++){
                    sql = new StringBuilder("SELECT 1 FROM T_ZZ_CZJL T WHERE T.PID = ?");
                    List<Map<String, Object>> operationList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString(), new Object[]{sids[i]});
                    if(operationList.size() == 0){
                        sql = new StringBuilder("SELECT T.PID AS ID,T.QSNSX,T.CZSJ FROM T_ZZ_SC" + farmingTypes[i] + " T WHERE ID = '" + sids[i] + "' AND T.IS_DELETE <> '1'");
                        shareMap = DatabaseHandlerDao.getInstance().queryForMap(sql.toString());
                        //如果操作起始农事则计算其他农事项起始结束时间
                        if("1".equals(String.valueOf(shareMap.get("QSNSX"))) && ("".equals(String.valueOf(shareMap.get("CZSJ"))) || "null".equals(String.valueOf(shareMap.get("CZSJ"))))){
                            //堆算农事项时间
                            String tables[] = {"T_ZZ_SCBZ", "T_ZZ_SCGG", "T_ZZ_SCSF", "T_ZZ_SCYY", "T_ZZ_SCCS", "T_ZZ_SCCC", "T_ZZ_SCQT"};
                            for (int m = 0; m < tables.length; m++) {
                                sql = new StringBuilder("SELECT * FROM " + tables[m] + " T WHERE T.PID = '" + String.valueOf(shareMap.get("ID")) + "' AND T.IS_DELETE <> '1' AND YGSJ1 IS NULL AND YGSJ2 IS NULL");
                                List<Map<String, Object>> farmingList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString());
                                for (Map<String, Object> map : farmingList) {
                                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    String operationTime = sdf.format(date);
                                    Date czsjDate = sdf.parse(operationTime);
                                    String ygsj1 = sdf.format(new Date(czsjDate.getTime() + Long.parseLong(String.valueOf(map.get("NSXJGSJ"))) * 24 * 60 * 60 * 1000 - Long.parseLong(String.valueOf(map.get("CZFDSJ"))) * 24 * 60 * 60 * 1000));
                                    String ygsj2 = sdf.format(new Date(czsjDate.getTime() + Long.parseLong(String.valueOf(map.get("NSXJGSJ"))) * 24 * 60 * 60 * 1000 + Long.parseLong(String.valueOf(map.get("CZFDSJ"))) * 24 * 60 * 60 * 1000));
                                    String budgettime = sdf.format(new Date(czsjDate.getTime() + Long.parseLong(String.valueOf(map.get("NSXJGSJ"))) * 24 * 60 * 60 * 1000));
                                    sql = new StringBuilder("UPDATE " + tables[m] + " T SET T.YGSJ1 = '" + ygsj1 + "',T.YGSJ2 = '" + ygsj2 + "',T.BUDGETTIME = '" + budgettime + "' WHERE T.ID = '" + String.valueOf(map.get("ID")) + "'");
                                    DatabaseHandlerDao.getInstance().executeSql(sql.toString());
                                }
                            }
                        }
                        //修改操作农事项的操作时间
                        sql = new StringBuilder("UPDATE T_ZZ_SC" + farmingTypes[i] + " T SET T.CZSJ = '" + dateStr + "' WHERE ID = '" + sids[i] + "'");
                        DatabaseHandlerDao.getInstance().executeSql(sql.toString());
                        //修改对应操作记录
                        sql = new StringBuilder("INSERT INTO T_ZZ_CZJL(ID,KSSJ,CZR,CZRBH,CREATE_TIME,PID) VALUES(SYS_GUID(),'" + dateStr + "','" + operatorList.get(0).get("XM") + "','" + operatorList.get(0).get("GZRYBH") + "','" + dateStr + "','" + sids[i] + "')");
                    }else{
                        sql = new StringBuilder("UPDATE T_ZZ_CZJL T SET T.JSSJ = '" + dateStr + "' WHERE T.PID = '" + sids[i] + "'");
                    }
                    DatabaseHandlerDao.getInstance().executeSql(sql.toString());
                }
                resultMap.put("RESULT", "SUCCESS");
                resultMap.put("MESSAGE","操作成功！");
            }else {
                resultMap.put("RESULT", "ERROR");
                resultMap.put("MESSAGE","操作失败！");
            }
            return resultMap;
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            resultMap.put("RESULT","ERROR");
            resultMap.put("MESSAGE","操作失败！");
        }
        return resultMap;
    }

    /**
     * 回退操作
     * @param sid 操作的农事项ID
     * @param farmingType 操作的农事项类型
     * @return
     */
    @Transactional
    public Object rollBack(String sid,String farmingType){
        String sids[] = sid.split(",");
        String farmingTypes[] = farmingType.split(",");
        Map<String,Object> shareMap;
        StringBuilder sql;
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("RESULT","ERROR");
        resultMap.put("MESSAGE","操作失败！");
        try{
            if(sids.length == farmingTypes.length){
                 for(int i = 0; i < sids.length; i++){
                     sql = new StringBuilder("SELECT A.CZSJ AS OPERATIONTIME, B.KSSJ AS BEGINTIME, B.JSSJ AS ENDTIME,B.ID AS GID FROM T_ZZ_SC" + farmingTypes[i] + " A,T_ZZ_CZJL B WHERE A.ID = B.PID AND B.IS_DELETE <> '1' AND A.ID = ?");
                     List<Map<String, Object>> share1Map = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString(), new Object[]{sids[i]});
                     Map<String,Object> operationMessageMap = share1Map.size() == 0 ? new HashMap<String, Object>() : share1Map.get(0);
                     if(!"".equals(String.valueOf(operationMessageMap.get("ENDTIME"))) && !"null".equals(String.valueOf(operationMessageMap.get("ENDTIME")))){
                         //将完成状态改成进行中状态
                         sql = new StringBuilder("UPDATE T_ZZ_CZJL T SET T.JSSJ = '' WHERE T.PID = ?");
                         DatabaseHandlerDao.getInstance().executeSql(sql.toString(), new Object[]{sids[i]});
                         resultMap.put("LEVEL","2");
                     }else if(!"".equals(String.valueOf(operationMessageMap.get("BEGINTIME"))) && !"null".equals(String.valueOf(operationMessageMap.get("BEGINTIME"))) && !"".equals(String.valueOf(operationMessageMap.get("OPERATIONTIME"))) && !"null".equals(String.valueOf(operationMessageMap.get("OPERATIONTIME")))){
                         //将进行中状态改成未开始状态
                         sql = new StringBuilder("DELETE FROM T_ZZ_CZJL T WHERE T.PID = ?");
                         DatabaseHandlerDao.getInstance().executeSql(sql.toString(), new Object[]{sids[i]});
                         sql = new StringBuilder("update t_zz_sc" + farmingTypes[i] + " t set t.czsj = '',t.budgettime = '' where t.id = ?");
                         DatabaseHandlerDao.getInstance().executeSql(sql.toString(), new Object[]{sids[i]});
                         resultMap.put("LEVEL","1");
                     }
                 }
                resultMap.put("RESULT","SUCCESS");
                resultMap.put("MESSAGE","操作成功！");
                return resultMap;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally{

        }
        return resultMap;
    }

    /**批量开始操作
     * @param sid
     * @param ickbh
     * @param farmingType
     * @return
     */
    @Transactional
    public Object beginOperation(String sid, String ickbh, String farmingType) {
        Map<String,Object> shareMap ;
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("RESULT","SUCCESS");
        resultMap.put("MESSAGE","操作成功！");
        try {
            //查询操作人
            StringBuilder sql = new StringBuilder("SELECT T.XM,T.GZRYBH FROM T_ZZ_GZRYDA T WHERE T.ICKBH = ?");
            List<Map<String, Object>> operatorList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString(), new Object[]{ickbh});
            //计算当前时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String dateStr = sdf.format(date);
            //判断是否是开始操作
            String sids[] = sid.split(",");
            String farmingTypes[] = farmingType.split(",");
            if(sids.length == farmingTypes.length){
                for(int i = 0; i < sids.length; i++){
                    sql = new StringBuilder("SELECT 1 FROM T_ZZ_CZJL T WHERE T.PID = ?");
                    List<Map<String, Object>> operationList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString(), new Object[]{sids[i]});
                    sql = new StringBuilder();
                    if(operationList.size() == 0){
                        sql = new StringBuilder("SELECT T.PID AS ID,T.QSNSX,T.CZSJ FROM T_ZZ_SC" + farmingTypes[i] + " T WHERE ID = '" + sids[i] + "' AND T.IS_DELETE <> '1'");
                        shareMap = DatabaseHandlerDao.getInstance().queryForMap(sql.toString());
                        //如果操作起始农事则计算其他农事项起始结束时间
                        if("1".equals(String.valueOf(shareMap.get("QSNSX"))) && ("".equals(String.valueOf(shareMap.get("CZSJ"))) || "null".equals(String.valueOf(shareMap.get("CZSJ"))))){
                            //堆算农事项时间
                            String tables[] = {"T_ZZ_SCBZ", "T_ZZ_SCGG", "T_ZZ_SCSF", "T_ZZ_SCYY", "T_ZZ_SCCS", "T_ZZ_SCCC", "T_ZZ_SCQT"};
                            for (int m = 0; m < tables.length; m++) {
                                sql = new StringBuilder("SELECT * FROM " + tables[m] + " T WHERE T.PID = '" + String.valueOf(shareMap.get("ID")) + "' AND T.IS_DELETE <> '1' AND YGSJ1 IS NULL AND YGSJ2 IS NULL");
                                List<Map<String, Object>> farmingList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString());
                                for (Map<String, Object> map : farmingList) {
                                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    String operationTime = sdf.format(date);
                                    Date czsjDate = sdf.parse(operationTime);
                                    String ygsj1 = sdf.format(new Date(czsjDate.getTime() + Long.parseLong(String.valueOf(map.get("NSXJGSJ"))) * 24 * 60 * 60 * 1000 - Long.parseLong(String.valueOf(map.get("CZFDSJ"))) * 24 * 60 * 60 * 1000));
                                    String ygsj2 = sdf.format(new Date(czsjDate.getTime() + Long.parseLong(String.valueOf(map.get("NSXJGSJ"))) * 24 * 60 * 60 * 1000 + Long.parseLong(String.valueOf(map.get("CZFDSJ"))) * 24 * 60 * 60 * 1000));
                                    String budgettime = sdf.format(new Date(czsjDate.getTime() + Long.parseLong(String.valueOf(map.get("NSXJGSJ"))) * 24 * 60 * 60 * 1000));
                                    sql = new StringBuilder("UPDATE " + tables[m] + " T SET T.YGSJ1 = '" + ygsj1 + "',T.YGSJ2 = '" + ygsj2 + "',T.BUDGETTIME = '" + budgettime + "' WHERE T.ID = '" + String.valueOf(map.get("ID")) + "'");
                                    DatabaseHandlerDao.getInstance().executeSql(sql.toString());
                                }
                            }
                        }
                        //修改操作农事项的操作时间
                        sql = new StringBuilder("UPDATE T_ZZ_SC" + farmingTypes[i] + " T SET T.CZSJ = '" + dateStr + "' WHERE ID = '" + sids[i] + "'");
                        DatabaseHandlerDao.getInstance().executeSql(sql.toString());
                        //修改对应操作记录
                        sql = new StringBuilder("INSERT INTO T_ZZ_CZJL(ID,KSSJ,CZR,CZRBH,CREATE_TIME,PID) VALUES(SYS_GUID(),'" + dateStr + "','" + operatorList.get(0).get("XM") + "','" + operatorList.get(0).get("GZRYBH") + "','" + dateStr + "','" + sids[i] + "')");
                    }
                    if(!"".equals(sql.toString()))
                    DatabaseHandlerDao.getInstance().executeSql(sql.toString());
                }
            }else {
                resultMap.put("RESULT", "ERROR");
                resultMap.put("MESSAGE","操作失败！");
            }
            return resultMap;
        }catch (Exception e){
            e.printStackTrace();
        }finally{
        }
        resultMap.put("RESULT","ERROR");
        resultMap.put("MESSAGE","操作失败！");
        return resultMap;
    }


    /**批量完成操作
     * @param sid
     * @param ickbh
     * @param farmingType
     * @return
     */
    @Transactional
    public Object endOperation(String sid, String ickbh, String farmingType) {
        Map<String,Object> shareMap ;
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("RESULT","SUCCESS");
        resultMap.put("MESSAGE","操作成功！");
        try {
            //查询操作人
            StringBuilder sql = new StringBuilder("SELECT T.XM,T.GZRYBH FROM T_ZZ_GZRYDA T WHERE T.ICKBH = ?");
            List<Map<String, Object>> operatorList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString(), new Object[]{ickbh});
            //计算当前时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String dateStr = sdf.format(date);
            //判断是否是开始操作
            String sids[] = sid.split(",");
            String farmingTypes[] = farmingType.split(",");
            if(sids.length == farmingTypes.length){
                for(int i = 0; i < sids.length; i++){
                    sql = new StringBuilder("SELECT 1 FROM T_ZZ_CZJL T WHERE T.PID = ?");
                    List<Map<String, Object>> operationList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString(), new Object[]{sids[i]});
                    sql = new StringBuilder();
                    if(operationList.size() != 0){
                        sql = new StringBuilder("UPDATE T_ZZ_CZJL T SET T.JSSJ = '" + dateStr + "' WHERE T.PID = '" + sids[i] + "'");
                    }
                    if(!"".equals(sql.toString()))
                    DatabaseHandlerDao.getInstance().executeSql(sql.toString());
                }
            }else {
                resultMap.put("RESULT", "ERROR");
                resultMap.put("MESSAGE","操作失败！");
            }
            return resultMap;
        }catch (Exception e){
            e.printStackTrace();
        }finally{
        }
        resultMap.put("RESULT","ERROR");
         resultMap.put("MESSAGE","操作失败！");
        return resultMap;
    }


    @Transactional
    public Object addFarmingItem(String sid, String farmingType){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            sid = sid.split(",")[0];
            String tableName = "t_zz_sc" + farmingType;
            tableName = tableName.toUpperCase();
            StringBuffer sql;
            //查询新一条农事项的农事项编号、ID以及创建时间
            sql = new StringBuffer();
            sql.append("SELECT (MAX(NSZYXBH)+1) AS NSZYXBH FROM " + tableName + " T WHERE PID = (SELECT PID FROM " + tableName + " A WHERE A.ID = '" + sid + "')  AND IS_DELETE <> '1'");
            Map<String, Object> newNsx = DatabaseHandlerDao.getInstance().queryForMap(sql.toString());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String nDate = sdf.format(date);
            //插入一条新的农事项
            sql = new StringBuffer();
            sql.append("SELECT T.COLUMN_NAME  FROM USER_TAB_COLS T WHERE TABLE_NAME = '" + tableName + "'");
            List<Map<String, Object>> nameList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString());
            sql = new StringBuffer();
            sql.append("INSERT INTO " + tableName + " (");
            for (Map<String, Object> map : nameList) {
                sql.append("" + String.valueOf(map.get("COLUMN_NAME")) + ",");
            }
            sql.append("--");
            sql = new StringBuffer(sql.toString().replaceAll(",--", "") + ") select");
            for (Map<String, Object> map : nameList) {
                if ("ID".equals(String.valueOf(map.get("COLUMN_NAME")))) {
                    sql.append(" SYS_GUID() AS ID,");
                } else if ("NSZYXBH".equals(String.valueOf(map.get("COLUMN_NAME")))) {
                    sql.append(" '" + String.valueOf(newNsx.get("NSZYXBH")) + "' AS NSZYXBH,");
                } else if ("CREATE_TIME".equals(String.valueOf(map.get("COLUMN_NAME")))) {
                    sql.append(" '" + nDate + "' AS CREATE_TIME,");
                } else if ("CZSJ".equals(String.valueOf(map.get("COLUMN_NAME")))) {
                    sql.append(" '' AS CZSJ,");
                } else if ("QSNSX".equals(String.valueOf(map.get("COLUMN_NAME")))) {
                    sql.append(" '0' AS QSNSX,");
                } else {
                    sql.append(" " + String.valueOf(map.get("COLUMN_NAME")) + ",");
                }
            }
            sql.append("--");
            sql = new StringBuffer(sql.toString().replaceAll(",--", "") + " FROM " + tableName + " T WHERE T.ID = '" + sid + "'");
            DatabaseHandlerDao.getInstance().executeSql(sql.toString());

            //插入农事项投入品
            sql = new StringBuffer();
            sql.append("SELECT NSZYXBH AS NSZYXBH,ID FROM " + tableName + " T WHERE PID = (SELECT PID FROM " + tableName + " A WHERE A.ID = '" + sid + "') AND NSZYXBH = '" + String.valueOf(newNsx.get("NSZYXBH")) + "' AND IS_DELETE <> '1'");
            newNsx = DatabaseHandlerDao.getInstance().queryForMap(sql.toString());
            sql = new StringBuffer();
            sql.append("SELECT COLUMN_NAME FROM USER_TAB_COLS WHERE TABLE_NAME = '" + tableName + "TRP'");
            nameList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString());
            sql = new StringBuffer();
            sql.append("SELECT * FROM " + tableName + "TRP T WHERE T.PID = '" + sid + "'");
            List<Map<String, Object>> trpList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString());

            sql = new StringBuffer();
            sql.append("INSERT INTO " + tableName + "TRP (");
            for (Map<String, Object> map : nameList) {
                sql.append("" + map.get("COLUMN_NAME") + ",");
            }
            sql.append("--");
            sql = new StringBuffer(sql.toString().replaceAll(",--", "") + ") select ");
            for (Map<String, Object> map : nameList) {
                if ("ID".equals(String.valueOf(map.get("COLUMN_NAME")))) {
                    sql.append(" SYS_GUID() AS ID,");
                } else if ("NSZYXBH".equals(String.valueOf(map.get("COLUMN_NAME")))) {
                    sql.append(" '" + String.valueOf(newNsx.get("NSZYXBH")) + "' AS NSZYXBH,");
                } else if ("CREATE_TIME".equals(String.valueOf(map.get("COLUMN_NAME")))) {
                    sql.append(" '" + nDate + "' AS CREATE_TIME,");
                } else if ("PID".equals(String.valueOf(map.get("COLUMN_NAME")))) {
                    sql.append(" '" + String.valueOf(newNsx.get("ID")) + "' AS PID,");
                } else {
                    sql.append(String.valueOf(map.get("COLUMN_NAME")) + ",");
                }
            }
            sql.append("--");
            sql = new StringBuffer(sql.toString().replaceAll(",--", "") + " FROM " + tableName + "TRP T WHERE T.PID = '" + sid + "' AND T.IS_DELETE <> '1'");
            DatabaseHandlerDao.getInstance().executeSql(sql.toString());
            sql = new StringBuffer("SELECT * FROM " + tableName + " T WHERE T.ID = '" + sid + "' AND T.IS_DELETE <> '1'");
            Map<String, Object> farmingItemMap = DatabaseHandlerDao.getInstance().queryForMap(sql.toString());
            sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date beginFarmingDate;//农事项起始时间
            if("1".equals(String.valueOf(farmingItemMap.get("QSNSX")))){
                beginFarmingDate = new Date(sdf.parse(String.valueOf(farmingItemMap.get("YGSJ1"))).getTime() + Long.parseLong(String.valueOf(farmingItemMap.get("CZFDSJ"))) * 24 * 60 * 60 * 1000);
            }else{
                beginFarmingDate = new Date(sdf.parse(String.valueOf(farmingItemMap.get("YGSJ1"))).getTime() + Long.parseLong(String.valueOf(farmingItemMap.get("CZFDSJ"))) * 24 * 60 * 60 * 1000 - Long.parseLong(String.valueOf(farmingItemMap.get("NSXJGSJ"))) * 24 * 60 * 60 * 1000);
            }
            Date nowDate = new Date();
            nowDate = sdf.parse(sdf.format(nowDate));
            int dateDay = (int)(nowDate.getTime() - beginFarmingDate.getTime())/(24 * 60 * 60 * 1000) + 14;//农事项间隔时间
            String antipateBeginTime = sdf.format(nowDate);//开始时间为今天
            String antipateEndTime = sdf.format(new Date(nowDate.getTime() + (Long.parseLong("30")* 24 * 60 * 60 * 1000)));//结束时间为今天开始30天之后
            String budgettime = sdf.format(new Date(nowDate.getTime() + (Long.parseLong("14")* 24 * 60 * 60 * 1000)));
            sql = new StringBuffer("UPDATE " + tableName + " T SET T.YGSJ1 = '" + antipateBeginTime + "',T.YGSJ2 = '" + antipateEndTime + "',T.NSXJGSJ = '" + dateDay + "',T.CZFDSJ = '15',T.IS_INCREASE = '1',T.BUDGETTIME = '" + budgettime + "' WHERE T.CREATE_TIME = '" + nDate + "'");
            int i = DatabaseHandlerDao.getInstance().executeSql(sql.toString());
            if(resultMap.size()==0){
                resultMap.put("result", "SUCCESS");
            }
        } catch (Exception e) {
            resultMap.put("result", "ERROR");
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     * @param sid
     * @param farmingType
     * @return
     */
    @Transactional
    public Object deleteFarmingItem(String sid, String farmingType){
        String sids[] = sid.split(",");
        String farmingTypes[] = farmingType.split(",");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("RESULT","ERROR");
        resultMap.put("MESSAGE","删除失败！");
        StringBuilder sql ;
        try{
            if(sids.length == farmingTypes.length){
                for(int i = 0; i < sids.length; i++){
                    sql = new StringBuilder("UPDATE T_ZZ_SC" + farmingTypes[i] + " T SET T.iS_DELETE = '1' WHERE T.ID = '" + sids[i] + "'");
                    DatabaseHandlerDao.getInstance().executeSql(sql.toString());
                    sql = new StringBuilder("UPDATE T_ZZ_SC" + farmingTypes[i] + "TRP T SET T.iS_DELETE = '1' WHERE T.PID = '" + sids[i] + "'");
                    DatabaseHandlerDao.getInstance().executeSql(sql.toString());
                }
                resultMap.put("RESULT","SUCCESS");
                resultMap.put("MESSAGE","删除成功！");
                return resultMap;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultMap;
    }



    /***********************************************touchV2.0 end*************************************************************/


}
