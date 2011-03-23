<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<script type="text/javascript">FP.sorters[FP.sorters.length] = "districtSorterActive";</script>

<s:hidden name="districtSorter.active" id="districtSorterActive" />
<s:hidden name="districtSorter.order" id="districtSorterOrder" />
<input type="button" class="btn-link" id="districtSorterButton"
       onclick="<s:if test="districtSorter.activated">$('#districtSorterOrder').val('<s:property value="districtSorter.oppositeOrder" />');</s:if>FP.activateSorter('districtSorterActive');" value="<s:text name="ab.district" />" />
<s:if test="districtSorter.activated">
	<s:if test="districtSorter.desc"><img
			src="<s:url value="/resources/common/img/i_arrow_up.gif" />"
			alt="" title="<s:text name="common.sort.asc" />">
	</s:if><s:else><img
			src="<s:url value="/resources/common/img/i_arrow_down.gif" />"
			alt="" title="<s:text name="common.sort.desc" />">
	</s:else>
</s:if>
