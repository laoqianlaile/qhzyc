package com.ces.component.prjytj.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.utils.AppDefineUtil;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.Map;

@Component
public class PrjytjService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    /**
     * qiucs 2014-10-9
     * <p>描述: 用户二次开发时，添加自己的过滤条件接口</p>
     * @param  paramMap --其他参数，详细见ShowModuleDefineServiceDaoController.getMarkParamMap方法介绍
     * @return String    返回类型
     * @throws
     */
    protected String buildCustomerFilter(String tableId, String componentVersionId, String moduleId, String menuId, Map<String, Object> paramMap) {
        // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
        // return AppDefineUtil.RELATION_AND + "DELETE_FLAG=0";
        return AppDefineUtil.RELATION_AND + "PFSCBM='"+ SerialNumberUtil.getInstance().getCompanyCode()+"'";
    }
}