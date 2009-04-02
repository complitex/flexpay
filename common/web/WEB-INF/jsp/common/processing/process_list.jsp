<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:form>

	<table cellpadding="3" cellspacing="1" border="0" width="100%">

		<tr>
			<td colspan="9">
				<s:text name="common.processing.process.filter.start_date"/>
				<%@include file="/WEB-INF/jsp/common/filter/begin_date_filter.jsp" %>

				<s:text name="common.processing.process.filter.end_date"/>
				<%@include file="/WEB-INF/jsp/common/filter/end_date_filter.jsp" %>

				<s:submit name="filtered" value="%{getText('common.show')}" cssClass="btn-exit"/>
			</td>
		</tr>

		<tr>
			<td colspan="9">
				<s:text name="common.processing.process.filter.state"/>
				<%@include file="/WEB-INF/jsp/common/processing/filters/process_state_filter.jsp" %>
			</td>
		</tr>

		<tr>
			<td colspan="9"><%@include file="../filter/pager/pager.jsp" %></td>
		</tr>

		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="1%"><input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');">
			</td>
			<td class="th"><s:text name="common.processing.process.id"/></td>

			<td class="<s:if test="processSorterByName.activated">th_s</s:if><s:else>th</s:else>" nowrap="nowrap">
				<%@ include file="sorters/process_sort_by_name_header.jsp" %>
			</td>

			<td class="<s:if test="processSorterByStartDate.activated">th_s</s:if><s:else>th</s:else>" nowrap="nowrap">
				<%@ include file="sorters/process_sort_by_start_date_header.jsp" %>
			</td>

			<td class="<s:if test="processSorterByEndDate.activated">th_s</s:if><s:else>th</s:else>" nowrap="nowrap">
				<%@ include file="sorters/process_sort_by_end_date_header.jsp" %>
			</td>

			<td class="th">
				<s:text name="common.processing.process.user"/>
			</td>

			<td class="<s:if test="processSorterByState.activated">th_s</s:if><s:else>th</s:else>" nowrap="nowrap">
				<%@ include file="sorters/process_sort_by_state_header.jsp" %>
			</td>

			<td class="th">
				<s:text name="common.processing.process.pause"/>
			</td>

			<s:iterator value="processList" status="status">
		<tr valign="middle" class="cols_1">
			<td class="col" width="1%"><s:property value="%{#status.index + 1}"/></td>
			<td class="col" width="1%">
				<input type="checkbox" name="objectIds" value="<s:property value="%{id}"/>"/>
			</td>
			<td class="col"><s:property value="id"/></td>
			<td class="col">
				<a href="<s:url action="processView"><s:param name="process.id" value="%{id}" /></s:url>">
					<s:text name="%{processDefinitionName}"/>
				</a>
			</td>
			<td class="col"><s:date name="processStartDate" format="yyyy/MM/dd"/></td>
			<td class="col"><s:date name="processEndDate" format="yyyy/MM/dd"/></td>
			<td class="col">&nbsp;</td>
			<td class="col"><s:property value="%{getTranslation(processState)}"/></td>
			<td class="col">&nbsp;</td>
		</tr>
		</s:iterator>

		<tr>
			<td colspan="9"><%@include file="../filter/pager/pager.jsp" %></td>
		</tr>

		<tr>
			<td colspan="2" align="center">
				<s:submit name="submitted" value="%{getText('ab.delete')}" cssClass="btn-exit"/>
			</td>
		</tr>

	</table>
</s:form>

	