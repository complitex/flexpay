<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<select id="processNameFilter" name="processNameFilter.selectedId" <s:if test="processNameFilter.readOnly">disabled</s:if> <s:if test="processNameFilter.needAutoChange">onchange="pagerAjax();"</s:if> class="form-select">
	<s:if test="processNameFilter.allowEmpty">
		<option value="-1">&nbsp;</option>
	</s:if>

	<s:iterator value="processNameFilter.processNames">
		<option value="<s:property value="id" />" <s:if test="id == processNameFilter.selectedId"> selected</s:if>>
			<s:property value="getText(name)" />
		</option>
	</s:iterator>
</select>
