package com.ces.config.dhtmlx.action.appmanage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


import com.ces.config.action.base.ShowModuleDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.ShowModuleDao;
import com.ces.config.dhtmlx.service.appmanage.ShowModuleService;
import com.ces.config.utils.IndexCommonUtil;
import com.ces.xarch.core.entity.StringIDEntity;

public class ShowModuleController extends ShowModuleDefineServiceDaoController<StringIDEntity, ShowModuleService, ShowModuleDao> {
    
    /** 
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么).
     */
    private static final long serialVersionUID = -5144512022428483509L;
    /*
     * (非 Javadoc)   
     * <p>描述: 注入服务层(Service)</p>   
     * @param service   
     * @see com.ces.xarch.core.web.struts2.BaseController#setService(com.ces.xarch.core.service.AbstractService)
     */
    @Override
    @Autowired
    @Qualifier("showModuleService")
    protected void setService(ShowModuleService service) {
        super.setService(service);
    }
    /*
     * (非 Javadoc)   
     * <p>标题: initModel</p>   
     * <p>描述: </p>      
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new StringIDEntity());
    }
    
    public Object test() {
    	
    	return NONE;
    }
    
    /**
     * qiucs 2015-5-28 下午2:44:25
     * <p>描述: 重建索引 </p>
     * @return Object
     */
    public Object rebuildIndex() {
    	
    	setReturnData(IndexCommonUtil.rebuildIndex());
    	
    	return NONE;
    }
    
    /**
     * qiucs 2015-5-28 下午2:44:29
     * <p>描述: 增量索引 </p>
     * @return Object
     */
    public Object increaseIndex() {

    	setReturnData(IndexCommonUtil.increaseIndex());
    	
    	return NONE;
    }
    
    /**
     * qiucs 2015-7-7 上午11:52:24
     * <p>描述: 启动索引线程 </p>
     * @return Object
     */
    public Object startIndexScan() {
    	
    	IndexCommonUtil.startIndexScan();
    	
    	return NONE;
    }
    
    /**
     * qiucs 2015-7-7 上午11:52:32
     * <p>描述: 停止索引线程 </p>
     * @return Object
     */
    public Object stopIndexScan() {
    	
    	IndexCommonUtil.stopIndexScan();
    	
    	return NONE;
    }
    
}
