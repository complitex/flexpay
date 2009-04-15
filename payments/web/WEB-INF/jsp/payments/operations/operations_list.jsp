<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@include file="/WEB-INF/jsp/payments/include/stylesheet.jsp" %>

<s:actionerror/>

<s:form action="operationsList">

	<table cellpadding="3" cellspacing="1" border="0" width="100%">

		<tr>
			<td colspan="10" nowrap="nowrap">
				<s:text name="payments.report.generate.date_from"/>
				<%@include file="/WEB-INF/jsp/common/filter/begin_date_filter.jsp" %>

				<s:text name="payments.report.generate.date_till"/>
				<%@include file="/WEB-INF/jsp/common/filter/end_date_filter.jsp" %>

				<s:submit name="submitted" cssClass="btn-exit" value="%{getText('payments.operations.list.filter')}"/>
			</td>
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
					<input type="submit" name="submitted" class="btn-exit"
						   value="<s:text name="payments.operations.list.submit"/>"/>
					<input type="button" class="btn-exit" value="<s:text name="payments.operations.list.detailed"/>"/>
				</td>
				<td colspan="5">
					<%@include file="/WEB-INF/jsp/common/filter/pager/pager.jsp" %>
				</td>
			</tr>

			<tr>
				<td class="th" width="1%"><s:text name="payments.operations.list.number_symbol"/></td>
				<td class="th" width="1%">&nbsp;</td>
				<td class="th"><s:text name="payments.operations.list.creation_date"/></td>
				<td class="th"><s:text name="payments.operations.list.address"/></td>
				<td class="th"><s:text name="payments.operations.list.fio"/></td>
				<td class="th"><s:text name="payments.operations.list.summ"/></td>
				<td class="th"><s:text name="payments.operations.list.pay_summ"/></td>
				<td class="th"><s:text name="payments.operations.list.change"/></td>
				<td class="th" style="display: none;"><s:text name="payments.operations.list.service"/></td>
				<td class="th" style="display: none;"><s:text name="payments.operations.list.provider"/></td>
			</tr>

			<s:iterator value="operations" status="opStatus">
				<tr valign="middle"
					<s:if test="%{isOperationRegistered(operationStatus.code)}"> class="col_black"</s:if>
					<s:elseif test="%{isOperationCreated(operationStatus.code) || isOperationError(operationStatus.code)}"> class="col_blue"</s:elseif>
					<s:elseif test="%{isOperationReturned(operationStatus.code)}"> class="col_red"</s:elseif>>

					<td class="col_oper" align="right"><s:property value="%{#opStatus.index + 1}"/></td>
					<td class="col_oper" nowrap="nowrap">
						<input type="radio" name="selected_operation" value="<s:property value="%{#opStatus.index + 1}"/>"/>
					</td>
					<td class="col_oper" nowrap="nowrap"><s:date name="creationDate" format="HH:mm"/></td>
					<td class="col_oper" nowrap="nowrap"><s:property value="address"/></td>
					<td class="col_oper" nowrap="nowrap"><s:property value="payerFIO"/></td>
					<td class="col_oper" nowrap="nowrap"><s:property value="operationSumm"/></td>
					<td class="col_oper" nowrap="nowrap"><s:property value="operationInputSumm"/></td>
					<td class="col_oper" nowrap="nowrap"><s:property value="change"/></td>
					<td class="col_oper" nowrap="nowrap" style="display: none;">&nbsp;</td>
					<td class="col_oper" nowrap="nowrap" style="display: none;">&nbsp;</td>
				</tr>

				<s:iterator value="documents">
					<s:if test="%{!isDocumentDeleted(documentStatus.code)}">
						<tr <s:if test="%{isDocumentRegistered(documentStatus.code)}"> class="col_black"</s:if>
							<s:elseif test="%{isDocumentCreated(documentStatus.code) || isDocumentError(documentStatus.code)}"> class="col_blue"</s:elseif>
							<s:elseif test="%{isDocumentReturned(documentStatus.code)}"> class="col_red"</s:elseif>>

							<td class="col_doc" nowrap="nowrap">&nbsp;</td>
							<td class="col_doc" nowrap="nowrap">&nbsp;</td>
							<td class="col_doc" nowrap="nowrap">&nbsp;</td>
							<td class="col_doc" nowrap="nowrap"><s:property value="address"/></td>
							<td class="col_doc" nowrap="nowrap"><s:property value="payerFIO"/></td>
							<td class="col_doc" nowrap="nowrap"><s:property value="summ"/></td>
							<td class="col_doc" nowrap="nowrap">&nbsp;</td>
							<td class="col_doc" nowrap="nowrap">&nbsp;</td>
							<td class="col_doc" nowrap="nowrap" style="display: none;"><s:property value="service.serviceType.name"/></td>
							<td class="col_doc" nowrap="nowrap" style="display: none;"><s:property value="service.serviceProvider.name"/></td>
						</tr>
					</s:if>
				</s:iterator>
			</s:iterator>

			<tr>
				<td colspan="5">
					<input type="submit" name="submitted" class="btn-exit" value="<s:text name="payments.operations.list.submit"/>"/>
					<input type="button" class="btn-exit" value="<s:text name="payments.operations.list.detailed"/>"/>
				</td>
				<td colspan="5">
					<%@include file="/WEB-INF/jsp/common/filter/pager/pager.jsp" %>
				</td>
			</tr>
		</s:else>

	</table>

</s:form>