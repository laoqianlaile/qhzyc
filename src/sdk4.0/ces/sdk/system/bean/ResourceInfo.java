package ces.sdk.system.bean;

/**
 * 资源对象接口.<br>
 * @author Administrator
 *
 */
public class ResourceInfo {
    /** UUID */
    private String id;
    /** 父资源ID */
    private String parentId;
    /** 资源名称 */
    private String name;
    /** 备注 */
    private String comments;
    /** 资源图片 */
    private String resourceImg;
    /** 资源值 */
    private String resoureceKey;
    /** 资源URL */
    private String url;
    /** 排序号 */
    private Long showOrder;
    /** 是否是菜单 */
    private String isMenu;
    /** 资源文件 */
    private String sourceFile;
    /** 资源类型名称 */
    private String resourceTypeName;
    /** 导航URL */
    private String navigateUrl;
    /** 业务URL */
    private String businessUrl;
    /** 其他URL */
    private String otherUrl;
    /** 调用函数 */
    private String useFunction;
    /** 模块ID */
    private String moduleId;
	/** 是否拥有树 */
	private String hasTree;
	
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
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }
    public String getResourceImg() {
        return resourceImg;
    }
    public void setResourceImg(String resourceImg) {
        this.resourceImg = resourceImg;
    }
    public String getResoureceKey() {
        return resoureceKey;
    }
    public void setResoureceKey(String resoureceKey) {
        this.resoureceKey = resoureceKey;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public Long getShowOrder() {
        return showOrder;
    }
    public void setShowOrder(Long showOrder) {
        this.showOrder = showOrder;
    }
    public String getIsMenu() {
        return isMenu;
    }
    public void setIsMenu(String isMenu) {
        this.isMenu = isMenu;
    }
    public String getSourceFile() {
        return sourceFile;
    }
    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }
    public String getResourceTypeName() {
        return resourceTypeName;
    }
    public void setResourceTypeName(String resourceTypeName) {
        this.resourceTypeName = resourceTypeName;
    }
    public String getNavigateUrl() {
        return navigateUrl;
    }
    public void setNavigateUrl(String navigateUrl) {
        this.navigateUrl = navigateUrl;
    }
    public String getBusinessUrl() {
        return businessUrl;
    }
    public void setBusinessUrl(String businessUrl) {
        this.businessUrl = businessUrl;
    }
    public String getOtherUrl() {
        return otherUrl;
    }
    public void setOtherUrl(String otherUrl) {
        this.otherUrl = otherUrl;
    }
    public String getUseFunction() {
        return useFunction;
    }
    public void setUseFunction(String useFunction) {
        this.useFunction = useFunction;
    }
    public String getModuleId() {
        return moduleId;
    }
    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }
	/**
	 * @return 返回  String hasTree
	 */
	public String getHasTree() {
		return hasTree;
	}
	/**
	 * @param 设置  String hasTree
	 */
	public void setHasTree(String hasTree) {
		this.hasTree = hasTree;
	}


}
