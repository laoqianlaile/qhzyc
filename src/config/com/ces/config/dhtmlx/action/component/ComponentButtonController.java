package com.ces.config.dhtmlx.action.component;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.component.ComponentButtonDao;
import com.ces.config.dhtmlx.entity.component.ComponentButton;
import com.ces.config.dhtmlx.service.component.ComponentButtonService;

/**
 * 构件按钮Controller
 * 
 * @author wanglei
 * @date 2014-07-31
 */
public class ComponentButtonController extends ConfigDefineServiceDaoController<ComponentButton, ComponentButtonService, ComponentButtonDao> {

    private static final long serialVersionUID = 5448108829911125571L;

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new ComponentButton());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     * StringIDService)
     */
    @Autowired
    @Qualifier("componentButtonService")
    @Override
    protected void setService(ComponentButtonService service) {
        super.setService(service);
    }

    /**
     * 获取构件中的按钮
     * 
     * @return Object
     */
    public Object getButtonsOfComponent() {
        String componentVersionId = getParameter("componentVersionId");
        List<ComponentButton> componentButtonList = getService().getByComponentVersionId(componentVersionId);
        List<String[]> list = new ArrayList<String[]>();
        if (CollectionUtils.isNotEmpty(componentButtonList)) {
            String[] strs = null;
            for (ComponentButton componentButton : componentButtonList) {
                strs = new String[3];
                strs[0] = componentButton.getId();
                strs[1] = componentButton.getId();
                strs[2] = componentButton.getDisplayName();
                list.add(strs);
            }
        }
        setReturnData(list);
        return new DefaultHttpHeaders("success").disableCaching();
    }
}
