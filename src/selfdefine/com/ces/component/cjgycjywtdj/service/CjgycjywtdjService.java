package com.ces.component.cjgycjywtdj.service;

import cesgroup.rear.lib.client.enterprise.RearClientBuilder;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.UUIDGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import enterprise.endpoints.EnterpriseService;
import enterprise.entity.TestEntity;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import com.ces.component.cjgycjywtdj.dao.CjgycjywtdjDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class CjgycjywtdjService extends TraceShowModuleDefineDaoService<StringIDEntity, CjgycjywtdjDao> {
    @Transactional
    public Object save(Map map, List<File> imageUpload, List<String> imageUploadFileName, String REAL_PATH) {
        boolean isNew = false;
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
        String id = String.valueOf(newMap.get("ID"));
        if(StringUtil.isEmpty(id)){//是新增操作
            String jybh = SerialNumberUtil.getInstance().getSerialNumber("SDZYC","SDZYCYCJYBH",false);//SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCCSPCH", false);
            String jylx="委托检验";
            newMap.put("JYXX",jylx);
            newMap.put("JYBH", jybh);
            isNew =true;
        }
        if( null != imageUpload){
            //进行文件上传，则删除历史保存文件
            File oldFile = new File(REAL_PATH+"/"+newMap.get("T_SDZYC_CJG_YCJXX"));
            String[] args = imageUploadFileName.get(0).split("\\.");
            String fileNameWithStamp = System.currentTimeMillis()+ UUIDGenerator.uuid()+"."+args[1];
            File newFile = new File(REAL_PATH+"/"+fileNameWithStamp);
            try {
                if(oldFile.exists()){
                    oldFile.delete();
                }
                FileUtils.copyFile(imageUpload.get(0),newFile);
                //文件上传成功进行数据保存
                newMap.put("JYWJ",fileNameWithStamp);
                id = save("T_SDZYC_CJG_YCJXX" ,newMap ,null);
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
        }else{
            id = save("T_SDZYC_CJG_YCJXX" ,newMap ,null);
        }

        String updSql ="update t_sdzyc_cjg_ycjgxx set jyjg=? where  jgpch=?";
        DatabaseHandlerDao.getInstance().executeSql(updSql,new String[]{String.valueOf(newMap.get("JYJG")),String.valueOf(newMap.get("PCH"))});
        if(isNew) {
            sendCreateTestEnttity(newMap);
        }
        return newMap;
    }
    /**
     * ���ļ�ת��base64 �ַ���
     * @param path
     * @return  *
     * @throws Exception
     */

    public static String encodeBase64File(String path) throws Exception {
        InputStream in;
        byte[] data = null;
        //��ȡͼƬ�ֽ�����
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
        //���ֽ�����Base64����
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);//����Base64��������ֽ������ַ���

    }

    public void sendCreateTestEnttity(Map<String, String> map){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        TestEntity testEntity = new TestEntity();
        testEntity.setId(map.get("ID"));
        testEntity.setBatch_no(map.get("PCH"));
        testEntity.setComp_code(map.get("QYBM"));
        testEntity.setDate(dateToLong(map.get("JYSJ")));
        testEntity.setPerson(map.get("JYRY"));
        if (!StringUtil.isEmpty(map.get("JYFF"))) {
            testEntity.setMethod(map.get("JYFF"));
        }
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
    public Map<String,Object> searchById(String id) {
        String sql =  "select * from t_sdzyc_cjg_ycjxx where id=?"+defaultCode();
        return DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{id});
    }

    public int updateCdzm(String id) {
        String sql =  "update t_sdzyc_cjg_ycjxx set jywj='' where id=?"+defaultCode();
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
