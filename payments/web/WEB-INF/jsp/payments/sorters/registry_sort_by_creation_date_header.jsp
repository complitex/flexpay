<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<script type="text/javascript">FP.sorters[FP.sorters.length] = "registrySorterByCreationDateActive";</script>

<s:hidden name="registrySorterByCreationDate.active" id="registrySorterByCreationDateActive"/>
<s:hidden name="registrySorterByCreationDate.order" id="registrySorterByCreationDateOrder" />

<input type="button" class="btn-link" id="registrySorterByCreationDateButton"
       onclick="<s:if test="registrySorterByCreationDate.activated">$('#registrySorterByCreationDateOrder').val('<s:property value="registrySorterByCreationDate.oppositeOrder" />');</s:if>FP.activateSorter('registrySorterByCreationDateActive');" value="<s:text name="eirc.date" />" />
<s:if test="registrySorterByCreationDate.activated">
	<s:if test="registrySorterByCreationDate.desc">
		<img src="<s:url value="/resources/common/img/i_arrow_up.gif" />" alt="" title="<s:text name="common.sort.asc" />">
	</s:if><s:else>
		<img src="<s:url value="/resources/common/img/i_arrow_down.gif" />" alt="" title="<s:text name="common.sort.desc" />">
	</s:else>
</s:if>
