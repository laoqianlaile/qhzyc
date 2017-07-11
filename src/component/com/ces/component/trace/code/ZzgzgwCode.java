package com.ces.component.trace.code;

import com.ces.component.trace.utils.DataDictionaryUtil;
import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/11.
 */
public class ZzgzgwCode extends CodeApplication {

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
        List<Code> list= DataDictionaryUtil.getInstance().getDictionaryData("SCZZGZGW");
//        System.out.println(DatabaseHandlerDao.getInstance().queryForList("select sjbm value  ,sjmc name ,sxjb show_order  from t_common_SJLX_CODE  where lxbm='SCZZGZGW'"));
//        Code code=new Code();
//        code.setName("地块管理员");
//        code.setValue("1");
//        code.setShowOrder(1);
//        code.setRemark("地块管理员");
//        list.add(code);
//
//        code=new Code();
//        code.setName("种植管理员");
//        code.setValue("2");
//        code.setShowOrder(2);
//        code.setRemark("种植管理员");
//        list.add(code);
        return list;
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
