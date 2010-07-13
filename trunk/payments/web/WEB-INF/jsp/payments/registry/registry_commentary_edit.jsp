<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_bgiframe.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_ui_core.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_ui_datepicker.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<script type="text/javascript">
    FP.calendars("paymentDate", true);

    function doValidate() {
        if ($("#").value.length > 256) {
            alert("<s:property value="getText('payments.error.registry.commentary.commentary_too_long')" />");
            return false;
        }
    }
</script>

<s:form action="registryCommentaryEdit" method="POST">
    <s:hidden name="registry.id" />
    <table>
        <tr>
            <td>
                <s:text name="payments.registry.commentary.payment_date" /><br />
                <input id="paymentDate" name="paymentDate" value="<s:property value="paymentDate" />" readonly="readonly" />
            </td>
        </tr>
        <tr>
            <td>
                <s:text name="payments.registry.commentary.payment_number" /><br />
                <s:textfield maxLength="16" name="paymentNumber" />
            </td>
        </tr>
        <tr>
            <td>
                <s:text name="payments.registry.commentary" /><br />
                <s:textarea rows="10" cols="50" name="commentary" /><br/>
            </td>
        </tr>
        <tr>
            <td align="left">
                <s:submit name="submitted" cssClass="btn-exit" onclick="doValidate();" value="%{getText('common.save')}" />
            </td>
        </tr>
    </table>
</s:form>
