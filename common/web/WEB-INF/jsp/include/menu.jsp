<%@include file="../common/taglibs.jsp"%>
<div class="left">
	<div class="left-padding">
		<div class="explorer">
			<ul class="folders">
				<li><a href="#"><spring:message code="menu.dictionaries"/></a>
					<ul class="docs">
						<li><a href="<c:url value="/dicts/list_countries.action"/>"><spring:message code="menu.countries"/></a></li>
						<li><a href="#"><spring:message code="menu.regions"/></a></li>
					</ul>
				</li>
			</ul>
		</div>
	</div>
</div>
