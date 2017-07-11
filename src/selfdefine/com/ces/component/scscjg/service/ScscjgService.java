package com.ces.component.scscjg.service;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ScscjgService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    @Override
    public Map<String, String> save(String tableId, String entityJson, Map<String, Object> paramMap) {
        //获得表名
        String tableName = getTableName(tableId);
        // 表单数据转换成json数据
        JsonNode entityNode = JsonUtil.json2node(entityJson);
        //json数据转换成Map类型数据
        Map<String, String> dataMap = node2map(entityNode);
        // 默认密码MD5加密
        dataMap.put("MM",new ces.coral.encrypt.MD5().getMD5ofStr("000000"));
        //执行保存方法  执行成功返回ID
        String id = save(tableName, dataMap, paramMap);
        //把ID传递到前台 ，防止保存时，添加重复数据
        dataMap.put(AppDefineUtil.C_ID, id);
        return dataMap;
    }

    public String checkUserName(String id ,String dlm) {
       // String flag = "0"; //0没有重复  1为：有重复的dlm
        Map<String, Object> dataMap = null;
        String sql = "" ;
        if (null != id && !"".equals(id)) {
            //先通过ID查出原来用户的登录名
            sql = "select JGYXM,SFZH,DLM,MM,QYBM,QYZT from T_SC_JGY where id='"+id+"'"; //根据ID查出数据
            dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql);
            if (null != dlm && !"".equals(dlm)) {//
                if (dlm.equals(dataMap.get("DLM"))) {//如果相同说明是同一个状物
                } else {
                    sql = "select  JGYXM,SFZH,MM,QYBM,QYZT from T_SC_JGY where dlm='"+dlm+"'"; //根据dlm查出数据
                    dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql);
                    if (null != dataMap && !dataMap.isEmpty() && dataMap.size() > 0) {//
                        return "1";
                    }
                }
            }
        } else {
            sql = "select  JGYXM,SFZH,MM,QYBM,QYZT from T_SC_JGY where dlm='"+dlm+"'"; //根据dlm查出数据
            dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql);
            if (null != dataMap && !dataMap.isEmpty() && dataMap.size() > 0) {//
                return "1";
            }
        }
        return "0";
    }
}