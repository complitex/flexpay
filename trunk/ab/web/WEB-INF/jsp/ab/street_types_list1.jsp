<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>


<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<form id="ftypes" method="post" action="">
		<tr>
			<td class="th">&nbsp;</td>
			<td class="th"><input type="checkbox"
								  onchange="FP.setCheckboxes(this.checked, 'streetTypeIds')">
			</td>
			<td class="th"><spring:message code="ab.town_type_name"/></td>
			<td class="th">&nbsp;</td>
		</tr>
		<s:iterator value="translations">
		  <tr>
		    <td>
		      #
		    </td>
		    <td>
		      <s:checkbox name="name" value="streetTypeIds"/>
		    </td>
		    <td>
		      <s:property value="name"/>
		    </td>
		    <td>
		      edit url
		    </td>
		  </tr>
		</s:iterator>
		<tr>
			<td colspan="0">
				<input type="submit" class="btn-exit"
					   onclick="$('ftypes').action='<c:url value="/dicts/delete_town_types.action"/>';$('ftypes').submit()"
					   value="<spring:message code="common.delete_selected"/>"/>
				<input type="button" class="btn-exit"
					   onclick="window.location='<c:url value="/dicts/create_town_type.action"/>'"
					   value="<spring:message code="common.new"/>"/>
			</td>
		</tr>
	</form>
</table>