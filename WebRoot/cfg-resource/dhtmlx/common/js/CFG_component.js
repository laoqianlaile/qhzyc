document.write("<script type='text/javascript' src='" + DHX_RES_PATH + "/common/js/CFG_embedded_page.js'></script>");
document.write("<script type='text/javascript' src='" + DHX_RES_PATH + "/common/js/CFG_navigation.js'></script>");
document.write("<script type='text/javascript' src='" + DHX_RES_PATH + "/views/config/appmanage/js/AppActionURI.js'></script>");
var _this = this;
var localUrl = window.location.href;
var paramStr = localUrl.substring(localUrl.indexOf('?') + 1);
var params = paramStr.split('&');
var CFG_urlParams = {};
for (var i in params) {
    var keyvalue = params[i].split("=");
    CFG_urlParams[keyvalue[0]] = keyvalue[1];
}
// 从父页面中获取本页面构件的输入参数
apply(CFG_urlParams, parent.pageInputParams);
// 获取配置信息的URL
var CFG_configUrl = contextPath + '/construct/construct!getConfigInfo.json';
if (!page) {
    var temp = localUrl.substring(0, localUrl.indexOf('?'));
    page = temp.substring(temp.lastIndexOf('/') + 1);
}
CFG_configUrl += '?page=' + page;
if (CFG_urlParams['menuId'] != undefined) {
    CFG_configUrl += '&menuId=' + CFG_urlParams['menuId'];
}
if (CFG_urlParams['constructId'] != undefined) {
    CFG_configUrl += '&constructId=' + CFG_urlParams['constructId'];
}
if (CFG_urlParams['constructDetailId'] != undefined) {
    CFG_configUrl += '&constructDetailId=' + CFG_urlParams['constructDetailId'];
}
if (CFG_urlParams['componentVersionId'] != undefined) {
    CFG_configUrl += '&componentVersionId=' + CFG_urlParams['componentVersionId'];
}
// 构件的配置信息
var CFG_configInfo = eval("(" + loadJson(CFG_configUrl) + ")");
// 声明系统参数、构件自身参数、构件入参、预留区
var CFG_systemParams, CFG_selfParams, CFG_inputParams, CFG_reserveZones;
// 页面构件出参
var CFG_outputParams = {};
/**
 * 获取系统参数的值
 * @param {string} name 系统参数名称
 * @return {string}
 */
function CFG_getSystemParamValue(name) {
    if (!CFG_systemParams) {
        if (CFG_configInfo && CFG_configInfo.params && CFG_configInfo.params.systemParams) {
            CFG_systemParams = CFG_configInfo.params.systemParams;
        }
    }
    if (CFG_systemParams) {
        for (var o in CFG_systemParams) {
            if (CFG_systemParams[o].name == name) {
                return CFG_systemParams[o].value || '';
            }
        }
    }
    return '';
}
/**
 * 获取构件自身参数的值
 * @param {string} name 构件自身参数名称
 * @return {string}
 */
function CFG_getSelfParamValue(name) {
    if (!CFG_selfParams) {
        if (CFG_configInfo && CFG_configInfo.params && CFG_configInfo.params.selfParams) {
            CFG_selfParams = CFG_configInfo.params.selfParams;
        }
    }
    if (CFG_selfParams) {
        for (var o in CFG_selfParams) {
            if (CFG_selfParams[o].name == name) {
                return CFG_selfParams[o].value || '';
            }
        }
    }
    return '';
}
/**
 * 获取构件入参的值
 * @param {string} name 构件入参名称
 * @return {string}
 */
function CFG_getInputParamValue(name) {
    if (CFG_urlParams['bindingType'] == 'menu') {
        if (!CFG_inputParams) {
            if (CFG_configInfo && CFG_configInfo.params && CFG_configInfo.params.inputParams) {
                CFG_inputParams = CFG_configInfo.params.inputParams;
            }
        }
        if (CFG_inputParams) {
            for (var o in CFG_inputParams) {
                if (CFG_inputParams[o].name == name) {
                    return CFG_inputParams[o].value || '';
                }
            }
        }
    } else {
        if (CFG_urlParams[name] != 'undefined') {
            return CFG_urlParams[name] || '';
        }
    }
    return '';
}
/**
 * 获取预留区中页面构件配置信息
 * @param {string} name 预留区名称
 * @param {string} type 预留区类型 0-工具条 1-列表超链接
 * @return {Array}
 */
