package com.ces.component.trace.code;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Synge on 2015/8/19.
 */
public class ZzyymcCode extends CodeApplication {
    @Override
    public String getCodeValue(String name) {
        return null;
    }

    @Override
    public String getCodeName(String value) {
        return null;
    }

    @Override
    public List<Code> getCodeList(String codeTypeCode) {
        //1.数据权限过滤   2.是否排序  3.状态是否判断
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        String sql="select * from (select t.TRPMC,t.TRPBH from t_zz_trpjbxx t where t.qybm = ? and t.is_delete<>1)";
        List<Map<String,Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[] {qybm});
        List<Code> dataList=new ArrayList<Code>();
        int i = 0, len =maps.size();
        Code code = null;
        for (; i < len; i++) {
            Map<String,Object> dataMap=maps.get(i);
            code = new Code();
            code.setCodeTypeCode(codeTypeCode);
            code.setValue(dataMap.get("TRPBH").toString());
            code.setName(dataMap.get("TRPMC").toString());
            code.setShowOrder(i + 1);
            dataList.add(code);
        }
        return dataList;
    }

    @Override
    public Object getCodeTree(String codeTypeCode) {
        return null;
    }

    @Override
    public Object getCodeGrid(String codeTypeCode) {
        return null;
    }
}
