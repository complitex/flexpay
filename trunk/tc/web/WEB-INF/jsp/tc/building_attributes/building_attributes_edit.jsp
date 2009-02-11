<%@ page import="org.flexpay.bti.persistence.BuildingAttributeTypeSimple" %>
<%@ page import="org.flexpay.bti.persistence.BuildingAttributeTypeEnum" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table cellpadding="3" cellspacing="1" border="0" width="100%">

    <s:form action="buildingAttributesEdit">

        <s:hidden name="building.id" value="%{building.id}"/>

        <%-- main address + alternatives --%>
        <s:iterator value="%{alternateAddresses}">
            <tr valign="middle" class="cols_1">
                <td class="col" colspan="2">
                    <s:property value="%{getAddress(id)}"/>
                    <s:if test="primaryStatus">(<s:text name="tc.edit_building_attributes.primary_status"/>)</s:if>
                </td>
            </tr>
        </s:iterator>

        <tr>
            <td class="th" colspan="2">
                <s:property value="%{getAddress(building.id)}"/>
                <s:if test="%{building.primaryStatus}">(<s:text
                        name="tc.edit_building_attributes.primary_status"/>)</s:if>
            </td>
        </tr>

        <%-- calendar --%>
        <tr>
            <td>
                <s:text name="tc.edit_building_attributes.date"/>
                <input type="text" name="attributeDate" id="attribute.date" readonly="true"
                       value="<s:property value="%{attributeDate}"/>"/>
                <img src="<c:url value="/resources/common/js/jscalendar/img.gif"/>" alt=""
                     id="trigger_attribute.date"
                     style="cursor: pointer; border: 1px solid red;"
                     title="<s:text name="common.calendar"/>"
                     onmouseover="this.style.background='red';"
                     onmouseout="this.style.background='';"/>
                <script type="text/javascript">
                    Calendar.setup({
                        inputField     : "attribute.date",
                        ifFormat     : "%Y/%m/%d",
                        button         : "trigger_attribute.date",
                        align         : "Tl",
                        singleClick  : true
                    });
                </script>
                <s:submit name="dateSubmitted" value="%{getText('tc.show_attribute_values')}" cssClass="btn-exit"/>
            </td>
        </tr>

        <%-- attribute groups (+misc) --%>


        <s:iterator value="attributeGroups" id="groupId">

            <tr>
                <td class="th" colspan="2">
                    <s:property value="%{getGroupName(#groupId)}"/>
                </td>
            </tr>

            <s:iterator value="%{getGroupAttributes(#groupId)}">
                <tr valign="middle" class="cols_1">

                    <td class="col"><s:property value="%{getAttributeTypeName(key)}"/></td>

                    <td class="col">

                        <s:if test="%{isBuildingAttributeTypeSimple(key)}">
                            <nobr>
                                <s:textfield name="attributeMap[%{key}]" value="%{value}"/>

                                <s:if test="%{isTempAttribute(key)}">

                                    <img src="<s:url value="/resources/common/img/i_clock.gif"/>"
                                         alt="<s:text name="tc.temp_attribute"/>"
                                         style="vertical-align: middle;"/>
                                </s:if>
                            </nobr>
                        </s:if>

                        <s:if test="%{isBuildingAttributeTypeEnum(key)}">
                            <nobr>
                                <s:select name="attributeMap[%{key}]"
                                          value="%{value}"
                                          list="%{getTypeValues(key)}"
                                          listKey="order"
                                          listValue="value"
                                          emptyOption="true"/>

                                <s:if test="%{isTempAttribute(key)}">
                                    <img src="<s:url value="/resources/common/img/i_clock.gif"/>"
                                         alt="<s:text name="tc.temp_attribute"/>"
                                         style="vertical-align: middle;"/>
                                </s:if>
                            </nobr>
                        </s:if>

                    </td>

                </tr>
            </s:iterator>
        </s:iterator>

        <%-- submit button --%>
        <tr>
            <td colspan="2">
                <s:submit name="submitted" value="%{getText('common.save')}" cssClass="btn-exit"/>
            </td>
        </tr>

    </s:form>

</table>