function CFG_getReserveZoneInfo(name, type) {
    var CFG_reserveZones
    if (CFG_configInfo && CFG_configInfo.reserveZones) {
        CFG_reserveZones = CFG_configInfo.reserveZones;
    }
    var constructDetails;
    if (CFG_reserveZones) {
        for (var o in CFG_reserveZones) {
            if (CFG_reserveZones[o].name == name && CFG_reserveZones[o].type == type) {
                constructDetails = CFG_reserveZones[o].constructDetails;
                break;
            }
        }
    }
    if (!constructDetails) {
    	var url = contextPath + '/construct/construct!getReverseZoneConfigInfo.json';
    	url += '?page=' + (CFG_configInfo.page||page||"MT_page_") + "&reserveZoneName=" + name;
	    if (CFG_urlParams['menuId'] != undefined) {
	        url += '&menuId=' + CFG_urlParams['menuId'];
	    }
	    if (CFG_urlParams['constructId'] != undefined) {
	        url += '&constructId=' + CFG_urlParams['constructId'];
	    }
        var reserveZone = eval("(" + loadJson(url) + ")");
        if (!CFG_configInfo.reserveZones) {
            CFG_configInfo.reserveZones = new Array();
        }
        CFG_configInfo.reserveZones.push(reserveZone);
        constructDetails = reserveZone.constructDetails;
    }
    return constructDetails;
}
/**
 * 判断预留区是否绑定了按钮
 * @param {string} name 预留区名称
 * @param {string} type 预留区类型 0-工具条 1-列表超链接
 * @return {boolean}
 */
function CFG_isReserveZoneBinding(name, type) {
    var constructDetails = CFG_getReserveZoneInfo(name, type);
    var result = false;
    if (constructDetails && constructDetails.length > 0) {
        result = true;
    }
    return result;
}
/**
 * 向工具条预留区中添加按钮
 * @param {object} toolBar 工具条
 * @param {string} reserveZoneName 工具条预留区名称
 * @param {number} startIndex 工具条上按钮开始index
 */
function CFG_addToolbarButtons(toolBar, reserveZoneName, startIndex) {
    var constructDetails = CFG_getReserveZoneInfo(reserveZoneName, '0');
    if (constructDetails && constructDetails.length > 0) {
        var comboButtons = new Array();
        for (var o in constructDetails) {
            if (constructDetails[o].buttonType == 2) {
                comboButtons.push(constructDetails[o]);
            } else if (constructDetails[o].buttonType == 0) {
                toolBar.addSeparator("septr", ++startIndex);
                toolBar.addButton(constructDetails[o].buttonCode, ++startIndex, "<font color='blue'>"
                                + constructDetails[o].buttonDisplayName + "</font>", constructDetails[o].buttonImg);
            } else if (constructDetails[o].buttonType == 1) {
                toolBar.addSeparator("septr", ++startIndex);
                if (isNotEmpty(constructDetails[o].bindingComponentUrl)) {
                    toolBar.addButtonSelect(constructDetails[o].buttonCode, ++startIndex, "<font color='blue'>"
                            + constructDetails[o].buttonDisplayName + "</font>", [], constructDetails[o].buttonImg);
                } else {
                    toolBar.addButtonSelect(constructDetails[o].buttonCode, ++startIndex, "<font color='blue'>"
                            + constructDetails[o].buttonDisplayName + "</font>", [], constructDetails[o].buttonImg, null, "disabled", true);
                }
            }
        }
        if (toolBar.getPosition("more") == null) {
            for (var o in comboButtons) {
                if (comboButtons[o].parentButtonCode == "more") {
                    toolBar.addButtonSelect("more", ++startIndex, "更多", [], "release.gif", null, "disabled", true);
                    break;
                }
            }
        } else {
            toolBar.setPosition("more", ++startIndex);
        }
        for (var o in comboButtons) {
            toolBar.addListOption(comboButtons[o].parentButtonCode, comboButtons[o].buttonCode, ++startIndex, "button", "<font color='blue'>"
                    + comboButtons[o].buttonDisplayName + "</font>", comboButtons[o].buttonImg == "" ? null : comboButtons[o].buttonImg);
        }
    }
}
/**
 * 获取超链接预留区绑定的按钮
 * @param {string} name 预留区名称
 * @return {boolean}
 */
