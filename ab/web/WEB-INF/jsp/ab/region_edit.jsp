<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<form action="<c:url value='/dicts/edit_region.action'/>" method="post">

		<input type="hidden" name="id" value="<s:property value="id" />" />

		<tr>
			<td class="th" width="100%" colspan="3" align="center">
				<%@ include file="filters/country_filter.jsp" %>
			</td>
		</tr>

		<c:set scope="page" var="ids" value=""/>
		<c:forEach items="${requestScope['region_names']}" varStatus="st" var="rName">
			<c:if test="${rName.id != null}">
				<c:set scope="page" var="rNameId" value="${rName.id}"/>
			</c:if>
			<c:if test="${rName.id == null}">
				<c:set scope="page" var="rNameId" value="new_${st.index}"/>
			</c:if>
			<c:set scope="page" var="ids" value="${ids} ${rNameId}"/>
			<tr>
				<td class="th" width="100%" colspan="3" align="center">
					<s:text name="ab.from"/>
					<input type="text" name="<c:out value="from_${rNameId}" />"
						   id="<c:out value="from_${rNameId}" />"
							<c:if test="${rName.begin != null}">
								value="<dt:format pattern="yyyy/MM/dd" date="${rName.begin}"/>"
							</c:if>/>
					<img src="<c:url value="/js/jscalendar/img.gif"/>" alt=""
						 id="<c:out value="trigger_from_${rNameId}" />"
						 style="cursor: pointer; border: 1px solid red;"
						 title="<s:text name="common.calendar"/>"
						 onmouseover="this.style.background='red';"
						 onmouseout="this.style.background=''"/>
					&nbsp;&nbsp;
					<s:text name="ab.till"/>
					<input type="text" name="<c:out value="till_${rNameId}" />"
						   id="<c:out value="till_${rNameId}" />"
							<c:if test="${rName.end != null}">
								value="<dt:format pattern="yyyy/MM/dd" date="${rName.end}"/>"
							</c:if>/>
					<img src="<c:url value="/js/jscalendar/img.gif"/>" alt=""
						 id="<c:out value="trigger_till_${rNameId}" />"
						 style="cursor: pointer; border: 1px solid red;"
						 title="<s:text name="common.calendar"/>"
						 onmouseover="this.style.background='red';"
						 onmouseout="this.style.background=''"/>
					&nbsp;&nbsp;
					<input type="submit" class="btn-exit"
						   name="<c:out value="delete_${rNameId}" />"
						   value="<s:text name="ab.delete" />"/>
				</td>
				<script type="text/javascript">
					Calendar.setup({
						inputField	 : "<c:out value="from_${rNameId}" />",
						ifFormat	 : "%Y/%m/%d",
						button		 : "<c:out value="trigger_from_${rNameId}" />",
						align		 : "Tl",
						singleClick  : true
					});
					Calendar.setup({
						inputField	 : "<c:out value="till_${rNameId}" />",
						ifFormat	 : "%Y/%m/%d",
						button		 : "<c:out value="trigger_till_${rNameId}" />",
						align		 : "Tl",
						singleClick	 : true
					});
				</script>
			</tr>
			<tr>
				<td class="th" width="2%">&nbsp;</td>
				<td class="th" width="49%"><s:text name="ab.language"/></td>
				<td class="th" width="49%"><s:text name="ab.region_name"/></td>
			</tr>
			<c:forEach items="${rName.translations}" varStatus="status" var="rNameTrans">
				<tr valign="middle" class="cols_1">
					<td class="col_1s"><c:out value="${status.index + 1}"/></td>
					<td class="col"><c:out
							value="${rNameTrans.langTranslation.translation}"/></td>
					<td class="col"><input type="text"
										   name="<c:out value="name_${rNameId}_${rNameTrans.lang.id}" />"
										   value="<c:out value="${rNameTrans.name}" />"/>
					</td>
				</tr>
			</c:forEach>
		</c:forEach>
		<tr>
			<td colspan="3" height="3" bgcolor="#4a4f4f"/>
		<tr>
			<td colspan="3">
				<input type="submit" class="btn-exit" name="add_name"
					   value="<s:text name="ab.add"/>"/>
				<input type="submit" class="btn-exit"
					   value="<s:text name="common.save"/>"/>
			</td>
		</tr>
		<input type="hidden" name="ids" value="<c:out value="${ids}"/>"/>
	</form>
</table>
