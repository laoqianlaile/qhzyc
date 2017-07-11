package com.ces.component.virtural.service;

import com.ces.component.virtural.dao.VirturalLogicDao;
import com.ces.config.service.base.ShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.service.StringIDDefineDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Map;

/**
 * Created by WILL on 15/3/26.
 */
@Component
public class VirturalLogicService extends StringIDDefineDaoService<StringIDEntity, VirturalLogicDao> {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public void setDao(VirturalLogicDao dao) {
		super.setDao(dao);
	}

	/**
	 * 保存卡用户信息
	 */
	@Transactional
	public void saveUserInfo(String tableName, Map<String, String> dataMap) {
		getService(ShowModuleDefineDaoService.class).save(tableName, dataMap, null);
	}

}
