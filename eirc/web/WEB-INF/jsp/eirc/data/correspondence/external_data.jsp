<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table cellpadding="3" cellspacing="0" border="0" width="100%">
	<s:iterator value="elementList">
		<tr>
			<td style="border-top: 1px solid #dae1e1;"><input
					type="checkbox" name="external_source" value="<s:property value="top" />"
					class="form"/></td>
			<td style="border-top: 1px solid #dae1e1;" width="100%"><p class="text">
				<s:property value="top"/></p></td>
			<td style="border-top: 1px solid #dae1e1;">
				<a href="#">
					<!-- <img src="<s:url value='/resources/common/img/i_edit.gif' />"
								 hspace="10" width="16" height="16" alt=""
								 border="0"/> -->
					<s:text name="common.edit"/> 			 
				</a>
				<a href="#">
					<!-- <img src="<s:url value='/resources/common/img/i_view.gif' />" width="16"
					height="16" alt="" border="0"/> -->
					<s:text name="common.view"/>
				</a>
			</td>
		</tr>
	</s:iterator>

	<tr class="cols_1">
		<td class="col" width="100%" colspan="4" align="center">
			<s:include value="/WEB-INF/jsp/ab/filters/pager.jsp"/>
		</td>
	</tr>


</table>
