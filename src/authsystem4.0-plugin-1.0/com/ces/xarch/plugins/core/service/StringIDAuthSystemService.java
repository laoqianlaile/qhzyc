package com.ces.xarch.plugins.core.service;

import java.util.Collection;
import java.util.List;

import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.FatalException;
import com.ces.xarch.core.security.entity.SysUser;
import com.ces.xarch.core.service.StringIDService;
import com.ces.xarch.plugins.core.entity.ISortEntity;
import com.ces.xarch.plugins.core.entity.SortEntity;

import org.apache.struts2.ServletActionContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

/**
 * 具有尾巴的服务类.
 * <p>描述:具有尾巴的服务类</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 *
 * @author 管俊 GuanJun <a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>
 * @version 1.0.2015.0424
 * @date 2015-4-24 16:03:40
 */
public abstract class StringIDAuthSystemService<T extends StringIDEntity> extends StringIDService<T> {

	protected SysUser getLoginUser() {
		SecurityContext sc = SecurityContextHolder.getContext();

		if (sc == null || sc.getAuthentication() == null) {
			sc = (SecurityContext) ServletActionContext.getRequest().getSession().getAttribute("SPRING_SECURITY_CONTEXT");
		}

		if (sc != null && sc.getAuthentication() != null) {
			return (SysUser) sc.getAuthentication().getPrincipal() ;
		}

		return null;
	}

	protected Collection<? extends GrantedAuthority> getAuthorities(){
		SecurityContext sc = SecurityContextHolder.getContext();

		if (sc == null || sc.getAuthentication() == null) {
			sc = (SecurityContext) ServletActionContext.getRequest().getSession().getAttribute("SPRING_SECURITY_CONTEXT");
		}

		if (sc != null && sc.getAuthentication() != null) {
			Collection<? extends GrantedAuthority> authorities = sc.getAuthentication().getAuthorities();
			return authorities;
		}
		return null;
	}

	/**
	 * 查唯一
	 */
	@Override
	public abstract T getByID(String id);

	/**
	 * 增
	 */
	@Override
	@Transactional
	public abstract T save(T entity);

	/**
	 * 删
	 */
	@Override
	@Transactional
	public abstract void delete(String id);

	@Override
	@Transactional
	public void delete (T entity){
		super.delete(entity);
	}
	
	/**
	 * 改
	 */
	@Transactional
	public abstract T update(T entity);

	/**
	 * 分页查询
	 */
	@Override
	public abstract Page<T> find(Specification<T> spec, Pageable pageable);

	/**
	 * 按条件查询
	 */
	@Override
	public abstract List<T> find(Specification<T> spec);

	/**
	 * 获取最大orderNo，并且已经+1
	 *
	 * @param model
	 * @return
	 * @author Niklaus(管俊 GuanJun<a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>)
	 * @comments:<p> 获取最大orderNo，并且已经+1</p>
	 * @date 2015年4月27日下午9:13:20
	 */
	protected abstract long findMaxOrderNo(T model);

	/**
	 * 排序算法
	 *
	 * @param sort
	 */
	public void sort(SortEntity sort) {
		String[] beforeIDs = sort.getSortBeforeIDs().split(",");
		String[] afterIDs = sort.getSortAfterIDs().split(",");
		if (afterIDs.length == 0)
			return;
		T entity = this.getByID(beforeIDs[0]);
		if (!(entity instanceof ISortEntity)) {
			throw new FatalException("实体类没有集成 ISortEntity接口,无法排序");
		}
		ISortEntity sortEntity = (ISortEntity) entity;
		long orderNo = 1L;
		if (sortEntity.getShowOrder() != null) {
			orderNo = sortEntity.getShowOrder();
		}
		for (int i = 0; i < afterIDs.length; i++) {
			entity = this.getByID(afterIDs[i]);
			sortEntity = (ISortEntity) entity;
			sortEntity.setShowOrder(orderNo);
			this.update(entity);
			orderNo++;
		}
	}

	/**
	 * 当前用户是否有某一角色
	 * @return
	 */
	public boolean currentUserhasRole(String roleKey) {
		if (isSuperRole()) {
			return true;
		}
		SysUser sysUser = (SysUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<GrantedAuthority> authorities = sysUser.getAuthorities();
		for (GrantedAuthority authoritie : authorities) {
			if (roleKey.equals(authoritie.getAuthority())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 当前用户是否是超级管理员
	 * @return
	 */
	public boolean isSuperRole() {
		SysUser sysUser = (SysUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<GrantedAuthority> authorities = sysUser.getAuthorities();
		for (GrantedAuthority authoritie : authorities) {
			if ("superrole".equals(authoritie.getAuthority())) {
				return true;
			}
		}
		return false;
	}

}
