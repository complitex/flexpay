<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:if test="buildingsFilter.readOnly">
	<s:hidden name="buildingsFilter.selectedId" value="%{buildingsFilter.selectedId}"/>
	<s:property value="%{getBuildingNumber(buildingsFilter.selected)}"/>
</s:if><s:else>
	<select name="buildingsFilter.selectedId" id="buildingsFilter" onchange="this.form.submit();" class="form-select">
		<s:if test="buildingsFilter.allowEmpty">
			<option value="-1"><s:text name="common.select"/></option>
		</s:if>
		<s:iterator value="buildingsFilter.buildingses">
			<option value="<s:property value="id"/>"<s:if
					test="%{id == buildingsFilter.selectedId}"> selected</s:if>><s:property
					value="%{@org.flexpay.ab.util.TranslationUtil@getBuildingNumber(buildingAttributes, userPreferences.locale)}"/></option>
		</s:iterator>
	</select>
</s:else>
