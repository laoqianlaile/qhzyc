package com.ces.xarch.plugins.authsystem.service;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ces.sdk.system.bean.OrgTypeInfo;
import ces.sdk.system.facade.OrgTypeInfoFacade;
import ces.sdk.util.StringUtil;

import com.ces.xarch.core.security.entity.SysOrgType;
import com.ces.xarch.plugins.authsystem.utils.FacadeUtil;
import com.ces.xarch.plugins.common.global.Constants;
import com.ces.xarch.plugins.common.utils.BeanConvertUtil;
import com.ces.xarch.plugins.core.service.StringIDAuthSystemService;

/**
 * 组织级别服务类
 * <p>描述:组织级别服务类</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
 * @date 2015年6月2日 上午10:56:40
 * @version 1.0.2015.0601
 */
@Component
public class SysOrgTypeService extends StringIDAuthSystemService<SysOrgType>{
	
	@Override
	public void bindingDao(Class<SysOrgType> entityClass) {}

	@Override
	public SysOrgType getByID(String id) {
		OrgTypeInfoFacade orgTypeFacade = FacadeUtil.getOrgTypeFacade();
		OrgTypeInfo orgTypeInfo = orgTypeFacade.findByID(id);
		return (SysOrgType) BeanConvertUtil.converter(orgTypeInfo, new SysOrgType());
	}


	@Override
	public Page<SysOrgType> find(Specification<SysOrgType> spec,
			Pageable pageable) {
		OrgTypeInfoFacade orgTypeFacade = FacadeUtil.getOrgTypeFacade();
		List<SysOrgType> sysOrgTypes = new ArrayList<SysOrgType>();
		Map<String,String> param = BeanConvertUtil.getFilter(spec);
		int total = orgTypeFacade.findTotal(param);
		List<OrgTypeInfo> orgTypeInfos = orgTypeFacade.find(param,pageable.getPageNumber() + 1, pageable.getPageSize());
		sysOrgTypes = this.converterOrgType(orgTypeInfos, sysOrgTypes);
		return new PageImpl<SysOrgType>(sysOrgTypes,pageable,total);
	}


	@Override
	public List<SysOrgType> find(Specification<SysOrgType> spec) {
		OrgTypeInfoFacade orgTypeFacade = FacadeUtil.getOrgTypeFacade();
		List<SysOrgType> sysOrgTypes = new ArrayList<SysOrgType>();
		Map<String,String> param = BeanConvertUtil.getFilter(spec);
		List<OrgTypeInfo> orgTypes = orgTypeFacade.findByCondition(param);
		sysOrgTypes = this.converterOrgType(orgTypes, sysOrgTypes);
		return sysOrgTypes;
	}
	
	@Override
	@Transactional
	public SysOrgType save(SysOrgType sysOrgType) {
		OrgTypeInfoFacade orgTypeFacade = FacadeUtil.getOrgTypeFacade();
		sysOrgType.setShowOrder(this.findMaxOrderNo(sysOrgType));
		
		OrgTypeInfo orgTypeInfo = (OrgTypeInfo) BeanConvertUtil.converter(sysOrgType, new OrgTypeInfo());
		orgTypeInfo = orgTypeFacade.save(orgTypeInfo);
		
		return (SysOrgType) BeanConvertUtil.converter(orgTypeInfo, new SysOrgType());
	}

	@Override
	@Transactional
	public SysOrgType update(SysOrgType sysOrgType) {
		OrgTypeInfoFacade orgTypeFacade = FacadeUtil.getOrgTypeFacade();
		
		OrgTypeInfo orgTypeInfo = (OrgTypeInfo) BeanConvertUtil.converter(sysOrgType, new OrgTypeInfo());
		orgTypeFacade.update(orgTypeInfo);
		return sysOrgType;
	}

	@Override
	protected long findMaxOrderNo(SysOrgType sysOrgType) {
		OrgTypeInfoFacade orgTypeFacade = FacadeUtil.getOrgTypeFacade();
		long maxOrder = orgTypeFacade.findMaxOrderNo(sysOrgType.getParentId());
		return maxOrder + 1;
	}

