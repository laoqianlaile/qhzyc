package com.ces.component.wlwsjcj.enitty;

import java.util.List;

import com.ces.component.trace.utils.JSON;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;

public class WlwsjBean {
	private String gatewaySN;
	private String nodeSN;
	private List<WlwChannelValue> sensorData;
	DatabaseHandlerDao dao = null;
	

	public WlwsjBean() {
		super();
	}

	public String getVarByChannel(String channel) {
		String val = "0";
		for (int i = 0; i < sensorData.size(); i++) {
			if (channel.equalsIgnoreCase(sensorData.get(i).getChannel())) {
				val = sensorData.get(i).getVal();
			}
		}
		return val;
	}

	public String getGatewaySN() {
		return gatewaySN;
	}

	public void setGatewaySN(String gatewaySN) {
		this.gatewaySN = gatewaySN;
	}

	public String getNodeSN() {
		return nodeSN;
	}

	public void setNodeSN(String nodeSN) {
		this.nodeSN = nodeSN;
	}

	public List<WlwChannelValue> getSensorData() {
		return sensorData;
	}

	public void setSendsorData(List<WlwChannelValue> sensorData) {
		this.sensorData = sensorData;
	}

	public static void main(String[] args) {
		String jsonContent = "{gatewaySN:555,nodeSN:556,sensorData:[{channel:1,val:23.56},{channel:2,val:23.56}]}";

		jsonContent = jsonContent.replaceAll("gatewaySN", "\"gatewaySN\"");
		jsonContent = jsonContent.replaceAll("nodeSN", "\"nodeSN\"");
		jsonContent = jsonContent.replaceAll("sensorData", "\"sensorData\"");
		jsonContent = jsonContent.replaceAll("channel", "\"channel\"");
		jsonContent = jsonContent.replaceAll("val", "\"val\"");
		WlwsjBean sjBean = JSON.fromJSON(jsonContent, WlwsjBean.class);
		System.out.println(sjBean.getSensorData().size());
	}
}
