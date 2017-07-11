package com.ces.config.dhtmlx.action.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.menu.MenuInputParamDao;
import com.ces.config.dhtmlx.entity.menu.MenuInputParam;
import com.ces.config.dhtmlx.service.menu.MenuInputParamService;

/**
 * 菜单绑定构件的入参Controller
 * 
 * @author wanglei
 * @date 2014-09-11
 */
public class MenuInputParamController extends
        ConfigDefineServiceDaoController<MenuInputParam, MenuInputParamService, MenuInputParamDao> {

    private static final long serialVersionUID = 5448108829911125571L;

    /*
     * (non-Javadoc)
     * 
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new MenuInputParam());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * 
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     *      StringIDService)
     */
    @Autowired
    @Qualifier("menuInputParamService")
    @Override
    protected void setService(MenuInputParamService service) {
        super.setService(service);
    }

    /**
     * 获取构件入参列表数据
     * 
     * @return Object
     */
    public Object getInputParamList() {
        String menuId = getParameter("menuId");
        List<Object[]> list = new ArrayList<Object[]>();
        List<Object[]> inputParamList = getService().getInputParamList(menuId);
        if (CollectionUtils.isNotEmpty(inputParamList)) {
            Object[] obj = null;
            for (Object[] inputParam : inputParamList) {
                obj = new Object[5];
                obj[0] = inputParam[0];
                obj[1] = inputParam[0];
                obj[2] = inputParam[1];
                obj[3] = inputParam[2];
                obj[4] = inputParam[3];
                list.add(obj);
            }
        }
        setReturnData(list);
        return new DefaultHttpHeaders("success").disableCaching();
    }

}