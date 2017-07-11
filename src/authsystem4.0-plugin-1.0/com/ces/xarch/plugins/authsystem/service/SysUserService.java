package com.ces.xarch.plugins.authsystem.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ces.sdk.system.facade.RoleInfoFacade;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import ces.sdk.system.bean.UserInfo;
import ces.sdk.system.facade.OrgInfoFacade;
import ces.sdk.system.facade.UserInfoFacade;
import ces.sdk.util.StringUtil;

import com.ces.xarch.core.security.entity.SysOrg;
import com.ces.xarch.core.security.entity.SysUser;
import com.ces.xarch.plugins.authsystem.utils.FacadeUtil;
import com.ces.xarch.plugins.common.utils.BeanConvertUtil;
import com.ces.xarch.plugins.core.service.StringIDAuthSystemService;

@Component
public class SysUserService extends StringIDAuthSystemService<SysUser>{

	@Override
	public void bindingDao(Class<SysUser> entityClass) {}
	
	@Override
	public SysUser getByID(String id) {
		UserInfoFacade userFacade = FacadeUtil.getUserInfoFacade();
		UserInfo userInfo = userFacade.findByID(id);
		return (SysUser) BeanConvertUtil.converter(userInfo, new SysUser());
	}

	@Override
	public SysUser save(SysUser sysUser) {
		UserInfoFacade userFacade = FacadeUtil.getUserInfoFacade();
		sysUser.setShowOrder(this.findMaxOrderNo(sysUser));
		
		UserInfo userInfo = (UserInfo) BeanConvertUtil.converter(sysUser, new UserInfo());
		if(isSuperRole()){ // 如果是超级系统管理员新增单位系统管理员,则给单位系统管理员添加管辖单位
 			userInfo.setAdminOrgId(sysUser.getParentId());
		}else{  // 如果是单位系统管理员新增业务用户,则给业务用户添加所属单位
			userInfo.setBelongOrgId(this.getByID(this.getLoginUser().getId()).getAdminOrgId());
		}
		
		userInfo = userFacade.save(userInfo, sysUser.getParentId(),sysUser.getRoleId());
		
		return (SysUser) BeanConvertUtil.converter(userInfo, new SysUser());
	}

	@Override
	public void delete(String id) {
		UserInfoFacade userFacade = FacadeUtil.getUserInfoFacade();
		userFacade.delete(id);
	}

	@Override
	public void delete(SysUser sysUser){
		UserInfoFacade userFacade = FacadeUtil.getUserInfoFacade();
		userFacade.delete(sysUser.getId());
	}
	
	@Override
	public SysUser update(SysUser sysUser) {
		UserInfoFacade userFacade = FacadeUtil.getUserInfoFacade();
		UserInfo userInfo = (UserInfo) BeanConvertUtil.converter(sysUser, new UserInfo());
		userInfo = userFacade.update(userInfo);
		
		return (SysUser) BeanConvertUtil.converter(userInfo, new SysUser());
	}

	@Override
	public Page<SysUser> find(Specification<SysUser> spec, Pageable pageable) {
		UserInfoFacade userFacade = FacadeUtil.getUserInfoFacade();
		Map<String,String> param = BeanConvertUtil.getFilter(spec);
		
		int total = userFacade.findTotal(param);
		List<UserInfo> userInfos = userFacade.find(param, pageable.getPageNumber() + 1, pageable.getPageSize());
		
		List<SysUser> sysUsers = new ArrayList<SysUser>(userInfos.size());
		sysUsers = this.converterUserInfo(userInfos, sysUsers);
		return new PageImpl<SysUser>(sysUsers,pageable,total);
	}

	private List<SysUser> converterUserInfo(List<UserInfo> userInfos, List<SysUser> sysUsers) {
		for (UserInfo userInfo : userInfos) {
			sysUsers.add((SysUser)BeanConvertUtil.converter(userInfo, new SysUser()));
		}
		return sysUsers;
	}

	@Override
	public List<SysUser> find(Specification<SysUser> spec) {
		UserInfoFacade userFacade = FacadeUtil.getUserInfoFacade();
		
		Map<String,String> param = BeanConvertUtil.getFilter(spec);
		List<UserInfo> userInfos = userFacade.findByCondition(param);
		
		List<SysUser> sysUsers = new ArrayList<SysUser>(userInfos.size());
		sysUsers = this.converterUserInfo(userInfos, sysUsers);
		
		return sysUsers;
	}

	@Override
	protected long findMaxOrderNo(SysUser sysUser) {
		UserInfoFacade userFacade = FacadeUtil.getUserInfoFacade();
		return userFacade.findMaxOrderNo(sysUser.getParentId());
	}

	/**
	 * 为用户绑定系统角色
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年7月2日 下午2:42:02
	 * @param userId 用户id
	 * @param systemRoleId 系统角色id, 系统角色的格式是 [systemId0-roleId0, systemId1-roleId1]
	 */
	public void grantRole(String userId, String systemRoleId, String orgId) {
		UserInfoFacade userInfoFacade = FacadeUtil.getUserInfoFacade();
		Map<String, String> roleSystemIds = new HashMap<String, String>();
		if(StringUtil.isNotBlank(systemRoleId)){
			for (String sysRoleId : systemRoleId.split(",")) {
				String[] sysRoleIdsArray = sysRoleId.split("-");
				String systemId = sysRoleIdsArray[0];
				String roleId = sysRoleIdsArray[1];
				roleSystemIds.put(roleId, systemId);
				sysRoleIdsArray = null;
				systemId = null;
				roleId = null;
			}
		}
		userInfoFacade.grantRoleBatch(userId, roleSystemIds, orgId);
	}

	/**
	 * 判断用户是否拥有该角色
	 * @param roleId
	 * @param userId
	 * @return
	 */
	public boolean hasRole(String roleId, String userId, String orgId) {
		RoleInfoFacade roleInfoFacade = FacadeUtil.getRoleInfoFacade();
		return roleInfoFacade.hasRole(roleId, userId, orgId);
	}

	public void resetPassword(String userIds) {
		UserInfoFacade userInfoFacade=FacadeUtil.getUserInfoFacade();
		String[] userIdsArray = userIds.split(",");
		for (String userId : userIdsArray) {
			userInfoFacade.resetUserPasswd(userId);
		}
	}
	
	/**
	 * 修改用户密码
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年7月3日 下午7:37:08
	 * @param model
	 * @param oldPassword
	 * @return
	 */
	public Object updateUserPasswd(SysUser sysUser, String oldPassword) {
		String loginName = this.getByID(sysUser.getId()).getLoginName();
		UserInfoFacade  userfacade=FacadeUtil.getUserInfoFacade();
		return userfacade.updateUserPasswd(loginName, oldPassword, sysUser.getPassword());
	}

}
