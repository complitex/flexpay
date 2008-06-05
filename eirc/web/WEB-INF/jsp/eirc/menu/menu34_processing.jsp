<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<ul class="folders">
	<li class="open"><b><s:text name="eirc.menu2.processing"/></b>
		<ul class="docs">
			<li><a href="<c:url value='/eirc/generateTickets.action' />"><s:text name="eirc.menu34.processing.generate_tickets"/></a></li>
			<li><a href="<c:url value='/eirc/processListAction.action' />"><s:text name="eirc.menu34.processing.process_list"/></a></li>
		</ul>
		
	</li>
	

	<!--
		<li><a href="123123">Some folder</a>
			<ul class="docs">
				<li><a href="123">Some document</a></li>
			</ul>
		</li>
		-->

</ul>
