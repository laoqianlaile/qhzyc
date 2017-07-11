package com.ces.component.yzzsda.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.xarch.core.entity.StringIDEntity;

@Component
public class YzzsdaService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setDao(TraceShowModuleDao dao) {
        super.setDao(dao);
    }


    /**
     * 根据养殖的信息进行猪舍的空闲状态管理
     *
     * @param zsbh
     * @param zzzt
     */
    public void updataSyzt(String csbh, String syzt) {
        String sql = "update T_YZ_ZSDA set SYZT='" + syzt + "'  WHERE QYZT=1 and csbh='" + csbh + "' " + defaultCode();
        DatabaseHandlerDao.getInstance().jdbcExecuteSql(sql);
    }

    public Object getZsdaFzr(String csbh) {
        String sql = "select T.FZR,T.FZRBH  from T_YZ_ZSDA T WHERE T.csbh='" + csbh + "' " + defaultCode();
        List<Map<String, String>> list = (List<Map<String, String>>) entityManager
                .createNativeQuery(sql).unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return list;
    }


    public String checkOnlybh(String csbh) {
        String sql = "select T.CSBH,T.CSLX,T.WZ,T.MJ from T_YZ_ZSDA T where T.CSBH='" + csbh + "' " + defaultCode();
        List<Map<String, String>> list = entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        if (list == null || list.isEmpty()) {
            return "none";
        } else {
            return "have";
        }
    }


    /**
     * 获得下拉列表的数据
     *
     * @return
     */
    public Map<String, Object> getZsda(String qyzt, String syzt, String self) {
        StringBuffer sql = new StringBuffer("select T.csbh, z.name as CSLX from T_YZ_ZSDA T,T_XTPZ_CODE z where z.code_type_code ='ZSLX' and z.value=T.cslx ");
        sql.append(" and ((T.QYZT=" + qyzt);
        sql.append(" and T.SYZT=" + syzt + ")");
        sql.append(" or  T.csbh='" + self + "') ");
        sql.append(defaultCode());
        sql.append("  order by QYZT ASC,SYZT DESC,CSBH DESC");
        List<Map<String, String>> list = (List<Map<String, String>>) entityManager.createNativeQuery(sql.toString()).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return getResultData(list);
    }

    public Map<String, Object> getResultData(List<Map<String, String>> data) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", data);
        return result;
    }


    /**
     * 默认权限过滤
     *
     * @return
     */
    public String defaultCode() {
        String code = SerialNumberUtil.getInstance().getCompanyCode();
        String defaultCode = " ";
        if (code != null && !"".equals(code))
            defaultCode = AppDefineUtil.RELATION_AND + " QYBM = '" + code + "' ";
        return defaultCode;
    }

    @Override
    protected String buildCustomerFilter(String tableId,
                                         String componentVersionId, String moduleId, String menuId,
                                         Map<String, Object> paramMap) {
        // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
        return defaultCode();
    }
}