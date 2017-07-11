package com.ces.component.moduledemo.dhtmlx.action;

import java.util.List;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.component.moduledemo.dhtmlx.dao.ModuleDemoDao;
import com.ces.component.moduledemo.dhtmlx.entity.ModuleDemo;
import com.ces.component.moduledemo.dhtmlx.service.ModuleDemoService;
import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.xarch.core.exception.FatalException;

public class ModuleDemoController extends
        ConfigDefineServiceDaoController<ModuleDemo, ModuleDemoService, ModuleDemoDao> {

    private static final long serialVersionUID = -8037122071030457027L;

    /*
     * (non-Javadoc)
     * 
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new ModuleDemo());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * 
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     *      StringIDService)
     */
    @Autowired
    @Qualifier("moduleDemoService")
    @Override
    protected void setService(ModuleDemoService service) {
        super.setService(service);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController#create()
     */
    @Override
    public Object create() throws FatalException {
        Integer maxShowOrder = getService().getMaxShowOrder(model.getCategoryId());
        int showOrder = 0;
        if (maxShowOrder == null) {
            showOrder = 1;
        } else {
            showOrder = maxShowOrder + 1;
        }
        model.setShowOrder(showOrder);
        model = getService().save(model);
        return SUCCESS;
    }

    /**
     * 排序
     * 
     * @return Object
     */
    public Object sort() {
        String start = getParameter("start");
        String end = getParameter("end");
        ModuleDemo startModuleDemo = getService().getByID(start);
        ModuleDemo endModuleDemo = getService().getByID(end);
        if (startModuleDemo.getShowOrder().intValue() > endModuleDemo.getShowOrder().intValue()) {
            // 向上
            List<ModuleDemo> moduleDemoList = getService().getModuleDemoListByShowOrder(
                    endModuleDemo.getShowOrder(), startModuleDemo.getShowOrder(),
                    startModuleDemo.getCategoryId());
            startModuleDemo.setShowOrder(endModuleDemo.getShowOrder());
            getService().save(startModuleDemo);
            for (ModuleDemo moduleDemo : moduleDemoList) {
                if (moduleDemo.getId().equals(startModuleDemo.getId())) {
                    continue;
                }
                moduleDemo.setShowOrder(moduleDemo.getShowOrder() + 1);
                getService().save(moduleDemo);
            }
        } else {
            // 向下
            List<ModuleDemo> moduleDemoList = getService().getModuleDemoListByShowOrder(
                    startModuleDemo.getShowOrder(), endModuleDemo.getShowOrder(),
                    startModuleDemo.getCategoryId());
            startModuleDemo.setShowOrder(endModuleDemo.getShowOrder());
            getService().save(startModuleDemo);
            for (ModuleDemo moduleDemo : moduleDemoList) {
                if (moduleDemo.getId().equals(startModuleDemo.getId())) {
                    continue;
                }
                moduleDemo.setShowOrder(moduleDemo.getShowOrder() - 1);
                getService().save(moduleDemo);
            }
        }
        setReturnData("排序成功");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 校验字段是否已经存在
     * 
     * @return Object
     */
    public Object validateFields() {
        ModuleDemo moduleDemo = (ModuleDemo) getModel();
        ModuleDemo temp = getService().getModuleDemoByName(moduleDemo.getName());
        boolean nameExist = false;
        if (null != moduleDemo.getId() && !"".equals(moduleDemo.getId())) {
            ModuleDemo oldModuleDemo = getService().getByID(moduleDemo.getId());
            if (null != temp && null != oldModuleDemo && !temp.getId().equals(oldModuleDemo.getId())) {
                nameExist = true;
            }
        } else {
            if (null != temp) {
                nameExist = true;
            }
        }
        setReturnData("{'nameExist' : " + nameExist + "}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
}
