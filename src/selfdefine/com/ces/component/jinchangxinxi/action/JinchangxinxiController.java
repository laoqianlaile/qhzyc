package com.ces.component.jinchangxinxi.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.component.jinchangxinxi.dao.JinchangxinxiDao;
import com.ces.component.jinchangxinxi.service.JinchangxinxiService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.xarch.core.entity.StringIDEntity;

public class JinchangxinxiController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, JinchangxinxiService, JinchangxinxiDao> {

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
    
    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * 
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     *      StringIDService)
     */
    @Autowired
    @Qualifier("jinchangxinxiService")
    @Override
    protected void setService(JinchangxinxiService service) {
        super.setService(service);
    }
    
    //TODO 从session中获取企业编码
    public void getJcsfbh(){
    	String qz = this.getRequest().getParameter("Qz");
    	String zl = this.getRequest().getParameter("Zl"); 
    	String jcsfbh = SerialNumberUtil.getInstance().getSerialNumber("PC",zl,true);
    	super.setReturnData(jcsfbh);
    }
}
