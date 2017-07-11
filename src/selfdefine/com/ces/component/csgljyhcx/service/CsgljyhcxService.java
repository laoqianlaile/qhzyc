package com.ces.component.csgljyhcx.service;

import com.ces.component.trace.utils.CompanyInfoUtil;
import com.ces.config.utils.AppDefineUtil;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.Map;

@Component
public class CsgljyhcxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    public String defaultCode(){
        String csxx = CompanyInfoUtil.getInstance().getCityInfo();
        return AppDefineUtil.RELATION_AND+" csxx like '"+csxx+"%' ";
    }
}