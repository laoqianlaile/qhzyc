package com.ces.component.zlyjya.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.utils.StringUtil;
import org.apache.http.HttpRequest;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.xarch.core.entity.StringIDEntity;

/**
 * Created by Administrator on 2015/9/18.
 */
@Component
public class ZlyjyaService  extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

    public List<Map<String,Object>> loadRadio(){

        String sql="select distinct yabh, yamc from t_zl_yjya where is_delete <> '1'";
        List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
        Map<String,Object> map=new HashMap<String, Object>();
        list= DatabaseHandlerDao.getInstance().queryForMaps(sql);
        Map<String,Object> maps = new HashMap<String, Object>();
        maps.put("data", list);
        return list;

    }

    public List<Map<String,Object>> getColdata(String yabh){
        String sql="select ZDMC,XSMC from t_zl_yjya where yabh='"+yabh+"' and qybm = '"+ SerialNumberUtil.getInstance().getCompanyCode()+"'";
        List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
        list=DatabaseHandlerDao.getInstance().queryForMaps(sql);

        return list;
    }

    /**
     * 查询过滤
     * @param cpmc
     * @param PCH
     * @param CPZSM
     * @return
     */

    public Map<String,Object> searchGridInfo(String cpmc,String PCH,String CPZSM,String yalx){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date qssj=new Date();
        Date jssj=new Date();
        String tablename="";
        if("yyxx".equals(yalx)){tablename="v_yjya_yyxx";}else if("xsqx".equals(yalx)){
            tablename="v_yjya_xsqx";
        }

        //String sql="select * from v_yjya_yyxx where cpmc like'%"+cpmc+"%' and  cpzsm like '%"+CPZSM+"%' and pch like '%"+PCH+"%'";
        String sql="select * from  "+tablename+" where cpmc like'%"+cpmc+"%' and  cpzsm like '%"+CPZSM+"%' and pch like '%"+PCH+"%'";
        List<Map<String,Object>> list= DatabaseHandlerDao.getInstance().queryForMaps(sql);
        Map<String,Object> dataMap = new  HashMap<String, Object>();
        dataMap.put("data",list);
        return dataMap;
    }

    /**
     * 获取列表数据
     *
     * @param pageRequest
     * @param list
     * @return
     */
    public Map<String,Object> getGriddata(PageRequest pageRequest, String[] list, String yalx){
        String sql="";
        String tablename="";
        if("yyxx".equals(yalx)){tablename="v_yjya_yyxx";}else if("xsqx".equals(yalx)){
            tablename="v_yjya_xsqx";
        }
        for(int i=0;i<list.length;i++){
            if(i+1==list.length){sql+=list[i]+" ";}else{sql+=list[i]+",";}

        }
        sql="select  "+sql+"from  "+tablename+" where qybm = '"+SerialNumberUtil.getInstance().getCompanyCode()+"'";
        Map<String,Object> listmap= (Map<String,Object>)queryPage(pageRequest,sql);
        return listmap;
    }

    /**
     * 导出列表
     * @param tableHeaderList
     * @param idsList
     * @param yabh
     * @return
     */
    public List<Map<String,Object>> exportExcel(String[] tableHeaderList,String[] idsList,String yabh,String yalx){
        String sql=" ";
        String tablename="";
        if("yyxx".equals(yalx)){tablename="v_yjya_yyxx";}else if("xsqx".equals(yalx)){
            tablename="v_yjya_xsqx";
        }

        for(int i=0;i<tableHeaderList.length;i++){
            if(i==0){sql+=tableHeaderList[i];}else{
                sql+=","+tableHeaderList[i];
            }
        }
        String filter="(";
        for(int i=0;i<idsList.length;i++){
            if(i==0){filter+=idsList[i];}else{
                filter+=","+idsList[i];
            }
        }
        sql="select "+sql+" from "+tablename+"  where id in"+filter+")";
        List<Map<String,Object>> list=new ArrayList<Map<String, Object>>();
        list=DatabaseHandlerDao.getInstance().queryForMaps(sql);
        exportExcelTable(list,tableHeaderList,yabh);
        return list;
    }

    /**
     * 导出excel表格
     * @param list
     * @param tableHeaderList
     * @return
     */
    public String exportExcelTable(List<Map<String,Object>> list,String[] tableHeaderList,String yabh){
        String sql="select ZDMC,XSMC from t_zl_yjya where yabh='"+yabh+"'";
        List<Map<String,Object>> listfor=new ArrayList<Map<String,Object>>();
        listfor=DatabaseHandlerDao.getInstance().queryForMaps(sql);
        List<String> listforName=new ArrayList<String>();
        for(int m=0;m<tableHeaderList.length;m++){
            for(int j=0;j<tableHeaderList.length;j++){
                if(listfor.get(m).get("ZDMC").toString().equals(tableHeaderList[j])){
                    listforName.add(listfor.get(m).get("XSMC").toString());
                }
            }
        }
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet s = wb.createSheet();



        //HSSFRow r = s.createRow(1);
        //HSSFCell c =r.createCell(0);
        //设置表头
        int daw=0;
        for(int i=0;i<list.size()+1;i++){
            //list.
            HSSFRow r=s.createRow(i);
            for(int k=0;k<tableHeaderList.length;k++){
                if(i==0){
                    listfor.get(k).get(tableHeaderList[k]);
                    r.createCell(k).setCellValue(listforName.get(k));
                //r.createCell(k).setCellValue(listfor.get(k).get(tableHeaderList[k]).toString());
                }
                else{
                    if(list.get(i-1).get(tableHeaderList[k])==null){r.createCell(k).setCellValue("");}else{
                    r.createCell(k).setCellValue(list.get(i-1).get(tableHeaderList[k]).toString());}
                }
            }
        }

        FileOutputStream out = null;
        Long timestamp=System.currentTimeMillis();
        HttpServletRequest request = ServletActionContext.getRequest();        
        String pah= request.getSession().getServletContext().getRealPath("/");
        String filePath=pah+"spzstpfj\\exportTable.xls";
       // Date timestamp=new Date();
        File f = new File(filePath);
        if(f.exists()){
        	f.delete();
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
     * 获取预案类型
     */
    public Map<String,Object> getYalx(String yabh){
        String sql="select distinct yalx from t_zl_yjya where yabh ='"+yabh+"' and is_delete <> '1'";
        return DatabaseHandlerDao.getInstance().queryForMap(sql);
    }

    public Object getYyChartData () {
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select sum(v.yl) yl, v.trpmc from (select * from v_yjya_yyxx where qybm = ?) v group by v.trpmc";
        List<Map<String,Object>> mapList = DatabaseHandlerDao.getInstance().queryForMaps(sql,new Object[]{qybm});
        return mapList;
    }

    public Object getXsqxChartData () {
        String qybm = SerialNumberUtil.getInstance().getCompanyCode();
        String sql = "select count(*)as sl, khmc from (select * from v_yjya_xsqx where qybm = ?)v group by v.khmc ";
        List<Map<String,Object>> mapList = DatabaseHandlerDao.getInstance().queryForMaps(sql,new Object[]{qybm});
        return mapList;
    }

    /**
     * 分页查询
     *
     * @param pageRequest
     * @param sql
     * @return Object
     */
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
}