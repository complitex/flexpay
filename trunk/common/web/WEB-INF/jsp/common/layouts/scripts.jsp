<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<link rel="stylesheet" type="text/css" href="<s:url value="/resources/common/style/fp.css" />" />

<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/jquery-1.5.1.min.js" />"></script>
<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/protify/jquery.protify-0.3.min.js" />"></script>
<%--<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/dump/jquery.dump.min.js" />"></script>--%>
<script type="text/javascript" src="<s:url value="/resources/common/js/flexpay_common.js" />"></script>
<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/log/jquery.log.js" />"></script>
<script type="text/javascript">
    FP.base = "<s:url value="/" />";
    FP.messages = {
        loading: "<s:text name="common.loading" />"
    };
	$.ajaxSetup({
		// redirect to the root as 401 status code received
		error: function(xhr) {
			if (xhr.status == 401) {
				window.location.href = "<s:url value="/" />";
			}
		},
        traditional : true,
//  HACK:
//    jquery-1.5 sets up jsonp for all json ajax requests
//    undo the jsonp setting here so the json requests work again
        jsonp: null,
        jsonpCallback: null
	});

</script>
