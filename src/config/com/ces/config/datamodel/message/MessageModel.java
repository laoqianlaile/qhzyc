package com.ces.config.datamodel.message;

/**
 * <p>描述: 消息提示</p>
 * <p>公司: 上海中信信息发展股份有限公司</p>
 * @author qiucs   
 * @date 2014-11-6 下午15:26:32
 *
 */
public class MessageModel {
    
    private Integer status = Integer.parseInt("0");
    
    private Boolean success = Boolean.FALSE;
    
    private Object data;
    
    private String message;
    
    public MessageModel() {}

    public MessageModel(Boolean success, Object data) {
    	this.data = data;
        this.success = success;
    }

    public MessageModel(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public MessageModel(Boolean success, Integer status, String message) {
        this.status = status;
        this.success = success;
        this.message = message;
    }
    
    public MessageModel(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    /****** 构造器 ******/
    public static MessageModel newInstance(Boolean success) {
        return newInstance(success, "");
    }
    
    public static MessageModel newInstance(Boolean success, String message) {
        return new MessageModel(success, message);
    }
    
    public static MessageModel trueInstance(String message) {
        return new MessageModel(Boolean.TRUE, message);
    }
    
    public static MessageModel trueInstance(Integer status, String message) {
        return new MessageModel(Boolean.TRUE, status,message);
    }
    
    public static MessageModel trueInstance(Object data) {
        return new MessageModel(Boolean.TRUE, data);
    }

    public static MessageModel trueInstance(Integer status, Object data) {
        return new MessageModel(Boolean.FALSE, data);
    }
    
    public static MessageModel falseInstance(String message) {
        return new MessageModel(Boolean.FALSE, message);
    }

    public static MessageModel falseInstance(Integer status, String message) {
        return new MessageModel(Boolean.FALSE, status, message);
    }
    
    public static MessageModel falseInstance(Object data) {
        return new MessageModel(Boolean.TRUE, data);
    }

    public static MessageModel falseInstance(Integer status, Object data) {
        return new MessageModel(Boolean.FALSE, data);
    }
}
