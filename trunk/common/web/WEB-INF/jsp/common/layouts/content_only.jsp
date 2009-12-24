<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<tiles:importAttribute ignore="true" name="title" scope="request" />
	<title><s:text name="%{#attr.title}" /></title>

    <%@include file="scripts.jsp"%>

</head>
<body>

    <tiles:insertAttribute name="body" ignore="true" />

</body>
</html>
