<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<s:form id="fobjects">
	<table cellpadding="3" cellspacing="1" border="0" width="100%">

		<tr>
			<td colspan="4">
				<%@ include file="/WEB-INF/jsp/ab/filters/groups/country_region_town_streetname_building.jsp" %>
			</td>
		</tr>

		<tr>
			<td colspan="4">
				<%@ include file="/WEB-INF/jsp/ab/filters/pager.jsp" %>
			</td>
		</tr>
		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="1%">
				<input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');">
			</td>
			<td colspan="2" class="th" width="98%"><s:text name="ab.apartment"/></td>
		</tr>
		<s:iterator value="%{apartments}" status="status">
			<tr valign="middle" class="cols_1">
				<td class="col_1s" align="right">
					<s:property value="%{#status.index + pager.thisPageFirstElementNumber + 1}"/>
				</td>
				<td class="col">
					<input type="checkbox" value="<s:property value="%{id}"/>" name="objectIds"/>
				</td>
				<td class="col">
					<s:property value="%{number}"/>
				</td>
				<td class="col">
					<a href="<s:url action='accountsForQuittancesList'><s:param name="apartmentFilter.selectedId" value="%{id}"/></s:url>">
						<s:text name="eirc.quittances.payment.account_search"/>
					</a>
				</td>
			</tr>
		</s:iterator>
		<tr>
			<td colspan="4">
				<%@ include file="/WEB-INF/jsp/ab/filters/pager.jsp" %>
			</td>
		</tr>
	</table>
</s:form>
