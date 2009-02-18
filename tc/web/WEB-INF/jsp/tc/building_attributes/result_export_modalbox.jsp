<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>

<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<script type="text/javascript">

    var x = 0;
    var y = 0;

    function getXY(e) {
        var left = document.documentElement.scrollLeft;
        var top = document.documentElement.scrollTop;
        if (Prototype.Browser.IE) {
            x = event.clientX + left;
            y = event.clientY + top;
        } else {
            x = e.clientX + left;
            y = e.clientY + top;
        }
    }
    document.onmousemove = getXY;

    function uploadResults() {
        $('uploadTCResults').submit();
        parent.Windows.closeAll();
    }
</script>


<s:form action="uploadTCResults" id="uploadTCResults">
    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr>
            <td>
                <nobr>

                    <s:hidden name="buildingId" value="%{buildingId}"/>
                    <s:hidden name="calculationDate" value="%{calculationDate}"/>
                    <s:hidden name="submitted" value="true"/>

                    <s:text name="tc.upload_tc_results.date"/>
                    <s:textfield name="date" id="date" readonly="true" value="%{date}"/>
                    <img src="<c:url value="/resources/common/js/jscalendar/img.gif"/>" alt=""
                         id="trigger.date"
                         style="cursor: pointer; border: 1px solid red;"
                         title="<s:text name="common.calendar"/>"
                         onmouseover="this.style.background='red';"
                         onmouseout="this.style.background='';"/>
                    <script type="text/javascript">
                        Calendar.setup({
                            inputField     : "date",
                            ifFormat     : "%Y/%m/%d",
                            button         : "trigger.date",
                            align         : "Tl",
                            singleClick  : true,
                            position: [x, y]
                        });
                    </script>

                    <input type="button" class="btn-exit" value="<s:text name="%{getText('tc.upload')}"/>"
                           onclick="uploadResults();"/>
                </nobr>
            </td>
        </tr>
    </table>
</s:form>