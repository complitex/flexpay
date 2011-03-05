<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_autocomplete.jsp"%>
<%@include file="/WEB-INF/jsp/eirc/includes/flexpay_registry.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_ui_core.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_bbq.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_simplemodal.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_collapser.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td>
            <div id="addressFilters" style="display:inline;" class="col">
                <s:text name="payments.town" />: <input type="text" name="townFilter" class="text" value="" />
                <s:text name="payments.street" />: <input type="text" name="streetFilter" class="text" value="" />
                <s:text name="payments.building" />: <input type="text" name="buildingFilter" class="text" value="" />
                <s:text name="payments.apartment" />: <input type="text" name="apartmentFilter" class="text" value="" />
            </div><br>
            <div style="margin:4px 0;" class="col">
                <s:text name="payments.fio" />: <input type="text" name="fioFilter" class="text" value="" /><br>
            </div>

            <%@include file="/WEB-INF/jsp/payments/filters/registry_record_status_filter.jsp"%>
            <span id="errorTypeBlock" style="display:none;">
                <%@include file="/WEB-INF/jsp/ab/filters/import_error_type_filter.jsp"%>
                <input id="groupErrors" type="checkbox" name="groupErrors" value="1" />
                <label for="groupErrors" class="col">
                    <s:text name="eirc.registry.group_by_errors" />
                </label>
            </span>
                    
            <input type="hidden" name="errorType" value="-1" />
            <input type="hidden" name="recordStatus" value="-1" />
            <input type="hidden" name="fio" value="" />
            <input type="button" value="<s:text name="eirc.filter" />" class="btn-exit" onclick="submit();" />
            <%@include file="/WEB-INF/jsp/payments/registry/data/registry_info.jsp"%>
        </td>
    </tr>
    <tr>
        <td id="result">
        </td>
    </tr>
</table>

