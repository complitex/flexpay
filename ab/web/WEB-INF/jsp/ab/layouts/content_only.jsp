<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<tiles:importAttribute ignore="true" name="title" scope="request" />
	<title><s:text name="%{#attr.title}" /></title>
	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/common/style/fp.css"/>" />
    <link rel="stylesheet" type="text/css" href="<s:url value="/resources/common/js/jquery/autocomplete/jquery.autocomplete.css" includeParams="none"/>" />

	<!-- calendar stylesheet -->
	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/common/js/jscalendar/calendar-blue.css"/>" />

	<script type="text/javascript" src="<c:url value="/resources/common/js/prototype.js" />"></script>
    <script type="text/javascript" src="<c:url value="/resources/common/js/jquery/jquery-1.3.2.js"/>"></script>
    <script type="text/javascript" src="<s:url value="/resources/common/js/jquery/autocomplete/jquery.autocomplete.js" includeParams="none"/>"></script>
    <script type="text/javascript" src="<s:url value="/resources/common/js/jquery/jquery.protify-0.2.js" includeParams="none"/>"></script>
	<script type="text/javascript" src="<c:url value="/resources/common/js/flexpay_common.js" />"></script>

	<!-- main calendar program -->
	<script type="text/javascript" src="<c:url value="/resources/common/js/jscalendar/calendar_stripped.js" />"></script>

	<!-- language for the calendar -->
	<!-- TODO: set language selector switchable -->
	<script type="text/javascript" src="<c:url value="/resources/common/js/jscalendar/lang/calendar-ru.js" />"></script>

	<!-- the following script defines the Calendar.setup helper function, which makes
		 adding a calendar a matter of 1 or 2 lines of code. -->
	<script type="text/javascript" src="<c:url value="/resources/common/js/jscalendar/calendar-setup_stripped.js" />"></script>
</head>
<body>

<tiles:insertAttribute name="body" ignore="true" />

</body>
</html>