package com.ces.config.dhtmlx.action.appmanage;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import ces.workflow.wapi.Coflow;
import ces.workflow.wapi.org.WFOrgException;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.WorkflowDefineDao;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowDefine;
import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.service.appmanage.WorkflowDefineService;
import com.ces.config.utils.TableUtil;
import com.ces.utils.BeanUtils;
import com.ces.xarch.core.exception.FatalException;
import com.ces.xarch.core.web.jackson.BeanFilter;

public class WorkflowDefineController extends ConfigDefineServiceDaoController<WorkflowDefine, WorkflowDefineService, WorkflowDefineDao>{

    private static final long serialVersionUID = 3874214983831418535L;
    
    private static Log log = LogFactory.getLog(WorkflowDefineController.class);

    @Override
    protected void initModel() {
        setModel(new WorkflowDefine());
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
    @Qualifier("workflowDefineService")
    protected void setService(WorkflowDefineService service) {
        super.setService(service);
    }
    
    /**
     * qiucs 2013-8-23 
     * <p>标题: adjustShowOrder</p>
     * <p>描述: 拖拽调整顺序</p>
     * @return Object    返回类型   
     * @throws
     */
    public Object adjustShowOrder() {
        String parentId  = getParameter("P_parentId");
        String sourceIds = getParameter("P_sourceIds");
        String targetId  = getParameter("P_targetId");
        getService().adjustShowOrder(parentId, sourceIds, targetId);
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
            getService().delete(getId());
            setReturnData(MessageModel.trueInstance("OK"));
        } catch (RuntimeException e) {
            setReturnData(MessageModel.falseInstance(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            setReturnData(MessageModel.falseInstance("ERROR"));
        }
        return NONE;
    }
    
    /**
     * qiucs 2014-12-18 下午7:34:47
     * <p>描述: 删除检查 </p>
     * @return Object
     */
    public Object checkDelete() {
    	try {
			setReturnData(getService().checkDelete(getId()));
		} catch (Exception e) {
			log.error("删除检查出错", e);
		}
    	return NONE;
    }

    /**
     * 同步工作流中用到的组织和用户
     * 
     * @return Object
     */
    public Object syncOrgUser() {
        try {
            Coflow.getOrgAccess().update();
        } catch (WFOrgException e) {
            e.printStackTrace();
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
    
    /**
     * qiucs 2014-10-21 
     * <p>描述: 工作流流程文件定义检查</p>
     * @return Object    返回类型   
     * @throws
     */
    public Object checkCoflow() {
        try {
            setReturnData(getService().checkCoflow(model));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return NONE;
    }

	@Override
	protected List<WorkflowDefine> beforeProcessData(List<WorkflowDefine> list) {
		for (WorkflowDefine entity : list) {
			entity.setBusinessTableText(TableUtil.getTableText(entity.getBusinessTableId()));
		}
		return (list);
	}

	@Override
	public Object show() throws FatalException {
		BeanUtils.copy(getService().getByID(getId()),model);
		processFilter((BeanFilter)model);
		model.setBusinessTableText(TableUtil.getTableText(model.getBusinessTableId()));
		return new DefaultHttpHeaders("show").disableCaching();
	}
	
	/**
	 * qiucs 2015-1-22 下午10:28:36
	 * <p>描述: 同步业务表视图 </p>
	 * @return Object
	 */
	public Object syncBusinessView() {
		try {
            getService().syncBusinessView(getId());
            setReturnData(MessageModel.trueInstance("OK"));
        } catch (Exception e) {
            log.error("同步业务表视图出错", e);
        }
        
        return NONE;
	}
}
