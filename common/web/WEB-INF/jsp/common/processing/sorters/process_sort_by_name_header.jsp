<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<script type="text/javascript">FP.sorters[FP.sorters.length] = "processSorterByNameActive";</script>

<s:hidden name="processSorterByName.active" id="processSorterByNameActive"/>
<s:hidden name="processSorterByName.order" id="processSorterByNameOrder" />

<input type="button" class="btn-link" id="processSorterByNameButton" 
       onclick="<s:if test="processSorterByName.activated">$('#processSorterByNameOrder').val('<s:property value="processSorterByName.oppositeOrder" />');</s:if>FP.activateSorter('processSorterByNameActive');" value="<s:text name="common.processing.process.name"/>"/>
<s:if test="processSorterByName.activated">
	<s:if test="processSorterByName.desc">
		<img src="<s:url value="/resources/common/img/i_arrow_up.gif" includeParams="none" />" alt="" title="<s:text name="common.sort.asc" />">
	</s:if><s:else>
		<img src="<s:url value="/resources/common/img/i_arrow_down.gif" includeParams="none" />" alt="" title="<s:text name="common.sort.desc" />">
	</s:else>
</s:if>
