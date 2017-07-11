package com.ces.config.dhtmlx.action.cell;

import org.apache.struts2.rest.DefaultHttpHeaders;

import com.ces.config.dhtmlx.action.base.ConfigController;
import com.ces.config.utils.CommonUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.ces.xarch.core.exception.FatalException;

public class CellController extends ConfigController<StringIDEntity> {

    private static final long serialVersionUID = -5615872005536395078L;

    @Override
    protected void initModel() {
        setModel(new StringIDEntity());
    }

    /**
     * 获取token
     * 
     * @return Object
     * @throws FatalException
     */
    public Object token() throws FatalException {
        String token = CommonUtil.getToken();
        setReturnData(token);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

}
