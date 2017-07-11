<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String dhxResPath = path + com.ces.config.dhtmlx.utils.DhtmlxCommonUtil.DHX_FOLDER;
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>工具条更新</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
  </head>
  <body>
	<script type="text/javascript">
		MODEL_URL = "<%=path%>/toolmanage/tool-manage";
		var dhxLayout = new dhtmlXLayoutObject("content", "1C");
		dhxLayout.cells("a").hideHeader();
		dhxLayout.cells("a").detachToolbar();
		var formJson = {
				format: [
		   		    {type: "block", width: 800, offsetTop: 20, list:[
	                    {type: "itemlabel", label: "将“表单工具条”改成“表单_上工具条”（历史数据更改）", labelWidth: 420},
	                    {type:"newcolumn"},
	                    {type: "button", name: "update1", value: "修改工具条", width: 100}
		    		]},
		    		{type: "block", width: 800, list:[
   	                    {type: "itemlabel", label: "将“表单工具条”改成“表单_下工具条”（历史数据更改）", labelWidth: 420},
   	                    {type:"newcolumn"},
   	                    {type: "button", name: "update2", value: "修改工具条", width: 100}
   		    		]},
   		    		{type: "block", width: 800, list:[
   	                    {type: "itemlabel", label: "生成所有的“表单_上工具条”（历史数据更改）", labelWidth: 420},
   	                    {type:"newcolumn"},
   	                    {type: "button", name: "update3", value: "新增工具条", width: 100}
   		    		]},
   		    		{type: "block", width: 800, list:[
   	                    {type: "itemlabel", label: "生成所有的“表单_下工具条”（历史数据更改）", labelWidth: 420},
   	                    {type:"newcolumn"},
   	                    {type: "button", name: "update4", value: "新增工具条", width: 100}
   		    		]},
   		    		{type: "block", width: 800, list:[
   	                    {type: "itemlabel", label: "将所有“表单_上工具条”上的按钮复制到“表单_下工具条”", labelWidth: 420},
   	                    {type:"newcolumn"},
   	                    {type: "button", name: "update5", value: "复制按钮", width: 100}
   		    		]},
   		    		{type: "block", width: 800, list:[
   	                    {type: "itemlabel", label: "将所有“表单_下工具条”上的按钮复制到“表单_上工具条”", labelWidth: 420},
   	                    {type:"newcolumn"},
   	                    {type: "button", name: "update6", value: "复制按钮", width: 100}
   		    		]},
   		    		{type: "block", width: 800, list:[
   	                    {type: "itemlabel", label: "删除所有“表单_上工具条”上的按钮", labelWidth: 420},
   	                    {type:"newcolumn"},
   	                    {type: "button", name: "delete1", value: "删除按钮", width: 100}
   		    		]},
   		    		{type: "block", width: 800, list:[
  	                    {type: "itemlabel", label: "删除所有“表单_下工具条”上的按钮", labelWidth: 420},
  	                    {type:"newcolumn"},
  	                    {type: "button", name: "delete2", value: "删除按钮", width: 100}
  		    		]},
   		    		{type: "block", width: 800, list:[
  	                    {type: "itemlabel", label: "将构件组装模块中所有“新增”和“修改”按钮的“页面组装类型”设置为“弹出”", labelWidth: 420},
  	                    {type:"newcolumn"},
  	                    {type: "button", name: "update7", value: "表单弹出设置", width: 100}
  		    		]},
  		    		{type: "block", width: 800, list:[
 	                    {type: "itemlabel", label: "将构件组装模块中所有“新增”和“修改”按钮的“页面组装类型”设置为“嵌入”", labelWidth: 420},
 	                    {type:"newcolumn"},
 	                    {type: "button", name: "update8", value: "表单嵌入设置", width: 100}
 		    		]}
		        ],
				settings: {labelWidth: 80, inputWidth: 160}
			};
		var form = dhxLayout.cells("a").attachForm(initFormFormat(formJson));
		form.attachEvent("onButtonClick", function(id) {
		    if (id == "update1") {
		        var result = eval("(" + loadJson(MODEL_URL + "!changeFormTbToTopTb.json") + ")");
	        	if (result.msg) {
	        		dhtmlx.message(result.msg);
	        	}
		    } else if (id == "update2") {
		        var result = eval("(" + loadJson(MODEL_URL + "!changeFormTbToBottomTb.json") + ")");
	        	if (result.msg) {
	        		dhtmlx.message(result.msg);
	        	}
		    } else if (id == "update3") {
		        var result = eval("(" + loadJson(MODEL_URL + "!createTopTb.json") + ")");
	        	if (result.msg) {
	        		dhtmlx.message(result.msg);
	        	}
		    } else if (id == "update4") {
		        var result = eval("(" + loadJson(MODEL_URL + "!createBottomTb.json") + ")");
	        	if (result.msg) {
	        		dhtmlx.message(result.msg);
	        	}
		    } else if (id == "update5") {
		        dhtmlx.confirm({
		            type:"confirm",
		            text: "该操作将先删除所有“表单_下工具条”上的按钮，确定吗？",
		            ok: "确定",
		            cancel: "取消",
		            callback: function(flag) {
		                if (flag) {
		                    var result = eval("(" + loadJson(MODEL_URL + "!copyButtonFormTopTb.json") + ")");
		    	        	if (result.msg) {
		    	        		dhtmlx.message(result.msg);
		    	        	}
		                }
		            }
		        });
		    } else if (id == "update6") {
		        dhtmlx.confirm({
		            type:"confirm",
		            text: "该操作将先删除所有“表单_上工具条”上的按钮，确定吗？",
		            ok: "确定",
		            cancel: "取消",
		            callback: function(flag) {
		                if (flag) {
		                    var result = eval("(" + loadJson(MODEL_URL + "!copyButtonFormBottomTb.json") + ")");
		    	        	if (result.msg) {
		    	        		dhtmlx.message(result.msg);
		    	        	}
		                }
		            }
		        });
		    } else if (id == "delete1") {
		        dhtmlx.confirm({
		            type:"confirm",
		            text: "将删除所有“表单_上工具条”上的按钮，确定吗？",
		            ok: "确定",
		            cancel: "取消",
		            callback: function(flag) {
		                if (flag) {
		                    var result = eval("(" + loadJson(MODEL_URL + "!deleteButtonOfTopTb.json") + ")");
		    	        	if (result.msg) {
		    	        		dhtmlx.message(result.msg);
		    	        	}
		                }
		            }
		        });
		    } else if (id == "delete2") {
		        dhtmlx.confirm({
		            type:"confirm",
		            text: "将删除所有“表单_下工具条”上的按钮，确定吗？",
		            ok: "确定",
		            cancel: "取消",
		            callback: function(flag) {
		                if (flag) {
		                    var result = eval("(" + loadJson(MODEL_URL + "!deleteButtonOfBottomTb.json") + ")");
		    	        	if (result.msg) {
		    	        		dhtmlx.message(result.msg);
		    	        	}
		                }
		            }
		        });
		    } else if (id == "update7") {
		        var result = eval("(" + loadJson(MODEL_URL + "!setFormAssembleType.json?assembleType=0") + ")");
	        	if (result.msg) {
	        		dhtmlx.message(result.msg);
	        	}
		    } else if (id == "update8") {
		        var result = eval("(" + loadJson(MODEL_URL + "!setFormAssembleType.json?assembleType=1") + ")");
	        	if (result.msg) {
	        		dhtmlx.message(result.msg);
	        	}
		    }
		});
	</script>
  </body>
</html>
