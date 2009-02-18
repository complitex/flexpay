<%@ page import="org.flexpay.bti.persistence.BuildingAttributeTypeSimple" %>
<%@ page import="org.flexpay.bti.persistence.BuildingAttributeTypeEnum" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<script type="text/javascript" src="<s:url value="/resources/common/js/prototype.js"/>"></script>
<script type="text/javascript"
        src="<s:url value="/resources/common/js/windows_js_1.3/javascripts/window.js"/>"></script>

<script type="text/javascript">

    var attributeGroups = new Array();

    <s:iterator value="attributeGroups" id="groupId">
    attributeGroups[<s:property value="#groupId"/>] = new Array(
            <s:iterator value="%{getGroupAttributes(#groupId)}" status="status">
            "attr_<s:property value="%{key}"/>_id"
            <s:if test="!#status.last">, </s:if>
            </s:iterator>
            );
    </s:iterator>

    function hideAttributesGroup(groupId) {
        for (var i = 0; i < attributeGroups[groupId].length; i++) {
            $(attributeGroups[groupId][i]).hide();
        }
        $('show_group_' + groupId).show();
        $('hide_group_' + groupId).hide();
    }

    function showAttributesGroup(groupId) {
        for (var i = 0; i < attributeGroups[groupId].length; i++) {
            $(attributeGroups[groupId][i]).show();
        }
        $('show_group_' + groupId).hide();
        $('hide_group_' + groupId).show();
    }

    var x = 0;
    var y = 0;

    function getXY(e) {
        var left = document.documentElement.scrollLeft;
        var top = document.documentElement.scrollTop;
        if (Prototype.Browser.IE) {
            x = event.clientX + left;
            y = event.clientY + top;
        } else {
            x = e.clientX + left;
            y = e.clientY + top;
        }
    }

    document.onmousemove = getXY;

    function uploadSubmit(calcDate) {
        showBeginDateWindow(calcDate);
        return false;
    }

    function showBeginDateWindow(calcDate) {
        var win = new Window({
            className: "spread",
            title: "<s:text name="tc.upload_tc_results" />",
            url: "<s:url action="uploadTCResults" includeParams="none"><s:param name="buildingId" value="%{building.id}"/></s:url>&calculationDate=" + calcDate,
            width: 300,
            height: 200,
            resizable: false,
            minimizable: false,
            maximizable: false,
            destroyOnClose: true
        });

        win.showCenter(true, y, x);
    }

    function saveSubmit(calcDate) {
        var elements = $$('input[id$="'+ calcDate + '"]');

        for (var i = 0; i < elements.length; i++) {
            var parts = elements[i].id.split("_");
            var tariffId = parts[1];

            $('tcResultsEdit_tariffMap_' + tariffId + '_').value = elements[i].value;
        }

        $('tcResultsEdit_calculationDate').value = calcDate;
        $('tcResultsEdit').submit();
    }

</script>

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
            <td style="text-align: right;">
                <s:submit name="submitted" value="%{getText('common.save')}" cssClass="btn-exit"/>
            </td>
        </tr>

        <tr>
            <td colspan="2" class="cols_1"/>
        </tr>
        <tr>
            <td class="th_s" colspan="2">
                <s:text name="tc.building_attributes"/>
            </td>
        </tr>
        <tr>
            <td colspan="2" class="cols_1"/>
        </tr>

        <%-- attribute groups --%>
        <s:iterator value="attributeGroups" id="groupId" status="groupStatus">
            <tr>
                <td class="th" colspan="2" style="padding: 0;">
                    <table style="width: 100%; font-size: 100%; font-weight: bold;">
                        <tr>
                            <td>
                                <s:property value="%{getGroupName(#groupId)}"/>
                            </td>
                            <td style="text-align: right;">
                                <a href="#" id="show_group_<s:property value="#groupId"/>"
                                   onclick="showAttributesGroup(<s:property value="#groupId"/>);"
                                        <s:if test="#groupStatus.first"> style="display: none;"</s:if>>
                                    <s:text name="tc.show_group"/>
                                </a>
                                <a href="#" id="hide_group_<s:property value="#groupId"/>"
                                   onclick="hideAttributesGroup(<s:property value="#groupId"/>);"
                                        <s:if test="!#groupStatus.first"> style="display: none;"</s:if>>
                                    <s:text name="tc.hide_group"/>
                                </a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>

            <s:iterator value="%{getGroupAttributes(#groupId)}">
                <tr id="attr_<s:property value="%{key}"/>_id" valign="middle" class="cols_1"
                        <s:if test="!#groupStatus.first"> style="display: none;"</s:if>>

                    <td class="col" style="width: 80%;"><s:property value="%{getAttributeTypeName(key)}"/></td>

                    <td class="col" style="width: 20%;">
                        <s:if test="%{isBuildingAttributeTypeSimple(key)}">
                            <nobr>
                                <s:textfield name="attributeMap[%{key}]" value="%{value}" cssStyle="width: 140px;"/>

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
                                          emptyOption="true"
                                          cssStyle="width: 140px;"/>

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
    </s:form>
</table>

<br/>
<br/>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td class="th_s" colspan="2">
            <s:text name="tc.tariffs_calculated"/>
        </td>
    </tr>
    <tr>
        <td colspan="2" class="cols_1"/>
    </tr>

    <s:if test="%{tariffCalculationDatesIsEmpty()}">
        <tr class="cols_1">
            <td class="col" colspan="2">
                <s:text name="tc.no_tariff_results"/>
            </td>
        </tr>
    </s:if>
    <s:else>
        <s:form action="tcResultsEdit">

            <s:iterator value="%{listTariffIds()}" id="tariffId">
                <s:hidden name="tariffMap[%{tariffId}]"/>
            </s:iterator>

            <s:hidden name="buildingId" value="%{building.id}"/>
            <s:hidden name="calculationDate" />

            <s:iterator value="tariffCalculationDates" id="calcDate">
                <tr>
                    <td colspan="2" class="th" style="padding: 0;">
                        <table style="width: 100%; font-size: 100%; font-weight: bold;">
                            <tr>
                                <td><s:text name="tc.tariffs_calculated_on"><s:param value="%{formatDate(#calcDate)}"/></s:text></td>
                                <td style="text-align: right;">
                                        <%-- TODO folding--%>

                                    <input type="button" class="btn-exit"
                                           value="<s:property value="%{getText('common.save')}"/>"
                                           onclick="saveSubmit('<s:property value="%{formatDate(#calcDate)}"/>');"/>

                                    <input type="button" class="btn-exit"
                                           value="<s:property value="%{getText('tc.upload')}"/>"
                                           onclick="uploadSubmit('<s:property value="%{formatDate(#calcDate)}"/>');"/>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>

                <s:iterator value="%{getTcResults(#calcDate)}">
                    <tr class="cols_1">
                        <td class="col" style="width: 80%;"><s:property value="%{getTariffTranslation(key)}"/></td>
                        <td class="col" style="width: 20%;">
                            <input id="tariff_<s:property value="%{key}"/>_<s:property value="%{formatDate(#calcDate)}"/>"
                                   type="text" value="<s:property value="%{value}"/>"/>
                        </td>
                    </tr>
                </s:iterator>
            </s:iterator>
        </s:form>
    </s:else>

</table>