package com.ces.component.scscspxx.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ScscspxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    /**
     * 获得商铺下拉列表数据
     * @return
     */
    public Map<String,Object> getspxxGrid(){
        Map<String , Object> map = new HashMap<String, Object>();
        String sql = "select t.spbh,t.wz,t.mj from t_sc_spxx t where 1=1 "+defaultCode() +"and t.syzt = 2 and t.qyzt = 1 order by t.spbh desc";
        List<Map<String,Object>> mapList= DatabaseHandlerDao.getInstance().queryForMaps(sql);
        map.put("data",mapList);
        return map;
    }
    /**
     * 默认权限过滤
     *
     * @return
     */
    public String defaultCode() {
        String code = SerialNumberUtil.getInstance().getCompanyCode();
        String defaultCode = " ";
        if (code != null && !"".equals(code))
            defaultCode = AppDefineUtil.RELATION_AND + " QYBM = '" + code
                    + "' ";
        return defaultCode;
    }

    @Override
    protected String buildCustomerFilter(String tableId,
                                         String componentVersionId, String moduleId, String menuId,
                                         Map<String, Object> paramMap) {
        // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
        return defaultCode();
    }

    public boolean getCheckspbh(String spbh){
        boolean bool = false;
        String sql = "select * from T_SC_SPXX where spbh = '" + spbh + "'";
        Map<String,Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql);
        if(map.size()>0){
            bool = false;
        }else if(map.size() == 0){
            bool = true;
        }
        return bool;
    }
}