function CFG_getLinkReserveZoneButton(name) {
    var constructDetails = CFG_getReserveZoneInfo(name, '1');
    var link = "";
    if (constructDetails && constructDetails.length > 0) {
        var comboButtons = new Array();
        for (var o in constructDetails) {
            if (constructDetails[o].buttonImg) {
                link += "<img src=\"" + DHX_RES_PATH + "/common/images/icon/"
                        + constructDetails[o].buttonImg + "\" title=\"" + constructDetails[o].buttonDisplayName
                        + "\" onclick=\"javascript:CFG_clickToolbar('" + name + "','" + constructDetails[o].buttonCode
                        + "','1')\"/> ";
            } else {
                link += "<a style=\"text-decoration:none\" href=\"javascript:CFG_clickToolbar('" + name + "','" + constructDetails[o].buttonCode
                        + "','1')\">" + constructDetails[o].buttonDisplayName + "</a> ";
            }
        }
    }
    return link;
}
/**
 * 向工具条预留区中添加按钮的click事件（超链接也可以用）
 * @param {string} reserveZoneName 工具条预留区名称
 * @param {string} id 工具条上点击的按钮的name
 * @param {string} type 预留区类型 0-工具条 1-列表超链接
 */
function CFG_clickToolbar(reserveZoneName, id, type) {
    if (!type) {
        type = 0;
    }
    var constructDetails = CFG_getReserveZoneInfo(reserveZoneName, type);
    if (constructDetails && constructDetails.length > 0) {
        for (var o in constructDetails) {
            if (constructDetails[o].buttonCode == id) {
                if (isEmpty(constructDetails[o].bindingComponentUrl)) {
                    continue;
                }
                var funs = constructDetails[o].paramFunctions;
                var callbacks = constructDetails[o].paramCallbacks;
                var bindingComponentUrl = constructDetails[o].bindingComponentUrl;
                var componentType = constructDetails[o].componentType;
                var assembleType = constructDetails[o].assembleType;
                var buttonDisplayName = constructDetails[o].buttonDisplayName;
                var constructDetailId = constructDetails[o].constructDetailId;
                if (bindingComponentUrl.indexOf("?") == -1) {
                    bindingComponentUrl += "?constructDetailId=" + constructDetailId;
                } else {
                    bindingComponentUrl += "&constructDetailId=" + constructDetailId;
                }
                bindingComponentUrl += "&assembleType=" + assembleType;
                if (CFG_configInfo.CFG_urlParams['menuId'] && isNotEmpty(CFG_configInfo.CFG_urlParams['menuId'])) {
                    bindingComponentUrl += "&menuId=" + CFG_configInfo.CFG_urlParams['menuId'];
                }
                if (CFG_configInfo.CFG_urlParams['menuCode'] && isNotEmpty(CFG_configInfo.CFG_urlParams['menuCode'])) {
                    bindingComponentUrl += "&menuCode=" + CFG_configInfo.CFG_urlParams['menuCode'];
                }
                var pageInputParams = {};
                if (funs) {
                    for (var i in funs) {
                        var funcName = funs[i].functionName;
                        var funcNameArray = funcName.split('.');
                        var obj = _this;
                        for (var p in funcNameArray) {
                            if (obj[funcNameArray[p]]) {
                                obj = obj[funcNameArray[p]];
                                if (typeof obj == 'function' && p != (funcNameArray.length-1)) {
                                    obj = obj[funcNameArray[p]]();
                                    if (!obj) {
                                        return;
                                    }
                                }
                            } else {
                                return;
                            }
                        }
                        var retObj;
                        if (typeof obj == 'function') {
                            retObj = obj.apply(_this);
                        } else {
                            retObj = obj;
                        
                        }
                        if (false == retObj.status) {
                            return;
                        }
                        var params = funs[i].params;
                        for (var j in params) {
                            obj = retObj[params[j].outputParam];
                            if (typeof obj === "function") obj = obj.apply(_this);
                            pageInputParams[params[j].inputParam] = obj;
                        }
                    }
                }
                // URL后面拼接参数，可能会超出浏览器限制的URL长度。逻辑构件改成Post方式;页面构件通过js获取需要的入参
                /*for (var k in pageInputParams) {
                    bindingComponentUrl += "&" + k + "=" + encodeURIComponent(pageInputParams[k]);
                }*/
                _this.pageInputParams = pageInputParams;
                // 调用构件之前的前置事件处理
                var beforeClickJs = constructDetails[o].beforeClickJs;
                if (beforeClickJs && typeof(beforeClickJs) == "string") {
                    beforeClickJs = eval("(" + beforeClickJs + ")");
                }
                if (beforeClickJs && typeof(beforeClickJs) == "function") {
                    var checkResult = beforeClickJs(pageInputParams);
                    if (false == checkResult.success) {
                        if (checkResult.message && "" != checkResult.message) {
                            dhtmlx.message(checkResult.message);
                        }
                        return;
                    }
                }
                if (componentType == "1" || componentType == "3" || componentType == "4" || componentType == "5" || componentType == "9") {
                    // 假如是页面构件，访问的地址是指向后台的action，那么需要通过url的方式传递参数，所以此处url后也加参数（可能会出现url超过长度限制的情况）
                    for (var k in pageInputParams) {
                        bindingComponentUrl += "&" + k + "=" + encodeURIComponent(pageInputParams[k]);
                    }
                    if (assembleType == "0") {
                        if (!dhxWins) {
                            dhxWins = new dhtmlXWindows();
                        }
                        var width = 800;
                        var height = 600;
                        var w = constructDetails[o].width;
                        var h = constructDetails[o].height;
                        if (w) {
                            var bodyWidth = document.body.clientWidth;
                            if (w.indexOf("%") != -1) {
                                width = bodyWidth * parseFloat(w) * 0.01;
                            } else if (w.indexOf(".") != -1) {
                                width = bodyWidth * parseFloat(w);
                            } else {
                                width = w;
                            }
                        }
                        if (h) {
                            var bodyHeight = document.body.clientHeight;
                            if (h.indexOf("%") != -1) {
                                height = bodyHeight * parseFloat(h) * 0.01;
                            } else if (h.indexOf(".") != -1) {
                                height = bodyHeight * parseFloat(h);
                            } else {
                                height = h;
                            }
                        }
                        var pageWin = dhxWins.createWindow("pageComponent", 0, 0, width, height);
                        pageWin.setModal(true);
                        pageWin.setText(constructDetails[o].buttonDisplayName);
                        pageWin.center();
                        pageWin.attachURL(bindingComponentUrl);
                        if (callbacks) {
                            pageWin.attachEvent("onClose", function() {
                                var CFG_outputParams = pageWin.getFrame().contentWindow.CFG_outputParams;
                                for (var i in callbacks) {
                                    CFG_execCallback(callbacks[i], _this, CFG_outputParams);
                                }
                                return true;
                            });
                        }
                    } else if (assembleType == "1") {
                        if (window.CFG_openComponent) {
                            var eventObj = CFG_openComponent(bindingComponentUrl, buttonDisplayName);
                            if (callbacks) {
                                eventObj.attachEvent("onComponentZoneDestory", function(){
                                    var CFG_outputParams = eventObj.contentWindow.CFG_outputParams;
                                    for (var i in callbacks) {
                                        CFG_execCallback(callbacks[i], _this, CFG_outputParams);
                                    }
                                });
                            }
                        }
                    } else if (assembleType == "2") {
                        if (top.CFG_addTab) {
                            // tab的组装方式没有callback
                            top.CFG_addTab(buttonDisplayName, bindingComponentUrl);
                        }
                    }
                } else if (componentType == "2") {
                    // 使用post方式提交
                    var params = "";
                    for (var k in pageInputParams) {
                        params += "&" + k + "=" + encodeURIComponent(pageInputParams[k]);
                    }
                    if (params.length > 0) {
                        params = params.substring(1, params.length);
                    }
                    dhtmlxAjax.post(bindingComponentUrl, params, function(loader) {
                        var result = eval("(" + loader.xmlDoc.responseText + ")");
                        if (typeof result == "string") {
                            result = eval("(" + result + ")");
                        }
                        if (result.message) {
                            dhtmlx.message(result.message);
                        }
                        if (callbacks) {
                            for (var i in callbacks) {
                                CFG_execCallback(callbacks[i], _this, result);
                            }
                        }
                    });
                }
            }
        }
    }
}
/**
 * 按钮预留区或树节点预留区点击事件
 * @param {string} reserveZoneName 预留区名称
 * @param {string} title 工具条上点击的按钮的name
 * @param {string} type 预留区类型 2-按钮预留区 3-树节点预留区
 */
