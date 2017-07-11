package com.ces.component.zzbzfa.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.util.List;
import java.util.Map;

@Component
public class ZzbzfaService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {


    public Object deleteBzfa(String dataId) {
        String sql = "update t_zz_bzfa set is_delete = '1' where id = ?";
        int result = DatabaseHandlerDao.getInstance().executeSql(sql,new Object[]{dataId});
        if(1==result) return "SUCCESS";
        else return "ERROR";
    }

    public Object getTrpxx(String lx) {
        String tabName ="";
        String tymName = "";
        String tymBH = "";
        if(lx.equals("bzfa")){
            tabName = "T_SDZYC_ZZZM";
            tymName = "ZZZMMC";
            tymBH = "ZZZMBH";
        }else if(lx.equals("NY")){
            tabName = "T_SDZYC_NYXX";
            tymName = "NYTYM";
            tymBH = "NYBH";
        }else if(lx.equals("FL")){
            tabName = "T_SDZYC_FLXX";
            tymName = "FLTYM";
            tymBH = "FLBH";
        }else if(lx.equals("NJJ")){
            tabName = "T_SDZYC_NJJXX";
            tymName = "NJJMC";
            tymBH = "NJJBH";
        }
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select distinct t."+tymName+" as value, t."+tymName+" as text from "+tabName+" t where t.qybm = '" + qybm + "'";
        if(lx.equals("bzfa")||lx.equals("NJJ")){
            sql = "select distinct t."+tymName+", t."+tymBH+", t.YT from "+tabName+" t where t.qybm = '" + qybm + "'";
        }
        List<Map<String,Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        return maps;
    }
    public Object getTym() {
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select t.zzzmc as text, t.zzzmc as value, t,yt from t_sdzyc_zzzm t where t.qybm = ?";
        List<Map<String,Object>> maps = DatabaseHandlerDao.getInstance().queryForMaps(sql,new Object[]{qybm});
        return maps;
    }

    public Object deleteBzfaTrp(String dataId) {
        String sql = "update t_zz_bzfatrp set is_delete = '1' where id = ?";
        int result = DatabaseHandlerDao.getInstance().executeSql(sql,new Object[]{dataId});
        if(1==result) return "SUCCESS";
        else return "ERROR";
    }
}