<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:if test="closeDateFilter.readOnly">
    <s:property value="closeDateFilter.stringDate" />
</s:if><s:else>
    <input type="text" name="closeDateFilter.stringDate" id="closeDateFilter" value="<s:property value="closeDateFilter.stringDate" />" />
    <img src="<s:url value="/resources/common/js/jscalendar/img.gif" includeParams="none"/>" alt=""
         id="trigger_closeDateFilter"
         style="cursor:pointer;border:1px solid red;"
         title="<s:text name="common.calendar"/>"
         onmouseover="this.style.background='red';"
         onmouseout="this.style.background='';"/>
    <script type="text/javascript">
        Calendar.setup({
            inputField : "closeDateFilter",
            ifFormat   : "%Y/%m/%d",
            button     : "trigger_closeDateFilter",
            align      : "Tl"
        });
    </script>
</s:else>
