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
        $("input[type='checkbox'][name^='" + prefix + "']").each(function() {
            this.checked = checked;
        });
    },

    endis : function (label, endis) {
        var e = $(label);
        if (endis) {
            return e.removeAttr("disabled");
        } else {
            return e.attr("disabled", true);
        }
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
        var buffer = [];
//        console.log("message = " + message);
        for (var i = 0; i < params.length; i++) {
            end = message.indexOf("||", start);
            buffer[buffer.length] = message.substring(start, end);
            buffer[buffer.length] = params[i];
            start = end + 2;
        }
        buffer[buffer.length] = message.substring(start);
//        console.log("buffer.join(\"\") = " + buffer.join(""));
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
		return e.keyCode != this.ENTER_KEY_CODE;
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
            s.css({
                height: el.innerHeight(),
                width: el.innerWidth(),
                left: el.position().left,
                top: el.position().top
            });
        }
        if (params["background-color"] == null) {
            params["background-color"] = "#eeeded";
        }
        s.css(params);
    },

    showShadowText : function(shadowId, elementId) {
        FP.resizeShadow(shadowId, elementId, {visibility:"visible", "background-color":""});
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
            target: "body",
            pageSizeChangedName:"pageSizeChanged",
            pageNumberName:"pager.pageNumber",
            pageSizeName:"pager.pageSize",
            notPagerRequest:false,
            params: {}
        }, opt);

        var params = opt.params;
        var shadowId = opt.shadowId;
        var resultId = opt.resultId;
        var result = $("#" + resultId);

        if (result.innerHeight() < 40) {
            FP.showShadowText(shadowId, resultId);
        } else {
            FP.showShadow(shadowId, resultId);
        }

        var pageSizeName = opt.pageSizeName;

        if (element == null || element == undefined) {
            if (params[opt.pageSizeChangedName] == null) {
                params[opt.pageSizeChangedName] = false;
            }
            var curPage = $(opt.target).find("input[name='curPage']");
            if (params[opt.pageNumberName] == null) {
                params[opt.pageNumberName] = curPage.get(0) != null && curPage.get(0) != undefined ? curPage.val() : 1;
            }
            var pageSize = $(opt.target).find("select[name='" + pageSizeName + "']");
            if (pageSize.get(0) != null && pageSize.get(0) != undefined && params[pageSizeName] == null) {
                params[pageSizeName] = pageSize.val();
            }
        } else {

            var isSelect = element.name == pageSizeName; 
            var elValue = element.value != undefined ? element.value : ($.browser.msie ? element.innerText : element.text);
            var elValueInt = parseInt(elValue);
            if (elValueInt + "" === "NaN" && (elValue == "<" || elValue == ">")) {
                elValue = element.getAttribute("value");
                elValueInt = parseInt(elValue);
            }
            if (elValueInt + "" === "NaN" || elValue != elValueInt || elValueInt <= 0) {
                FP.hideShadow(shadowId);
                return;
            }
            if (params[opt.pageSizeChangedName] == null) {
                params[opt.pageSizeChangedName] = isSelect;
            }
            if (!isSelect && params[opt.pageNumberName] == null) {
                params[opt.pageNumberName] = elValue;
            }
            if (params[pageSizeName] == null) {
                params[pageSizeName] = isSelect ? elValue : $(opt.target).find("select[name='" + pageSizeName + "']").val();
            }
        }

        $.post(opt.action, params, function(data, status) {
                    if (data == "" && status == "success") {
                        window.location.href = FP.base;
                    }
                    result.html(data);
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
                $("input[id='" + id + "']").each(function() {
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

    serviceElements : function(action, name, callback, opt) {

        opt = opt || {};

        opt = $.extend({
            shadowId: "shadow",
            resultId: "result",
            params: {}
        }, opt);

        FP.showShadow(opt.shadowId, opt.resultId);

        var params = opt.params;

        if (name != null) {
            var ids = [];
            $("input[name='" + name + "']:checked").each(function() {
                ids[ids.length] = this.value;
            });
            if (ids.length == 0) {
                FP.hideShadow(opt.shadowId, opt.resultId);
                return;
            }
            params[name] = ids;

        }
        $.post(action, params, function(data, status) {
                    if (data == "" && status == "success") {
                        window.location.href = FP.base;
                    }
                    $("#messagesBlock").html(data);
                    callback(null);
                });
    },

    post : function(url, params) {
        var temp = document.createElement("form");
        temp.action = url;
        temp.method = "POST";
        temp.style.display = "none";
        for(var x in params) {
            var opt = document.createElement("textarea");
            opt.name = x;
            opt.value = params[x];
            temp.appendChild(opt);
        }
        document.body.appendChild(temp);
        temp.submit();
        return temp;
    },

    parseAutocompleterData : function(data) {
        if (data == null || !data) {
            return null;
        }
        var parsed = [];
        var rows = data.split("\n");
        for (var i = 0; i < rows.length; i++) {
            var row = $.trim(rows[i]);
            if (row) {
                parsed[parsed.length] = row.split("|");
            }
        }
        return parsed;
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

Function.prototype.getBody = function() {
    // Get content between first { and last }
    var m = this.toString().match(/\{([\s\S]*)\}/m)[1];
    // Strip comments
    return m.replace(/^\s*\/\/.*$/mg,'');
};
