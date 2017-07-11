package com.ces.component.sensor.utils;

import ces.coral.lang.StringUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/9.
 */
public class SensorCommonUtil {

    public static Map<String, String> tokenMap = new HashMap<String, String>();
    public static Map<String, String> loginuserMap = new HashMap<String, String>();

    /**
     * 将page转换成coral4.0能用的格式
     *
     * @param page
     * @param pageSize
     * @param pageNo
     * @return
     */
    public static Map putPageToMap(Page page, String pageSize, String pageNo) {
        if (page == null) {
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
    public static Page queryPage(PageRequest pageRequest, String sql) {
        pageRequest = new PageRequest(pageRequest.getPageNumber() - 1, pageRequest.getPageSize());
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


    /**
     * 根据追溯码判断商品种类 包装或散货
     *
     * @param zsm
     * @return tableName
     */
    public static String getTableNameByZsm(String zsm) {
        String tableName = "";
        String sql = "select t.id from t_zz_ccbzcpxx t where t.cpzsm = ?";
        List bzList = DatabaseHandlerDao.getInstance().queryForList(sql, new Object[]{zsm});
        if (bzList != null && bzList.size() > 0) {
            tableName = "T_ZZ_CCBZCPXX";
            return tableName;
        }
        String shSql = "select t.id from t_zz_ccshxx t where t.cpzsm = ?";
        List shList = DatabaseHandlerDao.getInstance().queryForList(shSql, new Object[]{zsm});
        if (shList != null && shList.size() > 0) {
            tableName = "T_ZZ_CCSHXX";
            return tableName;
        }
        return tableName;
    }
}
