<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<fieldset class="fieldset">
    <legend class="legend">
        <s:text name="payments.payment_point.detail.status" />&nbsp;:&nbsp;
        <s:property value="tradingDayControlPanel != null ? tradingDayControlPanel.processStatus : processStatus" />,&nbsp;
        <s:text name="payments.payment_point.detail.available_actions" />:&nbsp;
    </legend>

    <s:if test="cashbox != null">

        <s:if test="tradingDayControlPanel.availableCommands.isEmpty()">
            <br/>
            <s:text name="payments.payment_point.detail.no_action_available" />
            <br/>
        </s:if><s:else>
            <s:iterator value="tradingDayControlPanel.availableCommands" id="button">
                <input type="button" onclick="tradingDay(this);" value="<s:property value="button" />" />
            </s:iterator>
        </s:else>
        
    </s:if><s:else>
        <s:if test="open">
            <input type="button" value="<s:text name="common.close" />"
                   onclick="tradingDay(<s:property value="@org.flexpay.payments.actions.tradingday.ProcessTradingDayControlPanelAction@COMMAND_STOP" />);" />
        </s:if><s:else>
            <input type="button" value="<s:text name="common.open" />"
                   onclick="tradingDay(<s:property value="@org.flexpay.payments.actions.tradingday.ProcessTradingDayControlPanelAction@COMMAND_OPEN" />);" />
        </s:else>
    </s:else>
</fieldset>

<script type="text/javascript">

    function tradingDay(button) {
        $.post("<s:url action="processTradingDay" includeParams="none" />",
                {
                    <s:if test="cashbox != null">
                        "cashbox.id":<s:property value="cashbox.id" />,
                        "tradingDayControlPanel.command":button.value
                    </s:if><s:elseif test="paymentPoint != null">
                        "paymentPoint.id":<s:property value="paymentPoint.id" />,
                        command:button
                    </s:elseif><s:elseif test="paymentCollector != null">
                        "paymentCollector.id":<s:property value="paymentCollector.id" />,
                        command:button
                    </s:elseif>
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