package com.ces.component.acxmlsjdr.service;

import com.ces.component.trace.utils.DataTypeConvertUtil;
import com.ces.component.trace.utils.JdbcDaoUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.utils.StatisticalCodeUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.UUIDGenerator;
import org.apache.struts2.ServletActionContext;
import org.aspectj.util.FileUtil;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Component;

import com.ces.component.acxmlsjdr.dao.AcxmlsjdrDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Component
public class AcxmlsjdrService extends TraceShowModuleDefineDaoService<StringIDEntity, AcxmlsjdrDao> {
    private static Connection conn;
    private final String REAL_PATH = ServletActionContext.getServletContext().getRealPath("/spzstpfj");

    public Map<String, String> saveData(File file, String fileName, String fileContentType, Map map) {
        Map<String, String> newMap = DataTypeConvertUtil.getInstance().mapObj2mapStrFile(map);
        newMap.put("WJMC", fileName);
        newMap.put("SCSJ", DataTypeConvertUtil.getInstance().DataConvert("yyyy-MM-dd HH:mm:ss", new Date()));
        newMap.put("QYBM", SerialNumberUtil.getInstance().getCompanyCode());
        newMap.put("ZT", "1");
        xmlDom4j(file, newMap, fileName);
        return newMap;
    }

    public  Map<String,Object> searchycmcComboGridData(){
        String sql = "select T.SPMC,T.BATCHNO,T.LINENAME,T.MADEDATE from T_SDZYC_ACXML T where ZT='1' " + defaultCode();
        return getGridTypeData(DatabaseHandlerDao.getInstance().queryForMaps(sql));
    }

    public Map<String,Object> getGridTypeData(List<Map<String,Object>> list){
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("data",list);
        return  dataMap;
    }

    public  String defaultCode(){
        String code= SerialNumberUtil.getInstance().getCompanyCode();
        String  defaultCode=" ";
        if(code!=null && !"".equals(code) )
            defaultCode= AppDefineUtil.RELATION_AND+" QYBM= '"+code+"' "+ AppDefineUtil.RELATION_AND+" is_delete <> '1'";
        return defaultCode;
    }

    public int xmlDom4j(File file, Map map, String fileName) {
        SAXReader reader = new SAXReader();
        //处理电子监管码数据，校验数据的有效性，有效在更改状态为1 ，否则不改变状态
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
            map.put("BATCHNO", batch.attributeValue("batchNo"));
            //生产日期
            map.put("MADEDATE", batch.attributeValue("madeDate"));
            //生产线名称
            map.put("LINENAME", batch.attributeValue("lineName"));
            //保存主表数据
            String id = saveOne("T_SDZYC_ACXML", map);
            //循环遍历Batch资源中的code资源
            Iterator ICode = batch.elementIterator();
            conn = JdbcDaoUtil.getInstance().getConnection();
            sql = "update t_sdzyc_dzjgmewm set packlayer=?,parentcode=?,PID=?,flag=1 where curCode=?";
            conn.setAutoCommit(false);
            psm = conn.prepareStatement(sql);
            int count = 1;
            while (ICode.hasNext()) {
                Element code = (Element) ICode.next();
                psm.setString(1, code.attributeValue("packLayer"));
                psm.setString(2, code.attributeValue("parentCode"));
                psm.setString(4, id);
                psm.setString(3, code.attributeValue("curCode"));
                psm.addBatch();
                if (count == 10001) {
                    psm.executeBatch();
                    psm.clearBatch();
                    conn.commit();
                    count = 1;
                } else {
                    count++;
                }
            }
            psm.executeBatch();
            psm.clearBatch();
            conn.commit();
            //数据处理完成进行文件上传操作
            File newFile = new File(REAL_PATH + "/" + fileName);
            FileUtil.copyFile(file, newFile);
        } catch (Exception e) {
            //执行失败删除导入数据
            e.printStackTrace();
        } finally {
            try {
                if (psm != null) {
                    psm.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        }

    }
}
