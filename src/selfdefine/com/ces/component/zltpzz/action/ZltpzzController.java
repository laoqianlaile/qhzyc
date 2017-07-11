package com.ces.component.zltpzz.action;

import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zltpzz.service.ZltpzzService;
import com.ces.xarch.core.entity.StringIDEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 同批追踪Controller
 * Created by dengjinhui on 15/9/10.
 */
public class ZltpzzController extends TraceShowModuleDefineServiceDaoController<StringIDEntity,ZltpzzService,TraceShowModuleDao>{

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
    @Autowired
    private ZltpzzService zltpzzService;

    /**
     * 同批追踪列表查询
     */
    public String queryTpzzlb(){
        String zsm = this.getRequest().getParameter("zsm");
        PageRequest pageRequest = buildPageRequest();
        //获取产品批次号
        Page page = zltpzzService.queryTpzzlb(zsm,pageRequest);
        list = getDataModel(getModelTemplate());
//        setReturnData(putPageToMap(page,page.getSize()+"",page.getNumber()+""));
        list.setData(page);
        return null;
//        String zsm=getParameter("zsm");
//        setReturnData(getService().queryTpzzlb(zsm));
    }
    /**
     * 将page转换成coral4.0能用的格式
     * @param page
     * @param pageSize
     * @param pageNo
     * @return
     */
    public Map putPageToMap(Page page,String pageSize,String pageNo){
        if(page==null){
            return null;
        }
        Map map = new HashMap();
        map.put("data", page.getContent());
        map.put("total", page.getTotalElements());
        map.put("pageSize", pageSize);
        map.put("pageNumber", pageNo);
        map.put("totalPages", page.getTotalPages());
        return map;
    }

    /**
     * 修改问题标记字段
     */
    public void changeWtbj(){
        String id=getParameter("id");
        String bj=getParameter("bj");
        setReturnData(getService().changeWtbj(id, bj));

    }

    public void getJdandWd(){
        String cpzsm=getParameter("cpzsm");
        setReturnData(getService().getJdandWd(cpzsm));
    }

    public void reloadGrid(){
        String cpzsm=getParameter("cpzsm");
        setReturnData(getService().reloadGrid(cpzsm));
    }

    public Object reloadGridBX(){
        PageRequest pageRequest = this.buildPageRequest();
        list = getDataModel(getModelTemplate());
        Page<Object> page = this.getService().reloadGridBX(pageRequest);
        list.setData(page);
        return null;
    }

    public void reloadGridAtferClick(){
        PageRequest pageRequest = this.buildPageRequest();
        list = getDataModel(getModelTemplate());
        Page<Object> page = this.getService().reloadGridAtferClick(pageRequest);
        list.setData(page);

    }




}