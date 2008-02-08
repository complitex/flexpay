<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://www.davidjc.com/taglibs"     prefix="djc"     %>

<script type="text/javascript" src="/struts-blanc/struts/com/davidjc/javascript/jquery-1.2.1.min.js"></script>
<script type="text/javascript" src="/struts-blanc/struts/com/davidjc/javascript/ajaxfileupload.js"></script>
<script type="text/javascript" src="/struts-blanc/struts/com/davidjc/javascript/ajaxFileUploadProgress.js"></script>
<script type="text/javascript">
function newXMLHttpRequest() {

  var xmlreq = false;

  if (window.XMLHttpRequest) {

    // Создадим XMLHttpRequest объект для не-Microsoft браузеров
    xmlreq = new XMLHttpRequest();

  } else if (window.ActiveXObject) {

    // Создадим XMLHttpRequest с помощью MS ActiveX
    try {
      // Попробуем создать XMLHttpRequest для поздних версий
      // Internet Explorer

      xmlreq = new ActiveXObject("Msxml2.XMLHTTP");

    } catch (e1) {

      // Не удалось создать требуемый ActiveXObject

      try {
        // Пробуем вариант, который поддержат более старые версии
        //  Internet Explorer

        xmlreq = new ActiveXObject("Microsoft.XMLHTTP");

      } catch (e2) {

        // Не в состоянии создать XMLHttpRequest с помощью ActiveX
      }
    }
  }

  return xmlreq;
}

var xmlHttp = newXMLHttpRequest();
var i = 0;
var j = 0;

function callServer() { 
  var url = "/sz/sz/uploadprogress.action";

  xmlHttp.onreadystatechange = updatePage;
  
  xmlHttp.open("POST", url, true);

  xmlHttp.send("");
}

function updatePage() {
  document.getElementById('count').text = i; i++;
  
  if (xmlHttp.readyState == 4) {
    if (request.status == 200) {
      document.getElementById('countS').text = response; j++;  
      var response = xmlHttp.responseText;
      document.getElementById('message').text = response;
    }
  } 
  

  if(i < 1000) {
    callServer();
  }
}



	
</script>
<link rel="stylesheet" type="text/css" href="/struts-blanc/struts/com/davidjc/style/default.css" />




<s:form action="doUpload" method="POST" enctype="multipart/form-data" id="ajaxFileUploadForm" >

<table>
<tr>
  <td>
    <s:text name="year" />
  </td>
  <td>
    <s:select name="year" list="years" value="year" required="true" />
  </td>
</tr>

<tr>
  <td>
    <s:text name="month" />
  </td>
  <td>
    <s:select name="month" list="months" required="true" />
  </td>
</tr>

<tr>
  <td>
    <s:text name="sz.oszn" />
  </td>
  <td>
    <s:select name="osznId" list="osznList" listKey="id" listValue="description" required="true" />
  </td>
</tr>

<tr>
  <td>
    <s:text name="sz.file" />
  </td>
  <td>
    <s:file name="upload" label="File" id="upload" />
    
  </td>
</tr>

<tr>
  <td colspan="2">
    &nbsp
  </td>
</tr>

<tr>
  <td colspan="2">
    <s:submit name="submit" value="%{getText('common.upload')}" cssClass="btn-exit" id="ajaxFileUploadForm_0" onclick='callServer();' />
    <div id="message"></div>
    <div id="count"></div>
    <div id="countS"></div>
  </td>
</tr>    



</table>

</s:form>






