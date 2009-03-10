<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@ taglib prefix="menu" uri="http://struts-menu.sf.net/tag" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <tiles:importAttribute ignore="true" name="title" scope="request" />
    <title><s:text name="%{#attr.title}" /></title>
    <link rel="stylesheet" type="text/css" href="<s:url value="/resources/common/style/fp.css" includeParams="none"/>"/>

	<!-- calendar stylesheet -->
	<link rel="stylesheet" type="text/css" href="<s:url value="/resources/common/js/jscalendar/calendar-blue.css" includeParams="none"/>" />

	<script type="text/javascript" src="<s:url value="/resources/common/js/prototype.js" includeParams="none"/>"></script>
	<script type="text/javascript" src="<s:url value="/resources/common/js/flexpay_common.js" includeParams="none"/>"></script>

	<!-- main calendar program -->
	<script type="text/javascript" src="<s:url value="/resources/common/js/jscalendar/calendar.js" includeParams="none"/>"></script>

	<!-- language for the calendar -->
	<!-- TODO: set language selector switchable -->
	<script type="text/javascript" src="<s:url value="/resources/common/js/jscalendar/lang/calendar-ru.js" includeParams="none"/>"></script>

	<!-- the following script defines the Calendar.setup helper function, which makes
		 adding a calendar a matter of 1 or 2 lines of code. -->
	<script type="text/javascript" src="<s:url value="/resources/common/js/jscalendar/calendar-setup.js" includeParams="none"/>"></script>

	<!-- Text field autocomplete script -->
	<link rel="stylesheet" type="text/css" href="<s:url value="/resources/common/js/autosuggest/autosuggest_inquisitor.css" includeParams="none"/>" />
	<script type="text/javascript" src="<s:url value="/resources/common/js/autosuggest/bsn.AutoSuggest_2.1.3.js" includeParams="none"/>"></script>

	<!-- Window script -->
	<script type="text/javascript" src="<s:url value="/resources/common/js/windows_js_1.3/javascripts/window.js" includeParams="none"/>"></script>
	<script type="text/javascript" src="<s:url value="/resources/common/js/windows_js_1.3/javascripts/debug.js" includeParams="none"/>"></script>
	<script type="text/javascript" src="<s:url value="/resources/common/js/windows_js_1.3/javascripts/effects.js" includeParams="none"/>"></script>
	<link href="<s:url value="/resources/common/js/windows_js_1.3/themes/default.css" includeParams="none"/>" rel="stylesheet" type="text/css"/>
	<link href="<s:url value="/resources/common/js/windows_js_1.3/themes/alert.css" includeParams="none"/>" rel="stylesheet" type="text/css"/>
	<link href="<s:url value="/resources/common/js/windows_js_1.3/themes/spread.css" includeParams="none"/>" rel="stylesheet" type="text/css"/>
	<link href="<s:url value="/resources/common/js/windows_js_1.3/themes/alphacube.css" includeParams="none"/>" rel="stylesheet" type="text/css"/>

</head>
<body>

<tiles:insertAttribute name="header"/>

    <table cellpadding="0" cellspacing="0" border="0" width="100%">
        <tr>
            <menu:useMenuDisplayer name="fpDisplayer">
                <menu:displayMenu name="FPMenu" levelBegin="1" levelEnd="1" />
            </menu:useMenuDisplayer>

            <tiles:insertAttribute name="language"/>
            <%--<%@ include file="/WEB-INF/jsp/common/layouts/language_switch.jsp" %>--%>

            <!--<form name="search">-->
            <td class="topmenu_form_search" nowrap="1" >&nbsp;
                <!--<input type="text" class="form-search">&nbsp;<input type="button" value="Найти" class="btn-search" />-->
            </td>
            <!--</form>-->
        </tr>
    </table>

    <menu:useMenuDisplayer name="fpDisplayer">
        <menu:displayMenu name="FPMenu" levelBegin="2" levelEnd="2" />
    </menu:useMenuDisplayer>

<!--
<div class="gradusnik">
    <div class="gradusnik-padding">
        <tiles:insertAttribute name="gradusnik"/>
    </div>
</div>
-->

<div class="columns">

    <menu:useMenuDisplayer name="fpDisplayer">
        <menu:displayMenu name="FPMenu" levelBegin="3" />
    </menu:useMenuDisplayer>

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
