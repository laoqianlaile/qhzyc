package com.ces.component.qyptjbfwxx.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.qyptjbfwxx.service.QyptjbfwxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.config.utils.ConstantVar;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.BusinessException;
import com.ces.xarch.core.exception.FatalException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class QyptjbfwxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, QyptjbfwxxService, TraceShowModuleDao> {

    private static final long serialVersionUID = 1L;

    private static Log log = LogFactory.getLog(QyptjbfwxxController.class);
    
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

}