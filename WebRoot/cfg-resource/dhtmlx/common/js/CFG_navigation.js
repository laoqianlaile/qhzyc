/**
 * 获取导航条
 */
(function CFG_NVG_getNaviagationBar() {
	var win = window.parent;
	while (1 == 1) {
		if (win.CFG_navigationBar) {
			window.CFG_navigationBar = win.CFG_navigationBar;
			break;
		}
		if (win == top) {
			break;
		}
		win = win.parent;
	}
})(window);
/**
 * 获取导航条上条目ID
 * @param pos 位置
 * @returns {String}
 */
function CFG_NVG_getItemId(pos) {
	return "CFG_NVG_ITEM_" + pos;
}
/**
 * 向导航条上添加导航
 * @param text 导航条目名称
 * @return 导航条目的位置
 */
function CFG_NVG_addItem(text) {
	if (window.CFG_navigationBar) {
		CFG_navigationBar.CFG_navigationItemPosition++;
		CFG_navigationBar.addNavigation(CFG_NVG_getItemId(CFG_navigationBar.CFG_navigationItemPosition), CFG_navigationBar.CFG_navigationItemPosition, text);
		CFG_navigationBar.CFG_navigationWindows[CFG_navigationBar.CFG_navigationItemPosition] = window;
		return CFG_navigationBar.CFG_navigationItemPosition;
	}
	return null;
}
/**
 * 更换导航条上某位置上导航的名称
 * @param pos 导航条目位置
 * @param text 更改后的名称
 */
function CFG_NVG_setText(pos, text) {
	if (window.CFG_navigationBar) {
		var itemId = CFG_NVG_getItemId(pos);
		CFG_navigationBar.setItemText(itemId, text);
		CFG_NVG_removeAfterPos(pos);
	}
}
/**
 * 删除导航条上某位置后的所有导航
 * @param pos 导航条目位置
 */
function CFG_NVG_removeAfterPos(pos) {
	if (window.CFG_navigationBar
			&& pos < CFG_navigationBar.CFG_navigationItemPosition) {
		for ( var i = pos + 1; i <= CFG_navigationBar.CFG_navigationItemPosition; i++) {
			var itemId = CFG_NVG_getItemId(i);
			CFG_navigationBar.removeItem(itemId);
		}
		CFG_returnToMainComponent(CFG_navigationBar.CFG_navigationWindows[pos + 1]);
		CFG_navigationBar.CFG_navigationWindows = CFG_navigationBar.CFG_navigationWindows
				.slice(0, pos + 1);
		CFG_navigationBar.CFG_navigationItemPosition = pos;
	}
}
/**
 * 页面上清除嵌入的构件时，清除导航条上无效的条目
 * @param win
 */
function CFG_NVG_clearItem(win) {
	if (window.CFG_navigationBar) {
		for (var i = CFG_navigationBar.CFG_navigationItemPosition; i >= 0; i--) {
			if (CFG_navigationBar.CFG_navigationWindows[i] == win) {
				for ( var j = i; j <= CFG_navigationBar.CFG_navigationItemPosition; j++) {
					var itemId = CFG_NVG_getItemId(j);
					CFG_navigationBar.removeItem(itemId);
				}
				if (i == 0) {
					CFG_navigationBar.CFG_navigationWindows = [];
				} else {
					CFG_navigationBar.CFG_navigationWindows = CFG_navigationBar.CFG_navigationWindows
					.slice(0, i);
				}
				CFG_navigationBar.CFG_navigationItemPosition = i - 1;
				break;
			}
		}
	}
}
/**
 * 导航条上点击事件触发的方法
 * @param obj 导航条目
 */
function CFG_NVG_doClick(obj) {
	var posStr = obj.getAttribute("position");
	if (isEmpty(posStr))
		posStr = -1;
	var pos = parseInt(posStr);
	if (pos == CFG_navigationBar.CFG_navigationItemPosition) {
		return;
	}
	CFG_NVG_removeAfterPos(pos);
}