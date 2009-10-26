<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<link rel="stylesheet" type="text/css" href="<s:url value="/resources/common/style/fp.css" includeParams="none" />" />
<link rel="stylesheet" type="text/css" href="<s:url value="/resources/common/js/jquery/jquery-ui/css/smoothness/jquery-ui-1.7.1.custom.min.css" includeParams="none" />" />
<style type="text/css">@import "<s:url value="/resources/common/js/jquery/timeentry/jquery.timeentry.css" includeParams="none" />";</style>

<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/jquery-1.3.2.min.js" includeParams="none" />"></script>
<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/jquery.protify-0.3.min.js" includeParams="none" />"></script>
<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/jquery.dump.min.js" includeParams="none" />"></script>
<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/validate/jquery.validate.pack.js" includeParams="none" />"></script>
<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/jquery-ui/development-bundle/external/bgiframe/jquery.bgiframe.yui.js" includeParams="none" />"></script>
<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/jquery-ui/jquery-ui-1.7.1.custom.min.js" includeParams="none" />"></script>
<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/jquery-ui/development-bundle/ui/i18n" includeParams="none" />/ui.datepicker-<s:if test="%{#session.WW_TRANS_I18N_LOCALE != null}"><s:text name="%{#session.WW_TRANS_I18N_LOCALE}" /></s:if><s:else>ru</s:else>.js"></script>
<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/timeentry/jquery.timeentry.pack.js" includeParams="none" />"></script>
<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/timeentry" includeParams="none" />/jquery.timeentry<s:if test="%{#session.WW_TRANS_I18N_LOCALE != null}">-<s:text name="%{#session.WW_TRANS_I18N_LOCALE}" /></s:if><s:else>-ru</s:else>.js"></script>

<script type="text/javascript" src="<s:url value="/resources/common/js/flexpay_common.js" includeParams="none" />"></script>
<script type="text/javascript">
    FP.base = "<s:url value="/" includeParams="none" />";
    FP.messages = {
        loading: "<s:text name="common.loading" />"
    };

	$.ajaxSetup({
		// redirect to the root as 401 status code received
		error: function(xhr, status, err) {
			if (xhr.status == 401) {
				window.location.href = "<s:url value="/" includeParams="none" />";
			}
		}
	});

</script>
