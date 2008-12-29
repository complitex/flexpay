<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<s:hidden name="streetSorterByName.active" id="streetSorterByName.active" />
<s:hidden name="streetSorterByName.order" id="streetSorterByName.order" />
<script type="text/javascript">FP.sorters[FP.sorters.length] = 'streetSorterByName.active';</script>
<input type="submit" class="btn-link" onclick="<s:if test="streetSorterByName.activated">$('streetSorterByName.order').value='<s:property value="%{streetSorterByName.oppositeOrder}" />';</s:if>FP.activateSorter('streetSorterByName.active');" value="<s:text name="ab.street"/>"/>
<s:if test="streetSorterByName.activated">
	<s:if test="streetSorterByName.desc"><img
			src="<s:url value="/resources/common/img/i_arrow_up.gif" includeParams="none" />"
			alt="" title="<s:text name="common.sort.asc" />">
	</s:if><s:else><img
			src="<s:url value="/resources/common/img/i_arrow_down.gif" includeParams="none" />"
			alt="" title="<s:text name="common.sort.desc" />">
	</s:else>
</s:if>