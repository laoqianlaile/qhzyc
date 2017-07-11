package com.ces.component.trace.code;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Synge on 2015/8/12.
 */
public class GysbhCode extends CodeApplication {
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
        String sql="select t.sjbm, t.sjmc from t_common_sjlx_code t where t.lxbm = 'GYSLB'";
        List<Map<String,Object>> maps= DatabaseHandlerDao.getInstance().queryForMaps(sql);
        List<Code> dataList=new ArrayList<Code>();
        Code code = null;
        int i = 0;
        for (Map<String,Object> map : maps) {
            code = new Code();
            code.setCodeTypeCode(codeTypeCode);
            code.setValue(map.get("SJBM").toString());
            code.setName(map.get("SJMC").toString());
            code.setShowOrder(i + 1);
            dataList.add(code);
            i++;
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
