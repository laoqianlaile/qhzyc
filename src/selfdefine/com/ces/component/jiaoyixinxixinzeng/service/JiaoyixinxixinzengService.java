package com.ces.component.jiaoyixinxixinzeng.service;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.component.cspt.entity.TCsptZsxxEntity;
import com.ces.component.jiaoyixinxixinzeng.dao.JiaoyixinxixinzengDao;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.TraceChainUtil;
import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.databind.JsonNode;

@Component
public class JiaoyixinxixinzengService extends TraceShowModuleDefineDaoService<StringIDEntity, JiaoyixinxixinzengDao> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setDao(JiaoyixinxixinzengDao dao) {
        super.setDao(dao);
    }

    //获取进场理货编号
    public Object getJclhbhByPfsbm(String pfsbm) {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String compareDate = df.format(new Date(date.getTime() - 4 * 24 * 60 * 60 * 1000));
        String sql = "select T.JCLHBH,T.PFSMC,T.PFSBM,T.JCRQ,T.ID from T_PC_JCLHXX T where T.PFSBM LIKE '%" + pfsbm + "%' and T.JCRQ>='" + compareDate + "'" + defaultCode() + " order by T.JCLHBH desc";
        List<Map<String, Object>> map = (List<Map<String, Object>>) entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return map;
    }

    //获取商品名称
    public Object getSpmcByPid(String id) {
        String sql = "select T.SPBM,T.SPMC from T_PC_JCLHMXXX T where T.PID LIKE '%" + id + "%'";
        List<Map<String, Object>> map = (List<Map<String, Object>>) entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return map;
    }

    //获取理货信息ID
    public Object getIdByJclhbh(String jclhbh) {
        Object result = getDao().getIdByJclhbh(jclhbh);
        return result;
    }

    public Object getPfsmcByJclhbh(String jclhbh) {
        String sql = "select T.PFSMC,T.PFSBM from T_PC_JCLHXX T where T.JCLHBH ='" + jclhbh + "'";
        List<Map<String, Object>> map = (List<Map<String, Object>>) entityManager.createNativeQuery(sql).unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return map;
    }

    //获取交易凭证号和到达地
    public Object getInfoBySpbm(String spbm, String jclhbh) {
        List<Object[]> info = getDao().getInfoByJclhbh(spbm, jclhbh);
        //Map<String,Object> item = null;
        Object[] firstInfo = info.get(0);
        return firstInfo;
    }

    //获取商品到达地
    public String getDddByLssbm(String lssbm) {
        String ddd = getDao().getDddByLssbm(lssbm) != null ? getDao().getDddByLssbm(lssbm) : "";
        return ddd;
    }

    public Object getJhpch(String jclhid, String spbm) {
        String sql = "select T.* from T_PC_JCLHMXXX T where T.PID=? and T.SPBM=? and rownum=1";
        return DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{jclhid, spbm});
    }

    @Transactional
    @Override
    public Object saveAll(String tableId, String entityJson, String dTableId, String dEntitiesJson, Map<String, Object> paramMap) {
        JsonNode entity = JsonUtil.json2node(entityJson);
        Map<String, String> dataMap = node2map(entity);//主表信息
        MessageModel message = (MessageModel) super.saveAll(tableId, entityJson, dTableId, dEntitiesJson, paramMap);
        Map<String, Object> map = (Map) message.getData();
        List<Map<String, String>> detail = (List<Map<String, String>>) map.get("detail");//从表信息
        for (Map<String, String> m : detail) {
            /*****同步追溯信息*****/
            TCsptZsxxEntity zsEntity = new TCsptZsxxEntity();
            zsEntity.setJhpch(m.get("JHPCH"));
            zsEntity.setJypzh(m.get("JYPZH"));
            zsEntity.setZsm(m.get("ZSM"));
            zsEntity.setQymc(dataMap.get("PFSCMC"));
            zsEntity.setQybm(dataMap.get("PFSCBM"));
            zsEntity.setJyzbm(dataMap.get("PFSBM"));
            zsEntity.setJyzmc(dataMap.get("PFSMC"));
            zsEntity.setMjmc(dataMap.get("LSSMC"));
            zsEntity.setMjbm(dataMap.get("LSSBM"));
            zsEntity.setXtlx("3");
            zsEntity.setRefId(m.get("ID"));
            TraceChainUtil.getInstance().syncZsxx(zsEntity);
            /********结束*******/
        }
        /********修改该商品的剩余重量*******/
        cutZl(dataMap.get("JCLHBH").toString(), detail);
        /********结束*******/
        return message;
    }

    public String defaultCode() {//批发市场编码条件
        String code = SerialNumberUtil.getInstance().getCompanyCode();
        String defaultCode = " ";
        if (code != null && !"".equals(code))
            defaultCode = AppDefineUtil.RELATION_AND + " PFSCBM = '" + code
                    + "' ";
        return defaultCode;
    }

    //
    public Map<String, Object> getCdxxByJclhbh(String jclhbh) {
        String sql = "select T.SCJD,T.CDBM,T.YSCPH from T_PC_JCLHXX T where T.JCLHBH = '" + jclhbh + "'";
        Map<String, Object> map = (Map<String, Object>) DatabaseHandlerDao.getInstance().queryForMap(sql);
        return map;
    }

    //todo 此方法无用
    //获得该批次剩余重量
    public Object getJclihzl(String jclhbm) {
        String sql = "select b.pid,a.jclhbh,a.lhzzl,avg(b.syzl) as syzl,b.spbm,b.spmc,sum(b.zl) as zl,sum(b.js) as js from t_pc_jclhxx a,t_pc_jclhmxxx b where a.id = b.pid and a.jclhbh = '" + jclhbm + "' group by b.pid,a.jclhbh,a.lhzzl,b.spbm,b.spmc";
        List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);

