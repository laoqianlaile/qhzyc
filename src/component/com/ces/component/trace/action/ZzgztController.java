package com.ces.component.trace.action;

import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.component.trace.dao.ZzgztDao;
import com.ces.component.trace.service.ZzgztService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by harper on 2015/9/15.
 */
public class ZzgztController extends TraceShowModuleDefineServiceDaoController<StringIDEntity,ZzgztService,ZzgztDao> {

    @Override
    protected void initModel() {
        setModel(new StringIDEntity());
    }

    @Override
    @Autowired
    protected void setService(ZzgztService service) {
        super.setService(service);
    }

    public void searchTrp(){
        setReturnData(getService().searchTrp());
    }

    /**
     * 工作台：查询订单信息
     */
    public void searchDdxx(){
        setReturnData(getService().searchDdxx());
    }

    /**
     * 工作台：查询区域信息
     */
    public void searchQyxx(){
        setReturnData(getService().searchQyxx());
    }

    /**
     * 工作台：根据区域编号查询地块信息
     */
    public void searchDkxx(){
        String qybh = getParameter("qybh");
        setReturnData(getService().searchDkxx(qybh));
    }

    /**
     * 工作台：根据地块编号查询物联网作息
     */
    public void searchWlwxx(){
        String dkbh = getParameter("dkbh");
        setReturnData(getService().searchWlwxx(dkbh));
    }


    /**
     * 查看操作规范
     */
    public void searchCzgf(){
        setReturnData(getService().searchCzgf());
    }

    public Object serchFarmingInfo(){
        setReturnData(getService().serchFarmingInfo());
        return null;
    }

}
