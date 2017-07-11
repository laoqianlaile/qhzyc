package com.ces.component.qyptsjlxbm.service;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class QyptsjlxbmService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {
	/**
	 * 根据类型编码在数据编码表里查找数据
	 * @param pageRequest
	 * @param lxbm
	 * @return
	 * @author zhaoben
	 */
    public Page getLxData(PageRequest pageRequest,String lxbm){
        String sql = "select t.ID,t.SJBM,t.SJMC,t.LXBM from t_common_sjlx_code t where t.lxbm = '" + lxbm + "' order by t.SJBM";
//        List<Map<String,Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
//        Map<String,Object> dataMap = new HashMap<String, Object>();
//        dataMap.put("data",list);
        return queryPage(pageRequest,sql);
    }

    /**
	 * 分页查询
	 * @param pageRequest
	 * @param sql
	 * @return Object
	 */
	public Page queryPage(PageRequest pageRequest,String sql) {
		//查总数
		String count = "select count(0) as count from ("+sql+") dd";
		Map map = DatabaseHandlerDao.getInstance().queryForMap(count);
		//总数
		  long total = Long.parseLong( map.get("COUNT").toString());
		  int begin = pageRequest.getOffset();
	      int end   = begin + pageRequest.getPageSize();
	      if (begin > total) {
	            int remainder = (int) (total%pageRequest.getPageSize());
	            end = (int) total;
	            begin = (int) (total - (remainder == 0 ? pageRequest.getPageSize() : remainder));
	        }

		List<Map<String, Object>> content = DatabaseHandlerDao.getInstance().pageMaps(sql, begin, end);
		return new PageImpl<Map<String, Object>>(content, pageRequest, total);
	}
    
}