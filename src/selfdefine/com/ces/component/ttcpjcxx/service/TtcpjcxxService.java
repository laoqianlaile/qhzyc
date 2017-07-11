package com.ces.component.ttcpjcxx.service;

import com.ces.component.cspt.entity.TCsptZsxxEntity;
import com.ces.component.trace.utils.CompanyInfoUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.TraceChainUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.StringUtil;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class TtcpjcxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {
    /**
     * 默认权限过滤
     * @return
     */
    public  String defaultCode(){
        String code= SerialNumberUtil.getInstance().getCompanyCode();
        String  defaultCode=" ";
        if(code!=null && !"".equals(code) )
            defaultCode=" AND TTBM = '"+code+"'";
        return defaultCode;
    }

    @Override
    protected String buildCustomerFilter(String tableId,
                                         String componentVersionId, String moduleId, String menuId,
                                         Map<String, Object> paramMap) {
        // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
        return defaultCode();
    }
    /**
     * 条码进场保存
     * @param barCode
     * @return
     */
    @Transactional
    public List<String> barCodeSave(String barCode) {
        //返回插入表的ID
        if(null !=barCode && !"".equals(barCode) && barCode.contains("JG")){
            return saveCpData(barCode);
        }
        return null;
    }


    public List<String> saveCpData(String barCode){
        List<String> ids = new ArrayList<String>();
        String sql = "";
        sql = "select j.qybm,j.jgcmc,t.CPBH,t.CPMC,t.CCBZGG,t.CCBZSL,t.CPZZL,t.PCH,t.ZSM,t.CCDJ from t_jg_cpccxx t,t_jg_JGCDA j  where t.is_in = '0' and t.cctmh='"+barCode.toUpperCase()+"' and j.qybm=t.qybm ";
        Map<String, Object> jyxx = DatabaseHandlerDao.getInstance().queryForMap(sql);
        if (!jyxx.isEmpty()) {
            Map<String,String> dataMap = new HashMap<String,String>();
            dataMap.put("TTBM", SerialNumberUtil.getInstance().getCompanyCode());
            dataMap.put("TTMC", CompanyInfoUtil.getInstance().getCompanyName("TT", SerialNumberUtil.getInstance().getCompanyCode()));
            dataMap.put("JGCBM", String.valueOf(jyxx.get("QYBM")));//加工厂编号
            dataMap.put("JGCMC", String.valueOf(jyxx.get("JGCMC")));//加工厂名称
            dataMap.put("CPBH", String.valueOf(jyxx.get("CPBH")));//成品编号
            dataMap.put("CPMC", String.valueOf(jyxx.get("CPMC")));//成品名称
            dataMap.put("DJ", String.valueOf(jyxx.get("CCDJ")));//单价
            dataMap.put("JCRQ", dateToString(new Date()));//进场日期
            dataMap.put("BZGG", String.valueOf(jyxx.get("CCBZGG")));//包装规格
            dataMap.put("BZSL", String.valueOf(jyxx.get("CCBZSL")));//包装数量
            dataMap.put("ZLL", String.valueOf(jyxx.get("CPZZL")));//总重量
            //dataMap.put("DJ", String.valueOf(jyxx.get("DJ")));
            dataMap.put("JHPCH", String.valueOf(jyxx.get("PCH")));//进化批次号
            dataMap.put("ZSPZH", String.valueOf(jyxx.get("ZSM")));//追溯码


           // dataMap.put("ZL", String.valueOf(jyxx.get("ZL")));
            String jcId = save("T_TT_CPJCXX", dataMap, null);

            ids.add(jcId);
            //更新批肉交易（已进场）
            sql = "update T_JG_CPCCXX t set t.is_in = '1' where upper(t.cctmh) = '" + barCode.toUpperCase() + "'";
            DatabaseHandlerDao.getInstance().executeSql(sql);
        }
        return ids;
    }

    @Override
    @Transactional
    public String save(String tableName, Map<String, String> dataMap, Map<String, Object> paramMap) {
        // 1. 获取表名
        if (StringUtil.isEmpty(tableName)) return "";
        // 2. 保存前，业务处理接口
        processBeforeSave(tableName, dataMap, paramMap);
        // 3. 根据ID判断是新增，还是修改
        String id = saveOne(tableName, dataMap);
        // 保存后，业务处理接口
        processAfterSave(tableName, dataMap, paramMap);
        /*****同步追溯信息*****/
        TCsptZsxxEntity entity = new TCsptZsxxEntity();
        entity.setJypzh(dataMap.get("ZSPZH"));
        entity.setJhpch(dataMap.get("JHPCH"));
        entity.setQybm(dataMap.get("TTBM"));
        entity.setQymc(dataMap.get("TTMC"));
        entity.setJyzbm(dataMap.get("JGCBM"));
        entity.setJyzmc(dataMap.get("JGCMC"));
        entity.setXtlx("6");
        entity.setRefId(dataMap.get("ID"));
        TraceChainUtil.getInstance().syncZsxx(entity);
        /********结束*******/
        return id;
    }




    public String dateToString(Date date){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

}