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

    if (options.filterId == undefined) {
        options.filterId = options.name + "_filter";
    }

    if (options.valueId == undefined) {
        options.valueId = "selected_" + options.name + "_id";
    }

    options = $.extend({
        action: "",
        filterId: "",
        valueId: "",
        isArray: false,
        extraParams: {},
        resultAction: null,
        resultId: "result",
        preRequest: true,
        parents: []
    }, options);

    this.options = options;

    this.name = options.name;
    this.action = options.action;
    this.isArray = options.isArray;
    this.extraParams = options.extraParams;
    this.parents = [];
    for (var ind in options.parents) {
        this.parents[options.parents[ind]] = options.parents[ind];
    }
    this.parentsCount = options.parents.length;
    this.resultAction = options.resultAction;
    this.preRequest = options.preRequest;

    this.result = $("#" + options.resultId);
    this.value = $("#" + options.valueId);
    this.string = $("#" + options.filterId);

    this.defaultResult = this.result.html();

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
        if (!options.isArray) {
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

};

var FF = {

    filters : [],

    getFiltersByParentName : function(parentName) {
        var ret = [];
        for (var i in this.filters) {
            var filter = this.filters[i];
            if (filter.parents[parentName] != undefined) {
                ret.push(filter);
            }
        }
        return ret;
    },

    createFilter : function (name, options) {
        var filter = new Filter(name, options);
        filter.string.attr("onChange", "FF.onChange2('" + name + "');");
        this.filters[name] = filter;
        this.filters.splice(this.filters.length - 1, 1);
        if (filter.preRequest) {
            var k = 0;
            for (var i in filter.parents) {
                if (this.filters[i].preRequest && this.filters[i].value.val() != "") {
                    k++;
                }
            }
            if (filter.parentsCount > 0 && k < filter.parentsCount) {
                filter.string.attr("readonly", true);
            }
            $.post(filter.action, {filterValue:filter.value.val(), preRequest:true},
                function(data) {
                    var r = data.split("|");
                    filter.string.val(r[0]);
                    filter.value.val(r[1]);
                    FF.onSelect(filter.name);
                });
            pausecomp(100);
        } else {
            if (filter.parentsCount > 0) {
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
        }
        this.filters[name].result.html(this.filters[name].defaultResult);
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
        if (filter.resultAction != null && filter.value.val() != "") {
            $.post(filter.resultAction, {parents: [filter.value.val()]},
                function(data) {
                    filter.result.html(data);
                });
        }
        if (filters.length == 0) {
            var tab = $('a[tabindex="1"],input[tabindex="1"],button[tabindex="1"],textarea[tabindex="1"],select[tabindex="1"]');
            if (tab != null && tab.size() > 0) {
                tab.get(0).focus();
            }
            return;
        }
        for (var i in filters) {
            var filter2 = filters[i];
            var parentsFilled = true;
            for (var i1 in filter2.parents) {
                if (this.filters[i1].value.val() == "") {
                    parentsFilled = false;
                    break;
                }
            }
            if (parentsFilled) {
                filter2.string.removeAttr("readonly").focus();
            }
            if (!filter2.isArray) {
                filter2.autocompleter.setExtraParams({parents : this.getParentParams(filter2)});
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
