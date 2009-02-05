<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:if test="endDateFilter.readOnly"><s:property value="endDateFilter.stringDate" /></s:if><s:else>
<input type="text" name="endDateFilter.stringDate" id="endDateFilter"
		value="<s:property value="endDateFilter.stringDate" />" />
<img src="<s:url value="/resources/common/js/jscalendar/img.gif" includeParams="none"/>" alt=""
	 id="trigger_endDateFilter"
	 style="cursor: pointer; border: 1px solid red;"
	 title="<s:text name="common.calendar"/>"
	 onmouseover="this.style.background='red';"
	 onmouseout="this.style.background='';"/>
<script type="text/javascript">
Calendar.setup({
	inputField	 : "endDateFilter",
	ifFormat	 : "%Y/%m/%d",
	button		 : "trigger_endDateFilter",
	align		 : "Tl"
});
</script></s:else>
