package com.ces.component.zzxyqk.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.TableNameUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.FileUtil;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class ZzxyqkService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    @Transactional
    public List<String> saveXyqk(Map<String, Object> map, List<File> imageUpload, List<String> imageUploadFileName) {
        //获取旧数据
        String sql = "";
        Map<String, Object> oldMap = new HashMap<String, Object>();
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        String realpath = ServletActionContext.getServletContext().getRealPath("/spzstpfj");
        List<String> fileNamesWithStamp = new ArrayList<String>();
        Map<String, String> dataMap = new HashMap<String, String>();
        if (map.get("id").toString() != null && !"".equals(map.get("id").toString())) {
            sql = "select * from t_zz_xyqk t where id = '" + map.get("id").toString() + "'";
            oldMap = DatabaseHandlerDao.getInstance().queryForMap(sql);
            dataMap.put("JFTP", String.valueOf(oldMap.get("JFTP")));
            dataMap.put("TPLJ", String.valueOf(oldMap.get("TPLJ")));
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
                dataMap.put("JFTP", imageUploadFileName.get(i));
                dataMap.put("TPLJ", fileNameWithStamp);
            }
        }
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date date=new Date();
        String create_time=sdf.format(date);

        dataMap.put("CREATE_TIME",create_time );
        dataMap.put("QYBM", qybm);
        dataMap.put("ID", String.valueOf(map.get("id")));
        dataMap.put("JFNR", String.valueOf(map.get("jfnr")));
        dataMap.put("JFDW", String.valueOf(map.get("jfdw")));
        dataMap.put("JFSJ", String.valueOf(map.get("jfsj")));
        saveOne("t_zz_xyqk", dataMap);
        /****保存图片end*****/

        if (oldMap.get("TPLJ") != null && !oldMap.get("TPLJ").equals(dataMap.get("TPLJ"))) {
            //删除旧图片
//            FileUtil.deleteFile(realpath + "/" + oldMap.get("TPLJ"));
        }

        /****保存数据start******/
//        String sqlStr ="";
//        Set<Map.Entry<String, Object>> allSet = map.entrySet();
//        Iterator<Map.Entry<String,Object>> iter = allSet.iterator();
//        while(iter.hasNext()){
//            Map.Entry<String,Object> entry = iter.next();
//            if(!entry.getKey().equals("id")){
//                sqlStr += entry.getKey() + "= '" +entry.getValue() + "',";
//            }
//        }
//        sqlStr = sqlStr.substring(0, sqlStr.length()-1);
//        String s = "select * from t_zz_xyqk ";
//        List<Map<String,Object>> l = DatabaseHandlerDao.getInstance().queryForMaps(s);
//        String sql2 = "update t_zz_xyqk set " +sqlStr+ " where id='"+qybm+"'";
        //DatabaseHandlerDao.getInstance().executeSql(sql2);
        /****保存数据end******/
        fileNamesWithStamp.clear();
        fileNamesWithStamp.add(dataMap.get("ID"));
        return fileNamesWithStamp;
    }


    /**
     * 修改时加载数据
     */
    public Object getXyqkData(String id) {
        String sql = "";
        sql = "select * from t_zz_xyqk t where id = '" + id + "'";
        Map<String, Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql);

        return map;
    }

    @Override
    @Transactional
    public void delete(String tableId, String dTableIds, String ids, boolean isLogicalDelete, Map<String, Object> paramMap) {
        super.delete(tableId, dTableIds, ids, isLogicalDelete, paramMap);
//        String realpath = ServletActionContext.getServletContext().getRealPath("/spzstpfj");
//        String sql = "";
//        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//        String id[] = ids.split(",");
//        sql = "select * from t_zz_xyqk t where 1 = 1";
//        for (int i = 0; i < id.length; i++) {
//            sql += "or id = '" + id[i] + "'";
////            list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
//        }
//        for (Map<String, Object> map : list) {
//            //删除旧图片
////            FileUtil.deleteFile(realpath + "/" + String.valueOf(map.get("TPLJ")));
//        }

    }


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

    public Object deleteImage(String tplj){
        String sql = "update t_zz_xyqk t set t.jftp = '',t.tplj = '' where t.tplj = '" + tplj + "'";
        int i = DatabaseHandlerDao.getInstance().executeSql(sql);
        return i;
    }

}