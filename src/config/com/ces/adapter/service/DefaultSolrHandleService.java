/**
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * qiucs 2015-6-26 下午4:56:09
 * <p>描述: TODO(用一句话描述该文件做什么)</p>
 */
package com.ces.adapter.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.ces.adapter.facade.SolrHandleFacade;
import com.ces.config.service.base.ShowModuleDefineDaoService.SearchParameter;

/**
 * @author qiucs
 *
 */
@Component
public class DefaultSolrHandleService implements SolrHandleFacade {

	/* qiucs 2015-6-26 下午4:56:33
	 * (non-Javadoc)
	 * @see com.ces.solr.facade.HandleSolrFacade#syncCache()
	 */
	@Override
	public void syncCache(String tableId, int type) {
		// TODO Auto-generated method stub
		
	}

	/* qiucs 2015-6-26 下午4:56:33
	 * (non-Javadoc)
	 * @see com.ces.solr.facade.HandleSolrFacade#startIndexScan()
	 */
	@Override
	public void startIndexScan() {
		// TODO Auto-generated method stub
		
	}

	/* qiucs 2015-6-26 下午4:56:33
	 * (non-Javadoc)
	 * @see com.ces.solr.facade.HandleSolrFacade#stopIndexScan()
	 */
	@Override
	public void stopIndexScan() {
		// TODO Auto-generated method stub
		
	}

	/* qiucs 2015-6-26 下午4:56:33
	 * (non-Javadoc)
	 * @see com.ces.solr.facade.HandleSolrFacade#queryPage(com.ces.config.service.base.ShowModuleDefineDaoService.SearchParameter, java.lang.String, java.lang.String)
	 */
	@Override
	public Page<Object> queryPage(SearchParameter parameter, String tableName,
			String columns) {
		// TODO Auto-generated method stub
		return null;
	}

	/* qiucs 2015-6-26 下午4:56:33
	 * (non-Javadoc)
	 * @see com.ces.solr.facade.HandleSolrFacade#rebuildIndex()
	 */
	@Override
	public boolean rebuildIndex() {
		// TODO Auto-generated method stub
		return true;
	}

	/* qiucs 2015-7-7 上午9:39:09
	 * (non-Javadoc)
	 * @see com.ces.adapter.facade.SolrHandleFacade#increaseIndex()
	 */
	@Override
	public boolean increaseIndex() {
		// TODO Auto-generated method stub
		return true;
	}

	/* qiucs 2015-6-26 下午4:56:33
	 * (non-Javadoc)
	 * @see com.ces.solr.facade.HandleSolrFacade#increaseIndex()
	 */
	@Override
	public boolean isAlive() {
		// TODO Auto-generated method stub
		return false;
	}

}
