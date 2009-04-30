<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@include file="/WEB-INF/jsp/payments/include/stylesheet.jsp" %>

<script type="text/javascript">
	function showDetails() {
		$('tr.full_operation_header_row').toggle();
		$('tr.brief_operation_header_row').toggle();
		$('tr.document_row').toggle();
		$('tr.operation_footer_row').toggle();

		// because of nesting the normal toggle will not work like we need it
		// so here is a little trick (let me do a little bit monkey business)
		if ($('#service_column_header:hidden').length > 0) {
			$('td.service_column:hidden').show();
			$('td.service_provider_column:hidden').show();
		} else {
			$('td.service_column:visible').hide();
			$('td.service_provider_column:visible').hide();
		}
	}

	function showButtons(state) {

		switch (state) {
			case 1:
				enableButtons('.btn-register');
				disableButtons('.btn-return');
				enableButtons('.btn-delete');
				break;
			case 2:
				disableButtons('.btn-register');
				enableButtons('.btn-return');
				disableButtons('.btn-delete');
				break;
			case 4:
				disableButtons('.btn-register');
				disableButtons('.btn-return');
				enableButtons('.btn-delete');
				break;
			case 5:
				disableButtons('.btn-register');
				disableButtons('.btn-return');
				enableButtons('.btn-delete');
				break;
			default:
				disableButtons('.btn-register');
				disableButtons('.btn-return');
				disableButtons('.btn-delete');
				break;
		}
	}

	function enableButtons(selector) {
		$(selector).removeAttr('disabled');
		$(selector).removeClass('btn-search');
		$(selector).addClass('btn-exit');
	}

	function disableButtons(selector) {
		$(selector).attr('disabled', 'disabled');
		$(selector).removeClass('btn-exit');
		$(selector).addClass('btn-search');
	}

	$(function() {
		disableButtons('.btn-register');
		disableButtons('.btn-return');
		disableButtons('.btn-delete');
	});

	function setOperationId(id) {
		$('#operationsList_selectedOperationId').val(id);
	}

	function setStatus(status) {
		$('#operationsList_status').val(status);
		return true;
	}

</script>

<s:actionerror/>

