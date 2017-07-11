package com.ces.config.dhtmlx.action.appmanage;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.domain.Specification;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.ColumnOperationDao;
import com.ces.config.dhtmlx.entity.appmanage.ColumnDefine;
import com.ces.config.dhtmlx.entity.appmanage.ColumnOperation;
import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.config.dhtmlx.service.appmanage.ColumnDefineService;
import com.ces.config.dhtmlx.service.appmanage.ColumnOperationService;
import com.ces.config.dhtmlx.service.appmanage.TriggerService;
import com.ces.config.utils.CodeUtil;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.exception.FatalException;
import com.ces.xarch.core.utils.SearchFilter;
import com.ces.xarch.core.utils.SearchHelper;
import com.ces.xarch.core.web.jackson.BeanFilter;
import com.google.common.collect.Maps;

public class ColumnOperationController extends ConfigDefineServiceDaoController<ColumnOperation, ColumnOperationService, ColumnOperationDao> {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void initModel() {
		setModel(new ColumnOperation());
	}
	
	/*
     * (非 Javadoc)   
     * <p>标题: setService</p>   
     * <p>描述: 注入服务层(Service)</p>   
     * @param service   
     * @see com.ces.xarch.core.web.struts2.BaseController#setService(com.ces.xarch.core.service.AbstractService)
     */
    @Override
    @Autowired
    @Qualifier("columnOperationService")
    protected void setService(ColumnOperationService service) {
        super.setService(service);
    }
	
	public Object show()throws FatalException {
		model = getService().findOne(buildEntitySpecification());
		processFilter((BeanFilter)model);
	    return new DefaultHttpHeaders("success").disableCaching();
    }
    
    private  Specification<ColumnOperation> buildEntitySpecification() throws FatalException {
        Map<String, SearchFilter> filterMap = Maps.newHashMap();
        String id = getId(); // column relation id
        System.out.println("----------id---------"+id);
        filterMap.put("EQ_columnRelationId", new SearchFilter("columnRelationId", SearchFilter.Operator.EQ, id));
        return SearchHelper.buildSpecification(filterMap.values(), getModelClass());
    }

	@Override
	public Object create() throws FatalException {
		model = getService().save(model);
		// 生成触发器
		getService(TriggerService.class).generateColumnRelationTrigger(model.getTableId());
		getService(TriggerService.class).generateColumnRelationTrigger(model.getOriginTableId());
        return SUCCESS;
	}

	@Override
	public Object update() throws FatalException {
	    // 更新前子表与父表ID
	    ColumnOperation oldEntity = getService().getByID(model.getId());
	    // 更新数据
		getService().save(model);
		// 生成触发器
        getService(TriggerService.class).generateColumnRelationTrigger(model.getTableId());
        getService(TriggerService.class).generateColumnRelationTrigger(model.getOriginTableId());
        // 如果更换父表
		if (!model.getOriginTableId().equals(oldEntity.getOriginTableId())) {
		    getService(TriggerService.class).generateColumnRelationTrigger(oldEntity.getOriginTableId());
		}
		return SUCCESS;
	}

    /**
     * 最值计算时校验子表字段是否是编码字段，如果是，编码字段是否设置了最值顺序
     * 
     * @return Object
     */
    public Object validateMostValue() {
        String columnId = getParameter("columnId");
        ColumnDefine columnDefine = getService(ColumnDefineService.class).getByID(columnId);
        // 是否是编码字段
        boolean isCode = false;
        // 如果是编码字段且编码字段含有最值顺序
        boolean hasMostValueShowOrder = false;
        String codeTypeCode = columnDefine.getCodeTypeCode();
        if (StringUtil.isNotEmpty(codeTypeCode)) {
            isCode = true;
            List<Code> codeList = CodeUtil.getInstance().getCodeList(codeTypeCode);
            if (CollectionUtils.isNotEmpty(codeList)) {
                for (Code code : codeList) {
                    if (code.getMostValueShowOrder() != null) {
                        hasMostValueShowOrder = true;
                    }
                }
            }
        }
        setReturnData("{'isCode':" + isCode + ", 'hasMostValueShowOrder':" + hasMostValueShowOrder + "}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
}
