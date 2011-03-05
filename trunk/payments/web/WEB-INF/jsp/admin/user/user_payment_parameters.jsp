<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<script type="text/javascript">

    var defaultPP = "<s:property value="paymentPointFilter.selectedId" />";
    var defaultC = "<s:property value="cashboxFilter.selectedId" />";
    var pc = "select[name='paymentCollectorFilter.selectedId']";
    var pp = "select[name='paymentPointFilter.selectedId']";
    var c = "select[name='cashboxFilter.selectedId']";
    var ppFilterBody = "#ppFilterBody";
    var ppFilter = "#ppFilter";
    var cFilterBody = "#cFilterBody";
    var cFilter = "#cFilter";

    function selectPC(start) {

        var isStart = start != undefined && start;
        var $pc = $(pc);
        var $pp = $(pp);

        eraseCashbox();

        if ($pc.val() <= 0) {
            erasePaymentPoint();
            return;
        } else {
            $pp.val(-1);
        }

        var params = {
            "paymentCollectorFilter.selectedId":$pc.val()
        };

        if (isStart) {
            params["paymentPointFilter.selectedId"] = defaultPP;
        }

        $.post("<s:url action="paymentPointFilterAjax" namespace="/payments" includeParams="none" />", params,
                function(data, status) {
                    if (status == "success") {
                        $(pp).val(-1);
                        $(ppFilterBody).html(data).ready(function() {
                            $(pp).change(function() {
                                selectPP();
                            });
                            $(ppFilter).show("fast");
                        });
                        if (isStart) {
                            selectPP(true);
                        }

                    }

                }
        );

    }

    function erasePaymentPoint() {
        $(ppFilter).hide("fast");
        $(ppFilterBody).html("");
    }

    function eraseCashbox() {
        $(cFilter).hide("fast");
        $(cFilterBody).html("");
    }

    function selectPP(start) {

        var isStart = start != undefined && start;
        var $pp = $(pp);

        if ($pp.length == 0 || $pp.val() <= 0) {
            eraseCashbox();
            return;
        } else {
            $(c).val(-1);
        }

        var params = {
            "paymentPointFilter.selectedId":$pp.val()
        };

        if (isStart) {
            params["cashboxFilter.selectedId"] = defaultC;
        }

        $.post("<s:url action="cashboxFilterAjax" namespace="/payments" includeParams="none" />", params,
                function(data, status) {
                    if (status == "success") {
                        $(cFilterBody).html(data).ready(function() {
                            $(cFilter).show("fast");
                        });

                    }
                }
        );

    }

    $("#result").ready(function() {
        $(pc).get(0).setAttribute("onchange", "selectPC();");
        selectPC(true);
    });

</script>

<s:form action="userPaymentParametersEdit"  method="post">

    <s:hidden name="preference.username" />

    <table id="result" cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr class="cols_1">
            <td class="col"><s:text name="admin.user.name" />:</td>
            <td class="col"><s:property value="preference.username" /></td>
        </tr>
        <tr class="cols_1">
            <td class="col"><s:text name="admin.payment.parameters.payment_collector_id" />:</td>
            <td class="col"><%@include file="/WEB-INF/jsp/orgs/filters/payment_collector_filter.jsp"%></td>
        </tr>
        <tr id="ppFilter" class="cols_1" style="display:none;">
            <td class="col"><s:text name="admin.payment.parameters.payment_point_id" />:</td>
            <td class="col" id="ppFilterBody"></td>
        </tr>
        <tr id="cFilter" class="cols_1" style="display:none;">
            <td class="col"><s:text name="admin.payment.parameters.cashbox_id" />:</td>
            <td class="col" id="cFilterBody"></td>
        </tr>
        <tr class="cols_1">
            <td colspan="2">
                <input type="submit" name="submitted" class="btn-exit" value="<s:text name="common.save" />" />
            </td>
        </tr>
    </table>

</s:form>
