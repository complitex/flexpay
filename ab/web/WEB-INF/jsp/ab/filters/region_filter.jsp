<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<s:text name="ab.region"/>&nbsp;
<select name="regionFilter.selectedId">
	<s:iterator value="regionFilter.names">
		<option value="<s:property value="region.id"/>"
				<s:if test="%{region.id == regionFilter.selectedId}">selected</s:if> >
			<s:property value="name"/>
		</option>
	</s:iterator>
</select>