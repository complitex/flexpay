<%@ page import="org.flexpay.bti.persistence.BuildingAttributeTypeSimple" %>
<%@ page import="org.flexpay.bti.persistence.BuildingAttributeTypeEnum" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table cellpadding="3" cellspacing="1" border="0" width="100%">

    <s:form action="buildingAttributesEdit">

        <s:hidden name="building.id" value="%{building.id}"/>

        <%-- main address + alternatives --%>
        <s:iterator value="%{alternateBuildingsList}">
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
                <input type="text" name="attributeDate" id="attribute.date"
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
            </td>
        </tr>

        <%-- attribute groups (+misc) --%>
        <tr>
            <td class="th" colspan="2">
                Group 1 Savsem adin
            </td>
        </tr>

        <s:iterator value="attributeMap">
            <tr valign="middle" class="cols_1">

                <td class="col"><s:property value="%{getAttributeTypeName(key)}"/></td>

                <td class="col">

                    <s:if test="%{isBuildingAttributeTypeSimple(key)}">
                        <s:textfield name="attributeMap[%{key}]" value="%{value}"/>
                    </s:if>

                    <s:if test="%{isBuildingAttributeTypeEnum(key)}">
                        <s:select name="attributeMap[%{key}]"
                                  value="%{value}"
                                  list="%{key.values}"
                                  listKey="order"
                                  listValue="value"/>
                    </s:if>

                </td>

            </tr>
        </s:iterator>

        <%-- submit button --%>
        <tr>
            <td colspan="2">
                <s:submit name="submitted" value="%{getText('common.save')}" cssClass="btn-exit"/>
            </td>
        </tr>

    </s:form>

</table>