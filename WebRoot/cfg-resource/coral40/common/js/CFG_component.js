document.write("<script type='text/javascript' src='" + $.contextPath + "/cfg-resource/coral40/common/js/CFG_embedded_page.js'></script>");
document.write("<script type='text/javascript' src='" + $.contextPath + "/cfg-resource/coral40/common/js/CFG_navigation.js'></script>");
document.write("<script type='text/javascript' src='" + $.contextPath + "/cfg-resource/dhtmlx/views/config/appmanage/js/AppActionURI.js'></script>");
var baseConfigInfoInitObj = {
    /** 页面名称 */
    'page' : '',
    /** 页面中的最大元素 */
    'maxEleInPage' : $(document.body),
    /** 获取构件嵌入的区域 */
    'getEmbeddedZone' : function() {
    },
    /** 初始化预留区 */
    'initReserveZones' : function(configInfo) {
    },
    /** 页面初始化的方法 */
    'bodyOnLoad' : function(configInfo) {
    }
};
/**
 * 初始化页面中的信息
 * @param obj 初始化的条件
 */
function CFG_initConfigInfo(obj) {
    var initObj = $.extend({}, baseConfigInfoInitObj, obj);
    var selfUrl = initObj.maxEleInPage.parent().data('selfUrl');
    var parentConfigInfo = initObj.maxEleInPage.parent().data('parentConfigInfo');
    var configInfo = {};
    if (selfUrl) {
        configInfo = CFG_getConfigInfo(selfUrl, initObj.page, parentConfigInfo);
    }
    configInfo.page = obj.page;
    if (configInfo.assembleType == "1") {
        configInfo.CFG_navigationBar = parentConfigInfo.CFG_navigationBar;
    }
    configInfo.parentConfigInfo = parentConfigInfo;
    parentConfigInfo.childConfigInfo = configInfo;
    configInfo.dialogParent = initObj.maxEleInPage;
    configInfo.CFG_reserveZone = initObj.getEmbeddedZone;
    configInfo.CFG_initReserveZones = function() {
        initObj.initReserveZones(configInfo);
    }
    if (initObj.setReturnButton) {
        configInfo.CFG_setReturnButton = function() {
            initObj.setReturnButton(configInfo);
        }
    }
    if (initObj.setCloseButton) {
        configInfo.CFG_setCloseButton = function() {
            initObj.setCloseButton(configInfo);
        }
    }
    configInfo.CFG_bodyOnLoad = function() {
        initObj.bodyOnLoad(configInfo);
    }
    initObj.maxEleInPage.data('configInfo', configInfo);
    configInfo.CFG_outputParams = {};
    return configInfo;
}
/**
 * 获取构件的配置信息
 * @param pageUrl 页面的url
 * @param page 页面名称（用于获取page下的方法、回调函数、预留区）
 * @param parentConfigInfo 父构件的配置信息
 */
