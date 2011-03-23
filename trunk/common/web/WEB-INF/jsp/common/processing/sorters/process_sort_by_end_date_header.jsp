<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<script type="text/javascript">FP.sorters[FP.sorters.length] = "processSorterByEndDateActive";</script>

<s:hidden name="processSorterByEndDate.active" id="processSorterByEndDateActive"/>
<s:hidden name="processSorterByEndDate.order" id="processSorterByEndDateOrder" />

<input type="button" class="btn-link" id="processSorterByEndDateButton"
       onclick="<s:if test="processSorterByEndDate.activated">$('#processSorterByEndDateOrder').val('<s:property value="processSorterByEndDate.oppositeOrder" />');</s:if>FP.activateSorter('processSorterByEndDateActive');" value="<s:text name="common.processing.process.end_date"/>"/>
<s:if test="processSorterByEndDate.activated">
	<s:if test="processSorterByEndDate.desc">
		<img src="<s:url value="/resources/common/img/i_arrow_up.gif" />" alt="" title="<s:text name="common.sort.asc" />">
	</s:if><s:else>
		<img src="<s:url value="/resources/common/img/i_arrow_down.gif" />" alt="" title="<s:text name="common.sort.desc" />">
	</s:else>
</s:if>
