/**
 * 构造嵌入信息的存储对象
 * @param configInfo 构件配置信息
 */
function CFG_getEmbeddedInfo(configInfo) {
	if (!configInfo.embeddedInfo) {
		var parentConfigInfo = configInfo.parentConfigInfo;
		if (parentConfigInfo && parentConfigInfo.embeddedInfo) {
			configInfo.embeddedInfo = parentConfigInfo.embeddedInfo;
		} else {
			configInfo.embeddedInfo = {'CFG_componentZoneDivIds':[]};
		}
		configInfo.embeddedInfo.CFG_componentZoneDivIds[configInfo.embeddedInfo.CFG_componentZoneDivIds.length] = generateId('CFG_componentZoneDiv');
		configInfo.currentComponentDivIndex = configInfo.embeddedInfo.CFG_componentZoneDivIds.length - 1;
	}
}
/**
 * 打开嵌套构件
 * @param configInfo 构件配置信息
 * @param url 嵌套构件url
 * @param text 导航条目名称
 * @param noReturnButton true:不加返回按钮
 */
function CFG_openComponent(configInfo, url, text, noReturnButton) {
	CFG_getEmbeddedInfo(configInfo);
	var CFG_componentZoneDivId = configInfo.embeddedInfo.CFG_componentZoneDivIds[configInfo.currentComponentDivIndex];
	var componentZoneDiv = document.getElementById(CFG_componentZoneDivId);
	if (componentZoneDiv == null) {
		componentZoneDiv = document.createElement("div");
		componentZoneDiv.setAttribute("id", CFG_componentZoneDivId);
		componentZoneDiv.className = "component_zone fill coral-adjusted";
		if (configInfo.CFG_reserveZone) {
		    configInfo.CFG_reserveZone().children().hide();
			configInfo.CFG_reserveZone().append(componentZoneDiv);
		} else {
			return;
		}
		if (!$(componentZoneDiv).parents(".coral-dialog").length) {
			//$.coral.refreshAllComponent();
			$(".coral-layout").layout("refresh");
		}
		//$(window).resize(function(){
        //    CFG_componentZoneResize(configInfo);
		//});
		//CFG_componentZoneResize(configInfo);
		$.ajax({
			url : url,
			async : false,
			dataType :"html", 
			context : document.body
		}).done(function(html) {
			$('#'+CFG_componentZoneDivId).data("selfUrl", url);
			$('#'+CFG_componentZoneDivId).data("parentConfigInfo", configInfo);
			$('#'+CFG_componentZoneDivId).append(html);
			$.parser.parse($('#'+CFG_componentZoneDivId));
		});
		if (configInfo.childConfigInfo && configInfo.CFG_navigationBar) {
			var navigationIndex = configInfo.CFG_navigationBar.getSize()[0];
			configInfo.childConfigInfo.currentNavigationIndex = navigationIndex;
			configInfo.CFG_navigationBar.addNavigation({'name':text, 'index':navigationIndex, 'configInfo':configInfo.childConfigInfo});
		}
		if (configInfo.childConfigInfo && configInfo.childConfigInfo.CFG_initReserveZones) {
			configInfo.childConfigInfo.CFG_initReserveZones();
		}
		if (!noReturnButton && configInfo.childConfigInfo) {
		    if (configInfo.childConfigInfo.CFG_setReturnButton) {
		        configInfo.childConfigInfo.CFG_setReturnButton();
		    } else if (configInfo.childConfigInfo.CFG_setCloseButton) {
		        configInfo.childConfigInfo.CFG_setCloseButton();
		    }
		}
		if (configInfo.childConfigInfo && configInfo.childConfigInfo.CFG_bodyOnLoad) {
			configInfo.childConfigInfo.CFG_bodyOnLoad();
		}
	} else {
		var navigationIndex;
		if (configInfo.CFG_navigationBar) {
		    if (configInfo.childConfigInfo && configInfo.childConfigInfo.currentNavigationIndex!="undefined") {
		        navigationIndex = configInfo.childConfigInfo.currentNavigationIndex;
		    }
		}
		$.ajax({
			url : url,
			async : false,
			dataType :"html", 
			context : document.body
		}).done(function(html) {
			$('#'+CFG_componentZoneDivId).empty();
			$('#'+CFG_componentZoneDivId).data("selfUrl", url);
			$('#'+CFG_componentZoneDivId).data("parentConfigInfo", configInfo);
			$('#'+CFG_componentZoneDivId).append(html);
			$.parser.parse($('#'+CFG_componentZoneDivId));
		});
		if (configInfo.childConfigInfo && configInfo.CFG_navigationBar && navigationIndex!="undefined") {
			configInfo.childConfigInfo.currentNavigationIndex = navigationIndex;
			configInfo.CFG_navigationBar.addNavigation({'name':text, 'index':navigationIndex, 'configInfo':configInfo.childConfigInfo});
		}
		if (configInfo.childConfigInfo && configInfo.childConfigInfo.CFG_initReserveZones) {
			configInfo.childConfigInfo.CFG_initReserveZones();
		}
		if (!noReturnButton && configInfo.childConfigInfo) {
            if (configInfo.childConfigInfo.CFG_setReturnButton) {
                configInfo.childConfigInfo.CFG_setReturnButton();
            } else if (configInfo.childConfigInfo.CFG_setCloseButton) {
                configInfo.childConfigInfo.CFG_setCloseButton();
            }
        }
		if (configInfo.childConfigInfo && configInfo.childConfigInfo.CFG_bodyOnLoad) {
			configInfo.childConfigInfo.CFG_bodyOnLoad();
		}
	}
	return $('#'+CFG_componentZoneDivId);
}
/**
 * 将一个元素嵌套一个DIV
 * @param configInfo 构件配置信息
 * @param content 要追加到DIV中的内容
 * @param text 导航条目名称
 * @param beforeReturn 回调
 */
