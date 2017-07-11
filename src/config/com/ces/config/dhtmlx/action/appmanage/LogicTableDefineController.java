package com.ces.config.dhtmlx.action.appmanage;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.LogicTableDefineDao;
import com.ces.config.dhtmlx.entity.appmanage.LogicTableDefine;
import com.ces.config.dhtmlx.entity.component.ComponentReserveZone;
import com.ces.config.dhtmlx.entity.construct.ConstructDetail;
import com.ces.config.dhtmlx.service.appmanage.LogicTableDefineService;
import com.ces.config.dhtmlx.service.component.ComponentReserveZoneService;
import com.ces.config.dhtmlx.service.construct.ConstructDetailService;
import com.ces.xarch.core.exception.FatalException;

public class LogicTableDefineController extends ConfigDefineServiceDaoController<LogicTableDefine, LogicTableDefineService, LogicTableDefineDao> {

    private static final long serialVersionUID = 1L;

    @Override
    protected void initModel() {
        setModel(new LogicTableDefine());
    }

    /*
     * (非 Javadoc)
     * <p>标题: setService</p>
     * <p>描述: 注入服务层(Service)</p>
     * @param service
     * @see com.ces.xarch.core.web.struts2.BaseController#setService(com.ces.xarch.core.service.AbstractService)
     */
    @Override
    @Autowired
    @Qualifier("logicTableDefineService")
    protected void setService(LogicTableDefineService service) {
        super.setService(service);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#destroy()
     */
    @Override
    public Object destroy() throws FatalException {
        String[] idArr = getId().split(",");
        LogicTableDefine logicTableDefine = null;
        ComponentReserveZone reserveZone1 = null;
        ComponentReserveZone reserveZone2 = null;
        ComponentReserveZone reserveZone3 = null;
        List<ConstructDetail> constructDetailList = null;
        StringBuilder msg = new StringBuilder();
        for (int i = 0; i < idArr.length; i++) {
            logicTableDefine = getService().getByID(idArr[i]);
            reserveZone1 = getService(ComponentReserveZoneService.class).getCommonReserveZoneByName(logicTableDefine.getCode() + "_GRID");
            reserveZone2 = getService(ComponentReserveZoneService.class).getCommonReserveZoneByName(logicTableDefine.getCode() + "_FORM");
            reserveZone3 = getService(ComponentReserveZoneService.class).getCommonReserveZoneByName(logicTableDefine.getCode() + "_GRID_LINK");
            if (reserveZone1 != null) {
                constructDetailList = getService(ConstructDetailService.class).getByReserveZoneId(reserveZone1.getId());
                if (CollectionUtils.isNotEmpty(constructDetailList)) {
                    msg.append(logicTableDefine.getShowName()).append(',');
                    continue;
                }
            }
            if (reserveZone2 != null) {
                constructDetailList = getService(ConstructDetailService.class).getByReserveZoneId(reserveZone2.getId());
                if (CollectionUtils.isNotEmpty(constructDetailList)) {
                    msg.append(logicTableDefine.getShowName()).append(',');
                    continue;
                }
            }
            if (reserveZone3 != null) {
                constructDetailList = getService(ConstructDetailService.class).getByReserveZoneId(reserveZone3.getId());
                if (CollectionUtils.isNotEmpty(constructDetailList)) {
                    msg.append(logicTableDefine.getShowName()).append(',');
                }
            }
        }
        if (msg.length() > 0) {
            msg.deleteCharAt(msg.length() - 1).append("的预留区已经绑定了构件，无法删除！");
            setReturnData("{'success':false, 'message':'" + msg + "'}");
        } else {
            getService().delete(getId());
            setReturnData("{'success':true}");
        }
        return SUCCESS;
    }

    /**
     * qiujinwei 2014-11-14
     * <p>标题: adjustShowOrder</p>
     * <p>描述: 拖拽调整顺序</p>
     * 
     * @return Object 返回类型
     * @throws
     */
    public Object adjustShowOrder() throws FatalException {
        try {
            String sourceIds = getParameter("P_sourceIds");
            String targetId = getParameter("P_targetId");
            getService().adjustShowOrder(sourceIds, targetId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * qiujinwei 2014-11-20
     * <p>标题: comboOfTables2TableCopy</p>
     * <p>描述: 获取逻辑表下所有数据</p>
     * 
     * @return Object 返回类型
     * @throws
     */
    public Object comboOfTables2TableCopy() {
        setReturnData(getService().getComboOfTables2TabelCopy());
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 将物理表生成逻辑表
     * 
     * @return Object
     */
    public Object copyToLogic() {
        String showName = getParameter("showName");
        String code = getParameter("code");
        String copyTableId = getParameter("copyTableId");
        getService().copyToLogic(showName, code, copyTableId);
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * qiujinwei 2014-11-28
     * <p>标题: getLTExcludingLG</p>
     * <p>描述: 获取不在选中逻辑表组的逻辑表</p>
     * 
     * @param
     * @return Object 返回类型
     * @throws
     */
    public Object getLTExcludingLG() {
        String groupCode = getParameter("P_groupCode");
        setReturnData(getService().getLTExcludingLG(groupCode));
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 获取逻辑表下拉框选项(构件定义中)
     * 
     * @return Object
     */
    public Object comboOfLogicTables() {
        String logicTableGroupCode = getParameter("logicTableGroupCode");
        String parentTableCode = getParameter("parentTableCode");
        try {
            setReturnData(getService().comboOfLogicTables(logicTableGroupCode, parentTableCode));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
    
    /**
     * 获取获取对应逻辑表组下的父逻辑表选项
     * 
     * @return Object
     */
    public Object getLTIncludingLG(){
    	String groupCode = getParameter("P_groupCode");
    	String id = getParameter("P_rId");
        setReturnData(getService().getLTIncludingLG(groupCode, id));
    	return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
    
}
