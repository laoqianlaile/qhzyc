package com.ces.component.trace.code;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by HP.plain on 2016/4/22.
 */
public class SdzycjjgycrkcgdhCode extends CodeApplication {
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
        String sql="select  m.QYXSDDH,T.XSDDH from T_SDZYC_CJG_YCJYXXXX  t inner join T_sdzyc_cjg_ycjyxx m on t.xsddh=m.xsddh where t.SFRK='0' and  t.qybm = '"+ SerialNumberUtil.getInstance().getCompanyCode()+"' order by m.jysj desc";
        List<Map<String,Object>> maps= DatabaseHandlerDao.getInstance().queryForMaps(sql);
        List<Code> dataList=new ArrayList<Code>();
        Code code = null;
        int i = 0;
        for (Map<String,Object> map : maps) {
            code = new Code();
            code.setCodeTypeCode(codeTypeCode);
            code.setValue(map.get("QYXSDDH").toString());
            code.setName(map.get("QYXSDDH").toString());
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
    public Object getCodeGrid(String codeTypeCode)  {

        return null;
    }

}
