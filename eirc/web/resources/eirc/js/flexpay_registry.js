var FPR = {

    openedType : -1,
    openedGroup : -1,
    registryId : -1,
    errorStatusCode : -10,
    errorTypeBlockSelector : "#errorTypeBlock",
    groupErrorsSelector : "#groupErrors",
    recordStatusFilterSelector : "select[name='recordStatusFilter.selectedId']",
    importErrorTypeFilter : "select[name='importErrorTypeFilter.selectedType']",
    errorTypeFilterSelector : "input[name='errorType']",
    recordStatusFilter : "input[name='recordStatus']",
    fioFilter : "input[name='fioFilter']",
    fio : "input[name='fio']",
    groups : [],

    $etBlock : $(this.errorTypeBlockSelector),
    $gErrors : $(this.groupErrorsSelector),
    $rsFilter : $(this.recordStatusFilterSelector),
    $ietFilter : $(this.importErrorTypeFilter),
    $eType : $(this.errorTypeFilterSelector),
    $rStatus : $(this.recordStatusFilter),
    $fioFilter : $(this.fioFilter),
    $fio : $(this.fio),

    init : function(opts) {

        var opt = $.extend({
            registryId : -1,
            errorStatusCode : -10,
            urlRegistryRecordsList : "",
            urlRegistryRecordErrorsGroupsList : "",
            urlRegistryRecordsListSimple : "",
            urlSelectCorrectionType : "",
            errorTypeBlockSelector : "#errorTypeBlock",
            groupErrorsSelector : "#groupErrors",
            recordStatusFilterSelector : "select[name='recordStatusFilter.selectedId']",
            importErrorTypeFilter : "select[name='importErrorTypeFilter.selectedType']",
            errorTypeFilterSelector : "input[name='errorType']",
            recordStatusFilter : "input[name='recordStatus']",
            fio : "input[name='fio']",
            fioFilter : "input[name='fioFilter']"
        }, opts);

        this.registryId = opt.registryId;
        this.errorStatusCode = opt.errorStatusCode;

        this.errorTypeBlockSelector = opt.errorTypeBlockSelector;
        this.groupErrorsSelector = opt.groupErrorsSelector;
        this.recordStatusFilterSelector = opt.recordStatusFilterSelector;
        this.importErrorTypeFilter = opt.importErrorTypeFilter;
        this.errorTypeFilterSelector = opt.errorTypeFilterSelector;
        this.recordStatusFilter = opt.recordStatusFilter;
        this.fio = opt.fio;
        this.fioFilter = opt.fioFilter;

        this.groups = [];

        this.$etBlock = $(this.errorTypeBlockSelector);
        this.$gErrors = $(this.groupErrorsSelector);
        this.$rsFilter = $(this.recordStatusFilterSelector);
        this.$ietFilter = $(this.importErrorTypeFilter);
        this.$eType = $(this.errorTypeFilterSelector);
        this.$rStatus = $(this.recordStatusFilter);
        this.$fioFilter = $(this.fioFilter);
        this.$fio = $(this.fio);

        this.initFilters();
        
    },

    initFilters : function() {

        FPR.$rsFilter.change(function() {
            if (this.value == FPR.errorStatusCode) {
                FPR.$etBlock.show("fast");
                FPR.$gErrors.attr("checked", true);
            } else {
                FPR.$etBlock.hide("fast");
                FPR.$ietFilter.val(-1);
                FPR.$gErrors.removeAttr("checked");
            }
        });

        FPR.$rsFilter.change();

    },

    addGroups : function(gg) {
        for (var i = 0; i < gg.length; i++) {
            var p = gg[i];
            this.groups[this.groups.length] = {
                townName:p[0],
                streetType:p[1],
                streetName:p[2],
                buildingNumber:p[3],
                buildingBulk:p[4],
                apartmentNumber:p[5],
                lastName:p[6],
                middleName:p[7],
                firstName:p[8],
                numberOfRecords:p[9]
            };
        }
    },

    addGroupParams : function(params, i) {

        var group = FPR.groups[i];

        if (group.townName != null) {
            params["group.townName"] = group.townName;
        }
        if (group.streetType != null) {
            params["group.streetType"] = group.streetType;
        }
        if (group.streetName != null) {
            params["group.streetName"] = group.streetName;
        }
        if (group.buildingNumber != null) {
            params["group.buildingNumber"] = group.buildingNumber;
        }
        if (group.buildingBulk != null) {
            params["group.buildingBulk"] = group.buildingBulk;
        }
        if (group.apartmentNumber != null) {
            params["group.apartmentNumber"] = group.apartmentNumber;
        }
        if (group.lastName != null) {
            params["group.lastName"] = group.lastName;
        }
        if (group.middleName != null) {
            params["group.middleName"] = group.middleName;
        }
        if (group.firstName != null) {
            params["group.firstName"] = group.firstName;
        }
        if (group.numberOfRecords != null) {
            params["group.numberOfRecords"] = group.numberOfRecords;
        }
    },

    createAutocompleter : function(selector, url, isArray, params) {
        var filter = $(selector);
        if (isArray) {
            filter.attr("readonly", true);
            filter.addClass("ac_loading");
            $.post(url, params,
                function(data, status) {
                    if (data == "" && status == "success") {
                        window.location.href = FP.base;
                    }
                    filter.autocomplete(FP.parseAutocompleterData(data),
                        {
                            delay:1000,
                            minChars:0,
                            cacheLength:10,
                            scroll: true,
                            scrollHeight: 200
                        });
                    filter.removeAttr("readonly");
                    filter.removeClass("ac_loading");
                });

        } else {
            filter.autocomplete(url,
                {
                    delay:30,
                    minChars:3,
                    matchContains: true,
                    cacheLength:10,
                    scroll: true,
                    scrollHeight: 200,
                    extraParams: params
                });
        }
    }

};
