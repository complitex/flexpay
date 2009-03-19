<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<link rel="stylesheet" type="text/css" href="<s:url value="/resources/common/js/jquery/jquery-ui/css/smoothness/jquery-ui-1.7.custom.css" includeParams="none" />"/>

<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/jquery-ui/jquery-ui-1.7.custom.min.js" includeParams="none" />"></script>
<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/jquery-ui/development-bundle/ui/i18n" includeParams="none" />/ui.datepicker-<s:if test="%{#session.WW_TRANS_I18N_LOCALE != null}"><s:text name="%{#session.WW_TRANS_I18N_LOCALE}" /></s:if><s:else>ru</s:else>.js"></script>
