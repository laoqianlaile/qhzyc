package com.ces.component.qyptmdgl.action;


import com.ces.component.qyptmdgl.dao.QyptmdglDao;
import com.ces.component.qyptmdgl.service.QyptmdglService;
import com.ces.component.trace.utils.JSON;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import org.codehaus.jettison.json.JSONArray;

/**
 * Created by Administrator on 2015/7/10.
 */
public class QyptmdglController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, QyptmdglService, QyptmdglDao> {
    private static final long serialVersionUID = 1L;

    @Override
    protected void initModel() {
        setModel(new StringIDEntity());
    }

    public void getQyxx(){
        String auth_parent_id = getParameter("auth_parent_id");
        setReturnData(getService().getQyxx());

    }
    public void getMdxx(){
        String auth_parent_id = getParameter("auth_parent_id");
        setReturnData(getService().getMdxx(auth_parent_id));
    }
    public void saveQyht(){
        String YYZZ=getParameter("YYZZ");
        String LXR=getParameter("LXR");
        String YX=getParameter("YX");
        String SJ=getParameter("SJ");
        String ZJ=getParameter("ZJ");
        String CZ=getParameter("CZ");
        String SXSJ=getParameter("SXSJ");
        String DZ=getParameter("DZ");


        String JD=getParameter("JD");
        String WD=getParameter("WD");
        String CSXX=getParameter("CSXX");
        String CSMC=getParameter("CSMC");
        String XTLX=getParameter("XTLX");
        String auth_parent_id=getParameter("auth_parent_id");
        String auth_id=getParameter("auth_id");
        int num=getService().saveQyht(YYZZ,LXR,YX,SJ,ZJ,CZ,SXSJ,DZ,JD,WD,CSXX,CSMC,XTLX,auth_parent_id,auth_id);
        setReturnData(num);
    }
    public void initForm(){
        String auth_parent_id=getParameter("auth_parent_id");
        String auth_id=getParameter("auth_id");
        setReturnData(getService().initForm(auth_parent_id,auth_id));

    }

    public void getXtlxGrid(){
        String sysName = getParameter("sysName");
        setReturnData(getService().getXtlxGrid(sysName));

    }
}
