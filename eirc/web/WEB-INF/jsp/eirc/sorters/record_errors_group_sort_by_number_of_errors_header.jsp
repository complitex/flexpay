<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<script type="text/javascript">FP.sorters[FP.sorters.length] = "recordErrorsGroupSorterByNumberOfErrorsActive";</script>

<s:hidden name="recordErrorsGroupSorterByNumberOfErrors.active" id="recordErrorsGroupSorterByNumberOfErrorsActive"/>
<s:hidden name="recordErrorsGroupSorterByNumberOfErrors.order" id="recordErrorsGroupSorterByNumberOfErrorsOrder" />

<input type="button" class="btn-link" id="recordErrorsGroupSorterByNumberOfErrorsButton"
       onclick="<s:if test="recordErrorsGroupSorterByNumberOfErrors.activated">$('#recordErrorsGroupSorterByNumberOfErrorsOrder').val('<s:property value="recordErrorsGroupSorterByNumberOfErrors.oppositeOrder" />');</s:if>FP.activateSorter('recordErrorsGroupSorterByNumberOfErrorsActive');" value="<s:text name="eirc.registry.record.error_group.number_of_errors" />" />
<s:if test="recordErrorsGroupSorterByNumberOfErrors.activated">
	<s:if test="recordErrorsGroupSorterByNumberOfErrors.desc">
		<img src="<s:url value="/resources/common/img/i_arrow_up.gif" includeParams="none" />" alt="" title="<s:text name="common.sort.asc" />">
	</s:if><s:else>
		<img src="<s:url value="/resources/common/img/i_arrow_down.gif" includeParams="none" />" alt="" title="<s:text name="common.sort.desc" />">
	</s:else>
</s:if>
