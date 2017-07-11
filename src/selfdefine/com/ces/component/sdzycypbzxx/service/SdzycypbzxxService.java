package com.ces.component.sdzycypbzxx.service;

import cesgroup.rear.lib.client.enterprise.RearClientBuilder;
import com.ces.component.sdzycypbzxx.dao.SdzycypbzxxDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.component.trace.utils.DataTypeConvertUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.StatisticalCodeUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.ComponentFileUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.databind.JsonNode;
import enterprise.endpoints.EnterpriseService;
import enterprise.entity.PrecisionEntity;
import enterprise.entity.PrecisionPack;
import enterprise.entity.TraceEntity;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class SdzycypbzxxService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzycypbzxxDao> {
    private final String REAL_PATH = ServletActionContext.getServletContext().getRealPath("/zsm");
    /**
     * 山东中药材精加工药材包装生产批次号下拉列表编码
     * @return
     */
    public Map<String,Object> searchBzxxscpchComboGridData(){
        String  companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "";
        if(companyCode.equalsIgnoreCase("000000809")||companyCode.equalsIgnoreCase("000000012"))
        {
            //阿胶 不检验也能包装
           // sql = "select y.scpch, y.qyscpch as QYPCH,y.lldh,y.jgzzl,y.qypch as pch,y.ypmc,y.ycdm from T_SDZYC_JJG_YPSCXX y where  y.qybm= ? order by y.scrq desc";
            sql = "select y.scpch, y.qyscpch as QYPCH,y.jgzzl,y.kc,y.ypmc,y.ycdm from v_sdzyc_jjg_bzkcglxx y  where y.kc>0 and  y.qybm= ? order by y.scrq desc";
        }else {
           // sql = "select y.scpch, y.qyscpch as QYPCH,y.lldh,y.jgzzl,y.qypch as pch,y.ypmc,y.ycdm from T_SDZYC_JJG_YPSCXX y,T_SDZYC_JJG_YPJYJCXX t  where y.scpch=t.pch and t.jyjg='1' and t.qybm= ?";
            sql = "select y.scpch, y.qyscpch as QYPCH,y.jgzzl,y.kc,y.ypmc,y.ycdm from v_sdzyc_jjg_bzkcglxx y,T_SDZYC_JJG_YPJYJCXX t  where y.scpch=t.pch and y.kc>0 and t.jyjg='1' and (t.jyxx ='企业自检' or t.jyxx='委托待检') and t.qybm= ? order by y.scrq desc";
        }
            return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql,new String[]{SerialNumberUtil.getInstance().getCompanyCode()}));
    }
    public Map<String,Object> getGridTypeData(List<Map<String,Object>> list){
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data",list);
        return  dataMap;
    }

    /**
     * 判断 生产批次号是否存在
     * @param scpch
     * @return
     */
    public List<Map<String,Object>> checkScpch(String scpch){
        String sql = " select * from T_sdzyc_jjg_ypbzxx where scpch = '"+scpch+"'";
        List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return list;

    }


    public void sendCreatePreLoadService(List<TraceEntity> traceEntities){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        service.preLoad(traceEntities);
    }
    public List<TraceEntity> traceInfo(String zsm, Map<String, Object> jjgMap){
        List<TraceEntity> traceEntities = new ArrayList<TraceEntity>();
        TraceEntity traceEntity = new TraceEntity();
        String ylpch = String.valueOf(jjgMap.get("YYCPCH"));
        Map<String, Object> rkMap = new HashMap<String, Object>();
        if(StringUtil.isNotEmpty(ylpch)) {
            String sql = "select t.ID,t.yptzsm from t_sdzyc_jjg_yycrkxx t where t.pch=? and qybm=?";
            rkMap = DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{ylpch,SerialNumberUtil.getInstance().getCompanyCode()});
        }
        if (ylpch.length()<26){
            traceEntity.setAtom(ylpch);
        }
        traceEntity.setIn_id(((String) rkMap.get("ID")));
        traceEntity.setComp_code((String) jjgMap.get("QYBM"));
        traceEntity.setIn_trace_code((String) rkMap.get("YPTZSM"));
        traceEntity.setOut_trace_code(zsm);
        traceEntity.setProcess_id((String) jjgMap.get("ID"));
        traceEntity.setComp_type("3");
        traceEntities.add(traceEntity);

        return traceEntities;
    }
    public TraceEntity traceEntityInfo(String zsm , Map<String,Object> bzMap){
        TraceEntity traceEntity = new TraceEntity();
        String qypch = String.valueOf(bzMap.get("QYPCH"));

        String sql = "select l.JGZZL,l.YYCPCH,l.pid,t.qybm from t_sdzyc_jjg_ypsctl l,t_sdzyc_jjg_ypscxx t where l.pid =t.ID and t.qyscpch=? and t.qybm = ?";
        List<Map<String,Object>> jjgMapList = DatabaseHandlerDao.getInstance().queryForMaps(sql,new String[]{qypch,SerialNumberUtil.getInstance().getCompanyCode()});
        double weight =0;
        String ylpch = "";
        Map<String,Object> jjgMap = null;
        for(Map<String,Object> map : jjgMapList){
            double oldWeight = Double.parseDouble(String.valueOf(map.get("JGZZL")));
            if(oldWeight > 0 ){
                ylpch = String.valueOf(map.get("YYCPCH"));
                jjgMap = map;
            }
        }
        //根据包装信息查询对应使用的原料批次号
        ///z
        Map<String, Object> rkMap = new HashMap<String, Object>();
        if(StringUtil.isNotEmpty(ylpch)) {
            sql = "select t.ID,t.yptzsm from t_sdzyc_jjg_yycrkxx t where t.pch=? and qybm = ?";
            rkMap = DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{ylpch,SerialNumberUtil.getInstance().getCompanyCode()});
        }
        if (ylpch.length()<26){
            traceEntity.setAtom(ylpch);
        }
        traceEntity.setIn_id(((String) rkMap.get("ID")));
        traceEntity.setComp_code(SerialNumberUtil.getInstance().getCompanyCode());
        traceEntity.setIn_trace_code((String) rkMap.get("YPTZSM"));
        traceEntity.setOut_trace_code(zsm);
        traceEntity.setProcess_id(String.valueOf(jjgMap.get("PID")));
        traceEntity.setComp_type("3");
        traceEntity.setPack_id((String)bzMap.get("ID"));
        return traceEntity;
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
    public void sendCreatePrecisionPackService(Map<String, String> map){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        PrecisionPack pe = precisionPackService(map);
        service.createPrecisionPack(pe);
    }
    public void sendModifyPrecisionPackService(String id,Map<String, String> map){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        PrecisionPack pe = precisionPackService(map);
        service.modifyPrecisionPack(id,pe);
    }
    private PrecisionPack precisionPackService(Map<String, String> map)
    {
        PrecisionPack pe = new PrecisionPack();
        pe.setId(map.get("ID"));
        pe.setPrecision_batch_no(map.get("SCPCH"));
        pe.setProduct_code(map.get("YCDM").toString());
        pe.setProduct_name(map.get("YPMC").toString());
        pe.setSpec(map.get("BZGG").toString());
        pe.setPack_weight(map.get("BZGG").toString());
        pe.setDate(map.get("BZSJ").toString());
        pe.setProcess_id(map.get("process_id"));
        pe.setBase64(map.get("base64"));
        pe.setImage(map.get("imageName").toString());
        pe.setComp_code(SerialNumberUtil.getInstance().getCompanyCode());
        return pe;
    }

    public Map<String, Object> piecesInfo(Map<String, String> map){
        String sql = "select * from t_sdzyc_jjg_ypscxx t where t.scpch=? and qybm=?";
        Map<String, Object> dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{map.get("SCPCH"),SerialNumberUtil.getInstance().getCompanyCode()});
        String REAL_PATH = ServletActionContext.getServletContext().getRealPath("/spzstpfj")+"/"+dataMap.get("XCTP");
        try {
            dataMap.put("base64", encodeBase64File(REAL_PATH));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataMap;
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

    public void saveBzzsm(String bzpch,String zsm,Map<String,String> dataMap){
        String sql = "insert into T_SDZYC_ZSM(id,ZSM,BZPCH) values(sys_guid(),?,?)";
        DatabaseHandlerDao.getInstance().executeSql(sql,new String[]{zsm,bzpch});
    }
    public void deleteOldZsm(String bzpch){
        String delSql = "delete from T_SDZYC_ZSM where bzpch=?";
        DatabaseHandlerDao.getInstance().executeSql(delSql,new String[]{bzpch});
    }

    public Map<String,String> searchOldBzxx(String id ){
        String sql = "select * from T_SDZYC_JJG_YPBZXX a_ where a_.ID=?";
        return DataTypeConvertUtil.getInstance().mapObj2mapStr(DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{id}));
    }

    public  void writText(String bzpch,String zsm,boolean need){
        String path = REAL_PATH+"/"+bzpch+".txt";
        String propertyPath = ComponentFileUtil.getConfigPath() +"/trace/zsm.properties";
        File file = new File(path);
        try {
            if(!file.exists())
                file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file,need);
            fos.write((DataTypeConvertUtil.getInstance().readTxtProp("zsmUrl",propertyPath)+zsm+"\r\n").getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过id 查找饮片包装信息
     * @param id
     * @return
     */
    public Map<String,Object> searchById(String id) {
        String sql =  "select * from T_SDZYC_JJG_YPBZXX where id=? and is_delete <> '1'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{id});
    }

    /**
     * 更新包装文件
     * @param id
     * @return
     */
    public int updateBzwj(String id) {
        String sql =  "update T_SDZYC_JJG_YPBZXX set bztp='' where id=? and is_delete <> '1'";
        return DatabaseHandlerDao.getInstance().executeSql(sql,new String[]{id});
    }
    public List<Map<String,Object>> inquirebzpch(String bzpch){
        String  sql="select * from t_sdzyc_zsm t where t.bzpch=?";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql, new String[]{bzpch});
    }
    public Map<String,Object> queryScPch(String pch){
        String sql =  "select id from t_sdzyc_jjg_ypbzxx where scpch='"+pch+"'";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }
    public Map<String,Object> queryPch(String pch){
        String sql =  "select sfrk from t_sdzyc_jjg_ypbzxx where bzpch='"+pch+"'";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }
}
