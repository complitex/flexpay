<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@include file="/WEB-INF/jsp/common/includes/jquery_ui.jsp"%>

<%@page import="org.flexpay.bti.persistence.building.BuildingAttributeTypeSimple" %>
<%@page import="org.flexpay.bti.persistence.building.BuildingAttributeTypeEnum" %>

<script type="text/javascript">

    function toggleGroup(id) {
        $("#toggle" + id).val($("#group" + id).toggle().is(":visible") ? "<s:text name="common.hide" />" : "<s:text name="common.show" />");
    }

    FP.calendars("buildingAttributesEdit_attributeDate", true);

</script>

<style type="text/css">

    .attrGroup {
        width:100%;
        font-size:100%;
        font-weight:bold;
    }
    
</style>

<s:actionerror />
<s:actionmessage />

<s:form action="buildingAttributesEdit">

    <s:hidden name="building.id" />

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <%-- main address + alternatives --%>
        <s:iterator value="alternateAddresses">
            <tr valign="middle" class="cols_1">
                <td class="col" colspan="2">
                    <s:property value="getAddress(id)" /><s:if test="primaryStatus"> (<s:text name="tc.edit_building_attributes.primary_status" />)</s:if>
                </td>
            </tr>
        </s:iterator>
        <tr>
            <td class="th" colspan="2">
                <s:property value="getAddress(building.id)" /><s:if test="%{building.primaryStatus}"> (<s:text name="tc.edit_building_attributes.primary_status" />)</s:if>
            </td>
        </tr>
        <tr>
            <td>
                <s:text name="tc.edit_building_attributes.date" />
                <s:textfield name="attributeDate" readonly="true" />
                <s:submit name="dateSubmitted" value="%{getText('tc.show_attribute_values')}" cssClass="btn-exit" />
            </td>
            <td style="text-align:right;">
                <s:submit name="submitted" value="%{getText('common.save')}" cssClass="btn-exit" />
            </td>
        </tr>
        <tr>
            <td class="th_s" colspan="2"><s:text name="tc.building_attributes" /></td>
        </tr>

        <s:iterator value="getAttributeGroups()" id="groupId" status="groupStatus">
            <s:set name="attrTypes" value="getGroupAttributeTypes(#groupId)" />

            <s:if test="!#attrTypes.isEmpty()">
                <tr>
                    <td class="th" colspan="2" style="padding:0;">
                        <table class="attrGroup">
                            <tr>
                                <td><s:property value="getGroupName(#groupId)" /></td>
                                <td style="text-align:right;">
                                    <input type="button" class="btn-exit" id="toggle<s:property value="#groupId" />"
                                           onclick="toggleGroup(<s:property value="#groupId" />);"
                                           value="<s:if test="#groupStatus.first"><s:text name="common.hide" /></s:if><s:else><s:text name="common.show" /></s:else>" />
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr id="group<s:property value="#groupId" />" class="cols_1" <s:if test="!#groupStatus.first"> style="display:none;"</s:if>>
                    <td colspan="2">
                        <table>
                            <s:iterator value="attrTypes" id="typeId">
                                <tr class="cols_1">
                                    <td class="col" style="width:80%;">
                                        <s:property value="getAttributeTypeName(#typeId)" />
                                    </td>
                                    <td class="col">
                                        <s:set name="attrValue" value="getAttributeValue(#typeId)" />
                                        <nobr>
                                            <s:if test="isBuildingAttributeTypeSimple(#typeId)">
                                                <s:textfield name="attributeMap[%{typeId}]" value="%{attrValue}" cssStyle="width:140px;" />
                                            </s:if><s:elseif test="isBuildingAttributeTypeEnum(#typeId)">
                                                <s:select name="attributeMap[%{typeId}]" value="%{attrValue}" list="getTypeValues(#typeId)" listKey="order" listValue="value" emptyOption="true" cssStyle="width:140px;" />
                                            </s:elseif>
                                            <s:if test="isTempAttribute(#typeId)"><img src="<s:url value="/resources/common/img/i_clock.gif" includeParams="none" />" alt="<s:text name="tc.temp_attribute" />" style="vertical-align:middle;" /></s:if>
                                        </nobr>
                                    </td>
                                </tr>
                            </s:iterator>
                        </table>
                    </td>
                </tr>
            </s:if>

        </s:iterator>
    </table>
</s:form>
