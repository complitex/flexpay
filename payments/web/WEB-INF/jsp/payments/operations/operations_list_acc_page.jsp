<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_timeentry.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_bbq.jsp"%>
<%@include file="/WEB-INF/jsp/payments/includes/stylesheet.jsp"%>

<%-- filters are temporary hidden! --%>
<%--<sec:authorize ifAllGranted="ROLE_PAYMENTS_DEVELOPER">--%>

<s:hidden name="documentSearch" />

<table cellpadding="3" cellspacing="1" border="0" width="100%" class="operations">
    <tr>
        <td><s:text name="payments.report.generate.date_from" /></td>
        <td><%@include file="/WEB-INF/jsp/common/filter/begin_date_filter.jsp"%></td>
        <td><s:text name="payments.report.generate.date_till" /></td>
        <td><%@include file="/WEB-INF/jsp/common/filter/end_date_filter.jsp"%></td>
    </tr>
    <tr>
        <td><s:text name="payments.operations.list.time_from"/></td>
        <td><%@include file="/WEB-INF/jsp/common/filter/begin_time_with_sec_filter.jsp"%></td>
        <td><s:text name="payments.operations.list.time_till"/></td>
        <td><%@include file="/WEB-INF/jsp/common/filter/end_time_with_sec_filter.jsp"%></td>
    </tr>
    <tr>
        <td><s:text name="payments.operations.list.payment_point"/></td>
        <td><%@include file="/WEB-INF/jsp/orgs/filters/payment_point_filter.jsp"%></td>
        <td><s:text name="payments.operations.list.cashbox"/></td>
        <td id="cFilterBody"><%@include file="/WEB-INF/jsp/orgs/filters/cashbox_filter.jsp"%></td>
    </tr>
    <tr>
        <td><s:text name="payments.operations.list.sum_from" /></td>
        <td><s:textfield name="minimalSum" /></td>
        <td><s:text name="payments.operations.list.sum_up_to" /></td>
        <td><s:textfield name="maximalSum" /></td>
    </tr>
    <tr>
        <td><s:text name="payments.operations.list.service_type" /></td>
        <td colspan="2"><%@include file="/WEB-INF/jsp/payments/filters/service_type_filter.jsp"%></td>
        <td>
            <input type="button" class="btn-exit" value="<s:text name="payments.operations.list.filter" />" onclick="pagerAjax();" />
        </td>
    </tr>
</table>
<%--</sec:authorize>--%>

<span id="result"></span>

<script type="text/javascript">

    var defaultCFilter = $("#cFilterBody").html();

    var $pp = $("select[name=paymentPointFilter.selectedId]");
    var $c = $("select[name=cashboxFilter.selectedId]");

    $pp.get(0).setAttribute("onchange", "selectPP();");
    $c.get(0).setAttribute("onchange", "");

    $(function() {
        pagerAjax();
    });

    function pagerAjax(element) {
        var cValue = $("select[name=cashboxFilter.selectedId]").val();
        if ($("select[name=cashboxFilter.selectedId]").attr("disabled")) {
            cValue = -1
        }
        FP.pagerAjax(element, {
            action:"<s:url action="operationsListAccAjax" includeParams="none" />",
            params: {
                "cashboxFilter.selectedId":cValue,
                "paymentPointFilter.selectedId":$("select[name=paymentPointFilter.selectedId]").val(),
                "beginDateFilter.stringDate":$("input[name=beginDateFilter.stringDate]").val(),
                "endDateFilter.stringDate":$("input[name=endDateFilter.stringDate]").val(),
                "beginTimeFilter.stringDate":$("input[name=beginTimeFilter.stringDate]").val(),
                "endTimeFilter.stringDate":$("input[name=endTimeFilter.stringDate]").val(),
                "serviceTypeFilter.selectedId":$("select[name=serviceTypeFilter.selectedId]").val(),
                "operationSorterById.active": $("#operationSorterByIdActive").val(),
                "operationSorterById.order": $("#operationSorterByIdOrder").val(),
                "minimalSumFilter.value":$("input[name=minimalSum]").val(),
                "maximalSumFilter.value":$("input[name=maximalSum]").val(),
                documentSearch:$("input[name=documentSearch]").val()
            }
        });
    }

    function sorterAjax() {
        pagerAjax();
    }

    function selectPP() {

        if ($pp.length == 0 || $pp.val() <= 0) {
            eraseCashbox();
            return;
        } else {
            $c.val(-1);
        }

        var params = {
            "paymentPointFilter.selectedId":$pp.val()
        };

        $.post("<s:url action="cashboxFilterAjax" namespace="/payments" includeParams="none" />", params,
                function(data, status) {
                    if (status == "success") {
                        $(cFilterBody).html(data);
                    }
                }
        );

    }

    function eraseCashbox() {
        $(cFilterBody).html(defaultCFilter);
    }

    function setStatus(status) {
        FP.serviceElements("<s:url action="operationSetStatus" includeParams="none" />", "operation.id", pagerAjax,
                            {params:{status:status}});
    }

    function printQuittance(opId) {
        var params = {
            copy: true,
            operationId: opId,
            format:"pdf"
        };
        window.open($.param.querystring("<s:url action="paymentOperationReportAction" includeParams="none" />", params), "_blank");
    }

    function showButtons(state) {

        var reg = $(".btn-register");
        var ret = $(".btn-return");
        var del = $(".btn-delete");

        switch (state) {
            case 1:
                buttons({disable:false,button:reg}, {button:ret}, {disable:false,button:del});
                break;
            case 2:
                buttons({button:reg}, {disable:false,button:ret}, {button:del});
                break;
            case 4:
                buttons({button:reg}, {button:ret}, {disable:false,button:del});
                break;
            case 5:
                buttons({button:reg}, {button:ret}, {disable:false,button:del});
                break;
            default:
                buttons({button:reg}, {button:ret}, {button:del});
                break;
        }
    }

    function buttons() {
        for (var i = 0; i < arguments.length; i++) {

            var but = but || {};
            but = $.extend({
                disable: true
            }, arguments[i]);

            if (but.disable) {
                but.button.attr("disabled", true).removeClass("btn-exit").addClass("btn-search");
            } else {
                but.button.removeAttr("disabled").removeClass("btn-search").addClass("btn-exit");
            }
        }
    }

    function showDetails() {

        $("tr.document_row").toggle();
        //$("tr.operation_footer_row").toggle();

        var par = $("td.th.service_column:hidden").length == 0;

        if (par) {
            $("tr.brief_operation_header_row").removeClass("brief_operation_header_row").addClass("full_operation_header_row");
            $("td.service_column").hide();
            $("td.service_provider_column").hide();
            $("input[name=showDetails]").each(function() {
                this.value = "<s:text name="payments.operations.list.with_detailed" />";
            });
        } else {
            $("tr.full_operation_header_row").removeClass("full_operation_header_row").addClass("brief_operation_header_row");
            $("td.service_column").show();
            $("td.service_provider_column").show();
            $("input[name=showDetails]").each(function() {
                this.value = "<s:text name="payments.operations.list.without_detailed" />";
            });
        }

        documentSearch(!par);
    }

    function documentSearch(par) {
        if (par) {
            $("#serviceTypeFilter").removeAttr("disabled");
        } else {
            $("#serviceTypeFilter").attr("disabled", true);
        }
        $("#documentSearch").val(par);
    }

</script>
