package com.ces.component.gjzzlb.service;

import ces.coral.lang.StringUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Component
public class GjzzlbService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    @Override
    protected String buildCustomerFilter(String tableId, String componentVersionId, String moduleId, String menuId, Map<String, Object> paramMap) {
        String qybm= SerialNumberUtil.getInstance().getCompanyCode();
        StringBuffer filter = new StringBuffer(AppDefineUtil.RELATION_AND + "  qybm = '"+qybm+"'");
//        String tableName = TableUtil.getTableName(tableId);
//        boolean isQybmColumnExist = DatabaseHandlerDao.getInstance().columnExists(tableName, "qybm");
//        if (isQybmColumnExist) {
//            String qybm = SerialNumberUtil.getInstance().getCompanyCode();
//            filter.append(AppDefineUtil.RELATION_AND + " qybm = '" + qybm + "'");
//        }
//        log.debug("表" + tableName + "的过滤条件：" + filter.toString());
        return filter.toString();


        //int i=0;
        // return "";
    }



    public Object getJdandWd(String zsm,String cppch,String lowdate,String highdate,String csqy){
        String sql;
        if((lowdate.isEmpty())&&(highdate.isEmpty())){
            sql="select * from V_ZLZZ_GJZZ where cpzsm like '%"+zsm+"%' and cppch like '%"+cppch+"%' and scqy like '%"+csqy+"%' ";
        }
        else if((!lowdate.isEmpty())&&(!highdate.isEmpty())){
            sql="select * from V_ZLZZ_GJZZ where cpzsm like '%"+zsm+"%' and cppch like '%"+cppch+"%' and scqy like '%"+csqy+"%' and (JDRQ between   '"+lowdate+"' and '"+highdate+"')";}
        else if((!lowdate.isEmpty())&&(highdate.isEmpty())){
            sql="select * from V_ZLZZ_GJZZ where cpzsm like '%"+zsm+"%' and cppch like '%"+cppch+"%' and scqy like '%"+csqy+"%' and jdrq > '"+lowdate+"'";
        }else
        {
            sql="select * from V_ZLZZ_GJZZ where cpzsm like '%"+zsm+"%' and cppch like '%"+cppch+"%' and scqy like '%"+csqy+"%' and jdrq < '"+lowdate+"'";
        }

         //sql="select * from V_ZLZZ_WTQD where cpzsm like '%"+zsm+"%' and cppch like '%"+cppch+"%' and scqy like '%"+csqy+"%' and (JDRQ between   '"+lowdate+"' and '"+highdate+"')";
        List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
        Map<String,Object> maps=new HashMap<String, Object>();
        list=DatabaseHandlerDao.getInstance().queryForMaps(sql);
        if(!list.isEmpty()){
            List<Map<String,Object>> listformap=new ArrayList<Map<String,Object>>();
            String content="(";
            for(int i=0;i<list.size();i++){
                if(i==0){content+=list.get(i).get("XSQX");}else{
                    content+=","+list.get(i).get("XSQX");
                }
                list.get(i).get("XSQX");
            }

//            for(int i=0;i<list.size();i++){
//                list.get(i).get("XSQX");
//                String sql1="select * from t_qypt_zhgl where zhbh='"+list.get(i).get("XSQX")+"'";
//                listformap.add(DatabaseHandlerDao.getInstance().queryForMap(sql1));
//            }
            String sql2="select q.*, tb.* from t_qypt_zhgl q , (select max(t.tpbcmc) as tpbcmc ，t.zbid from t_qypt_zhglfj t, (select id from t_qypt_zhgl) ta where t.zbid = ta.id  group by t.zbid) tb where q.id = tb.zbid and q.zhbh in"+content+")";
            listformap=DatabaseHandlerDao.getInstance().queryForMaps(sql2);
            maps.put("data",listformap);
//            List<List> listlist =new ArrayList<List>();
//            listlist.add(list);
//            listlist.add(listformap);
            return listformap;}else{return null;}
    }

    public Object getJdandWdByIs(String value){
        String sql="select * from V_ZLZZ_GJZZ where cpzsm like '%"+value+"%' or cppch like '%"+value+"%' or scqy like '%"+value+"%' or to_char(jdrq) like '%"+value+"%' or cpmc like '%"+value+"%'";
        List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
        list=DatabaseHandlerDao.getInstance().queryForMaps(sql);
        if(!list.isEmpty()){
            List<Map<String,Object>> listformap=new ArrayList<Map<String,Object>>();

            String content="(";
            for(int i=0;i<list.size();i++){
                if(i==0){content+=list.get(i).get("XSQX");}else{
                    content+=","+list.get(i).get("XSQX");
                }

            }
            String sql1="select * from t_qypt_zhgl where zhbh in "+content+" )";
            listformap=DatabaseHandlerDao.getInstance().queryForMaps(sql1);
            return listformap;}else{return null;}

    }

    public int changeWtbj(String value,String pch,String bzxs,String cpzsm){
        String sql;
        String sql1;
        if("散装小包装".equals(bzxs)){
        if("标记问题".equals(value)){
             sql="update T_ZZ_CSNZWXQ set WTBJ='"+1+"' where pch ='"+pch+"'";
             sql1="update T_ZZ_CCBZCPXX set ccwtbj ="+1+ "where cpzsm ='"+cpzsm+"'";
             DatabaseHandlerDao.getInstance().executeSql(sql1);
        }else{
             sql="update T_ZZ_CSNZWXQ set WTBJ='"+0+"' where pch ='"+pch+"'";
             sql1="update T_ZZ_CCBZCPXX set ccwtbj ="+0+ "where cpzsm ='"+cpzsm+"'";
             DatabaseHandlerDao.getInstance().executeSql(sql1);
        }}else
//        if("礼盒装".equals(bzxs))
        {
            if("标记问题".equals(value)){
                sql="update T_ZZ_CCBZCPXX set CCWTBJ="+1+" where CPZSM ='"+cpzsm+"'";
            }else{
                sql="update T_ZZ_CCBZCPXX set CCWTBJ="+0+" where CPZSM ='"+cpzsm+"'";
            }
        }
        int i= DatabaseHandlerDao.getInstance().executeSql(sql);
        return i;
    }

    public Object reloadGrid(String zsm,String cppch,String lowdate,String highdate,String csqy){
        String sql;
        if((lowdate.isEmpty())&&(highdate.isEmpty())){
            sql="select * from V_ZLZZ_GJZZ where cpzsm like '%"+zsm+"%' and cppch like '%"+cppch+"%' and scqy like '%"+csqy+"%' order by ccsj";
        }
        else if((!lowdate.isEmpty())&&(!highdate.isEmpty())){
            sql="select * from V_ZLZZ_GJZZ where cpzsm like '%"+zsm+"%' and cppch like '%"+cppch+"%' and scqy like '%"+csqy+"%' and (JDRQ between   '"+lowdate+"' and '"+highdate+"')  order by ccsj";}
        else if((!lowdate.isEmpty())&&(highdate.isEmpty())){
            sql="select * from V_ZLZZ_GJZZ where cpzsm like '%"+zsm+"%' and cppch like '%"+cppch+"%' and scqy like '%"+csqy+"%' and jdrq > '"+lowdate+"'  order by ccsj";
        }else
        {
            sql="select * from V_ZLZZ_GJZZ where cpzsm like '%"+zsm+"%' and cppch like '%"+cppch+"%' and scqy like '%"+csqy+"%' and jdrq < '"+lowdate+"'  order by ccsj";
        }

        //sql="select * from V_ZLZZ_WTQD where cpzsm like '%"+zsm+"%' and cppch like '%"+cppch+"%' and scqy like '%"+csqy+"%' and (JDRQ between   '"+lowdate+"' and '"+highdate+"')";
        List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
        Map<String,Object> maps=new HashMap<String, Object>();
        list=DatabaseHandlerDao.getInstance().queryForMaps(sql);
        maps.put("data",list);
        return maps;
    }

    public Object reloadGridIs(String value){
        String sql="select * from V_ZLZZ_GJZZ where cpzsm like '%"+value+"%' or cppch like '%"+value+"%' or scqy like '%"+value+"%' or to_char(jdrq) like '%"+value+"%' or cpmc like '%"+value+"%'";
        List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
        list=DatabaseHandlerDao.getInstance().queryForMaps(sql);
        Map<String,Object> maps=new HashMap<String, Object>();
        maps.put("data",list);
        return maps;
    }

    public Page BaseSearch(PageRequest pageRequest,String zsm,String pch,String qymc,String khmc,String cpmc,String aftdate,String befdate){
        String content="";
        String datecontent="";
        if(!"".equals(befdate)){
            datecontent=datecontent+" ccsj > '"+befdate+"' and ";
        }
        if(!"".equals(aftdate)){
            datecontent=datecontent+" ccsj < '"+ aftdate + "' and ";
        }

        if(!"".equals(zsm)){content=content+"cpzsm = '"+zsm+"' and ";}
        if(!"".equals(pch)){content=content+"pch = '"+pch+"' and ";}
        String sql = "select * from v_zlzz_gjzz where "+content+datecontent+" qymc like '%"+qymc+"%' and khmc like '%"+khmc+"%' and cpmc like '%"+cpmc+"%' and qybm = '"+SerialNumberUtil.getInstance().getCompanyCode()+"' order by ccsj";
        return queryPage(pageRequest, sql);
    }

    public static Page queryPage(PageRequest pageRequest, String sql) {
//        pageRequest = new PageRequest(pageRequest.getPageNumber() - 1, pageRequest.getPageSize());
        if (StringUtil.isBlank(sql)) {
            return null;
        }
        //查总数
        String count = "select count(*) as count from (" + sql + ")";
        Map map = DatabaseHandlerDao.getInstance().queryForMap(count);
        //总数
        long total = Long.parseLong(map.get("COUNT").toString());
        int begin = pageRequest.getOffset();
        int end = begin + pageRequest.getPageSize();
        if (begin > total) {
            int remainder = (int) (total % pageRequest.getPageSize());
            end = (int) total;
            begin = (int) (total - (remainder == 0 ? pageRequest.getPageSize() : remainder));
        }
        List<Map<String, Object>> content = DatabaseHandlerDao.getInstance().pageMaps(sql, begin, end);
        if (content == null) {
            return null;
        } else {
            return new PageImpl<Map<String, Object>>(content, pageRequest, total);
        }
    }

    public Object SearchXsqx(String zsm,String pch,String qymc,String khmc,String cpmc,String aftdate,String befdate){
        String content="";
        String datecontent="";
        if(!"".equals(befdate)){
            datecontent=datecontent+" ccsj > '"+befdate+"' and";
        }
        if(!"".equals(aftdate)){
            datecontent=datecontent+" ccsj < '"+ aftdate + "' and ";
        }
        if(!"".equals(zsm)){content=content+"cpzsm = '"+zsm+"' and ";}
        if(!"".equals(pch)){content=content+"pch = '"+pch+"' and";}
        String sql = "select * from t_zz_khxx where khbh in (select khbh from v_zlzz_tpzz where "+content+datecontent+" qymc like '%"+qymc+"%' and khmc like '%"+khmc+"%' and cpmc like '%"+cpmc+"%' and qybm = '"+SerialNumberUtil.getInstance().getCompanyCode()+"')";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql);
    }
    
    /**
     * 搜索客户信息的精度和纬度
     */
    public Object SearchKhxx(){
    	String qybm= SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select * from t_zz_khxx where khbh in (select khbh from v_zlzz_tpzz where qybm='"+qybm+"' )";    	
    	return DatabaseHandlerDao.getInstance().queryForMaps(sql);
    }
}