function CFG_clickButtonOrTreeNode(reserveZoneName, title, type) {
    if (!type) {
        type = 2;
    }
    var constructDetails = CFG_getReserveZoneInfo(reserveZoneName, type);
    if (constructDetails && constructDetails.length > 0) {
        var funs = constructDetails[0].paramFunctions;
        var callbacks = constructDetails[0].paramCallbacks;
        var bindingComponentUrl = constructDetails[0].bindingComponentUrl;
        var componentType = constructDetails[0].componentType;
        var assembleType = constructDetails[0].assembleType;
        var constructDetailId = constructDetails[0].constructDetailId;
        if (bindingComponentUrl.indexOf("?") == -1) {
            bindingComponentUrl += "?constructDetailId=" + constructDetailId;
        } else {
            bindingComponentUrl += "&constructDetailId=" + constructDetailId;
        }
        bindingComponentUrl += "&assembleType=" + assembleType;
        if (CFG_configInfo.CFG_urlParams['menuId'] && isNotEmpty(CFG_configInfo.CFG_urlParams['menuId'])) {
            bindingComponentUrl += "&menuId=" + CFG_configInfo.CFG_urlParams['menuId'];
        }
        if (CFG_configInfo.CFG_urlParams['menuCode'] && isNotEmpty(CFG_configInfo.CFG_urlParams['menuCode'])) {
            bindingComponentUrl += "&menuCode=" + CFG_configInfo.CFG_urlParams['menuCode'];
        }
        var pageInputParams = {};
        if (funs) {
            for (var i in funs) {
                var funcName = funs[i].functionName;
                var funcNameArray = funcName.split('.');
                var obj = _this;
                for (var p in funcNameArray) {
                    if (obj[funcNameArray[p]]) {
                        obj = obj[funcNameArray[p]];
                        if (typeof obj == 'function' && p != (funcNameArray.length-1)) {
                            obj = obj[funcNameArray[p]]();
                            if (!obj) {
                                return;
                            }
                        }
                    } else {
                        return;
                    }
                }
                var retObj;
                if (typeof obj == 'function') {
                    retObj = obj.apply(_this);
                } else {
                    retObj = obj;
                
                }
                if (false == retObj.status) {
                    return;
                }
                var params = funs[i].params;
                for (var j in params) {
                    obj = retObj[params[j].outputParam];
                    if (typeof obj === "function") obj = obj.apply(_this);
                    pageInputParams[params[j].inputParam] = obj;
                }
            }
        }
        // URL后面拼接参数，可能会超出浏览器限制的URL长度。逻辑构件改成Post方式;页面构件通过js获取需要的入参
        /*for (var k in pageInputParams) {
            bindingComponentUrl += "&" + k + "=" + encodeURIComponent(pageInputParams[k]);
        }*/
        _this.pageInputParams = pageInputParams;
        // 调用构件之前的前置事件处理
        var beforeClickJs = constructDetails[0].beforeClickJs;
        if (beforeClickJs && typeof(beforeClickJs) == "string") {
            beforeClickJs = eval("(" + beforeClickJs + ")");
        }
        if (beforeClickJs && typeof(beforeClickJs) == "function") {
            var checkResult = beforeClickJs(pageInputParams);
            if (false == checkResult.success) {
                if (checkResult.message && "" != checkResult.message) {
                    dhtmlx.message(checkResult.message);
                }
                return;
            }
        }
        if (componentType == "1" || componentType == "3" || componentType == "4" || componentType == "5" || componentType == "9") {
            // 假如是页面构件，访问的地址是指向后台的action，那么需要通过url的方式传递参数，所以此处url后也加参数（可能会出现url超过长度限制的情况）
            for (var k in pageInputParams) {
                bindingComponentUrl += "&" + k + "=" + encodeURIComponent(pageInputParams[k]);
            }
            if (assembleType == "0") {
                if (!dhxWins) {
                    dhxWins = new dhtmlXWindows();
                }
                var width = 800;
                var height = 600;
                var w = constructDetails[0].width;
                var h = constructDetails[0].height;
                if (w) {
                    var bodyWidth = document.body.clientWidth;
                    if (w.indexOf("%") != -1) {
                        width = bodyWidth * parseFloat(w) * 0.01;
                    } else if (w.indexOf(".") != -1) {
                        width = bodyWidth * parseFloat(w);
                    } else {
                        width = w;
                    }
                }
                if (h) {
                    var bodyHeight = document.body.clientHeight;
                    if (h.indexOf("%") != -1) {
                        height = bodyHeight * parseFloat(h) * 0.01;
                    } else if (h.indexOf(".") != -1) {
                        height = bodyHeight * parseFloat(h);
                    } else {
                        height = h;
                    }
                }
                var pageWin = dhxWins.createWindow("pageComponent", 0, 0, width, height);
                pageWin.setModal(true);
                pageWin.setText(title);
                pageWin.center();
                pageWin.attachURL(bindingComponentUrl);
                if (callbacks) {
                    pageWin.attachEvent("onClose", function() {
                        var CFG_outputParams = pageWin.getFrame().contentWindow.CFG_outputParams;
                        for (var i in callbacks) {
                            CFG_execCallback(callbacks[i], _this, CFG_outputParams);
                        }
                        return true;
                    });
                }
            } else if (assembleType == "1") {
                if (window.CFG_openComponent) {
                    var eventObj = CFG_openComponent(bindingComponentUrl, title);
                    if (callbacks) {
                        eventObj.attachEvent("onComponentZoneDestory", function(){
                            var CFG_outputParams = eventObj.contentWindow.CFG_outputParams;
                            for (var i in callbacks) {
                                CFG_execCallback(callbacks[i], _this, CFG_outputParams);
                            }
                        });
                    }
                }
            } else if (assembleType == "2") {
                if (top.CFG_addTab) {
                    // tab的组装方式没有callback
                    top.CFG_addTab(title, bindingComponentUrl);
                }
            }
        } else if (componentType == "2") {
            // 使用post方式提交
            var params = "";
            for (var k in pageInputParams) {
                params += "&" + k + "=" + encodeURIComponent(pageInputParams[k]);
            }
            if (params.length > 0) {
                params = params.substring(1, params.length);
            }
            dhtmlxAjax.post(bindingComponentUrl, params, function(loader) {
                var result = eval("(" + loader.xmlDoc.responseText + ")");
                if (typeof result == "string") {
                    result = eval("(" + result + ")");
                }
                if (result.message) {
                    dhtmlx.message(result.message);
                }
                if (callbacks) {
                    for (var i in callbacks) {
                        CFG_execCallback(callbacks[i], _this, result);
                    }
                }
            });
        }
    }
}
/**
 * 树构件上树节点的点击事件
 * @param {string} treeNodeType 树节点类型
 * @param {string} treeNodeProperty 树节点属性值 根节点——空；空节点——值；字段节点（跨表）——字段标签；表节点——表ID；物理表组节点——逻辑表组Code或ThirdParty
 * @param {string} title 导航的标题
 * @param {string} _params 打开构件时传递的参数a=a&b=b
 */
