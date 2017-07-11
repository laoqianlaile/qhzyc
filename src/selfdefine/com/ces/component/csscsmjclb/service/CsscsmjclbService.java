package com.ces.component.csscsmjclb.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.TableUtil;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.Map;

@Component
public class CsscsmjclbService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    /**
     * 默认过滤掉企业编码和is_delete
     * @param tableId
     * @param componentVersionId
     * @param moduleId
     * @param menuId
     * @param  paramMap --其他参数，详细见ShowModuleDefineServiceDaoController.getMarkParamMap方法介绍
     * @return
     */
    @Override
    protected String buildCustomerFilter(String tableId, String componentVersionId, String moduleId, String menuId, Map<String, Object> paramMap) {
        StringBuffer filter = new StringBuffer(AppDefineUtil.RELATION_AND + " is_delete = '0'");

            String qybm = SerialNumberUtil.getInstance().getCompanyCode();
            filter.append(AppDefineUtil.RELATION_AND + " csbm = '" + qybm + "'");

        return filter.toString();
    }
}