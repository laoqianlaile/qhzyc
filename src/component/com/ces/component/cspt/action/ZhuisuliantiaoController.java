package com.ces.component.cspt.action;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.ces.component.cspt.dao.ZhuisuliantiaoDao;
import com.ces.component.cspt.entity.TCsptZsxxEntity;
import com.ces.component.cspt.service.ZhuisuliantiaoSerivce;
import com.ces.xarch.core.web.struts2.StringIDDefineServiceDaoController;
@Results({
	@Result(name="ZXZhuiSuliantiao" , location="/cfg-resource/coral40/views/cspt/zhuisuliantiao/zhuisuliuchengTest.jsp"),
	@Result(name="FXZhuiSuliantiao" , location="/cfg-resource/coral40/views/cspt/zhuisuliantiao/fxZhuisuliuchengTest.jsp"),
	@Result(name="zhongzhixinxi" , location="/cfg-resource/coral40/views/cspt/zhuisuliantiao/zhongzhixinxi.jsp"),
	@Result(name="yangzhixinxi" , location="/cfg-resource/coral40/views/cspt/zhuisuliantiao/yangzhixinxi.jsp"),
	@Result(name="shucaipifaxinxi" , location="/cfg-resource/coral40/views/cspt/zhuisuliantiao/shucaipifaxinxi.jsp"),
	@Result(name="tuzaixinxi" , location="/cfg-resource/coral40/views/cspt/zhuisuliantiao/tuzaixinxi.jsp"),
	@Result(name="zhuroupifaxinxi" , location="/cfg-resource/coral40/views/cspt/zhuisuliantiao/zhuroupifaxinxi.jsp"),
	@Result(name="tuanticaigouxinxi" , location="/cfg-resource/coral40/views/cspt/zhuisuliantiao/tuanticaigouxinxi.jsp"),
	@Result(name="chaoshixinxi" , location="/cfg-resource/coral40/views/cspt/zhuisuliantiao/chaoshixinxi.jsp"),
	@Result(name="lingshoushichangxinxi" , location="/cfg-resource/coral40/views/cspt/zhuisuliantiao/lingshoushichangxinxi.jsp")
})
public class ZhuisuliantiaoController extends StringIDDefineServiceDaoController<TCsptZsxxEntity, ZhuisuliantiaoSerivce, ZhuisuliantiaoDao> {
	private String jhpch;			//进货批次号
	private String jypzh;			//交易凭证号
	private String zsm;				//追溯码
	private String refId;			//关联ID
	private String xtlx;			//系统类型
	private String zzpch;			//种植批次号
	private String yzpch;			//养殖批次号
	private String qybm;			//企业编码
	
	/*
     * (非 Javadoc)   
     * <p>标题: initModel</p>   
     * <p>描述: </p>      
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new TCsptZsxxEntity());
    }
    @Autowired
    @Override
    protected void setService(ZhuisuliantiaoSerivce service){
    	super.setService(service);
    }
	/*public String list(){
		List list = this.getService().getFxzhuisuliantiaoList(model.getZsm());
		ServletActionContext.getRequest().setAttribute("ltList", list);
		ServletActionContext.getRequest().setAttribute("size", list.size());
		return "FXZhuiSuliantiao";
    }*/
    /**
	 * 获取正向追溯列表list
	 * @return
	 */
	/*public Object getZxZhuisuList(){
		//分页处理
		PageRequest pageRequest = this.buildPageRequest();
		list = getDataModel(getModelTemplate());
		Page<Object> page = this.getService().getZxZhuisuList(pageRequest, model.getJhpch(), model.getJypzh());
        list.setData(page);
		return NONE;
	}*/
	/**
	 * 查看追溯详细信息
	 * @return
	 */
	public String getZhuisuxxxx(){
		String returnType = "";
		ServletActionContext.getRequest().setAttribute("map", this.getService().getZhuisuXxxx(model));
		if("1".equals(model.getXtlx())){
			returnType = "zhongzhixinxi";
		}else if("2".equals(model.getXtlx())){
			returnType = "yangzhixinxi";
		}else if("3".equals(model.getXtlx())){
			returnType = "shucaipifaxinxi";
		}else if("4".equals(model.getXtlx())){
			returnType = "tuzaixinxi";
		}else if("5".equals(model.getXtlx())){
			returnType = "zhuroupifaxinxi";
		}else if("6".equals(model.getXtlx())){
			returnType = "tuanticaigouxinxi";
		}else if("7".equals(model.getXtlx())){
			returnType = "chaoshixinxi";
		}else if("8".equals(model.getXtlx())){
			returnType = "lingshoushichangxinxi";
		}
		return returnType;
	}
	/**
	 * 获取种植施肥信息列表
	 * @return
	 */
	public Object getZzSfxxList(){
		//分页处理
		PageRequest pageRequest = this.buildPageRequest();
		list = getDataModel(getModelTemplate());
		Page<Object> page = this.getService().getZzSfxx(pageRequest, zzpch,qybm);
        list.setData(page);
		return NONE;
	}
	/**
	 * 获取种植施药信息列表
	 * @return
	 */
	public Object getZzSyxxList(){
		//分页处理
		PageRequest pageRequest = this.buildPageRequest();
		list = getDataModel(getModelTemplate());
		Page<Object> page = this.getService().getZzSyxx(pageRequest, zzpch,qybm);
		list.setData(page);
		return NONE;
	}
	/**
	 * 获取养殖饲养信息列表
	 * @return
	 */
	public Object getYzSyxxList(){
		//分页处理
		PageRequest pageRequest = this.buildPageRequest();
		list = getDataModel(getModelTemplate());
		Page<Object> page = this.getService().getYzSlxx(pageRequest, yzpch,qybm);
        list.setData(page);
		return NONE;
	}
	/**
	 * 获取养殖用药信息列表
	 * @return
	 */
	public Object getYzYyxxList(){
		//分页处理
		PageRequest pageRequest = this.buildPageRequest();
		list = getDataModel(getModelTemplate());
		Page<Object> page = this.getService().getYzYyxx(pageRequest, yzpch,qybm);
        list.setData(page);
		return NONE;
	}
	/**
	 * 判断是否为末节点
	 */
	/*public void isEndNode(){
		jhpch=(String) ServletActionContext.getRequest().getAttribute("jhpch");
		jypzh=(String) ServletActionContext.getRequest().getAttribute("jypzh");
		setReturnData(this.getService().isEndNode(jhpch, jypzh));
	}*/
	public void getTree(){
		jhpch=(String) ServletActionContext.getRequest().getAttribute("jhpch");
		setReturnData(this.getService().getTree(jhpch));
	}
	
	public String getJhpch() {
		return jhpch;
	}
	public void setJhpch(String jhpch) {
		this.jhpch = jhpch;
	}
	public String getJypzh() {
		return jypzh;
	}
	public void setJypzh(String jypzh) {
		this.jypzh = jypzh;
	}
	public String getZsm() {
		return zsm;
	}
	public void setZsm(String zsm) {
		this.zsm = zsm;
	}
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	public String getXtlx() {
		return xtlx;
	}
	public void setXtlx(String xtlx) {
		this.xtlx = xtlx;
	}
	public String getZzpch() {
		return zzpch;
	}
	public void setZzpch(String zzpch) {
		this.zzpch = zzpch;
	}
	public String getYzpch() {
		return yzpch;
	}
	public void setYzpch(String yzpch) {
		this.yzpch = yzpch;
	}

	public String getQybm() {
		return qybm;
	}

	public void setQybm(String qybm) {
		this.qybm = qybm;
	}
}
