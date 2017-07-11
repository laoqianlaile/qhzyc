package com.ces.component.zzcpxxgl.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.database.Database;
import com.ces.config.utils.AppDefineUtil;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.*;

@Component
public class ZzcpxxglService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {
    /**
     * 默认权限过滤
     *
     * @return
     */
    public String defaultCode() {
        String code = SerialNumberUtil.getInstance().getCompanyCode();
        String defaultCode = " ";
        if (code != null && !"".equals(code))
            defaultCode = AppDefineUtil.RELATION_AND + " QYBM = '" + code
                    + "' and is_delete <> '1'";
        return defaultCode;
    }

    @Override
    protected String buildCustomerFilter(String tableId,
                                         String componentVersionId, String moduleId, String menuId,
                                         Map<String, Object> paramMap) {
        // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
        return defaultCode();
    }


    /**
     * 根据产品编号获取产品信息
     */
    public Object getCpxxByCpbh(String cpbh) {
        String sql = "";
        Map<String, Object> shareMap = new HashMap<String, Object>();
        sql = "select * from t_zz_cpxxgl t where t.qybm = '" + SerialNumberUtil.getInstance().getCompanyCode() + "' and cpbh = '" + cpbh + "'";
        Map<String, Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql);
        //编码转换
        sql = "select b.sjmc" +
                "  from t_common_sjlx a, t_common_sjlx_code b" +
                " where a.lxbm = b.lxbm" +
                "   and a.lxbm = 'DJ'" +
                "   and b.sjbm = '" + String.valueOf(map.get("DJ")) + "'";
        shareMap = DatabaseHandlerDao.getInstance().queryForMap(sql);
        map.put("DJ", String.valueOf(shareMap.get("SJMC")));
        sql = "select b.sjmc" +
                "  from t_common_sjlx a, t_common_sjlx_code b" +
                " where a.lxbm = b.lxbm" +
                "   and a.lxbm = 'BZXS'" +
                "   and b.sjbm = '" + String.valueOf(map.get("BZXS")) + "'";
        shareMap = DatabaseHandlerDao.getInstance().queryForMap(sql);
        map.put("BZXS", String.valueOf(shareMap.get("SJMC")));
        return map;
    }

    /**
     * 根据产品编号获取配料信息
     */
    public Object getPlxxByCpbh(String cpbh) {
        String sql = "";
        sql = "select b.pz,b.pzbh" +
                " from t_zz_cpxxgl a" +
                " left join t_zz_cpxxglplxx b" +
                " on a.id = b.pid" +
                " where a.qybm = '" + SerialNumberUtil.getInstance().getCompanyCode() + "'" +
                " and a.cpbh = '" + cpbh + "' and b.is_delete <> '1'";
        List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", list);
        map.put("pageNumber", 1);
        map.put("pageSize", list.size());
        map.put("total", 1);
        map.put("totalPages", 1);
        return map;
    }


    /**
     * 包装管理产品名称下拉列表获取数据
     */
    public Object getCpxx() {
        String sql = "";
        sql = "SELECT CPBH, CPMC FROM T_ZZ_CPXXGL T WHERE T.QYBM = '" + SerialNumberUtil.getInstance().getCompanyCode() + "' AND IS_DELETE <> '1' ORDER BY CPBH DESC";
        List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return list;
    }


    public Object getSjzdSj(String lxbm) {
        String lxbms[] = lxbm.split(",");
        Map<String, Object> dataMap = new HashMap<String, Object>();
        List<Map<String, Object>> list;
        for (int i = 0; i < lxbms.length; i++) {
            String sql = "";
            sql = "SELECT SJBM AS VALUE,SJMC AS TEXT FROM T_COMMON_SJLX_CODE T WHERE T.LXBM = '" + lxbms[i] + "' AND T.IS_DELETE <> '1' ORDER BY T.SXJB ASC";
            list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
            dataMap.put(lxbms[i], list);
        }
        return dataMap;
    }

    @Transactional
    public List<String> saveCpxx(Map<String, Object> map, List<File> imageUpload, List<String> imageUploadFileName, List<Map<String,String>> gridData){
        String sql;
        Map<String, Object> oldMap = new HashMap<String, Object>();
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        String realpath = ServletActionContext.getServletContext().getRealPath("/spzstpfj");
        List<String> fileNamesWithStamp = new ArrayList<String>();
        Map<String, String> dataMap = new HashMap<String, String>();
        if (!"".equals(String.valueOf(map.get("ID"))) && !"null".equals(String.valueOf(map.get("ID")))) {
            sql = "SELECT * FROM T_ZZ_CPXXGL T WHERE ID = '" + map.get("ID").toString() + "'";
            oldMap = DatabaseHandlerDao.getInstance().queryForMap(sql);
            dataMap.put("TPMC", String.valueOf(oldMap.get("TPMC")));
            dataMap.put("TPBCMC", String.valueOf(oldMap.get("TPBCMC")));
        }else{
            map.put("CPBH",SerialNumberUtil.getInstance().getSerialNumber("ZZ", "ZZCPBH", false));
        }
        if (imageUpload != null && imageUpload.size() != 0 && !imageUpload.isEmpty()) {
            for (int i = 0; i < imageUpload.size(); i++) {
                if (i != imageUpload.size() - 1) {
                    continue;
                }
                String fileNameWithStamp = System.currentTimeMillis() + "_" + imageUploadFileName.get(i);
                fileNamesWithStamp.add(fileNameWithStamp);
                File destFile = new File(realpath + "/" + fileNameWithStamp);
                try {
                    FileUtils.copyFile(imageUpload.get(i), destFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Date now = new Date();
                DateFormat df = DateFormat.getDateTimeInstance();
                String date = df.format(now);
                dataMap.put("TPMC", imageUploadFileName.get(i));
                dataMap.put("TPBCMC", fileNameWithStamp);
            }
        } else {
            dataMap.put("TPMC", String.valueOf(dataMap.get("TPMC")));
            dataMap.put("TPBCMC", String.valueOf(dataMap.get("TPBCMC")));
        }
        dataMap.put("QYBM", qybm);
        dataMap.put("ID", String.valueOf(map.get("ID")));
        dataMap.put("CPBH", String.valueOf(map.get("CPBH")));
        dataMap.put("CPMC", String.valueOf(map.get("CPMC")));
        dataMap.put("BZXS", String.valueOf(map.get("BZXS")));
        dataMap.put("GG", String.valueOf(map.get("GG")));
        dataMap.put("DJ", String.valueOf(map.get("DJ")));
        dataMap.put("CCFS", String.valueOf(map.get("CCFS")));
        dataMap.put("SM", String.valueOf(map.get("SM")));
        dataMap.put("BXQ", String.valueOf(map.get("BXQ")));
        saveOne("T_ZZ_CPXXGL", dataMap);
        //保存子表数据
        StringBuffer ids = new StringBuffer();
        ids.append("('!'");
        for(Map<String,String> xmap : gridData){
            Map<String,String> newxmap = new HashMap<String, String>();
            ids.append(",'" + xmap.get("ID") + "'");
            newxmap.put("ID", xmap.get("ID"));
            newxmap.put("DPZ",xmap.get("DPZ"));
            newxmap.put("DPZBH",xmap.get("DPZBH"));
            newxmap.put("PZBH",xmap.get("XPZBH"));
            newxmap.put("PZ",xmap.get("XPZ"));
            newxmap.put("ZL",xmap.get("ZL"));
            newxmap.put("PID", dataMap.get("ID"));
            saveOne("T_ZZ_CPXXGLPLXX", newxmap);
        }
        ids.append(")");
        sql = "UPDATE T_ZZ_CPXXGLPLXX T SET T.IS_DELETE = '1' WHERE T.ID NOT IN " + ids.toString() + " AND T.PID = '" + dataMap.get("ID") + "'";
        DatabaseHandlerDao.getInstance().executeSql(sql);
        /****保存图片end*****/
//        if (oldMap.get("TPLJ") != null && !oldMap.get("TPLJ").equals(dataMap.get("TPLJ"))) {
//            //删除旧图片
////            FileUtil.deleteFile(realpath + "/" + oldMap.get("TPLJ"));
//        }
        fileNamesWithStamp.clear();
        fileNamesWithStamp.add(dataMap.get("ID"));

        return fileNamesWithStamp;
    }


    public Object getPzxx(){
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select distinct t.plbh as value, t.pl as text from t_zz_dpzxx t where t.qybm = ? and t.is_delete <> '1'";
        List<Map<String,Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{companyCode});
        for (Map<String, Object> map : maps) {
            map.put("value", map.get("VALUE"));
            map.put("text", map.get("TEXT"));
        }
        return maps;
    }

    public Object getPlxxByPzxx(String plbh){
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select distinct t.pzbh as value, t.pz as text from t_zz_xpzxx t where t.qybm = ? and t.is_delete <> '1' ";
        List<String> params = new ArrayList<String>();
        params.add(companyCode);
        if (null!=plbh && !"".equals(plbh)) {
            sql += " and t.plbh = ?";
            params.add(plbh);
        }
        List<Map<String,Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql, params.toArray());
        for (Map<String, Object> map : maps) {
            map.put("value", map.get("VALUE"));
            map.put("text", map.get("TEXT"));
        }
        return maps;
    }

    /**
     * 修改时查询产品信息（包括产品配料信息）
     * @return
     */
    public Object getCpxxById(String id){
        Map<String,Object> dataMap = new HashMap<String, Object>();
        String sql = "SELECT * FROM T_ZZ_CPXXGL T WHERE T.ID = '" + id + "' AND T.IS_DELETE <> '1'";
        dataMap.put("cpxx", DatabaseHandlerDao.getInstance().queryForMap(sql));
        sql = "SELECT ID,PID,DPZ,DPZBH,PZ AS XPZ,PZBH AS XPZBH,ZL FROM T_ZZ_CPXXGLPLXX T WHERE T.PID = '" + id + "' AND T.IS_DELETE <> '1'";
        dataMap.put("cpxxplxx", DatabaseHandlerDao.getInstance().queryForMaps(sql));
        return dataMap;
    }


    @Override
    @Transactional
    public void delete(String tableId, String dTableIds, String ids, boolean isLogicalDelete, Map<String, Object> paramMap) {
        super.delete(tableId, dTableIds, ids, isLogicalDelete, paramMap);
        String id[] = ids.split(",");
        for(int i = 0; i < id.length; i++){
            String sql = "update T_ZZ_CPXXGLPLXX t set t.is_delete = '1' where t.pid = '" + id[i] + "'";
            DatabaseHandlerDao.getInstance().executeSql(sql);
        }
    }



    public Object deleteImage(String tplj){
        String sql = "update t_zz_cpxxgl t set t.tpmc = '',t.tpbcmc = '' where t.tpbcmc = ?";
        int i = DatabaseHandlerDao.getInstance().executeSql(sql, new Object[]{tplj});
        return i;
    }

    public Object getKhxx(){
        String qybm=SerialNumberUtil.getInstance().getCompanyCode();
        String sql="select KHMC,KHBH from T_ZZ_KHXX where qybm = '"+qybm+"'";
        List<Map<String,Object>> maps=DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return maps;
    }
    public Object getMdxx(String khbh){
        String qybm=SerialNumberUtil.getInstance().getCompanyCode();
        String sql="select MDMC,MDDZ FROM T_ZZ_KHMDXX WHERE qybm = '"+qybm+"' and pid = (select id from T_ZZ_KHXX where KHBH= '"+khbh+"')";
        List<Map<String,Object>> maps=DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return maps;
    }
}