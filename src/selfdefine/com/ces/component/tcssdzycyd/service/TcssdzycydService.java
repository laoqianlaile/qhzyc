package com.ces.component.tcssdzycyd.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.TableUtil;
import org.springframework.stereotype.Component;

import com.ces.component.tcssdzycyd.dao.TcssdzycydDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.Map;

@Component
public class TcssdzycydService extends TraceShowModuleDefineDaoService<StringIDEntity, TcssdzycydDao> {

    @Override
    protected String buildCustomerFilter(String tableId, String componentVersionId, String moduleId, String menuId, Map<String, Object> paramMap) {
        StringBuffer filter = new StringBuffer(AppDefineUtil.RELATION_AND + " is_delete = '0'");
        return filter.toString();
    }
}
