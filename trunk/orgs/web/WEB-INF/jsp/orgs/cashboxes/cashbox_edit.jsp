<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<s:form action="cashboxEdit">

	<s:hidden name="cashbox.id" />

	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr valign="top" class="cols_1">
			<td class="col"><s:text name="orgs.cashbox.name" />:</td>
			<td class="col">
				<s:iterator value="names">
                    <s:set name="l" value="%{getLang(key)}" />
					<s:textfield name="names[%{key}]" value="%{value}" />(<s:if test="%{#l.default}">*</s:if><s:property value="%{getLangName(#l)}" />)<br />
				</s:iterator>
			</td>
		</tr>
        <tr valign="middle" class="cols_1">
            <td class="col"><s:text name="orgs.payment_point" />:</td>
            <td class="col">
                <%@include file="/WEB-INF/jsp/orgs/filters/payment_point_filter.jsp"%>
            </td>
        </tr>
		<tr valign="middle" class="cols_1">
			<td colspan="2">
                <input type="submit" name="submitted" class="btn-exit" value="<s:text name="common.save" />" />
            </td>
		</tr>
	</table>

</s:form>
