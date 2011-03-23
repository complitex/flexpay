<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<script type="text/javascript">FP.sorters[FP.sorters.length] = "eircAccountSorterByAccountNumberActive";</script>

<s:hidden name="eircAccountSorterByAccountNumber.active" id="eircAccountSorterByAccountNumberActive"/>
<s:hidden name="eircAccountSorterByAccountNumber.order" id="eircAccountSorterByAccountNumberOrder" />

<input type="button" class="btn-link" id="eircAccountSorterByAccountNumberButton"
       onclick="<s:if test="eircAccountSorterByAccountNumber.activated">$('#eircAccountSorterByAccountNumberOrder').val('<s:property value="eircAccountSorterByAccountNumber.oppositeOrder" />');</s:if>FP.activateSorter('eircAccountSorterByAccountNumberActive');" value="<s:text name="eirc.eirc_account" />" />
<s:if test="eircAccountSorterByAccountNumber.activated">
	<s:if test="eircAccountSorterByAccountNumber.desc">
		<img src="<s:url value="/resources/common/img/i_arrow_up.gif" />" alt="" title="<s:text name="common.sort.asc" />">
	</s:if><s:else>
		<img src="<s:url value="/resources/common/img/i_arrow_down.gif" />" alt="" title="<s:text name="common.sort.desc" />">
	</s:else>
</s:if>
