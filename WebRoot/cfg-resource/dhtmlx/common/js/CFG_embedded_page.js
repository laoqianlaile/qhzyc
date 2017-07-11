/**
 * 打开嵌套构件
 * @param url 嵌套构件url
 * @param text 导航条目名称
 */
function CFG_openComponent(url, text) {
	var componentZoneDiv = document.getElementById("CFG_componentZoneDiv");
	if (componentZoneDiv == null) {
		componentZoneDiv = document.createElement("div");
		componentZoneDiv.setAttribute("id", "CFG_componentZoneDiv");
		componentZoneDiv.className = "component_zone";
		document.body.appendChild(componentZoneDiv);
		if (window.addEventListener) {
			window.addEventListener("resize", function(){
				CFG_componentZoneResize();
			});
		} else {
			window.attachEvent("onresize", function(){
				CFG_componentZoneResize();
			});
		}
		CFG_componentZoneResize();
		var ifrm = document.createElement("iframe");
		ifrm.setAttribute("id", "CFG_componentZoneIfrm");
		ifrm.setAttribute("name", "CFG_componentZoneIfrm");
		ifrm.setAttribute("src", url);
		ifrm.setAttribute("width", "100%");
		ifrm.setAttribute("height", "100%");
		ifrm.setAttribute("border", "0");
		ifrm.setAttribute("marginwidth", "0");
		ifrm.setAttribute("marginheight", "0");
		ifrm.setAttribute("scrolling", "no");
		ifrm.setAttribute("frameborder", "0", 0);
		componentZoneDiv.appendChild(ifrm);
		if (window.CFG_NVG_addItem) {
			window.CFG_NVG_pos = CFG_NVG_addItem(text);
		}
	} else {
		var childs = componentZoneDiv.childNodes;
		for (var i in childs) {
			if (childs[i].tagName == 'IFRAME') {
				childs[i].src = url;
				break;
			}
		}
		if (window.CFG_NVG_setText) {
			CFG_NVG_setText(window.CFG_NVG_pos, text);
		}
	}
	
	// 添加监听对象，用于调用回调函数
	var obj = new Object();
	obj.contentWindow = window;
	dhtmlxEventable(obj);
	window.eventableObj = obj;
	return obj;
}
/**
 * 嵌套构件区域调整大小
 */
function CFG_componentZoneResize() {
	var componentZoneDiv = document.getElementById("CFG_componentZoneDiv");
	if (!componentZoneDiv)
		return;
	if (window.CFG_reserveZone == undefined) {
		CFG_reserveZone = document.getElementById("CFG_reserveZone");
		if (CFG_reserveZone == null) {
			CFG_reserveZone = document.body;
		}
	}
	if (window.CFG_hasLayoutToolbar) {
		componentZoneDiv.style.top = (CFG_reserveZone.offsetTop + 29) + "px";
	} else {
		componentZoneDiv.style.top = CFG_reserveZone.offsetTop + "px";
	}
	componentZoneDiv.style.left = CFG_reserveZone.offsetLeft + "px";
	componentZoneDiv.style.width = CFG_reserveZone.offsetWidth + "px";
	componentZoneDiv.style.height = CFG_reserveZone.offsetHeight + "px";
}
/**
 * 关闭打开的构件，返回父构件window
 * @param win 父构件Window
 */
function CFG_returnToMainComponent(win) {
	// flag为true表示点击导航条触发的，false表示点击返回按钮触发的
	var flag = false;
	if (win) {
		flag = true;
	}
	win = win || window.parent;
	var componentZoneDiv = win.document.getElementById("CFG_componentZoneDiv");
	while (1 == 1) {
		if (componentZoneDiv != null) {
			if (!flag && window.CFG_NVG_clearItem) {
				CFG_NVG_clearItem(win);
			}
			componentZoneDiv.parentNode.removeChild(componentZoneDiv);
			break;
		}
		if (win == top) {
			break;
		} else {
			win = win.parent;
			componentZoneDiv = win.document.getElementById("CFG_componentZoneDiv");
		}
	}
}
/**
 * 删除当前页面中打开的构件
 */
function CFG_clearComponentZone() {
	var componentZoneDiv = document.getElementById("CFG_componentZoneDiv");
	if (componentZoneDiv != null) {
		if (window.CFG_NVG_clearItem) {
			CFG_NVG_clearItem(window);
		}
		componentZoneDiv.parentNode.removeChild(componentZoneDiv);
	}
}
/**
 * 设置返回按钮
 */
function CFG_setReturnButton(CFG_returnButtonZone) {
    if (!CFG_returnButtonZone) {
        return;
    }
    if (CFG_returnButtonZone instanceof dhtmlXToolbarObject) {
        if (CFG_getInputParamValue("assembleType") == "1") {
            CFG_returnButtonZone.addButton("CFG_closeComponentZone", 100, "返回");
        }
    }
}
/**
 * 点击返回按钮
 * @param id 按钮Id
 */
function CFG_clickReturnButton(id) {
	if (id == "CFG_closeComponentZone") {
		var win =  window.parent;
		var eventableObj = win.eventableObj;
		while (1 == 1) {
			if (eventableObj != null) {
				eventableObj.callEvent("onComponentZoneDestory");
				break;
			}
			if (win == top) {
				eventableObj = win.eventableObj;
				if (!eventableObj) {
					break;
				}
			} else {
				win = win.parent;
				eventableObj = win.eventableObj;
			}
		}
		CFG_returnToMainComponent();
	}
}