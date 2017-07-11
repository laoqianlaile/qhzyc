package com.ces.component.zltpzz.service;

import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 同批追踪Serivce
 * Created by dengjinhui on 15/9/10.
 */
@Component
public class ZltpzzService extends TraceShowModuleDefineDaoService<StringIDEntity,TraceShowModuleDao>{
    /**
     * 查询追溯吗对应的产品批次号
     * @param zsm 追溯码
     * @return
     */
    public List<Map<String,Object>> queryCppchByZsh(String zsm){
//        String sql = "SELECT BZPL.CSLSH\n" +
//                "  FROM T_ZZ_CCGL     CC,\n" +
//                "       T_ZZ_CCBZCPXX CCBZ,\n" +
//                "       T_ZZ_BZGLDYMX BZDY,\n" +
//                "       T_ZZ_BZGL     BZ,\n" +
//                "       T_ZZ_BZGLPLXX BZPL\n" +
//                " WHERE CC.ID = CCBZ.PID\n" +
//                "   AND CCBZ.CPZSM = BZDY.CPZSM\n" +
//                "   AND BZDY.PID = BZ.ID\n" +
//                "   AND BZ.ID = BZPL.PID AND CCBZ.CPZSM = ?";
        String sql="select ID, CPZSM,CPPCH,CPMC,SCQY,SCJD,SCDK,SCFZR,CCSJ from V_ZLZZ_WTQD where CPZSM='"+zsm+"'";
        String sql1="select ID, CPZSM,CPPCH,CPMC,SCQY,SCJD,SCDK,SCFZR,CCSJ from V_ZLZZ_WTQD";
        if(StringUtil.isNotEmpty(zsm))
            return DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{zsm});
        else
            return DatabaseHandlerDao.getInstance().queryForMaps(sql1);
    }
    /**
     *
     * 查询同批追踪列表
     * @param zsm 对应追溯码
     * @return
    */
    public Page queryTpzzlb(String zsm ,PageRequest pageRequest){
        if(StringUtil.isNotEmpty(zsm)){
//        List<Map<String,Object>> pchMaps = queryCppchByZsh(zsm);
//        if(pchMaps==null)
//            return null;
//        String pch="";
//        for(int i=0;i<pchMaps.size();++i){
//            Map map = pchMaps.get(i);
//            pch+=map.get("CPPCH");
//            if(i<pchMaps.size()-1){
//                pch+="','";
//            }
//        }
//        String sql = "SELECT T.CPZSM AS ZSH,\n" +
//                "       T.CSLSH AS CPPH,\n" +
//                "       T.CPMC AS CPMC,\n" +
//                "       T.QYMC AS SCQY,\n" +
//                "       T.JDMC AS SCJD,\n" +
//                "       T.DKMC AS SCDK,\n" +
//                "       T.FZR AS SCFZ,\n" +
//                "       T.CCSJ AS CCRQ\n" +
//                "  FROM V_ZLZZ_WTQD T WHERE T.QYBM = '"+ SerialNumberUtil.getInstance().getCompanyCode()+"' AND T.CSLSH in('"+pch+"')";
        String sql="select ID,CPZSM,CPPCH,CPMC,SCQY,SCJD,SCDK,SCFZR,CCSJ from V_ZLZZ_WTQD where CPZSM='"+zsm+"'";
        return queryPage(sql,pageRequest);}
        else{
            List<Map<String,Object>> pchMaps = queryCppchByZsh(zsm);
            String sql="select ID, CPZSM,CPPCH,CPMC,SCQY,SCJD,SCDK,SCFZR,CCSJ from V_ZLZZ_WTQD";
            return queryPage(sql,pageRequest);
        }
    }
    /**
     * 分页查询
     * @param sql
     * @param pageRequest
     * @return
     */
    public Page queryPage(String sql,PageRequest pageRequest){
        if(StringUtil.isEmpty(sql)){
            return null;
        }
        //查总数
        String count = "SELECT COUNT(*) AS COUNT FROM ("+sql+")";
        Map map = DatabaseHandlerDao.getInstance().queryForMap(count);
        //总数
        long total = Long.parseLong(map.get("COUNT").toString());
        int begin = pageRequest.getOffset();
        int end = begin + pageRequest.getPageSize();
        if(begin >total){
            int remainder = (int)(total % pageRequest.getPageSize());
            end = (int) total;
            begin = (int)(total - (remainder == 0 ? pageRequest.getPageSize() : remainder));
        }

        List<Map<String,Object>> content = DatabaseHandlerDao.getInstance().pageMaps(sql,begin,end);
        if(content == null){
            return null;
        }else{
            return new PageImpl(content,pageRequest,total);
        }
    }

    /**
     * 标记问题修改字段
     * @param id
     * @param bj
     * @return
     */
    public int changeWtbj(String id,String bj){
        String sql;
        if("标记问题".equals(bj)){
            sql="update T_ZZ_CSGL set WTBJ ='1' where id='"+id+"'";
        }
        //return DatabaseHandlerDao.getInstance().executeSql(sql);}
        else {
            sql="update T_ZZ_CSGL set WTBJ ='0' where id='"+id+"'";

        }
        return DatabaseHandlerDao.getInstance().executeSql(sql);

    }

    /**
     * 返回销售去向的门店信息
     * @param cpzsm
     * @return
     */

    public Object getJdandWd(String cpzsm){
        String sql="select distinct KHBH from V_ZLZZ_TPZZ where cpzsm like '%"+cpzsm+"%'";
        //List<String> list=new ArrayList();
        List<Map<String,Object>> list=new ArrayList<Map<String, Object>>();
        list=DatabaseHandlerDao.getInstance().queryForMaps(sql);
        if(!list.isEmpty()){
        List<Map<String,Object>> listformap=new ArrayList<Map<String,Object>>();
        String content="(";
        for(int i=0;i<list.size();i++){
            if(i==0){content+=list.get(i).get("KHBH");}else{
                content+=","+list.get(i).get("KHBH");
            }
            list.get(i).get("KHBH");
        }
            String sql1="select * from t_qypt_zhgl zhgl,t_qypt_zhglfj zhglfj where zhbh in "+content+") and zhgl.id=zhglfj.zbid and rownum = 1";
            String sql3="select * from t_zz_khxx where khbh in "+content+")";
            String sql2="select q.*, tb.* from t_qypt_zhgl q , (select max(t.tpbcmc) as tpbcmc ，t.zbid from t_qypt_zhglfj t, (select id from t_qypt_zhgl) ta where t.zbid = ta.id  group by t.zbid) tb where q.id = tb.zbid and q.zhbh in"+content+")";
            listformap=DatabaseHandlerDao.getInstance().queryForMaps(sql3);
        return listformap;}else{return null;}
    }

    public Object reloadGrid(String cpzsm){
//        String sql="select distinct xsqx from V_ZLZZ_WTQD where cpzsm like '%"+cpzsm+"%'";
        String sql;
        String sql1="select bzxs from t_zz_bzgl where cpzsm ='"+cpzsm+"' and qybm = '"+SerialNumberUtil.getInstance().getCompanyCode()+"'";
        Map<String,Object> zsmmap=DatabaseHandlerDao.getInstance().queryForMap(sql1);
        if("散装小包装".equals(zsmmap.get("BZXS"))){
            sql =" select * from v_zlzz_tpzz where pch in (select pch from v_zlzz_tpzz where cpzsm = '"+cpzsm+"') and qybm = '"+SerialNumberUtil.getInstance().getCompanyCode()+"'";
        }else{
            sql="select * from v_zlzz_tpzz where cpzsm = '"+cpzsm+"' and qybm = '"+SerialNumberUtil.getInstance().getCompanyCode()+"'";

        }

        List<Map<String,Object>> list=new ArrayList<Map<String, Object>>();
        list=DatabaseHandlerDao.getInstance().queryForMaps(sql);
        Map<String,Object> maps=new HashMap<String, Object>();
        maps.put("data",list);
        return maps;
    }

    public Page reloadGridBX(PageRequest pageRequest){
        String sql="select * from v_zlzz_tpzz where qybm = '"+SerialNumberUtil.getInstance().getCompanyCode()+"'";
        return queryPage(pageRequest, sql);
    }

    public static Page queryPage(PageRequest pageRequest, String sql) {
//        pageRequest = new PageRequest(pageRequest.getPageNumber() - 1, pageRequest.getPageSize());
        if (ces.coral.lang.StringUtil.isBlank(sql)) {
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

    public Page reloadGridAtferClick(PageRequest pageRequest){
        String sql="select * from v_zlzz_tpzz where qybm = '"+SerialNumberUtil.getInstance().getCompanyCode()+"'";
        return queryPage(pageRequest, sql);
    }

}