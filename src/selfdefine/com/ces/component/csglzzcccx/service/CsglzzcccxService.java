package com.ces.component.csglzzcccx.service;

import com.ces.component.trace.utils.CompanyInfoUtil;
import com.ces.config.utils.AppDefineUtil;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.Map;

@Component
public class CsglzzcccxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    @Override
    protected String buildCustomerFilter(String tableId, String componentVersionId, String moduleId, String menuId, Map<String, Object> paramMap) {
        return defaultCode();
    }

    public String defaultCode(){
        String csxx = CompanyInfoUtil.getInstance().getCityInfo();
        if(null!=csxx && !csxx.equals("")  && !csxx.equals("null")){
            if(csxx.length()==2){
                csxx+="0000";
            }else{
                csxx+="00";
            }

            return AppDefineUtil.RELATION_AND+" LSXZQHDM='"+csxx+"' ";
        }

        return AppDefineUtil.RELATION_AND+" LSXZQHDM='310000' ";
    }
}