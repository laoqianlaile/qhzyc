package com.ces.component.qyptqttz.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ces.component.qyptqttz.dao.QyptqttzDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.service.StringIDDefineDaoService;
@Service
public class QyptqttzService extends StringIDDefineDaoService<StringIDEntity, QyptqttzDao>{
	@Autowired
	@Override
    public void setDao(QyptqttzDao dao){
		super.setDao(dao);
	}
	
	public List queryXxlbList(String zhbm){
		String sql = "SELECT X.ID,T.ZT,X.FSSJ FROM T_QYPT_XXSZ T ,T_QYPT_XXLB X WHERE T.ID = X.XXBH AND X.ZHBH='"+zhbm+"' AND ROWNUM <8 ORDER BY X.FSSJ DESC";
		return DatabaseHandlerDao.getInstance().queryForMaps(sql);
	}
	/**
	 * 根据账户编号获取authId
	 * @param zhbh
	 * @return
	 */
	public String queryAuthId(String zhbh){
		String sql = "SELECT T.AUTH_ID FROM T_QYPT_ZHGL T WHERE T.ZHBH='"+zhbh+"'";
		return (String) DatabaseHandlerDao.getInstance().queryForList(sql).get(0);
	}
}
