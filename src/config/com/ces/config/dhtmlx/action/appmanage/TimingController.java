package com.ces.config.dhtmlx.action.appmanage;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.TimingDao;
import com.ces.config.dhtmlx.entity.appmanage.TimingEntity;
import com.ces.config.dhtmlx.service.appmanage.TimingService;
import com.ces.config.utils.TimeManager;
import com.ces.xarch.core.exception.FatalException;
import com.ces.xarch.core.logger.Logger;

public class TimingController extends ConfigDefineServiceDaoController<TimingEntity, TimingService, TimingDao> {

    private static final long serialVersionUID = -567814481832252865L;

    @Override
    protected void initModel() {
        setModel(new TimingEntity());
    }

    @Logger(model = "定时任务管理", action = "查询", logger = "查询定时任务|level=4|")
    public Object search() {
        super.search();
        return DATA_MODEL_COMBOBOX;
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
    @Qualifier("timingService")
    protected void setService(TimingService service) {
        super.setService(service);
    }

    /**
     * 定时任务详情列表
     * 
     * @return
     * @throws FatalException
     */
    public Object getTimingTasks() throws FatalException {
        setReturnData(getService().getTimingTasks());
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 获取任务的cmd命令语句
     * 
     * @return
     * @throws FatalException
     */
    public Object getCommandById() throws FatalException {
        try {
            String id = getParameter("Id");
            setReturnData(getService().getCommandById(id));
        } catch (Exception e) {
            setStatus(false);
            setMessage(e.getMessage());
        }
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 修改操作状态
     * 
     * @return
     * @throws FatalException
     */
    public Object updTimingOperates() throws FatalException {
        try {
            String status = getParameter("status");
            String id = getParameter("Id");
            getService().updTimingOperates(status, id);
            if (TimeManager.startTask.equals("true")) {
                // 启动定时任务
                if (status.equals("0")) { // 启动定时任务
                    TimingEntity timingEntity = getService().getByID(id);
                    TimeManager.startSchedule(timingEntity);
                } else if (status.equals("1")) { // 移除定时任务
                    TimeManager.stopSchedule(id);
                }
            }
            setReturnData(MessageModel.trueInstance("OK"));
        } catch (Exception e) {
            e.printStackTrace();
            setReturnData(MessageModel.falseInstance("ERROR"));
        }
        return NONE;
    }

    public Object create() throws FatalException {
        // HttpServletRequest request = ServletActionContext.getRequest();
        String time = model.getTime();
        if (time != null && !"".equalsIgnoreCase(time) && time.indexOf(",") != -1) {
            time = time.split(",")[1];
            model.setTime(time);
        }
        // String time1 =request.getParameter("time")==null?"":request.getParameter("time");
        getService().save(model);

        return SUCCESS;
    }

    /**
     * 编辑
     * 
     * @return
     * @throws FatalException
     */
    public Object update() throws FatalException {
        String time = model.getTime();
        if (time != null && !"".equalsIgnoreCase(time) && time.indexOf(",") != -1) {
            time = time.split(",")[1];
            model.setTime(time);
        }
        model = getService().save(model);
        return SUCCESS;
    }
    
}
