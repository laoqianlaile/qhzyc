package com.ces.component.jjgwtdjxz.service;

import cesgroup.rear.lib.client.enterprise.RearClientBuilder;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.StringUtil;
import enterprise.endpoints.EnterpriseService;
import enterprise.entity.TestEntity;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import com.ces.component.jjgwtdjxz.dao.JjgwtdjxzDao;
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
public class JjgwtdjxzService extends TraceShowModuleDefineDaoService<StringIDEntity, JjgwtdjxzDao> {
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
//        String Updatewsql ="update t_sdzyc_jjg_ypscxx set jyjg='1'where CSPCH=?";
//        DatabaseHandlerDao.getInstance().executeSql(Updatewsql,new Object[]{String.valueOf(cspch)});
//        String id  = getService().save("T_SDZYC_JDXX" ,newMap ,null);
        String id = String.valueOf(newMap.get("ID"));
        if(StringUtil.isEmpty(id)){//����������
            String jybh = SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCJJGYPJYBH", false);
            String jylx="委托待检";
            newMap.put("JYXX",jylx);
            Object cspch  = newMap.get("PCH");
            String Updatewsql ="update t_sdzyc_jjg_ypscxx set JYJG='1'where SCPCH='"+cspch+"'";
            DatabaseHandlerDao.getInstance().executeSql(Updatewsql);
            newMap.put("JYBH",jybh);
            id = save("T_SDZYC_JJG_YPJYJCXX" ,newMap ,null);
            newMap.put(AppDefineUtil.C_ID, id);
            if(!StringUtil.isEmpty(id)){//���ݱ���ɹ�ִ���ļ��ϴ�����
                if( null != imageUpload){
                    //�����ļ��ϴ�����ɾ����ʷ�����ļ�
                    File oldFile = new File(REAL_PATH+"/"+newMap.get("T_SDZYC_JJG_YPJYJCXX"));

                    String fileNameWithStamp = System.currentTimeMillis() + "_" + imageUploadFileName.get(0);
                    File newFile = new File(REAL_PATH+"/"+fileNameWithStamp);
                    try {
                        if(oldFile.exists()){
                            oldFile.delete();
                        }
                        FileUtils.copyFile(imageUpload.get(0), newFile);
                        //�ļ��ϴ��ɹ��������ݱ���
                        newMap.put("ID",id);
                        newMap.put("JYWJ",fileNameWithStamp);
                        save("T_SDZYC_JJG_YPJYJCXX" ,newMap ,null);
                        try {
                            newMap.put("base64", encodeBase64File(REAL_PATH+"/"+fileNameWithStamp));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        //�ϴ��ļ�ʧ��ɾ���������ݺ�ͼƬ
                        delete(id);
                        if(newFile.exists()){
                            oldFile.delete();
                        }
                    }

                }
            }
            sendCreateTestEnttity(newMap);
            return newMap;
        }
        save("T_SDZYC_JJG_YPJYJCXX" ,newMap ,null);
        newMap.put(AppDefineUtil.C_ID, id);
        if(!StringUtil.isEmpty(id)){//���ݱ���ɹ�ִ���ļ��ϴ�����
            if( null != imageUpload){
                //�����ļ��ϴ�����ɾ����ʷ�����ļ�
                File oldFile = new File(REAL_PATH+"/"+newMap.get("JYWJ"));

                String fileNameWithStamp = System.currentTimeMillis() + "_" + imageUploadFileName.get(0);
                File newFile = new File(REAL_PATH+"/"+fileNameWithStamp);
                try {
                    if(oldFile.exists()){
                        oldFile.delete();
                    }
                    FileUtils.copyFile(imageUpload.get(0),newFile);
                    //�ļ��ϴ��ɹ��������ݱ���
                    newMap.put("ID",id);
                    newMap.put("JYWJ",fileNameWithStamp);
                    save("T_SDZYC_JJG_YPJYJCXX" ,newMap ,null);
                } catch (IOException e) {
                    //�ϴ��ļ�ʧ��ɾ���������ݺ�ͼƬ
                    delete(id);
                    if(newFile.exists()){
                        oldFile.delete();
                    }
                }

            }
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


    public  String defaultCode(){
        String code= SerialNumberUtil.getInstance().getCompanyCode();
        String  defaultCode=" ";
        if(code!=null && !"".equals(code) )
            defaultCode= AppDefineUtil.RELATION_AND+" QYBM= '"+code+"' "+ AppDefineUtil.RELATION_AND+" is_delete <> '1'";
        return defaultCode;
    }
    public Map<String,Object> searchById(String id) {
        String sql =  "select * from t_sdzyc_jjg_ypjyjcxx where id=?"+defaultCode();
        return DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{id});
    }
    /**
     * �޸�ͼƬ��Ϣ
     * @param id
     * @return
     */
    public int updateJywj(String id) {
        String sql =  "update t_sdzyc_jjg_ypjyjcxx set jywj='' where id=?"+defaultCode();
        return DatabaseHandlerDao.getInstance().executeSql(sql, new String[]{id});
    }
    
}
