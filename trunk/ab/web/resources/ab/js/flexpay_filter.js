function pausecomp(millis){
    var date = new Date();
    var curDate = null;

    do {
        curDate = new Date();
    } while (curDate - date < millis);
}

function Filter(name, options) {

    options = options || {};

    options.name = name;

    options = $.extend({
        action: "",
        filterId: name + "_filter",
        valueId: "selected_" + name + "_id",
        isArray: false,
        display: "input",
        extraParams: {},
        preRequest: true,
        required: true,
        parents: []
    }, options);

    this.options = options;

    this.listeners = [];
    this.eraseFunctions = [];

    this.name = options.name;
    this.action = options.action;
    this.isArray = options.isArray;
    this.readonly = options.display == "input-readonly";
    this.justText = options.display == "text";
    this.extraParams = options.extraParams;
    this.parents = [];
    for (var i in options.parents) {
        this.parents[options.parents[i]] = options.parents[i];
    }
    this.parentsCount = options.parents.length;
    this.requiredParentsCount = 0;
    for (var i in this.parents) {
        if (FF.filters[i].required) {
            this.requiredParentsCount++;
        }
    }
    this.haveRequiredParents = this.requiredParentsCount > 0;
    this.preRequest = options.preRequest;
    this.required = options.required;

    this.value = $("#" + options.valueId);
    this.string = $("#" + options.filterId);
    this.string.parent().prepend('<span id="' + options.filterId + '_text" class="filter">' + this.string.val() + '</span>');
    this.text = $("#" + options.filterId + "_text");

    if (this.justText) {
        this.string.css({display: "none"});
        this.text.removeAttr("style");
        this.readonly = true;
    }

    displayParents(this);

    function displayParents(filter) {
        if (filter.readonly || filter.justText) {
            filter.string.attr("readonly", true);
            if (filter.justText) {
                filter.string.css({display: "none"});
            }
            for (var i in filter.parents) {
                if (filter.readonly) {
                    FF.filters[i].readonly = true;
                }
                if (filter.justText) {
                    FF.filters[i].justText = true;
                }
                displayParents(FF.filters[i]);
            }
        }
    }

    function findValue(li) {
        if (li == null) {
            alert("No match!");
            return;
        }

        var sValue = !!li.extra ? li.extra[0] : li.selectValue;
        $("#" + options.valueId).val(sValue);
        if (li.extra) {
            $("#" + options.filterId).val(li.selectValue);
        }
        FF.onChange(options.name);
        saveValues(options.name);
    }

    function formatItem(row) {
        var filterValue = row[0].toLowerCase();
        var value = $("#" + options.filterId).val().toLowerCase();
        var i = filterValue.indexOf(value);
        return row[0].substr(0, i) + "<strong>" + row[0].substr(i, value.length) + "</strong>" + row[0].substr(i + value.length);
    }

    function selectItem(li) {
        findValue(li);
    }

    function create(filter) {
        if (!options.isArray && !this.readonly) {
            filter.autocompleter = filter.string.autocomplete(options.action,
                    {
                        delay:10,
                        minChars:3,
                        selectOnly:1,
                        matchContains:1,
                        cacheLength:10,
                        formatItem:formatItem,
                        onItemSelect:selectItem,
                        extraParams:options.extraParams
                    })[0].autocompleter;
        } else {
            filter.autocompleter = null;
        }
    }

    function saveValues(name) {
        $.post(FF.filters[name].action, {filterValue:FF.filters[name].value.val(),saveFilterValue:true});
    }

    create(this);

    this.formatItem = function(row) {
        return formatItem(row);
    };

    this.selectItem = function(li) {
        return selectItem(li);
    };

    this.addListener = function(listener) {
        for (var i = 0; i < this.listeners.length; i++) {
            if (this.listeners[i] == listener) {
                return;
            }
        }
        this.listeners.push(listener);
    };

    this.addEraseFunction = function(func) {
        for (var i = 0; i < this.eraseFunctions.length; i++) {
            if (this.eraseFunctions[i] == func) {
                return;
            }
        }
        this.eraseFunctions.push(func);
    };

};

