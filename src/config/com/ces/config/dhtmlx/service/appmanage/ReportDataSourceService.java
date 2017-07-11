package com.ces.config.dhtmlx.service.appmanage;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.ces.config.dhtmlx.dao.appmanage.ColumnDefineDao;
import com.ces.config.dhtmlx.dao.appmanage.ReportColumnDao;
import com.ces.config.dhtmlx.dao.appmanage.ReportDataSourceDao;
import com.ces.config.dhtmlx.entity.appmanage.ColumnDefine;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalTableDefine;
import com.ces.config.dhtmlx.entity.appmanage.ReportColumn;
import com.ces.config.dhtmlx.entity.appmanage.ReportDataSource;
import com.ces.config.dhtmlx.entity.appmanage.ReportTable;
import com.ces.config.dhtmlx.json.entity.common.DhtmlxComboOption;
import com.ces.config.dhtmlx.json.entity.common.DhtmlxTreeNode;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.ReportUtil;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.entity.StringIDEntity;
import com.google.common.collect.Lists;

@Component
public class ReportDataSourceService extends ConfigDefineDaoService<StringIDEntity, ReportDataSourceDao> {
    /*
     * (非 Javadoc)   
     * <p>标题: bindingDao</p>   
     * <p>描述: 注入自定义持久层(Dao)</p>   
     * @param entityClass   
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("reportDataSourceDao")
    @Override
    protected void setDaoUnBinding(ReportDataSourceDao dao) {
        super.setDaoUnBinding(dao);
    }
    
    /**
     * <p>标题: getAllTable</p>
     * <p>描述: TODO(这里用一句话描述这个方法的作用)</p>
     * @return Object    返回类型   
     * @throws
     */
    public Object getTables() {
        List<Object[]> list = Lists.newArrayList(); 
        List<PhysicalTableDefine> tables = getService(PhysicalTableDefineService.class).getTableList(true);
        for (PhysicalTableDefine table : tables) {
            Object[] obj = new Object[3];
            obj[0] = table.getId();  // id
            //obj[1] = table.getText() + " (" + table.getRealTableName() + ")"; // show name
            obj[1] = table.getShowName() + " (" + table.getTableName() + ")";   // show name
            //obj[2] = table.getRealTableName();
            //obj[2] = "U";
            //obj[4] = table.getText();
            list.add(obj);
        }
        tables = null;
        return list;
    }

    /**
     * qiucs 2013-8-7 
     * <p>标题: getColumnsOfTable</p>
     * <p>描述: 获取表中的字段</p>
     * @param  @param tableId
     * @param  @return    设定参数   
     * @return Object    返回类型   
     * @throws
     */
    public Object getColumnsOfTable(String tableId) {
        List<Object[]> list = Lists.newArrayList(); 
        List<ColumnDefine> cols = getDao(ColumnDefineDao.class, ColumnDefine.class).findByTableId(tableId);
        for (ColumnDefine col : cols) {
            Object[] obj = new Object[3];
            obj[0] = col.getId();
            obj[1] = col.getShowName() + " (" + col.getColumnName() + ")";
            list.add(obj);
        }
        cols = null;
        return list;
    }
    
    /**
     * <p>标题: getDefineTableAndColumn</p>
     * <p>描述: </p>
     * @param  reportId
     * @return Object    返回类型   
     * @throws
     */
    public Object getDefineTableAndColumn(String reportId) {
        List<Object[]> list    = Lists.newArrayList();
        List<Object[]> defines = getDao().findByReportId(reportId);
        String preTableId = null, curTableId = null;
        //[0-tableId,1-columnId,2-tableComment,3-tableName,4-columnComment,5-columnName]
        for (Object[] define : defines) {
            Object[] objs = new Object[5];
            // id
            objs[0] = define[0] + ";" + define[1];
            // userdata[tableComment (tableName)]
            objs[3] = define[2] + " (" + define[3] + ")";
            // table show name 
            curTableId = StringUtil.null2empty(define[0]);
            if (null == preTableId || !preTableId.equals(curTableId)) {
                objs[1] = objs[3];
            } else {
                objs[1] = "";
            }
            // column show name
            objs[2] = define[4] + " (" + define[5] + ")";
            // add list
            list.add(objs);
            // reset preTableId
            preTableId = curTableId;
        }
        defines = null;
        return list;
    }
    
    /**
     * 根据报表ID获取ReportDataSource
     * 
     * @param reportId 报表ID
     * @return List<ReportDataSource>
     */
    public List<ReportDataSource> getByReportId(String reportId) {
        return getDao().getByReportId(reportId);
    }
    
