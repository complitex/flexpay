
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<s:form id="fobjects">

	<%@ include file="/WEB-INF/jsp/ab/filters/groups/country_region_town_streetname_building_apartment.jsp" %>

	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="1%">
                <input type="checkbox" disabled="1" onchange="FP.setCheckboxes(this.checked, 'objectIds');">
			</td>
			<td class="th"><s:text name="eirc.eirc_account"/></td>
			<td class="th"><s:text name="eirc.eirc_account.person"/></td>
			<td class="th"><s:text name="eirc.eirc_account.apartment"/></td>
			<td class="th">&nbsp;</td>
		</tr>
		<s:iterator value="%{eircAccounts}" status="status">
			<tr valign="middle" class="cols_1">
				<td class="col" align="right">
                    <s:property value="%{#status.index + pager.thisPageFirstElementNumber + 1}"/>
				</td>
				<td class="col">
					<input type="checkbox" disabled="1" value="<s:property value="%{id}"/>" name="objectIds"/>
				</td>
				<td class="col">
					<a href="<s:url action='eircAccountView'><s:param name="eircAccount.id" value="%{id}"/></s:url>">
						<s:property value="%{accountNumber}"/>
					</a>
				</td>
				<td class="col">
					<s:if test="person != null">
						<a href="<s:url action='personView' namespace="/dicts" includeParams="none"><s:param name="person.id" value="%{person.id}"/></s:url>">
							<s:property value="%{getFIO(person)}"/>
						</a>
					</s:if><s:else>
						(*)&nbsp;<s:property value="%{consumerInfo.FIO}"/>
					</s:else>
				</td>
				<td class="col">
					<a href="<s:url action='apartmentRegistration' namespace="/dicts" includeParams="none"><s:param name="apartment.id" value="%{apartment.id}"/></s:url>">
						<s:property value="%{getAddress(apartment)}"/>
					</a>
				</td>
				<td class="col">
					<s:set name="quittance" value="%{getAccountQuittance(id)}" />
					<s:if test="#quittance != null">
					<a href="<s:url action='quittancePay' includeParams="none"><s:param name="quittance.id" value="%{#quittance.id}"/><s:param name="source" value="'address'"/></s:url>">
						<s:text name="eirc.quittances.quittance_pay.pay" />
					</a></s:if><s:else>
						<s:text name="eirc.quittances.no_quittances" />
					</s:else>
				</td>
			</tr>
		</s:iterator>
		<tr>
			<td colspan="5">
				<%@include file="/WEB-INF/jsp/ab/filters/pager.jsp" %>
			</td>
		</tr>
	</table>
</s:form>
