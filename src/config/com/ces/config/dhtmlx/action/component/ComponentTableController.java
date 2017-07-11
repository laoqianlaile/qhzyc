package com.ces.config.dhtmlx.action.component;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.component.ComponentTableDao;
import com.ces.config.dhtmlx.entity.component.ComponentConfig;
import com.ces.config.dhtmlx.entity.component.ComponentTable;
import com.ces.config.dhtmlx.service.component.ComponentTableService;

/**
 * 构件相关表Controller
 * 
 * @author wanglei
 * @date 2013-08-16
 */
public class ComponentTableController extends
        ConfigDefineServiceDaoController<ComponentTable, ComponentTableService, ComponentTableDao> {

    private static final long serialVersionUID = -8037122071030457027L;

    /*
     * (non-Javadoc)
     * 
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new ComponentTable());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * 
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     *      StringIDService)
     */
    @Autowired
    @Qualifier("componentTableService")
    @Override
    protected void setService(ComponentTableService service) {
        super.setService(service);
    }

    /**
     * 获取构件关联的表
     * 
     * @return Object
     */
    public Object getComponentTables() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String componentConfigKey = request.getParameter("componentConfigKey");
        ComponentConfig componentConfig = (ComponentConfig) request.getSession().getAttribute(componentConfigKey);

        if (componentConfig == null) {
            setReturnData("上传失败");
        } else {
            list = getDataModel(getModelTemplate());
            processFilter(list);
            list.setData(componentConfig.getComponentTables());
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 根据名称获取构件相关表
     * 
     * @return Object
     */
    public Object getComponentTableByName() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String tableName = request.getParameter("tableName");
        ComponentTable componentTable = getService().getComponentTableByName(tableName);
        if (componentTable == null) {
            setReturnData("{'exist':false, 'message':'该表没有冲突'}");
        } else {
            setReturnData("{'id':'" + componentTable.getId() + "', 'exist':true}");
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
}
