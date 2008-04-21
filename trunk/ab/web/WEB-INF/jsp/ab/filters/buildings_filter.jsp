<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<s:text name="ab.building"/>&nbsp;
<select name="buildingsFilter.selectedId" onchange="this.form.submit()"><s:iterator value="buildingsFilter.buildingses">
	<option value="<s:property value="building.id"/>"<s:if test="%{building.id == buildingsFilter.selectedId}"> selected</s:if>><s:property value="%{getBuildingNumber(buildingAttributes)}"/></option></s:iterator>
</select>