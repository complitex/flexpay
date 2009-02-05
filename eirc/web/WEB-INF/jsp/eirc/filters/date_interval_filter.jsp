<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:text name="ab.from"/>
<input type="text" name="fromDate" id="dateInterval.begin"
		value="<s:property value="fromDate"/>" />
<img src="<c:url value="/resources/common/js/jscalendar/img.gif"/>" alt=""
	 id="trigger_dateInterval.begin"
	 style="cursor: pointer; border: 1px solid red;"
	 title="<s:text name="common.calendar"/>"
	 onmouseover="this.style.background='red';"
	 onmouseout="this.style.background='';"/>
&nbsp;&nbsp;
<s:text name="ab.till"/>
<input type="text" name="tillDate" id="dateInterval.end"
	   value="<s:property value="tillDate" />" />
<img src="<c:url value="/resources/common/js/jscalendar/img.gif"/>" alt=""
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
