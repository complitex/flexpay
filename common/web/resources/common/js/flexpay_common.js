var FP = {
    // Set checkboxes group (names starts with prefix) state to checked
    setCheckboxes : function (checked, prefix) {
        var boxes = $$('input[type="checkbox"]');
        boxes.each(function(inp) {
            if (inp.name.startsWith(prefix)) {
                inp.checked = checked;
            }
        });
    },

    sorters : [],

    activateSorter : function (sorter) {

        // disable all sorters
        this.sorters.each(function (field) {
            $(field).value = 0;
        });
        // set active passed sorter
        $(sorter).value = 1;
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

    /**
     *  This fuction for get checked radio value
     *
     * @param radioObj radio button elements array
     * @return  the value of the radio button that is checked
     *          and return null if none are checked, or there are no radio buttons
     */
    getCheckedValue : function(radioObj) {
        if (!radioObj) {
            return null;
        }
        var len = radioObj.length;
        if (radioLength == undefined) {
            return radioObj.checked ? radioObj.value : null;
        }
        for(var i = 0; i < len; i++) {
            if(radioObj[i].checked) {
                return radioObj[i].value;
            }
        }
        return null;
    }

};

Array.prototype.remove = function(from, to) {
    var rest = this.slice((to || from) + 1 || this.length);
    this.length = from < 0 ? this.length + from : from;
    return this.push.apply(this, rest);
};

// add trim() to strings (http://blog.stevenlevithan.com/archives/faster-trim-javascript)
String.prototype.trim = function() {
    var str = this.replace(/^\s\s*/, ''),
            ws = /\s/,
            i = this.length;
    while (ws.test(str.charAt(--i)));
    return str.slice(0, i + 1);
};