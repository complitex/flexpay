<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<link rel="stylesheet" type="text/css" href="<s:url value="/resources/common/js/jquery/timeentry/jquery.timeentry.css" includeParams="none" />" />
<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/timeentry/jquery.timeentry.pack.js" includeParams="none" />"></script>
<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/timeentry/jquery.timeentry-%{#session.WW_TRANS_I18N_LOCALE != null ? #session.WW_TRANS_I18N_LOCALE : 'ru'}.js" includeParams="none" />"></script>
