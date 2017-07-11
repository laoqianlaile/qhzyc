package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.ces.config.dao.base.ShowModuleStringIDDao;
import com.ces.xarch.core.entity.StringIDEntity;

public interface ShowModuleDao extends ShowModuleStringIDDao<StringIDEntity> {

    @Query(value="select * from t_xtpz_test where id in(?1)", nativeQuery=true)
    public List<Object[]> test(String ids);
}
