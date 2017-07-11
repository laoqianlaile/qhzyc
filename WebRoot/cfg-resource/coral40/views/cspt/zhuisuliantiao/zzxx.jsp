<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script type="text/javascript">
<!--

//-->
$(function(){
  	var $grid = $("#gridDemo1"),
	_colModel=[
	    {name:"ID",width:100,hidden:true},
		{name:"SYFL", align:"center",width:20},
		{name:"PL", align:"center",width:20},
		{name:"DCYL", align:"center",width:20},
		{name:"FZR", align:"center",width:20},
		{name:"KSRQ", align:"center",width:20},
		{name:"JSRQ", align:"center",width:20}
		],
	_colNames=["ID","使用肥料","频率（次/天） ","单次用量（克/亩）","负责人","开始日期","结束日期"],
	_setting={
		type:"post",
		url:$.contextPath + "/zhuisuliantiao!getZzSfxxList.json?zzpch=${map.zzCcxx['ZZPCH']}",
		width:"200",
		height:"250",
		fitStyle:"width",
		datatype:"json",
		colModel:_colModel,
		colNames:_colNames
	};
	$grid.grid(_setting);
	var $grid = $("#gridDemo2"),
	_colModel=[
	    {name:"ID",width:100,hidden:true},
		{name:"SYNY", align:"center",width:20},
		{name:"PL", align:"center",width:20},
		{name:"DCYL", align:"center",width:20},
		{name:"FZR", align:"center",width:20},
		{name:"KSRQ", align:"center",width:20},
		{name:"JSRQ", align:"center",width:20},
		{name:"YWCLQX", align:"center",width:20}
		],
	_colNames=["ID","使用农药","频率（次/天） ","单次用量（克/亩）","负责人","开始日期","结束日期","药物残留期限"],
	_setting={
		type:"post",
		url:$.contextPath + "/zhuisuliantiao!getZzSyxxList.json?zzpch=${map.zzCcxx['ZZPCH']}",
		width:"200",
		height:"250",
		fitStyle:"width",
		datatype:"json",
		colModel:_colModel,
		colNames:_colNames
	};
	$grid.grid(_setting);
});
</script>

