package com.ces.component.qyxxjdxx.service;

import ces.sdk.util.StringUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;

import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.*;

@Component
public class QyxxjdxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {
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
                    + "' " + AppDefineUtil.RELATION_AND + " is_delete <> '1'";
        return defaultCode;
    }

    @Override
    protected String buildCustomerFilter(String tableId,
                                         String componentVersionId, String moduleId, String menuId,
                                         Map<String, Object> paramMap) {
        // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
        return defaultCode();
    }

    public  Map<String,Object> getJDXX(){
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        Map<String,Object> dataMap = new HashMap<String, Object>();
        String sql = "select * from t_zz_jdxx t where qybm = ? and create_time = (select max(create_time) from t_zz_jdxx t where t.qybm =? and t.is_delete <> '1') ";
        dataMap  = DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{qybm,qybm});
        if(dataMap.size() == 0){
            sql = "select t.qymc from t_qypt_zhgl t where t.zhbh = ?";
            dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{qybm});
            Map<String, String> formData = new HashMap<String, String>();
            formData.put("QYBM", qybm);
            String lsh = DatabaseHandlerDao.getInstance().queryForMap("select nvl(to_number(max(a.jdbh)),0) as lsh from t_zz_jdxx a where a.qybm in (select b.zhbh from t_qypt_zhgl b where b.auth_parent_id = (select c.auth_parent_id from t_qypt_zhgl c where c.zhbh = '" + qybm + "')) and a.jdbh like '_________'").get("LSH").toString();
            formData.put("JDBH", String.format("%09d", (Integer.parseInt(lsh) + 1)));
            formData.put("JDMC", String.valueOf(dataMap.get("QYMC")));
            saveOne("T_ZZ_JDXX", formData);
            sql = "select * from t_zz_jdxx t where qybm = ? and create_time = (select max(create_time) from t_zz_jdxx t where t.qybm =? and t.is_delete <> '1') ";
            dataMap  = DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{qybm,qybm});
        }

        return dataMap;
    }

    @Transactional
    public List<String> saveJDXX(Map<String, Object> map, List<File> imageUpload, List<String> imageUploadFileName) {
        //获取旧数据
        String sql = "";

        Map<String, Object> oldMap = new HashMap<String, Object>();
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        String realpath = ServletActionContext.getServletContext().getRealPath("/spzstpfj");
        List<String> fileNamesWithStamp = new ArrayList<String>();
        Map<String, String> dataMap = new HashMap<String, String>();
        if (!"".equals(String.valueOf(map.get("ID"))) && !"null".equals(String.valueOf(map.get("ID")))) {
            sql = "select * from t_zz_jdxx t where id = '" + map.get("ID").toString() + "'";
            oldMap = DatabaseHandlerDao.getInstance().queryForMap(sql);
            dataMap.put("ZP", String.valueOf(oldMap.get("ZP")));
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
                dataMap.put("ZP", imageUploadFileName.get(i));
                dataMap.put("TPLJ", fileNameWithStamp);
            }
        }
        dataMap.put("QYBM", qybm);
        dataMap.put("ID", String.valueOf(map.get("ID")));
        dataMap.put("JDMC", String.valueOf(map.get("JDMC")));
        dataMap.put("JDBH", String.valueOf(map.get("JDBH")));
        dataMap.put("JDLX", String.valueOf(map.get("JDLX")));
        dataMap.put("JDMJ", String.valueOf(map.get("JDMJ")));
        dataMap.put("SSDQBM", String.valueOf(map.get("SSDQBM")));
        dataMap.put("SSDQ",String.valueOf(map.get("SSDQ")));
        dataMap.put("FZRBH", String.valueOf(map.get("FZRBH")));
        dataMap.put("FZR",String.valueOf(map.get("FZR")));
        dataMap.put("LXFS", String.valueOf(map.get("LXFS")));
        dataMap.put("LXDZ", String.valueOf(map.get("LXDZ")));
        dataMap.put("BZ", String.valueOf(map.get("BZ")));

        saveOne("t_zz_jdxx", dataMap);
        /****保存图片end*****/
        if (oldMap.get("TPLJ") != null && !oldMap.get("TPLJ").equals(dataMap.get("TPLJ"))) {
            //删除旧图片
//            FileUtil.deleteFile(realpath + "/" + oldMap.get("TPLJ"));
        }
        fileNamesWithStamp.clear();
        fileNamesWithStamp.add(dataMap.get("ID"));
        return fileNamesWithStamp;
    }


    /**
     * 删除图片
     * @param tplj
     * @return
     */
    public Object deleteImage(String tplj){
        String sql = "update t_zz_jdxx t set t.zp = '',t.tplj = '' where t.tplj = ?";
        int i = DatabaseHandlerDao.getInstance().executeSql(sql,new Object[]{tplj});
        return i;
    }

    /**
     * 获取负责人(工作人员)信息
     * @return
     */
    public Map<String,Object> getFzrxx(){
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        Map<String,Object> dataMap = new HashMap<String,Object>();
        String sql = "select t.gzrybh,t.xm from t_zz_gzryda t where qybm = ? and t.is_delete <> '1' ";
        List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql,new String[]{qybm});
        dataMap.put("data",list);
        return dataMap;

    }

    
}