package com.ces.component.sdzycdzjgmdr.service;

import com.ces.component.trace.utils.DataTypeConvertUtil;
import com.ces.component.trace.utils.JdbcDaoUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.StatisticalCodeUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.UUIDGenerator;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.stereotype.Component;

import com.ces.component.sdzycdzjgmdr.dao.SdzycdzjgmdrDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Component
public class SdzycdzjgmdrService extends TraceShowModuleDefineDaoService<StringIDEntity, SdzycdzjgmdrDao> {

    private static Connection conn;
    private final String REAL_PATH = ServletActionContext.getServletContext().getRealPath("/spzstpfj");
    public Map<String, String> saveData(File file,String fileName,String fileContentType, Map map) {
        boolean isAdd = false;
        Map<String, String> newMap = DataTypeConvertUtil.getInstance().mapObj2mapStrFile(map);
        newMap.put("WJMC",fileName);
        newMap.put("SCSJ", DataTypeConvertUtil.getInstance().DataConvert("yyyy-MM-dd HH:mm:ss",new Date()));
        newMap.put("QYBM", SerialNumberUtil.getInstance().getCompanyCode());
        String id = newMap.get("ID");
        if (StringUtil.isEmpty(id)) {
            isAdd = true;
            newMap.put("ZT","0");
        }
        //不管新增还是修改都需要重新处理电子监管码
        if (!isAdd) {//如果是修改在删除旧的数据
            String sql = "delete from t_sdzyc_dzjgm where pid = ?";
            DatabaseHandlerDao.getInstance().executeSql(sql, new String[]{id});
        }
        if("xml".equalsIgnoreCase(fileContentType)) {
            xmlDom4j(file, newMap, fileName);
        }else{
            txtIO(file, newMap, fileName);
        }
        return newMap;
    }

    @Override
    public void delete(String tableId, String dTableIds, String ids, boolean isLogicalDelete, Map<String, Object> paramMap) {
        String filter    ="('" + ids.replace(",", "','") + "')";
        String sql ="delete from t_sdzyc_dzjgmewm where pid in "+filter;
        System.out.println("SQL:="+sql +"  params:="+filter);
        DatabaseHandlerDao.getInstance().executeSql(sql);
        super.delete(tableId, dTableIds, ids, isLogicalDelete, paramMap);
    }

