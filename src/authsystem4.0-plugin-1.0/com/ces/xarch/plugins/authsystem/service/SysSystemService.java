package com.ces.xarch.plugins.authsystem.service;

import ces.sdk.system.bean.SystemInfo;
import ces.sdk.system.facade.SystemFacade;

import com.ces.xarch.core.security.entity.SysSystem;
import com.ces.xarch.plugins.authsystem.utils.FacadeUtil;
import com.ces.xarch.plugins.common.utils.BeanConvertUtil;
import com.ces.xarch.plugins.core.service.StringIDAuthSystemService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SysSystemService extends StringIDAuthSystemService<SysSystem>{

	@Override
	public void bindingDao(Class<SysSystem> entityClass) {}
	
	@Override
	public SysSystem getByID(String id) {
		SystemFacade systemFacade = FacadeUtil.getSystemFacade();
		SystemInfo systemInfo = systemFacade.findByID(id);
		return (SysSystem) BeanConvertUtil.converter(systemInfo, new SysSystem());
	}

	@Override
	public SysSystem save(SysSystem sysSystem) {
		SystemFacade systemFacade = FacadeUtil.getSystemFacade();
		sysSystem.setShowOrder(this.getMaxOrderNo());
		
		SystemInfo systemInfo = (SystemInfo) BeanConvertUtil.converter(sysSystem, new SystemInfo());
		systemInfo = systemFacade.save(systemInfo);
		
		return (SysSystem) BeanConvertUtil.converter(systemInfo, new SysSystem());
	}

	@Override
	public void delete(String id) {
		SystemFacade systemFacade = FacadeUtil.getSystemFacade();
		systemFacade.delete(id);
	}

	@Override
	public SysSystem update(SysSystem sysSystem) {
		SystemFacade systemFacade = FacadeUtil.getSystemFacade();
		
		SystemInfo systemInfo = (SystemInfo) BeanConvertUtil.converter(sysSystem, new SystemInfo());
		systemFacade.update(systemInfo);
		return sysSystem;
	}

	@Override
	public Page<SysSystem> find(Specification<SysSystem> spec, Pageable pageable) {
		SystemFacade systemFacade = FacadeUtil.getSystemFacade();
		List<SysSystem> sysSystems = new ArrayList<SysSystem>();
		Map<String,String> param = BeanConvertUtil.getFilter(spec);
		int total = systemFacade.findTotal(param);
		List<SystemInfo> systemInfos = systemFacade.find(param,pageable.getPageNumber() + 1, pageable.getPageSize());
		sysSystems = this.converterSystemInfo(systemInfos, sysSystems);
		return new PageImpl<SysSystem>(sysSystems,pageable,total);
	}

	@Override
	public List<SysSystem> find(Specification<SysSystem> spec) {
		SystemFacade systemFacade = FacadeUtil.getSystemFacade();
		List<SysSystem> sysSystems = new ArrayList<SysSystem>();
		Map<String,String> param = BeanConvertUtil.getFilter(spec);
		List<SystemInfo> systemInfos = systemFacade.findByCondition(param);
		sysSystems = this.converterSystemInfo(systemInfos, sysSystems);
		return sysSystems;
	}

	protected long getMaxOrderNo() {
		SystemFacade systemFacade = FacadeUtil.getSystemFacade();
		long maxOrder = systemFacade.findMaxOrderNo();
		return maxOrder + 1;
	}

	public List<SysSystem> getAllSystems(Map<String, String> param) {
		SystemFacade systemFacade = FacadeUtil.getSystemFacade();
		List<SysSystem> sysSystems = new ArrayList<SysSystem>();//系统list
		List<SystemInfo> systemInfos = new ArrayList<SystemInfo>();
		systemInfos = systemFacade.findSystems(param);
		for(SystemInfo systeminfo : systemInfos){
			SysSystem sysSystem = new SysSystem();
			sysSystem = (SysSystem) BeanConvertUtil.converter(systeminfo, new SysSystem());
			//sysSystem.setIsParent(systemFacade.hasResource(sysSystem.getId()));
			sysSystems.add(sysSystem);
		}
		return sysSystems;
	}
	
	private List<SysSystem> converterSystemInfo(List<SystemInfo> systemInfos, List<SysSystem> sysSystems){
		for (SystemInfo systemInfo : systemInfos) {
			sysSystems.add((SysSystem) BeanConvertUtil.converter(systemInfo, new SysSystem()));
		}
		systemInfos = null;
		return sysSystems;
	}
	
	private List<SystemInfo> converterSysSystem(List<SysSystem> sysSystems, List<SystemInfo> systemInfos){
		for (SysSystem sysSystem : sysSystems) {
			systemInfos.add((SystemInfo) BeanConvertUtil.converter(sysSystem, new SystemInfo()));
		}
		return systemInfos;
	}
	
	@Override
	protected long findMaxOrderNo(SysSystem model) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * 系统下是否存在资源
	 * @param systemId
	 * @return
	 */
	public boolean hasResource(String systemId) {
		SystemFacade systemFacade = FacadeUtil.getSystemFacade();
		return systemFacade.hasResource(systemId);
	}

    /**
     * 系统下是否存在角色
     * @param systemId
     * @return
     */
    public boolean hasRole(String systemId){
        SystemFacade systemFacade = FacadeUtil.getSystemFacade();
        return systemFacade.hasRole(systemId);
    }
    
    public List<SysSystem> findAll(){
    	SystemFacade systemFacade = FacadeUtil.getSystemFacade();
    	List<SystemInfo> systemInfos = systemFacade.findAll();
    	List<SysSystem> sysSystems = new ArrayList<SysSystem>();
    	sysSystems = this.converterSystemInfo(systemInfos, sysSystems );
    	return sysSystems;
    }

	public List<SysSystem> findSystemsByOrgId(String parentOrgId) {
		SystemFacade systemFacade = FacadeUtil.getSystemFacade();
		List<SystemInfo> systemInfos = systemFacade.findSystemsByOrgId(parentOrgId);
    	List<SysSystem> sysSystems = new ArrayList<SysSystem>();
    	sysSystems = this.converterSystemInfo(systemInfos, sysSystems );
    	return sysSystems;
	}
}
