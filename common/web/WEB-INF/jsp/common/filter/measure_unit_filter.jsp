<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<s:if test="measureUnitFilter.isReadOnly()">
	<s:hidden name="measureUnitFilter.selectedId" />
	<s:property value="getTranslationName(measureUnitFilter.selected.names)" />
</s:if><s:else>
    <select name="measureUnitFilter.selectedId" class="form-select">
        <s:if test="measureUnitFilter.allowEmpty">
            <option value="-1"><s:text name="common.measure_unit" /></option>
        </s:if>
        <s:iterator value="measureUnitFilter.measureUnits">
            <option value="<s:property value="id" />"<s:if test="id == measureUnitFilter.selectedId"> selected</s:if>>
                <s:property value="getTranslationName(unitNames)" />
            </option>
        </s:iterator>
    </select>
</s:else>