var FF = {

    filters : [],

    getFiltersByParentName : function(parentName) {
        var ret = [];
        for (var i in this.filters) {
            var filter = this.filters[i];
            if (filter.parents[parentName]) {
                ret.push(filter);
            }
        }
        return ret;
    },

    setFocusByTabIndex : function(filters) {
        if (filters.length == 0) {
            var tab = $('a[tabindex="2"],input[tabindex="2"],button[tabindex="2"],textarea[tabindex="2"],select[tabindex="2"]');
            if (tab != null && tab.size() > 0) {
                tab.get(0).focus();
            }
            return true;
        }
        return false;
    },

    createFilter : function (name, options) {
        var filter = new Filter(name, options);
        this.filters[name] = filter;
        filter.string.attr("onchange", "FF.onChange2('" + name + "');");
        filter.string.attr("tabIndex", "1");
        this.filters.splice(this.filters.length - 1, 1);
        if (filter.preRequest) {
            var k = 0;
            for (var i in filter.parents) {
                if (this.filters[i].preRequest && this.filters[i].value.val() != "") {
                    k++;
                }
            }
            if ((filter.haveRequiredParents && k < filter.requiredParentsCount) || (filter.parentsCount > 0 && !filter.haveRequiredParents)) {
                filter.string.attr("readonly", true);
            }
            $.post(filter.action, {filterValue:filter.value.val(), preRequest:true},
                function(data) {
                    var r = data.split("|");
                    filter.string.val(r[0]);
                    if (filter.justText) {
                        filter.text.text(r[0]);
                    }
                    filter.value.val(r[1]);
                    FF.onSelect(filter.name);
                });
            pausecomp(100);
        } else {
            if (filter.haveRequiredParents || filter.requiredParentsCount == filter.parentsCount) {
                filter.string.attr("readonly", true);
            }
        }
    },

    onChange : function (name) {
        this.eraseChildFilters(name);
        this.onSelect(name);
    },

    onChange2 : function (name) {
        this.filters[name].value.val("");
//        this.onChange(name);
    },

    eraseChildFilters : function(name) {
        var filters = this.getFiltersByParentName(name);
        for (var i in filters) {
            var filter = filters[i];
            filter.value.val("");
            filter.string.val("").attr("readonly", true);
            if (filter.autocompleter != null) {
                filter.autocompleter.flushCache();
            }
            this.eraseChildFilters(filter.name);
            for (var i = 0; i < filter.eraseFunctions.length; i++) {
                filter.eraseFunctions[i].call();
            }
        }
    },

    getParentParams : function (filter) {
        var parents = [];
        var i = 0;
        for (var k in filter.parents) {
            parents[i] = this.filters[k].value.val();
            i++;
        }
        return parents;
    },

    onSelect : function(filterName) {
        var filters = this.getFiltersByParentName(filterName);
        var filter = this.filters[filterName];
        if (!filter.string.attr("readonly") || filter.readonly) {
            for (var i = 0; i < filter.listeners.length; i++) {
                filter.listeners[i].call();
            }
        }
        if (filter.justText) {
            filter.text.val(filter.string.val());
        }
        if (this.setFocusByTabIndex(filters)) {
            return;
        }
        for (var i in filters) {
            var filter2 = filters[i];
            var parentsFilled = true;
            var filledParentsCount = 0;
            for (var i1 in filter2.parents) {
                if (this.filters[i1].value.val() != "") {
                    filledParentsCount++;
                }
                if (this.filters[i1].value.val() == "" && this.filters[i1].required) {
                    parentsFilled = false;
                }
            }
            if (filter.requiredParentsCount == filter.parentsCount && filledParentsCount == 0) {
                parentsFilled = false;
            }
            if (parentsFilled) {
                filter2.string.focus()
                if (!filter2.readonly || (filter2.readonly && this.getFiltersByParentName(filter2.name).length == 0)) {
                    filter2.string.removeAttr("readonly");
                }
            }
            if (!filter2.isArray) {
                if (filter2.autocompleter != null) {
                    filter2.autocompleter.setExtraParams({parents : this.getParentParams(filter2)});
                }
            } else {
                filter2.string.addClass("ac_loading");
                var params = this.getParentParams(filter2);
                var filled = false;
                for (var n in params) {
                    if (params[n] != "") {
                        filled = true;
                        break;
                    }
                }
                if (!filled && params.length > 0) {
                    filter2.string.removeClass("ac_loading");
                    return;
                }
                $.post(filter2.action, {parents : params},
                    function(data) {
                        filter2.autocompleter = filter2.string.autocompleteArray(
                                FF.parseAutocompleterData(data),
                                {
                                    delay:10,
                                    selectOnly:1,
                                    cacheLength:10,
                                    formatItem:filter2.formatItem,
                                    onItemSelect:filter2.selectItem
                                })[0].autocompleter;
                        if (parentsFilled) {
                            filter2.string.focus();
                        }
                        filter2.string.removeClass("ac_loading");
                    });
            }
        }
    },

    parseAutocompleterData : function(data) {
        if (!data) {
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
    }

};
