package com.ces.component.zzrzxx.service;

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

import javax.management.StringValueExp;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.*;

@Component
public class ZzrzxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    private String sysName;

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
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


    public String saveAdd(Map<String,String> map,File imageUpload,String imageUploadFileName)
           // (String CPMC,String RZMC,String RZJG,String RZRQ,String ZSBH,String YXQ)
    {
       // String qybm = SerialNumberUtil.getInstance().getCompanyCode();
       // String tableName = TableNameUtil.getQydaTableName(sysName);
        String realpath = ServletActionContext.getServletContext().getRealPath("/spzstpfj");
       // List<String> fileNamesWithStamp = new ArrayList<String>();
        String fileNameWithStamp = System.currentTimeMillis() + "_" + imageUploadFileName;
//		String newImageName = "";
//		String oldImageFile = (String) DatabaseHandlerDao
//				.getInstance()
//				.queryForObject("select QYTP2 from "+tableName+" where QYBM='"+qybm+"'");
//		newImageName = oldImageFile;
        /****保存图片start*****/
        if(!"".equals(String.valueOf(map.get("ID")))){
            String sql = "select * from T_ZZ_RZXX where id = '" + String.valueOf(map.get("ID")) + "'" +
                    " and is_delete <> '1'";
            Map<String,Object> dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql);
            map.put("TP",String.valueOf(dataMap.get("TP")));
        }
        map.put("TP", String.valueOf(map.get("TP")));
        if(imageUpload!=null){
            //for(int i = 0;i<imageUpload.size();i++) {
                Map<String,String> dataMap = new HashMap<String, String>();

               // fileNamesWithStamp.add(fileNameWithStamp);
                File destFile = new File(realpath + "/" + fileNameWithStamp);
                try {
                    FileUtils.copyFile(imageUpload, destFile);
//				newImageName = fileNameWithStamp;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Date now = new Date();
               //DateFormat df = DateFormat.getDateTimeInstance();
                //String date = df.format(now);
//                dataMap.put("TPMC",imageUploadFileName);
//                dataMap.put("TPLJ",fileNameWithStamp);
//                dataMap.put("QYBM",qybm);
//                dataMap.put("UPLOADTIME",date);
//                saveOne("T_COMMON_QYTP",dataMap);
                map.put("TP",fileNameWithStamp);

//			String sql = "update " + tableName + " T set T.QYTP='"
//					+ imageUploadFileName + "',T.QYTP2='"
//					+ fileNameWithStamp + "' where T.QYBM='" + qybm + "'";
//			DatabaseHandlerDao.getInstance().executeSql(sql);
                //删除旧图片
//			FileUtil.deleteFile(realpath + "/" + oldImageFile);
            }
            map.put("QYBM",SerialNumberUtil.getInstance().getCompanyCode());

       // }
        /****保存图片end*****/
        /****保存数据start******/
//        String sqlStr ="";
//        Set<Map.Entry<String, Object>> allSet = map.entrySet();
//        Iterator<Map.Entry<String,Object>> iter = allSet.iterator();
//        while(iter.hasNext()){
//            Map.Entry<String,Object> entry = iter.next();
//            sqlStr += entry.getKey() + "= '" +entry.getValue() + "',";
//        }
//        sqlStr = sqlStr.substring(0, sqlStr.length()-1);
//        String sql2 = "update " +tableName+ " set " +sqlStr+ " where QYBM='"+qybm+"'";
//        DatabaseHandlerDao.getInstance().executeSql(sql2);
        /****保存数据end******/
        return saveOne("T_ZZ_RZXX", map);
//        String sql="insert into T_ZZ_RZXX(CPMC,RZMC,RZJG,RZRQ,ZSBH,YXQ) VALUES('"+CPMC+"','"+RZMC+"','"+RZJG+"','"+RZRQ+"','"+ZSBH+"','"+YXQ+"')";
//        return(DatabaseHandlerDao.getInstance().executeSql(sql));
//        //return 1;
    }

    @Override
    public String save(String tableName, Map<String, String> dataMap, Map<String, Object> paramMap) {
        return super.save(tableName, dataMap, paramMap);
    }

    @Transactional
    public boolean deleteById(String ids){
        try {
            String sql = "";
            int rs = 0;
            String id[] = ids.split("___");
            for(int i = 0;i<id.length;i++){
                sql = "update t_zz_rzxx t set t.is_delete = '1' where t.id = '" + id[i] + "'";
                rs = DatabaseHandlerDao.getInstance().executeSql(sql);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Map<String,Object> getRzxx(){
        Map<String,Object> map = new HashMap<String, Object>();
        String sql = "select * from t_zz_rzxx where is_delete <> '1' and qybm = '" + SerialNumberUtil.getInstance().getCompanyCode() + "'";
        List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        map.put("data",list);
        return map;
    }

    public Object getRzxxById(String id){
        String sql = "select * from t_zz_rzxx where id = '" + id + "' and is_delete <> '1'";
        Map<String,Object> dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql);
        return dataMap;
    }


    /**
     * @param tplj
     * @return
     */
    public Object deleteImage(String tplj) {
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "update t_zz_rzxx t set t.tp = '' where t.tp = '"+tplj+"' and t.qybm = '"+qybm+"'";
        int result = DatabaseHandlerDao.getInstance().executeSql(sql);
        return result;
    }
}