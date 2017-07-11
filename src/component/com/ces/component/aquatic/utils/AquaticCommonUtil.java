package com.ces.component.aquatic.utils;

import ces.coral.lang.StringUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.xarch.core.web.listener.XarchListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工具类
 * Created by bdz on 2015/7/7.
 */
@Component
public class AquaticCommonUtil {
    public static AquaticCommonUtil getInstance(){
        return XarchListener.getBean(AquaticCommonUtil.class);
    }
    /**
     * 将page转换成coral4.0能用的格式
     * @param page
     * @param pageSize
     * @param pageNo
     * @return
     */
    public Map putPageToMap(Page page,String pageSize,String pageNo){
        if(page==null){
            return null;
        }
        Map map = new HashMap();
        map.put("data", page.getContent());
        map.put("total", page.getTotalElements());
        map.put("pageSize", pageSize);
        map.put("pageNumber", pageNo);
        map.put("totalPages", page.getTotalPages());
        return map;
    }
    /**
     * 分页查询
     *
     * @param pageRequest
     * @param sql
     * @return Object
     */
    public Page queryPage(PageRequest pageRequest, String sql) {
        pageRequest = new PageRequest(pageRequest.getPageNumber()-1,pageRequest.getPageSize());
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
