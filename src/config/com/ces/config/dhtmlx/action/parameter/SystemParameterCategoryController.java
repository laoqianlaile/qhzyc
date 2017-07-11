package com.ces.config.dhtmlx.action.parameter;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.parameter.SystemParameterCategoryDao;
import com.ces.config.dhtmlx.entity.menu.Menu;
import com.ces.config.dhtmlx.entity.parameter.SystemParameter;
import com.ces.config.dhtmlx.entity.parameter.SystemParameterCategory;
import com.ces.config.dhtmlx.json.entity.common.DhtmlxTreeNode;
import com.ces.config.dhtmlx.service.menu.MenuService;
import com.ces.config.dhtmlx.service.parameter.SystemParameterCategoryService;
import com.ces.config.dhtmlx.service.parameter.SystemParameterService;
import com.ces.config.utils.ConstantVar;
import com.ces.xarch.core.exception.FatalException;
import com.google.common.collect.Lists;

/**
 * 系统参数类别Controller
 * 
 * @author wanglei
 * @date 2013-08-12
 */
public class SystemParameterCategoryController
        extends
        ConfigDefineServiceDaoController<SystemParameterCategory, SystemParameterCategoryService, SystemParameterCategoryDao> {

    private static final long serialVersionUID = 8779572375141586653L;

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new SystemParameterCategory());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     * StringIDService)
     */
    @Autowired
    @Qualifier("systemParameterCategoryService")
    @Override
    protected void setService(SystemParameterCategoryService service) {
        super.setService(service);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController#create()
     */
    @Override
    public Object create() throws FatalException {
        model.setHasChild(false);
        model = getService().save(model);
        if (!"-1".endsWith(model.getParentId())) {
            SystemParameterCategory systemParameterCategory = getService().getByID(model.getParentId());
            if (null !=systemParameterCategory && !systemParameterCategory.getHasChild()) {
                systemParameterCategory.setHasChild(true);
                getService().save(systemParameterCategory);
            }
        }
        return SUCCESS;
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#destroy()
     */
    @Override
    public Object destroy() throws FatalException {
        List<SystemParameterCategory> categoryList = getService().getCategoryListByParentId(model.getId());
        List<SystemParameter> systemParameterList = getService(SystemParameterService.class).getByCategoryId(
                model.getId());
        boolean flag = false;
        String message = null;
        if (CollectionUtils.isNotEmpty(categoryList)) {
            message = "该分类下存在子分类，不能删除";
        } else if (CollectionUtils.isNotEmpty(systemParameterList)) {
            message = "该分类下存在系统参数，不能删除";
        } else {
            String parentId = getService().getByID(model.getId()).getParentId();
            getService().delete(getId());
            List<SystemParameterCategory> childList = getService().getCategoryListByParentId(parentId);
            if (CollectionUtils.isEmpty(childList)) {
                SystemParameterCategory parent = getService().getByID(parentId);
                if( null != parent ) {
                	parent.setHasChild(false);
                    getService().save(parent);
                }
            }
            flag = true;
        }
        setReturnData("{'success':" + flag + ", 'message':'" + message + "'}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 校验字段是否已经存在
     * 
     * @return Object
     */
    public Object validateFields() {
        SystemParameterCategory category = (SystemParameterCategory) getModel();
        SystemParameterCategory temp = getService().getByNameAndParentId(category.getName(), category.getParentId());
        boolean nameExist = false;
        if (null != category.getId() && !"".equals(category.getId())) {
            SystemParameterCategory oldCategory = getService().getByID(category.getId());
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
    
    /***
     * 获取参数类型树
	 * @return Object
     */
    public Object tree() {
        list = getDataModel(getModelTemplate());
        processFilter(list);
        String treeNodeId = getId();
        List<DhtmlxTreeNode> treeNodelist = Lists.newArrayList();
        DhtmlxTreeNode treeNode = null;
        if ("-1".equals(treeNodeId)) {
        	treeNode = new DhtmlxTreeNode();
            treeNode.setId("C_-1");
            treeNode.setText("公共设置");
            treeNode.setIm0(ConstantVar.IconTreeNode.LEAF);
            treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
            treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
            treeNode.setChild("1");
            treeNode.setOpen(false);
            treeNodelist.add(treeNode);
            List<Menu> menuList = getService(MenuService.class).getMenuByParentId("-1");
            for(Iterator<Menu> iterator = menuList.iterator();iterator.hasNext();) {
            	Menu menu = iterator.next();
            	if(menu.getId().equals("sys_0")) {
            		iterator.remove();
            		break;
            	}
            }
            if( CollectionUtils.isNotEmpty(menuList) ) {
                for(Menu m : menuList ) {
                	treeNode = new DhtmlxTreeNode();
                    treeNode.setId(m.getId());
                    treeNode.setText(m.getName());
                    treeNode.setIm0(ConstantVar.IconTreeNode.LEAF);
                    treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                    treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                    treeNode.setChild("1");
                    treeNode.setOpen(false);
                    treeNodelist.add(treeNode);
                }
            }
        }else {
        	if( treeNodeId.indexOf("C_-1") !=-1) {
        		treeNodeId = treeNodeId.substring(2);
        	}
    		List<SystemParameterCategory> list  = getService().getCategoryListByParentId(treeNodeId);
        	if(CollectionUtils.isNotEmpty(list)) {
        		for( SystemParameterCategory s : list ) {
        			treeNode = new DhtmlxTreeNode();
                    treeNode.setId(s.getId());
                    treeNode.setText(s.getName());
                    treeNode.setIm0(ConstantVar.IconTreeNode.LEAF);
                    treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                    treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                    treeNode.setChild(s.getHasChild()?"1":"0");
                    treeNode.setOpen(false);
                    treeNodelist.add(treeNode);
        		}
        	}
        }
        list.setData(treeNodelist);
        return new DefaultHttpHeaders("success").disableCaching();
    }

}
