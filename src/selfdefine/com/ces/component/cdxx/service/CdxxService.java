package com.ces.component.cdxx.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ces.coral.lang.StringUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import static com.ces.component.farm.utils.FarmCommonUtil.queryPage;

@Component
public class CdxxService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

	@PersistenceContext
	private EntityManager entityManager;
	
    @Override
    public void setDao(TraceShowModuleDao dao){
    	super.setDao(dao);
    }
    
    public Map<String,Object>  getCdxx(){
    	String sql = "select T.CDMC as DDD,T.CDMC,T.CDBM from T_COMMON_CDXX T order by cdbm asc";
    	List<Map<String, String>> list =  (List<Map<String, String>>)entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    	return getResultData(list);
    }
    
    public Map<String,Object> getResultData(List<Map<String,String>> data){
    	Map<String,Object> result = new HashMap<String,Object>();
    	result.put("data", data);
    	return result;
    }

    public Object getShdqxxGrid() {
        String sql = "select T.CDMC,T.CDBM from t_common_cdxx T  order by cdbm asc";
        List<Map<String, String>> list =  (List<Map<String, String>>)entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return getResultData(list);
    }

    public Page searchCandi(PageRequest pageRequest,String value){
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<value.length();i++){
            if(i==(value.length()-1)){
                sb.append(value.charAt(i));
            }else
            {sb.append(value.charAt(i)+"%");}
        }

        String sql="select ID,CDMC,CDBM from T_COMMON_CDXX where CDMC LIKE '%"+sb.toString()+"%' or CDBM like '%"+sb.toString()+"%' order by CDBM ";
        List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
        list= DatabaseHandlerDao.getInstance().queryForMaps(sql);
        Map<String,Object> maps=new HashMap<String, Object>();
        maps.put("data",list);
//         maps.put("","F");
        return queryPage(pageRequest, sql);
//         return maps;
    }

    public static Page queryPage(PageRequest pageRequest, String sql) {
//        pageRequest = new PageRequest(pageRequest.getPageNumber() - 1, pageRequest.getPageSize());
        if (StringUtil.isBlank(sql)) {
            return null;
        }
        //查总数
        String count = "select count(*) as count from (" + sql + ")";
        Map map = DatabaseHandlerDao.getInstance().queryForMap(count);
        //总数
        long total = Long.parseLong(map.get("COUNT").toString());
        int begin = pageRequest.getOffset();
        int end = begin + pageRequest.getPageSize();
        if (begin > total) {
            int remainder = (int) (total % pageRequest.getPageSize());
            end = (int) total;
            begin = (int) (total - (remainder == 0 ? pageRequest.getPageSize() : remainder));
        }

        List<Map<String, Object>> content = DatabaseHandlerDao.getInstance().pageMaps(sql, begin, end);
        if (content == null) {
            return null;
        } else {
            return new PageImpl<Map<String, Object>>(content, pageRequest, total);
        }
    }
}