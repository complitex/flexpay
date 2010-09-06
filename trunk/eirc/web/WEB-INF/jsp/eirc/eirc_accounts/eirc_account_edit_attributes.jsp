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
                <s:iterator value="attributeTypes" id="type">
                    <td class="col" style="text-align:center;">
                        <s:set name="attribute" value="#formAttributesMap.get(#type.id)" />
                        <s:if test="#attribute != null && #attribute.isNotNew()">
                            <s:textfield name="attributes[%{#attribute.id}]" value="%{#attribute.value()}" /><br />
                            <input type="radio" name="<s:property value="'attr' + #type.id" />" value="<s:property value="#attribute.id" />" />
                        </s:if>
                    </td>
                </s:iterator>
            </tr>
        </s:iterator>
        <tr>
            <td colspan="15">
                <s:if test="!eircAccount.consumers.isEmpty()">
                    <s:submit cssClass="btn-exit" name="submitted" value="%{getText('common.save')}" />
                    <input type="button" class="btn-exit" value="<s:text name="eirc.eirc_account.set_values" />" onclick="setValues();" />
                </s:if>
                <input type="button" class="btn-exit" value="<s:text name="common.back" />" onclick="backf();" />
            </td>
        </tr>
    </table>

    <s:hidden name="output" value="%{output}" />
    <s:hidden name="apartmentFilter" value="%{apartmentFilter != null ? apartmentFilter : 0}" />
    <s:hidden name="buildingFilter" value="%{buildingFilter != null ? buildingFilter : 0}" />
    <s:hidden name="streetFilter" value="%{streetFilter != null ? streetFilter : 0}" />
    <s:hidden name="townFilter" value="%{townFilter != null ? townFilter : 0}" />
    <s:hidden name="regionFilter" value="%{regionFilter != null ? regionFilter : 0}" />
    <s:hidden name="countryFilter" value="%{countryFilter != null ? countryFilter : 0}" />
    <s:hidden name="eircAccountSorterByAccountNumber.active" value="%{eircAccountSorterByAccountNumber != null ? eircAccountSorterByAccountNumber.active : 0}" />
    <s:hidden name="eircAccountSorterByAccountNumber.order" value="%{eircAccountSorterByAccountNumber.order}" />
    <s:hidden name="eircAccountSorterByAddress.active" value="%{eircAccountSorterByAddress != null ? eircAccountSorterByAddress.active : 0}" />
    <s:hidden name="eircAccountSorterByAddress.order" value="%{eircAccountSorterByAddress.order}" />
    <s:hidden name="pager.pageNumber" value="%{pager.pageNumber}" />
    <s:hidden name="pager.pageSize" value="%{pager.pageSize}" />
    <s:hidden name="returnTo" value="%{returnTo}" />
    <s:hidden name="eircAccount.id" value="%{eircAccount.id}" />

</s:form>

    <script type="text/javascript">

        function setValues() {
            $.each([<s:iterator value="attributeTypes" status="i"><s:property value="id" /><s:if test="!#i.last">,</s:if></s:iterator>], function(i, typeId) {
                $("input[name=attr" + typeId + "]:radio:checked").each(function() {
                    var checkedValue = $("input[name=\"attributes[" + this.value + "]\"]").val();
                    $("input[name=attr" + typeId + "]:radio").each(function() {
                        $("input[name=\"attributes[" + this.value + "]\"]").val(checkedValue);
                    });
                });
            });
        }

        function backf() {

            FP.post("<s:if test="returnTo == 1"><s:url action="eircAccountsList" includeParams="none" /></s:if><s:else><s:url action="eircAccountView" includeParams="none" /></s:else>", {
                <s:if test="output != null">output: <s:property value="output" />,</s:if>
                <s:if test="apartmentFilter != null">apartmentFilter: <s:property value="apartmentFilter" />,</s:if>
                <s:if test="buildingFilter != null">buildingFilter: <s:property value="buildingFilter" />,</s:if>
                <s:if test="streetFilter != null">streetFilter: <s:property value="streetFilter" />,</s:if>
                <s:if test="townFilter != null">townFilter: <s:property value="townFilter" />,</s:if>
                <s:if test="regionFilter != null">regionFilter: <s:property value="regionFilter" />,</s:if>
                <s:if test="countryFilter != null">countryFilter: <s:property value="countryFilter" />,</s:if>
                "personSearchFilter.searchString": "<s:property value="personSearchFilter.searchString" />",
                "eircAccountSorterByAccountNumber.active": "<s:property value="eircAccountSorterByAccountNumber.active" />",
                "eircAccountSorterByAccountNumber.order": "<s:property value="eircAccountSorterByAccountNumber.order" />",
                "eircAccountSorterByAddress.active": "<s:property value="eircAccountSorterByAddress.active" />",
                "eircAccountSorterByAddress.order": "<s:property value="eircAccountSorterByAddress.order" />",
                "pager.pageNumber": "<s:property value="pager.pageNumber" />",
                "pager.pageSize": "<s:property value="pager.pageSize" />",
                "eircAccount.id": "<s:property value="eircAccount.id" />",
            });
        }

    </script>
