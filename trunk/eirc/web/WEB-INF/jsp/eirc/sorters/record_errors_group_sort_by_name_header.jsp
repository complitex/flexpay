<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<script type="text/javascript">FP.sorters[FP.sorters.length] = "recordErrorsGroupSorterByNameActive";</script>

<s:hidden name="recordErrorsGroupSorterByName.active" id="recordErrorsGroupSorterByNameActive"/>
<s:hidden name="recordErrorsGroupSorterByName.order" id="recordErrorsGroupSorterByNameOrder" />

<input type="button" class="btn-link" id="recordErrorsGroupSorterByNameButton"
       onclick="<s:if test="recordErrorsGroupSorterByName.activated">$('#recordErrorsGroupSorterByNameOrder').val('<s:property value="recordErrorsGroupSorterByName.oppositeOrder" />');</s:if>FP.activateSorter('recordErrorsGroupSorterByNameActive');" value="<s:text name="eirc.registry.record.error_group.name" />" />
<s:if test="recordErrorsGroupSorterByName.activated">
	<s:if test="recordErrorsGroupSorterByName.desc">
		<img src="<s:url value="/resources/common/img/i_arrow_up.gif" />" alt="" title="<s:text name="common.sort.asc" />">
	</s:if><s:else>
		<img src="<s:url value="/resources/common/img/i_arrow_down.gif" />" alt="" title="<s:text name="common.sort.desc" />">
	</s:else>
</s:if>
