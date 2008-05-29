<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<select name="buildingsFilter.selectedId" <s:if test="buildingsFilter.readOnly">disabled="1"</s:if> onchange="this.form.submit()" class="form-select"><s:iterator value="buildingsFilter.buildingses">
	<option value="<s:property value="building.id"/>"<s:if test="%{building.id == buildingsFilter.selectedId}"> selected</s:if>><s:property value="%{getBuildingNumber(buildingAttributes)}"/></option></s:iterator>
</select>