function CFG_getEmbeddedDiv(configInfo, content, text, beforeReturn) {
	CFG_getEmbeddedInfo(configInfo);
	var CFG_componentZoneDivId = configInfo.embeddedInfo.CFG_componentZoneDivIds[configInfo.currentComponentDivIndex];
	var componentZoneDiv = document.getElementById(CFG_componentZoneDivId);
	if (componentZoneDiv == null) {
		componentZoneDiv = document.createElement("div");
		componentZoneDiv.setAttribute("id", CFG_componentZoneDivId);
		componentZoneDiv.className = "component_zone fill coral-adjusted";
		if (configInfo.CFG_reserveZone) {
		    configInfo.CFG_reserveZone().children().hide();
			configInfo.CFG_reserveZone().append(componentZoneDiv);
		} else {
			return;
		}
		if (!$(componentZoneDiv).parents(".coral-dialog").length) {
			//$.coral.refreshAllComponent();
			$(".coral-layout").layout("refresh");
		}
		//$(window).resize(function(){
        //    CFG_componentZoneResize(configInfo);
		//});
		//CFG_componentZoneResize(configInfo);
		if (configInfo.CFG_navigationBar) {
			var navigationIndex = configInfo.CFG_navigationBar.getSize()[0];
			configInfo.childConfigInfo = {};
			configInfo.childConfigInfo.CFG_navigationBar = configInfo.CFG_navigationBar;
			configInfo.childConfigInfo.currentNavigationIndex = navigationIndex;
			configInfo.CFG_navigationBar.addNavigation({'name':text, 'index':navigationIndex, 'configInfo':configInfo.childConfigInfo});
		}
	} else {
		var navigationIndex;
		if (configInfo.CFG_navigationBar) {
		    if (configInfo.childConfigInfo && configInfo.childConfigInfo.currentNavigationIndex!="undefined") {
		        navigationIndex = configInfo.childConfigInfo.currentNavigationIndex;
		    }
		}
		$('#'+CFG_componentZoneDivId).trigger("componentZoneDestory");
		$('#'+CFG_componentZoneDivId).empty();
		if (configInfo.CFG_navigationBar && navigationIndex!="undefined") {
			configInfo.childConfigInfo = {};
			configInfo.childConfigInfo.CFG_navigationBar = configInfo.CFG_navigationBar;
			configInfo.childConfigInfo.currentNavigationIndex = navigationIndex;
			configInfo.CFG_navigationBar.addNavigation({'name':text, 'index':navigationIndex, 'configInfo':configInfo.childConfigInfo});
		}
	}
	$('#'+CFG_componentZoneDivId).append(content);
	$('#'+CFG_componentZoneDivId).bind("componentZoneDestory", beforeReturn);
	configInfo.hasEmbeddedDiv = true;
	return $('#'+CFG_componentZoneDivId);
}
/**
 * 将一个元素嵌套一个DIV（不加导航）
 * @param configInfo 构件配置信息
 * @param content 要追加到DIV中的内容
 * @param beforeReturn 回调
 */
