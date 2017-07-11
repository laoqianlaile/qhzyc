
function GetDateStr(AddDayCount){
	var dd = new Date();
	dd.setDate(dd.getDate()+AddDayCount);
	var y = dd.getFullYear();
	var m = dd.getMonth()+1;
	var d = dd.getDate();
	return y+"-"+m+"-"+d;
} 
	var myDate = new Date();
	var nextDay = GetDateStr(-1);
	var currentData = GetDateStr(+1);
function initTimeListDiv(tabbar) {
	var div = document.getElementById("tab01$from");
	var height = document.body.offsetHeight-60;
	var width = document.body.offsetWidth;
	if(div!=null){
		div.innerHTML="";
	}
	var dhxLayout = tabbar.cells("timing$01").attachLayout("1C");
	initLayout(dhxLayout);	
	dhxLayout.cells("a").setText("定时任务详情列表");
    dhxLayout.cells("a").attachObject(div);	
	var JsonObj = loadJson(contextPath + "/appmanage/timing!getTimingTasks.json");	
	var div_out = document.getElementById("DIV_TIME_ID_OUT");
	if(null == div_out){
		div_out = document.createElement("DIV");
		div_out.setAttribute("id", "DIV_TIME_ID_OUT");
		div_out.setAttribute("style", "font-family: Tahoma;height:"+height+"px;width:"+width+"px;OVERFLOW-Y: auto;OVERFLOW-X: hidden;");
	}
	document.getElementById("tab01$from").appendChild(div_out);
	
	for(var i = 0;i<JsonObj.length;i++){
		var Name =  JsonObj[i][1];
		var Timing =  JsonObj[i][2];
		var Stats =  JsonObj[i][3];
		var type =  JsonObj[i][4];
		var id =  JsonObj[i][0];
		
		var cuTime = currentData+" "+Timing;
    	var nTime = nextDay + " "+Timing;		    	
		    	
		var operName;
		if(Stats=="1"){
			operName = "停止";
		}else if(Stats=="0"){
			operName = "启动";
		}
		createTableHelpDiv(Name,Timing,operName,type,i,id,Stats,cuTime,nTime);
	}	
	return dhxLayout;
}

function createTableHelpDiv(Name,Timing,operName,type,i,id,Stats,cuTime,nTime) {
	var divObj = document.getElementById("DIV_TIME_ID_"+i);
	if (null == divObj) {
		divObj = document.createElement("DIV");
		divObj.setAttribute("id", "DIV_TIME_ID_"+i);
		divObj.setAttribute("style", "width:700px;font-family: Tahoma;");

		divObj.innerHTML = "\n"
		+ "<fieldset>"
			+ "<table>"
				+ "	<tr>"
				+ "	<td width=\"120\" align=\"rihgt\"><div  style=\"font-size: 13px;\" class=\"dhxform_label dhxform_label_align_right\">任务名称：</td>"
				+ "	<td width=\"180\" align=\"left\"><div  style=\"font-size: 13px;\" class=\"dhxform_label dhxform_label_align_left\">"+Name+"</td>"				
				+ "	<td width=\"120\" align=\"rihgt\"><div  style=\"font-size: 13px;\" class=\"dhxform_label dhxform_label_align_right\">操作：</td>"
				+ "	<td width=\"180\" align=\"left\"><div  style=\"font-size: 13px;\" class=\"dhxform_label dhxform_label_align_left\">" +
				"		<div id=\""+id+"\">" +
							"<a href=\"javascript:Operates("+Stats+","+type+",'"+id+"')\">"+operName+
							"</a>" +
						"</div>"+
				"	</td>"		
				+ "	</tr>"
				+ "	<tr>"
				+ " <td width=\"120\" align=\"rihgt\"><div  style=\"font-size: 13px;\" class=\"dhxform_label dhxform_label_align_right\">定时时间：</td>"
				+ "	<td width=\"180\" align=\"left\"><div  style=\"font-size: 13px;\" class=\"dhxform_label dhxform_label_align_left\">"+Timing+"</td>"				
				+ " <td width=\"120\" align=\"rihgt\"><div  style=\"font-size: 13px;\" class=\"dhxform_label dhxform_label_align_right\">下次运行时间：</td>"
				+ "	<td width=\"180\" align=\"left\"><div  style=\"font-size: 13px;\" class=\"dhxform_label dhxform_label_align_left\">"+cuTime+"</td>"		
				+ "	</tr>"
				+ "	<tr>"
				+ " <td width=\"120\" align=\"rihgt\"><div  style=\"font-size: 13px;\" class=\"dhxform_label dhxform_label_align_right\">上次运行时间：</td>"
				+ "	<td width=\"180\" align=\"left\"><div  style=\"font-size: 13px;\" class=\"dhxform_label dhxform_label_align_left\">"+nTime+"</td>"
				+ " <td width=\"120\" align=\"rihgt\"><div  style=\"font-size: 13px;\" class=\"dhxform_label dhxform_label_align_right\">运行明细：</td>"
				+ "	<td width=\"180\" align=\"left\"><div  style=\"font-size: 13px;\" class=\"dhxform_label dhxform_label_align_left\">"+
				"		<div id=\""+id+"\">" +
							"<a href=\"javascript:taskDetail("+Stats+","+type+",'"+id+"')\">"+"显示"+
							"</a>" +
						"</div>"+
				"	</td>"	
				+ "	</tr>"
			+ " </table>"
		+ "</fieldset>"
			+ "\n";
		
		document.getElementById("DIV_TIME_ID_OUT").appendChild(divObj);
	}
}


