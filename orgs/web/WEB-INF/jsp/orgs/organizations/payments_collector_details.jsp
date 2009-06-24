<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror />

<s:form id="fobjects">
	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td colspan="6">
				<s:text name="eirc.payments_collector"/>:&nbsp;
				<%@include file="../filters/payments_collector_filter.jsp"%>
			</td>
		</tr>

		<s:if test="%{isPaymentCollectorLoaded()}">
		<tr>
			<td colspan="6">
				<s:text name="eirc.payments_collector.organization_name"/>:&nbsp;
				<s:property value="%{getOrganizationName()}"/>
			</td>
		</tr>
		<tr>
			<td colspan="6">
				<fieldset class="fieldset">
                	<legend class="legend"><s:text name="eirc.payments_collector.description"/></legend>
					<s:property value="%{getPaymentsCollectorDescription()}"/>
				</fieldset>
				<br/>
			</td>
		</tr>
		<tr>
			<td colspan="6">
				<s:text name="eirc.payment_points"/>:
			</td>
		</tr>
		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="1%"><input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');"></td>
			<td class="th"><s:text name="eirc.payment_point.id"/></td>
            <td class="th"><s:text name="eirc.payment_point.name"/></td>
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
				<td class="col"><s:property value="%{id}"/></td>
                <td class="col">
					<a href="<s:url action="paymentPointDetails"><s:param name="paymentsCollectorFilter.selectedId" value="%{paymentsCollector.id}"/>
								<s:param name="paymentPointsFilter.selectedId" value="%{id}"/></s:url>">
						<s:property value="getTranslation(names).name"/>
					</a>
				</td>
                <td class="col"><s:property value="getCollectorName(collector)"/></td>
                <td class="col"><s:property value="address"/></td>
				<td class="col">
					<a href="<s:url action="paymentPointEdit"><s:param name="point.id" value="%{id}"/></s:url>">
						<s:text name="common.edit"/>
					</a>
				</td>
			</tr>
		</s:iterator>
		<tr>
			<td colspan="7">
				<%@include file="/WEB-INF/jsp/common/filter/pager/pager.jsp" %>
				<input type="submit" value="<s:text name="common.delete_selected" />" class="btn-exit"
					   onclick="$('#fobjects').attr('action', '<s:url action="paymentPointsDelete" includeParams="none" />');"/>
				<input type="button" class="btn-exit"
					   onclick="window.location='<s:url action="paymentPointEdit"><s:param name="point.id" value="0"/></s:url>';"
					   value="<s:text name="common.new"/>"/>
			</td>
		</tr>
		</s:if>
	</table>
</s:form>
