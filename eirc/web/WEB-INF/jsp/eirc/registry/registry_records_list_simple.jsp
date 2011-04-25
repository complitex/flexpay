<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<input type="hidden" name="index" value="<s:property value="index" />" />

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td colspan="6">
            <%@include file="/WEB-INF/jsp/eirc/filters/pager/pager_inner_ajax.jsp"%>
        </td>
    </tr>
    <tr>
        <td class="th">&nbsp;</td>
        <td class="th"><s:text name="payments.registry.record.service" /></td>
        <td class="th"><s:text name="payments.registry.record.address" /></td>
        <td class="th"><s:text name="payments.registry.record.fio" /></td>
        <td class="th"><s:text name="payments.date" /></td>
        <td class="th"><s:text name="payments.registry.record.containers" /></td>
    </tr>
    <s:iterator value="records" status="status">
        <tr valign="middle" class="cols_1">
            <td class="col" width="1%" align="right">
                <s:property value="#status.index + pager.thisPageFirstElementNumber + 1" />
            </td>
            <td class="col"><s:property value="serviceCode" /></td>
            <td class="col" nowrap>
                <s:if test="streetType != null && streetName != null && (buildingNum != null || buildingBulkNum != null) && apartmentNum != null">
                    <s:set name="addressVal" value="%{streetType + ' ' + streetName + ', ' + (buildingNum != null ? buildingNum : '') + (buildingBulkNum != null ? ' ' + buildingBulkNum : '') + ', ' + apartmentNum}" />
                    <s:property value="#addressVal" />
                </s:if>
            </td>
            <td class="col">
                <s:if test="firstName != null || middleName != null || lastName != null">
                    <s:set name="fioVal" value="%{(lastName != null ? lastName : '') + (firstName != null ? ' ' + firstName : '') + (middleName != null ? ' ' + middleName : '')}" />
                    <s:property value="#fioVal" />
                </s:if>
            <td class="col"><s:date name="operationDate" format="yyyy/MM/dd" /></td>
            <td class="col">
                <s:if test="containers.isEmpty()">
                    N/A
                </s:if>
                <s:else>
                    <s:set name="lastContainerIndex" value="%{containers.size() - 1}" />
                    <s:iterator value="containers" status="containerStatus" id="container">
                        <s:property value="#container.data" /><s:if test="#containerStatus.index < #lastContainerIndex">; </s:if>
                    </s:iterator>
                </s:else>
            </td>
        </tr>
    </s:iterator>
    <tr>
        <td colspan="6">
            <%@include file="/WEB-INF/jsp/eirc/filters/pager/pager_inner_ajax.jsp"%>
        </td>
    </tr>
</table>
