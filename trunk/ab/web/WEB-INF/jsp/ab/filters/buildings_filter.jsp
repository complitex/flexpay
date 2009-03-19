
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<s:if test="buildingsFilter.readOnly">
	<s:hidden name="buildingsFilter.selectedId" value="%{buildingsFilter.selectedId}" />
	<s:property value="%{getBuildingNumber(buildingsFilter.selected)}" />
</s:if><s:else>
<select name="buildingsFilter.selectedId"  onchange="this.form.submit();" class="form-select"><s:iterator value="buildingsFilter.buildingses">
	<option value="<s:property value="building.id"/>"<s:if test="%{building.id == buildingsFilter.selectedId}"> selected</s:if>><s:property value="%{getBuildingNumber(buildingAttributes)}"/></option></s:iterator>
</select>
</s:else>