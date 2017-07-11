package com.ces.config.dhtmlx.dao.appmanage;

import org.springframework.data.jpa.repository.Query;

import com.ces.config.dhtmlx.entity.appmanage.Report;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface ReportDao extends StringIDDao<Report> {

    /**
     * qiucs 2014-3-20 
     * <p>描述: 获取最大显示顺序</p>
     */
    @Query("select max(showOrder) from Report where parentId=?1")
    public Integer getMaxShowOrderByParentId(String parentId);
}
