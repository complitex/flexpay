
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<title><tiles:getAsString name="title"/></title>
	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/common/style/fp.css"/>"/>

	<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/jquery-1.3.2.min.js" includeParams="none"/>"></script>
	<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/jquery.protify-0.2.min.js" includeParams="none"/>"></script>
	<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/validate/jquery.validate.min.js" includeParams="none" />"></script>
	<link rel="stylesheet" type="text/css" href="<s:url value="/resources/common/js/jquery/jquery-ui/css/smoothness/jquery-ui-1.7.1.custom.min.css" includeParams="none" />"/>
	<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/jquery-ui/development-bundle/external/bgiframe/jquery.bgiframe.yui.js" includeParams="none" />"></script>
	<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/jquery-ui/jquery-ui-1.7.1.custom.min.js" includeParams="none" />"></script>
	<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/jquery-ui/development-bundle/ui/i18n" includeParams="none" />/ui.datepicker-<s:if test="%{#session.WW_TRANS_I18N_LOCALE != null}"><s:text name="%{#session.WW_TRANS_I18N_LOCALE}" /></s:if><s:else>ru</s:else>.js"></script>

</head>
<body>
    <table cellpadding="0" cellspacing="0" border="0" width="100%">
        <%--<tr>--%>
            <%--<td>--%>
                <%--<tiles:insertAttribute name="header" />--%>
            <%--</td>--%>
        <%--</tr>--%>
        <tr>
            <td>
                <tiles:insertAttribute name="body" />
            </td>
        </tr>
    </table>

    <tiles:insertAttribute name="footer" />

</body>
</html>
