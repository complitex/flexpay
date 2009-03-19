
<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror />

<s:form id="fobjects">

	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td colspan="5"><%@include file="../filters/payments_collector_filter.jsp"%> </td>
		</tr>
		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="1%"><input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');"></td>
            <td class="th"><s:text name="eirc.payments_collector"/></td>
            <td class="th"><s:text name="ab.address"/></td>
			<td class="th">&nbsp;</td>
		</tr>
		<s:iterator value="points" status="status">
			<tr valign="middle" class="cols_1">
				<td class="col" width="1%">
					<s:property value="%{#status.index + pager.thisPageFirstElementNumber + 1}"/>
				</td>
				<td class="col" width="1%">
					<input type="checkbox" name="objectIds" value="<s:property value="%{id}"/>"/>
				</td>
                <td class="col"><s:property value="getCollectorName(collector)"/></td>
                <td class="col"><s:property value="address"/></td>
				<td class="col"><a href="<s:url action="paymentPointEdit"><s:param name="point.id" value="%{id}"/></s:url>">
						 <s:text name="common.edit"/></a></td>
			</tr>
		</s:iterator>
		<tr>
			<td colspan="5">
				<%@include file="/WEB-INF/jsp/ab/filters/pager.jsp" %>
				<input type="submit" value="<s:text name="common.delete_selected" />" class="btn-exit"
					   onclick="$('#fobjects').attr('action', '<s:url action="paymentPointsDelete" includeParams="none" />');"/>
				<input type="button" class="btn-exit"
					   onclick="window.location='<s:url action="paymentPointEdit"><s:param name="point.id" value="0"/></s:url>';"
					   value="<s:text name="common.new"/>"/>
			</td>
		</tr>
	</table>
</s:form>
