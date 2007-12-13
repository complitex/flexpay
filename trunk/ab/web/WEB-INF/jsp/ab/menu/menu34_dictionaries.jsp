<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<ul class="folders">
	<li class="open"><a href="123"><b>Dictionaries</b></a>
		<ul class="docs">
			<li><a href="<c:url value='/dicts/list_countries.action' />"><s:text
					name="menu34.dictionaries.countries"/></a></li>
			<li><a href="<c:url value='/dicts/list_regions.action' />"><s:text
					name="menu34.dictionaries.regions"/></a>
			</li>
			<li><a href="123"><s:text name="menu34.dictionaries.cities"/></a></li>
			<li><a href="<c:url value='/dicts/list_town_types.action' />"><s:text
					name="menu34.dictionaries.town_types"/></a></li>
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
