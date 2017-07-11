package com.ces.component.herb.dto;

import com.ces.component.trace.dao.base.TraceShowModuleStringIDDao;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.stereotype.Component;

/**
 * Created by Plain on 2016/6/25.
 */
@Component
public interface  HerbDao extends TraceShowModuleStringIDDao<StringIDEntity> {
}
