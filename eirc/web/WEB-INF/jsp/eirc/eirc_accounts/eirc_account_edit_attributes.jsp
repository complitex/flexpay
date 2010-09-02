<%@ taglib prefix="S" uri="/struts-tags" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<tr class="cols_1">
		<td class="col">
            <strong><s:text name="eirc.eirc_account" />:</strong> <s:property value="eircAccount.accountNumber" />
        </td>
		<td class="col">
            <strong><s:text name="eirc.eirc_account.person" />:</strong> <s:property value="eircAccount.person != null ? getFIO(eircAccount.person) : '(*) ' + eircAccount.consumerInfo.FIO" />
        </td>
	</tr>
	<tr class="cols_1">
		<td class="col">
            <strong><s:text name="eirc.eirc_account.address" />:</strong> <s:property value="getAddress(eircAccount.apartment)" />
        </td>
		<td class="col">
		</td>
	</tr>
</table>

<s:if test="!eircAccount.consumers.isEmpty()">

<s:form action="eircAccountEditConsumerAttributes" method="POST">

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr>
            <td class="th" width="1%">&nbsp;</td>
            <td class="th" nowrap>
                <s:text name="eirc.service" /><br />
                (<s:text name="eirc.eirc_account.consumer_fio" />)
            </td>
            <s:iterator value="attributeTypes">
                <td class="th">
                    <s:property value="getTranslationName(names)" /> <s:if test="measureUnit != null">(<s:property value="getTranslationName(measureUnit.unitNames)" />)</s:if>
                </td>
            </s:iterator>
        </tr>
        <s:iterator value="eircAccount.consumers" status="status">
            <tr valign="middle" class="cols_1">
                <td class="col_1s" align="right"><s:property value="#status.index + 1" /></td>
                <td class="col" nowrap>
                    <s:property value="getServiceDescription(service)" /><br />
                    (<s:property value="consumerInfo.lastName + ' ' + consumerInfo.firstName + ' ' + consumerInfo.middleName" />)
                </td>
                <s:set name="consumerId" value="id" />
                <s:set name="formAttributesMap" value="formAttributes.get(id)" />
                <s:iterator value="attributeTypes">
                    <td class="col">
                        <s:set name="attribute" value="#formAttributesMap.get(id)" />
                        <s:if test="#attribute != null && #attribute.isNotNew()">
                            <s:textfield name="attributes[%{#attribute.id}]" value="%{#attribute.value()}" />
                        </s:if>
                    </td>
                </s:iterator>
            </tr>
        </s:iterator>
        <tr>
            <td colspan="15">
                <input type="submit" name="submitted" class="btn-exit" value="<s:text name="common.save" />" />
            </td>
        </tr>
    </table>

    <s:hidden name="eircAccount.id" value="%{eircAccount.id}" />

<%--
    <script type="text/javascript">

        var formAttributes = {
            <s:set name="consumersCount" value="eircAccount.consumers.size()" />
            <s:iterator value="eircAccount.consumers" status="consumersStatus">
                "<s:property value="id" />": [
                    <s:set name="formAttributesMap" value="formAttributes.get(id + '')" />
                    <s:set name="i" value="0" />
                    <s:iterator value="attributeTypes">
                        <s:set name="attribute" value="#formAttributesMap.get(id + '')" />
                        <s:if test="#attribute != null">
                            "<s:property value="#attribute.id" />"
                        </s:if><s:else>
                            "-1"
                        </s:else>
                        <s:if test="#i + 1 < @org.flexpay.payments.actions.outerrequest.request.response.data.ConsumerAttributes@EIRC_ATTRIBUTES.size()">,</s:if>
                        <s:set name="i" value="#i + 1" />
                    </s:iterator>
                ]<s:if test="#consumersStatus.index + 1 < #consumersCount">,</s:if>
            </s:iterator>
        };

        function submitForm() {

            var params = {
                "eircAccount.id":<s:property value="eircAccount.id" />,
                submitted:true
            };

            $.log(formAttributes);

            for (var consumerId in formAttributes) {
                for (var i in formAttributes[consumerId]) {
                    var attributeId = formAttributes[consumerId][i];
                    if (attributeId == "-1") {
                        continue;
                    }
                    params["attributes['" + attributeId + "']"] = $("input[name=\"attributes['" + attributeId + "']\"]").val();
                }
            }

            $.log(params);

            $.post("<s:url action="eircAccountEditConsumerAttributes" includeParams="none" />", params,
                    function(data, status) {
//                        window.location.href = "<s:url action="eircAccountView" includeParams="none"><s:param name="eircAccount.id" value="%{eircAccount.id}" /></s:url>";
                    });

        }

    </script>
--%>

</s:form>

</s:if>
