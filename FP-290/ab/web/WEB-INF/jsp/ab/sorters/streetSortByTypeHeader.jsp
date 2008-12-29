<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<s:hidden name="streetSorterByType.active" id="streetSorterByType.active" />
<s:hidden name="streetSorterByType.order" id="streetSorterByType.order" />
<script type="text/javascript">FP.sorters[FP.sorters.length] = 'streetSorterByType.active';</script>
<input type="submit" class="btn-link" onclick="<s:if test="streetSorterByType.activated">$('streetSorterByType.order').value='<s:property value="%{streetSorterByType.oppositeOrder}" />';</s:if>FP.activateSorter('streetSorterByType.active');" value="<s:text name="ab.street_type"/>"/>
<s:if test="streetSorterByType.active">
	<s:if test="streetSorterByType.desc"><img
			src="<s:url value="/resources/common/img/i_arrow_up.gif" includeParams="none" />"
			alt="" title="<s:text name="common.sort.asc" />">
	</s:if><s:else><img
			src="<s:url value="/resources/common/img/i_arrow_down.gif" includeParams="none" />"
			alt="" title="<s:text name="common.sort.desc" />">
	</s:else>
</s:if>