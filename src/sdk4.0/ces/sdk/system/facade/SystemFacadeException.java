package ces.sdk.system.facade;

/**
 * 操作异常类.<br>
 * @author Administrator
 *
 */
public class SystemFacadeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * 错误代码
	 */
	private String errorCode;

	/**
	 * 
	 * @param errorCode:错误代码
	 * @param errorMsg：错误名称
	 * @param exce:具体例外
	 */
	public SystemFacadeException(String errorCode, String errorMsg,
	                             Throwable exce) {
		super(errorMsg, exce);
		this.errorCode = errorCode;
	}

	/**
	 * 
	 * @param errorMsg：错误名称
	 * @param exce:具体例外
	 */
	public SystemFacadeException(String errorMsg, Throwable exce) {
		super(errorMsg, exce);
	}

	/**
	 * 
	 * @param  
	 */
	public SystemFacadeException(Throwable exce) {
		super(exce);
	}

	/**
	 * 
	 * @param errorMsg 异常信息
	 */
	public SystemFacadeException(String errorMsg) {
		super(errorMsg);
	}

	/**
	 * 
	 * @param errorCode 异常代码
	 * @param errorMsg 异常信息
	 */
	public SystemFacadeException(String errorCode, String errorMsg) {
		super(errorMsg);
		this.errorCode = errorCode;
	}

	/**
	 * 获取 异常代码
	 * @return
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * 设置 异常代码
	 * @param errorCode
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
