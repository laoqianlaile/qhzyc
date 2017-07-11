package com.ces.xarch.plugins.authsystem.actions;

import org.springframework.beans.factory.annotation.Autowired;

import ces.sdk.util.MD5;

import com.ces.xarch.core.logger.Logger;
import com.ces.xarch.core.security.entity.SysUser;
import com.ces.xarch.core.security.vo.SystemRoleVO;
import com.ces.xarch.plugins.authsystem.service.SysOrgService;
import com.ces.xarch.plugins.authsystem.service.SysRoleService;
import com.ces.xarch.plugins.authsystem.service.SysUserService;
import com.ces.xarch.plugins.core.actions.StringIDDefineServiceAuthSystemController;

public class SysUserController extends StringIDDefineServiceAuthSystemController<SysUser, SysUserService>{

	/** 系统-角色 实体类*/
	private SystemRoleVO systemRoleVO;
	/** 旧密码 */
	private String oldPassword;
	
	
	@Override
	protected void initModel() {
		setModel(new SysUser());
	}

	
	@Override
	@Autowired
	protected void setService(SysUserService service) {
		super.setService(service);
	}
	
	/**
	 * 获取左侧静态树
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月30日 下午2:08:28
	 * @return
	 */
	public Object getStaticTree(){
		setReturnData(getService(SysOrgService.class).getStaticTree4User()); 
		return SUCCESS;
	}
	
	/**
	 * 用户授予角色, 生成一棵同步树
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年7月2日 下午1:30:34
	 * @return
	 */
	public Object getStaticSysRoleTree4UserGratRole(){
		setReturnData(getService(SysRoleService.class).getStaticSysRoleTree4UserGrantRole(model.getId(), model.getParentId())); 
		return SUCCESS;
	}

	/**
	 * 授予角色
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年7月3日 下午7:20:57
	 * @return
	 */
	public Object grantRole(){
		//getService().grantRole(model.getId(),systemRoleVO.getSystemRoleId());
		return SUCCESS;
	}

	/**
	 * 重置密码
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @throws Exception 
	 * @comments:
	 * @date 2015年4月28日下午4:18:29
	 */
	@Logger(action = "重置密码", logger = "重置密码: ${id}")
	public Object resetPassword() throws Exception{
		getService().resetPassword(model.getId());
		return SUCCESS;
	}
	
	
	/**
	 * .
	 * <p>描述:验证原密码是否正确</p>
	 * @author Administrator
	 * @date 2013-08-14  13:51:22
	 */
	@Logger(action = "检验", logger = "检验密码: ${id}")
	public void checkPassword(){
		SysUser sysUser=getService().getByID(model.getId());
		String oldPassword=new MD5().getMD5ofStr(model.getPassword()).toLowerCase();
		if(oldPassword.equalsIgnoreCase(sysUser.getPassword())){
			setReturnData(1);
		}else{
			setReturnData(0);
		}
	}
	
	/**
	 * .
	 * <p>描述:修改用户密码</p>
	 * @author Administrator
	 * @throws Exception 
	 * @date 2013-08-14  13:51:01
	 */
	@Logger(action = "修改", logger = "修改密码: ${id}")
	public void updateUserPasswd() throws Exception{
		setReturnData(getService().updateUserPasswd(model,oldPassword));		
	}
	
	/**
	 * @return 返回  SystemRoleVO systemRoleVO
	 */
	public SystemRoleVO getSystemRoleVO() {
		return systemRoleVO;
	}


	/**
	 * @param 设置  SystemRoleVO systemRoleVO
	 */
	public void setSystemRoleVO(SystemRoleVO systemRoleVO) {
		this.systemRoleVO = systemRoleVO;
	}

	/**
	 * @return 返回  String oldPassword
	 */
	public String getOldPassword() {
		return oldPassword;
	}


	/**
	 * @param 设置  String oldPassword
	 */
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	
	
}
