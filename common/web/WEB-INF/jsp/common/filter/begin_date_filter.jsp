<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:if test="beginDateFilter.readOnly"><s:property value="beginDateFilter.stringDate" /></s:if><s:else>
<input type="text" name="beginDateFilter.stringDate" id="beginDateFilter"
		value="<s:property value="beginDateFilter.stringDate" />" />
<img src="<s:url value="/resources/common/js/jscalendar/img.gif" includeParams="none"/>" alt=""
	 id="trigger_beginDateFilter"
	 style="cursor: pointer; border: 1px solid red;"
	 title="<s:text name="common.calendar"/>"
	 onmouseover="this.style.background='red';"
	 onmouseout="this.style.background='';"/>
<script type="text/javascript">
Calendar.setup({
	inputField	 : "beginDateFilter",
	ifFormat	 : "%Y/%m/%d",
	button		 : "trigger_beginDateFilter",
	align		 : "Tl"
});
</script></s:else>
