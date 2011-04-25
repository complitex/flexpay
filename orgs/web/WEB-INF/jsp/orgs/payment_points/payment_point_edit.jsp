<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<s:form action="paymentPointEdit">

	<s:hidden name="point.id" />

	<table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr valign="top" class="cols_1">
            <td class="col"><s:text name="orgs.payment_point.name" />:</td>
            <td class="col">
                <s:iterator value="names">
                    <s:set name="l" value="%{getLang(key)}" />
                    <s:textfield name="names[%{key}]" value="%{value}" />(<s:if test="%{#l.default}">*</s:if><s:property value="%{getLangName(#l)}" />)<br />
                </s:iterator>
            </td>
        </tr>
		<tr valign="middle" class="cols_1">
			<td class="col"><s:text name="orgs.payment_collector" />:</td>
			<td class="col"><%@include file="/WEB-INF/jsp/orgs/filters/payment_collector_filter.jsp"%></td>
		</tr>
		<tr valign="middle" class="cols_1">
			<td class="col"><s:text name="orgs.address" />:</td>
			<td class="col">
				<s:textfield name="point.address" />
			</td>
		</tr>
		<tr valign="middle">
			<td colspan="2">
                <input type="submit" class="btn-exit" name="submitted" value="<s:text name="common.save" />" />
            </td>
		</tr>
	</table>

</s:form>
