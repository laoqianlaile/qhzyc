package com.ces.config.dhtmlx.action.label;

import java.util.List;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.label.ColumnLabelDao;
import com.ces.config.dhtmlx.entity.label.ColumnLabel;
import com.ces.config.dhtmlx.service.label.ColumnLabelService;
import com.ces.xarch.core.exception.FatalException;

public class ColumnLabelController extends ConfigDefineServiceDaoController<ColumnLabel, ColumnLabelService, ColumnLabelDao> {

    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new ColumnLabel());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     * StringIDService)
     */
    @Autowired
    @Qualifier("columnLabelService")
    @Override
    protected void setService(ColumnLabelService service) {
        super.setService(service);
    }

    /*
     * (non-Javadoc)
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
     * 校验字段是否已经存在
     * 
     * @return Object
     */
    public Object validateFields() {
        ColumnLabel columnLabel = (ColumnLabel) getModel();
        ColumnLabel temp1 = getService().getByName(columnLabel.getName());
        ColumnLabel temp2 = getService().getByCode(columnLabel.getCode());
        boolean nameExist = false;
        boolean codeExist = false;
        if (null != columnLabel.getId() && !"".equals(columnLabel.getId())) {
            ColumnLabel oldColumnLabel = getService().getByID(columnLabel.getId());
            if (null != temp1 && null != oldColumnLabel && !temp1.getId().equals(oldColumnLabel.getId())) {
                nameExist = true;
            }
            if (null != temp2 && null != oldColumnLabel && !temp2.getId().equals(oldColumnLabel.getId())) {
                codeExist = true;
            }
        } else {
            if (null != temp1) {
                nameExist = true;
            }
            if (null != temp2) {
                codeExist = true;
            }
        }
        setReturnData("{'nameExist' : " + nameExist + ", 'codeExist' : " + codeExist + "}");
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
        String categoryId = getParameter("categoryId");
        ColumnLabel startColumnLabel = getService().getByID(start);
        ColumnLabel endColumnLabel = getService().getByID(end);
        if (startColumnLabel.getShowOrder().intValue() > endColumnLabel.getShowOrder().intValue()) {
            // 向上
            List<ColumnLabel> columnLabelList = getService().getColumnLabelListByShowOrder(endColumnLabel.getShowOrder(), startColumnLabel.getShowOrder(),
                    categoryId);
            startColumnLabel.setShowOrder(endColumnLabel.getShowOrder());
            getService().save(startColumnLabel);
            for (ColumnLabel columnLabel : columnLabelList) {
                if (columnLabel.getId().equals(startColumnLabel.getId())) {
                    continue;
                }
                columnLabel.setShowOrder(columnLabel.getShowOrder() + 1);
                getService().save(columnLabel);
            }
        } else {
            // 向下
            List<ColumnLabel> columnLabelList = getService().getColumnLabelListByShowOrder(startColumnLabel.getShowOrder(), endColumnLabel.getShowOrder(),
                    categoryId);
            startColumnLabel.setShowOrder(endColumnLabel.getShowOrder());
            getService().save(startColumnLabel);
            for (ColumnLabel columnLabel : columnLabelList) {
                if (columnLabel.getId().equals(startColumnLabel.getId())) {
                    continue;
                }
                columnLabel.setShowOrder(columnLabel.getShowOrder() - 1);
                getService().save(columnLabel);
            }
        }
        setReturnData("排序成功");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 更改构件分类
     * 
     * @return Object
     */
    public Object changeCategory() {
        String columnLabelIds = getParameter("columnLabelIds");
        String categoryId = getParameter("categoryId");
        String[] labelIds = columnLabelIds.split(",");
        getService().changeCategory(categoryId, labelIds);
        setReturnData("更改分类成功！");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 剔除本表已经绑定的字段标签
     * 
     * @return
     */
    public Object getUnBindedLabel() {
        String tableId = getParameter("tableId");
        setReturnData(getService().getUnBindedLabel(tableId));
        return new DefaultHttpHeaders("success").disableCaching();
    }
}
