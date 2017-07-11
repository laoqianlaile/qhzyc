package com.ces.component.jclhxz.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.TableNameUtil;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.component.cspt.entity.TCsptZsxxEntity;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.utils.TraceChainUtil;
import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.UUIDGenerator;
import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.databind.JsonNode;

@Component
public class JclhxzService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    public Map<String,Object> getZzccxx(String zztmh){
        /*********************1.0********************/
//    	System.out.println("字符串截取结果为：=" +newPch);
//    	System.out.println("字符串替换结果为：=" +ccpch.replace("PC", ""));
//		String qybm = zztmh.substring(2,11);
//    	String sql= " select T.A_CDZMH as CDZMH,T.A_YSCPH as YSCPH,T.B_SPMC as SPMC,T.B_SPBM as SPBM ," +
//				" T.A_PCH as CCPCH,T.A_ZL as ZL,P.ZZJDMC as ZZJDMC,P.LSXZQHDM as CDBM,P.LSXZQ as CDMC,T.A_ZSM as ZSM " +
//				" from V_ZZ_CCXX_ZZ_ZPXX T,T_ZZ_CDDA P where T.A_CCTMH='"
//    				+zztmh+"' and t.a_is_in = '0' and P.QYBM = '"+qybm+"'";
//    	Map<String,Object> map=DatabaseHandlerDao.getInstance().queryForMap(sql);
        /*********************1.0********************/
        Map<String,Object> resultMap = new HashMap<String, Object>();
        resultMap.put("result","success");
        Map<String, Object> dataMap = null ;
        String sql = "select ID,t.qybm,t.khmc,t.zzl,t.KHBH, PSZRR, XSDDH, CCSJ, PSFS, CPH,t.cclsh" +
                "  from T_ZZ_CCGL t" +
                " where SFDH =? " +            //todo 删除or条件以及rownum条件
                "   AND IS_DELIVERED <> '1' and rownum=1 ";
        //查询有效的出场信息:根据出场条码
        dataMap = DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{zztmh});
        //判断是否为:客户出场
        if(dataMap!=null && !dataMap.isEmpty()){
            //查询企业相关信息
            String qyxxSql = "select * from T_ZZ_CDDA T where T.QYBM=?";
            Map<String,Object> qyxxMap = DatabaseHandlerDao.getInstance().queryForMap(qyxxSql,new Object[]{dataMap.get("QYBM")});
            resultMap.put("qyData",qyxxMap);
            //查询出场详细信息
            //散货出场信息
            String shSql = "select t_.ID," +
                    "       t_.PCH," +
                    "       t_.CPZSM," +
                    "       t_.PZ," +
                    "       t_.PZBH," +
                    "       t_.CSZZL," +
                    "       t_.KCZL," +
                    "       t_.CCZL," +
                    "       t_.CJZDBH,t_.ZSPCH," +
                    "       t_.PID,cs.PL,cs.PLBH," +
                    "       'GRID_LINK_RESERVE_ZONE' AS COL_010" +
                    "  from T_ZZ_CCSHXX t_ left join t_zz_csnzwxq xq on t_.PCH = xq.pch left join t_zz_csgl cs on xq.pid=cs.id" +
                    " where (t_.PID = ? ) AND (t_.IS_DELETE <> '1')";
            List<Map<String,Object>> shList = DatabaseHandlerDao.getInstance().queryForMaps(shSql,new Object[]{dataMap.get("ID")});
            if(shList!=null && shList.size()>0){
                resultMap.put("shData",shList);
            }
            //产品出场信息
            String cpSql = "select t.bzlsh,t.cpbh,t.cpmc,t.cpdj,t.ccjs,t.zl,t.zsm,t.ZSPCH from T_ZZ_CCBZCPXX t where t.pid = ? AND (IS_DELETE <> '1') ";
            List<Map<String,Object>> cpList = DatabaseHandlerDao.getInstance().queryForMaps(cpSql,new Object[]{dataMap.get("ID")});
            if(cpList!=null && cpList.size()>0){
                resultMap.put("cpData",cpList);
            }
            resultMap.put("ccData",dataMap);
            resultMap.put("type",1);
            return resultMap;
        }
