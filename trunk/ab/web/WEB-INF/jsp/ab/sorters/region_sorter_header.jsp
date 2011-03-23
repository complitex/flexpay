<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<script type="text/javascript">FP.sorters[FP.sorters.length] = "regionSorterActive";</script>

<s:hidden name="regionSorter.active" id="regionSorterActive" />
<s:hidden name="regionSorter.order" id="regionSorterOrder" />
<input type="button" class="btn-link" id="regionSorterButton"
       onclick="<s:if test="regionSorter.activated">$('#regionSorterOrder').val('<s:property value="regionSorter.oppositeOrder" />');</s:if>FP.activateSorter('regionSorterActive');" value="<s:text name="ab.region" />" />
<s:if test="regionSorter.activated">
	<s:if test="regionSorter.desc"><img
			src="<s:url value="/resources/common/img/i_arrow_up.gif" />"
			alt="" title="<s:text name="common.sort.asc" />">
	</s:if><s:else><img
			src="<s:url value="/resources/common/img/i_arrow_down.gif" />"
			alt="" title="<s:text name="common.sort.desc" />">
	</s:else>
</s:if>
