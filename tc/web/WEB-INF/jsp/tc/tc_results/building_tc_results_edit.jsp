<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_validation.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<script type="text/javascript">
    // validation
    function isValidTcResultValue(value) {
        var pattern = /^(-)?(\d)+([\.,]?\d{0,4})$/;
        return value.match(pattern);
    }

    $(function() {
        $.validator.addMethod("tcResultValue", function(value, element) {
            return isValidTcResultValue(value);
        }, '<s:text name="tc.error.bad.tc.result.value"/>');
    });

    // validator configuration
    $(function() {
        var validator = $('#tcResultsEdit').validate({
            rules: {
                <s:iterator value="%{tariffMap}" status="status">"tariffMap[<s:property value="%{key}" />]": "tcResultValue"<s:if test="!#status.last">, </s:if></s:iterator>
            },
            messages: {},
            errorClass: "cols_1_error",
            success: function(label) {
                if (validator.numberOfInvalids() == 0) {
                    $("#tcResultsEdit_errors").hide();
                }
            },
            showErrors: function(errorMap, errorList) {
                $("#tcResultsEdit_errors").html('<s:text name="tc.error.tc.results.form.contains.errors" />').addClass("cols_1_error").show();
                this.defaultShowErrors();
            },
            errorPlacement: function(error, element) {
            },
            invalidHandler: function(form, validator) {
                alert('<s:text name="tc.error.invalid.tc.result.submit" />');
            }
        });
    });
</script>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td class="th_s" colspan="2">
            <s:property value="%{getAddress(buildingId)}"/><s:if test="%{hasPrimaryStatus(buildingId)}"> (<s:text name="tc.building_tc_results_edit.primary_status"/>)</s:if>
        </td>
    </tr>
    <s:iterator value="%{alternateAddresses}">
        <tr valign="middle" class="cols_1">
            <td class="col" colspan="2">
                <s:property value="%{getAddress(id)}"/><s:if test="primaryStatus">(<s:text name="tc.building_tc_results_edit.primary_status"/>)</s:if>
            </td>
        </tr>
    </s:iterator>
</table>

<s:form id="tcResultsEdit" action="buildingTCResultsEdit">
    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr>
            <td colspan="2" class="th" style="padding:0;">
                <table style="width:100%;font-size:100%;font-weight:bold;">
                    <tr>
                        <td><s:text name="tc.tariffs_calculated_on"><s:param value="%{calculationDate}"/></s:text></td>
                        <td style="text-align:right;"><s:submit name="submitted" value="%{getText('common.save')}" cssClass="btn-exit"/></td>
                    </tr>
                </table>
            </td>
        </tr>

        <tr>
            <td id="tcResultsEdit_errors" colspan="2" style="display:none;"></td>
        </tr>

        <s:hidden name="buildingId" value="%{buildingId}"/>
        <s:hidden name="calculationDate" value="%{calculationDate}"/>

        <s:iterator value="%{tariffMap}">
            <tr <s:if test="%{value < 0}"> class="cols_1_highlighted" </s:if><s:else> class="cols_1"</s:else>>
                <td class="col" style="width:80%;"><s:property value="%{getTariffTranslation(key)}"/></td>
                <td class="col" style="width:20%;"><s:textfield name="tariffMap[%{key}]" value="%{value}"/></td>
            </tr>
        </s:iterator>

        <tr class="cols_1">
            <td class="col" style="width:80%;font-weight: bold;"><s:text name="tc.total_tariff"/></td>
            <td class="col" style="width:20%;font-weight: bold;"><s:property value="%{getTotalTariff()}"/></td>
        </tr>
    </table>
</s:form>
