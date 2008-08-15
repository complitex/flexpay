<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<form id="fobjects" method="post" action="<s:url value="/eirc/eircAccountList.action" includeParams="none" />">
 
		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="1%"><input type="checkbox" disabled="1" onchange="FP.setCheckboxes(this.checked, 'objectIds')"></td>
			<td class="th" width="63%"><s:text name="eirc.eirc_account"/></td>
		</tr>
		<s:iterator value="%{eircAccountList}" status="status">
			<tr valign="middle" class="cols_1">
				<td class="col_1s" align="right"><s:property value="%{#status.index + pager.thisPageFirstElementNumber + 1}"/>
					&nbsp;
				</td>
				<td class="col">
					<input type="checkbox" disabled="1" value="<s:property value="%{id}"/>" name="objectIds"/>
				</td>
				<td class="col">
					<a href="<s:url action='eircAccountView'><s:param name="eircAccount.id" value="%{id}"/></s:url>">
	      				<s:property value="%{accountNumber}"/>
	    			</a>
				</td>
			</tr>
		</s:iterator>
		<tr>
			<td colspan="3">
				<%@include file="/WEB-INF/jsp/ab/filters/pager.jsp" %>
				<input type="button" class="btn-exit"
					   onclick="window.location='<s:url action="eircAccountCreateForm1"/>'"
					   value="<s:text name="common.new"/>"/>
			</td>
		</tr>
	</form>
</table>
