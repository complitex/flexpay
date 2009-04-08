<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@include file="/WEB-INF/jsp/payments/include/stylesheet.jsp"%>

<s:actionmessage/>

<s:form action="generateRegistry">
	<table>
		<tr>
			<td nowrap="nowrap">
				<s:text name="payments.registry.generate.generate_for"/>
			</td>
			
			<td>
				<select>
					<option selected="selected"><s:text name="payments.demo.data.months.mar2009"/></option>
					<option><s:text name="payments.demo.data.months.feb2009"/></option>
					<option><s:text name="payments.demo.data.months.jan2009"/></option>
					<option><s:text name="payments.demo.data.months.dec2008"/></option>
				</select>
			</td>

			<td>
				<s:submit name="submitted" cssClass="btn-exit" value="%{getText('payments.registry.generate.generate')}"/>
			</td>
		</tr>
	</table>
</s:form>