<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<s:if test="serviceProviderFilter.readOnly">
	<s:hidden name="serviceProviderFilter.selectedId" />
	<s:property value="getTranslationName(serviceProviderFilter.selected.organization.names)"/>
</s:if><s:else>
    <select id="serviceProviderFilter" name="serviceProviderFilter.selectedId" <s:if test="serviceProviderFilter.needAutoChange"> onchange="this.form.submit();"</s:if> class="form-select">
	<s:if test="serviceProviderFilter.allowEmpty">
        <option value="-1"><s:text name="eirc.service_provider" /></option>
    </s:if><s:iterator value="serviceProviderFilter.instances">
    	<option value="<s:property value="id" />"<s:if test="id == serviceProviderFilter.selectedId"> selected</s:if>><s:property value="getTranslationName(organization.names)" /></option></s:iterator>
    </select>
</s:else>
