
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:if test="endDateFilter.readOnly">
    <s:property value="endDateFilter.stringDate" />
</s:if><s:else>
    <%@include file="/WEB-INF/jsp/common/jquery_ui.jsp"%>

    <script type="text/javascript">
        FP.calendars("#endDateFilter", "<s:url value="/resources/common/js/jquery/jquery-ui/images/calendar.gif" includeParams="none" />");
    </script>
    <input type="text" name="endDateFilter.stringDate" id="endDateFilter" value="<s:property value="endDateFilter.stringDate" />" readonly="readonly" />
</s:else>
