package com.ces.config.dhtmlx.action.appmanage;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.appmanage.TableRelationDao;
import com.ces.config.dhtmlx.entity.appmanage.TableRelation;
import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.service.appmanage.ColumnOperationService;
import com.ces.config.dhtmlx.service.appmanage.ModuleService;
import com.ces.config.dhtmlx.service.appmanage.TableRelationService;
import com.ces.config.dhtmlx.service.appmanage.TreeDefineService;
import com.ces.config.utils.StringUtil;
import com.ces.xarch.core.exception.FatalException;

public class TableRelationController extends
        ConfigDefineServiceDaoController<TableRelation, TableRelationService, TableRelationDao> {

    private static final long serialVersionUID = -567814481832252865L;

    @Override
    protected void initModel() {
        setModel(new TableRelation());
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
    @Qualifier("tableRelationService")
    protected void setService(TableRelationService service) {
        super.setService(service);
    }

    /**
     * 源表字段检索
     * 
     * @return
     * @throws FatalException
     */
    public Object getShowYTableColum() throws FatalException {
        String tableId = getParameter("Q_tableId");
        setReturnData(getService().getShowYColumnName(tableId,null));
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 目标表字段检索
     * 
     * @return
     * @throws FatalException
     */

    public Object getShowMbTableColum() throws FatalException {
        String tableId = getParameter("Q_tableId");
        String relatedTableId = getParameter("Q_relTableId");
        setReturnData(getService().getShowYColumnName(tableId,relatedTableId));
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 源表、目标表字段检索
     * 
     * @return
     * @throws FatalException
     */
    public Object getShowRelationColum() throws FatalException {
        String tableId = getParameter("Q_tableId");
        String relTableId = getParameter("Q_relTableId");
        setReturnData(getService().getShowRelationColumnName(tableId, relTableId));
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * @return
     * @throws FatalException
     */
    public Object getShowCheckDefineCoumn() throws FatalException {
        String tableId = getParameter("Q_tableId");
        setReturnData(getService().getShowCheckDefineCoumn(tableId));
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 保存表关系
     * 
     * @return
     */
    public Object saveColumn() {
        try {
            String rowsValue = getParameter("P_rowsValue");
            String tableId = getParameter("Q_tableId");
            String mTableId = getParameter("Q_mTableId");
            getService().saveColumn(rowsValue, tableId, mTableId);
            setReturnData(MessageModel.trueInstance("OK"));
        } catch (Exception e) {
            e.printStackTrace();
            setReturnData(MessageModel.falseInstance("ERROR"));
        }
        return NONE;
    }

    /**
     * 下拉框选择表
     * 
     * @return
     * @throws FatalException
     */
    public Object checkMBTable() throws FatalException {
        String tableId = getParameter("Q_tableId");
        String groupId = getParameter("Q_groupId");
        System.out.println(tableId);
        if (StringUtil.isEmpty(groupId)) {
            setReturnData(getService().getCheckMuTable(tableId));
		} else {
			setReturnData(getService().getCheckMuTable(tableId,groupId));
		}
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 删除表关系
     * 
     * @return
     * @throws FatalException
     */
    public void delTableRelation() throws FatalException {
        String rId = getParameter("reId");
        String tableId = getParameter("talbeId");
        getService().delTableRelation(rId, tableId, Boolean.TRUE);
    }

    /**
     * 修改表关系之前验证表关系是否在字段关系中被使用
     * 
     * @return
     * @throws FatalException
     */
    public Object checkRelationTable(){
    	String rId = getParameter("reId");
        String tableId = getParameter("talbeId");
        long tr_cp = getService(ColumnOperationService.class).getTotalTableRelation(tableId, rId);
        if(tr_cp==0){
        	setReturnData("success");
        }else{
        	setReturnData("error");
        }
    	return new DefaultHttpHeaders("success").disableCaching();
    }
    
    /**
     * 修改表关系之前验证表关系是否在字段关系中被使用
     * 
     * @return
     * @throws FatalException
     */
    public Object validateRelationTable(){
    	String rId = getParameter("reId");
        String tableId = getParameter("talbeId");
        int rows = Integer.parseInt(getParameter("rows"));
        long tr_cp = getService(ColumnOperationService.class).getTotalTableRelation(tableId, rId);
        if(tr_cp>0){
        	setReturnData("confirm");
        }else if (tr_cp==0) {
        	Object result = getService().getTotalTableRelation(tableId, rId);
        	if(rows!=Integer.parseInt(result.toString())){
        		setReturnData("success");
        	}else{
        		long tr_m = getService(ModuleService.class).getTotalTableRelation(tableId, rId);
        		if(tr_m>0){
        			setReturnData("confirm");
        		}else if (tr_m==0) {
        			long tr_t = getService(TreeDefineService.class).getTotalTableRelation(tableId, rId);
        			if(tr_t>0){
        				setReturnData("confirm");
        			}else {
        				setReturnData("success");
					}
				}
        	}
		}
    	return new DefaultHttpHeaders("success").disableCaching();
    }
    
    /**
	 * 查询源表和关联表记录数目
	 * @param tableId, rId
	 */
    public Object getTotalTableRelation(){
    	String rId = getParameter("reId");
        String tableId = getParameter("talbeId");
        // 1. columnOperator
        
        // 2. if result > 0 return confirm
        
        // 2. result == 0  check relation num == delete num  1 module  m_result > 0 return confirm  else tree
    	Object result = getService().getTotalTableRelation(tableId, rId);
    	setReturnData(Integer.parseInt(result.toString())>1?"morethan1":"equal1");
    	return new DefaultHttpHeaders("success").disableCaching();
	}
    
    /**
     * 查询表关系所有的列
     * 
     * @return
     * @throws FatalException
     */
    public Object getAllTableRelationList() throws FatalException {
        String tableId = getParameter("Q_tableId");
        // setReturnData(getService().getAllTableRelations(tableId));
        setReturnData(getService().getRelationByTableId(tableId));
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 新增表关系 源表列表字段 ---add
     * 
     * @return
     * @throws FatalException
     */
    public Object getAddyTableRelation() throws FatalException {
        String tableId = getParameter("Q_tableId");
        String relatedTableId = getParameter("Q_relTableId");
        setReturnData(getService().getAddyTable(tableId,relatedTableId));
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 新增表关系 源表列表字段 ---add
     * 
     * @return
     * @throws FatalException
     */
    public void delYMRelation() throws FatalException {
        String Id = getParameter("Q_Id");
        getService().delYmTableRelation(Id);
    }
    
    public Object comboOfRelateTables() {
        String tableId = getParameter("P_tableId");
        String includeMe = getParameter("P_includeMe");
        setReturnData(getService().getComboOfRelateTables(tableId, includeMe));
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     *  根据表ID查询关联表的信息
     * @return
     * @throws FatalException
     * */
    public Object queryRelationTableByTableID(){
    	String tableId= getParameter("tableId");
    	setReturnData(getService().queryRelationTableByTableID(tableId));
    	return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
    
    /** gmh 2013-10-21
     *  用于字段关联查询两表之间的关联字段
     * @param child_tid
     * @param parent_tid
     * */
    public Object queryRelationCols(){
    	String child_tid= getParameter("child_tid");
    	String parent_tid= getParameter("parent_tid");
    	setReturnData(getService().queryRelationCols(child_tid,parent_tid));
    	return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
    
    /**
     *  根据ids遍历表关联关系
     * @param ids
     * @return
     * @throws FatalException
     * */
    public Object getRelationsOfIds(){
    	String ids = getParameter("P_ids");
    	String groupId = getParameter("P_groupId");
    	String codes = getParameter("P_codes");
    	try {
            setReturnData(getService().getRelationsOfIds(ids, groupId, codes));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    	return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
}
