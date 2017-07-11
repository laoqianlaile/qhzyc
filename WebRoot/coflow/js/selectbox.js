function showParticipantSelect(btn){
	var srcOptions = new Array();
	var objOptions = new Array();
	var xmlDoc = $(btn).data("xmlDoc");
	var ps = getParticipants(xmlDoc);
	if(ps != null){
		for(var i=0;i<ps.length;i++){
			srcOptions.push(ps[i].getAttribute("id"));
		}
	}
	var input = $(btn).data("input");
	var value = input.val();
	if(value != ""){
		objOptions = value.split(",");
	}
	resetOptions(srcOptions,objOptions);
	$("#dlg_selectBox").dialog({
		buttons: [
		          {text:'确定',handler:function(){
		        	  	var v = checkSelectedOption();
		        	  	input.val(v);
		        	  	$('#dlg_selectBox').dialog('close');
		        	  }
		          },
		          {text:'取消',handler:function(){
		        	  	$('#dlg_selectBox').dialog('close');
		        	  }
		          }
		         ],
		onBeforeOpen:function(){
			$(btn).data("showed",true);
		},
		onClose:function(){
			$(btn).data("showed",false);
		}
	});
	$("#dlg_selectBox").dialog("open");
}