function CFG_getEmbeddedDivNoNav(configInfo, content, beforeReturn) {
	var CFG_componentZoneDivId, jqReserveZone, zIndexMax = 0, 
		jqComponentZone, navigationIndex, zoneDivLen;
	if (configInfo.embeddedInfo) {
		zoneDivLen = configInfo.embeddedInfo.CFG_componentZoneDivIds.length;
		configInfo.embeddedInfo.CFG_componentZoneDivIds[zoneDivLen] = generateId('CFG_componentZoneDiv');
		configInfo.currentComponentDivIndex = zoneDivLen - 1;
	} else {
		CFG_getEmbeddedInfo(configInfo);
	}
	CFG_componentZoneDivId = configInfo.embeddedInfo.CFG_componentZoneDivIds[configInfo.currentComponentDivIndex];
	jqComponentZone = $("<DIV id=\"" + CFG_componentZoneDivId + "\" class=\"component_zone fill coral-adjusted\"></DIV>");
	if (configInfo.CFG_reserveZone) {
		jqReserveZone = configInfo.CFG_reserveZone();
	    jqReserveZone.children().hide();
		jqReserveZone.children().each(function() {
			var zIndex = $(this).css("z-index");
			if (zIndex === "auto") zIndex = 0;
			if (zIndex > zIndexMax) zIndexMax = zIndex;
		});
		jqReserveZone.prepend(jqComponentZone);
		jqComponentZone.css("z-index", ++zIndexMax).height(jqReserveZone.height());
	} else {
		return;
	}
	// 导航上不加元素，但是生成元素的位置
	if (configInfo.CFG_navigationBar) {
		navigationIndex = configInfo.CFG_navigationBar.getSize()[0];
		configInfo.childConfigInfo = {CFG_navigationBar: configInfo.CFG_navigationBar, currentNavigationIndex: navigationIndex};
	}
	configInfo.hasEmbeddedDiv = true;
	return jqComponentZone;
}
/**
 * 添加导航（嵌套的DIV已经通过CFG_getEmbeddedDiv方法生成）
 * @param configInfo 构件配置信息
 * @param componentZoneDiv 嵌套的DIV（JQuery对象）
 * @param text 导航条目名称
 */
function CFG_addNavigationItem(configInfo, componentZoneDiv, text) {
    if (componentZoneDiv && isNotEmpty(componentZoneDiv.attr("id")) && document.getElementById(componentZoneDiv.attr("id")) != null) {
		if (configInfo.CFG_navigationBar) {
		    if (configInfo.childConfigInfo && configInfo.childConfigInfo.currentNavigationIndex!="undefined") {
		        var navigationIndex = configInfo.childConfigInfo.currentNavigationIndex;
			    configInfo.CFG_navigationBar.addNavigation({'name':text, 'index':navigationIndex, 'configInfo':configInfo.childConfigInfo});
		    }
		}
    } else {
        if (configInfo.CFG_navigationBar) {
			var navigationIndex = configInfo.CFG_navigationBar.getSize()[0];
			configInfo.childConfigInfo = {};
			configInfo.childConfigInfo.CFG_navigationBar = configInfo.CFG_navigationBar;
			configInfo.childConfigInfo.currentNavigationIndex = navigationIndex;
			configInfo.CFG_navigationBar.addNavigation({'name':text, 'index':navigationIndex, 'configInfo':configInfo.childConfigInfo});
		}
    }
}
/**
 * 嵌套构件区域调整大小
 * @param configInfo 构件配置信息
 */
function CFG_componentZoneResize(configInfo) {
	var componentZoneDiv = document.getElementById(configInfo.embeddedInfo.CFG_componentZoneDivIds[configInfo.currentComponentDivIndex]);
	if (!componentZoneDiv || !configInfo.CFG_reserveZone)
		return;
	componentZoneDiv.style.top = "0px";
	componentZoneDiv.style.left = configInfo.CFG_reserveZone().get(0).offsetLeft + "px";
	componentZoneDiv.style.width = configInfo.CFG_reserveZone().get(0).offsetWidth + "px";
	componentZoneDiv.style.height = configInfo.CFG_reserveZone().get(0).offsetHeight + "px";
	$(componentZoneDiv).find(':coral-layout').layout('refresh');
}
/**
 * 删除当前页面中打开的构件
 * @param configInfo 构件配置信息
 */
