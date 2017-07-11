package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ces.config.dhtmlx.entity.appmanage.TimingEntity;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

/**
 * 定时任务dao层
 * 
 * @author wang
 */
public interface TimingDao extends StringIDDao<TimingEntity> {

    /**
     * 定时任务详情列表
     * 
     * @return List<Object[]>
     */
    @Query(value = "select t.id,t.name,t.time,t.is_operates,t.type from t_xtpz_timing t order by id", nativeQuery = true)
    public List<Object[]> getTimingTasks();

    /**
     * 获取启动的定时任务
     * 
     * @return List<TimingEntity>
     */
    @Query("from TimingEntity t where t.isOperates = '1'")
    public List<TimingEntity> getTimingIsOperates();

    /**
     * 获取任务 cmd命令
     * 
     * @return String
     */
    @Query(value = "select t.command from t_xtpz_timing t where t.id=?1", nativeQuery = true)
    public String getCommandById(String Id);

    /**
     * 修改操作状态
     * 
     * @param operates
     * @param Id
     */
    @Transactional
    @Modifying
    @Query(value = "update t_xtpz_timing set is_operates =?1  where id=?2", nativeQuery = true)
    public void updTimingOperates(String status, String Id);

}
