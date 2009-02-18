<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<ul class="folders">
	<li class="open"><strong><s:text name="menu2.address_dictionaries" /></strong>
		<ul class="docs">
			<li>
				<a href="<s:url namespace="/dicts" action='countriesList' includeParams="none" />"><s:text
						name="menu34.dictionaries.countries" /></a></li>
			<li>
				<a href="<s:url namespace="/dicts" action='regionsList' includeParams="none" />"><s:text
						name="menu34.dictionaries.regions" /></a></li>
			<li>
				<a href="<s:url namespace="/dicts" action='townsList' includeParams="none" />"><s:text
						name="menu34.dictionaries.towns" /></a></li>
			<li>
				<a href="<s:url namespace="/dicts" action='districtsList' includeParams="none" />"><s:text
						name="menu34.dictionaries.districts" /></a></li>
			<li>
				<a href="<s:url namespace="/dicts" action='streetsList' includeParams="none" />"><s:text
						name="menu34.dictionaries.streets" /></a></li>
			<li>
				<a href="<s:url namespace="/dicts" action='buildingsList' includeParams="none"/>"><s:text
						name="menu34.dictionaries.buildings" /></a></li>
			<li>
				<a href="<s:url namespace="/dicts" action='apartmentsList' includeParams="none"/>"><s:text
						name="menu34.dictionaries.apartments" /></a></li>
		</ul>
	</li>
	<li class="open"><strong><s:text name="menu2.person_dictionaries" /></strong>
		<ul class="docs">
			<li>
				<a href="<s:url namespace="/dicts" action='personsList' includeParams="none" />"><s:text
						name="menu34.dictionaries.persons" /></a></li>
		</ul>
	</li>
	<li class="open"><strong><s:text name="menu2.type_dictionaries" /></strong>
		<ul class="docs">
			<li>
				<a href="<s:url namespace="/dicts" action='townTypesList' includeParams="none" />"><s:text
						name="menu34.dictionaries.town_types" /></a></li>
			<li>
				<a href="<s:url namespace="/dicts" action='streetTypesList' includeParams="none" />"><s:text
						name="menu34.dictionaries.street_types" /></a></li>
			<li>
				<a href="<s:url namespace="/dicts" action='identityTypesList' includeParams="none" />"><s:text
						name="menu34.dictionaries.identity_types" /></a></li>
			<li>
				<a href="<s:url namespace="/dicts" action='addressAttributeTypesList' includeParams="none" />"><s:text
						name="ab.buildings.attribute_types" /></a></li>
		</ul>
	</li>
	<li class="open"><strong><s:text name="menu2.other_dictionaries" /></strong>
		<ul class="docs">
			<li>
				<a href="<s:url namespace="/dicts" action='measureUnitsList' includeParams="none" />"><s:text
						name="menu34.dictionaries.measure_units" /></a></li>
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
