<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:text name="ab.from"/>
<input type="text" name="dateInterval_begin" id="dateInterval.begin"
		value="<s:date name="dateInterval.begin" format="yyyy/MM/dd"/>" />
<img src="<s:url value="/resources/js/jscalendar/img.gif" includeParams="none"/>" alt=""
	 id="trigger_dateInterval.begin"
	 style="cursor: pointer; border: 1px solid red;"
	 title="<s:text name="common.calendar"/>"
	 onmouseover="this.style.background='red';"
	 onmouseout="this.style.background='';"/>
&nbsp;&nbsp;
<s:text name="ab.till"/>
<input type="text" name="dateInterval_end" id="dateInterval.end"
	   value="<s:date name="dateInterval.end" format="yyyy/MM/dd"/>" />
<img src="<s:url value="/resources/js/jscalendar/img.gif" includeParams="none"/>" alt=""
	 id="trigger_dateInterval.end"
	 style="cursor: pointer; border: 1px solid red;"
	 title="<s:text name="common.calendar"/>"
	 onmouseover="this.style.background='red';"
	 onmouseout="this.style.background='';"/>
<script type="text/javascript">
Calendar.setup({
	inputField	 : "dateInterval.begin",
	ifFormat	 : "%Y/%m/%d",
	button		 : "trigger_dateInterval.begin",
	align		 : "Tl"
});
Calendar.setup({
	inputField	 : "dateInterval.end",
	ifFormat	 : "%Y/%m/%d",
	button		 : "trigger_dateInterval.end",
	align		 : "Tl"
});
</script>
