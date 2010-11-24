
function Filter(name, options) {

    options = options || {};

    options.name = name;

    options = $.extend({
        action: "",
        filterId: name + "_string",
        valueId: name + "_selected",
        rawId: name + "_raw",
        isArray: false,
        display: "input",
        inputClass: "form-search",
        extraParams: {},
        valueType: "number",
        defaultValue: 0,
        defaultString: "",
        preRequest: true,
        required: true,
        receiveData: function() {},
        parents: []
    }, options);

    this.ready = false;
    this.options = options;

    var dv = options.defaultValue;

    this.isString = options.valueType == "string";
    this.isNumber = options.valueType == "number";

    if (this.isString) {
        options.defaultValue = dv + "";
    } else if (this.isNumber) {
        options.defaultValue = dv == null || dv == "" ? 0 : parseInt(dv);
    } else {
        alert("Unknown value type");
        return false;
    }

    $("#" + options.rawId).append('<input id="' + options.valueId + '" type="hidden" name="' + name + 'Filter" value="' + options.defaultValue + '" />\n' +
                                  '<input id="' + options.filterId + '" type="text" tabindex="1" class="' + options.inputClass + '" value="' + options.defaultString + '" />');

    this.listeners = [];
    this.erasers = [];

    this.name = options.name;
    this.action = options.action;
    this.isArray = options.isArray;
    this.readonly = options.display == "input-readonly";
    this.justText = options.display == "text";
    this.extraParams = options.extraParams;
    this.defaultValue = options.defaultValue;
    this.parents = [];
    for (var i in options.parents) {
        this.parents[options.parents[i]] = options.parents[i];
    }
    this.parentsCount = options.parents.length;
    this.requiredParentsCount = 0;
    for (var i1 in this.parents) {
        if (FF.filters[i1].required) {
            this.requiredParentsCount++;
        }
    }
    this.hasRequiredParents = this.requiredParentsCount > 0;
    this.preRequest = options.preRequest;
    this.required = options.required;

    this.value = $("#" + options.valueId);
    this.string = $("#" + options.filterId);
    this.string.parent().prepend('<span id="' + options.filterId + '_text" class="filter" style="display:none;">' + this.string.val() + '</span>');
    this.text = $("#" + options.filterId + "_text");

    if (this.justText) {
        this.string.css({display: "none"});
        this.text.removeAttr("style");
        this.readonly = true;
    }

    displayParents(this);

    function displayParents(filter) {

        var readonly = filter.readonly;
        var justText = filter.justText;
        var string = filter.string;
        if (readonly || justText) {
            string.attr("readonly", true);
            if (justText) {
                string.css({display: "none"});
            }
            $.each(filter.parents, function (i, v) {
                var filter2 = FF.filters[v];
                if (readonly) {
                    filter2.readonly = true;
                }
                if (justText) {
                    filter2.justText = true;
                }
                displayParents(filter2);
            });
        }
    }

    function findValue(li) {
        if (li == null) {
            alert("No match!");
            return;
        }
//        var sValue = !!li.extra ? li.extra : li.selectValue;
        var filter = FF.filters[options.name];
        filter.value.val(li.extra);
        if (li.extra) {
            $.getJSON(filter.action, {filterValue:li.extra,saveFilterValue:true,preRequest:true},
                    function(data) {
                        filter.string.val(data.string);
                        FF.onChange(options.name);
                    });
        }
    }

    function formatItem(row) {
        var filterValue = row[0].toLowerCase();
        var value = $("#" + options.filterId).val().toLowerCase();
        var i = filterValue.indexOf(value);
        return i < 0 ? row[0] : row[0].substr(0, i) + "<strong>" + row[0].substr(i, value.length) + "</strong>" + row[0].substr(i + value.length);
    }

    function receiveData() {
        options.receiveData();
    }

    function selectItem(event, data) {
        var li = {
            extra:data[1],
            selectValue:data[0]
        };
        findValue(li);
    }

    function create(filter) {
        if (!options.isArray && !filter.readonly) {
            filter.string.autocomplete(options.action,
                {
                    delay:30,
                    minChars:3,
                    matchContains: true,
                    cacheLength:10,
                    scroll: true,
                    scrollHeight: 200,
                    formatItem:formatItem,
                    receiveData: receiveData,
                    extraParams:options.extraParams
                });
            filter.string.result(selectItem);
        }

    }

    create(this);

    this.formatItem = function(row) {
        return formatItem(row);
    };

    this.selectItem = function(event, data, formatted) {
        return selectItem(event, data, formatted);
    };

    this.addListener = function(listener) {
        var listeners = this.listeners;
        for (var l in listeners) {
            if (l == listener) {
                return;
            }
        }
        listeners.push(listener);
    };

    this.removeListener = function() {
        var listeners = this.listeners;
        Array.remove(listeners, listeners.length);
    };

    this.listen = function() {
        var el = this;
        $.each(el.listeners, function (i, v) {
            v.call(el, el);
        });
    };

    this.addEraser = function(eraser) {
        var erasers = this.erasers;
        for (var e in erasers) {
            if (e == eraser) {
                return;
            }
        }
        erasers.push(eraser);
    };

    this.erase = function() {
        var el = this;
        $.each(this.erasers, function (i, v) {
            v.call(el, el);
        });
    };

    this.displayParents = function(filter) {
        displayParents(filter);
    };

    return true;

}

