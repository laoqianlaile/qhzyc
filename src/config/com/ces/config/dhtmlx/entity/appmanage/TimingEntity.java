package com.ces.config.dhtmlx.entity.appmanage;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.ces.xarch.core.entity.StringIDEntity;
/**
 * 定时任务
 * @author wang
 *
 */
@Entity
@Table(name = "T_XTPZ_TIMING")
public class TimingEntity extends StringIDEntity{

	private static final long serialVersionUID = 1L;
	
	/**  名称 **/
	private String name;
	/**  定时时间 **/
	private String time;
	/**  Spring配置文件BeanID**/
	private String beanId;
	/**  方法名 **/
	private String method;
	/**  方式：0-JAVA 1-批处理 **/
	private String type;
	/**  描述 **/
	private String remark;
	/**  cmd命令 **/
	private String command;
	/**  是否启动 **/
	private String isOperates = "0";
	/**定时类型： 0-定时 1-间隔**/
	private String timingType;
	//**上一次执行结果： 0-没执行 1-成功 2-失败**/
    //private String result;

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getBeanId() {
		return beanId;
	}
	public void setBeanId(String beanId) {
		this.beanId = beanId;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	
    public String getIsOperates() {
		return isOperates;
	}
	public void setIsOperates(String isOperates) {
		this.isOperates = isOperates;
	}

	public String getTimingType() {
		return timingType;
	}
	public void setTimingType(String timingType) {
		this.timingType = timingType;
	}

	/*public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }*/

    private static final String DEFINE_STAUTS_START = "启动";
    /** 所有模块 设置状态值(1)描述：已设置 */
    private static final String DEFINE_STAUTS_STOP = "停止";
	
	
	@Transient
	public String getOperatesArea(){
		return getDefineArea(isOperates);
	}
	
	
    private String getDefineArea(String status){
    	        
    	String statusArea = DEFINE_STAUTS_START;
    	if(status.equals("0")){
    		statusArea = DEFINE_STAUTS_START;
    	}else if(status.equals("1")){
    		statusArea = DEFINE_STAUTS_STOP;
    	}
    	statusArea += "^javascript:Operates(" + status + "," + type + ","+ "\"" + id + "\")^_self";
    	return statusArea;
    }
    
    @Transient
	public String getTaskDetailArea(){
		return getTaskById(isOperates);
	}
	
	
    private String getTaskById(String status){
    	        
    	String statusArea = "显示";
    	statusArea += "^javascript:taskDetail(" + status + "," + type + ","+ "\"" + id + "\")^_self";
    	return statusArea;
    }
	
}
