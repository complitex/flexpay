<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<s:actionerror />

<s:form>

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr valign="top" class="cols_1">
            <td class="col">
                <s:text name="tc.calc_result.calc_date"/>:
            </td>
            <td class="col">
                <s:select id="date" name="date" list="allDates" required="true" />
            </td>
        </tr>
        <tr valign="middle">
            <td colspan="2">
                <input type="button" name="export" value="<s:text name="tc.calc_result.export" />" class="btn-exit" onclick="calcDateWindow();" />
            </td>
        </tr>
    </table>

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

        function calcDateWindow() {

            var win = new Window({
                className: "spread",
                title: "<s:text name="tc.export.window_title" />",
                url: "<s:url action="calcResultExport"><s:param name="modal" value="1" /></s:url>&date=" + $("date").value,
                width:350,
                height:250,
                resizable: false,
                minimizable: false,
                maximizable: false,
                destroyOnClose: true
            });

            win.showCenter(true, y, x);
        }

    </script>

</s:form>
