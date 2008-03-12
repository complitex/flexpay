<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>


<table cellpadding="0" cellspacing="0" border="0" width="100%" height="90%">
	<tr valign="top">
		<th>
			External sources        			
		</th>
		<th>
			Internal sources
		</th>
	</tr>
	<tr valign="top">
		<td>
			&nbsp;        			
		</td>
		<td>
			<table>
		      <tr>
		        <td>
		          Street
		        </td>
		        <td>
		          <input type="text" id="street" value="" />
		        </td>
		      </tr>
		      <tr>
		        <td>
		          Building
		        </td>
		        <td>
		          <select name="buildings" id="buildings">
                  </select>
		        </td>
		      </tr>  
		    </table>
		</td>
	</tr>
	
	<tr valign="top">
		<td style="padding-right: 10px; height:100%;">
			<iframe src="<s:url value='/eirc/correspondence_external.action' />" style="overflow-x: hidden; owerflow-y: scroll; border: 1px solid #eee; width: 100%; height: 100%;" /></iframe>        			
		</td>
		<td width="50%" style="height:100%;">
			<iframe id="internalFrame" src="<s:url value='/eirc/correspondence_internal.action' />" style="overflow-x: hidden; owerflow-y: scroll; border: 1px solid #eee; width: 100%; height: 100%;" /></iframe>        			
		</td>
	</tr>
	
	<tr>
		<td style="padding-top: 10px; padding-right: 10px; height:100%;" align="right">
		  <input type="button" value="<s:text name="common.set" />" class="btn-save" />
		</td>
		<td width="50%" style="padding-top: 10px; padding-right: 10px; height:100%;">
		  <input type="button" value="<s:text name="common.cancel" />" class="btn-cancel" />
		</td>
	</tr>
</table>




<script type="text/javascript">
	var options = {
		script:"<s:url value='/eirc/street_ajax.action' />?",
		varname:"input",
		json:true,
		shownoresults:false,
		maxresults:6,
		timeout:10000,
		callback: sendBuildingRequest
	};
	var as_json = new bsn.AutoSuggest('street', options);
	
function newXMLHttpRequest() {
  var xmlreq = false;
  if (window.XMLHttpRequest) {
    xmlreq = new XMLHttpRequest();
  } else if (window.ActiveXObject) {
    try {
      xmlreq = new ActiveXObject("Msxml2.XMLHTTP");
    } catch (e1) {
      try {
        xmlreq = new ActiveXObject("Microsoft.XMLHTTP");
      } catch (e2) {
      }
    }
  }
  return xmlreq;
}

var xmlHttp = newXMLHttpRequest();

function sendBuildingRequest(obj) {
  var url = "<s:url value='/eirc/building_ajax.action' />";
  xmlHttp.open("GET", url, true);
  xmlHttp.onreadystatechange = setBuildings;
  xmlHttp.send(null);  
}

function setBuildings() { 
   if(xmlHttp.readyState == 4) { 
      buildingsJson = eval('(' + xmlHttp.responseText + ')');
      buildingArray = buildingsJson.results;
      buildingSelectElem = document.getElementById('buildings');
      buildingSelectElem.options[0] = new Option('---', '---');
      for(i = 0; i < buildingArray.length; i++) {
        option = new Option(buildingArray[i].id, buildingArray[i].value)
        buildingSelectElem.options[buildingSelectElem.options.length] = option;
      }
      
      //document.getElementById('internalFrame').location.href = 'abrakadabra';
   }
}



</script>