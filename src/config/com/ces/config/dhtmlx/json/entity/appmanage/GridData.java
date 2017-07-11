package com.ces.config.dhtmlx.json.entity.appmanage;

import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.web.frame.impl.DefaultDataModel;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GridData<T extends StringIDEntity> extends DefaultDataModel<T, String> {

    /* (非 Javadoc)   
     * <p>描述: </p>   
     * @return   
     * @see com.ces.xarch.core.web.frame.impl.DefaultDataModel#getData()
     */
    @Override
    @JsonProperty("rows")
    public Object getData() {
        return super.getData();
    }

    /* (非 Javadoc)   
     * <p>描述: </p>   
     * @return   
     * @see com.ces.xarch.core.web.frame.impl.DefaultDataModel#getTotal()
     */
    @Override
    @JsonProperty("total_count")
    public long getTotal() {
        return super.getTotal();
    }
    
    /**
     * 获取当前记录位置.
     * @author qiucs
     * @date 2013-09-12
     */
    public long getPos() {
        return (getPageNumber()-1)*getPageSize();
    }
}
