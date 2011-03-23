<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<script type="text/javascript">FP.sorters[FP.sorters.length] = "operationSorterByIdActive";</script>

<s:hidden name="operationSorterById.active" id="operationSorterByIdActive"/>
<s:hidden name="operationSorterById.order" id="operationSorterByIdOrder" />

<input type="button" class="btn-link" id="operationSorterByIdButton"
       onclick="<s:if test="operationSorterById.activated">$('#operationSorterByIdOrder').val('<s:property value="operationSorterById.oppositeOrder" />');</s:if>FP.activateSorter('operationSorterByIdActive');" value="<s:text name="payments.operations.list.uno"/>"/>
<s:if test="operationSorterById.activated">
	<s:if test="operationSorterById.desc">
		<img src="<s:url value="/resources/common/img/i_arrow_up.gif" />" alt="" title="<s:text name="common.sort.asc" />">
	</s:if><s:else>
		<img src="<s:url value="/resources/common/img/i_arrow_down.gif" />" alt="" title="<s:text name="common.sort.desc" />">
	</s:else>
</s:if>
