package com.ces.config.dhtmlx.service.appmanage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.ces.config.dhtmlx.dao.appmanage.ReportColumnDao;
import com.ces.config.dhtmlx.dao.appmanage.ReportDefineDao;
import com.ces.config.dhtmlx.entity.appmanage.ReportColumn;
import com.ces.config.dhtmlx.entity.appmanage.ReportDefine;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.JsonUtil;
import com.ces.config.utils.ReportUtil;
import com.ces.config.utils.StringUtil;

@Component
public class ReportDefineService extends ConfigDefineDaoService<ReportDefine, ReportDefineDao> {
    /*
     * (非 Javadoc)   
     * <p>标题: bindingDao</p>   
     * <p>描述: 注入自定义持久层(Dao)</p>   
     * @param entityClass   
     * @see com.ces.xarch.core.service.AbstractService#bindingDao(java.lang.Class)
     */
    @Autowired
    @Qualifier("reportDefineDao")
    @Override
    protected void setDaoUnBinding(ReportDefineDao dao) {
        super.setDaoUnBinding(dao);
    }
    
    /*
     * qiucs 2015-8-4 下午4:15:38
     * (non-Javadoc)
     * @see com.ces.xarch.core.service.AbstractService#save(com.ces.xarch.core.entity.BaseEntity)
     */
    @Override
    @Transactional
	public ReportDefine save(ReportDefine entity) {
		if (StringUtil.isEmpty(entity.getId())) {
			ReportDefine oldEntity = getByReportId(entity.getReportId());
			if (StringUtil.isNotEmpty(oldEntity.getId())) {
				entity.setId(oldEntity.getId());
			}
		}
		return super.save(entity);
	}

	/**
     * 上传CLL文件
     * 
     * @param fileName
     * @throws IOException 
     */
    public void cllUpload(File cllFile, InputStream is) throws IOException  {
        BufferedOutputStream writer = null;
        BufferedInputStream  reader = null;
        try {
            writer = new BufferedOutputStream((new FileOutputStream(cllFile)));
            reader = new BufferedInputStream(is);
            byte[] buffer = new byte[512];
            int d = reader.read(buffer);

            while (d != -1) {
                writer.write(buffer, 0, buffer.length);
                d = reader.read(buffer);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                reader.close();
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }
    }

    /**
     * qiucs 2013-8-14 
     * <p>标题: save</p>
     * <p>描述: 保存CELL报表中的字段位置</p>
     * @param  reportId
     * @param  fields    设定参数   
     * @return void    返回类型   
     * @throws
     */
    @Transactional
    public void save(String reportId, String fields) {
        // reset cell column
        getDao(ReportColumnDao.class, ReportColumn.class).resetCellColumn(reportId);        
        if (StringUtil.isEmpty(fields)) return;
        //
        String[] fieldArr = fields.split(";");
        boolean hasRn = false;
        for (String one : fieldArr) {
            String[] f = one.split(",");
            if (ReportUtil.RN_COLUMN.equals(f[0])) {
                updateRnCell(reportId, Integer.parseInt(f[1]), Integer.parseInt(f[2]));
                hasRn = true;
            } else {
                getDao(ReportColumnDao.class, ReportColumn.class).updateCellColumn(reportId, f[0], Integer.parseInt(f[1]), Integer.parseInt(f[2]));
            }
        }
        // 重置序号位置
        if (!hasRn) updateRnCell(reportId, null, null);
    }
    
    /**
     * qiucs 2015-8-4 下午3:48:43
     * <p>描述: 保存报表设置（包含页面、条件、排序、分组） </p>
     * @return void
     */
    @Transactional
    public void save(String reportId, String fields, String pageSetting, String printSetting1, String printSetting2, String printSetting3) {
    	// 页面
    	if (StringUtil.isNotEmpty(pageSetting)) {
    		ReportDefine entity = JsonUtil.toBean(pageSetting, ReportDefine.class);
    		save(entity);
    	}
    	// 条件、排序、分组
    	if (StringUtil.isNotEmpty(printSetting1)) {
    		getService(ReportPrintSettingService.class).save(reportId, "1", printSetting1);
    	}
    	if (StringUtil.isNotEmpty(printSetting2)) {
    		getService(ReportPrintSettingService.class).save(reportId, "2", printSetting2);
    	}
    	if (StringUtil.isNotEmpty(printSetting3)) {
    		getService(ReportPrintSettingService.class).save(reportId, "3", printSetting3);
    	}
    	// 字段绑定
    	save(reportId, fields);
    }
    
    /**
     * qiucs 2014-10-14 
     * <p>描述: </p>
     * @param  reportId    设定参数   
     * @return void    返回类型   
     * @throws
     */
    @Transactional
    protected void updateRnCell(String reportId, Integer rowIndex, Integer colIndex) {
        ReportDefine entity = getByReportId(reportId);
        if (null == entity) {
            entity = new ReportDefine();
            entity.setReportId(reportId);
        }
        entity.setRnRowIndex(rowIndex);
        entity.setRnColIndex(colIndex);
        save(entity);
    }
    
    /**
     * qiucs 2013-8-14 
     * <p>标题: getByReportId</p>
     * <p>描述: 根据报表ID页面配置</p>
     * @param  @param reportId
     * @return ReportDefine    返回类型   
     * @throws
     */
    public ReportDefine getByReportId(String reportId) {
        ReportDefine entity = getDao().findByReportId(reportId);
        if (null == entity) {
            entity = new ReportDefine();
            entity.setReportId(reportId);
        }
        return entity;
    }

    /**
     * qiucs 2013-8-14 
     * <p>标题: getBindedColumns</p>
     * <p>描述: 根据报表ID获取字段绑定信息</p>
     * @param  @param reportId
     * @return Object    返回类型   
     * @throws
     */
    public Object getBindedColumns(String reportId) {
        List<ReportColumn> list = getDao(ReportColumnDao.class, ReportColumn.class).findBindedColumns(reportId);
        ReportColumn cell = getRnCell(reportId);
        if (null != cell) list.add(cell);
        return list;
    }
    
    /**
     * qiucs 2014-10-14 
     * <p>描述: 序号列</p>
     * @throws
     */
    private ReportColumn getRnCell(String reportId) {
        ReportDefine entity = getByReportId(reportId);
        if (null != entity && null != entity.getRnColIndex() && null != entity.getRnRowIndex()) {
            ReportColumn cell = new ReportColumn();
            cell.setColumnId(ReportUtil.RN_COLUMN);
            cell.setColumnComment("序号");
            cell.setRowIndex(entity.getRnRowIndex());
            cell.setColIndex(entity.getRnColIndex());
            return cell;
        }
        return null;
    }
    
}
