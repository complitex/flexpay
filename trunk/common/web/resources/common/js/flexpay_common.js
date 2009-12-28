var FP = {

	// key code constants
	TAB_KEY_CODE: 9,
	ENTER_KEY_CODE: 13,
	BACKSPACE_KEY_CODE: 8,
	DELETE_KEY_CODE: 46,
	LEFT_KEY_CODE: 37,
	RIGHT_KEY_CODE: 39,
	HOME_KEY_CODE: 36,
	END_KEY_CODE: 35,

    base : "",

    messages : {
        loading : ""
    },

    // Set checkboxes group (names starts with prefix) state to checked
    setCheckboxes : function (checked, prefix) {
        $("input[type=checkbox][name^=" + prefix + "]").each(function() {
            this.checked = checked;
        });
    },

    validateSubmit : function (warnMes) {
        if ($("#setupType").val() == "setupType") {
            return true;
        }

        var radio = $("input[type=radio][name=object.id]:checked");

        if (!radio) {
            alert(warnMes);
            $("#setupType").val("setupType");
            return false;
        }

        return true;
    },

    sorters : [],

    activateSorter : function (sorter) {
        // disable all sorters
        $.protify(this.sorters).each(function (field) {
            $("#" + field).val(0);
        });
        // set active passed sorter
        $("#" + sorter).val(1);
    },

    /**
     *  This function for including params in i18n-messages
     *
     *  Example:
     *      in i18n-file:
     *          i18n.message_key = This is the message with param1 = ||, param2 = || and param3 = ||
     *
     *      in javascript-block on the web-page or javascript file:
     *          document.write(FP.formatI18nMessage("<s:text name="i18n.message_key" />", ["do", "cu", "ment"]));
     *
     *  Result:
     *      This is the message with param1 = do, param2 = cu and param3 = ment
     *
     * @param message i18n-format message, which we want include params
     * @param params params for including
     */
    formatI18nMessage : function(message, params) {
        var start = 0;
        var end = 0;
        var buffer = new Array();
        for (var i = 0; i < params.length; i++) {
            end = message.indexOf("||", start);
            buffer[buffer.length] = message.substring(start, end);
            buffer[buffer.length] = params[i];
            start = end + 2;
        }
        buffer[buffer.length] = message.substring(start);
        return buffer.join("");
    },

    calendars : function(id, withImg, options) {

        options = options || {};

        if (withImg) {
            options = $.extend({
                showOn: "both",
                buttonImage: FP.base + "/resources/common/js/jquery/ui/datepicker/images/calendar.gif",
                buttonImageOnly: true
            }, options);
        }

        options = $.extend({
            dateFormat: "yy/mm/dd",
            showButtonPanel: true,
            changeMonth: true,
            changeYear: true
        }, options);

        $("#" + id).ready(function() {
            $("#" + id).datepicker(options);
        });
    },

	/**
	 * Disable enter key event
	 *
	 * Usage: <input type="text" name="mytext" onKeyPress="return disableEnterKey(event)">
	 *
	 * @param e Event
	 */
	disableEnterKey : function(e) {
		return e.keyCode != 13;
	},

    pagerSubmitForm : function(element) {
        if (element.name != "pager.pageSize") {
            $("#pageNumber").val(element.value);
            element.form.submit();
            return true;
        }
        $('select[name=pager.pageSize]').each(function() {
            if (this != element) {
                this.name = null;
            }
        });
        element.name = "pager.pageSize";
        $("#pageSizeChanged").val(true);
        element.form.submit();
    },

    createShadow : function(id){
        $("body").append('<div id="' + id + '" class="shadow"><img src="' + this.base + '/resources/common/img/indicator_big.gif" width="32" height="32" />' + this.messages.loading + '</div>');
    },

    resizeShadow : function(shadowId, elementId, params) {
        var s = $("#" + shadowId);
        if (s.length == 0) {
            FP.createShadow(shadowId);
            s = $("#" + shadowId);
        }
        if (elementId != null) {
            var el = $("#" + elementId);
            s.css("height", el.innerHeight()).css("width", el.innerWidth()).
                    css("left", el.position().left).css("top", el.position().top);
        }
        if (params["background-color"] == null) {
            params["background-color"] = "#eeeded";
        }
        s.css(params);
    },

    showShadowText : function(shadowId, elementId) {
        FP.resizeShadow(shadowId, elementId, {visibility: "visible", "background-color":""});
    },

    showShadow : function(shadowId, elementId) {
        FP.resizeShadow(shadowId, elementId, {visibility:"visible"});
    },

    hideShadow : function(shadowId) {
        FP.resizeShadow(shadowId, null, {visibility:"hidden"});
    },

    pagerAjax : function (element, opt) {
        opt = opt || {};

        opt = $.extend({
            action: "",
            shadowId: "shadow",
            resultId: "result",
            pageSizeChangedName:"pageSizeChanged",
            pageNumberName:"pager.pageNumber",
            pageSizeName:"pager.pageSize",
            notPagerRequest:false,
            params: {}
        }, opt);

        var params = opt.params;
        var shadowId = opt.shadowId;
        var resultId = opt.resultId;

        if ($("#" + resultId).innerHeight() < 40) {
            FP.showShadowText(shadowId, resultId);
        } else {
            FP.showShadow(shadowId, resultId);
        }

        var pageSizeName = opt.pageSizeName;

        if (element == null || element == undefined) {
            params[opt.pageSizeChangedName] = false;
            var curPage = $("input[name=curPage]");
            params[opt.pageNumberName] = curPage.get(0) != null && curPage.get(0) != undefined ? curPage.val() : 1;
            var pageSize = $("select[name=" + pageSizeName + "]");
            if (pageSize.get(0) != null && pageSize.get(0) != undefined) {
                params[pageSizeName] = pageSize.val();
            }
        } else {

            var isSelect = element.name == pageSizeName;
            var elValue = element.value;

            params[opt.pageSizeChangedName] = isSelect;
            if (!isSelect) {
                params[opt.pageNumberName] = elValue;
            }
            params[pageSizeName] = isSelect ? elValue : $("select[name=" + pageSizeName + "]").val();
        }

        $.post(opt.action, params, function(data) {
                    $("#" + resultId).html(data);
                    FP.hideShadow(shadowId);
                });

    },

    switchSorter : function(arg) {
        $(function() {
            if (arg == null || arg.length == 0) {
                return;
            }
            var ids = [];
            if (typeof arg === "string") {
                ids[0] = arg;
            } else {
                ids = arg;
            }
            $.protify(ids).each(function(id) {
                $('input[id="' + id + '"]').each(function() {
                    if ($.browser.msie) {
                        var f = new Function(this.onclick.getBody());
                        this.onclick = function() {
                            f();
                            sorterAjax();
                        };
                    } else {
                        this.setAttribute("onclick", this.getAttribute("onclick") + "sorterAjax();");
                    }
                });
            });
        });
    },

    deleteElements : function(action, name, callback, opt) {

        opt = opt || {};

        opt = $.extend({
            shadowId: "shadow",
            resultId: "result",
            params: {}
        }, opt);

        FP.showShadow(opt.shadowId, opt.resultId);
        var ids = [];
        $("input[name=" + name + "]:checked").each(function() {
            ids[ids.length] = this.value;
        });
        if (ids.length == 0) {
            FP.hideShadow(opt.shadowId, opt.resultId);
            return;
        }
        var params = opt.params;
        params[name] = ids;
        $.post(action, params,
                function(data) {
                    $("#messagesBlock").html(data);
                    callback(null);
                });
    },

	/*
	 * Clipboard function
	 */
	copyToClipboard : function(content) {
		if ($.browser.mozilla) {
			netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
			var gClipboardHelper = Components.classes["@mozilla.org/widget/clipboardhelper;1"].getService(Components.interfaces.nsIClipboardHelper);
			gClipboardHelper.copyString(content);
		} else if ($.browser.msie) {
			window.clipboardData.setData("Text", content);			
		}
	}
};

// Array Remove - By John Resig (MIT Licensed)
Array.remove = function(array, from, to) {
  var rest = array.slice((to || from) + 1 || array.length);
  array.length = from < 0 ? array.length + from : from;
  return array.push.apply(array, rest);
};

// add trim() to strings (http://blog.stevenlevithan.com/archives/faster-trim-javascript)
String.prototype.trim = function() {
    var str = this.replace(/^\s\s*/, ''), ws = /\s/, i = this.length;
    while (ws.test(str.charAt(--i))) {

    }
    return str.slice(0, i + 1);
};

Function.prototype.getBody = function() {
    // Get content between first { and last }
    var m = this.toString().match(/\{([\s\S]*)\}/m)[1];
    // Strip comments
    return m.replace(/^\s*\/\/.*$/mg,'');
};
