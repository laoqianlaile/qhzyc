package com.ces.component.pagedemo.dhtmlx.action;

import java.util.List;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.component.pagedemo.dhtmlx.dao.PageDemoDao;
import com.ces.component.pagedemo.dhtmlx.entity.PageDemo;
import com.ces.component.pagedemo.dhtmlx.service.PageDemoService;
import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.xarch.core.exception.FatalException;

public class PageDemoController extends
        ConfigDefineServiceDaoController<PageDemo, PageDemoService, PageDemoDao> {

    private static final long serialVersionUID = -8037122071030457027L;

    /*
     * (non-Javadoc)
     * 
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new PageDemo());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * 
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     *      StringIDService)
     */
    @Autowired
    @Qualifier("pageDemoService")
    @Override
    protected void setService(PageDemoService service) {
        super.setService(service);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController#create()
     */
    @Override
    public Object create() throws FatalException {
        Integer maxShowOrder = getService().getMaxShowOrder(model.getModuleId());
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
        PageDemo startPageDemo = getService().getByID(start);
        PageDemo endPageDemo = getService().getByID(end);
        if (startPageDemo.getShowOrder().intValue() > endPageDemo.getShowOrder().intValue()) {
            // 向上
            List<PageDemo> pageDemoList = getService().getPageDemoListByShowOrder(
                    endPageDemo.getShowOrder(), startPageDemo.getShowOrder(),
                    startPageDemo.getModuleId());
            startPageDemo.setShowOrder(endPageDemo.getShowOrder());
            getService().save(startPageDemo);
            for (PageDemo pageDemo : pageDemoList) {
                if (pageDemo.getId().equals(startPageDemo.getId())) {
                    continue;
                }
                pageDemo.setShowOrder(pageDemo.getShowOrder() + 1);
                getService().save(pageDemo);
            }
        } else {
            // 向下
            List<PageDemo> pageDemoList = getService().getPageDemoListByShowOrder(
                    startPageDemo.getShowOrder(), endPageDemo.getShowOrder(),
                    startPageDemo.getModuleId());
            startPageDemo.setShowOrder(endPageDemo.getShowOrder());
            getService().save(startPageDemo);
            for (PageDemo pageDemo : pageDemoList) {
                if (pageDemo.getId().equals(startPageDemo.getId())) {
                    continue;
                }
                pageDemo.setShowOrder(pageDemo.getShowOrder() - 1);
                getService().save(pageDemo);
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
        PageDemo pageDemo = (PageDemo) getModel();
        PageDemo temp = getService().getPageDemoByName(pageDemo.getName());
        boolean nameExist = false;
        if (null != pageDemo.getId() && !"".equals(pageDemo.getId())) {
            PageDemo oldPageDemo = getService().getByID(pageDemo.getId());
            if (null != temp && null != oldPageDemo && !temp.getId().equals(oldPageDemo.getId())) {
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
