<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<%
    if (session != null) {
        session.invalidate();
    }
%>

<c:redirect url="/" />
