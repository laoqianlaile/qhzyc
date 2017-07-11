package com.ces.component.herb.service;

/**
 * Created by Administrator on 2016/6/1.
 */

import com.ces.component.trace.utils.QRCodeUtil;
import com.ces.component.trace.utils.StatisticalCodeUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.UUIDGenerator;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class nsxService {
    private final String TABLE_NAME = " v_zz_nsxgz ";

    /**
     * 查找用户需要完成的农事项
     * @param username
     * @return
     */
    public Map<String, Object> searchNsx(String username) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtil.isNotEmpty(username)) {
            String sql = "select distinct(dkbh),qybm from T_zz_scda t  where zzfzr = ? and t.is_delete != '1'";
            List<Map<String, Object>> zhgl = DatabaseHandlerDao.getInstance().queryForMaps(sql, new Object[]{username});
            if(zhgl.size() == 0) return new HashMap<String, Object>();
            String qybm = String.valueOf(zhgl.get(0).get("QYBM"));
            //cszt 0 已结束 1 未开始 2 进行中
            StringBuffer nsxglSql = new StringBuffer("select *  from v_zz_nsxgz t where  t.zt='3' and t.cszt != '0' and  ( ");
            for(int i=0;i<zhgl.size();i++){//遍历将筛选条件逐个加入到条件中
                String dkbh = String.valueOf(zhgl.get(i).get("DKBH"));
                nsxglSql.append(" t.dkbh='"+dkbh+"' or ");
            }
            nsxglSql.replace(nsxglSql.lastIndexOf("or"),nsxglSql.lastIndexOf("or")+2," ");//删除多余的判断条件
            nsxglSql.append(")");
            List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(nsxglSql.toString());
            if(list.size() == 0){
                return new HashMap<String, Object>();
            }
            map.put("data",list);
            List<Map<String,Object>>  nsxtrp = new ArrayList<Map<String, Object>>();
            List<Map<String, Object>> list_trp =new ArrayList<Map<String, Object>>();
            String zzpch = "";
            for (int  i=0;i<list.size();i++){
                //对农事项遍历
                String nsxlx = String.valueOf(list.get(i).get("NSXLX"));
                String scdabh = String.valueOf(list.get(i).get("SCDABH"));
                String id = String.valueOf(list.get(i).get("ID"));
                //其他投入品的搜索(默认)
                String sj = getQssj("t_Zz_Scqt",scdabh,username);//农事项间隔时间
                String nsxsj_sql = "select to_date(qsnsxsj,'yyyy-mm-dd')+"+sj+" as nsxsj  from t_zz_scda t where t.scdabh = '"+scdabh+"' and t.qybm = '"+qybm+"'";
                String nsx_sql = "select trpmc as  trp from (select * from t_Zz_Scqt where pid = (select t.id from t_zz_scda t where t.scdabh = '"+scdabh+"')) a " +
                        "join t_zz_scqttrp b on a.id = b.pid where a.is_delete = '0'";
                if("1".equals(nsxlx)){//播种
                    sj = getQssj("t_Zz_Scbz",scdabh,username);
                    //查询该农事项下的投入品名称
                    nsx_sql = "select zzzmmc as trp from t_sdzyc_zzzm z,(select trpmc as trp from (select  * from T_zz_scbz where pid IN (select t.id from  t_zz_scda t where t.scdabh= '"+scdabh+"')) a join  t_zz_scbztrp b on a.id=b.pid where a.is_delete='0') t where z.zzzmbh=t.trp or zzzmmc=t.trp" ;
                }else if ("2".equals(nsxlx)){//灌溉
                    sj = getQssj("T_zz_scgg",scdabh,username);
                    nsx_sql= "select sjmc as trp from t_common_sjlx_code c where c.sjbm= (select sylx" +
                            " from (select *  from T_zz_scgg  where pid = (select t.id" +
                            " from t_zz_scda t where t.scdabh = '"+scdabh+"')) a" +
                            " join t_zz_scggtrp b on a.id = b.pid where a.is_delete = '0') ";
                }else if ("3".equals(nsxlx)){//施肥
                    sj = getQssj("t_Zz_Scsf",scdabh,username);
                    nsx_sql="select trpmc as trp from (select * from t_Zz_Scsf where pid = (select t.id" +
                            " from t_zz_scda t where t.scdabh = '"+scdabh+"')) a" +
                            " join t_zz_scsftrp b on a.id = b.pid  where a.is_delete = '0'";
                }else if("4".equals(nsxlx)){//用药
                    sj = getQssj("t_Zz_Scyy",scdabh,username);
                    nsx_sql="select trpmc as trp from (select * from t_Zz_Scyy where pid = (select t.id from t_zz_scda t where t.scdabh = '"+scdabh+"')) a " +
                            "join t_zz_scyytrp b on a.id = b.pid where a.is_delete = '0'";
                }else if("5".equals(nsxlx)){//锄草
                    sj = getQssj("t_Zz_Sccc",scdabh,username);
                    nsx_sql = "select  trpmc as trp from (select * from t_Zz_Sccc where pid = (select t.id from t_zz_scda t where t.scdabh = '"+scdabh+"')) a " +
                            "join t_zz_sccctrp b on a.id = b.pid where a.is_delete = '0'";
                }else if ("6".equals(nsxlx)){//采收
                    sj = getQssj("t_Zz_Sccs",scdabh,username);
                    nsx_sql="select njjmc as trp from T_SDZYC_NJJXX t where t.njjbh= " +
                            "       (select trpmc from (select * from t_Zz_Sccs where pid = (select t.id from t_zz_scda t where t.scdabh = '"+scdabh+"')) a " +
                            "join t_zz_sccstrp b on a.id = b.pid where a.is_delete = '0')";
                     zzpch = getZzpch(id);//采收时获取种植批次号
                }
                //获取投入品
                List<Map<String,Object>>  map_trp= DatabaseHandlerDao.getInstance().queryForMaps(nsx_sql);
                //获取农事项时间
                Map<String,Object>  map_nsxsj= DatabaseHandlerDao.getInstance().queryForMap(nsxsj_sql);
                if(map_trp.size() != 0){//非空验证
                    if(StringUtil.isEmpty(map_trp.get(0).get("TRP"))){
                        map_trp.get(0).put("TRP", "无投入品");
                    }
                    String nsxsj = String.valueOf(map_trp.get(0).get("QSNSXSJ"));
                    map_trp.get(0).put("NSXSJ", sj);
                    if(zzpch != null){//非空判断
                        map_trp.get(0).put("ZZPCH", zzpch);
                    }
                /*临时方案 多投入品的方案只取第一个*/
                    list_trp.add(map_trp.get(0));
                }else{//查找不到投入品的情况
                    Map<String,Object> map_else = new HashMap<String, Object>();
                    map_else.put("TRP","无投入品");
                    list_trp.add(map_else);
                }
            }
            map.put("nsx", list_trp);
        }
        return map;
    }

    /**
     * 获取农事项间隔时间
     * * @param tabName
     * @param scdabh
     * @return
     */
    protected  String getQssj(String tabName,String scdabh,String zzfzr){
        String qssj_sql = "select * from "+tabName+" where is_delete ='0' and  pid IN (select id from t_zz_scda t where t.scdabh = '"+scdabh+"' and zzfzr=?) order by create_time";
        List<Map<String,Object>> qssj_data = DatabaseHandlerDao.getInstance().queryForMaps(qssj_sql, new Object[]{zzfzr});
        String jgsj = qssj_data.size()== 0?"0":String.valueOf(qssj_data.get(0).get("BUDGETTIME"));
        return jgsj;
    }

    protected String getZzpch(String id){
        String pch_sql = "select t.* from T_ZZ_SCDA t where t.id IN (select PID from  t_zz_sccs t  where t.id = ?)";
        Map<String,Object> pch_data = DatabaseHandlerDao.getInstance().queryForMap(pch_sql,new Object[]{id});
        if(StringUtil.isNotEmpty(pch_data))
            return (pch_data.get("ZZPCH")).toString();
        else
            return "";
    }
    /**
     *防止重复插入农事项开始时间
     * @param id
     * @return
     */
    public boolean dataVerify(String id){
        String user_sql = "select count(*) as num from t_zz_czjl where pid='"+id+"'";
        //通过Id查找用户 ,保证不重复插入
        Map<String, Object> flag = DatabaseHandlerDao.getInstance().queryForMap(user_sql);
        if (Integer.parseInt(String.valueOf(flag.get("NUM"))) != 0){
                return false;
        }
        return true;
    }
    /**
     * 点击开始按钮  插入时间
     * @param id
     */
    public  void  updateNsxzt(String id,String username){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String now = df.format(new Date());
        String uuid = UUIDGenerator.uuid();
        String sql= "select  *from T_ZZ_GZRYDA where xm='"+username+"' and is_delete='0'";
        List<Map<String,Object>>  maps = DatabaseHandlerDao.getInstance().queryForMaps(sql);
        Map<String, Object> map = maps.get(0);
        if(maps.size()>1){//若数据库存在相同用户名
            map = maps.get(0);
        }
        String czr = String.valueOf(map.get("XM")) ;
        String czrbh =String.valueOf(map.get("GZRYBH"));
        String czSql="insert into t_zz_czjl (id,is_delete,kssj,czr,czrbh,pid) values "+
                "('" + uuid+
                "','0','"+
                now+"','"+czr+"','"+czrbh+"','"+id+"')";
        DatabaseHandlerDao.getInstance().executeSql(czSql);
    }

    /**
     * 添加结束时间
     * @param id
     */
    public void endNsx(String id){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String now = df.format(new Date());
        String sql = "update t_zz_czjl set jssj='"+now+"' where pid='"+id+"'";
        DatabaseHandlerDao.getInstance().executeSql(sql);
        String nsxlx_sql = "select * from v_zz_nsxgz where nsxlx='6'  and id='"+id+"'";//根据ID查询类型为采收的数据
        Map<String,Object> nsxlx_data = DatabaseHandlerDao.getInstance().queryForMap(nsxlx_sql);
        if(nsxlx_data.size()!= 0)
        processQrCode(String.valueOf(nsxlx_data.get("ZZPCH")));
    }

    /**
     * 种植采收环节生成电子随附单
     * @param cspch
     */
    public void processQrCode(String cspch){
        //获取request
        HttpServletRequest request =(HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        String path = request.getRealPath("/")+"qrCode/zz";
        try {
            QRCodeUtil.encode(cspch, path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询采收记录信息（一次种植一次采收）
     * @param userName
     * @return
     */
    public List<Map<String,Object>> searchCsnsxList(String userName ){//zt 0 已结束 1 未开始 2 进行中
        String sql = "select * from t_zz_scda where  zzfzr='"+userName+"' order by zt desc,zzjssj desc";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql);
    }

    /**
     * 种植随附单获取采收批次号
     * @param zzpch
     * @return
     */
    public List<Map<String,Object>> getCspch(String zzpch){
        String cs_sql = "select * from t_sdzyc_csglxx t where t.zzpch='"+zzpch+"' order by t.kssj";
        List<Map<String,Object>> cs_map = DatabaseHandlerDao.getInstance().queryForMaps(cs_sql);
        if(StringUtil.isNotEmpty(cs_map))
            return cs_map;
        else
            return new ArrayList<Map<String, Object>>();
    }

    /**
     * 开始采收
     * @param id
     */
    public void  startRecovery(String id){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String now = df.format(new Date());
        String uuid = UUIDGenerator.uuid();
        String sql = "select * from t_zz_scda t where t.id='"+id+"'";
        Map<String, Object> map = DatabaseHandlerDao.getInstance().queryForMap(sql);
        ServletActionContext.getRequest().getSession().setAttribute("_companyCode_", map.get("QYBM"));//将企业编码写入session
        String cspch = StatisticalCodeUtil.getInstance().getTwentyFivePcm("ZZ","SDZYC","SDZYCYLJYJCBH");//生成采收批次号
        String zzpch = String.valueOf(map.get("ZZPCH"));//种植批次号
        String qypch = zzpch.substring(zzpch.length()-11,zzpch.length());//企业批次号
        String qycspch = cspch.substring(cspch.length()-11,cspch.length());//企业采收批次号
        String qybm = String.valueOf(map.get("QYBM"));//企业编码
        String csSql="insert into t_sdzyc_csglxx (id,is_delete,kssj,qybm,ycmc,ycdm,cspch,zzpch,qypch,qycspch) values "+
                "('" + uuid
                +"','0','"
                + now+"','"
                +qybm+"','"
                +String.valueOf(map.get("YCMC"))+"','"
                +String.valueOf(map.get("YCDM"))+"','"
                +cspch+"','"
                +zzpch+"','"
                +qypch+"','"
                +qycspch
                +"')";
        DatabaseHandlerDao.getInstance().executeSql(csSql);
        //更改种植方案状态
        String checkSql = "select * from t_sdzyc_csglxx where id='"+uuid+"'";
        Map<String,Object> data = DatabaseHandlerDao.getInstance().queryForMap(checkSql);//确认采收管理表中是否真的插入数据
        if(data.size() != 0){//zt 0 已结束 1 未开始 2 进行中
            String processStatusSql = "update t_zz_scda set zt ='2' where id='"+id+"'";
            DatabaseHandlerDao.getInstance().executeSql(processStatusSql);
        }
    }

    /**
     * 结束采收且生成电子随附单
     * @param zzpch
     * @param cszl
     * @param csmj
     */
    public void endRecovery(String zzpch,String cszl,String csmj){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String now = df.format(new Date());
        if(StringUtil.isNotEmpty(zzpch)){//zt 0 已结束 1 未开始 2 进行中
            String sql = "update t_sdzyc_csglxx set jssj='"+now+"',cszl='"+cszl+"',csmj='"+csmj+"' where zzpch='"+zzpch+"'";
            DatabaseHandlerDao.getInstance().executeSql(sql);
            String zt_sql = "update t_zz_scda set zt ='0',zzjssj='"+now+"' where zzpch='"+zzpch+"'";
            DatabaseHandlerDao.getInstance().executeSql(zt_sql);
            String cspch_sql = "select * from t_sdzyc_csglxx t where t.zzpch='"+zzpch+"' order by t.kssj";
            List<Map<String,Object>> cspch_data = DatabaseHandlerDao.getInstance().queryForMaps(cspch_sql);
            String cspch = String.valueOf(cspch_data.get(0).get("CSPCH"));
            processQrCode(cspch);
        }
    }

}