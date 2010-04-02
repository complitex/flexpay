<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<fieldset class="fieldset">
    <legend class="legend">
        <s:text name="payments.payment_point.detail.status" />&nbsp;:&nbsp;
        <s:property value="tradingDayControlPanel.processStatus" />,&nbsp;
        <s:text name="payments.payment_point.detail.available_actions" />:&nbsp;
    </legend>
    <s:if test="tradingDayControlPanel.availableCommands.isEmpty()">
        <br/>
        <s:text name="payments.payment_point.detail.no_action_available" />
        <br/>
    </s:if><s:else>
        <s:iterator value="tradingDayControlPanel.availableCommands" id="button">
            <input type="button" onclick="tradingDay(this);" value="<s:property value="button" />" />
        </s:iterator>
    </s:else>
</fieldset>

<script type="text/javascript">

    function tradingDay(button) {
        $.post("<s:url action="processTradingDay" includeParams="none" />",
                {
                    <s:if test="cashbox != null">
                        "cashbox.id":<s:property value="cashbox.id" />,
                    </s:if><s:elseif test="paymentPoint != null">
                        "paymentPoint.id":<s:property value="paymentPoint.id" />,
                    </s:elseif>
                    "tradingDayControlPanel.command":button.value
                },
                function(data, status) {
                    if (data == "" && status == "success") {
                        window.location.href = FP.base;
                    }
                    if (pagerAjax != undefined) {
                        pagerAjax();
                    }
        });
    }

</script>