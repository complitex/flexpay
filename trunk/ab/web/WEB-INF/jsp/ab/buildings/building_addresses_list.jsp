<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<div id="response">
    <%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>
</div>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th" width="1%">
            <input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');" />
        </td>
        <td class="th" width="58%"><s:text name="ab.building.addresses" /></td>
        <td class="th" width="40%">&nbsp;</td>
    </tr>
    <s:iterator value="building.buildingses">
        <tr valign="middle" class="cols_1">
            <td class="col_1s" align="right">
                <s:property value="#status.index + 1" />
            </td>
            <td class="col">
                <s:if test="!primaryStatus">
                    <input type="checkbox" value="<s:property value="id" />" name="objectIds" />
                </s:if>
            </td>
            <td class="col">
                <s:property value="getAddress(id)" />
                <s:if test="primaryStatus">(<s:text name="ab.building_address.primary_status" />)</s:if>
            </td>
            <td class="col">
                <a href="<s:url action="buildingAddressEdit"><s:param name="building.id" value="building.id" /><s:param name="address.id" value="id" /></s:url>">
                    <s:text name="common.edit" />
                </a>
                <s:if test="!primaryStatus">
                    &nbsp;
                    <a href="#" onclick="setPrimaryStatus(<s:property value="id" />);">
                       <s:text name="ab.building.set_primary_status" />
                    </a>
                </s:if>
            </td>
        </tr>
    </s:iterator>

</table>
