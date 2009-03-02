<%@ page import="org.flexpay.bti.persistence.BuildingAttributeTypeSimple" %>
<%@ page import="org.flexpay.bti.persistence.BuildingAttributeTypeEnum" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<link rel="stylesheet" type="text/css" href="<c:url value="/resources/common/js/jquery/jquery-ui/theme/ui.all.css"/>"/>

<script type="text/javascript" src="<c:url value="/resources/common/js/jquery/jquery-1.3.2.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/common/js/jquery/jquery-ui/jquery-ui-personalized-1.6rc6.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/common/js/jquery/jquery-ui/i18n/ui.datepicker-ru.js"/>"></script>

<script type="text/javascript">
    // folding functions
    function toggleElements(elements, showButton) {
        jQuery.each(elements, function(i, value) {
            jQuery(value).toggle();
        });

        if (jQuery(elements[0]).is(':visible')) {
            jQuery(showButton).val('<s:text name="common.hide"/>');
        } else {
            jQuery(showButton).val('<s:text name="common.show"/>');
        }
    }

    // defining attribute groups
    var attributeGroups = new Array();
    <s:iterator value="attributeGroups" id="groupId">
    attributeGroups[<s:property value="#groupId"/>] = new Array(<s:iterator value="%{getGroupAttributes(#groupId)}" status="status">"#attr_<s:property value="%{key}"/>_id"<s:if test="!#status.last">, </s:if></s:iterator>);
    </s:iterator>

    function toggleAttributesGroup(groupId) {
        toggleElements(attributeGroups[groupId], '#toggle_attribute_group_' + groupId);
    }

    // attribute date picker configuration
    jQuery(function() {
        jQuery('#buildingAttributesEdit_attributeDate').datepicker({
            dateFormat: 'yy/mm/dd'
        });
    });   
</script>

<s:actionerror />
<s:form action="buildingAttributesEdit">
    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <s:hidden name="building.id" value="%{building.id}"/>

        <%-- main address + alternatives --%>
        <s:iterator value="%{alternateAddresses}">
            <tr valign="middle" class="cols_1"><td class="col" colspan="2">
                    <s:property value="%{getAddress(id)}"/><s:if test="primaryStatus">(<s:text name="tc.edit_building_attributes.primary_status"/>)</s:if>
            </td></tr>
        </s:iterator>

        <tr><td class="th" colspan="2">
                <s:property value="%{getAddress(building.id)}"/><s:if test="%{building.primaryStatus}">(<s:text name="tc.edit_building_attributes.primary_status"/>)</s:if>
        </td></tr>

        <tr>
            <td>
                <s:text name="tc.edit_building_attributes.date"/>
                <s:textfield name="attributeDate" value="%{attributeDate}" readonly="true"/>
                <s:submit name="dateSubmitted" value="%{getText('tc.show_attribute_values')}" cssClass="btn-exit"/>
            </td>
            <td style="text-align:right;"><s:submit name="submitted" value="%{getText('common.save')}" cssClass="btn-exit"/></td>
        </tr>

        <tr><td colspan="2" class="cols_1"/></tr>

        <tr><td class="th_s" colspan="2"><s:text name="tc.building_attributes"/></td></tr>

        <tr><td colspan="2" class="cols_1"/></tr>

        <%-- attribute groups --%>
        <s:iterator value="attributeGroups" id="groupId" status="groupStatus">
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

            <s:iterator value="%{getGroupAttributes(#groupId)}">
                <tr id="attr_<s:property value="%{key}"/>_id" valign="middle" class="cols_1" <s:if test="!#groupStatus.first"> style="display:none;"</s:if>>
                    <td class="col" style="width:80%;"><s:property value="%{getAttributeTypeName(key)}"/></td>
                    <td class="col" style="width:20%;">
                        <s:if test="%{isBuildingAttributeTypeSimple(key)}">
                            <nobr>
                                <s:textfield name="attributeMap[%{key}]" value="%{value}" cssStyle="width:140px;"/>
                                <s:if test="%{isTempAttribute(key)}"><img src="<s:url value="/resources/common/img/i_clock.gif"/>" alt="<s:text name="tc.temp_attribute"/>" style="vertical-align: middle;"/></s:if>
                            </nobr>
                        </s:if>

                        <s:if test="%{isBuildingAttributeTypeEnum(key)}">
                            <nobr>
                                <s:select name="attributeMap[%{key}]" value="%{value}" list="%{getTypeValues(key)}" listKey="order" listValue="value" emptyOption="true" cssStyle="width:140px;"/>
                                <s:if test="%{isTempAttribute(key)}"><img src="<s:url value="/resources/common/img/i_clock.gif"/>" alt="<s:text name="tc.temp_attribute"/>" style="vertical-align:middle;"/></s:if>
                            </nobr>
                        </s:if>
                    </td>
                </tr>
            </s:iterator>
        </s:iterator>
    </table>
</s:form>