    public  int xmlDom4j(File file ,Map map ,String fileName){
        SAXReader reader = new SAXReader();
        String zsspm = String.valueOf(map.get("SPDM"));
        //所有数据保存完成后 保存电子监管码导入信息
        String id =  null;
        PreparedStatement psm = null;
        String sql = null;
        try {
            Document doc = reader.read(file);
            //获得跟资源
            Element root = doc.getRootElement();
            //Events资源
            Iterator IEvents = root.elementIterator();
            Element events = (Element) IEvents.next();
            //Event资源
            Iterator IEvent = events.elementIterator();
            Element event = (Element) IEvent.next();
            //Relation资源
            Iterator IRelation = event.elementIterator();
            Element relation = (Element) IRelation.next();
            //Batch资源
            Iterator IBatch = relation.elementIterator();
            Element batch = (Element) IBatch.next();
            //ERP批次号
            map.put("BATCHNO",batch.attributeValue("batchNo"));
            //生产日期
            map.put("MADEDATE",batch.attributeValue("madeDate"));
            //生产线名称
            map.put("LINENAME",batch.attributeValue("lineName"));
            //保存主表数据
            id =  saveOne("T_SDZYC_DZJGMDR", map);
            //循环遍历Batch资源中的code资源
            Iterator ICode = batch.elementIterator();
            conn = JdbcDaoUtil.getInstance().getConnection();
            sql = "insert into t_sdzyc_dzjgmewm(id,curcode,packlayer,parentcode,zsm,url,pid,flag) values(?,?,?,?,?,?,?,'0')";
            conn.setAutoCommit(false);
            psm = conn.prepareStatement(sql);
            int count = 1;
            Long start = new Date().getTime();

            while(ICode.hasNext()){
                Element code = (Element) ICode.next();
                psm.setString(1,UUIDGenerator.uuid());
                psm.setString(2,code.attributeValue("curCode"));
                psm.setString(3,code.attributeValue("packLayer"));
                psm.setString(4,code.attributeValue("parentCode"));
                String packLayer = code.attributeValue("packLayer");
                String zsm = null;
                if("3".equals(packLayer)){//三级码
                    zsm = StatisticalCodeUtil.getInstance().getThirtyFiveZsm("JGG","6",zsspm,"SDZYC","SDZYCDZJGMZSM");
                }else if ("2".equals(packLayer)){//二级码
                    zsm = StatisticalCodeUtil.getInstance().getThirtyFiveZsm("JGG","5",zsspm,"SDZYC","SDZYCDZJGMZSM");
                }else{//一级码
                    zsm = StatisticalCodeUtil.getInstance().getThirtyFiveZsm("JGG","4",zsspm,"SDZYC","SDZYCDZJGMZSM");
                }
                psm.setString(5,zsm);//zsm
                psm.setString(6,"http://www.sdzyczs.com/zsm?"+zsm);//zsmUrl
                psm.setString(7,id);//pid
                code.addAttribute("zsmUrl","http://www.sdzyczs.com/zsm?"+zsm);
                psm.addBatch();
                if(count==10000 ){
                    psm.executeBatch();
                    psm.clearBatch();
                    conn.commit();
                    count = 1 ;
                }else {
                    count++;
                }
            }
            psm.executeBatch();
            psm.clearBatch();
            conn.commit();
            //数据处理完成进行文件上传操作
            File newFile = new File(REAL_PATH+"/"+fileName);
            writerDocumentToNewFile(doc,newFile);// FileUtil.copyFile(file,newFile);
        } catch (Exception e) {
            //执行失败删除导入数据
            if( conn != null) conn.rollback(); conn.close();
            String sql2 = "delete from t_sdzyc_dzjgmdr where id = ?";
            DatabaseHandlerDao.getInstance().executeSql(sql2, new String[]{id});
            map.remove("ID");
            System.out.println("读取xml文件失败");
            e.printStackTrace();
        }finally {
            if (psm != null) {
                try {
                    psm.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return 0;
        }

    }

    /**
     * Document写入新的文件
     * @param document
     * @param newFile
     * @throws Exception
     */
    public void writerDocumentToNewFile(Document document,File newFile)throws Exception{
        //输出格式
        OutputFormat format = OutputFormat.createPrettyPrint();
        //设置编码
        format.setEncoding("UTF-8");
        //XMLWriter 指定输出文件以及格式
        XMLWriter writer = new XMLWriter(new OutputStreamWriter(new FileOutputStream(newFile),"UTF-8"), format);
        //写入新文件
        writer.write(document);
        writer.flush();
        writer.close();
    }
    /**
     * 写入读取的txt中电子监管码数据
     * @param file
     * @param map
     * @param fileName
     * @return
     */
    public int txtIO(File file,Map map ,String fileName) {
        String zsspm = String.valueOf(map.get("SPDM"));
        //所有数据保存完成后 保存电子监管码导入信息
        String id =  null;
        PreparedStatement psm = null;
        String sql = null;
        try {
            String encoding = "GBK";

            int count = 1;
            if (file.isFile() && file.exists()) { //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = bufferedReader.readLine();
                System.out.println(lineTxt);
                //数据处理完成进行文件上传操作 保存到新的文件中
                File newFile = new File(REAL_PATH+"/"+fileName);
                PrintWriter ps=new PrintWriter(new FileOutputStream(newFile));
                //保存主表数据
                id =  saveOne("T_SDZYC_DZJGMDR", map);
                int i =0;
                //不需要处理第一行的数据直接写入txt中
                ps.write(lineTxt+"\n");
                conn = JdbcDaoUtil.getInstance().getConnection();
                sql = "insert into t_sdzyc_dzjgmewm(id,curcode,zsm,url,pid,flag) values(?,?,?,?,?,'0')";
                conn.setAutoCommit(false);
                psm = conn.prepareStatement(sql);
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    String zsm =  StatisticalCodeUtil.getInstance().getThirtyFiveZsm("JGG","4",zsspm,"SDZYC","SDZYCDZJGMZSM");
                    ps.write(lineTxt+",http://zyzs.sdcom.gov.cn:8080/zsm?"+zsm+"\n");
                    psm.setString(1,UUIDGenerator.uuid());
                    psm.setString(2,lineTxt);//zsm
                    psm.setString(3,zsm);//zsm
                    psm.setString(4,"http://zyzs.sdcom.gov.cn:8080/zsm?"+zsm);//zsmUrl
                    psm.setString(5,id);//pid
                    psm.addBatch();
                    if(count==5000 ){
                        psm.executeBatch();
                        psm.clearBatch();
                        conn.commit();
                        count = 1 ;
                    }else {
                        count++;
                    }

                }
                psm.executeBatch();
                psm.clearBatch();
                conn.commit();
                read.close();
                ps.close();
            }
            return 0;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (psm != null) {
                try {
                    psm.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return 0;
        }
    }
}