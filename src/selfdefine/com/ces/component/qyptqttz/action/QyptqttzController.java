package com.ces.component.qyptqttz.action;

import ces.sdk.system.bean.OrgInfo;
import ces.sdk.system.bean.UserInfo;
import ces.sdk.system.facade.OrgInfoFacade;
import com.ces.component.qyptqttz.dao.QyptqttzDao;
import com.ces.component.qyptqttz.service.QyptqttzService;
import com.ces.component.qyptqtzh.service.QyptqtzhService;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.utils.CommonUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.security.entity.SysUser;
import com.ces.xarch.core.web.listener.XarchListener;
import com.ces.xarch.core.web.struts2.StringIDDefineServiceDaoController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QyptqttzController extends StringIDDefineServiceDaoController<StringIDEntity, QyptqttzService, QyptqttzDao>{
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
	protected void setService(QyptqttzService service){
		super.setService(service);
	}
	public void queryTzxx(){
		OrgInfoFacade orgInfoFacade = CommonUtil.getOrgInfoFacade();
		String zhbh = SerialNumberUtil.getInstance().getCompanyCode();
		String orgId = this.getService().queryAuthId(zhbh);
		QyptqtzhService qyptqtzhService = XarchListener
				.getBean(QyptqtzhService.class);
		Map<String, Object> map = new HashMap();
		// 获取企业基本信息map
		Map<String, Object> mapValue = qyptqtzhService
				.getEnterInfo(zhbh);
		map.put("qyxx", mapValue);
		map.put("xxlb", this.getService().queryXxlbList(zhbh));
		/*List<OrgInfo> li = CommonUtil.getOrgInfoFacade().findChildsByParentId(orgId);
		List userList = new ArrayList();
		for(OrgInfo orgInfo :li){
			String orgInfoId = orgInfo.getId();
			List<UserInfo> userLi = CommonUtil.getUserInfoFacade().findUsersByOrgId(orgInfoId);
			userList.addAll(userLi);
		}
		map.put("userList",userList);*/
		setReturnData(map);
	}
}
