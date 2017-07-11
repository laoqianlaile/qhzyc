// JavaScript Document

var distance;
var n=1;
var length;
$(window).load(function(){
     length =$(".itemsbox .items").length;
	 getDistance();  
});  
$(window).resize(function(){  
	 getDistance();
}); 
function getDistance(){
	distance=$(".middlebox .itemsbox").css("width");
	distance=distance.substring(0,distance.length-2);
	distance=-(parseInt(distance)/3);//modified by Synge @20160225 : 最多滚动3屏
	//distance=distance+'px';
	if(n!=1){
		$(".itemsbox").css("margin-left",distance);
	}
}

$(".system-select #next-arrow").click(function(){
    if(n<length){
	    $(".itemsbox").animate({"margin-left":distance*(parseInt(n))+"px"},600);
		n+=1;
	}	
});
$(".system-select #back-arrow").click(function(){
    if(n>1){
	    $(".itemsbox").animate({"margin-left":distance*(parseInt(n)-2<0?0:parseInt(n)-2)+"px"},600);
		n-=1;
	}
});