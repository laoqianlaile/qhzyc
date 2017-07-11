package com.ces.component.zlaqyj.service;

import cn.sendsms.GatewayException;
import cn.sendsms.Library;
import cn.sendsms.OutboundMessage;
import cn.sendsms.SendSMSException;
import cn.sendsms.Service;
import cn.sendsms.AGateway.Protocols;
import cn.sendsms.TimeoutException;
import cn.sendsms.modem.SerialModemGateway;

import com.ces.component.sendMsg.SendMessage;
import com.ces.component.sendMsg.SendMessage.OutboundNotification;
//import com.ces.component.sendMsg.SendMessage.OutboundNotification;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.utils.AppDefineUtil;
import com.esms.MessageData;
import com.esms.PostMsg;
//import com.xuanwu.msggate.common.protobuf.CommonItem;
import com.esms.common.entity.Account;
import com.esms.common.entity.GsmsResponse;
import com.esms.common.entity.MTPack;
import org.springframework.stereotype.Component;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.trace.service.base.TraceShowModuleDefineDaoService;
import com.ces.xarch.core.entity.StringIDEntity;

import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class ZlaqyjService extends TraceShowModuleDefineDaoService<StringIDEntity, TraceShowModuleDao> {

	private static  Service srv;
	private static SerialModemGateway gateway;
	private  static int count=0;

//初始化srv  gateway
	synchronized public static void smsStart(){
		if(count==1){return;};
    	//使用时请修改端口号和波特率,如果不清楚,可以去www.sendsms.com.cn下载金笛设备检测工具检测一下
    			gateway = new SerialModemGateway("jindi", "COM4", 115200, "Wavecom", null);
    			srv = Service.getInstance();
    	                // 设置通道gateway是否处理接受到的短信
    			//gateway.setInbound(true);

    	                // 设置是否可发送短信
    			gateway.setOutbound(true);

    			gateway.setSimPin("0000");

    	                // 设置短信编码格式，默认为 PDU (如果只发送英文，请设置为TEXT)。CDMA设备只支持TEXT协议
    	        gateway.setProtocol(Protocols.PDU);
    			
    	                // 添加Gateway到Service对象，如果有多个Gateway，都要一一添加。
    			try {
					srv.addGateway(gateway);
				} catch (GatewayException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

    			
    			
    			//启动服务
    			try {
					srv.startService();
				} catch (TimeoutException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (GatewayException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SendSMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    }

    @Override
    protected String buildCustomerFilter(String tableId, String componentVersionId, String moduleId, String menuId, Map<String, Object> paramMap) {
        String qybm= SerialNumberUtil.getInstance().getCompanyCode();
        StringBuffer filter = new StringBuffer(AppDefineUtil.RELATION_AND + " (wtbj = '1' or ccwtbj = '1') and qybm = '"+qybm+"'");
        return filter.toString();

    }
//    xx店xxx同志，xxx企业发现xxxx批次青椒，yyy批次西红柿存在食品安全问题
//    ，请即刻下架，避免流向消费群体。请保留问题产品现状，
//    我们会尽快联系您启动召回事宜。给您工作带来不便，敬请谅解，xxx企业。2015年8月8日 8时8分。

    /**
     * 发送一条短信
     * @param id
     * @return
     */
    public synchronized Object sendoneMessage(String id){
    	
//    	try {
//			app.doIt();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        Date now=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dat=sdf.format(now);

        String sql ="update t_zz_bzgl set  yjsj = '"+dat+"' where cpzsm = (select cpzsm from v_zlzz_aqyj where id= '"+id+"')";
        String sql2 ="update t_zz_bzgl set yjzt = '1' where cpzsm = (select cpzsm from v_zlzz_aqyj where id='"+id+"')";
        //String sql3 = "insert into t_zl_yjxq (sjid,lxr,yjsj) values("","","") ";
        Map<String, String> dataMap =new  HashMap<String, String>();
        
//      String sql22="update t_zz_bzgl set yjzt = 1  where cpzsm in ( select cpzsm from v_zlzz_aqyj where id in ("+sb.toString()+" ) )";
//      String sql11="update t_zz_bzgl set yjsj = '"+dat+"' where cpzsm in ( select cpzsm from v_zlzz_aqyj where id in ("+sb.toString()+" ) )";
        String sql1="select * from v_zlzz_aqyj where id = '"+id+"'";
        Map<String,Object> map=new HashMap<String, Object>();
        map=DatabaseHandlerDao.getInstance().queryForMap(sql1);
        //DatabaseHandlerDao.getInstance().executeSql(sql1)
        DatabaseHandlerDao.getInstance().executeSql(sql2);
        DatabaseHandlerDao.getInstance().executeSql(sql);
        //将数据添加到预警详情表中
        String  m = (String) map.get("LXR");
        String cpzsm = (String) map.get("CPZSM");;
        dataMap.put("CPZSM", cpzsm);
        dataMap.put("LXR", m);
        dataMap.put("YJSJ", dat);
        saveOne("T_ZL_YJXQ",dataMap);
//      StringBuffer sb=new StringBuffer();
        StringBuilder sb=new StringBuilder();
        sb.append(map.get("KHMC"));
        sb.append(map.get("LXR"));
        sb.append(",");
        sb.append(map.get("QYMC"));
        sb.append("发现追溯码为");
        sb.append(map.get("CPZSM"));
        sb.append(map.get("CPMC"));
        sb.append("存在隐患,");
        sb.append(map.get("QYMC"));
        sb.append(".");
        sb.append(dat);
        String content =String.valueOf(sb.toString());
        String phone=String.valueOf(map.get("DH"));
//        int i=DatabaseHandlerDao.getInstance().executeSql(sql);
        
        
/*        //发短信功能
//          SendMessage app=new SendMessage(){
//    		public void doIt(){
    	System.out.println("进入son");
//    			Service srv;
    	OutboundMessage msg;
//    			OutboundNotification outboundNotification = new OutboundNotification();
//    			srv = Service.getInstance();          
//    			SerialModemGateway gateway = new SerialModemGateway("jindi", "COM4", 115200, "Wavecom", null);   	              
//    			gateway.setOutbound(true); 
//    			gateway.setSimPin("0000");	         
//    	        gateway.setProtocol(Protocols.PDU);      
    	if(count==0){
    		smsStart();
    		count++;
    	}
    	System.out.println(phone);
    	System.out.println(content);


    			// 发送短信
    	msg = new OutboundMessage(phone, content);
    	msg.setEncoding(OutboundMessage.MessageEncodings.ENCUCS2);
    	msg.setStatusReport(true);
    	System.out.println(msg);
//    			System.out.println("111111111111111111");
                
    			try {
    				System.out.println("开始发送");
					boolean bolean=srv.sendMessage(msg);
					System.out.println("发送完毕");
					System.out.println(msg);
					System.out.println(bolean);
				} catch (TimeoutException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (GatewayException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			
//    		}
    		
//    	};
//    	try {
//			app.doIt();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
*/        return null;
    }

    /**
     * 发送多条短信
     * @param ids
     * @return
     */

    public Object sendGroupMessage(String[] ids){
    	for(int i=0;i<ids.length;i++){
        	sendoneMessage(ids[i]);
        	try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    	return null;
//          String[] content=new String[]{};
//          String[] dh=new String[]{};
////         int [] iarr=null;
//         
////         int[] i=new int[1]();
//       
//        StringBuilder sb=new StringBuilder();
//        for(int i=0;i<ids.length;i++){
//            if(i==ids.length-1){
//                sb.append("'"+ids[i]+"'");
//            }else{
//            sb.append("'"+ids[i]+"'");
//            sb.append(",");}
//        }
//        Date now =new Date();
//        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String dat=sdf.format(now);
//        String sql="update t_zz_bzgl set yjzt = 1  where cpzsm in ( select cpzsm from v_zlzz_aqyj where id in ("+sb.toString()+" ) )";
//        String sql11="update t_zz_bzgl set yjsj = '"+dat+"' where cpzsm in ( select cpzsm from v_zlzz_aqyj where id in ("+sb.toString()+" ) )";
//        DatabaseHandlerDao.getInstance().executeSql(sql);
//        DatabaseHandlerDao.getInstance().executeSql(sql11);
//        String idcon=sb.toString();
//        String sql1="select * from v_zlzz_aqyj where id in ("+idcon+")";
//        List<Map<String,Object>> listmap=new ArrayList<Map<String,Object>>();
//        listmap=DatabaseHandlerDao.getInstance().queryForMaps(sql1);
//        List<Map<String,Object>> onmullistmap=new ArrayList<Map<String,Object>>();
//        boolean flag=false;
////        for(Iterator iter = listmap.iterator(); iter.hasNext();){
////           if(){}
////            Map<String,Object> mapiter=(Map<String,Object>)iter.next();
////            for(int on=0;on<onmullistmap.size();on++){
////                if(onmullistmap.get(on).get("DH")==mapiter.get("DH")){
////
////                }else{
////                    onmullistmap.
////                }
////            }
////        }
//        for(int i=0;i<listmap.size();i++){
//            int k=0;
//            for(int m=0;m<onmullistmap.size();m++){
//            	if(onmullistmap.size()==0){
//            		onmullistmap.add(listmap.get(i));
//            	}
//                if(onmullistmap.get(m).get("DH").equals(listmap.get(i).get("DH"))){
//                    flag=true;
//                    k=m;
//                    break;
//                }
//            }
//            if(flag){
//                onmullistmap.get(k).put("CPZSM",onmullistmap.get(k).get("CPZSM").toString()+","+listmap.get(i).get("CPZSM").toString());
////                onmullistmap.get(k).get("DH").toString()+","+listmap.get(i).get("DH").toString();
//                onmullistmap.get(k).put("CPMC",onmullistmap.get(k).get("CPMC").toString()+","+listmap.get(i).get("CPMC").toString());
//            }else{
//                onmullistmap.add(listmap.get(i));
//            }
//            flag=false;
//        }
////        for(int i=0;i<listmap.size();i++){
////            for(int k=i;k<listmap.size();k++){
////                if(listmap.get(i).get("DH").equals(listmap.get(k).get("DH"))){
//////                  onmullistmap.remove(k);
//////                  listmap.
//////                  String ox=listmap.get(i).get("DH").toString()+","+listmap.get(k).get("DH").toString();
////                    listmap.get(i).put("CPZSM",listmap.get(i).get("CPZSM").toString()+","+listmap.get(k).get("CPZSM").toString());
////                    listmap.get(i).put("CPMC",listmap.get(i).get("CPMC").toString()+","+listmap.get(k).get("CPMC").toString());
////                    listmap.remove(k);
////                    k--;
////                    i--;
////                }
////            }
////        }
////        for(int i=0;i<listmap.size();i++){
////            for(int k=0;k<i;k++){
////                listmap.get(i).get("DH").equals(listmap.get(k).get("DH"));
////            }
////        }
//        String nr;
//        for(int i=0;i<onmullistmap.size();i++){
//            StringBuilder sb1=new StringBuilder();
//            sb1.append(onmullistmap.get(i).get("KHMC"));
//            sb1.append(onmullistmap.get(i).get("LXR"));
//            sb1.append(",");
//            sb1.append(onmullistmap.get(i).get("QYMC"));
//            sb1.append("发现追溯码为");
//            sb1.append(onmullistmap.get(i).get("CPZSM"));
//            sb1.append(onmullistmap.get(i).get("CPMC"));
//            sb1.append("存在食品安全问题,请即刻下架，避免流向消费群体。请保留问题产品现状,我们会尽快联系您启动召回事宜。给您工作带来不便，敬请谅解,");
//            sb1.append(onmullistmap.get(i).get("QYMC"));
//            sb1.append(".");
//            sb1.append(dat);
//            nr=sb1.toString();
//            content[i]=nr;
//        }
//        for(int i=0;i<onmullistmap.size();i++){
//            dh[i]=onmullistmap.get(i).get("DH").toString();
//        }
//        final String[] content1=content;
//        final String[] dh1=dh;
//        
//        //发送短信
//        SendMessage app=new SendMessage(){
//        	public void doIt(){
//        		
////        		OutboundMessage[] msgarr;
//        		List<OutboundMessage> msgarr=new ArrayList<OutboundMessage>();
//        		OutboundMessage msg;
//        		OutboundNotification outboundNotification = new OutboundNotification();
//        		if(count==0){
//    				smsStart();
//    				count++;
//    			}
//        		
//                  
//        	
//        		
////        		System.out.println();
//        		System.out.println("设备信息:");
//        		// 发送短信
//        		for(int i=0;i< content1.length;i++){
//        			msg = new OutboundMessage(dh1[i], content1[i]);
//            		msg.setEncoding(OutboundMessage.MessageEncodings.ENCUCS2);
//            		msg.setStatusReport(true);
//            		msgarr.add(msg);
//        		}
//        		try {
//        			for(int i=0;i<msgarr.size();i++){
//        				srv.sendMessage(msgarr.get(i));
//        			}
//					
//				} catch (TimeoutException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (GatewayException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//	
//        		
//        	}
//        };
//        try {
//			app.doIt();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
//        return null;
    }

    public Map<String, Object> viewEarlyWarning(String cpzsm){
    	Map<String, Object> data = new HashMap<String, Object>();
    	String sql = "select * from T_ZL_YJXQ t where  t.CPZSM='"+cpzsm+"' order by t.yjsj desc";
    	 List<Map<String, Object>> list = DatabaseHandlerDao.getInstance().queryForMaps(sql);
    	 data.put("data", list);
    	 return data;
    }
    
}