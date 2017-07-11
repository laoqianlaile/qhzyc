package com.ces.coflow.web.util;

public interface Const {
	/**
	 * 未定义，表示当前流程文件未被创建
	 */
	public static String UNDEFINED = "undefined";
	/**
	 * 正运行的，表示当前流程正在运行
	 */
	public static String RUNNING = "running";
	/**
	 * 已更新的，表示当前流程已被更新
	 */
	public static String UPDATED = "updated";
	/**
	 * 已停止的，表示当前流程已被停止
	 */
	public static String STOPPED = "stoped";
	/**
	 * 未注册的，表示当前流程还未注册到引擎
	 */
	public static String UNREGISTERED = "unregist";
	/**
	 * 本地的，表示服务器端没有该文件
	 */
	public static String LOCAL = "local";
	/**
	 * 未知的，表示当前没有连接到服务器
	 */
	public static String UNKNOWN = "unknown";
	/**
	 * 错误的，表示服务器上的文件已不存在
	 */
	public static String ERROR = "error";
	/**
	 * 已经修改的前缀标识
	 */
	public static String MODIFIED = "modified";
	/**
	 * 已修改的，表示文件在本地已被修改过，与服务器端的文件不一致，
	 */
	public static String MODIFIED_RUNNING = "modified running";
	/**
	 * 已修改的，表示文件在本地已被修改过，与服务器端的文件不一致，
	 */
	public static String MODIFIED_UPDATED = "modified updated";
	/**
	 * 已修改的，表示文件在本地已被修改过，与服务器端的文件不一致，
	 */
	public static String MODIFIED_STOPPED = "modified stoped";
	/**
	 * 已修改的，表示文件在本地已被修改过，与服务器端的文件不一致，
	 */
	public static String MODIFIED_UNREGISTERED = "modified unregist";
}
