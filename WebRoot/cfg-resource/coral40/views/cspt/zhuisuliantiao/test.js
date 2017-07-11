$(function(){
	var jhpch=$("#jhpch").val();
	var jypzh = $("#jypzh").val();
	var url = $.contextPath + "/zhuisuliantiao!getZxZhuisuList.json?jhpch="+jhpch;
	if(jypzh.length>0){
		url+="&jypzh="+jypzh;
	}
	var $grid = $("#gridDemo1"),
	_colModel=[
	    {name:"ID",width:100,hidden:true},
		{name:"JHPCH", align:"center",width:100},
		{name:"ZSM", align:"center",width:100},
		{name:"QYMC", align:"center",width:100},
		{name:"JYZMC", align:"center",width:100},
		{name:"CZ",width:100, align:"center",formatter:function(value,rec,rowObject){
			var operate ="";
			$.ajax({
				type:"post",
				url:$.contextPath + "/zhuisuliantiao!isEndNode.json?jhpch="+rowObject.JHPCH+"&jypzh="+rowObject.ZSM,
				async:false,
				success:function(data){
					if(!data){
						operate =  "<a href='javascript:void(0)' style='color:blue' onclick='turnTo("+JSON.stringify(rowObject)+")'>追查</a>";
					}
				}
			})
			return operate;
		}}
	],
	_colNames=["主键","进货批次号","追溯码","企业名称","经营者名称","操作"],
	_setting={
		type:"post",
		url:url,
		width:"auto",
		height:"250",
		fitStyle:"width",
		datatype:"json",
		colModel:_colModel,
		colNames:_colNames
	};
	$grid.grid(_setting);
});

function turnTo(row){
	window.location.href=$.contextPath + "/cfg-resource/coral40/views/cspt/zhuisuliantiao/zhuisuliuchengTest.jsp?jhpch="+row.JHPCH+"&jypzh="+row.ZSM;
}