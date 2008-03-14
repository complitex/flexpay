<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<script type="text/javascript">
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

var internalSourcesXmlHttp = newXMLHttpRequest();
function sendInternalSourcesRequest() {
  buildingId =  2;
  var url = "<s:url value='/eirc/correspondence_internal.action' />?streetId=" + currentStreetId + "&buildingId=" + buildingId;
  internalSourcesXmlHttp.open("GET", url, true);
  internalSourcesXmlHttp.onreadystatechange = setInternalSources;
  internalSourcesXmlHttp.send(null);
}
function setInternalSources() {
  if(internalSourcesXmlHttp.readyState == 4) { 
      internalSourcesDiv = document.getElementById('internal_sources');
      internalSourcesDiv.innerHTML = internalSourcesXmlHttp.responseText;;
   }
}
</script>

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
		          <select name="buildings" id="buildings" onChange="sendInternalSourcesRequest()">
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
		<!-- 
			<iframe id="internalFrame" src="<s:url value='/eirc/correspondence_internal.action' />" style="overflow-x: hidden; owerflow-y: scroll; border: 1px solid #eee; width: 100%; height: 100%;" /></iframe>
		 -->
		 <div id=internal_sources style="border: 1px solid #eee; overflow:auto; height:200px">
		   
           
		 </div>	        			
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
		varname:"streetVar",
		json:true,
		shownoresults:false,
		maxresults:6,
		timeout:10000,
		callback: sendBuildingRequest
	};
	var as_json = new bsn.AutoSuggest('street', options);

var xmlHttp = newXMLHttpRequest();

var currentStreetId;

function sendBuildingRequest(obj) {
  currentStreetId = obj.id;
  var url = "<s:url value='/eirc/building_ajax.action' />?streetId=" + currentStreetId;
  xmlHttp.open("GET", url, true);
  xmlHttp.onreadystatechange = setBuildings;
  xmlHttp.send(null);  
}

function setBuildings() { 
   if(xmlHttp.readyState == 4) { 
      buildingsJson = eval('(' + xmlHttp.responseText + ')');
      buildingArray = buildingsJson.results;
      buildingSelectElem = document.getElementById('buildings');
      clearSelect(buildingSelectElem);
      buildingSelectElem.options[0] = new Option('---', '---');
      for(i = 0; i < buildingArray.length; i++) {
        option = new Option(buildingArray[i].value, buildingArray[i].id)
        buildingSelectElem.options[buildingSelectElem.options.length] = option;
   }
}
    
function clearSelect(selectElem) {
    while (selectElem.childNodes.length) {
        // Данный if только для Opera-ы, которая не удаляет
        // optgroup, если в нем есть элементы.
        if (selectElem.firstChild.tagName == 'OPTGROUP') {
            while (selectElem.firstChild.childNodes.length) {
                selectElem.firstChild.removeChild(selectElem.firstChild.firstChild);
            }
        }
        selectElem.removeChild(selectElem.firstChild);
    }
    return true;
}
   
}

</script>