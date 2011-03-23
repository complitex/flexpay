<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<script type="text/javascript">FP.sorters[FP.sorters.length] = "buildingsSorterActive";</script>

<s:hidden name="buildingsSorter.active" id="buildingsSorterActive" />
<s:hidden name="buildingsSorter.order" id="buildingsSorterOrder" />
<input type="button" class="btn-link" id="buildingsSorterButton"
       onclick="<s:if test="buildingsSorter.activated">$('#buildingsSorterOrder').val('<s:property value="buildingsSorter.oppositeOrder" />');</s:if>FP.activateSorter('buildingsSorterActive');" value="<s:text name="ab.building" />" />
<s:if test="buildingsSorter.activated">
	<s:if test="buildingsSorter.desc"><img
			src="<s:url value="/resources/common/img/i_arrow_up.gif" />"
			alt="" title="<s:text name="common.sort.asc" />">
	</s:if><s:else><img
			src="<s:url value="/resources/common/img/i_arrow_down.gif" />"
			alt="" title="<s:text name="common.sort.desc" />">
	</s:else>
</s:if>
