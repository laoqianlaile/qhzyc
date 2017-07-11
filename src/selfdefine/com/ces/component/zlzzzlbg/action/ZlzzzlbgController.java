package com.ces.component.zlzzzlbg.action;


import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.component.trace.utils.CompanyInfoUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zlzzzlbg.service.ZlzzzlbgService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class ZlzzzlbgController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZlzzzlbgService, TraceShowModuleDao> {

    private static final long serialVersionUID = 1L;
    
    /*
     * (非 Javadoc)   
     * <p>标题: initModel</p>   
     * <p>描述: </p>      
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new StringIDEntity());
    }

    //取当前登录用户的门店名称
    public Object getCompanyName() {
        String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
        String companyName = CompanyInfoUtil.getInstance().getCompanyName("ZZ",companyCode);
        setReturnData(companyName);
        return SUCCESS;
    }

    public Object getPzxx () {
        setReturnData(getService().getPzxx());
        return SUCCESS;
    }

    //获取安全率数据
    public Object getAql () {
        String startDate = getParameter("startDate");
        String endDate = getParameter("endDate");
        String cpbh = getParameter("cpbh");
        String scqy = getParameter("scqy");
        Map<String,String> param = new HashMap<String, String>();
        param.put("startDate",startDate);
        param.put("endDate",endDate);
        param.put("cpbh",cpbh);
        param.put("scqy",scqy);
        try {
            setReturnData(getService().getAql(param));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }
}