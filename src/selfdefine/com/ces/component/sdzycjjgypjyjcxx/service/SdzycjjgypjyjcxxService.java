package com.ces.component.sdzycjjgypjyjcxx.service;

import cesgroup.rear.lib.client.enterprise.RearClientBuilder;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.StatisticalCodeUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.UUIDGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import enterprise.endpoints.EnterpriseService;
import enterprise.entity.TestEntity;
import enterprise.entity.TestFileEntity;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;

import com.ces.component.sdzycjjgypjyjcxx.dao.SdzycjjgypjyjcxxDao;
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
public class SdzycjjgypjyjcxxService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzycjjgypjyjcxxDao> {
    private final String REAL_PATH = ServletActionContext.getServletContext().getRealPath("/spzstpfj");

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
            String jybh = SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCJJGYPJYBH", false);
            String jylx="政府飞检";
            newMap.put("JYXX",jylx);
            newMap.put("JYBH",jybh);
            id = save("T_SDZYC_JJG_YPJYJCXX" ,newMap ,null);
           // String updSql ="update t_sdzyc_cjg_ycjgxx set jyjg=? where  jgpch=?";
            String updSql ="update t_sdzyc_jjg_ypscxx set jyjg=? where  scpch=?";
           // System.out.println("updSql=="+updSql+"="+String.valueOf(newMap.get("SCPCH")));
            DatabaseHandlerDao.getInstance().executeSql(updSql,new String[]{String.valueOf(newMap.get("JYJG")),String.valueOf(newMap.get("PCH"))});
            newMap.put(AppDefineUtil.C_ID, id);
            if(!StringUtil.isEmpty(id)){//数据保存成功执行文件上传操作
                if( null != imageUpload){
                    //进行文件上传，则删除历史保存文件
                    File oldFile = new File(REAL_PATH+"/"+newMap.get("T_SDZYC_JJG_YPJYJCXX"));
                    String[] args = imageUploadFileName.get(0).split("\\.");
                    String fileNameWithStamp = System.currentTimeMillis()+ UUIDGenerator.uuid()+"."+args[1];
                    File newFile = new File(REAL_PATH+"/"+fileNameWithStamp);
                    try {
                        if(oldFile.exists()){
                            oldFile.delete();
                        }
                        FileUtils.copyFile(imageUpload.get(0),newFile);
                        //文件上传成功进行数据保存
                        newMap.put("ID",id);
                        newMap.put("JYWJ",fileNameWithStamp);
                        save("T_SDZYC_JJG_YPJYJCXX" ,newMap ,null);
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
            //同步数据
            String sql = "select id from t_sdzyc_jjg_ypscxx where scpch = ? and qybm = ?";
            Map<String,Object> mapData = DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{String.valueOf(newMap.get("pch")),SerialNumberUtil.getInstance().getCompanyCode()});
            String processId = String.valueOf(mapData.get("ID"));
            newMap.put("process_id",processId==null?"":processId.toString());
            sendCreateTestEnttity(newMap);
            return newMap;
        }
        save("T_SDZYC_JJG_YPJYJCXX" ,newMap ,null);
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
                    save("T_SDZYC_JJG_YPJYJCXX" ,newMap ,null);
                } catch (IOException e) {
                    //上传文件失败删除保存数据和图片
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

    public Map<String,Object> searchYpscpch() {
        String  companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select * from t_sdzyc_jjg_ypscxx t where t.is_delete = '0' and t.qybm= '"+companyCode+"' ORDER BY scpch desc ";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }

    public Map<String,Object> setqyzjpch(){
       // String  companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select * from t_sdzyc_jjg_ypscxx t where t.is_delete = '0' and t.jyjg='0' and t.qybm=? ORDER BY scrq  desc ";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql,new String[]{SerialNumberUtil.getInstance().getCompanyCode()}));

    }
    public Map<String,Object> getGridTypeData(List<Map<String,Object>> list){
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data",list);
        return  dataMap;
    }

    public Map<String,Object> searchById(String id) {
        String sql =  "select * from t_sdzyc_jjg_ypjyjcxx where id=?"+defaultCode();
        return DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{id});
    }

    public int updateJywj(String id) {
        String sql =  "update t_sdzyc_jjg_ypjyjcxx set jywj='' where id=?"+defaultCode();
        return DatabaseHandlerDao.getInstance().executeSql(sql, new String[]{id});
    }

    public  String defaultCode(){
        String code= SerialNumberUtil.getInstance().getCompanyCode();
        String  defaultCode=" ";
        if(code!=null && !"".equals(code) )
            defaultCode= AppDefineUtil.RELATION_AND+" QYBM= '"+code+"' "+ AppDefineUtil.RELATION_AND+" is_delete <> '1'";
        return defaultCode;
    }
    @Override
    public void delete(String tableId, String dTableIds, String ids, boolean isLogicalDelete, Map<String, Object> paramMap) {
        super.delete(tableId, dTableIds, ids, isLogicalDelete, paramMap);
        //在进行删除之后取出ids进行附件删除
        String filter    =  " t_sdzyc_jjg_jcbgfjb WHERE PID IN ('" + ids.replace(",", "','") + "')";
        String sql = "SELECT BGFJ FROM "+filter;
        List<String> dataList = DatabaseHandlerDao.getInstance().queryForList(sql);
        if ( dataList!=null && dataList.size()>0){
            for (String bgfj : dataList){
                String filePath = REAL_PATH+"/"+bgfj;
                File file = new File(filePath);
                if(file.exists()){
                    file.delete();
                }
            }
        }
    }
    public void sendDelTestEntity(String id){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        service.deleteTest(id);
    }
}
