<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<ul class="folders">
	<li class="open"><b><s:text name="eirc.menu2.processing"/></b>
		<ul class="docs">
			<li><a href="<s:url value='/eirc/generateQuittances.action' includeParams="none" />"><s:text name="eirc.menu34.quittances.generate_quittances"/></a></li>
			<li><a href="<s:url value='/eirc/printTickets.action' includeParams="none" />"><s:text name="eirc.menu34.tickets.print_tickets"/></a></li>
		</ul>
		<ul class="docs">
			<li><a href="<s:url value='/eirc/ticketPayForm.action' includeParams="none" />"><s:text name="eirc.menu34.tickets.pay_ticket"/></a></li>
		</ul>
	</li>

</ul>
