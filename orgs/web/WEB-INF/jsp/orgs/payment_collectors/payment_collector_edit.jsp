<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<s:form action="paymentCollectorEdit">

	<s:hidden name="collector.id" />

	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr valign="middle" class="cols_1">
			<td class="col"><s:text name="orgs.organization" />:</td>
			<td class="col"><%@include file="../filters/organization_filter.jsp"%></td>
		</tr>
		<tr valign="top" class="cols_1">
			<td class="col"><s:text name="orgs.orginstance.description" />:</td>
			<td class="col">
                <s:iterator value="descriptions"><s:set name="l" value="%{getLang(key)}" />
                    <s:textfield name="descriptions[%{key}]" value="%{value}" />(<s:if test="%{#l.default}">*</s:if><s:property value="%{getLangName(#l)}" />)<br />
                </s:iterator>
			</td>
		</tr>
		<tr valign="middle" class="cols_1">
			<td class="col"><s:text name="orgs.payment_collector.email"/>:</td>
			<td class="col"><s:textfield name="email" /></td>
		</tr>
		<tr valign="middle" class="cols_1">
			<td class="col"><s:text name="orgs.payment_collector.trading_day_begin_time"/>:</td>
			<td class="col"><%@include file="/WEB-INF/jsp/common/filter/begin_time_filter.jsp" %></td>
		</tr>
		<tr valign="middle" class="cols_1">
			<td class="col"><s:text name="orgs.payment_collector.trading_day_end_time"/>:</td>
			<td class="col"><%@include file="/WEB-INF/jsp/common/filter/end_time_filter.jsp" %></td>
		</tr>
		<tr valign="middle">
			<td colspan="2">
                <input type="submit" class="btn-exit" name="submitted" value="<s:text name="common.save"/>" />
            </td>
		</tr>
	</table>

</s:form>
