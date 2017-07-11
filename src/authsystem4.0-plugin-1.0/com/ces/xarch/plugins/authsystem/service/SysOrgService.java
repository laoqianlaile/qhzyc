package com.ces.xarch.plugins.authsystem.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import ces.sdk.system.bean.OrgInfo;
import ces.sdk.system.bean.UserInfo;
import ces.sdk.system.facade.OrgInfoFacade;
import ces.sdk.system.facade.UserInfoFacade;
import ces.sdk.util.StringUtil;

import com.ces.xarch.core.security.entity.SysOrg;
import com.ces.xarch.core.security.entity.SysUser;
import com.ces.xarch.plugins.authsystem.utils.FacadeUtil;
import com.ces.xarch.plugins.common.global.Constants;
import com.ces.xarch.plugins.common.utils.BeanConvertUtil;
import com.ces.xarch.plugins.core.service.StringIDAuthSystemService;

@Component
public class SysOrgService extends StringIDAuthSystemService<SysOrg>{

	@Override
	public void bindingDao(Class<SysOrg> entityClass) {}

	@Override
	public SysOrg getByID(String id){
		OrgInfoFacade orgFacade = FacadeUtil.getOrgInfoFacade();
		OrgInfo orgInfo = orgFacade.findByID(id);
		return (SysOrg) BeanConvertUtil.converter(orgInfo, new SysOrg());
	}
	
	
	@Override
	public SysOrg save(SysOrg sysOrg) {
		OrgInfoFacade orgFacade = FacadeUtil.getOrgInfoFacade();
		sysOrg.setShowOrder(this.findMaxOrderNo(sysOrg));
		
		OrgInfo orgInfo = (OrgInfo) BeanConvertUtil.converter(sysOrg, new OrgInfo());
		orgInfo = orgFacade.save(orgInfo);
		
		return (SysOrg) BeanConvertUtil.converter(orgInfo, new SysOrg());
	}

	@Override
	public void delete(String id) {
		OrgInfoFacade orgFacade = FacadeUtil.getOrgInfoFacade();
		List<SysOrg> allChildSysOrgs = new ArrayList<SysOrg>();
		//查询所有的子组织级别, 一并删除
		allChildSysOrgs = this.findAllChildsByParentId(id, allChildSysOrgs);
		StringBuilder ids = new StringBuilder();
		for (SysOrg sysOrg : allChildSysOrgs) {
			ids.append(sysOrg.getId()).append(",");
		}
		ids.append(id);
		orgFacade.delete(ids.toString());
	}

	
	@Override
	public SysOrg update(SysOrg sysOrg) {
		OrgInfoFacade orgFacade = FacadeUtil.getOrgInfoFacade();
		
		OrgInfo orgInfo = (OrgInfo) BeanConvertUtil.converter(sysOrg, new OrgInfo());
		orgFacade.update(orgInfo);
		return sysOrg;
	}

	@Override
	public Page<SysOrg> find(Specification<SysOrg> spec, Pageable pageable) {
		OrgInfoFacade orgFacade = FacadeUtil.getOrgInfoFacade();
		Map<String,String> param = BeanConvertUtil.getFilter(spec);
		int total = orgFacade.findTotal(param);
		List<OrgInfo> orgInfos = orgFacade.find(param, pageable.getPageNumber() + 1, pageable.getPageSize());
		List<SysOrg> sysOrgs = new ArrayList<SysOrg>(orgInfos.size());
		sysOrgs = this.converterOrgInfo(orgInfos, sysOrgs); //转换
		sysOrgs = removeIndependentOrg(sysOrgs); //移除独立组织
		return new PageImpl<SysOrg>(sysOrgs,pageable,total);
	}

	
	@Override
	public List<SysOrg> find(Specification<SysOrg> spec) {
		OrgInfoFacade orgFacade = FacadeUtil.getOrgInfoFacade();
		Map<String,String> param = BeanConvertUtil.getFilter(spec);
		List<OrgInfo> orgInfos = orgFacade.findByCondition(param);
		List<SysOrg> sysOrgs = new ArrayList<SysOrg>(orgInfos.size());
		sysOrgs = this.converterOrgInfo(orgInfos, sysOrgs);
		return sysOrgs;
	}

	@Override
	protected long findMaxOrderNo(SysOrg sysOrg) {
		OrgInfoFacade orgFacade = FacadeUtil.getOrgInfoFacade();
		long maxOrder = orgFacade.findMaxOrderNo(sysOrg.getParentId());
		return maxOrder + 1;
	}
	
	public List<SysOrg> findAll(){
		OrgInfoFacade orgFacade = FacadeUtil.getOrgInfoFacade();
		List<OrgInfo> orgInfos =orgFacade.findAll();
		List<SysOrg> sysOrgs = new ArrayList<SysOrg>(orgInfos.size());
		sysOrgs = this.converterOrgInfo(orgInfos, sysOrgs);
		return sysOrgs;
	}

