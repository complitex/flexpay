<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<s:form action="edit_street_type" method="post">

		<tr>
			<td class="th" width="100%" colspan="3" align="center">
				<s:text name="ab.from" />&nbsp;
				<s:textfield name="date" id="date" value="%{date}"/>
				<img src="<s:url value="/resources/common/js/jscalendar/img.gif"/>" alt=""
					 id="trigger_from"
					 style="cursor: pointer; border: 1px solid red;"
					 title="<s:text name="common.calendar"/>"
					 onmouseover="this.style.background='red';"
					 onmouseout="this.style.background=''"/>
				<script type="text/javascript">
					Calendar.setup({
						inputField	 : "date",
						ifFormat	 : "%Y/%m/%d",
						button		 : "trigger_from",
						align		 : "Tl",
						singleClick  : true
					});
				</script>
				&nbsp;
				<%@include file="filters/street_type_filter.jsp"%>
			</td>
		</tr>

		<tr>
			<td colspan="3" height="3" bgcolor="#4a4f4f"/>
		<tr>
			<td colspan="3">
				<input type="submit" class="btn-exit"
					   value="<s:text name="common.save"/>"/>
			</td>
		</tr>
		<s:hidden name="temporal.id" value="%{temporal.id}" />
		<s:hidden name="object.id" value="%{object.id}" />
	</s:form>
</table>
