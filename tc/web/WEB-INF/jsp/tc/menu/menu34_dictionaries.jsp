<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<ul class="folders">
	<li class="open"><b><s:text name="tc.menu2.dictionaries"/></b>
		<ul class="docs">
			<li><a href="<c:url value='/tc/sewerTypesList.action' />"><s:text name="tc.menu34.sewer_types"/></a></li>
		</ul>
	</li>

<%--
    <li><a href="123123">Some folder</a>
        <ul class="docs">
            <li><a href="123">Some document</a></li>
        </ul>
    </li>
--%>

</ul>
