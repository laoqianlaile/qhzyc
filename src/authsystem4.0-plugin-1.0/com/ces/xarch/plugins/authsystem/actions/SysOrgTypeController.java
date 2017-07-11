package com.ces.xarch.plugins.authsystem.actions;

import org.springframework.beans.factory.annotation.Autowired;

import com.ces.xarch.core.security.entity.SysOrgType;
import com.ces.xarch.plugins.authsystem.service.SysOrgTypeService;
import com.ces.xarch.plugins.core.actions.StringIDDefineServiceAuthSystemController;

/**
 * 组织级别控制类
 * <p>描述:组织级别控制类</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
 * @date 2015年6月2日 上午10:57:34
 * @version 1.0.2015.0601
 */
public class SysOrgTypeController extends StringIDDefineServiceAuthSystemController<SysOrgType, SysOrgTypeService>{

	@Override
	protected void initModel() {
		setModel(new SysOrgType());
	}

	
	@Override
	@Autowired
	protected void setService(SysOrgTypeService service) {
		super.setService(service);
	}
	
	/**
	 * 获取一颗异步树
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> 修改数据权限</p>
	 * @date 2015年6月2日 上午10:57:58
	 * @return
	 */
	public Object getAsyncTree(){
		setReturnData(this.getService().findChildsByParentId(model.getId()));
		return SUCCESS;
	}
	
	
	/**
	 * 获取一颗同步树
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> 修改数据权限</p>
	 * @date 2015年6月2日 上午10:57:58
	 * @return
	 */
	public Object getStaticTree(){
		setReturnData(this.getService().getStaticTree());
		return SUCCESS;
	}
	
	
	
}
