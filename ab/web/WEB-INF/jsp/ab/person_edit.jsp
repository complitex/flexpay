<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<%@include file="person_edit_fio.jsp"%>

<s:if test="person.notNew">
<br />
<%@include file="person_set_registration_form.jsp"%>
</s:if>

<br />
<%@include file="person_view_identities.jsp"%>
