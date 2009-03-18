<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<script type="text/javascript" src="<c:url value="/resources/common/js/jquery/jquery-1.3.2.js"/>"></script>

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
            jQuery(showButton).val('<s:text name="common.hide"/>');
        } else {
            jQuery(showButton).val('<s:text name="common.show"/>');
        }
    }
    ;

    function toggleResultsGroup(calcDate) {
        toggleElements(jQuery('tr[id^="tariff_row_"][id$="' + calcDate + '"]'), '#toggle_result_group_' + calcDate);
    }

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
        jQuery('#uploadTcResultsDialog_' + calcDate).dialog('open');
    }
</script>

<s:actionerror/>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr><td class="th_s" colspan="2">
        <s:property value="%{getAddress(buildingId)}"/>

        <s:if test="%{hasPrimaryStatus(buildingId)}">(<s:text name="tc.building_tc_results_edit.primary_status"/>)</s:if>
    </td></tr>

    <s:iterator value="%{alternateAddresses}">
        <tr valign="middle" class="cols_1"><td class="col" colspan="2"><s:property value="%{getAddress(id)}"/><s:if test="primaryStatus">(<s:text name="tc.building_tc_results_edit.primary_status"/>)</s:if></td></tr>
    </s:iterator>

    <s:if test="%{tariffCalculationDatesIsEmpty()}">
        <tr class="cols_1"><td class="col" colspan="2"><s:text name="tc.no_tariff_results"/></td></tr>
    </s:if>
</table>

<s:if test="%{!tariffCalculationDatesIsEmpty()}">
    <s:iterator value="tariffCalculationDates" id="calcDate">
        <s:actionerror />
        <s:form action="buildingTCResultsEdit">
            <s:hidden name="buildingId" value="%{buildingId}"/>
            <s:hidden name="calculationDate" value="%{formatDate(#calcDate)}"/>

            <table cellpadding="3" cellspacing="1" border="0" width="100%">
                <tr>
                    <td colspan="3" class="th" style="padding:0;">
                        <table style="width:100%;font-size:100%;font-weight:bold;">
                            <tr>
                                <td><s:text name="tc.tariffs_calculated_on"><s:param value="%{formatDate(#calcDate)}"/></s:text></td>
                                <td style="text-align: right;">
                                    <s:submit value="%{getText('common.edit')}" cssClass="btn-exit"/>
                                    <input type="button" class="btn-exit" value="<s:text name="common.upload"/>" onclick="uploadSubmit('<s:property value="%{formatDateWithUnderlines(#calcDate)}"/>');"/>
                                    <input type="button" class="btn-exit" value="<s:text name="common.hide"/>" id="toggle_result_group_<s:property value="%{formatDateWithUnderlines(#calcDate)}"/>" onclick="toggleResultsGroup('<s:property value="%{formatDateWithUnderlines(#calcDate)}"/>');"/>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>

                <s:iterator value="%{getTcResults(#calcDate)}" status="stat">
                    <tr id="tariff_row_<s:property value="%{id}"/>_<s:property value="%{formatDateWithUnderlines(#calcDate)}"/>"<s:if test="%{value < 0}"> class="cols_1_highlighted" </s:if><s:else> class="cols_1"</s:else>>
                        <td class="col" style="width: 60%;"><s:property value="%{getTariffTranslation(tariff.id)}"/></td>
                        <td class="col" style="width: 20%;"><s:property value="%{value}"/></td>
                        <td class="col" style="width: 20%;"><s:text name="%{getTariffCalculationExportCode(id)}"/></td>
                    </tr>
                </s:iterator>

                <tr class="cols_1">
                    <td class="col" style="width: 60%; font-weight: bold;"><s:text name="tc.total_tariff"/></td>
                    <td class="col" style="width: 20%; font-weight: bold;"><s:property value="%{getTotalTariff(#calcDate)}"/></td>
                    <td class="col" style="width: 20%; font-weight: bold;">&nbsp;</td>
                </tr>
            </table>
        </s:form>
    </s:iterator>
    
    <s:iterator value="tariffCalculationDates" id="calcDate">
        <div id="uploadTcResultsDialog_<s:property value="%{formatDateWithUnderlines(#calcDate)}"/>" style="display: none;">
            <s:form action="buildingTCResultsUpload" id="uploadTCResults_%{formatDateWithUnderlines(#calcDate)}">
                <s:hidden name="buildingId" value="%{buildingId}"/>
                <s:hidden name="calculationDate" value="%{formatDate(#calcDate)}"/>
                <s:hidden name="submitted" value="true"/>
                <s:hidden name="date" value="%{date}"/>
                <div id="uploadTCResults_<s:property value="%{formatDateWithUnderlines(#calcDate)}"/>_calendar"/>
            </s:form>
        </div>
    </s:iterator>
    
</s:if>