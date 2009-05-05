
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@ taglib prefix="menu" uri="http://struts-menu.sourceforge.net/tag" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <tiles:importAttribute ignore="true" name="title" scope="request" />
    <title><s:text name="%{#attr.title}" /></title>
    <link rel="stylesheet" type="text/css" href="<s:url value="/resources/common/style/fp.css" includeParams="none"/>"/>
    <link rel="stylesheet" type="text/css" href="<s:url value="/resources/common/js/jquery/jquery-ui/css/smoothness/jquery-ui-1.7.1.custom.min.css" includeParams="none" />"/>
	<link rel="stylesheet" type="text/css" href="<s:url value="/resources/common/js/jquery/timepicker/jquery.timepickr.css" includeParams="none"/>"/>

    <script type="text/javascript" src="<s:url value="/resources/common/js/jquery/jquery-1.3.2.min.js" includeParams="none" />"></script>
    <script type="text/javascript" src="<s:url value="/resources/common/js/jquery/jquery.protify-0.2.min.js" includeParams="none" />"></script>
	<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/validate/jquery.validate.min.js" includeParams="none" />"></script>
	<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/jquery-ui/development-bundle/external/bgiframe/jquery.bgiframe.yui.js" includeParams="none" />"></script>
	<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/jquery-ui/jquery-ui-1.7.1.custom.min.js" includeParams="none" />"></script>
	<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/jquery-ui/development-bundle/ui/i18n" includeParams="none" />/ui.datepicker-<s:if test="%{#session.WW_TRANS_I18N_LOCALE != null}"><s:text name="%{#session.WW_TRANS_I18N_LOCALE}" /></s:if><s:else>ru</s:else>.js"></script>
	<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/timepicker/jquery.timepickr.fixed.for.flexpay.js" includeParams="none"/>"></script>

	<script type="text/javascript" src="<s:url value="/resources/common/js/flexpay_common.js" includeParams="none" />"></script>
    <script type="text/javascript">FP.base = "<s:url value="/" includeParams="none" />";</script>
</head>
<body>

<tiles:insertAttribute name="header" />

    <table cellpadding="0" cellspacing="0" border="0" width="100%">
        <tr>
            <menu:useMenuDisplayer name="fpDisplayer">
                <menu:displayMenu name="FPMenu" levelBegin="1" levelEnd="1" />
            </menu:useMenuDisplayer>

            <tiles:insertAttribute name="language" />

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

    <div class="breadCrumbs">
        <tiles:insertAttribute name="breadCrumbs" />
    </div>

<!--
<div class="gradusnik">
    <div class="gradusnik-padding">
        <tiles:insertAttribute name="gradusnik" />
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
                <tiles:insertAttribute name="tip" />
            </div>
        </div>
        -->
    </div>

</div>

<tiles:insertAttribute name="footer" />

</body>
</html>
