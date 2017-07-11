// JavaScript Document
function extra(x){
	if(x < 10)  
	{  
		return "0" + x;  
	}  
	else  
	{  
		return x;  
	}  
}
function timeShow(){
	var date=new Date();
	var hour=date.getHours();
	var minute=date.getMinutes();
	var second=date.getSeconds();
	hour=extra(hour);
	minute=extra(minute);
	second=extra(second);
	document.getElementById("hour").innerHTML=hour;
	document.getElementById("minute").innerHTML=minute;
	document.getElementById("second").innerHTML=second;	
}
timeShow();
setInterval("timeShow()",1000);

var date1=new Date();
var week;
if(date1.getDay()==0){week="周日";}
else if(date1.getDay()==1){week="周一";}
else if(date1.getDay()==2){week="周二";}
else if(date1.getDay()==3){week="周三";}
else if(date1.getDay()==4){week="周四";}
else if(date1.getDay()==5){week="周五";}
else{week="周六";}
var year=date1.getFullYear();
var month=date1.getMonth()+1;
var dt=date1.getDate();
month=extra(month);
dt=extra(dt);
document.getElementById("date").innerHTML=week+" "+year+"年"+month+"月"+dt+"日";


$("#card-number input").focus(function(){
	$(this).attr("value","");
}).blur(function(){
	if($(this).attr("value")==""){
		$(this).attr("value","请输入卡号");
	}
});