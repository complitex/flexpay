<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<s:form id="fobjects">
	<s:hidden name="account.id" />

	<table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr>
            <td colspan="5">
                <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp" %>
            </td>
        </tr>
		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="1%">
                <input type="checkbox" disabled="1" onchange="FP.setCheckboxes(this.checked, 'objectIds');">
			</td>
			<td class="th"><s:text name="common.month"/></td>
			<td class="th"><s:text name="eirc.quittance.number"/></td>
			<td class="th">&nbsp;</td>
		</tr>
		<s:iterator value="%{quittances}" status="status">
			<tr valign="middle" class="cols_1">
				<td class="col" align="right">
                    <s:property value="%{#status.index + pager.thisPageFirstElementNumber + 1}"/>
				</td>
				<td class="col">
					<input type="checkbox" disabled="1" value="<s:property value="%{id}"/>" name="objectIds"/>
				</td>
				<td class="col"><s:date format="MM/yyyy" name="dateFrom" /></td>
				<td class="col"><s:property value="orderNumber" /></td>
				<td class="col">
					<a href="<s:url action='quittancePay'><s:param name="quittance.id" value="id"/></s:url>">
						<s:text name="eirc.quittances.quittance_pay.title"/></a></td>
			</tr>
		</s:iterator>
		<tr>
			<td colspan="5">
				<%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp" %>
			</td>
		</tr>
	</table>
</s:form>
