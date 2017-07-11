package com.ces.config.dhtmlx.action.construct;

import java.util.List;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.construct.ConstructCallbackDao;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.entity.construct.Construct;
import com.ces.config.dhtmlx.entity.construct.ConstructCallback;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.dhtmlx.service.construct.ConstructCallbackService;
import com.ces.config.dhtmlx.service.construct.ConstructService;
import com.ces.config.utils.ComponentParamsUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.xarch.core.exception.FatalException;

/**
 * 预留区绑定的构件和回调函数的绑定关系Controller
 * 
 * @author wanglei
 * @date 2013-09-28
 */
public class ConstructCallbackController extends ConfigDefineServiceDaoController<ConstructCallback, ConstructCallbackService, ConstructCallbackDao> {

    private static final long serialVersionUID = 5448108829911125571L;

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new ConstructCallback());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     * StringIDService)
     */
    @Autowired
    @Qualifier("constructCallbackService")
    @Override
    protected void setService(ConstructCallbackService service) {
        super.setService(service);
    }

    /**
     * 获取回调函数参数列表数据
     * 
     * @return Object
     * @throws FatalException
     */
    public Object getCallbackParamList() throws FatalException {
        String constructDetailId = getParameter("constructDetailId");
        String componentVersionId = getParameter("componentVersionId");
        setReturnData(getService().getCallbackParamList(componentVersionId, constructDetailId));
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 获取预留区绑定的构件的出参列表数据
     * 
     * @return Object
     * @throws FatalException
     */
    public Object getOutputParamList() throws FatalException {
        String componentVersionId = getParameter("componentVersionId");
        ComponentVersion componentVersion = getService(ComponentVersionService.class).getByID(componentVersionId);
        if (ConstantVar.Component.Type.ASSEMBLY.equals(componentVersion.getComponent().getType())) {
            Construct construct = getService(ConstructService.class).getByAssembleComponentVersionId(componentVersion.getId());
            componentVersionId = construct.getBaseComponentVersionId();
        }
        setReturnData(getService().getOutputParamList(componentVersionId));
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 获取回调函数参数和构件出参绑定关系列表数据
     * 
     * @return Object
     * @throws FatalException
     */
    public Object getConstructCallbackList() throws FatalException {
        String constructDetailId = getParameter("constructDetailId");
        setReturnData(getService().getConstructCallbackList(constructDetailId));
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 保存预留区绑定的构件和回调函数的绑定关系
     * 
     * @return Object
     */
    public Object saveConstructCallback() {
        try {
            String rowIds = getParameter("rowIds");
            String constructDetailId = getParameter("constructDetailId");
            getService().saveConstructCallback(rowIds, constructDetailId);
            // 缓存
            ComponentParamsUtil.removeParamCallbacks(constructDetailId);
            List<String[]> constructCallbackList = getService().getConstructCallbackList(constructDetailId);
            ComponentParamsUtil.putParamCallbacks(constructDetailId, ComponentParamsUtil.parseParamCallbacks(constructCallbackList));
            setReturnData("{'success':true}");
        } catch (Exception e) {
            setReturnData("{'success':false}");
            setMessage(e.toString());
        }
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 删除预留区绑定的构件和回调函数的绑定关系
     * 
     * @return Object
     */
    public Object deleteConstructCallbacks() {
        try {
            String constructDetailId = getParameter("constructDetailId");
            getService().deleteConstructCallbacks(constructDetailId);
            setReturnData("{'success':true}");
        } catch (Exception e) {
            setReturnData("{'success':false}");
            setMessage(e.toString());
        }
        return new DefaultHttpHeaders("success").disableCaching();
    }
}