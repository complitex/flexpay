<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<script type="text/javascript">
    FP.switchSorter(["operationSorterByIdButton"]);
</script>

<table cellpadding="3" cellspacing="1" border="0" width="100%" class="operations">
    <s:if test="tradingDayControlPanel.processStatus != null">
        <tr>
            <td colspan="14">
                <%@include file="/WEB-INF/jsp/payments/trading_day/trading_day_control_panel.jsp"%>
            </td>
        </tr>
    </s:if>
    <s:if test="operations.isEmpty()">
        <tr>
            <td colspan="14">
                <s:text name="payments.operations.list.no_operations_found" />
            </td>
        </tr>
    </s:if><s:else>
        <tr>
            <td colspan="14" class="operations_list_summary">
                <s:text name="payments.operations.list.total_operations">
                    <s:param value="pager.getTotalNumberOfElements()" />
                </s:text>
                &nbsp;
                <s:text name="payments.operations.list.total_payments_sum">
                    <s:param value="getTotalPaymentsSum()" />
                    <s:param value="getCurrencyName()" />
                </s:text>
                &nbsp;
                <s:text name="payments.operations.list.total_returns_sum">
                    <s:param value="getTotalReturnsSum()" />
                    <s:param value="getCurrencyName()" />
                </s:text>
            </td>
        </tr>
        <tr>
            <td colspan="14">
<%--
                <input type="button" onclick="setStatus(2);" class="btn-exit btn-register" value="<s:text name="payments.operations.list.register" />" />
                <input type="button" onclick="setStatus(3);" class="btn-exit btn-delete" value="<s:text name="payments.operations.list.delete" />" />
--%>
                <input type="button" onclick="setStatus(4);" class="btn-exit btn-return" value="<s:text name="payments.operations.list.return" />" />
                <input type="button" onclick="showDetails();" name="showDetails" class="btn-exit" value="<s:text name="payments.operations.list.with_detailed" />" />
                <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
            </td>
        </tr>
        <tr>
            <td class="th" width="1%">&nbsp;</td>
            <td class="th" width="1%"><s:text name="payments.operations.list.number_symbol" /></td>
            <td class="<s:property value="operationSorterById.activated ? 'th_s' : 'th'" />" nowrap>
                <%@include file="/WEB-INF/jsp/payments/sorters/operation_sort_by_id_header.jsp"%>
            </td>
            <td class="th"><s:text name="payments.operations.list.creation_date" /></td>
            <td class="th"><s:text name="payments.operations.list.address" /></td>
            <td class="th"><s:text name="payments.operations.list.fio" /></td>
            <td class="th"><s:text name="payments.operations.list.sum" /></td>
            <td class="th"><s:text name="payments.operations.list.pay_sum" /></td>
            <td class="th"><s:text name="payments.operations.list.change" /></td>
            <td class="th service_column" style="display:none;"><s:text name="payments.operations.list.service" /></td>
            <td class="th service_provider_column" style="display:none;"><s:text name="payments.operations.list.provider" /></td>
            <td class="th">&nbsp;</td>
        </tr>
        <s:iterator value="operations" status="opStatus">
            <tr valign="middle" class="col_oper full_operation_header_row<s:if test="%{isOperationRegistered(operationStatus.code)}"> col_black</s:if>
                    <s:elseif test="%{isOperationCreated(operationStatus.code) || isOperationError(operationStatus.code)}"> col_blue</s:elseif>
                    <s:elseif test="%{isOperationReturned(operationStatus.code)}"> col_red</s:elseif>">

                <td class="col_oper">
                    <input type="radio" name="operation.id" onclick="showButtons(<s:property value="operationStatus.code" />);" value="<s:property value="id" />" />
                </td>
                <td align="right"><s:property value="#opStatus.index + pager.thisPageFirstElementNumber + 1" /></td>
                <td><s:property value="id" /></td>
                <td><s:date name="creationDate" format="HH:mm:ss" /></td>
                <td><s:property value="address" /></td>
                <td><s:property value="payerFIO" /></td>
                <td><s:property value="operationSumm" /></td>
                <td><s:property value="operationInputSumm" /></td>
                <td><s:property value="change" /></td>
                <td class="service_column" style="display:none;">&nbsp;</td>
                <td class="service_provider_column" style="display:none;">&nbsp;</td>
                <td>
                    <a href="#" onclick="printQuittance(<s:property value="id" />);"><s:text name="print" /></a>
                </td>
            </tr>
            <%-- document rows (are not shown by default, appear in 'detailed' view) --%>
            <s:iterator value="documents" id="document">
                <s:if test="!isDocumentDeleted(documentStatus.code)">
                    <tr style="display:none;" class="col_doc document_row<s:if test="isDocumentRegistered(documentStatus.code)"> col_black</s:if>
                            <s:elseif test="isDocumentCreated(documentStatus.code) || isDocumentError(documentStatus.code)"> col_blue</s:elseif>
                            <s:elseif test="isDocumentReturned(documentStatus.code)"> col_red</s:elseif><s:if test="isHighlighted(#document)"> highlighted</s:if>">

                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td><s:property value="address" /></td>
                        <td><s:property value="payerFIO" /></td>
                        <td><s:property value="sum" /></td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                        <td class="service_column" style="display:none;"><s:property value="service.serviceType.name" /></td>
                        <td class="service_provider_column" style="display:none;" colspan="2"><s:property value="service.serviceProvider.name" /></td>
                    </tr>
                </s:if>
            </s:iterator>
            <%-- operation footer (is not shown by default, but appears in 'detailed' view) --%>
<%--
            <tr valign="middle" style="display: none;" class="col_oper operation_footer_row
                    <s:if test="%{isOperationRegistered(operationStatus.code)}"> col_black</s:if>
                    <s:elseif test="%{isOperationCreated(operationStatus.code) || isOperationError(operationStatus.code)}"> col_blue</s:elseif>
                    <s:elseif test="%{isOperationReturned(operationStatus.code)}"> col_red</s:elseif>">

                <td align="right">&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td><s:property value="address"/></td>
                <td><s:property value="payerFIO"/></td>
                <td><s:property value="operationSum"/></td>
                <td><s:property value="operationInputSum"/></td>
                <td><s:property value="change"/></td>
                <td class="service_column" style="display: none;">&nbsp;</td>
                <td class="service_provider_column" style="display: none;" colspan="2">&nbsp;</td>
            </tr>
--%>
        </s:iterator>
        <tr>
            <td colspan="14">
<%--
                <input type="button" onclick="setStatus(2);" class="btn-exit btn-register" value="<s:text name="payments.operations.list.register" />" />
                <input type="button" onclick="setStatus(3);" class="btn-exit btn-delete" value="<s:text name="payments.operations.list.delete" />" />
--%>
                <input type="button" onclick="setStatus(4);" class="btn-exit btn-return" value="<s:text name="payments.operations.list.return" />" />
                <input type="button" onclick="showDetails();" name="showDetails" class="btn-exit" value="<s:text name="payments.operations.list.with_detailed" />" />
                <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
            </td>
        </tr>
    </s:else>

</table>

<script type="text/javascript">

    $(function() {
        if ($("#documentSearch").val() == "true") {
            showDetails();
            documentSearch(true);
        } else {
            documentSearch(false);
        }
        showButtons();
    });

</script>
