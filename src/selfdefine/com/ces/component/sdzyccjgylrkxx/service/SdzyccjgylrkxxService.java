package com.ces.component.sdzyccjgylrkxx.service;

import cesgroup.rear.lib.client.enterprise.RearClientBuilder;
import com.ces.component.trace.utils.Base64FileUtil;
import com.ces.component.trace.utils.DataTypeConvertUtil;
import com.ces.component.trace.utils.ImageCompressUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.fasterxml.jackson.databind.JsonNode;
import enterprise.endpoints.EnterpriseService;
import enterprise.entity.TradeInEntity;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;

import com.ces.component.sdzyccjgylrkxx.dao.SdzyccjgylrkxxDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class SdzyccjgylrkxxService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzyccjgylrkxxDao> {
    private final String REAL_PATH = ServletActionContext.getServletContext().getRealPath("/spzstpfj");
    /**
     *
     * @param paramMap --参数Map（具体参数要求请查看ShowModuleDefineServiceDaoController.getMarkParamMap方法说明）
     * @return
     */
    public Map<String, String> saveDate(Map<String, String> paramMap) {
        Map<String, String> dataMap = DataTypeConvertUtil.getInstance().mapObj2mapStrFile(paramMap);
        String id = dataMap.get("ID");
        String base64 = String.valueOf(dataMap.get("JCXXLJ"));
        String imageSuffix = String.valueOf(dataMap.get("IMGSUFFIX"));
        dataMap.remove("JCXXLJ");
        dataMap.remove("IMGSUFFIX");

        String filename = ImageCompressUtil.getInstance().encordMd5(base64)+"."+imageSuffix;
        File file = new File(REAL_PATH+"/"+filename);
        if(!file.exists()){//文件上传重复，不再进行文件上传操作
            try {
                Base64FileUtil.decoderBase64File(base64, REAL_PATH + "/" + filename);//base64文件上传
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        dataMap.put("JCXXLJ",filename);
        if(StringUtil.isEmpty(id)){//是新增操作
            String ylrkbh = SerialNumberUtil.getInstance().getSerialNumber("SDZYC", "SDZYCCJGYLRKBH", true);
            dataMap.put("RKBH",ylrkbh);
            dataMap.put("CGLX","自产");
            saveOne("T_SDZYC_CJG_YLRKXX", dataMap);
            sendCreateTradeInService(dataMap);
        }else {
            saveOne("T_SDZYC_CJG_YLRKXX", dataMap);
            if(dataMap.get("PCH").toString().startsWith("W")) {
                dataMap.put("CGLX", "外购");
            }else{
                dataMap.put("CGLX","自产");
            }
            sendModifyTradeInService(id,dataMap);
        }
        dataMap.put(AppDefineUtil.C_ID, id);
        //保存成功同步处理种植企业采收信息更改为已入库状态
        String sql = "update T_SDZYC_CSGLXX set SFJC=1 where CSPCH=?";
        DatabaseHandlerDao.getInstance().executeSql(sql,new Object[]{dataMap.get("PCH")});
        return dataMap;
    }

    private void sendCreateTradeInService(Map<String, String> dataMap){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        TradeInEntity inEntity = TradeInService(dataMap);
        service.createTradeIn(inEntity);
    }
    private  TradeInEntity  TradeInService(Map<String, String> dataMap)
    {
        TradeInEntity inEntity = new TradeInEntity();
        inEntity.setId(dataMap.get("ID"));
        inEntity.setBatch_no(dataMap.get("PCH"));
        inEntity.setHerb_name(dataMap.get("YLMC"));
        if (StringUtil.isNotEmpty(dataMap.get("GYS"))) {
            inEntity.setOrigin(dataMap.get("GYS"));
        } else {
            inEntity.setOrigin(dataMap.get("YLCD"));
        }
        if (dataMap.get("RKZL") != null && dataMap.get("RKZL") != "") {
            inEntity.setWeight(Float.parseFloat(dataMap.get("RKZL")));
        } else {
            inEntity.setWeight(0);
        }
        inEntity.setDate(dateToLong(dataMap.get("RKSJ").toString()));
        inEntity.setTest_link(dataMap.get("JYDH").toString());
        inEntity.setPerson_in_charge(dataMap.get("RKDJFZR").toString());
        inEntity.setComp_code(dataMap.get("QYBM"));
        inEntity.setComp_type("2");
        inEntity.setChannel(dataMap.get("LYZT").toString());
        inEntity.setCglx(dataMap.get("CGLX").toString());
        String sql = "select distinct t.ycxmname from t_sdzyc_zycspbm t where t.zsspm=?";
        inEntity.setHerb_name_detail(String.valueOf(DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{dataMap.get("YCDM")}).get("ycxmname")));
        return inEntity;
    }
    private void sendModifyTradeInService(String id,Map<String, String> dataMap){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        TradeInEntity inEntity = TradeInService(dataMap);
        service.modifyTradeIn(id,inEntity);
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
     * 获取原料入库批次号
     */
    public Map<String,Object> searchylrkxxComboGridData(){
        String sql = "select * from t_sdzyc_cjg_ylrkxx where 1=1 "+defaultCode()+" order by rksj desc";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }
    public Map<String,Object> getGridTypeData(List<Map<String,Object>> list){
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data",list);
        return  dataMap;
    }
    public Map<String,Object> searchylrkxxBylldh(String pch){
        String sql =  "select * from t_sdzyc_cjg_ylrkxx where pch=?"+defaultCode();
        return DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{pch});
    }
    public Map<String,Object> searchDataByCspch(String cspch){
        String sql ="select * from t_sdzyc_cjg_ylrkxx where pch='"+cspch+"'";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }
    /**
     * 获取企业名称
     */
    public Map<String,Object> searchDataByQybm(String qybm,String dwlx){
        String sql ="select qymc from t_sdzyc_qyda where qybm='"+qybm+"'and dwlx='"+dwlx+"'";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }

    /**
     * 获取粗加工原料领料企业批次号下拉框的数据
     * @return
     */
    public Map<String,Object> getYlllpchGridData(){
        String sql = "select * from v_sdzyc_cjg_ylkcglxx where ylkc > 0 and qybm=? order by rksj desc";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql,new String[]{SerialNumberUtil.getInstance().getCompanyCode()}));
    }

    /**
     * 粗加工原料采购一批次号一库存  库存查询
     * @param pch
     * @return
     */
    public Map<String,Object> searchDataByPch(String pch){
        String sql = " select * from t_sdzyc_csglxx t where t.cspch ='"+pch+"'";
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }

    /**
     * 通过id 查找原料入库信息
     * @param id
     * @return
     */
    public Map<String,Object> searchById(String id) {
        String sql =  "select * from T_SDZYC_CJG_YLRKXX where id=? and is_delete <> '1'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{id});
    }

    /**
     * 更新原料入库检测信息链接
     * @param id
     * @return
     */
    public int updateJcxx(String id) {
        String sql =  "update T_SDZYC_CJG_YLRKXX set jcxxlj='' where id=? and is_delete <> '1'";
        return DatabaseHandlerDao.getInstance().executeSql(sql,new String[]{id});
    }

    /**保存导入数据
     * @param dataList
     * @return
     */
    @Transactional
    public Object importXls(List<Map<String,String>> dataList){
        Map<String,String> resultMap = new HashMap<String, String>();
        Map<String,Object> shareMap;
        if(dataList.size() == 0){
            resultMap.put("RESULT","ERROR");
            resultMap.put("MSG","模版里无数据！");
            return resultMap;
        }
        resultMap.put("RESULT","SUCCESS");
        resultMap.put("MSG","成功导入数据！");
        StringBuilder sql ;
        if("SUCCESS".equals(resultMap.get("RESULT"))){
            for(Map<String,String> map:dataList){
                saveOne("T_sdzyc_cjg_ylrkxx", map);
            }
        }
        return resultMap;
    }
    public void delete(String pch,String id){
        String sql = " delete from t_sdzyc_cjg_ylrkxx where id  = '" + id + "'";
        DatabaseHandlerDao.getInstance().executeSql(sql);
        String Updatesql = "update t_sdzyc_csglxx  set sfjc='0' where cspch=?";
        DatabaseHandlerDao.getInstance().executeSql(Updatesql, new Object[]{pch});
        sendDelTradeInService(id);
    }
    private void sendDelTradeInService(String id){
        EnterpriseService service = new RearClientBuilder().build(EnterpriseService.class);
        service.deleteTradeIn(id);
    }
/*    public void delete(String ids) {
        // 1. 获取所有关联表的要删除的IDS
        String tableName = "t_sdzyc_cjg_ylrkxx";
        String[] idDatas=ids.split(",");
        StringBuffer newIds=new StringBuffer("");
        for (int i = 0; i < idDatas.length; i++) {
            if(i >0 && i<=idDatas.length-1){
                newIds.append(",");
            }
            String string = idDatas[i];
            newIds.append("'"+string.split("_")[0]+"'");
        }
        String filter = "DELETE FROM "+tableName + " WHERE ID IN (" + newIds.toString() + ")";
        DatabaseHandlerDao.getInstance().executeSql(filter);
    }*/
}
    

