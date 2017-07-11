package com.ces.config.dhtmlx.action.appmanage;


import java.util.List;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.ColumnRelationDao;
import com.ces.config.dhtmlx.entity.appmanage.ColumnRelation;
import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.service.appmanage.ColumnRelationService;
import com.ces.config.dhtmlx.service.appmanage.TriggerService;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.exception.FatalException;

public class ColumnRelationController extends ConfigDefineServiceDaoController<ColumnRelation, ColumnRelationService, ColumnRelationDao>{

	private static final long serialVersionUID = 1L;

	@Override
	protected void initModel() {
		setModel(new ColumnRelation());
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
    @Qualifier("columnRelationService")
    protected void setService(ColumnRelationService service) {
        super.setService(service);
    }
    
    /**
     * 字段管理列表查询
     * @return
     * @throws FatalException
     */
    public Object getAllColumnRelationList() throws FatalException {
        String tableId = getParameter("tableId");
        setReturnData(getService().getAllColumnRelations(tableId));
        return new DefaultHttpHeaders("success").disableCaching();
    }
    
    public Object sortShowOrderRelation() throws FatalException{
        String start = getParameter("start");
        String end = getParameter("end");
        String tableId = getParameter("tableId");
        
        ColumnRelation startRelation = getService().getByID(start);
        ColumnRelation endRelation = getService().getByID(end);
        if(startRelation.getShowOrder() > endRelation.getShowOrder()){
        	//向上拖拽
        	List<ColumnRelation> columnList = getService().getColumnListByShowOrder(endRelation.getShowOrder(), startRelation.getShowOrder(),tableId);
        	startRelation.setShowOrder(endRelation.getShowOrder());
        	getService().save(startRelation);
        	for(ColumnRelation columnRelation : columnList){
        		if(columnRelation.getId().equals(startRelation.getId())){
        			continue;
        		}
        		columnRelation.setShowOrder(columnRelation.getShowOrder() + 1);
        		getService().save(columnRelation);
        	}
        }else{
			//向下拖拽
        	List<ColumnRelation> columnList = getService().getColumnListByShowOrder(startRelation.getShowOrder(), endRelation.getShowOrder(),tableId);
        	startRelation.setShowOrder(endRelation.getShowOrder());
        	getService().save(startRelation);
        	for(ColumnRelation columnRelation : columnList){
        		if(columnRelation.getId().equals(startRelation.getId())){
        			continue;
        		}
        		columnRelation.setShowOrder(columnRelation.getShowOrder() - 1);
        		getService().save(columnRelation);
        	}
		}
        setReturnData("排序成功");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
    
    /*
     * (非 Javadoc)   
     * <p>标题: destroy</p>   
     * <p>描述: </p>   
     * @return
     * @throws FatalException   
     * @see com.ces.xarch.core.web.struts2.BaseController#destroy()
     */
    @Override
    public Object destroy() throws FatalException {
        try {
            String message = "OK";
            String tableIds = getService().deleteRelation(getId());
            if (StringUtil.isNotEmpty(tableIds)) {
                String[] tableIdArr = tableIds.split(",");
                // 重新生成触发器
                for (String tableId : tableIdArr) {
                    try {
                        getService(TriggerService.class).generateColumnRelationTrigger(tableId);
                    } catch (Exception e) {
                        e.printStackTrace();
                        message = "重新生成触发器出问题";
                    }
                }
            }
            setReturnData(new MessageModel(Boolean.TRUE, message));
        } catch (Exception e) {
            e.printStackTrace();
            setReturnData(new MessageModel(Boolean.FALSE, "ERROR"));
        }
        return NONE;
    }
    
    
    /**
     * 字段复制
     * @throws FatalException
     */

    public Object saveCopyColumn() throws FatalException {
        try {
            String rId = getParameter("rId");
            String type = getParameter("type");
            getService().saveCopyColumnServcie(rId, type);
            setReturnData(new MessageModel(Boolean.TRUE, ""));
        } catch (Exception e) {
            setStatus(false);
            setMessage(e.getMessage());
            setReturnData(new MessageModel(Boolean.FALSE, ""));
        }
        return new DefaultHttpHeaders("success").disableCaching();
    }

    
    
}
