package com.ces.component.sendMsg;

import cn.sendsms.AGateway;
import cn.sendsms.IOutboundMessageNotification;
import cn.sendsms.Library;
import cn.sendsms.OutboundMessage;
import cn.sendsms.Service;
import cn.sendsms.AGateway.Protocols;
import cn.sendsms.modem.SerialModemGateway;

//import com.ces.component.zlaqyj.service.SendMessage;
//import com.ces.component.zlaqyj.service.SendMessage.OutboundNotification;

public class SendMessage {
	public void doIt() throws Exception
	{
		System.out.println("进入father");
		Service srv;
		OutboundMessage msg;
		OutboundNotification outboundNotification = new OutboundNotification();
		System.out.println("示例: 通过串口短信设备发送短信.");
		System.out.println(Library.getLibraryDescription());
		System.out.println("版本: " + Library.getLibraryVersion());

         //有时由于信号问题,可能会引起超时,运行时若出现No Response 请把这句注释打开
         //System.setProperty("sendsms.nocops",new String());
		//在linux系统上如果出现No Response，在信号问题已经排除的情况下，请把这句注释打开
        //System.setProperty("sendsms.serial.polling",new String());
		srv = Service.getInstance();

                //使用时请修改端口号和波特率,如果不清楚,可以去www.sendsms.com.cn下载金笛设备检测工具检测一下
		SerialModemGateway gateway = new SerialModemGateway("jindi", "COM4", 115200, "Wavecom", null);
          

                // 设置通道gateway是否处理接受到的短信
		//gateway.setInbound(true);

                // 设置是否可发送短信
		gateway.setOutbound(true);

		gateway.setSimPin("0000");

                // 设置短信编码格式，默认为 PDU (如果只发送英文，请设置为TEXT)。CDMA设备只支持TEXT协议
                gateway.setProtocol(Protocols.PDU);
		
                // 添加Gateway到Service对象，如果有多个Gateway，都要一一添加。
		srv.addGateway(gateway);

		
		
		//启动服务
		srv.startService();
			
	
		
		System.out.println();
//		System.out.println("serviceStatus:"+serviceStatus);
		System.out.println("设备信息:");
		System.out.println("  厂  商: " + gateway.getManufacturer());
		System.out.println("  型  号: " + gateway.getModel());
		System.out.println("  序列号: " + gateway.getSerialNo());
		System.out.println("  IMSI号: " + gateway.getImsi());
		System.out.println("  信  号: " + gateway.getSignalLevel() + "%");
		System.out.println("  电  池: " + gateway.getBatteryLevel() + "%");
		System.out.println();

		// 发送短信
		msg = new OutboundMessage("13262940821", "您好ma?");
		msg.setEncoding(OutboundMessage.MessageEncodings.ENCUCS2);
		msg.setStatusReport(true);

		srv.sendMessage(msg);
		System.out.println(msg);

		System.out.println("按<回车>键退出...");
		System.in.read();
		
		srv.stopService();
		
	
	}

	public class OutboundNotification implements IOutboundMessageNotification
	{
		public void process(AGateway gateway, OutboundMessage msg)
		{
			System.out.println("Outbound handler called from Gateway: " + gateway.getGatewayId());
			System.out.println(msg);
		}
	}

//	public static void main(String args[])
//	{
//		SendMessage app = new SendMessage();
//		try
//		{
//			app.doIt();
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//	}


}
