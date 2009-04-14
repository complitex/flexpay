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
					<input type="submit" name="submitted" class="btn-exit" value="<s:text name="payments.operations.list.submit"/>"/>
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

			<s:iterator value="operations" status="operationStatus">
				<tr valign="middle" class="cols_1">
					<td class="col" align="right"><s:property value="%{#operationStatus.index + 1}"/></td>
					<td class="col" nowrap="nowrap">
						<input type="radio" name="selected_operation" value="<s:property value="%{#operationStatus.index + 1}"/>"/>
					</td>
					<td class="col" nowrap="nowrap"><s:date name="creationDate" format="HH:mm"/></td>					
					<td class="col" nowrap="nowrap"><s:property value="address"/></td>
					<td class="col" nowrap="nowrap"><s:property value="payerFIO"/></td>
					<td class="col" nowrap="nowrap"><s:property value="operationSumm"/></td>
					<td class="col" nowrap="nowrap"><s:property value="operationInputSumm"/></td>
					<td class="col" nowrap="nowrap"><s:property value="change"/></td>
					<td class="col" nowrap="nowrap" style="display: none;"><%-- TODO extract from document --%></td>
					<td class="col" nowrap="nowrap" style="display: none;"><%-- TODO extract from document --%></td>
				</tr>
			</s:iterator>

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
		</s:else>

	</table>

</s:form>