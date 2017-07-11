package com.ces.component.zlyjya.action;

import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zlyjya.service.ZlyjyaService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/18.
 */
public class ZlyjyaController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZlyjyaService, TraceShowModuleDao> {
    private static final long serialVersionUID = 1L;
    /**
     * 获得radiolist值
     */
    public void loadRadio(){
        setReturnData(getService().loadRadio());
    }

    @Override
    protected void initModel() {

        setModel(new StringIDEntity());
    }



    @Override
    protected void setService(ZlyjyaService service) {
        super.setService(service);
    }

    /**
     * 获得列表的值
     */
    public void getColdata(){
        String YABH=getParameter("yabh");
        setReturnData(getService().getColdata(YABH));
    }

    /**
     * 更具查询条件过滤
     */
    public void searchGridInfo(){
        String cpmc=getParameter("CPMC");
        String PCH=getParameter("PCH");
        String CPZSM=getParameter("CPZSM");
        String yalx=getParameter("yalx");
        setReturnData(getService().searchGridInfo(cpmc,PCH,CPZSM,yalx));
    }

    /**
     *获得列表数据
     */
    public void getGriddata(){
        PageRequest pageRequest = this.buildPageRequest();
        String data=getParameter("data");
        String yalx=getParameter("yalx");
        data.split(",");
        String[]  list=data.split(",");
        setReturnData(getService().getGriddata(pageRequest,list,yalx));
        //list.addAll();
    }

    /**
     * 导出选中列表信息
     */
    public void exportExcel(){
        String tableHeader=getParameter("tableHeader");
        String ids=getParameter("ids");
        String yabh=getParameter("yabh");
        String yalx=getParameter("yalx");
        String[] tableHeaderList= tableHeader.split(",");
        String[] idsList=ids.split(",");
        setReturnData(getService().exportExcel(tableHeaderList, idsList,yabh,yalx));

    }

    /**
     * 获取预案类型
     */
    public void getYalx(){
        String yabh=getParameter("yabh");
        setReturnData(getService().getYalx(yabh));
    }

    /**
     * 获取用药信息图表数据
     * @return
     */
    public Object getYyChartData () {
        setReturnData(getService().getYyChartData());
        return SUCCESS;
    }

    /**
     * 获取销售去向图标数据
     * @return
     */
    public Object getXsqxChartData () {
        setReturnData(getService().getXsqxChartData());
        return  SUCCESS;
    }
}