    /**
     * <p>标题: save</p>
     * <p>描述: 报表数据源配置保存</p>
     * @param  reportId
     * @param  rowsValue    设定参数   
     * @return void    返回类型   
     * @throws
     */
    @Transactional
    public void save(String reportId, String tabRowsValue, String colRowsValue) {
        Assert.state(StringUtil.isNotEmpty(reportId), "保存时，reportId不能为空！");   
        //System.out.println("table rows value = " + tabRowsValue); 
        //System.out.println("column rows value = " + colRowsValue);
        String[] rowsArr = null;
        // delete old configuration
        getDao().deleteReportColumnByReportId(reportId);
        getDao().deleteReportTableByReportId(reportId);
        // save report table configuration
        List<ReportTable> tabList = Lists.newArrayList();        
        tabRowsValue = tabRowsValue.replace(" (", ",").replace(")", "");
        rowsArr = tabRowsValue.split(";");
        for (int i = 0; i < rowsArr.length; i++) {
            ReportTable rTab = new ReportTable();
            rTab.setShowOrder(Integer.parseInt(String.valueOf(i)));
            rTab.setOneRowValue(reportId, rowsArr[i]);
            tabList.add(rTab);
        }
        getDao().save(tabList);
        // save report column configuration
        List<ReportColumn> colList = Lists.newArrayList();
        colRowsValue = colRowsValue.replace(" (", ",").replace(")", "");
        rowsArr = colRowsValue.split(";");
        for (int i = 0; i < rowsArr.length; i++) {
            ReportColumn rCol = new ReportColumn();
            rCol.setShowOrder(Integer.parseInt(String.valueOf(i)));
            rCol.setOneRowValue(reportId, rowsArr[i]);
            colList.add(rCol);
        }
        getDao().save(colList);
    }
    
    /**
     * <p>标题: getReportTables</p>
     * <p>描述: 表下拉框数据</p>
     * @param  reportId
     * @return Object    返回类型   
     * @throws
     */
    public Object getReportTables(String reportId) {
        /*List<Object[]> list = Lists.newArrayList();
        List<Object[]> tabs = getDao().getReportTables(reportId);
        for (Object[] tab : tabs) {
            Object[] obj = new Object[2];
            obj[0] = tab[0];
            obj[1] = tab[1] + " (" + tab[2] + ")";
            list.add(obj);
        }//*/
        List<DhtmlxComboOption> opts = Lists.newArrayList();
        List<Object[]> tabs = getDao().getReportTables(reportId);
        for (Object[] tab : tabs) {
            DhtmlxComboOption opt = new DhtmlxComboOption();
            opt.setValue(StringUtil.null2empty(tab[0]));
            opt.setText(tab[1] + " (" + tab[2] + ")");
            opts.add(opt);
        }
        return opts;
    }
    
    /**
     * <p>标题: getReportTreeTables</p>
     * <p>描述: </p>
     * @param  reportId
     * @return Object    返回类型   
     * @throws
     */
    public Object getReportTreeTables(String reportId) {
        List<Object[]> datas = getDao().getReportTables(reportId);
        List<DhtmlxTreeNode> list = Lists.newArrayList();
        for (Object[] data : datas) {
            DhtmlxTreeNode tree = new DhtmlxTreeNode();
            tree.setId(String.valueOf(data[0]));
            tree.setText(String.valueOf(data[1]));
            tree.setIm0(ConstantVar.IconTreeNode.FOLDER_CLOSE);
            tree.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
            tree.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
            tree.setType("T"); // 表节点
            tree.setChild("1");
            list.add(tree);
        }
        return list;
    }
    
    /**
     * <p>标题: getReportTreeColumns</p>
     * <p>描述: </p>
     * @param  reportId
     * @param  tableId
     * @return Object    返回类型   
     * @throws
     */
    public Object getReportTreeColumns(String reportId, String tableId) {
        List<Object[]> datas = getDao().getReportTableColunms(reportId, tableId);
        List<DhtmlxTreeNode> list = Lists.newArrayList();
        DhtmlxTreeNode tree = new DhtmlxTreeNode();
        tree.setId(ReportUtil.RN_COLUMN);
        tree.setText("序号");
        tree.setIm0(ConstantVar.IconTreeNode.LEAF);
        tree.setIm1(ConstantVar.IconTreeNode.LEAF);
        tree.setIm2(ConstantVar.IconTreeNode.LEAF);
        tree.setType("C"); // 字段节点
        tree.setChild("0");
        list.add(tree);
        
        for (Object[] data : datas) {
            tree = new DhtmlxTreeNode();
            tree.setId(String.valueOf(data[0]));
            tree.setText(String.valueOf(data[1]));
            tree.setIm0(ConstantVar.IconTreeNode.LEAF);
            tree.setIm1(ConstantVar.IconTreeNode.LEAF);
            tree.setIm2(ConstantVar.IconTreeNode.LEAF);
            tree.setType("C"); // 字段节点
            tree.setChild("0");
            list.add(tree);
        }
        return list;
    }
    
    
    
    /**
     * qiucs 2013-10-16 
     * <p>描述: </p>
     * @param  tableId
     * @return List<Object[]>    返回类型   
     * @throws
     */
    public Object getReportByTableId(String tableId, String moduleId) {
        Object rlt = getDao().getReportByTableId(tableId, moduleId);
        if (null == rlt) return (new ArrayList<Object[]>());
        return rlt;
    }

    /**
     * qiucs 2013-10-16 
     * <p>描述: </p>
     * @param  reportId
     * @return Object    返回类型   
     * @throws
     */
    public Object getColumnsOfReport(String reportId) {
        List<DhtmlxComboOption> list = Lists.newArrayList();
        List<Object[]> cols = getDao(ReportColumnDao.class, ReportColumn.class).findColumnComboByReportId(reportId);
        if (null != cols && !cols.isEmpty()) {
            for (Object[] obj : cols) {
                DhtmlxComboOption opt = new DhtmlxComboOption();
                opt.setValue(String.valueOf(obj[0]));
                opt.setText(obj[2] + "(" + obj[1] + ")");
                list.add(opt);
            }
        }
        return list;
    }
    
}
