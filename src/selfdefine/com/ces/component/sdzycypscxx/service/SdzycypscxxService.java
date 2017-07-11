package com.ces.component.sdzycypscxx.service;

import cesgroup.rear.lib.client.enterprise.RearClientBuilder;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.StatisticalCodeUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.fasterxml.jackson.databind.JsonNode;
import enterprise.endpoints.EnterpriseService;
import enterprise.entity.PrecisionEntity;
import enterprise.entity.PrecisionLlEntity;
import org.springframework.stereotype.Component;

import com.ces.component.sdzycypscxx.dao.SdzycypscxxDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SdzycypscxxService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzycypscxxDao> {
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
     * 获取精加工饮片生产编号下拉列表数据
     * @return
     */
    public Map<String,Object> searchYpscbhxxComboGridData(){
        String  companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select SCFABH,YPMC,YCL,YCLBL,t.JGGY,t.YCDM,t.YPJS from T_SDZYC_YPSCFA t where t.qybm= '"+companyCode+"'";
        List<Map<String,Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        for ( int i =0 ; i<dataList.size() ; i++){
            Map<String,Object> map = dataList.get(i);
            map.put("JGGY",searchJggymc(String.valueOf(map.get("JGGY"))));
        }
        return getGridTypeData(dataList);
    }

    /**
     * 通用方法：添加map数据类型，以便后台传值
     * @param list
     * @return
     */
    public Map<String,Object> getGridTypeData(List<Map<String,Object>> list){
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data",list);
        return  dataMap;
    }

    /**
     *获取领料单号下拉列表的数据
     * @return
     */
    public Map<String,Object> searchLldhData(){
        String  companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        //String sql = "select * from T_SDZYC_JJG_SCLLXX t where t.qybm= '"+companyCode+"' order by t.llsj desc";
        String sql = "select lldh,llsj,sum(llzzl) llzzl from  v_sdzyc_jjg_scllxx  where issc='0' and qybm= '"+companyCode+"' group by lldh,llsj order by llsj desc";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }
    public List<Map<String,Object>> searchLlycxxData(String lldh){
        String sql = "select LLZZL,LLMC,PCH,LLDH,YCDM,CSPCH,QYPCH,LLSJ,cd from T_SDZYC_JJG_SCLLXXXX t where t.qybm=? and lldh=?";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql,new String []{SerialNumberUtil.getInstance().getCompanyCode(),lldh});
    }

    public String searchJggymc(String jggy){
        String sql = "select gymc from T_SDZYC_JJG_JGGY t where t.gybh in ('"+jggy.replace(",", "','")+"') and t.qybm=?";
        List<String> list = DatabaseHandlerDao.getInstance().queryForList(sql,new String[]{SerialNumberUtil.getInstance().getCompanyCode()});
        String data = list.toString();
        data = data.replace("[","").replace("]","");
        return data;
    }
    /**
     * 山东中药材精加工饮片生产批次号自动生成 保存
     * @param tableId
     * @param entityJson
     * @param paramMap --参数Map（具体参数要求请查看ShowModuleDefineServiceDaoController.getMarkParamMap方法说明）
     * @return
     */
    public Map<String, String> save(String tableId, String entityJson, Map<String, Object> paramMap) {
        String tableName = getTableName(tableId);
        JsonNode entityNode = JsonUtil.json2node(entityJson);
        Map<String, String> dataMap = node2map(entityNode);
        String id = dataMap.get("ID");
        if(StringUtil.isEmpty(id)){//是新增操作
            String jjggysbh = StatisticalCodeUtil.getInstance().getTwentyFivePcm("JJG","SDZYC", "SDZYCJJGYPSCPCH");
            dataMap.put("SCPCH",jjggysbh);
            id = save(tableName, dataMap, paramMap);
            sendCreatePrecisionService(dataMap);
        }else {
            save(tableName, dataMap, paramMap);
        }
        dataMap.put(AppDefineUtil.C_ID, id);
        return dataMap;
    }
    public void sendCreatePrecisionService(Map<String, String> map){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        PrecisionEntity pe = new PrecisionEntity();
        pe.setId((String) map.get("ID"));
        pe.setComp_code(map.get("QYBM"));
        pe.setManufact_plan_no((String) map.get("SCFABH"));
        pe.setManufact_batch_no((String) map.get("SCPCH"));
        pe.setHerb_name((String) map.get("YPMC"));
        pe.setDate(dateToLong((String) map.get("BZSJ")));
        pe.setWeight(Float.parseFloat(String.valueOf(map.get("JGZZL"))));
        pe.setStandard((String) map.get("ZXBZ"));
        pe.setCraft_man((String) map.get("GYY"));
        pe.setManager((String) map.get("SCJL"));
        pe.setImage((String) map.get("XCTP"));
        pe.setBatch_no((String) map.get("YYCPCH"));
        pe.setBase64((String) map.get("base64"));
        pe.setImage((String) map.get("imageName"));
        service.createPrecision(pe);
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
     * 根据Id查询精加工加工信息
     * @param id
     * @return
     */
    public Map<String ,Object> searchById(String id ){
        String sql =  "select * from t_sdzyc_jjg_ypscxx where id=?"+defaultCode();
        return DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{id});
    }
    /**
     * 修改图片信息
     * @param id
     * @return
     */
    public int updateXctp(String id ){
        String sql =  "update t_sdzyc_jjg_ypscxx set xctp='' where id=?"+defaultCode();
        return DatabaseHandlerDao.getInstance().executeSql(sql,new String[]{id});
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
            File imgFile = new File(path);
            if(imgFile.isFile()) {
                in = new FileInputStream(path);
                data = new byte[in.available()];
                in.read(data);
                in.close();
                //对字节数组Base64编码
                BASE64Encoder encoder = new BASE64Encoder();
                return encoder.encode(data);//返回Base64编码过的字节数组字符串
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;

    }
    public static String encodeBase64File(File imgFile) throws Exception {
        InputStream in;
        byte[] data = null;
        //读取图片字节数组
        try
        {
            if(imgFile.isFile()) {
                in = new FileInputStream(imgFile);
                data = new byte[in.available()];
                in.read(data);
                in.close();
                //对字节数组Base64编码
                BASE64Encoder encoder = new BASE64Encoder();
                return encoder.encode(data);//返回Base64编码过的字节数组字符串
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;

    }
    public void delete(String lldh,String id){
        String Updatesql = "update t_sdzyc_jjg_scllxx t set issc='0' where lldh=?";
        DatabaseHandlerDao.getInstance().executeSql(Updatesql, new String[]{lldh});
        String sql = " delete from t_sdzyc_jjg_ypscxx where id  =?";
        DatabaseHandlerDao.getInstance().executeSql(sql,new String[]{id});
        String sqltl = " delete from t_sdzyc_jjg_ypsctl where pid  =?";
        DatabaseHandlerDao.getInstance().executeSql(sqltl ,new String[]{id});
        }
    public Map<String,Object> queryPch(String pch){
        String sql =  "select jyjg from t_sdzyc_jjg_ypscxx where scpch='"+pch+"'";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }
    public void sendDelPrecisionService(String id){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        service.deletePrecision(id);
    }
}
