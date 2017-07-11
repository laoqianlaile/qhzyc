package com.ces.component.yzcljl.service;

import java.util.Map;

import javax.servlet.ServletContext;

import com.ces.component.trace.utils.CompanyInfoUtil;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ces.component.cspt.entity.TCsptZsxxEntity;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.TraceChainUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.JsonUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.databind.JsonNode;

@Component
public class YzcljlService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

	@Override
	@Transactional
	public Map<String, String> save(String tableId, String entityJson,
			Map<String, Object> paramMap) {
		String xgzjsl = ServletActionContext.getRequest().getParameter("xgzjsl");
		String tableName = "T_YZ_CLXX";
		entityJson=entityJson.replace("A_", "");
    	JsonNode entityNode = JsonUtil.json2node(entityJson);
    	Map<String, String> dataMap = node2map(entityNode);
    	dataMap.remove("B_SYZS");
    	dataMap.remove("B_JLRQ");
    	dataMap.remove("B_PZTYM");
    	dataMap.remove("B_SL");
    	dataMap.remove("B_PZQC");
    	String dataId=dataMap.get("ID");
    	if(dataId!=null && !"".equals(dataId)){
    		dataId=dataId.split("_")[0];
    		dataMap.put("ID", dataId);
    	}
    	String id = save(tableName, dataMap, paramMap);
    	//获得总共出栏的数量 ：出栏数量+无公害处理数量
        int sl = 0 ;
        if(!"".equals(dataMap.get("SL"))){
            sl = Integer.parseInt(dataMap.get("SL"));
        }
        int wghclsl = 0 ;
        if(!"".equals(dataMap.get("WGHCLSL"))){
            wghclsl = Integer.parseInt(dataMap.get("WGHCLSL"));
        }
        int clsl=sl+wghclsl;
        if(null != xgzjsl && !"".equals(xgzjsl)){
        	clsl = Integer.parseInt(xgzjsl);
        }
        //修改进栏状态和猪舍状态
    	updateJlzt(dataMap.get("YZPCH"),clsl );
    	dataMap.put(AppDefineUtil.C_ID, id);
    	/*****同步追溯信息*****/
    	TCsptZsxxEntity entity = new TCsptZsxxEntity();
    	entity.setZsm(dataMap.get("ZSM"));
    	entity.setJhpch(dataMap.get("PCH"));
    	entity.setQybm(dataMap.get("QYBM"));
    	entity.setQymc(CompanyInfoUtil.getInstance().getCompanyName("YZ", dataMap.get("QYBM")));
    	entity.setXtlx("2");
    	entity.setRefId(dataMap.get("ID"));
		entity.setZZYZPCH(dataMap.get("YZPCH"));
    	TraceChainUtil.getInstance().syncZsxx(entity);
    	/********结束*******/
    	dataMap.put(AppDefineUtil.C_ID, id+"_"+getYzpch_id(dataMap.get("YZPCH")));
        return dataMap;
	}
	
	
	public String getYzpch_id(String yzpch){
		String sql ="select id from T_YZ_JLXX where YZPCH='"+yzpch+"' and QYBM=" +SerialNumberUtil.getInstance().getCompanyCode();
		return (String) DatabaseHandlerDao.getInstance().queryForObject(sql);
	}

	public void updateJlzt(String yzpch, int clsl){
		//xgzjsl 为修改时数量的增减量
		String xgzjsl = ServletActionContext.getRequest().getParameter("xgzjsl");
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        String sql;
        //先查询该养殖批次剩余的猪的头数
        sql= "select t.sl  from T_YZ_JLXX  t where t.YZPCH='"+yzpch +"' and t.QYBM= '" + qybm +"'";
        Map<String,Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql);
        //获得当前出栏的批次
        if(map!=null && !map.isEmpty()){
            //当前批次中的数量
            int sl = Integer.parseInt(map.get("SL").toString());
            //剩余的数量=当前批次中剩余的头数-出栏数量
            int sysl=sl-clsl;
            //剩余的数量等于于0则 在修改当前养殖批次状态为失效，猪舍为使用中变成空闲状态
            if(sysl==0){
                //修改控制养殖批次失效
                sql="update T_YZ_JLXX set qyzt = 2 where YZPCH='"+yzpch +"' and QYBM= '" + qybm +"'";
                DatabaseHandlerDao.getInstance().executeSql(sql);
                //猪舍为使用中变成空闲状态
                sql="update T_YZ_ZSDA set SYZT = 2 where CSBH in(select t.syzs from  T_YZ_JLXX t where t.YZPCH='"+yzpch +"') and QYBM='" + qybm +"'";
                DatabaseHandlerDao.getInstance().executeSql(sql);
            }
            
            //修改时，先查询该猪栏状态是否为放空状态
            if(xgzjsl != null || !"".equals(xgzjsl)){
            	sql = "select SYZT from T_YZ_ZSDA where CSBH in(select t.syzs from  T_YZ_JLXX t where t.YZPCH='"+yzpch +"') and QYBM='" + qybm +"'";
            	Map<String,Object> syztmap = DatabaseHandlerDao.getInstance().queryForMap(sql);
            	if(syztmap!=null && !syztmap.isEmpty()){
            		int syzt = Integer.parseInt(syztmap.get("SYZT").toString());
            		if(syzt == 1){
            			
            		}else if(syzt == 2){
            			if(sysl > 0){
            				//修改控制养殖批次有效
                            sql="update T_YZ_JLXX set qyzt = 1 where YZPCH='"+yzpch +"' and QYBM='" + qybm + "'";
                            DatabaseHandlerDao.getInstance().executeSql(sql);
                            //猪舍为空闲状态变成使用中
                            sql="update T_YZ_ZSDA set SYZT = 1 where CSBH in(select t.syzs from  T_YZ_JLXX t where t.YZPCH='"+yzpch +"') and QYBM='" +qybm + "'";
                            DatabaseHandlerDao.getInstance().executeSql(sql);
            			}
            		}
            	}
            }
            
            
            //修改当前出栏的养殖批次剩余数量为sylsl
            sql="update T_YZ_JLXX set sl = "+sysl+" where YZPCH='"+yzpch +"' and QYBM='" + qybm + "'";
            DatabaseHandlerDao.getInstance().executeSql(sql);
    }

    }
	
	public boolean getIssyzs(String yzpch){
		boolean bool = false;
		String qybm = SerialNumberUtil.getInstance().getCompanyCode();
		String sql = "select SYZT from T_YZ_ZSDA where CSBH in(select t.syzs from  T_YZ_JLXX t where t.YZPCH='"+yzpch +"') and QYBM=" +qybm;
    	Map<String,Object> syztmap = DatabaseHandlerDao.getInstance().queryForMap(sql);
    	if(syztmap.get("SYZT").toString().equals("1")){
    		bool = false;
    	}else if(syztmap.get("SYZT").toString().equals("2")){
    		bool = true;
    	}
    	return bool;
	}
	
	
	
	@Override
	public void delete(String tableId, String dTableIds, String ids, boolean isLogicalDelete, Map<String, Object> paramMap) {
		 // 1. 获取所有关联表的要删除的IDS
        String tableName = "T_YZ_CLXX";
        String[] idDatas=ids.split(",");
        StringBuffer newIds=new StringBuffer("");
        for (int i = 0; i < idDatas.length; i++) {
        	if(i >0 && i<=idDatas.length-1){
				newIds.append(",");
			}
			String string = idDatas[i];
			newIds.append("'"+string.split("_")[0]+"'");
			
		}
        String filter    = "DELETE FROM "+tableName + " WHERE ID IN (" + newIds.toString() + ")";
        DatabaseHandlerDao.getInstance().executeSql(filter);
	}
	
	/**
	 * 默认权限过滤
	 * @return
	 */
	public  String defaultCode(){
		String code=SerialNumberUtil.getInstance().getCompanyCode();
		String  defaultCode=" ";
		if(code!=null && !"".equals(code) )
			defaultCode=AppDefineUtil.RELATION_AND+" A_QYBM = '"+code+"' ";
		return defaultCode;
	}

	@Override
	protected String buildCustomerFilter(String tableId,
			String componentVersionId, String moduleId, String menuId,
			Map<String, Object> paramMap) {
		  // 返回过滤条件时，要以AppDefineUtil.RELATION_AND、AppDefineUtil.RELATION_OR开头，如下所示：
        return defaultCode();
	}
	/**
	 * 校验生猪产地检疫证号是否已存在
	 * @param jyzh
	 * @return
	 */
	public boolean checkJyzh(String jyzh){
		boolean flag = false;
		String sql = "select count(*) as num from T_YZ_CLXX T where T.SZCDJYZH = '" +jyzh+ "'";
		Map<String,Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql);
		Object num  = map.get("NUM");
		if(Integer.parseInt(String.valueOf(num))>0){
			flag = true;
		}
		return flag;
	}
}