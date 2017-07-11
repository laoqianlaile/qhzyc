package com.ces.component.scjysh.service;

import ces.coral.encrypt.MD5;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ScjyshService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    @Override
    @Transactional
    public Map<String, String> save(String tableId, String entityJson, Map<String, Object> paramMap) {
        String sql = "";
        String tableName = getTableName(tableId);
        JsonNode entityNode = JsonUtil.json2node(entityJson);
        Map<String, String> dataMap = node2map(entityNode);
        //根据ID查出该条数据原数据中的商铺编号
        if(!"".equals(dataMap.get("ID")) && null != dataMap.get("ID")){
            sql = "select SPBH from T_SC_JYSH t_  where  1 = 1 " + defaultCode() + " and ID = '" +dataMap.get("ID")+"'";
            Map<String , Object> map =  DatabaseHandlerDao.getInstance().queryForMap(sql);
            //分辨商铺编号的增删
            String[] oldspbh = map.get("SPBH").toString().split(",");
            String[] newspbh = dataMap.get("SPBH").toString().split(",");
            //将空闲改为使用中
            for(int i=0;i<newspbh.length;i++){
                sql = "update T_SC_SPXX t set t.syzt = 1 where 1 = 1 " + defaultCode() + " and t.spbh = '"+newspbh[i]+"'" ;
                DatabaseHandlerDao.getInstance().executeSql(sql);
            }
            //将使用中改为空闲
            for(int i=0;i<oldspbh.length;i++){
                boolean bool = true;
                for(int j=0;j<newspbh.length;j++){
                    if(newspbh[j].equals(oldspbh[i])){
                        bool = false;
                    }
                }
                if(bool){
                    sql = "update T_SC_SPXX t set t.syzt = 2 where 1 = 1 " + defaultCode() + " and t.spbh = '"+oldspbh[i]+"'" ;
                    DatabaseHandlerDao.getInstance().executeSql(sql);
                }
            }
        }else{
            String[] newspbh = dataMap.get("SPBH").toString().split(",");
            //将空闲改为使用中
            for(int i=0;i<newspbh.length;i++){
                sql = "update T_SC_SPXX t set t.syzt = 1 where 1 = 1 " + defaultCode() + " and t.spbh = '"+newspbh[i]+"'" ;
                DatabaseHandlerDao.getInstance().executeSql(sql);
            }
        }

        //密码加密
        if("".equals(dataMap.get("ID"))){
            MD5 m5 =new MD5();
            dataMap.put("MM", m5.getMD5ofStr("000000"));
        }
        String id = save(tableName, dataMap, paramMap);
        dataMap.put(AppDefineUtil.C_ID, id);
        return dataMap;
    }
    /**
     * 默认权限过滤
     * @return
     */

    public String checkUserName(String id ,String dlm) {
        // String flag = "0"; //0没有重复  1为：有重复的dlm
        Map<String, Object> dataMap = null;
        String sql = "" ;
        if (null != id && !"".equals(id)) {
            //先通过ID查出原来用户的登录名
            sql = "select ID, DLM from T_SC_JYSH where id='"+id+"'"; //根据ID查出数据
            dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql);
            if (null != dlm && !"".equals(dlm)) {//
                if (dlm.equals(dataMap.get("DLM"))) {//如果相同说明是同一个状物
                } else {
                    sql = "select ID, DLM from T_SC_JYSH where dlm='"+dlm+"'"; //根据dlm查出数据
                    dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql);
                    if (null != dataMap && !dataMap.isEmpty() && dataMap.size() > 0) {//
                        return "1";
                    }
                }
            }
        } else {
            sql = "select ID,DLM from T_SC_JYSH where dlm='"+dlm+"'"; //根据dlm查出数据
            dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql);
            if (null != dataMap && !dataMap.isEmpty() && dataMap.size() > 0) {//
                return "1";
            }

        }
        return "0";
    }
    public  String defaultCode(){
        String code= SerialNumberUtil.getInstance().getCompanyCode();
        String  defaultCode=" ";
        if(code!=null && !"".equals(code) )
            defaultCode=AppDefineUtil.RELATION_AND+" QYBM = '"+code+"'";
        return defaultCode;
    }

    @Override
    protected String buildCustomerFilter(String tableId,
                                         String componentVersionId, String moduleId, String menuId,
                                         Map<String, Object> paramMap) {
        // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
        return defaultCode();
    }


    public Object getUpdateSpbh(String spbh){
        String spbhs[] = spbh.split(",");
        StringBuffer sql = new StringBuffer("SELECT SPBH,WZ,MJ FROM T_SC_SPXX T WHERE SPBH IN (");
        for(int i = 0 ; i < spbhs.length ; i++){
            sql.append("'" + spbhs[i] + "'");
            if(i == (spbhs.length - 1)){
                break;
            }
            sql.append(",");
        }
        sql.append(") OR ( QYZT = '1' AND SYZT = '2' AND QYBM = '" + SerialNumberUtil.getInstance().getCompanyCode() + "' AND IS_DELETE <> '1') ORDER BY SPBH DESC");
        List<Map<String,Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString());
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data",dataList);
        return dataMap;
    }


}