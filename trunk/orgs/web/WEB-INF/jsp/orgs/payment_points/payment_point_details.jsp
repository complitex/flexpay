<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror />

<s:form id="fcashboxes">
    <table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
            <td colspan="5">
				<s:text name="eirc.payment_collector"/>:
				<%@include file="../filters/payment_collector_filter.jsp"%>
			</td>
		</tr>
		<tr>
            <td colspan="5">
				<s:text name="eirc.payment_point"/>:
				<%@include file="../filters/payment_points_filter.jsp"%>
			</td>
		</tr>

		<tr>
            <td colspan="5">
				<s:text name="eirc.payment_point.address"/>:
				<s:property value="%{getPaymentPointAddress()}"/>
			</td>
		</tr>
		<tr>
            <td colspan="5">
				<s:text name="eirc.cashboxes"/>:				
			</td>
		</tr>
        <tr>
            <td class="th" width="1%">&nbsp;</td>
            <td class="th" width="1%"><input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');"></td>
			<td class="th"><s:text name="eirc.cashbox.id"/></td>
            <td class="th"><s:text name="eirc.cashbox.name"/></td>
            <td class="th">&nbsp;</td>
        </tr>
        <s:iterator value="cashboxes" status="status">
            <tr valign="middle" class="cols_1">
                <td class="col" width="1%"><s:property
                        value="%{#status.index + pager.thisPageFirstElementNumber + 1}"/></td>
                <td class="col" width="1%"><input type="checkbox" name="objectIds" value="<s:property value="%{id}"/>"/></td>
				<td class="col"><s:property value="%{id}"/></td>
                <td class="col"><s:property value="getTranslation(names).name"/></td>
                <td class="col"><a href="<s:url action="cashboxEdit"><s:param name="cashbox.id" value="%{id}"/></s:url>"><s:text name="common.edit"/></a></td>
            </tr>
        </s:iterator>
        <tr>
            <td colspan="5">
                <%@include file="/WEB-INF/jsp/common/filter/pager/pager.jsp" %>
                <input type="submit" value="<s:text name="common.delete_selected" />" class="btn-exit"
                       onclick="$('#fcashboxes').attr('action', '<s:url action="cashboxDelete" includeParams="none" />');"/>
                <input type="button" class="btn-exit"
                       onclick="window.location='<s:url action="cashboxEdit"><s:param name="cashbox.id" value="0"/></s:url>';"
                       value="<s:text name="common.new"/>"/>
            </td>
        </tr>
    </table>
</s:form>