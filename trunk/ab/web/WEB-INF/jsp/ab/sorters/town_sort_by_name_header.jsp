<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<script type="text/javascript">FP.sorters[FP.sorters.length] = "townSorterByNameActive";</script>

<s:hidden name="townSorterByName.active" id="townSorterByNameActive" />
<s:hidden name="townSorterByName.order" id="townSorterByNameOrder" />
<input type="button" class="btn-link" id="townSorterByNameButton"
       onclick="<s:if test="townSorterByName.activated">$('#townSorterByNameOrder').val('<s:property value="townSorterByName.oppositeOrder" />');</s:if>FP.activateSorter('townSorterByNameActive');" value="<s:text name="ab.town" />" />
<s:if test="townSorterByName.activated">
	<s:if test="townSorterByName.desc"><img
			src="<s:url value="/resources/common/img/i_arrow_up.gif" />"
			alt="" title="<s:text name="common.sort.asc" />">
	</s:if><s:else><img
			src="<s:url value="/resources/common/img/i_arrow_down.gif" />"
			alt="" title="<s:text name="common.sort.desc" />">
	</s:else>
</s:if>
