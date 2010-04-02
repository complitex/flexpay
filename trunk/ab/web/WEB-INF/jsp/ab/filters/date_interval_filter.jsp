<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_bgiframe.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_ui_core.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_ui_datepicker.jsp"%>

<script type="text/javascript">
    FP.calendars("dateIntervalBegin", true);
    FP.calendars("dateIntervalEnd", true);
</script>

<s:text name="common.from" />
<input type="text" name="dateInterval_begin" id="dateIntervalBegin" size="10" value="<s:date name="dateInterval.begin" format="yyyy/MM/dd" />" readonly="readonly" />
&nbsp;&nbsp;
<s:text name="common.till" />
<input type="text" name="dateInterval_end" id="dateIntervalEnd" size="10" value="<s:date name="dateInterval.end" format="yyyy/MM/dd" />" readonly="readonly" />
