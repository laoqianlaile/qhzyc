package com.ces.component.zzddtj.service;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.StringUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ZzddtjService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {
    private static List<Map<String,Object>> list=null;

    /**
     * 列表查询方法
     * @param khlx
     * @param khbh
     * @param cpmc
     * @param qssj
     * @param jssj
     * @return
     */

    public Object searchGridData(String khlx,String khbh,String cpmc,String qssj,String jssj,PageRequest pageRequest){
        String sql;
        String condition=(khlx==""?"":(" and N.KHLX = '"+khlx+"' "))+(khbh==""?"":(" and N.khbh = '"+khbh+"' "))+(qssj==""?"":(" and t.xdrq >= '"+qssj+"'"))+(jssj==""?"":(" and t.xdrq <= '"+jssj+"'"))+("and T.qybm = '"+SerialNumberUtil.getInstance().getCompanyCode()+"'");

        //String sql="SELECT T.QYBM, T.KHMC,T.KHBH,K.CPMC ,SUM(CPSL) as cpsl,NULL as pz,NULL as zlhj FROM T_ZZ_XSDDXX T,T_ZZ_DDCPXX K where T.ID=K.PID "+condition+(cpmc==""?"":(" and cpmc='"+cpmc+","))+" GROUP BY T.KHMC,T.KHBH,K.CPMC,T.QYBM   UNION ALL SELECT T.QYBM , T.KHMC,T.KHBH,NULL,NULL,K.PZ,SUM(ZL) AS ZL FROM T_ZZ_XSDDXX T,T_ZZ_DDSHXX K where T.ID=K.PID "+condition+" GROUP BY T.KHMC,T.KHBH,K.PZ,T.QYBM";
        if(cpmc==""){
            sql="SELECT T.QYBM, T.KHMC,T.KHBH,M.CPMC ,SUM(CPSL) as cpsl,NULL as pz,NULL as zlhj FROM T_ZZ_XSDDXX T,T_ZZ_DDCPXX K ,T_ZZ_CPXXGL M ,T_ZZ_KHXX N where N.QYBM=T.QYBM AND  N.KHBH=T.KHBH AND K.CPMC=M.CPBH and T.ID=K.PID "+condition+(cpmc==""?"":(" and K.cpmc='"+cpmc+"'"))+" and M.qybm= '"+SerialNumberUtil.getInstance().getCompanyCode()+"' GROUP BY T.KHMC,T.KHBH,M.CPMC,T.QYBM   UNION ALL SELECT T.QYBM , T.KHMC,T.KHBH,NULL,NULL,K.PZ,SUM(ZL) AS ZL FROM T_ZZ_XSDDXX T,T_ZZ_DDSHXX K ,T_ZZ_KHXX N where N.QYBM=T.QYBM AND  N.KHBH=T.KHBH and T.ID=K.PID "+condition+" GROUP BY T.KHMC,T.KHBH,K.PZ,T.QYBM";

        }else{
            sql="SELECT T.QYBM, T.KHMC,T.KHBH,M.CPMC ,SUM(CPSL) as cpsl,NULL as pz,NULL as zlhj FROM T_ZZ_XSDDXX T,T_ZZ_DDCPXX K ,T_ZZ_CPXXGL M ,T_ZZ_KHXX N where N.QYBM=T.QYBM  AND N.KHBH=T.KHBH AND K.CPMC=M.CPBH and T.ID=K.PID "+condition+(cpmc==""?"":(" and K.cpmc='"+cpmc+"'"))+" and M.qybm= '"+SerialNumberUtil.getInstance().getCompanyCode()+"'  GROUP BY T.KHMC,T.KHBH,M.CPMC,T.QYBM  ";
        }
        return queryPage(pageRequest, sql.toString());
    }

    private Object queryPage(PageRequest pageRequest, String sql) {
        if (StringUtil.isEmpty(sql)) {
            return null;
        }
        //查总数
        String count = "select count(*) as count from (" + sql + ")";
        System.out.println("countSql:" + count);
        Map map = DatabaseHandlerDao.getInstance().queryForMap(count);
        //总数
        double total = Double.parseDouble(map.get("COUNT").toString());
        int pagesize = pageRequest.getPageSize();
        //总页数
        int totalPages = 0;
        if (pagesize == 0) {
            totalPages = (int) Math.ceil(total);
        } else {
            totalPages = (int) Math.ceil(total / pagesize);
        }
        int begin = pageRequest.getOffset();
        int end = begin + pagesize;
        if (begin > total) {
            int remainder = (int) (total % pageRequest.getPageSize());
            end = (int) total;
            begin = (int) (total - (remainder == 0 ? pageRequest.getPageSize() : remainder));
        }

        List<Map<String, Object>> data = DatabaseHandlerDao.getInstance().pageMaps(sql, begin, end);
//        list.clear();
        list=data;
        for(int i=0;i<list.size();i++){
            list.get(i).put("ID",i+1);
        }
        if (data == null) {
            return null;
        } else {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("data", data);
            resultMap.put("total", (int) total);
            resultMap.put("totalPages", totalPages);
            resultMap.put("pageNumber", pageRequest.getPageNumber() + 1);
            return resultMap;
        }
    }

    /**
     * 导出ecxel
     * @param idarr
     * @return
     */
    public Object printGrid(String[] idarr){

       //数据处理
        String[] headerlist=new String[]{"客户名称","产品名称","产品数量","散货品种","散货总重量"};
        List<String> idlist=new ArrayList<String>();
        if(idarr.length==0){
            if(list==null){return null;}
            else{
                for(int i=0;i<list.size();i++){
                    idlist.add(""+(list.get(i).get("ID")));
                }
            }
        }else{
            for(int i=0;i<idarr.length;i++){
                idlist.add(idarr[i]);
            }
        }

        List<ArrayList<Object>> listdata=new ArrayList<ArrayList<Object>>();
        for(int i=0;i<idlist.size();i++){
            ArrayList<Object> listi=new ArrayList<Object>();
            listdata.add(listi);
        }
        int l=0;
        for(int i=0;i<list.size();i++){
            if(idlist.contains(String.valueOf(list.get(i).get("ID")))){
                listdata.get(l).add(list.get(i).get("KHMC")==null?"":list.get(i).get("KHMC"));
                listdata.get(l).add(list.get(i).get("CPMC")==null?"":list.get(i).get("CPMC"));
                listdata.get(l).add(list.get(i).get("CPSL")==null?"":list.get(i).get("CPSL"));
                listdata.get(l).add(list.get(i).get("PZ")==null?"":list.get(i).get("PZ"));
                listdata.get(l).add(list.get(i).get("ZLHJ")==null?"":list.get(i).get("ZLHJ"));
                l++;
            }
        }
        //创建exel表格
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet s = wb.createSheet();

        //设置表头

//        各个标签写入
        for(int i=0;i<idlist.size()+1;i++){
            //list.
            HSSFRow r=s.createRow(i);
            if(i==0){
                for(int k=0;k<headerlist.length;k++){
                    r.createCell(k).setCellValue(headerlist[k]);
                }
            }else{
                for(int k=0;k<headerlist.length;k++){
                    r.createCell(k).setCellValue(String.valueOf(listdata.get(i-1).get(k)));
                }
            }
        }
        String path = ServletActionContext.getServletContext().getRealPath("/spzstpfj");
        //String FilePath = path + File.separator + String.valueOf(fileName.get("TPBCMC"));

        //将文件复制到指定的位置.
        FileOutputStream out = null;

        //String filePath="d:/"+timestamp+".xls";
//        String filePath="/Users/wngyu/desktop/"+timestamp+".xls";
        String filePath=path+"/ddtj.xls";
        File filemodul=new File(filePath);
        if(filemodul.exists()){
            filemodul.delete();
        }
        try {
            out = new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            wb.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "1";
    }

    /**
     * 初始化客户类型下拉框
     * @return
     */
    public Object loadKhlx(){
        return null;
    }

    /**
     * 初始化客户名称下拉框
     * @param khlx
     * @return
     */
    public Object loadKhmc(String khlx){
        String sql="select * from t_zz_khxx where khlx = '"+khlx+"' and qybm = '"+ SerialNumberUtil.getInstance().getCompanyCode()+"'";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql);
    }

    /**
     * 初始化产品名称下拉框
     * @return
     */
    public Object loadCpmc(){
        String sql="select * from t_zz_cpxxgl where qybm ='"+ SerialNumberUtil.getInstance().getCompanyCode()+"'";
        return DatabaseHandlerDao.getInstance().queryForMaps(sql);
    }

    
}