<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<s:if test="cashboxFilter.readOnly">
	<s:hidden id="" name="cashboxFilter.selectedId" />
	<s:property value="getTranslationName(cashboxFilter.selected.names)" />
</s:if><s:else>
    <select name="cashboxFilter.selectedId"<s:if test="cashboxFilter.needAutoChange"> onchange="this.form.submit();"</s:if><s:if test="cashboxFilter.disabled"> disabled="disabled"</s:if> class="form-select">
	<s:if test="cashboxFilter.allowEmpty"><option value="-1"><s:text name="orgs.cashbox" /></option></s:if>
    <s:iterator value="cashboxFilter.cashboxes">
	    <option value="<s:property value="id" />"<s:if test="id == cashboxFilter.selectedId"> selected</s:if>><s:property value="getTranslationName(names)" /></option></s:iterator>
    </select>
</s:else>
