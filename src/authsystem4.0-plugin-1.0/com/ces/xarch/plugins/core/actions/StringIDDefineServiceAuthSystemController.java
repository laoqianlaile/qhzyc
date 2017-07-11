package com.ces.xarch.plugins.core.actions;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.FatalException;
import com.ces.xarch.core.logger.Logger;
import com.ces.xarch.core.web.struts2.StringIDDefineServiceController;
import com.ces.xarch.plugins.common.utils.BeanConvertUtil;
import com.ces.xarch.plugins.common.utils.BeanUtil;
import com.ces.xarch.plugins.core.entity.SortEntity;
import com.ces.xarch.plugins.core.service.StringIDAuthSystemService;
import org.apache.struts2.ServletActionContext;

/**
 * 
 * 具有尾巴的控制类.
 * <p>描述:具有尾巴的控制类</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * @author 管俊 GuanJun <a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>
 * @date 2015-4-24 17:10:50
 * @version 1.0.2015.0424
 */
public abstract class StringIDDefineServiceAuthSystemController<T extends StringIDEntity, Service extends StringIDAuthSystemService<T>>
		extends StringIDDefineServiceController<T, Service> {

	private SortEntity sort;
	private Map<String,String> map = new HashMap<String,String>();
	
	/**
	 * 获取entity类名
	 * 
	 * @return
	 */
	protected String getModelName() {
		return this.getModelClass().getName();
	}

	
	
	@Override
	@Logger(action="添加",logger="${id}")
	public Object create() throws FatalException {
		setReturnData(this.getService().save(model));
		return SUCCESS;
	}
	
	/**
	 * 修改数据
	 */
	@Override
	@Logger(action = "修改", logger = "${id}")
	public Object update() throws FatalException {
		setReturnData(this.getService().update(model));
		return SUCCESS;
	}

	@Override
	@Logger(action = "删除", logger = "${id}")
	public Object destroy() throws FatalException {
		this.getService().delete(model.getId());
		return SUCCESS;
	}
	
	@Logger(action = "删除", logger = "${id}")
	public Object destroyModel() throws FatalException {
		this.getService().delete(model);
		return SUCCESS;
	}
	
	@Logger(action = "排序设置", logger = "${id}")
	public Object sort() throws Exception {
		this.getService().sort(this.getSort());
		return NONE;
	}

	/**
	 * 唯一性校验
	 */
	@Logger(action = "唯一性检测", logger = "${id}")
	public Object checkUnique() {
		List<T> entitys = this.getService().find(buildSpecification());
		this.returnData = false;
		if (entitys == null || entitys.size() == 0) {
			this.returnData = true;
		} else {
			if (!StringUtils.isBlank(model.getId())) {
				if (entitys.get(0).getId().equals(model.getId())) {
					this.returnData = true;
				}
			}
		}

		return NONE;
	}



	/**
	 * 获取SortEntity sort
	 * @author 管俊 GuanJun <a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>
	 * @date 2015年4月30日下午4:26:13
	 * @return the sort
	 */
	public SortEntity getSort() {
		return sort;
	}



	/**
	 * 设置SortEntity sort
	 * @author 管俊 GuanJun <a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>
	 * @date 2015年4月30日下午4:26:13
	 * @param sort the sort to set
	 */
	public void setSort(SortEntity sort) {
		this.sort = sort;
	}



	/**
	 * 获取Map<String,String> map
	 * @author 管俊 GuanJun <a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>
	 * @date 2015年4月30日下午4:26:13
	 * @return the map
	 */
	public Map<String, String> getMap() {
		return map;
	}



	/**
	 * 设置Map<String,String> map
	 * @author 管俊 GuanJun <a href="mailto:guan.jun@cesgroup.com.cn">guan.jun@cesgroup.com.cn</a>
	 * @date 2015年4月30日下午4:26:13
	 * @param map the map to set
	 */
	public void setMap(Map<String, String> map) {
		this.map = map;
	}


	/**
	 * 获取request参数
	 * @param param
	 * @return
	 */
	protected String getParameter(String param) {
		return ServletActionContext.getRequest().getParameter(param);
	}
	
}
