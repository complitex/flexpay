<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<s:if test="cashboxFilter.readOnly">
	<s:hidden name="cashboxFilter.selectedId" />
	<s:property value="getTranslationName(cashboxFilter.selected.names)" />
</s:if><s:else>
    <select id="cashboxFilter.selectedId" name="cashboxFilter.selectedId"<s:if test="cashboxFilter.needAutoChange"> onchange="this.form.submit();"</s:if> class="form-select">
	<s:if test="cashboxFilter.allowEmpty">
        <option value="-1"><s:text name="payments.cashbox" /></option>
    </s:if><s:iterator value="cashboxFilter.cashboxes">
	    <option value="<s:property value="id" />"<s:if test="id == cashboxFilter.selectedId"> selected</s:if>><s:property value="getTranslationName(names)" /></option></s:iterator>
    </select>
</s:else>
