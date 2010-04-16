<%@include file="taglibs.jsp"%>
Security roles: <pre><s:iterator value="userPreferences.authorities">
<s:property value="authority" /><br /></pre></s:iterator>
