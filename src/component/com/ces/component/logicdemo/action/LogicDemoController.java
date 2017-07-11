package com.ces.component.logicdemo.action;

import org.apache.struts2.rest.DefaultHttpHeaders;

import com.ces.config.action.base.LogicComponentController;

public class LogicDemoController extends LogicComponentController {

    private static final long serialVersionUID = 1L;

    // 系统参数
    private static final String systemName = "系统名称";

    // 自身参数
    private static final String authenticationType = "authenticationType";

    // 输入参数
    private static final String quanzongNo = "quanzongNo";

    public Object test() {
        System.out.println("=====调用逻辑构件！");
        System.out.println("=====系统参数：");
        System.out.println("=====" + systemName + ":" + getSystemParamValue(systemName));
        System.out.println("=====自身参数：");
        System.out.println("=====" + authenticationType + ":" + getSelfParamValue(authenticationType));
        System.out.println("=====输出参数：");
        System.out.println("=====" + quanzongNo + ":" + getInputParamValue(quanzongNo));
        setReturnData("{'success':true,'message':'测试成功'}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
}
