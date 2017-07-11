package com.ces.config.dhtmlx.action.appmanage;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.WorkflowVersionDao;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowVersion;
import com.ces.config.dhtmlx.service.appmanage.WorkflowVersionService;
import com.ces.config.utils.AppUtil;
import com.ces.xarch.core.exception.FatalException;

public class WorkflowVersionController extends ConfigDefineServiceDaoController<WorkflowVersion, WorkflowVersionService, WorkflowVersionDao> {

    private static final long serialVersionUID = -1743513971470970368L;
    
    private static Log log = LogFactory.getLog(WorkflowVersionController.class);

    @Override
    protected void initModel() {
        setModel(new WorkflowVersion());
    }

    @Override
	public Object destroy() throws FatalException {
    	try {
            getService().delete(getId());
            setReturnData(MessageModel.trueInstance("删除成功！"));
            AppUtil.getInstance().reload();
        } catch (Exception e) {
            log.error("删除出错(id=" + getId() + ")", e);
            setReturnData(MessageModel.falseInstance(e.getMessage()));
        }
        return SUCCESS;
	}

	@Override
    @Autowired
    @Qualifier("workflowVersionService")
    protected void setService(WorkflowVersionService service) {
        super.setService(service);
    }
    
    /*
     * qiucs 2015-4-30 下午2:41:47
     * (non-Javadoc)
     * @see com.ces.config.action.base.StringIDConfigDefineServiceDaoController#create()
     */
    @Override
	public Object create() throws FatalException {
    	model = getService().save(model);
    	AppUtil.getInstance().reload();
        return SUCCESS;
	}

	/**
     * qiucs 2014-12-18 下午7:23:29
     * <p>描述: 删除校验 </p>
     * @return Object
     */
    public Object checkDelete() {
    	try {
			setReturnData(getService().checkDelete(getId()));
		} catch (Exception e) {
			log.error("删除校验出错", e);
		}
    	return NONE;
    }
    
    /**
     * qiucs 2014-12-23 下午3:20:17
     * <p>描述: 获取流程节点 </p>
     * @return Object
     */
    public Object getActivities() {
    	try {
    		setReturnData(getService().getActivities(getId()));
		} catch (Exception e) {
			log.error("获取流程节点出错", e);
		}
    	
    	return NONE;
    }
    
    /**
     * @date 2014-12-22 下午3:59:39
     * <p>描述: 获取流程文件 </p>
     * @return Object
     */
    public Object getProcessFile(){
		setReturnData(getService().getProcessFile(getId()));
		return NONE;
	}
    
    /**
     * @date 2014-12-22 下午5:35:10
     * <p>描述: 保存流程文件 </p>
     * @return Object
     */
    public Object saveProcessFile() {
    	String data = getParameter("data");
		setReturnData(getService().saveProcessFile(data));
    	return NONE;
    }
    
    public Object checkProcessFile() {
    	
    	String data = getParameter("data");
		setReturnData(getService().checkProcessFile(data));
		
    	return NONE;
    }
    
    /**
     * qiucs 2014-12-24 下午9:28:38
     * <p>描述: 同步服务 </p>
     * @return Object
     */
    public Object syncServer() {
    	try {
			String serverIp = ServletActionContext.getRequest().getServerName();
			setReturnData(getService().syncServer(getId(), serverIp));
		} catch (Exception e) {
			log.error("同步服务器出错", e);
		}
    	return NONE;
    }
    
    /**
     * qiucs 2014-12-24 下午10:39:46
     * <p>描述: 删除流程 </p>
     * @return Object
     */
    public Object deleteProcess() {
    	try {
			String serverIp = ServletActionContext.getRequest().getServerName();
			setReturnData(getService().deleteProcess(getId(), serverIp));
		} catch (Exception e) {
			log.error("删除流程出错", e);
		}
    	return NONE;
    }
    
    /**
     * qiucs 2014-12-24 下午11:19:26
     * <p>描述: 注册流程 </p>
     * @return Object
     */
    public Object registerProcess() {
    	try {
			String serverIp = ServletActionContext.getRequest().getServerName();
			setReturnData(getService().registerProcess(getId(), serverIp));
		} catch (Exception e) {
			log.error("注册流程出错", e);
		}
    	return NONE;
    }
    
