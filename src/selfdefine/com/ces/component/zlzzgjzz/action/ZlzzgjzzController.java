package com.ces.component.zlzzgjzz.action;

import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.component.zlwtqd.service.ZlwtqdService;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zlzzgjzz.service.ZlzzgjzzService;
import com.ces.xarch.core.entity.StringIDEntity;

/**
 * Created by Administrator on 2015/9/14.
 */
public class ZlzzgjzzController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZlzzgjzzService, TraceShowModuleDao> {
       public void changeWtbj(){
           String id=getParameter("id");
           String bj=getParameter("bj");
           setReturnData(getService().changeWtbj(id,bj));

       }

}