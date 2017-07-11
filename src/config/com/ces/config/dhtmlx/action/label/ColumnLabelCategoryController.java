package com.ces.config.dhtmlx.action.label;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.label.ColumnLabelCategoryDao;
import com.ces.config.dhtmlx.entity.label.ColumnLabelCategory;
import com.ces.config.dhtmlx.json.entity.common.DhtmlxTreeNode;
import com.ces.config.dhtmlx.service.label.ColumnLabelCategoryService;
import com.ces.config.utils.ConstantVar;
import com.ces.xarch.core.exception.FatalException;
import com.google.common.collect.Lists;

/**
 * 字段标签分类Controller
 * 
 * @author wanglei
 * @date 2013-07-15
 */
public class ColumnLabelCategoryController extends ConfigDefineServiceDaoController<ColumnLabelCategory, ColumnLabelCategoryService, ColumnLabelCategoryDao> {

    private static final long serialVersionUID = -8339780449025617169L;

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new ColumnLabelCategory());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     * StringIDService)
     */
    @Autowired
    @Qualifier("columnLabelCategoryService")
    @Override
    protected void setService(ColumnLabelCategoryService service) {
        super.setService(service);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController#create()
     */
    @Override
    public Object create() throws FatalException {
        Integer maxShowOrder = getService().getMaxShowOrder();
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

    /*
     * (non-Javadoc)
     * 字段标签分类树
     * @see com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController#tree()
     */
    public Object tree() {
        String view = getParameter("view");
        String menuId = getParameter("menuId");
        list = getDataModel(getModelTemplate());
        processFilter(list);
        String treeId = getId();
        List<DhtmlxTreeNode> treeNodelist = Lists.newArrayList();
        DhtmlxTreeNode treeNode = null;
        if ("-1".equals(treeId)) {
            List<ColumnLabelCategory> columnLabelCategoryList = getService().getByMenuId(menuId);
            if (CollectionUtils.isNotEmpty(columnLabelCategoryList)) {
                for (ColumnLabelCategory columnLabelCategory : columnLabelCategoryList) {
                    treeNode = new DhtmlxTreeNode();
                    treeNode.setId(columnLabelCategory.getId());
                    treeNode.setText(columnLabelCategory.getName());
                    treeNode.setIm0(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                    treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                    treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                    treeNode.setChild("0");
                    treeNodelist.add(treeNode);
                }
            }
        }
        list.setData(treeNodelist);
        if (!"dhtmlx".equals(view)) {
            setReturnData(list.getData());
        }
        return new DefaultHttpHeaders("list").disableCaching();
    }

    /**
     * 校验字段是否已经存在
     * 
     * @return Object
     */
    public Object validateFields() {
        ColumnLabelCategory columnLabelCategory = (ColumnLabelCategory) getModel();
        ColumnLabelCategory temp = getService().getColumnLabelCategoryByName(columnLabelCategory.getName());
        boolean nameExist = false;
        boolean cannotUpdateIsBusiness = false;
        if (null != columnLabelCategory.getId() && !"".equals(columnLabelCategory.getId())) {
            ColumnLabelCategory oldColumnLabelCategory = getService().getByID(columnLabelCategory.getId());
            if (null != temp && null != oldColumnLabelCategory && !temp.getId().equals(oldColumnLabelCategory.getId())) {
                nameExist = true;
            }
        } else {
            if (null != temp) {
                nameExist = true;
            }
        }
        setReturnData("{'nameExist' : " + nameExist + ", 'cannotUpdateIsBusiness' : " + cannotUpdateIsBusiness + "}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 排序
     * 
     * @return Object
     */
    public Object sort() {
        String start = getParameter("start");
        String end = getParameter("end");
        String menuId = getParameter("menuId");
        ColumnLabelCategory startColumnLabelCategory = getService().getByID(start);
        ColumnLabelCategory endColumnLabelCategory = getService().getByID(end);
        if (startColumnLabelCategory.getShowOrder().intValue() > endColumnLabelCategory.getShowOrder().intValue()) {
            // 向上
            List<ColumnLabelCategory> columnLabelCategoryList = getService().getByShowOrder(endColumnLabelCategory.getShowOrder(),
                    startColumnLabelCategory.getShowOrder(), menuId);
            startColumnLabelCategory.setShowOrder(endColumnLabelCategory.getShowOrder());
            getService().save(startColumnLabelCategory);
            for (ColumnLabelCategory columnLabelCategory : columnLabelCategoryList) {
                if (columnLabelCategory.getId().equals(startColumnLabelCategory.getId())) {
                    continue;
                }
                columnLabelCategory.setShowOrder(columnLabelCategory.getShowOrder() + 1);
                getService().save(columnLabelCategory);
            }
        } else {
            // 向下
            List<ColumnLabelCategory> columnLabelCategoryList = getService().getByShowOrder(startColumnLabelCategory.getShowOrder(),
                    endColumnLabelCategory.getShowOrder(), menuId);
            startColumnLabelCategory.setShowOrder(endColumnLabelCategory.getShowOrder());
            getService().save(startColumnLabelCategory);
            for (ColumnLabelCategory columnLabelCategory : columnLabelCategoryList) {
                if (columnLabelCategory.getId().equals(startColumnLabelCategory.getId())) {
                    continue;
                }
                columnLabelCategory.setShowOrder(columnLabelCategory.getShowOrder() - 1);
                getService().save(columnLabelCategory);
            }
        }
        setReturnData("排序成功");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
}