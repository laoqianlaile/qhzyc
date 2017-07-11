package ces.sdk.system.bean;

public class OrgTypeInfo {
	
	
	/** 级别ID */
	private String id;
	/** 父组织ID */
	private String parentId;
	/** 级别名称 */
	private String name;
	/** 排序号 */
	private Integer showOrder;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getShowOrder() {
		return showOrder;
	}
	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}
}