<script type="text/javascript">

    FPR.init({
        registryId : <s:property value="registry.id" />,
        errorStatusCode : <s:property value="errorStatusCode" />
    });

    $("#addressFilters").ready(function() {

        var filterUrl = "<s:url action="filterAutocompleter" namespace="/payments" includeParams="none" />";

        FPR.createAutocompleter("input[name='townFilter']", filterUrl, false,
                                {
                                    "registry.id":FPR.registryId,
                                    "filterData.type":<s:property value="@org.flexpay.common.persistence.filter.StringValueFilter@TYPE_TOWN" />
                                });

        FPR.createAutocompleter("input[name='streetFilter']", filterUrl, false,
                                {
                                    "registry.id":FPR.registryId,
                                    "filterData.type":<s:property value="@org.flexpay.common.persistence.filter.StringValueFilter@TYPE_STREET" />
                                });

        FPR.createAutocompleter("input[name='buildingFilter']", filterUrl, true,
                                {
                                    "registry.id":FPR.registryId,
                                    "filterData.type":<s:property value="@org.flexpay.common.persistence.filter.StringValueFilter@TYPE_BUILDING" />
                                });

        FPR.createAutocompleter("input[name='apartmentFilter']", filterUrl, true,
                                {
                                    "registry.id":FPR.registryId,
                                    "filterData.type":<s:property value="@org.flexpay.common.persistence.filter.StringValueFilter@TYPE_APARTMENT" />
                                });

    });


    $("#result").ready(function() {
        submit();
    });

    function submit() {
        FPR.$eType.val(FPR.$ietFilter.val());
        FPR.$rStatus.val(FPR.$rsFilter.val());
        FPR.$fio.val(FPR.$fioFilter.val());
        FPR.openedType = -1;
        FPR.openedGroup = -1;
        pagerAjax();
    }

    function pagerAjax(element, resultId) {

        var isResult = resultId != undefined && resultId != null;
        var action = "<s:url action="eircRegistryRecordsListAjax" namespace="/eirc" includeParams="none" />";
        var params = {
            "registry.id":FPR.registryId
        };

        if ($("input[name='townFilter']").val().length > 0) {
            params["townFilter.value"] = $("input[name='townFilter']").val();
        }
        if ($("input[name='streetFilter']").val().length > 0) {
            params["streetFilter.value"] = $("input[name='streetFilter']").val();
        }
        if ($("input[name='buildingFilter']").val().length > 0) {
            params["buildingFilter.value"] = $("input[name='buildingFilter']").val();
        }
        if ($("input[name='apartmentFilter']").val().length > 0) {
            params["apartmentFilter.value"] = $("input[name='apartmentFilter']").val();
        }
        if ($("input[name='fioFilter']").val().length > 0) {
            params["fioFilter.value"] = $("input[name='fioFilter']").val();
        }

        if (FPR.$rStatus.val() == FPR.errorStatusCode && FPR.$gErrors.get(0).checked) {
            if (FPR.$eType.val() < 0 && !isResult) {
                action ="<s:url action="eircRegistryRecordErrorsTypesListAjax" namespace="/eirc" includeParams="none" />";
            } else {
                action ="<s:url action="eircRegistryRecordErrorsGroupsListAjax" namespace="/eirc" includeParams="none" />";
                params["recordErrorsGroupSorterByName.active"] = $("#recordErrorsGroupSorterByNameActive").val();
                params["recordErrorsGroupSorterByName.order"] = $("#recordErrorsGroupSorterByNameOrder").val();
                params["recordErrorsGroupSorterByNumberOfErrors.active"] = $("#recordErrorsGroupSorterByNumberOfErrorsActive").val();
                params["recordErrorsGroupSorterByNumberOfErrors.order"] = $("#recordErrorsGroupSorterByNumberOfErrorsOrder").val();
            }
            if (isResult) {
                FPR.$eType.val($("#et" + FPR.openedType).val());
                params["fioFilter.value"] = FPR.$fio.val();
            }
            params["importErrorTypeFilter.selectedType"] = FPR.$eType.val();

        } else {
            params["recordStatusFilter.selectedId"] = FPR.$rStatus.val();
        }

        var opt = {
            action: action,
            params: params
        };

        if (isResult) {
            opt["resultId"] = resultId;
        }
        if (FPR.openedType >= 0) {
            $("input[name='curPage']").val(1);
            opt["resultId"] = "panelType" + FPR.openedType;
        }

        FP.pagerAjax(element, opt);

    }

    function sorterAjax() {
        pagerAjax();
    }

    function pagerInnerAjax(element, i) {

        if (i == undefined || i == null) {
            i = $("input[name='index']").val();
        }

        var params = {
            index:i,
            "registry.id":FPR.registryId,
            "group.errorType":FPR.$eType.val()
        };

        FPR.addGroupParams(params, i);
        params["fioFilter.value"] = FPR.$fio.val();

        if (element == null) {
            $("span.innerPaging").find("input[name='curPage']").val(1);
        }

        FP.pagerAjax(element, {
            action: "<s:url action="eircRegistryRecordsListSimpleAjax" namespace="/eirc" includeParams="none" />",
            resultId: "panelGroup" + i,
            target:"span.innerPaging",
            params: params
        });
    }

    function createTypeCollapser(i) {

        $("#type" + i).collapser({
            target: "#panelType" + i + ", #panelTypeTr" + i,
            effect: "slide",
            changeText: 0
        }, function() {

            $(".panelTypeTr").slideUp();
            $(".panelType").slideUp(400, function () {
                $(this).html("");
            });

        }, function() {

            var i = $(this).get(0).id.substring(4);

            if (FPR.openedType == i) {
                FPR.openedType = -1;
                return;
            }

            FPR.openedType = i;

            if ($("#panelType" + i).is(":visible")) {
                pagerAjax(null, "panelType" + i);
            }

        });

    }

    function createGroupCollapser(i) {

        $("#group" + i).collapser({
            target: "#panelGroup" + i + ", #panelGroupTr" + i,
            effect: "slide",
            changeText: 0
        }, function() {

            $(".panelGroupTr").slideUp();
            $(".panelGroup").slideUp(400, function () {
                $(this).html("");
            });

        }, function() {

            var i = $(this).get(0).id.substring(5);

            if (FPR.openedGroup == i) {
                FPR.openedGroup = -1;
                return;
            }

            FPR.openedGroup = i;

            if ($("#panelGroup" + i).is(":visible")) {
                pagerInnerAjax(null, i);
            }

        });

    }

    function createDialogForGroup(i) {

        var params = {
            "registry.id":FPR.registryId,
            "group.errorType":FPR.$eType.val()
        };

        FPR.addGroupParams(params, i);

        var src = $.param.querystring("<s:url action="selectCorrectionType" namespace="/payments" includeParams="none" />", params);
        $.modal('<iframe src="' + src + '" height="550" width="800" style="border:0" />', {
            containerCss: {
                backgroundColor:"#fff",
                borderColor:"#0063dc",
                height:550,
                padding:0,
                width:800
            },
            escClose:true,
            overlayClose:true,
            onClose: function() {
                $.modal.close();
                updateErrorsNumber();
                pagerAjax();
            }
        });

    }

    function createItem(i, type) {

        var params = {
            "registry.id":FPR.registryId,
            "group.errorType":FPR.$eType.val()
        };

        FPR.addGroupParams(params, i);

        if (type != null && type != undefined) {
            $("#selectedGroupIndex").val(i);
            if (type ==  "st") {
                $("#stName<s:property value="@org.flexpay.common.util.config.ApplicationConfig@getDefaultLanguage().getId()" />").val(params["group.streetType"]);
                $("#stShortName<s:property value="@org.flexpay.common.util.config.ApplicationConfig@getDefaultLanguage().getId()" />").val(params["group.streetType"]);
                $("#streetTypeModal").modal({
                    containerCss: {
                        backgroundColor:"#fff",
                        borderColor:"#0063dc",
                        height:550,
                        padding:0,
                        width:800
                    },
                    escClose:true,
                    overlayClose:true
                });
            } else if (type == "b") {
                $("#address").text((params["group.buildingNumber"] != null ? "<s:text name="ab.buildings_attribute_type.buiding_number" />: " + params["group.buildingNumber"] : "")
                        + (params["group.buildingBulk"] != null ? "\n<s:text name="ab.buildings_attribute_type.buiding_bulk" />: " + params["group.buildingBulk"] : ""));
                $("#buildingModal").modal();
            }
            return;
        }

        $("input[id^='stName']").each(function() {
           params[this.name] = this.value;
        });
        $("input[id^='stShortName']").each(function() {
           params[this.name] = this.value;
        });
        $("select[name='districtFilter.selectedId']").each(function() {
           params["district.id"] = this.value;
        });

        $.modal.close();

        $.post("<s:url action="registryRecordErrorFixAjax" includeParams="none" />", params,
            function(data, status) {
                $("#messagesBlock").html(data);
                updateErrorsNumber();
                pagerAjax(null, "panelType" + FPR.openedType);
            });

    }

</script>
