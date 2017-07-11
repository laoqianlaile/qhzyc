package com.ces.component.moduledemo.dhtmlx.action;

import java.util.List;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.component.moduledemo.dhtmlx.dao.ModuleCategoryDao;
import com.ces.component.moduledemo.dhtmlx.entity.ModuleCategory;
import com.ces.component.moduledemo.dhtmlx.entity.ModuleDemo;
import com.ces.component.moduledemo.dhtmlx.service.ModuleCategoryService;
import com.ces.component.moduledemo.dhtmlx.service.ModuleDemoService;
import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.xarch.core.exception.FatalException;

public class ModuleCategoryController extends
        ConfigDefineServiceDaoController<ModuleCategory, ModuleCategoryService, ModuleCategoryDao> {

    private static final long serialVersionUID = 8779572375141586653L;

    /*
     * (non-Javadoc)
     * 
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new ModuleCategory());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * 
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     *      StringIDService)
     */
    @Autowired
    @Qualifier("moduleCategoryService")
    @Override
    protected void setService(ModuleCategoryService service) {
        super.setService(service);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController#create()
     */
    @Override
    public Object create() throws FatalException {
        model.setHasChild(false);
        model = getService().save(model);
        if (!"-1".equals(model.getParentId())) {
            ModuleCategory moduleCategory = getService().getByID(model.getParentId());
            moduleCategory.setHasChild(true);
            getService().save(moduleCategory);
        }
        return SUCCESS;
    }

    /**
     * 校验字段是否已经存在
     * 
     * @return Object
     */
    public Object validateFields() {
        ModuleCategory category = (ModuleCategory) getModel();
        ModuleCategory temp = getService().getByNameAndParentId(category.getName(), category.getParentId());
        boolean nameExist = false;
        if (null != category.getId() && !"".equals(category.getId())) {
            ModuleCategory oldCategory = getService().getByID(category.getId());
            if (null != temp && null != oldCategory && !temp.getId().equals(oldCategory.getId())) {
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

    /*
     * (non-Javadoc)
     * 
     * @see com.ces.xarch.core.web.struts2.BaseController#destroy()
     */
    @Override
    public Object destroy() throws FatalException {
        List<ModuleCategory> categoryList = getService().getCategoryListByParentId(model.getId());
        List<ModuleDemo> moduleDemoList = getService(ModuleDemoService.class).getByCategoryId(model.getId());
        if (categoryList != null && !categoryList.isEmpty()) {
            setMessage("该分类下存在子分类，不能删除");
        } else if (moduleDemoList != null && !moduleDemoList.isEmpty()) {
            setMessage("该分类下存在ModuleDemo，不能删除");
        } else {
            String parentId = getService().getByID(model.getId()).getParentId();
            getService().delete(getId());
            List<ModuleCategory> childList = getService().getCategoryListByParentId(parentId);
            if (childList == null || childList.isEmpty()) {
                ModuleCategory parent = getService().getByID(parentId);
                parent.setHasChild(false);
                getService().save(parent);
            }
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
}
