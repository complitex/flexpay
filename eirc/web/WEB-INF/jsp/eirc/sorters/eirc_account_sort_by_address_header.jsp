<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<script type="text/javascript">FP.sorters[FP.sorters.length] = "eircAccountSorterByAddressActive";</script>

<s:hidden name="eircAccountSorterByAddress.active" id="eircAccountSorterByAddressActive"/>
<s:hidden name="eircAccountSorterByAddress.order" id="eircAccountSorterByAddressOrder" />

<input type="button" class="btn-link" id="eircAccountSorterByAddressButton"
       onclick="<s:if test="eircAccountSorterByAddress.activated">$('#eircAccountSorterByAddressOrder').val('<s:property value="eircAccountSorterByAddress.oppositeOrder" />');</s:if>FP.activateSorter('eircAccountSorterByAddressActive');" value="<s:text name="eirc.eirc_account.address" />" />
<s:if test="eircAccountSorterByAddress.activated">
	<s:if test="eircAccountSorterByAddress.desc">
		<img src="<s:url value="/resources/common/img/i_arrow_up.gif" includeParams="none" />" alt="" title="<s:text name="common.sort.asc" />">
	</s:if><s:else>
		<img src="<s:url value="/resources/common/img/i_arrow_down.gif" includeParams="none" />" alt="" title="<s:text name="common.sort.desc" />">
	</s:else>
</s:if>
