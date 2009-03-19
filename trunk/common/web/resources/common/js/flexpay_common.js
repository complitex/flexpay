var FP = {
    // Set checkboxes group (names starts with prefix) state to checked
    setCheckboxes : function (checked, prefix) {
        $('input[type=checkbox][name^=' + prefix + ']').each(function(el) {
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

    calendars : function(arg, img) {
        $(function() {
            if (img != null) {
                $(arg).datepicker({
                    showOn: "both",
                    dateFormat: "yy/mm/dd",
                    buttonImage: img,
                    buttonImageOnly: true
                });
            } else {
                $(arg).datepicker({
                    dateFormat: "yy/mm/dd"
                });
            }
        });
    }

};

Array.prototype.remove = function(from, to) {
    var rest = this.slice((to || from) + 1 || this.length);
    this.length = from < 0 ? this.length + from : from;
    return this.push.apply(this, rest);
};

// add trim() to strings (http://blog.stevenlevithan.com/archives/faster-trim-javascript)
String.prototype.trim = function() {
    var str = this.replace(/^\s\s*/, ''), ws = /\s/, i = this.length;
    while (ws.test(str.charAt(--i))) {

    }
    return str.slice(0, i + 1);
};
