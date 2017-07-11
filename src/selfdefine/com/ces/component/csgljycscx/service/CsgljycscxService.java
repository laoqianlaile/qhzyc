package com.ces.component.csgljycscx.service;

import com.ces.component.trace.utils.CompanyInfoUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.utils.AppDefineUtil;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.Map;

@Component
public class CsgljycscxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    @Override
    protected String buildCustomerFilter(String tableId, String componentVersionId, String moduleId, String menuId, Map<String, Object> paramMap) {

        return defaultCode();
    }

    public String defaultCode(){
        String csxx = CompanyInfoUtil.getInstance().getCityInfo();
        return AppDefineUtil.RELATION_AND+" csxx like '"+csxx+"%' and xtlx !=20";
    }
}