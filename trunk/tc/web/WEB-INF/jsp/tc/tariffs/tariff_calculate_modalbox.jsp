<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

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

    function validateFrom() {
        var v = $("inputCalcDate").value;
        if (v == null || v == "") {
            alert('<s:text name="tc.error.field_calc_date_required" />');
            return false;
        }
        return true;
    }

    function submitForm() {
        var isValid = validateFrom();
        if (!isValid) {
            return;
        }

        $("form").submit();
        parent.Windows.closeAll();
    }

</script>

<s:form id="form" action="tariffCalculate" target="_parent">

    <s:hidden name="id" />

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr valign="top" class="cols_1">
            <td class="col">
                <s:text name="tc.calc_result.calc_date" />
            </td>
            <td class="col">
                <s:textfield id="inputCalcDate" name="calcDate" readonly="true" size="10" required="true" />
                <img id="calcDate" src="<c:url value="/resources/common/js/jscalendar/img.gif"/>"
                     style="cursor:pointer;border:1px solid red;"
                     alt="<s:text name="common.calendar"/>"
                     onmouseover="this.style.background='red';"
                     onmouseout="this.style.background='';" />
                <script type="text/javascript">
                    Calendar.setup({
                        inputField	 : "inputCalcDate",
                        ifFormat	 : "%d.%m.%Y",
                        button		 : "calcDate",
                        align		 : "Tl",
                        position : [x, y]
                    });
                </script>
            </td>
        </tr>
        <tr valign="middle">
            <td colspan="2">
                <input type="hidden" name="submitted" value="true" />
                <input type="button" class="btn-exit" value="<s:text name="tc.calculate"/>" onclick="submitForm();" />
                <input type="button" class="btn-exit" value="<s:text name="common.cancel"/>" onclick="parent.Windows.closeAll();" />
            </td>
        </tr>
    </table>
</s:form>
