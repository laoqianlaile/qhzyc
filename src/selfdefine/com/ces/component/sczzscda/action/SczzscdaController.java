package com.ces.component.sczzscda.action;

import com.ces.component.sczzscda.service.SczzscdaService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.utils.JSON;
import com.ces.xarch.core.entity.StringIDEntity;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Map;

public class SczzscdaController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, SczzscdaService, TraceShowModuleDao> {

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
	 * 获取区域编号信息
	 * @return
	 */
    public Object getQybhList() {
	    setReturnData(getService().getQybhList());
	    return SUCCESS;
    }

	/**
	 * 根据区域编号获取地块信息
	 * @return
	 */
	public Object getDkxxByQybh() {
		setReturnData(getService().getDkxxByQybh());
		return SUCCESS;
	}

	/**
	 * 根据地块编号获取种植单元信息
	 * @return
	 */
	public Object getZzdyBydk() {
		String dkbh = getParameter("dkbh");
		setReturnData(getService().getZzdyBydk(dkbh));
		return SUCCESS;
	}

	/**
	 * 修改时根据地块编号获取种植单元信息
	 * @return
	 */
	public Object getZzdyBydkForUpdate() {
		String dkbh = getParameter("dkbh");
		String zzdybh = getParameter("zzdybh");
		setReturnData(getService().getZzdyBydkForUpdate(dkbh, zzdybh));
		return SUCCESS;
	}

	/**
	 * 获取种植方案配置信息
	 * @return
	 */
	public Object getZzfapzxx() {
		setReturnData(getService().getZzfapzxx());
		return SUCCESS;
	}

	/**
	 * 获取投入品肥料信息
	 * @return
	 */
	public Object getTrpflxx() {
		String fllx = getParameter("fllx");
		setReturnData(getService().getTrpflxx(fllx));
		return SUCCESS;
	}

	/**
	 * 获取投入品用药信息
	 * @return
	 */
	public Object getTrpYyxx() {
		setReturnData(getService().getTrpYyxx());
		return SUCCESS;
	}

	/**
	 * 保存生产档案信息
	 * @return
	 */
	public Object saveScdaxx() {
		String scdaxx = getParameter("scdaxx");
		Map<String, Object> data = JSON.fromJSON(scdaxx, new TypeReference<Map<String, Object>>() {});
		setReturnData(getService().saveScdaxx(data));
		return SUCCESS;
	}

	/**
	 * 获取生产档案信息
	 * @return
	 */
	public Object getScdaxxById() {
		String rowId = getParameter("rowId");
		setReturnData(getService().getScdaxxById(rowId));
		return SUCCESS;
	}

	/**
	 * 更新生产档案信息
	 * @return
	 */
	public Object updateScdaxx() {
		String scdaxx = getParameter("scdaxx");
		Map<String, Object> data = JSON.fromJSON(scdaxx, new TypeReference<Map<String, Object>>() {
		});
		setReturnData(getService().updateScdaxx(data));
		return SUCCESS;
	}

	/**
	 * 获取所有的投入品信息
	 * @return
	 */
	public Object getAllTrpxx() {
		setReturnData(getService().getAllTrpxx());
		return SUCCESS;
	}

	/**
	 * 根据投入品通用名获取投入品信息
	 * @return
	 */
	public Object getTrpxxByTym() {
		String lx = getParameter("lx");
		String tym = getParameter("tym");
		setReturnData(getService().getTrpxxByTym(lx, tym));
		return SUCCESS;
	}
	/**
	 * 根据投入品通用名编号获取投入品信息
	 * @return
	 */
	public Object getTrpxxByTymbh() {
		String lx = getParameter("lx");
		String tymbh = getParameter("tymbh");
		setReturnData(getService().getTrpxxByTymbh(lx, tymbh));
		return SUCCESS;
	}
	/**
	 * 根据投入品信息获取用途
	 * @return
	 */
	public Object getYtByTrpmc() {
		String lx = getParameter("lx");
		String trpmc = getParameter("trpmc");
		setReturnData(getService().getYtByTrpmc(lx, trpmc));
		return SUCCESS;
	}
	public Object getTrpByTym() {
		String tym = getParameter("tym");
		setReturnData(getService().getTrpByTym(tym));
		return SUCCESS;
	}

	/**
	 * 根据投入品类型获取投入品信息
	 * @return
	 */
	public Object getTrpxxByTrplx() {
		String lx = getParameter("lx");
		setReturnData(getService().getTrpxxByTrplx(lx));
		return SUCCESS;
	}

