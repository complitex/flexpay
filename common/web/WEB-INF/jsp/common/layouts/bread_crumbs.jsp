<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<s:iterator	value="userPreferences.crumbs" status="status">
    <nobr>
        <s:if test="!#status.last">
            <a href="<s:url value="%{getUrl()}" />" class="crumb"><s:text name="%{wildPortionOfName}"/></a>
        </s:if><s:else>
            <strong><s:text name="%{wildPortionOfName}"/></strong>
        </s:else>
        <s:if test="!#status.last">
            &#187;
        </s:if>
    </nobr>
</s:iterator>
