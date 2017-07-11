package com.ces.component.cyscjcxx.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.component.cspt.entity.TCsptZsxxEntity;
import com.ces.component.cyscjcxx.dao.CyscjcxxDao;
import com.ces.component.trace.utils.CompanyInfoUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.TraceChainUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class CyscjcxxService extends TraceShowModuleDefineDaoService<StringIDEntity, CyscjcxxDao> {

    /**
     * 默认权限过滤
     *
     * @return
     */
    public String defaultCode() {
        String code = SerialNumberUtil.getInstance().getCompanyCode();
        String defaultCode = " ";
        if (code != null && !"".equals(code))
            defaultCode = " AND CYXFDWBM = '" + code + "' ";
        return defaultCode;
    }

    @Override
    protected String buildCustomerFilter(String tableId,
                                         String componentVersionId, String moduleId, String menuId,
                                         Map<String, Object> paramMap) {
        // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
        return defaultCode();
    }

    public List getSpmc() {
        List list = getDao().getAllSpmc();
        return list;
    }

    public String dateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    @Override
    @Transactional
    public String save(String tableName, Map<String, String> dataMap, Map<String, Object> paramMap) {
        // 1. 获取表名
        if (StringUtil.isEmpty(tableName)) return "";
        // 2. 保存前，业务处理接口
        processBeforeSave(tableName, dataMap, paramMap);
        // 3. 根据ID判断是新增，还是修改
        String id = saveOne(tableName, dataMap);
        // 保存后，业务处理接口
        processAfterSave(tableName, dataMap, paramMap);
        /*****同步追溯信息*****/
        TCsptZsxxEntity entity = new TCsptZsxxEntity();
        entity.setJypzh(dataMap.get("JYPZH"));
        entity.setJhpch(dataMap.get("JHPCH"));
        entity.setQybm(dataMap.get("CYXFDWBM"));
        entity.setQymc(dataMap.get("CYXFDWMC"));
        entity.setJyzbm(dataMap.get("GYSBM"));
        entity.setJyzmc(dataMap.get("GYSMC"));
        entity.setXtlx("9");
        entity.setRefId(dataMap.get("ID"));
        TraceChainUtil.getInstance().syncZsxx(entity);
        /********结束*******/
        return id;
    }

    /**
     * 条码进场保存
     *
     * @param barCode
     */
    @Transactional
    public List<String> barCodeSave(String barCode) {
        String prefix = barCode.substring(0, 2);
        List<String> ids = new ArrayList<String>();
        String sql = "";
        if ("ZZ".equals(prefix)) {
//            sql = " select T.A_QYMC as QYMC,T.A_QYBM as QYBM,T.B_SPMC as SPMC,T.B_SPBM as SPBM ,T.A_PCH as CCPCH,T.A_ZL as ZL,T.A_ZSM as ZSM from V_ZZ_CCXX_ZZ_ZPXX T where T.A_CCTMH='"
//                    + barCode + "' and t.a_is_in = '0'";
//            Map<String, Object> zzccxx = DatabaseHandlerDao.getInstance().queryForMap(sql);
//            Map<String, String> dataMap = new HashMap<String, String>();
//            if (!zzccxx.isEmpty()) {
//                dataMap.put("CYXFDWBM", SerialNumberUtil.getInstance().getCompanyCode());
//                dataMap.put("CYXFDWMC", CompanyInfoUtil.getInstance().getCompanyName("CY", SerialNumberUtil.getInstance().getCompanyCode()));
//                dataMap.put("JCRQ", dateToString(new Date()));
//                dataMap.put("SPBM", String.valueOf(zzccxx.get("SPBM")));
//                dataMap.put("SPMC", String.valueOf(zzccxx.get("SPMC")));
//                dataMap.put("ZL", String.valueOf(zzccxx.get("ZL")));
//                dataMap.put("JHPCH", String.valueOf(zzccxx.get("CCPCH")));
//                dataMap.put("JYPZH", String.valueOf(zzccxx.get("ZSM")));
//                dataMap.put("GHSCBM", String.valueOf(zzccxx.get("QYBM")));
//                dataMap.put("GHSCMC", String.valueOf(zzccxx.get("QYMC")));
//                String jcId = save("T_CY_SCJCXX", dataMap, null);
//
//                //更新种植出场（已进场）
//                sql = "update t_zz_ccxx t set t.is_in = '1' where upper(t.cctmh) = '" + barCode + "'";
//                DatabaseHandlerDao.getInstance().executeSql(sql);
//
//                ids.add(jcId);
//            } old
            Map<String, Object> dataMap = null;
            sql = "select ID,t.qybm,t.khmc,t.zzl,t.KHBH, PSZRR, XSDDH, CCSJ, PSFS, CPH,t.cclsh,t.SFDH" +
                    "  from T_ZZ_CCGL t" +
                    " where SFDH =? " +
                    "   AND IS_DELIVERED <> '1'";
            //查询有效的出场信息:根据出场条码
            dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{barCode});
            if (dataMap != null && !dataMap.isEmpty()) {
                //查询企业相关信息
                String qyxxSql = "select * from T_ZZ_CDDA T where T.QYBM=?";
                Map<String, Object> qyxxMap = DatabaseHandlerDao.getInstance().queryForMap(qyxxSql, new Object[]{dataMap.get("QYBM")});
                //查询出场详细信息
                //散货出场信息
                String shSql = "select t_.ID," +
                        "       t_.PCH," +
                        "       t_.CPZSM," +
                        "       t_.PZ," +
                        "       a.PLBH," +
                        "       t_.PZBH," +
                        "       t_.CSZZL," +
                        "       t_.KCZL," +
                        "       t_.CCZL," +
                        "       t_.CJZDBH,t_.ZSPCH," +
                        "       t_.PID " +
                        "  from T_ZZ_CCSHXX t_ left join t_zz_xpzxx a on t_.pzbh = a.pzbh and t_.qybm = a.qybm" +
                        " where (t_.PID = ? ) AND (t_.IS_DELETE <> '1')";
                List<Map<String, Object>> shList = DatabaseHandlerDao.getInstance().queryForMaps(shSql, new Object[]{dataMap.get("ID")});
                //产品出场信息
                String cpSql = "select t.bzlsh,t.cpbh,t.cpmc,t.cpdj,t.ccjs,t.zl,t.zsm,t.ZSPCH from T_ZZ_CCBZCPXX t where t.pid = ? AND (IS_DELETE <> '1') ";
                List<Map<String, Object>> cpList = DatabaseHandlerDao.getInstance().queryForMaps(cpSql, new Object[]{dataMap.get("ID")});
                //散货信息保存
                for (Map<String, Object> sh : shList) {
                    Map<String, String> saveMap = new HashMap<String, String>();
                    saveMap.put("CYXFDWBM", SerialNumberUtil.getInstance().getCompanyCode());
                    saveMap.put("CYXFDWMC", CompanyInfoUtil.getInstance().getCompanyName("CY", SerialNumberUtil.getInstance().getCompanyCode()));
                    saveMap.put("JCRQ", dateToString(new Date()));
                    saveMap.put("SPBM", String.valueOf(sh.get("PLBH")));
                    saveMap.put("SPMC", String.valueOf(sh.get("PZ")));
                    saveMap.put("ZL", String.valueOf(sh.get("CCZL")));
                    saveMap.put("JHPCH", String.valueOf(sh.get("ZSPCH")));
                    saveMap.put("JYPZH", String.valueOf(sh.get("CPZSM")));
                    saveMap.put("GYSBM", String.valueOf(qyxxMap.get("QYBM")));
                    saveMap.put("GHSCBM", String.valueOf(qyxxMap.get("QYBM")));
                    saveMap.put("GYSMC", String.valueOf(qyxxMap.get("QYMC")));
                    saveMap.put("GHSCMC", String.valueOf(qyxxMap.get("QYMC")));
                    String jcId = save("T_CY_SCJCXX", saveMap, null);
                    ids.add(jcId);
                }
                for (Map<String, Object> cp : cpList) {
                    Map<String, String> saveMap = new HashMap<String, String>();
                    saveMap.put("CYXFDWBM", SerialNumberUtil.getInstance().getCompanyCode());
                    saveMap.put("CYXFDWMC", CompanyInfoUtil.getInstance().getCompanyName("CY", SerialNumberUtil.getInstance().getCompanyCode()));
                    saveMap.put("JCRQ", dateToString(new Date()));
                    saveMap.put("SPBM", String.valueOf(cp.get("CPBH")));
                    saveMap.put("SPMC", String.valueOf(cp.get("CPMC")));
                    saveMap.put("JS", String.valueOf(cp.get("CCJS")));
                    saveMap.put("JHPCH", String.valueOf(cp.get("ZSPCH")));
                    saveMap.put("JYPZH", String.valueOf(cp.get("ZSM")));
                    saveMap.put("GHSCBM", String.valueOf(qyxxMap.get("QYBM")));
                    saveMap.put("GYSBM", String.valueOf(qyxxMap.get("QYBM")));
                    saveMap.put("GHSCMC", String.valueOf(qyxxMap.get("QYMC")));
                    saveMap.put("GYSMC", String.valueOf(qyxxMap.get("QYMC")));
                    String jcId = save("T_CY_SCJCXX", saveMap, null);
                    ids.add(jcId);
                }
                //同步上家数据进场状态
                String syncSql = "update T_ZZ_CCGL t set IS_DELIVERED = '1' where SFDH =?";
                DatabaseHandlerDao.getInstance().executeSql(syncSql, new Object[]{barCode});
            }
        } else {
            sql = "select * from t_pc_jyxx t where upper(t.jytmh) = '" + barCode.toUpperCase() + "' and t.is_in ='0' ";
            Map<String, Object> jyxx = DatabaseHandlerDao.getInstance().queryForMap(sql);
            sql = "select * from t_pc_jymxxx t where t.is_in = '0' and t.T_PC_JYXX_ID = '" + jyxx.get("ID") + "'";
            List<Map<String, Object>> jymxxxs = DatabaseHandlerDao.getInstance().queryForMaps(sql);

            //返回插入表的ID
            Map<String, String> dataMap = new HashMap<String, String>();
            for (Map<String, Object> jymxxx : jymxxxs) {
                dataMap.clear();
                dataMap.put("CYXFDWBM", SerialNumberUtil.getInstance().getCompanyCode());
                dataMap.put("CYXFDWMC", CompanyInfoUtil.getInstance().getCompanyName("CY", SerialNumberUtil.getInstance().getCompanyCode()));
                dataMap.put("JCRQ", dateToString(new Date()));
                dataMap.put("GYSBM", String.valueOf(jyxx.get("PFSBM")));
                dataMap.put("GYSMC", String.valueOf(jyxx.get("PFSMC")));
                dataMap.put("SPBM", String.valueOf(jymxxx.get("SPBM")));
                dataMap.put("SPMC", String.valueOf(jymxxx.get("SPMC")));
                dataMap.put("ZL", String.valueOf(jymxxx.get("ZL")));
                dataMap.put("JS", String.valueOf(jymxxx.get("JS")));
                dataMap.put("DJ", String.valueOf(jymxxx.get("DJ")));
                dataMap.put("JHPCH", String.valueOf(jymxxx.get("JHPCH")));
                dataMap.put("JYPZH", String.valueOf(jymxxx.get("ZSM")));
                dataMap.put("GHSCBM", String.valueOf(jyxx.get("PFSCBM")));
                dataMap.put("GHSCMC", String.valueOf(jyxx.get("PFSCMC")));
                String jcId = save("T_CY_SCJCXX", dataMap, null);
                //更新批菜交易（已进场）
                sql = "update t_pc_jymxxx t set t.is_in = '1' where t.T_PC_JYXX_ID = '" + jyxx.get("ID") + "'";
                DatabaseHandlerDao.getInstance().executeSql(sql);
                sql = "update t_pc_jyxx t set t.is_in = '1' where t.jytmh = '" + barCode + "'";
                DatabaseHandlerDao.getInstance().executeSql(sql);

                ids.add(jcId);
            }
        }
        return ids;
    }

}
