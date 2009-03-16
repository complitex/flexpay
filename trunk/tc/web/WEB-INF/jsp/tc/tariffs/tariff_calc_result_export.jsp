<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<s:actionerror />

<s:form id="form" action="calcResultExport">

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr valign="top" class="cols_1">
            <td class="col">
                <s:text name="tc.calc_result.calc_date"/>:
            </td>
            <td class="col">
                <s:select id="date" name="date" list="allDates" required="true" />
            </td>
        </tr>
        <tr valign="top" class="cols_1">
            <td class="col">
                <s:text name="tc.calc_result.ariff_action_begin_date" />
            </td>
            <td class="col">
                <s:textfield id="tariffBegin" name="tariffBegin" readonly="true" size="10" required="true" />
                <img id="calcDate" src="<c:url value="/resources/common/js/jscalendar/img.gif"/>"
                     style="cursor:pointer;border:1px solid red;"
                     alt="<s:text name="common.calendar"/>"
                     onmouseover="this.style.background='red';"
                     onmouseout="this.style.background='';" />
                <script type="text/javascript">
                    Calendar.setup({
                        inputField	 : "tariffBegin",
                        ifFormat	 : "%Y/%m/%d",
                        button		 : "calcDate",
                        align		 : "Tl"
                    });
                </script>
            </td>
        </tr>
        <tr valign="middle">
            <td colspan="2">
                <input type="hidden" name="submitted" value="true" />
                <input type="button" value="<s:text name="tc.calc_result.export" />" class="btn-exit" onclick="submitForm();" />
            </td>
        </tr>
    </table>

    <script type="text/javascript">

        function validateFrom() {
            var v = jQuery("#tariffBegin").val();
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

            jQuery("#form").submit();
        }

    </script>

</s:form>
