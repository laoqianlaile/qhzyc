package com.ces.config.dhtmlx.json.entity.appmanage;

import java.util.List;
import java.util.Map;

import com.ces.xarch.core.entity.StringIDEntity;
import com.google.common.collect.Lists;

public class GridRow extends StringIDEntity{

    private static final long serialVersionUID = -8012891783898308577L;
    /* 列表一行数据.*/
    private List<String> data = Lists.newArrayList();
    /* 隐藏值.*/
    private Map<String, String> userdata = null;
    
    public List<String> getData() {
        return data;
    }
    public void setData(List<String> data) {
        this.data = data;
    }
    public Map<String, String> getUserdata() {
        return userdata;
    }
    public void setUserdata(Map<String, String> userdata) {
        this.userdata = userdata;
    }
}
