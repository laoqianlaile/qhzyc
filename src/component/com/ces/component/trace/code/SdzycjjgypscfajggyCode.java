package com.ces.component.trace.code;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.application.CodeApplication;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.code.Code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 山东中药材精加工饮片生产方案加工工艺复选框编码
 * Created by Synge on 2015/8/28.
 */
public class SdzycjjgypscfajggyCode extends CodeApplication {

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
        String sql="select t.GYBH,t.GYMC from T_SDZYC_JJG_JGGY t where qybm=?";
        List<Map<String,Object>> maps= DatabaseHandlerDao.getInstance().queryForMaps(sql,new String[]{SerialNumberUtil.getInstance().getCompanyCode()});
        List<Code> dataList=new ArrayList<Code>();
        Code code = null;
        int i = 0;
        for (Map<String,Object> map : maps) {
            code = new Code();
            code.setCodeTypeCode(codeTypeCode);
            code.setValue(map.get("GYBH").toString());//设置药材名称显示值
            code.setName(map.get("GYMC").toString());//设置药材名称隐藏值
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
        Map<String, Object> gcfg = new HashMap<String, Object>();
        gcfg.put("colNames", getColNames());
        gcfg.put("colModel", getColModel());
        gcfg.put("url", "sdzycjjgjggy!searchjggyxx.json");
        gcfg.put("panelWidth",280);
        gcfg.put("valueField", "GYMC");// 列表中PFSBM列值作为 显示值
        gcfg.put("textField", "GYMC");// 列表中PFSMC列值作为 隐藏值
        gcfg.put("multiple", true);
        return gcfg;
    }

    protected List<String> getColNames() {
        List<String> list = new ArrayList<String>();
        list.add("工艺编号");
        list.add("工艺名称");

      /*  list.add("仓库名称");*/

        return list;
    }

    protected List<Map<String, Object>> getColModel() {
        List<Map<String, Object>> colModel = new ArrayList<Map<String, Object>>();
        Map<String, Object> item =null;

       /* item = new HashMap<String, Object>();
        item.put("name", "CKBH");
        item.put("width", 120);
        colModel.add(item);*/

        item = new HashMap<String, Object>();
        item.put("name", "GYBH");
        item.put("width", 120);
        colModel.add(item);

        item = new HashMap<String, Object>();
        item.put("name", "GYMC");
        item.put("width", 120);
        colModel.add(item);

        return colModel;
    }


}


