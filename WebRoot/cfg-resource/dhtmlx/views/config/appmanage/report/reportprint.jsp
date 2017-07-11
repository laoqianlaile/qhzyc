<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String dhxResPath = path + com.ces.config.dhtmlx.utils.DhtmlxCommonUtil.DHX_FOLDER;
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String iconPath = dhxResPath + "/views/config/appmanage/report";
String reportId=request.getParameter("P_reportId");
String tableId=request.getParameter("P_tableId");
String rowIds=request.getParameter("P_rowIds");
String columnFilter=request.getParameter("P_columnFilter");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">
		<title>CELL报表打印</title>
		<LINK rel=stylesheet type=text/css HREF="<%=dhxResPath %>/views/config/appmanage/report/control/olstyle.css">
		<script type="text/javascript">
			var basePath = "<%=basePath%>";
			var contextPath="<%=path%>";
			var iconPath = DHX_RES_PATH + "/views/config/appmanage/report/";
			var reportId = "<%=reportId%>";
			var cllPath  = "cll/" + reportId + ".cll";
		</script>
		<script src="<%=dhxResPath %>/common/js/base.js" type="text/javascript"></script>
		<script src="<%=dhxResPath %>/common/js/common.js" type="text/javascript"></script>
		<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/appcommon.js"></script>
		<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/AppActionURI.js"></script>
		<script type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/js/reportdefine.js"></script>
		<SCRIPT language="vbscript" src="<%=dhxResPath %>/views/config/appmanage/report/control/function.vbs"></SCRIPT>
		<SCRIPT type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/report/control/buttons.js"></SCRIPT>
		<script language="vbscript" src="<%=dhxResPath %>/views/config/appmanage/report/control/menuhandler.vbs"></script>
		<SCRIPT type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/report/control/reportprint.js"></SCRIPT>
	</head>
	<body scroll="no">
	<script type="text/javascript">
		<!-- 报表菜单控件 -->
		//writeCellMenu();
	</script>
	<!-- 报表菜单控件 -->
	<div id="reporttoolbar$1$div"></div>
	<div id="reporttoolbar$2$div"></div>
	<div id="reporttoolbar$3$div"></div>
	<div id="cellweb$div" style="float:left;">
	<script type="text/javascript">
		writeCellWeb();
	</script>
	</div>
	<div id="accordion$div" style="width:260px;height:400px;float:left;">
	</div>
	<SCRIPT type="text/javascript" src="<%=dhxResPath %>/views/config/appmanage/report/control/CellUtil.js"></SCRIPT>
	<script type="text/javascript">
		var fontNameCombo; // reportobject.js中使用的变量，不可以删除
		initReportToolbar();
		initObj();
		//initAccordion();
		//CellUtil.initBinded();
	   	var rdloader = dhtmlxAjax.getSync(AppActionURI.reportPrint + "!getReportDefine.json?P_reportId=<%=reportId%>");
		var rdloaderDoc = rdloader.xmlDoc.responseText;
		var rdJson = eval('(' + rdloaderDoc + ')');
		var rowSize=0;
		var inrows=CellUtil.cell.GetRows(0)-1;
		var incols=CellUtil.cell.GetCols(0)-1;
		var pageSize=inrows;
		if(rdJson.tailStart!=null){
			rowSize=rdJson.tailStart-rdJson.cycleStart;
		}else if(rdJson.lastStart!=null){
			rowSize=rdJson.lastStart-rdJson.cycleStart;
		}else{
			rowSize=CellUtil.cell.GetRows(0)-rdJson.cycleStart;
		}		
		var cycleSize=0;
		var lastRows=0;
		lastRows=rdJson.lastEnd-rdJson.lastStart+1;
		cycleSize=rdJson.cycleEnd-rdJson.cycleStart+1;  
	    var totalpage=getTotalPages(pageSize,cycleSize,rowSize);
	    CellUtil.cell.CopyRange(1, 1, incols,inrows);	    
		for (var i = 0; i < totalpage; i++) {
	    	CellUtil.cell.Paste(1, inrows*i+1, 0, 1, 0);
	    	if(i<totalpage-1){
		    	if(rdJson.lastEnd!=null || rdJson.lastStart!=null){
		    		CellUtil.cell.SetRowHidden(inrows*(i+1)-lastRows+1,inrows*(i+1));
		    	} 	    	
	    	}
	    }
	    var endRow=writData(pageSize,cycleSize,rowSize);
	    if(totalpage>1){
	 	 	CellUtil.cell.CopyRange(1,inrows*totalpage-lastRows+1,incols,inrows*totalpage);
			CellUtil.cell.SetRowHidden(inrows*totalpage-lastRows+1,inrows*totalpage);
			CellUtil.cell.Paste(1,endRow+1,0,1,0);  
	    }
		function writData(pageSize,cycleSize,rowSize){
			var url=AppActionURI.reportPrint + "!getReportData.json?P_reportId=<%=reportId%>&P_tableId=<%=tableId%>&P_rowIds=<%=rowIds%>&P_columnFilter=<%=columnFilter%>";
			var loader = dhtmlxAjax.getSync(url);
		    var loaderDoc = loader.xmlDoc.responseText;
		    var dataJson = eval('(' + loaderDoc + ')');
			var pageNo=0;
			var endRow=0;
			for(var i=0;i<dataJson.length;i++){
				var group=dataJson[i].group;
				var data=dataJson[i].data;
				if(null!=group){
			    	for(var j=0;j<group.length;j++){
			    		var val=group[j].val;
			    		var row=group[j].row+pageNo*pageSize;
			    		var col=group[j].col; 
			    		CellUtil.cell.S(col, row, 0, val);
			    	}
				}
		    	for(var k=0;k<data.length;k++){
		    		var cell=data[k];
		    		if((cycleSize*k)%rowSize==0){
		    			pageNo++;
		    			if(null!=group){
			    			for(var j=0;j<group.length;j++){
					    		var val=group[j].val;
					    		var row=group[j].row+pageNo*pageSize;
					    		var col=group[j].col; 
					    		CellUtil.cell.S(col, row, 0, val);
					    	}
		    			}
		    		}
		    		for(var m=0;m<cell.length;m++){
		    			var val=cell[m].val;
			    		var row=cell[m].row;
			    		var col=cell[m].col; 
			    		var newrow=0;
			    		newrow=(pageNo-1)*pageSize+(cycleSize*k)%rowSize+row;
			    		endRow=newrow;
			    		CellUtil.cell.S(col, newrow, 0, val);
		    		}		    		
		    	}	
			}
			return endRow;
		}
		function getTotalPages(pageSize,cycleSize,rowSize){
			var url=AppActionURI.reportPrint + "!getReportData.json?P_reportId=<%=reportId%>&P_tableId=<%=tableId%>&P_rowIds=<%=rowIds%>&P_columnFilter=<%=columnFilter%>";
			var loader = dhtmlxAjax.getSync(url);
		    var loaderDoc = loader.xmlDoc.responseText;
		    var dataJson = eval('(' + loaderDoc + ')');
			var pageNo=0;
			for(var i=0;i<dataJson.length;i++){
				var data=dataJson[i].data;
		    	for(var k=0;k<data.length;k++){
		    		var cell=data[k];
		    		if((cycleSize*k)%rowSize==0){
		    			pageNo++;
		    		}	    		
		    	}	
			}
			return pageNo;
		}		
	</script>
	</body>
</html>
