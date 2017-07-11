package com.ces.component.trace.utils;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.web.listener.XarchListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/13.
 */
@Component
public class DataDictionaryUtil {

    public static DataDictionaryUtil getInstance() {
        return XarchListener.getBean(DataDictionaryUtil.class);
    }

    /**
     * 根据数据类型编码获得数据字典数据。
     * @param lxbm
     * @return
     */
    public List<Code>  getDictionaryData(String lxbm){
        List<Code> list = new ArrayList<Code>();
        List<Map<String,Object>> dataMap = DatabaseHandlerDao.getInstance().queryForMaps("select sjbm as value  ,sjmc as name ,nvl(sxjb,'') show_Order  from t_common_sjlx_code  where lxbm='" + lxbm + "'");
        if(dataMap != null  && !dataMap.isEmpty()){
            int showOrder = 1;
            for(Map<String,Object> map :dataMap){
                Code code = new Code();
                code.setName(map.get("NAME").toString());
                code.setValue(map.get("VALUE").toString());
                String oderby = String.valueOf(map.get("SHOW_ORDER"));
                code.setShowOrder(Integer.parseInt(StringUtil.isEmpty(oderby)?"0": oderby));
                list.add(code);
            }
        }
        return list;
    }
}
