<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<script type="text/javascript">FP.sorters[FP.sorters.length] = "processSorterByStateActive";</script>

<s:hidden name="processSorterByState.active" id="processSorterByStateActive"/>
<s:hidden name="processSorterByState.order" id="processSorterByStateOrder" />

<input type="button" class="btn-link" id="processSorterByStateButton"
       onclick="<s:if test="processSorterByState.activated">$('#processSorterByStateOrder').val('<s:property value="processSorterByState.oppositeOrder" />');</s:if>FP.activateSorter('processSorterByStateActive');" value="<s:text name="common.processing.process.state"/>"/>
<s:if test="processSorterByState.activated">
	<s:if test="processSorterByState.desc">
		<img src="<s:url value="/resources/common/img/i_arrow_up.gif" includeParams="none" />" alt="" title="<s:text name="common.sort.asc" />">
	</s:if><s:else>
		<img src="<s:url value="/resources/common/img/i_arrow_down.gif" includeParams="none" />" alt="" title="<s:text name="common.sort.desc" />">
	</s:else>
</s:if>
