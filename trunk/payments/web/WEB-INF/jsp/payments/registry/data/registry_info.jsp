<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<tr class="cols_1">
		<td class="col">
            <strong><s:text name="eirc.registry.number" />:</strong> <s:property value="registry.registryNumber" />
        </td>
		<td class="col">
            <strong><s:text name="eirc.sender" />:</strong> <s:property value="getTranslationName(orgs.get(registry.senderCode).names)" />
        </td>
	</tr>
	<tr class="cols_1">
		<td class="col">
            <strong><s:text name="eirc.registry_type" />:</strong> <s:text name="%{registry.registryType.i18nName}" />
        </td>
		<td class="col">
            <strong><s:text name="eirc.recipient" />:</strong> <s:property value="getTranslationName(orgs.get(registry.recipientCode).names)" />
		</td>
	</tr>
	<tr class="cols_1">
		<td class="col">
            <strong><s:text name="common.from" />:</strong> <s:date name="registry.fromDate" format="yyyy/MM/dd" />
			<strong><s:text name="common.till" />:</strong> <s:date name="registry.tillDate" format="yyyy/MM/dd" />
        </td>
		<td class="col">
            <strong><s:text name="eirc.records_number" />:</strong> <s:property value="registry.recordsNumber" />
		</td>
	</tr>
	<tr class="cols_1">
		<td class="col">
            <strong><s:text name="eirc.registry.errors_number" />:</strong> <span id="errorsNumber"></span>
        </td>
		<td class="col">
            <strong><s:text name="eirc.status" />:</strong> <s:text name="%{registry.registryStatus.i18nName}" />
		</td>
	</tr>
</table>

<script type="text/javascript">

    var $erNum = $("#errorsNumber");

    $erNum.ready(function() {

        $erNum.html("<img src=\"<s:url value="/resources/common/img/indicator.gif" />\" />");

        $.getJSON("<s:url action="checkRegistryErrorsNumber" includeParams="none" />", {"registry.id": <s:property value="registry.id" />},
            function(data) {
                $erNum.text(data.errorsNumber);
                if (data.errorMessage != "") {
                    $("#messagesBlock").html(data.errorMessage);
                }
            })
    });


</script>