//		for(Map<String,Object> map:list){
//			if(map.get("SYZL")==null){
//				map.put("SYZL",map.get("ZL"));
//                sql = "update t_pc_jclhmxxx t set syzl = '"+map.get("ZL").toString()+"' where t.pid = '"+map.get("PID")+"' and t.spbm = '"+map.get("SPBM")+"'";
//                DatabaseHandlerDao.getInstance().executeSql(sql);
//			}
//		}
        return list;
    }

    //计算商品剩余重量
    public void cutZl(String jclhbh, List<Map<String, String>> spxx) {
        String id = (String) DatabaseHandlerDao.getInstance().queryForObject("select id from t_pc_jclhxx where jclhbh = ?", new Object[]{jclhbh});
        for (Map<String, String> spMap : spxx) {
//			sql = "select b.spbm,b.spmc,sum(b.zl) as zl from t_pc_jyxx a,t_pc_jymxxx b where a.id = b.t_pc_jyxx_id  and a.jclhbh = '"+jclhbh+"' and b.spbm = '"+amap.get("SPBM")+"' group by b.spbm,b.spmc";
//			Map<String,Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql);
//            sql  = "select b.pid,a.jclhbh,a.lhzzl,avg(b.syzl) as syzl,b.spbm,b.spmc,sum(b.zl) as zl from t_pc_jclhxx a,t_pc_jclhmxxx b where a.id = b.pid and a.jclhbh = '"+jclhbh+"' and b.spbm = '"+amap.get("SPBM")+"' group by b.pid,a.jclhbh,a.lhzzl,b.spbm,b.spmc";
//            Map<String,Object> syzlmap = DatabaseHandlerDao.getInstance().queryForMap(sql);
//            int syzl = Integer.parseInt((Float.parseFloat(syzlmap.get("ZL").toString())+"").split("\\.")[0]);
//            int zl = Integer.parseInt((Float.parseFloat(map.get("ZL").toString())+"").split("\\.")[0]);
//            int zzl = syzl -zl;
//            sql = "update t_pc_jclhmxxx t set t.syzl = '"+zzl+"' where t.pid = '"+syzlmap.get("PID")+"' and t.spbm = '"+amap.get("SPBM")+"'";
//            DatabaseHandlerDao.getInstance().executeSql(sql);
            String sql = "";
            Object[] list = new Object[]{};
            if (!spMap.get("ZL").equals("") && spMap.get("ZL") != null) {
                sql = "update t_pc_jclhmxxx set syzl = decode(syzl,null,zl-?,syzl-?) where pid = ? and spbm = ?";
                list = new Object[]{spMap.get("ZL"), spMap.get("ZL"), id, spMap.get("SPBM")};
            } else if (!spMap.get("JS").equals("") && spMap.get("JS") != null) {
                sql = "update t_pc_jclhmxxx set syjs = decode(syjs,null,js-?,syjs-?) where pid = ? and spbm = ?";
                list = new Object[]{spMap.get("JS"), spMap.get("JS"), id, spMap.get("SPBM")};
            }
            DatabaseHandlerDao.getInstance().executeSql(sql, list);
        }
    }
}
