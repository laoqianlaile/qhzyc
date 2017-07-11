package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.ces.config.dhtmlx.entity.appmanage.CoflowOpinion;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface CoflowOpinionDao extends StringIDDao<CoflowOpinion> {
    
    @Query("FROM CoflowOpinion T WHERE T.dataId=?1")
    public List<CoflowOpinion> getOpinions(String dataId);

}
