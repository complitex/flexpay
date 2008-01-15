<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<s:text name="ab.country"/>&nbsp;
<select name="countryFilter.selectedId">
	<s:iterator value="countryFilter.names">
		<option value="<s:property value="country.id"/>"
				<s:if test="%{country.id == countryFilter.selectedId}">selected</s:if> >
			<s:property value="name"/>
		</option>
	</s:iterator>
</select>