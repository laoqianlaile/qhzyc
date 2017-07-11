package com.ces.component.trace.code;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Synge on 2015/8/19.
 */
public class ZzplCode extends CodeApplication {

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
        String sql="select * from (select t.PL,t.PLBH from T_ZZ_DPZXX  t where t.qybm='" + qybm + "' and t.is_delete <> '1')";
        List<Object[]> list= DatabaseHandlerDao.getInstance().queryForList(sql);
        List<Code> dataList=new ArrayList<Code>();
        int i = 0, len =list.size();
        Code code = null;
        for (; i < len; i++) {
            Object[] dataMap=list.get(i);
            code = new Code();
            code.setCodeTypeCode(codeTypeCode);
            code.setValue(dataMap[1].toString());
            code.setName(dataMap[0].toString());
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
