<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<input type="button" class="btn-exit"
       onclick="window.location='<s:url action="personEdit"><s:param name="person.id" value="person.id"/></s:url>';"
       value="<s:text name="common.edit"/>" />

<%@include file="person_view_fio.jsp" %>

<br />
<%@include file="person_view_registration.jsp" %>

<br />
<%@include file="person_view_identities.jsp" %>

<br />
<%@include file="person_view_attributes.jsp" %>
