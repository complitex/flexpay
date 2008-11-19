<%@ taglib prefix="s" uri="/struts-tags" %>


<s:form action="spFileCreate" method="POST" enctype="multipart/form-data">

	<table>
		<tr>
			<td><s:text name="eirc.file" /></td>
			<td><s:file name="upload" label="File" id="upload" /></td>
		</tr>
		<tr>
			<td colspan="2">&nbsp</td>
		</tr>

		<tr>
			<td colspan="2">
				<s:submit name="submitted" value="%{getText('common.upload')}" cssClass="btn-exit"
						  onclick='startUpload();' />
				<font color="red" id="ajaxResponse"></font>
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<s:if test="%{uploaded & submitted}">
					<s:text name="eirc.successfullUpload" />
				</s:if>
			</td>
		</tr>
	</table>

</s:form>


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

	function getProgress() {
		var url = "/eirc/common/fileUploadProgress.action";
		xmlHttp.open("POST", url, true);
		xmlHttp.onreadystatechange = updatePage;
		xmlHttp.send(null);

	}

	function updatePage() {
		if (xmlHttp.readyState == 4) {
			document.getElementById('ajaxResponse').innerHTML = 'Loaded ' + xmlHttp.responseText + '%';
			setTimeout(getProgress, 1000);
		}
	}

	function startUpload() {
		//document.getElementById('submit').disabled = 'true';
		setTimeout(getProgress, 2000);
	}

</script>
