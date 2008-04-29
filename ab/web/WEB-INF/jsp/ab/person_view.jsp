<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<s:iterator value="person.personIdentities" status="rowstatus">
		<tr class="cols_1">
			<td class="th"><s:text name="ab.person.identity_type"/></td>
			<td class="col_1"><s:property value="%{getTranslation(identityType.translations).name}"/></td>
		</tr>
		<tr class="cols_1">
			<td class="th"><s:text name="ab.person.last_name"/></td>
			<td class="col"><s:property value="%{lastName}"/></td>
		</tr>
		<tr class="cols_1">
			<td class="th"><s:text name="ab.person.first_name"/></td>
			<td class="col"><s:property value="%{firstName}"/></td>
		</tr>
		<tr class="cols_1">
			<td class="th"><s:text name="ab.person.middle_name"/></td>
			<td class="col"><s:property value="%{middleName}"/></td>
		</tr>
		<tr class="cols_1">
			<td class="th"><s:text name="ab.person.birth_date"/></td>
			<td class="col"><s:property value="%{format(birthDate)}"/></td>
		</tr>
		<tr class="cols_1">
			<td class="th"><s:text name="ab.person.organization"/></td>
			<td class="col"><s:property value="%{organization}"/></td>
		</tr>
		<tr class="cols_1">
			<td class="th"><s:text name="ab.person.begin_date"/></td>
			<td class="col"><s:property value="%{format(beginDate)}"/></td>
		</tr>
		<tr class="cols_1">
			<td class="th"><s:text name="ab.person.end_date"/></td>
			<td class="col"><s:property value="%{format(endDate)}"/></td>
		</tr>
		<tr class="cols_1">
			<td class="th"><s:text name="ab.person.serial_number"/></td>
			<td class="col"><s:property value="%{serialNumber}"/></td>
		</tr>
		<tr class="cols_1">
			<td class="th"><s:text name="ab.person.document_number"/></td>
			<td class="col"><s:property value="%{documentNumber}"/></td>
		</tr>
		<tr>
			<td colspan="2" height="3" bgcolor="#4a4f4f"/>
		</tr>
	</s:iterator>
	<tr class="cols_1">
		<td class="th"><s:text name="ab.person.registration_address"/></td>
		<td class="col_1">
			<s:property value="address"/>
			<a href="<s:url value="/dicts/setRegistrationForm.action" />">
						<s:text name="ab.edit"/>
			</a>
		</td>
	</tr>
	<tr>
		<td colspan="2" height="3" bgcolor="#4a4f4f"/>
	</tr>
</table>
