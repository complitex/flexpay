<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<%@include file="/WEB-INF/jsp/common/includes/jquery_bgiframe.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_ui_core.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_ui_datepicker.jsp"%>

<script type="text/javascript">
    FP.calendars("dateIntervalBegin", true);
    FP.calendars("dateIntervalEnd", true);
</script>

<s:text name="common.from" />
<input type="text" name="fromDate" id="dateIntervalBegin" value="<s:property value="fromDate" />" readonly="readonly" />
&nbsp;&nbsp;
<s:text name="common.till" />
<input type="text" name="tillDate" id="dateIntervalEnd" value="<s:property value="tillDate" />" readonly="readonly" />
