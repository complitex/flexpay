<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<select id="processStateFilter" name="processStateFilter.selectedId"<s:if test="processStateFilter.readOnly"> disabled</s:if><s:if test="processStateFilter.needAutoChange"> onchange="pagerAjax();"</s:if> class="form-select">
	<s:if test="processStateFilter.allowEmpty">
		<option value="-1">&nbsp;</option>
	</s:if>

	<s:iterator value="processStateFilter.processStates">
		<option value="<s:property value="id" />"<s:if test="id == processStateFilter.selectedId"> selected</s:if>>
			<s:property value="getText(name)" />
		</option>
	</s:iterator>
</select>