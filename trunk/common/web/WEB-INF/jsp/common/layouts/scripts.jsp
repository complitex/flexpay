<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<link rel="stylesheet" type="text/css" href="<s:url value="/resources/common/style/fp.css" includeParams="none" />" />

<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/jquery-1.4.3.min.js" includeParams="none" />"></script>
<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/protify/jquery.protify-0.3.min.js" includeParams="none" />"></script>
<%--<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/dump/jquery.dump.min.js" includeParams="none" />"></script>--%>
<script type="text/javascript" src="<s:url value="/resources/common/js/flexpay_common.js" includeParams="none" />"></script>
<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/log/jquery.log.js" includeParams="none" />"></script>
<script type="text/javascript">
    FP.base = "<s:url value="/" includeParams="none" />";
    FP.messages = {
        loading: "<s:text name="common.loading" />"
    };
	$.ajaxSetup({
		// redirect to the root as 401 status code received
		error: function(xhr) {
			if (xhr.status == 401) {
				window.location.href = "<s:url value="/" includeParams="none" />";
			}
		},
        traditional : true
	});

</script>
