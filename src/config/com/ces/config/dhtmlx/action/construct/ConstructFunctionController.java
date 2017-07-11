package com.ces.config.dhtmlx.action.construct;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.construct.ConstructFunctionDao;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.entity.construct.Construct;
import com.ces.config.dhtmlx.entity.construct.ConstructFunction;
import com.ces.config.dhtmlx.entity.construct.ConstructInputParam;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.dhtmlx.service.construct.ConstructFunctionService;
import com.ces.config.dhtmlx.service.construct.ConstructInputParamService;
import com.ces.config.dhtmlx.service.construct.ConstructService;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.ComponentParamsUtil;
import com.ces.config.utils.StringUtil;

/**
 * 预留区绑定的构件和页面方法的绑定关系Controller
 * 
 * @author wanglei
 * @date 2013-08-27
 */
public class ConstructFunctionController extends ConfigDefineServiceDaoController<ConstructFunction, ConstructFunctionService, ConstructFunctionDao> {

    private static final long serialVersionUID = 5448108829911125571L;

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new ConstructFunction());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     * StringIDService)
     */
    @Autowired
    @Qualifier("constructFunctionService")
    @Override
    protected void setService(ConstructFunctionService service) {
        super.setService(service);
    }

    /**
     * 获取方法返回值列表数据
     * 
     * @return Object
     */
    public Object getFunctionDataList() {
        String componentVersionId = getParameter("componentVersionId");
        String constructDetailId = getParameter("constructDetailId");
        setReturnData(getService().getFunctionDataList(componentVersionId, constructDetailId));
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 获取构件入参列表数据
     * 
     * @return Object
     */
    public Object getInputParamList() {
        String componentVersionId = getParameter("componentVersionId");
        String constructDetailId = getParameter("constructDetailId");
        ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(componentVersionId);
        if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
            Construct construct = getService(ConstructService.class).getByAssembleComponentVersionId(componentVersion.getId());
            ComponentVersion baseComponentVersion = getService(ComponentVersionService.class).getByID(construct.getBaseComponentVersionId());
            if (ConstantVar.Component.Type.TAB.equals(baseComponentVersion.getComponent().getType())) {
                List<ConstructInputParam> constructInputParamList = getService(ConstructInputParamService.class).getByConstructId(construct.getId());
                List<Object[]> inputParamList = new ArrayList<Object[]>();
                if (CollectionUtils.isNotEmpty(constructInputParamList)) {
                    Object[] inputParam = null;
                    for (ConstructInputParam constructInputParam : constructInputParamList) {
                        inputParam = new Object[3];
                        inputParam[0] = constructInputParam.getId();
                        inputParam[1] = constructInputParam.getName();
                        inputParam[2] = "";
                        inputParamList.add(inputParam);
                    }
                    if (CollectionUtils.isNotEmpty(inputParamList)) {
                        List<String[]> constructFunctionList = getService().getConstructFunctionList(constructDetailId);
                        if (CollectionUtils.isNotEmpty(constructFunctionList)) {
                            for (Iterator<Object[]> it = inputParamList.iterator(); it.hasNext();) {
                                inputParam = it.next();
                                String inputParamName = StringUtil.null2empty(inputParam[1]);
                                for (String[] constructFunction : constructFunctionList) {
                                    if (inputParamName.equals(constructFunction[1])) {
                                        it.remove();
                                    }
                                }
                            }
                        }
                    }
                }
                setReturnData(inputParamList);
            } else {
                setReturnData(getService().getInputParamList(construct.getBaseComponentVersionId(), constructDetailId));
            }
        } else {
            setReturnData(getService().getInputParamList(componentVersionId, constructDetailId));
        }
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 获取方法出参和构件入参绑定关系列表数据
     * 
     * @return Object
     */
    public Object getConstructFunctionList() {
        String constructDetailId = getParameter("constructDetailId");
        setReturnData(getService().getConstructFunctionList(constructDetailId));
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 保存预留区绑定的构件和页面方法的绑定关系
     * 
     * @return Object
     */
    public Object saveConstructFunction() {
        try {
            String rowIds = getParameter("rowIds");
            String constructDetailId = getParameter("constructDetailId");
            getService().saveConstructFunction(rowIds, constructDetailId);
            // 缓存
            ComponentParamsUtil.removeParamFunctions(constructDetailId);
            List<String[]> constructFunctionList = getService().getConstructFunctionList(constructDetailId);
            ComponentParamsUtil.putParamFunctions(constructDetailId, ComponentParamsUtil.parseParamFunctions(constructFunctionList));
            setReturnData("{'success':true}");
        } catch (Exception e) {
            setReturnData("{'success':false}");
            setMessage(e.toString());
        }
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 删除预留区绑定的构件和页面方法的绑定关系
     * 
     * @return Object
     */
    public Object deleteConstructFunctions() {
        try {
            String constructDetailId = getParameter("constructDetailId");
            getService().deleteConstructFunctions(constructDetailId);
            setReturnData("{'success':true}");
        } catch (Exception e) {
            setReturnData("{'success':false}");
            setMessage(e.toString());
        }
        return new DefaultHttpHeaders("success").disableCaching();
    }
}