function CFG_clickTreeNode(treeNodeType, treeNodeProperty, title, params) {
    var constructDetails = CFG_getReserveZoneInfo("TREE", "TREE");
    if (constructDetails && constructDetails.length > 0) {
        var constructDetail;
        for (var o in constructDetails) {
            if (constructDetails[o].treeNodeType == treeNodeType && (treeNodeType == "0" || constructDetails[o].treeNodeProperty == treeNodeProperty)) {
                if (isEmpty(constructDetails[o].bindingComponentUrl)) {
                    return false;
                }
                constructDetail = constructDetails[o];
                break;
            }
        }
        if (!constructDetail) {
            for (var o in constructDetails) {
                if (constructDetails[o].treeNodeProperty == "ThirdParty") {
                    if (isEmpty(constructDetails[o].bindingComponentUrl)) {
                        return false;
                    }
                    constructDetail = constructDetails[o];
                    break;
                }
            }
        }
        if (!constructDetail) {
            return false;
        }
        var funs = constructDetail.paramFunctions;
        var callbacks = constructDetail.paramCallbacks;
        var bindingComponentUrl = constructDetail.bindingComponentUrl;
        var componentType = constructDetail.componentType;
        var constructDetailId = constructDetail.constructDetailId;
        if (bindingComponentUrl.indexOf("?") == -1) {
            bindingComponentUrl += "?constructDetailId=" + constructDetailId;
        } else {
            bindingComponentUrl += "&constructDetailId=" + constructDetailId;
        }
        if (CFG_configInfo.CFG_urlParams['menuId'] && isNotEmpty(CFG_configInfo.CFG_urlParams['menuId'])) {
            bindingComponentUrl += "&menuId=" + CFG_configInfo.CFG_urlParams['menuId'];
        }
        if (CFG_configInfo.CFG_urlParams['menuCode'] && isNotEmpty(CFG_configInfo.CFG_urlParams['menuCode'])) {
            bindingComponentUrl += "&menuCode=" + CFG_configInfo.CFG_urlParams['menuCode'];
        }
        var pageInputParams = {};
        if (funs) {
            for (var i in funs) {
                var funcName = funs[i].functionName;
                var funcNameArray = funcName.split('.');
                var obj = _this;
                for (var p in funcNameArray) {
                    if (obj[funcNameArray[p]]) {
                        obj = obj[funcNameArray[p]];
                        if (typeof obj == 'function' && p != (funcNameArray.length-1)) {
                            obj = obj[funcNameArray[p]]();
                            if (!obj) {
                                return;
                            }
                        }
                    } else {
                        return;
                    }
                }
                var retObj;
                if (typeof obj == 'function') {
                    retObj = obj.apply(_this);
                } else {
                    retObj = obj;
                
                }
                if (false == retObj.status) {
                    return;
                }
                var params = funs[i].params;
                for (var j in params) {
                    obj = retObj[params[j].outputParam];
                    if (typeof obj === "function") obj = obj.apply(_this);
                    pageInputParams[params[j].inputParam] = obj;
                }
            }
        }
        // URL后面拼接参数，可能会超出浏览器限制的URL长度。逻辑构件改成Post方式;页面构件通过js获取需要的入参
        /*for (var k in pageInputParams) {
            bindingComponentUrl += "&" + k + "=" + encodeURIComponent(pageInputParams[k]);
        }*/
        _this.pageInputParams = pageInputParams;
        // 调用构件之前的前置事件处理
        var beforeClickJs = constructDetail.beforeClickJs;
        if (beforeClickJs && typeof(beforeClickJs) == "string") {
            beforeClickJs = eval("(" + beforeClickJs + ")");
        }
        if (beforeClickJs && typeof(beforeClickJs) == "function") {
            var checkResult = beforeClickJs(pageInputParams);
            if (false == checkResult.success) {
                if (checkResult.message && "" != checkResult.message) {
                    dhtmlx.message(checkResult.message);
                }
                return;
            }
        }
        if (componentType == "1" || componentType == "3" || componentType == "4" || componentType == "5" || componentType == "9") {
            // 假如是页面构件，访问的地址是指向后台的action，那么需要通过url的方式传递参数，所以此处url后也加参数（可能会出现url超过长度限制的情况）
            for (var k in pageInputParams) {
                bindingComponentUrl += "&" + k + "=" + encodeURIComponent(pageInputParams[k]);
            }
            if (window.CFG_openComponent) {
                var eventObj = CFG_openComponent(bindingComponentUrl, title);
                if (callbacks) {
                    eventObj.attachEvent("onComponentZoneDestory", function(){
                        var CFG_outputParams = eventObj.contentWindow.CFG_outputParams;
                        for (var i in callbacks) {
                            CFG_execCallback(callbacks[i], _this, CFG_outputParams);
                        }
                    });
                }
            }
        }
    }
}
/**
 * 调用回调函数
 */
