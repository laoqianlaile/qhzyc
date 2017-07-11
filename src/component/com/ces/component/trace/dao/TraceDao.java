package com.ces.component.trace.dao;

import com.ces.component.trace.dao.base.TraceShowModuleStringIDDao;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.stereotype.Component;

/**
 * Created by 黄翔宇 on 15/5/4.
 */
@Component
public interface TraceDao extends TraceShowModuleStringIDDao<StringIDEntity> {
}
