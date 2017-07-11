/**
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * qiucs 2015-6-26 下午4:27:33
 * <p>描述: TODO(用一句话描述该文件做什么)</p>
 */
package com.ces.adapter.facade;

import org.springframework.data.domain.Page;

import com.ces.config.service.base.ShowModuleDefineDaoService.SearchParameter;

/**
 * @author qiucs
 *
 */
public interface SolrHandleFacade {

	void syncCache(String tableId, int type);
	
	void startIndexScan();
	
	void stopIndexScan();
	
	Page<Object> queryPage(SearchParameter parameter, String tableName, String columns);
	
	boolean rebuildIndex();
	
	boolean increaseIndex();
	
	boolean isAlive();
	
}
