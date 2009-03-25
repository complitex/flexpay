
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<tiles:importAttribute ignore="true" name="title" scope="request" />
	<title><s:text name="%{#attr.title}" /></title>
	<link rel="stylesheet" type="text/css" href="<s:url value="/resources/common/style/fp.css" includeParams="none" />" />
    <link rel="stylesheet" type="text/css" href="<s:url value="/resources/common/js/jquery/jquery-ui/css/smoothness/jquery-ui-1.7.1.custom.min.css" includeParams="none" />"/>

    <script type="text/javascript" src="<s:url value="/resources/common/js/jquery/jquery-1.3.2.min.js" includeParams="none"/>"></script>
    <script type="text/javascript" src="<s:url value="/resources/common/js/jquery/jquery.protify-0.2.min.js" includeParams="none"/>"></script>
    <script type="text/javascript" src="<s:url value="/resources/common/js/jquery/jquery-ui/development-bundle/external/bgiframe/jquery.bgiframe.yui.js" includeParams="none" />"></script>
    <script type="text/javascript" src="<s:url value="/resources/common/js/jquery/jquery-ui/jquery-ui-1.7.1.custom.min.js" includeParams="none" />"></script>
    <script type="text/javascript" src="<s:url value="/resources/common/js/jquery/jquery-ui/development-bundle/ui/i18n" includeParams="none" />/ui.datepicker-<s:if test="%{#session.WW_TRANS_I18N_LOCALE != null}"><s:text name="%{#session.WW_TRANS_I18N_LOCALE}" /></s:if><s:else>ru</s:else>.js"></script>

    <script type="text/javascript" src="<s:url value="/resources/common/js/flexpay_common.js" includeParams="none"/>"></script>
    <script type="text/javascript">FP.base = "<s:url value="/" includeParams="none"/>";</script>
</head>
<body>

<tiles:insertAttribute name="body" ignore="true" />

</body>
</html>