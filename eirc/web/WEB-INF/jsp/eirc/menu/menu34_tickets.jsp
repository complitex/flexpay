<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<ul class="folders">
	<li class="open"><b><s:text name="eirc.menu2.processing"/></b>
		<ul class="docs">
			<li><a href="<c:url value='/eirc/printTickets.action' />"><s:text name="eirc.menu34.tickets.print_tickets"/></a></li>
		</ul>
		<ul class="docs">
			<li><a href="<c:url value='/eirc/ticketPayForm.action' />"><s:text name="eirc.menu34.tickets.pay_ticket"/></a></li>
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
