package com.ces.config.dhtmlx.dao.release;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.release.ReleaseButton;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 系统发布菜单中组装的按钮DAO
 * 
 * @author wanglei
 * @date 2014-10-13
 */
public interface ReleaseButtonDao extends StringIDDao<ReleaseButton> {

    /**
     * 根据系统发布的ID删除相关发布的按钮信息
     * 
     * @param systemReleaseId 系统发布ID
     */
    @Transactional
    @Modifying
    @Query("delete from ReleaseButton where systemReleaseId=?")
    public void deleteBySystemReleaseId(String systemReleaseId);
}
