/**
 * <p>Copyright:Copyright(c) 2013</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * <p>包名:com.ces.config.dhtmlx.dao.appmanage</p>
 * <p>文件名:LoggerDao.java</p>
 * <p>类更新历史信息</p>
 * @todo Administrator 创建于 2013-11-11 15:53:30
 */
package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.ces.config.dhtmlx.entity.appmanage.LoggerEntity;
import com.ces.xarch.core.persistence.jpa.StringIDDao;
/**
 * .
 * <p>描述:日志查看DAO管理类</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * @author Administrator
 * @date 2013-11-11  15:53:30
 * @version 1.0.2013.1111
 */
public interface LoggerDao extends StringIDDao<LoggerEntity>{
    /**
     * 查询不重复操作名
     * @return
     * @author Administrator
     * @date 2013-11-20  16:31:22
     */
    @Query(value="select DISTINCT t.action from t_xarch_business_logs t",nativeQuery=true)
    public List<String> findActions();
}
