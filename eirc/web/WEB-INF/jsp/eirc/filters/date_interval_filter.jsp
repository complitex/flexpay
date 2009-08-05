<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<%@include file="/WEB-INF/jsp/common/includes/jquery_ui.jsp"%>

<script type="text/javascript">
    FP.calendars("#dateIntervalBegin", true);
    FP.calendars("#dateIntervalEnd", true);
</script>

<s:text name="ab.from"/>
<input type="text" name="fromDate" id="dateIntervalBegin" value="<s:property value="fromDate"/>" readonly="readonly" />
&nbsp;&nbsp;
<s:text name="ab.till"/>
<input type="text" name="tillDate" id="dateIntervalEnd" value="<s:property value="tillDate" />" readonly="readonly" />
