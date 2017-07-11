package com.ces.component.qyptxxsz.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.qyptxxsz.service.QyptxxszService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class QyptxxszController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, QyptxxszService, TraceShowModuleDao> {

    private static final long serialVersionUID = 1L;
    
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

    /**
     * 获取所有的消息列表（下拉框）
     * @return
     */
    public Object getAllMsgs() {
        setReturnData(getService().getAllMsgs());
        return SUCCESS;
    }

    /**
     * 发送消息
     * @return
     */
    public Object sendMsg() {
	    String uuids = getParameter("rowIds");
	    String msgId = getParameter("msgId");
        try {
            getService().sendMsg(msgId, uuids);
        } catch (Exception e) {
            return ERROR;
        }
        return SUCCESS;
    }

    public void checkYxId(){
        String ids=getParameter("ids");

        setReturnData(getService().checkYxId(ids));
    }

}