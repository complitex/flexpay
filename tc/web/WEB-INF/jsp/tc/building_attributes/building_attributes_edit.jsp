<%@ page import="org.flexpay.bti.persistence.BuildingAttributeTypeSimple" %>
<%@ page import="org.flexpay.bti.persistence.BuildingAttributeTypeEnum" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<script type="text/javascript" src="<c:url value="/resources/common/js/jquery/jquery-1.3.2.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/common/js/jquery/validate/jquery.validate.js"/>"></script>

<link rel="stylesheet" type="text/css" href="<c:url value="/resources/common/js/jquery/jquery-ui/theme/ui.all.css"/>"/>
<script type="text/javascript" src="<c:url value="/resources/common/js/jquery/jquery-ui/jquery-ui-personalized-1.6rc6.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/common/js/jquery/jquery-ui/i18n/ui.datepicker-ru.js"/>"></script>

<script type="text/javascript">
    // folding functions
    function toggleElements(elements, showButton) {
        jQuery.each(elements, function(i, value) {
            jQuery(value).toggle();
        });

        if (jQuery(elements[0]).is(':visible')) {
            jQuery(showButton).val('<s:text name="tc.hide_group"/>');
        } else {
            jQuery(showButton).val('<s:text name="tc.show_group"/>');
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

    function toggleResultsGroup(calcDate) {
        toggleElements(jQuery('tr[id^="tariff_row_"][id$="' + calcDate + '"]'), '#toggle_result_group_' + calcDate);
    }
    
    // validation
    function isValidTcResultValue(value) {
        var pattern = /^(-)?(\d)+([\.,]?\d{0,4})$/;
        return value.match(pattern);
    }

    jQuery(document).ready(function() {
        jQuery.validator.addMethod("tcResultValue", function(value, element) {
            return isValidTcResultValue(value);
        }, '<s:text name="tc.error.bad.tc.result.value"/>');
    });

    // validator configuration
    <s:iterator value="tariffCalculationDates" id="calcDate">
    jQuery(function() {
        var validator = jQuery('#tcResultsEdit_<s:property value="%{formatDateWithUnderlines(#calcDate)}"/>').validate({
            rules: {
                <s:iterator value="%{getTcResults(#calcDate)}" status="status">'tariffMap[<s:property value="%{key}"/>]': 'tcResultValue'<s:if test="!#status.last">, </s:if></s:iterator>
            },
            messages: {},
            errorClass: 'cols_1_error',
            success: function(label) {
                if (validator.numberOfInvalids() == 0) {
                    jQuery('#tcResultsEdit_<s:property value="%{formatDateWithUnderlines(#calcDate)}"/>_errors').hide();
                }
            },
            showErrors: function(errorMap, errorList) {
                jQuery('#tcResultsEdit_<s:property value="%{formatDateWithUnderlines(#calcDate)}"/>_errors').html("<s:text name="tc.error.tc.results.form.contains.errors"/>").addClass("cols_1_error").show();
                this.defaultShowErrors();
            },
            errorPlacement: function(error, element) {
            },
            invalidHandler: function(form, validator) {
                alert('<s:text name="tc.error.invalid.tc.result.submit"/>');
            }
        });
    });
    </s:iterator>

    // attribute date picker configuration
    jQuery(function() {
        jQuery('#buildingAttributesEdit_attributeDate').datepicker({
            dateFormat: 'yy/mm/dd'
        });
    });   

    // upload results submission processing
    <s:iterator value="tariffCalculationDates" id="calcDate">
    jQuery(function() {
        jQuery('#uploadTcResultsDialog_<s:property value="%{formatDateWithUnderlines(#calcDate)}"/>').dialog({
            bgiframe: true,
            modal: true,
            width: 380,
            height: 420,
            closeOnEscape: true,
            title: '<s:text name="tc.export.window_title"/>',
            autoOpen: false,
            buttons: {
                '<s:text name="tc.upload"/>': function() {    
                    jQuery('#uploadTCResults_<s:property value="%{formatDateWithUnderlines(#calcDate)}"/>').submit();
                },
                '<s:text name="tc.cancel"/>': function() {
                    jQuery(this).dialog('close');
                }
            }
        });

        jQuery('#uploadTCResults_<s:property value="%{formatDateWithUnderlines(#calcDate)}"/>_calendar').datepicker({
            dateFormat: 'yy/mm/dd',
            onSelect: function(dateText) {
                jQuery('#uploadTCResults_<s:property value="%{formatDateWithUnderlines(#calcDate)}"/>_date').val(dateText);
            }
        });
    });
    </s:iterator>

    function uploadSubmit(calcDate) {
        if (tcResultsChanged(calcDate)) {
            alert('<s:text name="tc.must_save_tc_results"/>');
            return;
        }

        jQuery('#uploadTcResultsDialog_' + calcDate).dialog('open');
    }

    function tcResultsChanged(calcDate) {
        var elements = jQuery('input[id^="tcResultsEdit_' + calcDate + '_tariffMap_"][type="text"]');
        var changed = false;

        jQuery.each(elements, function(i, value) {
            var newValue = jQuery(value).val();
            var oldValue = jQuery('#' + value.id + 'old').val();

            if (oldValue != newValue) {
                changed = true;
            }
        });

        return changed;
    }
</script>

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

        <%-- calendar --%>
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
                                <input type="button" class="btn-exit" id="toggle_attribute_group_<s:property value="#groupId"/>" onclick="toggleAttributesGroup(<s:property value="#groupId"/>);" value="<s:if test="#groupStatus.first"><s:text name="tc.hide_group"/></s:if><s:else><s:text name="tc.show_group"/></s:else>"/>
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

<br/>
<br/>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr><td class="th_s" colspan="2"><s:text name="tc.tariffs_calculated"/></td></tr>
    <s:if test="%{tariffCalculationDatesIsEmpty()}"><tr class="cols_1"><td class="col" colspan="2"><s:text name="tc.no_tariff_results"/></td></tr></s:if>
</table>

<s:if test="%{!tariffCalculationDatesIsEmpty()}">
    <s:iterator value="tariffCalculationDates" id="calcDate">
        <s:form id="tcResultsEdit_%{formatDateWithUnderlines(#calcDate)}" action="tcResultsEdit">
            <table cellpadding="3" cellspacing="1" border="0" width="100%">
                <tr>
                    <td colspan="2" class="th" style="padding:0;">
                        <table style="width:100%;font-size:100%;font-weight:bold;">
                            <tr>
                                <td><s:text name="tc.tariffs_calculated_on"><s:param value="%{formatDate(#calcDate)}"/></s:text></td>
                                <td style="text-align: right;">
                                    <s:submit cssClass="btn-exit" value="%{getText('common.save')}"/>
                                    <input type="button" class="btn-exit" value="<s:text name="tc.upload"/>" onclick="uploadSubmit('<s:property value="%{formatDateWithUnderlines(#calcDate)}"/>');"/>
                                    <input type="button" class="btn-exit" value="<s:text name="tc.hide_group"/>" id="toggle_result_group_<s:property value="%{formatDateWithUnderlines(#calcDate)}"/>" onclick="toggleResultsGroup('<s:property value="%{formatDateWithUnderlines(#calcDate)}"/>');"/>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>

                <tr>
                    <td id="tcResultsEdit_<s:property value="%{formatDateWithUnderlines(#calcDate)}"/>_errors" colspan="2" style="display: none;"/>
                </tr>

                <s:hidden name="buildingId" value="%{building.id}"/>
                <s:hidden name="calculationDate" value="%{formatDate(#calcDate)}"/>

                <s:iterator value="%{getTcResults(#calcDate)}">
                    <tr id="tariff_row_<s:property value="%{key}"/>_<s:property value="%{formatDateWithUnderlines(#calcDate)}"/>"<s:if test="%{value < 0}"> class="cols_1_highlighted" </s:if><s:else> class="cols_1"</s:else>>
                        <td class="col" style="width: 80%;"><s:property value="%{getTariffTranslation(key)}"/></td>
                        <td class="col" style="width: 20%;">
                            <s:textfield name="tariffMap[%{key}]" value="%{value}"/>
                            <input type="hidden" value="<s:property value="%{value}"/>" id="tcResultsEdit_<s:property value="%{formatDateWithUnderlines(#calcDate)}"/>_tariffMap_<s:property value="%{key}"/>_old"/>
                        </td>
                    </tr>
                </s:iterator>

                <tr class="cols_1">
                    <td class="col" style="width: 80%; font-weight: bold;"><s:text name="tc.total_tariff"/></td>
                    <td class="col" style="width: 20%; font-weight: bold;"><s:property value="%{getTotalTariff(#calcDate)}"/></td>
                </tr>
            </table>
        </s:form>

        <div id="uploadTcResultsDialog_<s:property value="%{formatDateWithUnderlines(#calcDate)}"/>" style="display: none;">
            <s:form action="uploadTCResults" id="uploadTCResults_%{formatDateWithUnderlines(#calcDate)}">
                <s:hidden name="buildingId" value="%{building.id}"/>
                <s:hidden name="calculationDate" value="%{formatDate(#calcDate)}"/>
                <s:hidden name="submitted" value="true"/>
                <s:hidden name="date" value="%{date}"/>
                <div id="uploadTCResults_<s:property value="%{formatDateWithUnderlines(#calcDate)}"/>_calendar"/>
            </s:form>
        </div>

    </s:iterator>
</s:if>