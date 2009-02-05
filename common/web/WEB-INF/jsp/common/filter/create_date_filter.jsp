<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:if test="createDateFilter.readOnly"><s:property value="createDateFilter.stringDate" /></s:if><s:else>
<input type="text" name="createDateFilter.stringDate" id="createDateFilter"
		value="<s:property value="createDateFilter.stringDate" />" />
<img src="<s:url value="/resources/common/js/jscalendar/img.gif" includeParams="none"/>" alt=""
	 id="trigger_createDateFilter"
	 style="cursor: pointer; border: 1px solid red;"
	 title="<s:text name="common.calendar"/>"
	 onmouseover="this.style.background='red';"
	 onmouseout="this.style.background='';"/>
<script type="text/javascript">
Calendar.setup({
	inputField	 : "createDateFilter",
	ifFormat	 : "%Y/%m/%d",
	button		 : "trigger_createDateFilter",
	align		 : "Tl"
});
</script></s:else>
