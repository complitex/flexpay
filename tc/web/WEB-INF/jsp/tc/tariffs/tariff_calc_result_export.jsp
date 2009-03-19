
<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<%@include file="/WEB-INF/jsp/common/jquery_ui.jsp"%>

<script type="text/javascript">

    function validateFrom() {
        var v = $("#tariffBegin").val();
        if (v == null || v == "") {
            alert('<s:text name="tc.error.field_calc_date_required" />');
            return false;
        }
        return true;
    }

    function submitForm() {
        if (!validateFrom()) {
            return;
        }
        $("#form").submit();
    }

    FP.calendars("#tariffBegin", "<s:url value="/resources/common/js/jquery/jquery-ui/images/calendar.gif" includeParams="none" />");

</script>

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
            </td>
        </tr>
        <tr valign="middle">
            <td colspan="2">
                <input type="hidden" name="submitted" value="true" />
                <input type="button" value="<s:text name="tc.calc_result.export" />" class="btn-exit" onclick="submitForm();" />
            </td>
        </tr>
    </table>

</s:form>
