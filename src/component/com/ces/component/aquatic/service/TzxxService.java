package com.ces.component.aquatic.service;

import com.ces.component.aquatic.dao.TzxxDao;
import com.ces.component.aquatic.entity.TzxxEntity;
import com.ces.xarch.core.service.StringIDDefineDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by 黄翔宇 on 15/7/7.
 */
@Component
public class TzxxService extends StringIDDefineDaoService<TzxxEntity, TzxxDao> {

	@Override
	@Autowired
	public void setDao(TzxxDao dao) {
		super.setDao(dao);
	}
}
