<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <tiles:importAttribute ignore="true" name="title" scope="request" />
    <title><s:text name="%{#attr.title}" /></title>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/common/style/fp.css"/>"/>

	<!-- calendar stylesheet -->
	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/common/js/jscalendar/calendar-blue.css"/>" />

	<script type="text/javascript" src="<c:url value="/resources/common/js/prototype.js" />"></script>
	<script type="text/javascript" src="<c:url value="/resources/common/js/flexpay_common.js" />"></script>

	<!-- main calendar program -->
	<script type="text/javascript" src="<c:url value="/resources/common/js/jscalendar/calendar.js" />"></script>

	<!-- language for the calendar -->
	<!-- TODO: set language selector switchable -->
	<script type="text/javascript" src="<c:url value="/resources/common/js/jscalendar/lang/calendar-ru.js" />"></script>

	<!-- the following script defines the Calendar.setup helper function, which makes
		 adding a calendar a matter of 1 or 2 lines of code. -->
	<script type="text/javascript" src="<c:url value="/resources/common/js/jscalendar/calendar-setup.js" />"></script>
	
	<!-- Text field autocomplete script -->
	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/common/js/autosuggest/autosuggest_inquisitor.css"/>" />
	<script type="text/javascript" src="<c:url value="/resources/common/js/autosuggest/bsn.AutoSuggest_2.1.3.js" />"></script>
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
    <h1>
      <tiles:insertTemplate template="/WEB-INF/jsp/ab/menu/title.jsp" />
    </h1>
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
            <tiles:insertAttribute name="body" ignore="true" />
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