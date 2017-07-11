package com.ces.config.dhtmlx.service.appmanage;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.dao.appmanage.ReportTableRelationDao;
import com.ces.config.dhtmlx.entity.appmanage.ReportTableRelation;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.StringUtil;
import com.google.common.collect.Lists;

@Component
public class ReportTableRelationService extends ConfigDefineDaoService<ReportTableRelation, ReportTableRelationDao> {
    /*
     * (非 Javadoc)
     * <p>标题: bindingDao</p>
     * <p>描述: 注入自定义持久层(Dao)</p>
     * @param entityClass
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("reportTableRelationDao")
    @Override
    protected void setDaoUnBinding(ReportTableRelationDao dao) {
        super.setDaoUnBinding(dao);
    }

    /**
     * <p>标题: getDefineTableRelation</p>
     * <p>描述: </p>
     * 
     * @param reportId
     * @return Object 返回类型
     * @throws
     */
    public Object getDefineTableRelation(String reportId) {
        List<Object[]> list = Lists.newArrayList();
        // [0-]
        List<Object[]> defines = getDao().findByReportId(reportId);
        for (Object[] define : defines) {
            Object[] objs = new Object[5];
            // id
            objs[0] = define[0] + ";" + define[1] + ";" + define[2] + ";" + define[3];
            // origin table show name
            objs[1] = define[4] + " (" + define[5] + ")";
            // origin column show name
            objs[2] = define[6] + " (" + define[7] + ")";
            // relate table show name
            objs[3] = define[8] + " (" + define[9] + ")";
            // relate column show name
            objs[4] = define[10] + " (" + define[11] + ")";
            // add list
            list.add(objs);
            //
        }
        defines = null;
        return list;
    }

    /**
     * 根据报表ID获取该报表下表关系
     * 
     * @param reportId 报表ID
     * @return List<ReportTableRelation>
     */
    public List<ReportTableRelation> getByReportId(String reportId) {
        return getDao().getByReportId(reportId);
    }

    /**
     * <p>标题: save</p>
     * <p>描述: TODO(这里用一句话描述这个方法的作用)</p>
     * 
     * @param reportId
     * @param rowsValue 设定参数
     * @return void 返回类型
     * @throws
     */
    @Transactional
    public void save(String reportId, String rowsValue) {
        Assert.state(StringUtil.isNotEmpty(reportId), "保存时，reportId不能为空！");
        // delete old configuration
        getDao().deleteByReportId(reportId);
        // save current configuration
        List<ReportTableRelation> list = Lists.newArrayList();
        if (StringUtil.isNotEmpty(rowsValue)) {
        	String[] rowsArr = rowsValue.split(";");
        	for (int i = 0; i < rowsArr.length; i++) {
        		ReportTableRelation rds = new ReportTableRelation();
        		rds.setOneRowValue(reportId, rowsArr[i]);
        		rds.setShowOrder(Short.parseShort(String.valueOf(i)));
        		list.add(rds);
        	}
        	getDao().save(list);
        }
    }

    /**
     * qiucs 2013-8-12
     * <p>标题: check</p>
     * <p>描述: </p>
     * 
     * @param reportId
     * @return Object 返回类型
     * @throws
     */
    public MessageModel check(String reportId) {
        Assert.state(StringUtil.isNotEmpty(reportId), "保存时，reportId不能为空！");
        Object ttl = getDao().countReportTables(reportId);
        // 只有报表数据源表多于1张时，才需要设置表关系
        if (null != ttl && Integer.parseInt(StringUtil.null2zero(ttl)) > 1) {
            List<String> tables = getDao().findReportTablesNotInRelation(reportId);
            if (CollectionUtils.isNotEmpty(tables)) {
            	return MessageModel.falseInstance(tables.toString());
            }
            return MessageModel.trueInstance("数据源表关系已经配置好了！");
        }

        return MessageModel.trueInstance("只有一张表数据源，无配置表关系！");
    }

    /**
     * qiucs 2013-8-12
     * <p>标题: findTableRelation</p>
     * <p>描述: </p>
     * 
     * @param reportId
     * @return int 返回类型
     *         -1： 只有一张数据源表，不需要加载表关系
     *         0：没有找到配置好的表关系或表关系已配置好
     *         1：表关系加载成功
     * @throws
     */
    @Transactional
    public MessageModel findTableRelation(String reportId) {
        System.out.println(reportId);
        List<ReportTableRelation> list = Lists.newArrayList();
        Object ttl = getDao().countReportTables(reportId);
        if (null == ttl || Integer.parseInt(ttl.toString()) == 0) {
            return MessageModel.trueInstance(0, "只有一张数据源表，不需配置表关系！");
        }
        List<Object[]> relations = getDao().findTableRelation(reportId);
        if (null != relations && !relations.isEmpty()) {
            short showOrder = 1;
            for (Object[] obj : relations) {
                ReportTableRelation relation = new ReportTableRelation();
                relation.setReportId(reportId);
                relation.setTableId(StringUtil.null2empty(obj[0]));
                relation.setColumnId(StringUtil.null2empty(obj[1]));
                relation.setRelateTableId(StringUtil.null2empty(obj[2]));
                relation.setRelateColumnId(StringUtil.null2empty(obj[3]));
                relation.setShowOrder(showOrder);
                list.add(relation);
                showOrder += 1;
            }
            relations = null;
        } else {
        	Long cnt = count("EQ_reportId=" + reportId);
        	if (cnt > 0) return MessageModel.trueInstance(0, "表关系已经配置好了！");
        	else return MessageModel.trueInstance(0, "在表关系中没有找到该数据源对应的表关系！");
        }
        getDao().save(list);
        list = null;
        return MessageModel.trueInstance(1, "表关系自动匹配成功！");
    }

}
