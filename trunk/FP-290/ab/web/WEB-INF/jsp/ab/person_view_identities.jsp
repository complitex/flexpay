<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<tr class="cols_1">
		<td class="col_1s" colspan="14"><b><s:text name="ab.person.documents"/></b></td>
	</tr>
	<tr class="cols_1">
		<td class="th">&nbsp;</td>
		<td class="th" width="1%"><s:text name="ab.person.identity_default"/></td>
		<td class="th"><s:text name="ab.person.identity_type"/></td>
		<td class="th"><s:text name="ab.person.last_name"/></td>
		<td class="th"><s:text name="ab.person.first_name"/></td>
		<td class="th"><s:text name="ab.person.middle_name"/></td>
		<td class="th"><s:text name="ab.person.sex"/></td>
		<td class="th"><s:text name="ab.person.birth_date"/></td>
		<td class="th"><s:text name="ab.person.organization"/></td>
		<td class="th"><s:text name="ab.person.begin_date"/></td>
		<td class="th"><s:text name="ab.person.end_date"/></td>
		<td class="th"><s:text name="ab.person.serial_number"/></td>
		<td class="th"><s:text name="ab.person.document_number"/></td>
		<td class="th"><s:text name="ab.person.identity.attributes"/></td>
	</tr>
	<s:iterator value="person.personIdentities" status="rowstatus">
		<tr class="cols_1">
			<td class="col" align="right"><s:property
					value="%{#rowstatus.index + 1}"/>&nbsp;</td>
			<td class="col" width="1%"><input type="checkbox" disabled="disabled"
											  <s:if test="%{default}">checked</s:if>>
			</td>
			<td class="col"><s:property value="%{getTranslation(identityType.translations).name}"/></td>
			<td class="col"><s:property value="%{lastName}"/></td>
			<td class="col"><s:property value="%{firstName}"/></td>
			<td class="col"><s:property value="%{middleName}"/></td>
			<td class="col">
				<s:if test="%{isMan()}"><s:text name="ab.person.sex.man.short"/></s:if>
				<s:if test="%{isWoman()}"><s:text name="ab.person.sex.woman.short"/></s:if> 
			</td>
			<td class="col"><s:property value="%{format(birthDate)}"/></td>
			<td class="col"><s:property value="%{organization}"/></td>
			<td class="col"><s:property value="%{format(beginDate)}"/></td>
			<td class="col"><s:property value="%{format(endDate)}"/></td>
			<td class="col"><s:property value="%{serialNumber}"/></td>
			<td class="col"><s:property value="%{documentNumber}"/></td>
			<td class="col">
				<s:iterator value="personIdentityAttributes">
					<s:property value="%{name}" /> : <s:property value="%{value}" /><br />
				</s:iterator>
			</td>
		</tr>
	</s:iterator>
</table>
