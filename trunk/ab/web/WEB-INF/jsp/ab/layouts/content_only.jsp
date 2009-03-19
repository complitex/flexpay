
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<tiles:importAttribute ignore="true" name="title" scope="request" />
	<title><s:text name="%{#attr.title}" /></title>
	<link rel="stylesheet" type="text/css" href="<s:url value="/resources/common/style/fp.css" includeParams="none" />" />

    <script type="text/javascript" src="<s:url value="/resources/common/js/jquery/jquery-1.3.2.min.js" includeParams="none"/>"></script>
    <script type="text/javascript" src="<s:url value="/resources/common/js/jquery/jquery.protify-0.2.js" includeParams="none"/>"></script>
    <script type="text/javascript" src="<s:url value="/resources/common/js/flexpay_common.js" includeParams="none"/>"></script>
</head>
<body>

<tiles:insertAttribute name="body" ignore="true" />

</body>
</html>