package com.ces.component.qyptqtzh.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.qyptqtzh.service.QyptqtzhService;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.web.struts2.StringIDDefineServiceDaoController;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
@Results({
	@Result(name="qyptqtzh",location="/cfg-resource/coral40/views/component/qyptqt/qyptqtzh.jsp")
})
public class QyptqtzhController extends StringIDDefineServiceDaoController<StringIDEntity, QyptqtzhService, TraceShowModuleDao>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
    protected void initModel() {
        setModel(new StringIDEntity());
    }
	@Autowired
	@Override
	protected void setService(QyptqtzhService service){
		super.setService(service);
	}
	
	/**
	 * 获取企业用户信息及基本服务，增值服务
	 * @return
	 */
	public Object getOrgInfo(){
		//获取企业基本信息map
		Map<String,Object> mapValue =  this.getService().getEnterInfo(SerialNumberUtil.getInstance().getCompanyCode());
		//ServletActionContext.getRequest().setAttribute("enterInfo", mapValue);
		setReturnData(mapValue);
		return new DefaultHttpHeaders(SUCCESS).disableCaching();
	}
	
}