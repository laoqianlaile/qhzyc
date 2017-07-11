<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/portal/jsp/common.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<utility:base target="easyui" themes="bootstrap"  packages="portal" />
	
	<style type="text/css">
		#portal {
			position: relative;
			overflow: hidden;
			padding: 0 0.5% 0.5% 0.5%;
		}
		
		.portal-item{height: 100%; padding: 0;}
		.portal-item-both {width: 30%;}
		.portal-item-center {width: 40%;}
		.portal-item-split {width: 0.5%;background-color: #fff;}
		
		.portal-panel {margin: 8px 0 0 0!important;}
		.portal-content {width: 100%;height: 100%;}
		
		.panel-title {
			font-size: 12px !important;
			font-weight: normal !important;
			margin-left: 5px !important;
		}
		
		.panel-heading {
			display: inline;
			padding: 8px 15px;
			border-top-left-radius: 2px;
			border-top-right-radius: 2px;
		}
	</style>
</head>
<body>
	<div id="portal">
		<div class="portal-item portal-item-both">
			<div title="运维人员动态" iconCls="icon-zico05">
				<table style="width: 100%;height: 100%;" >
					<tr align="center">
						<td>地址</td>
						<td>额定人员</td>
						<td>在岗人员</td>
					</tr>
					<tr align="center">
						<td>张江</td>
						<td>10</td>
						<td>9</td>
					</tr>
					<tr align="center">
						<td>华阳路</td>
						<td>7</td>
						<td>7</td>
					</tr>
					<tr align="center">
						<td>300号</td>
						<td>6</td>
						<td>6</td>
					</tr>
				
				</table>
			</div>
			<div title="故障工单统计" iconCls="icon-zico08" class="maximizable is-tabs">
			     <div title="故障工单" id="faultArea" class="portal-content" type="bar"></div>  
			</div>
		</div>
		<div class="portal-item portal-item-split"></div>
		<div class="portal-item portal-item-center">
			<div iconCls="icon-zico01">
				<div id="mainMap" class="portal-content"></div>
			</div>
			<div title="设备统计" iconCls="icon-zico04" class="maximizable">
				<div id="sourceArea" class="portal-content" type="bar"></div>
			</div>
		</div>
		<div class="portal-item portal-item-split"></div>
		<div class="portal-item portal-item-both"  >
			<div title="用户节点统计" iconCls="icon-zico05" class="maximizable">
				<div id="userNode" class="portal-content" type="force" style="width:100%;height: 100%;float: left;"></div>
				<!-- <div id="userAreaData" class="portal-content" type="force" style="width:40%;height: 100%;float: right;text-align: center;vertical-align: middle;">
						<table style="height: 80%;" >
							<tr style="height: 80px;"><td>&nbsp;</td><Td>&nbsp;</Td></tr>
							<tr >
								<td align="right">双向统一出口节点：</td>
								<td ><span class="userNodes"></span>个</td>
							</tr>
							<tr>
								<td align="right">单向统一出口节点：</td>
								<td ><span class="userNodes"></span>个</td>
							</tr>
							<tr>
								<td align="right">IDC托管节点：</td>
								<td><span class="userNodes"></span>个</td>
							</tr>
							<tr>
								<td align="right">加密入口节点：</td>
								<td ><span class="userNodes"></span>个</td>
							</tr>
						</table>
				</div> -->
			</div>
			<div title="告警统计" iconCls="icon-zico07" class="maximizable" >
				<div id="alarmArea" class="portal-content" type="bar"></div>
			</div>
			
		</div>
	</div>
	
	<div id="dialog"></div>
	
	<script type="text/javascript">
	
		// 面板最大化事件
		var $dialog;
		var element,func,type;
		function maxPanel(_func, _type){
			element="dialog",func=_func,type=_type;
			
			if(!$dialog){
				$dialog = $("#dialog").dialog({
					title:'&nbsp;',
					width:800,
					height:600,
					onOpen:function(){
						require(['echarts','echarts/chart/'+type],eval(func));
					}
				});
			}else{
				$dialog.dialog("open");
			}
		}
		
		$(function(){
			$('#portal').portal({
				fit: true,
				border: false,
				fixheight: 9
			});
			
			// 为面板添加最大化按钮
			$(".maximizable").each(function(i,n){
				var node = $(n).find("div.portal-content");
				var param = '"'+node.attr("id")+'","'+node.attr("type")+'"';
				$(n).prev("div.panel-header")
					.find("div.panel-tool")
					.append('<a class="panel-tool-max" href="javascript:void(0);" onclick=maxPanel('+param+');/>');
			});

	    	// 全网运行图
			require(['echarts','echarts/chart/map'],DrawEChart);
	    	// 资源统计
	        require(['echarts','echarts/chart/bar'],sourceArea);
	    	// 告警统计
	        require(['echarts','echarts/chart/bar'],alarmArea);
	    	// 用户节点统计
	    	require(['echarts','echarts/chart/pie'],userNode);
	    	//故障工单统计
	    	require(['echarts','echarts/chart/bar'],faultArea);
		});
	</script>
	<script src="${ctx}/resources/scripts/echarts/build/dist/echarts.js"></script>
	<script type="text/javascript">
		require.config({
            paths: {
                echarts: '${ctx}/resources/scripts/echarts/build/dist'
            }
        });
        var areaName = "all";
     	var areaTitle = "";
    </script>
    
    <!-- 为适应面板最大化时重绘图表，对下列方法进行改造 -->
    <script type="text/javascript"> 
		var myChart;
        function DrawEChart(ec) {
        	myChart = ec.init(document.getElementById('mainMap'));
        	
			require('echarts/util/mapData/params').params.上海 = {
			    getGeoJson: function (callback) {
			       $.getJSON('${ctx}/resources/scripts/echarts/build/sh.jsonp', callback);
			    }
			}  
			
			var ecConfig = require('echarts/config');
			myChart.on(ecConfig.EVENT.MAP_SELECTED, function (param){
			    areaTitle = param.target;
			    areaName = param.target;
			    // 全网运行图
				require(['echarts','echarts/chart/map'],DrawEChart);
			 	//设备统计
		 	  	require(['echarts','echarts/chart/bar'],sourceArea);
		    	// 告警统计
		        require(['echarts','echarts/chart/bar'],alarmArea);
		    	// 用户节点统计
		    	require(['echarts','echarts/chart/pie'],userNode);
		    	//故障工单统计
		    	require(['echarts','echarts/chart/bar'],faultArea);
				
			});
			var errorCountData = [];
			$.ajax({
				url:'${ctx}/current-alarm!netMapControl.json',		
				type:'POST',
				async:false,
				success:function(result){ 
					errorCountData = result.data;
				}
			});
			
			option = {
			    title: {
			        text : '全网运行图',
			        subtext : areaTitle
			    },
			    tooltip : {
			        trigger: 'item',
			        formatter: '{b}:{c}个'
			    },
			    dataRange: {
			        x : 'right',
			        orient : 'vertical' ,
			        min: 1,
			        max: 20,
			        color: ['red','yellow'],
			        calculable : true
			    }, 
			    roamController: {
			        show: true,
			        x: 'right',
			        mapTypeControl: {
			            '上海': true
			        }
			    },
			    toolbox: {
			        show : true,
			        orient: 'horizontal',
			        x: 'left',
			        y: 'bottom',
			        feature : {
			            dataView : {show: true, readOnly: false},
			            myTool : {
			                show : true,
			                title : '刷新',
			                icon : 'refresh.png',
			                onclick : function (){
			                	areaName = "all";
			                	areaTitle = "";
			                	// 全网运行图
								require(['echarts','echarts/chart/map'],DrawEChart);
								//设备统计
						 	  	require(['echarts','echarts/chart/bar'],sourceArea);
						    	// 告警统计
						        require(['echarts','echarts/chart/bar'],alarmArea);
						    	// 用户节点统计
						    	require(['echarts','echarts/chart/pie'],userNode);
						    	//故障工单统计
						    	require(['echarts','echarts/chart/bar'],faultArea);
			                }
			            },
			            saveAsImage : {show: true}
			           
			        }
			    },
			    calculable : true,
			    series : [
			        {
			            name: '全网运行图',
			            type: 'map',
			            mapType: '上海',
			            selectedMode : 'single',
			            itemStyle:{
			                normal:{label:{show:true},areaStyle:{color:'lightgreen'}},
			                emphasis:{label:{show:true}}
			            },
			            data:errorCountData
			        }
			    ]
			};
            myChart.setOption(option);
        }
    </script>
    <script type="text/javascript"> 
		var sourceAreaChart;
        function sourceArea(ec) {
        	sourceAreaChart = ec.init(document.getElementById(element || 'sourceArea'));
			sourceAreaChart.showLoading({
			    text : '努力加载数据中...',
			    textStyle : {
			        fontSize : 20
			    }
			});
			var sourceData = ['网络','安全','服务器','存储'];
			var normalData = new Array();
			var abnormalData = new Array();
			
			$.ajax({
				url:'${ctx}/statics!resourceStatics.json',		
				type:'POST',
				data:{'areaMessage':areaName},
				async:false,
				success:function(result){  
					var re = result.data;
					for(var i =0 ;i<re.length;i++){
						var datas = re[i];
						normalData = datas.goodCount;
						abnormalData = datas.errorCount;
					}
				}
			});
			sourceAreaOption = {
				title: {
			        text : areaTitle
			    },
			    tooltip : {
			        trigger: 'axis',
			        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
			            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
			        }
			    },
			    legend: {
			        data:['正常','异常']
			    },
			    toolbox: {
			        show : true,
			        orient: 'horizontal',
			        x: 'right',
			        y: 'top',
			        feature : {
			            magicType : {show: true, type: ['stack', 'tiled']},
			            myTool : {
			                show : true,
			                title : '刷新',
			                icon : 'refresh.png',
			                onclick : function (){
			                	sourceAreaChart.showLoading({
					    			text: '正在努力的读取数据中...',    
								});
			                   	sourceAreaChart.clear();
			                  	$.ajax({
									url:'${ctx}/statics!resourceStatics.json',		
									type:'POST',
									data:{'message':areaName},
									async:false,
									success:function(result){  
										var re = result.data;
										for(var i =0 ;i<re.length;i++){
											var datas = re[i];
											normalData = datas.goodCount;
											abnormalData = datas.errorCount;
										}
									}
								});
								sourceAreaChart.setOption(sourceAreaOption);
								sourceAreaChart.hideLoading();
			                }
			            },
			            saveAsImage : {show: true}
			        }
			    },
			    calculable : true,
			    xAxis : [
			        {
			            'type':'category',
               			'axisLabel':{'interval':0},
			            boundaryGap : true,
			            data : sourceData
			        }
			    ],
			    yAxis : [
			        {
			            type : 'value'
			        }
			    ],
			    series : [
			        {
			            name:'正常',
			            type:'bar',
			            stack: '堆积',
			            data:normalData,
			            barMinHeight : 5 ,
			            itemStyle: {
			                normal: {
			                    label: {
			                         show: true,
			                         position : 'inside',
			                         formatter : '{c}',
			                         textStyle: {
			                             color: '#000000'
			                         }
			                     }
			                }
			            }
			        },
			        {
			            name:'异常',
			            type:'bar',
			            stack: '堆积',
			            data:abnormalData,
			            barMinHeight : 5 ,
			            itemStyle: {
			                normal: {
			                    label: {
			                         show: true,
			                         position : 'inside',
			                         formatter : '{c}',
			                         textStyle: {
			                             color: '#000000'
			                         }
			                     }
			                }
			            }
			        }
			    ]
			};
			var ecConfig = require('echarts/config');
			function linkToMoudle(param) {
				if(confirm('确定要链接到查询列表界面？')){
					var type = $.trim(param.name)+"设备";
			    	var resourceStatus = param.seriesIndex; //资源状态  0:正常   1：异常  与数据库中是相反的
			    	var status = "";
			    	if(type=="网络设备"){
			    		if(resourceStatus=="0"){
				    		status = "true";
				    	}else {
				    	 	status = "false";
				    	}
			    	}else if(type=="安全设备"){
			    		if(resourceStatus=="0"){
				    		status = "1";
				    	}else {
				    	 	status = "0";
				    	}
			    	}
			    	var param = "{'Q_EQ_status':'"+status+"'}";
			    	var areaParam = "";
			    	if(areaName==""||areaName=="all"){
			    		areaParam = "全市";
			    	}else {
			    		areaParam= areaName;
			    	}
			    	
			        window.location.href="${ctx}/views/zwww/zygl/index.jsp?type="+type+"&area="+areaParam+"&param="+param;
				}
		    }
		    sourceAreaChart.on(ecConfig.EVENT.CLICK, linkToMoudle);
            sourceAreaChart.setOption(sourceAreaOption);
            sourceAreaChart.hideLoading();//取消loading
            
        }
    </script>
    <script type="text/javascript">  
		var alarmAreaChart;
        function alarmArea(ec) {
        	alarmAreaChart = ec.init(document.getElementById(element || 'alarmArea'));
			alarmAreaChart.showLoading({
    			text: '正在努力的读取数据中...',    //loading话术
			});
			var jjgjData = new Array();
			var zygjData = new Array();
			var cygjData = new Array();
			var jggjData = new Array();
			$.ajax({
				url:'${ctx}/statics!alarmStatics.json',		
				type:'POST',
				data:{'areaMessage':areaName},
				async:false,
				success:function(result){  
					var re = result.data;
					var netCount = re[0].netCount;
					var safeCount = re[0].safeCount;
					jjgjData.push(netCount[0]);
					zygjData.push(netCount[1]);
					cygjData.push(netCount[2]);
					jggjData.push(netCount[3]);
					jjgjData.push(safeCount[0]);
					zygjData.push(safeCount[1]);
					cygjData.push(safeCount[2]);
					jggjData.push(safeCount[3]);
				}
			});
			alarmAreaOption = {
				title: {
			        text : areaTitle
			    },
			    tooltip : {
			        trigger: 'axis',
			        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
			            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
			        }
			    },
			    legend: {
			        data:['紧急告警','重要告警','次要告警','警告告警'],
			        x:'center',
			        y:'bottom'
			        
			    },
			    toolbox: {
			        show : true,
			        orient: 'horizontal',
			        x: 'right',
			        y: 'top',
			        feature : {
			            magicType : {show: true, type: ['stack', 'tiled']},
			            myTool : {
			                show : true,
			                title : '刷新',
			                icon : 'refresh.png',
			                onclick : function (){
			                	alarmAreaChart.showLoading({
					    			text: '正在努力的读取数据中...',    //loading话术
								});
			                   	alarmAreaChart.clear();
			                   	$.ajax({
									url:'${ctx}/statics!alarmStatics.json',		
									type:'POST',
									data:{'areaMessage':areaName},
									async:false,
									success:function(result){  
										var re = result.data;
										var netCount = re[0].netCount;
										var safeCount = re[0].safeCount;
										jjgjData = new Array();
										zygjData = new Array();
										cygjData = new Array();
										jggjData = new Array();
										jjgjData.push(netCount[0]);
										zygjData.push(netCount[1]);
										cygjData.push(netCount[2]);
										jggjData.push(netCount[3]);
										jjgjData.push(safeCount[0]);
										zygjData.push(safeCount[1]);
										cygjData.push(safeCount[2]);
										jggjData.push(safeCount[3]);
									}
								});
								alarmAreaChart.setOption(alarmAreaOption);
								alarmAreaChart.hideLoading();
			                }
			            },
			            saveAsImage : {show: true}
			           
			        }
			    },
			    calculable : true,
			    xAxis : [
			        {
			            type : 'category',
			            'axisLabel':{'interval':0},
			            data : ['网络告警','安全告警']
			        }
			    ],
			    yAxis : [
			        {
			            type : 'value'
			        }
			    ],
			    series : [
			        {
			            name:'紧急告警',
			            type:'bar',
			            stack: '堆积',
			            data: jjgjData,
			            barMinHeight : 5 ,
			            itemStyle: {
			                normal: {
			                    label: {
			                         show: true,
			                         position : 'inside',
			                         formatter : '{c}',
			                         textStyle: {
			                             color: '#000000'
			                         }
			                     }
			                }
			            }
			        },
			        {
			            name:'重要告警',
			            type:'bar',
			            stack: '堆积',
			            data:zygjData,
			            barMinHeight : 5 ,
			            itemStyle: {
			                normal: {
			                    label: {
			                         show: true,
			                         position : 'inside',
			                         formatter : '{c}',
			                         textStyle: {
			                             color: '#000000'
			                         }
			                     }
			                }
			            }
			        },
			        {
			            name:'次要告警',
			            type:'bar',
			            stack: '堆积',
			            data:cygjData,
			            barMinHeight : 5 ,
			            itemStyle: {
			                normal: {
			                    label: {
			                         show: true,
			                         position : 'inside',
			                         formatter : '{c}',
			                         textStyle: {
			                             color: '#000000'
			                         }
			                     }
			                }
			            }
			        },
			        {
			            name:'警告告警',
			            type:'bar',
			            stack: '堆积',
			            data:jggjData,
			            barMinHeight : 5 ,
			            itemStyle: {
			                normal: {
			                    label: {
			                         show: true,
			                         position : 'inside',
			                         formatter : '{c}',
			                         textStyle: {
			                             color: '#000000'
			                         }
			                     }
			                }
			            }
			        }
			    ]
			};
			
			var ecConfig = require('echarts/config');
			
		    function linkToAlarm(param) {
				if(confirm('确定要链接到查询列表界面？')){
					var type = $.trim(param.name);
			    	var resourceStatus = param.seriesIndex; //资源状态  0:正常   1：异常  与数据库中是相反的
			    	
			    	var status = "";
			    	if(resourceStatus=="0"){
			    		status="1";
			    	}else if(resourceStatus=="1"){
			    		status="2";
			    	}else if(resourceStatus=="2"){
			    		status="3";
			    	}else if(resourceStatus=="3"){
			    		status="4";
			    	} 
			    	
			    	var param = "{'Q_EQ_severity':'"+status+"'}";
			    	var areaParam = "";
			    	if(areaName==""||areaName=="all"){
			    		areaParam = "全市";
			    	}else {
			    		areaParam= areaName;
			    	}
			    	
			        window.location.href="${ctx}/views/zwww/gjgl/index.jsp?type="+type+"&area="+areaParam+"&param="+param;
				}
		    }
		    alarmAreaChart.on(ecConfig.EVENT.CLICK, linkToAlarm);
            alarmAreaChart.setOption(alarmAreaOption);
            alarmAreaChart.hideLoading();
        } 
    </script>
   
    <script type="text/javascript">
		var faultAreaChart;
        function faultArea(ec) {
        	faultAreaChart = ec.init(document.getElementById('faultArea'));
			faultAreaChart.showLoading({
			    text : '努力加载数据中...',
			    textStyle : {
			        fontSize : 20
			    }
			});
			//var sourceData = new Array();
			//var newOrderData = new Array();
			//var runOrderData = new Array();
			//var unDoData = new Array();
			
			var countData = new Array();
			$.ajax({
				//url:'${ctx}/statics!errorWorkOrderStatics.json',		
				url :'${ctx}/statics!faultWorkOrderStatics.json',	 
				type:'POST',
				data:{/* 'toolMessage':"personal", */'areaMessage':areaName},
				async:false,
				success:function(result){  
					var re = result.data;
					for(var i =0 ;i<re.length;i++){
						var datas = re[i];
						countData = datas.countList;
						/* sourceData = datas.sourceData ;
						newOrderData =  datas.newCountData ;
						runOrderData = datas.runCountData ;
						unDoData = datas.unDoCountData ; */
					}
				}
			});
			faultAreaOption = {
				title : {
			        text: areaTitle,
			        subtext: '',
			        x:'left',
			        y:'top'
			    },
			    tooltip : {
			        trigger: 'axis',
			        formatter: '{b}\n{c}个'
			    },
			   /*  legend: {
			        data:['新增工单','处理中工单','完成工单'],
			        x:'left',
			        y:'top'
			    }, */
			    toolbox: {
			        show : true,
			        orient: 'horizontal' ,
			        x: 'right',
			        y: 'top',
			        feature : {
			        	/* weekTool : {
			        		show : true,
			                title : '近3周',
			                icon : 'dateType.png',
			                onclick : function (){
			                	faultAreaChart.showLoading({
								    text : '努力加载数据中...',
								    textStyle : {
								        fontSize : 20
								    }
								});
			                	$.ajax({
									url:'${ctx}/statics!errorWorkOrderStatics.json',		
									type:'POST',
									data:{'toolMessage':"week",'areaMessage':areaName},
									async:false,
									success:function(result){  
										var re = result.data;
										sourceData = [];
										newOrderData =  [];
										runOrderData = [];
										unDoData = [];
										faultAreaChart.clear();
										for(var i =0 ;i<re.length;i++){
											var datas = re[i];
											sourceData = datas.sourceData ;
											newOrderData =  datas.newCountData ;
											runOrderData = datas.runCountData ;
											unDoData = datas.unDoCountData ;
										}
										faultAreaOption.xAxis[0].data=sourceData;
										faultAreaOption.series[0].data=newOrderData;
										faultAreaOption.series[1].data=runOrderData;
										faultAreaOption.series[2].data=unDoData;
										faultAreaChart.setOption(faultAreaOption);
            							faultAreaChart.hideLoading();//取消loading
									}
								});
			                }
			        	},
			        	monthTool : {
			        		show : true,
			                title : '近3月',
			                icon : 'dateType.png',
			                onclick : function (){
			                	faultAreaChart.showLoading({
								    text : '努力加载数据中...',
								    textStyle : {
								        fontSize : 20
								    }
								});
			                	$.ajax({
									url:'${ctx}/statics!errorWorkOrderStatics.json',		
									type:'POST',
									data:{'toolMessage':"month",'areaMessage':areaName},
									async:false,
									success:function(result){  
										var re = result.data;
										sourceData = [];
										newOrderData =  [];
										runOrderData = [];
										unDoData = [];
										faultAreaChart.clear();
										for(var i =0 ;i<re.length;i++){
											var datas = re[i];
											sourceData = datas.sourceData ;
											newOrderData =  datas.newCountData ;
											runOrderData = datas.runCountData ;
											unDoData = datas.unDoCountData ;
										}
										faultAreaOption.xAxis[0].data=sourceData;
										faultAreaOption.series[0].data=newOrderData;
										faultAreaOption.series[1].data=runOrderData;
										faultAreaOption.series[2].data=unDoData;
										faultAreaChart.setOption(faultAreaOption);
            							faultAreaChart.hideLoading();//取消loading
									}
								});
			                }
			        	}, 
			            magicType : {show: true, type: ['stack', 'tiled']},*/
			            myTool : {
			                show : true,
			                title : '刷新',
			                icon : 'refresh.png',
			                onclick : function (){
			                	faultAreaChart.showLoading({
								    text : '努力加载数据中...',
								    textStyle : {
								        fontSize : 20
								    }
								});
			                	$.ajax({
									//url:'${ctx}/statics!errorWorkOrderStatics.json',		
									url :'${ctx}/statics!faultWorkOrderStatics.json',	 
									type:'POST',
									data:{/* 'toolMessage':"personal", */'areaMessage':areaName},
									async:false,
									success:function(result){  
										var re = result.data;
										/* sourceData = [];
										newOrderData =  [];
										runOrderData = [];
										unDoData = []; */
										countData= [];
										faultAreaChart.clear();
										for(var i =0 ;i<re.length;i++){
											var datas = re[i];
											countData = datas.countList;
											/* sourceData = datas.sourceData ;
											newOrderData =  datas.newCountData ;
											runOrderData = datas.runCountData ;
											unDoData = datas.unDoCountData ; */
										}
										//faultAreaOption.xAxis[0].data=sourceData;
										faultAreaOption.series[0].data=countData;
										//faultAreaOption.series[1].data=runOrderData;
										//faultAreaOption.series[2].data=unDoData;
										faultAreaChart.setOption(faultAreaOption);
            							faultAreaChart.hideLoading();//取消loading
									}
								});
			                }
			            },
			            saveAsImage : {show: true}
			        }
			    },
			    calculable : true,
			    xAxis : [
			        {
			            type : 'category',
			            'axisLabel':{'interval':0},
			            data : ['新增工单','处理中工单','今天完成工单']
			        }
			    ],
			    yAxis : [
			        {
			            type : 'value'
			        }
			    ],
			    series : [
			        {
			            type:'bar',
			            data: countData,
			            itemStyle: {
			                normal: {
			                    color: function(params) {
			                        // build a color map as your need.
			                        var colorList = [
			                          '#C1232B','#3CB371','#60C0DD','#FCCE10','#E87C25','#27727B',
			                           '#FE8463','#9BCA63','#FAD860','#F3A43B',
			                           '#D7504B','#C6E579','#F4E001','#F0805A','#26C0C0'
			                        ];
			                        return colorList[params.dataIndex]
			                    },
			                    label: {
			                        show: true,
			                        position: 'top',
			                        formatter: '{b}\n{c}个'
			                    }
			                }
			            }
			        }/* ,
			        {
			            name:'处理中工单',
			            type:'bar',
			            stack: '堆积',
			            data:runOrderData,
			            barMinHeight : 5 ,
			            itemStyle: {
			                normal: {
			                    label: {
			                         show: true,
			                         position : 'inside',
			                         formatter : '{c}',
			                         textStyle: {
			                             color: '#000000'
			                         }
			                     }
			                }
			            }
			        },
			        {
			            name:'完成工单',
			            type:'bar',
			            stack: '堆积',
			            data:unDoData,
			            barMinHeight : 5 ,
			            itemStyle: {
			                normal: {
			                    label: {
			                         show: true,
			                         position : 'inside',
			                         formatter : '{c}',
			                         textStyle: {
			                             color: '#000000'
			                         }
			                     }
			                }
			            }
			        } */
			    ]
			};
			
			var ecConfig = require('echarts/config');
		    function linkToWorkOrder(param) {
				if(confirm('确定要链接到查询列表界面？')){
					var type = $.trim(param.name);
			    	var resourceStatus = param.seriesIndex; //资源状态  0:正常   1：异常  与数据库中是相反的
			    	
			    	var param = "{'Q_EQ_severity':'"+resourceStatus+"'}";
			    	var areaParam = "";
			    	if(areaName==""||areaName=="all"){
			    		areaParam = "全市";
			    	}else {
			    		areaParam= areaName;
			    	}
			    	
			        window.location.href="${ctx}/views/zwww/gdgl/index.jsp?type="+type+"&area="+areaParam+"&param="+param;
				}
		    }
		    //faultAreaChart.on(ecConfig.EVENT.CLICK, linkToWorkOrder);
            faultAreaChart.setOption(faultAreaOption);
            faultAreaChart.hideLoading();//取消loading
        }
    </script>
   <script type="text/javascript">
   		//用户节点统计
		var userNodeChart;
        function userNode(ec) {
        	userNodeChart = ec.init(document.getElementById('userNode'));
			userNodeChart.showLoading({
			    text : '努力加载数据中...',
			    textStyle : {
			        fontSize : 20
			    }
			});
			
			var sourceData = ['市级政务外网','视频网','专网','双向互联网','单向互联网','张江云平台托管','300号云平台托管','加密延伸'];
			var countData = new Array();
			$.ajax({
				url:'${ctx}/statics!userNodeStatics.json',		
				type:'POST',
				data:{'areaMessage':areaName},
				async:false,
				success:function(result){  
					var datas = result.data;
					countData.push({'value':datas[0],'name':'市级政务外网'});
					countData.push({'value':datas[1],'name':'视频网'});
					countData.push({'value':datas[2],'name':'专网'});
					countData.push({'value':datas[3],'name':'双向互联网'});
					countData.push({'value':datas[4],'name':'单向互联网'});
					countData.push({'value':datas[5],'name':'张江云平台托管'});
					countData.push({'value':datas[6],'name':'300号云平台托管'});
					countData.push({'value':datas[7],'name':'加密延伸'});
					/* $(".userNodes").each(function(i,n){
						$(n).text(datas[i]);
					}); */
				}
			});
			option = {
				title : {
			        text: areaTitle,
			        subtext: '',
			        x:'left',
			        y:'bottom'
			    },
			    tooltip : {
			        trigger: 'item',
			        formatter: "{b} <br/> {c}个 <br/> {d}%"
			    },
			    legend: {
			    	show : true,
			        orient : 'horizontal' ,
			        y : 'top',
			        x : 'left',
			        data:sourceData
			    },
			   
			    toolbox: {
			        show : true,
			        x:'right',
			        y:'bottom',
			        feature : {
			        	dataView : {
					        show : true,
					        title : '详细数据',
					        readOnly: true
					    },
			            myTool : {
			                show : true,
			                title : '刷新',
			                icon : 'refresh.png',
			                onclick : function (){
			                	userNodeChart.showLoading({
					    			text: '正在努力的读取数据中...',    //loading话术
								});
			                   	userNodeChart.clear();
			                  	$.ajax({
									url:'${ctx}/statics!userNodeStatics.json',		
									type:'POST',
									data:{'areaMessage':areaName},
									async:false,
									success:function(result){  
										var datas = result.data;
										countData = new Array();
										countData.push({'value':datas[0],'name':'市级政务外网'});
										countData.push({'value':datas[1],'name':'视频网'});
										countData.push({'value':datas[2],'name':'专网'});
										countData.push({'value':datas[3],'name':'双向互联网'});
										countData.push({'value':datas[4],'name':'单向互联网'});
										countData.push({'value':datas[5],'name':'张江云平台托管'});
										countData.push({'value':datas[6],'name':'300号云平台托管'});
										countData.push({'value':datas[7],'name':'加密延伸'});
										/* $(".userNodes").each(function(i,n){
											$(n).text(datas[i]);
										}); */
										option.series[0].data = countData;
										userNodeChart.setOption(option);
										userNodeChart.hideLoading();
									}
								});
			                }
			            },
			            saveAsImage : {show: true}
			        }
			    },
			    calculable : true,
			    series : [
			        {
			        	name : '用户节点统计数据',
			            type:'pie',
			            radius : '60%',
			            center: ['50%', '60%'],
			            data:countData,
			            itemStyle:{ 
			            	normal:{ 
				                  label:{ 
				                    	show: true, 
				                      	formatter: '{c}个' ,
				                        position : 'inside',
				                        textStyle: {
				                            color: '#000000'
				                        } 
				                   } ,
				                   labelLine : {
				                        show : false
				                   }
			                } 
			            } 
			        }
			    ]
			};
            userNodeChart.setOption(option);
            userNodeChart.hideLoading();//取消loading
        }
   </script>
</body>
</html>