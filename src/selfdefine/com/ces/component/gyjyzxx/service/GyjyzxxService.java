package com.ces.component.gyjyzxx.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.utils.AppDefineUtil;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.Map;

@Component
public class GyjyzxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    /**
     * 获得当前登录用户企业编码
     * @return
     */
    public String getQybm(){
        return SerialNumberUtil.getInstance().getCompanyCode();
    }
    /**
     * 默认权限过滤
     * @return
     */
    public  String defaultCode(){
        String code= SerialNumberUtil.getInstance().getCompanyCode();
        String  defaultCode=" ";
        if(code!=null && !"".equals(code) )
            defaultCode= AppDefineUtil.RELATION_AND+" B_QYBM = '"+code+"' ";
        return defaultCode;
    }
    @Override
    protected String buildCustomerFilter(String tableId,
                                         String componentVersionId, String moduleId, String menuId,
                                         Map<String, Object> paramMap) {
        // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
        return defaultCode();
    }

}