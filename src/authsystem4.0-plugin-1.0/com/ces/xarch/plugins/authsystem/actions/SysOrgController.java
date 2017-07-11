package com.ces.xarch.plugins.authsystem.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import ces.sdk.system.bean.UserInfo;
import ces.sdk.system.facade.UserInfoFacade;

import com.ces.xarch.core.security.entity.SysOrg;
import com.ces.xarch.core.security.entity.SysUser;
import com.ces.xarch.core.security.vo.SystemRoleVO;
import com.ces.xarch.plugins.authsystem.service.SysOrgService;
import com.ces.xarch.plugins.authsystem.service.SysRoleService;
import com.ces.xarch.plugins.authsystem.service.SysUserService;
import com.ces.xarch.plugins.authsystem.utils.FacadeUtil;
import com.ces.xarch.plugins.core.actions.StringIDDefineServiceAuthSystemController;

public class SysOrgController  extends StringIDDefineServiceAuthSystemController<SysOrg, SysOrgService>{

	/** 系统-角色 实体类*/
	private SystemRoleVO systemRoleVO;
	
	
	@Override
	protected void initModel() {
		setModel(new SysOrg());
	}

	
	@Override
	@Autowired
	protected void setService(SysOrgService service) {
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

	/**
	 * 获取组织用户数树（组织底下列出所有子组织和用户）
	 * @return
	 */
	public Object getOrgUserTree() {
		UserInfoFacade userInfoFacade = FacadeUtil.getUserInfoFacade();
		SysUser loginUser = getLoginUser();
		UserInfo userInfo = userInfoFacade.findByID(loginUser.getId());
		boolean isSuperRole = getService().isSuperRole();
		String roleId = getParameter("roleId");//树上勾选的角色ID
		List<Object> list = new ArrayList<Object>();
		List<SysOrg> sysOrgs = this.getService().findChildsByParentId(model.getId());
		Iterator<SysOrg> iterator = sysOrgs.iterator();
		while (iterator.hasNext()) {
			SysOrg sysOrg = iterator.next();
			if (model.getId() != null) {//根节点不判断
				//不是超级管理员，只显示当前登陆单位系统管理员的企业
				if (!isSuperRole && !sysOrg.getId().equals(userInfo.getAdminOrgId()) && "-1".equals(sysOrg.getParentId())) {
					iterator.remove();
					continue;
				}
				if (!getService().hasRole(sysOrg.getId(), roleId)) {//组织没有角色则在树上不现实
					iterator.remove();
					continue;
				}
			}
			sysOrg.setIsParent(true);
		}
		list.addAll(sysOrgs);
		List<SysUser> users = this.getService().getChildUserInfoByOrgId(model.getId());
		Iterator<SysUser> sysUserInfoIterator = users.iterator();
		while (sysUserInfoIterator.hasNext()) {
			SysUser sysUserInfo = sysUserInfoIterator.next();
			if (!isSuperRole && sysUserInfo.getAdminOrgId() != null) {//单位系统管理员不显示
				sysUserInfoIterator.remove();
				continue;
			}
			if (getService(SysUserService.class).hasRole(roleId, sysUserInfo.getId(), model.getId())) {
				sysUserInfo.setChecked(true);
			}
		}
		list.addAll(users);
		setReturnData(list);
		return SUCCESS;
	}

	public Object grantRole(){
		getService().grantRole(model.getId(),systemRoleVO.getSystemRoleId());
		return SUCCESS;
	}

	/**
	 * 组织变更
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:
	 * @date 2015年4月28日下午5:31:15
	 * @param orgId 组织id
	 * @param parentId 变更到的父组织id
	 */
	public Object changeOrg() {
		this.getService().changeOrg(model.getId() , model.getParentId() );
		return SUCCESS;
	}

	
    
	/**
	 * 获取静态的系统角色树
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年7月1日 上午10:35:30
	 * @return
	 */
	public Object getStaticSysRoleTree(){
		setReturnData(this.getService(SysRoleService.class).getStaticSysRoleTree4OrgGrantRole(model.getId(), model.getParentId()));
		return SUCCESS;
	}
	
	
	/**
	 * @return 返回  SystemRoleVO systemRoleVO
	 */
	public SystemRoleVO getSystemRoleVO() {
		return systemRoleVO;
	}
	
	
	/**
	 * @param systemRoleVO
	 */
	public void setSystemRoleVO(SystemRoleVO systemRoleVO) {
		this.systemRoleVO = systemRoleVO;
	}
}
