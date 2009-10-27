<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<%@include file="/WEB-INF/jsp/common/includes/jquery_ui.jsp"%>

<script type="text/javascript">
    FP.calendars("dateIntervalBegin", true);
    FP.calendars("dateIntervalEnd", true);
</script>

<s:text name="ab.from" />
<input type="text" name="dateInterval_begin" id="dateIntervalBegin" value="<s:date name="dateInterval.begin" format="yyyy/MM/dd" />" readonly="readonly" />
&nbsp;&nbsp;
<s:text name="ab.till" />
<input type="text" name="dateInterval_end" id="dateIntervalEnd" value="<s:date name="dateInterval.end" format="yyyy/MM/dd" />" readonly="readonly" />
