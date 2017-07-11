package com.ces.component.aquatic.dao;

import com.ces.component.aquatic.entity.TzEntity;
import com.ces.xarch.core.persistence.jpa.StringIDDao;
import org.springframework.stereotype.Component;

/**
 * Created by 黄翔宇 on 15/7/7.
 */
@Component
public interface TzDao extends StringIDDao<TzEntity> {
}