	/**
	 * 查询直接的子组织级别
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> 查询子组织级别</p>
	 * @date 2015年6月1日 下午7:02:57
	 * @param parentId
	 * @return
	 */
	public List<SysOrgType> findChildsByParentId(String parentId) {
		OrgTypeInfoFacade orgTypeFacade = FacadeUtil.getOrgTypeFacade();
		List<SysOrgType> sysOrgTypes = new ArrayList<SysOrgType>();
		
		if(StringUtil.isBlank(parentId)){
			SysOrgType sysOrgType = this.getByID(Constants.OrgType.TOP);
			sysOrgType.setIsParent(this.hasChild(sysOrgType.getId()));
			sysOrgTypes.add(sysOrgType);
		} else {
			List<OrgTypeInfo> orgTypeInfos = orgTypeFacade.findChildsByParentId(parentId);
			for (OrgTypeInfo orgTypeInfo : orgTypeInfos) {
				SysOrgType sysOrgType = (SysOrgType) BeanConvertUtil.converter(orgTypeInfo, new SysOrgType());
				sysOrgType.setIsParent(this.hasChild(sysOrgType.getId()));
				sysOrgTypes.add(sysOrgType);
			}
		}
		return sysOrgTypes;
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
		OrgTypeInfoFacade orgTypeFacade = FacadeUtil.getOrgTypeFacade();
		List<OrgTypeInfo> orgTypeInfos = orgTypeFacade.findChildsByParentId(parentId);
		if(orgTypeInfos.size() > 0){
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 查找所有的子组织级别
	 * 
	 * <p>Company:上海中信信息发展股份有限公司</p>
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> </p>
	 * @date 2015年6月7日 下午11:50:47
	 * @param parentId
	 * @param sysOrgTypesAll
	 * @return
	 */
	public List<SysOrgType> findAllChildsByParentId(String parentId,List<SysOrgType> sysOrgTypesAll){
		List<SysOrgType> childSysOrgTypes= this.findChildsByParentId(parentId);
		for (SysOrgType sysOrgType : childSysOrgTypes) {
			sysOrgTypesAll.add(sysOrgType);
			sysOrgTypesAll=findAllChildsByParentId(sysOrgType.getId(), sysOrgTypesAll);
		}
		return sysOrgTypesAll;		
	}
	
	@Override
	@Transactional
	public void delete(String id) {
		OrgTypeInfoFacade orgTypeFacade = FacadeUtil.getOrgTypeFacade();
		List<SysOrgType> allChildSysOrgTypes = new ArrayList<SysOrgType>();
		//查询所有的子组织级别, 一并删除
		allChildSysOrgTypes = this.findAllChildsByParentId(id, allChildSysOrgTypes);
		StringBuilder ids = new StringBuilder();
		for (SysOrgType sysOrgType : allChildSysOrgTypes) {
			ids.append(sysOrgType.getId()).append(",");
		}
		ids.append(id);
		orgTypeFacade.delete(ids.toString());
	}
	
	
	private List<SysOrgType> converterOrgType(List<OrgTypeInfo> orgTypeInfos, List<SysOrgType> sysOrgTypes){
		for (OrgTypeInfo orgTypeInfo : orgTypeInfos) {
			sysOrgTypes.add((SysOrgType) BeanConvertUtil.converter(orgTypeInfo, new SysOrgType()));
		}
		return sysOrgTypes;
	}
	
	private List<OrgTypeInfo> converterSysOrgType(List<SysOrgType> sysOrgTypes, List<OrgTypeInfo> orgTypeInfos){
		for (SysOrgType sysOrgType : sysOrgTypes) {
			orgTypeInfos.add((OrgTypeInfo) BeanConvertUtil.converter(sysOrgType, new OrgTypeInfo()));
		}
		return orgTypeInfos;
	}

	public List<SysOrgType> getStaticTree() {
		List<SysOrgType>  allSysOrgTypes = this.findAll();
		List<SysOrgType> staticOrgTypeTree = this.fittingStaticTree(allSysOrgTypes);
		return staticOrgTypeTree;
	}

	private List<SysOrgType> fittingStaticTree(List<SysOrgType> allSysOrgTypes) {
		Map<String,SysOrgType> entitysMap = new LinkedHashMap<String,SysOrgType>();
		List<SysOrgType> result = new ArrayList<SysOrgType>();
		
		for (SysOrgType sysOrgType : allSysOrgTypes) {
			entitysMap.put(sysOrgType.getId(), sysOrgType);
			sysOrgType = null;
		}
		
		// 组装List(带层级)
		for (Map.Entry<String, SysOrgType> entry : entitysMap.entrySet()) {
			// 当前遍历的资源
			SysOrgType sysOrgType = entry.getValue();
			
			// 往集合中添加根结点
			if (sysOrgType.getParentId() == null) {
				result.add(sysOrgType);
			}
			// 给当前资源的父资源的子资源集合添加当前资源
			if (entitysMap.containsKey(sysOrgType.getParentId())) {
				entitysMap.get(sysOrgType.getParentId()).getChildren().add(sysOrgType);
			}
			sysOrgType = null;
		}
		return result;
	}

	public List<SysOrgType> findAll() {
		OrgTypeInfoFacade orgTypeFacade = FacadeUtil.getOrgTypeFacade();
		List<OrgTypeInfo> orgTypeInfos =orgTypeFacade.findAll();
		List<SysOrgType> sysOrgTypes = new ArrayList<SysOrgType>(orgTypeInfos.size());
		sysOrgTypes = this.converterOrgType(orgTypeInfos, sysOrgTypes);
		return sysOrgTypes;
	}

}
