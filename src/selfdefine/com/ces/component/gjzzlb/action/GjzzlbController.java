package com.ces.component.gjzzlb.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.gjzzlb.service.GjzzlbService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GjzzlbController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, GjzzlbService, TraceShowModuleDao> {

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

    public void getJdandWd(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String zsm=getParameter("cpzsm");
        String cppch=getParameter("cppch");
        String lowcssj=getParameter("lowcssj");
        String highcssj=getParameter("highcssj");
        String csqy=getParameter("csqy");

        setReturnData(getService().getJdandWd(zsm, cppch, lowcssj, highcssj, csqy));
    }

    public void getJdandWdByIs(){
        String value=getParameter("value");
        setReturnData(getService().getJdandWdByIs(value));

    }

    /**
     * 更改问题标记字段
     */
    public void changeWtbj(){
        String value=getParameter("value");
        String pch=getParameter("pch");
        String bzxs=getParameter("bzxs");
        String cpzsm=getParameter("cpzsm");
        setReturnData(getService().changeWtbj(value, pch, bzxs, cpzsm));

    }

    /**
     * 重新加载列表
     */
    public void reloadGrid(){
        String zsm=getParameter("cpzsm");
        String cppch=getParameter("cppch");
        String lowcssj=getParameter("lowcssj");
        String highcssj=getParameter("highcssj");
        String csqy=getParameter("csqy");
        setReturnData(getService().reloadGrid(zsm, cppch, lowcssj, highcssj, csqy));
    }

    public void reloadGridIs(){
        String value=getParameter("value");
        setReturnData(getService().reloadGridIs(value));
    }

    /**
     * 复写基本检索
     */
    public Object BaseSearch(){
        String zsm=getParameter("zsm");
        String pch=getParameter("pch");
        String qymc=getParameter("qymc");
        String khmc=getParameter("khmc");
        String cpmc=getParameter("cpmc");
        String aftdate=getParameter("aftdate");
        String befdate=getParameter("befdate");
        PageRequest pageRequest = this.buildPageRequest();
        list = getDataModel(getModelTemplate());
        Page<Object> page = this.getService().BaseSearch(pageRequest, zsm, pch, qymc, khmc, cpmc, aftdate, befdate);
        list.setData(page);
        return null;

    }

    public void SearchXsqx(){
        String zsm=getParameter("zsm");
        String pch=getParameter("pch");
        String qymc=getParameter("qymc");
        String khmc=getParameter("khmc");
        String cpmc=getParameter("cpmc");
        String aftdate=getParameter("aftdate");
        String befdate=getParameter("befdate");
        setReturnData(getService().SearchXsqx(zsm, pch, qymc, khmc, cpmc, aftdate, befdate));
    }
    
    public void SearchKhxx(){
        setReturnData(getService().SearchKhxx());

    }

}