package com.ces.component.sczzpccc.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SczzpcccService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    public Object getPrint(String id){
        String sql = "select t.ddbh as xsddh,t.khmc,t.ccsj,t.psdz,t.sfdh from t_zz_pccckhxx t where t.pid = '" + id + "'";
        List<Map<String,Object>> printData = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString());
        return printData;
    }


    public Object queryDetails(String id){
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        Map<String,Object> resultMap = new HashMap<String, Object>();
        StringBuilder sql ;
        sql = new StringBuilder("SELECT * FROM T_ZZ_PCCC T WHERE T.ID = ?");
        Map<String, Object> formData = DatabaseHandlerDao.getInstance().queryForMap(sql.toString(), new Object[]{id});
        sql = new StringBuilder("SELECT KCZL FROM T_ZZ_CSNZWXQ T WHERE T.PCH = '" + formData.get("PCH") + "' AND T.IS_DELETE <> '1'");
        formData.put("KCZL", DatabaseHandlerDao.getInstance().queryForMap(sql.toString()).get("KCZL"));
        resultMap.put("formData", formData);
        sql = new StringBuilder("SELECT * FROM T_ZZ_PCCCKHXX T WHERE T.PID = ? AND T.IS_DELETE <> '1'");
        resultMap.put("gridData", DatabaseHandlerDao.getInstance().queryForMaps(sql.toString(), new Object[]{String.valueOf(formData.get("ID"))}));
        return resultMap;
    }

    @Transactional
    public Object getPsdzByDdbh(String ddbh){
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        StringBuilder sql = new StringBuilder("SELECT A.ID AS AID,B.ID AS BID,A.DDBH,A.SHDZ AS PSDZ,B.PLBH,B.ZL,A.SHSJ AS CCSJ FROM T_ZZ_XSDDXX A LEFT JOIN T_ZZ_DDSHXX B ON A.ID = B.PID AND B.IS_DELETE <> '1' WHERE A.IS_DELETE <> '1' AND A.QYBM = '" + qybm + "' AND A.DDBH = '" + ddbh + "'");
        List<Map<String, Object>> dataList = DatabaseHandlerDao.getInstance().queryForMaps(sql.toString());
        return dataList;
    }
}