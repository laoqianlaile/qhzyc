package com.ces.config.dhtmlx.action.database;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.database.DatabaseDao;
import com.ces.config.dhtmlx.entity.database.Database;
import com.ces.config.dhtmlx.service.database.DatabaseService;
import com.ces.config.utils.DatabaseUtil;

/**
 * 数据库Controller
 * 
 * @author wanglei
 * @date 2013-07-10
 */
public class DatabaseController extends ConfigDefineServiceDaoController<Database, DatabaseService, DatabaseDao> {

    private static final long serialVersionUID = -7659084919140230484L;

    /*
     * (non-Javadoc)
     * 
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new Database());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * 
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     *      StringIDService)
     */
    @Autowired
    @Qualifier("databaseService")
    @Override
    protected void setService(DatabaseService service) {
        super.setService(service);
    }

    /**
     * 校验数据库字段是否已经存在
     * 
     * @return Object
     */
    public Object validateFields() {
        Database database = (Database) getModel();
        Database temp = getService().getDatabaseByName(database.getName());
        boolean nameExist = false;
        if (null != database.getId() && !"".equals(database.getId())) {
            Database oldDatabase = getService().getByID(database.getId());
            if (null != temp && null != oldDatabase && !temp.getId().equals(oldDatabase.getId())) {
                nameExist = true;
            }
        } else {
            if (null != temp) {
                nameExist = true;
            }
        }
        setReturnData("{'nameExist' : " + nameExist + "}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 数据库连接测试
     * 
     * @return Object
     */
    public Object connDatabase() {
        boolean connSuccess = false;
        Database database = new Database();
        database.setId(getParameter("P_id"));
        database.setName(getParameter("P_name"));
        database.setInstanceName(getParameter("P_instanceName"));
        database.setIp(getParameter("P_ip"));
        database.setPort(Integer.valueOf(getParameter("P_port")));
        database.setType(getParameter("P_type"));
        database.setUserName(getParameter("P_userName"));
        database.setPassword(getParameter("P_password"));
        connSuccess = DatabaseUtil.connDatabase(database);
        setReturnData("{'connSuccess' : " + connSuccess + "}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
}