function CFG_execCallback(callback, _this, outputParams) {
    var cbName = callback.callbackName;
    var cbNameArray = cbName.split('.');
    var obj = _this;
    for (var p in cbNameArray) {
        obj = obj[cbNameArray[p]];
        if (obj) {
            if (typeof obj == 'function' && p != (cbNameArray.length-1)) {
                obj = obj();
                if (!obj) {
                    return;
                }
            }
        } else {
            return;
        }
    }
    if (typeof obj == 'function') {
        var inputParams = [];
        if (outputParams) {
            var params = callback.params;
            for (var j in params) {
                inputParams[inputParams.length] = outputParams[params[j].outputParam];
            }
        }
        obj.apply(_this, inputParams);
    }
}
/**
 * 设置关闭按钮
 */
function CFG_setCloseButton(CFG_closeButtonZone) {
    if (!CFG_closeButtonZone) {
        return;
    }
    if (CFG_closeButtonZone instanceof dhtmlXToolbarObject) {
        if (CFG_getInputParamValue("assembleType") == "0") {
            CFG_closeButtonZone.addButton("CFG_close", 100, "关闭");
        }
    }
}
/**
 * 点击关闭按钮
 * @param id 按钮Id
 */
function CFG_clickCloseButton(id) {
    if (id == "CFG_close") {
        parent.dhxWins.window("pageComponent").close();
    }
}