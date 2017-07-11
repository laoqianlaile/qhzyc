package com.ces.component.tyttgj.service;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.ComponentFileUtil;
import com.ces.config.utils.TableUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.io.File;
import java.util.List;
import java.util.Map;

@Component
public class TyttgjService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {
	private static String SPZS_FJLJB = "spzstpfj";
	public static String SPZS_ZZ_GZRYDAFJ = "T_ZZ_GZRYDAFJ";

	/**
	 * 获得只有主从表两个表关系的字表id(tableId)
	 * @param pTableId
	 * @param logicTableCode 逻辑表构建名称
	 * @param flag   false 取默认逻辑表名称
	 * @return
	 */
	public String  getTableId(String pTableId,String logicTableCode,boolean flag) {
		if(!flag){
			logicTableCode=SPZS_FJLJB;
		}
		String sql = "select t.id, t.show_name, t.table_name from t_xtpz_physical_table_define t " +
				" where  t.logic_table_code='" + logicTableCode + "' and (" +
				"exists(select r.id from t_xtpz_table_relation r where r.relate_table_id=t.id and r.table_id=?) or " +
				"exists(select r.id from t_xtpz_table_relation r where r.table_id=t.id and r.relate_table_id=?)" +
				") ";
		Map<String, Object> data = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{pTableId, pTableId});
		String slaveTableId = String.valueOf(data.get("ID"));
		return slaveTableId;
	}


	@Override
	public void delete(String tableId, String dTableIds, String ids, boolean isLogicalDelete, Map<String, Object> paramMap) {
		//根据删除的ID获得对应的资源名称
		String tableName = getTableName(tableId);
		String sql =" select TPBCMC from "+tableName+" where ID IN ('" + ids.replace(",", "','") + "')";
		List<Map<String,Object>> listMap=DatabaseHandlerDao.getInstance().queryForMaps(sql);
		if (listMap != null && listMap.size() > 0) {
			for( Map<String,Object> map : listMap){
				String tpbcmc = String.valueOf(map.get("TPBCMC"));
				//根据资源名称判读是否存在该图片,
				File file = new File(ComponentFileUtil.getProjectPath() + "/spzstpfj/" + tpbcmc);
				File thumbFile = new File(ComponentFileUtil.getProjectPath() + "/spzstpfj/thumb/" + tpbcmc);
				FileUtils.deleteQuietly(file);
				FileUtils.deleteQuietly(thumbFile);
			}
		}
		sql = "delete from "+tableName+" where ID IN ('" + ids.replace(",", "','") + "')";
		DatabaseHandlerDao.getInstance().executeSql(sql);
	}
}