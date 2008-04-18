<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<script type="text/javascript">

	function sendInternalSourcesRequest() {
		alert("correspondence internal");
		new Ajax.Request("<s:url value='/eirc/correspondence_internal.action' />",
		{
			parameters : {streetId : currentStreetId, buildingId : 2},
			requestHeaders: {Accept: 'application/json'},
			onSuccess :	function (transport) {
				$('internal_sources').innerHTML = transport.responseText;
			}
		});
	}
</script>

<s:actionerror/>

<table cellpadding="0" cellspacing="0" border="0" width="100%" height="90%">
	<tr valign="top">
		<th>External sources</th>
		<th>Internal sources</th>
	</tr>
	<tr valign="top">
		<td>&nbsp;</td>
		<td>
			<table>
				<tr id="trShowExtAddress">
					<td colspan="2" onclick="$('trShowExtAddress').hide(); $w('trHideExtAddress trExtAddress').each(Element.show);">Show Country/Region/Town</td>
				</tr>
				<tr id="trHideExtAddress" style="display: none;">
					<td colspan="2" onclick="$('trShowExtAddress').show(); $w('trHideExtAddress trExtAddress').each(Element.hide);">Hide Country/Region/Town</td>
				</tr>
				<tr id="trExtAddress" style="display: none;">
					<td colspan="2">
						<%@ include file="/WEB-INF/jsp/ab/filters/country_filter.jsp" %>
						<%@ include file="/WEB-INF/jsp/ab/filters/region_filter.jsp" %>
						<%@ include file="/WEB-INF/jsp/ab/filters/town_filter.jsp" %>
					</td>
				</tr>
				<tr>
					<td><s:text name="ab.street"/>: <input type="text" id="street" value=""/></td>
					<td><s:text name="ab.building"/>: <select name="buildings" id="buildings"
								onChange="sendInternalSourcesRequest()"/></td>
				</tr>
			</table>
		</td>
	</tr>

	<tr valign="top">
		<td style="padding-right: 10px; height:100%;">
			<iframe src="<s:url value='/eirc/correspondence_external.action' />"
					style="overflow-x: hidden; overflow-y: hidden; border: 1px solid #eee; width: 100%; height: 100%;">
			</iframe>
		</td>
		<td width="50%" style="height:100%;">
			<div id=internal_sources
				 style="border: 1px solid #eee; overflow:auto; height:200px">
			</div>
		</td>
	</tr>

	<tr>
		<td style="padding-top: 10px; padding-right: 10px; height:100%;" align="right">
			<input type="button" value="<s:text name="common.set" />" class="btn-save"/>
		</td>
		<td width="50%" style="padding-top: 10px; padding-right: 10px; height:100%;">
			<input type="button" value="<s:text name="common.cancel" />"
				   class="btn-cancel"/>
		</td>
	</tr>
</table>


<script type="text/javascript">

	var currentStreetId;

	function sendBuildingRequest(obj) {
		currentStreetId = obj.id;
		new Ajax.Request("<s:url value='/eirc/building_ajax.action' />",
		{
			parameters : {streetId : currentStreetId},
			requestHeaders : {Accept: 'application/json'},
			onSuccess :	 function (transport) {
				$('buildings').childElements().each(
						function (el) {
							el.remove();
						});
				var buildingsJson = transport.responseText.evalJSON(true);
				var buildings = buildingsJson.results;

				$('buildings').options[$('buildings').options.length] = new Option('---', '---');
				for (var i = 0; i < buildings.length; i++) {
					$('buildings').options[$('buildings').options.length] = 
							new Option(buildings[i].value, buildings[i].id);
				}
			}
		});
	}

	var options = {
		script:"<s:url value='/eirc/street_ajax.action' />?",
		varname:"streetVar",
		json:true,
		shownoresults:true,
		minchars:3,
		maxresults:1000,
		timeout:10000,
		callback: sendBuildingRequest
	};
	var useBSNns = true;
	var as_json = new bsn.AutoSuggest('street', options);

</script>
