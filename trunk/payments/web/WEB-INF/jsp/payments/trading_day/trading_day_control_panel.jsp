<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<fieldset class="fieldset">
	<legend class="legend">
		<s:text name="payments.payment_point.detail.status"/>&nbsp;:&nbsp;
		<s:property value="tradingDayControlPanel.processStatus"/>,&nbsp;
		<s:text name="payments.payment_point.detail.available_actions"/>:&nbsp;
	</legend>

	<s:if test="tradingDayControlPanel.availableCommands.size == 0">
		<br/>
		<s:text name="payments.payment_point.detail.no_action_available"/>
		<br/>
	</s:if>
	<s:else>
		<s:iterator value="tradingDayControlPanel.availableCommands" id="button">
			<input type="submit" name="tradingDayControlPanel.command" class="" value="<s:property value="button"/>"/>
		</s:iterator>
	</s:else>
</fieldset>