    /**
     * qiucs 2014-12-24 下午11:19:47
     * <p>描述: 启动流程 </p>
     * @return Object
     */
    public Object startProcess() {
    	try {
			String serverIp = ServletActionContext.getRequest().getServerName();
			setReturnData(getService().startProcess(getId(), serverIp));
		} catch (Exception e) {
			log.error("启动流程出错", e);
		}
    	return NONE;
    }
    
    /**
     * qiucs 2014-12-24 下午11:20:19
     * <p>描述: 停止流程 </p>
     * @return Object
     */
    public Object stopProcess() {
    	try {
			String serverIp = ServletActionContext.getRequest().getServerName();
			setReturnData(getService().stopProcess(getId(), serverIp));
		} catch (Exception e) {
			log.error("停止流程出错", e);
		}
    	return NONE;
    }
    
    /**
     * qiucs 2014-12-24 下午11:20:52
     * <p>描述: 上传流程 </p>
     * @return Object
     */
    public Object uploadProcess() {
    	try {
			String serverIp = ServletActionContext.getRequest().getServerName();
			setReturnData(getService().uploadProcess(getId(), serverIp));
		} catch (Exception e) {
			log.error("上传流程出错", e);
		}
    	return NONE;
    }
    
    /**
     * qiucs 2015-3-26 上午11:47:41
     * <p>描述: 刷新流程 </p>
     * @return Object
     */
    public Object refreshProcess() {
    	try {
			String serverIp = ServletActionContext.getRequest().getServerName();
			setReturnData(getService().refreshProcess(getId(), serverIp));
		} catch (Exception e) {
			log.error("刷新流程出错", e);
		}
    	return NONE;
    }
    
    /**
     * qiucs 2014-12-24 下午11:21:18
     * <p>描述: 同步相关数据表 </p>
     * @return Object
     */
    public Object syncDataFieldTable() {
    	try {
			String serverIp = ServletActionContext.getRequest().getServerName();
			setReturnData(getService().syncDataFieldTable(getId(), serverIp));
		} catch (Exception e) {
			log.error("同步相关数据表出错", e);
		}
    	return NONE;
    }
    
    /**
     * qiucs 2014-12-25 下午8:32:03
     * <p>描述: 获取部门树 </p>
     * @return Object
     */
    public Object parseOrganization() {
    	try {
			setReturnData(getService().parseOrganization(getId()));
		} catch (Exception e) {
			log.error("获取部门树出错", e);
		}
    	return NONE;
    }
    
    /**
     * qiucs 2014-12-25 下午8:31:39
     * <p>描述: 获取用户树 </p>
     * @return Object
     */
    public Object parseUser() {
    	try {
			setReturnData(getService().parseUser(getId()));
		} catch (Exception e) {
			log.error("获取用户树出错", e);
		}
    	return NONE;
    }
    
    /**
     * qiucs 2014-12-25 下午8:31:08
     * <p>描述: 获取自定义公式 </p>
     * @return Object
     */
    public Object customFormula() {
    	try {
			setReturnData(getService().customFormula());
		} catch (Exception e) {
			log.error("获取自定义公式出错", e);
		}
    	return NONE;
    }
    
    /**
     * qiucs 2014-12-25 下午8:30:38
     * <p>描述: 导入相关数据 </p>
     * @return Object
     */
    public Object importDataField() {
    	try {
			setReturnData(getService().importDataField(getId()));
		} catch (Exception e) {
			log.error("导入相关数据出错", e);
		}
    	return NONE;
    }
    
    /**
     * qiucs 2014-12-25 下午8:30:13
     * <p>描述: 添加本地副本 </p>
     * @return Object
     */
    public Object addLocalCopy() {
    	try {
    		String version = getParameter("version");
			setReturnData(getService().addLocalCopy(getId(), version));
			AppUtil.getInstance().reload();
		} catch (Exception e) {
			log.error("添加本地副本出错", e);
		}
    	return NONE;
    }
    
    /**
     * qiucs 2014-12-25 下午8:29:28
     * <p>描述: 格式化流程文件 </p>
     * @return Object
     */
    public Object formatShow() {
        try {
        	String data = getParameter("data");
            setReturnData(getService().formatShow(data));
		} catch (Exception e) {
			log.error("格式化流程文件出错", e);
		}
    	return NONE;
	}

}
