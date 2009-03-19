<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:if test="createDateFilter.readOnly">
    <s:property value="createDateFilter.stringDate" />
</s:if><s:else>
    <%@include file="/WEB-INF/jsp/common/jquery_ui.jsp"%>

    <script type="text/javascript">
        FP.calendars("#createDateFilter", "<s:url value="/resources/common/js/jquery/jquery-ui/images/calendar.gif" includeParams="none" />");
    </script>
    <input type="text" name="createDateFilter.stringDate" id="createDateFilter" value="<s:property value="createDateFilter.stringDate" />" readonly="readonly" />

</s:else>