	/**
	 * 是否拥有子节点
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> 修改数据权限</p>
	 * @date 2015年6月2日 上午10:43:14
	 * @param parentId
	 * @return
	 */
	public boolean hasChild(String parentId){
		OrgInfoFacade orgFacade = FacadeUtil.getOrgInfoFacade();
		List<OrgInfo> orgInfos = orgFacade.findChildsByParentId(parentId);
		if(orgInfos.size() > 0){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 查询直接的子组织级别
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> 查询子组织级别</p>
	 * @date 2015-6-9 19:14:55
	 * @param parentId
	 * @return
	 */
	public List<SysOrg> findChildsByParentId(String parentId) {
		OrgInfoFacade orgFacade = FacadeUtil.getOrgInfoFacade();
		List<SysOrg> sysOrgs = new ArrayList<SysOrg>();
		
		
		if(StringUtil.isBlank(parentId)){
			SysOrg sysOrg = this.getByID(Constants.Org.TOP);
			sysOrg.setIsParent(this.hasChild(sysOrg.getId()));
			sysOrgs.add(sysOrg);
		} else {
			List<OrgInfo> orgInfos = orgFacade.findChildsByParentId(parentId);
			for (OrgInfo orgInfo : orgInfos) {
				SysOrg sysOrg = (SysOrg) BeanConvertUtil.converter(orgInfo, new SysOrg());
				sysOrg.setIsParent(this.hasChild(sysOrg.getId()));
				sysOrgs.add(sysOrg);
			}
		}
		return sysOrgs;
	}

	/**
	 * 查找所有的子组织
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月7日 下午11:50:47
	 * @param parentId
	 * @param allChildSysOrgs
	 * @return 该组织下所有的子组织
	 */
	public List<SysOrg> findAllChildsByParentId(String parentId, List<SysOrg> allChildSysOrgs) {
		List<SysOrg> childSysOrgs= this.findChildsByParentId(parentId);
		for (SysOrg sysOrg : childSysOrgs) {
			allChildSysOrgs.add(sysOrg);
			allChildSysOrgs=findAllChildsByParentId(sysOrg.getId(), allChildSysOrgs);
		}
		return allChildSysOrgs;		
	}
	
	/**
	 * 内部实体list转换
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月9日 下午8:24:21
	 * @param orgInfos
	 * @param sysOrgs
	 * @return
	 */
	private List<SysOrg> converterOrgInfo(List<OrgInfo> orgInfos, List<SysOrg> sysOrgs) {
		for (OrgInfo orgInfo : orgInfos) {
			sysOrgs.add((SysOrg) BeanConvertUtil.converter(orgInfo, new SysOrg()));
		}
		orgInfos = null; //让gc回收
		return sysOrgs;
	}

	//将根节点下面的所有记录全部查出
	public List<SysOrg> getStaticTree() {
		List<SysOrg>  allSysOrgs = this.findAll();
		allSysOrgs = removeIndependentOrg(allSysOrgs); //移除独立组织
		
		List<SysOrg> staticOrgTree = this.fittingStaticTree(allSysOrgs);
		return staticOrgTree;
	}

	public List<SysOrg> getStaticTree4User() {
		OrgInfoFacade orgFacade = FacadeUtil.getOrgInfoFacade();
		List<SysOrg> staticOrgTree = new ArrayList<SysOrg>();
		SysOrg root = this.getByID(Constants.Org.TOP);
		//如果是超级管理员进来的, 则只显示第一层
		if(isSuperRole()){
			List<OrgInfo> orgInfos = orgFacade.findChildsByParentId(Constants.Org.TOP);
			List<SysOrg> childrens = new ArrayList<SysOrg>();
			childrens = this.converterOrgInfo(orgInfos, childrens);
			if(isSuperRole()){
				// 如果是superRole用户登录,先约定屏蔽掉独立组织(以后在开启)
				childrens = removeIndependentOrg(childrens); //移除独立组织
			}
			root.setChildren(childrens);
		}else{
			//获取当前登陆用户所管辖的组织
			String adminOrgId = this.getService(SysUserService.class).getByID(this.getLoginUser().getId()).getAdminOrgId();
			SysOrg adminOrg =  getByID(adminOrgId);
			
			//根据管辖的组织查该它的所有子孙组织, 并设置子组织
			List<SysOrg> childrens = this.findAllHierarchicalChildsByParentId(adminOrg.getId());
			adminOrg.setChildren(childrens);
			
			//根据管辖的组织查该组织的父组织, 查到根结点为止(不包括根结点)
			SysOrg unitOrg = this.findUnitOrgByAdminOrg(adminOrg);
			root.getChildren().add(unitOrg);
		}
		staticOrgTree.add(root);
		return staticOrgTree;
	}
	
	/**
	 * 根据管辖的组织查找上层单位组织
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年7月2日 上午9:32:55
	 * @param adminOrg
	 * @return
	 */
	private SysOrg findUnitOrgByAdminOrg(SysOrg adminOrg){
		SysOrg sysOrg = this.getByID(adminOrg.getParentId());
		if(Constants.Org.TOP.equals(sysOrg.getId())){
			return adminOrg;
		}
		sysOrg.getChildren().add(adminOrg);
		return findUnitOrgByAdminOrg(sysOrg);
	}
	
	/**
	 * 根据组织id, 查询该组织下所有的子孙组织, 并带有层级关系
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年7月2日 上午9:53:10
	 * @param parentId
	 * @return
	 */
	private List<SysOrg> findAllHierarchicalChildsByParentId(String parentId){
		List<SysOrg> sysOrgs = this.findChildsByParentId(parentId);
		if(sysOrgs == null){
			return sysOrgs;
		}
		for (SysOrg sysOrg : sysOrgs) {
			sysOrg.setChildren(this.findAllHierarchicalChildsByParentId(sysOrg.getId()));
		}
		return sysOrgs;
	};
	
	private List<SysOrg> fittingStaticTree(List<SysOrg> allSysOrgs) {
		Map<String,SysOrg> entitysMap = new LinkedHashMap<String,SysOrg>();
		List<SysOrg> result = new ArrayList<SysOrg>();
		
		for (SysOrg sysOrg : allSysOrgs) {
			entitysMap.put(sysOrg.getId(), sysOrg);
			sysOrg = null;
		}
		
		// 组装List(带层级)
		for (Map.Entry<String, SysOrg> entry : entitysMap.entrySet()) {
			// 当前遍历的资源
			SysOrg sysOrg = entry.getValue();
			
			// 往集合中添加根结点
			if (sysOrg.getParentId() == null) {
				result.add(sysOrg);
			}
			// 给当前资源的父资源的子资源集合添加当前资源
			if (entitysMap.containsKey(sysOrg.getParentId())) {
				entitysMap.get(sysOrg.getParentId()).getChildren().add(sysOrg);
			}
			sysOrg = null;
		}
		return result;
		
	}

	/**
	 * 获取组织下所有的用户
	 * @param orgId
	 * @return
	 */
	public List<SysUser> getChildUserInfoByOrgId(String orgId) {
		UserInfoFacade userInfoFacade = FacadeUtil.getUserInfoFacade();
		List<SysUser> users = new ArrayList<SysUser>();
		if(StringUtil.isNotBlank(orgId)){
			List<UserInfo> userInfos = userInfoFacade.findUsersByOrgId(orgId);
			converterUserInfo(userInfos, users);
		}
		return users;
	}

	private List<SysUser> converterUserInfo(List<UserInfo> userInfos, List<SysUser> users) {
		for (UserInfo userInfo : userInfos) {
			users.add((SysUser)BeanConvertUtil.converter(userInfo, new SysUser()));
		}
		userInfos = null; //让gc回收
		return users;
	}

	/**
	 * 组织是否有该角色
	 * @param orgId
	 * @param roleId
	 * @return
	 */
	public boolean hasRole(String orgId, String roleId) {
		OrgInfoFacade orgInfoFacade = FacadeUtil.getOrgInfoFacade();
		return orgInfoFacade.hasRole(orgId, roleId);
	}

	/**
	 * 移除独立组织
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月30日 下午4:05:52
	 * @param allSysOrgs
	 * @return
	 */
	private List<SysOrg> removeIndependentOrg(List<SysOrg> allSysOrgs){
		//组织管理下的树不显示 独立组织节点
		for (int i = allSysOrgs.size()-1; i >= 0; i--) {
			if(Constants.Org.INDEPENDENT_ORG.equals(allSysOrgs.get(i).getId())){
				allSysOrgs.remove(i);
				break;
			}
		}
		return allSysOrgs;
	}
	
	
	public void changeOrg(String orgId , String parentId ){
		OrgInfoFacade orgInfoFacade = FacadeUtil.getOrgInfoFacade();
		orgInfoFacade.changeOrg(orgId, parentId);

	}

	/**
	 * 为组织关联角色, 先删除所有旧角色, 再保存新角色
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年7月1日 下午1:49:25
	 * @param orgId 组织id 
	 * @param systemRoleId 系统角色id (例: systemId-roleId )
	 */
	public void grantRole(String orgId, String systemRoleId) {
		OrgInfoFacade orgInfoFacade = FacadeUtil.getOrgInfoFacade();
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
		orgInfoFacade.grantRoleBatch(orgId, roleSystemIds);
		
	}
}
