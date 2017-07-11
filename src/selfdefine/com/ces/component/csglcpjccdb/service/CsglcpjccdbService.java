package com.ces.component.csglcpjccdb.service;

import com.ces.component.trace.utils.CompanyInfoUtil;
import com.ces.config.utils.AppDefineUtil;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.Map;

@Component
public class CsglcpjccdbService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    @Override
    protected String buildCustomerFilter(String tableId, String componentVersionId, String moduleId, String menuId, Map<String, Object> paramMap) {

        return defaultCode();
    }

    public String defaultCode(){
        String csxx = CompanyInfoUtil.getInstance().getCityInfo();
        if(null!=csxx && !csxx.equals(null) && !csxx.equals("null")){
            return AppDefineUtil.RELATION_AND+" xzqhdm like '"+csxx+"%'";
        }
        return "";
    }
}