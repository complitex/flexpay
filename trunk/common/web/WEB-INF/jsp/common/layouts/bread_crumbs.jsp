<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<div style="float:left;width:100%;">
<span>
	<s:iterator	value="%{#session['com.strutsschool.interceptors.breadcrumbs']}" status="status">
		<s:if test="#status.index > 0">
			&#187;
		</s:if>
		<nobr><a href="<s:url value="%{getUrl()}" includeParams="none" />"><s:property value="wildPortionOfName"/></a></nobr>
	</s:iterator>
</span>
</div>
