<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<script type="text/javascript">FP.sorters[FP.sorters.length] = "countrySorterActive";</script>

<s:hidden name="countrySorter.active" id="countrySorterActive" />
<s:hidden name="countrySorter.order" id="countrySorterOrder" />
<input type="button" class="btn-link" id="countrySorterButton"
       onclick="<s:if test="countrySorter.activated">$('#countrySorterOrder').val('<s:property value="countrySorter.oppositeOrder" />');</s:if>FP.activateSorter('countrySorterActive');" value="<s:text name="ab.country" />" />
<s:if test="countrySorter.activated">
	<s:if test="countrySorter.desc"><img
			src="<s:url value="/resources/common/img/i_arrow_up.gif" />"
			alt="" title="<s:text name="common.sort.asc" />">
	</s:if><s:else><img
			src="<s:url value="/resources/common/img/i_arrow_down.gif" />"
			alt="" title="<s:text name="common.sort.desc" />">
	</s:else>
</s:if>
