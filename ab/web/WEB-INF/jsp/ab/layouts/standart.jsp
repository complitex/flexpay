<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <tiles:useAttribute id="title" name="title" classname="java.lang.String" />
    <title><spring:message code="<%= title%>" /></title>
    <link rel="stylesheet" type="text/css" href="<c:url value="/style/fp.css"/>"/>
	<script type="text/javascript" src="<c:url value="/js/prototype.js" />">
	</script>
</head>
<body>

<tiles:insertAttribute name="header"/>
<tiles:insertAttribute name="menu1"/>
<tiles:insertAttribute name="menu2"/>
<!--
<div class="gradusnik">
    <div class="gradusnik-padding">
        <tiles:insertAttribute name="gradusnik"/>
    </div>
</div>
-->

<div style="padding-left: 25px; padding-right: 25px;">
    <h1><spring:message code="<%= title%>" /></h1>
</div>

<div class="columns">
    <div class="left">
        <div class="left-padding">
            <div class="explorer">
                <tiles:insertAttribute name="menu34"/>
            </div>
        </div>
    </div>

    <div class="main">
        <div class="main-content">
            <tiles:insertAttribute name="body"/>
        </div>

        <!--
        <div class="main-tip">
            <div class="main-tip-padding">
                <tiles:insertAttribute name="tip"/>
            </div>
        </div>
        -->
    </div>

</div>


<tiles:insertAttribute name="footer"/>


</body>
</html>