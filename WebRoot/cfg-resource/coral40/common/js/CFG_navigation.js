(function($) {
    $.navigation = $.navigation || {};
    $.extend($.navigation, {
        separator : '<div class="navigationbar_separator">-</div>',
        getAccessor : function(obj, expr) {
            var ret, p, prm, i;
            if (typeof expr === 'function') {
                return expr(obj);
            }
            ret = obj[expr];
            if (ret === undefined) {
                try {
                    if (typeof expr === 'string') {
                        prm = expr.split('.');
                    }
                    i = prm.length;
                    if (i) {
                        ret = obj;
                        while (ret && i--) {
                            p = prm.shift();
                            ret = ret[p];
                        }
                    }
                } catch (e) {
                }
            }
            return ret;
        },
        extend : function(methods) {
            $.extend($.fn.navigationbar, methods);
            $.fn.extend(methods);
        }
    });
    $.fn.navigationbar = function(pin) {
        if (typeof pin == 'string') {
            var fn = $.navigation.getAccessor($.fn.navigationbar, pin);
            if (!fn) {
                throw ("navigationbar - No such method: " + pin);
            }
            var args = $.makeArray(arguments).slice(1);
            return fn.apply(this, args);
        }
        if (this.bar) {
            return;
        }
        return this.each(function() {
            var bar = $('<div class="navigationbar"></div>');

            bar.prepend('<div class="navigationbar_head">&nbsp;&nbsp;您的位置：</div>');

            this.bar = bar;
            $(this).append(bar);
            var _this = this;
            $(window).unload(function() {
                _this = null;
            });
        });
    }
    $.navigation.extend({
        addNavigation : function(node) {
            if (!node || !node.name || typeof node.index == 'undefined') {
                return;
            }
            this.each(function() {
                if (this.bar.children('.navigationbar_element').length == 0) {
                    var refreshIcon = $('<div class="navigationbar_refresh"></div>');
                    this.bar.append(refreshIcon);
                    refreshIcon.click(function(){
                        refreshIcon.next('.navigationbar_element').first().trigger('click');
                    });
                }
                var n = $('<div class="navigationbar_element">' + node.name + '</div>');
                if (this.bar.children('.navigationbar_element').length > node.index) {
                    if (node.index == 0) {
                        this.bar.children('.navigationbar_element').remove();
                        this.bar.children('.navigationbar_separator').remove();
                    } else {
                        var tn = this.bar.children('.navigationbar_element')[node.index - 1];
                        $(tn).nextAll().remove();
                    }
                }
                if (this.bar.children('.navigationbar_element').length > 0) {
                    this.bar.append($.navigation.separator);
                }
                var _this = this;
                if (this.bar.children('.navigationbar_element').length > 1) {
                    var pren = this.bar.children('.navigationbar_element')[node.index - 1];
                    // $(pren).css("color", "blue");
                }
                this.bar.append(n);
                n.configInfo = node.configInfo;
                n.click(function() {
                    /*if (_this.bar.children('.navigationbar_element').index(n) == _this.bar
                            .children('.navigationbar_element').length
                            - 1) {
                        return;
                    }*/
                    if (node.configInfo) {
                        if (node.index == '0') {
                            if (CFG_refreshTab && node.configInfo.parentConfigInfo) {
                                var tabId = node.configInfo.parentConfigInfo.tabId;
                                var url = node.configInfo.parentConfigInfo.url;
                                var tabName = node.configInfo.parentConfigInfo.name;
                                CFG_refreshTab(tabId, tabName, url);
                            } else {
                                removeNavigation($(this));
                                if (window.CFG_clickReturnButton) {
                                    window.CFG_clickReturnButton(node.configInfo, true);
                                }
                            }
                        } else {
                            removeNavigation($(this));
                            node.configInfo.currentComponentDivIndex = node.index + 1;
                            if (window.CFG_clickReturnButton) {
                                window.CFG_clickReturnButton(node.configInfo, true);
                            }
                        }
                    }
                });
            });
        },
        removeAllNavigation : function() {
            this.each(function() {
                this.bar.children('.navigationbar_element').remove();
                this.bar.children('.navigationbar_separator').remove();
            });
        },
        getSize : function() {
            var size = [];
            this.each(function() {
            	if (this.bar) {
                    size[size.length] = this.bar.children('.navigationbar_element').length;
            	}
            });
            return size;
        },
        goBack : function(index, clickReturnButton) {
            this.each(function() {
                if (index < this.bar.children('.navigationbar_element').length) {
                    if (index == 0) {
                        this.bar.children('.navigationbar_element').remove();
                        this.bar.children('.navigationbar_separator').remove();
                    } else {
                        var tn = this.bar.children('.navigationbar_element')[index - 1];
                        $(tn).nextAll().remove();
                        if (!clickReturnButton) {
                            $(tn).trigger('click');
                        }
                    }
                }
            });
        }
    });
    /**
     * 移除该导航节点后的所有节点
     * 
     * @param node 导航节点
     */
    function removeNavigation(node) {
        $(node).nextAll().remove();
        // $(node).css("color", "black")
    }
})(jQuery);