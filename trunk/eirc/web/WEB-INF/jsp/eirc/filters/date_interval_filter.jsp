
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<%@include file="/WEB-INF/jsp/common/jquery_ui.jsp"%>

<script type="text/javascript">
    FP.calendars("#dateIntervalBegin", "<s:url value="/resources/common/js/jquery/jquery-ui/images/calendar.gif" includeParams="none" />");
    FP.calendars("#dateIntervalEnd", "<s:url value="/resources/common/js/jquery/jquery-ui/images/calendar.gif" includeParams="none" />");
</script>

<s:text name="ab.from"/>
<input type="text" name="fromDate" id="dateIntervalBegin" value="<s:property value="fromDate"/>" readonly="readonly" />
&nbsp;&nbsp;
<s:text name="ab.till"/>
<input type="text" name="tillDate" id="dateIntervalEnd" value="<s:property value="tillDate" />" readonly="readonly" />
