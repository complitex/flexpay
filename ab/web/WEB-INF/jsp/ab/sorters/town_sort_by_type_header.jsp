<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<script type="text/javascript">FP.sorters[FP.sorters.length] = "townSorterByTypeActive";</script>

<s:hidden name="townSorterByType.active" id="townSorterByTypeActive" />
<s:hidden name="townSorterByType.order" id="townSorterByTypeOrder" />
<input type="button" class="btn-link" id="townSorterByTypeButton" 
       onclick="<s:if test="townSorterByType.activated">$('#townSorterByTypeOrder').val('<s:property value="townSorterByType.oppositeOrder" />');</s:if>FP.activateSorter('townSorterByTypeActive');" value="<s:text name="ab.town_type" />" />
<s:if test="townSorterByType.activated">
	<s:if test="townSorterByType.desc"><img
			src="<s:url value="/resources/common/img/i_arrow_up.gif" />"
			alt="" title="<s:text name="common.sort.asc" />">
	</s:if><s:else><img
			src="<s:url value="/resources/common/img/i_arrow_down.gif" />"
			alt="" title="<s:text name="common.sort.desc" />">
	</s:else>
</s:if>
