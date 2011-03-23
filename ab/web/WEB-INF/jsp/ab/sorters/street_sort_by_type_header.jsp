<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<script type="text/javascript">FP.sorters[FP.sorters.length] = "streetSorterByTypeActive";</script>

<s:hidden name="streetSorterByType.active" id="streetSorterByTypeActive" />
<s:hidden name="streetSorterByType.order" id="streetSorterByTypeOrder" />
<input type="button" class="btn-link" id="streetSorterByTypeButton" 
       onclick="<s:if test="streetSorterByType.activated">$('#streetSorterByTypeOrder').val('<s:property value="streetSorterByType.oppositeOrder" />');</s:if>FP.activateSorter('streetSorterByTypeActive');" value="<s:text name="ab.street_type" />" />
<s:if test="streetSorterByType.activated">
	<s:if test="streetSorterByType.desc"><img
			src="<s:url value="/resources/common/img/i_arrow_up.gif" />"
			alt="" title="<s:text name="common.sort.asc" />">
	</s:if><s:else><img
			src="<s:url value="/resources/common/img/i_arrow_down.gif" />"
			alt="" title="<s:text name="common.sort.desc" />">
	</s:else>
</s:if>