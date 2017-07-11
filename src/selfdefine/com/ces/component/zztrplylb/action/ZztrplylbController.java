package com.ces.component.zztrplylb.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zztrplylb.service.ZztrplylbService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class ZztrplylbController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZztrplylbService, TraceShowModuleDao> {

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

    public void searchTrpmc(){

        String trplx = getParameter("trplx");
        setReturnData(getService().searchTrpmc(trplx));

    }

    /**
     * 根据投入品编号获得相关的详细信息
     */
    public void searchTrpxx(){
        String trpbh = getParameter("trpbh");
        setReturnData(getService().searchTrpxx(trpbh));
    }
    /**
     * 保存出库信息
     * @return
     */
    public String saveTrpckxx(){
        String entityJson = getParameter(E_ENTITY_JSON);
        String dEntitiesJson = getParameter(E_D_ENTITIES_JSON);
        String pEntitiesJson = getParameter("E_pEntityJson");
        setReturnData(getService().saveTrplyxx(entityJson, pEntitiesJson, dEntitiesJson));
        return SUCCESS;
    }

    /**
     * 保存出库信息
     * @return
     */
    public String saveTrplyxx_modify(){
        String entityJson = getParameter(E_ENTITY_JSON);
        String dEntitiesJson = getParameter(E_D_ENTITIES_JSON);
        setReturnData(getService().saveTrplyxx_modify(entityJson, dEntitiesJson));
        return SUCCESS;
    }
    public void searchTrpscdaxx(){
        String pid = getParameter("pid");
        setReturnData(getService().searchTrpscdaxx(pid));
    }

    /**
     * 查询指定的投入品出库明细信息
     */
    public void searchTrpmx(){
        String id = getParameter("id");
        setReturnData(getService().searchTrpmx(id));
    }
    /**
     * 获得投入品出库信息
     */
    public void searchTrckxx(){
        String id = getParameter("id");
        setReturnData(getService().searchTrpckxx(id));
    }

    /**
     * 获得图片出库明细信息
     */
    public void searchTrckxxmx(){
        String pid = getParameter("pid");
        setReturnData(getService().searchTrpckxxmx(pid));
    }

    /**
     * 获得区域负责人信息
     */
    public void searchQyfzr(){
        setReturnData(getService().searchQyfzr());
    }



    /**
     * 根据生产档案获得的对应的详细信息
     */
    public void searchScdaObj() {
        String scdabh = getParameter("scdabh");
        setReturnData(getService().searchScdaObj(scdabh));
    }

    /**
     * 根据负责人选择负责的区域信息
     */
    public void searchQyxx(){
        String fzrbh = getParameter("fzrbh");
        setReturnData(getService().searchQyxx(fzrbh));
    }

    /**
     * 根据区域编号查询地块信息
     */
    public void searchDkxx(){
        String qybh = getParameter("qybh");
        setReturnData(getService().searchDkxx(qybh));
    }

    /**
     *根据地块编号查询生产档案信息
     */
    public void searchScda (){
        String dkbh = getParameter("dkbh");
        setReturnData(getService().searchScda(dkbh));
    }
    /**
     * 查询所需投入品学名和通用名
     */
    public void searchSxtrp(){
        String pid = getParameter("pid");
        String tableName = getParameter("nsx");
        setReturnData(getService().searchSxtrp(tableName, pid));
    }

    /**
     * 根据生产档案ID和生产档案类型进行数据加载
     */
    public void searchNsx(){
        String qybh=getParameter("qybh");
        String dkbh=getParameter("dkbh");
        String scdabh=getParameter("scdabh");
        setReturnData(getService().searchNsx(qybh, dkbh, scdabh));


//        String pid = getParameter("pid");
//        String scdalx = getParameter("scdalx");
//        setReturnData(getService().searchNsx(pid, scdalx));
    }

    /**
     * 查询农事项详细
     */
    public void searchNsxObj(){
        String pid = getParameter("pid");
        String scdalx = getParameter("scdalx");
        String nszyxbh = getParameter("nszyxbh");
        setReturnData(getService().searchNsxObj(pid, scdalx, nszyxbh));

    }

    /**
     * 根据农事项编号、生产档案ID获取所需投入品名称和用量
     */
    public void searchTrp(){
        String pid = getParameter("pid");
        String scdalx = getParameter("scdalx");
        String nszyxbh = getParameter("nszyxbh");
        String id = getParameter("id");
        setReturnData(getService().searchTrp(pid, scdalx, nszyxbh, id));
    }

    /**
     * 获得父节点数据
     */
    public void searchTreeParentNode(){
        String pid = getParameter("pid");
        String scdalx = getParameter("scdalx");
        String nszyxbh = getParameter("nszyxbh");
        setReturnData(getService().searchTreeParentNode(pid, scdalx, nszyxbh));
    }
    /**
     * 根据投入品名称加载所需投入品需要的批次信息
     */
    public void searchUpdTrp(){
        String pid = getParameter("pid");
        String scdalx = getParameter("scdalx");
        String nszyxbh = getParameter("nszyxbh");
        String searchTrpmc = getParameter("trpmc");
        setReturnData(getService().searchUpdTrp(pid, scdalx, nszyxbh, searchTrpmc));
    }

    /**
     * 获得农事项投入品信息
     */
    public void searchNsxTrpObj(){
        String pid = getParameter("pid");
        String scdalx = getParameter("scdalx");
        String nszyxbh = getParameter("nszyxbh");
        String searchTrpmc = getParameter("trpmc");
        setReturnData(getService().searchNsxTrpObj(pid, scdalx, nszyxbh, searchTrpmc));

    }


    /**
     * 通过负责人编号获得地块信息
     */
    public void searchDkxxByQyfzr(){
        String qybh = getParameter("qybh");
        setReturnData(getService().searchDkxxByQyfzr(qybh));
    }

    /**
     * 根据负责人获得生产档案信息
     */
    public void searchScdaByQyfzr(){
        String qybh = getParameter("qybh");
        String dkbh=getParameter("dkbh");
        setReturnData(getService().searchScdaByQyfzr(qybh,dkbh));
    }

    /**
     * 根据领用生产档案id删除领用记录中的生产档案数据
     * @return
     */
    public String deleteScdalyxx(){
        String id = getParameter("id");
        getService().deleteScdalyxx(id);
        return SUCCESS;
    }

    /**
     * 根据入库流水号获得投入品采购信息
     */
    public void searchSelectedTrpxx(){
        String rklshs = getParameter("rklshs");
        setReturnData(getService().searchSelectedTrpxx(rklshs));
    }

    /**
     * 加载农事项
     */
    public void searchNsxgrid(){
        String lyr=getParameter("lyr");
        String qssj=getParameter("qssj");
        String jssj=getParameter("jssj");
        setReturnData(getService().searchNsxgrid(lyr, qssj, jssj));

//        PageRequest pageRequest = this.buildPageRequest();
//        list = getDataModel(getModelTemplate());
//        Page<Object> page = this.getService().searchNsxgrid(pageRequest, lyr, qssj, jssj);
//        list.setData(page);
//        return null;
    }

    /**
     *加载供应商名称
     */
    public void getGysData(){
        String trpbh=getParameter("trpbh");
        String scpch=getParameter("scpch");
        setReturnData(getService().getGysData(trpbh, scpch));
    }

    /**
     * 加载生产批次号
     */
    public void getScpchData(){
        String trpbh=getParameter("trpbh");
        String gysbh=getParameter("gysbh");
        setReturnData(getService().getScpchData(trpbh, gysbh));

    }

    public void getXgGysData(){
        setReturnData(getService().getXgGysData());
    }

    public void searchCzrxx(){
        setReturnData(getService().searchCzrxx());
    }

    /**
     * 获得打印数据
     */

    public void getDayinData(){
        String id=getParameter("id");
        setReturnData(getService().getDayinData(id));

    }

    /**
     * 重新加载grid数据
     */
    public void reloadDayiData(){
        String id=getParameter("id");
        PageRequest pageRequest = this.buildPageRequest();
        list = getDataModel(getModelTemplate());
        Page<Object> page = this.getService().reloadDayiData(pageRequest, id);
        list.setData(page);

//        setReturnData(getService().);
    }

    public void getCkslData(){
        String rklsh=getParameter("rklsh");
        setReturnData(getService().getCkslData(rklsh));

    }

    public void tjGridGysData(){
        String qybh=getParameter("qybh");
        String dkbh=getParameter("dkbh");
        String scda=getParameter("scda");
        String nsczx=getParameter("nsczxbh");
        String nsczxmc=getParameter("nxczxmc");
        String ids=getParameter("ids");
        setReturnData(getService().tjGridGysData(qybh,dkbh,scda,nsczx,ids,nsczxmc));

    }
}