var FF = {

    filters : [],

    getFiltersByParentName : function(parentName) {

        var ret = [];
        var filters = this.filters;

        for (var i in filters) {
            if (filters[i].parents[parentName]) {
                ret.push(filters[i]);
            }
        }
        return ret;
    },

    setFocusByTabIndex : function(filters) {
        if (filters.length == 0) {
            $('a[tabindex="2"],input[tabindex="2"],button[tabindex="2"],textarea[tabindex="2"],select[tabindex="2"]').focus();
            return true;
        }
        return false;
    },

    createFilter : function (name, options) {

        var filter = new Filter(name, options);
        var filters = this.filters;

        filters[name] = filter;
        filters.splice(filters.length - 1, 1);

        FF._createFilter(filter);

    },

    _createFilter : function (filter) {

        var hasReqParents = filter.hasRequiredParents;
        var reqParentsCount = filter.requiredParentsCount;
        var parentsCount = filter.parentsCount;
        var value = filter.value;
        var string = filter.string;

        if (filter.preRequest && !(filter.isNumber && value.val() == 0)) {
            var k = 0;
            for (var i in filter.parents) {

                var filter2 = FF.filters[i];
                var value2 = filter2.value.val();

                if (!filter2.ready) {
                    setTimeout(function() {
                        FF._createFilter(filter);
                    }, 100);
                    return;
                }

                if (filter2.preRequest && ((filter2.isString && value2 != "") || (filter2.isNumber && value2 != "0"))) {
                    k++;
                }
            }
            if ((hasReqParents && k < reqParentsCount) || (parentsCount > 0 && !hasReqParents)) {
                string.attr("readonly", true);
            }
            $.getJSON(filter.action, {filterValue:value.val(), preRequest:true},
                function(data) {
                    string.val(data.string);
                    if (filter.justText) {
                        filter.text.text(data.string);
                    }
                    value.val(data.value);
                    FF.onSelect(filter.name);
                    filter.ready = true;
                });
        } else {
            if (hasReqParents || (reqParentsCount == parentsCount && parentsCount > 0)) {
                string.attr("readonly", true);
            }
            filter.ready = true;
        }
    },

    updateFilter : function(name, options) {

        var filter = this.filters[name];

        if (options.readonly) {
            filter.readonly = true;
        }
        filter.displayParents(filter);
    },

    onChange : function(name) {
        this.eraseChildFilters(name);
        this.onSelect(name);
    },

    eraseChildFilters : function(name) {

        var filters = this.getFiltersByParentName(name);

        for (var i in filters) {

            var filter = filters[i];
            this.eraseFilter(filter);
        }
    },

    eraseFilter : function(filter) {
        var value = filter.value;

        if (filter.isString) {
            value.val("");
        } else if (filter.isNumber) {
            value.val("0");
        }
        filter.string.val("").attr("readonly", true);
        if (filter.string.autocomplete != null) {
            filter.string.flushCache();
        }
        this.eraseChildFilters(filter.name);
        filter.erase();
    },

    getParentParams : function (filter) {

        var parents = [];
        var i = 0;
        for (var k in filter.parents) {
            parents[i] = FF.filters[k].value.val();
            i++;
        }
        return parents;
    },

    onSelect : function(filterName) {

        var filters = this.getFiltersByParentName(filterName);
        var filter = this.filters[filterName];
        var value = filter.value.val();
        var string = filter.string;

        if (((value != "" && filter.isString) || (value != "0" && filter.isNumber)) && (!string.attr("readonly") || filter.readonly)) {
            filter.listen();
        }
        if (filter.justText) {
            filter.text.val(string.val());
        }
        if (this.setFocusByTabIndex(filters)) {
            return;
        }
        for (var i in filters) {

            var filter2 = filters[i];
            var string2 = filter2.string;
            var readonly = filter2.readonly;
            var parentsFilled = true;
            var filledParentsCount = 0;

            for (var i1 in filter2.parents) {

                var filter2Parent = this.filters[i1];
                var valuefilter2Parent = filter2Parent.value.val();
                var isString = filter2Parent.isString;
                var isNumber = filter2Parent.isNumber;

                if ((valuefilter2Parent != "" && isString) || (valuefilter2Parent != "0" && isNumber)) {
                    filledParentsCount++;
                }
                if ((valuefilter2Parent == "" && isString) || (valuefilter2Parent == "0" && isNumber) && filter2Parent.required) {
                    parentsFilled = false;
                }
            }
            if (filter.requiredParentsCount == filter.parentsCount && filledParentsCount == 0) {
                parentsFilled = false;
            }
            if (parentsFilled) {
                if (!readonly) {
                    string2.removeAttr("readonly");
                }
                string2.focus();
            }
            if (!filter2.isArray) {
                if (filter2.string.autocomplete != null) {
                    filter2.string.setOptions({extraParams: {parents : this.getParentParams(filter2)}});
                }
            } else {
                string2.addClass("ac_loading");
                var params = this.getParentParams(filter2);
                var filled = false;
                for (var n in params) {
                    if (params[n] != "") {
                        filled = true;
                        break;
                    }
                }
                if (readonly) {
                    string2.attr("readonly", true);
                }
                if (readonly || (!filled && params.length > 0)) {
                    string2.removeClass("ac_loading");
                    return;
                }
                $.post(filter2.action, {parents : params},
                    function(data, status) {
                        if (data == "" && status == "success") {
                            window.location.href = FP.base;
                        }
                        filter2.string.autocomplete(FP.parseAutocompleterData(data),
                            {
                                delay:30,
                                minChars:0,
                                cacheLength:10,
                                scroll: true,
                                scrollHeight: 200
                            });
                        filter2.string.result(filter2.selectItem);

                        if (parentsFilled) {
                            string2.focus();
                        }
                        string2.removeClass("ac_loading");
                    });
            }
        }
    },

    addListener : function(filterName, func) {

        var filter = this.filters[filterName];

        if (filter == null) {
            alert("Incorrect filterName!");
        }
        filter.addListener(func);
    },

    removeListener : function(filterName) {

        var filter = this.filters[filterName];

        if (filter == null) {
            alert("Incorrect filterName!");
        }
        filter.removeListener();
    },

    addEraser : function(filterName, func) {

        var filter = this.filters[filterName];

        if (filter == null) {
            alert("Incorrect filterName!");
        }
        filter.addEraser(func);
    },

    removeFilters : function() {
        this.filters = [];
    }

};
