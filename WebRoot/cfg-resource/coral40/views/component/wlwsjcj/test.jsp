<%@ page
	import="com.ces.config.utils.CommonUtil,com.ces.component.trace.utils.JSON,com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao,com.ces.config.utils.DateUtil,com.ces.config.utils.UUIDGenerator,com.ces.component.wlwsjcj.enitty.WlwsjBean"%>
<%
	DatabaseHandlerDao baseDao = DatabaseHandlerDao.getInstance();
	String hodliaPostType = request.getParameter("hodlia_posttype");
	if ("1".equalsIgnoreCase(hodliaPostType)) {
		String protocol = request.getParameter("protocol");
		protocol = "{gatewaySN:555,nodeSN:556,sensorData:[{channel:1,val:23.56},{channel:2,val:23.56}]}";

		protocol = protocol.replaceAll("gatewaySN", "\"gatewaySN\"");
		protocol = protocol.replaceAll("nodeSN", "\"nodeSN\"");
		protocol = protocol.replaceAll("sensorData", "\"sensorData\"");
		protocol = protocol.replaceAll("channel", "\"channel\"");
		protocol = protocol.replaceAll("val", "\"val\"");
		if (protocol != null && protocol.length() > 0) {
			WlwsjBean wlwsjBean = JSON.fromJSON(protocol,
					WlwsjBean.class);
			if (wlwsjBean != null) {
				StringBuffer sqlBuff = new StringBuffer(
						"insert  into T_ZZ_WLWCGQSJ(ID,SBSBH,JCSJ,DQWD,DQSD,TRWS,TRWD,GZQD,EYHTND) values(");
				sqlBuff.append("'" + UUIDGenerator.uuid() + "'");
				sqlBuff.append(",'" + wlwsjBean.getNodeSN() + "'");
				sqlBuff.append(",'" + DateUtil.currentTime() + "'");
				sqlBuff.append(",'" + wlwsjBean.getVarByChannel("1")
						+ "'");
				sqlBuff.append(",'" + wlwsjBean.getVarByChannel("2")
						+ "'");
				sqlBuff.append(",'" + wlwsjBean.getVarByChannel("5")
						+ "'");
				sqlBuff.append(",'" + wlwsjBean.getVarByChannel("6")
						+ "'");
				sqlBuff.append(",'" + wlwsjBean.getVarByChannel("7")
						+ "'");
				sqlBuff.append(",'" + wlwsjBean.getVarByChannel("8")
						+ "'");
				baseDao.executeSql(sqlBuff.toString());

			}
		}
	}
	System.out.println("ddd");
%>
