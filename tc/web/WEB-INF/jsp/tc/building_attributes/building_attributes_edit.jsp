
<%@ page import="org.flexpay.bti.persistence.BuildingAttributeTypeSimple" %>
<%@ page import="org.flexpay.bti.persistence.BuildingAttributeTypeEnum" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<%@include file="/WEB-INF/jsp/common/jquery_ui.jsp"%>

<script type="text/javascript">
    // folding functions
    function toggleElements(elements, showButton) {
        $.each(elements, function(i, value) {
            $(value).toggle();
        });
        $(showButton).val($(elements[0]).is(":visible") ? "<s:text name="common.hide" />" : "<s:text name="common.show" />");
    }

    // defining attribute groups
    var attributeGroups = new Array();
    <s:iterator value="%{getAttributeGroups()}" id="groupId">
        attributeGroups[<s:property value="#groupId"/>] = new Array(<s:iterator value="%{getGroupAttributeTypes(#groupId)}" id="typeId" status="status">"#attr_<s:property value="%{#typeId}"/>_id"<s:if test="!#status.last">, </s:if></s:iterator>);
    </s:iterator>

    function toggleAttributesGroup(groupId) {
        toggleElements(attributeGroups[groupId], '#toggle_attribute_group_' + groupId);
    }

    // attribute date picker configuration
    FP.calendars("#buildingAttributesEdit_attributeDate", "<s:url value="/resources/common/js/jquery/jquery-ui/images/calendar.gif" includeParams="none" />");
</script>

<s:actionerror />
<s:form action="buildingAttributesEdit">
    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <s:hidden name="building.id" value="%{building.id}"/>

        <%-- main address + alternatives --%>
        <s:iterator value="%{alternateAddresses}">
            <tr valign="middle" class="cols_1">
                <td class="col" colspan="2">
                    <s:property value="%{getAddress(id)}"/><s:if test="primaryStatus">(<s:text name="tc.edit_building_attributes.primary_status"/>)</s:if>
                </td>
            </tr>
        </s:iterator>
        <tr>
            <td class="th" colspan="2">
                <s:property value="%{getAddress(building.id)}"/><s:if test="%{building.primaryStatus}">(<s:text name="tc.edit_building_attributes.primary_status"/>)</s:if>
            </td>
        </tr>
        <tr>
            <td>
                <s:text name="tc.edit_building_attributes.date"/>
                <s:textfield name="attributeDate" value="%{attributeDate}" readonly="true"/>
                <s:submit name="dateSubmitted" value="%{getText('tc.show_attribute_values')}" cssClass="btn-exit"/>
            </td>
            <td style="text-align:right;">
                <s:submit name="submitted" value="%{getText('common.save')}" cssClass="btn-exit"/>
            </td>
        </tr>
        <tr>
            <td class="th_s" colspan="2"><s:text name="tc.building_attributes"/></td>
        </tr>

        <%-- attribute groups --%>
        <s:iterator value="%{getAttributeGroups()}" id="groupId" status="groupStatus">
            <tr>
                <td class="th" colspan="2" style="padding:0;">
                    <table style="width:100%;font-size:100%;font-weight: bold;">
                        <tr>
                            <td><s:property value="%{getGroupName(#groupId)}"/></td>
                            <td style="text-align:right;">
                                <input type="button" class="btn-exit" id="toggle_attribute_group_<s:property value="#groupId"/>" onclick="toggleAttributesGroup(<s:property value="#groupId"/>);" value="<s:if test="#groupStatus.first"><s:text name="common.hide"/></s:if><s:else><s:text name="common.show"/></s:else>"/>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>

            <s:iterator value="%{getGroupAttributeTypes(#groupId)}" id="typeId">
                <tr id="attr_<s:property value="%{#typeId}"/>_id" valign="middle" class="cols_1" <s:if test="!#groupStatus.first"> style="display:none;"</s:if>>
                    <td class="col" style="width:80%;"><s:property value="%{getAttributeTypeName(#typeId)}"/></td>
                    <td class="col" style="width:20%;">
                        <s:if test="%{isBuildingAttributeTypeSimple(#typeId)}">
                            <nobr>
                                <s:textfield name="attributeMap[%{#typeId}]" value="%{getAttributeValue(#typeId)}" cssStyle="width:140px;"/>
                                <s:if test="%{isTempAttribute(#typeId)}"><img src="<s:url value="/resources/common/img/i_clock.gif" includeParams="none" />" alt="<s:text name="tc.temp_attribute"/>" style="vertical-align: middle;"/></s:if>
                            </nobr>
                        </s:if>
                        <s:if test="%{isBuildingAttributeTypeEnum(#typeId)}">
                            <nobr>
                                <s:select name="attributeMap[%{#typeId}]" value="%{getAttributeValue(#typeId)}" list="%{getTypeValues(#typeId)}" listKey="order" listValue="value" emptyOption="true" cssStyle="width:140px;"/>
                                <s:if test="%{isTempAttribute(#typeId)}"><img src="<s:url value="/resources/common/img/i_clock.gif" includeParams="none" />" alt="<s:text name="tc.temp_attribute"/>" style="vertical-align:middle;"/></s:if>
                            </nobr>
                        </s:if>
                    </td>
                </tr>
            </s:iterator>
        </s:iterator>
    </table>
</s:form>