function CFG_clearComponentZone(configInfo) {
	if (configInfo) {
		if (configInfo.embeddedInfo) {
			for (var i=configInfo.embeddedInfo.CFG_componentZoneDivIds.length-1; i>=configInfo.currentComponentDivIndex; i--) {
				var CFG_componentZoneDivId = configInfo.embeddedInfo.CFG_componentZoneDivIds[i];
				if (CFG_componentZoneDivId) {
					var jq = $('#'+CFG_componentZoneDivId);
					jq.trigger("componentZoneDestory");
					CFG_removeJQ(jq);
				}
			}
		}
		if (configInfo.CFG_reserveZone()) {
		    configInfo.CFG_reserveZone().children().show();
		}
		if (configInfo.childConfigInfo && configInfo.CFG_navigationBar) {
			configInfo.CFG_navigationBar.goBack(configInfo.childConfigInfo.currentNavigationIndex, true);
		}
	}
}
/**
 * 设置返回按钮
 */
function CFG_setReturnButton(configInfo, CFG_returnButtonToolbar) {
    if (CFG_getInputParamValue(configInfo, "assembleType") == "1") {
    	var toolbarLength = CFG_returnButtonToolbar.toolbar("getLength");
    	toolbarLength = toolbarLength || 0;
    	CFG_returnButtonToolbar.toolbar("add", toolbarLength, {
			"id": "CFG_closeComponentZone",
			"label": "返回",
			"type": "button"
		});
    }
}
/**
 * 点击返回按钮
 * @param parentConfigInfo 父构件配置信息
 */
function CFG_clickReturnButton(parentConfigInfo, clickNavigation) {
	if (parentConfigInfo && parentConfigInfo.embeddedInfo && parentConfigInfo.embeddedInfo.CFG_componentZoneDivIds) {
		var CFG_componentZoneDivId = parentConfigInfo.embeddedInfo.CFG_componentZoneDivIds[parentConfigInfo.currentComponentDivIndex];
		$('#'+CFG_componentZoneDivId).trigger("componentZoneDestory");
		if (parentConfigInfo.CFG_reserveZone()) {
		    parentConfigInfo.CFG_reserveZone().children().show();
		}
	}
	CFG_returnToMainComponent(parentConfigInfo);
	if (!clickNavigation && parentConfigInfo.CFG_navigationBar) {
		var configInfo = parentConfigInfo.childConfigInfo;
		if (configInfo.CFG_navigationBar) {
		    if (configInfo.currentNavigationIndex) {
		        configInfo.CFG_navigationBar.goBack(configInfo.currentNavigationIndex, true);
		        if (configInfo.currentNavigationIndex != 0) {
		        	configInfo.currentNavigationIndex -= 1;
		        }
		    } else {
		        var navigationIndex = configInfo.CFG_navigationBar.getSize()[0];
		        configInfo.CFG_navigationBar.goBack(navigationIndex-1, true);
		    }
		}
	}
	$.coral.refreshAllComponent();
	//$(".coral-layout").layout("refresh");
}
/**
 * 关闭打开的构件
 * @param parentConfigInfo 父构件配置信息
 */
function CFG_returnToMainComponent(parentConfigInfo) {
	if (parentConfigInfo && parentConfigInfo.embeddedInfo && parentConfigInfo.embeddedInfo.CFG_componentZoneDivIds) {
	    if (parentConfigInfo.hasEmbeddedDiv) {
	        parentConfigInfo.hasEmbeddedDiv = false;
        }
	    for (var i=parentConfigInfo.currentComponentDivIndex; i<parentConfigInfo.embeddedInfo.CFG_componentZoneDivIds.length; i++) {
			var CFG_componentZoneDivId = parentConfigInfo.embeddedInfo.CFG_componentZoneDivIds[i];
			if (CFG_componentZoneDivId) {
				CFG_removeJQ($('#'+CFG_componentZoneDivId));
			}
		}
	}
}