	/**
	 * 获取种植地块信息
	 */
	public void getDkxx() {
		setReturnData(getService().getDkxx());
	}
	public void getFzrJD() {
		String dkbh = getParameter("dkbh");
		setReturnData(getService().getFzrdk(dkbh));
	}
	public void getJddk() {
		String jdbh = getParameter("jdbh");
		setReturnData(getService().getJddk(jdbh));
	}
	/**
	 * 获取种子种苗信息
	 */
	public void getZzzm() {
		String zzfabh = getParameter("zzfabh");
		setReturnData(getService().getZzzm(zzfabh));
	}
	/**
	 * 获取种子来源
	 */
	public void getZzly() {
		setReturnData(getService().getZzly("CZLY"));
	}
	/**
	 * 获取药材名称
	 */
	public void getYcmc() {
		String ycmc = getParameter("ycmc");
		setReturnData(getService().getYcmc(ycmc));
	}
	/**
	 * 获取种植负责人信息
	 */
	public void getZzfzr() {
		setReturnData(getService().getZzfzr());
	}

    /**
     * 根据已知姓名获取种植负责人
     */
    public void getZzfzrxxByZzfzr(){
        String zzfzr = getParameter("zzfzr");
        setReturnData(getService().getZzfzrxx(zzfzr));
    }
	/**
	 * 获取基地信息
	 */
	public void getJdxx() {
		setReturnData(getService().getJdxx());
	}



	/**
	 * 获取种子种苗通用名
	 */
	public Object getAllTrptym() {
		setReturnData(getService().getAllTrptym());
		return SUCCESS;
	}

	/**
	 * 根据投入品类型获取投入品的通用名
	 * @return
	 */
	public Object getTrptymByLx() {
		String lx = getParameter("lx");
		setReturnData(getService().getTrptymByLx(lx));
		return SUCCESS;
	}
	/**
	 * 获取农药通用名
	 * @return
	 */
	public Object getNyTym() {
		setReturnData(getService().getNyTym());
		return SUCCESS;
	}
	/**
	 * 获取肥料通用名
	 * @return
	 */
	public Object getFlTym() {
		setReturnData(getService().getFlTym());
		return SUCCESS;
	}
	/**
	 * 获取农机具通用名
	 * @return
	 */
	public Object getGgTym() {
		setReturnData(getService().getGgTym());
		return SUCCESS;
	}
	/**
	 * 获取农药标准名称
	 * @return
	 */
	public Object getNyTrpmc() {
		setReturnData(getService().getNyTrpmc());
		return SUCCESS;
	}
	/**
	 * 获取肥料标准名称
	 * @return
	 */
	public Object getFlTrpmc() {
		setReturnData(getService().getFlTrpmc());
		return SUCCESS;
	}
	/**
	 * 根据通用名获取投入品类型
	 * @return
	 */
	public Object getLxByTym() {
		String bh = getParameter("bh");
		setReturnData(getService().getLxByTym(bh));
		return SUCCESS;
	}
	/**
	 * 获取数据字典编码数据
	 * @return
	 */
	public Object getDataDictionary() {
		String lxbm = getParameter("lxbm");
		setReturnData(getService().getDataDictionary(lxbm));
		return SUCCESS;
	}
	/**
	 * 根据通用名获取投入品类型
	 * @return
	 */
	public Object getNjjLxByTym() {
		String bh = getParameter("bh");
		setReturnData(getService().getNjjLxByTym(bh));
		return SUCCESS;
	}
	/**
	 * 根据通用名获取农药投入品类型
	 * @return
	 */
	public Object getNyLxByTym() {
		String tym = getParameter("tym");
		setReturnData(getService().getNyLxByTym(tym));
		return SUCCESS;
	}
	/**
	 * 根据通用名获取肥料投入品类型
	 * @return
	 */
	public Object getFlLxByTym() {
		String tym = getParameter("tym");
		setReturnData(getService().getFlLxByTym(tym));
		return SUCCESS;
	}
	/**
	 * 查询操作详情
	 * @return
	 */
	public Object getCkxq(){
		String tableName = getParameter("tableName");
		String id = getParameter("id");
		setReturnData(getService().getCkxq(tableName, id));
		return SUCCESS;
	}

	/**
	 * 获取负责人
	 * @return
	 */
	public Object getFzr(){
        setReturnData(getService().getFzr());
		return SUCCESS;
	}

    /**
     * 查询销售去向
     * @return
     */
	public Object getXsqx(){
		String id = getParameter("id");
		setReturnData(getService().getXsqx(id));
		return  SUCCESS;
	}

	public Object canUpdateScda(){
		String id = getParameter("id");
		setReturnData(getService().canUpdateScda(id));
		return SUCCESS;
	}


	public Object preView(){
		String id = getParameter("id");
		String kind = getParameter("kind");
		setReturnData(getService().preView(id,kind));
		return SUCCESS;
	}

	public Object getFapzCombobox(){
		setReturnData(getService().getFapzCombobox());
		return SUCCESS;
	}
	//修改页面获取信息
	public void gettheFormdata(){
		String id=getParameter("id");
		setReturnData(getService().gettheFormdata(id));
	}
	public void queryPch() {
		String ids = getParameter("ids");
		setReturnData(getService().queryPch(ids));
	}

}