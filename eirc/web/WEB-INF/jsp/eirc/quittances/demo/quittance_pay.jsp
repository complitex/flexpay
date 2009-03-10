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

        // special rules to be generated
        jQuery.validator.addMethod('payValue_0_is_not_too_big', function(value, element) {
            value = replaceCommaWithDot(value);
            return parseFloat(value) <= 0.29;
        }, '<s:text name="eirc.quittances.demo.quittance_pay.error.pay_value_too_big"/>');

        jQuery.validator.addMethod('payValue_1_is_not_too_big', function(value, element) {
            value = replaceCommaWithDot(value);
            return parseFloat(value) <= 1.42;
        }, '<s:text name="eirc.quittances.demo.quittance_pay.error.pay_value_too_big"/>');

        jQuery.validator.addMethod('payValue_2_is_not_too_big', function(value, element) {
            value = replaceCommaWithDot(value);
            return parseFloat(value) <= 59.02;
        }, '<s:text name="eirc.quittances.demo.quittance_pay.error.pay_value_too_big"/>');
    });

    jQuery(function() {
        var validator = jQuery('#demoQuittancePayForm').validate({
            rules: {
                'servicePayValue[0]' : {
                    'validPayValue': true,
                    'payValue_0_is_not_too_big': true // to be generated
                },
                'servicePayValue[1]' : {
                    'validPayValue' : true,
                    'payValue_1_is_not_too_big': true // to be generated
                },
                'servicePayValue[2]' : {
                    'validPayValue' : true,
                    'payValue_2_is_not_too_big': true // to be generated
                },
                'total_pay': 'required' // covers jquery validation 1.5.1 bug (it doesn't allow form fields without any rules)
            },
            messages: {
                'servicePayValue[0]' : {
                    'validPayValue': '<s:text name="eirc.quittances.demo.quittance_pay.error.invalid_pay_value"/>',
                    'payValue_0_is_not_too_big': '<s:text name="eirc.quittances.demo.quittance_pay.error.pay_value_too_big"/>'
                },
                'servicePayValue[1]' : {
                    'validPayValue' : '<s:text name="eirc.quittances.demo.quittance_pay.error.invalid_pay_value"/>',
                    'payValue_1_is_not_too_big': '<s:text name="eirc.quittances.demo.quittance_pay.error.pay_value_too_big"/>'
                },
                'servicePayValue[2]' : {
                    'validPayValue' : '<s:text name="eirc.quittances.demo.quittance_pay.error.invalid_pay_value"/>',
                    'payValue_2_is_not_too_big': '<s:text name="eirc.quittances.demo.quittance_pay.error.pay_value_too_big"/>'
                }
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
        <td><s:text name="eirc.quittances.demo.quittance_pay.data.quittance_number"/>:</td>
        <td><s:text name="eirc.quittances.demo.quittance_pay.data.quittance_number.value"/></td>
    </tr>
    <tr>
        <td><s:text name="eirc.quittances.demo.quittance_pay.data.fio"/>:</td>
        <td><s:text name="eirc.quittances.demo.quittance_pay.data.fio.value"/></td>
    </tr>
    <tr>
        <td><s:text name="eirc.quittances.demo.quittance_pay.data.address"/>:</td>
        <td><s:text name="eirc.quittances.demo.quittance_pay.data.address.value"/></td>
    </tr>
</table>

<br/>

<s:form id="demoQuittancePayForm" action="demoQuittancePay">

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr>
            <td class="th" nowrap="nowrap"><s:text name="eirc.quittances.demo.quittance_pay.service"/></td>
            <td class="th"><s:text name="eirc.quittances.demo.quittance_pay.service_supplier"/></td>
            <td class="th" style="width: 20%;"><s:text name="eirc.quittances.demo.quittance_pay.payable"/></td>
            <td class="th" style="width: 20%;"><s:text name="eirc.quittances.demo.quittance_pay.pay"/></td>
        </tr>

        <tr class="cols_1_error" style="display: none;"><td colspan="4"/></tr>
        <tr class="cols_1">
            <td class="col" nowrap="nowrap"><s:text name="eirc.quittances.demo.quittance_pay.data.service1.name"/></td>
            <td class="col"><s:text name="eirc.quittances.demo.quittance_pay.data.service1.supplier"/></td>
            <td class="col"><s:text name="eirc.quittances.demo.quittance_pay.data.service1.payable"/></td>
            <td class="col"><s:textfield name="servicePayValue[0]" value="%{getText('eirc.quittances.demo.quittance_pay.data.service1.payable')}" cssStyle="width: 100%;"/></td>
        </tr>

        <tr class="cols_1_error" style="display: none;"><td colspan="4"/></tr>
        <tr class="cols_1">
            <td class="col" nowrap="nowrap"><s:text name="eirc.quittances.demo.quittance_pay.data.service2.name"/></td>
            <td class="col"><s:text name="eirc.quittances.demo.quittance_pay.data.service2.supplier"/></td>
            <td class="col"><s:text name="eirc.quittances.demo.quittance_pay.data.service2.payable"/></td>
            <td class="col"><s:textfield name="servicePayValue[1]" value="%{getText('eirc.quittances.demo.quittance_pay.data.service2.payable')}" cssStyle="width: 100%;"/></td>
        </tr>

        <tr class="cols_1_error" style="display: none;"><td colspan="4"/></tr>
        <tr class="cols_1">
            <td class="col" nowrap="nowrap"><s:text name="eirc.quittances.demo.quittance_pay.data.service3.name"/></td>
            <td class="col"><s:text name="eirc.quittances.demo.quittance_pay.data.service3.supplier"/></td>
            <td class="col"><s:text name="eirc.quittances.demo.quittance_pay.data.service3.payable"/></td>
            <td class="col"><s:textfield name="servicePayValue[2]" value="%{getText('eirc.quittances.demo.quittance_pay.data.service3.payable')}" cssStyle="width: 100%;"/></td>
        </tr>

        <tr>
            <td colspan="2" style="text-align: right; font-weight: bold;"><s:text name="eirc.quittances.demo.quittance_pay.total_payable"/></td>
            <td style="font-weight: bold;"><s:text name="eirc.quittances.demo.quittance_pay.total_payable.value"/></td>
            <td><s:textfield name="total_pay" readonly="true" value="0,00"/></td>
        </tr>
        
        <tr>
            <td colspan="3"/>
            <td style="text-align: right;"><s:submit name="submitted" value="%{getText('eirc.quittances.demo.quittance_pay.pay')}" cssClass="btn-exit" cssStyle="width: 100%;"/></td>
        </tr>
    </table>

</s:form>