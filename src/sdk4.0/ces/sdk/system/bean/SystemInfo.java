package ces.sdk.system.bean;

public class SystemInfo {

	/** id */
	private String id;
	/** 系统编码 */
	private String code;
	/** 系统名称 */
	private String name;
	/** 备注 */
	private String comments;
	/** 排序号 */
	private Long showOrder;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}
	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}
	/**
	 * @return the showOrder
	 */
	public Long getShowOrder() {
		return showOrder;
	}
	/**
	 * @param showOrder the showOrder to set
	 */
	public void setShowOrder(Long showOrder) {
		this.showOrder = showOrder;
	}
	
}
