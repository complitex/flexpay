<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@include file="/WEB-INF/jsp/payments/include/stylesheet.jsp"%>

<script type="text/javascript">
	FP.calendars("#beginDate", true);
	FP.calendars("#endDate", true);
</script>

<s:form>
	<table>
		<%--<tr>--%>
			<%--<td colspan="3">--%>
				<%--<s:text name="payments.operations.list.current_user"/>--%>
				<%--<sec:authentication property="principal.username"/>--%>
			<%--</td>--%>
		<%--</tr>--%>

		<%--<tr>--%>
			<%--<td nowrap="nowrap">--%>
				<%--<s:text name="payments.report.generate.date_from"/>--%>
				<%--<s:textfield name="beginDate" id="beginDate" readonly="true"/>--%>
			<%--</td>--%>
			<%--<td nowrap="nowrap">--%>
				<%--<s:text name="payments.report.generate.date_till"/>--%>
				<%--<s:textfield name="endDate" id="endDate" readonly="true"/>--%>
			<%--</td>--%>
			<%--<td>--%>
				<%--<input type="button" name="submitted" class="btn-exit"--%>
					   <%--value="<s:text name="payments.operations.list.filter"/>"/>--%>
			<%--</td>--%>
		<%--</tr>--%>

		<tr>
			<td colspan="3">
				<s:text name="payments.operations.list.total"/>
				<s:text name="payments.demo.data.total.operations"/>
			</td>
		</tr>
	</table>
</s:form>

<s:form action="operationsList">

	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td colspan="9">
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
			<%--<td class="th">&nbsp;</td>--%>
		</tr>

		<tr valign="middle" class="cols_1">
			<td class="col" align="right">1</td>
			<td class="col" nowrap="nowrap"><input type="radio"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation1.creation_date"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation1.address"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation1.fio"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation1.summ"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation1.pay_summ"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation1.change"/></td>
			<%--<td class="col" nowrap="nowrap"><a href="#"><s:text name="payments.operations.list.detailed"/></a></td>--%>
		</tr>

		<tr valign="middle" class="cols_1">
			<td class="col" align="right">2</td>
			<td class="col" nowrap="nowrap"><input type="radio"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation2.creation_date"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation2.address"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation2.fio"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation2.summ"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation2.pay_summ"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation2.change"/></td>
			<%--<td class="col" nowrap="nowrap"><a href="#"><s:text name="payments.operations.list.detailed"/></a></td>--%>
		</tr>

		<tr valign="middle" class="cols_1">
			<td class="col" align="right">3</td>
			<td class="col" nowrap="nowrap"><input type="radio"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.creation_date"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.address"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.fio"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.summ"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.pay_summ"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.change"/></td>
			<%--<td class="col" nowrap="nowrap"><a href="#"><s:text name="payments.operations.list.detailed"/></a></td>--%>
		</tr>

		<tr valign="middle" class="cols_1">
			<td class="col" align="right">4</td>
			<td class="col" nowrap="nowrap"><input type="radio"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.creation_date"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.address"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.fio"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.summ"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.pay_summ"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.change"/></td>
			<%--<td class="col" nowrap="nowrap"><a href="#"><s:text name="payments.operations.list.detailed"/></a></td>--%>
		</tr>

		<tr valign="middle" class="cols_1">
			<td class="col" align="right">5</td>
			<td class="col" nowrap="nowrap"><input type="radio"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.creation_date"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.address"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.fio"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.summ"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.pay_summ"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.change"/></td>
			<%--<td class="col" nowrap="nowrap"><a href="#"><s:text name="payments.operations.list.detailed"/></a></td>--%>
		</tr>

		<tr valign="middle" class="cols_1">
			<td class="col" align="right">6</td>
			<td class="col" nowrap="nowrap"><input type="radio"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.creation_date"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.address"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.fio"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.summ"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.pay_summ"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.change"/></td>
			<%--<td class="col" nowrap="nowrap"><a href="#"><s:text name="payments.operations.list.detailed"/></a></td>--%>
		</tr>

		<tr valign="middle" class="cols_1">
			<td class="col" align="right">7</td>
			<td class="col" nowrap="nowrap"><input type="radio"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.creation_date"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.address"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.fio"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.summ"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.pay_summ"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.change"/></td>
			<%--<td class="col" nowrap="nowrap"><a href="#"><s:text name="payments.operations.list.detailed"/></a></td>--%>
		</tr>

		<tr valign="middle" class="cols_1">
			<td class="col" align="right">8</td>
			<td class="col" nowrap="nowrap"><input type="radio"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.creation_date"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.address"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.fio"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.summ"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.pay_summ"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.change"/></td>
			<%--<td class="col" nowrap="nowrap"><a href="#"><s:text name="payments.operations.list.detailed"/></a></td>--%>
		</tr>

		<tr valign="middle" class="cols_1">
			<td class="col" align="right">9</td>
			<td class="col" nowrap="nowrap"><input type="radio"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.creation_date"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.address"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.fio"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.summ"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.pay_summ"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.change"/></td>
			<%--<td class="col" nowrap="nowrap"><a href="#"><s:text name="payments.operations.list.detailed"/></a></td>--%>
		</tr>

		<tr valign="middle" class="cols_1">
			<td class="col" align="right">10</td>
			<td class="col" nowrap="nowrap"><input type="radio"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.creation_date"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.address"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.fio"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.summ"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.pay_summ"/></td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.operation3.change"/></td>
			<%--<td class="col" nowrap="nowrap"><a href="#"><s:text name="payments.operations.list.detailed"/></a></td>--%>
		</tr>

		<tr>
			<td colspan="9">
				<%@include file="/WEB-INF/jsp/common/filter/pager/pager.jsp" %>
			</td>
		</tr>

	</table>

	<s:submit name="submitted" cssClass="btn-exit" value="%{getText('payments.operations.list.submit')}"/>

	<input type="button" class="btn-exit" value="<s:text name="payments.operations.list.detailed"/>"/>
</s:form>