function CFG_getConfigInfo(pageUrl, page, parentConfigInfo) {
    var paramStr = pageUrl.substring(pageUrl.indexOf('?') + 1);
    var params = paramStr.split('&');
    var CFG_urlParams = {};
    for (var i in params) {
        var keyvalue = params[i].split("=");
        CFG_urlParams[keyvalue[0]] = keyvalue[1];
    }
    // 从父构件中获取本构件的输入参数
    if (parentConfigInfo && parentConfigInfo.childInputParams) {
        $.extend(CFG_urlParams, parentConfigInfo.childInputParams);
    }
    // 获取配置信息的URL
    var CFG_configUrl = $.contextPath + '/construct/construct!getConfigInfo.json';
    if (!page) {
        var temp = pageUrl.substring(0, pageUrl.indexOf('?'));
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
    var CFG_configInfo = CFG_eval($.loadJson(CFG_configUrl));
    CFG_configInfo.CFG_urlParams = CFG_urlParams;
    // 构件出参
    CFG_configInfo.CFG_outputParams = {};
    return CFG_configInfo;
}
/**
 * 获取系统参数的值
 * @param {object} configInfo 构件配置信息
 * @param {string} name 系统参数名称
 * @return {string}
 */
function CFG_getSystemParamValue(CFG_configInfo, name) {
    var CFG_systemParams;
    if (CFG_configInfo && CFG_configInfo.params && CFG_configInfo.params.systemParams) {
        CFG_systemParams = CFG_configInfo.params.systemParams;
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
 * @param {object} configInfo 构件配置信息
 * @param {string} name 构件自身参数名称
 * @return {string}
 */
function CFG_getSelfParamValue(CFG_configInfo, name) {
    var CFG_selfParams;
    if (CFG_configInfo && CFG_configInfo.params && CFG_configInfo.params.selfParams) {
        CFG_selfParams = CFG_configInfo.params.selfParams;
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
 * @param {object} configInfo 构件配置信息
 * @param {string} name 构件入参名称
 * @return {string}
 */
function CFG_getInputParamValue(CFG_configInfo, name) {
    if (!CFG_configInfo) return "";
    var v;
    if (CFG_configInfo.CFG_urlParams['bindingType'] == 'menu') {
        var CFG_inputParams;
        if (CFG_configInfo && CFG_configInfo.params && CFG_configInfo.params.inputParams) {
            CFG_inputParams = CFG_configInfo.params.inputParams;
        }
        if (CFG_inputParams) {
            for (var o in CFG_inputParams) {
                if (CFG_inputParams[o].name == name) {
                    v = CFG_inputParams[o].value;
                    break;
                }
            }
        }
        if (!v && CFG_configInfo.CFG_urlParams[name] != 'undefined') {
            v = CFG_configInfo.CFG_urlParams[name];
        }
    } else {
        if (CFG_configInfo.CFG_urlParams[name] != 'undefined') {
            v = CFG_configInfo.CFG_urlParams[name];
        }
    }
    return v || '';
}
/**
 * 获取预留区中页面构件配置信息
 * @param {object} configInfo 构件配置信息
 * @param {string} name 预留区名称
 * @param {string} type 预留区类型 0-工具条 1-列表超链接
 * @return {Array}
 */
function CFG_getReserveZoneInfo(CFG_configInfo, name, type) {
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
        var url = $.contextPath + '/construct/construct!getReverseZoneConfigInfo.json';
        url += '?page=' + (CFG_configInfo.page||"MT_page_") + "&reserveZoneName=" + name;
        if (CFG_configInfo.CFG_urlParams['menuId'] != undefined) {
            url += '&menuId=' + CFG_configInfo.CFG_urlParams['menuId'];
        }
        if (CFG_configInfo.CFG_urlParams['constructId'] != undefined) {
            url += '&constructId=' + CFG_configInfo.CFG_urlParams['constructId'];
        }
        var reserveZone = CFG_eval($.loadJson(url));
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
 * @param {object} configInfo 构件配置信息
 * @param {string} name 预留区名称
 * @param {string} type 预留区类型 0-工具条 1-列表超链接
 * @return {boolean}
 */
function CFG_isReserveZoneBinding(CFG_configInfo, name, type) {
    var constructDetails = CFG_getReserveZoneInfo(CFG_configInfo, name, type);
    var result = false;
    if (constructDetails && constructDetails.length > 0) {
        result = true;
    }
    return result;
}
/**
 * 向工具条预留区中添加按钮
 * @param {object} configInfo 构件配置信息
 * @param {object} toolBar 工具条
 * @param {string} reserveZoneName 工具条预留区名称
 * @param {number} startIndex 工具条上按钮开始index
 */
function CFG_addToolbarButtons(CFG_configInfo, toolBar, reserveZoneName, startIndex) {
    var constructDetails = CFG_getReserveZoneInfo(CFG_configInfo, reserveZoneName, '0');
    if (constructDetails && constructDetails.length > 0) {
        var comboButtons = new Array();
        for (var o in constructDetails) {
            if (constructDetails[o].buttonType == 2) {
                comboButtons.push(constructDetails[o]);
            } else if (constructDetails[o].buttonType == 0) {
                toolBar.toolbar("add", startIndex++, {
                    "id": constructDetails[o].buttonCode,
                    "label": constructDetails[o].buttonDisplayName,
                    "type": "button"
                });
            } else if (constructDetails[o].buttonType == 1) {
                toolBar.toolbar("add", startIndex++, {
                    "id": constructDetails[o].buttonCode,
                    "label": constructDetails[o].buttonDisplayName,
                    "type": "split"
                });
            }
        }
        if (!toolBar.toolbar("isExist", "more")) {
            for (var o in comboButtons) {
                if (comboButtons[o].parentButtonCode == "more") {
                    toolBar.toolbar("add", startIndex++, {
                        "id": "more",
                        "label": "更多",
                        "type": "split"
                    });
                    break;
                }
            }
        }
        for (var o in comboButtons) {
            toolBar.toolbar("add", comboButtons[o].parentButtonCode, {
                "id": comboButtons[o].buttonCode,
                "label": comboButtons[o].buttonDisplayName,
                "type": "button"
            });
        }
    }
}
/**
 * 获取构件的URL
 * @param CFG_configInfo 构件配置信息
 * @param constructDetail
 * @param noAssembleType
 * @returns {string}
 */
function CFG_getComponentUrl(CFG_configInfo, constructDetail, noAssembleType) {
    var bindingComponentUrl = constructDetail.bindingComponentUrl;
    if (bindingComponentUrl) {
        var constructDetailId = constructDetail.constructDetailId;
        var assembleType = constructDetail.assembleType;
        if (bindingComponentUrl.indexOf("?") == -1) {
            bindingComponentUrl += "?constructDetailId=" + constructDetailId;
        } else {
            bindingComponentUrl += "&constructDetailId=" + constructDetailId;
        }
        if (!noAssembleType) {
            bindingComponentUrl += "&assembleType=" + assembleType;
        }
        if (CFG_configInfo.CFG_urlParams['menuId'] && isNotEmpty(CFG_configInfo.CFG_urlParams['menuId'])) {
            bindingComponentUrl += "&menuId=" + CFG_configInfo.CFG_urlParams['menuId'];
        }
        if (CFG_configInfo.CFG_urlParams['menuCode'] && isNotEmpty(CFG_configInfo.CFG_urlParams['menuCode'])) {
            bindingComponentUrl += "&menuCode=" + CFG_configInfo.CFG_urlParams['menuCode'];
        }
        if (CFG_configInfo.CFG_urlParams['topComVersionId'] && isNotEmpty(CFG_configInfo.CFG_urlParams['topComVersionId'])) {
            bindingComponentUrl += "&topComVersionId=" + CFG_configInfo.CFG_urlParams['topComVersionId'];
        }
    }
    return bindingComponentUrl;
}
/**
 * 获取构件的入参
 * @param constructDetail
 * @param {object} _this js作用域
 * @param {string} type 预留区类型 0-工具条 1-列表超链接
 * @param currentRowId 当前记录的ID 超链接使用
 * @returns {string}
 */
function CFG_getInputParams(constructDetail, _this, type, currentRowId) {
    var pageInputParams = {'status':true};
    var funs = constructDetail.paramFunctions;
    if (funs) {
        for (var i in funs) {
            var funcName = funs[i].functionName;
            if (type == "1" && (funcName == "value.selectedRowId" || funcName == "value.selectedRowIds" || funcName == "value.selectedRowIdsAll")) {
                pageInputParams[(funs[i].params)[0].inputParam] = currentRowId;
                continue;
            }
            var funcNameArray = funcName.split('.');
            var obj = _this;
            for (var p in funcNameArray) {
                if (obj[funcNameArray[p]]) {
                    obj = obj[funcNameArray[p]];
                    if (typeof obj == 'function' && p != (funcNameArray.length-1)) {
                        obj = obj[funcNameArray[p]]();
                        if (!obj) {
                            pageInputParams.status = false;
                            return pageInputParams;
                        }
                    }
                } else {
                    pageInputParams.status = false;
                    return pageInputParams;
                }
            }
            var retObj;
            if (typeof obj == 'function') {
                retObj = obj.apply(_this);
            } else {
                retObj = obj;
            
            }
            if (false == retObj.status) {
                pageInputParams.status = false;
                return pageInputParams;
            }
            var params = funs[i].params;
            for (var j in params) {
                obj = retObj[params[j].outputParam];
                if (typeof obj === "function") obj = obj.apply(_this);
                pageInputParams[params[j].inputParam] = obj;
            }
        }
    }
    return pageInputParams;
}
/**
 * 中转器构件转向其他构件
 * @return {object} result.success为1：不是中转器 2：没有转向到其他构件 3：转向到其他构件
 */
function transferTo(constructDetail, pageInputParams) {
    var result = {};
    result.success = 1;
    if (constructDetail.baseComponentType && constructDetail.baseComponentType == 8) {
        var assembleType = constructDetail.assembleType;
        var transferDeviceConstructDetails = constructDetail.constructDetails;
        if (transferDeviceConstructDetails && transferDeviceConstructDetails.length > 0) {
            var tranferResult = false;
            for (var o in transferDeviceConstructDetails) {
                if (pageInputParams.condition == transferDeviceConstructDetails[o].buttonCode) {
                    constructDetail = transferDeviceConstructDetails[o];
                    // constructDetail.assembleType = assembleType;
                    tranferResult = true;
                    break;
                }
            }
            if (!tranferResult) {
                result.success = 2;
                return result;
            } else {
                result.success = 3;
                var params = pageInputParams.params;
                pageInputParams = {};
                if (params) {
                    var paramArray = params.split(';');
                    for (var o in paramArray) {
                        var kv = paramArray[o].split('≡');
                        pageInputParams[kv[0]] = kv[1];
                    }
                }
            }
        }
    }
    result.pageInputParams = pageInputParams;
    result.constructDetail = constructDetail;
    return result;
}
/**
 * 向工具条预留区中添加按钮的click事件（超链接也可以用）
 * @param {object} CFG_configInfo 构件配置信息
 * @param {string} reserveZoneName 工具条预留区名称
 * @param {string} id 工具条上点击的按钮的name
 * @param {string} type 预留区类型 0-工具条 1-列表超链接
 * @param {object} _this js作用域
 * @param {string} currentRowId 当前记录的ID 超链接使用
 */
function CFG_clickToolbar(CFG_configInfo, reserveZoneName, id, type, _this, currentRowId) {
    if (!type) {
        type = 0;
    }
    var constructDetails = CFG_getReserveZoneInfo(CFG_configInfo, reserveZoneName, type);
    if (constructDetails && constructDetails.length > 0) {
        for (var o in constructDetails) {
            if (constructDetails[o].buttonCode == id) {
                var constructDetail = constructDetails[o];
                if (isEmpty(constructDetail.bindingComponentUrl)) {
                    continue;
                }
                var pageInputParams = CFG_getInputParams(constructDetail, _this, type, currentRowId);
                if (!pageInputParams.status)
                    return;
                var result = transferTo(constructDetail, pageInputParams);
                if (result.success == 3) {
                    pageInputParams = result.pageInputParams;
                    constructDetail = result.constructDetail;
                } else if (result.success == 2) {
                    CFG_message("没有找到构件！", "warning");
                    return;
                }
                var bindingComponentUrl = CFG_getComponentUrl(CFG_configInfo, constructDetail);
                CFG_configInfo.pageInputParams = pageInputParams;
                CFG_configInfo.constructDetail = constructDetail;
                CFG_configInfo.bindingComponentUrl = bindingComponentUrl;
                CFG_configInfo.title = constructDetail.buttonDisplayName;
                CFG_configInfo.type = type;
                CFG_configInfo._this = _this;
                // 调用构件之前的前置事件处理
                var beforeClickJs = constructDetail.beforeClickJs;
                if (beforeClickJs && typeof(beforeClickJs) == "string") {
                    try {
                        beforeClickJs = CFG_eval(beforeClickJs);
                    } catch (e) {
                        CFG_message("前置事件处理方法错误！", "warning");
                        return;
                    }
                }
                if (beforeClickJs && typeof(beforeClickJs) == "function") {
                    var checkResult = beforeClickJs(CFG_configInfo);
                    if (checkResult) {
                        if (true == checkResult.success) {
                            CFG_afterClickJs(CFG_configInfo);
                        }
                        if (checkResult.message && "" != checkResult.message) {
                            if (true == checkResult.success) {
                                CFG_message(checkResult.message, "success");
                            } else {
                                CFG_message(checkResult.message, "warning");
                            }
                        }
                    }
                } else {
                    CFG_afterClickJs(CFG_configInfo);
                }
            }
        }
    }
}
/**
 * 按钮预留区或树节点预留区点击事件
 * @param {object} configInfo 构件配置信息
 * @param {string} reserveZoneName 预留区名称
 * @param {string} title 工具条上点击的按钮的name
 * @param {string} type 预留区类型 2-按钮预留区 3-树节点预留区
 * @param {object} _this js作用域
 */
function CFG_clickButtonOrTreeNode(CFG_configInfo, reserveZoneName, title, type, _this) {
    if (!type) {
        type = 2;
    }
    var constructDetails = CFG_getReserveZoneInfo(CFG_configInfo, reserveZoneName, type);
    if (constructDetails && constructDetails.length > 0) {
        var constructDetail = constructDetails[0];
        var pageInputParams = CFG_getInputParams(constructDetail, _this, type);
        if (!pageInputParams.status)
            return;
        var result = transferTo(constructDetail, pageInputParams);
        if (result.success == 3) {
            pageInputParams = result.pageInputParams;
            constructDetail = result.constructDetail;
        } else if (result.success == 2) {
            CFG_message("没有找到构件！", "warning");
            return;
        }
        var bindingComponentUrl = CFG_getComponentUrl(CFG_configInfo, constructDetail, (3 == type));
        CFG_configInfo.pageInputParams = pageInputParams;
        CFG_configInfo.constructDetail = constructDetail;
        CFG_configInfo.bindingComponentUrl = bindingComponentUrl;
        CFG_configInfo.title = title;
        CFG_configInfo.type = type;
        CFG_configInfo._this = _this;
        // 调用构件之前的前置事件处理
        var beforeClickJs = constructDetail.beforeClickJs;
        if (beforeClickJs && typeof(beforeClickJs) == "string") {
            try {
                beforeClickJs = CFG_eval(beforeClickJs);
            } catch (e) {
                CFG_message("前置事件处理方法错误！", "warning");
                return;
            }
        }
        if (beforeClickJs && typeof(beforeClickJs) == "function") {
            var checkResult = beforeClickJs(CFG_configInfo);
            if (checkResult) {
                if (true == checkResult.success) {
                    CFG_afterClickJs(CFG_configInfo);
                }
                if (checkResult.message && "" != checkResult.message) {
                    if (true == checkResult.success) {
                        CFG_message(checkResult.message, "success");
                    } else {
                        CFG_message(checkResult.message, "warning");
                    }
                }
            }
        } else {
            CFG_afterClickJs(CFG_configInfo);
        }
    }
}
/**
 * 调用构件，点击工具条按钮和页面构件中树预留区节点时调用
 */
function CFG_afterClickJs(CFG_configInfo) {
    var pageInputParams = CFG_configInfo.pageInputParams;
    var constructDetail = CFG_configInfo.constructDetail;
    var bindingComponentUrl = CFG_configInfo.bindingComponentUrl;
    var title = CFG_configInfo.title;
    var type = CFG_configInfo.type;
    var _this = CFG_configInfo._this;
    var componentType = constructDetail.componentType;
    var assembleType = constructDetail.assembleType;
    var callbacks = constructDetail.paramCallbacks;
    if (componentType == "1" || componentType == "3" || componentType == "4" || componentType == "5" || componentType == "9") {
        // 假如是页面构件，访问的地址是指向后台的action，那么需要通过url的方式传递参数，所以此处url后也加参数（可能会出现url超过长度限制的情况）
        for (var k in pageInputParams) {
            bindingComponentUrl += "&" + k + "=" + pageInputParams[k];
        }
        bindingComponentUrl = encodeURI(bindingComponentUrl);
        if (assembleType == "0") {
            var dialogParent = CFG_getDialogParent(CFG_configInfo);
            var width = 800;
            var height = 600;
            var w = constructDetail.width;
            var h = constructDetail.height;
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
                if (height > dialogParent.height()) {
                    height = dialogParent.height() * 0.95;
                }
            }
            var dialogId = generateId('dialog');
            var appendToDiv = 'body';
            if (dialogParent && $(dialogParent).attr('id')) {
                appendToDiv = '#' + $(dialogParent).attr('id');
            }
            $("<div id='"+dialogId+"'></div>").appendTo(document.body);
            $('#'+dialogId).dialog({
                wtype: "dialog",
                title: title,
                width: width,
                height: height,
                modal: true,
                appendTo : appendToDiv,
                position: {
                    my: "center",
                    at: "center",
                    of: dialogParent || window,
                    collision: "fit",
                    // 确保标题栏始终可见
                    using: function( pos ) {
                        var topOffset = $( this ).css( pos ).offset().top;
                        if ( topOffset < 0 ) {
                            $( this ).css( "top", pos.top - topOffset );
                        }
                    }
                },
                onOpen: function() {
                    $.ajax({
                        url : bindingComponentUrl,
                        dataType :"html", 
                        context : document.body
                    }).done(function(html) {
                        $('#'+dialogId).data("selfUrl", bindingComponentUrl);
                        $('#'+dialogId).data("parentConfigInfo", CFG_configInfo);
                        $('#'+dialogId).append(html);
                        $.parser.parse($('#'+dialogId));
                        if (CFG_configInfo.childConfigInfo) {
                            CFG_configInfo.childConfigInfo.dialog = $('#'+dialogId);
                        }
                        if (CFG_configInfo.childConfigInfo && CFG_configInfo.childConfigInfo.CFG_bodyOnLoad) {
                            CFG_configInfo.childConfigInfo.CFG_bodyOnLoad();
                        }
                        if (CFG_configInfo.childConfigInfo && CFG_configInfo.childConfigInfo.CFG_initReserveZones) {
                            CFG_configInfo.childConfigInfo.CFG_initReserveZones();
                        }
                        if (CFG_configInfo.childConfigInfo && CFG_configInfo.childConfigInfo.CFG_setCloseButton) {
                            CFG_configInfo.childConfigInfo.CFG_setCloseButton();
                        }
                    });
                },
                beforeClose: function() {
                    var CFG_outputParams;
                    if (CFG_configInfo.childConfigInfo && CFG_configInfo.childConfigInfo.CFG_outputParams) {
                        CFG_outputParams = CFG_configInfo.childConfigInfo.CFG_outputParams;
                    }
                    for (var i in callbacks) {
                        CFG_execCallback(callbacks[i], _this, CFG_outputParams);
                    }
                    return true;
                },
                onClose: function() {
                    $('#'+dialogId).remove();
                    $.coral.refreshAllComponent();
                    //$(".coral-layout").layout("refresh");
                    $(".ctrl-init-layout:visible").layout("refresh");
                }
            });
        } else if (assembleType == "1") {
            if (window.CFG_openComponent) {
                var eventObj;
                if (type == "3") {
                    eventObj = CFG_openComponent(CFG_configInfo, bindingComponentUrl, title, true);
                } else {
                    eventObj = CFG_openComponent(CFG_configInfo, bindingComponentUrl, title);
                }
                if (callbacks) {
                    eventObj.bind("componentZoneDestory", function(){
                        // var parentConfigInfo = eventObj.data("parentConfigInfo");
                        var CFG_outputParams;
                        if (CFG_configInfo.childConfigInfo && CFG_configInfo.childConfigInfo.CFG_outputParams) {
                            CFG_outputParams = CFG_configInfo.childConfigInfo.CFG_outputParams;
                        }
                        for (var i in callbacks) {
                            CFG_execCallback(callbacks[i], _this, CFG_outputParams);
                        }
                    });
                }
            }
        } else if (assembleType == "2") {
            if (top.CFG_addTab) {
                // tab的组装方式没有callback
                top.CFG_addTab(CFG_configInfo, title, bindingComponentUrl);
            }
        }else if (assembleType == "3") {
            //弹出整个页面
            window.open(bindingComponentUrl,
                    'previewSys', "toolbar=no,location=no,directories=no,menubar=no,scrollbars=no,resizable=yes,top=0,left=0,height="+(screen.availHeight-50)+",width="+(screen.availWidth - 10)+",status=yes");
        }
    } else if (componentType == "2") {
        // 使用post方式提交
        var params = "";
        for (var k in pageInputParams) {
            params += "&" + k + "=" + pageInputParams[k];
        }
        if (params.length > 0) {
            params = params.substring(1, params.length);
        }
        bindingComponentUrl = encodeURI(bindingComponentUrl);
        $.post(bindingComponentUrl, params, function(result) {
            if (typeof result == "string") {
                result = CFG_eval(result);
            }
            if (result.message) {
                if (true == result.success) {
                    CFG_message(result.message, "success");
                } else {
                    CFG_message(result.message, "warning");
                }
            }
            if (callbacks) {
                for (var i in callbacks) {
                    CFG_execCallback(callbacks[i], _this, result);
                }
            }
        }, "json");
    }
}
/**
 * 树构件上树节点的点击事件
 * @param {object} configInfo 构件配置信息
 * @param {string} treeNodeType 树节点类型
 * @param {string} treeNodeProperty 树节点属性值 根节点——空；空节点——值；字段节点（跨表）——字段标签；表节点——表ID；物理表组节点——逻辑表组Code或ThirdParty
 * @param {string} title 导航的标题
 * @param {string} _params 打开构件时传递的参数a=a&b=b
 * @param {object} _this js作用域
 */
function CFG_clickTreeNode(CFG_configInfo, treeNodeType, treeNodeProperty, title, params, _this) {
    var constructDetails = CFG_getReserveZoneInfo(CFG_configInfo, "TREE", "TREE");
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
                if (constructDetails[o].treeNodeProperty == "ThirdParty" && constructDetails[o].treeNodeType == treeNodeType) {
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
        var pageInputParams = CFG_getInputParams(constructDetail, _this);
        if (!pageInputParams.status)
            return;
        var result = transferTo(constructDetail, pageInputParams);
        if (result.success == 3) {
            pageInputParams = result.pageInputParams;
            constructDetail = result.constructDetail;
        } else if (result.success == 2) {
            CFG_message("没有找到构件！", "warning");
            return;
        }
        var bindingComponentUrl = CFG_getComponentUrl(CFG_configInfo, constructDetail, true);
        if (params && typeof params == "string") {
            bindingComponentUrl += "&" + params;
        }
        CFG_configInfo.pageInputParams = pageInputParams;
        CFG_configInfo.constructDetail = constructDetail;
        CFG_configInfo.bindingComponentUrl = bindingComponentUrl;
        CFG_configInfo.title = title;
        // 调用构件之前的前置事件处理
        var beforeClickJs = constructDetail.beforeClickJs;
        if (beforeClickJs && typeof(beforeClickJs) == "string") {
            try {
                beforeClickJs = CFG_eval(beforeClickJs);
            } catch (e) {
                CFG_message("前置事件处理方法错误！", "warning");
                return;
            }
        }
        if (beforeClickJs && typeof(beforeClickJs) == "function") {
            var checkResult = beforeClickJs(CFG_configInfo);
            if (checkResult) {
                if (checkResult.message && "" != checkResult.message) {
                    if (true == checkResult.success) {
                        CFG_message(checkResult.message, "success");
                    } else {
                        CFG_message(checkResult.message, "warning");
                    }
                }
                if (false == checkResult.success)
                    return;
            }
        }
        var callbacks = constructDetail.paramCallbacks;
        var componentType = constructDetail.componentType;
        if (componentType == "1" || componentType == "3" || componentType == "4" || componentType == "5" || componentType == "9") {
            // 假如是页面构件，访问的地址是指向后台的action，那么需要通过url的方式传递参数，所以此处url后也加参数（可能会出现url超过长度限制的情况）
            for (var k in pageInputParams) {
                bindingComponentUrl += "&" + k + "=" + pageInputParams[k];
            }
            bindingComponentUrl = encodeURI(bindingComponentUrl);
            if (window.CFG_openComponent) {
                var eventObj = CFG_openComponent(CFG_configInfo, bindingComponentUrl, title, true);
                if (callbacks) {
                    eventObj.bind("componentZoneDestory", function(){
                        // var parentConfigInfo = eventObj.data("parentConfigInfo");
                        var CFG_outputParams;
                        if (CFG_configInfo.childConfigInfo && CFG_configInfo.childConfigInfo.CFG_outputParams) {
                            CFG_outputParams = CFG_configInfo.childConfigInfo.CFG_outputParams;
                        }
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
 * 按钮预留区或树节点预留区点击事件
 * @param {object} configInfo 构件配置信息
 * @param {string} reserveZoneName 预留区名称
 * @param {object} tabbar 标签页
 * @param {string} type 预留区类型 4-标签页预留区
 * @param {object} _this js作用域
 */
function CFG_addTabbar(CFG_configInfo, reserveZoneName, tabbar, type, _this) {
    if (!type) {
        type = 4;
    }
    var constructDetails = CFG_getReserveZoneInfo(CFG_configInfo, reserveZoneName, type);
    if (constructDetails && constructDetails.length > 0) {
        for (var o in constructDetails) {
            var constructDetail = constructDetails[o];
            var pageInputParams = CFG_getInputParams(constructDetail, _this, type);
            if (!pageInputParams.status)
                return;
            var result = transferTo(constructDetail, pageInputParams);
            if (result.success == 3) {
                pageInputParams = result.pageInputParams;
                constructDetail = result.constructDetail;
            } else if (result.success == 2) {
                CFG_message("没有找到构件！", "warning");
                return;
            }
            var bindingComponentUrl = CFG_getComponentUrl(CFG_configInfo, constructDetail, true);
            var inputParams = constructDetail.inputParams;
            if (inputParams) {
                for (var i in inputParams) {
                    var inputParam = inputParams[i];
                    var value = CFG_getInputParamValue(CFG_configInfo, constructDetail.buttonCode + "--" + inputParam.name);
                    if (!value) {
                        value = inputParams[i].value;
                    }
                    pageInputParams[inputParam.name] = value;
                }
            }
            CFG_configInfo.pageInputParams = pageInputParams;
            CFG_configInfo.constructDetail = constructDetail;
            CFG_configInfo.bindingComponentUrl = bindingComponentUrl;
            CFG_configInfo.title = constructDetail.buttonName;
            // 调用构件之前的前置事件处理
            var beforeClickJs = constructDetail.beforeClickJs;
            if (beforeClickJs && typeof(beforeClickJs) == "string") {
                try {
                    beforeClickJs = CFG_eval(beforeClickJs);
                } catch (e) {
                    CFG_message("前置事件处理方法错误！", "warning");
                    return;
                }
            }
            if (beforeClickJs && typeof(beforeClickJs) == "function") {
                var checkResult = beforeClickJs(CFG_configInfo);
                if (checkResult) {
                    if (checkResult.message && "" != checkResult.message) {
                        if (true == checkResult.success) {
                            CFG_message(checkResult.message, "success");
                        } else {
                            CFG_message(checkResult.message, "warning");
                        }
                    }
                    if (false == checkResult.success)
                        return;
                }
            }
            var callbacks = constructDetail.paramCallbacks;
            var componentType = constructDetail.componentType;
            if (componentType == "1" || componentType == "3" || componentType == "4" || componentType == "5" || componentType == "9") {
                // 假如是页面构件，访问的地址是指向后台的action，那么需要通过url的方式传递参数，所以此处url后也加参数（可能会出现url超过长度限制的情况）
                for (var k in pageInputParams) {
                    bindingComponentUrl += "&" + k + "=" + pageInputParams[k];
                }
                bindingComponentUrl = encodeURI(bindingComponentUrl);
                if (tabbar.data(bindingComponentUrl)) {
                    tabbar.tabs("option", "active", "#" + tabbar.data(bindingComponentUrl));
                    if ($("#" + tabbar.data(bindingComponentUrl)).data("parentConfigInfo")
                            && $("#" + tabbar.data(bindingComponentUrl)).data("parentConfigInfo").childConfigInfo
                            && $("#" + tabbar.data(bindingComponentUrl)).data("parentConfigInfo").childConfigInfo.CFG_bodyOnLoad) {
                        $("#" + tabbar.data(bindingComponentUrl)).data("parentConfigInfo").childConfigInfo.CFG_bodyOnLoad();
                    }
                } else {
                    var tabId = generateId("tab");
                    if (tabbar.tabs("getAllTabId").length == 0) {
                        tabbar.tabs("add", {
                            label : constructDetail.buttonDisplayName,
                            content : "",
                            href : "#" + tabId,
                            closeable : false
                        });
                        tabbar.tabs("option", "active", "#" + tabId);
                        $.ajax({
                            url : bindingComponentUrl,
                            dataType : "html",
                            context : document.body,
                            async : false
                        }).done(function(html) {
                            $("#" + tabId).data("selfUrl", bindingComponentUrl);
                            $("#" + tabId).data("parentConfigInfo", CFG_configInfo);
                            $("#" + tabId).append(html);
                            $.parser.parse($("#" + tabId));
                            if (!CFG_configInfo.childConfigInfos) {
                                CFG_configInfo.childConfigInfos = new Array();
                            }
                            if (CFG_configInfo.childConfigInfo) {
                                CFG_configInfo.childConfigInfo.title = constructDetail.buttonName;
                                CFG_configInfo.childConfigInfos[CFG_configInfo.childConfigInfos.length] = CFG_configInfo.childConfigInfo;
                            }
                            if (CFG_configInfo.childConfigInfo && CFG_configInfo.childConfigInfo.CFG_bodyOnLoad) {
                                CFG_configInfo.childConfigInfo.CFG_bodyOnLoad();
                            }
                            if (CFG_configInfo.childConfigInfo && CFG_configInfo.childConfigInfo.CFG_initReserveZones) {
                                CFG_configInfo.childConfigInfo.CFG_initReserveZones();
                            }
                        });
                        tabbar.tabs("option", "active", "#" + tabId);
                        tabbar.data(bindingComponentUrl, tabId);
                    } else {
                        tabbar.tabs("add", {
                            label : constructDetail.buttonDisplayName,
                            content : "",
                            href : "#" + tabId,
                            closeable : false
                        });
                        tabbar.data(bindingComponentUrl, tabId);
                        tabbar.data(tabId, {'url':bindingComponentUrl, 'loadComponent':false, 'title':constructDetail.buttonName});
                    }
                }
            }
        }
        tabbar.tabs("option", "onActivate", function(event, ui) {
            var tabId = ui.newTab.context.hash.replace("#", "");
            if (tabbar.data(tabId) &&　!tabbar.data(tabId).loadComponent) {
                var bindingComponentUrl = tabbar.data(tabId).url;
                $.ajax({
                    url : bindingComponentUrl,
                    dataType : "html",
                    context : document.body,
                    async : false
                }).done(function(html) {
                    $("#" + tabId).data("selfUrl", bindingComponentUrl);
                    $("#" + tabId).data("parentConfigInfo", CFG_configInfo);
                    $("#" + tabId).append(html);
                    $.parser.parse($("#" + tabId));
                    if (!CFG_configInfo.childConfigInfos) {
                        CFG_configInfo.childConfigInfos = new Array();
                    }
                    if (CFG_configInfo.childConfigInfo) {
                        CFG_configInfo.childConfigInfo.title = tabbar.data(tabId).title;
                        CFG_configInfo.childConfigInfos[CFG_configInfo.childConfigInfos.length] = CFG_configInfo.childConfigInfo;
                    }
                    if (CFG_configInfo.childConfigInfo && CFG_configInfo.childConfigInfo.CFG_bodyOnLoad) {
                        CFG_configInfo.childConfigInfo.CFG_bodyOnLoad();
                    }
                    if (CFG_configInfo.childConfigInfo && CFG_configInfo.childConfigInfo.CFG_initReserveZones) {
                        CFG_configInfo.childConfigInfo.CFG_initReserveZones();
                    }
                });
                tabbar.data(tabId, {'url':bindingComponentUrl, 'loadComponent':true});
            }
        });
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
        var o = {};
        if (outputParams) {
            var params = callback.params;
            for (var j in params) {
                o[params[j].inputParam] = outputParams[params[j].outputParam];
            }
        }
        var inputParams = [o];
        obj.apply(_this, inputParams);
    }
}
/**
 * 设置关闭按钮
 */
function CFG_setCloseButton(configInfo, CFG_returnButtonToolbar) {
    if (CFG_getInputParamValue(configInfo, "assembleType") == "0") {
        var toolbarLength = CFG_returnButtonToolbar.toolbar("getLength");
        toolbarLength = toolbarLength || 0;
        CFG_returnButtonToolbar.toolbar("add", toolbarLength, {
            "id": "CFG_closeComponentDialog",
            "label": "关闭",
            "type": "button"
        });
    } else {
        CFG_setReturnButton(configInfo, CFG_returnButtonToolbar);
    }
}
/**
 * 点击关闭按钮
 * @param configInfo
 */
function CFG_clickCloseButton(configInfo) {
    if (configInfo && configInfo.parentConfigInfo) {
        if (configInfo.hasEmbeddedDiv) {
            configInfo.hasEmbeddedDiv = false;
            CFG_clickReturnButton(configInfo);
        } else {
            if (configInfo.assembleType == "0") {
                if (configInfo.dialog) {
                    $(configInfo.dialog).dialog('close');
                }
            } else {
                CFG_clickReturnButton(configInfo.parentConfigInfo);
            }
        }
    }
}
/**
 * 弹出框在最外面的最大元素里打开
 * @param CFG_configInfo
 */
function CFG_getDialogParent(CFG_configInfo) {
    var dialogParent;
    if (CFG_configInfo && CFG_configInfo.parentConfigInfo) {
        dialogParent = CFG_getDialogParent(CFG_configInfo.parentConfigInfo);
        if (!dialogParent) {
            dialogParent = CFG_configInfo.dialogParent;
            if (!dialogParent && CFG_configInfo.childConfigInfo) {
                dialogParent = CFG_configInfo.childConfigInfo.dialogParent;
            }
        }
    }
    return dialogParent || $("body");
}
/**
 * 列表超链接按钮下拉按钮 显示
 * @param _this
 */
function CFG_clickSplitDownImgButton(e, _this) {
    var _t = $(_this);
    var _d = $(_this).next("div").first();
    var _tOffset = _t.offset();
    if (_d.css("display") == "none") {
        _d.show();
        _d.offset({top:_tOffset.top+31, left:_tOffset.left});
        var h = 0;
        _d.children().each(function(i, dom) {
            h += $(dom).height();
        });
        _d.height(h);
    } else {
        _d.hide();
    }
    e = e || window.event;
    if (e.stopPropagation) {
        e.stopPropagation();
    } else if (e.preventDefault) {
        e.preventDefault();
    }
}
/**
 * 列表超链接按钮下拉按钮 隐藏
 * @param _this
 */
function CFG_hideSplitDownImgButton(_this) {
    $(_this).next("div").first().hide();
}