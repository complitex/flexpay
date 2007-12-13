<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<s:text name="ab.country"/>&nbsp;
<select name="countryFilter.selectedCountryId">
	<s:iterator value="countryFilter.countryNames">
		<option value="<s:property value="country.id"/>"
				<s:if test="%{country.id == countryFilter.selectedCountryId}">selected</s:if> >
			<s:property value="name"/>
		</option>
	</s:iterator>
</select>