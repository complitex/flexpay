<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<script type="text/javascript">FP.sorters[FP.sorters.length] = "processSorterByStartDateActive";</script>

<s:hidden name="processSorterByStartDate.active" id="processSorterByStartDateActive"/>
<s:hidden name="processSorterByStartDate.order" id="processSorterByStartDateOrder" />

<input type="button" class="btn-link" id="processSorterByStartDateButton"
       onclick="<s:if test="processSorterByStartDate.activated">$('#processSorterByStartDateOrder').val('<s:property value="processSorterByStartDate.oppositeOrder" />');</s:if>FP.activateSorter('processSorterByStartDateActive');" value="<s:text name="common.processing.process.start_date"/>"/>
<s:if test="processSorterByStartDate.activated">
	<s:if test="processSorterByStartDate.desc">
		<img src="<s:url value="/resources/common/img/i_arrow_up.gif" includeParams="none" />" alt="" title="<s:text name="common.sort.asc" />">
	</s:if><s:else>
		<img src="<s:url value="/resources/common/img/i_arrow_down.gif" includeParams="none" />" alt="" title="<s:text name="common.sort.desc" />">
	</s:else>
</s:if>
