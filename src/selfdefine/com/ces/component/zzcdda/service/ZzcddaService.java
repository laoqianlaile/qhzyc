package com.ces.component.zzcdda.service;

import org.springframework.stereotype.Component;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.zzcdda.dao.ZzcddaDao;
import com.ces.component.zzcdda.entity.ZzcddaEntity;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;

@Component
public class ZzcddaService extends TraceShowModuleDefineDaoService<ZzcddaEntity, ZzcddaDao> {

	/**
	 * 通过当前登录者的账号编码获得产地档案
	 * @return CddaEntity
	 */
	public ZzcddaEntity getByQybm(){
		return getDao().getByQybm(getQybm());
	}
	
	/**
	 * 通过登录者信息获得账号编码
	 * @return
	 */
	public String  getQybm(){
		return SerialNumberUtil.getInstance().getCompanyCode();
	}
}
