dtmlXMLLoaderObject.prototype.waitLoadFunction=function(dhtmlObject){
	var once = true;
	this.check=function (){
		if ((dhtmlObject)&&(dhtmlObject.onloadAction != null)){
			if (dhtmlObject.xmlDoc.readyState==4) {
				var sessionStatus = dhtmlObject.xmlDoc.getResponseHeader("session-status");
		        if (sessionStatus == 'timeout') {
		            var redirect = dhtmlObject.xmlDoc.getResponseHeader("location");
		            top.location = redirect;
		            return;
		        }
			}
			if ((!dhtmlObject.xmlDoc.readyState)||(dhtmlObject.xmlDoc.readyState == 4)){
				if (!once)
					return;

				once=false; //IE 5 fix
				dtmlXMLLoaderObject.count++;
				if (typeof dhtmlObject.onloadAction == "function")
					dhtmlObject.onloadAction(dhtmlObject.mainObject, null, null, null, dhtmlObject);

				if (dhtmlObject.waitCall){
					dhtmlObject.waitCall.call(this,dhtmlObject);
					dhtmlObject.waitCall=null;
				}
			}
		}
	};
	return this.check;
};