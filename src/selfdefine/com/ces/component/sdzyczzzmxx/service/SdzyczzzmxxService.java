package com.ces.component.sdzyczzzmxx.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import org.springframework.stereotype.Component;

import com.ces.component.sdzyczzzmxx.dao.SdzyczzzmxxDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.List;
import java.util.Map;

@Component
public class SdzyczzzmxxService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzyczzzmxxDao> {

    /**
     * 获取类型
     * @return
     */
    public Object getLx() {
        String sql = "select distinct sjbm as value, sjmc as text from t_common_sjlx_code  where sjmc = '种苗' or sjmc = '种子'";
        List<Map<String,Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return maps;
    }
    /**
     * 根据id获取类型
     * @return
     */
    public Object getLxById(String id) {
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select lx from t_sdzyc_zzzm  where  qybm=? and id=?";
        Map<String,Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql, new Object[]{qybm,id});
        return map;
    }
    public Map<String,Object> searchById(String id) {
        String sql =  "select * from t_sdzyc_zzzm where id=?"+defaultCode();
        return DatabaseHandlerDao.getInstance().queryForMap(sql,new String[]{id});
    }

    public int updateSctp(String id) {
        String sql =  "update t_sdzyc_zzzm set sctp='' where id=?"+defaultCode();
        return DatabaseHandlerDao.getInstance().executeSql(sql,new String[]{id});
    }

    public  String defaultCode(){
        String code= SerialNumberUtil.getInstance().getCompanyCode();
        String  defaultCode=" ";
        if(code!=null && !"".equals(code) )
            defaultCode= AppDefineUtil.RELATION_AND+" QYBM= '"+code+"' "+ AppDefineUtil.RELATION_AND+" is_delete <> '1'";
        return defaultCode;
    }
}
