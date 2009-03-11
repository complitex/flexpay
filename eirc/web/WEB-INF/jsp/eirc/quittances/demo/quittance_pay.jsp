<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<script type="text/javascript" src="<c:url value="/resources/common/js/jquery/jquery-1.3.2.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/common/js/jquery/validate/jquery.validate.js"/>"></script>
<script type="text/javascript">

    function replaceCommaWithDot(value) {
        return value.replace(",", ".");
    }

    // summ update
    function updateTotalPay() {
        var total = 0.00;
        var elements = jQuery('input[id^="demoQuittancePayForm_servicePayValue_"]');

        for (var i = 0; i < elements.length; i++) {
            var value = jQuery(elements[i]).val();

            if (isValidPayValue(value)) {
                value = replaceCommaWithDot(value);
                total += parseFloat(value);
            } else {
                jQuery('#demoQuittancePayForm_total_pay').val('<s:text name="eirc.quittances.demo.quittance_pay.error.unaccessible"/>');
            }
        }

        var result = total.toFixed(2) + '';
        jQuery('#demoQuittancePayForm_total_pay').val(result.replace('.', ','));
    }

    // validation
    function isValidPayValue(value) {
        var pattern = /^(\d)+([\.,]?\d{0,2})$/;
        return value.match(pattern);
    }

    jQuery(function() {
        updateTotalPay();
    });

    jQuery(function() {
        jQuery.validator.addMethod("validPayValue", function(value, element) {
            value = replaceCommaWithDot(value);
            return isValidPayValue(value);
        }, '<s:text name="eirc.quittances.demo.quittance_pay.error.invalid_pay_value"/>');


        <s:iterator value="%{quittance.quittanceDetails}" id="qd">
        jQuery.validator.addMethod('payValue_<s:property value="%{#qd.id}"/>_is_not_too_big', function(value, element) {
            value = replaceCommaWithDot(value);
            return parseFloat(value) <= <s:property value="%{getPayable(#qd)}"/>;
        }, '<s:text name="eirc.quittances.demo.quittance_pay.error.pay_value_too_big"/>');
        </s:iterator>
    });

    jQuery(function() {
        var validator = jQuery('#demoQuittancePayForm').validate({
            rules: {
                <s:iterator value="%{quittance.quittanceDetails}" id="qd">
                'servicePayValue[<s:property value="%{#qd.id}"/>]' : {
                    'validPayValue': true,
                    'payValue_<s:property value="%{#qd.id}"/>_is_not_too_big': true
                },
                </s:iterator>                
                'total_pay': 'required' // covers jquery validation 1.5.1 bug (it doesn't allow form fields without any rules)
            },
            messages: {
                <s:iterator value="%{quittance.quittanceDetails}" id="qd" status="status">
                'servicePayValue[<s:property value="%{#qd.id}"/>]' : {
                    'validPayValue': '<s:text name="eirc.quittances.demo.quittance_pay.error.invalid_pay_value"/>',
                    'payValue_<s:property value="%{#qd.id}"/>_is_not_too_big': '<s:text name="eirc.quittances.demo.quittance_pay.error.pay_value_too_big"/>'
                }<s:if test="!#status.last">, </s:if>
                </s:iterator>
            },
            errorClass: 'cols_1_error',
            errorElement: 'span',
            success: function(label) {
                label.parent('td').parent('tr.cols_1_error').css('display', 'none');
                label.remove();

                if (validator.numberOfInvalids() == 0) {                    
                    updateTotalPay();
                }
            },
            showErrors: function(errorMap, errorList) {
                if (validator.numberOfInvalids() > 0) {                                        
                    jQuery('#demoQuittancePayForm_total_pay').val('<s:text name="eirc.quittances.demo.quittance_pay.error.unaccessible"/>');
                }

                this.defaultShowErrors();
            },
            errorPlacement: function(error, element) {
                var row = element.parent('td').parent('tr').prev('tr');
                var cell = row.children()[0];

                error.appendTo(cell);
                row.css('display', 'table-row');                
            },
            invalidHandler: function(form, validator) {
                alert('<s:text name="eirc.quittances.demo.quittance_pay.error.invalid_submit"/>');
            }
        });
    });
</script>

<s:actionerror/>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td><s:text name="eirc.quittances.demo.quittance_pay.quittance_number"/>:</td>
        <td><s:property value="%{quittanceNumber}"/></td>
    </tr>
    <tr>
        <td><s:text name="eirc.quittances.demo.quittance_pay.fio"/>:</td>
        <td><s:property value="%{getFIO()}"/></td>
    </tr>
    <tr>
        <td><s:text name="eirc.quittances.demo.quittance_pay.address"/>:</td>
        <td><s:property value="%{getAddress()}"/></td>
    </tr>
</table>

<br/>

<s:form id="demoQuittancePayForm" action="demoQuittancePay">

    <s:hidden name="quittanceNumber" value="%{quittanceNumber}"/>

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr>
            <td class="th" nowrap="nowrap"><s:text name="eirc.quittances.demo.quittance_pay.service"/></td>
            <td class="th"><s:text name="eirc.quittances.demo.quittance_pay.service_supplier"/></td>
            <td class="th" style="width: 20%;"><s:text name="eirc.quittances.demo.quittance_pay.payable"/></td>
            <td class="th" style="width: 20%;"><s:text name="eirc.quittances.demo.quittance_pay.pay"/></td>
        </tr>

        <s:iterator value="%{quittance.quittanceDetails}" id="qd">
            <tr class="cols_1_error" style="display: none;">
                <td colspan="4"/>
            </tr>
            <tr class="cols_1">
                <td class="col" nowrap="nowrap"><s:property value="%{getServiceName(#qd)}"/></td>
                <td class="col"><s:property value="%{getServiceProviderName(#qd)}"/></td>
                <td class="col"><s:property value="%{getPayable(#qd)}"/></td>
                <td class="col"><s:textfield name="servicePayValue[%{#qd.id}]" value="%{getPayable(#qd)}" cssStyle="width: 100%;"/></td>
            </tr>
        </s:iterator>

        <tr>
            <td colspan="2" style="text-align: right; font-weight: bold;"><s:text name="eirc.quittances.demo.quittance_pay.total_payable"/></td>
            <td style="font-weight: bold;"><s:property value="%{getTotalPayable()}"/></td>
            <td><s:textfield name="total_pay" readonly="true"/></td>
        </tr>

        <tr>
            <td colspan="3"/>
            <td style="text-align: right;"><input type="submit" name="submitted" value="<s:text name="eirc.quittances.demo.quittance_pay.pay"/>" class="btn-exit" style="width: 100%;"/></td>
        </tr>

    </table>

</s:form>