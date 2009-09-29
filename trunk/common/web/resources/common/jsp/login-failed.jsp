<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<% response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); %>

<tiles:insertDefinition name="common.login-failed" />
