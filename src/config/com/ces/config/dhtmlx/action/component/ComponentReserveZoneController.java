package com.ces.config.dhtmlx.action.component;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.component.ComponentReserveZoneDao;
import com.ces.config.dhtmlx.entity.component.ComponentReserveZone;
import com.ces.config.dhtmlx.service.component.ComponentReserveZoneService;
import com.ces.config.utils.StringUtil;

/**
 * 构件预留区Controller
 * 
 * @author wanglei
 * @date 2013-08-08
 */
public class ComponentReserveZoneController extends
        ConfigDefineServiceDaoController<ComponentReserveZone, ComponentReserveZoneService, ComponentReserveZoneDao> {

    private static final long serialVersionUID = -132561819688171083L;

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new ComponentReserveZone());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     * StringIDService)
     */
    @Autowired
    @Qualifier("componentReserveZoneService")
    @Override
    protected void setService(ComponentReserveZoneService service) {
        super.setService(service);
    }

    /**
     * 获取构件下的预留区，排除个别预留区
     * 
     * @return Object
     */
    public Object getReserveZones() {
        String componentVersionId = getParameter("componentVersionId");
        String reserveZoneId = getParameter("reserveZoneId");
        ComponentReserveZone componentReserveZone = getService().getByID(reserveZoneId);
        List<ComponentReserveZone> reserveZoneList = null;
        if (componentReserveZone.getIsCommon()) {
            reserveZoneList = getService().getCommonReserveZoneByType(componentReserveZone.getType());
        } else {
            reserveZoneList = getService().getByComponentVersionId(componentVersionId);
        }
        List<Object[]> list = new ArrayList<Object[]>();
        if (CollectionUtils.isNotEmpty(reserveZoneList)) {
            Object[] objArray = null;
            for (ComponentReserveZone reserveZone : reserveZoneList) {
                if (StringUtil.isNotEmpty(reserveZoneId) && reserveZoneId.equals(reserveZone.getId())) {
                    continue;
                } else if (!reserveZone.getType().equals(componentReserveZone.getType())) {
                    continue;
                } else {
                    objArray = new Object[2];
                    objArray[0] = reserveZone.getId();
                    objArray[1] = reserveZone.getAlias();
                    list.add(objArray);
                }
            }
        }
        setReturnData(list);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
}
