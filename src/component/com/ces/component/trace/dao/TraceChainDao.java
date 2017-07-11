package com.ces.component.trace.dao;

import com.ces.component.cspt.entity.TCsptZsxxEntity;
import com.ces.xarch.core.persistence.jpa.StringIDDao;
import org.springframework.stereotype.Component;

/**
 * 追溯流程Dao
 * Created by bdz on 2015/7/9.
 */
@Component
public interface TraceChainDao extends StringIDDao<TCsptZsxxEntity>{
}
