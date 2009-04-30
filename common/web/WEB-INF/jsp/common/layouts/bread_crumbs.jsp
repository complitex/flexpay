<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:iterator	value="%{#session['com.strutsschool.interceptors.breadcrumbs']}" status="status">
    <nobr>
        <s:if test="!#status.last">
            <a href="<s:url value="%{getUrl()}" includeParams="none" />"><s:text name="%{wildPortionOfName}"/></a>
        </s:if><s:else>
            <s:text name="%{wildPortionOfName}"/>
        </s:else>
        <s:if test="!#status.last">
            &#187;
        </s:if>
    </nobr>
</s:iterator>
