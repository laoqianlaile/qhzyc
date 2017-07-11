package com.ces.component.zyyypgysxx.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.StringUtil;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;

import com.ces.component.zyyypgysxx.dao.ZyyypgysxxDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Component
public class ZyyypgysxxService extends TraceShowModuleDefineDaoService<StringIDEntity, ZyyypgysxxDao> {

    private final String REAL_PATH = ServletActionContext.getServletContext().getRealPath("/spzstpfj");
    /**
     *
     * @return
     */
    public  String defaultCode(){
        String code= SerialNumberUtil.getInstance().getCompanyCode();
        String  defaultCode=" ";
        if(code!=null && !"".equals(code) )
            defaultCode= AppDefineUtil.RELATION_AND+" QYBM= '"+code+"' "+ AppDefineUtil.RELATION_AND+" is_delete <> '1'";
        return defaultCode;
    }


    public Map<String,Object> getGridTypeData(List<Map<String,Object>> list){
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data",list);
        return  dataMap;
    }
    public Map<String,Object> searchypgysxxBygysbh(String gysbh){
        String sql =  "select * from t_zyy_ypgysxx where gysbh=?"+defaultCode();
        return DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{gysbh});
    }

    @Transactional
    public Object save(Map map, List<File> imageUpload, List<String> imageUploadFileName, String REAL_PATH) {
        Map newMap = new HashMap();
        Set<Map.Entry> allSet = map.entrySet();
        Iterator<Map.Entry> iter = allSet.iterator();
        while(iter.hasNext()){
            Map.Entry entry = iter.next();
            String[] valueArray = (String[])entry.getValue();
            String value = "" ;
            for( int i = 0 ; i < valueArray.length ; i ++){
                value = valueArray[i];
            }
            newMap.put(entry.getKey() , value );
        }
        if( null != imageUpload){
                    //
                    File oldFile = new File(REAL_PATH+"/"+newMap.get("T_ZYY_YPGYSXX"));

                    String fileNameWithStamp = System.currentTimeMillis() + "_" + imageUploadFileName.get(0);
                    File newFile = new File(REAL_PATH+"/"+fileNameWithStamp);
                    try {
                        if(oldFile.exists()){
                            oldFile.delete();
                        }
                        FileUtils.copyFile(imageUpload.get(0), newFile);
                        newMap.put("YYZZWJ",fileNameWithStamp);
                    } catch (IOException e) {
                        //
                        newFile.delete();
                    }
        }
        newMap.put("QYBM",SerialNumberUtil.getInstance().getCompanyCode());
        save("T_ZYY_YPGYSXX", newMap, null);
        return newMap;
    }
    /**
     *
     * @param path
     * @return  *
     * @throws Exception
     */

    public static String encodeBase64File(String path) throws Exception {
        InputStream in;
        byte[] data = null;
        //
        try
        {
            in = new FileInputStream(path);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        //
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);//

    }
    /**
     *
     * @param id
     * @return
     */
    public Map<String,Object> searchById(String id) {
        String sql =  "select * from t_zyy_ypgysxx where id=?"+defaultCode();
        return DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{id});
    }
    /**
     *
     * @param id
     * @return
     */

    public int updateYyzzwj(String id) {
        String sql =  "update t_zyy_ypgysxx set yyzzwj='' where id=?"+defaultCode();
        return DatabaseHandlerDao.getInstance().executeSql(sql,new String[]{id});
    }



    
}
