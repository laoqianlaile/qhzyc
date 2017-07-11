package com.ces.config.dhtmlx.action.parameter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlStrategy;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.parameter.SystemParameterDao;
import com.ces.config.dhtmlx.entity.parameter.SystemParameter;
import com.ces.config.dhtmlx.service.parameter.SystemParameterService;
import com.ces.config.utils.IndexCommonUtil;
import com.ces.config.utils.SystemParameterUtil;
import com.ces.xarch.core.exception.FatalException;

/**
 * 系统参数Controller
 * 
 * @author wanglei
 * @date 2013-08-12
 */
public class SystemParameterController extends ConfigDefineServiceDaoController<SystemParameter, SystemParameterService, SystemParameterDao> {

    private static final long serialVersionUID = -8037122071030457027L;

    private ConcurrentSessionControlStrategy sessionAuthenticationStrategy;

    @Autowired
    public void setSessionAuthenticationStrategy(ConcurrentSessionControlStrategy sessionAuthenticationStrategy) {
        this.sessionAuthenticationStrategy = sessionAuthenticationStrategy;
    }

    /**
     * 更新当前环境中的最大session数
     * 
     * @param maxSessions 最大session数
     */
    public void changeSessionMAX(int maxSessions) {
        // 更新当前环境中的值
        sessionAuthenticationStrategy.setMaximumSessions(maxSessions);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new SystemParameter());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     * StringIDService)
     */
    @Autowired
    @Qualifier("systemParameterService")
    @Override
    protected void setService(SystemParameterService service) {
        super.setService(service);
    }

    /*
     * (non-Javadoc)
     * @see com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController#create()
     */
    @Override
    public Object create() throws FatalException {
        Integer maxShowOrder = getService().getMaxShowOrder(model.getCategoryId());
        int showOrder = 0;
        if (maxShowOrder == null) {
            showOrder = 1;
        } else {
            showOrder = maxShowOrder + 1;
        }
        model.setShowOrder(showOrder);
        model = getService().save(model);
        SystemParameterUtil.getInstance().putSystemParamValue(model.getName(), model.getValue());
        SystemParameterUtil.getInstance().putSystemParamValue1(model.getId(), model.getValue());
        return SUCCESS;
    }

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#update()
     */
    @Override
    public Object update() throws FatalException {
        if ("用户登录最大次数".equals(model.getName())) {
            changeSessionMAX(Integer.parseInt(model.getValue()));
        }
        SystemParameter oldSystemParameter = getService().getByID(model.getId());
        SystemParameterUtil.getInstance().removeSystemParam(oldSystemParameter.getName());
        SystemParameterUtil.getInstance().removeSystemParam1(oldSystemParameter.getId());
        getService().save(model);
        SystemParameterUtil.getInstance().putSystemParamValue(model.getName(), model.getValue());
        SystemParameterUtil.getInstance().putSystemParamValue1(model.getId(), model.getValue());
        
        if ("全文检索引擎".equals(model.getName())) {
    		if ("1".equals(model.getValue())) {
    			IndexCommonUtil.startIndexScan();
    		} else {
    			IndexCommonUtil.stopIndexScan();
    		}
    	}
        
        return SUCCESS;
    }

    /**
     * 获取所有的前台提示信息
     * 
     * @return Object
     */
    public Object getAllMessages() {
        List<SystemParameter> systemParameterList = getService().getByCategoryId("sys_1_5");
        Map<String, String> messageMap = new HashMap<String, String>();
        if (CollectionUtils.isNotEmpty(systemParameterList)) {
            for (SystemParameter systemParameter : systemParameterList) {
                messageMap.put(systemParameter.getName(), systemParameter.getValue());
            }
        }
        setReturnData(messageMap);
        return SUCCESS;
    }

    /**
     * 获取系统参数的值
     * 
     * @return Object
     */
    public Object getSystemParamValue() {
        String systemParamName = getParameter("systemParamName");
        setReturnData(SystemParameterUtil.getInstance().getSystemParamValue(systemParamName));
        return SUCCESS;
    }
    
    /**
     * qiucs 2015-10-19 下午4:04:10
     * <p>描述: 获取表单工具条位置 </p>
     * @return Object
     */
    public Object formToolBarPosition() {
    	
    	try {
    		Map<String, String> tbarPos = new HashMap<String, String>();
    		tbarPos.put("nested", SystemParameterUtil.getInstance().getSystemParamValue("嵌入式表单工具条默认显示位置"));
    		tbarPos.put("popup", SystemParameterUtil.getInstance().getSystemParamValue("弹出式表单工具条默认显示位置"));
    		setReturnData(tbarPos);
    	} catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
		}
    	
    	return NONE;
    }

    /**
     * 排序
     * 
     * @return Object
     */
    public Object sort() {
        String start = getParameter("start");
        String end = getParameter("end");
        SystemParameter startSystemParameter = getService().getByID(start);
        SystemParameter endSystemParameter = getService().getByID(end);
        if (startSystemParameter.getShowOrder().intValue() > endSystemParameter.getShowOrder().intValue()) {
            // 向上
            List<SystemParameter> systemParameterList = getService().getSystemParameterListByShowOrder(endSystemParameter.getShowOrder(),
                    startSystemParameter.getShowOrder(), startSystemParameter.getCategoryId());
            startSystemParameter.setShowOrder(endSystemParameter.getShowOrder());
            getService().save(startSystemParameter);
            for (SystemParameter systemParameter : systemParameterList) {
                if (systemParameter.getId().equals(startSystemParameter.getId())) {
                    continue;
                }
                systemParameter.setShowOrder(systemParameter.getShowOrder() + 1);
                getService().save(systemParameter);
            }
        } else {
            // 向下
            List<SystemParameter> systemParameterList = getService().getSystemParameterListByShowOrder(startSystemParameter.getShowOrder(),
                    endSystemParameter.getShowOrder(), startSystemParameter.getCategoryId());
            startSystemParameter.setShowOrder(endSystemParameter.getShowOrder());
            getService().save(startSystemParameter);
            for (SystemParameter systemParameter : systemParameterList) {
                if (systemParameter.getId().equals(startSystemParameter.getId())) {
                    continue;
                }
                systemParameter.setShowOrder(systemParameter.getShowOrder() - 1);
                getService().save(systemParameter);
            }
        }
        setReturnData("排序成功");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 校验字段是否已经存在
     * 
     * @return Object
     */
    public Object validateFields() {
        SystemParameter systemParameter = (SystemParameter) getModel();
        SystemParameter temp = getService().getSystemParameterByName(systemParameter.getName());
        boolean nameExist = false;
        if (null != systemParameter.getId() && !"".equals(systemParameter.getId())) {
            SystemParameter oldSystemParameter = getService().getByID(systemParameter.getId());
            if (null != temp && null != oldSystemParameter && !temp.getId().equals(oldSystemParameter.getId())) {
                nameExist = true;
            }
        } else {
            if (null != temp) {
                nameExist = true;
            }
        }
        setReturnData("{'nameExist' : " + nameExist + "}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
}