//        else{//批次出场信息
//            //根据随附单号查询
//            String pcSql = "select t.khbh,t.khmc,t.sfdh,t.ddbh,t.psfs,t.psdz,t.cczl from t_zz_pccckhxx t where t.sfdh = ?";
//            List<Map<String,Object>> pcList = DatabaseHandlerDao.getInstance().queryForMaps(pcSql, new String[]{zztmh});
//            if(pcList!=null && pcList.size()>0){
//                resultMap.put("type",2);
//                resultMap.put("pcData",pcList);
//                //查询企业相关信息
//                String qyxxSql = "select * from T_ZZ_CDDA T where T.QYBM=?";
//                Map<String,Object> qyxxMap = DatabaseHandlerDao.getInstance().queryForMap(qyxxSql,new Object[]{pcList.get(0).get("QYBM")});
//                resultMap.put("qyData",qyxxMap);
//                return resultMap;
//            }
//        }
        resultMap.put("result","error");
    	return resultMap;
    }
    
    public Map<String,Object> getPcjyxx(String pctmh){
    	String qybm = pctmh.substring(2,11);//企业编码
     	String lhxxSql = "select P.LSSMC,P.LSSBM,T.CDZMH,T.YSCPH,T.CDBM,T.CDMC,T.SCJD,P.PFSCMC,P.PFSCBM from T_PC_JCLHXX T,T_PC_JYXX P where P.JYTMH = '"
						 +pctmh+"' and P.JCLHBH = T.JCLHBH and P.IS_IN='0' ";//理货信息查询
    	String jymxxxSql = "select T.SPMC,T.SPBM,T.ZL,T.DJ,T.JE,T.JHPCH,T.ZSM,T.JS from T_PC_JYMXXX T left join T_PC_JYXX P on T.T_PC_JYXX_ID = P.ID where P.is_in = '0' and P.JYTMH = '"+pctmh+"'";
		Map<String,Object> map = DatabaseHandlerDao.getInstance().queryForMap(lhxxSql);
    	List<Map<String,Object>> listMap = DatabaseHandlerDao.getInstance().queryForMaps(jymxxxSql);
    	Map<String,Object> resultMap = new HashMap<String,Object>();
    	resultMap.put("lhxx", map);
    	resultMap.put("jymxxx", listMap);
    	return resultMap;
    }
    @Transactional
    @Override
    public Object saveAll(String tableId, String entityJson, String dTableId, String dEntitiesJson, Map<String, Object> paramMap) {
    	JsonNode entity = JsonUtil.json2node(entityJson);
    	Map<String, String> dataMap = node2map(entity);//主表信息
    	MessageModel message =  (MessageModel) super.saveAll(tableId, entityJson, dTableId, dEntitiesJson, paramMap);
    	Map<String,Object> map = (Map) message.getData();
    	List<Map<String,String>> detail = (List<Map<String, String>>) map.get("detail");//从表信息
    	for(Map<String,String> m :detail){
    		/*****同步追溯信息*****/
        	TCsptZsxxEntity zsEntity = new TCsptZsxxEntity();
        	zsEntity.setJhpch(m.get("JHPCH"));
        	zsEntity.setJypzh(m.get("JYPZH"));
        	zsEntity.setQymc(dataMap.get("PFSCMC"));
        	zsEntity.setQybm(dataMap.get("PFSCBM"));
        	zsEntity.setJyzbm(dataMap.get("PFSBM"));
        	zsEntity.setJyzmc(dataMap.get("PFSMC"));
        	zsEntity.setXtlx("3");
        	zsEntity.setRefId(m.get("ID"));
        	TraceChainUtil.getInstance().syncZsxx(zsEntity);
        	/********结束*******/


	        /******** 同步上家交易信息begin ********/
		    String barCode = ServletActionContext.getRequest().getParameter("barCode");
		    if (barCode != null && !"".equals(barCode)) {
			    String prefix = barCode.substring(0,2);
			    if (prefix.equalsIgnoreCase("PC")) {
					String sql = "update t_pc_jymxxx t set t.is_in = '1' where t.zsm = '" + m.get("JYPZH") + "'";
				    DatabaseHandlerDao.getInstance().executeSql(sql);
			    } else if (prefix.equalsIgnoreCase("ZZ")) {
                    String sql = "update T_ZZ_CCGL t set IS_DELIVERED = '1' where SFDH =?";
				    DatabaseHandlerDao.getInstance().executeSql(sql,new Object[]{barCode});
			    }
		    }
		    /******** 同步上家交易信息 end********/

    	}
    	return message;
    }
    
}