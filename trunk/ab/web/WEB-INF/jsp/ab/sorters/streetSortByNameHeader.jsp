<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<s:hidden name="streetSorterByName.active" id="streetSorterByNameActive" />
<s:hidden name="streetSorterByName.order" id="streetSorterByNameOrder" />
<script type="text/javascript">FP.sorters[FP.sorters.length] = 'streetSorterByNameActive';</script>
<input type="submit" class="btn-link" onclick="<s:if test="streetSorterByName.activated">$('#streetSorterByNameOrder').val('<s:property value="%{streetSorterByName.oppositeOrder}" />');</s:if>FP.activateSorter('streetSorterByNameActive');" value="<s:text name="ab.street"/>"/>
<s:if test="streetSorterByName.activated">
	<s:if test="streetSorterByName.desc"><img
			src="<s:url value="/resources/common/img/i_arrow_up.gif" includeParams="none" />"
			alt="" title="<s:text name="common.sort.asc" />">
	</s:if><s:else><img
			src="<s:url value="/resources/common/img/i_arrow_down.gif" includeParams="none" />"
			alt="" title="<s:text name="common.sort.desc" />">
	</s:else>
</s:if>