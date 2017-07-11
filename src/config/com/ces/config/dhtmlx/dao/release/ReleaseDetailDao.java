package com.ces.config.dhtmlx.dao.release;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.release.ReleaseDetail;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 系统发布详情DAO
 * 
 * @author wanglei
 * @date 2015-1-30
 */
public interface ReleaseDetailDao extends StringIDDao<ReleaseDetail> {

    /**
     * 获取发布系统或更新包的根节点
     * 
     * @param systemReleaseId 系统发布ID
     * @param dataId 数据ID（菜单ID）
     */
    @Query("from ReleaseDetail where systemReleaseId=? and dataId=?")
    public ReleaseDetail getReleaseDetailRootNode(String systemReleaseId, String dataId);

    /**
     * 根据系统发布的ID删除相关发布的详情
     * 
     * @param systemReleaseId 系统发布ID
     */
    @Transactional
    @Modifying
    @Query("delete from ReleaseDetail where systemReleaseId=?")
    public void deleteBySystemReleaseId(String systemReleaseId);
}
