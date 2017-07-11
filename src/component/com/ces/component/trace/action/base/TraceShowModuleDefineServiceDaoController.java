package com.ces.component.trace.action.base;

import com.ces.component.trace.dao.base.TraceShowModuleStringIDDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.action.base.ShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TraceShowModuleDefineServiceDaoController<T extends StringIDEntity, Service extends TraceShowModuleDefineDaoService<T, Dao>, Dao extends TraceShowModuleStringIDDao<T>>
    extends ShowModuleDefineServiceDaoController<T, Service, Dao> {
    
    private static final long serialVersionUID = 1L;
    private static Log log = LogFactory.getLog(TraceShowModuleDefineServiceDaoController.class);
    


}
