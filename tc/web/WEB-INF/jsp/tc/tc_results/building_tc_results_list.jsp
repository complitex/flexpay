<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<%-- TODO: jQuery UI Dialog plug-in was deleted. This code must be work with jQuery Window plug-n (see eirc/web/WEB-INF/jsp/payments/registry/registry_view.jsp) --%>
<%--
<%@include file="/WEB-INF/jsp/common/includes/jquery_ui.jsp"%>
--%>

<script type="text/javascript">

    // folding functions
    function toggleElements(elements, showButton) {
        $.each(elements, function(i, value) {
            $(value).toggle();
        });
        $(showButton).val($(elements[0]).is(":visible") ? "<s:text name="common.hide" />" : "<s:text name="common.show" />");
    }

    function toggleResultsGroup(calcDate) {
        toggleElements($("tr[id^=tariff_row_][id$=" + calcDate + "]"), "#toggle_result_group_" + calcDate);
    }

    // upload results submission processing
/*
    <s:iterator value="tariffCalculationDates" id="calcDate">
        $(function() {
            $("#uploadTcResultsDialog_<s:property value="formatDateWithUnderlines(#calcDate)" />").dialog({
                bgiframe: true,
                modal: true,
                width: 380,
                height: 420,
                closeOnEscape: true,
                title: '<s:text name="tc.export.window_title" />',
                autoOpen: false,
                buttons: {
                    '<s:text name="tc.upload" />': function() {
                        $("#uploadTCResults_<s:property value="formatDateWithUnderlines(#calcDate)" />").submit();
                    },
                    '<s:text name="tc.cancel" />': function() {
                        $(this).dialog("close");
                    }
                }
            });

            $("#uploadTCResults_<s:property value="formatDateWithUnderlines(#calcDate)" />_calendar").datepicker({
                dateFormat: "yy/mm/dd",
                onSelect: function(dateText) {
                    $("#uploadTCResults_<s:property value="formatDateWithUnderlines(#calcDate)" />_date").val(dateText);
                }
            });
        });
    </s:iterator>
*/

    function uploadSubmit(calcDate) {
//        $("#uploadTcResultsDialog_" + calcDate).dialog("open");
    }
</script>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td class="th_s" colspan="2">
            <s:property value="getAddress(buildingId)" />
            <s:if test="hasPrimaryStatus(buildingId)">(<s:text name="tc.building_tc_results_edit.primary_status" />)</s:if>
        </td>
    </tr>

    <s:iterator value="alternateAddresses">
        <tr valign="middle" class="cols_1">
            <td class="col" colspan="2">
                <s:property value="getAddress(id)" /><s:if test="primaryStatus"> (<s:text name="tc.building_tc_results_edit.primary_status" />)</s:if>
            </td>
        </tr>
    </s:iterator>

    <s:if test="tariffCalculationDatesIsEmpty()">
        <tr class="cols_1"><td class="col" colspan="2"><s:text name="tc.no_tariff_results" /></td></tr>
    </s:if>
</table>

<s:if test="!tariffCalculationDatesIsEmpty()">
    <s:iterator value="tariffCalculationDates" id="calcDate">
        <%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>
        <s:form action="buildingTCResultsEdit">
            <s:hidden name="buildingId" />
            <s:hidden name="calculationDate" value="%{formatDate(#calcDate)}" />

            <table cellpadding="3" cellspacing="1" border="0" width="100%">
                <tr>
                    <td colspan="4" class="th" style="padding:0;">
                        <table style="width:100%;font-size:100%;font-weight:bold;">
                            <tr>
                                <td><s:text name="tc.tariffs_calculated_on"><s:param value="%{formatDate(#calcDate)}" /></s:text></td>
                                <td style="text-align:right;">
                                    <s:submit value="%{getText('common.edit')}" cssClass="btn-exit" />
                                    <input type="button" class="btn-exit" value="<s:text name="common.upload"/>" onclick="uploadSubmit('<s:property value="%{formatDateWithUnderlines(#calcDate)}"/>');"/>
                                    <input type="button" class="btn-exit" value="<s:text name="common.hide"/>" id="toggle_result_group_<s:property value="%{formatDateWithUnderlines(#calcDate)}"/>" onclick="toggleResultsGroup('<s:property value="%{formatDateWithUnderlines(#calcDate)}"/>');"/>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>

                <s:iterator value="getTcResults(#calcDate)" status="stat">
                    <tr id="tariff_row_<s:property value="id" />_<s:property value="formatDateWithUnderlines(#calcDate)" />"<s:if test="value < 0"> class="cols_1_highlighted"</s:if><s:else> class="cols_1"</s:else>>
                        <td class="col" style="width:60%;"><s:property value="getTariffTranslation(tariff.id)" /></td>
                        <td class="col" style="width:20%;"><s:property value="value" /></td>
                        <td class="col" style="width:10%;"><s:text name="%{lastTariffExportLogRecord.exportdate}" /></td>
            			<td class="col" style="width:20%;"><s:text name="%{lastTariffExportLogRecord.tariffExportCode.i18nName}" /></td>
                    </tr>
                </s:iterator>

                <tr class="cols_1">
                    <td class="col" style="width:60%;font-weight:bold;"><s:text name="tc.total_tariff" /></td>
                    <td class="col" style="width:20%;font-weight:bold;"><s:property value="getTotalTariff(#calcDate)" /></td>
                    <td colspan="2" class="col" style="width:20%;font-weight:bold;">&nbsp;</td>
                </tr>
            </table>
        </s:form>
    </s:iterator>
    
    <s:iterator value="tariffCalculationDates" id="calcDate">
        <div id="uploadTcResultsDialog_<s:property value="formatDateWithUnderlines(#calcDate)"/>" style="display:none;">
            <s:form action="buildingTCResultsUpload" id="uploadTCResults_%{formatDateWithUnderlines(#calcDate)}" method="POST">
                <s:hidden name="buildingId" />
                <s:hidden name="calculationDate" value="%{formatDate(#calcDate)}" />
                <s:hidden name="submitted" value="true" />
                <s:hidden name="date" />
                <div id="uploadTCResults_<s:property value="formatDateWithUnderlines(#calcDate)" />_calendar" />
            </s:form>
        </div>
    </s:iterator>
    
</s:if>
