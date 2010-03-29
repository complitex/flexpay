<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<s:if test="dataSourceFilter.isReadOnly()">
	<s:hidden name="dataSourceFilter.selectedId" />
	<s:property value="dataSourceFilter.selected.description" />
</s:if><s:else>
    <select name="dataSourceFilter.selectedId" class="form-select">
        <s:if test="dataSourceFilter.allowEmpty">
            <option value="-1"><s:text name="common.data_source" /></option>
        </s:if>
        <s:iterator value="dataSourceFilter.dataSources">
            <option value="<s:property value="id" />"<s:if test="id == dataSourceFilter.selectedId"> selected</s:if>>
                <s:property value="description" />
            </option>
        </s:iterator>
    </select>
</s:else>
