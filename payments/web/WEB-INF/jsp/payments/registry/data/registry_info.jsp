<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<tr class="cols_1">
		<td class="col">
            <strong><s:text name="payments.registry.number" />:</strong> <s:property value="registry.id" />
        </td>
		<td class="col">
            <strong><s:text name="orgs.sender" />:</strong> <s:property value="getTranslationName(orgs.get(registry.senderCode).names)" />
        </td>
	</tr>
	<tr class="cols_1">
		<td class="col">
            <strong><s:text name="payments.registry_type" />:</strong> <s:text name="%{registry.registryType.i18nName}" />
        </td>
		<td class="col">
            <strong><s:text name="orgs.recipient" />:</strong> <s:property value="getTranslationName(orgs.get(registry.recipientCode).names)" />
		</td>
	</tr>
	<tr class="cols_1">
		<td class="col">
            <strong><s:text name="common.from" />:</strong> <s:date name="registry.fromDate" format="yyyy/MM/dd" />
			<strong><s:text name="common.till" />:</strong> <s:date name="registry.tillDate" format="yyyy/MM/dd" />
        </td>
		<td class="col">
            <strong><s:text name="payments.records_number" />:</strong> <s:property value="registry.recordsNumber" />
		</td>
	</tr>
	<tr class="cols_1">
		<td class="col">
            <strong><s:text name="payments.registry.total_errors_number" />:</strong> <span id="errorsNumber"></span>
        </td>
		<td class="col">
            <strong><s:text name="payments.status" />:</strong> <s:text name="%{registry.registryStatus.i18nName}" />
		</td>
	</tr>
    <s:if test="registry.registryType.code == @org.flexpay.common.persistence.registry.RegistryType@TYPE_CASH_PAYMENTS">
        <tr>
            <td colspan="2" height="3" bgcolor="#4a4f4f"></td>
        </tr>
        <s:if test="commentary != null && commentary != ''">
            <tr class="cols_1">
                <td class="col">
                    <strong><s:text name="payments.registry.commentary.payment_date" />:</strong> <s:property value="paymentDate" />
                </td>
                <td class="col">
                    <strong><s:text name="payments.registry.commentary.payment_number" />:</strong> <s:property value="paymentNumber" />
                </td>
            </tr>
            <tr class="cols_1">
                <td class="col" colspan="2">
                    <strong><s:text name="payments.registry.commentary" />:</strong> <s:property value="commentary" />
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <input type="button" class="btn-exit"
                           onclick="window.location='<s:url action="registryCommentaryEdit"><s:param name="registry.id" value="registry.id" /></s:url>';"
                           value="<s:text name="payments.registry.commentary.edit" />" />
                </td>
            </tr>
        </s:if><s:else>
            <tr>
                <td colspan="2">
                    <input type="button" class="btn-exit"
                           onclick="window.location='<s:url action="registryCommentaryEdit"><s:param name="registry.id" value="registry.id" /></s:url>';"
                           value="<s:text name="payments.registry.commentary.add" />" />
                </td>
            </tr>
        </s:else>
    </s:if>
</table>

<script type="text/javascript">

    var $erNum = $("#errorsNumber");

    $erNum.ready(function() {
        updateErrorsNumber();
    });

    function updateErrorsNumber() {
        $erNum.html("<img src=\"<s:url value="/resources/common/img/indicator2.gif" />\" />");

        $.getJSON("<s:url action="checkRegistryErrorsNumber" />", {"registry.id": <s:property value="registry.id" />},
            function(data) {
                $("#errorsNumber").text(data.errorsNumber);
                if (data.errorMessage != "") {
                    $("#messagesBlock").html(data.errorMessage);
                }
            });

    }


</script>
