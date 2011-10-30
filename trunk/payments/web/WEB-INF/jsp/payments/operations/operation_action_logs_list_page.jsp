<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_timeentry.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_bbq.jsp"%>
<%@include file="/WEB-INF/jsp/payments/includes/stylesheet.jsp"%>

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
        <td colspan="4">
            <input type="button" class="btn-exit" value="<s:text name="payments.operation_action_logs.list.filter" />" onclick="pagerAjax();" />
        </td>
    </tr>
</table>

<span id="result"></span>

<script type="text/javascript">

    var defaultCFilter = $("#cFilterBody").html();

    var $c = $("select[name='cashboxFilter.selectedId']");

    $("select[name='paymentPointFilter.selectedId']").get(0).setAttribute("onchange", "selectPP();");
    $c.get(0).setAttribute("onchange", "");

    $(function() {
        pagerAjax();
    });

    function pagerAjax(element) {
        var cValue = $("select[name='cashboxFilter.selectedId']").val();
        if ($("select[name='cashboxFilter.selectedId']").attr("disabled")) {
            cValue = -1
        }
        FP.pagerAjax(element, {
            action:"<s:url action="operationActionLogsListAjax" />",
            params: {
                "cashboxFilter.selectedId":cValue,
                "paymentPointFilter.selectedId":$("select[name='paymentPointFilter.selectedId']").val(),
                "beginDateFilter.stringDate":$("input[name='beginDateFilter.stringDate']").val(),
                "endDateFilter.stringDate":$("input[name='endDateFilter.stringDate']").val(),
                "beginTimeFilter.stringDate":$("input[name='beginTimeFilter.stringDate']").val(),
                "endTimeFilter.stringDate":$("input[name='endTimeFilter.stringDate']").val()
            }
        });
    }

    function sorterAjax() {
        pagerAjax();
    }

    function selectPP() {

        var $pp = $("select[name='paymentPointFilter.selectedId']");

        if ($pp.length == 0 || $pp.val() <= 0) {
            eraseCashbox();
            return;
        } else {
            $c.val(-1);
        }

        var params = {
            "paymentPointFilter.selectedId":$pp.val()
        };

        $.post("<s:url action="cashboxFilterAjax" namespace="/payments" />", params,
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

</script>
