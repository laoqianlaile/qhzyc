package com.ces.component.sdzycgysxx.service;

import cesgroup.rear.lib.client.enterprise.RearClientBuilder;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.fasterxml.jackson.databind.JsonNode;
import enterprise.endpoints.EnterpriseService;
import enterprise.entity.SupplierEntity;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;

import com.ces.component.sdzycgysxx.dao.SdzycgysxxDao;
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
public class SdzycgysxxService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzycgysxxDao> {

    private final String REAL_PATH = ServletActionContext.getServletContext().getRealPath("/spzstpfj");

    /**
     *默认权限过滤
     * @return
     */
    public  String defaultCode(){
        String code= SerialNumberUtil.getInstance().getCompanyCode();
        String  defaultCode=" ";
        if(code!=null && !"".equals(code) )
            defaultCode= AppDefineUtil.RELATION_AND+" QYBM= '"+code+"' "+ AppDefineUtil.RELATION_AND+" is_delete <> '1'";
        return defaultCode;
    }
    /**
     * 获取精加工供应商的编号
     * @return
     */
    public Map<String,Object> searchgysxxComboGridData(){
        String sql = "select * from t_sdzyc_jjg_Ycgysxx where 1=1"+defaultCode();///+ " order by llsj desc";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }
    public Map<String,Object> getGridTypeData(List<Map<String,Object>> list){
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data",list);
        return  dataMap;
    }

    /**
     * 获取粗加工供应商信息
     * @param gysbh
     * @return
     */
    public Map<String,Object> searchYlgysxxByGysbh(String gysbh){
        String sql =  "select * from t_sdzyc_cjg_cdzm where zbid IN (select id from t_sdzyc_cjg_ylgysxx t where t.gysbh=? "+defaultCode()+")  ";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql, new String[]{gysbh}));
    }

    /**
     * 获取精加工供应商信息
     * @param gysbh
     * @return
     */
    public Map<String,Object> searchYcgysxxByGysbh(String gysbh){
        String sql =  "select * from t_sdzyc_jjg_cdzmw where zbid IN (select id from t_sdzyc_jjg_ycgysxx t where t.gysbh=? "+defaultCode()+")  ";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql, new String[]{gysbh}));
    }

    /**
     * 山东中药材精加工供应商编号自动生成 保存
     * @param tableId
     * @param entityJson
     * @param paramMap --参数Map（具体参数要求请查看ShowModuleDefineServiceDaoController.getMarkParamMap方法说明）
     * @return
     */

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
            String gysbh = SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCJJGGYSBH", false);
            newMap.put("GYSBH",gysbh);
            id = save("T_SDZYC_JJG_YCGYSXX" ,newMap ,null);
            newMap.put(AppDefineUtil.C_ID, id);
            if(!StringUtil.isEmpty(id)){//数据保存成功执行文件上传操作
                if( null != imageUpload){
                    //进行文件上传，则删除历史保存文件
                    File oldFile = new File(REAL_PATH+"/"+newMap.get("T_SDZYC_JJG_YCGYSXX"));

                    String fileNameWithStamp = System.currentTimeMillis() + "_" + imageUploadFileName.get(0);
                    File newFile = new File(REAL_PATH+"/"+fileNameWithStamp);
                    try {
                        if(oldFile.exists()){
                            oldFile.delete();
                        }
                        FileUtils.copyFile(imageUpload.get(0), newFile);
                        //文件上传成功进行数据保存
                        newMap.put("ID",id);
                        newMap.put("YYZZWJ",fileNameWithStamp);
                        save("T_SDZYC_JJG_YCGYSXX" ,newMap ,null);
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
            return newMap;
        }
        save("T_SDZYC_JJG_YCGYSXX" ,newMap ,null);
        newMap.put(AppDefineUtil.C_ID, id);
        if(!StringUtil.isEmpty(id)){//数据保存成功执行文件上传操作
            if( null != imageUpload){
                //进行文件上传，则删除历史保存文件
                File oldFile = new File(REAL_PATH+"/"+newMap.get("YYZZWJ"));

                String fileNameWithStamp = System.currentTimeMillis() + "_" + imageUploadFileName.get(0);
                File newFile = new File(REAL_PATH+"/"+fileNameWithStamp);
                try {
                    if(oldFile.exists()){
                        oldFile.delete();
                    }
                    FileUtils.copyFile(imageUpload.get(0),newFile);
                    //文件上传成功进行数据保存
                    newMap.put("ID",id);
                    newMap.put("YYZZWJ",fileNameWithStamp);
                    save("T_SDZYC_JJG_YCGYSXX" ,newMap ,null);
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
    /**
     * 根据Id查询
     * @param id
     * @return
     */
    public Map<String,Object> searchById(String id) {
        String sql =  "select * from t_sdzyc_jjg_ycgysxx where id=?"+defaultCode();
        return DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{id});
    }
    /**
     * 修改图片信息
     * @param id
     * @return
     */

    public int updateYyzzwj(String id) {
        String sql =  "update t_sdzyc_jjg_ycgysxx set yyzzwj='' where id=?"+defaultCode();
        return DatabaseHandlerDao.getInstance().executeSql(sql,new String[]{id});
    }









}
