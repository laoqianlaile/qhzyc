package com.ces.component.sdzycyljyjcxx.service;

import cesgroup.rear.lib.client.enterprise.RearClientBuilder;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.StatisticalCodeUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.fasterxml.jackson.databind.JsonNode;
import enterprise.endpoints.EnterpriseService;
import enterprise.entity.TestEntity;
import org.apache.commons.io.FileUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import com.ces.component.sdzycyljyjcxx.dao.SdzycyljyjcxxDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Encoder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class SdzycyljyjcxxService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzycyljyjcxxDao> {

    public Map<String,Object> getZzpch(){
        String sql = "select T.ZZPCH,T.ZMZZMC,T.YCMC,T.YCDM,T.JDMC,T.DKMC,T.QYPCH,t.dkmj  from T_ZZ_SCDA  T where T.ZT != '0' and (t.JYJG='0' or t.JYJG='2') and qybm=?  order by zzsj desc";
        List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql,new String[]{SerialNumberUtil.getInstance().getCompanyCode()});
        return getResultData(list);
    }
    public Map<String,Object> getCszzpch(){
        String sql = "select T.ZZPCH,T.ZMZZMC,T.YCMC,T.YCDM,T.JDMC,T.DKMC,T.QYPCH,t.dkmj  from T_ZZ_SCDA  T where T.ZT != '0' and (t.JYJG='1' or t.JYJG='2') and qybm=? order by zzsj desc";
        List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql,new String[]{SerialNumberUtil.getInstance().getCompanyCode()});
        return getResultData(list);
    }
    public Map<String,Object> getResultData(List<Map<String,Object>> data){
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("data", data);
        return result;
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
//        String id  = getService().save("T_SDZYC_JDXX" ,newMap ,null);
        String id = String.valueOf(newMap.get("ID"));
        if(StringUtil.isEmpty(id)){//是新增操作
            String jybh = SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCYLJYJCBH", false);
            newMap.put("JYBH",jybh);
            id = save("T_SDZYC_YLJYJCXX" ,newMap ,null);
            newMap.put(AppDefineUtil.C_ID, id);
            if(!StringUtil.isEmpty(id)){//数据保存成功执行文件上传操作
                if( null != imageUpload){
                    //进行文件上传，则删除历史保存文件
                    File oldFile = new File(REAL_PATH+"/"+newMap.get("T_SDZYC_YLJYJCXX"));

                    String fileNameWithStamp = System.currentTimeMillis() + "_" + imageUploadFileName.get(0);
                    File newFile = new File(REAL_PATH+"/"+fileNameWithStamp);
                    try {
                        if(oldFile.exists()){
                            oldFile.delete();
                        }
                        FileUtils.copyFile(imageUpload.get(0),newFile);
                        //文件上传成功进行数据保存
                        newMap.put("ID",id);
                        newMap.put("JYWJ",fileNameWithStamp);
                        save("T_SDZYC_YLJYJCXX" ,newMap ,null);
                        try {
                            newMap.put("base64", encodeBase64File(REAL_PATH+"/"+fileNameWithStamp));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        //上传文件失败删除保存数据和图片
                        delete(id);
                        if(newFile.exists()){
                            oldFile.delete();
                        }
                    }

                }
            }
            String sql = "update t_zz_scda set jyjg=? where zzpch=?";
            DatabaseHandlerDao.getInstance().executeSql(sql,new String []{String.valueOf(newMap.get("JYJG")),String.valueOf(newMap.get("ZZPCH"))});
            sendCreateTestEnttity(newMap);
            return newMap;
        }
        save("T_SDZYC_YLJYJCXX" ,newMap ,null);
        newMap.put(AppDefineUtil.C_ID, id);
        if(!StringUtil.isEmpty(id)){//数据保存成功执行文件上传操作
            if( null != imageUpload){
                //进行文件上传，则删除历史保存文件
                File oldFile = new File(REAL_PATH+"/"+newMap.get("JYWJ"));

                String fileNameWithStamp = System.currentTimeMillis() + "_" + imageUploadFileName.get(0);
                File newFile = new File(REAL_PATH+"/"+fileNameWithStamp);
                try {
                    if(oldFile.exists()){
                        oldFile.delete();
                    }
                    FileUtils.copyFile(imageUpload.get(0),newFile);
                    //文件上传成功进行数据保存
                    newMap.put("ID",id);
                    newMap.put("JYWJ",fileNameWithStamp);
                    save("T_SDZYC_YLJYJCXX" ,newMap ,null);
                } catch (IOException e) {
                    //上传文件失败删除保存数据和图片
                    delete(id);
                    if(newFile.exists()){
                        oldFile.delete();
                    }
                }

            }
        }
        //修改种植任务检验结果字段信息
        String sql = "update t_zz_scda set jyjg=? where zzpch=?";
        DatabaseHandlerDao.getInstance().executeSql(sql,new String []{String.valueOf(newMap.get("JYJG")),String.valueOf(newMap.get("ZZPCH"))});

        return newMap;
    }

    /**
     * 将文件转成base64 字符串
     * @param path
     * @return  *
     * @throws Exception
     */

    public static String encodeBase64File(String path) throws Exception {
        InputStream in;
        byte[] data = null;
        //读取图片字节数组
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
        //对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);//返回Base64编码过的字节数组字符串

    }
    public void sendCreateTestEnttity(Map<String, String> map){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        TestEntity testEntity = new TestEntity();
        testEntity.setId(map.get("ID"));
        testEntity.setBatch_no(map.get("ZZPCH"));
        testEntity.setComp_code(map.get("QYBM"));
        testEntity.setDate(dateToLong(map.get("JYSJ")));
        testEntity.setPerson(map.get("JYRY"));
        testEntity.setMethod(map.get("JYFF"));
        service.createTest(testEntity);
    }
    private Long dateToLong(String str){
        SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        long t = 0;
        try{
            date =  dd.parse(str);
            t = date.getTime();
        }catch(ParseException e){
            e.printStackTrace();
        }
        return t;
    }
    /**
     * 根据Id查询原料检验检测信息
     * @param id
     * @return
     */
    public Map<String,Object> searchById(String id) {
        String sql =  "select * from t_sdzyc_yljyjcxx where id=?"+defaultCode();
        return DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{id});
    }
    /**
     * 修改图片信息
     * @param id
     * @return
     */

    public int updateJywj(String id) {
        String sql =  "update t_sdzyc_yljyjcxx set jywj='' where id=?"+defaultCode();
        return DatabaseHandlerDao.getInstance().executeSql(sql,new String[]{id});
    }

    public  String defaultCode(){
        String code= SerialNumberUtil.getInstance().getCompanyCode();
        String  defaultCode=" ";
        if(code!=null && !"".equals(code) )
            defaultCode= AppDefineUtil.RELATION_AND+" QYBM= '"+code+"' "+ AppDefineUtil.RELATION_AND+" is_delete <> '1'";
        return defaultCode;
    }
    
}
