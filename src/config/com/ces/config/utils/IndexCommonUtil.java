/**
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * qiucs 2015-2-12 下午2:27:26
 * <p>描述: TODO(用一句话描述该文件做什么)</p>
 */
package com.ces.config.utils;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;

import com.ces.adapter.facade.SolrHandleFacade;
import com.ces.adapter.service.DefaultSolrHandleService;
import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalTableDefine;
import com.ces.config.service.base.ShowModuleDefineDaoService.SearchParameter;
import com.ces.xarch.core.web.listener.XarchListener;

/**
 * @author qiucs
 *
 */
public class IndexCommonUtil {
	
	private static Log log = LogFactory.getLog(IndexCommonUtil.class);
	
	/**
	 * qiucs 2015-2-13 上午10:49:21
	 * <p>
	 * 描述: 添加索引信息 (使用 syncCache 方法替代)
	 * </p>
	 */
	@Deprecated
	public static void putIndexConfig(PhysicalTableDefine table) {
		syncCache(table, 1);
	}

	/**
	 * qiucs 2015-2-13 上午10:49:52
	 * <p>
	 * 描述: 删除索引信息 (使用 syncCache 方法替代)
	 * </p>
	 * @return void
	 */
	@Deprecated
	public static void removeIndexConfig(PhysicalTableDefine table) {
		syncCache(table, -1);
	}
	
	/**
	 * qiucs 2015-2-13 上午10:50:07
	 * <p>
	 * 描述: 同步索引结构 (使用 syncCache 方法替代)
	 * </p>
	 * @return void
	 */
	@Deprecated
	public static void syncSchema(PhysicalTableDefine table) {
		syncCache(table, 0);
	}
	
	/**
	 * qiucs 2015-6-30 下午1:14:13
	 * <p>
	 * 描述: 同步索引结构
	 * </p>
	 * @param  tableId 
	 *           如果为null时，则同步所有表信息
	 * @return void
	 */
	public static void syncCache(String tableId) {
		if (!solrPlugin) return;
		PhysicalTableDefine table = null;
		if (StringUtil.isNotEmpty(tableId)) {
			table = TableUtil.getTableEntity(tableId);
		}
		syncCache(table, 0);
	}
	
	/**
	 * qiucs 2015-6-30 下午1:14:13
	 * <p>
	 * 描述: 同步索引结构
	 * </p>
	 * @param  table 
	 *           如果为null时，则同步所有表信息
	 * @param  type
	 *           -1: 移除; 0:修改; 1:添加
	 * @return void
	 */
	public static void syncCache(PhysicalTableDefine table, int type) {
		if (!solrPlugin) return;
		if (null == table) {
			facade.syncCache(null, 0);
		} else if ("1".equals(table.getCreateIndex())) {
			facade.syncCache(table.getId(), type);
		}
	}
	
	/**
	 * qiucs 2015-2-13 上午10:50:28
	 * <p>描述: 启动同步索引库线程 </p>
	 * @return void
	 */
	public static void startIndexThread() {
		if (!solrPlugin) return;
		if ("1".equals(SystemParameterUtil.getInstance().getSystemParamValue("全文检索引擎"))) {
			facade.startIndexScan();
		}
	}
	
	/**
	 * qiucs 2015-4-3 下午2:22:52
	 * <p>描述: 停止同步索引库线程  </p>
	 * @return void
	 */
	public static void startIndexScan() {
		if (!solrPlugin) return;
		if (!facade.isAlive()) {
			facade.startIndexScan();
		}
	}
	
	/**
	 * qiucs 2015-4-3 下午2:22:52
	 * <p>描述: 停止同步索引库线程  </p>
	 * @return void
	 */
	public static void stopIndexScan() {
		if (!solrPlugin) return;
		if (!facade.isAlive()) {
			facade.stopIndexScan();
		}
	}
	
	/**
	 * qiucs 2015-2-13 上午10:51:05
	 * <p>描述: 索引检索 </p>
	 * @return Page<Object[]>
	 */
	public static Page<Object> queryPage(SearchParameter parameter, String tableName, String columns) {
		if (!solrPlugin) return null;
		return facade.queryPage(parameter, tableName, columns);
	}
	
	/**
	 * qiucs 2015-4-26 下午4:58:41
	 * <p>描述: 重建索引库 </p>
	 * @return void
	 */
	public static MessageModel rebuildIndex() {
		if (!solrPlugin) return MessageModel.falseInstance("SOLR插件未集成，请检查！");
		boolean success = false;
		String message  = "重建索引已完成！";
		if (isRebuild) {
			message = "索引正在重建中，请耐心等待……";
			log.info(message);
		} else {
			try {
				isRebuild = true;
				success = facade.rebuildIndex();
				if (!success) {
					message = "重建索引失败！";
				}
			} catch (Exception e) {
				message = "重建索引库出错";
				success = false;
				log.error(message, e);
			} finally {
				isRebuild = false;
			}
		}
		
		return MessageModel.newInstance(success, message);
	};
	
	/**
	 * qiucs 2015-4-26 下午5:07:56
	 * <p>描述: 增量索引 </p>
	 * @return void
	 */
	public static MessageModel increaseIndex() {
		if (!solrPlugin) return MessageModel.falseInstance("SOLR插件未集成，请检查！");
		boolean success = false;
		String  message = "增量索引同步成功！";
		if (!facade.isAlive()) {
			if (isIncrease) {
				message = "增量索引同步正在进行中，请耐心等待……";
				log.info(message);
			} else {
				try {
					isIncrease = true;
					success = facade.increaseIndex();
					if (!success) {
						message = "增量索引同步失败！";
					}
				} catch (Exception e) {
					message = "增量索引同步出错";
					success = false;
					log.error(message, e);
				} finally {
					isIncrease = false;
				}
			}
		} else {
			syncCache(null);
			message  = "增量建索引同步正在进行中，请耐心等待一会儿……";
			log.info(message);
		}
		return MessageModel.newInstance(success, message);
	}
	
	
	private static boolean isRebuild  = false;

	private static boolean isIncrease = false;
	
	private static SolrHandleFacade facade = null;
	
	private static boolean solrPlugin = true;
	
	static {
		try {
			Class<?> clz = Class.forName("com.ces.indexquery.plugins.service.SolrHandleService");
			facade = (SolrHandleFacade) XarchListener.getBean(clz);
		} catch (ClassNotFoundException e) {
			solrPlugin = false;
			facade = XarchListener.getBean(DefaultSolrHandleService.class);
			//log.warn("索引插件未集成，请检查！");
		}
	}
}
