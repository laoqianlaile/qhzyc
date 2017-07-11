package com.ces.component.zzddxxlb.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.CommonUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ZzddxxlbService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    /**
     * 获得产品信息下拉列表数据
     *
     * @return List<Map<String,Object>>
     */
    public Object getCpxx() {
        String sql = "select t.cpbh,t.cpmc from t_zz_cpxxgl t where 1=1 and is_delete <> '1' " + AppDefineUtil.RELATION_AND + " QYBM = '" + SerialNumberUtil.getInstance().getCompanyCode() + "' " + AppDefineUtil.RELATION_AND + " is_delete <> '1'";
        List<Map<String, Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return dataList;
    }

    /**
     * 根据产品编号获得对应的产品详细信息
     *
     * @param cpbh
     * @return Map<String,Object>
     */
    public Object getCpxxByBh(String cpbh) {
        String sql = "select * from t_zz_cpxxgl t where 1=1 and is_delete <> '1'  and cpbh=?" + AppDefineUtil.RELATION_AND + " QYBM = '" + SerialNumberUtil.getInstance().getCompanyCode() + "' " + AppDefineUtil.RELATION_AND + " is_delete <> '1'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{cpbh});
    }

    /**
     * 默认权限过滤
     *
     * @return 默认过滤的 and QYBM =123124
     */
    public String defaultCode() {
        String code = SerialNumberUtil.getInstance().getCompanyCode();
        List<GrantedAuthority> authorities = CommonUtil.getUser().getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if ("xsjdgly".equals(authority.getAuthority()) || "销售阶段管理员".equals(authority.getAuthority())) {
                return AppDefineUtil.RELATION_AND + " KHID ='" + CommonUtil.getCurrentUserId() + "'" + AppDefineUtil.RELATION_AND + " QYBM = '" + code + "' ";

            }
        }
        return AppDefineUtil.RELATION_AND + " QYBM = '" + code + "' " + AppDefineUtil.RELATION_AND + " is_delete <> '1'";
    }

    @Override
    protected String buildCustomerFilter(String tableId,
                                         String componentVersionId, String moduleId, String menuId,
                                         Map<String, Object> paramMap) {
        return defaultCode();
    }

    /**
     * 保存方法：保存订单主表信息，散货订单信息、产品订单信息
     *
     * @param entityJson
     * @param sdEntitiesJson
     * @param cdEntitiesJson
     * @return 主表ID
     */
    @Transactional
    public String saveDdxx(String entityJson, String sdEntitiesJson, String cdEntitiesJson) {
        String id = "";
        JsonNode entityNode = JsonUtil.json2node(entityJson);
        Map<String, String> entitydataMap = node2map(entityNode);
        String ddbh = SerialNumberUtil.getInstance().getSerialNumber("ZZ", "ZZDDBH", false);
        entitydataMap.put("DDBH", ddbh);
        //判断当前用户是否为客户
        List<GrantedAuthority> authorities = CommonUtil.getUser().getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if ("xsjdgly".equals(authority.getAuthority()) || "销售阶段管理员".equals(authority.getAuthority())) {
                entitydataMap.put("KHID", CommonUtil.getCurrentUserId());
            }
        }
        //保存主表数据
        id = saveOne("T_ZZ_XSDDXX", entitydataMap);
        if (sdEntitiesJson != null) {//处理散货订单信息
            JsonNode sentityNode = JsonUtil.json2node(sdEntitiesJson);
            Map<String, String> sDataMap;
            for (int i = 0, len = sentityNode.size(); i < len; i++) {
                sDataMap = node2map(sentityNode.get(i));
                //放置关联字段数据
                sDataMap.put("PID", id);
                String sid = sDataMap.get(AppDefineUtil.C_ID);
                //判断是否为新增操作
                if (StringUtil.isNotEmpty(sid) && sid.startsWith("UNSAVE_")) {
                    sDataMap.remove(AppDefineUtil.C_ID);
                }
                sDataMap.remove("CZ");
                //执行保存操作
                saveOne("T_ZZ_DDSHXX", sDataMap);
            }
        }

        if (cdEntitiesJson != null) {//处理产品订单信息
            JsonNode sentityNode = JsonUtil.json2node(cdEntitiesJson);
            Map<String, String> cDataMap;
            for (int i = 0, len = sentityNode.size(); i < len; i++) {
                cDataMap = node2map(sentityNode.get(i));
                //放置关联字段数据
                cDataMap.put("PID", id);
                String cid = cDataMap.get(AppDefineUtil.C_ID);
                //判断是否为新增操作
                if (StringUtil.isNotEmpty(cid) && cid.startsWith("UNSAVE_")) {
                    cDataMap.remove(AppDefineUtil.C_ID);
                }
                cDataMap.remove("CZ");
                //执行保存操作
                saveOne("T_ZZ_DDCPXX", cDataMap);
            }
        }
        return id;
    }

    /**
     * 根据ID获得对应的订单详细信息
     *
     * @param id
     * @return
     */
    public Map<String, Object> searchDdxxById(String id) {
        String sql = "select * from T_ZZ_XSDDXX where id=?";
        return DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{id});
    }

    /**
     * 根据订单ID获得对应产品订单信息
     *
     * @param pid
     * @return Map<String,Object>
     */
    public Map<String, Object> searchCpddxx(String pid) {
        String sql = "select * from t_zz_ddcpxx where pid=?";
        return getGridAndListData(DatabaseHandlerDao.getInstance().queryForMaps(sql, new String[]{pid}));
    }

    /**
     * 根据订单ID获得对应散货订单信息
     *
     * @param pid
     * @return Map<String,Object>
     */
    public Map<String, Object> searchShddxx(String pid) {
        String sql = "select * from T_ZZ_DDSHXX where pid=?";
        return getGridAndListData(DatabaseHandlerDao.getInstance().queryForMaps(sql, new String[]{pid}));
    }

    /**
     * combobox与combogrid数据格式化处理
     *
     * @param dataMap
     * @return
     */
    public Map<String, Object> getGridAndListData(List<Map<String, Object>> dataMap) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("data", dataMap);
        return data;
    }

    /**
     * 判断是否有客户身份登录，如果是返回客户信息，反之返回null
     *
     * @return null或客户信息Map<String,Object>
     */
    public Map<String, Object> isKhlogin() {
        //判断当前用户是否为客户
        List<GrantedAuthority> authorities = CommonUtil.getUser().getAuthorities();
        for (GrantedAuthority authority : authorities) {
            //判断当前登录者是否有客户登录
            if ("xsjdgly".equals(authority.getAuthority()) || "销售阶段管理员".equals(authority.getAuthority())) {
                //获得登录名
                String loginName = CommonUtil.getUserNameById(CommonUtil.getCurrentUserId());
                //根据登录名查询客户详细信息
                String sql = "select * from t_zz_khxx where yhm = ?";
                return DatabaseHandlerDao.getInstance().queryForMap(sql, new String[]{loginName});
            }
        }
        return null;
    }

    public void deleteShxxOrCpxx(String id, String type) {
        String sql = "";
        if ("1".equals(type)) {
            sql = "delete from T_ZZ_DDSHXX  where id ='" + id + "' ";
        } else if ("2".equals(type)) {
            sql = "delete from T_ZZ_DDCPXX  where id ='" + id + "' ";
        }
        DatabaseHandlerDao.getInstance().executeSql(sql);
    }
}