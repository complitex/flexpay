<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<link rel="stylesheet" type="text/css" href="<s:url value="/resources/common/style/fp.css" includeParams="none" />" />

<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/jquery-1.3.2.min.js" includeParams="none" />"></script>
<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/protify/jquery.protify-0.3.min.js" includeParams="none" />"></script>
<%--<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/dump/jquery.dump.min.js" includeParams="none" />"></script>--%>
<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/validate/jquery.validate.pack.js" includeParams="none" />"></script>

<style type="text/css">@import "<s:url value="/resources/common/js/jquery/timeentry/jquery.timeentry.css" includeParams="none" />";</style>
<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/timeentry/jquery.timeentry.pack.js" includeParams="none" />"></script>
<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/timeentry/jquery.timeentry-%{#session.WW_TRANS_I18N_LOCALE != null ? #session.WW_TRANS_I18N_LOCALE : 'ru'}.js" includeParams="none" />"></script>

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
