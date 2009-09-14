<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<link rel="stylesheet" type="text/css" href="<s:url value="/resources/common/style/fp.css" includeParams="none"/>"/>
<link rel="stylesheet" type="text/css" href="<s:url value="/resources/common/js/jquery/jquery-ui/css/smoothness/jquery-ui-1.7.1.custom.min.css" includeParams="none" />"/>
<style type="text/css">@import "<s:url value="/resources/common/js/jquery/timeentry/jquery.timeentry.css" includeParams="none"/>";</style>

<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/jquery-1.3.2.min.js" includeParams="none" />"></script>
<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/jquery.protify-0.2.min.js" includeParams="none" />"></script>
<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/jquery.dump.js" includeParams="none" />"></script>
<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/validate/jquery.validate.min.js" includeParams="none" />"></script>
<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/jquery-ui/development-bundle/external/bgiframe/jquery.bgiframe.yui.js" includeParams="none" />"></script>
<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/jquery-ui/jquery-ui-1.7.1.custom.min.js" includeParams="none" />"></script>
<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/jquery-ui/development-bundle/ui/i18n" includeParams="none" />/ui.datepicker-<s:if test="%{#session.WW_TRANS_I18N_LOCALE != null}"><s:text name="%{#session.WW_TRANS_I18N_LOCALE}" /></s:if><s:else>ru</s:else>.js"></script>
<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/timeentry/jquery.timeentry.js" includeParams="none"/>"></script>
<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/timeentry" includeParams="none"/>/jquery.timeentry<s:if test="%{#session.WW_TRANS_I18N_LOCALE != null}">-<s:text name="%{#session.WW_TRANS_I18N_LOCALE}" /></s:if><s:else>-ru</s:else>.js"></script>

<script type="text/javascript" src="<s:url value="/resources/common/js/flexpay_common.js" includeParams="none" />"></script>
<script type="text/javascript">
    FP.base = "<s:url value="/" includeParams="none" />";
    FP.messages = {
        loading: "<s:text name="common.loading" />"
    }
</script>
