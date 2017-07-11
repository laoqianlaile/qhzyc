package com.ces.config.action.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts2.ServletActionContext;

import com.ces.config.dhtmlx.entity.component.ComponentInputParam;
import com.ces.config.dhtmlx.entity.construct.ConstructDetail;
import com.ces.config.dhtmlx.entity.construct.ConstructDetailSelfParam;
import com.ces.config.dhtmlx.service.component.ComponentInputParamService;
import com.ces.config.dhtmlx.service.component.ComponentSystemParameterRelationService;
import com.ces.config.dhtmlx.service.construct.ConstructDetailSelfParamService;
import com.ces.config.dhtmlx.service.construct.ConstructDetailService;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.web.struts2.StringIDController;

/**
 * 逻辑构件的基类
 */
public class LogicComponentController extends StringIDController<StringIDEntity> {

    private static final long serialVersionUID = 1L;

    /** * 系统参数Map */
    private Map<String, String> systemParams = new HashMap<String, String>();

    /** * 自身参数Map */
    private Map<String, String> selfParams = new HashMap<String, String>();

    /** * 输入参数Map */
    private Map<String, String> inputParams = new HashMap<String, String>();

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new StringIDEntity());
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.AbstractController#prepare()
     */
    @Override
    public void prepare() throws Exception {
        super.prepare();
        initParams();
    }

    /**
     * 初始化构件参数
     */
    protected void initParams() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String constructDetailId = request.getParameter("constructDetailId");
        ConstructDetail constructDetail = getService(ConstructDetailService.class).getByID(constructDetailId);
        // 系统参数
        List<Object[]> componentSystemParamList = getService(ComponentSystemParameterRelationService.class).getComponentSystemParams(
                constructDetail.getComponentVersionId());
        if (CollectionUtils.isNotEmpty(componentSystemParamList)) {
            for (Object[] obj : componentSystemParamList) {
                systemParams.put(String.valueOf(obj[0]), String.valueOf(obj[1]));
            }
        }
        // 自身参数
        List<ConstructDetailSelfParam> constructDetailSelfParamList = getService(ConstructDetailSelfParamService.class).getByConstructDetailId(
                constructDetailId);
        if (constructDetailSelfParamList != null && !constructDetailSelfParamList.isEmpty()) {
            for (ConstructDetailSelfParam constructDetailSelfParam : constructDetailSelfParamList) {
                selfParams.put(constructDetailSelfParam.getName(), constructDetailSelfParam.getValue());
            }
        }
        // 输入参数
        List<ComponentInputParam> componentInputParamList = getService(ComponentInputParamService.class).getByComponentVersionId(
                constructDetail.getComponentVersionId());
        if (CollectionUtils.isNotEmpty(componentInputParamList)) {
            for (ComponentInputParam componentInputParam : componentInputParamList) {
                inputParams.put(componentInputParam.getName(), request.getParameter(componentInputParam.getName()));
            }
        }
    }

    /**
     * 获取系统参数的值
     * 
     * @param name
     * @return String
     */
    protected String getSystemParamValue(String name) {
        return systemParams.get(name);
    }

    /**
     * 获取自身参数的值
     * 
     * @param name
     * @return String
     */
    protected String getSelfParamValue(String name) {
        return selfParams.get(name);
    }

    /**
     * 获取输入参数的值
     * 
     * @param name
     * @return String
     */
    protected String getInputParamValue(String name) {
        return inputParams.get(name);
    }
}
