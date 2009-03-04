<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<ul class="folders">
	<li class="open"><span style="font-weight: bold;"><s:text name="eirc.menu2.tickets"/></span>
		<ul class="docs">
			<li><a href="<s:url action='quittanceGenerate' includeParams="none" />"><s:text name="eirc.menu34.quittances.generate_quittances"/></a></li>
			<li><a href="<s:url action='printQuittances' includeParams="none" />"><s:text name="eirc.menu34.tickets.print_tickets"/></a></li>
		</ul>
		<ul class="docs">
			<li><a href="<s:url action='quittancePayForm' includeParams="none" />"><s:text name="eirc.menu34.tickets.pay_ticket"/></a></li>
		</ul>
	</li>
	<li class="open"><span style="font-weight: bold;"><s:text name="eirc.quittance.packets"/></span>
		<ul class="docs">
			<li><a href="<s:url action='quittancePacketList' includeParams="none" />"><s:text name="eirc.quittance.packets"/></a></li>
		</ul>
	</li>
</ul>

<%--
<ul class="folders">
    <menu:useMenuDisplayer name="Simple" config="MenuStrings" bundle="struts.custom.i18n.resources">
        <menu:displayMenu name="L3Tickets" />
    </menu:useMenuDisplayer>
</ul>
--%>
