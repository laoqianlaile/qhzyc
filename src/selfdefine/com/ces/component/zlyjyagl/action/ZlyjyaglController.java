package com.ces.component.zlyjyagl.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zlyjyagl.service.ZlyjyaglService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;

public class ZlyjyaglController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZlyjyaglService, TraceShowModuleDao> {

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

    /**
     * 预案管理删除
     * @return
     */
    public Object deleteYjya () {
        //获取页面ID String类型
        String yjyaId = getParameter("yjyaId");
        //用逗号分割，取到id
        String[] yjyaId_one = yjyaId.split(",");
        for(int i=0;i<yjyaId_one.length;i++){
            getService().deleteYjya(yjyaId_one[i]);
        }
        return SUCCESS;
    }
}