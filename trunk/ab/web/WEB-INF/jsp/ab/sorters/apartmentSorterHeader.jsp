
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<s:hidden name="apartmentSorter.active" id="apartmentSorterActive" />
<s:hidden name="apartmentSorter.order" id="apartmentSorterOrder" />
<script type="text/javascript">FP.sorters[FP.sorters.length] = 'apartmentSorterActive';</script>
<input type="submit" class="btn-link" onclick="<s:if test="apartmentSorter.activated">$('#apartmentSorterOrder').val('<s:property value="%{apartmentSorter.oppositeOrder}" />');</s:if>FP.activateSorter('apartmentSorterActive');" value="<s:text name="ab.apartment"/>"/>
<s:if test="apartmentSorter.activated">
	<s:if test="apartmentSorter.desc"><img
			src="<s:url value="/resources/common/img/i_arrow_up.gif" includeParams="none" />"
			alt="" title="<s:text name="common.sort.asc" />">
	</s:if><s:else><img
			src="<s:url value="/resources/common/img/i_arrow_down.gif" includeParams="none" />"
			alt="" title="<s:text name="common.sort.desc" />">
	</s:else>
</s:if>
