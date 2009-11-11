<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror />

<s:form id="fpackets">
	<s:hidden name="packet.id" />
	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="1%"><input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');" />
			</td>
			<td class="th"><s:text name="common.summ" /></td>
			<td class="th"><s:text name="orgs.address" /></td>
			<td class="th"><s:text name="ab.person.fio" /></td>
			<td class="th"><s:text name="eirc.eirc_account" /></td>
			<td class="th"><s:text name="month" /></td>
			<td class="th"><s:text name="common.status" /></td>
			<td class="th">&nbsp;</td>
		</tr>
		<s:iterator value="payments" status="status">
			<tr valign="middle" class="cols_1">
				<td class="col" width="1%"><s:property
						value="%{#status.index + pager.thisPageFirstElementNumber + 1}" /></td>
				<td class="col" width="1%"><input type="checkbox" name="objectIds"
												  value="<s:property value="%{id}"/>" />
				</td>
				<td class="col"><s:property value="amount" /></td>
				<td class="col"><s:property value="%{getAddress(quittance)}" /></td>
				<td class="col"><s:property value="%{getFIO(quittance)}" /></td>
				<td class="col"><s:property value="quittance.eircAccount.accountNumber" /></td>
				<td class="col"><s:date format="yyyy/MM" name="quittance.dateTill" /></td>
				<td class="col"><s:text name="%{paymentStatus.i18nName}" /></td>
				<td class="col"><a
						href="<s:url action="quittancePacketEdit"><s:param name="packet.id" value="%{id}"/></s:url>">
					<s:text name="common.edit" />
				</a></td>
			</tr>
		</s:iterator>
		<tr>
			<td colspan="10">
				<%@include file="/WEB-INF/jsp/common/filter/pager/pager.jsp" %>
				<%--<input type="submit" value="<s:text name="common.delete_selected" />" class="btn-exit"--%>
					   <%--onclick="$('#fpackets').attr('action', '<s:url action="bankDelete"/>');" />--%>
				<input type="button" class="btn-exit"
					   onclick="window.location='<s:url action="quittanceSearch"><s:param name="packet.id" value="packet.id"/></s:url>';"
					   value="<s:text name="common.new"/>" />
			</td>
		</tr>
	</table>
</s:form>
