package com.ces.component.zzickxxgl.service;

import com.ces.component.trace.utils.CompanyInfoUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.UUIDGenerator;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component

public class ZzickxxglService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {
    @Transactional
    public int saveicbgb(String ID,String XM,String SFZH,String ICKKH,String XB,String QYZT,String GW,String BZ){
        String sql1="select * from T_ZZ_ICKXXGL where ICKKH='"+ICKKH+"'";
        Map<String,Object> dataMap= DatabaseHandlerDao.getInstance().queryForMap(sql1);

        if(dataMap.isEmpty()){
            String id= UUIDGenerator.uuid();
            String QYBM=SerialNumberUtil.getInstance().getCompanyCode();
            String sql2="insert into T_ZZ_ICKXXGL(id,CREATE_TIME,ICKKH,XM,GW,ZJHM,XB,QYZT,QYBM,BZ) VALUES('"+id+"','"+getFormatDate()+"','"+ICKKH+"','"+XM+"','"+GW+"','"+SFZH+"','"+XB+"','"+QYZT+"','"+QYBM+"','"+BZ+"')";
            return DatabaseHandlerDao.getInstance().executeSql(sql2);
            }else{
        String sql3="select * from T_ZZ_ICKBGB where ICKKH='"+ICKKH+"'";
           List<Map<String,Object>> listmap= DatabaseHandlerDao.getInstance().queryForMaps(sql3);

            if(listmap.isEmpty()){
                String id= UUIDGenerator.uuid();
                String bgqssyxm=dataMap.get("XM").toString();
                String czr= CompanyInfoUtil.getInstance().getCompanyName("ZZ", SerialNumberUtil.getInstance().getCompanyCode());
                String sysj=getYearMonthDay(dataMap.get("CREATE_TIME").toString())+"~"+getYearMonthDay(getFormatDate());
                String hzsysj=getYearMonthDay(getFormatDate())+"~至今";
                String czsj=getFormatDate();
                String sql2="update T_ZZ_ICKXXGL set ICKKH='"+ICKKH+"',XM='"+XM+"',GW='"+GW+"',ZJHM='"+SFZH+"',XB='"+XB+"',QYZT='"+QYZT+"',BZ='"+BZ+"' where id='"+ID+"'";
                String sql="insert into T_ZZ_ICKBGB(ID,BGQSYZXM,SYSJ,BGHSYXM,HZSYSJ,CZR,CZSJ,ICKKH) values ('"+id+"','"+bgqssyxm+"','"+sysj+"','"+XM+"','"+hzsysj+"','"+czr+"','"+czsj+"','"+ICKKH+"')";
                DatabaseHandlerDao.getInstance().executeSql(sql2);
                return DatabaseHandlerDao.getInstance().executeSql(sql);
            }else{
                int num= listmap.size();
                int index=listmap.get(num-1).get("HZSYSJ").toString().indexOf("~");
                String id= UUIDGenerator.uuid();
                String bgqssyxm=dataMap.get("XM").toString();
                String czr= CompanyInfoUtil.getInstance().getCompanyName("ZZ", SerialNumberUtil.getInstance().getCompanyCode());
                String sysj=getYearMonthDay(dataMap.get("CREATE_TIME").toString())+"~"+getYearMonthDay(getFormatDate());
                String hzsysj1 = listmap.get(num-1).get("HZSYSJ").toString().substring(0,index) +"~"+getYearMonthDay(getFormatDate());
                String hzsysj=getYearMonthDay(getFormatDate())+"~至今";
                String czsj=getFormatDate();
                String sql4="update t_zz_ickbgb set HZSYSJ='"+hzsysj1+"' where CZSJ=(select max(czsj) from t_zz_ickbgb)";
                String sql2="update T_ZZ_ICKXXGL set ICKKH='"+ICKKH+"',XM='"+XM+"',GW='"+GW+"',ZJHM='"+SFZH+"',XB='"+XB+"',QYZT='"+QYZT+"',BZ='"+BZ+"' where id='"+ID+"'";
                String sql="insert into T_ZZ_ICKBGB(ID,BGQSYZXM,SYSJ,BGHSYXM,HZSYSJ,CZR,CZSJ,ICKKH) values ('"+id+"','"+bgqssyxm+"','"+sysj+"','"+XM+"','"+hzsysj+"','"+czr+"','"+czsj+"','"+ICKKH+"')";
                DatabaseHandlerDao.getInstance().executeSql(sql4);
                DatabaseHandlerDao.getInstance().executeSql(sql2);
                return DatabaseHandlerDao.getInstance().executeSql(sql);
            }
        }
    }

    public String getFormatDate(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    public String getYearMonthDay(String dateformat){
        int num=dateformat.indexOf(" ");
        String rightdate=dateformat.substring(0,num);
        return rightdate;
    }
}