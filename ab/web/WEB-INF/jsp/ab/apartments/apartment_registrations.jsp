<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<div id="response">
    <%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>
</div>

<s:if test="apartment != null && apartment.isNotNew()">

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<tr>
		<td colspan="5" width="100%">
			<s:property value="getTranslationName(country.translations)" />,
			<s:property value="getTranslationName(region.currentName.translations)" />,
			<s:property value="getTranslationName(town.currentName.translations)" />,
			<s:property value="getTranslation(street.currentType.translations).shortName" />.
			<s:property value="getTranslationName(street.currentName.translations)" />,
			<s:property value="buildings.format(userPreferences.locale, true)" />,
			<s:text name="ab.apartment" />
			<s:property value="apartment.number" />
		</td>
	</tr>
	<tr>
		<td class="th" width="1%">&nbsp;</td>
		<td class="th"><s:text name="ab.person.fio" /></td>
		<td class="th"><s:text name="ab.person.birth_date" /></td>
		<td class="th"><s:text name="ab.person.registration.begin_date" /></td>
		<td class="th"><s:text name="ab.person.registration.end_date" /></td>
	</tr>
	<s:iterator value="apartment.validPersonRegistrations" status="status">
		<tr valign="middle" class="cols_1">
			<td class="col_1s" align="right"><s:property value="#status.index + 1" /></td>
			<td class="col">
				<a href="<s:url action="personView"><s:param name="person.id" value="person.id" /></s:url>">
					<s:property value="person.defaultIdentity.lastName" />
					<s:property value="person.defaultIdentity.firstName" />
					<s:property value="person.defaultIdentity.middleName" />
				</a>
			</td>
			<td class="col"><s:property value="format(person.defaultIdentity.birthDate)" /></td>
			<td class="col"><s:property value="format(beginDate)" /></td>
			<td class="col"><s:property value="format(endDate)" /></td>
		</tr>
	</s:iterator>
	<tr>
		<td colspan="5" width="100%">
			<input type="button" class="btn-exit"
				   onclick="window.location='<s:url action="apartmentRegistrationsHistory"><s:param name="apartment.id" value="apartment.id" /><s:param name="buildings.id" value="buildings.id" /></s:url>'"
				   value="<s:text name="ab.apartment.registrations_history.title" />" />
		</td>
	</tr>
</table>
</s:if>
