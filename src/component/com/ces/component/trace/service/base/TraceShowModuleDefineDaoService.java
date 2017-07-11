package com.ces.component.trace.service.base;

import com.ces.component.trace.dao.base.TraceShowModuleStringIDDao;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.service.base.ShowModuleDefineDaoService;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.TableUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TraceShowModuleDefineDaoService<T extends StringIDEntity, Dao extends TraceShowModuleStringIDDao<T>>
extends ShowModuleDefineDaoService<T, Dao> {
	
	private static Log log = LogFactory.getLog(TraceShowModuleDefineDaoService.class);

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
			String qybm = SerialNumberUtil.getInstance().getCompanyCode();
			filter.append(AppDefineUtil.RELATION_AND + " qybm = '" + qybm + "'");
		}
		log.debug("表" + tableName + "的过滤条件：" + filter.toString());
		return filter.toString();
	}
}
