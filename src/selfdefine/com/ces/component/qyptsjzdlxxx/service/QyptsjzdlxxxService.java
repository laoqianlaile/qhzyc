package com.ces.component.qyptsjzdlxxx.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;

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
public class QyptsjzdlxxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    public Page getFlData(PageRequest pageRequest,String lxbm){
        String sql = "select t.ID, t.LXBM,t.LXMC,t.FLMC from t_common_sjlx t where t.flbm = '" + lxbm + "'and t.flag=0 order by t.CREATE_TIME ";
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
		String count = "select count(*) as count from ("+sql+")";
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