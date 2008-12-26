<%@ taglib prefix="s" uri="/struts-tags" %>

<div id="mainBlock">

    <div id="copyBlock0">

        <s:form id="uploadForm0" action="doSzFileUploadAjax" method="POST" enctype="multipart/form-data" onsubmit="startUpload(0);" target="uploadFrame0" >
            <div>
                <span>
                    <s:text name="year" />
                    <s:select name="year" list="years" value="year" required="true" />
                </span>
                <span>
                    <s:text name="month" />
                    <s:select name="month" list="months" required="true" />
                </span>
                <span>
                    <s:text name="sz.oszn" />
                    <s:select name="osznId" list="osznList" listKey="id" listValue="description" required="true" />
                </span>
                <span>
                    <s:text name="sz.file" />
                    <s:file name="upload" label="File" />
                </span>
                <a href="javascript:void(0)" id="delBut0" idd="0" onclick="removeBlock(this);" style="font-size:10px;display:none;"><s:text name="remove_block" /></a>
            </div>
        </s:form>

        <iframe id="uploadFrame0" name="uploadFrame0" style="display:none;"></iframe>
        <font color="red" id="ajaxResponse0"></font>

    </div>

</div>

<script type="text/javascript">

    var newBlocks = 0;
    var mainBlock = "mainBlock";
    var copyBlock = "copyBlock";

    function submitAll() {
        $$('form[name="doSzFileUploadAjax"]').each(
                function (s) {
                    s.submit();
                }
            );
    }

    function addBlock() {
        newBlocks++;
        //clone
        var block = $(copyBlock + "0");

        var clone = block.cloneNode(1);
        clone.id = copyBlock + newBlocks;
        $(mainBlock).appendChild(clone);

        var removeBtn = $$('a[id="delBut0"]')[1];
        removeBtn.id = "delBut" + newBlocks;
        removeBtn.setAttribute("idd", newBlocks);
        removeBtn.style.display = "block";

        var uploadForm = $$('form[id="uploadForm0"]')[1];
        uploadForm.id = "uploadForm" + newBlocks;
        uploadForm.setAttribute("target", "uploadFrame" + newBlocks);
        uploadForm.setAttribute("onsubmit", "startUpload(" + newBlocks + ");");

        var uploadFrame = $$('iframe[id="uploadFrame0"]')[1];
        uploadFrame.id = "uploadFrame" + newBlocks;
        uploadFrame.setAttribute("name", "uploadFrame" + newBlocks);

        var statusEl = $$('font[id="ajaxResponse0"]')[1];
        statusEl.id = "ajaxResponse" + newBlocks;

    }

    function eraseValues() {

    }

    function removeBlock(btn) {
        var id = Number(btn.getAttribute("idd"));
        Element.Methods.remove("copyBlock" + id);
    }

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

    function getProgress(id) {
      var url = "/sz/common/fileUploadProgress.action";
      xmlHttp.open("POST", url, true);
      xmlHttp.onreadystatechange = updatePage(id);
      xmlHttp.send(null);
    }

    function updatePage(id) {
       if (xmlHttp.readyState == 4) {
           $("ajaxResponse" + id).innerHTML = 'Loaded ' + xmlHttp.responseText + '%';
           setTimeout(getProgress,1000);
       }
    }

    function startUpload(id) {
      //document.getElementById('submit').disabled = 'true';
      setTimeout(getProgress(id),2000);
    }
	
</script>
