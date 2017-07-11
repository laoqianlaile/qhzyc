// 增加cookie
function addCookie(objName, objValue, objHours) {
    var str = objName + "=" + escape(objValue) + "; path=/";
    if (objHours > 0) {
        var date = new Date();
        var ms = objHours * 3600 * 1000;
        date.setTime(date.getTime() + ms);
        str += "; expires=" + date.toGMTString();
    }
    document.cookie = str;
}
// 读取cookie
function getCookie(objName) {
    var arrStr = document.cookie.split("; ");
    for (var i = 0; i < arrStr.length; i++) {
        var temp = arrStr[i].split("=");
        if (temp[0] == objName)
            return unescape(temp[1]);
    }
}
String.prototype.trim = function() {
    return this.replace(/(^\s*)|(\s*$)/g, '');
};
/**
 * 禁用浏览器的右键菜单
 * @param {event} e 事件
 * @return {Boolean}
 */
document.oncontextmenu = function(e) {
    if (e) {
        e.returnValue = false;
    } else {
        window.event.returnValue = false;
    }
    return false;
}
/**
 * 下载
 * @param {} url 下载表单url
 * @param {} params 参数
 * @param {} target 目标表单存放位置
 */
function download(url, params, target) {
    var obj = document.getElementById("frm_download");
    if (obj == null) {
        obj = document.createElement("iframe");
        obj.setAttribute("id", "frm_download");
        obj.setAttribute("style", "display: none;");
        document.body.appendChild(obj);
    }
    if (params) {
        var submitForm = document.createElement("FORM");
        submitForm.target = target || 'frm_download';
        submitForm.action = url;
        document.body.appendChild(submitForm);
        submitForm.method = "POST";
        for (p in params) {
            if (!params[p] || !p)
                continue;
            var input = document.createElement('input');
            input.setAttribute('name', p);
            input.setAttribute('type', 'hidden');
            if (typeof(params[p]) == 'string') {
                input.setAttribute('value', params[p].replace(/\"/g, '\\"'));
            } else {
                input.setAttribute('value', params[p]);
            }
            submitForm.appendChild(input);
        }
        submitForm.submit();
        document.body.removeChild(submitForm);
    } else {
        var frame = document.getElementById('frm_download');
        frame.src = url;
    }
}
/**
 * @returns 当前时间戳
 */
function getTimestamp() {
    return new Date().getTime();
}
/**
 * 生成ID
 * @returns 生成的ID
 */
function generateId(prefix) {
	return prefix + "_" + getTimestamp();
}
/**
 * url中添加时间戳
 * @param url
 * @returns {String}
 */
function addTimestamp(url) {
    if (url.indexOf("?") > 0)
        return (url + "&timestamp=" + getTimestamp());
    return (url + "?timestamp=" + getTimestamp());
}
/**
 * 将null或"null"字符串置为""
 * @param obj
 * @returns
 */
function null2empty(obj) {
    if (!obj || "null" == obj)
        return "";
    for (var p in obj) {
        if (obj[p] instanceof Object) {
            obj[p] = null2empty(obj[p]);
        } else if (!obj[p] || "null" == obj[p]) {
            obj[p] = "";
        }
    }
    return obj;
}
/**
 * 将null或"null"字符串置为"0"
 * @param obj
 * @returns
 */
function null2zero(obj) {
    return isEmpty(obj) ? "0" : obj;
}
/**
 * 判断是否为空串（null/"null"/""都作为空串处理）
 * @param obj
 * @returns {Boolean}
 */
function isEmpty(obj) {
    if (undefined === obj || null === obj || 0 === obj.length || "null" === obj) {
        return true;
    }
    return false;
}
/**
 * 判断是否不为空串（null/"null"/""都作为空串处理）
 * @param obj
 * @returns
 */
function isNotEmpty(obj) {
    return !isEmpty(obj);
}
/**
 * 上传文件对话框
 * @param jq -- 窗口Jquery对象
 * @param uploadOptions -- uploadify 组件参数
 * @param dialogOptions -- dialog 组件参数
 */
function CFG_upload(jq, uploadOptions, dialogOptions) {
	if (!jq) jq = $(document.body);
    var dId = generateId('dialog'),
        fId = generateId('file'),
        _uOptions, _dOption;
    // uploadify 默认参数配置
    _uOptions = {
  		  auto : false,
		  height : 30,
		  buttonText : "浏览文件",
		  buttonCursor: "pointer",
		  swf    : $.contextPath + "/cfg-resource/coral40/_cui_library/ui/uploadify.swf",
		  fileObjName : "uploadify",
		  width  : 120,
		  queueSizeLimit : 25,    // 上传数量
	      fileSizeLimit  : "2GB",// 上传文件的大小限制
	      removeCompleted: false, // 不自动将已完成任务从队列中删除
		  //返回一个错误，选择文件的时候触发
	      onSelectError : function(file, errorCode, errorMsg) {
	          switch(errorCode) {
	              case -100:
	            	  CFG_message("上传的文件数量已经超出系统限制的" + this.settings.queueSizeLimit + "个文件！", "error");
	                  break;
	              case -110:
	            	  CFG_message("文件 [" + file.name + "] 大小超出系统限制的" + this.settings.fileSizeLimit + "大小！", "error");
	                  break;
	              case -120:
	            	  CFG_message("文件 [" + file.name + "] 大小异常！", "error");
	                  break;
	              case -130:
	            	  CFG_message("文件 [" + file.name + "] 类型不正确！", "error");
	                  break;
	          }
	      },
	      // 检测FLASH失败调用
	      onFallback : function() {
	    	  CFG_message("您未安装FLASH控件，无法上传文件！请安装FLASH控件后再试。", "error");
	      },
	      //
	      onSelect : function () {
	    	  
	      },
	      // 上传到服务器，服务器返回相应信息到data里
	      onUploadSuccess : function(file, data, response) {
	          //alert(data);
	      },
	      // 上传失败
	      onUploadError : function(file, errorCode, errorMsg, errorStr) {
	    	  if (errorStr == "Cancelled") return;
	    	  CFG_message("文件（" + file.name + "）上传失败，错误信息（" + errorStr + "）", "warning");
	      },
	      // 上传队列完成
	      onQueueComplete : function(queueData) {
	    	  CFG_message("文件上传成功数量为 " + queueData.uploadsSuccessful + "，失败数据 " + queueData.uploadsErrored + ".", "success");
	      }
	 };
    // 对话默认参数配置
    _dOptions = {
    		width :  400,
            height : 300,
            modal : true,
            title : "文件上传",
            appendTo : jq,
            position : {
                of : jq
            },
            buttons : {
                "上传" : function() {
                	var jq = $("#" + fId),
                	    up = jq.data("uploadify");
                	if (up.queueData.filesSelected > 0) {
                		jq.uploadify("upload", "*");
                	} else {
                		CFG_message("请先选择文件再上传！", "warning");
                	}
                }/*,
                "取消" : function() {
                	var jq = $("#" + fId),
                	    up = jq.data("uploadify");
                	if (up.queueData.filesSelected > 0) {
                		jq.uploadify("cancel", "*");
                	} else {
                		CFG_message("请先选择文件再取消！", "warning");
                	}
                }*/,
                "关闭" : function() {
                    $(this).dialog("close");
                }
            },
            onClose : function() {
            	var jqUpload = $("#" + fId);
            	if (jqUpload.length > 0) {
            		jqUpload.uploadify("destroy");
            		jqUpload.remove();
            	}
                $("#" + dId).remove();
            }
    };
    
    $.extend(_uOptions, uploadOptions);
    $.extend(_dOptions, dialogOptions);
    
    $("<div id=\"" + dId + "\"><input type=\"file\" id=\"" + fId + "\"/></div>").appendTo(jq);
    
    $("#" + fId).uploadify(_uOptions);
    $("#" + dId).dialog(_dOptions);
}
/**
 * 系统配置平台统一提示窗口方法接口
 * @param message
 * @param type --success 成功
 *             --warning 警告/失败
 *             --error   错误
 **/
function CFG_message(msg, type) {
    $.config.defaults.message(msg, type);
}
/**
 * eval(兼容IE8)
 * @param srcStr
 * @returns object
 */
function CFG_eval(srcStr) {
    if ($.browser.msie8) {
        return eval("0, (" + srcStr + ")");
    } else {
        return eval("(" + srcStr + ")");
    }
}