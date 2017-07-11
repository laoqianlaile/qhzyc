package com.ces.component.trace.code;

import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 认证类型编码
 * Created by bdz on 2015/8/12.
 */
public class ZzrzlxCode extends CodeApplication {
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
       String sql = "SELECT T.SJBM,T.SJMC FROM T_COMMON_SJLX_CODE T WHERE T.LXBM = 'RZLX'";
        List<Map<String, Object>> li = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        List codeList = new ArrayList();
        for(Map m :li){
            Code code = new Code();
            code.setName(m.get("sjmc").toString());
            code.setValue(m.get("sjbm").toString());
            codeList.add(code);
        }
        return codeList;
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
