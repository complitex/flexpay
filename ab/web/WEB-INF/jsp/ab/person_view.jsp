<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<a href="<s:url action="personEdit"><s:param name="person.id" value="person.id" /></s:url>"><s:text name="common.edit" /></a>
<br />

<%@include file="person_view_fio.jsp" %>

<br />
<%@include file="person_view_registration.jsp" %>

<br />
<%@include file="person_view_identities.jsp" %>

<br />
<%@include file="person_view_atributes.jsp" %>
