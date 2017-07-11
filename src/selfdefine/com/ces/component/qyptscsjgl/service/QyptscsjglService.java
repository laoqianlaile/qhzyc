package com.ces.component.qyptscsjgl.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.TableUtil;
import com.ces.xarch.core.entity.StringIDEntity;

import freemarker.template.SimpleDate;

@Component
public class QyptscsjglService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

	/**
	 * 默认过滤掉企业编码和is_delete
	 * @param tableId
	 * @param componentVersionId
	 * @param moduleId
	 * @param menuId
	 * @param  paramMap --其他参数，详细见ShowModuleDefineServiceDaoController.getMarkParamMap方法介绍
	 * @return
	 */
	@Override
	protected String buildCustomerFilter(String tableId, String componentVersionId, String moduleId, String menuId, Map<String, Object> paramMap) {
		StringBuffer filter = new StringBuffer(AppDefineUtil.RELATION_AND + " is_delete = '0'");
		String tableName = TableUtil.getTableName(tableId);
		boolean isQybmColumnExist = DatabaseHandlerDao.getInstance().columnExists(tableName, "qybm");
		if (isQybmColumnExist) {
			//String qybm = SerialNumberUtil.getInstance().getCompanyCode();
			filter.append("" );//+ " qybm = '" + qybm + "'"
		}
		//log.debug("表" + tableName + "的过滤条件：" + filter.toString());
		return filter.toString();
	}
    public Object getJdbm(String id){
        String sql="select zhbh from T_QYPT_ZHGL b where b.auth_parent_id = (select a.auth_id from T_QYPT_ZHGL a where id ='"+id+"')";
        List<Map<String,Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data",dataList);
       /* StringBuffer sb = new StringBuffer();
        sb.append(dataList);*/
        return dataMap;
    }
    /**
     * 判断是否有生产数据
     * @param id
     * @return
     */
    public boolean getScsj(String  id){
    	List<Map<String,Object>> list = new ArrayList();
    	List<Map<String,Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(
    			"select zhbh from T_QYPT_ZHGL b where b.auth_parent_id = (select a.auth_id from T_QYPT_ZHGL a where id ='"+id+"')");
    	//List<Map<String,Object>> data = (List<Map<String,Object>>) dataList.get("data");
    	for(int i=0;i<dataList.size();i++){
    		String  zhbh =  dataList.get(i).get("ZHBH").toString();
    		String sql = "select * from T_ZZ_SCDA t_  where  is_delete = '0'and qybm="+zhbh;
    		list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
    	}
    	if(list.size()==0){
    		return false;
    	}
    	return true;
    }
}