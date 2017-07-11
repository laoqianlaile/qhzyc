package com.ces.config.dhtmlx.dao.appmanage;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.ces.config.dhtmlx.entity.appmanage.TableTree;
import com.ces.xarch.core.persistence.jpa.StringIDDao;

public interface TableTreeDao extends StringIDDao<TableTree> {

    /**
     * 根据名称和父节点ID获取TableTree
     * 
     * @param name 名称
     * @param parentId 父节点ID
     * @return TableTree
     */
    public TableTree getByNameAndParentId(String name, String parentId);
    
    /**
     * 根据父节点ID获取TableTree
     * 
     * @param parentId 父节点ID
     * @return TableTree
     */
    public List<TableTree> getByParentId(String parentId);
    
    /**
     * 获取TableTree第一层节点
     * 
     * @return TableTree
     */
    @Query("from TableTree t where t.parentId = ?1")
    public List<TableTree> getFirst(String classification);
    
    /**
     * qiujinwei 2015-1-21 
     * <p>标题: getMaxShowOrderByParentId</p>
     * <p>描述: 根据父节点ID获取最大显示顺序</p>
     * @return Integer    返回类型   
     * @throws
     */
    @Query("SELECT MAX(T.showOrder) FROM TableTree T WHERE parentId=?1")
    public Integer getMaxShowOrderByParentId(String parentId);
    
//    /**
//     * 获取所有TableTree节点
//     * 
//     * @return TableTree
//     */
//    @Query(value="select ",nativeQuery = true)
//    public List<Object[]> getcomboOfTableTree(){
//    	
//    }
}
