package com.ces.config.dhtmlx.action.label;

import java.util.List;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.label.TypeLabelDao;
import com.ces.config.dhtmlx.entity.label.TypeLabel;
import com.ces.config.dhtmlx.service.label.TypeLabelService;
import com.ces.xarch.core.exception.FatalException;
import com.ces.xarch.core.logger.Logger;

public class TypeLabelController extends ConfigDefineServiceDaoController<TypeLabel, TypeLabelService, TypeLabelDao> {

    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new TypeLabel());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     * StringIDService)
     */
    @Autowired
    @Qualifier("typeLabelService")
    @Override
    protected void setService(TypeLabelService service) {
        super.setService(service);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController#create()
     */
    @Override
    @Logger(action = "添加", logger = "${code}")
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
     * @see com.ces.xarch.core.web.struts2.BaseController#destroy()
     */
    @Override
    public Object destroy() throws FatalException {
        getService().delete(getId());
        setReturnData("{'success':true}");
        return SUCCESS;
    }

    /**
     * 校验分类是否已经存在
     * 
     * @return Object
     */
    public Object validateFields() {
        TypeLabel TypeLabel = (TypeLabel) getModel();
        TypeLabel temp1 = getService().getTypeLabelByName(TypeLabel.getName());
        TypeLabel temp2 = getService().getTypeLabelByCode(TypeLabel.getCode());
        boolean nameExist = false;
        boolean codeExist = false;
        if (null != TypeLabel.getId() && !"".equals(TypeLabel.getId())) {
            TypeLabel oldTypeLabel = getService().getByID(TypeLabel.getId());
            if (null != temp1 && null != oldTypeLabel && !temp1.getId().equals(oldTypeLabel.getId())) {
                nameExist = true;
            }
            if (null != temp2 && null != oldTypeLabel && !temp2.getId().equals(oldTypeLabel.getId())) {
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
        String menuId = getParameter("menuId");
        TypeLabel startTypeLabel = getService().getByID(start);
        TypeLabel endTypeLabel = getService().getByID(end);
        if (startTypeLabel.getShowOrder().intValue() > endTypeLabel.getShowOrder().intValue()) {
            // 向上
            List<TypeLabel> TypeLabelList = getService().getTypeLabelListByShowOrder(endTypeLabel.getShowOrder(), startTypeLabel.getShowOrder(), menuId);
            startTypeLabel.setShowOrder(endTypeLabel.getShowOrder());
            getService().save(startTypeLabel);
            for (TypeLabel TypeLabel : TypeLabelList) {
                if (TypeLabel.getId().equals(startTypeLabel.getId())) {
                    continue;
                }
                TypeLabel.setShowOrder(TypeLabel.getShowOrder() + 1);
                getService().save(TypeLabel);
            }
        } else {
            // 向下
            List<TypeLabel> TypeLabelList = getService().getTypeLabelListByShowOrder(startTypeLabel.getShowOrder(), endTypeLabel.getShowOrder(), menuId);
            startTypeLabel.setShowOrder(endTypeLabel.getShowOrder());
            getService().save(startTypeLabel);
            for (TypeLabel TypeLabel : TypeLabelList) {
                if (TypeLabel.getId().equals(startTypeLabel.getId())) {
                    continue;
                }
                TypeLabel.setShowOrder(TypeLabel.getShowOrder() - 1);
                getService().save(TypeLabel);
            }
        }
        setReturnData("排序成功");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

}