<s:form action="operationsList">
	<s:hidden name="status"/>
	<s:hidden name="selectedOperationId"/>

	<table cellpadding="3" cellspacing="1" border="0" width="100%" class="operations">

		<sec:authorize ifAllGranted="ROLE_PAYMENTS_DEVELOPER">
		<tr>
			<td colspan="10" nowrap="nowrap">
				<s:text name="payments.report.generate.date_from"/>
				<%@include file="/WEB-INF/jsp/common/filter/begin_date_filter.jsp" %>

				<s:text name="payments.report.generate.date_till"/>
				<%@include file="/WEB-INF/jsp/common/filter/end_date_filter.jsp" %>

				<s:submit name="dateSubmitted" cssClass="btn-exit" value="%{getText('payments.operations.list.filter')}"/>
			</td>
		</tr>
		</sec:authorize>

		<tr>
			<%-- filtering by time --%>

			<%-- filtering by service type --%>

			<%-- filtering by summ --%>
		</tr>

		<s:if test="%{operationsListIsEmpty()}">
			<tr>
				<td colspan="10">
					<s:text name="payments.operations.list.no_operations_found"/>
				</td>
			</tr>
		</s:if>
		<s:else>
			<tr>
				<td colspan="10">
					<s:text name="payments.operations.list.total"/>
					<s:property value="%{getTotalOperations()}"/>
				</td>
			</tr>

			<tr>
				<td colspan="5">
					<s:submit name="registerSubmitted" onclick="setStatus(2);" cssClass="btn-exit btn-register" value="%{getText('payments.operations.list.register')}"/>
					<s:submit name="returnSubmitted" onclick="setStatus(4);" cssClass="btn-exit btn-return" value="%{getText('payments.operations.list.return')}"/>
					<s:submit name="deleteSubmitted" onclick="setStatus(3);" cssClass="btn-exit btn-delete" value="%{getText('payments.operations.list.delete')}"/>
					
					<input type="button" class="btn-exit" onclick="showDetails();" value="<s:text name="payments.operations.list.detailed"/>"/>
				</td>
				<td colspan="5">
					<%@include file="/WEB-INF/jsp/common/filter/pager/pager.jsp" %>
				</td>
			</tr>

			<tr>
				<td class="th" width="1%">&nbsp;</td>
				<td class="th" width="1%"><s:text name="payments.operations.list.number_symbol"/></td>
				<td class="th" width="1%"><s:text name="payments.operations.list.uno"/></td>
				<td class="th"><s:text name="payments.operations.list.creation_date"/></td>
				<td class="th"><s:text name="payments.operations.list.address"/></td>
				<td class="th"><s:text name="payments.operations.list.fio"/></td>
				<td class="th"><s:text name="payments.operations.list.summ"/></td>
				<td class="th"><s:text name="payments.operations.list.pay_summ"/></td>
				<td class="th"><s:text name="payments.operations.list.change"/></td>
				<td class="th service_column" id="service_column_header" style="display: none;"><s:text name="payments.operations.list.service"/></td>
				<td class="th service_provider_column" style="display: none;"><s:text name="payments.operations.list.provider"/></td>
			</tr>

			<%-- operation rows --%>
			<s:iterator value="operations" status="opStatus">

				<%-- full operation header--%>
				<tr valign="middle" class="full_operation_header_row<s:if test="%{isOperationRegistered(operationStatus.code)}"> col_black</s:if>
					<s:elseif test="%{isOperationCreated(operationStatus.code) || isOperationError(operationStatus.code)}"> col_blue</s:elseif>
					<s:elseif test="%{isOperationReturned(operationStatus.code)}"> col_red</s:elseif>">

					<td class="col_oper" nowrap="nowrap">
						<input type="radio" onclick="showButtons(<s:property value="operationStatus.code"/>);setOperationId(<s:property value="id"/>);"/>
					</td>
					<td class="col_oper" align="right"><s:property value="%{#opStatus.index + 1}"/></td>
					<td class="col_oper" nowrap="nowrap"><s:property value="id"/></td>
					<td class="col_oper" nowrap="nowrap"><s:date name="creationDate" format="HH:mm"/></td>
					<td class="col_oper" nowrap="nowrap"><s:property value="address"/></td>
					<td class="col_oper" nowrap="nowrap"><s:property value="payerFIO"/></td>
					<td class="col_oper" nowrap="nowrap"><s:property value="operationSumm"/></td>
					<td class="col_oper" nowrap="nowrap"><s:property value="operationInputSumm"/></td>
					<td class="col_oper" nowrap="nowrap"><s:property value="change"/></td>
					<td class="col_oper service_column" nowrap="nowrap" style="display: none;">&nbsp;</td>
					<td class="col_oper service_provider_column" nowrap="nowrap" style="display: none;">&nbsp;</td>
				</tr>

				<%-- brief operation header (is not shown by default, appears in 'detailed' view) --%>
				<tr valign="middle" style="display: none;" class="brief_operation_header_row<s:if test="%{isOperationRegistered(operationStatus.code)}"> col_black</s:if>
					<s:elseif test="%{isOperationCreated(operationStatus.code) || isOperationError(operationStatus.code)}"> col_blue</s:elseif>
					<s:elseif test="%{isOperationReturned(operationStatus.code)}"> col_red</s:elseif>">

					<td class="col_oper" nowrap="nowrap">
						<input type="radio" onclick="showButtons(<s:property value="operationStatus.code"/>);setOperationId(<s:property value="id"/>);"/>
					</td>
					<td class="col_oper" align="right"><s:property value="%{#opStatus.index + 1}"/></td>
					<td class="col_oper" nowrap="nowrap"><s:property value="id"/></td>
					<td class="col_oper" nowrap="nowrap"><s:date name="creationDate" format="HH:mm"/></td>
					<td class="col_oper" nowrap="nowrap">&nbsp;</td>
					<td class="col_oper" nowrap="nowrap">&nbsp;</td>
					<td class="col_oper" nowrap="nowrap">&nbsp;</td>
					<td class="col_oper" nowrap="nowrap">&nbsp;</td>
					<td class="col_oper" nowrap="nowrap">&nbsp;</td>
					<td class="col_oper service_column" nowrap="nowrap" style="display: none;">&nbsp;</td>
					<td class="col_oper service_provider_column" nowrap="nowrap" style="display: none;">&nbsp;</td>
				</tr>

				<%-- document rows (are not shown by default, appear in 'detailed' view) --%>
				<s:iterator value="documents">
					<s:if test="%{!isDocumentDeleted(documentStatus.code)}">
						<tr style="display: none;" class="document_row<s:if test="%{isDocumentRegistered(documentStatus.code)}"> col_black</s:if>
							<s:elseif test="%{isDocumentCreated(documentStatus.code) || isDocumentError(documentStatus.code)}"> col_blue</s:elseif>
							<s:elseif test="%{isDocumentReturned(documentStatus.code)}"> col_red</s:elseif>">

							<td class="col_doc" nowrap="nowrap">&nbsp;</td>
							<td class="col_doc" nowrap="nowrap">&nbsp;</td>
							<td class="col_doc" nowrap="nowrap">&nbsp;</td>
							<td class="col_doc" nowrap="nowrap">&nbsp;</td>
							<td class="col_doc" nowrap="nowrap"><s:property value="address"/></td>
							<td class="col_doc" nowrap="nowrap"><s:property value="payerFIO"/></td>
							<td class="col_doc" nowrap="nowrap"><s:property value="summ"/></td>
							<td class="col_doc" nowrap="nowrap">&nbsp;</td>
							<td class="col_doc" nowrap="nowrap">&nbsp;</td>
							<td class="col_doc service_column" nowrap="nowrap" style="display: none;"><s:property value="service.serviceType.name"/></td>
							<td class="col_doc service_provider_column" nowrap="nowrap" style="display: none;"><s:property value="service.serviceProvider.name"/></td>
						</tr>
					</s:if>
				</s:iterator>

				<%-- operation footer (is not shown by default, but appears in 'detailed' view) --%>
				<tr valign="middle" style="display: none;" class="operation_footer_row
					<s:if test="%{isOperationRegistered(operationStatus.code)}"> col_black</s:if>
					<s:elseif test="%{isOperationCreated(operationStatus.code) || isOperationError(operationStatus.code)}"> col_blue</s:elseif>
					<s:elseif test="%{isOperationReturned(operationStatus.code)}"> col_red</s:elseif>">

					<td class="col_oper" align="right">&nbsp;</td>
					<td class="col_oper" nowrap="nowrap">&nbsp;</td>
					<td class="col_oper" nowrap="nowrap">&nbsp;</td>
					<td class="col_oper" nowrap="nowrap">&nbsp;</td>
					<td class="col_oper" nowrap="nowrap"><s:property value="address"/></td>
					<td class="col_oper" nowrap="nowrap"><s:property value="payerFIO"/></td>
					<td class="col_oper" nowrap="nowrap"><s:property value="operationSumm"/></td>
					<td class="col_oper" nowrap="nowrap"><s:property value="operationInputSumm"/></td>
					<td class="col_oper" nowrap="nowrap"><s:property value="change"/></td>
					<td class="col_oper service_column" nowrap="nowrap" style="display: none;">&nbsp;</td>
					<td class="col_oper service_provider_column" nowrap="nowrap" style="display: none;">&nbsp;</td>
				</tr>
			</s:iterator>

			<tr>
				<td colspan="5">
					<s:submit name="registerSubmitted" onclick="setStatus(2);" cssClass="btn-exit btn-register" value="%{getText('payments.operations.list.register')}"/>
					<s:submit name="returnSubmitted" onclick="setStatus(4);" cssClass="btn-exit btn-return" value="%{getText('payments.operations.list.return')}"/>
					<s:submit name="deleteSubmitted" onclick="setStatus(3);" cssClass="btn-exit btn-delete" value="%{getText('payments.operations.list.delete')}"/>

					<input type="button" class="btn-exit" onclick="showDetails();" value="<s:text name="payments.operations.list.detailed"/>"/>
				</td>
				<td colspan="5">
					<%@include file="/WEB-INF/jsp/common/filter/pager/pager.jsp" %>
				</td>
			</tr>
		</s:else>

	</table>

</s:form>